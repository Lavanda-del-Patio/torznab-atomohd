package es.lavanda.torznab.atomohd.service.impl;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.lavanda.torznab.atomohd.model.dto.AtomohdObject;
import es.lavanda.torznab.atomohd.model.dto.input.FlaresolverrIDTO;
import es.lavanda.torznab.atomohd.model.dto.output.FlaresolverrODTO;
import es.lavanda.torznab.atomohd.service.ExternalPagesService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExternalPagesServiceImpl implements ExternalPagesService {

    private final String BT4ORG_URL = "https://bt4g.org/";

    private final String BT4ORG_WITHOUT_SLASH_URL = "https://bt4g.org";

    private final String FLARESOLVERR_URL = "http://localhost:8191/v1";

    private final String ATOMOHD_URL = "https://atomohd.care/";

    private final static String CATEGORY_TV = "tv-sonarr"; //FIXME:

    private final static String CATEGORY_FILM = "radarr"; //FIXME:

    private static final String TRACKERS = "&tr=udp://tracker.opentrackr.org:1337/announce&tr=udp://opentracker.i2p.rocks:6969/announce&tr=https://opentracker.i2p.rocks:443/announce&tr=udp://open.demonii.com:1337/announce&tr=http://tracker.openbittorrent.com:80/announce&tr=udp://tracker.openbittorrent.com:6969/announce&tr=udp://9.rarbg.com:2810/announce&tr=udp://open.stealth.si:80/announce&tr=udp://exodus.desync.com:6969/announce&tr=udp://tracker.torrent.eu.org:451/announce&tr=udp://tracker.moeking.me:6969/announce&tr=https://tracker.tamersunion.org:443/announce&tr=udp://tracker.bitsearch.to:1337/announce&tr=udp://movies.zsw.ca:6969/announce&tr=udp://explodie.org:6969/announce&tr=https://tracker.gbitt.info:443/announce&tr=http://tracker.gbitt.info:80/announce&tr=http://open.acgnxtracker.com:80/announce&tr=udp://tracker1.bt.moack.co.kr:80/announce&tr=udp://tracker.altrosky.nl:6969/announce&tr=https://tracker.bt4g.com:443/announce";
    private static final String MAGNET_LINK_INIT = "magnet:?xt=urn:btih:";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<AtomohdObject> callToAtomoHD() {
        String html = null;
        try {
            html = restTemplate.getForObject(ATOMOHD_URL, String.class);
        } catch (RestClientException e) {
            log.debug("Error calling to AtomoHD: " + e.getMessage());
            if (e.getMessage().contains("403")) {
                log.debug("403 error calling to AtomoHD, trying with Flaresolverr: {} ", e.getMessage());
                html = callWithFlaresolverr(ATOMOHD_URL).getSolution().getResponse();
            }
        }
        Document doc = Jsoup.parse(html);
        Elements uls = doc.getElementsByClass("featured-slides caption-style-2");
        List<AtomohdObject> atomoHdObjects = new ArrayList<>();
        for (Element ul : uls) {
            for (Node element : ul.childNodes()) {
                for (Node aElement : element.childNodes()) {
                    if (aElement.hasAttr("href") && aElement.hasAttr("title")) {
                        System.out.println(element);
                        AtomohdObject atomohdObject = new AtomohdObject();
                        atomohdObject.setUrl(aElement.attr("href"));
                        atomohdObject.setDomain(ATOMOHD_URL);
                        atomohdObject.setImage(getByteArrayFromImageURL("https:" + aElement.childNode(1).attr("src")));
                        String name = aElement.outerHtml().split("<strong>")[1].split("</strong>")[0].trim();
                        String name2 = aElement.outerHtml().split("<span>")[1].split("</span>")[0].trim();
                        atomohdObject.setName(name + " " + name2);
                        atomohdObject.setFullName(getFullNameFromAtomohd(atomohdObject.getUrl()));
                        atomohdObject.setCategory(html);
                        atomoHdObjects.add(atomohdObject);
                    }
                }
            }
        }
        for (AtomohdObject atomohdObject : atomoHdObjects) {
            callToBT4G(atomohdObject.getFullName(), atomohdObject);
            log.info(atomohdObject.getName());
        }
        return atomoHdObjects;
    }

    private String getFullNameFromAtomohd(String url) {
        String html = callWithFlaresolverr(url).getSolution().getResponse();
        return html.split("<h1><strong>")[1].split("</h1>")[0].replaceAll(" </strong> ", " ").trim();
    }

    private void callToBT4G(String search, AtomohdObject atomohdObject) {
        String html = callWithFlaresolverr(BT4ORG_URL + "search/" + search).getSolution().getResponse();
        Document document = Jsoup.parse(html);
        int numberElements = Integer.parseInt(html.split("<span> Found <b>")[1].split("</b>")[0]);
        if (numberElements > 1) {
            log.debug("More than one element found, trying to get the best one");
        }
        Elements magnetObjects = document.getElementsByTag("h5");
        for (Element magnetObject : magnetObjects) {
            atomohdObject.setMagnetHash(magnetObject.child(0).attr("href").split("/magnet/")[1].split("\"")[0]);
            atomohdObject
                    .setMagnet(createMagnetWithTrackers(atomohdObject.getMagnetHash(), atomohdObject.getFullName()));
            fillFilesArray(BT4ORG_WITHOUT_SLASH_URL + magnetObject.child(0).attr("href"), atomohdObject);
            atomohdObject.setSeeders(Integer.parseInt(magnetObject.parent().getElementById("seeders").text()));
            atomohdObject.setLeechers(Integer.parseInt(magnetObject.parent().getElementById("leechers").text()));
            atomohdObject.setSize(magnetObject.parent().getElementsByClass("cpill red-pill").get(0).text());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            atomohdObject.setCreateDate(
                    LocalDate.parse(
                            magnetObject.parent().html().split("<span>Create Time:&nbsp;<b>")[1].split("</b>")[0],
                            formatter));
        }
    }

    private void fillFilesArray(String magnetBt4gUrl, AtomohdObject atomohdObject) {
        String html = callWithFlaresolverr(magnetBt4gUrl).getSolution().getResponse();
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("ol");
        if (elements.size() == 2) {
            List<String> nameFiles = new ArrayList<>();
            for (Element element : elements.get(1).children()) {
                nameFiles.add(element.text());
            }
            atomohdObject.setNameFiles(nameFiles);
            atomohdObject.setNumFiles(atomohdObject.getNameFiles().size());
        } else {
            log.error("More than one element found");
            // FIXME: HANDLE THIS
        }

    }

    private String createMagnetWithTrackers(String hashMagnet, String name) {
        return MAGNET_LINK_INIT + hashMagnet + "&dn=" + name
                + TRACKERS;
    }

    private FlaresolverrIDTO callWithFlaresolverr(String url) {
        FlaresolverrODTO flaresolverrODTO = new FlaresolverrODTO();
        flaresolverrODTO.setCmd("request.get");
        flaresolverrODTO.setMaxTimeout(600000);
        flaresolverrODTO.setUrl(url);
        return restTemplate.postForObject(FLARESOLVERR_URL, flaresolverrODTO, FlaresolverrIDTO.class);
    }

    private String getByteArrayFromImageURL(String url) {
        try {
            byte[] imageBytes = IOUtils.toByteArray(new URL(url));
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

}

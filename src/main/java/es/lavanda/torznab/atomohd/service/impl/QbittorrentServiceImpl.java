package es.lavanda.torznab.atomohd.service.impl;

import org.springframework.stereotype.Service;

import es.lavanda.torznab.atomohd.service.QbittorrentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QbittorrentServiceImpl implements QbittorrentService {


    @Override
    public void downloadMagnet(String magnet, String category) {
        //TODO:
        throw new UnsupportedOperationException("Unimplemented method 'downloadMagnet'");
    }

}

package es.lavanda.torznab.atomohd.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.lavanda.torznab.atomohd.model.dto.AtomohdObject;
import es.lavanda.torznab.atomohd.model.dto.output.AtomohdODTO;
import es.lavanda.torznab.atomohd.model.entity.Atomohd;
import es.lavanda.torznab.atomohd.repository.AtomohdRepository;
import es.lavanda.torznab.atomohd.service.AtomoHDService;
import es.lavanda.torznab.atomohd.service.ExternalPagesService;
import es.lavanda.torznab.atomohd.service.QbittorrentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class AtomoHDServiceImpl implements AtomoHDService {

    private final AtomohdRepository atomohdRepository;

    private final ExternalPagesService externalPagesService;

    private final QbittorrentService qbittorrentService;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public void updateInternalData() {
        List<AtomohdObject> newAtomohdObjects = externalPagesService.callToAtomoHD();
        List<Atomohd> toSave = checkIfExistsAndMapper(newAtomohdObjects);
        if (Boolean.FALSE.equals(toSave.isEmpty())) {
            atomohdRepository.saveAll(toSave);
        }
    }

    private List<Atomohd> checkIfExistsAndMapper(List<AtomohdObject> newAtomohdObjects) {
        List<Atomohd> newObjects = new ArrayList<>();

        for (AtomohdObject newAtomohd : newAtomohdObjects) {
            if (Boolean.FALSE.equals(atomohdRepository.existsByMagnetHash(newAtomohd.getMagnetHash()))) {
                newObjects.add(modelMapper.map(newAtomohd, Atomohd.class));
            }
        }
        return newObjects;
    }

    @Override
    public Page<AtomohdODTO> getAll(String search, Pageable pageable) {
        List<AtomohdODTO> atomohdODTOs = new ArrayList<>();
        if (Objects.isNull(search)) {
            Page<Atomohd> pageAtomoHd = atomohdRepository.findAll(pageable);
            for (Atomohd atomohd : pageAtomoHd) {

                atomohdODTOs.add(modelMapper.map(atomohd, AtomohdODTO.class));
            }
        } else {
            Page<Atomohd> pageAtomoHd = atomohdRepository.findAllByFullName(search, pageable);
            for (Atomohd atomohd : pageAtomoHd) {
                atomohdODTOs.add(modelMapper.map(atomohd, AtomohdODTO.class));
            }
        }
        return new PageImpl<>(atomohdODTOs, pageable, atomohdODTOs.size());
    }

    @Override
    public void downloadTorrent(String id) {
        atomohdRepository.findById(id)
                .ifPresentOrElse(
                        (torrent) -> qbittorrentService.downloadMagnet(torrent.getMagnet(), torrent.getCategory()),
                        (() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "")));
    }

}

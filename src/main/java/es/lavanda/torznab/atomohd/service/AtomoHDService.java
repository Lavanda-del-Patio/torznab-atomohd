package es.lavanda.torznab.atomohd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.lavanda.torznab.atomohd.model.dto.output.AtomohdODTO;

public interface AtomoHDService {

    Page<AtomohdODTO> getAll(String search, Pageable pageable);

    void updateInternalData();

    void downloadTorrent(String id);
}

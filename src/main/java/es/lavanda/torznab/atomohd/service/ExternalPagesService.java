package es.lavanda.torznab.atomohd.service;

import java.util.List;

import es.lavanda.torznab.atomohd.model.dto.AtomohdObject;

public interface ExternalPagesService {

    List<AtomohdObject> callToAtomoHD();

}

package es.lavanda.torznab.atomohd.controller;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.lavanda.torznab.atomohd.model.dto.output.AtomohdODTO;
import es.lavanda.torznab.atomohd.service.AtomoHDService;
import es.lavanda.torznab.atomohd.service.ExternalPagesService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/atomohd")
@RequiredArgsConstructor
@CrossOrigin(allowedHeaders = "*", origins = { "http://localhost:4200", "https://lavandadelpatio.es",
        "https://pre.lavandadelpatio.es" }, allowCredentials = "true", exposedHeaders = "*", methods = {
                RequestMethod.OPTIONS, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST,
                RequestMethod.PUT }, originPatterns = { "*" })
public class AtomohdRestController {

    private final AtomoHDService atomoHDService;

    private final Executor executor = Executors.newFixedThreadPool(1);

    @GetMapping
    public ResponseEntity<Page<AtomohdODTO>> getAllPageable(@RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(atomoHDService.getAll(search, pageable));
    }

    @PostMapping
    public ResponseEntity<Object> update() {
        executor.execute(() -> atomoHDService.updateInternalData());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/downloads/{id}")
    public ResponseEntity<Object> downloadTorrent(@PathVariable String id) {
        atomoHDService.downloadTorrent(id);
        return ResponseEntity.noContent().build();
    }

}

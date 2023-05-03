package es.lavanda.torznab.atomohd.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import es.lavanda.torznab.atomohd.model.entity.Atomohd;

@Repository
public interface AtomohdRepository extends CrudRepository<Atomohd, String> {

    Page<Atomohd> findAllByFullName(String search, Pageable pageable);

    Page<Atomohd> findAll(Pageable pageable);

    Optional<Atomohd> findById(String id);

    boolean existsByMagnetHash(String magnetHash);

}

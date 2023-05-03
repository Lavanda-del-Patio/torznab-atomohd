package es.lavanda.torznab.atomohd.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AtomohdObject {

    private String name;

    private String domain;

    // private String year;

    private String url;

    private String image;

    private String fullName;

    private String category;

    private String magnet;

    private String magnetHash;

    private int seeders;

    private int leechers;

    private String size;

    private LocalDate createDate;

    private List<String> nameFiles;
    
    private int numFiles;
}

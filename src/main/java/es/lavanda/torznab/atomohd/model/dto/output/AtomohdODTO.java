package es.lavanda.torznab.atomohd.model.dto.output;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class AtomohdODTO {

    private String domain;

    // private String year;

    private String url;

    private String image;

    private String name;

    private String fullName;

    private String magnet;

    private String magnetHash;

    private int seeders;

    private int leechers;

    private String size;

    private LocalDate createDate;

    private List<String> nameFiles;

    private int numFiles;
}

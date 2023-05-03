package es.lavanda.torznab.atomohd.model.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document("atomohd")
public class Atomohd {

    @Id
    private String id;

    private String name;

    @Field("full_name")
    private String fullName;

    private String category;

    private String domain;

    // private String year;
    private String url;

    private String image;

    private String magnet;

    @Field("magnet_hash")
    private String magnetHash;

    private int seeders;

    private int leechers;

    private String size;

    @Field("create_date")
    private LocalDate createDate;

    @Field("name_files")
    private List<String> nameFiles;

    @Field("num_files")
    private int numFiles;

}

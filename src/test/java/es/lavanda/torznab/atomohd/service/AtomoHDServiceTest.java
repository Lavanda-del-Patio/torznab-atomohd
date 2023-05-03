package es.lavanda.torznab.atomohd.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.lavanda.torznab.atomohd.model.dto.AtomohdObject;
import es.lavanda.torznab.atomohd.service.impl.ExternalPagesServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AtomoHDServiceTest {

    private final String BT4ORG_URL = "https://bt4g.org/search/atomohd";

    @Autowired
    private ExternalPagesService atomoHDService;

    @Test
    public void testCallToAtomoHD() {
        List<AtomohdObject> result = atomoHDService.callToAtomoHD();
        System.out.println(result);
    }

}

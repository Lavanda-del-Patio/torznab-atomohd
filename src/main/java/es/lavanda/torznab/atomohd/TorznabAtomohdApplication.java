package es.lavanda.torznab.atomohd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TorznabAtomohdApplication {

	public static void main(String[] args) {
		SpringApplication.run(TorznabAtomohdApplication.class, args);
	}

}

package cultureinfo.culture_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CultureAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CultureAppApplication.class, args);
	}

}

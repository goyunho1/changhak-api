package changhak.changhakapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ChanghakApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChanghakApiApplication.class, args);
	}

}

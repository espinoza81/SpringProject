package dogpark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DogParkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DogParkApplication.class, args);
	}

}

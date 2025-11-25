package in.gov.irms.train;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("in.gov.irms.train.model")
public class TrainService implements CommandLineRunner {
	@Value("${server.port}")
	private int port;
	@Value("${spring.application.name}")
	private String application;

	public static void main(String[] args) {
		SpringApplication.run(TrainService.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.printf("%s started successfully on port: %s%n", application, port);
	}
}

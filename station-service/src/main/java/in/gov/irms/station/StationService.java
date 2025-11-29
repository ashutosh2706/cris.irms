package in.gov.irms.station;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("in.gov.irms.station.model")
public class StationService implements CommandLineRunner {

	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${server.port}")
	private String port;

	public static void main(String[] args) {
		SpringApplication.run(StationService.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.printf("%s started successfully on port: %s%n", applicationName, port);
	}
}

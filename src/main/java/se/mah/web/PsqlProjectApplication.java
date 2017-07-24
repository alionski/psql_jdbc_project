package se.mah.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages={"se.mah.web", "se.mah.business"})
public class PsqlProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsqlProjectApplication.class, args);
	}
}

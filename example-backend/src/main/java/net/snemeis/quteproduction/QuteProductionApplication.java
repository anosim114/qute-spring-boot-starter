package net.snemeis.quteproduction;

import net.snemeis.EngineProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(EngineProducer.class)
public class QuteProductionApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuteProductionApplication.class, args);
	}
}

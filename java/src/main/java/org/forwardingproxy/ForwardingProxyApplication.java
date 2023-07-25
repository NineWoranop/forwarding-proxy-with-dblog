package org.forwardingproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForwardingProxyApplication {

	@Bean
	public ConversionService conversionService() {
		return new DefaultConversionService();
	}

	public static void main(String[] args) {
		SpringApplication.run(ForwardingProxyApplication.class, args);
	}

}

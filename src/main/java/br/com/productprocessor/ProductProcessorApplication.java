package br.com.productprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication 
@ComponentScan({"br.com.productprocessor"})
@EntityScan("br.com.productprocessor.models")
public class ProductProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductProcessorApplication.class, args);
	}
}

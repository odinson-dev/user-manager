package com.convrse.accessprovider;

import com.convrse.usermanagerlib.SharedConfigurationReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(SharedConfigurationReference.class)
public class AccessProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessProviderApplication.class, args);

	}

}

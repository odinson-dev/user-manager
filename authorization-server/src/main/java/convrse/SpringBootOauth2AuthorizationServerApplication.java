package convrse;

import com.convrse.accountmanagerlib.SharedConfigurationReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SharedConfigurationReference.class)
public class SpringBootOauth2AuthorizationServerApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpringBootOauth2AuthorizationServerApplication.class, args);

	}

}

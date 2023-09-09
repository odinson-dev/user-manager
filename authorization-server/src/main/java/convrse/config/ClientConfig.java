package convrse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import convrse.encoder.BcryptPasswordEncoder;

@Configuration
public class ClientConfig {
    @Autowired
	private BcryptPasswordEncoder passwordEncoder;

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient1 = RegisteredClient.withId("client")
				.clientId("client")
				.clientSecret(passwordEncoder.encode("secret"))
				.scope("read")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.scope("message.read")
				.scope("message.write")
				.scope("refresh_token")
				.scope("read")
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/myoauth2")
				.redirectUri("https://oauthdebugger.com/debug")
				.redirectUri("http://insomnia")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				// .tokenSettings(tokenSettings())
				.clientSettings(clientSettings())
				.build();

		return new InMemoryRegisteredClientRepository(registeredClient1);
	}

    	@Bean
	public ClientSettings clientSettings() {
		return ClientSettings.builder().build();
	}
}

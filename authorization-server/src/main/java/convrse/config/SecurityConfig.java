package convrse.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import com.convrse.accountmanagerlib.entity.User;
import com.convrse.accountmanagerlib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import convrse.config.oauth2GrantTypeConfig.passwordGrantType.OAuth2PasswordGrantAuthenticationConverter;
import convrse.config.oauth2GrantTypeConfig.passwordGrantType.OAuth2PasswordGrantAuthenticationProvider;
@SuppressWarnings("deprecation")
@Configuration
public class SecurityConfig {

	@Autowired
	private JwtConfiguration jwtConfiguration;



	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			UserService userDetailsService,
			PasswordEncoder passwordEncoder,
			OAuth2AuthorizationService authorizationService) throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.exceptionHandling(
				exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
				.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

		authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> tokenEndpoint
				.accessTokenRequestConverter(new OAuth2PasswordGrantAuthenticationConverter())
				.authenticationProvider(new OAuth2PasswordGrantAuthenticationProvider(userDetailsService,
						passwordEncoder, authorizationService, tokenGenerator())));

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						authorize -> authorize.anyRequest().authenticated())
				.formLogin(withDefaults())
				.build();
	}

	@Bean
	public OAuth2AuthorizationService authorizationService() {
		return new InMemoryOAuth2AuthorizationService();
	}

	@Bean
	public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService() {
		return new InMemoryOAuth2AuthorizationConsentService();
	}

	@Bean
	public TokenSettings tokenSettings() {
		return TokenSettings.builder()
				.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
				.accessTokenTimeToLive(Duration.ofDays(10))
				.build();
	}


	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	@Bean
	public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
		NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
		jwtGenerator.setJwtCustomizer(tokenCustomizer());
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
		return new DelegatingOAuth2TokenGenerator(
				jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
	}

	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return context -> {
			System.out.println("::::::::::::::::::: customizing token :::::::::::::::::::::::");
			//for the password type
//			JwtVerifier  jwtVerifier = new JwtVerifier();
//			jwtVerifier.verifyJwtToken("eyJraWQiOiIzMDI2ZTM5Yy0wOTM3LTQ5OTctYTQ5My0xMDA1OTAzZTBiYzIiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vYXV0aC1zZXJ2ZXI6OTAwMiIsInN1YiI6IjEiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2ODgxMTIzNTksImV4cCI6MTY4ODExMjY1OSwiaWF0IjoxNjg4MTEyMzU5fQ.BhQ_3T4fDH9_j4hopRQ9gBlMU_IKyPG_xR6-giu-fvSJeP-tCVZUKCw2xTH4nKBb81ODusZQdgq6mRIsxxMrRKxDGKlA4v4o4FkEBRGeFAT3i2NSKyXHtha_dgklAh1JuilR3mh4AInTW0FtsIqvangDBoUkBhKpFCt6jt6T4fBBKHdClqIrlBQVQ2k-txo_RvSu2GmyC0VFDOkTjaYBhxeHlXq8rltRTnAEqAa6wFE0Jtxw8stKyR_1z_CDsO4zjzKRLeU471xA599cAELG4Y5iXj1wZQEfrJfqWVe4i0OEPVOzLpTOjEbtOHfq6ouO4UHMbhIjEb1i-0DGgojf4w");
			if (context.getAuthorizationGrantType().getValue().equals(AuthorizationGrantType.PASSWORD.getValue())) {
   					 User user = (User) context.getPrincipal().getDetails();
						if (context.getTokenType().getValue().equals("access_token"))
							context.getClaims()
									.claim("sub", user.getId());
			}
		};
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = jwtConfiguration.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

}

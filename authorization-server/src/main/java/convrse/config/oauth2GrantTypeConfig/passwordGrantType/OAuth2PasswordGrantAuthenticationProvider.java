package convrse.config.oauth2GrantTypeConfig.passwordGrantType;

import com.convrse.accountmanagerlib.entity.User;
import convrse.config.oauth2GrantTypeConfig.OAuth2EndpointUtils;
import lombok.extern.slf4j.Slf4j;
import convrse.config.oauth2GrantTypeConfig.OAuth2AuthenticationProviderUtils;
import convrse.records.UsernameAuthorities;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;
import com.convrse.accountmanagerlib.service.UserService;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link AuthenticationProvider} implementation for the OAuth 2.0 Resource
 * Owner Password Credentials Grant.
 *
 * @author Attoumane AHAMADI
 */
@Slf4j
public class OAuth2PasswordGrantAuthenticationProvider implements AuthenticationProvider {
    private final UserService UserService;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    public OAuth2PasswordGrantAuthenticationProvider(UserService UserService,
            PasswordEncoder passwordEncoder, OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.UserService = UserService;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        System.out.println("OAuth2PasswordGrantAuthenticationProvider.authenticate()");
        
        OAuth2PasswordGrantAuthenticationToken passwordGrantAuthenticationToken = (OAuth2PasswordGrantAuthenticationToken) authentication;

        // Ensure the client is authenticated
        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(
                        (Authentication) passwordGrantAuthenticationToken.getPrincipal());

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // System.out.println("registeredClient: " + registeredClient);


        // si le client n'est pas enregistré ou ne supporte pas le grant type password
        if (registeredClient == null || !registeredClient.getAuthorizationGrantTypes()
                .contains(passwordGrantAuthenticationToken.getGrantType())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        Set<String> authorizedScopes = Collections.emptySet();
        if (!CollectionUtils.isEmpty(passwordGrantAuthenticationToken.getScopes())) {
            for (String requestedScope : passwordGrantAuthenticationToken.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(passwordGrantAuthenticationToken.getScopes());
        }

        // System.out.println("authorizedScopes: " + authorizedScopes);

        if (log.isDebugEnabled()) {
            log.debug("Checking user credentials");
        }
        String providedUsername = passwordGrantAuthenticationToken.getUsername();
        String providedPassword = passwordGrantAuthenticationToken.getPassword();
        String providedAppName = passwordGrantAuthenticationToken.getAppName();
        User user = null;
        try{
        user = this.UserService.getUserByEmailAndAppName(providedUsername,providedAppName );
        }catch (Exception e){
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "user not found",
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        if (!this.passwordEncoder.matches(providedPassword, user.getPassword())) {
            throw new OAuth2AuthenticationException("Invalid resource owner credentials");
        }

        if (log.isDebugEnabled()) {
            log.debug("Generating access token");
        }

//        authorizedScopes = user.getAuthorities().stream()
//                .map(scope -> scope.getAuthority())
//                .filter(scope -> registeredClient.getScopes().contains(scope))
//                .collect(Collectors.toSet());

        // -----------Create a new Security Context Holder Context----------
        OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken = (OAuth2ClientAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        UsernameAuthorities customPasswordUser = new UsernameAuthorities(providedUsername,
                authorizedScopes);
        oAuth2ClientAuthenticationToken.setDetails(user);

        var newcontext = SecurityContextHolder.createEmptyContext();
        newcontext.setAuthentication(oAuth2ClientAuthenticationToken);
        SecurityContextHolder.setContext(newcontext);

        // -----------TOKEN BUILDERS----------
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(clientPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .authorizationGrant(passwordGrantAuthenticationToken);

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .attribute(Principal.class.getName(), clientPrincipal)
                .principalName(clientPrincipal.getName())
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .authorizedScopes(authorizedScopes);

        // -----------ACCESS TOKEN----------
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken,
                    (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                            ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        System.out.println("access token : " + accessToken.getTokenValue());


        // -----------REFRESH TOKEN----------
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }
            System.out.println("generating refresh token");
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            System.out.println("refresh token : " + refreshToken.getTokenValue());
            authorizationBuilder.refreshToken(refreshToken);
        }

        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
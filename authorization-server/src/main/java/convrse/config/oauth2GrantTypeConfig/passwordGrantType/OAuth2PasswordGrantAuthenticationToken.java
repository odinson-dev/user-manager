package convrse.config.oauth2GrantTypeConfig.passwordGrantType;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Set;

/**
 * Authentication token for the OAuth 2.0 Resource Owner Password Credentials
 * Grant.
 *
 * @author Attoumane AHAMADI
 */
@Getter
public class OAuth2PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 7840626509676504832L;
    private final String username;
    private final String password;
    private final String clientId;
    private final Set<String> scopes;
    private final String appName;

    public OAuth2PasswordGrantAuthenticationToken(String username, String password, Authentication clientPrincipal,
            Set<String> scopes, String appName) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, null);
        this.password = password;
        this.username = username;
        this.clientId = clientPrincipal.getName();
        this.scopes = scopes;
        this.appName = appName;
    }
}
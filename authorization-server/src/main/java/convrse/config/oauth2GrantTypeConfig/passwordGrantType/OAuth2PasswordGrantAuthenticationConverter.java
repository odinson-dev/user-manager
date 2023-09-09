package convrse.config.oauth2GrantTypeConfig.passwordGrantType;

import jakarta.servlet.http.HttpServletRequest;
import convrse.config.oauth2GrantTypeConfig.OAuth2EndpointUtils;

import org.springframework.util.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;

import java.util.Set;

public class OAuth2PasswordGrantAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)

        System.out.println("OAuth2PasswordGrantAuthenticationConverter.convert()");

        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        if (clientPrincipal == null) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_CLIENT, OAuth2ParameterNames.CLIENT_ID,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // if client id does not match the client id in the request, throw an error
         if
         (!(clientPrincipal.getName().equals(parameters.getFirst(OAuth2ParameterNames.CLIENT_ID))))
         {
         OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_CLIENT,
         OAuth2ParameterNames.CLIENT_ID,
         OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
         }

        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (scope != null && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> scopes = scope != null ? Set.of(scope.split(" ")) : null;

        System.out.println("Client Principal: " + clientPrincipal);

        return new OAuth2PasswordGrantAuthenticationToken(parameters.getFirst(OAuth2ParameterNames.USERNAME),
                parameters.getFirst(OAuth2ParameterNames.PASSWORD), clientPrincipal, scopes, parameters.getFirst("app"));
    }
}
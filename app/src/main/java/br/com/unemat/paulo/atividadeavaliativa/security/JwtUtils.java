package br.com.unemat.paulo.atividadeavaliativa.security;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class JwtUtils {

    public static UUID getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        try {
            JWT jwt = new JWT(token);
            String sub = jwt.getSubject();
            if (sub == null) {
                return null;
            }
            return UUID.fromString(sub);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getRolesFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            JWT jwt = new JWT(token);
            Claim scopeClaim = jwt.getClaim("scope");
            String scopes = scopeClaim.asString();

            if (scopes == null || scopes.trim().isEmpty()) {
                return Collections.emptyList();
            }
            return Collections.singletonList(scopes);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static boolean hasRole(String token, String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            return false;
        }
        return getRolesFromToken(token).contains(roleName);
    }
}
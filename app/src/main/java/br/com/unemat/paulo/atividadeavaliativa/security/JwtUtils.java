package br.com.unemat.paulo.atividadeavaliativa.security;

import com.auth0.android.jwt.JWT;

import java.util.UUID;

public class JwtUtils {

    public static UUID getUserIdFromToken(String token) {
        try {
            JWT jwt = new JWT(token);
            String sub = jwt.getSubject();
            return UUID.fromString(sub);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
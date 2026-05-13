package com.homerentsolution.msprecios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key KEY =
            Keys.secretKeyFor(
                    SignatureAlgorithm.HS256
            );

    // generar token
    public static String generarToken(
            String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 86400000
                        )
                )
                .signWith(KEY)
                .compact();
    }

    // validar token
    public static boolean validarToken(
            String token) {

        try {

            Jwts.parser()
                    .verifyWith(
                            (javax.crypto.SecretKey) KEY
                    )
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    // obtener usuario
    public static String obtenerUsername(
            String token) {

        Claims claims = Jwts.parser()
                .verifyWith(
                        (javax.crypto.SecretKey) KEY
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}
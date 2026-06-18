package com.cashcontrol.usuario.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.cashcontrol.usuario.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    private static final String CHAVE_SECRETA = "umaChaveSuperSeguraComMaisDe32Caracteres123!";
    private static final long EXPIRACAO_ACCESS = 1000 * 60 * 15; // 15 minutos
    private static final long EXPIRACAO_REFRESH = 1000 * 60 * 60 * 24 * 7; // 7 dias

    private final SecretKey secretKey;

    public JwtService() {
        this.secretKey = new SecretKeySpec(
                CHAVE_SECRETA.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");
    }

    public String gerarAccessToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("nome", usuario.getNome())
                .claim("tipo", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACAO_ACCESS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String gerarRefreshToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("tipo", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACAO_REFRESH))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String gerarAccessTokenCookie(String token) {
        return String.format(
            "accessToken=%s; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=900",
            token
        );
    }

    public String gerarRefreshTokenCookie(String token) {
        return String.format(
            "refreshToken=%s; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=604800",
            token
        );
    }

    public String gerarToken(Usuario usuario) {
        return gerarAccessToken(usuario);
    }

    public Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public String extrairTipo(String token) {
        return extrairClaims(token).get("tipo", String.class);
    }

    public boolean isTokenValido(String token) {
        try {
            extrairClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpirado(String token) {
        try {
            Claims claims = extrairClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
    
}


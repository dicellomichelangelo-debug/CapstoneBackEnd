package michelangelodicello.CapstoneBackEnd.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTTools {

    @Value("${JWT_SECRET:chiavesegretasupersegreta12345678901234567890}")
    private String secret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Utente utente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .subject(String.valueOf(utente.getId()))
                .signWith(getSecretKey())
                .compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("Token non valido o scaduto! Effettua nuovamente il login.");
        }
    }

    public String extractIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
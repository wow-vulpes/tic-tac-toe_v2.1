package tictactoe.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tictactoe.domain.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final long accessTokenTtlMs;
    private final long refreshTokenTtlMs;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.access-ttl-ms}") long accessTokenTtlMs,
                       @Value("${jwt.refresh-ttl-ms}") long refreshTokenTtlMs){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenTtlMs = accessTokenTtlMs;
        this.refreshTokenTtlMs = refreshTokenTtlMs;
    }

    public String generateAccessToken(User user){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenTtlMs);
        return Jwts.builder()
                .subject(user.getLogin())
                .claim("type", "access")
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .issuedAt(now).expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenTtlMs);
        return Jwts.builder()
                .subject(user.getLogin())
                .claim("type", "refresh")
                .claim("userId", user.getId())
                .issuedAt(now).expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateAccessToken(String token){
        return validateToken(token, "access");
    }

    public boolean validateRefreshToken(String token){
        return validateToken(token, "refresh");
    }

    public Claims getClaims(String token){
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private boolean validateToken(String token, String tokenType){
        try {
            Claims claims = getClaims(token);

            return claims.getExpiration().after(new Date())
                    && claims.get("type").equals(tokenType);
        } catch (Exception ex) {
            return false;
        }
    }
}

package tictactoe.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import tictactoe.domain.model.gamecomponents.Role;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    public JwtAuthentication createJwtAuthentication(Claims claims){
        if (claims == null){
            throw new IllegalArgumentException("There're no claims");
        }

        UUID userId = (UUID) claims.get("userId");

        Object rolesRaw = claims.get("roles");

        if (!(rolesRaw instanceof Collection<?> collection)) {
            throw new IllegalArgumentException("Invalid roles format");
        }

        Set<Role> roles = collection.stream()
                .map(Object::toString)
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        return new JwtAuthentication(userId, roles, true);
    }
}

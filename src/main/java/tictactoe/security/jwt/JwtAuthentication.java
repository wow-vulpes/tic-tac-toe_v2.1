package tictactoe.security.jwt;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import tictactoe.domain.model.Role;

import java.util.*;

public class JwtAuthentication implements Authentication {
    private final UUID userId;
    private Set<Role> roles = new HashSet<>();
    private boolean isAuthorized;

    public JwtAuthentication(UUID userId, Set<Role> roles, boolean isAuthorized){
        this.userId = userId;
        this.roles = roles;
        this.isAuthorized = isAuthorized;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return userId;
    }

    @Override
    public String getName() {
        return userId.toString();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthorized;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated){
            throw new IllegalArgumentException("Attempt to extend the token's validity");
        }

        isAuthorized = false;
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return null;
    }
}

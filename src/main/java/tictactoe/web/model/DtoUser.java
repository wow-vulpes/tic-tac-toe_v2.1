package tictactoe.web.model;

import java.util.Set;
import java.util.UUID;

public class DtoUser {
    private final UUID id;
    private final String login;

    private final Set<DtoRole> roles;

    public DtoUser(UUID id, String login, Set<DtoRole> roles){
        this.id = id;
        this.login = login;

        this.roles = roles;
    }

    public String getLogin(){
        return login;
    }

    public UUID getId(){
        return id;
    }

    public Set<DtoRole> getRoles() {
        return roles;
    }
}

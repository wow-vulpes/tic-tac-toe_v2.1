package tictactoe.domain.model;

import tictactoe.domain.model.gamecomponents.Role;

import java.util.Set;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String login;
    private final String password;

    private final Set<Role> roles;

    public User(String login, String password, Set<Role> roles){
        this.login = login;
        this.password = password;

        this.roles = roles;

        this.id = UUID.randomUUID();
    }

    public User(UUID id, String login, String password, Set<Role> roles){
        this.id = id;
        this.login = login;
        this.roles = roles;
        this.password = password;
    }

    public UUID getId(){
        return id;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}

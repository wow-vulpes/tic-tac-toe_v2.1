package tictactoe.datasource.model;

import jakarta.persistence.*;
import tictactoe.web.model.DtoRole;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<DtoRole> roles = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(UUID id, String login, String password, Set<DtoRole> roles) {
        this.id = id;
        this.login = login;
        this.password = password;

        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Set<DtoRole> getRoles() {
        return roles;
    }
}

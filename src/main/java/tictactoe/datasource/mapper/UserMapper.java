package tictactoe.datasource.mapper;

import tictactoe.datasource.model.UserEntity;
import tictactoe.domain.model.User;
import org.springframework.stereotype.Component;
import tictactoe.domain.model.gamecomponents.Role;
import tictactoe.web.model.gamecomponents.DtoRole;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserEntity toUserEntity(User user) {
        return new UserEntity(user.getId(), user.getLogin(), user.getPassword(), mapRolesToData(user.getRoles()));
    }

    public User toUser(UserEntity user) {
        return new User(user.getId(), user.getLogin(), user.getPassword(), mapRolesToDomain(user.getRoles()));
    }

    private Set<DtoRole> mapRolesToData(Set<Role> roles) {
        return roles.stream()
                .map(role -> DtoRole.valueOf(role.name()))
                .collect(Collectors.toSet());
    }

    private Set<Role> mapRolesToDomain(Set<DtoRole> dtoRoles) {
        return dtoRoles.stream()
                .map(role -> Role.valueOf(role.name()))
                .collect(Collectors.toSet());
    }
}

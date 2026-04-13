package tictactoe.datasource.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import tictactoe.datasource.mapper.UserMapper;
import tictactoe.datasource.model.UserEntity;
import tictactoe.datasource.repository.UserRepository;
import tictactoe.domain.model.User;
import tictactoe.domain.model.gamecomponents.Role;
import tictactoe.domain.service.UserServiceInterface;
import org.springframework.stereotype.Service;
import tictactoe.web.model.gamecomponents.DtoRole;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserServiceInterface {
    public final UserRepository userRepository;
    public final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(userMapper.toUserEntity(user));
    }

    @Override
    public User getUserByLogin(String login) {
        if (login == null){
            throw new IllegalArgumentException("There's no login");
        }

        Optional<UserEntity> user = userRepository.findByLogin(login);
        return user.map(userMapper::toUser).orElse(null);
    }

    @Override
    public User getUserById(UUID id) {
        if (id == null){
            throw new IllegalArgumentException("There's no ID");
        }

        UserEntity user = userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("There's no user with that ID"));
        return userMapper.toUser(user);
    }
}

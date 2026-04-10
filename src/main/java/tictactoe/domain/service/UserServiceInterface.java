package tictactoe.domain.service;

import tictactoe.domain.model.User;

import java.util.UUID;

public interface UserServiceInterface {
    void saveUser(User user);

    User getUserByLogin(String login);

    User getUserById(UUID id);
}

package tictactoe.datasource.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tictactoe.domain.model.User;
import org.springframework.stereotype.Service;
import tictactoe.domain.model.gamecomponents.Role;
import tictactoe.security.jwt.JwtAuthentication;
import tictactoe.security.jwt.JwtProvider;
import tictactoe.web.model.SignUpRequest;
import tictactoe.web.model.jwt.JwtRequest;
import tictactoe.web.model.jwt.JwtResponse;

import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    public boolean register(SignUpRequest request) {
        if (isIncorrectRequest(request) || hasAlreadyRegistered(request.getLogin())) {
            return false;
        }

        Set<Role> defaultRoles = Set.of(Role.USER);
        String hashedPassword = encoder.encode(request.getPassword());

        User newUser = new User(request.getLogin(), hashedPassword, defaultRoles);
        userService.saveUser(newUser);
        return true;
    }

    public JwtResponse authorize(JwtRequest request) {
        if (request == null || request.login() == null || request.password() == null) {
            throw new IllegalArgumentException("There're no login and password");
        }

        User user = validateUser(request.login(), request.password());

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    public JwtResponse updateAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            UUID userId = UUID.fromString(jwtProvider.getClaims(refreshToken).get("userId", String.class));
            User user = userService.getUserById(userId);

            String accessToken = jwtProvider.generateAccessToken(user);

            return new JwtResponse("Bearer", accessToken, refreshToken);
        }

        throw new IllegalArgumentException("Incorrect refresh token");
    }

    public JwtResponse updateRefreshToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            UUID userId = UUID.fromString(jwtProvider.getClaims(refreshToken).get("userId", String.class));
            User user = userService.getUserById(userId);

            String accessToken = jwtProvider.generateAccessToken(user);
            String newRefreshToken = jwtProvider.generateRefreshToken(user);

            return new JwtResponse("Bearer", accessToken, newRefreshToken);
        }

        throw new IllegalArgumentException("Incorrect refresh token");
    }

    public JwtAuthentication getJwtAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthentication jwtAuthentication) {
            return jwtAuthentication;
        }

        throw new IllegalStateException("There's no JwtAuthentication in SecurityContext");
    }

    private User validateUser(String login, String password) {
        User user = userService.getUserByLogin(login);

        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect login or password");
        }

        return user;
    }

    private boolean isIncorrectRequest(SignUpRequest request) {
        return request == null || request.getLogin() == null || request.getPassword() == null ||
                request.getLogin().isBlank() || request.getPassword().isBlank();
    }

    private boolean hasAlreadyRegistered(String login) {
        return userService.getUserByLogin(login) != null;
    }
}

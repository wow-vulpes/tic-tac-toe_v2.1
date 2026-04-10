package tictactoe.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import tictactoe.datasource.service.AuthService;
import org.springframework.web.bind.annotation.*;
import tictactoe.datasource.service.UserService;
import tictactoe.domain.model.User;
import tictactoe.security.jwt.JwtAuthentication;
import tictactoe.web.mapper.WebMapper;
import tictactoe.web.model.DtoUser;
import tictactoe.web.model.SignUpRequest;
import tictactoe.web.model.jwt.JwtRefreshRequest;
import tictactoe.web.model.jwt.JwtRequest;
import tictactoe.web.model.jwt.JwtResponse;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final WebMapper mapper;

    public AuthController(AuthService authService, UserService userService, WebMapper mapper){
        this.authService = authService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/sign-up")
    public boolean signUp(@RequestBody SignUpRequest request){
        return authService.register(request);
    }

    @PostMapping("/sign-in")
    public JwtResponse signIn(@RequestBody JwtRequest request){
        return authService.authorize(request);
    }

    @PostMapping("/access-token")
    public JwtResponse refreshAccessToken(@RequestBody JwtRefreshRequest request){
        return authService.updateAccessToken(request.refreshToken());
    }

    @PostMapping("/refresh-token")
    public JwtResponse refreshRefreshToken(@RequestBody JwtRefreshRequest request){
        String refreshToken = request.refreshToken();
        if (refreshToken == null || refreshToken.isBlank()){
            throw new IllegalArgumentException("There's no refresh token");
        }

        return authService.updateRefreshToken(refreshToken);
    }

    @GetMapping("/current-user")
    public DtoUser getCurrentUser(){
        JwtAuthentication authentication = authService.getJwtAuthentication();
        User user = userService.getUserById((UUID) authentication.getPrincipal());
        return mapper.toDtoUser(user);
    }
}

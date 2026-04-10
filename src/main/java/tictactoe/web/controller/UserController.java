package tictactoe.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import tictactoe.domain.model.User;
import tictactoe.datasource.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tictactoe.web.mapper.WebMapper;
import tictactoe.web.model.DtoUser;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final WebMapper mapper;
    private final UserService userService;

    public UserController(WebMapper mapper, UserService userService){
        this.mapper = mapper;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public DtoUser getUserInfo(@PathVariable("id") UUID userId){
        User user = userService.getUserById(userId);
        return mapper.toDtoUser(user);
    }
}

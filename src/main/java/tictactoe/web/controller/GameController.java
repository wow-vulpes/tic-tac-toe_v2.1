package tictactoe.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import tictactoe.domain.model.Game;
import tictactoe.domain.service.GameServiceInterface;
import org.springframework.web.bind.annotation.*;
import tictactoe.web.mapper.WebMapper;
import tictactoe.web.model.CreateRequest;
import tictactoe.web.model.MoveRequest;
import tictactoe.web.model.DtoGame;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {
    private final WebMapper mapper;
    private final GameServiceInterface gameService;


    public GameController(WebMapper mapper, GameServiceInterface gameService) {
        this.mapper = mapper;
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public DtoGame createSession(@RequestBody CreateRequest request, @AuthenticationPrincipal UUID playerId){
        Game currentGame = gameService.createGame(playerId, mapper.toGameMode(request.getMode()));
        return mapper.toDtoSession(currentGame);
    }

    @GetMapping("/available")
    public List<UUID> getAvailableGames(@AuthenticationPrincipal UUID playerId){
        return gameService.getAvailableGames(playerId);
    }

    @PostMapping("/join/{id}")
    public DtoGame joinSession(@PathVariable("id") UUID gameId, @AuthenticationPrincipal UUID playerId){
        Game currentGame = gameService.joinTheGame(gameId, playerId);
        return mapper.toDtoSession(currentGame);
    }

    @PostMapping("/play/{id}")
    public DtoGame updateSession(@PathVariable("id") UUID gameId, @RequestBody MoveRequest newMove, @AuthenticationPrincipal UUID playerId){
        Game currentGame = gameService.updateCurrentGame(gameId, playerId, mapper.toMove(newMove));
        return mapper.toDtoSession(currentGame);
    }

    @GetMapping("/get/{id}")
    public DtoGame getCurrentSession(@PathVariable("id") UUID gameId){
        Game currentGame = gameService.getCurrentGame(gameId);
        return mapper.toDtoSession(currentGame);
    }
}
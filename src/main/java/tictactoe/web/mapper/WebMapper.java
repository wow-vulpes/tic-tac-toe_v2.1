package tictactoe.web.mapper;

import tictactoe.domain.model.*;
import tictactoe.domain.model.User;
import tictactoe.domain.model.gamecomponents.*;
import org.springframework.stereotype.Component;
import tictactoe.web.model.*;
import tictactoe.web.model.gamecomponents.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebMapper {

    public WebMapper() {

    }

    public DtoUser toDtoUser(User user){
        return new DtoUser(user.getId(), user.getLogin(), toDtoRoles(user.getRoles()));
    }

    public DtoGame toDtoSession(Game game) {
        DtoGame gameResponse = new DtoGame();

        gameResponse.setGameId(game.getId());
        gameResponse.setField(new DtoGameField(game.getGameField()));
        gameResponse.setState(toDtoGameState(game.getGameState()));
        gameResponse.setMode(toDtoGameMode(game.getMode()));

        DtoPlayerToken player1Token = definePlayerToken(game.getPlayer1().getPlayerToken());
        gameResponse.setPlayer1(new DtoPlayer(game.getPlayer1().getPlayerId(), player1Token));

        if (game.getPlayer2() != null){
            DtoPlayerToken player2Token = definePlayerToken(game.getPlayer2().getPlayerToken());
            gameResponse.setPlayer2(new DtoPlayer(game.getPlayer2().getPlayerId(), player2Token));
        }

        gameResponse.setCreatedAt(game.getCreatedAt()
                .atZone(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime());

        return gameResponse;
    }

    public List<DtoLeaderBoard> toDtoLeaderBoard(List<LeaderBoard> leaderBoard){
        return leaderBoard.stream()
                .map(lb -> new DtoLeaderBoard(lb.userId(), lb.winRate()))
                .toList();
    }

    private Set<DtoRole> toDtoRoles(Set<Role> roles){
        return roles.stream().map(role -> DtoRole.valueOf(role.name())).collect(Collectors.toSet());
    }

    private DtoGameMode toDtoGameMode(GameMode gameMode) {
        return gameMode == GameMode.PVE ? DtoGameMode.PVE : DtoGameMode.PVP;
    }

    private DtoGameState toDtoGameState(GameState gameState) {
        switch (gameState.getState()) {
            case WAITING_FOR_PLAYERS -> {
                return DtoGameState.WAITING_FOR_PLAYERS;
            }

            case PLAYER_TURN -> {
                return DtoGameState.PLAYER_TURN;
            }

            case DRAW -> {
                return DtoGameState.DRAW;
            }

            case PLAYER_WIN -> {
                return DtoGameState.PLAYER_WIN;
            }
        }

        throw new IllegalArgumentException("Game state's not defined");
    }

    private DtoPlayerToken definePlayerToken(DefaultValues.Token token) {
        return token == DefaultValues.Token.X ? DtoPlayerToken.X : DtoPlayerToken.O;
    }

    public GameMode toGameMode(DtoGameMode mode) {
        try {
            return GameMode.valueOf(mode.name());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to create a game session: unknown game mode");
        }
    }

    public Move toMove(MoveRequest moveRequest) {
        return new Move(moveRequest.getX(), moveRequest.getY());
    }
}
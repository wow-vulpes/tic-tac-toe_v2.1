package tictactoe.datasource.mapper;

import tictactoe.datasource.model.gamecomponents.*;
import tictactoe.datasource.model.GameEntity;
import tictactoe.domain.model.*;
import tictactoe.domain.model.gamecomponents.*;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    // GAME
    public GameEntity toGameEntity(Game game) {
        GameEntity gameEntity = new GameEntity();

        gameEntity.setId(game.getId());
        gameEntity.setField(game.getGameField());
        gameEntity.setGameState(toGameStateEmbeddable(game.getGameState()));
        gameEntity.setMode(toDtoGameMode(game.getMode()));
        gameEntity.setPlayer1(toPlayerEmbeddable(game.getPlayer1()));

        if (game.getPlayer2() != null){
            gameEntity.setPlayer2(toPlayerEmbeddable(game.getPlayer2()));
        }

        return gameEntity;
    }

    public Game toGame(GameEntity gameEntity) {
        Game game = new Game();

        game.setId(gameEntity.getId());
        game.setField(new GameField(gameEntity.getField()));
        game.setState(toGameState(gameEntity.getGameState()));
        game.setMode(toGameMode(gameEntity.getMode()));
        game.setPlayer1(toPlayer(gameEntity.getPlayer1()));

        if (gameEntity.getPlayer2() != null){
            game.setPlayer2(toPlayer(gameEntity.getPlayer2()));
        }

        return game;
    }

    private PlayerEmbeddable toPlayerEmbeddable(Player player) {
        PlayerEmbeddable playerEmb = new PlayerEmbeddable();
        playerEmb.setPlayerId(player.getPlayerId());

        DtoPlayerToken token = player.getPlayerToken() == DefaultValues.Token.X ? DtoPlayerToken.X : DtoPlayerToken.O;
        playerEmb.setPlayerToken(token);
        return playerEmb;
    }

    private Player toPlayer(PlayerEmbeddable playerEmb){
        Player player = new Player();
        player.setPlayerId(playerEmb.getPlayerId());

        DefaultValues.Token token = playerEmb.getPlayerToken() == DtoPlayerToken.X ? DefaultValues.Token.X : DefaultValues.Token.O;
        player.setPlayerToken(token);

        return player;
    }

    private GameStateEmbeddable toGameStateEmbeddable(GameState state){
        GameStateEmbeddable gameStateEmb = new GameStateEmbeddable(state.getPlayerId());
        switch (state.getState()){
            case WAITING_FOR_PLAYERS -> gameStateEmb.setState(DtoGameState.WAITING_FOR_PLAYERS);
            case PLAYER_TURN -> gameStateEmb.setState(DtoGameState.PLAYER_TURN);
            case DRAW -> gameStateEmb.setState(DtoGameState.DRAW);
            case PLAYER_WIN -> gameStateEmb.setState(DtoGameState.PLAYER_WIN);
        }

        return gameStateEmb;
    }

    private GameState toGameState(GameStateEmbeddable gameStateEmb){
        GameState state = new GameState(gameStateEmb.getPlayerId());
        switch (gameStateEmb.getState()){
            case WAITING_FOR_PLAYERS -> state.setState(State.WAITING_FOR_PLAYERS);
            case PLAYER_TURN -> state.setState(State.PLAYER_TURN);
            case DRAW -> state.setState(State.DRAW);
            case PLAYER_WIN -> state.setState(State.PLAYER_WIN);
        }

        return state;
    }

    private GameMode toGameMode(DtoGameMode mode){
        return mode == DtoGameMode.PVP ? GameMode.PVP : GameMode.PVE;
    }

    private DtoGameMode toDtoGameMode(GameMode mode){
        return mode == GameMode.PVP ? DtoGameMode.PVP : DtoGameMode.PVE;
    }
}

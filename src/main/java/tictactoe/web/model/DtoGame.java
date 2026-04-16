package tictactoe.web.model;

import tictactoe.web.model.gamecomponents.DtoGameField;
import tictactoe.web.model.gamecomponents.DtoGameMode;
import tictactoe.web.model.gamecomponents.DtoGameState;
import tictactoe.web.model.gamecomponents.DtoPlayer;

import java.time.LocalDateTime;
import java.util.UUID;

public class DtoGame {
    private UUID gameId;

    private LocalDateTime createdAt;

    private DtoGameField field;
    private DtoGameState state;
    private DtoGameMode mode;
    private DtoPlayer player1;
    private DtoPlayer player2;

    public DtoGame(){

    }

    public UUID getGameId() {
        return gameId;
    }

    public DtoGameState getState() {
        return state;
    }

    public DtoGameMode getMode() {
        return mode;
    }

    public DtoGameField getField() {
        return field;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public void setField(DtoGameField field) {
        this.field = field;
    }

    public void setState(DtoGameState state) {
        this.state = state;
    }

    public void setMode(DtoGameMode mode) {
        this.mode = mode;
    }

    public DtoPlayer getPlayer1() {
        return player1;
    }

    public DtoPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer1(DtoPlayer player1) {
        this.player1 = player1;
    }

    public void setPlayer2(DtoPlayer player2) {
        this.player2 = player2;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

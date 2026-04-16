package tictactoe.domain.model;

import tictactoe.domain.model.gamecomponents.*;

import java.time.Instant;
import java.util.UUID;

public class Game {
    private UUID id;

    private Instant createdAt;

    private GameField field;
    private GameState state;
    private GameMode mode;
    private Player player1;
    private Player player2;

    //для Entity
    public Game(){

    }

    public Game(UUID player1Id) {
        id = UUID.randomUUID();
        field = new GameField();
        player1 = new Player(player1Id, DefaultValues.Token.X);
    }

    public GameState getGameState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int[][] getGameField() {
        return field.getField();
    }

    public void updateGameField(Move newMove, DefaultValues.Token token){
        field.updateField(newMove, token);
    }

    public void setField(GameField field) {
        this.field = field;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

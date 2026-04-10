package tictactoe.domain.model.gamecomponents;

import java.util.UUID;

public class GameState {
    private State state;
    private UUID playerId;

    public GameState(){

    }

    public GameState(State state){
        this.state = state;
    }

    public GameState(State state, UUID playerId){
        this.state = state;
        this.playerId = playerId;
    }

    public GameState(UUID playerId){
        this.playerId = playerId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }
}

package tictactoe.datasource.model.gamecomponents;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

@Embeddable
public class GameStateEmbeddable {
    @Enumerated(EnumType.STRING)
    private DtoGameState state;

    private UUID playerId;

    public GameStateEmbeddable(){}

    public GameStateEmbeddable(UUID playerId){
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setState(DtoGameState state) {
        this.state = state;
    }

    public DtoGameState getState() {
        return state;
    }
}

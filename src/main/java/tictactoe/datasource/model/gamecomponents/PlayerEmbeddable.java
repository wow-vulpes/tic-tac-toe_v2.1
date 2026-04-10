package tictactoe.datasource.model.gamecomponents;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

@Embeddable
public class PlayerEmbeddable {
    private UUID playerId;

    @Enumerated(EnumType.STRING)
    private DtoPlayerToken playerToken;

    public PlayerEmbeddable() {}

    public PlayerEmbeddable(UUID playerId, DtoPlayerToken playerToken){
        this.playerId = playerId;
        this.playerToken = playerToken;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public DtoPlayerToken getPlayerToken() {
        return playerToken;
    }

    public void setPlayerToken(DtoPlayerToken playerToken) {
        this.playerToken = playerToken;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }
}

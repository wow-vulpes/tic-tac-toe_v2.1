package tictactoe.domain.model.gamecomponents;

import java.util.UUID;

public class Player {
    private UUID playerId;
    private DefaultValues.Token playerToken;

    public Player(){}

    public Player(UUID id, DefaultValues.Token playerToken){
        this.playerId = id;
        this.playerToken = playerToken;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public DefaultValues.Token getPlayerToken() {
        return playerToken;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public void setPlayerToken(DefaultValues.Token playerToken) {
        this.playerToken = playerToken;
    }
}

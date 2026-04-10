package tictactoe.web.model.gamecomponents;

import java.util.UUID;

public class DtoPlayer {
    private UUID playerId;
    private DtoPlayerToken playerToken;

    public DtoPlayer(){}

    public DtoPlayer(UUID id, DtoPlayerToken token){
        playerId = id;
        playerToken = token;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public DtoPlayerToken getPlayerToken() {
        return playerToken;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public void setPlayerToken(DtoPlayerToken playerToken) {
        this.playerToken = playerToken;
    }
}

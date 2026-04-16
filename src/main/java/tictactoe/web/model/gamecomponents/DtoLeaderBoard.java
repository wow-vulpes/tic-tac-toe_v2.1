package tictactoe.web.model.gamecomponents;

import java.util.UUID;

public record DtoLeaderBoard(UUID userId, double winRate){
}

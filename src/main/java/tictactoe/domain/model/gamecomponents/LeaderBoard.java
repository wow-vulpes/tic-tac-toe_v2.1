package tictactoe.domain.model.gamecomponents;

import java.util.UUID;

public record LeaderBoard(UUID userId, double winRate){
}

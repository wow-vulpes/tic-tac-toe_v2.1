package tictactoe.domain.service;

import tictactoe.domain.model.Game;
import tictactoe.domain.model.gamecomponents.GameMode;
import tictactoe.domain.model.gamecomponents.LeaderBoard;
import tictactoe.domain.model.gamecomponents.Move;

import java.util.List;
import java.util.UUID;

public interface GameServiceInterface {
    Game createGame(UUID playerId, GameMode mode);

    List<UUID> getAvailableGames(UUID playerId);

    Game updateCurrentGame(UUID gameId, UUID playerId, Move newMove);

    Game joinTheGame(UUID gameId, UUID playerId);

    Game getCurrentGame(UUID gameId);

    List<UUID> getFinishedGames(UUID playerId);

    List<LeaderBoard> getLeaderBoard(int N);
}

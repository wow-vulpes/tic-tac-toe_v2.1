package tictactoe.datasource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tictactoe.datasource.model.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
    @Query("""
       SELECT g.id
       FROM GameEntity g
       WHERE g.state.state = tictactoe.datasource.model.gamecomponents.DtoGameState.WAITING_FOR_PLAYERS
         AND g.player1.playerId <> :currentPlayerId
       """)
    List<UUID> findAvailableGames(@Param("currentPlayerId") UUID currentPlayerId);
}
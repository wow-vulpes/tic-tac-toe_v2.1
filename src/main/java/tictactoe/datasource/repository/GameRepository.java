package tictactoe.datasource.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tictactoe.datasource.model.gamecomponents.DtoLeaderBoard;
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
       WHERE g.state.state = 'WAITING_FOR_PLAYERS'
         AND g.player1.playerId <> :userId
       """)
    List<UUID> findAvailableGames(@Param("userId") UUID userId);

    @Query("""
            SELECT g.id
            FROM GameEntity g
            WHERE (g.state.state = 'DRAW' AND (g.player1.playerId = :userId OR g.player2.playerId = :userId))
            OR (g.state.playerId = :userId AND g.state.state = 'PLAYER_WIN')
           """)
    List<UUID> findFinishedGames(@Param("userId") UUID userId);

    @Query(value = """
            WITH player_games AS (
                SELECT
                    gs.player1_id AS user_id,
                    (gs.state = 'PLAYER_WIN' AND gs.player_id = gs.player1_id)::int AS win,
                    (gs.state = 'PLAYER_WIN' AND gs.player_id <> gs.player1_id)::int AS loss,
                    (gs.state = 'DRAW')::int AS draw
                FROM game_sessions gs
                WHERE gs.state IN ('PLAYER_WIN', 'DRAW')
            
                UNION ALL
            
                SELECT
                    gs.player2_id AS user_id,
                    (gs.state = 'PLAYER_WIN' AND gs.player_id = gs.player2_id)::int AS win,
                    (gs.state = 'PLAYER_WIN' AND gs.player_id <> gs.player2_id)::int AS loss,
                    (gs.state = 'DRAW')::int AS draw
                FROM game_sessions gs
                WHERE gs.state IN ('PLAYER_WIN', 'DRAW')
            ),
            
            stats AS (
                SELECT
                    user_id,
                    SUM(win) AS wins,
                    SUM(loss) AS losses,
                    SUM(draw) AS draws
                FROM player_games
                GROUP BY user_id
            )
            
            SELECT
                user_id,
                CASE
                    WHEN (losses + draws) = 0 THEN wins::double precision
                    ELSE wins::double precision / (losses + draws)
                END AS win_rate
            FROM stats
            WHERE user_id <> '00000000-0000-0000-0000-000000000001'
            ORDER BY win_rate DESC
            LIMIT :limit;
            """, nativeQuery = true)
    List<DtoLeaderBoard> findLeadersStats(@Param("limit") int limit);
}
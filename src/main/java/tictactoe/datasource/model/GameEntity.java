package tictactoe.datasource.model;

import tictactoe.datasource.mapper.FieldConverter;
import tictactoe.datasource.model.gamecomponents.DtoGameMode;
import tictactoe.datasource.model.gamecomponents.GameStateEmbeddable;
import tictactoe.datasource.model.gamecomponents.PlayerEmbeddable;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "game_sessions")
public class GameEntity {
    @Id
    private UUID id;

    @Convert(converter = FieldConverter.class)
    @Column(name = "game", nullable = false, columnDefinition = "TEXT")
    private int[][] field;

    @Embedded
    private GameStateEmbeddable state;

    @Enumerated(EnumType.STRING)
    private DtoGameMode mode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "playerId", column = @Column(name = "player1_id")),
            @AttributeOverride(name = "playerToken", column = @Column(name = "player1_token"))
    })
    private PlayerEmbeddable player1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "playerId", column = @Column(name = "player2_id")),
            @AttributeOverride(name = "playerToken", column = @Column(name = "player2_token"))
    })
    private PlayerEmbeddable player2;

    public GameEntity() {
    }

    public UUID getId() {
        return id;
    }

    public int[][] getField() {
        return field;
    }

    public GameStateEmbeddable getGameState() {
        return state;
    }

    public DtoGameMode getMode() {
        return mode;
    }

    public PlayerEmbeddable getPlayer1() {
        return player1;
    }

    public PlayerEmbeddable getPlayer2() {
        return player2;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setField(int[][] field) {
        this.field = field;
    }

    public void setGameState(GameStateEmbeddable state) {
        this.state = state;
    }

    public void setMode(DtoGameMode mode) {
        this.mode = mode;
    }

    public void setPlayer1(PlayerEmbeddable player1) {
        this.player1 = player1;
    }

    public void setPlayer2(PlayerEmbeddable player2) {
        this.player2 = player2;
    }
}

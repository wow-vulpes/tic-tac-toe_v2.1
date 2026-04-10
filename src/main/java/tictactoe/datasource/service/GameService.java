package tictactoe.datasource.service;

import tictactoe.datasource.mapper.GameMapper;
import tictactoe.datasource.model.GameEntity;
import tictactoe.datasource.model.gamecomponents.DtoGameMode;
import tictactoe.datasource.repository.GameRepository;
import tictactoe.domain.model.*;
import tictactoe.domain.model.gamecomponents.*;
import tictactoe.domain.service.GameServiceInterface;
import org.springframework.stereotype.Service;
import tictactoe.web.model.gamecomponents.DtoGameState;

import java.util.List;
import java.util.UUID;

@Service
public class GameService implements GameServiceInterface {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public GameService(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    private void saveGame(Game game) {
        gameRepository.save(gameMapper.toGameEntity(game));
    }

    private Game createGameWithAI(UUID playerId) {
        Game game = new Game(playerId);
        game.setMode(GameMode.PVE);
        game.setState(new GameState(State.PLAYER_TURN, game.getPlayer1().getPlayerId()));
        game.setPlayer2(new Player(DefaultValues.AI_ID, DefaultValues.Token.O));

        saveGame(game);
        return game;
    }

    private Game createGameWithPlayer(UUID player1Id) {
        Game game = new Game(player1Id);
        game.setMode(GameMode.PVP);
        game.setState(new GameState(State.WAITING_FOR_PLAYERS));

        saveGame(game);
        return game;
    }

    @Override
    public Game createGame(UUID playerId, GameMode mode) {
        return mode == GameMode.PVP ?
                createGameWithPlayer(playerId) : createGameWithAI(playerId);
    }

    @Override
    public List<UUID> getAvailableGames(UUID playerId) {
        return gameRepository.findAvailableGames(playerId);
    }

    @Override
    public Game getCurrentGame(UUID gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId).orElse(null);
        if (gameEntity != null) {
            return gameMapper.toGame(gameEntity);
        }

        throw new IllegalArgumentException("There's no game with that ID.");
    }

    @Override
    public Game joinTheGame(UUID gameId, UUID playerId) {
        Game game = getCurrentGame(gameId);

        if (playerId.equals(game.getPlayer1().getPlayerId())){
            throw new IllegalArgumentException("User can't play against themselves.");
        }

        if (game.getPlayer2() == null && game.getMode() == GameMode.PVP
                && game.getGameState().getState() == State.WAITING_FOR_PLAYERS) {
            game.setPlayer2(new Player(playerId, DefaultValues.Token.O));
            updateGameState(game);

            saveGame(game);
            return game;
        }

        throw new IllegalArgumentException("There's no available game with that ID.");
    }

    @Override
    public Game updateCurrentGame(UUID gameId, UUID playerId, Move newMove) {
        Game currentGame = getCurrentGame(gameId);

        if(currentGame.getGameState().getState() == State.WAITING_FOR_PLAYERS){
            throw new IllegalArgumentException("There're not enough players in the game");
        }

        if (isGameOver(currentGame)) {
            throw new IllegalArgumentException("Game is already over");
        }

        validatePlayerTurn(currentGame.getGameState(), playerId);
        validateGameField(currentGame, newMove);

        applyNewMove(currentGame, playerId, newMove);
        updateGameState(currentGame);

        if (currentGame.getMode() == GameMode.PVE && !isGameOver(currentGame)) {
            Move AI_move = getNextMove(currentGame);
            currentGame.updateGameField(AI_move, currentGame.getPlayer2().getPlayerToken());
            updateGameState(currentGame);
        }

        saveGame(currentGame);
        return currentGame;
    }

    // метод получения следующего хода текущей игры алгоритмом «Минимакс»;
    public Move getNextMove(Game game) {
        int[][] field = game.getGameField();

        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1, bestColumn = -1;

        for (int i = 0; i < DefaultValues.BOARD_SIZE; i++) {
            for (int j = 0; j < DefaultValues.BOARD_SIZE; j++) {
                if (field[i][j] == DefaultValues.Token.EMPTY.getValue()) {
                    field[i][j] = DefaultValues.Token.O.getValue();

                    int newScore = minimax(field, true);
                    if (newScore > bestScore) {
                        bestScore = newScore;
                        bestRow = i;
                        bestColumn = j;
                    }

                    field[i][j] = DefaultValues.Token.EMPTY.getValue();
                }
            }
        }

        if (bestRow >= 0 && bestColumn >= 0) {
            field[bestRow][bestColumn] = DefaultValues.Token.O.getValue();
            return new Move(bestRow, bestColumn);
        }

        throw new IllegalArgumentException("AI couldn't make a move.");
    }


    // метод валидации игрового поля текущей игры (проверь, что не изменены предыдущие ходы);
    private void validateGameField(Game game, Move newMove) throws IllegalArgumentException {
        int boardSize = game.getGameField().length;
        if ((newMove.getRow() < 0 || newMove.getCol() < 0) || (newMove.getRow() >= boardSize || newMove.getCol() >= boardSize)) {
            throw new IllegalArgumentException("Invalid move");
        }

        int[][] gameField = game.getGameField();

        if (gameField[newMove.getRow()][newMove.getCol()] != DefaultValues.Token.EMPTY.getValue()) {
            throw new IllegalArgumentException("Invalid move");
        }
    }


    // метод проверки окончания игры.
    private boolean isGameOver(Game game) {
        State gameState = game.getGameState().getState();
        return gameState == State.PLAYER_WIN || gameState == State.DRAW;
    }

    private void validatePlayerTurn(GameState state, UUID currentPlayer) {
        if (!state.getPlayerId().equals(currentPlayer)) {
            throw new IllegalArgumentException("Player " + currentPlayer + " tried to move out of turn.It was player " + state.getPlayerId() + " turn.");
        }
    }

    private int minimax(int[][] field, boolean isAI) {
        int winner = getWinner(field);
        if (winner != DefaultValues.Token.EMPTY.getValue()) {
            return (winner == DefaultValues.Token.O.getValue()) ? DefaultValues.GameScore.WIN.getValue() : DefaultValues.GameScore.LOSE.getValue();
        }

        if (canNotMove(field)) {
            return DefaultValues.GameScore.DRAW.getValue();
        }

        int bestScore;
        if (isAI) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < DefaultValues.BOARD_SIZE; i++) {
                for (int j = 0; j < DefaultValues.BOARD_SIZE; j++) {
                    if (field[i][j] == DefaultValues.Token.EMPTY.getValue()) {
                        field[i][j] = DefaultValues.Token.O.getValue();

                        bestScore = Math.max(bestScore, minimax(field, false));

                        field[i][j] = DefaultValues.Token.EMPTY.getValue();
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < DefaultValues.BOARD_SIZE; i++) {
                for (int j = 0; j < DefaultValues.BOARD_SIZE; j++) {
                    if (field[i][j] == DefaultValues.Token.EMPTY.getValue()) {
                        field[i][j] = DefaultValues.Token.X.getValue();

                        bestScore = Math.min(bestScore, minimax(field, true));

                        field[i][j] = DefaultValues.Token.EMPTY.getValue();
                    }
                }
            }
        }

        return bestScore;
    }


    private boolean canNotMove(int[][] field) {
        for (int[] row : field) {
            for (int cell : row) {
                if (cell == DefaultValues.Token.EMPTY.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getWinner(int[][] field) {
        // строки
        for (int i = 0; i < DefaultValues.BOARD_SIZE; i++) {
            if (hasWon(field, i, 0, 0, 1)) {
                return field[i][0];
            }
        }

        // столбцы
        for (int j = 0; j < DefaultValues.BOARD_SIZE; j++) {
            if (hasWon(field, 0, j, 1, 0)) {
                return field[0][j];
            }
        }

        // главная диагональ
        if (hasWon(field, 0, 0, 1, 1)) {
            return field[0][0];
        }

        // побочная диагональ
        if (hasWon(field, 0, DefaultValues.BOARD_SIZE - 1, 1, -1)) {
            return field[0][DefaultValues.BOARD_SIZE - 1];
        }

        return DefaultValues.Token.EMPTY.getValue();
    }

    private boolean hasWon(int[][] field, int startRow, int startCol, int dRow, int dCol) {
        if (field[startRow][startCol] == DefaultValues.Token.EMPTY.getValue()) {
            return false;
        }

        for (int i = 1; i < DefaultValues.BOARD_SIZE; i++) {
            if (field[startRow + i * dRow][startCol + i * dCol] != field[startRow][startCol]) {
                return false;
            }
        }

        return true;
    }

    private void applyNewMove(Game currentGame, UUID playerId, Move newMove) {
        DefaultValues.Token playerToken = playerId.equals(currentGame.getPlayer1().getPlayerId()) ?
                currentGame.getPlayer1().getPlayerToken() : currentGame.getPlayer2().getPlayerToken();

        currentGame.updateGameField(newMove, playerToken);
    }

    public void updateGameState(Game game) {
        GameState gameState = game.getGameState();
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        int winnerToken = getWinner(game.getGameField());
        if (winnerToken != DefaultValues.Token.EMPTY.getValue()) {
            gameState.setState(State.PLAYER_WIN);

            UUID winnerId = defineWinner(player1, player2, winnerToken);
            gameState.setPlayerId(winnerId);
            return;
        }

        if (canNotMove(game.getGameField())) {
            gameState.setState(State.DRAW);
            return;
        }

        if (player2 == null) {
            gameState.setState(State.WAITING_FOR_PLAYERS);
            return;
        }

        gameState.setState(State.PLAYER_TURN);
        UUID nextPlayer = defineNextPlayer(gameState.getPlayerId(), player1.getPlayerId(), player2.getPlayerId());
        gameState.setPlayerId(nextPlayer);
    }

    private UUID defineWinner(Player player1, Player player2, int winner) {
        if (winner == player1.getPlayerToken().getValue()) {
            return player1.getPlayerId();
        }

        return player2.getPlayerId();
    }

    private UUID defineNextPlayer(UUID currentPlayer, UUID player1, UUID player2) {
        if (currentPlayer != null) {
            return currentPlayer.equals(player1) ? player2 : player1;
        }

        return player1;
    }
}

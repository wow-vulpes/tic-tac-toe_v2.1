package tictactoe.domain.model.gamecomponents;

public class GameField {
    private final int[][] field;

    private final int boardSize = DefaultValues.BOARD_SIZE;

    public GameField(int[][] field){
        this.field = field;
    }

    public GameField(){
        field = new int[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                field[i][j] = DefaultValues.Token.EMPTY.getValue();
            }
        }
    }

    public int[][] getField() {
        return field;
    }

    public void updateField(Move newMove, DefaultValues.Token token){
        field[newMove.getRow()][newMove.getCol()] = token.getValue();
    }

    public int getBoardSize() {
        return boardSize;
    }
}

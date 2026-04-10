package tictactoe.web.model.gamecomponents;

public class DtoGameField {
    private int[][] field;

    public DtoGameField(){}

    public DtoGameField(int[][] field){
        this.field = field;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }
}
package tictactoe.web.model;

public class MoveRequest {
    int x, y;

    public MoveRequest(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
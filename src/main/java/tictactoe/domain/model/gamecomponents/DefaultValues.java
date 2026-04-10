package tictactoe.domain.model.gamecomponents;

import java.util.UUID;

public class DefaultValues {
    public static final int BOARD_SIZE = 3;

    public static final UUID AI_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public enum Token {
        EMPTY(0),
        X(1),
        O(2);

        private final int value;

        Token(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    //считается относительно AI
    public enum GameScore {
        WIN(10),
        LOSE(-10),
        DRAW(0);

        private final int value;

        GameScore(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

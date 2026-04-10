package tictactoe.web.model;

import tictactoe.web.model.gamecomponents.DtoGameMode;

public class CreateRequest {
    private DtoGameMode mode;

    public DtoGameMode getMode() {
        return mode;
    }

    public void setMode(DtoGameMode mode) {
        this.mode = mode;
    }
}

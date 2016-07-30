package carcassonne.control;

import carcassonne.model.Round;
import carcassonne.view.MainGUI;

public class MainController {
    GameOptions options = GameOptions.getInstance();
    MainGUI gui;
    Round currentRound;

    public MainController() {
        gui = new MainGUI(this);
        currentRound = new Round(2, options.getGridWidth(), options.getGridHeight());
    }

    public void newGame(int playerCount) {
        currentRound = new Round(playerCount, options.getGridWidth(), options.getGridHeight());
        gui.rebuildLabelGrid();
    }

}

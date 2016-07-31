package carcassonne.control;

import carcassonne.model.Round;
import carcassonne.view.MainGUI;

/**
 * Main controller for the game. This class is not a classic controller class from the
 * Model/View/Controller Architecture. It gets the user input from all view classes and notifies
 * both the model and the view. It contains state information. TODO create state machine - make
 * multiple state classes - make abstract state - implement the state machine functions
 * @author Timur
 */
public class MainController {
    GameOptions options = GameOptions.getInstance();
    MainGUI gui;
    Round currentRound;

    /**
     * Basic constructor. Creates the view and the model of the game.
     */
    public MainController() {
        gui = new MainGUI(this);
        currentRound = new Round(2, options.getGridWidth(), options.getGridHeight());
    }

    /**
     * Starts new round with a specific amount of players.
     * @param playerCount sets the amount of players.
     */
    public void newGame(int playerCount) {
        currentRound = new Round(playerCount, options.getGridWidth(), options.getGridHeight());
        gui.rebuildLabelGrid();
    }

    /**
     * Methods for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void requestTilePlacement(int x, int y) {

    }

    /**
     * Method for the view to call if a user mans a tile with a meeple.
     */
    public void placeMeeple() {

    }

}

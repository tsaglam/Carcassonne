package carcassonne.control;

import carcassonne.model.Round;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.view.MainGUI;
import carcassonne.view.PlacementGUI;
import carcassonne.view.RotationGUI;
import carcassonne.view.SecondaryGUI;

/**
 * Main controller for the game. This class is not a classic controller class from the
 * Model/View/Controller Architecture. It gets the user input from all view classes and notifies
 * both the model and the view. It contains state information. TODO (MEDIUM) create state machine -
 * make multiple state classes - make abstract state - implement the state machine functions
 * @author Timur
 */
public class MainController {
    GameOptions options = GameOptions.getInstance();
    MainGUI gui;
    RotationGUI rotationGUI;
    SecondaryGUI placementGUI;
    Round currentRound;

    /**
     * Basic constructor. Creates the view and the model of the game.
     */
    public MainController() {
        gui = new MainGUI(this);
        rotationGUI = new RotationGUI(this);
        placementGUI = new PlacementGUI(this);
        currentRound = new Round(2, options.getGridWidth(), options.getGridHeight());
    }

    /**
     * Starts new round with a specific amount of players.
     * @param playerCount sets the amount of players.
     */
    public void newGame(int playerCount) {
        currentRound = new Round(playerCount, options.getGridWidth(), options.getGridHeight());
        gui.rebuildLabelGrid();
        // TODO (MEDIUM) get the control or GUI to draw the tile.
    }

    /**
     * Method for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     * @return true if request was granted.
     */
    public boolean requestTilePlacement(int x, int y) {
        Tile tile = rotationGUI.useTile();
        // if (t can be placed) {
        // round.place(t);
        placementGUI.setTile(tile);
        // return true;
        // } TODO (MEDIUM) implement tile placement.
        return false;
    }

    /**
     * Method for the view to call if the user wants to skip a round.
     * @return true if request was granted.
     */
    public boolean requestSkip() {
        currentRound.nextTurn();
        return true;
    }

    /**
     * Method for the view to call if a user mans a tile with a meeple.
     */
    public boolean requestMeeplePlacement(GridDirection position) {
        // TODO (MEDIUM) implement meeple placement.
        System.out.println("requestMeeplePlacement: " + position);
        return false;
    }

}

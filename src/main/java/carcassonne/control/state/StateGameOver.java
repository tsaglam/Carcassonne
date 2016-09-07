package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.view.main.MainGUI;
import carcassonne.view.main.menubar.Scoreboard;
import carcassonne.view.secondary.GameMessage;
import carcassonne.view.secondary.GameStatisticsGUI;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * The specific state where the statistics are shown can be placed.
 * @author Timur Saglam
 */
public class StateGameOver extends AbstractControllerState {

    private GameStatisticsGUI gameStatistics;

    /**
     * Constructor of the state.
     * @param controller sets the Controller
     * @param mainGUI sets the MainGUI
     * @param rotationGUI sets the RotationGUI
     * @param placementGUI sets the PlacementGUI
     * @param scoreboard sets the Scoreboard
     */
    public StateGameOver(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Scoreboard scoreboard) {
        super(controller, mainGUI, rotationGUI, placementGUI, scoreboard);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#abortGame()
     */
    @Override
    public void abortGame() {
        GameMessage.showWarning("You already aborted the current game. Close the game statistics to start a new game.");
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#newGame()
     */
    @Override
    public void newGame(int playerCount) {
        exit();
        changeState(StateIdle.class);
        startNewRound(playerCount);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#placeMeeple()
     */
    @Override
    public void placeMeeple(GridDirection position) {
        throw new IllegalStateException("Placing meeples in StateGameOver is not allowed.");
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#placeTile()
     */
    @Override
    public void placeTile(int x, int y) {
        // do nothing.
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#skip()
     */
    @Override
    public void skip() {
        scoreboard.disable();
        exit();
        changeState(StateIdle.class);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#entry()
     */
    @Override
    protected void entry() {
        System.out.println("FINAL PATTERNS:"); // TODO (HIGH) remove debug output
        for (GridPattern pattern : grid.getAllPatterns()) {
            System.out.println(pattern); // TODO (HIGH) remove debug output
            pattern.forceDisburse();
        }
        updateScores();
        GameMessage.showMessage("The game is over. Winning player(s): " + round.getWinningPlayers());
        gameStatistics = new GameStatisticsGUI(controller, round);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#exit()
     */
    @Override
    protected void exit() {
        gameStatistics.closeGUI();
    }

}

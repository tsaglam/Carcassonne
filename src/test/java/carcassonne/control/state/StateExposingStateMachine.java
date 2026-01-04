package carcassonne.control.state;

import carcassonne.model.Player;
import carcassonne.model.Round;
import carcassonne.model.ai.RuleBasedAI;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.TileStack;
import carcassonne.settings.GameSettings;
import carcassonne.testutils.GridPrinter;
import carcassonne.view.ViewFacade;

/**
 * A {@link StateMachine} implementation that exposes internal game-state objects such as the current {@link Round},
 * {@link TileStack}, and {@link Grid}. This class is primarily intended for testing and debugging, allowing external
 * code to inspect the state that is normally encapsulated inside the state machine. The exposed fields are refreshed
 * whenever {@link #updateStates(Round, TileStack, Grid)} is invoked as part of the state machine lifecycle.
 */
public class StateExposingStateMachine extends StateMachine {
    private static final String STATE_CHANGE_MESSAGE = " --> State is ";
    private static final String STATE_PREFIX = "State";
    private static final String PLAYER_FORMAT = ", turn of player %d (%s with %d free meeples and %d points):";
    private static final String EMPTY_STRING = "";

    private Round round;
    private TileStack tileStack;
    private Grid grid;
    private final boolean printGridOnTransitions;
    private String lastPrintedGrid;
    private Class<? extends AbstractGameState> currenState;

    /**
     * Constructor using the specified view facade and game settings. A {@link RuleBasedAI} is automatically created based
     * on the provided settings.
     * @param views the view facade used for UI interactions. Can be a test facade.
     * @param settings are the settings for the game.
     * @param printGridOnTransitions controls whether the ASCII grid representation should be printed between state
     * transitions.
     */
    public StateExposingStateMachine(ViewFacade views, GameSettings settings, boolean printGridOnTransitions) {
        super(views, new RuleBasedAI(settings), settings);
        this.printGridOnTransitions = printGridOnTransitions;
        currenState = StateIdle.class;
    }

    @Override
    void updateStates(Round round, TileStack tileStack, Grid grid) {
        this.round = round;
        this.tileStack = tileStack;
        this.grid = grid;
        super.updateStates(round, tileStack, grid);
    }

    @Override
    void changeState(Class<? extends AbstractGameState> stateType) {
        if (printGridOnTransitions && grid != null) {
            String state = currenState.getSimpleName().replace(STATE_PREFIX, EMPTY_STRING);
            Player player = round.getActivePlayer();
            String playerDescription = round == null ? EMPTY_STRING
                    : PLAYER_FORMAT.formatted(player.getNumber(), player.getName(), player.getFreeMeeples(), player.getScore());
            System.out.println(STATE_CHANGE_MESSAGE + state + playerDescription);
            String currentGrid = GridPrinter.printGrid(grid);
            if (!currentGrid.equals(lastPrintedGrid)) {
                System.out.print(currentGrid);
                lastPrintedGrid = currentGrid;
            }
        }
        currenState = stateType;
        super.changeState(stateType);
    }

    /**
     * Returns the current {@link Round} instance stored by the state machine.
     * @return the current round, or {@code null} if not yet initialized
     */
    public Round getRound() {
        return round;
    }

    /**
     * Returns the current {@link TileStack} instance stored by the state machine.
     * @return the current tile stack, or {@code null} if not yet initialized
     */
    public TileStack getTileStack() {
        return tileStack;
    }

    /**
     * Returns the current {@link Grid} instance stored by the state machine.
     * @return the current grid, or {@code null} if not yet initialized
     */
    public Grid getGrid() {
        return grid;
    }

}

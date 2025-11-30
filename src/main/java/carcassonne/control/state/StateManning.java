package carcassonne.control.state;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.ai.AbstractCarcassonneMove;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.view.ViewFacade;
import carcassonne.view.main.MainView;
import carcassonne.view.util.GameMessage;

/**
 * The specific state when a Meeple can be placed.
 * @author Timur Saglam
 */
public class StateManning extends AbstractGameState {
    private final boolean[] noMeeplesNotification;

    /**
     * Constructor of the state.
     * @param stateMachine is the state machine managing this state.
     * @param settings are the game settings.
     * @param views contains the user interfaces.
     * @param playerAI is the current AI strategy.
     */
    public StateManning(StateMachine stateMachine, GameSettings settings, ViewFacade views, ArtificialIntelligence playerAI) {
        super(stateMachine, settings, views, playerAI);
        noMeeplesNotification = new boolean[GameSettings.MAXIMAL_PLAYERS]; // stores whether a player was already notified about a lack of meeples
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#abortGame()
     */
    @Override
    public void abortGame() {
        changeState(StateGameOver.class);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#newRound()
     */
    @Override
    public void newRound(int playerCount) {
        views.reroute(() -> GameMessage.showWarning("Abort the current game before starting a new one."));
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#placeMeeple()
     */
    @Override
    public void placeMeeple(GridDirection position) {
        if (!round.getActivePlayer().isComputerControlled()) {
            Tile tile = views.getSelectedTile();
            views.onMainView(it -> it.resetMeeplePreview(tile));
            placeMeeple(position, tile);
        }
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#placeTile()
     */
    @Override
    public void placeTile(int x, int y) {
        // do nothing.
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#skip()
     */
    @Override
    public void skip() {
        if (!round.getActivePlayer().isComputerControlled()) {
            skipPlacingMeeple();
        }
    }

    private void placeMeeple(GridDirection position, Tile tile) {
        Player player = round.getActivePlayer();
        if (player.hasFreeMeeples() && tile.allowsPlacingMeeple(position, player, settings)) {
            tile.placeMeeple(player, position, settings);
            views.onMainView(it -> it.setMeeple(tile, position, player));
            updateScores();
            processGridPatterns();
            startNextTurn();
        } else {
            views.reroute(() -> GameMessage.showWarning("You can't place meeple directly on an occupied Castle or Road!"));
        }
    }

    private void skipPlacingMeeple() {
        if (!round.getActivePlayer().isComputerControlled()) {
            Tile tile = views.getSelectedTile();
            views.onMainView(it -> it.resetMeeplePreview(tile));
        }
        processGridPatterns();
        startNextTurn();
    }

    // gives the players the points they earned.
    private void processGridPatterns() {
        Tile tile = getSelectedTile();
        for (GridPattern pattern : grid.getModifiedPatterns(tile.getGridSpot())) {
            if (pattern.isComplete()) {
                for (Meeple meeple : pattern.getMeepleList()) {
                    GridSpot spot = meeple.getLocation();
                    views.onMainView(it -> it.removeMeeple(spot.getX(), spot.getY()));
                }
                pattern.disburse(settings.getSplitPatternScore());
                updateScores();
            }
        }
    }

    // starts the next turn and changes the state to state placing.
    private void startNextTurn() {
        if (round.isOver()) {
            changeState(StateGameOver.class);
        } else {
            if (!round.getActivePlayer().isComputerControlled()) {
                views.onMainView(MainView::resetPlacementHighlights);
            }
            round.nextTurn();
            views.onMainView(it -> it.setCurrentPlayer(round.getActivePlayer()));
            changeState(StatePlacing.class);
        }
    }

    private void placeMeepleWithAI() {
        AbstractCarcassonneMove move = playerAI.getCurrentMove().orElseThrow(() -> new IllegalStateException(NO_MOVE));
        if (move.involvesMeeplePlacement()) {
            placeMeeple(move.getMeeplePosition(), move.getOriginalTile());
        } else {
            skipPlacingMeeple();
        }
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#entry()
     */
    @Override
    protected void entry() {
        Player player = round.getActivePlayer();
        Tile selectedTile = getSelectedTile();
        if (player.hasFreeMeeples()) {
            noMeeplesNotification[player.getNumber()] = false; // resets out of meeple message!
            if (player.isComputerControlled()) {
                placeMeepleWithAI();
            } else {
                views.onMainView(it -> it.setMeeplePreview(selectedTile, player));
                views.onMeepleView(it -> it.setTile(selectedTile, player));
            }
        } else {
            if (!noMeeplesNotification[player.getNumber()] && !player.isComputerControlled()) { // Only warn player once until he regains meeples
                views.reroute(
                        () -> GameMessage.showMessage("You have no Meeples left. Regain Meeples by completing patterns to place Meepeles again."));
                noMeeplesNotification[player.getNumber()] = true;
            }
            processGridPatterns();
            startNextTurn();
        }
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#exit()
     */
    @Override
    protected void exit() {
        views.onMeepleView(it -> it.setVisible(false));
    }
}

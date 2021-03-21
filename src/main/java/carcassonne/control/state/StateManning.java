package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.ai.CarcassonneMove;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.view.main.MainGUI;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.PreviewGUI;
import carcassonne.view.util.GameMessage;

/**
 * The specific state when a Meeple can be placed.
 * @author Timur Saglam
 */
public class StateManning extends AbstractGameState {
    private boolean[] noMeeplesNotification;

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     * @param mainGUI sets the MainGUI
     * @param previewGUI sets the PreviewGUI
     * @param placementGUI sets the PlacementGUI
     */
    public StateManning(MainController controller, MainGUI mainGUI, PreviewGUI previewGUI, PlacementGUI placementGUI,
            ArtificialIntelligence playerAI) {
        super(controller, mainGUI, previewGUI, placementGUI, playerAI);
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
     * @see carcassonne.control.state.AbstractGameState#isPlaceable()
     */
    @Override
    public boolean isPlaceable(GridDirection position) {
        return getSelectedTile().allowsPlacingMeeple(position, round.getActivePlayer(), controller.getSettings());
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#newRound()
     */
    @Override
    public void newRound(int playerCount) {
        GameMessage.showWarning("Abort the current game before starting a new one.");
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#placeMeeple()
     */
    @Override
    public void placeMeeple(GridDirection position) {
        Tile tile = previewGUI.getSelectedTile();
        mainGUI.resetMeeplePreview(tile);
        placeMeeple(position, tile);
    }

    private void placeMeeple(GridDirection position, Tile tile) {
        Player player = round.getActivePlayer();
        if (player.hasFreeMeeples() && isPlaceable(position)) {
            tile.placeMeeple(player, position, controller.getSettings());
            mainGUI.setMeeple(tile, position, player);
            updateScores();
            processGridPatterns();
            startNextTurn();
        } else {
            GameMessage.showWarning("You can't place meeple directly on an occupied Castle or Road!");
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
        mainGUI.resetMeeplePreview(previewGUI.getSelectedTile());
        processGridPatterns();
        startNextTurn();
    }

    // gives the players the points they earned.
    private void processGridPatterns() {
        Tile tile = getSelectedTile();
        for (GridPattern pattern : grid.getModifiedPatterns(tile.getGridSpot())) {
            if (pattern.isComplete()) {
                for (Meeple meeple : pattern.getMeepleList()) {
                    mainGUI.removeMeeple(meeple);
                }
                pattern.disburse();
                updateScores();
            }
        }
    }

    // starts the next turn and changes the state to state placing.
    private void startNextTurn() {
        if (round.isOver()) {
            changeState(StateGameOver.class);
        } else {
            round.nextTurn();
            mainGUI.setCurrentPlayer(round.getActivePlayer());
            changeState(StatePlacing.class);
        }
    }

    private void placeMeepleWithAI() {
        CarcassonneMove move = playerAI.getCurrentMove().orElseThrow(() -> new IllegalStateException(NO_MOVE));
        if (move.involvesMeeplePlacement()) {
            placeMeeple(move.getPosition(), move.getTile());
        } else {
            skip();
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
                mainGUI.setMeeplePreview(selectedTile, player);
                placementGUI.setTile(selectedTile, player);
            }
        } else {
            if (!noMeeplesNotification[player.getNumber()] && !player.isComputerControlled()) { // Only warn player once until he regains meeples
                GameMessage.showMessage("You have no Meeples left. Regain Meeples by completing patterns to place Meepeles again.");
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
        placementGUI.setVisible(false);
    }
}

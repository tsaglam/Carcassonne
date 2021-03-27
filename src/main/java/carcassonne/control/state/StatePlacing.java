package carcassonne.control.state;

import java.util.Optional;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.ai.CarcassonneMove;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.view.main.MainGUI;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.PreviewGUI;
import carcassonne.view.util.GameMessage;

/**
 * The specific state when a Tile can be placed.
 * @author Timur Saglam
 */
public class StatePlacing extends AbstractGameState {

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     * @param mainGUI sets the MainGUI
     * @param previewGUI sets the PreviewGUI
     * @param placementGUI sets the PlacementGUI
     */
    public StatePlacing(MainController controller, MainGUI mainGUI, PreviewGUI previewGUI, PlacementGUI placementGUI,
            ArtificialIntelligence playerAI) {
        super(controller, mainGUI, previewGUI, placementGUI, playerAI);
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
        return false; // can never place meeple in this state.
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
        throw new IllegalStateException("Placing meeples in StatePlacing is not allowed.");
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#placeTile()
     */
    @Override
    public void placeTile(int x, int y) {
        Tile tile = previewGUI.getSelectedTile();
        placeTile(tile, x, y);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#skip()
     */
    @Override
    public void skip() {
        if (round.isOver()) {
            changeState(StateGameOver.class);
        } else {
            Tile tile = getTileToDrop(); // TODO (HIGH) AI players cannot drop tiles right now, which one to skip?
            tileStack.putBack(tile);
            if (!round.getActivePlayer().dropTile(tile)) {
                throw new IllegalStateException("Cannot drop tile " + tile + "from player " + round.getActivePlayer());
            }
            round.nextTurn();
            mainGUI.setCurrentPlayer(round.getActivePlayer());
            entry();
        }
    }

    private void placeTile(Tile tile, int x, int y) {
        if (grid.place(x, y, tile)) {
            round.getActivePlayer().dropTile(tile);
            mainGUI.setTile(tile, x, y);
            GridSpot spot = grid.getSpot(x, y);
            highlightSurroundings(spot);
            changeState(StateManning.class);
        }
    }

    private void placeTileWithAI(Player player) {
        Optional<CarcassonneMove> bestMove = playerAI.calculateBestMoveFor(player.getHandOfTiles(), player, grid, tileStack);
        if (bestMove.isEmpty() || bestMove.get().getValue() < 0) {
            skip();
        }
        bestMove.ifPresent(it -> {
            Tile tile = it.getTile();
            tile.rotateTo(it.getRotation());
            placeTile(tile, it.getX(), it.getY());
        });
    }

    private Tile getTileToDrop() {
        if (round.getActivePlayer().isComputerControlled()) {
            return playerAI.chooseTileToDrop(round.getActivePlayer().getHandOfTiles());
        }
        return previewGUI.getSelectedTile();
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#entry()
     */
    @Override
    protected void entry() {
        Player player = round.getActivePlayer();
        if (!player.hasFullHand() && !tileStack.isEmpty()) {
            player.addTile(tileStack.drawTile());
        }
        updateStackSize();
        if (player.isComputerControlled()) {
            placeTileWithAI(player);
        } else {
            previewGUI.setTiles(player);
        }
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#exit()
     */
    @Override
    protected void exit() {
        previewGUI.setVisible(false);
    }

}

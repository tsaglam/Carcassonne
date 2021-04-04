package carcassonne.control.state;

import java.util.Optional;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.ai.AbstractCarcassonneMove;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.view.ViewFacade;
import carcassonne.view.util.GameMessage;

/**
 * The specific state when a Tile can be placed.
 * @author Timur Saglam
 */
public class StatePlacing extends AbstractGameState {

    private static final int SLEEP_DURATION = 10;

    /**
     * Constructor of the state.
     * @param controller is the game controller.
     * @param views contains the user interfaces.
     * @param playerAI is the current AI strategy.
     */
    public StatePlacing(MainController controller, ViewFacade views, ArtificialIntelligence playerAI) {
        super(controller, views, playerAI);
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
        Tile tile = views.getSelectedTile();
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
            Tile tile = getTileToDrop();
            tileStack.putBack(tile);
            if (!round.getActivePlayer().dropTile(tile)) {
                throw new IllegalStateException("Cannot drop tile " + tile + "from player " + round.getActivePlayer());
            }
            round.nextTurn();
            views.onMainView(it -> it.setCurrentPlayer(round.getActivePlayer()));
            entry();
        }
    }

    private void placeTile(Tile tile, int x, int y) {
        if (grid.place(x, y, tile)) {
            round.getActivePlayer().dropTile(tile);
            views.onMainView(it -> it.setTile(tile, x, y));
            GridSpot spot = grid.getSpot(x, y);
            highlightSurroundings(spot);
            changeState(StateManning.class);
        }
    }

    private void placeTileWithAI(Player player) {
        Optional<AbstractCarcassonneMove> bestMove = playerAI.calculateBestMoveFor(player.getHandOfTiles(), player, grid, tileStack);
        if (bestMove.isEmpty() || bestMove.get().getValue() < 0) {
            skip();
        }
        bestMove.ifPresent(it -> {
            Tile tile = it.getOriginalTile();
            tile.rotateTo(it.getRequiredTileRotation());
            placeTile(tile, it.getX(), it.getY());
        });
    }

    private Tile getTileToDrop() {
        if (round.getActivePlayer().isComputerControlled()) {
            return playerAI.chooseTileToDrop(round.getActivePlayer().getHandOfTiles());
        }
        return views.getSelectedTile();
    }

    private void waitForUI() {
        while (views.isBusy()) {
            try {
                Thread.sleep(SLEEP_DURATION);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
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
        if (player.isComputerControlled()) { // TODO (HIGH) [AI] Slow with timer between two AI players.
            waitForUI();
            placeTileWithAI(player);
        } else {
            views.onTileView(it -> it.setTiles(player));
        }
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#exit()
     */
    @Override
    protected void exit() {
        views.onTileView(it -> it.setVisible(false));
    }

}

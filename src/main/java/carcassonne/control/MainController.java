package carcassonne.control;

import carcassonne.model.Player;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.view.MainGUI;
import carcassonne.view.PlacementGUI;
import carcassonne.view.RotationGUI;
import carcassonne.view.SecondaryGUI;

/**
 * TODO (MEDIUM) comment this class.
 * 
 * @author Timur
 */
public class MainController {

	private GameOptions options = GameOptions.getInstance();
	private MainGUI gui;
	private RotationGUI rotationGUI;
	private SecondaryGUI placementGUI;
	private Round round;
	private Grid grid;

	/**
	 * 
	 */
	public MainController() {
		gui = new MainGUI(this);
		rotationGUI = new RotationGUI(this);
		placementGUI = new PlacementGUI(this);
		newGame(2);
	}

	public void newGame(int playerCount) {
		grid = new Grid(options.gridWidth, options.gridHeight, options.foundationType);
		round = new Round(playerCount, grid);
		gui.rebuildLabelGrid();
		gui.set(round.getCurrentTile(), options.gridCenterX, options.gridCenterY);
		placementGUI.setTile(round.getCurrentTile());
	}

	/**
	 * Method for the view to call if a user places a tile.
	 * 
	 * @param x is the x coordinate.
	 * @param y is the y coordinate.
	 * @return true if request was granted.
	 */
	public boolean requestTilePlacement(int x, int y) {
		Tile tile = rotationGUI.useTile();
		if (grid.place(x, y, tile)) {
			round.updateCurrentTile(tile);
			gui.set(tile, x, y);
			rotationGUI.disableFrame();
			placementGUI.setTile(tile); // TODO (MEDIUM) only if player has meeples else show message box
			return true;
		}
		return false;
	}

	/**
	 * Method for the view to call if the user wants to skip a round.
	 * 
	 * @return true if request was granted.
	 */
	public boolean requestSkip() {
		rotationGUI.disableFrame(); // TODO (MEDIUM) test skipping
		round.nextTurn();
		return true;
	}

	/**
	 * Method for the view to call if a user mans a tile with a meeple.
	 */
	public boolean requestMeeplePlacement(GridDirection position) {
		Player player = round.getActivePlayer();
		if (player.hasUnusedMeeples()) {
			player.placeMeepleAt(round.getCurrentTile(), position);
			gui.set(null, 0, 0, position); // TODO (HIGH) get real information
			placementGUI.disableFrame();
			round.nextTurn();
			rotationGUI.setTile(round.getCurrentTile());
			return true;
		}
		return false;
	}
}

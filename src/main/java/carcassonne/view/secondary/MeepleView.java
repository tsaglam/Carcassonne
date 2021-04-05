package carcassonne.view.secondary;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import carcassonne.control.ControllerFacade;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.util.ImageLoadingUtil;
import carcassonne.view.main.MainView;
import carcassonne.view.util.MouseClickListener;
import carcassonne.view.util.ThreadingUtil;

/**
 * A view for the placement of Meeples on the Tile that was placed previously.
 * @author Timur Saglam
 */
public class MeepleView extends SecondaryView {
    private static final long serialVersionUID = 1449264387665531286L;
    private Map<GridDirection, JButton> meepleButtons;
    private Color defaultButtonColor;
    private Tile tile;

    /**
     * Creates the view.
     * @param controller is the game controller.
     * @param ui is the main view.
     */
    public MeepleView(ControllerFacade controller, MainView ui) {
        super(controller, ui);
        buildButtonSkip();
        buildButtonGrid();
        pack();
    }

    /**
     * Sets the tile of the view, updates the view and then makes it visible. Should be called to show the view. The method
     * implements the template method pattern using the method <code>update()</code>.
     * @param tile sets the tile.
     * @param currentPlayer sets the color scheme according to the player.
     */
    public void setTile(Tile tile, Player currentPlayer) {
        if (tile == null) {
            throw new IllegalArgumentException("Tried to set the tile of the " + getClass().getSimpleName() + " to null.");
        }
        this.tile = tile;
        setCurrentPlayer(currentPlayer);
        ThreadingUtil.runAndCallback(() -> updatePlacementButtons(), () -> showUI());
    }

    // build button grid
    private void buildButtonGrid() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        meepleButtons = new HashMap<>();
        int index = 0;
        for (GridDirection direction : GridDirection.byRow()) {
            JButton button = new PlacementButton(controller, direction);
            button.setToolTipText("Place Meeple on the " + direction.toReadableString() + " of the tile.");
            constraints.gridx = index % 3; // from 0 to 2
            constraints.gridy = index / 3 + 1; // from 1 to 3
            dialogPanel.add(button, constraints);
            meepleButtons.put(direction, button);
            index++;
        }
    }

    private void buildButtonSkip() {
        JButton buttonSkip = new JButton(ImageLoadingUtil.SKIP.createHighDpiImageIcon());
        buttonSkip.setToolTipText("Don't place meeple and preserve for later use");
        defaultButtonColor = buttonSkip.getBackground();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 3;
        dialogPanel.add(buttonSkip, constraints);
        buttonSkip.addMouseListener((MouseClickListener) event -> controller.requestSkip());
    }

    /**
     * Updates the meepleButtons to reflect the current placement options.
     */
    private void updatePlacementButtons() {
        for (GridDirection direction : GridDirection.values()) {
            TerrainType terrain = tile.getTerrain(direction);
            Boolean placeable = tile.hasMeepleSpot(direction) && controller.getSettings().getMeepleRule(terrain);
            JButton button = meepleButtons.get(direction);
            if (placeable) {
                button.setIcon(ImageLoadingUtil.createHighDpiImageIcon(GameSettings.getMeeplePath(terrain, false)));
            } else {
                button.setIcon(ImageLoadingUtil.createHighDpiImageIcon(GameSettings.getMeeplePath(TerrainType.OTHER, false)));
            }
            if (placeable && tile.allowsPlacingMeeple(direction, currentPlayer, controller.getSettings())) {
                button.setEnabled(true);
                button.setBackground(defaultButtonColor);
            } else {
                button.setEnabled(false);
                button.setBackground(currentPlayer.getColor().lightColor());
            }
        }
    }
}
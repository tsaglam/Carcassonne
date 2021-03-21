package carcassonne.view.secondary;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;
import carcassonne.util.ImageLoadingUtil;
import carcassonne.view.main.MainGUI;
import carcassonne.view.util.MouseClickListener;

/**
 * GUI class for the tile orientation. It lets the user look at the tile to place and rotate it both right and left.
 * @author Timur Saglam
 */
public class PreviewGUI extends SecondaryGUI {
    private static final long serialVersionUID = -5179683977081970564L;
    private static final int BOTTOM_SPACE = 5;
    private static final int VERTICAL_SPACE = 10;
    private static final double SELECTION_FACTOR = 0.9;
    private static final int SELECTION_BORDER_WIDTH = 3;
    private static final String TILE_TOOL_TIP = "Tile of type ";
    private final int selectionSize;
    private final int defaultSize;
    private JButton buttonRotateLeft;
    private JButton buttonRotateRight;
    private JButton buttonSkip;
    private List<JLabel> tileLabels;
    private List<Tile> tiles;
    private int selectionIndex;

    /**
     * Simple constructor which uses the constructor of the <code>SmallGUI</code>.
     * @param controller is the game controller.
     * @param ui is the main GUI.
     */
    public PreviewGUI(MainController controller, MainGUI ui) {
        super(controller, ui);
        buildContent();
        pack();
        selectionSize = dialogPanel.getWidth() - VERTICAL_SPACE;
        defaultSize = (int) (selectionSize * SELECTION_FACTOR);
    }

    /**
     * Returns the tile correlating to the selected tile label.
     * @return the tile.
     */
    public Tile getSelectedTile() {
        return tiles.get(selectionIndex);
    }

    /**
     * If the UI is active, rotates the tile to the left.
     */
    public void rotateLeft() {
        if (isVisible() && selectionIndex < tiles.size()) {
            tiles.get(selectionIndex).rotateLeft();
            updateTileLabel(selectionIndex);
        }
    }

    /**
     * If the UI is active, rotates the tile to the right.
     */
    public void rotateRight() {
        if (isVisible() && selectionIndex < tiles.size()) {
            tiles.get(selectionIndex).rotateRight();
            updateTileLabel(selectionIndex);
        }
    }

    /**
     * Selects the next tile label above the current selected one.
     */
    public void selectAbove() {
        if (selectionIndex > 0) {
            selectTileLabel(selectionIndex - 1);
        }
    }

    /**
     * Selects the next tile label below the current selected one.
     */
    public void selectBelow() {
        if (selectionIndex + 1 < tiles.size()) {
            selectTileLabel(selectionIndex + 1);
        }

    }

    /**
     * Sets the tiles of the GUI to the tiles of the current player, updates the GUI and then makes it visible. Should be
     * called to show the GUI.
     * @param currentPlayer is the active player.
     */
    public void setTiles(Player currentPlayer) {
        tiles.clear();
        tiles.addAll(currentPlayer.getHandOfTiles());
        setCurrentPlayer(currentPlayer);
        updatePreviewLabels();
        showUI();
    }

    @Override
    public void notifyChange() {
        super.notifyChange();
        if (!tiles.isEmpty()) {
            updateTileLabel(selectionIndex);
        }
    }

    /**
     * Selects a specific tile label, increasing its size and adding a border. Resets the previous selection.
     * @param index is the index of the label. If the index is not valid nothing will happen.
     */
    public void selectTileLabel(int index) {
        if (index < tiles.size()) {
            int oldSelection = selectionIndex;
            selectionIndex = index;
            updateTileLabel(index);
            updateTileLabel(oldSelection);
        }
    }

    // build the GUI content
    private void buildContent() {
        // create buttons:
        buttonSkip = new JButton(ImageLoadingUtil.SKIP.createHighDpiImageIcon());
        buttonRotateLeft = new JButton(ImageLoadingUtil.LEFT.createHighDpiImageIcon());
        buttonRotateRight = new JButton(ImageLoadingUtil.RIGHT.createHighDpiImageIcon());
        // set tool tips:
        buttonSkip.setToolTipText("Don't place tile and skip turn");
        buttonRotateLeft.setToolTipText("Rotate left");
        buttonRotateRight.setToolTipText("Rotate right");
        // set listeners:
        buttonSkip.addMouseListener((MouseClickListener) event -> controller.requestSkip());
        buttonRotateLeft.addMouseListener((MouseClickListener) event -> rotateLeft());
        buttonRotateRight.addMouseListener((MouseClickListener) event -> rotateRight());
        // set constraints:
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weightx = 1; // keeps buttons evenly positioned.
        // add buttons:
        dialogPanel.add(buttonRotateLeft, constraints);
        dialogPanel.add(buttonSkip, constraints);
        dialogPanel.add(buttonRotateRight, constraints);
        // change constraints and add label:
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        ImageIcon defaultImage = new Tile(TileType.Null).getScaledIcon(50);
        tileLabels = new ArrayList<>();
        tiles = new ArrayList<>();
        for (int i = 0; i < GameSettings.MAXIMAL_TILES_ON_HAND; i++) {
            JLabel label = new JLabel(defaultImage);
            tileLabels.add(label);
            constraints.gridy++;
            final int index = i;
            label.addMouseListener((MouseClickListener) event -> selectTileLabel(index));
            dialogPanel.add(label, constraints);
        }
        constraints.gridy++;
        dialogPanel.add(Box.createVerticalStrut(BOTTOM_SPACE), constraints);
    }

    // Updates the image of a specific tile label.
    private void updateTileLabel(int index) {
        boolean singleTile = tiles.size() == 1;
        boolean selected = index == selectionIndex; // is the label selected or not?
        ImageIcon icon = tiles.get(index).getScaledIcon(selected || singleTile ? selectionSize : defaultSize);
        tileLabels.get(index).setToolTipText(TILE_TOOL_TIP + tiles.get(index).getType().readableRepresentation());
        tileLabels.get(index).setIcon(icon);
        tileLabels.get(index).setBorder(selected && !singleTile ? createSelectionBorder() : null);
    }

    // Resets the selection index and adapts the tile labels to the given amount of tiles.
    private void updatePreviewLabels() {
        selectionIndex = 0;
        for (int i = tiles.size(); i < GameSettings.MAXIMAL_TILES_ON_HAND; i++) {
            tileLabels.get(i).setVisible(false);
        }
        for (int i = 0; i < tiles.size(); i++) {
            tileLabels.get(i).setVisible(true);
            updateTileLabel(i);
        }
        pack();
    }

    // Creates the selection border. The color is always up to date.
    private Border createSelectionBorder() {
        return new LineBorder(currentPlayer.getColor().textColor(), SELECTION_BORDER_WIDTH);
    }
}
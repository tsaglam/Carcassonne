package carcassonne.view.secondary;

import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.util.ImageLoadingUtil;
import carcassonne.view.main.MainGUI;

/**
 * GUI class for the tile orientation. It lets the user look at the tile to place and rotate it both right and left.
 * @author Timur Saglam
 */
public class PreviewGUI extends SecondaryGUI {
    private static final long serialVersionUID = -5179683977081970564L;
    private static final int TILE_INCREASE = 10;
    private static final int MAXIMUM_TILES = 5;
    private static final int TILE_PREVIEW_SIZE = 100;
    private JButton buttonRotateLeft;
    private JButton buttonRotateRight;
    private JButton buttonSkip;
    private ArrayList<JLabel> tileLabels;
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
        if (isVisible()) {
            tiles.get(selectionIndex).rotateLeft();
            updateTileLabel(selectionIndex);
        }
    }

    /**
     * If the UI is active, rotates the tile to the right.
     */
    public void rotateRight() {
        if (isVisible()) {
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
        setPlayerAndUpdateGUI(currentPlayer);
    }

    @Override
    public void notifyChange() {
        super.notifyChange();
        selectTileLabel(selectionIndex);
    }

    private void addMouseAdapters() {
        buttonSkip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.requestSkip();
            }
        });
        buttonRotateLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rotateLeft();
            }
        });
        buttonRotateRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rotateRight();
            }
        });
    }

    // build the GUI content
    private void buildContent() {
        // create buttons:
        buttonSkip = new JButton(ImageLoadingUtil.SKIP.createImageIcon());
        buttonRotateLeft = new JButton(ImageLoadingUtil.LEFT.createImageIcon());
        buttonRotateRight = new JButton(ImageLoadingUtil.RIGHT.createImageIcon());
        // set tool tips:
        buttonSkip.setToolTipText("Don't place tile and skip turn");
        buttonRotateLeft.setToolTipText("Rotate left");
        buttonRotateRight.setToolTipText("Rotate right");
        // set listeners:
        addMouseAdapters();
        // set constraints:
        constraints.fill = GridBagConstraints.BOTH;
        // add buttons:
        dialogPanel.add(buttonRotateLeft, constraints);
        dialogPanel.add(buttonSkip, constraints);
        dialogPanel.add(buttonRotateRight, constraints);
        // change constraints and add label:
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.ipady = 0;
        constraints.gridwidth = 3;
        ImageIcon defaultImage = new Tile(TileType.Null).getScaledIcon(TILE_PREVIEW_SIZE);
        tileLabels = new ArrayList<>();
        tiles = new ArrayList<>();
        for (int i = 0; i < MAXIMUM_TILES; i++) {
            JLabel label = new JLabel(defaultImage);
            label.setVisible(true);
            tileLabels.add(label);
            constraints.gridy = i + 1;
            final int index = i;
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    selectTileLabel(index);
                }
            });
            dialogPanel.add(label, constraints);
        }
    }

    /**
     * Selects a specific tile label, increasing its size and adding a border. Resets the previous selection.
     * @param index is the index of the label.
     */
    private void selectTileLabel(int index) {
        if (tiles.size() > 1 && index < tiles.size()) {
            selectionIndex = index;
            for (int i = 0; i < tiles.size(); i++) {
                ImageIcon icon = tiles.get(i).getScaledIcon(TILE_PREVIEW_SIZE);
                tileLabels.get(i).setIcon(icon);
                tileLabels.get(i).setBorder(null);
            }
            JLabel label = tileLabels.get(index);
            label.setIcon(tiles.get(index).getScaledIcon(TILE_PREVIEW_SIZE + TILE_INCREASE)); // TODO constant
            label.setBorder(new LineBorder(currentPlayer.getColor().textColor(), 2)); // TODO constant
            pack();
        }
    }

    /**
     * Updates the image of a specific tile label.
     * @param index is the index of the label.
     */
    private void updateTileLabel(int index) {
        int iconSize = tiles.size() > 1 && index == selectionIndex ? TILE_PREVIEW_SIZE + TILE_INCREASE : TILE_PREVIEW_SIZE;
        ImageIcon icon = tiles.get(index).getScaledIcon(iconSize);
        tileLabels.get(index).setIcon(icon);
    }

    /**
     * Primitive operation for the template method <code>setTile()</code>. Uses the tile to update the GUI content according
     * to the tiles properties.
     */
    @Override
    protected void updateGUI() {
        for (int i = tiles.size(); i < MAXIMUM_TILES; i++) {
            tileLabels.get(i).setVisible(false);
        }
        for (int i = 0; i < tiles.size(); i++) {
            tileLabels.get(i).setVisible(true);
            updateTileLabel(i);
        }
        selectTileLabel(0);
    }
}
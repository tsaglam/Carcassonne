package carcassonne.view.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import carcassonne.control.ControllerFacade;
import carcassonne.model.Player;
import carcassonne.model.tile.Tile;
import carcassonne.view.PaintShop;

/**
 * Is a simple class derived form JLabel, which stores (additionally to the JLabel functions) the coordinates of the
 * label on the label grid.
 * @author Timur Saglam
 */
public class TileDepiction {
    private Tile tile;
    private final Tile defaultTile;
    private final Tile highlightTile;
    private final JLabel label;
    private ImageIcon coloredHighlight;
    private int tileSize;
    private Player recentlyPlaced;

    /**
     * Simple constructor calling the <codeJLabel>JLabel(ImageIcon image)</code> constructor.
     * @param tileSize is the initial edge length of the tile according to the zoom level.
     * @param defaultTile is the tile that determines the default look.
     * @param highlightTile is the tile that determines the highlight look.
     * @param controller is the controller of the view.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public TileDepiction(int tileSize, Tile defaultTile, Tile highlightTile, ControllerFacade controller, int x, int y) {
        this.defaultTile = defaultTile;
        this.highlightTile = highlightTile;
        this.tileSize = tileSize;
        label = new JLabel();
        reset();
        label.addMouseListener(new MouseAdapter() {
            /**
             * Method for processing mouse clicks on the <code>TileImage</code> of the class. notifies the
             * <code>MainController</code> of the class.
             * @param e is the <code>MouseEvent</code> of the click.
             */
            @Override
            public void mouseClicked(MouseEvent event) {
                controller.requestTilePlacement(x, y);
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                if (highlightTile.equals(tile)) {
                    label.setIcon(coloredHighlight);
                }
            }

            @Override
            public void mouseExited(MouseEvent event) {
                if (highlightTile.equals(tile)) {
                    setTile(highlightTile);
                }
            }
        });
    }

    /**
     * Shows a {@link Tile} image on this label.
     * @param tile is the {@link Tile} that provides the image.
     */
    public final void setTile(Tile tile) {
        this.tile = tile;
        if (recentlyPlaced == null) {
            label.setIcon(tile.getScaledIcon(tileSize));
        } else {
            label.setIcon(PaintShop.getColoredTile(tile, recentlyPlaced, tileSize, false));
        }
    }

    /**
     * Adapts the size of the tile to make the tile label larger or smaller.
     * @param tileSize is the new tile size in pixels.
     * @param preview determines if the size adjustment is part of the preview or final.
     */
    public void setTileSize(int tileSize, boolean preview) {
        this.tileSize = tileSize;
        if (recentlyPlaced == null) {
            label.setIcon(tile.getScaledIcon(tileSize, preview));
        } else {
            label.setIcon(PaintShop.getColoredTile(tile, recentlyPlaced, tileSize, preview));
        }

    }

    /**
     * Sets a colored mouseover highlight.
     * @param coloredHighlight is the {@link ImageIcon} depicting the highlight.
     */
    public void setColoredHighlight(ImageIcon coloredHighlight) {
        this.coloredHighlight = coloredHighlight;
    }

    /**
     * Enables the colored mouseover highlight.
     */
    public void highlightSelection() {
        setTile(highlightTile);
    }

    /**
     * Enables the colored highlight that indicated the recent placement.
     * @param player is this player that placed the tile.
     */
    public void highlightPlacement(Player player) {
        recentlyPlaced = player;
        setTile(tile);
    }

    /**
     * Resets the colored highlight that indicated the recent placement.
     */
    public void resetPlacementHighlight() {
        recentlyPlaced = null;
        setTile(tile);
    }

    /**
     * Disables the colored mouseover highlight and sets this tile to the default tile.
     */
    public final void refresh() {
        setTile(tile);
    }

    /**
     * Disables the colored mouseover highlight and sets this tile to the default tile.
     */
    public final void reset() {
        if (tile != defaultTile) {
            recentlyPlaced = null;
            setTile(defaultTile);
        }
    }

    /**
     * Grants access to the {@link JLabel} of this label.
     * @return the tile {@link JLabel}.
     */
    public JLabel getLabel() {
        return label;
    }
}

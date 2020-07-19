package carcassonne.view.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;

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

    /**
     * Simple constructor calling the <codeJLabel>JLabel(ImageIcon image)</code> constructor.
     * @param tileSize is the initial edge length of the tile according to the zoom level.
     * @param defaultTile is the tile that determines the default look.
     * @param highlightTile is the tile that determines the highlight look.
     * @param controller is the controller of the GUI.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public TileDepiction(int tileSize, Tile defaultTile, Tile highlightTile, MainController controller, int x, int y) {
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
        label.setIcon(tile.getScaledIcon(tileSize));
    }

    /**
     * Adapts the size of the tile to make the tile label larger or smaller.
     * @param tileSize is the new tile size in pixels.
     * @param preview determines if the size adjustment is part of the preview or final.
     */
    public void setTileSize(int tileSize, boolean preview) {
        this.tileSize = tileSize;
        label.setIcon(tile.getScaledIcon(tileSize, preview));
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
    public void highlight() {
        setTile(highlightTile);
    }

    /**
     * Disables the colored mouseover highlight and sets this tile to the default tile.
     */
    public final void reset() {
        if (tile != defaultTile) {
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

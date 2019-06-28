package carcassonne.view.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.view.PaintShop;

/**
 * Is a simple class derived form JLabel, which stores (additionally to the JLabel functions) the coordinates of the
 * label on the label grid.
 * @author Timur Saglam
 */
public class TileLabel {
    private Tile tile;
    private final Tile defaultTile;
    private final Tile highlightTile;
    private final JLabel label;
    private ImageIcon coloredHighlight;
    private PaintShop paintShop;

    /**
     * Simple constructor calling the <codeJLabel>JLabel(ImageIcon image)</code> constructor.
     * @param image sets the ImageIcon of the label.
     * @param controller is the controller of the GUI.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public TileLabel(MainController controller, PaintShop paintShop, int x, int y) {
        this.paintShop = paintShop;
        label = new JLabel();
        defaultTile = new Tile(TileType.Null);
        highlightTile = new Tile(TileType.Null);
        defaultTile.rotateRight();
        reset();
        label.addMouseListener(new MouseAdapter() {
            /**
             * Method for processing mouse clicks on the <code>TileLabel</code> of the class. notifies the
             * <code>MainController</code> of the class.
             * @param e is the <code>MouseEvent</code> of the click.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
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

    public void setTile(Tile tile) {
        this.tile = tile;
        if (tile.hasEmblem()) {
            label.setIcon(paintShop.addEmblem(tile.getImage()));
        } else {
            label.setIcon(tile.getIcon());
        }
    }

    public void setColoredHighlight(ImageIcon coloredHighlight) {
        this.coloredHighlight = coloredHighlight;
    }

    public void highlight() {
        setTile(highlightTile);
    }

    public void reset() {
        setTile(defaultTile);
    }

    public JLabel getLabel() {
        return label;
    }
}

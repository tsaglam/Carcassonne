package carcassonne.view.main;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.control.PaintShop;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;

/**
 * Special {@link JLabel} for showing meeples.
 * @author Timur Saglam
 */
public class MeepleLabel extends JLabel { // TODO (HIGH) delegate instead of inherit.
    private static final int HIGHLIGHT_TRANSPARANCY = 100;
    private static final long serialVersionUID = 3333971053086379413L;
    private final ImageIcon imageEmpty;
    private final PaintShop paintShop;
    private int playerNumber;
    private final MouseAdapter mouseAdapter;
    private TerrainType terrain;
    private final GameOptions options;

    /**
     * Creates a blank meeple label.
     * @param paintShop is the paint shop for the meeple generation.
     */
    public MeepleLabel(PaintShop paintShop, MainController controller, GridDirection direction, JFrame frame) {
        super();
        options = GameOptions.getInstance();
        imageEmpty = new ImageIcon(GameOptions.getInstance().getMeeplePath(TerrainType.OTHER, false));
        reset();
        this.paintShop = paintShop;
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    controller.requestMeeplePlacement(direction);
                    setOpaque(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                setOpaque(true);
                Color color = options.getPlayerColor(playerNumber); // TODO (HIGH) move colors to player class
                Color highlightColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), HIGHLIGHT_TRANSPARANCY);
                setBackground(highlightColor);
                frame.repaint();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                setOpaque(false);
                frame.repaint();
            }
        };
    }

    /**
     * Refreshes its icon by getting the newest image from the {@link PaintShop}.
     */
    public void refresh() {
        if (terrain != TerrainType.OTHER) {
            ImageIcon icon = paintShop.getColoredMeeple(terrain, playerNumber);
            super.setIcon(icon);
        }
    }

    /**
     * Resets the label, which means it displays nothing.
     */
    public void reset() {
        terrain = TerrainType.OTHER;
        playerNumber = -1;
        super.setIcon(imageEmpty);
        removeMouseListener(mouseAdapter);
    }

    /**
     * Sets the icon of the meeple label according to the player number and terrain type.
     * @param terrain is the terrain type and affects the meeple type.
     * @param playerNumber is the player number and affects the color.
     */
    public void setIcon(TerrainType terrain, int playerNumber) {
        this.terrain = terrain;
        this.playerNumber = playerNumber;
        refresh();
    }

    /**
     * Sets the specific {@link TerrainType} as meeple placement highlight, which means a transparent image of the
     * correlating meeple.
     * @param terrain is the specific {@link TerrainType}.
     */
    public void setHighlight(TerrainType terrain, int playerNumber) {
        this.playerNumber = playerNumber;
        addMouseListener(mouseAdapter);
        super.setIcon(new ImageIcon(options.getMeeplePath(terrain, false)));
    }

}

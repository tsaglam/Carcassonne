package carcassonne.view.main;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.control.PaintShop;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;

/**
 * Special {@link JLabel} for showing meeples.
 * @author Timur Saglam
 */
public class MeepleLabel {
    private static final int BORDER_THICKNESS = 2;
    private static final int TRANSPARANT = 100;
    private final ImageIcon imageEmpty;
    private final PaintShop paintShop;
    private Player player;
    private final MouseAdapter mouseAdapter;
    private TerrainType terrain;
    private final GameOptions options;
    private final Border transparentBorder;
    private final JLabel label;

    /**
     * Creates a blank meeple label.
     * @param paintShop is the paint shop for the meeple generation.
     */
    public MeepleLabel(PaintShop paintShop, MainController controller, GridDirection direction, JFrame frame) {
        options = GameOptions.getInstance();
        label = new JLabel();
        imageEmpty = new ImageIcon(GameOptions.getInstance().getMeeplePath(TerrainType.OTHER, false));
        transparentBorder = BorderFactory.createLineBorder(new Color(TRANSPARANT, true), BORDER_THICKNESS);
        reset();
        this.paintShop = paintShop;
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    controller.requestMeeplePlacement(direction);
                    label.setOpaque(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent event) { // TODO only color the meeple? Slight shadow?
                label.setOpaque(true);
                Color color = player.getColor();
                Color highlightColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), TRANSPARANT);
                label.setBackground(highlightColor);
                label.setBorder(BorderFactory.createLineBorder(color, BORDER_THICKNESS, true));
                frame.repaint();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                label.setOpaque(false);
                label.setBorder(transparentBorder);
                frame.repaint();
            }
        };
    }

    /**
     * Refreshes its icon by getting the newest image from the {@link PaintShop}.
     */
    public void refresh() {
        if (terrain != TerrainType.OTHER) {
            ImageIcon icon = paintShop.getColoredMeeple(terrain, player);
            label.setIcon(icon);
        }
    }

    /**
     * Resets the label, which means it displays nothing.
     */
    public void reset() {
        terrain = TerrainType.OTHER;
        label.setIcon(imageEmpty);
        label.setBorder(transparentBorder);
        label.removeMouseListener(mouseAdapter);
    }

    /**
     * Sets the icon of the meeple label according to the player number and terrain type.
     * @param terrain is the terrain type and affects the meeple type.
     * @param playerNumber is the player number and affects the color.
     */
    public void setIcon(TerrainType terrain, Player player) {
        this.terrain = terrain;
        this.player = player;
        refresh();
    }

    /**
     * Sets the specific {@link TerrainType} as meeple placement highlight, which means a transparent image of the
     * correlating meeple.
     * @param terrain is the specific {@link TerrainType}.
     */
    public void setHighlight(TerrainType terrain, Player player) {
        this.player = player;
        label.addMouseListener(mouseAdapter);
        label.setIcon(new ImageIcon(options.getMeeplePath(terrain, false)));
    }

    /**
     * Grants access to the {@link JLabel} itself.
     * @return the {@link JLabel}
     */
    public JLabel getLabel() {
        return label;
    }

}

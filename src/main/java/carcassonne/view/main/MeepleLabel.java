package carcassonne.view.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.settings.GameSettings;
import carcassonne.view.PaintShop;

/**
 * Special {@link JLabel} for showing meeples.
 * @author Timur Saglam
 */
public class MeepleLabel {
    private final ImageIcon imageEmpty;
    private final PaintShop paintShop;
    private Player player;
    private final MouseAdapter mouseAdapter;
    private TerrainType terrain;
    private final JLabel label;
    private boolean preview;

    /**
     * Creates a blank meeple label.
     * @param paintShop is the paint shop for the meeple generation.
     */
    public MeepleLabel(PaintShop paintShop, MainController controller, GridDirection direction, JFrame frame) {
        label = new JLabel();
        imageEmpty = new ImageIcon(GameSettings.getMeeplePath(TerrainType.OTHER, false));
        preview = false;
        reset();
        this.paintShop = paintShop;
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    controller.requestMeeplePlacement(direction);
                }
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                setMeepleIcon();
                frame.repaint();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                setPreviewIcon();
                frame.repaint();
            }
        };
    }

    /**
     * Refreshes its icon by getting the newest image from the {@link PaintShop}.
     */
    public void refresh() {
        if (terrain != TerrainType.OTHER && !preview) {
            setMeepleIcon();
        }
    }

    /**
     * Resets the label, which means it displays nothing.
     */
    public void reset() {
        terrain = TerrainType.OTHER;
        label.setIcon(imageEmpty);
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
        preview = false;
        refresh();
    }

    /**
     * Sets the specific {@link TerrainType} as meeple placement preview, which means a transparent image of the correlating
     * meeple.
     * @param terrain is the specific {@link TerrainType}.
     */
    public void setPreview(TerrainType terrain, Player player) {
        this.terrain = terrain;
        this.player = player;
        preview = true;
        label.addMouseListener(mouseAdapter);
        setPreviewIcon();
    }

    /**
     * Grants access to the {@link JLabel} itself.
     * @return the {@link JLabel}
     */
    public JLabel getLabel() {
        return label;
    }

    private void setMeepleIcon() {
        label.setIcon(paintShop.getColoredMeeple(terrain, player));
    }

    private void setPreviewIcon() {
        label.setIcon(new ImageIcon(GameSettings.getMeeplePath(terrain, false)));
    }
}

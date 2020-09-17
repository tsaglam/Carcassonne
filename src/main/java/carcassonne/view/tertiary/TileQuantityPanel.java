package carcassonne.view.tertiary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.NumberFormatter;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.view.util.MouseClickListener;

/**
 * Panel that depicts the quantity of a tile type. Shows a image of the tile and a text field for the quantity.
 * @author Timur Saglam
 */
public class TileQuantityPanel extends JPanel {
    private static final long serialVersionUID = 6368792603603753333L;
    private static final int TILE_SIZE = 100;
    private static final int BORDER_SIZE = 2;
    private static final String CLICK_TO_ROTATE = "Click to rotate the tile of type ";
    private final TileType type;
    private JTextField textField;

    /**
     * Creates a quantity panel for a certain tile type.
     * @param type is the tile type.
     * @param initialQuantity is the initial quantity depicted in the text field.
     */
    public TileQuantityPanel(TileType type, int initialQuantity, TileDistributionGUI ui) {
        this.type = type;
        setLayout(new BorderLayout());
        setBorder(new LineBorder(Color.DARK_GRAY, BORDER_SIZE));
        setBackground(Color.LIGHT_GRAY);
        createTileLabel(type);
        createTextField(initialQuantity, ui);
    }

    /**
     * Getter for the tile type this panel is depicting.
     * @return the tile type.
     */
    public TileType getTileType() {
        return type;
    }

    /**
     * Getter for the quantity specified in the text field of the panel.
     * @return the specified quantity.
     */
    public int getQuantity() {
        return Integer.parseInt(textField.getText());
    }

    /**
     * Setter to update the quantity specified in the text field of the panel.
     * @param quantity is the new updated quantity.
     */
    public void setQuantity(int quantity) {
        textField.setText(Integer.toString(quantity));
    }

    private void createTileLabel(TileType type) {
        Tile tile = new Tile(type);
        JLabel label = new JLabel(tile.getScaledIcon(TILE_SIZE));
        label.setToolTipText(CLICK_TO_ROTATE + type.readableRepresentation());
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        label.addMouseListener((MouseClickListener) event -> {
            tile.rotateRight();
            label.setIcon(tile.getScaledIcon(TILE_SIZE));
        });
        add(label, BorderLayout.NORTH);
    }

    private void createTextField(int initialQuantity, TileDistributionGUI ui) {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setMinimum(0);
        formatter.setMaximum(99);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        textField = new JFormattedTextField(formatter);
        textField.addPropertyChangeListener(event -> ui.updateStackSizePreview());
        setQuantity(initialQuantity);
        add(textField, BorderLayout.SOUTH);
    }
}
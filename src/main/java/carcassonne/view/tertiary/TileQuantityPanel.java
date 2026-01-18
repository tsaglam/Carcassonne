package carcassonne.view.tertiary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.text.NumberFormatter;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.view.PaintShop;
import carcassonne.view.util.MouseClickListener;

/**
 * Panel that depicts the quantity of a tile type. Shows an image of the tile and a text field for the quantity.
 * @author Timur Saglam
 */
public class TileQuantityPanel extends JPanel {
    private static final long serialVersionUID = 6368792603603753333L;
    private static final int TILE_SIZE = 100;
    private static final int BORDER_SIZE = 2;
    private static final String CLICK_TO_ROTATE = "Click to rotate the tile of type ";
    private static final Color NORMAL_BACKGROUND = Color.GRAY;
    private static final Color HIGHLIGHT_INCREASE = new Color(34, 139, 34);
    private static final Color HIGHLIGHT_DECREASE = new Color(178, 34, 34);
    private static final int HIGHLIGHT_DURATION_MS = 1500;
    private static final int FADE_INTERVAL_MS = 50;
    private static final int FADE_STEPS = HIGHLIGHT_DURATION_MS / FADE_INTERVAL_MS; // Should be 30 steps

    private final TileType type;
    private JTextField textField;
    private Timer highlightTimer;
    private int originalQuantity = -1;
    private int fadeStep;

    /**
     * Creates a quantity panel for a certain tile type.
     * @param type is the tile type.
     * @param initialQuantity is the initial quantity depicted in the text field.
     * @param parentUI is the parent UI for callbacks.
     */
    public TileQuantityPanel(TileType type, int initialQuantity, TileDistributionView parentUI) {
        this.type = type;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, BORDER_SIZE));
        setBackground(NORMAL_BACKGROUND);
        createTileLabel(type);
        createTextField(initialQuantity, parentUI);
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
     * @return the specified quantity or -1 if it is empty.
     */
    public int getQuantity() {
        String text = textField.getText();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Setter to update the quantity specified in the text field of the panel.
     * @param quantity is the new updated quantity.
     */
    public void setQuantity(int quantity) {
        textField.setText(Integer.toString(quantity));
    }

    /**
     * Highlights the panel temporarily based on whether the quantity increased or decreased.
     * @param previousQuantity the quantity before the change.
     * @param newQuantity the quantity after the change.
     */
    public void highlightChange(int previousQuantity, int newQuantity) {
        if (previousQuantity == newQuantity) {
            return;
        }

        cancelExistingHighlight();

        Color highlightColor = (newQuantity > previousQuantity) ? HIGHLIGHT_INCREASE : HIGHLIGHT_DECREASE;
        setBackground(highlightColor);
        repaint();

        startFadeAnimation(highlightColor);
    }

    /**
     * Shows a preview highlight for a potential change without applying it.
     * @param currentQuantity the current quantity.
     * @param previewQuantity the quantity that would result from the change.
     */
    public void showPreviewHighlight(int currentQuantity, int previewQuantity) {
        cancelExistingHighlight();

        if (currentQuantity != previewQuantity) {
            // Store original quantity and temporarily show preview
            if (originalQuantity == -1) {
                originalQuantity = currentQuantity;
            }
            textField.setText(Integer.toString(previewQuantity));

            Color highlightColor = (previewQuantity > currentQuantity) ? HIGHLIGHT_INCREASE : HIGHLIGHT_DECREASE;
            setBackground(highlightColor);
        } else {
            restoreOriginalQuantity();
            setBackground(NORMAL_BACKGROUND);
        }
        repaint();
    }

    /**
     * Clears any preview highlighting and restores the normal background and original quantity.
     */
    public void clearPreviewHighlight() {
        cancelExistingHighlight();
        restoreOriginalQuantity();
        setBackground(NORMAL_BACKGROUND);
        repaint();
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

    private void createTextField(int initialQuantity, TileDistributionView ui) {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setMinimum(0);
        formatter.setMaximum(99);
        formatter.setAllowsInvalid(true);
        formatter.setCommitsOnValidEdit(true);
        textField = new JFormattedTextField(formatter);
        textField.addPropertyChangeListener(event -> ui.updateStackSizePreview());
        setQuantity(initialQuantity);
        add(textField, BorderLayout.SOUTH);
    }

    private void cancelExistingHighlight() {
        if (highlightTimer != null && highlightTimer.isRunning()) {
            highlightTimer.stop();
        }
    }

    private void restoreOriginalQuantity() {
        if (originalQuantity != -1) {
            textField.setText(Integer.toString(originalQuantity));
            originalQuantity = -1;
        }
    }

    private void startFadeAnimation(Color startColor) {
        fadeStep = 0;

        highlightTimer = new Timer(FADE_INTERVAL_MS, event -> {
            fadeStep++;

            if (fadeStep >= FADE_STEPS) {
                setBackground(NORMAL_BACKGROUND);
                highlightTimer.stop();
            } else {
                float progress = (float) fadeStep / (float) FADE_STEPS;
                Color interpolatedColor = PaintShop.interpolateColor(startColor, NORMAL_BACKGROUND, progress);
                setBackground(interpolatedColor);
            }
            repaint();
        });

        highlightTimer.start();
    }
}
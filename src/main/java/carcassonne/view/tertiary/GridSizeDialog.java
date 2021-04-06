package carcassonne.view.tertiary;

import java.awt.BorderLayout;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import carcassonne.settings.GameSettings;
import carcassonne.view.util.GameMessage;

/**
 * Custom dialog for changing the grid size.
 * @author Timur Saglam
 */
public class GridSizeDialog extends JPanel {

    private static final long serialVersionUID = 6533357898928866596L;
    private static final String BRACKET = ")";
    private static final String TOTAL_TILES = "(spots to place tiles: ";
    private static final String INVALID_SIZE = "Invalid grid size!";
    private static final String CROSS = " x ";
    private static final String TITLE = "Carcassonne";
    private static final String WIDTH = "Width:";
    private static final String HEIGHT = "Height:";
    private static final String NOT_CORRECT = " is not a valid grid sizes!";
    private static final String MESSAGE = "<html>Changes to the grid size will affect the next game. Choose values between 3 and 99.<br/>Large grids may affect the performance!</html>";
    private static final int MIN_VALUE = 3;
    private static final int GAP = 5;
    private static final int SPACE = 100;
    private static final int MAX_VALUE = 99;
    private static final int TEXT_FIELD_COLUMNS = 3;
    private final GameSettings settings;
    private final JFormattedTextField heightInput;
    private final JFormattedTextField widthInput;
    private final JLabel numberOfTiles;

    /**
     * Creates a dialog to change the grid size.
     * @param settings are the {@link GameSettings} that will receive the new grid size.
     */
    public GridSizeDialog(GameSettings settings) {
        this.settings = settings;
        numberOfTiles = new JLabel();
        numberOfTiles.setHorizontalAlignment(SwingConstants.CENTER);
        Font font = new Font(numberOfTiles.getFont().getName(), Font.ITALIC, numberOfTiles.getFont().getSize());
        numberOfTiles.setFont(font);
        widthInput = new JFormattedTextField(createNumberFormatter());
        widthInput.setColumns(TEXT_FIELD_COLUMNS);
        heightInput = new JFormattedTextField(createNumberFormatter());
        heightInput.setColumns(TEXT_FIELD_COLUMNS);
        widthInput.addPropertyChangeListener(event -> updateNumberOfTiles());
        heightInput.addPropertyChangeListener(event -> updateNumberOfTiles());
        setLayout(new BorderLayout(GAP, GAP));
        add(new JLabel(MESSAGE), BorderLayout.NORTH);
        JPanel subPanel = new JPanel();
        subPanel.add(new JLabel(WIDTH));
        subPanel.add(widthInput);
        subPanel.add(Box.createHorizontalStrut(SPACE));
        subPanel.add(new JLabel(HEIGHT));
        subPanel.add(heightInput);
        add(subPanel, BorderLayout.CENTER);
        add(numberOfTiles, BorderLayout.SOUTH);
    }

    /**
     * Shows the grid size dialog and waits for the user input which is then sent to the game settings.
     */
    public void showDialog() {
        widthInput.setText(Integer.toString(settings.getGridWidth()));
        heightInput.setText(Integer.toString(settings.getGridHeight()));
        updateNumberOfTiles(settings.getGridWidth(), settings.getGridHeight());
        int result = JOptionPane.showConfirmDialog(null, this, TITLE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION,
                GameMessage.getGameIcon());
        if (result == JOptionPane.OK_OPTION) {
            processUserInput();
        }
    }

    private void updateNumberOfTiles(int width, int height) {
        int maxTiles = width * height;
        numberOfTiles.setText(TOTAL_TILES + maxTiles + BRACKET);
    }

    private void updateNumberOfTiles() {
        if (widthInput.isEditValid() && heightInput.isEditValid()) {
            try {
                updateNumberOfTiles(Integer.parseInt(widthInput.getText()), Integer.parseInt(heightInput.getText()));
            } catch (NumberFormatException exception) {
                numberOfTiles.setText(INVALID_SIZE);
            }
        } else {
            numberOfTiles.setText(INVALID_SIZE);
        }
    }

    /**
     * Parses the text input to valid integers and sends them to the settings.
     */
    private void processUserInput() {
        try {
            settings.setGridWidth(Integer.parseInt(widthInput.getText()));
            settings.setGridHeight(Integer.parseInt(heightInput.getText()));
        } catch (NumberFormatException exception) {
            GameMessage.showWarning(widthInput.getText() + CROSS + heightInput.getText() + NOT_CORRECT);
        }
    }

    /**
     * Creates a number formatter that enforces the valid minimum and maximum values for the text fields.
     */
    private NumberFormatter createNumberFormatter() {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
        formatter.setCommitsOnValidEdit(true);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(MIN_VALUE);
        formatter.setMaximum(MAX_VALUE);
        return formatter;
    }
}
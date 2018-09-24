package carcassonne.view.main.menubar;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;

import carcassonne.control.GameOptions;

/**
 * Is the scoreboard class of the game. Manages a score label for each player.
 * @author Timur Saglam
 */
public class Scoreboard {
    private final JLabel[] scoreLabels;
    private final JLabel stackSizeLabel;
    private final ArrayList<JLabel> allLabels;
    private final GameOptions options;

    /**
     * Standard constructor. Creates score board.
     */
    public Scoreboard() {
        options = GameOptions.getInstance();
        scoreLabels = new JLabel[options.maximalPlayers];
        for (int i = 0; i < scoreLabels.length; i++) {
            scoreLabels[i] = new JLabel();
            scoreLabels[i].setForeground(options.getPlayerColor(i));
        }
        stackSizeLabel = new JLabel();
        allLabels = new ArrayList<>(Arrays.asList(scoreLabels));
        allLabels.add(stackSizeLabel);
        Font font = new Font("Helvetica", Font.BOLD, 12);
        for (JLabel label : allLabels) {
            label.setFont(font);
        }
    }

    /**
     * Disables all the scoreboard labels.
     */
    public void disable() {
        for (JLabel label : allLabels) {
            label.setVisible(false);
        }
        stackSizeLabel.setVisible(false);
    }

    /**
     * Enables all the scoreboard labels.
     */
    public void enable() {
        for (JLabel label : allLabels) {
            label.setVisible(true);
        }
    }

    /**
     * Grants access to the labels themselves.
     * @return the array of labels.
     */
    public List<JLabel> getLabels() {
        return allLabels;
    }

    /**
     * Only shows the specified amount of labels.
     * @param playerCount is the amount of players to show labels for.
     */
    public void rebuild(int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            scoreLabels[i].setVisible(true);
        }
        stackSizeLabel.setVisible(true);
    }

    /**
     * Updates a specific player label of the scoreboard.
     * @param playerNumber is the number of the player whose label should get updated.
     * @param points is the amount of points the players has.
     * @param unusedMeeples is the amount of unused meeples of the player.
     */
    public void update(int playerNumber, int points, int unusedMeeples) {
        String playerName = options.playerNames[playerNumber] + " ";
        scoreLabels[playerNumber].setText("[" + playerName + ": " + points + " points, " + unusedMeeples + " meeples]    ");
    }

    /**
     * Updates the stack size label.
     * @param stackSize is the updated size of the stack.
     */
    public void updateStackSize(int stackSize) {
      stackSizeLabel.setText("   [Stack Size: " + stackSize + "]");
    }
}

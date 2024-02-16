package carcassonne.view.menubar;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;

import carcassonne.model.Player;
import carcassonne.settings.GameSettings;
import carcassonne.view.NotifiableView;
import carcassonne.view.main.MainView;

/**
 * Is the scoreboard class of the game. Manages a score label for each player.
 * @author Timur Saglam
 */
public class Scoreboard implements NotifiableView {
    private static final String FONT_TYPE = "Helvetica";
    private String TOOL_TIP = "Settings for player ";
    private final JLabel[] scoreLabels;
    private final JLabel stackSizeLabel;
    private final List<JLabel> allLabels;
    private final List<ActionListener> settingsListeners;
    private final GameSettings settings;

	private String SETTING_1;
	private String SETTING_2;
	private String POINTS;
	private String MEEPLES;
	private String STACK;


    /**
     * Standard constructor. Creates score board.
     * @param settings are the {@link GameSettings}.
     * @param mainView is the main user interface.
     */
    public Scoreboard(GameSettings settings, MainView mainView) { // TODO (MEDIUM) [UI] link with players?
		initResource();
        this.settings = settings;
        scoreLabels = new JLabel[GameSettings.MAXIMAL_PLAYERS];
        settingsListeners = new ArrayList<>();
        for (int i = 0; i < scoreLabels.length; i++) {
            scoreLabels[i] = new JLabel();
            scoreLabels[i].setForeground(settings.getPlayerColor(i).textColor());
            MenuViewListener listener = new MenuViewListener(i, settings, mainView);
            settingsListeners.add(listener);
            scoreLabels[i].addMouseListener(listener);
        }
        stackSizeLabel = new JLabel();
        allLabels = new ArrayList<>(Arrays.asList(scoreLabels));
        allLabels.add(stackSizeLabel);
        for (JLabel label : allLabels) {
            label.setFont(new Font(FONT_TYPE, Font.BOLD, 12));
        }
    }

    /**
     * Disables all the scoreboard labels.
     */
    public void disable() {
        allLabels.forEach(it -> it.setVisible(false));
        stackSizeLabel.setVisible(false);
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
     * @param player is the player whose scoreboard should be updated.
     */
    public void update(Player player) {
        String playerName = player.getName();
        String text = "[" + playerName + ": " + player.getScore() + " " + POINTS + ", " + player.getFreeMeeples() + " " + MEEPLES +"]    ";
        scoreLabels[player.getNumber()].setText(text);
        // scoreLabels[player.getNumber()].setToolTipText(TOOL_TIP + player.getName());
        scoreLabels[player.getNumber()].setToolTipText(SETTING_1 + player.getName() + SETTING_2);
    }

    /**
     * Updates the stack size label.
     * @param stackSize is the updated size of the stack.
     */
    public void updateStackSize(int stackSize) {
        stackSizeLabel.setText("   [" + STACK + ": " + stackSize + "]");
    }

    /**
     * Grants access to a specific mouse listener of one players settings.
     * @param playerNumber specifies the player.
     * @return the correlating mouse listener.
     */
    public ActionListener getSettingsListener(int playerNumber) {
        return settingsListeners.get(playerNumber);
    }

    @Override
    public void notifyChange() {
        for (int i = 0; i < scoreLabels.length; i++) {
            scoreLabels[i].setForeground(settings.getPlayerColor(i).textColor()); // replace only color and player name:
            scoreLabels[i].setText(scoreLabels[i].getText().replaceFirst("\\[.*?:", "[" + settings.getPlayerName(i) + ":"));
            scoreLabels[i].setToolTipText(TOOL_TIP + settings.getPlayerName(i));
        }
    }

	private void initResource() {
		LoadTextScoreboard resource =new LoadTextScoreboard();
		SETTING_1 = resource.get("SETTING_1");
		SETTING_2 = resource.get("SETTING_2");
		POINTS = resource.get("POINTS");
		MEEPLES = resource.get("MEEPLES");
		STACK = resource.get("STACK");
	}
}

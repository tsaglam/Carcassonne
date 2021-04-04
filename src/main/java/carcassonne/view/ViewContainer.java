package carcassonne.view;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import carcassonne.model.Round;
import carcassonne.model.tile.Tile;
import carcassonne.view.main.MainGUI;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.PreviewGUI;
import carcassonne.view.tertiary.GameStatisticsGUI;
import carcassonne.view.util.GameMessage;

/**
 * Container class for the different views that also manages proper swing threading for the UI access.
 * @author Timur Saglam
 */
public class ViewContainer {

    private final MainGUI mainView;
    private final PreviewGUI tileView;
    private final PlacementGUI placmementView;
    private final Scoreboard scoreboard;
    private GameStatisticsGUI gameStatistics;
    Tile selectedTile;

    /**
     * Creates a view container that encapsulates the access to the three main user interfaces. Can be seen as view facade.
     * @param mainView is the main view showing the grid and the menu bar.
     * @param tileView is the view used for previewing and rotating tiles.
     * @param placmementView is the view used to place meeples.
     */
    public ViewContainer(MainGUI mainView, PreviewGUI tileView, PlacementGUI placmementView) {
        if (mainView == null || tileView == null || placmementView == null) {
            throw new IllegalArgumentException("View container can only contain non-null views!<");
        }
        this.mainView = mainView;
        this.tileView = tileView;
        this.placmementView = placmementView;
        this.scoreboard = mainView.getScoreboard();
    }

    /**
     * Operates an instruction on the main view. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onMainView(Consumer<MainGUI> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(mainView));
    }

    /**
     * Operates an instruction on the tile view. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onTileView(Consumer<PreviewGUI> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(tileView));
    }

    /**
     * Operates an instruction on the placement view. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onPlacementView(Consumer<PlacementGUI> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(placmementView));
    }

    /**
     * Operates an instruction on the scoreboard. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onScoreboard(Consumer<Scoreboard> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(scoreboard));
    }

    /**
     * Creates and shows a game statistics view for the current round.
     * @param round is the current round.
     */
    public void showGameStatistics(Round round) {
        EventQueue.invokeLater(() -> {
            gameStatistics = new GameStatisticsGUI(mainView, round);
        });
    }

    /**
     * Closes the statistics view.
     */
    public void closeGameStatistics() {
        EventQueue.invokeLater(() -> {
            gameStatistics.closeGUI();
        });
    }

    public Tile getSelectedTile() {
        try {
            EventQueue.invokeAndWait(() -> {
                selectedTile = tileView.getSelectedTile();
            });
        } catch (InvocationTargetException | InterruptedException exception) {
            GameMessage.showError("Could not create UI: " + exception.getMessage());
        }
        return selectedTile;
    }

}

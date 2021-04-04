package carcassonne.view;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import carcassonne.model.Round;
import carcassonne.model.tile.Tile;
import carcassonne.view.main.MainView;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.secondary.MeepleView;
import carcassonne.view.secondary.TileView;
import carcassonne.view.tertiary.GameStatisticsView;
import carcassonne.view.util.GameMessage;

/**
 * Container class for the different views that also manages proper swing threading for the UI access.
 * @author Timur Saglam
 */
public class ViewContainer {
    private final MainView mainView;
    private final MeepleView meepleView;
    private final Scoreboard scoreboard;
    private final TileView tileView;
    private GameStatisticsView gameStatistics;
    private Tile selectedTile;

    /**
     * Creates a view container that encapsulates the access to the three main user interfaces. Can be seen as view facade.
     * @param mainView is the main view showing the grid and the menu bar.
     * @param tileView is the view used for previewing and rotating tiles.
     * @param meepleView is the view used to place meeples.
     */
    public ViewContainer(MainView mainView, TileView tileView, MeepleView placmementView) {
        if (mainView == null || tileView == null || placmementView == null) {
            throw new IllegalArgumentException("View container can only contain non-null views!<");
        }
        this.mainView = mainView;
        this.tileView = tileView;
        this.meepleView = placmementView;
        this.scoreboard = mainView.getScoreboard();
    }

    /**
     * Closes the statistics view.
     */
    public void closeGameStatistics() {
        EventQueue.invokeLater(() -> {
            gameStatistics.closeView();
        });
    }

    /**
     * Returns the selected tile from the tile view.
     * @return the selected tile.
     */
    public Tile getSelectedTile() {
        try {
            EventQueue.invokeAndWait(() -> {
                selectedTile = tileView.getSelectedTile();
            });
        } catch (InvocationTargetException | InterruptedException exception) {
            GameMessage.showError("Could not create user interface: " + exception.getMessage());
        }
        return selectedTile;
    }

    /**
     * Operates an instruction on the main view. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onMainView(Consumer<MainView> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(mainView));
    }

    /**
     * Operates an instruction on the placement view. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onMeepleView(Consumer<MeepleView> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(meepleView));
    }

    /**
     * Operates an instruction on the scoreboard. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onScoreboard(Consumer<Scoreboard> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(scoreboard));
    }

    /**
     * Operates an instruction on the tile view. The instruction is scheduled with the {@link EventQueue}.
     * @param instruction is the instruction of form of a {@link Consumer}.
     */
    public void onTileView(Consumer<TileView> instruction) {
        EventQueue.invokeLater(() -> instruction.accept(tileView));
    }

    /**
     * Creates and shows a game statistics view for the current round.
     * @param round is the current round.
     */
    public void showGameStatistics(Round round) {
        EventQueue.invokeLater(() -> {
            gameStatistics = new GameStatisticsView(mainView, round);
        });
    }
}

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
 * Facade class for the different views that manages proper swing threading for the UI access. This is not a traditional
 * facade per se, as it technically grant access to its managed objects.
 * @author Timur Saglam
 */
public class ViewFacade {
    private final MainView mainView;
    private final MeepleView meepleView;
    private final Scoreboard scoreboard;
    private final TileView tileView;
    private GameStatisticsView gameStatistics;
    private Tile selectedTile;
    private int jobCounter;

    /**
     * Creates a view container that encapsulates the access to the three main user interfaces. Can be seen as view facade.
     * @param mainView is the main view showing the grid and the menu bar.
     * @param tileView is the view used for previewing and rotating tiles.
     * @param placementView is the view used to place meeples.
     */
    public ViewFacade(MainView mainView, TileView tileView, MeepleView placementView) {
        if (mainView == null || tileView == null || placementView == null) {
            throw new IllegalArgumentException("View container can only contain non-null views!");
        }
        this.mainView = mainView;
        this.tileView = tileView;
        meepleView = placementView;
        scoreboard = mainView.getScoreboard();
    }

    /**
     * Executes a job on the main view. The job is scheduled with the {@link EventQueue}.
     * @param job is the job of form of a {@link Consumer}.
     */
    public void onMainView(Consumer<MainView> job) {
        schedule(() -> job.accept(mainView));
    }

    /**
     * Executes a job on the placement view. The job is scheduled with the {@link EventQueue}.
     * @param job is the job of form of a {@link Consumer}.
     */
    public void onMeepleView(Consumer<MeepleView> job) {
        schedule(() -> job.accept(meepleView));
    }

    /**
     * Executes a job on the scoreboard. The job is scheduled with the {@link EventQueue}.
     * @param job is the job of form of a {@link Consumer}.
     */
    public void onScoreboard(Consumer<Scoreboard> job) {
        schedule(() -> job.accept(scoreboard));
    }

    /**
     * Executes a job on the tile view. The job is scheduled with the {@link EventQueue}.
     * @param job is the job of form of a {@link Consumer}.
     */
    public void onTileView(Consumer<TileView> job) {
        schedule(() -> job.accept(tileView));
    }

    /**
     * Executes a UI call immediately on the calling thread without scheduling it with the {@link EventQueue}. This avoids
     * EventQueue overhead but means the job runs synchronously and not on the EDT. Use this for simple updates from game
     * logic; prefer other onXView methods for thread-safe UI operations.
     * @param job the unspecified UI call, for example to {@link GameMessage}
     */
    public void reroute(Runnable job) {
        job.run();
    }

    /**
     * Creates and shows a game statistics view for the current round.
     * @param round is the current round.
     */
    public void showGameStatistics(Round round) {
        schedule(() -> gameStatistics = new GameStatisticsView(mainView, round));
    }

    /**
     * Closes the statistics view.
     */
    public void closeGameStatistics() {
        schedule(() -> {
            if (gameStatistics != null) {
                gameStatistics.closeView();
            }
        });
    }

    /**
     * Returns the selected tile from the tile view.
     * @return the selected tile.
     */
    public Tile getSelectedTile() {
        try {
            EventQueue.invokeAndWait(() -> selectedTile = tileView.getSelectedTile());
        } catch (InvocationTargetException | InterruptedException exception) {
            exception.printStackTrace();
            GameMessage.showError("Cannot retrieve selected tile:" + System.lineSeparator() + exception);
        }
        return selectedTile;
    }

    /**
     * Indicates whether there are jobs that are queued but are not completed yet.
     * @return true if there is at least one unfinished job.
     */
    public boolean isBusy() {
        return jobCounter > 0;
    }

    /**
     * Schedules and tracks a job.
     */
    private void schedule(Runnable job) {
        synchronized (this) {
            jobCounter++;
        }
        EventQueue.invokeLater(() -> {
            job.run();
            synchronized (this) {
                jobCounter--;
            }
        });
    }
}

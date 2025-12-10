package carcassonne.testutils;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import carcassonne.model.Round;
import carcassonne.model.tile.Tile;
import carcassonne.view.ViewFacade;
import carcassonne.view.main.MainView;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.secondary.MeepleView;
import carcassonne.view.secondary.TileView;

/**
 * Test implementation of {@link ViewFacade} that suppresses UI interactions and allows programmatic control of tile
 * selection for testing purposes.
 */
public class TestViewFacade extends ViewFacade {

    private Tile tile;

    private boolean logUiCalls;

    /**
     * Creates the facade using test stubs.
     */
    public TestViewFacade(boolean logUiCalls) {
        super(mock(MainView.class, RETURNS_DEEP_STUBS), mock(TileView.class), mock(MeepleView.class));
        this.logUiCalls = logUiCalls;
    }

    /**
     * Specifies the next tile selected in the next turn by a player. This completely ignores the tile stack and its
     * content. The choices of AI players cannot be specified this way.
     * @param tile is the tile to play next.
     */
    public void setNextTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void onMainView(Consumer<MainView> job) {
        logCallToUserInterface(MainView.class, job);
    }

    @Override
    public void onMeepleView(Consumer<MeepleView> job) {
        logCallToUserInterface(MeepleView.class, job);
    }

    @Override
    public void onScoreboard(Consumer<Scoreboard> job) {
        logCallToUserInterface(Scoreboard.class, job);
    }

    @Override
    public void onTileView(Consumer<TileView> job) {
        logCallToUserInterface(TileView.class, job);
    }

    @Override
    public void reroute(Runnable job) {
        if (logUiCalls) {
            System.out.println("Test framework swallowed message box.");
        }
    }

    @Override
    public void showGameStatistics(Round round) {
        if (logUiCalls) {
            System.out.println("UI event to show game statistics.");
        }
    }

    @Override
    public void closeGameStatistics() {
        if (logUiCalls) {
            System.out.println("UI event to close game statistics.");
        }
    }

    @Override
    public Tile getSelectedTile() {
        return tile;
    }

    @Override
    public boolean isBusy() {
        return false; // in tests, everything is synchronous
    }

    private void logCallToUserInterface(Class<?> uiClass, Consumer<?> job) {
        if (logUiCalls) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement caller = stackTrace[3];
            String simple = simpleCallerName(caller);
            String callerText = caller.toString().replace(caller.getClassName(), simple);

            // if abstract game state...
            String actualCaller = simpleCallerName(stackTrace[4]);
            for (int i = 4; i < stackTrace.length && actualCaller.equals("AbstractGameState"); i++) {
                actualCaller = simpleCallerName(stackTrace[i]);
            }
            callerText = callerText.replaceFirst("AbstractGameState", actualCaller);
            callerText = callerText.replace("(", " (");

            System.out.println("[UI event on " + uiClass.getSimpleName() + "] " + callerText);
            for (Field field : job.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    // TODO could be improved, e.g. if arg is just a state
                    if (field.get(job) != null && !field.get(job).toString().contains("carcassonne.control.state")) {
                        System.out.println(" → " + field.get(job));
                    }

                } catch (IllegalArgumentException | IllegalAccessException exception) {
                    System.err.println("Error during UI event logging of " + field.getName() + ":");
                    exception.printStackTrace();
                }
            }
        }
    }

    private String simpleCallerName(StackTraceElement caller) {
        String simple = caller.getClassName();
        int lastDot = simple.lastIndexOf('.');
        if (lastDot != -1) {
            simple = simple.substring(lastDot + 1);
        }
        return simple;
    }
}

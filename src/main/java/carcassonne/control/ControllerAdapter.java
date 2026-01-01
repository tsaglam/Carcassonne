package carcassonne.control;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import carcassonne.model.grid.GridDirection;
import carcassonne.settings.GameSettings;
import carcassonne.util.ErrorReportingRunnable;
import carcassonne.view.GlobalKeyBindingManager;

/**
 * ControllerFacade adapter for view classes that manages the AWT/Swing threading for them and delegates all calls to a
 * real controller.
 */
public class ControllerAdapter implements ControllerFacade {

    private final MainController controller;
    private final ExecutorService service;

    /**
     * Creates the controller adapter from an original controller.
     * @param controller is the original to which all calls are delegated to.
     */
    ControllerAdapter(MainController controller) {
        this.controller = controller;
        service = Executors.newSingleThreadExecutor();
    }

    @Override
    public void requestAbortGame() {
        controller.requestAsynchronousAbort(); // require for AI vs. AI play where the thread never pauses
        runInBackground(controller::requestAbortGame);
    }

    @Override
    public void requestMeeplePlacement(GridDirection position) {
        runInBackground(() -> controller.requestMeeplePlacement(position));

    }

    @Override
    public void requestNewRound() {
        runInBackground(controller::requestNewRound);
    }

    @Override
    public void requestSkip() {
        runInBackground(controller::requestSkip);
    }

    @Override
    public void requestTilePlacement(int x, int y) {
        runInBackground(() -> controller.requestTilePlacement(x, y));
    }

    @Override
    public GlobalKeyBindingManager getKeyBindings() {
        return controller.getKeyBindings(); // TODO (HIGH) [THREADING] Should this be on view thread?
    }

    @Override
    public GameSettings getSettings() {
        return controller.getSettings(); // TODO (HIGH) [THREADING] Should this be on view thread?
    }

    private void runInBackground(Runnable task) {
        service.submit(new ErrorReportingRunnable(task, "UI request led to an error:" + System.lineSeparator()));
    }

}

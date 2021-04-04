package carcassonne.control;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import carcassonne.model.grid.GridDirection;
import carcassonne.settings.GameSettings;
import carcassonne.view.GlobalKeyBindingManager;

/**
 * ControllerFacade adapter for view classes that manages the AWT/Swing threading for them and delegates all calls to a real
 * controller.
 */
public class ControllerAdapter implements ControllerFacade {

    private final MainController controller;

    /**
     * Creates the controller adapter from a original controller.
     * @param controller is the original to which all calls are delegated to.
     */
    ControllerAdapter(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void requestAbortGame() {
        runInBackground(() -> controller.requestAbortGame());
    }

    @Override
    public void requestMeeplePlacement(GridDirection position) {
        runInBackground(() -> controller.requestMeeplePlacement(position));

    }

    @Override
    public void requestNewRound() {
        runInBackground(() -> controller.requestNewRound());
    }

    @Override
    public void requestSkip() {
        runInBackground(() -> controller.requestSkip());
    }

    @Override
    public void requestTilePlacement(int x, int y) {
        runInBackground(() -> controller.requestTilePlacement(x, y));
    }

    @Override
    public GlobalKeyBindingManager getKeyBindings() {
        return controller.getKeyBindings(); // TODO (HIGH) [THREADING] Should not be on view thread!
    }

    @Override
    public GameSettings getSettings() {
        return controller.getSettings(); // TODO (HIGH) [THREADING] Should not be on view thread!
    }

    private void runInBackground(Runnable task) { // TODO (HIGH) [THREADING] Should be a single thread to allow aborting
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(task);
    }

}

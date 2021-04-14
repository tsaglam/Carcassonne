package carcassonne.control;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import carcassonne.control.state.StateMachine;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.ai.RuleBasedAI;
import carcassonne.model.grid.GridDirection;
import carcassonne.settings.GameSettings;
import carcassonne.view.GlobalKeyBindingManager;
import carcassonne.view.ViewFacade;
import carcassonne.view.main.MainView;
import carcassonne.view.secondary.MeepleView;
import carcassonne.view.secondary.TileView;
import carcassonne.view.util.GameMessage;

/**
 * Central class of the game, creates all requires components, receives user input from the user interface in the
 * <code>view package</code>, and controls both the <code>view</code> and the <code>model</code>.
 * @author Timur Saglam
 */
public class MainController implements ControllerFacade {
    private GlobalKeyBindingManager keyBindings;
    private MainView mainView;
    private MeepleView meepleView;
    private final GameSettings settings;
    private final StateMachine stateMachine;
    private TileView tileView;

    /**
     * Basic constructor. Creates the view and the model of the game.
     */
    public MainController() {
        settings = new GameSettings();
        createUserInterface();
        ArtificialIntelligence playerAI = new RuleBasedAI(settings);
        ViewFacade views = new ViewFacade(mainView, tileView, meepleView);
        stateMachine = new StateMachine(views, playerAI, settings);
    }

    /**
     * Getter for the global key binding manager.
     * @return the global key bindings.
     */
    @Override
    public GlobalKeyBindingManager getKeyBindings() {
        return keyBindings;
    }

    /**
     * Getter for the {@link GameSettings}, which grants access to the games settings.
     * @return the {@link GameSettings} instance.
     */
    @Override
    public GameSettings getSettings() {
        return settings;
    }

    /**
     * Requests to abort the round.
     */
    @Override
    public void requestAbortGame() {
        stateMachine.getCurrentState().abortGame();
        stateMachine.setAbortRequested(false);
    }

    /**
     * Signals that a abort request was scheduled. This request wait too long during AI vs. AI gameplay, thus this method
     * requests the state machine to abort on the next state change. This method should not be queued on the state machine
     * thread.
     */
    public void requestAsynchronousAbort() {
        stateMachine.setAbortRequested(true);
    }

    /**
     * Method for the view to call if a user mans a tile with a meeple.
     * @param position is the position the user wants to place on.
     */
    @Override
    public void requestMeeplePlacement(GridDirection position) {
        stateMachine.getCurrentState().placeMeeple(position);
    }

    /**
     * Requests to start a new round with a specific amount of players.
     */
    @Override
    public void requestNewRound() {
        stateMachine.getCurrentState().newRound(settings.getNumberOfPlayers());
    }

    /**
     * Method for the view to call if the user wants to skip a round.
     */
    @Override
    public void requestSkip() {
        stateMachine.getCurrentState().skip();
    }

    /**
     * Method for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    @Override
    public void requestTilePlacement(int x, int y) {
        stateMachine.getCurrentState().placeTile(x, y);
    }

    /**
     * Shows the main user interface.
     */
    public void startGame() {
        EventQueue.invokeLater(() -> mainView.showUI());
    }

    /**
     * Creates the views and waits on the completion of the creation.
     */
    private final void createUserInterface() {
        try {
            EventQueue.invokeAndWait(() -> {
                ControllerAdapter adapter = new ControllerAdapter(this);
                mainView = new MainView(adapter);
                tileView = new TileView(adapter, mainView);
                meepleView = new MeepleView(adapter, mainView);
                keyBindings = new GlobalKeyBindingManager(adapter, mainView, tileView);
                mainView.addKeyBindings(keyBindings);
                tileView.addKeyBindings(keyBindings);
                meepleView.addKeyBindings(keyBindings);
                settings.registerNotifiable(mainView.getScoreboard());
                settings.registerNotifiable(mainView);
                settings.registerNotifiable(meepleView);
                settings.registerNotifiable(tileView);
            });
        } catch (InvocationTargetException | InterruptedException exception) {
            GameMessage.showError("Could not create user interface: " + exception.getMessage());
        }
    }
}

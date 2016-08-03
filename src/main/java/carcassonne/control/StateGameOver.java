package carcassonne.control;

/**
 * The specific state where the stats are shown can be placed.
 * @author Timur Saglam
 */
public class StateGameOver extends ControllerState {

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     */
    public StateGameOver(MainController controller) {
        super(controller);
    }

    /**
     * @see carcassonne.control.ControllerState#entry()
     */
    @Override
    protected void entry() {
        // TODO (MEDIUM) show stats in StateGameOver instead of jump to next state
        changeState(StateIdle.class);

    }

    /**
     * @see carcassonne.control.ControllerState#exit()
     */
    @Override
    protected void exit() {
        // TODO (MEDIUM) hide stats in StateGameOver

    }

}

package carcassonne.control;

import carcassonne.model.tile.Tile;

/**
 * The specific state when a Tile can be placed.
 * @author Timur Saglam
 */
public class StatePlacing extends ControllerState {

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     */
    public StatePlacing(MainController controller) {
        super(controller);
    }

    /**
     * @see carcassonne.control.ControllerState#entry()
     */
    @Override
    protected void entry() {
        controller.getRotationGUI().setTile(controller.getRound().getCurrentTile());
    }

    /**
     * @see carcassonne.control.ControllerState#exit()
     */
    @Override
    protected void exit() {
        controller.getRotationGUI().disableFrame();
    }

    @Override
    public boolean placeTile(int x, int y) {
        Tile tile = controller.getRotationGUI().useTile();
        if (controller.getGrid().place(x, y, tile)) {
            controller.getRound().updateCurrentTile(tile);
            controller.getMainGUI().set(tile, x, y);
            controller.getRotationGUI().disableFrame();
            changeState(StateManning.class);
            return true;
        }
        return false;
    }

    @Override
    public boolean skip() {
        controller.getRound().nextTurn();
        return true;
    }

    /**
     * @see carcassonne.control.ControllerState#abortGame()
     */
    @Override
    public boolean abortGame() {
        changeState(StateGameOver.class);
        return true;
    }

}

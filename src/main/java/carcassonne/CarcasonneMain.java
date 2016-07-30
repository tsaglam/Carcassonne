package carcassonne;

import carcassonne.control.MainController;

/**
 * Carcassonne main class for starting the game itself.
 * @author Timur
 */
public final class CarcasonneMain {
    /**
     * Main method for the Carcassonne game.
     * @param args are not used.
     */
    public static void main(String[] args) {
        new MainController();
    }

    private CarcasonneMain() {
        throw new IllegalAccessError("This class mustn't be instantiated.");
    }
}

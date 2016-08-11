package carcassonne;

import carcassonne.control.MainController;

/**
 * Carcassonne main class for starting the game itself.
 * @author Timur Saglam
 */
public final class CarcasonneMain {
    /**
     * Main method for the Carcassonne game.
     * @param args are not used.
     */
    public static void main(String[] args) {
        // try { // Simulate Windows Look and Feel
        // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        new MainController();
    }
    
    private CarcasonneMain() {
    }
}

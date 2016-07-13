package carcassonne.model;

/**
 * The class for the player objects. It manages the meeples and the points.
 * @author Timur
 */
public class Player {

    private int points;
    private int playerNumber;

    /**
     * TODO comment method.
     */
    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        points = 0;
    }

    /**
     * Getter for the points of the player.
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds points to the players point value.
     * @param points are the points to add.
     */
    public void addPoints(int points) {
        points += points;
    }

}

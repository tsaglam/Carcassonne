package carcassonne.model.ai;

import carcassonne.model.Meeple;
import carcassonne.model.Player;

/**
 * Temporary meeples that prevents
 * @author Timur Saglam
 */
public class TemporaryMeeple extends Meeple {

    public TemporaryMeeple(Player owner) {
        super(owner);
    }

    @Override
    public void removePlacement() {
        throw new IllegalStateException("Temporary meeple is being removed. Should not be used as real meeple.");
    }
}

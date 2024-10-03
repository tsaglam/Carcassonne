package carcassonne.view.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Utility interface that allows declaring functional mouse click listeners. By providing default methods for the other
 * method signatures defined by {@link MouseListener}, this becomes a functional interface. This interface can be used
 * like this: <code>addMouseListener((MouseClickListener)(e)->doSomething())</code>
 * @author Timur Saglam
 */
public interface MouseClickListener extends MouseListener {

    @Override
    default void mousePressed(MouseEvent event) {
        // do nothing as only clicks are listened to.
    }

    @Override
    default void mouseClicked(MouseEvent event) {
        // do nothing as only clicks are listened to.
    }

    @Override
    default void mouseEntered(MouseEvent event) {
        // do nothing as only clicks are listened to.
    }

    @Override
    default void mouseExited(MouseEvent event) {
        // do nothing as only clicks are listened to.
    }
}

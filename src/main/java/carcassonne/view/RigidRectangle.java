package carcassonne.view;

import java.awt.Dimension;

import javax.swing.Box.Filler;

/**
 * Creates an invisible component that's always of a specified size but still allows changing that size manually.
 * @author Timur Saglam
 */
public class RigidRectangle extends Filler {

    private static final long serialVersionUID = 8847635761705081422L;

    /**
     * Creates a rigid rectangle.
     * @param dimension specifies the width and height of the rectangle.
     */
    public RigidRectangle(Dimension dimension) {
        super(dimension, dimension, dimension);
    }

    /**
     * Creates a rigid square.
     * @param sideLength is the square.
     */
    public RigidRectangle(int sideLength) {
        this(new Dimension(sideLength, sideLength));
    }

    /**
     * Changes the fixed size of the rectangle.
     * @param dimension specifies the width and height of the rectangle.
     */
    public void changeShape(Dimension dimension) {
        changeShape(dimension, dimension, dimension);
    }

    /**
     * Changes the fixed size of the rectangle.
     * @param sideLength is the square.
     */
    public void changeShape(int sideLength) {
        changeShape(new Dimension(sideLength, sideLength));
    }
}

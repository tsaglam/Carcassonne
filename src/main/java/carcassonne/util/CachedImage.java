package carcassonne.util;

import java.awt.Image;

/**
 * Data objects to cache images with their scaling information.s
 * @author Timur Saglam
 */
public class CachedImage {
    private final Image image;
    private final boolean preview;

    /**
     * Creates
     * @param image the image to cache, cannot be null.
     * @param preview whether this image was scaled as a preview image or not.
     */
    public CachedImage(Image image, boolean preview) {
        if (image == null) {
            throw new IllegalArgumentException("Cached image cannot be null!");
        }
        this.image = image;
        this.preview = preview;
    }

    public Image getImage() {
        return image;
    }

    public boolean isPreview() {
        return preview;
    }
}

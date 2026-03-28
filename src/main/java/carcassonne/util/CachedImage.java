package carcassonne.util;

import java.awt.Image;
import java.util.Objects;

/**
 * Data object to cache images with their scaling information.
 * @param image the cached image, cannot be null
 * @param isPreview whether this image was scaled as a preview image or not
 * @author Timur Saglam
 */
public record CachedImage(Image image, boolean isPreview) {

    /**
     * Compact constructor with validation.
     */
    public CachedImage {
        Objects.requireNonNull(image, "Cached image cannot be null!");
    }
}

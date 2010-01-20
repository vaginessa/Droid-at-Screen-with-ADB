package com.ribomation.droidAtScreen.img;

import java.awt.image.BufferedImage;

/**
 * Transforms an image.
 *
 * @user jens
 * @date 2010-jan-17 14:45:25
 */
public interface ImageTransformer {

    /**
     * Returns a transformed image.
     * @param img   input image
     * @return transformed image
     */
    BufferedImage   transform(BufferedImage img);
}

package com.ribomation.droidAtScreen.img;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Dummy transformer that returns the input image verbatim.
 *
 * @user jens
 * @date 2010-jan-17 14:47:11
 */
public class NoopImageTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage img) {
        return img;
    }
}

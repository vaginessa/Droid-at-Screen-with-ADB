package com.ribomation.droidAtScreen.img;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Performs a scaling operation.
 *
 * @user jens
 * @date 2010-jan-17 14:50:34
 */
public class ScaleImageTransformer implements ImageTransformer {
    private RenderingHints  hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    private double          scale = 1;

    public ScaleImageTransformer() {
        setScale(1);
    }

    public ScaleImageTransformer(double scale) {
        setScale(scale);
    }

    public ScaleImageTransformer(int scale) {
        setScale(scale);
    }

    public double getScale() {
        return scale;
    }

    /**
     * Sets the value of the scale transformation.
     * @param scale     a positive real number (1 = no scaling)
     */
    public void setScale(double scale) {
        if (scale <= 0) {
            throw new IllegalArgumentException("Transformation scale must be a positive real number. Illegal value=" + scale);
        }
        this.scale = scale;
    }

    /**
     * Sets the scale using a percentage.
     * @param scale     in %
     */
    public void setScale(int scale) {
        setScale(scale / 100.0);
    }


    @Override
    public BufferedImage transform(BufferedImage img) {
//        if (getScale() == 1) return img;

        int w = img.getWidth(), h = img.getHeight();
        AffineTransform     tx = createTX();
        AffineTransformOp   op = new AffineTransformOp(tx, hints);

        return op.filter(img, new BufferedImage(w, h, img.getType()));
    }

    private AffineTransform createTX() {
        if (getScale() == 1) return new AffineTransform();
        return AffineTransform.getScaleInstance(scale, scale);
    }
}

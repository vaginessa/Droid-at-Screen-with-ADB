/*
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.dev;

import com.android.ddmlib.RawImage;

import java.awt.image.BufferedImage;

/**
 * Wrapper around a Android raw screen image.
 *
 * @user jens
 * @date 2011-10-02 14:03
 */
public class ScreenImage {
    private RawImage rawImage;

    public ScreenImage(RawImage rawImage) {
        this.rawImage = rawImage;
    }

    public RawImage getRawImage() {
        return rawImage;
    }

    public BufferedImage toBufferedImage() {
        final int       W = rawImage.width;
        final int       H = rawImage.height;
        BufferedImage   image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        int             bytesPerPixels = rawImage.bpp >> 3; //bpp = bits / pixels --> bytes / pixels

        for (int y = 0, pixelIdx = 0; y < H; y++) {
            for (int x = 0; x < W; x++, pixelIdx += bytesPerPixels) {
                image.setRGB(x, y, rawImage.getARGB(pixelIdx));
            }
        }

        return image;
    }

    public ScreenImage rotate() {
        rawImage = rawImage.getRotated();
        return this;
    }

    @Override
    public String toString() {
        return String.format("RawImage[%dx%d, %d bytes, bits/px=%d]",
                rawImage.width, rawImage.height, rawImage.data.length, rawImage.bpp);
    }

    public int getWidth() {
        return rawImage.width;
    }

    public int getHeight() {
        return rawImage.height;
    }
}

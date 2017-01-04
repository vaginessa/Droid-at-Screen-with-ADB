/*
 * Project:  droidAtScreen
 * File:     ScreenImage.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.dev;

import com.android.ddmlib.RawImage;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Wrapper around a Android raw screen image.
 */
public class ScreenImage {
	private long size;
	private BufferedImage bufferedImage;

	public BufferedImage toBufferedImage() {
		return bufferedImage;
	}

	public ScreenImage copy() {
		ColorModel colorModel = bufferedImage.getColorModel();
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		WritableRaster raster = bufferedImage.copyData(bufferedImage.getRaster().createCompatibleWritableRaster());
		BufferedImage bImage = new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
		ScreenImage copy = new ScreenImage();
		copy.setBufferedImage(bImage, size);
		return copy;
	}

	public void setBufferedImage(BufferedImage bufferedImage, long size) {
		this.bufferedImage = bufferedImage;
		this.size = size;
	}

	public void setBufferedImage(RawImage rawImage) {
		if (rawImage != null) {
			final int W = rawImage.width;
			final int H = rawImage.height;
			bufferedImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
			int bytesPerPixels = rawImage.bpp >> 3; //bpp = bits / pixels --> bytes / pixels
			for (int y = 0, pxIdx = 0; y < H; y++) {
				for (int x = 0; x < W; x++, pxIdx += bytesPerPixels) {
					bufferedImage.setRGB(x, y, rawImage.getARGB(pxIdx));
				}
			}
			size = rawImage.size;
		}
	}

	public long getSize() {
		return size;
	}

	public int getWidth() {
		return bufferedImage.getWidth();
	}

	public int getHeight() {
		return bufferedImage.getHeight();
	}
}

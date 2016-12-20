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
		copy.setBufferedImage(bImage);
		copy.setSize(size);
		return copy;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setSize(long size) {
		this.size = size;
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

package com.ribomation.droidAtScreen.dev;

import com.ribomation.droidAtScreen.dev.AndroidDevice;
import com.ribomation.droidAtScreen.img.ImageTransformer;

import java.awt.image.BufferedImage;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-17 22:49:53
 */
public class TransformingAndroidDevice  extends AndroidDeviceDecorator {
    private ImageTransformer    transformer;
        
    public TransformingAndroidDevice(ImageTransformer transformer, AndroidDevice device) {
        super(device);
        this.transformer = transformer;
    }

    @Override
    public BufferedImage getScreenShot() {
        return transformer.transform( super.getScreenShot() );
    }
}

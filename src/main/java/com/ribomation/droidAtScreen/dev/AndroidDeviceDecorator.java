package com.ribomation.droidAtScreen.dev;

import com.ribomation.droidAtScreen.dev.AndroidDevice;

import java.awt.image.BufferedImage;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-17 22:47:43
 */
public class AndroidDeviceDecorator implements AndroidDevice {
    private AndroidDevice   device;

    public AndroidDeviceDecorator(AndroidDevice device) {
        this.device = device;
    }

    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public boolean isEmulator() {
        return device.isEmulator();
    }

    @Override
    public ConnectionState getState() {
        return device.getState();
    }

    @Override
    public BufferedImage getScreenShot() {
        return device.getScreenShot();
    }

    @Override
    public BufferedImage getScreenShot(boolean landscapeMode) {
        return getScreenShot();
    }

    @Override
    public boolean equals(Object obj) {
        return device.equals(obj);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }

    @Override
    public String toString() {
        return device.toString();
    }
}

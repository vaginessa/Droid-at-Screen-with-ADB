package com.ribomation.droidAtScreen.dev;

import java.awt.image.BufferedImage;

/**
 * Facade/wrapper around an Android device.
 *
 * @user jens
 * @date 2010-jan-17 12:16:55
 */
public interface AndroidDevice {
    /**
     * Models the device state
     */
    enum ConnectionState {booting, offline, online}

    /**
     * Returns its device name.
     * @return its name
     */
    String              getName();

    /**
     * Returns if true if it's an emulator, i.e., not a physical device.
     * @return true if not physical device
     */
    boolean             isEmulator();

    /**
     * Returns its connect state
     * @return its state
     */
    ConnectionState     getState();

    /**
     * Captures and returns a screen-shot from the device.
     * @return a new screen shot
     * @throws RuntimeException     if it failed
     */
    BufferedImage       getScreenShot();

    /**
     * Captures and returns a screen-shot from the device.
     * @param landscapeMode     true if the image should flipped to landscape mode
     * @return a new screen shot
     * @throws RuntimeException     if it failed
     */
    BufferedImage       getScreenShot(boolean landscapeMode);
}

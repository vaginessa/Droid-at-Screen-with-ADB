package com.ribomation.droidAtScreen;

import com.ribomation.droidAtScreen.dev.AndroidDevice;
import com.ribomation.droidAtScreen.dev.AndroidDeviceListener;
import com.ribomation.droidAtScreen.gui.ApplicationFrame;
import com.ribomation.droidAtScreen.gui.DeviceFrame;

import javax.swing.*;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * Application interface that provides a set of services for disparate parts of the app.
 *
 * @user jens
 * @date 2010-jan-18 10:06:42
 */
public interface Application {

    ApplicationFrame getAppFrame();

    AndroidDevice       getSelectedDevice();

    String getName();

    String getVersion();

    void addAndroidDeviceListener(AndroidDeviceListener listener);

    void setScale(int percentage);

//    void addDevice(AndroidDevice dev);
//
//    void removeDevice(AndroidDevice dev);

    Preferences getPreferences();

    void setAutoShow(boolean show);

    void setSkipEmulator(boolean skip);

    void setAdbExecutablePath(File file);

    void setPortraitMode(boolean portrait);

    void setUpsideDown(boolean upsideDown);

    void setFrameRate(int rate);

    void savePreferences();

    void destroyPreferences();

    void showDevice(AndroidDevice dev);

    void hideDevice(DeviceFrame dev);
    void hideDevice(DeviceFrame dev, boolean doDispose);

}

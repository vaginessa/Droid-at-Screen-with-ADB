package com.ribomation.droidAtScreen.dev;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A facade to AndroidDebugBridge.
 *
 * @user jens
 * @date 2010-jan-17 11:13:33
 */
public class AndroidDeviceManager
     extends Thread
  implements AndroidDebugBridge.IDeviceChangeListener,
             AndroidDebugBridge.IDebugBridgeChangeListener
{
    private Logger                          log = Logger.getLogger(this.getClass());
    private List<AndroidDeviceListener>     listeners = new ArrayList<AndroidDeviceListener>();
    private Map<String, AndroidDevice>      devices = new HashMap<String, AndroidDevice>();


    public AndroidDeviceManager() {
        AndroidDebugBridge.init(false);
        Runtime.getRuntime().addShutdownHook(this);

        AndroidDebugBridge.addDebugBridgeChangeListener(this);
        AndroidDebugBridge.addDeviceChangeListener(this);
    }

    public boolean isInitialized() {
        return getAdb() != null;
    }

    public void init() {
        if (getAdbExecutable() != null && getAdb() == null) {
            try {
                adb = AndroidDebugBridge.createBridge(getAdbExecutable().getCanonicalPath(), true);
            } catch (IOException e) {
                throw new RuntimeException("Failed to created the absolute path to the ADB executable: " + getAdbExecutable());
            }
        }
    }

    public Map<String, AndroidDevice> getDevices() {
        return Collections.unmodifiableMap( devices );
    }

    public void addAndroidDeviceListener(AndroidDeviceListener l) {
        listeners.add(l);
    }

    public void removeAndroidDeviceListener(AndroidDeviceListener l) {
        listeners.remove(l);
    }

    @Override
    public void bridgeChanged(AndroidDebugBridge adb) {
        this.adb = adb;
        log.debug("ADB changed");
    }

    @Override
    public void deviceConnected(IDevice target) {
        log.info("Device connected: " + target);

        AndroidDevice  dev = new AndroidDeviceImpl(target);
        devices.put(dev.getName(), dev);
        for (AndroidDeviceListener deviceListener : listeners) {
            deviceListener.connected(dev);
        }
    }

    @Override
    public void deviceDisconnected(IDevice target) {
        log.info("Device disconnected: " + target);
        AndroidDevice dev = devices.remove( new AndroidDeviceImpl(target).getName() );
        if (dev != null) {
            for (AndroidDeviceListener deviceListener : listeners) {
                deviceListener.disconnected(dev);
            }
        }
    }

    @Override
    public void deviceChanged(IDevice dev, int changeMask) {
        log.debug("Device changed: " + dev + ", mask=" + toMaskString(changeMask));
    }

    protected String toMaskString(int mask) {
        StringBuilder   result = new StringBuilder("");
        if ((mask & IDevice.CHANGE_BUILD_INFO) != 0) {
            result.append("CHANGE_BUILD_INFO ");
        }
        if ((mask & IDevice.CHANGE_CLIENT_LIST) != 0) {
            result.append("CHANGE_CLIENT_LIST ");
        }
        if ((mask & IDevice.CHANGE_STATE) != 0) {
            result.append("CHANGE_STATE ");
        }
        return result.toString();
    }


    @Override
    public void run() {
        try {
            AndroidDebugBridge.disconnectBridge();
            AndroidDebugBridge.terminate();
        } catch (Exception e) {
            System.err.println("Failed to shutdown Android Device Bridge " + e);
        }
    }

    /**
     * The ADB object.
     */
    private AndroidDebugBridge adb;

    public AndroidDebugBridge getAdb() {
        return adb;
    }

    /**
     * Refers the ADB executable.
     */
    private File adbExecutable;

    public File getAdbExecutable() {
        return adbExecutable;
    }

    public void setAdbExecutable(File adbExecutable) {
        if (!adbExecutable.isFile()) {
            throw new RuntimeException("ADB executable '" + adbExecutable + "' is not a file");
        }
        if (!adbExecutable.canExecute()) {
            throw new RuntimeException("ADB executable '" + adbExecutable + "' is not executable.");
        }
        this.adbExecutable = adbExecutable;

        try {
            adb = AndroidDebugBridge.createBridge(getAdbExecutable().getCanonicalPath(), false);
        } catch (IOException e) {
            throw new RuntimeException("Failed to created the absolute path to the ADB executable: " + getAdbExecutable());
        }
    }
}

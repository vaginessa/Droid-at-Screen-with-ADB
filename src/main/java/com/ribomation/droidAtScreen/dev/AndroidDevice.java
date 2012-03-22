/*
 * Project:  droidAtScreen
 * File:     AndroidDevice.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.dev;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Map;

/**
 * Wrapper around an Android device.
 *
 * @user jens
 * @date 2010-jan-17 12:16:55, 2011-Oct-02
 */
public class AndroidDevice implements Comparable<AndroidDevice> {
    /**
     * Models the device state
     */
    enum ConnectionState {booting, offline, online}


    private static final double     SECS = 1000 * 1000 * 1000.0D;
    private static final int        MAX_TIMINGS = 60;
    private final Logger            log;
    private final IDevice           target;
    private final ArrayDeque<Long>  timings = new ArrayDeque<Long>(MAX_TIMINGS);

    public AndroidDevice(IDevice target) {
        this.target = target;
        log = Logger.getLogger(AndroidDevice.class.getName() + ":" + target.getSerialNumber());
    }

    public ScreenImage  getScreenImage() {
        try {
            long        start    = System.nanoTime();
            RawImage    rawImage = target.getScreenshot();
            long        elapsed  = System.nanoTime() - start;
            if (rawImage == null) return null;

            timings.addLast(elapsed);
            if (timings.size() > MAX_TIMINGS) timings.removeFirst();

            ScreenImage image = new ScreenImage(rawImage);
//            log.debug(String.format("Captured %s in %.4f secs", image, elapsed / SECS));

            return image;
        } catch (IOException e) {
            log.error("Failed to get screenshot: " + e);
            throw new RuntimeException("Failed to get screenshot", e);
        } catch (TimeoutException e) {
            log.warn("Got timeout");
            return null;
        } catch (AdbCommandRejectedException e) {
            log.error("ADB command rejected: OFFLINE=" + e.isDeviceOffline());
            throw new RuntimeException(e);
        }
    }

    public long getAverageTimings() {
        if (timings.isEmpty()) return 0;
        
        long sum = 0;
        for (Long t : timings) sum += t;
        return sum / timings.size();
    }

    public double getAverageTimingsInSeconds() {
        return getAverageTimings() / SECS;
    }

    public ConnectionState getState() {
        IDevice.DeviceState s = target.getState();
        if (s == IDevice.DeviceState.ONLINE) return ConnectionState.online;
        if (s == IDevice.DeviceState.BOOTLOADER) return ConnectionState.booting;
        if (s == IDevice.DeviceState.OFFLINE) return ConnectionState.offline;
        return ConnectionState.offline;
    }

    public String getName() {
        return target.getSerialNumber();
    }

    public String getAVDName() {
        return target.getAvdName();
    }

    public Map<String, String> getProperties() {
        return target.getProperties();
    }

    public boolean isEmulator() {
        return target.isEmulator();
    }

    @Override
    public String toString() {
        return getName() + " (" + (isEmulator() ? "emulator" : "device") + ")";
    }

    @Override
    public boolean equals(Object obj) {
        System.out.printf("AndroidDevice.equals: %s == %s%n", this, obj);

        if (this == obj) return true;
        if (!(obj instanceof AndroidDevice)) return false;

        AndroidDevice that = (AndroidDevice) obj;
        return this.getName().equals( that.getName() );
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public int compareTo(AndroidDevice that) {
        return this.getName().compareTo( that.getName() );
    }
    
    
}

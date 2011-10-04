/*
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen;

import org.apache.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Facad for all the application settings.
 *
 * @user jens
 * @date 2011-10-03 22:58
 */
public class Settings {
    private final Preferences    applicationPreferences;
    private final Logger         log;
    private PropertyChangeSupport   propSupport;

    public Settings() {
        applicationPreferences = Preferences.userNodeForPackage(Settings.class);
        log = Logger.getLogger(Settings.class);
        propSupport = new PropertyChangeSupport(this);
    }

    public void addListener(String name, PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(name, listener);
    }

    public void removeListener(String name, PropertyChangeListener listener) {
        propSupport.removePropertyChangeListener(name, listener);
    }

    public void destroyPreferences() {
        try {
            getPreferences().removeNode();
        } catch (BackingStoreException e) {
            log.error("Failed to destroy app settings", e);
        }
    }

    public void dump() {
        log.debug("--- Application Settings ---");
        try {
            for (String key : applicationPreferences.keys()) {
                log.debug(String.format("  %s: %s", key, applicationPreferences.get(key, "[none]")));
            }
        } catch (BackingStoreException e) {
            log.warn("Failed to dump the app settings", e);
        }
    }

    public Preferences getPreferences() {
        return applicationPreferences;
    }

    protected void savePreferences() {
        try {
            getPreferences().flush();
        } catch (BackingStoreException e) {
            log.error("Failed to flush app settings", e);
        }
    }

    // ----------------------------------------------------------
    // --- Settings
    // ----------------------------------------------------------

    public File getAdbExecutable() {
        String f = applicationPreferences.get("adbExecutable", null);
        if (f != null) return new File(f);
        return null;
    }

    public void setAdbExecutable(File value) {
        File oldExe = getAdbExecutable();
        applicationPreferences.put("adbExecutable", value.getAbsolutePath());
        savePreferences();
        propSupport.firePropertyChange("adbExecutable", oldExe, value);
    }

    public boolean isAskBeforeQuit() {
        return applicationPreferences.getBoolean("askBeforeQuit", true);
    }

    public void setAskBeforeQuit(boolean value) {
        set("askBeforeQuit", value);
    }

    private static final String[] IMG_FMTS = {"PNG", "JPG"};
    public String[] getImageFormats() {
        return IMG_FMTS;
    }
    
    public String   getImageFormat() {
        return applicationPreferences.get("imageFormat", "PNG");
    }

    public void setImageFormat(String value) {
        set("imageFormat", value);
    }

    private static final Integer[]           scales = {25,50,75,100,125,150,175,200,250,300};
    public Integer[]    getScales() {
        return scales;
    }

    public int getScale() {
        return applicationPreferences.getInt("scale", 100);
    }

    public void setScale(int value) {
        set("scale", value);
    }

    private static final Integer[] updatesPerMinute = {1, 15, 30, 60, 100};
    public Integer[] getFrameRates() {
        return updatesPerMinute;
    }

    public int getFrameRate() {
        return applicationPreferences.getInt("frameRate", 60);
    }

    public void setFrameRate(int value) {
        set("frameRate", value);
    }

    public boolean isHideEmulators() {
        return applicationPreferences.getBoolean("hideEmulators", false);
    }

    public void setHideEmulators(boolean value) {
        set("hideEmulators", value);
    }

    public boolean isUpsideDown() {
        return applicationPreferences.getBoolean("upsideDown", false);
    }

    public void setUpsideDown(boolean value) {
        set("upsideDown", value);
    }

    public boolean isLandscape() {
        return applicationPreferences.getBoolean("landscape", false);
    }

    public void setLandscape(boolean value) {
        set("landscape", value);
    }

    public boolean isAutoShow() {
        return applicationPreferences.getBoolean("autoShow", false);
    }

    public void setAutoShow(boolean value) {
        set("autoShow", value);
    }










    private void set(String name, String value) {
        String old = applicationPreferences.get(name, "PNG");
        applicationPreferences.put(name, value);
        savePreferences();
        propSupport.firePropertyChange(name, old, value);
    }

    private void set(String name, int value) {
        int old = applicationPreferences.getInt(name, 60);
        applicationPreferences.putInt(name, value);
        savePreferences();
        propSupport.firePropertyChange(name, old, value);
    }

    private void set(String name, boolean value) {
        boolean old = applicationPreferences.getBoolean(name, false);
        applicationPreferences.putBoolean(name, value);
        savePreferences();
        propSupport.firePropertyChange(name, old, value);
    }

}


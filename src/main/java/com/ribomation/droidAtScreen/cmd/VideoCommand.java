/*
 * Project:  droidAtScreen
 * File:     VideoCommand.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.Settings;
import com.ribomation.droidAtScreen.dev.RecordingListener;
import com.ribomation.droidAtScreen.dev.ScreenImage;
import com.ribomation.droidAtScreen.gui.DeviceFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Records a series of screen-shots into an AVI video clip.
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class VideoCommand extends Command implements RecordingListener {
    private AtomicInteger               count = new AtomicInteger(1);
    private AtomicBoolean               capturing = new AtomicBoolean(false);
    private BlockingQueue<ScreenImage>  imageQueue = new LinkedBlockingQueue<ScreenImage>(120);
    private Thread                      runner;

    public VideoCommand() {
        setLabel("Record");
        setTooltip("Continuously record screen-shots and save them to a directory, for further processing.");
        setIcon("record");
        setEnabledOnlyWithDevice(true);
    }

    @Override
    protected void doExecute(Application app) {
        DeviceFrame device = app.getSelectedDevice();
        if (device == null) return;

        if (capturing.get()) {
            capturing.set(false);
            if (runner != null) runner.interrupt();
            return;
        }

        final Settings settings = app.getSettings();

        File imageDir = new File(".");
        if (settings.isAskBeforeScreenshot()) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(app.getSettings().getImageDirectory());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Select target directory for the images");
            chooser.setApproveButtonText("Images Dir");
            chooser.setApproveButtonToolTipText("All screen-shots will go into this directory, sequentially numbered.");
            
            int rc = chooser.showOpenDialog(app.getAppFrame());
            if (rc != JFileChooser.APPROVE_OPTION) return;

            imageDir = chooser.getSelectedFile();
            if (!(imageDir.isAbsolute() && imageDir.canWrite())) {
                JOptionPane.showMessageDialog(app.getAppFrame(),
                        "Not a writable directory " + imageDir, "Invalid directory", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        boolean notEmpty = imageDir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isFile();
            }
        }).length > 0;
        if (notEmpty) {
            int rc = JOptionPane.showConfirmDialog(app.getAppFrame(),
                    "The chosen directory is not empty. \n" +
                            "Do you still want to proceed and possibly \n" +
                            "overwrite some/all of previously saved screen-shots?",
                    "Not empty directory",
                    JOptionPane.YES_NO_OPTION);
            if (rc != JOptionPane.YES_OPTION) return;
        }

        final File destinationDir = imageDir;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    while (capturing.get()) {
                        ScreenImage     image  = imageQueue.take();
                        String          format = getApplication().getSettings().getImageFormat();
                        File            file   = new File(destinationDir, String.format("droidAtScreen-%d.%s", count.getAndIncrement(), format.toLowerCase()));

                        ImageIO.write(image.toBufferedImage(), format, file);
                        getLog().debug("written " + file);
                    }
                } catch (InterruptedException ignore) {
                    return;
                } catch (IOException e) {
                    getLog().error("Failed to write image to " + destinationDir, e);
                    return;
                } finally {
                    getLog().info("Continuous screen capture recording stopped, count=" + count.get());
                    capturing.set(false);
                    imageQueue.clear();
                    final int oldCount = count.getAndSet(1) - 1;
                    runner = null;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setIcon("record");
                            getApplication().getAppFrame().getStatusBar().message("Recording stopped. %d images saved.", oldCount);
                        }
                    });
                }
            }
        };

        
        setIcon("recording");
        imageQueue.clear();
        capturing.set(true);
        count.set(1);
        runner = new Thread(task);
        runner.start();
        device.setRecordingListener(this);
        getLog().info("Continuous screen capture recording started, dir=" + imageDir.getAbsolutePath());
        app.getAppFrame().getStatusBar().message("Recording to %s", truncate(imageDir.getAbsolutePath(), 30));
    }

    public void record(ScreenImage image) {
        try {
            imageQueue.put(image);
        } catch (InterruptedException ignore) {
        }
    }

    private String truncate(String s, int max) {
        int n = s.length();
        return (n > max ? "..." : "") + s.substring((n - max) < 0 ? 0 : n - max);
    }

}


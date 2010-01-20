package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.dev.AndroidDevice;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class ScreenShotCommand extends Command {
    private static final String     JPG = "jpg";
    private File                    lastFile = null;

    public ScreenShotCommand() {
        setLabel("Save ScreenShot as JPG");
        setTooltip("Takes a screen-shot of the current device and saves it as JPG file.");
//        setIcon("quit_16x16");
//        setMnemonic('Q');
        setEnabled(false);
    }

    @Override
    protected void doExecute(final Application app) {
        AndroidDevice dev = app.getSelectedDevice();
        if (dev == null) return;

        final BufferedImage     screenShot = dev.getScreenShot();
        JFileChooser            chooser    = new JFileChooser(lastFile);
        chooser.setSelectedFile(lastFile!=null ? lastFile : new File("droidAtScreen.jpg"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG Files", "jpg"));

        int rc = chooser.showSaveDialog(app.getAppFrame());
        if (rc == JFileChooser.APPROVE_OPTION) {
            lastFile = chooser.getSelectedFile();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (lastFile.exists()) {
                            int rc = JOptionPane.showConfirmDialog(app.getAppFrame(),
                                    "File '"+lastFile+"' already exist. Do you want to overwrite?",
                                    "Overwrite file",
                                    JOptionPane.YES_NO_OPTION);
                            if (rc != JOptionPane.YES_OPTION) return;
                        }

                        ImageIO.write(screenShot, JPG, lastFile);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(app.getAppFrame(),
                                "Failed to save screen-shot file " + lastFile + ". " + e,
                                "Filed to save file",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

    }
}

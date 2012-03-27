/*
 * Project:  droidAtScreen
 * File:     ApplicationFrame.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.cmd.Command;
import com.ribomation.droidAtScreen.cmd.QuitCommand;
import com.ribomation.droidAtScreen.dev.AndroidDevice;
import com.ribomation.droidAtScreen.dev.AndroidDeviceListener;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main GUI window.
 *
 * @user jens
 * @date 2010-jan-18 17:44:12
 */
public class ApplicationFrame extends JFrame {
    private Logger                  log = Logger.getLogger(ApplicationFrame.class);
    private Application             app;
//    private DefaultComboBoxModel    deviceListModel = new DefaultComboBoxModel();
    private StatusBar               statusBar;

    private final String[] TOOLBAR      = {"ImageDirectory", "-", "AdbRestart", "AdbReloadDevices", "-", "About"};
    private final String[] FILE_MENU    = {"Quit"};
    private final String[] IMAGE_MENU   = {"ImageDirectory", "ImageFormat", "AskBeforeScreenshot"};
    private final String[] ADB_MENU     = {"AdbRestart", "AdbReloadDevices", "AdbExePath"};
    private final String[] OPTIONS_MENU = {"PreferredScale", "-", "AutoShow", "SkipEmulator", "AskBeforeQuit", "-", "LookAndFeel", "-", "RemoveProperties"};
    private final String[] HELP_MENU    = {"Help", "About"};


    public ApplicationFrame(Application app) throws HeadlessException {
        this.app = app;
    }

    public StatusBar getStatusBar() { return statusBar; }
    
//    public ComboBoxModel getDeviceList() { return deviceListModel; }

    public void  initGUI() {
        setIconImage(GuiUtil.loadIcon("device").getImage());
        setTitle(app.getInfo().getName() + ", Version " + app.getInfo().getVersion());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Command.find(QuitCommand.class).execute();
            }
        });

        setJMenuBar(createMenubar());
        add(GuiUtil.createToolbar(TOOLBAR), BorderLayout.NORTH);
        add(createDevicesTable(), BorderLayout.CENTER);
        add(statusBar = new StatusBar(app), BorderLayout.SOUTH);

        pack();
        setLocationByPlatform(true);
    }

    protected JMenuBar createMenubar() {
        JMenuBar     mb = new JMenuBar();
        mb.add(GuiUtil.createMenu("File"   , 'F', FILE_MENU));
        mb.add(GuiUtil.createMenu("Image"  , 'I', IMAGE_MENU));
        mb.add(GuiUtil.createMenu("ADB"    , 'A', ADB_MENU));
        mb.add(GuiUtil.createMenu("Options", 'O', OPTIONS_MENU));
        mb.add(GuiUtil.createMenu("Help"   , 'H', HELP_MENU));
        return mb;
    }
    
    
    private JComponent createDevicesTable() {
        JTable tbl = new JTable(app.getDeviceTableModel());
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbl.setRowSelectionAllowed(true);
        tbl.setShowHorizontalLines(true);
        tbl.setFillsViewportHeight(true);

        JScrollPane pane = new JScrollPane(tbl);
        pane.setBorder(BorderFactory.createTitledBorder("Devices"));
//        pane.setMinimumSize(new Dimension(400, 200));
        
        return pane;
    }
    

//    private JPanel createDeviceControlPane() {
//        JPanel p = new JPanel(new GridLayout(1, 1, 0, 5));
//        p.add(createDevicesList());
//        return p;
//    }
//
//    private JPanel createDevicesList() {
//        JComboBox devices = new JComboBox(deviceListModel);
//        devices.setPreferredSize(new Dimension(200, 20));
//
//        app.addAndroidDeviceListener(new AndroidDeviceListener() {
//            @Override
//            public void connected(AndroidDevice dev) {
//                log.debug("[devicesBox] connected: dev=" + dev);
//                deviceListModel.addElement(dev.getName());
//                deviceListModel.setSelectedItem(dev.getName());
//            }
//
//            @Override
//            public void disconnected(AndroidDevice dev) {
//                log.debug("[devicesBox] disconnected: dev=" + dev);
//                deviceListModel.removeElement(dev.getName());
//            }
//        });
//
//        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p.setBorder(BorderFactory.createTitledBorder("Devices"));
//        p.add(devices);
//        p.add( Command.get("Show").createButton() );
//
//        return p;
//    }

}

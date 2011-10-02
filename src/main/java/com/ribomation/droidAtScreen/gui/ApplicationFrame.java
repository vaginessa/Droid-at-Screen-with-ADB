package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.cmd.Command;
import com.ribomation.droidAtScreen.cmd.QuitCommand;
import com.ribomation.droidAtScreen.dev.AndroidDevice;
import com.ribomation.droidAtScreen.dev.AndroidDeviceListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 17:44:12
 */
public class ApplicationFrame extends JFrame {
    private Logger                  log = Logger.getLogger(ApplicationFrame.class);
    private Application             application;
//    @Deprecated
    private DefaultComboBoxModel    deviceListModel = new DefaultComboBoxModel();
    private StatusBar               statusBar;
//    private DevicesPane             devices;

//    private final String[] TOOLBAR   = {"ImageFormat", "LookAndFeel", "AdbExePath", "About", "Quit"};
    private final String[] TOOLBAR   = {"Orientation", "Scale", "-", "ScreenShot", "Video", "-", "Quit"};
    private final String[] FILE_MENU = {"ScreenShot", "Video", "-", "Quit"};
    private final String[] VIEW_MENU = {"Orientation", "Scale", "UpsideDown"};
    private final String[] HELP_MENU = {"About"};
    private final String[] OPTIONS_MENU = {
            "ImageFormat", "FrameRate",
            "-", "AutoShow", "SkipEmulator", "AskBeforeQuit",
            "-", "AdbExePath", "-", "LookAndFeel", "-", "RemoveProperties"
    };

    public ApplicationFrame() throws HeadlessException {
        super();
    }

    public ApplicationFrame(Application application) throws HeadlessException {
        this.application = application;
    }

    public Application getApplication() {
        if (application == null) {
            throw new IllegalStateException("Missing application ref. Must invoke setApplication(...) before use.");
        }
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

//    @Deprecated
    public ComboBoxModel getDeviceList() { return deviceListModel; }

//    public DevicesPane getDevices() { return devices; }

    public void  initGUI() {
        setIconImage(GuiUtil.loadIcon("device").getImage());
        setTitle(getApplication().getName()+", Version "+getApplication().getVersion());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Command.find(QuitCommand.class).execute();
            }
        });

        setJMenuBar( createMenubar() );
        add(GuiUtil.createToolbar(TOOLBAR), BorderLayout.NORTH);
        add( createDeviceControlPane() , BorderLayout.CENTER);
        add( statusBar = new StatusBar(application) , BorderLayout.SOUTH);

        pack();
        setLocationByPlatform(true);
    }

//    @Override
//    public Dimension getMinimumSize() {
//        Dimension dev = devices.getMinimumSize();
//        Dimension frm = super.getMinimumSize();
//        return new Dimension(Math.max(dev.width, frm.width), Math.max(dev.height, frm.height));
//    }

    protected JMenuBar createMenubar() {
        JMenuBar     mb = new JMenuBar();
        mb.add(GuiUtil.createMenu("File"   , 'F', FILE_MENU));
        mb.add(GuiUtil.createMenu("View"   , 'V', VIEW_MENU));
        mb.add(GuiUtil.createMenu("Options", 'O', OPTIONS_MENU));
        mb.add(GuiUtil.createMenu("Help"   , 'H', HELP_MENU));
        return mb;
    }

//    @Deprecated
    private JPanel createDeviceControlPane() {
        JPanel p = new JPanel(new GridLayout(1, 1, 0, 5));
        p.add(createDevicesList());
        return p;
    }

//    @Deprecated
    private JPanel createDevicesList() {
        JComboBox devices = new JComboBox(deviceListModel);
        devices.setPreferredSize(new Dimension(200, 20));

        getApplication().addAndroidDeviceListener(new AndroidDeviceListener() {
            @Override
            public void connected(AndroidDevice dev) {
                log.debug("[devicesBox] connected: dev=" + dev);
                deviceListModel.addElement(dev.getName());
                deviceListModel.setSelectedItem(dev.getName());
            }

            @Override
            public void disconnected(AndroidDevice dev) {
                log.debug("[devicesBox] disconnected: dev=" + dev);
                deviceListModel.removeElement( dev.getName() );
            }
        });

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder(BorderFactory.createTitledBorder("Devices"));
        p.add(devices);
        p.add( Command.get("Show").createButton() );

        return p;
    }

}

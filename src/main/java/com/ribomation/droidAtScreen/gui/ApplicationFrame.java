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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 17:44:12
 */
public class ApplicationFrame extends JFrame {
    private Logger                  log = Logger.getLogger(ApplicationFrame.class);
    private Application             application;
    private DefaultComboBoxModel    deviceListModel = new DefaultComboBoxModel();

    private final String[] toolbar     = {"Orientation", "Scale", "-", "ScreenShot", "Video", "-", "Quit"};
    private final String[] fileMenu    = {"ScreenShot", "Video", "-", "Quit"};
    private final String[] viewMenu    = {"Orientation", "Scale", "UpsideDown"};
    private final String[] helpMenu    = {"About"};
    private final String[] optionsMenu = {
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

    public void  initGUI() {
        setTitle(getApplication().getName()+", Version "+getApplication().getVersion());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Command.find(QuitCommand.class).execute();
            }
        });
        
        setJMenuBar( createMenubar() );
        add(GuiUtil.createToolbar(toolbar), BorderLayout.NORTH);
        add( createDeviceControlPane() , BorderLayout.CENTER);
        pack();
    }

    protected JMenuBar createMenubar() {
        JMenuBar     mb = new JMenuBar();
        mb.add(GuiUtil.createMenu("File"   , 'F', fileMenu));
        mb.add(GuiUtil.createMenu("View"   , 'V', viewMenu));
        mb.add(GuiUtil.createMenu("Options", 'O', optionsMenu));
        mb.add(GuiUtil.createMenu("Help"   , 'H', helpMenu));
        return mb;
    }

    private JPanel createDeviceControlPane() {
        JPanel p = new JPanel(new GridLayout(1, 1, 0, 5));
        p.add(createDevicesList());
        return p;
    }

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

    public ComboBoxModel getDeviceList() { return deviceListModel; }

}

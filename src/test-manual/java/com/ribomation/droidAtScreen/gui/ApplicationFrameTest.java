package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.cmd.Command;
import com.ribomation.droidAtScreen.cmd.CommandTest;
import org.junit.Before;
import org.junit.Test;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 22:15:14
 */
public class ApplicationFrameTest {
    @Before
    public void setUp() throws Exception {
        Command.setApplication( CommandTest.createDummyApplication() );
    }

    @Test
    public void testInitGUI() throws Exception {
        ApplicationFrame  app = new ApplicationFrame();
        app.setApplication( CommandTest.createDummyApplication() );
        
        app.initGUI();
        app.pack();
//        app.setSize(350, 500);
        app.setLocation(400, 200);
        app.setVisible(true);
        Thread.sleep(20 * 1000);
    }
}

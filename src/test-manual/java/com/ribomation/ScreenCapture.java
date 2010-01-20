package com.ribomation;

import com.ribomation.droidAtScreen.dev.AndroidDevice;
import com.ribomation.droidAtScreen.dev.AndroidDeviceListener;
import com.ribomation.droidAtScreen.dev.AndroidDeviceManager;
import com.ribomation.droidAtScreen.img.ImageTransformer;
import com.ribomation.droidAtScreen.img.OrientationImageTransformer;
import com.ribomation.droidAtScreen.img.ScaleImageTransformer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-17 15:43:26
 */
public class ScreenCapture {
    private String                  adbCmdPath = "C:\\Lang\\java\\tools\\Android-r04\\tools\\adb.exe";
    private AndroidDeviceManager    mgr;

    public static void main(String[] args) throws InterruptedException {
        ScreenCapture   app = new ScreenCapture();

//        app.capture();
//        app.scale(2);
//        app.scale(0.5);
//        app.landscape(1);
//        app.landscapeAndHalfSize();
        app.landscapeAndDoubleSize();

        System.out.println("Sleeping for a while...");
        Thread.sleep(15 * 1000);
    }

    public ScreenCapture() {
        mgr = new AndroidDeviceManager();
        mgr.setAdbExecutable( new File(adbCmdPath) );
    }

    public void capture() throws InterruptedException {
        mgr.addAndroidDeviceListener( new DeviceListener() );
    }

    public void scale(final double scale) throws InterruptedException {
        mgr.addAndroidDeviceListener( new DeviceListener() {
            @Override
            protected AndroidDevice wrap(AndroidDevice dev) {
                return new TransformingAndroidDevice(new ScaleImageTransformer(scale), dev);
            }
        });
    }

    public void landscape() {
        mgr.addAndroidDeviceListener( new DeviceListener() {
            @Override
            protected AndroidDevice wrap(AndroidDevice dev) {
                return new TransformingAndroidDevice(new OrientationImageTransformer(false), dev);
            }
        });
    }

    public void  landscapeAndHalfSize() {
        mgr.addAndroidDeviceListener( new DeviceListener() {
            @Override
            protected AndroidDevice wrap(AndroidDevice dev) {
                return new TransformingAndroidDevice(new OrientationImageTransformer(false),
                        new TransformingAndroidDevice(new ScaleImageTransformer(0.5), dev)
                );
            }
        });
    }

    public void landscapeAndDoubleSize() {
        mgr.addAndroidDeviceListener(new DeviceListener() {
            @Override
            protected AndroidDevice wrap(AndroidDevice dev) {
                return new TransformingAndroidDevice(new OrientationImageTransformer(false),
                        new TransformingAndroidDevice(new ScaleImageTransformer(2.0), dev)
                );
            }
        });
    }

    class TransformingAndroidDevice implements AndroidDevice {
        private AndroidDevice   delegate;
        private ImageTransformer    transformer;

        TransformingAndroidDevice(ImageTransformer transformer, AndroidDevice delegate) {
            this.delegate = delegate;
            this.transformer = transformer;
        }

        @Override
        public BufferedImage getScreenShot() {
            return transformer.transform( delegate.getScreenShot() );
        }

        @Override
        public BufferedImage getScreenShot(boolean landscapeMode) {
            return getScreenShot();
        }

        @Override
        public String getName() {
            return delegate.getName();
        }

        @Override
        public boolean isEmulator() {
            return delegate.isEmulator();
        }

        @Override
        public ConnectionState getState() {
            return delegate.getState();
        }
    }


    class DeviceListener implements AndroidDeviceListener {
        protected AndroidDevice wrap(AndroidDevice dev) {
            return dev;
        }

        @Override
        public void connected(AndroidDevice dev) {
            System.out.println("ScreenCapture.connected: dev=" + dev);
            if (dev.isEmulator()) return;
            createFrame( wrap(dev) );
        }

        @Override
        public void disconnected(AndroidDevice dev) {
        }
    }

    private void createFrame(AndroidDevice dev) {
        DevicePane devPane = new DevicePane(dev);
        JFrame f    = new JFrame("Android Screen Capture");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(devPane);
        scrollPane.setSize(devPane.getPreferredSize());
        f.add(scrollPane, BorderLayout.CENTER);
        f.pack();

        f.setLocation(200, 200);
        f.setVisible(true);
    }

    class DevicePane extends JPanel {
        private AndroidDevice dev;

        DevicePane(AndroidDevice dev) {
            this.dev = dev;
            updateSize(dev.getScreenShot());
        }

        protected void paintComponent(Graphics g) {
            BufferedImage img = dev.getScreenShot();
            updateSize(img);

            int x = (this.getWidth()  - img.getWidth()) / 2;
            int y = (this.getHeight() - img.getHeight()) / 2;
            g.drawImage(img, x, y, this);
        }

        protected void  updateSize(BufferedImage img) {
            setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        }
    }
    
}

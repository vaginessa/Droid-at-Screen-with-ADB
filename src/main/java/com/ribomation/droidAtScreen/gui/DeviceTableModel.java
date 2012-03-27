package com.ribomation.droidAtScreen.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Table model for "known" devices.
 * <p/>
 * User: Jens
 * Created: 2012-03-26, 22:01
 */
public class DeviceTableModel extends AbstractTableModel {
    private static final int NAME = 0;
    private static final int TYPE = 1;
    private static final int SERNO = 2;
    private static final int STATE = 3;
    private static final int SHOW = 4;
    
    private List<DeviceFrame> devices = new ArrayList<DeviceFrame>();


    /**
     * Number of columns of the device table.
     * <pre>
     *   Name  Type  Ser.No  State  Show/Hide  
     * </pre>
     * @return
     */
    @Override
    public int getColumnCount() { return 5; }

    @Override
    public int getRowCount() { return devices.size(); }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case NAME:  return"Name";
            case TYPE:  return "Emulator";
            case SERNO: return "Serial No.";
            case STATE: return "State";
            case SHOW:  return "Visible";
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case NAME:  return String.class;
            case TYPE:  return String.class;
            case SERNO: return String.class;
            case STATE: return String.class;
            case SHOW:  return Boolean.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        DeviceFrame dev = devices.get(row);
        if (dev != null) switch (col) {
            case NAME:  return dev.getDevice().getName();
            case TYPE:  return dev.getDevice().isEmulator() ? "EMU" : "DEV";        
            case SERNO: return dev.getDevice().getDevice().getSerialNumber();
            case STATE: return dev.getDevice().getState();
            case SHOW:  return dev.isVisible();
        }
        return null;
    }

    public void add(DeviceFrame dev) {
        devices.add(dev);
        Collections.sort(devices);
        fireTableDataChanged();
    }

    public void remove(DeviceFrame dev) {
        devices.remove(dev); 
        fireTableDataChanged();
    }
    
    public void removeAll() {
        devices.clear(); 
        fireTableDataChanged();
    }
    
    public void refresh() {
        fireTableDataChanged();
    }
    
}

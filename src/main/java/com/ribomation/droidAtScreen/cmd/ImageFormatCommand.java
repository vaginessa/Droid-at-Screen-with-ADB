package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;

/**
 * Sets the default image format, when saving screen-shots.
 *
 * @user jens
 * @date 2011-10-01 12:13
 */
public class ImageFormatCommand extends Command {
    private static  final String[] formats = {"PNG", "JPG"};
    
    public ImageFormatCommand() {
        updateView(getCurrentFormat());
        setIcon("imgfmt");
        setTooltip("Set the default image-format when saving screen-shots.");
    }
    
    protected void updateView(String imgFmt) {
        setLabel(String.format("Image Format (%s)", imgFmt));
    }

    @Override
    protected void doExecute(Application app) {
        int rc = JOptionPane.showOptionDialog(app.getAppFrame(),
                "Image Formats", "Set default image format",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, formats, getPreferenceValue());

        if (0 <= rc && rc < formats.length) {
            setPreferenceValue(formats[rc]);
            updateView(formats[rc]);
        }
    }

    protected String getPreferencesKey() {
        return "image-format";
    }

    protected void setPreferenceValue(String value) {
        getApplication().getPreferences().put(getPreferencesKey(), value);
        getApplication().savePreferences();
    }

    protected String getPreferenceValue() {
        return getApplication().getPreferences().get(getPreferencesKey(), "png");
    }
    
    public String getCurrentFormat() {
        return getPreferenceValue();
    }
    
    public String[] getFormats() {
        return formats;
    }
}

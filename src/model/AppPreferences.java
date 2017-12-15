package model;

import view.DartCamPanel;

import java.awt.*;
import java.io.*;
import java.util.prefs.Preferences;

public class AppPreferences {

    private Preferences prefs;

    private static AppPreferences instance;

    public static AppPreferences getInstance() {
        if(instance == null) {
            instance = new AppPreferences();
        }

        return instance;
    }

    private AppPreferences() {
        prefs = Preferences.userRoot().node("DartManager");
    }

    public String getLeftPanelURL() {
        return prefs.get("leftPanelURL", "https://www.google.com");
    }

    public void setSecondCamURL(String secondCamURL) {
        prefs.put("secondCamURL", secondCamURL);
    }

    public String getSecondCamURL() {
        return prefs.get("secondCamURL", "https://www.google.com");
    }

    public void setLeftPanelURL(String leftPanelURL) {
        prefs.put("leftPanelURL", leftPanelURL);
    }

    public int getDividerLocationVertical() {
        return prefs.getInt("dividerLocationVert", 300);
    }

    public void setDividerLocationVertical(int dividerLocation) {
        prefs.putInt("dividerLocationVert", dividerLocation);
    }

    public int getDividerLocationHorizontal() {
        return prefs.getInt("dividerLocationHoriz", 300);
    }

    public void setDividerLocationHorizontal(int dividerLocation) {
        prefs.putInt("dividerLocationHoriz", dividerLocation);
    }

    public int getChoosenCamera() {
        int deviceNumber = prefs.getInt("choosenCamera", 0);
        if (Camera.getCameraNames().size() - 1 < deviceNumber) {
            deviceNumber = 0;
        }
        return deviceNumber;
    }

    public void setChoosenCamera(int deviceNumber) {
        prefs.putInt("choosenCamera", deviceNumber);
    }

    public Rectangle getSecondCamBounds() {
        try {
            return (Rectangle)AppPreferences.bytes2Object(prefs.getByteArray("secondCamWindowBounds", AppPreferences.object2Bytes(new Rectangle(0,0,200,200))));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSecondCamBounds(Rectangle rect) {
        try {
            prefs.putByteArray("secondCamWindowBounds", AppPreferences.object2Bytes(rect));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveZoomedArea(int zaIndex, Object o) {
        try {
            prefs.putByteArray("za" + zaIndex, AppPreferences.object2Bytes(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getZoomedArea(int zaIndex) {
        try {
            return AppPreferences.bytes2Object(prefs.getByteArray("za" + zaIndex, null));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    static private byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        return baos.toByteArray();
    }

    static private Object bytes2Object( byte raw[] )
            throws IOException, ClassNotFoundException {
        if (raw == null) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        return ois.readObject();
    }
}

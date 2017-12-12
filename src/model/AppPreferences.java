package model;

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
        return prefs.getInt("choosenCamera", 0);
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



//    public Dimension getWebcamDimension(String webcamName) {
//        try {
//            return (Dimension)AppPreferences.bytes2Object(prefs.getByteArray("camDimension" + webcamName, AppPreferences.object2Bytes(WebcamResolution.VGA.getSize())));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return WebcamResolution.VGA.getSize();
//    }

    public void setWebcamDimension(String webcamName, Dimension dim) {
        try {
            prefs.putByteArray("camDimension" + webcamName, AppPreferences.object2Bytes(dim));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isWebcamActivated(String webcamName) {
        return prefs.getBoolean("camActivated" + webcamName, true);
    }

    public void setWebcamActivated(String webcamName, boolean isActivated) {
        prefs.putBoolean("camActivated" + webcamName, isActivated);
    }


    static private byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        return baos.toByteArray();
    }

    static private Object bytes2Object( byte raw[] )
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        return ois.readObject();
    }
}

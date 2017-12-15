package view;

import model.AppPreferences;
import model.Camera;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class ZoomedAreaDialog extends FramelessDialog {
    private static int overallZAIndex = 0;

    private JTextField textField = new JTextField();
    private DartCamPanel panel;
    private int zaIndex;

    public ZoomedAreaDialog(DartCamPanel panel) {
        super();
        zaIndex = overallZAIndex;
        overallZAIndex++;
        this.panel = panel;
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Arial", Font.ITALIC, 20));
        textField.setForeground(Color.RED);
        textField.setBackground(Color.decode("#EEEEEE"));
        layeredPane.add(textField, BorderLayout.NORTH);
        this.add(panel);
        panel.setBorder(BorderFactory.createLineBorder(Color.decode("#EEEEEE"), 3, false));

        setListeners();

        setSize(200,200);
        setVisible(true);
    }

    private void setListeners() {
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                AppPreferences.getInstance().saveZoomedArea(zaIndex, null);
                panel.getCameraModel().deleteObserver(panel);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                saveSettings();
            }
        });
    }

    protected void onFixButtonClick() {
        super.onFixButtonClick();
        textField.setEditable(!textField.isEditable());
        saveSettings();
    }

    public void saveSettings() {
        Map<String, Object> o = new HashMap<>();
        o.put("zaX", getLocation().x);
        o.put("zaY", getLocation().y);
        o.put("zaWidth", getWidth());
        o.put("zaHeight", getHeight());
        o.put("zaText", textField.getText());
        o.put("camZoom", panel.getCameraPosition().zoomLevel);
        o.put("camPosX", panel.getCameraPosition().refPoint.x);
        o.put("camPosY", panel.getCameraPosition().refPoint.y);

        AppPreferences.getInstance().saveZoomedArea(zaIndex, o);
    }

    public static void restoreAllFromSettings(Camera cam) {
        int i = 0;
        Map<String,Object> o;
        while((o = (Map<String,Object>)(AppPreferences.getInstance().getZoomedArea(i))) != null) {
            DartCamPanel dcp = new DartCamPanel(cam);
            ZoomedAreaDialog za = new ZoomedAreaDialog(dcp);
            za.setLocation((int)o.get("zaX"), (int)o.get("zaY"));
            za.setSize((int)o.get("zaWidth"), (int)o.get("zaHeight"));
            za.textField.setText((String)o.get("zaText"));
            za.onFixButtonClick();
            dcp.getCameraPosition().zoomLevel = (double)o.get("camZoom");
            dcp.getCameraPosition().refPoint.x = (int)o.get("camPosX");
            dcp.getCameraPosition().refPoint.y = (int)o.get("camPosY");
            i++;
        };

    }
}

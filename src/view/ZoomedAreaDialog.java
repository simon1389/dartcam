package view;

import javax.swing.*;
import java.awt.*;

public class ZoomedAreaDialog extends FramelessDialog {
    public ZoomedAreaDialog() {
        super();
        layeredPane.add(new JTextField(), BorderLayout.NORTH);
    }
}

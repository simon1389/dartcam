package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FramelessDialog extends JDialog {

    private JButton fixButton = new JButton();
    private JLayeredPane layeredPane = new JLayeredPane();

    public FramelessDialog() {
        super();
        setFrameHideListener();
    }

//    @Override
    public Component add(Component comp) {
        return layeredPane.add(comp, 1);
    }

    private void setFrameHideListener() {
        layeredPane.setLayout(new BorderLayout());
        super.add(layeredPane);
        fixButton.setSize(80,30);
        fixButton.setOpaque(false);
        fixButton.setContentAreaFilled(false);
        fixButton.setBorderPainted(false);
        layeredPane.add(fixButton, 2);


        JDialog d = this;
        this.fixButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                d.setVisible(false);
                d.dispose();
                d.setUndecorated(!d.isUndecorated());
                d.setVisible(true);
                d.setAlwaysOnTop(true);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                fixButton.setOpaque(true);
                fixButton.setContentAreaFilled(true);
                fixButton.setBorderPainted(true);
                fixButton.setText("Fixieren");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                fixButton.setOpaque(false);
                fixButton.setContentAreaFilled(false);
                fixButton.setBorderPainted(false);
                fixButton.setText("");
            }
        });
    }

}

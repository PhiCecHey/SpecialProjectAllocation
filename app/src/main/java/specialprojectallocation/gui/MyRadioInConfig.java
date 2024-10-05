package specialprojectallocation.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyRadioInConfig extends JRadioButton {
    static final ArrayList<MyRadioInConfig> all = new ArrayList<>();

    MyRadioInConfig() {
        super();
        MyRadioInConfig.all.add(this);
    }

    static void anyCheckChanged(JButton button) {
        for (MyRadioInConfig radio : MyRadioInConfig.all) {
            radio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    warn();
                }

                public void warn() {
                    button.setBackground(Colors.yellowTransp);
                    Gui.saveConfigs.setBackground(Colors.blueTransp);
                }
            });
        }
    }
}

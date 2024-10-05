package specialprojectallocation.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyCheckboxInConfig extends JCheckBox {
    static final ArrayList<MyCheckboxInConfig> all = new ArrayList<>();

    MyCheckboxInConfig() {
        super();
        MyCheckboxInConfig.all.add(this);
    }

    static void anyCheckChanged(JButton button) {
        for (MyCheckboxInConfig check : MyCheckboxInConfig.all) {
            check.addActionListener(new ActionListener() {
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

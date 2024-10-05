package specialprojectallocation.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class MyTextFieldInConfig extends JTextField {
    static final ArrayList<MyTextFieldInConfig> all = new ArrayList<>();

    MyTextFieldInConfig(String s) {
        super(s);
        MyTextFieldInConfig.all.add(this);
    }

    MyTextFieldInConfig() {
        super();
        MyTextFieldInConfig.all.add(this);
    }

    static void anyFieldChanged(JButton button) {
        for (MyTextFieldInConfig field : MyTextFieldInConfig.all) {
            field.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    warn();
                }

                public void removeUpdate(DocumentEvent e) {
                    warn();
                }

                public void insertUpdate(DocumentEvent e) {
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

package specialprojectallocation.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class MyTextFieldInImport extends JTextField {
    static final ArrayList<MyTextFieldInImport> all = new ArrayList<>();

    MyTextFieldInImport() {
        super();
        MyTextFieldInImport.all.add(this);
    }

    static void anyFieldChanged(JButton button) {
        for (MyTextFieldInImport field : MyTextFieldInImport.all) {
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
                    JTextField test = new JTextField();
                    field.setBackground(test.getBackground());

                    button.setBackground(Colors.blueTransp);
                }
            });
        }
    }
}

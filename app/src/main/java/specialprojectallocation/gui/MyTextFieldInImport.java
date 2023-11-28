package specialprojectallocation.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class MyTextFieldInImport extends JTextField {
    static ArrayList<MyTextFieldInImport> all = new ArrayList<>();

    MyTextFieldInImport(String s) {
        super(s);
        MyTextFieldInImport.all.add(this);
    }

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
                    button.setBackground(Colors.yellowTransp);
                }
            });
        }
    }
}

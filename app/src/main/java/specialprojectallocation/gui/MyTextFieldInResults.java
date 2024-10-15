package specialprojectallocation.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class MyTextFieldInResults extends JTextField {
    static final ArrayList<MyTextFieldInResults> all = new ArrayList<>();

    MyTextFieldInResults() {
        super();
        MyTextFieldInResults.all.add(this);
    }

    static void anyFieldChanged() {
        for (MyTextFieldInResults field : MyTextFieldInResults.all) {
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

                }
            });
        }
    }
}

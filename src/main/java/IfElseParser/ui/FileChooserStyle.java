package IfElseParser.ui;

import javax.swing.*;
import java.awt.*;

public final class FileChooserStyle {

    private FileChooserStyle() {
    }

    public static JFileChooser createChooser(String title) {
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        chooser.setPreferredSize(new Dimension(900, 600));

        return chooser;
    }
}
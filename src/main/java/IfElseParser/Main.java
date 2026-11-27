package IfElseParser;

import IfElseParser.ui.EditorFrame;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                throw new IllegalStateException("Ошибка настройки GUI", e);
            }

            EditorFrame frame = new EditorFrame();
            frame.setVisible(true);
        });
    }
}
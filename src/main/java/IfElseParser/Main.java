package IfElseParser;

import IfElseParser.ui.EditorFrame;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Main {
    private static final int UI_FONT_SIZE = 16;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                throw new IllegalStateException("Ошибка настройки GUI", e);
            }

            UIManager.put("defaultFont", new FontUIResource("Noto Sans", Font.PLAIN, UI_FONT_SIZE));

            EditorFrame frame = new EditorFrame();
            frame.setVisible(true);
        });
    }
}

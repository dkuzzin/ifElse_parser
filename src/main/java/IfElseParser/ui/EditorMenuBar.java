
package IfElseParser.ui;

import javax.swing.*;
import java.awt.Font;

public class EditorMenuBar extends JMenuBar {
    private static final Font MENU_FONT =
            new Font("Noto Sans", Font.PLAIN, 16);

    public EditorMenuBar(Action[] fileActions, Action[] editActions,
                         Action[] textActions, Action runAction, Action[] helpActions) {
        add(createMenu("Файл", fileActions));
        add(createMenu("Правка", editActions));
        add(createMenu("Текст", textActions));
        add(createMenu("Пуск", runAction));
        add(createMenu("Справка", helpActions));
    }

    private JMenu createMenu(String title, Action... actions) {
        JMenu menu = new JMenu(title);
        menu.setFont(MENU_FONT);

        for (Action action : actions) {
            JMenuItem item = new JMenuItem(action);
            item.setFont(MENU_FONT);
            item.setBorder(
                    BorderFactory.createEmptyBorder(8, 20, 8, 20)
            );
            menu.add(item);
        }

        return menu;
    }
}

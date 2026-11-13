package IfElseParser.ui;

import javax.swing.*;
import java.awt.*;

public class EditorMenuBar extends JMenuBar {
    private static final int MENU_VERTICAL_PADDING = 8;
    private static final int MENU_HORIZONTAL_PADDING = 20;
    private static final String MENU_FONT_FAMILY = "Noto Sans";
    private static final int MENU_FONT_SIZE = 16;

    private static final Font MENU_FONT =  new Font(MENU_FONT_FAMILY, Font.PLAIN, MENU_FONT_SIZE);

    public EditorMenuBar(Action newDocumentAction, Action openAction, Action saveAction, Action saveAsAction,
                         Action exitAction, Action helpAction, Action aboutAction, Action[] textActions,
                         Action... editActions) {
        JMenu fileMenu = createMenu("Файл");
        fileMenu.add(styleMenuItem(new JMenuItem(newDocumentAction)));
        fileMenu.add(styleMenuItem(new JMenuItem(openAction)));
        fileMenu.add(styleMenuItem(new JMenuItem(saveAction)));
        fileMenu.add(styleMenuItem(new JMenuItem(saveAsAction)));
        fileMenu.add(styleMenuItem(new JMenuItem(exitAction)));
        add(fileMenu);

        JMenu editMenu = createMenu("Правка");
        for (Action action : editActions) {
            editMenu.add(styleMenuItem(new JMenuItem(action)));
        }
        add(editMenu);

        JMenu textMenu = createMenu("Текст");
        for (Action action : textActions) {
            textMenu.add(styleMenuItem(new JMenuItem(action)));
        }
        add(textMenu);

        add(new JMenu("Пуск"));

        JMenu helpMenu = createMenu("Справка");
        helpMenu.add(styleMenuItem(new JMenuItem(helpAction)));
        helpMenu.add(styleMenuItem(new JMenuItem(aboutAction)));
        add(helpMenu);
    }

    private JMenu createMenu(String title, String... items) {
        JMenu menu = new JMenu(title);
        menu.setFont(MENU_FONT);

        for (String item : items) {
            menu.add(styleMenuItem(new JMenuItem(item)));
        }

        return menu;
    }

    private JMenuItem styleMenuItem(JMenuItem item) {
        item.setFont(MENU_FONT);
        item.setBorder(BorderFactory.createEmptyBorder(
                MENU_VERTICAL_PADDING,
                MENU_HORIZONTAL_PADDING,
                MENU_VERTICAL_PADDING,
                MENU_HORIZONTAL_PADDING
        ));
        return item;
    }
}

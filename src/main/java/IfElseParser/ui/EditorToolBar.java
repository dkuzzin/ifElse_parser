package IfElseParser.ui;

import IfElseParser.ui.dialog.EditorDialogs;

import javax.swing.*;
import java.awt.*;

public final class EditorToolBar extends JToolBar {
    private static final int PADDING = 6;
    private static final int BUTTON_GAP = 4;
    private static final int GROUP_GAP = 12;
    private static final Dimension BUTTON_SIZE = new Dimension(42, 38);

    public EditorToolBar(Action[] fileActions, Action[] editActions, Action[] helpActions) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        addActions(fileActions);
        addSeparator(new Dimension(GROUP_GAP, 0));
        addActions(editActions);
        addSeparator(new Dimension(GROUP_GAP, 0));

        JButton runButton = createIconButton("Пуск", null);
        runButton.setEnabled(false);
        runButton.setToolTipText("Анализатор текста пока не реализован");
        add(runButton);
        addSeparator(new Dimension(GROUP_GAP, 0));
        addActions(helpActions);
    }

    private void addActions(Action[] actions) {
        for (Action action : actions) {
            String name = (String) action.getValue(Action.NAME);
            JButton button = createIconButton(name, action);
            add(button);
            add(Box.createHorizontalStrut(BUTTON_GAP));
        }
    }

    private JButton createIconButton(String name, Action action) {
        JButton button = EditorDialogs.createButton("");
        if (action != null) {
            button.setAction(action);
        }
        button.setText(null);
        button.setIcon(ToolBarIcon.forAction(name));
        button.setToolTipText(name);
        button.getAccessibleContext().setAccessibleName(name);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        return button;
    }

    public JScrollPane createScrollPane() {
        JScrollPane scroll = new JScrollPane(this,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                // Reserve room for the scrollbar when the window is narrow.
                if (getWidth() > 0 && getWidth() < EditorToolBar.this.getPreferredSize().width) {
                    size.height = EditorToolBar.this.getPreferredSize().height
                            + getHorizontalScrollBar().getPreferredSize().height
                            + getInsets().top + getInsets().bottom;
                }
                return size;
            }
        };
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }
}

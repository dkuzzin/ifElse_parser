package IfElseParser.ui.dialog;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public final class EditorDialogs {
    public enum Choice { SAVE, DISCARD, CANCEL }

    private static final int BUTTON_VERTICAL_PADDING = 6;
    private static final int BUTTON_HORIZONTAL_PADDING = 14;
    private static final int BUTTON_ARC = 8;
    private static final int BUTTON_EDGE = 2;
    private static final Font TEXT_FONT = new Font("Noto Sans", Font.PLAIN, 16);
    private static final Color TEXT = new Color(0x20242B);
    private static final Color BUTTON_BACKGROUND = new Color(0xF5F6F8);
    private static final Color BUTTON_HOVER = new Color(0xE8EBEF);
    private static final Color BUTTON_BORDER = new Color(0xB9C0CA);
    private static final Color FOCUS = new Color(0x5276B8);

    private EditorDialogs() {
    }

    public static Choice showUnsavedChanges(JFrame owner) {
        JLabel message = new JLabel("Сохранить изменения в текущем документе?");
        message.setFont(TEXT_FONT);
        JButton saveButton = createButton("Сохранить");
        JButton discardButton = createButton("Не сохранять");
        JButton cancelButton = createButton("Отмена");
        JOptionPane pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION, null,
                new Object[]{saveButton, discardButton, cancelButton}, cancelButton);
        saveButton.addActionListener(event -> pane.setValue(Choice.SAVE));
        discardButton.addActionListener(event -> pane.setValue(Choice.DISCARD));
        cancelButton.addActionListener(event -> pane.setValue(Choice.CANCEL));

        JDialog dialog = pane.createDialog(owner, "Несохранённые изменения");
        dialog.getRootPane().setDefaultButton(cancelButton);
        SwingUtilities.invokeLater(() -> cancelButton.requestFocusInWindow());
        try {
            dialog.setVisible(true);
            return pane.getValue() instanceof Choice choice ? choice : Choice.CANCEL;
        } finally {
            dialog.dispose();
        }
    }

    public static void showError(JFrame owner, String title, String message) {
        showMessage(owner, title, message, JOptionPane.ERROR_MESSAGE);
    }

    public static void showAbout(JFrame owner) {
        showMessage(owner, "О программе", """
                IfElseParser

                Учебный текстовый редактор на Java Swing.
                Создание, открытие и сохранение файлов в UTF-8,
                редактирование текста, отмена и повтор изменений.

                Разрабатывается как интерфейс языкового процессора.
                Анализатор текста пока не реализован.
                """, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showMessage(JFrame owner, String title, String message, int messageType) {
        JButton closeButton = createButton("Закрыть");
        JOptionPane pane = new JOptionPane(message, messageType,
                JOptionPane.DEFAULT_OPTION, null, new Object[]{closeButton}, closeButton);
        closeButton.addActionListener(event -> pane.setValue(JOptionPane.OK_OPTION));
        JDialog dialog = pane.createDialog(owner, title);
        dialog.getRootPane().setDefaultButton(closeButton);
        try {
            dialog.setVisible(true);
        } finally {
            dialog.dispose();
        }
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new RoundedButtonUI());
        button.setFont(TEXT_FONT);
        button.setForeground(TEXT);
        button.setBackground(BUTTON_BACKGROUND);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setBorder(BorderFactory.createEmptyBorder(BUTTON_VERTICAL_PADDING, BUTTON_HORIZONTAL_PADDING,
                BUTTON_VERTICAL_PADDING, BUTTON_HORIZONTAL_PADDING));
        return button;
    }

    private static final class RoundedButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics graphics, JComponent component) {
            JButton button = (JButton) component;
            Graphics2D canvas = (Graphics2D) graphics.create();
            try {
                canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int width = button.getWidth() - 2 * BUTTON_EDGE - 1;
                int height = button.getHeight() - 2 * BUTTON_EDGE - 1;
                boolean active = button.getModel().isRollover() || button.getModel().isPressed();
                canvas.setColor(active ? BUTTON_HOVER : button.getBackground());
                canvas.fillRoundRect(BUTTON_EDGE, BUTTON_EDGE,
                        width, height, BUTTON_ARC, BUTTON_ARC);
                canvas.setColor(button.hasFocus() ? FOCUS : BUTTON_BORDER);
                canvas.setStroke(new BasicStroke(button.hasFocus() ? 2f : 1f));
                canvas.drawRoundRect(BUTTON_EDGE, BUTTON_EDGE,
                        width, height, BUTTON_ARC, BUTTON_ARC);
            } finally {
                canvas.dispose();
            }
            super.paint(graphics, component);
        }
    }
}

package IfElseParser.ui.dialog;

import javax.swing.*;
import java.awt.*;

public final class EditorDialogs {

    public enum Choice { SAVE, DISCARD, CANCEL }

    private EditorDialogs() {
    }

    public static Choice showUnsavedChanges(JFrame owner) {
        String[] options = {"Сохранить", "Не сохранять", "Отмена"};

        int answer = JOptionPane.showOptionDialog(
                owner,
                "Сохранить изменения в текущем документе?",
                "Несохранённые изменения",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]
        );

        return switch (answer) {
            case 0 -> Choice.SAVE;
            case 1 -> Choice.DISCARD;
            default -> Choice.CANCEL;
        };
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

    private static void showMessage(JFrame owner, String title,
                                    String message, int messageType) {
        JOptionPane.showOptionDialog(
                owner,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                messageType,
                null,
                new String[]{"Закрыть"},
                "Закрыть"
        );
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Noto Sans", Font.PLAIN, 16));
        return button;
    }
}

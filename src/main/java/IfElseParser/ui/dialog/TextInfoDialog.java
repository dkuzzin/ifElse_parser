package IfElseParser.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public final class TextInfoDialog {
    private static final Dimension CONTENT_SIZE = new Dimension(720, 430);

    private TextInfoDialog() {
    }

    public static void show(JFrame owner, String title) {
        JDialog dialog = new JDialog(owner, title, true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JTextArea text = new JTextArea(contentFor(title));
        text.setFont(new Font("Noto Sans", Font.PLAIN, 16));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setCaretPosition(0);
        text.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JButton closeButton = EditorDialogs.createButton("Закрыть");
        closeButton.addActionListener(event -> dialog.dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        buttons.add(closeButton);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        content.add(new JScrollPane(text), BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        dialog.setContentPane(content);
        dialog.getRootPane().setDefaultButton(closeButton);
        dialog.getRootPane().registerKeyboardAction(event -> dialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.setMinimumSize(new Dimension(480, 300));
        dialog.setPreferredSize(CONTENT_SIZE);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private static String contentFor(String title) {
        return switch (title) {
            case "Постановка задачи" -> "Цель работы — создать графический текстовый редактор, "
                    + "который в следующих лабораторных работах будет дополнен языковым процессором.\n\n"
                    + "Редактор принимает исходный текст, позволяет сохранить его в UTF-8 и предоставляет "
                    + "область для вывода результатов анализа.";
            case "Грамматика" -> "В этом разделе будет описана грамматика языка, обрабатываемого "
                    + "языковым процессором. На текущем этапе редактор подготовлен к вводу и сохранению "
                    + "текста, а синтаксический анализатор ещё не подключён.";
            case "Классификация грамматики" -> "Здесь будет приведена классификация грамматики: её тип "
                    + "по иерархии Хомского, свойства порождающих правил и ограничения на исходные тексты.";
            case "Метод анализа" -> "Здесь будет описан алгоритм синтаксического анализа и порядок его "
                    + "применения к тексту из верхней области редактора.";
            case "Тестовый пример" -> "Здесь будет размещён пример исходного текста и ожидаемый результат "
                    + "его анализа. Команда «Пуск» будет использовать такие примеры после реализации анализатора.";
            case "Список литературы" -> "Здесь будет приведён список учебной и справочной литературы, "
                    + "использованной при разработке грамматики и языкового процессора.";
            case "Исходный код программы" -> "Исходный код IfElseParser находится в каталоге "
                    + "src/main/java/IfElseParser проекта. Запуск приложения из IDE выполняется через класс "
                    + "IfElseParser.Main.";
            default -> "Информация для этого пункта пока не подготовлена.";
        };
    }
}

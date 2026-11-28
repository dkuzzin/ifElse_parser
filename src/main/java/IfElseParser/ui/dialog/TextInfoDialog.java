
package IfElseParser.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TextInfoDialog {
    private static final Dimension CONTENT_SIZE = new Dimension(820, 520);

    private static final String[] SOURCE_FILES = {
            "IfElseParser/Main.java",
            "IfElseParser/parser/Parser.java",
            "IfElseParser/parser/ParseError.java",
            "IfElseParser/ui/EditorFrame.java",
            "IfElseParser/ui/EditorMenuBar.java",
            "IfElseParser/ui/EditorToolBar.java",
            "IfElseParser/ui/SyntaxHighlighter.java",
            "IfElseParser/ui/ToolBarIcon.java",
            "IfElseParser/ui/FileChooserStyle.java",
            "IfElseParser/ui/dialog/EditorDialogs.java",
            "IfElseParser/ui/dialog/HelpDialog.java",
            "IfElseParser/ui/dialog/TextInfoDialog.java"
    };

    private TextInfoDialog() {
    }

    public static void show(JFrame owner, String title) {
        JDialog dialog = new JDialog(owner, title, true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        boolean codeLike = title.equals("Грамматика")
                || title.equals("Тестовый пример")
                || title.equals("Исходный код программы");

        JTextArea text = new JTextArea(contentFor(title));
        text.setFont(new Font(
                codeLike ? "Monospaced" : "Noto Sans",
                Font.PLAIN,
                codeLike ? 14 : 16
        ));
        text.setEditable(false);
        text.setLineWrap(!codeLike);
        text.setWrapStyleWord(!codeLike);
        text.setCaretPosition(0);
        text.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JScrollPane scroll = new JScrollPane(text);

        JButton closeButton = EditorDialogs.createButton("Закрыть");
        closeButton.addActionListener(event -> dialog.dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        buttons.add(closeButton);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        content.add(scroll, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.getRootPane().setDefaultButton(closeButton);

        dialog.getRootPane().registerKeyboardAction(
                event -> dialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        dialog.setMinimumSize(new Dimension(480, 300));
        dialog.setPreferredSize(CONTENT_SIZE);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private static String contentFor(String title) {
        return switch (title) {

            case "Постановка задачи" -> """
                    ПОСТАНОВКА ЗАДАЧИ

                    Цель работы — разработать языковой процессор для
                    синтаксического анализа условного оператора if-else
                    с графическим интерфейсом на языке Java.

                    Входными данными является текст одной конструкции:

                    if (условие) {
                        переменная = значение;
                    } else {
                        переменная = значение;
                    }

                    Условие может содержать сравнения идентификаторов
                    и целых чисел, объединённые логическими операторами
                    && и ||.

                    В каждой ветви должно находиться ровно одно
                    присваивание. Наличие ветви else обязательно.

                    ОСНОВНЫЕ ТРЕБОВАНИЯ

                    1. Разработать контекстно-свободную грамматику
                       заданного языка.

                    2. Реализовать синтаксический анализатор методом
                       рекурсивного спуска.

                    3. Проверять соответствие исходного текста
                       заданной грамматике.

                    4. Обнаруживать синтаксические ошибки:
                       - отсутствие ключевых слов if и else;
                       - пропуск обязательных скобок;
                       - отсутствие операторов сравнения;
                       - отсутствие знака присваивания;
                       - пропуск значений;
                       - отсутствие точки с запятой;
                       - недопустимые символы и конструкции.

                    5. Выводить сообщения об ошибках с указанием
                       номера строки и столбца.

                    6. Обеспечить возможность восстановления разбора
                       после некоторых синтаксических ошибок.

                    7. Реализовать графический интерфейс с редактором
                       исходного текста и областью результатов.

                    8. Обеспечить открытие и сохранение текстовых
                       файлов в кодировке UTF-8.

                    9. Реализовать подсветку синтаксиса и выделение
                       обнаруженных ошибок в исходном тексте.

                    10. Провести автоматизированное тестирование
                        синтаксического анализатора.

                    Программа проверяет синтаксис, но не выполняет
                    введённые инструкции.
                    """;

            case "Грамматика" -> """
                    КОНТЕКСТНО-СВОБОДНАЯ ГРАММАТИКА

                    Начальный нетерминал: <IfElse>

                    <IfElse> -> if ( <Condition> ) { <Assignment> } else { <Assignment> }

                    <Condition> -> <AndExpr> <OrTail>

                    <OrTail> -> || <AndExpr> <OrTail> | ε

                    <AndExpr> -> <Comparison> <AndTail>

                    <AndTail> -> && <Comparison> <AndTail> | ε

                    <Comparison> -> <Value> <CompareOp> <Value>

                    <CompareOp> -> > | < | >= | <= | == | !=

                    <Value> -> <Identifier> | <Number>

                    <Assignment> -> <Identifier> = <Value> ;

                    <Identifier> -> <Letter> <IdTail>

                    <IdTail> -> <Letter> <IdTail> | <Digit> <IdTail> | ε

                    <Number> -> <Sign> <Digit> <NumTail>

                    <Sign> -> - | ε

                    <NumTail> -> <Digit> <NumTail> | ε

                    <Letter> -> a | ... | z | A | ... | Z

                    <Digit> -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9


                    ОБОЗНАЧЕНИЯ

                    ->    правило вывода
                    |     выбор альтернативы
                    ε     пустая цепочка
                    <...> нетерминальный символ

                    <Letter> включает все латинские буквы
                    верхнего и нижнего регистра.

                    <Digit> включает десятичные цифры от 0 до 9.


                    ОСОБЕННОСТИ ГРАММАТИКИ

                    1. Начальный символ — <IfElse>.

                    2. Обе ветви условного оператора обязательны.

                    3. Каждая ветвь содержит одно присваивание.

                    4. Логические выражения могут включать
                       произвольное количество сравнений.

                    5. Оператор && имеет более высокий приоритет,
                       чем оператор ||.

                    6. Числа могут быть положительными,
                       нулевыми или отрицательными.

                    7. Идентификатор начинается с латинской буквы,
                       за которой могут следовать буквы и цифры.

                    8. Пробельные символы допускаются между
                       элементами конструкции, но не внутри
                       идентификаторов и чисел.
                    """;

            case "Классификация грамматики" -> """
                    КЛАССИФИКАЦИЯ ГРАММАТИКИ

                    Разработанная грамматика относится к типу 2
                    по иерархии Хомского.

                    Тип 2 — контекстно-свободные грамматики.

                    В контекстно-свободной грамматике каждая
                    продукция имеет вид:

                    A -> α

                    где A — один нетерминальный символ,
                    α — цепочка терминалов и нетерминалов,
                    которая также может быть пустой.

                    Во всех правилах разработанной грамматики
                    слева находится ровно один нетерминал.

                    Примеры:

                    <Value> -> <Identifier> | <Number>

                    <Assignment> -> <Identifier> = <Value> ;

                    <AndTail> -> && <Comparison> <AndTail> | ε


                    РЕКУРСИВНЫЕ ПРАВИЛА

                    Грамматика содержит правую рекурсию:

                    <OrTail> -> || <AndExpr> <OrTail> | ε

                    <AndTail> -> && <Comparison> <AndTail> | ε

                    Благодаря этому допускаются условия
                    с несколькими логическими операциями.


                    ПРИОРИТЕТ ОПЕРАЦИЙ

                    Структура грамматики определяет приоритет
                    логических операций:

                    1. Сначала формируются сравнения.
                    2. Сравнения объединяются через &&.
                    3. Полученные выражения объединяются через ||.

                    Таким образом, && имеет больший приоритет,
                    чем ||.


                    СПОСОБ АНАЛИЗА

                    Для распознавания предложений языка
                    используется рекурсивный спуск.

                    Каждому основному нетерминалу соответствует
                    метод синтаксического анализатора.
                    """;

            case "Метод анализа" -> """
                    МЕТОД СИНТАКСИЧЕСКОГО АНАЛИЗА

                    Для разбора исходного текста используется
                    метод рекурсивного спуска.

                    Анализатор реализован в классе Parser.


                    1. НАЧАЛО АНАЛИЗА

                    Метод analyze() сбрасывает текущее состояние
                    парсера и запускает разбор с метода ifElse().

                    Начальная позиция:
                    строка = 1, столбец = 1.


                    2. РАЗБОР УСЛОВНОГО ОПЕРАТОРА

                    Метод ifElse() проверяет последовательность:

                    if
                    (
                    условие
                    )
                    {
                    присваивание
                    }
                    else
                    {
                    присваивание
                    }


                    3. РАЗБОР УСЛОВИЯ

                    Метод condition() обрабатывает логическое ИЛИ.

                    Метод andExpr() обрабатывает логическое И.

                    Метод comparison() проверяет два значения
                    и оператор сравнения между ними.

                    Метод value() распознаёт идентификатор
                    либо целое число.


                    4. РАЗБОР ПРИСВАИВАНИЯ

                    Метод assignment() проверяет:

                    - идентификатор;
                    - знак =;
                    - значение;
                    - точку с запятой.


                    5. ПРОВЕРКА СИМВОЛОВ

                    Метод match() проверяет наличие ожидаемой
                    последовательности символов.

                    Метод expect() требует обязательный элемент
                    и сообщает об ошибке, если он отсутствует.

                    Метод advance() перемещает текущую позицию
                    и обновляет номер строки и столбца.


                    6. ОБНАРУЖЕНИЕ ОШИБОК

                    При обнаружении ошибки формируется объект
                    ParseError, содержащий:

                    - сообщение об ошибке;
                    - номер строки;
                    - номер столбца;
                    - цепочку q, если она определена.


                    7. НЕЙТРАЛИЗАЦИЯ ОШИБОК

                    Метод neutralize() регистрирует ошибку
                    и выполняет предусмотренное восстановление.

                    Используются два основных подхода:

                    А. Условная вставка недостающих символов.

                    Например, если после значения отсутствует
                    точка с запятой, анализатор может записать
                    q = ";" и продолжить разбор.

                    Б. Пропуск ошибочного участка.

                    Метод recoverTo() перемещает позицию
                    к подходящей границе продолжения.

                    В зависимости от места ошибки такими
                    границами могут быть:

                    &&, ||, ), {, }, else.

                    Для разных участков грамматики используются
                    соответствующие контексты восстановления.

                    Это упрощённая грамматически ориентированная
                    нейтрализация, а не полный алгоритм Айронса.


                    8. РЕЗУЛЬТАТ АНАЛИЗА

                    Если список ошибок пуст, конструкция
                    считается синтаксически корректной.

                    Если ошибки обнаружены, они возвращаются
                    в виде списка объектов ParseError.

                    Исходный текст при восстановлении
                    автоматически не изменяется.
                    """;

            case "Тестовый пример" -> """
                    ТЕСТИРОВАНИЕ СИНТАКСИЧЕСКОГО АНАЛИЗАТОРА


                    ПРИМЕР 1. КОРРЕКТНАЯ КОНСТРУКЦИЯ

                    if (x >= 10 && y != 0 || z < 5) {
                        result = x;
                    } else {
                        result = -25;
                    }

                    Ожидаемый результат:
                    синтаксические ошибки отсутствуют.


                    ПРИМЕР 2. ПРОПУЩЕННАЯ ТОЧКА С ЗАПЯТОЙ

                    if (a > b) {
                        x = 10
                    } else {
                        y = 20;
                    }

                    Ожидаемый результат:
                    обнаружена 1 ошибка.

                    Недостающий символ: ;
                    Цепочка восстановления: q = ;


                    ПРИМЕР 3. НЕСКОЛЬКО ОШИБОК

                    if (a > b) {
                        x = 10
                    } else {
                        y = ;
                    }

                    Ожидаемый результат:
                    обнаружены 2 ошибки.

                    1. Отсутствует точка с запятой после 10.
                    2. Отсутствует значение после y =.


                    ПРИМЕР 4. ПРОПУЩЕННЫЕ СИМВОЛЫ В КОНЦЕ

                    if (a > b) { x = 1; } else { y = 2

                    Ожидаемый результат:
                    обнаружены 2 ошибки.

                    1. Отсутствует ;
                    2. Отсутствует }

                    Общая цепочка восстановления: q = ;}


                    АВТОМАТИЗИРОВАННОЕ ТЕСТИРОВАНИЕ

                    Для проверки анализатора используется JUnit 5.

                    Тесты проверяют:
                    - корректные конструкции;
                    - некорректные конструкции;
                    - количество обнаруженных ошибок;
                    - координаты ошибок;
                    - восстановительные цепочки q;
                    - повторный запуск анализатора;
                    - обработку длинных идентификаторов.

                    Запуск тестов из корня Maven-проекта:

                    mvn test

                    При несоответствии фактического результата
                    ожидаемому тест завершается ошибкой.
                    """;

            case "Список литературы" -> """
                    СПИСОК ЛИТЕРАТУРЫ И МАТЕРИАЛОВ

                    1. Шорников Ю. В.
                       Теория и практика языковых процессоров.
                       Учебное пособие. 2022.

                    2. Методические материалы по разработке
                       графического интерфейса текстового редактора
                       и языкового процессора (GUI).

                    3. Индивидуальная постановка задачи:
                       разработка синтаксического анализатора
                       условного оператора if-else.

                    4. Документация Java Platform, Standard Edition.
                       Java API: javax.swing, java.util,
                       java.nio.file.

                       https://docs.oracle.com/en/java/

                    5. JUnit 5 User Guide.
                       Документация по модульному тестированию.

                       https://junit.org/junit5/docs/current/user-guide/
                    """;

            case "Исходный код программы" -> loadSources();

            default -> "Информация для этого пункта не найдена.";
        };
    }

    private static String loadSources() {
        StringBuilder result = new StringBuilder(
                "ИСХОДНЫЙ КОД ПРОГРАММЫ IFELSEPARSER\n\n"
        );

        int loaded = 0;

        for (String name : SOURCE_FILES) {
            String source = null;

            // Попытка прочитать исходник из ресурсов JAR.
            try (InputStream stream = TextInfoDialog.class
                    .getResourceAsStream("/sources/" + name)) {

                if (stream != null) {
                    source = new String(
                            stream.readAllBytes(),
                            StandardCharsets.UTF_8
                    );
                }
            } catch (IOException ignored) {
                // Попробуем прочитать файл из каталога проекта.
            }

            // При запуске из IntelliJ IDEA.
            if (source == null) {
                try {
                    source = Files.readString(
                            Path.of("src/main/java", name),
                            StandardCharsets.UTF_8
                    );
                } catch (IOException ignored) {
                    // Исходный файл не найден.
                }
            }

            if (source != null) {
                result.append("========== ")
                        .append(name)
                        .append(" ==========\n\n")
                        .append(source)
                        .append("\n\n");

                loaded++;
            }
        }

        if (loaded == 0) {
            return """
                    Исходные файлы не найдены.

                    Для просмотра кода запустите приложение
                    из корневого каталога Maven-проекта.

                    При автономном запуске JAR исходные файлы
                    должны быть дополнительно включены в ресурсы.
                    """;
        }

        return result.toString();
    }
}

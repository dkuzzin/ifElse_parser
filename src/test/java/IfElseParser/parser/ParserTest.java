package IfElseParser.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Синтаксический анализатор if-else")
class ParserTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validPrograms")
    @DisplayName("Корректные конструкции")
    void acceptsValidPrograms(String description, String source) {
        List<ParseError> errors = new Parser(source).analyze();
        assertTrue(errors.isEmpty(), () -> "Корректный код отклонён:\n"
                + source + "\nОшибки: " + errors);
    }

    private static Stream<Arguments> validPrograms() {
        return Stream.of(
                Arguments.of("Обычный if-else", "if (a > b) { x = 10; } else { x = -5; }"),
                Arguments.of("Все логические операции", "if (x >= 10 && y != 0 || z < 5) { result = x; } else { result = 0; }"),
                Arguments.of("Равенство, неравенство, отрицательные числа", "if (a == b || c <= -3) { x = a; } else { y = b; }"),
                Arguments.of("Идентификатор с цифрами", "if (x123 > 42) { result2 = x123; } else { result2 = 0; }"),
                Arguments.of("else как имя переменной", "if (a > b) { x = else; } else { y = 20; }"),
                Arguments.of("Скобки без пробелов", "if(a>b){x=1;}else{y=2;}"),
                Arguments.of("Пробелы, табуляция и переносы", "\n  if\t( a >= -10 )\n {\n  x = 0;\n }\n else {\n  y = -25;\n } \n"),
                Arguments.of("Идентификатор длиной 10000", "if (" + "a".repeat(10000)
                        + " > 0) { x = 1; } else { y = 2; }")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidPrograms")
    @DisplayName("Ошибочные конструкции и восстановление")
    void rejectsInvalidPrograms(String description, String source, int expectedCount) {
        List<ParseError> errors = new Parser(source).analyze();
        assertEquals(expectedCount, errors.size(),
                () -> "Неверное количество ошибок для:\n" + source + "\nНайдено: " + errors);
    }

    private static Stream<Arguments> invalidPrograms() {
        return Stream.of(
                Arguments.of("Пустой ввод", "", 1),
                Arguments.of("Нет правого операнда", "if (a >) { x = 1; } else { y = 2; }", 1),
                Arguments.of("Нет точки с запятой", "if (a > b) { x = 1 } else { y = 2; }", 1),
                Arguments.of("Нет else", "if (a > b) { x = 1; }", 1),
                Arguments.of("Идентификатор начинается с цифры", "if (a > b) { 1x = 10; } else { x = 2; }", 1),
                Arguments.of("Неверное ключевое слово ifx", "ifx (a > b) { x = 1; } else { y = 2; }", 1),
                Arguments.of("elsewhere вместо else", "if (a > b) { x = 1; } elsewhere { y = 2; }", 1),
                Arguments.of("Пробел внутри if", "i f (a > b) { x = 1; } else { y = 2; }", 1),
                Arguments.of("Число без цифр в условии", "if (a > -) { x = 1; } else { y = 2; }", 1),
                Arguments.of("Плюс перед числом", "if (a > +1) { x = 1; } else { y = 2; }", 1),
                Arguments.of("Дробное число", "if (a > b) { x = 1.5; } else { y = 2; }", 1),
                Arguments.of("Недопустимый оператор >>", "if (a >> b) { x = 1; } else { y = 2; }", 1),
                Arguments.of("Отрицательное число без цифр", "if (a > b) { x = 1; } else { y = -; }", 1),
                Arguments.of("Лишний текст после if-else", "if (a > b) { x = 1; } else { y = 2; } abc", 1),
                Arguments.of("Два присваивания в первой ветви", "if (a > b) { x = 1; y = 2; } else { z = 3; }", 1),
                Arguments.of("Нет закрывающей )", "if (a > b { x = 1; } else { y = 2; }", 1),
                Arguments.of("Нет } перед else", "if (a > b) { x = 1; else { y = 2; }", 1),
                Arguments.of("Нет последней }", "if (a > b) { x = 1; } else { y = 2;", 1),
                Arguments.of("Нет ; и } в конце", "if (a > b) { x = 1; } else { y = 2", 2),
                Arguments.of("Пропущен =", "if (a > b) { x 10; } else { y = 20; }", 1),
                Arguments.of("Пропущено значение", "if (a > b) { x = ; } else { y = 20; }", 1),
                Arguments.of("Пустая первая ветвь", "if (a > b) { } else { y = 20; }", 1),
                Arguments.of("Пустая первая ветвь и нет }", "if (a > b) { else { y = 20; }", 2),
                Arguments.of("Ошибки в обеих ветвях", "if (a > b) { x = ; } else { y = ; }", 2),
                Arguments.of("Нет ; в обеих ветвях", "if (a > b) { x = 10 } else { y = 20 }", 2),
                Arguments.of("Нет оператора сравнения дважды", "if (a b && c d) { x = 1; } else { y = 2; }", 2),
                Arguments.of("Ошибка в двух сравнениях через &&", "if (a > + && b < -) { x = 1; } else { y = 2; }", 2),
                Arguments.of("Ошибка в первом сравнении через ||", "if (a > + || b < 5) { x = 1; } else { y = 2; }", 1),
                Arguments.of("Ошибка в условии и нет )", "if (a > + { x = 1; } else { y = 2; }", 2),
                Arguments.of("Ложная точка синхронизации else", "if (a > b) { x = + else; } else { y = 20; }", 1),
                Arguments.of("Ошибки в условии и обеих ветвях", "if (a > -) { x = ; } else { y = -; }", 3),
                Arguments.of("Ошибка в условии, пропущены три разделителя", "if (a > && b < c { x = ; else { y = 20 }", 5),
                Arguments.of("Нет ) и трёх разделителей", "if (a > b { x = 10 else { y = 20 }", 4),
                Arguments.of("Нет = и значения в разных ветвях", "if (a > b) { x 10; } else { y = ; }", 2),
                Arguments.of("Нет значения, ; и } перед else", "if (a > b) { x = else { y = 20; }", 3),
                Arguments.of("Лишний символ перед ;", "if (a > b) { x = +; } else { y = 20; }", 1)
        );
    }

    @Test
    @DisplayName("Восстановление с q: пропущены = и значение")
    void storesMissingAssignmentTokens() {
        List<ParseError> errors = new Parser(
                "if (a > b) { x 10; } else { y = ; }"
        ).analyze();

        assertEquals(2, errors.size());
        assertEquals("Expected: =", errors.get(0).message());
        assertEquals("=", errors.get(0).q());
        assertEquals("Expected: value", errors.get(1).message());
        assertEquals("0", errors.get(1).q());
    }

    @Test
    @DisplayName("Восстановление с q: пропущено значение сравнения")
    void storesMissingComparisonValue() {
        List<ParseError> errors = new Parser(
                "if (a > ) { x = 1; } else { y = 2; }"
        ).analyze();

        assertEquals(1, errors.size());
        assertEquals("Expected: value", errors.getFirst().message());
        assertEquals("0", errors.getFirst().q());
    }

    @Test
    @DisplayName("Восстановление с q: пропущена последовательность ;} перед else")
    void recoversTwoMissingTokensBeforeElse() {
        List<ParseError> errors = new Parser(
                "if (a > b) { x = 10 else { y = 20; }"
        ).analyze();

        assertEquals(2, errors.size());
        assertEquals("Expected: ;", errors.get(0).message());
        assertEquals("Expected: }", errors.get(1).message());
        assertEquals(";}", errors.get(0).q() + errors.get(1).q());
    }

    @Test
    @DisplayName("Восстановление с q: пропущена последовательность ;} в конце файла")
    void recoversTwoMissingTokensAtEof() {
        List<ParseError> errors = new Parser(
                "if (a > b) { x = 1; } else { y = 2"
        ).analyze();

        assertEquals(2, errors.size());
        assertEquals("Expected: ;", errors.get(0).message());
        assertEquals("Expected: }", errors.get(1).message());
        assertEquals(";}", errors.get(0).q() + errors.get(1).q());
    }

    @Test
    @DisplayName("Строка и столбец ошибки отсчитываются с единицы")
    void reportsLineAndColumn() {
        String source = "if (a > b) {\n    x = 10\n} else {\n    y = 20;\n}";
        List<ParseError> errors = new Parser(source).analyze();

        assertEquals(1, errors.size());
        ParseError error = errors.getFirst();
        assertEquals("Expected: ;", error.message());
        assertEquals(2, error.line());
        assertEquals(11, error.column());
        assertEquals(";", error.q());
    }

    @Test
    @DisplayName("Повторный запуск анализатора не сохраняет старые ошибки")
    void clearsErrorsOnRepeatedAnalysis() {
        Parser parser = new Parser("if (a > b) { x = ; } else { y = 2; }");
        List<ParseError> first = parser.analyze();
        List<ParseError> second = parser.analyze();

        assertEquals(1, first.size());
        assertEquals(first, second);
    }
}

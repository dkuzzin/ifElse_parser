
package IfElseParser.parser;

import java.util.List;

public class ParserTest {
    public static void main(String[] args) {
        test("if (a > b) { x = 10; } else { x = -5; }", true);

        test("if (x >= 10 && y != 0 || z < 5) { result = x; } else { result = 0; }", true);

        test("if (a >) { x = 1; } else { x = 2; }", false);

        test("if (a > b) { x = 1 } else { x = 2; }", false);

        test("if (a > b) { x = 1; }", false);

        test("if (a > b) { 1x = 10; } else { x = 2; }", false);
        test("""
        if (a > b) {
            x = 10
        } else {
            x = 20;
        }
        """, false);

        test("""
        if (a > b) {
            x = ;
        } else {
            y = 20
        }
        """, false);


        test("""
        if (a > b) {
            x = ;
        } else {
            y = ;
        }
        """, false);

        test("""
        if (a > && b < c) {
            x = ;
        } else {
            y = 20
        }
        """, false);

        test("""
        if (a > b {
            x = 10;
        } else {
            y = 20;
        }
        """, false);

        test("""
        if (a > b) {
            x = 10;
        else {
            y = 20;
        }
        """, false);

        test("""
        if (a > b) {
            x = ;
        else {
            y = 20
        }
        """, false);

        testErrors("""
        if (a > && b < c {
            x = ;
        else {
            y = 20
        }
        """, 5);

        testErrors("""
        if (a > b) {
            x = 10;
        elsewhere {
            y = 20;
        }
        """, 1);

        testErrors("""
        ifx (a > b) {
            x = 10;
        } else {
            y = 20;
        }
        """, 1);

        testErrors("""
        if (a > && b < ) {
            x = 1;
        } else {
            y = 2;
        }
        """, 2);

        testErrors("""
        if (a > b) {
            x =
        } else {
            y = ;
        }
        """, 3);

        testErrors("""
        if (a > b) {
            x 10;
        } else {
            y = 20;
        }
        """, 1);

        testErrors("""
        if (a > b) {
            x 10;
        } else {
            y = ;
        }
        """, 2);

        testErrors("""
        if (a > b) {
            x =
        else {
            y = 20;
        }
        """, 3);

        test("""
        if (a > b) {
            x = else;
        } else {
            y = 20;
        }
        """, true);

        // Корректные конструкции
        test("if (a >= -10 && b != 0) { x = 0; } else { y = -25; }", true);

        test("if (a == b || c <= 3) { x = a; } else { y = b; }", true);

        // Пустой ввод
        testErrors("", 1);

        // Неправильные числа
        testErrors("if (a > -) { x = 1; } else { y = 2; }", 1);

        testErrors("if (a > +1) { x = 1; } else { y = 2; }", 1);

        testErrors("if (a > b) { x = 1.5; } else { y = 2; }", 1);

        // Неправильный оператор сравнения
        testErrors("if (a >> b) { x = 1; } else { y = 2; }", 1);

        // Отрицательное число без цифр
        testErrors("if (a > b) { x = 1; } else { y = -; }", 1);

        // Лишние символы после всей конструкции
        testErrors("if (a > b) { x = 1; } else { y = 2; } abc", 1);

        // Несколько присваиваний в одной ветви
        testErrors("if (a > b) { x = 1; y = 2; } else { z = 3; }", 1);

        // Три ошибки в разных частях конструкции
        testErrors("if (a > -) { x = ; } else { y = -; }", 3);

    }

    private static void test(String text, boolean expectedValid) {
        List<ParseError> errors = new Parser(text).analyze();

        for (ParseError error : errors) {
            System.out.printf(
                    "Error: %s at line %d, column %d%n",
                    error.message(), error.line(), error.column()
            );
        }

        boolean valid = errors.isEmpty();
        System.out.println((valid == expectedValid ? "PASS" : "FAIL") + "\n");
    }

    private static void testErrors(String text, int expectedCount) {
        List<ParseError> errors = new Parser(text).analyze();

        for (ParseError error : errors) {
            System.out.printf(
                    "Error: %s at line %d, column %d%n",
                    error.message(), error.line(), error.column()
            );
        }

        System.out.println(
                "Found: " + errors.size() +
                        ", expected: " + expectedCount
        );

        System.out.println(
                (errors.size() == expectedCount ? "PASS" : "FAIL") + "\n"
        );

    }

}

package IfElseParser.parser;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final String text;
    private int position;
    private int line = 1;
    private int column = 1;
    private final List<ParseError> errors = new ArrayList<>();

    public Parser(String text) {
        this.text = text;
        this.position = 0;
    }

    private boolean hasMore() {
        return position < text.length();
    }

    private char current() {
        return text.charAt(position);
    }

    private void advance() {
        char c = current();
        position++;

        if (c == '\n') {
            line++;
            column = 1;
        } else if (c != '\r') {
            column++;
        }
    }

    private void skipWhitespace() {
        while (hasMore()) {
            char c = current();

            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                break;
            }

            advance();
        }
    }


    private boolean match(String expected) {
        skipWhitespace();

        if (text.startsWith(expected, position)) {
            for (int i = 0; i < expected.length(); i++) {
                advance();
            }
            return true;
        }

        return false;
    }

    private void expect(String expected) {
        if (!match(expected)) {
            throw syntaxError("Expected: " + expected);
        }
    }

    private void condition() {
        andExpr();

        while (match("||")) {
            andExpr();
        }
    }

    private void compareOp() {
        if (match(">=") || match("<=") ||
                match("==") || match("!=") ||
                match(">") || match("<")) {
            return;
        }

        throw syntaxError("Expected comparison operator");
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void digit() {
        if (!hasMore() || !isDigit(current())) {
            throw syntaxError("Expected digit at position");
        }

        advance();
    }

    private void letter() {
        if (!hasMore() || !isLetter(current())) {
            throw syntaxError("Expected letter at position");
        }

        advance();
    }

    private void identifier() {
        skipWhitespace();
        letter();
        idTail();
    }

    private void idTail() {
        if (!hasMore()) return;

        if (isLetter(current())) {
            letter();
            idTail();
        } else if (isDigit(current())) {
            digit();
            idTail();
        }
    }


    private void number() {
        skipWhitespace();
        sign();
        digit();
        numTail();
    }

    private void sign() {
        if (hasMore() && current() == '-') {
            advance();
        }
    }

    private void numTail() {
        if (hasMore() && isDigit(current())) {
            digit();
            numTail();
        }
    }

    private void value() {
        skipWhitespace();

        if (!hasMore()) {
            throw syntaxError("Expected value");
        }

        char c = current();

        if (isLetter(c)) {
            identifier();
        } else if (isDigit(c) || c == '-') {
            number();
        } else {
            throw syntaxError("Expected identifier or number");
        }
    }

    private void comparison() {
        value();
        compareOp();
        value();
    }


    private void andExpr() {
        comparison();

        while (match("&&")) {
            comparison();
        }
    }

    private void assignment() {
        identifier();
        expect("=");
        value();
        expect(";");
    }

    private void skipUntil(char first, char second) {
        while (hasMore() && current() != first && current() != second) {
            advance();
        }
    }

    private void assignmentWithRecovery(boolean firstBranch) {
        try {
            assignment();
        } catch (IllegalArgumentException e) {
            errors.add(new ParseError(e.getMessage(), line, column));

            while (hasMore() && current() != '}') {
                if (firstBranch && isElse()) {
                    break;
                }
                advance();
            }
        }
    }


    private void conditionWithRecovery() {
        try {
            condition();
        } catch (IllegalArgumentException e) {
            errors.add(new ParseError(e.getMessage(), line, column));
            skipUntil(')', '{');
        }
    }


    private void ifElse() {
        expectKeyword("if");
        expect("(");
        conditionWithRecovery();
        expectClosing(")", "{");
        expect("{");

        assignmentWithRecovery(true);

        expectClosing("}", "else");
        expectKeyword("if");
        expect("{");

        assignmentWithRecovery(false);

        expect("}");
    }

    public List<ParseError> analyze() {
        position = 0;
        line = 1;
        column = 1;
        errors.clear();

        try {
            ifElse();
            skipWhitespace();

            if (hasMore()) {
                throw syntaxError("Unexpected characters");
            }
        } catch (IllegalArgumentException e) {
            errors.add(new ParseError(e.getMessage(), line, column));
        }

        return List.copyOf(errors);
    }

    private IllegalArgumentException syntaxError(String message) {
        return new IllegalArgumentException(message);
    }

    private void expectClosing(String expected, String next) {
        if (match(expected)) {
            return;
        }

        boolean foundNext = next.equals("else") ? isElse() : text.startsWith(next, position);
        if (foundNext) {
            errors.add(new ParseError("Expected: " + expected, line, column));
            return;
        }

        throw syntaxError("Expected: " + expected);
    }



    private boolean isElse() {
        if (!text.startsWith("else", position)) {
            return false;
        }

        int end = position + 4;

        return (position == 0 || !isLetter(text.charAt(position - 1)) &&
                        !isDigit(text.charAt(position - 1))) && (end == text.length() ||
                        !isLetter(text.charAt(end)) && !isDigit(text.charAt(end)));
    }

    private void expectKeyword(String keyword) {
        skipWhitespace();

        int end = position + keyword.length();

        if (!text.startsWith(keyword, position) ||
                (end < text.length() &&
                        (isLetter(text.charAt(end)) || isDigit(text.charAt(end))))) {
            throw syntaxError("Expected: " + keyword);
        }

        for (int i = 0; i < keyword.length(); i++) {
            advance();
        }
    }

}

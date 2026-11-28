package IfElseParser.parser;

public record ParseError(
        String message,
        int line,
        int column,
        String q
) {
    public ParseError(String message, int line, int column) {
        this(message, line, column, "");
    }
}

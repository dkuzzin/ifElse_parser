package IfElseParser.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter implements DocumentListener {

    private static final Pattern KEYWORDS =
            Pattern.compile("\\b(if|else)\\b");

    private static final Pattern NUMBERS =
            Pattern.compile("(?<!\\w)-?\\d+\\b");

    private final JTextPane editor;
    private final StyledDocument document;

    private final SimpleAttributeSet normalStyle;
    private final SimpleAttributeSet keywordStyle;
    private final SimpleAttributeSet numberStyle;

    public SyntaxHighlighter(JTextPane editor) {
        this.editor = editor;
        this.document = editor.getStyledDocument();

        normalStyle = createStyle(new Color(36, 41, 47));
        keywordStyle = createStyle(new Color(0, 51, 179));
        numberStyle = createStyle(new Color(23, 80, 235));

        document.addDocumentListener(this);

        highlight();
    }

    private SimpleAttributeSet createStyle(Color color) {
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, color);
        return style;
    }

    private void highlight() {
        String text = editor.getText();

        document.setCharacterAttributes(
                0, document.getLength(), normalStyle, false
        );

        applyStyle(text, KEYWORDS, keywordStyle);
        applyStyle(text, NUMBERS, numberStyle);
    }

    private void applyStyle(
            String text,
            Pattern pattern,
            SimpleAttributeSet style
    ) {
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            document.setCharacterAttributes(
                    matcher.start(),
                    matcher.end() - matcher.start(),
                    style,
                    false
            );
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(this::highlight);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(this::highlight);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Изменение цвета не требует повторной подсветки.
    }
}
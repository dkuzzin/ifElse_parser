package IfElseParser.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

public final class ToolBarIcon implements Icon {
    private static final int SIZE = 22;
    private static final Color ENABLED = new Color(0x36404A);
    private static final Color DISABLED = new Color(0x9AA1AA);
    private static final Stroke STROKE = new BasicStroke(
            1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private final String name;

    private ToolBarIcon(String name) {
        this.name = name;
    }

    public static Icon forAction(String name) {
        return new ToolBarIcon(name);
    }

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }

    @Override
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            canvas.translate(x, y);
            canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            canvas.setColor(component.isEnabled() ? ENABLED : DISABLED);
            canvas.setStroke(STROKE);
            switch (name) {
                case "Создать" -> paintNew(canvas);
                case "Открыть" -> paintOpen(canvas);
                case "Сохранить" -> paintSave(canvas);
                case "Отменить" -> paintHistory(canvas, false);
                case "Повторить" -> paintHistory(canvas, true);
                case "Копировать" -> paintCopy(canvas);
                case "Вырезать" -> paintCut(canvas);
                case "Вставить" -> paintPaste(canvas);
                case "Пуск" -> paintRun(canvas);
                case "Вызов справки" -> paintCircleLabel(canvas, "?");
                case "О программе" -> paintCircleLabel(canvas, "i");
                default -> paintCircleLabel(canvas, "•");
            }
        } finally {
            canvas.dispose();
        }
    }

    private static void paintNew(Graphics2D canvas) {
        Path2D document = new Path2D.Double();
        document.moveTo(5, 2);
        document.lineTo(13, 2);
        document.lineTo(17, 6);
        document.lineTo(17, 20);
        document.lineTo(5, 20);
        document.closePath();
        canvas.draw(document);
        canvas.drawLine(13, 2, 13, 6);
        canvas.drawLine(13, 6, 17, 6);
        canvas.drawLine(8, 13, 14, 13);
        canvas.drawLine(11, 10, 11, 16);
    }

    private static void paintOpen(Graphics2D canvas) {
        Path2D folder = new Path2D.Double();
        folder.moveTo(2, 6);
        folder.lineTo(9, 6);
        folder.lineTo(11, 8);
        folder.lineTo(20, 8);
        folder.lineTo(17, 19);
        folder.lineTo(3, 19);
        folder.closePath();
        canvas.draw(folder);
        canvas.drawLine(3, 6, 3, 4);
        canvas.drawLine(3, 4, 9, 4);
        canvas.drawLine(9, 4, 11, 6);
    }

    private static void paintSave(Graphics2D canvas) {
        canvas.drawRoundRect(3, 2, 16, 18, 2, 2);
        canvas.drawRect(7, 2, 8, 6);
        canvas.drawRoundRect(7, 13, 8, 7, 1, 1);
        canvas.drawLine(13, 4, 13, 7);
    }

    private static void paintHistory(Graphics2D canvas, boolean redo) {
        if (redo) {
            canvas.draw(new Arc2D.Double(4, 5, 14, 13, 205, 255, Arc2D.OPEN));
            canvas.drawLine(18, 5, 18, 11);
            canvas.drawLine(18, 5, 12, 5);
        } else {
            canvas.draw(new Arc2D.Double(4, 5, 14, 13, 80, 255, Arc2D.OPEN));
            canvas.drawLine(4, 5, 4, 11);
            canvas.drawLine(4, 5, 10, 5);
        }
    }

    private static void paintCopy(Graphics2D canvas) {
        canvas.drawRoundRect(7, 6, 12, 14, 2, 2);
        canvas.drawRoundRect(3, 2, 12, 14, 2, 2);
    }

    private static void paintCut(Graphics2D canvas) {
        canvas.drawOval(2, 14, 6, 6);
        canvas.drawOval(14, 14, 6, 6);
        canvas.drawLine(7, 15, 17, 3);
        canvas.drawLine(5, 3, 15, 15);
    }

    private static void paintPaste(Graphics2D canvas) {
        canvas.drawRoundRect(4, 5, 14, 15, 2, 2);
        canvas.drawRoundRect(7, 2, 8, 5, 2, 2);
        canvas.drawLine(8, 11, 14, 11);
        canvas.drawLine(8, 15, 14, 15);
    }

    private static void paintRun(Graphics2D canvas) {
        Path2D triangle = new Path2D.Double();
        triangle.moveTo(6, 3);
        triangle.lineTo(19, 11);
        triangle.lineTo(6, 19);
        triangle.closePath();
        canvas.draw(triangle);
    }

    private static void paintCircleLabel(Graphics2D canvas, String label) {
        canvas.drawOval(2, 2, 18, 18);
        canvas.setFont(new Font("Noto Sans", Font.BOLD, 14));
        FontMetrics metrics = canvas.getFontMetrics();
        int textX = (SIZE - metrics.stringWidth(label)) / 2;
        int textY = (SIZE - metrics.getHeight()) / 2 + metrics.getAscent();
        canvas.drawString(label, textX, textY);
    }
}

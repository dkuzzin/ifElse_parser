package IfElseParser.ui;

import javax.swing.*;
import java.awt.*;

public final class FileChooserStyle {
    private static final Font FONT = new Font("Noto Sans", Font.PLAIN, 16);
    private static final int PADDING = 16;
    private static final Dimension PREFERRED_SIZE = new Dimension(900, 600);

    private FileChooserStyle() {
    }

    public static JFileChooser createChooser(String title) {
        String[][] labels = {
                {"saveButtonText", "Сохранить"},
                {"cancelButtonText", "Отмена"},
                {"openButtonText", "Открыть"},
                {"saveDialogTitleText", "Сохранить как"},
                {"lookInLabelText", "Папка:"},
                {"saveInLabelText", "Сохранить в:"},
                {"fileNameLabelText", "Имя файла:"},
                {"folderNameLabelText", "Имя папки:"},
                {"filesOfTypeLabelText", "Тип файлов:"},
                {"acceptAllFileFilterText", "Все файлы"},
                {"saveButtonToolTipText", "Сохранить файл"},
                {"cancelButtonToolTipText", "Отменить выбор файла"},
                {"openButtonToolTipText", "Открыть выбранный файл"},
                {"upFolderToolTipText", "На уровень выше"},
                {"upFolderAccessibleName", "На уровень выше"},
                {"homeFolderToolTipText", "Домашняя папка"},
                {"homeFolderAccessibleName", "Домашняя папка"},
                {"newFolderToolTipText", "Создать папку"},
                {"newFolderAccessibleName", "Создать папку"},
                {"listViewButtonToolTipText", "Список"},
                {"listViewButtonAccessibleName", "Список"},
                {"detailsViewButtonToolTipText", "Таблица"},
                {"detailsViewButtonAccessibleName", "Таблица"},
                {"fileNameHeaderText", "Имя"},
                {"fileSizeHeaderText", "Размер"},
                {"fileTypeHeaderText", "Тип"},
                {"fileDateHeaderText", "Изменён"},
                {"fileAttrHeaderText", "Атрибуты"},
                {"viewMenuLabelText", "Вид"},
                {"listViewActionLabelText", "Список"},
                {"detailsViewActionLabelText", "Таблица"},
                {"refreshActionLabelText", "Обновить"},
                {"newFolderActionLabelText", "Создать папку"},
                {"newFolderTitleText", "Новая папка"},
                {"newFolderPromptText", "Имя новой папки:"},
                {"newFolderErrorText", "Не удалось создать папку"},
                {"newFolderErrorSeparator", ": "},
                {"newFolderParentDoesntExistTitleText", "Ошибка создания папки"},
                {"newFolderParentDoesntExistText", "Родительская папка не существует."},
                {"directoryOpenButtonText", "Открыть"},
                {"directoryOpenButtonToolTipText", "Открыть выбранную папку"},
                {"renameErrorTitleText", "Ошибка переименования"},
                {"renameErrorText", "Не удалось переименовать «{0}»."},
                {"renameErrorFileExistsText", "Файл «{0}» уже существует."}
        };
        for (String[] label : labels) {
            UIManager.put("FileChooser." + label[0], label[1]);
        }
        UIManager.put("FileChooser.other.newFolder", "Новая папка");
        UIManager.put("FileChooser.other.newFolder.subsequent", "Новая папка ({0})");

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        chooser.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        chooser.setPreferredSize(PREFERRED_SIZE);
        applyFont(chooser);

        // Nimbus exposes the same action as its "Таблица" toolbar button.
        Action detailsView = chooser.getActionMap().get("viewTypeDetails");
        if (detailsView != null) {
            detailsView.actionPerformed(new java.awt.event.ActionEvent(
                    chooser, java.awt.event.ActionEvent.ACTION_PERFORMED, "viewTypeDetails"));
            applyFont(chooser);
        }
        return chooser;
    }

    private static void applyFont(Component component) {
        component.setFont(FONT);
        if (component instanceof JTable table) {
            table.setRowHeight(table.getFontMetrics(FONT).getHeight() + PADDING);
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyFont(child);
            }
        }
    }
}

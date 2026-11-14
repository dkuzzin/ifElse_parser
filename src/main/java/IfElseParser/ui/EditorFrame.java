package IfElseParser.ui;

import IfElseParser.ui.dialog.EditorDialogs;
import IfElseParser.ui.dialog.HelpDialog;
import IfElseParser.ui.dialog.TextInfoDialog;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;


public class EditorFrame extends JFrame {
    private static final double EDITOR_SPACE_SHARE = 0.7;
    private static final String APPLICATION_NAME = "IfElseParser";
    private static final String NEW_DOCUMENT_NAME = "Новый документ";
    private static final int EDITOR_FONT_SIZE = 18;

    private final JTextArea editorArea = new JTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final JLabel documentNameLabel = new JLabel(NEW_DOCUMENT_NAME);
    private Path currentFile;
    private String savedText = "";
    private final UndoManager undoManager = new UndoManager();

    private final Action undoAction = new AbstractAction("Отменить") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
            updateUndoActions();
        }
    };

    private final Action redoAction = new AbstractAction("Повторить") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
            updateUndoActions();
        }
    };

    public EditorFrame() {
        super(NEW_DOCUMENT_NAME + " — " + APPLICATION_NAME);
        undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
        redoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
        editorArea.setFont(new Font("Noto Sans Mono", Font.PLAIN, EDITOR_FONT_SIZE));
        editorArea.getDocument().addUndoableEditListener(event -> {
            undoManager.addEdit(event.getEdit());
            updateUndoActions();
        });
        updateUndoActions();

        Action newDocumentAction = new AbstractAction("Создать") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!confirmUnsavedChanges()) {
                    return;
                }

                editorArea.setText("");
                outputArea.setText("");
                currentFile = null;
                savedText = "";
                updateWindowTitle();
                resetUndoHistory();
            }
        };

        Action openAction = new AbstractAction("Открыть") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDocument();
            }
        };

        Action saveAction = new AbstractAction("Сохранить") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDocument();
            }
        };

        Action saveAsAction = new AbstractAction("Сохранить как") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDocumentAs();
            }
        };

        Action exitAction = new AbstractAction("Выход") {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDocumentWindow();
            }
        };






        Action cutAction = createEditorAction("Вырезать", editorArea::cut);
        Action copyAction = createEditorAction("Копировать", editorArea::copy);
        Action pasteAction = createEditorAction("Вставить", editorArea::paste);
        Action deleteAction = createEditorAction("Удалить", () -> editorArea.replaceSelection(""));
        Action selectAllAction = createEditorAction("Выделить всё", editorArea::selectAll);
        Action helpAction = new AbstractAction("Вызов справки") {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpDialog.show(EditorFrame.this);
            }
        };
        Action aboutAction = new AbstractAction("О программе") {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorDialogs.showAbout(EditorFrame.this);
            }
        };




        Action[] textActions = createTextActions();

        setJMenuBar(new EditorMenuBar(newDocumentAction, openAction, saveAction, saveAsAction,
                exitAction, helpAction, aboutAction, textActions, undoAction, redoAction, cutAction, copyAction,
                pasteAction, deleteAction, selectAllAction));

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDocumentWindow();
            }
        });

        setLayout(new BorderLayout());



        EditorToolBar toolBar = new EditorToolBar(
                new Action[]{newDocumentAction, openAction, saveAction},
                new Action[]{undoAction, redoAction, copyAction, cutAction, pasteAction},
                new Action[]{helpAction, aboutAction});
        add(toolBar.createScrollPane(), BorderLayout.NORTH);

        outputArea.setEditable(false);

        documentNameLabel.setFont(new Font("Noto Sans", Font.BOLD, 15));
        documentNameLabel.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
        documentNameLabel.setToolTipText(NEW_DOCUMENT_NAME);

        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.add(documentNameLabel, BorderLayout.NORTH);
        editorPanel.add(new JScrollPane(editorArea), BorderLayout.CENTER);



        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, new JScrollPane(outputArea));

        splitPane.setResizeWeight(EDITOR_SPACE_SHARE);

        add(splitPane, BorderLayout.CENTER);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(EDITOR_SPACE_SHARE));
    }

    private Action createEditorAction(String name, Runnable command) {
        return new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorArea.requestFocusInWindow();
                command.run();
            }
        };
    }

    private Action[] createTextActions() {
        String[] titles = {
                "Постановка задачи", "Грамматика", "Классификация грамматики",
                "Метод анализа", "Тестовый пример", "Список литературы",
                "Исходный код программы"
        };
        Action[] actions = new Action[titles.length];
        for (int index = 0; index < titles.length; index++) {
            String title = titles[index];
            actions[index] = new AbstractAction(title) {
                @Override
                public void actionPerformed(ActionEvent event) {
                    TextInfoDialog.show(EditorFrame.this, title);
                }
            };
        }
        return actions;
    }

    private void updateUndoActions() {
        undoAction.setEnabled(undoManager.canUndo());
        redoAction.setEnabled(undoManager.canRedo());
    }

    private void updateWindowTitle() {
        String documentName = currentFile == null ? NEW_DOCUMENT_NAME : currentFile.getFileName().toString();
        setTitle(documentName + " — " + APPLICATION_NAME);
        documentNameLabel.setText(documentName);
        documentNameLabel.setToolTipText(currentFile == null ? NEW_DOCUMENT_NAME : currentFile.toAbsolutePath().toString());
    }

    private void resetUndoHistory() {
        undoManager.discardAllEdits();
        updateUndoActions();
    }

    private void closeDocumentWindow() {
        if (confirmUnsavedChanges()) {
            dispose();
        }
    }

    //todo change to flag? not effective.
    private boolean confirmUnsavedChanges() {
        if (editorArea.getText().equals(savedText)) {
            return true;
        }
        return switch (EditorDialogs.showUnsavedChanges(this)) {
            case SAVE -> saveDocument();
            case DISCARD -> true;
            case CANCEL -> false;
        };
    }

    private boolean saveDocument() {
        return currentFile == null ? saveDocumentAs() : writeDocument(currentFile);
    }

    private void openDocument() {
        JFileChooser chooser = FileChooserStyle.createChooser("Открыть файл");
        if (currentFile != null) {
            chooser.setSelectedFile(currentFile.toFile());
        }
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if (!confirmUnsavedChanges()) {
            return;
        }

        Path path = chooser.getSelectedFile().toPath();
        try {
            String text = Files.readString(path, StandardCharsets.UTF_8);
            editorArea.setText(text);
            editorArea.setCaretPosition(0);
            outputArea.setText("");
            currentFile = path;
            savedText = text;
            updateWindowTitle();
            resetUndoHistory();
        } catch (IOException | SecurityException e) {
            EditorDialogs.showError(this, "Ошибка открытия", "Не удалось открыть файл:\n" + e.getMessage());
        }
    }

    private boolean saveDocumentAs() {
        JFileChooser chooser = FileChooserStyle.createChooser("Сохранить как");
        if (currentFile != null) {
            chooser.setSelectedFile(currentFile.toFile());
        }

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        Path path = chooser.getSelectedFile().toPath();
        if (Files.exists(path)) {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Файл «" + path.getFileName() + "» уже существует. Заменить его?",
                    "Подтверждение перезаписи",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (answer != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        return writeDocument(path);
    }

    private boolean writeDocument(Path path) {
        try {
            String text = editorArea.getText();
            Files.writeString(path, text, StandardCharsets.UTF_8);
            currentFile = path;
            savedText = text;
            updateWindowTitle();
            return true;
        } catch (IOException | SecurityException e) {
            EditorDialogs.showError(this, "Ошибка сохранения",
                    "Не удалось сохранить файл:\n" + e.getMessage());
            return false;
        }
    }
}

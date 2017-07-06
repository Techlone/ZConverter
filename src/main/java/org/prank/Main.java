package org.prank;

import stanhebben.zenscript.statements.Statement;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main extends JFrame {
    private JTextPane zsTextPane;
    private JTextPane luaTextPane;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        Font verdana = new Font("Verdana", Font.PLAIN, 14);

        JButton pasteButton = createButton("Paste from clipboard", new PasteAction());
        JButton convertButton = createButton("Convert", new ConvertAction());
        JButton copyButton = createButton("Copy in clipboard", new CopyAction());

        JScrollPane zsScrollPane = createScrollPane(zsTextPane = new JTextPane());
        tuneTextPane(zsTextPane, verdana);
        zsTextPane.setText("//Paste ZenScript code here " + System.lineSeparator() +
                "//Or a full path to a folder with scripts");
        //zsTextPane.setText(getFile("test.zs"));

        JScrollPane luaScrollPane = createScrollPane(luaTextPane = new JTextPane());
        tuneTextPane(luaTextPane, verdana);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 3;
        c.insets = new Insets(5,5,0,5);
        add(zsScrollPane, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.gridx = 0;
        c.weighty = 0.0;
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.insets = new Insets(5,5,0,0);
        add(pasteButton, c);

        c.gridy = 1;
        c.gridx = 1;
        c.insets = new Insets(5,5,0,0);
        add(convertButton, c);

        c.gridy = 1;
        c.gridx = 2;
        c.insets = new Insets(5,5,0,5);
        add(copyButton, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridy = 2;
        c.gridx = 0;
        c.weighty = 1.0;
        c.weightx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(5,5,5,5);
        add(luaScrollPane, c);

        setTitle("ZConverter");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));
        pack();
        setVisible(true);
    }

    private JButton createButton(String text, AbstractAction action) {
        JButton convertButton = new JButton();
        convertButton.setAction(action);
        convertButton.setText(text);
        return convertButton;
    }

    private void tuneTextPane(JTextPane textPane, Font verdana) {
        textPane.setFont(verdana);
        textPane.setForeground(Color.BLUE);
        textPane.setBackground(Color.lightGray);
    }

    private JScrollPane createScrollPane(JTextPane view) {
        return new JScrollPane(view);
    }

    private class PasteAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);
            if (contents == null || !contents.isDataFlavorSupported(DataFlavor.stringFlavor)) return;
            try {
                zsTextPane.setText((String) contents.getTransferData(DataFlavor.stringFlavor));
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
    }

    private class ConvertAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            String zsInput = zsTextPane.getText();
            String luaOutput = ZConverter.convert(zsInput);
            luaTextPane.setText(luaOutput);
        }
    }


    private class CopyAction extends AbstractAction implements ClipboardOwner {
        @Override
        public void actionPerformed(ActionEvent e) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(luaTextPane.getText()), this);
        }

        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
        }
    }

    private String getFile(String fileName) {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append(Statement.nl);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}

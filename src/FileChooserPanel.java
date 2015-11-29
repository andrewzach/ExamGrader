// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import javax.swing.*;
import java.awt.*;

// A small JPanel that has a text field for entering a file path, as well as a button that opens a file dialog to select it
// Designed to be used in JOptionPane dialogs for entering information.
public class FileChooserPanel extends JPanel
{
    private String type; // either "open" or "save"
    private JTextField fileText;
    private JButton selectButton;
    private JFileChooser fileChooser;

    public FileChooserPanel(String type)
    {
        this.type = type;
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileText = new JTextField();
        selectButton = new JButton("...");
        setLayout(new BorderLayout(5, 5));
        add(fileText, BorderLayout.CENTER);
        add(selectButton, BorderLayout.EAST);
        if (type.equals("save"))
        {
            addFileSaveAction();
        }
        else
        {
            addFileOpenAction();
        }
    }

    private void addFileOpenAction()
    {
        selectButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(selectButton);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileText.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    private void addFileSaveAction()
    {
        selectButton.addActionListener(e -> {
            int result = fileChooser.showSaveDialog(selectButton);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                fileText.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    public String getPath()
    {
        return fileText.getText();
    }
}

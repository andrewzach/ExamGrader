// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 *  A small JPanel that has a text field for entering a file path, as well as a button that opens a file dialog to select it
 *  Designed to be used in JOptionPane dialogs for entering information.
 */
public class FileChooserPanel extends JPanel
{
    private String type; // either "open" or "save"
    private JTextField fileText;
    private JButton selectButton;
    private JFileChooser fileChooser;

    /**
     * Initializes the FileChooserPanel
     * @param type "save" or "open", depending on whether panel is used to save or open a file.
     */
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

    // Adds action to button bringing up file open dialog
    private void addFileOpenAction()
    {
        selectButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(selectButton);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileText.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    // Adds action to button bringing up file save dialog
    private void addFileSaveAction()
    {
        selectButton.addActionListener(e -> {
            fileChooser.setFileFilter(new FileNameExtensionFilter("Comma separated values (csv)", "csv"));
            int result = fileChooser.showSaveDialog(selectButton);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                if (!fileName.endsWith(".csv")) // Add correct file extension if it's not already there.
                {
                    fileName += ".csv";
                }
                fileText.setText(fileName);
            }
        });
    }

    /** @return absolute file path + filename for the selected file, or whatever is in the JTextField */
    public String getPath()
    {
        return fileText.getText();
    }
}

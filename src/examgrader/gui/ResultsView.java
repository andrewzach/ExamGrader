// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import examgrader.controllers.MainController;
import examgrader.model.*;

/**
 * Panel at the bottom of the GUI displaying JList of exam results.
 * Used in both the StudentsPanel and the ExamsPanel to display results.
 * Contains buttons to: View Answers (as a dialog) and Export to File.
 */
public class ResultsView extends JPanel
{
    private JScrollPane resultsListPane;
    private JList<StudentExam> resultsList;
    private DefaultListModel<StudentExam> resultsListModel;
    private JLabel resultsLabels;
    private JPanel buttonsPanel;
    private JButton viewAnswers;
    private JButton edit;
    private JButton export;
    private MainController mc;

    /**
     * Initializes the ResultsView
     * Creates all GUI components and arranges their layout.
     * Adds action listeners to all buttons.
     * @param mc MainController used to retrieve data and perform all actions on the model
     */
    public ResultsView(MainController mc)
    {
        this.mc = mc;
        setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Results"), BorderFactory.createEmptyBorder(8,8,8,8)));

        resultsListModel = new DefaultListModel<>();
        resultsList = new JList(resultsListModel);
        resultsListPane = new JScrollPane(resultsList);
        resultsLabels = new JLabel(String.format("%-13s %-15s %-12s %-15s %-5s    %-5s    %-3s", "First", "Last", "ID", "Exam Name", "Score", "Total", " % " ));
        buttonsPanel = new JPanel();
        viewAnswers = new JButton("View Answers");
        export = new JButton("Export to file...");

        resultsList.setFont(ExamGraderGUI.monospaced);
        resultsLabels.setFont(ExamGraderGUI.monospacedBold);

        gridComponents();
        addButtonActions();
    }

    /**
     * Updates the JList with the list of StudentExams provided.
     * @param studentExams A list of StudentExams to display
     */
    public void updateView(List<StudentExam> studentExams)
    {
        resultsListModel.clear();
        // Sort results by student's last name. Uses lambda function for comparator
        Collections.sort(studentExams, (se1, se2) -> se1.getStudent().compareTo(se2.getStudent()));
        for (StudentExam se : studentExams)
        {
            resultsListModel.addElement(se);
        }
    }

    // Handles all placement of components in the GUI.
    private void gridComponents()
    {
        buttonsPanel.add(viewAnswers);
        buttonsPanel.add(export);

        setLayout(new BorderLayout(5, 5));

        add(resultsLabels, BorderLayout.NORTH);
        add(resultsListPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    // Adds action listeners to all buttons
    private void addButtonActions()
    {
        // View Answers - view answers for selected StudentExam as a dialog comparing them to the correct answers
        viewAnswers.addActionListener(e -> {
            StudentExam selected = resultsList.getSelectedValue();
            if (selected == null) return; // if nothing selected, don't do anything
            JPanel dialog = new JPanel(new BorderLayout(5,5));
            int[] answers = selected.getAnswers();
            int[] correctAnswers = selected.getExam().getKey().getAnswers();
            JPanel labelsPanel = new JPanel(new GridLayout(3,1,10,10));
            labelsPanel.add(new JLabel("Question #: ", SwingConstants.RIGHT));
            labelsPanel.add(new JLabel("Student Answer: ", SwingConstants.RIGHT));
            labelsPanel.add(new JLabel("Correct Answer: ", SwingConstants.RIGHT));

            JPanel answersPanel = new JPanel(new GridLayout(3,answers.length+1,10,10));
            // Display Question #
            for (int i = 1; i <= correctAnswers.length; i++)
            {
                answersPanel.add(new JLabel(String.valueOf(i)));
            }
            // Display student Answers
            for (int i = 0; i < answers.length; i++)
            {
                JLabel answerLabel = new JLabel(String.valueOf(answers[i]));
                if (answers[i] != correctAnswers[i])
                {
                    answerLabel.setForeground(Color.RED);   // mark incorrect answers red
                }
                answersPanel.add(answerLabel);
            }
            // Display answer Key
            for (int i = 0; i < correctAnswers.length; i++)
            {
                answersPanel.add(new JLabel(String.valueOf(correctAnswers[i])));
            }

            dialog.add(labelsPanel, BorderLayout.WEST);
            dialog.add(answersPanel, BorderLayout.CENTER);
            JOptionPane.showMessageDialog(viewAnswers, dialog, selected.getStudent().getName() + "'s Answers", JOptionPane.INFORMATION_MESSAGE);
        });

        // Export to file... Exports all StudentExams in the ResultsView to a CSV file, selected in a file dialog.
        export.addActionListener(ae -> {
            JPanel dialog = new JPanel(new BorderLayout(5,5));
            FileChooserPanel outFile = new FileChooserPanel("save");
            dialog.add(new JLabel("Output File"), BorderLayout.WEST);
            dialog.add(outFile, BorderLayout.CENTER);
            dialog.setPreferredSize(new Dimension(400, 30));

            int success = JOptionPane.showConfirmDialog(export, dialog, "Select Export File", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (success == JOptionPane.OK_OPTION)
            {
                try
                {
                    String fileName = outFile.getPath();
                    List<StudentExam> results = new ArrayList<>();
                    for (int i = 0; i < resultsListModel.getSize(); i++) {
                        results.add(resultsListModel.getElementAt(i));
                    }

                    mc.writeResultsFile(fileName, results);
                    JOptionPane.showMessageDialog(export, "Results file successfully created.\n" + fileName);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(export, "Error creating output file." + e.toString());
                }
            }

        });
    }
}

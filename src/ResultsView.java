// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

// Panel at the bottom of the GUI displaying JList of exam results.
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
        export = new JButton("Export...");

        resultsList.setFont(ExamGraderGUI.monospaced);
        resultsLabels.setFont(ExamGraderGUI.monospacedBold);

        gridComponents();
        addButtonActions();
    }

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

    public void gridComponents()
    {
        buttonsPanel.add(viewAnswers);
        buttonsPanel.add(export);

        setLayout(new BorderLayout(5, 5));

        add(resultsLabels, BorderLayout.NORTH);
        add(resultsListPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public void addButtonActions()
    {
        viewAnswers.addActionListener(e -> {
            JPanel dialog = new JPanel(new BorderLayout(5,5));
            StudentExam selected = resultsList.getSelectedValue();
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

        export.addActionListener(ae -> {
            JPanel dialog = new JPanel(new BorderLayout(5,5));
            FileChooserPanel outFile = new FileChooserPanel("save");
            dialog.add(new JLabel("Output File"), BorderLayout.WEST);
            dialog.add(outFile, BorderLayout.CENTER);
            dialog.setPreferredSize(new Dimension(400,30));

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

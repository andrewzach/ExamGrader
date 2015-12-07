// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import examgrader.controllers.MainController;
import examgrader.model.*;
import java.util.List;

// Exams tab on GUI..
public class ExamsPanel extends JPanel
{
    private ExamGraderGUI parent;
    private MainController mc;

    private JPanel examListPanel;
    private JScrollPane examScrollPane;
    private JList<Exam> examList;
    private DefaultListModel<Exam> examListModel;
    private JLabel examsListLabels;

    private JButton addExam;
    private JButton delExam;
    private JButton addResults;
    private JButton addKey;
    private JButton clearAll;
    private JPanel clearAllPanel;
    private JPanel buttonsPanel;

    private ResultsView resultsView;

    public ExamsPanel(ExamGraderGUI parent, MainController mc)
    {
        this.parent = parent;
        this.mc = mc;

        // Instantiate GUI components
        examListPanel = new JPanel();
        examListModel = new DefaultListModel<>();
        examList = new JList<>(examListModel);
        examScrollPane = new JScrollPane(examList);
        examsListLabels = new JLabel(String.format("%-15s %-10s %-10s", "Name", "Questions", "Students"));
        addExam = new JButton("Add Exam");
        delExam = new JButton("Delete Exam");
        addResults = new JButton("Add Results");
        addKey = new JButton("Set Exam Key");
        clearAll = new JButton("Clear All Data");
        clearAllPanel = new JPanel();
        resultsView = new ResultsView(mc);
        buttonsPanel = new JPanel();

        examsListLabels.setFont(ExamGraderGUI.monospacedBold);
        examList.setFont(ExamGraderGUI.monospaced);

        gridComponents();
        addButtonActions();
        updateExamsList();
    }

    // Handles all placement of components in the GUI.
    private void gridComponents()
    {
        examListPanel.setLayout(new BorderLayout());
        examListPanel.add(examsListLabels, BorderLayout.NORTH);
        examListPanel.add(examScrollPane, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(5, 1, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 0, 5));
        buttonsPanel.setPreferredSize(new Dimension(170, 400));
        buttonsPanel.add(addExam);
        buttonsPanel.add(delExam);
        buttonsPanel.add(addResults);
        buttonsPanel.add(addKey);
        clearAllPanel.setLayout(new BorderLayout());
        clearAllPanel.add(clearAll, BorderLayout.SOUTH);
        buttonsPanel.add(clearAllPanel);

        resultsView.setPreferredSize(new Dimension(800, 300));

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(examListPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);
        add(resultsView, BorderLayout.SOUTH);
    }
    private void addButtonActions()
    {
        // View Results based on selection in Exam JList
        examList.addListSelectionListener(lse -> {
            if (!lse.getValueIsAdjusting()) {
                List<StudentExam> studentExams = new ArrayList<>();
                for (Exam e : examList.getSelectedValuesList())
                {
                    for (StudentExam se : e.getStudentAnswers())
                        studentExams.add(se);
                }
                resultsView.updateView(studentExams);
            }
        });

        // Delete Exam
        delExam.addActionListener(ae -> {
            for (Exam e : examList.getSelectedValuesList()) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete exam " + e.getName() + "?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    mc.deleteExam(e);
                }
            }
            parent.updateData();
        });

        // Add Results to existing exam
        addResults.addActionListener(ae -> {
            JPanel dialogPanel = new JPanel(new BorderLayout());
            FileChooserPanel answersFile = new FileChooserPanel("open");
            answersFile.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            dialogPanel.add(new JLabel("Answers File"), BorderLayout.WEST);
            dialogPanel.add(answersFile, BorderLayout.CENTER);
            dialogPanel.setPreferredSize(new Dimension(400, 30));

            int result = JOptionPane.showConfirmDialog(parent, dialogPanel, "Add Answers", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION)
            {
                try {
                    Exam selectedExam = examList.getSelectedValue();
                    List<StudentExam> studentAnswers = mc.loadStudentExams(answersFile.getPath(), selectedExam);
                    selectedExam.addStudentAnswers(studentAnswers);
                    selectedExam.markStudentAnswers(studentAnswers);
                    parent.updateData();
                }
                catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e)
                {
                    JOptionPane.showMessageDialog(parent, "Error parsing answers file", "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (NullPointerException e)
                {
                    JOptionPane.showMessageDialog(parent, "Please select an exam to add answers to.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add Exam
        addExam.addActionListener(ae -> {
            // Create dialog to get user input for new Exam
            JPanel dialogPanel = new JPanel(new BorderLayout());
            JPanel labels = new JPanel(new GridLayout(4,1,5,5));
            JPanel inputs = new JPanel(new GridLayout(4,1,5,5));
            JTextField name = new JTextField();
            JTextField penalty = new JTextField("0.25");
            FileChooserPanel keyFile = new FileChooserPanel("open");
            FileChooserPanel answersFile = new FileChooserPanel("open");

            labels.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            labels.add(new JLabel("Exam Name", SwingConstants.RIGHT));
            labels.add(new JLabel("Penalty", SwingConstants.RIGHT));
            labels.add(new JLabel("Answer Key", SwingConstants.RIGHT));
            labels.add(new JLabel("Student Answers", SwingConstants.RIGHT));

            inputs.add(name);
            inputs.add(penalty);
            inputs.add(keyFile);
            inputs.add(answersFile);

            dialogPanel.setPreferredSize(new Dimension(400, 150));
            dialogPanel.add(labels, BorderLayout.WEST);
            dialogPanel.add(inputs, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(parent, dialogPanel, "Add Exam", JOptionPane.OK_CANCEL_OPTION);
            if( result == JOptionPane.OK_OPTION)
            {
                try
                {
                    Exam newExam = new Exam(name.getText(), Double.parseDouble(penalty.getText()));
                    if (!keyFile.getPath().equals(""))
                    {
                        ExamKey key = mc.loadExamKey(keyFile.getPath(), newExam.getName());
                        newExam.addKey(key);
                    }
                    if (!answersFile.getPath().equals(""))
                    {
                        List<StudentExam>studentAnswers = mc.loadStudentExams(answersFile.getPath(), newExam);
                        newExam.addStudentAnswers(studentAnswers);
                        newExam.markStudentAnswers(studentAnswers);
                    }
                    mc.addExam(newExam);
                    parent.updateData();
                }
                catch (IllegalArgumentException e)
                {
                    JOptionPane.showMessageDialog(parent, "Error adding exam: " + e.getMessage());
                }
            }
        });

        clearAll.addActionListener(ae -> {
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all Exam and Student data?");
            if (confirmation == JOptionPane.YES_OPTION)
            {
                mc.clearAllData();
                parent.updateData();
            }
        });

        addKey.addActionListener(ae -> {
            JPanel dialogPanel = new JPanel(new BorderLayout());
            FileChooserPanel answersFile = new FileChooserPanel("open");
            answersFile.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            dialogPanel.add(new JLabel("Exam Key File"), BorderLayout.WEST);
            dialogPanel.add(answersFile, BorderLayout.CENTER);
            dialogPanel.setPreferredSize(new Dimension(400, 30));

            int result = JOptionPane.showConfirmDialog(parent, dialogPanel, "Add Exam Key", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION)
            {
                try
                {
                    Exam selectedExam = examList.getSelectedValue();
                    selectedExam.addKey(mc.loadExamKey(answersFile.getPath(), selectedExam.getName()));
                    selectedExam.markAllStudentAnswers();
                    parent.updateData();
                }
                catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e)
                {
                    JOptionPane.showMessageDialog(parent, "Error parsing answers file", "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (NullPointerException e)
                {
                    JOptionPane.showMessageDialog(parent, "Please select an exam to add answers to.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Update the Exam JList with most recent data
    public void updateExamsList()
    {
        examListModel.clear();
        for (Exam e : mc.getExams())
        {
            examListModel.addElement(e);
        }
    }
}

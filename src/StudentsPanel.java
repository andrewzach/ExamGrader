// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

// Students tab on GUI
public class StudentsPanel extends JPanel
{
    private ExamGraderGUI parent;
    private MainController mc;

    private JPanel studentListPanel;
    private JScrollPane studentScrollPane;
    private JList<Student> studentList;
    private DefaultListModel<Student> studentListModel;
    private JLabel studentListLabels;

    private JButton addStudent;
    private JButton delStudent;
    private JButton search;
    private JButton clear;
    private JTextField searchField;
    private JPanel controlsPanel;
    private JPanel searchPanel;
    private JPanel searchButtonsPanel;
    private JPanel buttonsPanel;

    private ResultsView resultsView;

    public StudentsPanel(ExamGraderGUI parent, MainController mc)
    {
        this.parent = parent;
        this.mc = mc;
        studentListModel = new DefaultListModel<>();
        studentListPanel = new JPanel();
        searchPanel = new JPanel();
        searchButtonsPanel = new JPanel();
        controlsPanel = new JPanel();
        studentList = new JList<>(studentListModel);
        studentScrollPane = new JScrollPane(studentList);
        studentListLabels = new JLabel(String.format("%-11s %-15s %-10s", "First", "Last", "ID"));
        addStudent = new JButton("Add Student");
        delStudent = new JButton("Delete Student");
        search = new JButton("Search");
        clear = new JButton("Clear");
        searchField = new JTextField();
        resultsView = new ResultsView(mc);
        buttonsPanel = new JPanel();

        studentList.setFont(ExamGraderGUI.monospaced);
        studentListLabels.setFont(ExamGraderGUI.monospacedBold);

        gridComponents();
        addButtonActions();

        updateStudentList();
    }

    public void updateStudentList()
    {
        studentListModel.clear();
        List<Student> students = mc.getStudents();
        Collections.sort(students); // Sort list of students by last name before displaying
        for (Student s : students)
        {
            studentListModel.addElement(s);
        }
    }

    // Handles all placement of components in the GUI.
    private void gridComponents()
    {
        studentListPanel.setLayout(new BorderLayout());
        studentListPanel.add(studentListLabels, BorderLayout.NORTH);
        studentListPanel.add(studentScrollPane, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
        buttonsPanel.add(addStudent);
        buttonsPanel.add(delStudent);

        searchButtonsPanel.setPreferredSize(new Dimension(170, 30));
        searchButtonsPanel.setLayout(new GridLayout(1, 2, 5, 5));
        searchButtonsPanel.add(search);
        searchButtonsPanel.add(clear);
        searchField.setMargin(new Insets(2, 2, 2, 2));
        searchField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        searchPanel.setLayout(new BorderLayout(5, 5));
        searchPanel.add(searchButtonsPanel, BorderLayout.SOUTH);
        searchPanel.add(searchField, BorderLayout.CENTER);

        controlsPanel.setLayout(new BorderLayout(5, 5));
        controlsPanel.add(buttonsPanel, BorderLayout.CENTER);
        controlsPanel.add(searchPanel, BorderLayout.SOUTH);

        resultsView.setPreferredSize(new Dimension(800,300));

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(studentListPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);
        add(resultsView, BorderLayout.SOUTH);
    }
    private void addButtonActions() {
        addStudent.addActionListener(ae -> {
            JPanel dialog = new JPanel(new BorderLayout(5,5));
            JPanel labels = new JPanel(new GridLayout(3,1,5,5));
            JPanel inputs = new JPanel(new GridLayout(3,1,5,5));
            JTextField firstName = new JTextField();
            JTextField lastName = new JTextField();
            JTextField id = new JTextField();

            inputs.add(firstName);
            inputs.add(lastName);
            inputs.add(id);
            labels.add(new JLabel("First Name", SwingConstants.RIGHT));
            labels.add(new JLabel("Last Name", SwingConstants.RIGHT));
            labels.add(new JLabel("Student ID", SwingConstants.RIGHT));

            dialog.setPreferredSize(new Dimension(250, 100));
            dialog.add(inputs, BorderLayout.CENTER);
            dialog.add(labels, BorderLayout.WEST);
            int confirm = JOptionPane.showConfirmDialog(parent, dialog, "Add new student", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION)
            {
                if (firstName.getText().equals("") || lastName.getText().equals("") || id.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(parent, "Could not add student. Field left blank.", "Error adding student", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    mc.addStudent(new Student(firstName.getText(), lastName.getText(), id.getText()));
                    updateStudentList();
                }
            }
        });

        delStudent.addActionListener(ae -> {
            for (Student s : studentList.getSelectedValuesList()) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete student " + s.getName() + "?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    mc.deleteStudent(s);
                }
            }
            parent.updateData();
        });

        studentList.addListSelectionListener(lse -> {
            if (!lse.getValueIsAdjusting()) {
                List<StudentExam> studentExams = new ArrayList<>();
                for (Student s : studentList.getSelectedValuesList()) {
                    for (StudentExam se : s.getExams()) {
                        studentExams.add(se);
                    }
                }
                resultsView.updateView(studentExams);
            }
        });

        ActionListener searchAction = e -> {
            String searchTerm = searchField.getText();
            if (!searchTerm.equals("")) {
                studentListModel.clear();
                for (Student s : mc.getStudents()) {
                    if (s.getName().contains(searchTerm) || s.getId().contains(searchTerm)) {
                        studentListModel.addElement(s);
                    }
                }
            }
            else { updateStudentList(); }
        };

        search.addActionListener(searchAction);
        searchField.addActionListener(searchAction);

        clear.addActionListener(e -> {
            searchField.setText("");
            updateStudentList();
        });


    }
}

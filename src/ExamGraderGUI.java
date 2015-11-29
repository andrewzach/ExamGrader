// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import javax.swing.*;
import java.awt.*;

public class ExamGraderGUI extends JFrame
{
    public static Font monospaced = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    public static Font monospacedBold = new Font(Font.MONOSPACED, Font.BOLD, 16);

    protected MainController mc;
    private JTabbedPane tabbedPane;
    private ExamsPanel examsPanel;
    private StudentsPanel studentsPanel;

    public ExamGraderGUI()
    {
        mc = new MainController();

        mc.loadSavedState();
        //loadTestData(); // Loads sample data. Comment out when done testing.

        tabbedPane = new JTabbedPane();
        examsPanel = new ExamsPanel(this, mc);
        studentsPanel = new StudentsPanel(this, mc);
        tabbedPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        tabbedPane.addTab("Exams", examsPanel);
        tabbedPane.addTab("Students", studentsPanel);

        setLayout(new BorderLayout(3, 3));

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args)
    {
        ExamGraderGUI gui = new ExamGraderGUI();
        gui.setSize(900, 650);
        gui.setVisible(true);
        gui.setTitle("Exam Grader");
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Runtime.getRuntime().addShutdownHook(new Thread(gui.mc::saveState, "ExamGraderSaveState"));
    }

    // Update JList data on both tabs
    public void updateData()
    {
        examsPanel.updateExamsList();
        studentsPanel.updateStudentList();
    }

    public void loadTestData()
    {
        // Fill in some data to test GUI with
        Exam testExam = new Exam("Exam 1");
        mc.addExam(testExam);
        testExam.addStudentAnswers(mc.loadStudentExams("D:\\Input\\StudentAnswers1.dat", testExam));
        ExamKey key = mc.loadExamKey("D:\\Input\\CorrectAnswers1.dat", testExam.getName());
        testExam.addKey(key);
        testExam.markAllStudentAnswers();

        Exam textExam2 = new Exam("Exam 2");
        mc.addExam(textExam2);
        textExam2.addStudentAnswers(mc.loadStudentExams("D:\\Input\\StudentAnswers2.dat", textExam2));
        ExamKey key2 = mc.loadExamKey("D:\\Input\\CorrectAnswers2.dat", textExam2.getName());
        textExam2.addKey(key2);
        textExam2.markAllStudentAnswers();
    }
}

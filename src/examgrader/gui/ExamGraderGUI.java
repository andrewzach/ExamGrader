// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.gui;

import javax.swing.*;
import java.awt.*;

import examgrader.controllers.MainController;
import examgrader.model.*;

/**
 * Main entry point for the ExamGrader app.
 * Extends JFrame and creates the main window for the application.
 * Main interface is contained in a JTabbedPain, with two tabs: ExamsPanel and StudentsPanel
 */
public class ExamGraderGUI extends JFrame
{
    public static Font monospaced = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    public static Font monospacedBold = new Font(Font.MONOSPACED, Font.BOLD, 16);

    protected MainController mc;
    private JTabbedPane tabbedPane;
    private ExamsPanel examsPanel;
    private StudentsPanel studentsPanel;

    /**
     * Initializes ExamGrader's GUI. Creates a MainController, the JTabbedPane, and all panels.
     */
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

    /**
     * Main program entry point. Creates the GUI and sets the window size and name.
     * Also adds a ShutdownHook to save program data on exit.
     * @param args not currently used
     */
    public static void main(String[] args)
    {
        ExamGraderGUI gui = new ExamGraderGUI();
        gui.setSize(900, 650);
        gui.setVisible(true);
        gui.setTitle("Exam Grader");
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Add a new thread that saves data on exit.
        Runtime.getRuntime().addShutdownHook(new Thread(gui.mc::saveState, "ExamGraderSaveState"));
    }

    /** Updates the data in the JLists on both the ExamsPanel and the StudentsPanel. */
    public void updateData()
    {
        examsPanel.updateExamsList();
        studentsPanel.updateStudentList();
    }

    /** Loads in some sample data from pre-defined sample files. Used to test program features. Not used in release */
    public void loadTestData()
    {
        // Fill in some data to test GUI with
        String inputDirectory = System.getProperty("user.dir") + "\\sample-input\\";
        Exam testExam = new Exam("Exam 1");
        mc.addExam(testExam);
        testExam.addStudentAnswers(mc.loadStudentExams(inputDirectory + "StudentAnswers1.dat", testExam));
        ExamKey key = mc.loadExamKey(inputDirectory + "CorrectAnswers1.dat", testExam.getName());
        testExam.addKey(key);
        testExam.markAllStudentAnswers();

        Exam textExam2 = new Exam("Exam 2");
        mc.addExam(textExam2);
        textExam2.addStudentAnswers(mc.loadStudentExams(inputDirectory + "StudentAnswers2.dat", textExam2));
        ExamKey key2 = mc.loadExamKey(inputDirectory + "CorrectAnswers2.dat", textExam2.getName());
        textExam2.addKey(key2);
        textExam2.markAllStudentAnswers();
    }
}

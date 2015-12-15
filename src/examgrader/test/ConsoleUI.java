package examgrader.test;// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import examgrader.controllers.MainController;
import examgrader.model.Exam;
import examgrader.model.ExamKey;
import examgrader.model.StudentExam;

import java.util.List;

/** Used to test model before developing GUI. No longer used. */
class ConsoleUI
{
    private static MainController mc;

    public static void main(String[] args)
    {
        mc = new MainController();
        String inputDirectory = System.getProperty("user.dir") + "\\sample-input\\";
        Exam testExam = new Exam("Test exam");
        mc.addExam(testExam);
        testExam.addStudentAnswers(mc.loadStudentExams(inputDirectory + "StudentAnswers1.dat", testExam));
        ExamKey key = mc.loadExamKey(inputDirectory + "CorrectAnswers1.dat", testExam.getName());
        testExam.addKey(key);
        testExam.markAllStudentAnswers();
        List<StudentExam> studentAnswers = testExam.getStudentAnswers();
        for (StudentExam e : studentAnswers)
        {
            System.out.println(e);
        }
    }
}

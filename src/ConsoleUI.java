// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import java.util.List;

// Used to test model before developing GUI. No longer used.
class ConsoleUI
{
    private static MainController mc;

    public static void main(String[] args)
    {
        mc = new MainController();
        Exam testExam = new Exam("Test exam");
        mc.addExam(testExam);
        testExam.addStudentAnswers(mc.loadStudentExams("D:\\Input\\StudentAnswers2.dat", testExam));
        ExamKey key = mc.loadExamKey("D:\\Input\\CorrectAnswers.dat", testExam.getName());
        testExam.addKey(key);
        testExam.markAllStudentAnswers();
        List<StudentExam> studentAnswers = testExam.getStudentAnswers();
        for (StudentExam e : studentAnswers)
        {
            System.out.println(e.getStudent().getId() + "\t" + e.getStudent().getName() + "\tMark: " + e.getMark());
        }
    }
}

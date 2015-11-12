import java.util.ArrayList;

/**
 * Created by Andrew on 11/9/2015.
 */
public class ConsoleUI
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
        testExam.markStudentAnswers();
        ArrayList<StudentExam> studentAnswers = testExam.getStudentAnswers();
        for (StudentExam e : studentAnswers)
        {
            System.out.println(e.getStudent().getId() + "\t" + e.getStudent().getName() + "\tMark: " + e.getMark());
        }
    }
}

/**
 * Created by Andrew on 11/9/2015.
 */
public class StudentExam
{
    private Exam exam;
    private int[] answers;
    private Student student;
    private double mark;

    public StudentExam(Exam exam, int[] answers, Student student)
    {
        this.exam = exam;
        this.answers = answers;
        this.student = student;
    }

    public int[] getAnswers() {
        return answers;
    }

    public Exam getExam() {
        return exam;
    }

    public Student getStudent() {
        return student;
    }

    public double getMark()
    {
        return mark;
    }

    public void setMark(double mark)
    {
        this.mark = mark;
    }

}

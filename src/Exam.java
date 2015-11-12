/**
 * Created by Andrew on 11/9/2015.
 */

import java.util.*;

public class Exam
{
    private String name;
    private double penalty;
    private ExamKey key;
    private ArrayList<StudentExam> studentAnswers;

    public Exam(String name, double penalty)
    {
        this.name = name;
        this.penalty = penalty;
        studentAnswers = new ArrayList<StudentExam>();
    }

    public Exam(String name)
    {
        this(name, 0.25);
    }

    public Exam(String name, ExamKey key)
    {
        this(name);
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void addStudentAnswers(ArrayList<StudentExam> answers)
    {
        studentAnswers.addAll(answers);
    }

    public ArrayList<StudentExam> getStudentAnswers()
    {
        return studentAnswers;
    }

    public ExamKey getKey()
    {
        return key;
    }

    public double getPenalty()
    {
        return penalty;
    }

    public void addKey(ExamKey key)
    {
        this.key = key;
    }

    public void markStudentAnswers()
    {
        for (StudentExam s : studentAnswers)
        {
            double mark = calcGrade(s);
            s.setMark(mark);
        }
    }

    private double calcGrade(StudentExam s)
    {
        int[] sAnswers = s.getAnswers();
        int[] kAnswers = key.getAnswers();
        double grade = 0;
        if (sAnswers.length != kAnswers.length)
        {
            return -1; // Exam lengths don't match
        }
        else
        {
            for (int i = 0; i<sAnswers.length; i++)
            {
                if (sAnswers[i] == kAnswers[i]) // correct answer
                {
                    grade += 1;
                }
                else if (sAnswers[i] != 0)  // incorrect answer, not blank
                {
                    grade -= penalty;
                }
            }
        }
        return grade;
    }
}

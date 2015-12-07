// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.model;

import java.util.*;
import java.io.*;

public class Exam implements Serializable
{
    private String name;
    private double penalty;
    private ExamKey key;
    private List<StudentExam> studentAnswers;

    public Exam(String name, double penalty)
    {
        this.name = name;
        this.penalty = penalty;
        studentAnswers = new ArrayList<>();
    }

    public Exam(String name)
    {
        // Default penalty for incorrect answers is -0.25 points
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

    public void addStudentAnswers(List<StudentExam> answers)
    {
        // Check to make sure StudentExams aren't already in studentAnswers before adding them.
        for (StudentExam se : answers)
        {
            if (!studentAnswers.contains(se)) {
                studentAnswers.add(se);
            }
        }
    }

    public List<StudentExam> getStudentAnswers()
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

    public void removeStudent(Student s)
    {
        // Use list iterator to safely remove StudentExams belonging to examgrader.model.Student s.
        for (Iterator<StudentExam> iterator = studentAnswers.iterator(); iterator.hasNext();)
        {
            StudentExam se = iterator.next();
            if (se.getStudent() == s)
            {
                iterator.remove();
            }
        }
    }

    public int length()
    {
        if (key != null) {
            return key.getAnswers().length;
        }
        else
        {
            return 0;
        }
    }

    // Calculate and store mark for all StudentExams
    public void markAllStudentAnswers()
    {
        if (key != null)
        {
            for (StudentExam s : studentAnswers) {
                double mark = calcGrade(s);
                s.setMark(mark);
            }
        }
    }

    // Calculate and store mark for selected StudentExams
    public void markStudentAnswers(List<StudentExam> newAnswers)
    {
        if (key != null)
        {
            for (StudentExam s : newAnswers) {
                double mark = calcGrade(s);
                s.setMark(mark);
            }
        }
    }

    // Calculates grade for examgrader.model.StudentExam. Gives 1 point for correct answers, subtracts penalty for incorrect
    // No change for blank answers. Final grade is rounded up to nearest whole number.
    // Return -1 if error marking exam.
    private double calcGrade(StudentExam s)
    {
        int[] sAnswers = s.getAnswers();
        int[] kAnswers = key.getAnswers();
        double grade = 0;
        if (sAnswers.length != kAnswers.length)
        {
            return StudentExam.MARKING_ERROR; // examgrader.model.Exam lengths don't match
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
            if (grade < 0) { grade = 0; } // Don't allow negative grades
        }
        return Math.ceil(grade);
    }

    public String toString()
    {
        return String.format("%-15s %-10d %-10d", name, length(), studentAnswers.size());
    }
}

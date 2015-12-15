// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.model;

import java.util.*;
import java.io.*;

/**
 * Represents a multiple choice exam given to students.
 * Each Exam has a name, a penalty amount for incorrect answers, and an ExamKey with all the correct answers.
 * Also stores a list of StudentExams, one for each Student who has taken the exam.
 */
public class Exam implements Serializable
{
    private String name;
    private double penalty;
    private ExamKey key;
    private List<StudentExam> studentAnswers;

    /**
     * Creates an exam with specified name and penalty
     * @param name Name of the exam
     * @param penalty Amount deducted for incorrect answers
     */
    public Exam(String name, double penalty)
    {
        this.name = name;
        this.penalty = penalty;
        studentAnswers = new ArrayList<>();
    }

    /**
     * Create an exam with specified name and default penalty of 0.25
     * @param name Name of the exam
     */
    public Exam(String name)
    {
        // Default penalty for incorrect answers is 0.25 points
        this(name, 0.25);
    }

    /**
     * Create an exam with specified name and key, with default penalty of 0.25
     * @param name Name of the exam
     * @param key ExamKey with correct answers
     */
    public Exam(String name, ExamKey key)
    {
        this(name);
        this.key = key;
    }

    /** return name of the exam */
    public String getName()
    {
        return name;
    }

    /**
     * Adds a new list of StudentExam answers to the Exam.
     * Checks each StudentExam before adding to make sure they aren't in the list to avoid duplicates.
     * @param answers A List of StudentExams to add.
     */
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

    /** @return the list of StudentExams, one for each Student who has taken the exam */
    public List<StudentExam> getStudentAnswers()
    {
        return studentAnswers;
    }

    /** @return the ExamKey with the correct answers to the Exam */
    public ExamKey getKey()
    {
        return key;
    }

    /** @return the penalty for each incorrect, non-blank answer. */
    public double getPenalty()
    {
        return penalty;
    }

    /**
     * Adds a new ExamKey to the Exam
     * @param key ExamKey with correct answers to be added
     */
    public void addKey(ExamKey key)
    {
        this.key = key;
    }

    /**
     * Removes all StudentExams belonging to the specified student. Usually called when deleting a student
     * @param s Student whose answers need to be deleted
     */
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

    /**
     * Returns the number of questions in the exam. Also represents the maximum score possible.
     * @return the number of questions in the exam, or 0 if no key has been added yet.
     */
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

    /** Calculates and stores mark for all StudentExams */
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

    /** Calculate and store mark for selected StudentExams. Called when adding new answers to an exiting exam. */
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

    /**
     * Calculates the grade for the specified StudentExam.
     * Gives 1 point for each correct answers. Subtracts penalty for each incorrect, non-blank answer.
     * Grade is rounded up to nearest whole number.
     * @param s StudentExam to be marked
     * @return calculated grade for the exam, between 0 and the exam's length.
     */
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

    /**
     * Returns a string representing the exam, formatted for output in a JList.
     * Format is %-15s %-10d %-10d for Name, Length, and number of students who have taken the exam.
     * @return formatted string representing the exam.
     */
    public String toString()
    {
        return String.format("%-15s %-10d %-10d", name, length(), studentAnswers.size());
    }
}

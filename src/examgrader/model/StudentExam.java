// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.model;

import java.io.Serializable;

/**
 * Represents a student's answers to an exam. Stores a reference to the appropriate Student and Exam objects.
 * Also stores the student's mark for the exam.
 */
public class StudentExam implements Serializable
{

    private Exam exam;
    private int[] answers;
    private Student student;
    private double mark;

    /** Constant value for exam's mark representing an exam that has not been marked yet */
    public static final int NOT_MARKED = -2;
    /** Constant value for exam's mark representing an error has occurred when marking the exam */
    public static final int MARKING_ERROR = -1;

    /**
     *
     * @param exam The Exam object associated with this StudentExam
     * @param answers int array of student's answers to the exam
     * @param student Student object associated with this StudentExam
     */
    public StudentExam(Exam exam, int[] answers, Student student)
    {
        this.exam = exam;
        this.answers = answers;
        this.student = student;
        this.mark = NOT_MARKED; // NOT_MARKED if not marked yet
    }

    /**
     * Returns the array of answers the student gave for the exam. Blank answers represented as 0.
     * @return an int array representing all answers given by the student for the exam.
     */
    public int[] getAnswers() {
        return answers;
    }

    public Exam getExam() {
        return exam;
    }

    /**
     * Returns the Student object associated with this exam
     * @return the Student object associated with this exam
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the exam mark as a double
     * @return exam mark
     */
    public double getMark()
    {
        return mark;
    }

    /**
     * Sets mark to the value specified
     * @param mark New mark value
     */
    public void setMark(double mark)
    {
        this.mark = mark;
    }

    /**
     * Calculates the percentage score on an exam (out of 100) and returns it
     * @return an int representing the percentage score of an exam
     */
    public int getPercentage()
    {
        if (mark == NOT_MARKED) { return NOT_MARKED; }    // return NOT_MARKED if not marked yet
        else if (mark == MARKING_ERROR) { return MARKING_ERROR; }     // return MARKING_ERROR if error in marking exam
        else { return (int)Math.round((mark/exam.length())*100); }
    }

    /**
     * Returns a formatted string designed for output in a JList. Format is %-15s %5s    %5d    %3d.
     * @return a formatted string designed for output in a JList.
     */
    public String toString()
    {
        String studentExamString;
        if (mark == NOT_MARKED)
        {
            studentExamString = student.toString() + " " + String.format("%-15s %5s    %5d    %3s", exam.getName(), "", exam.length(), "");
        }
        else if (mark == MARKING_ERROR)
        {
            studentExamString = student.toString() + " " + String.format("%-15s %5s    %5d    %3s", exam.getName(), "Error", exam.length(), "");
        }
        else
        {
            studentExamString = student.toString() + " " + String.format("%-15s %5.0f    %5d    %3d", exam.getName(), mark, exam.length(), getPercentage());
        }
        return studentExamString;
    }

    /**
     * Tests for equality. Implemented for comparison in List contains() method to prevent duplicates.
     * @param other Object to compare to
     * @return True if both are StudentExams with the same student name and exam name, false otherwise.
     */
    public boolean equals(Object other)
    {
        boolean equals = false;
        if (other != null && other instanceof StudentExam)
        {
            StudentExam otherExam = (StudentExam)other;
            // If student name and exam name are both equal, the two StudentExams are equal
            equals = (getStudent().getName().equals(otherExam.getStudent().getName()) && exam.getName().equals(otherExam.getExam().getName()));
        }
        return equals;
    }

    /**
     * Returns a string representation of StudentExam for file output.
     * @return A CSV string designed for file output.
     */
    public String toFileString()
    {
        return (exam.getName() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getId() + ",") + mark + "," + answers.length;
    }

}

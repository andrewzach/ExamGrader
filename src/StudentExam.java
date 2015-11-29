// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import java.io.Serializable;

public class StudentExam implements Serializable
{
    private Exam exam;
    private int[] answers;
    private Student student;
    private double mark;

    // constants used to check for marking errors and if an exam hasn't been marked yet
    public static final int NOT_MARKED = -2;
    public static final int MARKING_ERROR = -1;

    public StudentExam(Exam exam, int[] answers, Student student)
    {
        this.exam = exam;
        this.answers = answers;
        this.student = student;
        this.mark = NOT_MARKED; // NOT_MARKED if not marked yet
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

    public int getPercentage()
    {
        if (mark == NOT_MARKED) { return NOT_MARKED; }    // return NOT_MARKED if not marked yet
        else if (mark == MARKING_ERROR) { return MARKING_ERROR; }     // return MARKING_ERROR if error in marking exam
        else { return (int)Math.round((mark/exam.length())*100); }
    }
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

    // Tests for equality. Implemented for comparison in List contains() method to prevent duplicates.
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

// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.model;

import java.util.*;
import java.io.*;

/**
 * Represents a student and extends the abstract class Person.
 * Each student has a first name, last name, student ID, and a list of StudentExams for every exam they have taken.
 */
public class Student extends Person implements Comparable<Student>, Serializable
{
    private String firstName;
    private String lastName;
    private String id;
    private List<StudentExam> exams;

    /**
     * Creates a Student object with the information specified.
     * @param firstName student's first name
     * @param lastName student's last name
     * @param id student id
     */
    public Student(String firstName, String lastName, String id)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        exams = new ArrayList<>();
    }

    /**
     * Returns the student's first name as string
     * @return first name of the student
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the student's last name as string
     * @return last name of the student
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the student's full name, first + last
     * @return full name of the student
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the student's ID as string
     * @return student id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the list of StudentExams
     * @return a list of StudentExams
     */
    public List<StudentExam> getExams()
    {
        return exams;
    }

    // Get exam by name, return null if not found

    /**
     * Get a StudentExam by name. Returns null if not found.
     * @param examName Exam Name to search by
     * @return StudentExam matching examName. Null if not found.
     */
    public StudentExam getExam(String examName)
    {
        for (StudentExam exam : exams)
        {
            if (exam.getExam().getName().equals(examName))
            {
                return exam;
            }
        }
        return null;
    }

    /**
     * Add a new StudentExam to the student's list of exams.
     * @param se new StudentExam to add
     */
    public void addExam(StudentExam se)
    {
        if (!exams.contains(se))
        {
            exams.add(se);
        }
    }

    /**
     * Delete StudentExam from the student's exam list that matches the specified Exam
     * @param e Exam to be removed
     */
    public void deleteExam(Exam e)
    {
        // Use list iterator to safely remove StudentExams with Exam e.
        for (Iterator<StudentExam> iterator = exams.iterator(); iterator.hasNext();)
        {
            StudentExam se = iterator.next();
            if (se.getExam() == e)
            {
                iterator.remove();
            }
        }
    }

    /**
     * Checks for equality by student ID
     * @param other other Student to compare to
     * @return true if other is a Student object with the same id
     */
    public boolean equals(Object other)
    {
        if (other != null && other instanceof Student)
        {
            Student otherStudent = (Student)other;
            return (id.equals(otherStudent.getId()));
        }
        else { return false; }
    }

    /**
     * Returns a formatted string designed for output in a JList. Format is %-13s %-15s %-12s firstname, lastname, id.
     * @return a formatted string for output in a JList
     */
    public String toString()
    {
        return String.format("%-13s %-15s %-12s", firstName, lastName, id);
    }

    /**
     * Compares students by last name.
     * @param other other Student to compare to
     * @return String.compareTo() on last name of each Student.
     */
    public int compareTo(Student other)
    {
        return lastName.compareTo(other.getLastName());
    }
}



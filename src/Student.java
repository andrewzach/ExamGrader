// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015

import java.util.*;
import java.io.*;

public class Student extends Person implements Comparable<Student>, Serializable
{
    private String firstName;
    private String lastName;
    private String id;
    private List<StudentExam> exams;

    public Student(String firstName, String lastName, String id)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        exams = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getId() {
        return id;
    }

    public List<StudentExam> getExams()
    {
        return exams;
    }

    // Get exam by name, return null if not found
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

    public void addExam(StudentExam se)
    {
        if (!exams.contains(se))
        {
            exams.add(se);
        }
    }

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

    // Check for equality by ID
    public boolean equals(Object other)
    {
        if (other != null && other instanceof Student)
        {
            Student otherStudent = (Student)other;
            return (id.equals(otherStudent.getId()));
        }
        else { return false; }
    }

    public String toString()
    {
        return String.format("%-13s %-15s %-12s", firstName, lastName, id);
    }

    public int compareTo(Student other)
    {
        return lastName.compareTo(other.getLastName());
    }
}



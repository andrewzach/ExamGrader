/**
 * Created by Andrew on 11/9/2015.
 */

import java.util.*;

public class Student
{
    private String firstName;
    private String lastName;
    private String id;
    private ArrayList<StudentExam> exams;

    public Student(String firstName, String lastName, String id)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
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

    public StudentExam getExam(String examName)
    {
        for (StudentExam exam : exams)
        {
            if (exam.getExam().getName() == examName)
            {
                return exam;
            }
        }
        return null;
    }

    public boolean equals(Student other)
    {
        if (id == other.getId()) { return true; }
        else { return false; }
    }
}



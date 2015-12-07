// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.controllers;

import examgrader.model.*;

import java.io.*;
import java.util.*;

/**
 * Controls all interaction between ExamGrader's gui and model.
 * Stores all data as a List of Students and List of Exams and contains methods for interacting with them.
 * Also contains methods for file input and output.
 */
public class MainController
{
    private List<Student> students;
    private List<Exam> exams;
    private String saveData;

    public MainController()
    {
        students = new ArrayList<>();
        exams = new ArrayList<>();
        saveData = System.getProperty("user.dir") + "\\save-data\\savestate.ser";
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Exam> getExams() {
        return exams;
    }

    // Get Student by id. Return null if not found
    public Student getStudent(String id)
    {
        for (Student s : students)
        {
            if (s.getId().equals(id))
            {
                return s;
            }
        }
        return null;
    }

    // Get Exam by name. Return null if not found
    public Exam getExam(String name)
    {
        for (Exam e : exams)
        {
            if (e.getName().equals(name))
            {
                return e;
            }
        }
        return null;
    }

    public void addStudent(Student s)
    {
        if (!students.contains(s))
        {
            students.add(s);
        }
    }

    public void deleteStudent(Student s)
    {
        for (Exam exam : exams) // Remove student's exam answers from all exams
        {
            exam.removeStudent(s);
        }
        students.remove(s);
    }

    public void addExam(Exam e)
    {
        exams.add(e);
    }

    public void deleteExam(Exam e)
    {
        for (Student s : students)
        {
            s.deleteExam(e);
        }
        exams.remove(e);
    }

    // FILE INPUT/OUTPUT

    // Read in StudentExams from a file, return them as a List
    public List<StudentExam> loadStudentExams(String filename, Exam exam)
    {
        List<StudentExam> studentAnswers = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            while ((line = br.readLine()) != null)
            {
                String[] studentExamInfo = line.split(",");
                String id = studentExamInfo[0];
                String lastName = studentExamInfo[1];
                String firstName = studentExamInfo[2];
                String[] answersString = Arrays.copyOfRange(studentExamInfo, 3, studentExamInfo.length);
                // Convert answers to array of ints
                int[] answers = Arrays.stream(answersString).mapToInt(Integer::parseInt).toArray();
                Student student = getStudent(id);
                if (student == null)    // if student does not exist yet, create new one and add them to the students list.
                {
                    student = new Student(firstName, lastName, id);
                    addStudent(student);
                }
                StudentExam se = new StudentExam(exam, answers, student);
                student.addExam(se);
                studentAnswers.add(se);
            }
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find Student Answers file.", e);
        }
        catch (IOException e){
            throw new IllegalArgumentException("IO exception while reading Student Answers file", e);
        }
        return studentAnswers;
    }

    public void writeResultsFile(String filename, List<StudentExam> results)
    {
        try
        {
            File file = new File(filename);
            if (!file.exists())
            {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            writer.write("Exam Name, First, Last, Id, Score, Questions");   // Headers
            writer.newLine();
            for (StudentExam se : results)
            {
                writer.write(se.toFileString());
                writer.newLine();
            }
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    // Read ExamKey from a file
    public ExamKey loadExamKey(String filename, String examName)
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line = br.readLine();
            String[] answersString = line.split(",");
            int[] answers = Arrays.stream(answersString).mapToInt(Integer::parseInt).toArray();
            return new ExamKey(examName, answers);
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find Exam Key file.", e);
        }
        catch (IOException e){
            throw new IllegalArgumentException("IO exception while reading Exam Key file", e);
        }
    }

    public void clearAllData()
    {
        exams = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void saveState()
    {
        try
        {
            File saveFile = new File(saveData);
            if(!saveFile.exists()) {
                saveFile.getParentFile().mkdirs(); // create necessary intermediate directories
                saveFile.createNewFile();
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile));
            out.writeObject(students);
            out.writeObject(exams);
            out.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadSavedState()
    {
        try
        {
            File saveFile = new File(saveData);
            if (saveFile.exists())
            {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveData));
                students = (List<Student>) in.readObject();
                exams = (List<Exam>) in.readObject();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}

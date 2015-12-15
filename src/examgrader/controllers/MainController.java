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

    /** Initializes the list of Students and list of Exams. Also detects directory for existing save data. */
    public MainController()
    {
        students = new ArrayList<>();
        exams = new ArrayList<>();
        saveData = System.getProperty("user.dir") + "\\save-data\\savestate.ser";
    }

    /** @return a list of all Students stored */
    public List<Student> getStudents() {
        return students;
    }

    /** @return a list of all Exams stored */
    public List<Exam> getExams() {
        return exams;
    }

    // Get Student by id. Return null if not found

    /**
     * Returns a student matching the specified student ID. Returns null if not found.
     * @param id Student id to match
     * @return a student matching the specified student ID, null if not found.
     */
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

    /**
     * Returns an Exam matching the specified Exam name. Returns null if not found.
     * @param name Exam name to match
     * @return an Exam matching the specified Exam name, null if not found.
     */
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

    /** Adds a new Student to the controller's Student list. Checks to see if student already is in the list and does not add if so */
    public void addStudent(Student s)
    {
        if (!students.contains(s))
        {
            students.add(s);
        }
    }

    /**
     * Removes a Student from the program, including any data associated with them.
     * Deletes their answers to all Exams and then removes them from the controller's list of Students.
      * @param s Student to be removed
     */
    public void deleteStudent(Student s)
    {
        for (Exam exam : exams) // Remove student's exam answers from all exams
        {
            exam.removeStudent(s);
        }
        students.remove(s);
    }

    /** @param e New Exam to be added to the controller's list of exams */
    public void addExam(Exam e)
    {
        exams.add(e);
    }

    /**
     * Removes an Exam from the program, including any data associated with it.
     * Calls the deleteExam(e) method on all Students to remove the exam from each one of them as well.
     * @param e Exam to be deleted
     */
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

    /**
     * Reads in StudentExams from a file and returns them as a List.
     * Creates new Student objects for those that don't exist in the program yet and adds them to the controller's list of Students.
     * @param filename Absolute path + filename for the file containing the StudentExams to be added
     * @param exam Exam associated with the new StudentExams
     * @throws IllegalArgumentException if file can't be found or an error occurs when parsing it
     * @return List of StudentExams created from the given file
     */
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

    /**
     * Writes a list of StudentExams to a file. File is formatted as a comma-separated values (CSV) file.
     * Format is: Exam Name, First, Last, Id, Score, Questions
     * Overwrites file if it already exists.
     * @param filename Absolute path + filename of the file to be created.
     * @param results List of StudentExams to write to the file.
     */
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
    /**
     * Reads an ExamKey in from the specified file.
     * Input file is formatted as a list of answers, separated by commas.
     * @param filename Absolute path + filename of the file to be read
     * @param examName Name of the exam associated with the ExamKey
     * @return a new ExamKey object from the file.
     * @throws IllegalArgumentException if file can't be found or an error occurs when parsing it
     */
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

    /** Clears all data stored in the program. Creates new blank lists for Exams and Students */
    public void clearAllData()
    {
        exams = new ArrayList<>();
        students = new ArrayList<>();
    }

    /**
     * Saves all program data to a file (savestate.ser) in the save-data folder of the program's main directory.
     * The controller's list of Students and list of Exams are serialized and written to a binary file that can be loaded later.
     */
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

    /**
     * Checks for save data in the file save-data\savestate.ser and loads it if it exists.
     * Loads a list of Exams and a list of Students for the controller.
     */
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

/**
 * Created by Andrew on 11/9/2015.
 */
import java.io.*;
import java.util.*;

public class MainController
{
    private ArrayList<Student> students;
    private ArrayList<Exam> exams;

    public MainController()
    {
        students = new ArrayList<Student>();
        exams = new ArrayList<Exam>();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

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
        students.add(s);
    }

    public void addExam(Exam e)
    {
        exams.add(e);
    }

    public ArrayList<StudentExam> loadStudentExams(String filename, Exam exam)
    {
        ArrayList<StudentExam> studentAnswers = new ArrayList<StudentExam>();
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
                int[] answers = Arrays.stream(answersString).mapToInt(Integer::parseInt).toArray();
                Student student = getStudent(id);
                if (student == null)    // if student does not exist yet, create new one and add them to the students list.
                {
                    student = new Student(firstName, lastName, id);
                    addStudent(student);
                }
                studentAnswers.add(new StudentExam(exam, answers, student));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("I hate you Java.");
        }
        catch (IOException e){
            System.out.println("handled");
        }
        return studentAnswers;
    }

    public ExamKey loadExamKey(String filename, String examName)
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line = br.readLine();
            String[] answersString = line.split(",");
            int[] answers = Arrays.stream(answersString).mapToInt(Integer::parseInt).toArray();
            ExamKey key = new ExamKey(examName, answers);
            return key;
        }
        catch (FileNotFoundException e) {
            System.out.println("I hate you Java.");
            return null;
        }
        catch (IOException e){
            System.out.println("handled");
            return null;
        }
    }


}

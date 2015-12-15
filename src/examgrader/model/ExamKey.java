// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.model;

import java.io.Serializable;

/**
 * Represents an answer key to an exam.
 * Correct answers are stored as an int array.
 * The name of the Exam the key belongs to is also stored.
 */
public class ExamKey implements Serializable
{
    private String examName;
    private int[] answers;

    /**
     * Creates an ExamKey with the specified parameters
     * @param examName Name of the Exam the answer key belongs to
     * @param answers int array of correct answers to the exam
     */
    public ExamKey(String examName, int[] answers)
    {
        this.examName = examName;
        this.answers = answers;
    }

    /** @return the name of the Exam they key belongs to*/
    public String getExamName() {
        return examName;
    }

    /** @return an int array of correct answers */
    public int[] getAnswers() {
        return answers;
    }
}

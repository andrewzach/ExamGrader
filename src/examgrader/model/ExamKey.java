// DT265 - OOSD2 Java Project
// By Andrew Zacharias - D14127051
// 23 / 11 / 2015
package examgrader.model;

import java.io.Serializable;

public class ExamKey implements Serializable
{
    private String examName;
    private int[] answers;

    public ExamKey(String examName, int[] answers)
    {
        this.examName = examName;
        this.answers = answers;
    }

    public String getExamName() {
        return examName;
    }

    public int[] getAnswers() {
        return answers;
    }
}

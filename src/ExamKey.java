import java.io.Serializable;

/**
 * Created by Andrew on 11/9/2015.
 */
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

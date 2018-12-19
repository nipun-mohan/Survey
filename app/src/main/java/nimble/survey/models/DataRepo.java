package nimble.survey.models;

import java.util.List;

/**
 * Created by Nipun on 12/12/2018.
 */


/**
 * Responsible for holds the data to be rendered on the screen
 */
public class DataRepo {
    private static DataRepo dataRepo;

    public static DataRepo newInstance() {
        if (dataRepo == null) {
            dataRepo = new DataRepo();
        }
        return dataRepo;
    }

    /**
     * Surveys List
     */
    private List<Survey> surveys;

    public void setSurveys(List<Survey> surveys) {
        this.surveys = surveys;
    }

    public List<Survey> getSurveys() {
        return surveys;
    }


    /**
     * Question List
     */
    private List<Questions> questions;

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    public List<Questions> getQuestions() {
        return questions;
    }


    /**
     * Answers List
     */
    private List<List<Answers>> answers;

    public void setAnswers(List<List<Answers>> answers) {
        this.answers = answers;
    }

    public List<List<Answers>> getAnswers() {
        return answers;
    }


}

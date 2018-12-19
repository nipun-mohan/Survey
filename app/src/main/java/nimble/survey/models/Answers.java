package nimble.survey.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


/**
 * Answers database
 */

public class Answers extends RealmObject {

    @SerializedName("id")
    private String id;
    @SerializedName("question_id")
    private String questionId;
    @SerializedName("short_text")
    private String answer;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

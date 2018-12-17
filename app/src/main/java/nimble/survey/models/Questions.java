package nimble.survey.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Questions extends RealmObject {


    private String surveyId;
    @SerializedName("id")
    private String questionId;
    @SerializedName("short_text")
    private String title;
    @SerializedName("text")
    private String description;
    @SerializedName("cover_image_url")
    private String coverImage;


    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}

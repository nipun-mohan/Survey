package nimble.survey;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import nimble.survey.models.Answers;
import nimble.survey.models.DataRepo;
import nimble.survey.models.Questions;

public class SurveyActivity extends AppCompatActivity {

    private LinearLayout answersLayout;
    private SimpleDraweeView coverImage;
    private TextView txtQUestion;
    private FloatingActionButton btnNext;
    private FloatingActionButton btnPrev;
    private String surveyId;
    List<Questions> questions;
    List<List<Answers>> answersList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        coverImage = findViewById(R.id.coverImage);
        txtQUestion = findViewById(R.id.txtQuestion);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnBack);
        answersLayout = findViewById(R.id.layoutAnswers);


        if (getIntent().hasExtra("surveyId")) {
            surveyId = getIntent().getStringExtra("surveyId");
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            List<Questions> questionsList = realm.where(Questions.class).equalTo("surveyId", surveyId).findAll();
            DataRepo.newInstance().setQuestions(realm.copyFromRealm(questionsList));

            List<List<Answers>> answersList = new ArrayList<>();
            for (int i = 0; i < questionsList.size(); i++) {
                List<Answers> answers = realm.where(Answers.class).equalTo("questionId", questionsList.get(i).getQuestionId()).findAll();
                answersList.add(realm.copyFromRealm(answers));
            }
            DataRepo.newInstance().setAnswers(answersList);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < questions.size() - 1)
                    i = ++i;
                updateView();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0)
                    i = --i;
                updateView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.questions = DataRepo.newInstance().getQuestions();
        this.answersList = DataRepo.newInstance().getAnswers();
        updateView();
    }

    int i = 0;

    private void updateView() {

        if (i > 0)
            btnPrev.show();
        else
            btnPrev.hide();
        if (i + 1 == questions.size())
            btnNext.hide();
        else
            btnNext.show();

        if (i < questions.size() && i >= 0) {
            txtQUestion.setText(i + 1 + ". " + questions.get(i).getDescription());
            if (questions.get(i).getCoverImage() != null && questions.get(i).getCoverImage().length() > 0 && !questions.get(i).getCoverImage().equalsIgnoreCase("NA")) {
                Uri uri = Uri.parse(questions.get(i).getCoverImage() + "l");
                Utils.setupImage(coverImage, uri, true);
            }

            /*List<Answers> answers = this.answersList.get(i);
            for (int j = 0; j < answers.size(); i++) {
                TextView tv = new TextView(this);
                tv.setText(answers.get(j).getAnswer());
                answersLayout.addView(tv);
            }*/
        }
    }
}

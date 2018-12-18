package nimble.survey.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

import nimble.survey.AppController;
import nimble.survey.MainActivity;
import nimble.survey.R;
import nimble.survey.Utils;
import nimble.survey.interfaces.APIInterface;
import nimble.survey.interfaces.SurveyFetchInterface;
import nimble.survey.models.Answers;
import nimble.survey.models.DataRepo;
import nimble.survey.models.Questions;
import nimble.survey.models.Survey;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Nipun on 12/12/2018.
 */

public class SurveyPresenter implements SurveyFetchInterface.Presenter {

    private SurveyFetchInterface.View mDownloadView;

    public SurveyPresenter(SurveyFetchInterface.View view) {
        mDownloadView = view;
    }

    @Override
    public void start() {
        ((MainActivity) mDownloadView).startSpin();
        FetchShows task = new FetchShows();
        task.execute();
    }

    public class FetchShows extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            Map<String, String> data = new HashMap<>();
            data.put("access_token", AppController.getInstance().getResources().getString(R.string.api_key));
            data.put("page", "1");
            data.put("per_page", "10");


            APIInterface apiService = Utils.getClient(AppController.getInstance().getResources().getString(R.string.base_url)).create(APIInterface.class);
            Call<JsonArray> call = apiService.getSurveys(data);


            call.enqueue(new Callback<JsonArray>() {

                @Override
                public void onResponse(Call<JsonArray> call, retrofit2.Response<JsonArray> response) {

                    Log.e("Response", response.toString());

                    try {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        if (jsonArray.length() > 0) {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.where(Survey.class).findAll().deleteAllFromRealm();
                                        realm.where(Questions.class).findAll().deleteAllFromRealm();
                                        realm.where(Answers.class).findAll().deleteAllFromRealm();
                                    }
                                });
                            }
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final Survey survey = Utils.getGsonObject().fromJson(jsonArray.getJSONObject(i).toString(), Survey.class);
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealm(survey);
                                    }
                                });
                            }


                            /***
                             * Add Question for a survey
                             */

                            JSONArray questionArray = jsonArray.getJSONObject(i).getJSONArray("questions");
                            for (int j = 0; j < questionArray.length(); j++) {
                                final Questions questions = Utils.getGsonObject().fromJson(questionArray.getJSONObject(j).toString(), Questions.class);
                                try (Realm realm = Realm.getDefaultInstance()) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            questions.setSurveyId(survey.getSurveyId());
                                            realm.copyToRealm(questions);
                                        }
                                    });
                                }

                            /**
                             * Add answers
                             */
                               /* JSONArray answersArray = questionArray.getJSONObject(j).getJSONArray("answers");
                                for (int a = 0; a < answersArray.length(); a++) {
                                    final Answers answers = Utils.getGsonObject().fromJson(answersArray.getJSONObject(a).toString(), Answers.class);
                                    try (Realm realm = Realm.getDefaultInstance()) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                realm.copyToRealm(answers);
                                            }
                                        });
                                    }
                                }*/
                            }
                        }

                        List<Survey> showsList;
                        try (Realm realm = Realm.getDefaultInstance()) {
                            showsList = realm.where(Survey.class).findAll();
                            if (showsList.size() > 0) {
                                DataRepo.newInstance().setSurveys(realm.copyFromRealm(showsList));
                                ((MainActivity) mDownloadView).initViewPager();
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ((MainActivity) mDownloadView).stopSpin();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                    Log.e("Error", t.getMessage());

                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }
    }
}

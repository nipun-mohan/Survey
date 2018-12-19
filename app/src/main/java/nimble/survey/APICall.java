package nimble.survey;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import nimble.survey.interfaces.APIInterface;
import nimble.survey.interfaces.SurveyFetchInterface;
import retrofit2.Call;
import retrofit2.Callback;

public class APICall {



    SurveyFetchInterface.Presenter presenter;

    /**
     * Start the api call to update access token
     * @param mPresenter
     */
    public void start(SurveyFetchInterface.Presenter mPresenter) {
        this.presenter = mPresenter;
        FetchAccessToken task = new FetchAccessToken();
        task.execute();
    }


    /**
     * Async Task for API Call - Update/Fetch Access Token
     */
    private class FetchAccessToken extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            Map<String, String> data = new HashMap<>();
            data.put("grant_type", AppController.getInstance().getResources().getString(R.string.grant_type));
            data.put("username", AppController.getInstance().getResources().getString(R.string.username));
            data.put("password", AppController.getInstance().getResources().getString(R.string.password));

            APIInterface apiService = Utils.getClient(AppController.getInstance().getResources().getString(R.string.base_url)).create(APIInterface.class);
            Call<JsonObject> call = apiService.getAccessToken(data);

            call.enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                    try {
                        JSONObject job = new JSONObject(response.body().toString());
                        Pref.setValue(Pref.TYPE.ACCESS_TOKEN.toString(), job.getString("access_token"));
                        Pref.setValue(Pref.TYPE.EXPIRY.toString(), job.getString("expires_in"));
                        Pref.setValue(Pref.TYPE.TOKEN_FETCH_TIME.toString(), String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("Response", response.body().toString());
                    presenter.start();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                    Toast.makeText(AppController.getInstance(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }
    }
}

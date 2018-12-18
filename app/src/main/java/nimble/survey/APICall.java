package nimble.survey;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import nimble.survey.interfaces.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;

public class APICall {


    public void start() {
        FetchAccessToken task = new FetchAccessToken();
        task.execute();
    }

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

                    Log.e("Response", response.body().toString());


                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

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

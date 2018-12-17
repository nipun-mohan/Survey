package nimble.survey.interfaces;


import com.google.gson.JsonArray;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Nipun on 12/12/2018.
 */

public interface APIInterface {

    @GET("surveys.json")
    Call<JsonArray> getSurveys(@QueryMap Map<String, String> data);
}

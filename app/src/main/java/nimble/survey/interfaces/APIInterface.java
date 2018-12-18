package nimble.survey.interfaces;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Nipun on 12/12/2018.
 */

public interface APIInterface {

    @GET("surveys.json")
    Call<JsonArray> getSurveys(@QueryMap Map<String, String> data);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<JsonObject> getAccessToken(@FieldMap Map<String, String> data);
}

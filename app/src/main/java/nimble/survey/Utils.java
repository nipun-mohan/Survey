package nimble.survey;

import android.net.Uri;
import android.view.ViewTreeObserver;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nipun on 12/12/2018.
 */
public class Utils {

    public static int page = 1;
    public static int per_page_limit = 20;

    /**
     * Gson instantiation for json parsing
     *
     * @return
     */
    public static Gson getGsonObject() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
        return gson;
    }


    /**
     * Retrofit http library instantiation for network calls
     *
     */
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null || (retrofit != null && !retrofit.baseUrl().toString().equalsIgnoreCase(baseUrl))) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }


    /**
     * OkHttpClient for retrofit
     *
     * @return
     */
    private static OkHttpClient okClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        return client;
    }


    /**
     * Render Image with fresco
     *
     * @param simpleDraweeView
     * @param uri
     * @param isDetails
     */
    public static void setupImage(final SimpleDraweeView simpleDraweeView, final Uri uri, final boolean isDetails) {
        simpleDraweeView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                simpleDraweeView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (!isDetails)
                    simpleDraweeView.getHierarchy().setFailureImage(AppController.getInstance().getResources().getDrawable(R.drawable.no_image), ScalingUtils.ScaleType.FOCUS_CROP);
                else
                    simpleDraweeView.getHierarchy().setFailureImage(AppController.getInstance().getResources().getDrawable(R.drawable.bg_survey), ScalingUtils.ScaleType.FOCUS_CROP);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(simpleDraweeView.getWidth(), simpleDraweeView.getHeight()))
                        .build();
                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setOldController(simpleDraweeView.getController())
                        .setImageRequest(request)
                        .build();
                simpleDraweeView.setController(controller);
                return true;
            }
        });
    }
}

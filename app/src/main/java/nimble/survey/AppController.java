package nimble.survey;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppController extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //Preference initialization
        Pref.openPref(this);

        //Realm DB initialization
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //Fresco -  image loading library initialization
        Fresco.initialize(this);
        mContext = this;
    }

    /**
     * Application context method
     * @return
     */
    public static Context getInstance() {
        return mContext;
    }
}
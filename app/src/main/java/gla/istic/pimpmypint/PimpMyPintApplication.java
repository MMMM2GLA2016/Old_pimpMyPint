package gla.istic.pimpmypint;

import android.app.Application;

import com.firebase.client.Firebase;

public class PimpMyPintApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

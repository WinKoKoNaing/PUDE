package com.porlar.techhousestudio.pude;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import me.myatminsoe.mdetect.MDetect;

/**
 * Created by USER on 2/8/2019.
 */
public class Application extends android.app.Application {
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        MDetect.INSTANCE.init(this);
        Fresco.initialize(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

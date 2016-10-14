package com.bottlerocketstudios.continuitysample.core.injection;

import android.content.Context;
import android.util.LruCache;

import com.bottlerocketstudios.continuitysample.core.util.LogIt;
import com.bottlerocketstudios.continuitysample.legislator.domain.LegislatorRepository;
import com.bottlerocketstudios.continuitysample.legislator.domain.SharedPrefLegislatorStorage;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created on 9/14/16.
 */
public class ContextServiceInitializer implements Injectable<Context> {
    private static final String TAG = ContextServiceInitializer.class.getSimpleName();

    @Override
    public void receiveInjection(Context context) {
        OkHttpClient okHttpClient = initOkHttp(context);
        initLegislatorRepository(okHttpClient, context);
    }

    private OkHttpClient initOkHttp(Context context) {
        int cacheSize = 20 * 1024 * 1024;
        File cacheDir = new File(context.getCacheDir(), "httpCache");
        if (!cacheDir.isDirectory()) {
            if (!cacheDir.mkdirs()) {
                LogIt.e(TAG, "Unable to create Http Cache");
            }
        }
        Cache cache = new Cache(cacheDir, cacheSize);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        SampleServiceLocator.put(OkHttpClient.class, okHttpClient);
        return okHttpClient;
    }

    private void initLegislatorRepository(OkHttpClient okHttpClient, Context context) {
        final SharedPrefLegislatorStorage legislatorStorage = new SharedPrefLegislatorStorage(context);
        LegislatorRepository legislatorRepository = new LegislatorRepository(legislatorStorage, okHttpClient);
        SampleServiceLocator.put(LegislatorRepository.class, legislatorRepository);
    }
}

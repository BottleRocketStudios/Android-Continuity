
package com.bottlerocketstudios.continuitysample.core.injection;

import android.content.Context;

import com.bottlerocketstudios.continuity.ContinuityRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Perform one-time thread safe construction of persistent object graph. {@link ServiceInjector#injectWithType(Class, Injectable)}
 * Also Handle delivery of Context to objects that need it once the Application object is created.
 */
public class ServiceInitializer {
    private static final String TAG = ServiceInitializer.class.getSimpleName();

    private static boolean sInitialized;
    private static List<Injectable<Context>> sContextWaitingList = new ArrayList<>();

    static synchronized void initializeServices() {
        if (sInitialized) {
            return;
        }
        sInitialized = true;
        sContextWaitingList.add(new ContextServiceInitializer());
        initContinuity();
    }

    private static void initContinuity() {
        SampleServiceLocator.put(ContinuityRepository.class, new ContinuityRepository());
    }

    public static boolean isInitialized() {
        return sInitialized;
    }

    public static void initializeContext(Context context) {
        initializeServices();
        SampleServiceLocator.put(Context.class, context);
        if (sContextWaitingList != null) {
            for (Injectable<Context> injectable : sContextWaitingList) {
                ServiceInjector.injectWithType(Context.class, injectable);
            }
            sContextWaitingList = null;
        }
    }
}


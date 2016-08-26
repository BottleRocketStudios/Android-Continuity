package com.bottlerocketstudios.continuity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created on 8/22/16.
 */
public class ContinuityBuilder<T> {
    private static final String TAG = ContinuityBuilder.class.getSimpleName();
    private static final int DEFAULT_TASK_ID = -1;

    private final Object mAnchor;
    private final ContinuityRepository mContinuityRepository;
    private final Class<T> mContinuousClass;

    private long mLifetimeMs;
    private ContinuityFactory<T> mContinuityFactory;
    private int mTaskId;
    private String mTag = "";

    public ContinuityBuilder(ContinuityRepository continuityRepository, Object anchor, Class<T> continuousClass, long defaultLifetimeMs) {
        mAnchor = anchor;
        mContinuityRepository = continuityRepository;
        mContinuousClass = continuousClass;
        mTaskId = DEFAULT_TASK_ID;
        mLifetimeMs = defaultLifetimeMs;
    }

    public ContinuityBuilder<T> using(ContinuityFactory<T> continuityFactory){
        mContinuityFactory = continuityFactory;
        return this;
    }

    public ContinuityBuilder<T> task(int taskId) {
        mTaskId = taskId;
        return this;
    }

    public ContinuityBuilder<T> lifetime(long lifetimeMs) {
        mLifetimeMs = lifetimeMs;
        return this;
    }

    public ContinuityBuilder<T> tag(String tag) {
        mTag = tag;
        return this;
    }

    public T build() {
        ContinuityId continuityId = createContinuityId();

        T instance = mContinuityRepository.get(mAnchor, continuityId, mLifetimeMs);
        if (instance != null) {
            return instance;
        } else {
            instance = createInstance();
            if (instance != null) {
                mContinuityRepository.put(continuityId, mAnchor, instance, mLifetimeMs);
                return instance;
            }
        }
        throw new IllegalStateException("Unable to find or create instance");
    }

    public void remove() {
        ContinuityId continuityId = createContinuityId();
        mContinuityRepository.remove(continuityId);
    }

    public void destroyThenRemove() {
        ContinuityId continuityId = createContinuityId();
        mContinuityRepository.destroyThenRemove(mAnchor, continuityId);
    }

    private T createInstance() {
        if (mContinuityFactory != null) {
            return mContinuityFactory.create();
        } else {
            try {
                return mContinuousClass.newInstance();
            } catch (InstantiationException e) {
                Log.e(TAG, "The class " + mContinuousClass.getCanonicalName() + " must have a nullary constructor.", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Caught java.lang.IllegalAccessException", e);
            }
        }
        return  null;
    }

    @NonNull
    private ContinuityId createContinuityId() {
        if (mTaskId == DEFAULT_TASK_ID) {
            mTaskId = determineTaskIdFromAnchor(mAnchor);
        }
        return new ContinuityId(mAnchor.getClass().getCanonicalName(), mTaskId, mContinuousClass.getCanonicalName(), mTag);
    }

    private static int determineTaskIdFromAnchor(Object anchor) {
        if (anchor instanceof Activity) {
            return ((Activity)anchor).getTaskId();
        } else if (anchor instanceof android.support.v4.app.Fragment) {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) anchor;
            if (fragment.getActivity() != null) {
                return fragment.getActivity().getTaskId();
            } else {
                Log.w(TAG, "Anchor was a fragment that was not yet associated with an Activity, cannot determine task id");
            }
        } else if (anchor instanceof android.app.Fragment){
            android.app.Fragment fragment = (android.app.Fragment) anchor;
            if (fragment.getActivity() != null) {
                return fragment.getActivity().getTaskId();
            } else {
                Log.w(TAG, "Anchor was a fragment that was not yet associated with an Activity, cannot determine task id");
            }
        }
        return DEFAULT_TASK_ID;
    }
}

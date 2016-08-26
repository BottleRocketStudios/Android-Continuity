package com.bottlerocketstudios.continuity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Builder object that is created using {@link ContinuityRepository#with(Object, Class)}.
 *
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

    ContinuityBuilder(ContinuityRepository continuityRepository, Object anchor, Class<T> continuousClass, long defaultLifetimeMs) {
        mAnchor = anchor;
        mContinuityRepository = continuityRepository;
        mContinuousClass = continuousClass;
        mTaskId = DEFAULT_TASK_ID;
        mLifetimeMs = defaultLifetimeMs;
    }

    /**
     * Optionally provide a ContinuityFactory instance that will create the instance of the
     * ContinuousObject you wish to use. This is required if the object does not have a nullary
     * constructor.
     *
     * @param continuityFactory Instance of ContinuityFactory&lt;T&gt; generic factory.
     *
     * @return This builder
     */
    public ContinuityBuilder<T> using(ContinuityFactory<T> continuityFactory){
        mContinuityFactory = continuityFactory;
        return this;
    }

    /**
     * Optionally specify a task id. This will supersede the task id of the anchor and can be
     * used when the anchor is not a Fragment or Activity.
     *
     * @param taskId Current Android Task ID associated with the anchor object.
     *
     * @return This builder
     */
    public ContinuityBuilder<T> task(int taskId) {
        mTaskId = taskId;
        return this;
    }

    /**
     * Optional custom lifetime for this instance of the ContinuousObject being created/retrieved.
     * If there are multiple calls for the same ContinuousObject instance, the highest lifetime provided will be used.
     *
     * @param lifetimeMs Lifetime in milliseconds to retain the ContinuousObject after its anchor is destroyed or garbage collected.
     *
     * @return This builder
     */
    public ContinuityBuilder<T> lifetime(long lifetimeMs) {
        mLifetimeMs = lifetimeMs;
        return this;
    }

    /**
     * Provide an arbitrary string to differentiate this ContinuousObject from others of the same
     * type associated with the same type of anchor within the same task.
     *
     * @param tag String that will separate this ContinuousObject from others of the same type.
     *
     * @return This builder
     */
    public ContinuityBuilder<T> tag(String tag) {
        mTag = tag;
        return this;
    }

    /**
     * Create or retrieve an instance of the ContinuousObject by combining task ID, tag, type of the
     * anchor, and type of the ContinuousObject as the cache key.
     *
     * @return A new or cached instance of the ContinuousObject. You should always assume you are getting a cached instance.
     */
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

    /**
     * Remove the ContinuousObject from the cache. It will be call {@link ContinuousObject#onContinuityDiscard()},
     * but it will not call {@link ContinuousObject#onContinuityAnchorDestroyed(Object)}.
     * See also {@link #destroyThenRemove()}
     */
    public void remove() {
        ContinuityId continuityId = createContinuityId();
        mContinuityRepository.remove(continuityId);
    }

    /**
     * Notify the ContinuousObject of the anchor's destruction then remove it from the cache.
     * After the {@link ContinuousObject#onContinuityAnchorDestroyed(Object)} call, there will be a
     * call to {@link ContinuousObject#onContinuityDiscard()}. If the anchor destruction call should
     * be skipped see {@link #remove()}.
     */
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

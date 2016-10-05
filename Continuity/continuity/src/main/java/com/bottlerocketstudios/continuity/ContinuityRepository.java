package com.bottlerocketstudios.continuity;

import android.app.Activity;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * The ContinuityRepository is the core of the library and manages creation and retention of ContinuousObjects.
 * Typically you should create one instance centrally and reuse it in all of your Activities
 * and Fragments. Each instance will have a Thread associated with it that will automatically stop
 * running while idle or empty.
 * </p>
 * <p>
 * The default timings are available as public constants in this object. If you wish to modify the
 * timing values, use the provided constructor.
 * </p>
 * <p>
 * Use the {@link #with(Object, Class)} method to create a {@link ContinuityBuilder} and proceed from
 * there.
 * </p>
 * <p>
 * This object will retain a weak reference to the anchor give to the {@link #with(Object, Class)} method
 * allowing the anchor object to be garbage collected. At that point the associated ContinuousObject will
 * be discarded after its lifetime expires. However, it is very easy to leak this anchor object so
 * it is a good idea, though not strictly required, that you call {@link #onDestroy(Object)} with the
 * same anchor you used in the {@link #with(Object, Class)} command to clean up when your Activity
 * or Fragment is Destroyed.
 * </p>
 * <p>
 * See the README.md file included with this project in the source code repository for more details and examples.
 * </p>
 *
 * Created on 8/22/16.
 */
public class ContinuityRepository {
    private final String TAG = ContinuityRepository.class.getSimpleName() + " " + this.hashCode();

    public static final long DEFAULT_CHECK_INTERVAL_MS = 250;
    public static final long DEFAULT_IDLE_SHUTDOWN_MS = TimeUnit.SECONDS.toMillis(30);
    public static final long DEFAULT_LIFETIME_MS = TimeUnit.SECONDS.toMillis(1);
    public static final int DEFAULT_MAX_EMPTY_ITERATIONS = 10;

    private final long mCheckIntervalMs;
    private final long mIdleShutdownMs;
    private final long mDefaultLifetimeMs;
    private final int mMaxEmptyIterations;

    private final String mThreadLock = "";

    private final Map<Object, List<ContinuityId>> mAnchoredContinuityIdMap = Collections.synchronizedMap(new WeakHashMap<Object, List<ContinuityId>>());
    private final Map<ContinuityId, ContinuityContainer> mHeterogenousCache = Collections.synchronizedMap(new TreeMap<ContinuityId, ContinuityContainer>());

    private final List<ContinuityId> mDeletionCandidates = new ArrayList<>(50);
    private final List<ContinuityId> mAnchoredContinuityIdList = new ArrayList<>(50);

    private CleanupThread mThread;

    /**
     * Create a ContinuityRepository using the default values
     */
    public ContinuityRepository() {
        this(DEFAULT_CHECK_INTERVAL_MS, DEFAULT_IDLE_SHUTDOWN_MS, DEFAULT_LIFETIME_MS, DEFAULT_MAX_EMPTY_ITERATIONS);
    }

    /**
     * Create a ContinuityRepository with the provided settings.
     *
     * Several parameters can be adjusted to change how quickly cached items are released and how long
     * the watchdog thread that cleans the items will run while idle or empty.
     *
     * @param checkIntervalMs       Time in milliseconds between checks to cleanup unused references.
     * @param idleShutdownMs        Time in milliseconds before inactivity will release the Thread.
     * @param defaultLifetimeMs     Default time in milliseconds to keep an un-anchored reference around.
     * @param maxEmptyIterations    Number of checks to perform with an empty collection before releasing the Thread.
     */
    public ContinuityRepository(long checkIntervalMs, long idleShutdownMs, long defaultLifetimeMs, int maxEmptyIterations) {
        mCheckIntervalMs = checkIntervalMs;
        mIdleShutdownMs = idleShutdownMs;
        mDefaultLifetimeMs = defaultLifetimeMs;
        mMaxEmptyIterations = maxEmptyIterations;
    }

    /**
     * Change minimum logging importance level, skipping anything below that level of importance.
     *
     * @param loggingLevel Value between {@link Log#VERBOSE} to {@link Log#ERROR}
     */
    public void setMinLoggingLevel(int loggingLevel) {
        ContinuityLog.setMinLoggingLevel(loggingLevel);
    }

    /**
     * Thre primary interface for the ContinuityRepository. Use this method to construct or retrieve
     * a ContinuousObject.
     *
     * @param anchor            The object to anchor this instance to, it will remain available while this anchor is in memory.
     * @param continuousClass   The class which you wish to make continuous across creation and destruction of anchor instances.
     * @param <T>               The type of the ContinuousObject.
     * @return                  A new ContinuityBuilder associated with the supplied anchor that will build an instance of the continuousClass.
     */
    public <T> ContinuityBuilder<T> with(Object anchor, Class<T> continuousClass) {
        return new ContinuityBuilder<>(this, anchor, continuousClass, mDefaultLifetimeMs);
    }

    /**
     * Explicitly notify that this anchor is going out of scope so that references can be removed after lifetime timeout.
     * If the anchor supplied is a Fragment or Activity that is finishing, it will be an immediate removal without waiting
     * for lifetime timout.
     *
     * @param anchor The anchor object which is being destroyed.
     */
    public void onDestroy(Object anchor) {
        if (anchor == null) return;
        if (isFinishing(anchor)) {
            ContinuityLog.i(TAG, "Finishing");
            destroyThenRemoveAll(anchor);
        } else {
            ContinuityLog.i(TAG, "Destroying");
            destroy(anchor);
        }
    }

    private boolean isFinishing(Object anchor) {
        if (anchor instanceof Activity) {
            return ((Activity) anchor).isFinishing();
        } else if (anchor instanceof Fragment) {
            return (((Fragment) anchor).isRemoving() || ((Fragment) anchor).getActivity() != null && ((Fragment) anchor).getActivity().isFinishing());
        } else if (anchor instanceof android.support.v4.app.Fragment){
            return ((android.support.v4.app.Fragment) anchor).isRemoving() || (((android.support.v4.app.Fragment) anchor).getActivity() != null && ((android.support.v4.app.Fragment) anchor).getActivity().isFinishing());
        }
        return false;
    }

    private void destroy(Object anchor) {
        //Notify all of the ContinuousObjects associated with this anchor.
        notifyCacheOfDestruction(anchor);
        //Remove all anchored ContinuityIds associated with this anchor.
        mAnchoredContinuityIdMap.remove(anchor);
        touchCleanupThread();
    }

    private void notifyCacheOfDestruction(@NonNull Object anchor) {
        List<ContinuityId> continuityIdList = mAnchoredContinuityIdMap.get(anchor);
        if (continuityIdList != null) {
            //First create a shallow copy of ids.
            List<ContinuityId> continuityIdSnapshot = new ArrayList<>(continuityIdList);
            for (int i = 0; i < continuityIdSnapshot.size(); i++) {
                ContinuityContainer continuityContainer = mHeterogenousCache.get(continuityIdSnapshot.get(i));
                notifyDestruction(anchor, continuityContainer);
            }
        }
    }

    private void notifyDestruction(Object anchor, ContinuityContainer continuityContainer) {
        if (continuityContainer != null) {
            //If it is a ContinuousObject, notify it of the destroyed anchor.
            Object object = continuityContainer.getObject();
            if (object instanceof ContinuousObject) {
                ((ContinuousObject) object).onContinuityAnchorDestroyed(anchor);
            }
        }
    }

    private void notifyDiscard(ContinuityContainer continuityContainer) {
        if (continuityContainer != null) {
            //Ensure cache contains object
            Object object = continuityContainer.getObject();
            if (object instanceof ContinuousObject) {
                ((ContinuousObject) object).onContinuityDiscard();
            }
        }
    }

    @SuppressWarnings("unchecked")
    <T> T get(Object anchor, ContinuityId continuityId, long lifetimeMs) {
        ContinuityContainer continuityContainer = mHeterogenousCache.get(continuityId);
        if (continuityContainer != null) {
            appendContinuityIdToAnchor(anchor, continuityId);
            continuityContainer.updateLifetimeMs(lifetimeMs);
            continuityContainer.setExpirationMs(0);
            touchCleanupThread();
            return (T) continuityContainer.getObject();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    <T> void put(ContinuityId continuityId, Object anchor, T instance, long lifetimeMs) {
        appendContinuityIdToAnchor(anchor, continuityId);
        mHeterogenousCache.put(continuityId, new ContinuityContainer(instance, lifetimeMs));
        touchCleanupThread();
    }

    void remove(ContinuityId continuityId) {
        ContinuityContainer continuityContainer = mHeterogenousCache.remove(continuityId);
        notifyDiscard(continuityContainer);
        touchCleanupThread();
    }

    private void destroyThenRemoveAll(Object anchor) {
        List<ContinuityId> continuityIdList = mAnchoredContinuityIdMap.get(anchor);
        if (continuityIdList != null) {
            //First create a shallow copy of ids.
            List<ContinuityId> continuityIdSnapshot = new ArrayList<>(continuityIdList);
            for (int i = 0; i < continuityIdSnapshot.size(); i++) {
                destroyThenRemove(anchor, continuityIdSnapshot.get(i));
            }
        }
    }

    void destroyThenRemove(Object anchor, ContinuityId continuityId) {
        List<ContinuityId> continuityIdList = mAnchoredContinuityIdMap.get(anchor);
        if (continuityIdList != null) {
            continuityIdList.remove(continuityId);
        }
        ContinuityContainer continuityContainer = mHeterogenousCache.get(continuityId);
        notifyDestruction(anchor, continuityContainer);
        remove(continuityId);
    }

    private void appendContinuityIdToAnchor(Object anchor, ContinuityId continuityId) {
        List<ContinuityId> continuityIdCollection = mAnchoredContinuityIdMap.get(anchor);
        if (continuityIdCollection == null) {
            continuityIdCollection = new ArrayList<>();
            mAnchoredContinuityIdMap.put(anchor, continuityIdCollection);
        }
        continuityIdCollection.add(continuityId);
    }

    /**
     * This method will perform cleanup of any items that have lived past their lifetime without an anchor reference.
     */
    private void performCleanup(CleanupThread cleanupThread) {
        /*
         *  Check for error state of more than the known thread calling us.
         *  This operation is not threadsafe because class fields are used to contain lists modified
         *  during this operation. These fields are reused to reduce GC churn at the expense of thread safety.
         *
         *  That isn't an issue because this method will only ever get called from the mThread loop.
         */
        if (cleanupThread != mThread) {
            ContinuityLog.w(TAG, "Two threads were started");
            cleanupThread.stopRunning();
            return;
        }

        //Get the set of items that are in the cache which do not have anchors.
        //Reuse deletion candidate List to reduce GC churn on each iteration.
        mDeletionCandidates.clear();
        mDeletionCandidates.addAll(mHeterogenousCache.keySet());
        mDeletionCandidates.removeAll(getAnchoredContinuityIdCollection());

        //Walk the set of unanchored values and either set their expiration time or delete expired items.
        //Use for instead of foreach to reduce GC churn on iterator creation/destruction.
        for (int i = 0; i < mDeletionCandidates.size(); i++) {
            ContinuityId continuityId = mDeletionCandidates.get(i);
            ContinuityContainer continuityContainer = mHeterogenousCache.get(continuityId);
            if (continuityContainer.getExpirationMs() > 0) {
                if (continuityContainer.getExpirationMs() < SystemClock.uptimeMillis()) {
                    //Past deadline, delete it
                    mHeterogenousCache.remove(continuityId);
                    notifyDiscard(continuityContainer);
                }
            } else {
                //Set expiration deadline.
                continuityContainer.setExpirationMs(SystemClock.uptimeMillis() + continuityContainer.getLifetimeMs());
            }
        }
    }

    @NonNull
    private Collection<ContinuityId> getAnchoredContinuityIdCollection() {
        //Reuse anchored list to reduce GC churn.
        //Not thread safe. This is only called from performCleanup() which constrains it to a single thread.
        mAnchoredContinuityIdList.clear();
        for (List<ContinuityId> continuityIdList: mAnchoredContinuityIdMap.values()) {
            mAnchoredContinuityIdList.addAll(continuityIdList);
        }
        return mAnchoredContinuityIdList;
    }

    boolean isEmpty() {
        return mHeterogenousCache.isEmpty();
    }

    boolean isRunning() {
        return (mThread != null && mThread.isRunning());
    }

    private void touchCleanupThread() {
        if (mThread == null) {
            ContinuityLog.v(TAG, "Cleanup thread was missing");
            //Don't synchronize unless the thread is null.
            synchronized (mThreadLock) {
                //We block this section to ensure that only one instance is created.
                //Check that another thread didn't already create it while waiting on synchronize block.
                if (mThread == null) {
                    mThread = createThread();
                }
            }
        }
        mThread.touch();
    }

    private CleanupThread createThread() {
        ContinuityLog.v(TAG, "Creating new cleanup thread");
        CleanupThread thread = new CleanupThread();
        thread.start();
        return thread;
    }

    private void onThreadShutdown(CleanupThread cleanupThread) {
        if (mThread == cleanupThread) {
            ContinuityLog.v(TAG, "Removing thread reference");
            mThread = null;
        } else {
            ContinuityLog.w(TAG, "An unexpected thread was shutdown");
        }
    }

    private class CleanupThread extends Thread {
        private boolean mRunning;
        private long mLastTouchTimestampMs;
        private int mEmptyCount = 0;

        @Override
        public synchronized void start() {
            super.start();
            mRunning = true;
        }

        @Override
        public void run() {
            ContinuityLog.v(TAG, "Running thread");
            while (mRunning) {
                synchronized (this) {
                    try {
                        wait(mCheckIntervalMs);
                        performCleanup(this);

                        if (SystemClock.uptimeMillis() - mLastTouchTimestampMs > mIdleShutdownMs) {
                            ContinuityLog.v(TAG, "Repository was idle too long.");
                            stopRunning();
                        }

                        if (isEmpty()) {
                            mEmptyCount++;
                            ContinuityLog.v(TAG, "Cache is empty.");
                            if (mEmptyCount > mMaxEmptyIterations) {
                                stopRunning();
                            }
                        } else {
                            mEmptyCount = 0;
                        }
                    } catch (InterruptedException e) {
                        ContinuityLog.e(TAG, "Caught java.lang.InterruptedException", e);
                    }
                }
            }
            ContinuityLog.v(TAG, "Shutting down thread");
            onThreadShutdown(this);
        }

        public void touch() {
            mLastTouchTimestampMs = SystemClock.uptimeMillis();
            if (!mRunning) start();
        }

        public void stopRunning() {
            mRunning = false;
        }

        public boolean isRunning() {
            return mRunning;
        }
    }

}

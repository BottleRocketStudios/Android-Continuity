package com.bottlerocketstudios.continuity;

/**
 * Created on 8/22/16.
 */
public interface ContinuousObject {

    /**
     * The specified anchor to this presenter has been destroyed.
     */
    void onContinuityAnchorDestroyed(Object anchor);

    /**
     * This object has been removed from a ContinuityRepository and should never be used again.
     */
    void onContinuityDiscard();
}

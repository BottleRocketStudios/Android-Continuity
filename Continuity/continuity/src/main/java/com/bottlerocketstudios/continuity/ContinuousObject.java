package com.bottlerocketstudios.continuity;

/**
 * This interface should be implemented by any object you wish to have managed by the ContinuityRepository.
 * When the destruction of the anchor or the disposal of this object from the ContinuityRepository occurs
 * objects that implement this interface will be notified and can respond accordingly.
 *
 * Created on 8/22/16.
 */
public interface ContinuousObject {

    /**
     * The specified anchor to this ContinuousObject has been destroyed. Remove any references
     * to the anchor or its children which this object holds.
     *
     * @param anchor The anchor which was destroyed.
     */
    void onContinuityAnchorDestroyed(Object anchor);

    /**
     * This object has been removed from a ContinuityRepository and should never be used again.
     * Perform any cleanup as appropriate.
     */
    void onContinuityDiscard();
}

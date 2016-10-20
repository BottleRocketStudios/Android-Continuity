package com.bottlerocketstudios.continuitysample.core.usecase;

import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.groundcontrol.agent.AbstractAgent;

/**
 * For this application our Agents will not provide advanced progress indication.
 */
public abstract class BaseAgent<T> extends AbstractAgent<ResponseContainer<T>, Float> {

    private String mUniqueIdentifier;

    @Override
    public String getUniqueIdentifier() {
        if (mUniqueIdentifier == null) {
            mUniqueIdentifier = createUniqueIdentifier();
        }
        return mUniqueIdentifier;
    }

    public String createUniqueIdentifier() {
        //Default identifier is just the canonical name of the concrete implementation.
        return this.getClass().getCanonicalName();
    }

    @Override
    public void onProgressUpdateRequested() {
        //NOOP
    }
}

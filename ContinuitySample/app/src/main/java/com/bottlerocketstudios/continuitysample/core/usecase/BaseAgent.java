package com.bottlerocketstudios.continuitysample.core.usecase;

import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.groundcontrol.agent.AbstractAgent;

/**
 * Created on 9/14/16.
 */
public abstract class BaseAgent<T> extends AbstractAgent<ResponseContainer<T>, Void> {

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

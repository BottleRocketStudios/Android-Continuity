package com.bottlerocketstudios.continuity;

/**
 * This is a bare-bones generic Factory interface that can be used by the {@link ContinuityBuilder}
 * to instantiate objects which do not have nullary constructors.
 * Created on 8/22/16.
 */
public interface ContinuityFactory<T> {
    /**
     * Create a new instance of the type &lt;T&gt;
     * @return The new instance.
     */
    T create();
}

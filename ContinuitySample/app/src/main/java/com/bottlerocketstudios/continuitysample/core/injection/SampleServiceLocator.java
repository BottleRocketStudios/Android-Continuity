
package com.bottlerocketstudios.continuitysample.core.injection;

import java.util.HashMap;
import java.util.Map;

class SampleServiceLocator {

    private Map<Class<?>, Object> mLocatorMap;

    private SampleServiceLocator() {
        mLocatorMap = new HashMap<>();
    }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        public static final SampleServiceLocator instance = new SampleServiceLocator();
    }

    /**
     * Get the instance or create it. (inherently thread safe Bill Pugh pattern)
     */
    private static SampleServiceLocator getInstance() {
        return SingletonHolder.instance;
    }

    static <T> void put(Class<T> type, T instance) {
        if (type == null) {
            throw new NullPointerException("Type is null");
        }
        getInstance().mLocatorMap.put(type, instance);
    }

    static <T> T get(Class<T> type) {
        return type.cast(getInstance().mLocatorMap.get(type));
    }

}

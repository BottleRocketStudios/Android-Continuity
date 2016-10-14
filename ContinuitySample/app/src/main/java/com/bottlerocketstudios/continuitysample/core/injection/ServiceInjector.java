
package com.bottlerocketstudios.continuitysample.core.injection;

/**
 * Simple dependency injection tool that allows for single instance objects to be stored and
 * retrieved similar to a Service Locator. Obviates use of private constructor Singleton anti-pattern.
 */
public class ServiceInjector {

    /**
     * Initialize service object graph, if needed, then lookup and provide the type requested.
     */
    public static <T> void injectWithType(Class<? extends T> type, Injectable<T> injectable) {
        if (!ServiceInitializer.isInitialized()) {
            ServiceInitializer.initializeServices();
        }
        injectable.receiveInjection(SampleServiceLocator.get(type));
    }

}

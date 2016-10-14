
package com.bottlerocketstudios.continuitysample.core.injection;

public interface Injectable<T> {
    void receiveInjection(T injection);
}

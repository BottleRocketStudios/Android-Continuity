package com.bottlerocketstudios.continuitysample.util;


import android.util.LruCache;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 10/11/16.
 */

public class StubbedLruCache<K, V> {

    private final Map<K, V> mMap;
    private final Class<K> mKeyClass;
    private final Class<V> mValueClass;
    private final LruCache<K, V> mMockedCache;

    @SuppressWarnings("unchecked")
    public StubbedLruCache(Class<K> keyClass, Class<V> valueClass) {
        mMockedCache = (LruCache<K, V>) Mockito.mock(LruCache.class);

        mMap = new HashMap<>(10);
        mKeyClass = keyClass;
        mValueClass = valueClass;
        createMockPut();
        createMockGet();
    }

    public LruCache<K, V> getMockedCache() {
        return mMockedCache;
    }

    private void createMockGet() {
        ArgumentCaptor<K> key = ArgumentCaptor.forClass(mKeyClass);
        Mockito.when(mMockedCache.get(key.capture())).thenAnswer(new Answer<V>() {
            @Override
            public V answer(InvocationOnMock invocation) throws Throwable {
                return getMock(invocation.getArgumentAt(0, mKeyClass));
            }
        });
    }

    private V getMock(K key) {
        return mMap.get(key);
    }

    private void createMockPut() {
        ArgumentCaptor<K> key = ArgumentCaptor.forClass(mKeyClass);
        ArgumentCaptor<V> value = ArgumentCaptor.forClass(mValueClass);
        Mockito.when(mMockedCache.put(key.capture(), value.capture())).thenAnswer(new Answer<V>() {
            @Override
            public V answer(InvocationOnMock invocation) throws Throwable {
                return putMock(invocation.getArgumentAt(0, mKeyClass), invocation.getArgumentAt(1, mValueClass));
            }
        });
    }

    private V putMock(K key, V value) {
        return mMap.put(key, value);
    }

}

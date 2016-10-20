package com.bottlerocketstudios.continuitysample.core.ui;

import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.bottlerocketstudios.continuitysample.core.util.LogIt;

import java.lang.ref.WeakReference;

/**
 * This object proxies calls between an ObservableList and a RecyclerView.Adapter to prevent a leak and when
 * the WeakReference to the RecyclerView.Adapter breaks, unsubscribe from the ObservableList.
 */
public class WeakObservableListWatcher<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {

    private static final String TAG = WeakObservableListWatcher.class.getSimpleName();

    private final WeakReference<RecyclerView.Adapter> mAdapterReference;

    public WeakObservableListWatcher(RecyclerView.Adapter adapter) {
        mAdapterReference = new WeakReference<>(adapter);
    }

    @Nullable
    private RecyclerView.Adapter getAdapter(ObservableList<T> observableList) {
        RecyclerView.Adapter adapter = mAdapterReference.get();
        if (adapter == null) {
            unsubscribe(observableList);
        }
        return adapter;
    }

    private void unsubscribe(ObservableList<T> observableList) {
        LogIt.d(TAG, "Weak reference was broken, unsubscribing.");
        observableList.removeOnListChangedCallback(this);
    }

    @Override
    public void onChanged(ObservableList<T> observableList) {
        RecyclerView.Adapter adapter = getAdapter(observableList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> observableList, int positionStart, int itemCount) {
        RecyclerView.Adapter adapter = getAdapter(observableList);
        if (adapter != null) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> observableList, int positionStart, int itemCount) {
        RecyclerView.Adapter adapter = getAdapter(observableList);
        if (adapter != null) {
            adapter.notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> observableList,  int fromPosition, int toPosition, int itemCount) {
        RecyclerView.Adapter adapter = getAdapter(observableList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> observableList, int positionStart, int itemCount) {
        RecyclerView.Adapter adapter = getAdapter(observableList);
        if (adapter != null) {
            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }
}

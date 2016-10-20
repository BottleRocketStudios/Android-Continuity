package com.bottlerocketstudios.continuitysample.core.presenter;

import android.content.DialogInterface;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.core.ui.dialog.DialogConfiguration;
import com.bottlerocketstudios.continuitysample.core.util.LogIt;

import org.jetbrains.annotations.NotNull;

/**
 * An example presenter for a DialogFragment.
 */
public class SampleDialogPresenter implements ContinuousObject {
    private static final String TAG = SampleDialogPresenter.class.getSimpleName();

    private DialogConfiguration mDialogConfiguration;
    private DialogPresenterListener mListener;
    private ListenerWrapper mListenerWrapper = new ListenerWrapper();

    public void positiveClicked() {
        mListenerWrapper.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
    }

    public void negativeClicked() {
        mListenerWrapper.onClick(getDialog(), DialogInterface.BUTTON_NEGATIVE);
    }

    public void neutralClicked() {
        mListenerWrapper.onClick(getDialog(), DialogInterface.BUTTON_NEUTRAL);
    }

    private DialogInterface getDialog() {
        return mListener.getDialog();
    }

    public void setDialogConfiguration(DialogConfiguration dialogConfiguration) {
        mDialogConfiguration = dialogConfiguration;
    }

    public void bindListener(@NotNull DialogPresenterListener dialogPresenterListener) {
        mListener = dialogPresenterListener;
        mListener.bindDialogConfiguration(mDialogConfiguration);
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        mListenerWrapper.setOnClickListener(onClickListener);
    }

    /**
     * Handles the case of setting a listener after onCreateDialog
     */
    private static class ListenerWrapper implements DialogInterface.OnClickListener {
        private DialogInterface.OnClickListener mOnClickListener;

        public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(dialogInterface, which);
            } else {
                LogIt.e(TAG, "Dialog button clicked without a listener");
            }
        }
    }

    public interface DialogPresenterListener {
        void bindDialogConfiguration(DialogConfiguration dialogConfiguration);
        DialogInterface getDialog();
    }

    @Override
    public void onContinuityAnchorDestroyed(Object anchor) {
        mListener = null;
        mListenerWrapper.setOnClickListener(null);
    }

    @Override
    public void onContinuityDiscard() {}

}

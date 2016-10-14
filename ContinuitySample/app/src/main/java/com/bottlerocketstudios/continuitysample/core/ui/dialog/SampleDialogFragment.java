package com.bottlerocketstudios.continuitysample.core.ui.dialog;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuity.ContinuityRepository;
import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.injection.Injectable;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;
import com.bottlerocketstudios.continuitysample.core.presenter.SampleDialogPresenter;
import com.bottlerocketstudios.continuitysample.databinding.SampleDialogFragmentBinding;

/**
 * Created on 6/8/16.
 */
public class SampleDialogFragment extends DialogFragment {
    private static final String TAG = SampleDialogFragment.class.getSimpleName();

    private static final String ARG_DIALOG_CONFIGURATION = "dialogConfiguration";

    private DialogConfiguration mDialogConfiguration;
    private SampleDialogFragmentBinding mSampleDialogFragmentBinding;
    private ContinuityRepository mPresenterRepository;
    private SampleDialogPresenter mSampleDialogPresenter;
    private DialogInterface.OnClickListener mPendingOnClickListener;
    private Handler mHandler;

    /**
     * Use the builder
     */
    private static SampleDialogFragment newInstance(DialogConfiguration dialogConfiguration, DialogInterface.OnClickListener onClickListener) {
        SampleDialogFragment checkinDialogFragment = new SampleDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DIALOG_CONFIGURATION, dialogConfiguration);
        checkinDialogFragment.setArguments(args);
        checkinDialogFragment.setListener(onClickListener);
        return checkinDialogFragment;
    }

    public SampleDialogFragment() {
        ServiceInjector.injectWithType(ContinuityRepository.class, new Injectable<ContinuityRepository>() {
            @Override
            public void receiveInjection(ContinuityRepository injection) {
                mPresenterRepository = injection;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogConfiguration = getArguments().getParcelable(ARG_DIALOG_CONFIGURATION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterRepository.onDestroy(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setStyle(STYLE_NO_TITLE, getTheme());
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSampleDialogFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sample_dialog_fragment, container, false);
        return mSampleDialogFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSampleDialogPresenter = mPresenterRepository.with(this, SampleDialogPresenter.class)
                .tag(String.valueOf(mDialogConfiguration.getDialogId()))
                .build();
        mSampleDialogPresenter.setDialogConfiguration(mDialogConfiguration);
        mSampleDialogPresenter.bindListener(mDialogPresenterListener);
        if (mPendingOnClickListener != null) {
            mSampleDialogPresenter.setOnClickListener(mPendingOnClickListener);
            mPendingOnClickListener = null;
        }
        mSampleDialogFragmentBinding.setPresenter(mSampleDialogPresenter);
    }

    private SampleDialogPresenter.DialogPresenterListener mDialogPresenterListener = new SampleDialogPresenter.DialogPresenterListener() {
        @Override
        public void bindDialogConfiguration(DialogConfiguration dialogConfiguration) {
            mSampleDialogFragmentBinding.setDialogConfiguration(dialogConfiguration);
        }

        @Override
        public DialogInterface getDialog() {
            return SampleDialogFragment.this.getDialog();
        }
    };

    public void setListener(DialogInterface.OnClickListener onClickListener) {
        if (mSampleDialogPresenter != null) {
            mSampleDialogPresenter.setOnClickListener(onClickListener);
        } else {
            mPendingOnClickListener = onClickListener;
        }
    }

    public int getDialogId() {
        return mDialogConfiguration.getDialogId();
    }

    private Handler getUiHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    public void postShow(final FragmentManager fragmentManager, final String tag) {
        getUiHandler().post(new Runnable() {
            @Override
            public void run() {
                show(fragmentManager, tag);
            }
        });
    }

    /**
     * Convenience call-through to DialogConfiguration.Builder
     */
    public static class Builder {
        private DialogConfiguration.Builder mDialogConfigurationBuilder;
        private DialogInterface.OnClickListener mOnClickListener;

        public Builder() {
            mDialogConfigurationBuilder = new DialogConfiguration.Builder();
        }

        public Builder setDialogId(int dialogId) {
            mDialogConfigurationBuilder.setDialogId(dialogId);
            return this;
        }

        public Builder setIconDrawableResourceId(int iconDrawableResourceId) {
            mDialogConfigurationBuilder.setIconDrawableResourceId(iconDrawableResourceId);
            return this;
        }

        public Builder setTitle(String title) {
            mDialogConfigurationBuilder.setTitle(title);
            return this;
        }

        public Builder setMessage(String message) {
            mDialogConfigurationBuilder.setMessage(message);
            return this;
        }

        public Builder setPositiveText(String positiveText) {
            mDialogConfigurationBuilder.setPositiveText(positiveText);
            return this;
        }

        public Builder setNeutralText(String neutralText) {
            mDialogConfigurationBuilder.setNeutralText(neutralText);
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            mDialogConfigurationBuilder.setNegativeText(negativeText);
            return this;
        }

        public Builder setProgressVisible(boolean visible) {
            mDialogConfigurationBuilder.setProgressVisible(visible);
            return this;
        }

        public Builder setOnClickListener(DialogInterface.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
            return this;
        }

        public SampleDialogFragment build() {
            if (mOnClickListener == null) {
                mOnClickListener = new JustDismissOnClickListener();
            }
            return newInstance(new DialogConfiguration(mDialogConfigurationBuilder), mOnClickListener);
        }

    }
}

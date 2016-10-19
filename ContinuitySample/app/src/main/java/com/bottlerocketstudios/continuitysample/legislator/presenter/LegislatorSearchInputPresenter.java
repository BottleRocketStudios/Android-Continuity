package com.bottlerocketstudios.continuitysample.legislator.presenter;

import android.databinding.Observable;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.BR;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorSearchInputViewModel;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;

import java.util.regex.Pattern;


/**
 * Created on 10/6/16.
 */

public class LegislatorSearchInputPresenter implements ContinuousObject {

    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^[0-9]{5}$");

    private Listener mListener;
    private LegislatorSearchInputViewModel mViewModel = new LegislatorSearchInputViewModel();

    public LegislatorSearchInputPresenter() {
        mViewModel.addOnPropertyChangedCallback(mViewModelCallback);
    }

    public void bindListener(Listener listener) {
        mListener = listener;
        mListener.bindViewModel(mViewModel);
    }

    @Override
    public void onContinuityAnchorDestroyed(Object anchor) {
        mListener = null;
    }

    @Override
    public void onContinuityDiscard() {
        GroundControl.onDestroy(this);
    }

    private final Observable.OnPropertyChangedCallback mViewModelCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int fieldId) {
            LegislatorSearchInputViewModel viewModel = (LegislatorSearchInputViewModel) observable;
            if (fieldId == BR.legislatorName) {
                if (!viewModel.isNameValid()) validateName(viewModel.getLegislatorName());
            } else if (fieldId == BR.zipCode) {
                if (!viewModel.isZipValid()) validateZip(viewModel.getZipCode());
            }
        }
    };

    public void onNameSearchTap(LegislatorSearchInputViewModel viewModel) {
        if (validateName(viewModel.getLegislatorName())) {
            mListener.launchSearchForName(viewModel.getLegislatorName());
        }
    }

    private boolean validateName(String legislatorName) {
        boolean valid = legislatorName.length() >= 2;
        mViewModel.setNameValid(valid);
        return valid;
    }

    public void onZipSearchTap(LegislatorSearchInputViewModel viewModel) {
        if (validateZip(viewModel.getZipCode())) {
            mListener.launchSearchForZip(viewModel.getZipCode());
        }
    }

    private boolean validateZip(String zipCode) {
        boolean valid = ZIP_CODE_PATTERN.matcher(zipCode).matches();
        mViewModel.setZipValid(valid);
        return valid;
    }

    public void onSearchLocationTap() {
        mListener.launchSearchForLocation();
    }

    public interface Listener {
        void launchSearchForZip(String zip);
        void launchSearchForLocation();
        void launchSearchForName(String name);
        void bindViewModel(LegislatorSearchInputViewModel viewModel);
    }
}

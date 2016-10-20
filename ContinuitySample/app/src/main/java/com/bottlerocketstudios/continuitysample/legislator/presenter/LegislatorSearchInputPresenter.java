package com.bottlerocketstudios.continuitysample.legislator.presenter;

import android.databinding.Observable;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.BR;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorSearchInputViewModel;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;

import java.util.regex.Pattern;


/**
 * Presenter to handle legislator search input.
 */
public class LegislatorSearchInputPresenter implements ContinuousObject {

    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^[0-9]{5}$");

    //The ViewModel is never replaced while the presenter is alive, only updated.
    private final LegislatorSearchInputViewModel mViewModel = new LegislatorSearchInputViewModel();

    private Listener mListener;

    public LegislatorSearchInputPresenter() {
        //Watch for changes to the view model, see listener below.
        mViewModel.addOnPropertyChangedCallback(mViewModelCallback);
    }

    /**
     * Call this method to set the listener for this Presenter and have the Presenter bind ViewModels via the Listener.
     */
    public void bindListener(Listener listener) {
        mListener = listener;
        mListener.bindViewModel(mViewModel);
    }

    @Override
    public void onContinuityAnchorDestroyed(Object anchor) {
        //The UI has gone away, remove the reference to it.
        mListener = null;
    }

    @Override
    public void onContinuityDiscard() {
        //Notify GroundControl that we are no longer interested in any ongoing asynchronous work started here.
        //We don't even start any async work in this presenter, but it is a good habit and probably belongs in a BasePresenter.
        //I stopped short of making a BasePresenter with a base Listener to keep things simple.
        GroundControl.onDestroy(this);
    }

    //We replace TextWatchers with observation of a two-way bound ViewModel.
    private final Observable.OnPropertyChangedCallback mViewModelCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int fieldId) {
            LegislatorSearchInputViewModel viewModel = (LegislatorSearchInputViewModel) observable;
            //Switch will not work on BR fields, c'est la vie.
            if (fieldId == BR.legislatorName) {
                //Don't show validation failure unless they've already failed validation by submitting.
                //Eagerly show validation acceptance if they have failed validation.
                if (!viewModel.isNameValid()) validateName(viewModel.getLegislatorName());
            } else if (fieldId == BR.zipCode) {
                //Don't show validation failure unless they've already failed validation by submitting.
                //Eagerly show validation acceptance if they have failed validation.
                if (!viewModel.isZipValid()) validateZip(viewModel.getZipCode());
            }
        }
    };

    public void onNameSearchTap(LegislatorSearchInputViewModel viewModel) {
        //Perform validation on the search field then have listener navigate to search result.
        if (validateName(viewModel.getLegislatorName())) {
            mListener.launchSearchForName(viewModel.getLegislatorName());
        }
    }

    private boolean validateName(String legislatorName) {
        boolean valid = legislatorName.length() >= 2;
        //By updating the view model, the UI is updated with validation error information.
        mViewModel.setNameValid(valid);
        return valid;
    }

    public void onZipSearchTap(LegislatorSearchInputViewModel viewModel) {
        //Perform validation on the search field then have listener navigate to search result.
        if (validateZip(viewModel.getZipCode())) {
            mListener.launchSearchForZip(viewModel.getZipCode());
        }
    }

    private boolean validateZip(String zipCode) {
        boolean valid = ZIP_CODE_PATTERN.matcher(zipCode).matches();
        //By updating the view model, the UI is updated with validation error information.
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

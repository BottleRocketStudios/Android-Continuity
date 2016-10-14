package com.bottlerocketstudios.continuitysample.legislator.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bottlerocketstudios.continuitysample.BR;
import com.bottlerocketstudios.continuitysample.R;

/**
 * Created on 10/6/16.
 */

public class LegislatorSearchInputViewModel extends BaseObservable {

    private String mLegislatorName = "";
    private boolean mNameValid = true;
    private String mZipCode = "";
    private boolean mZipValid = true;

    public String getNameValidationFailureText(Context context, boolean nameValid) {
        return nameValid ? "" : context.getString(R.string.lsif_name_error);
    }

    public String getZipValidationFailureText(Context context, boolean zipValid) {
        return zipValid ? "" : context.getString(R.string.lsif_zip_error);
    }

    @Bindable
    public String getLegislatorName() {
        return mLegislatorName;
    }

    public void setLegislatorName(String legislatorName) {
        mLegislatorName = legislatorName;
        notifyPropertyChanged(BR.legislatorName);
    }

    @Bindable
    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
        notifyPropertyChanged(BR.zipCode);
    }

    @Bindable
    public boolean isZipValid() {
        return mZipValid;
    }

    public void setZipValid(boolean zipValid) {
        mZipValid = zipValid;
        notifyPropertyChanged(BR.zipValid);
    }

    @Bindable
    public boolean isNameValid() {
        return mNameValid;
    }

    public void setNameValid(boolean nameValid) {
        mNameValid = nameValid;
        notifyPropertyChanged(BR.nameValid);
    }
}

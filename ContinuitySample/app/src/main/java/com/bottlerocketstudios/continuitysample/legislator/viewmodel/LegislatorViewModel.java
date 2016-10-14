package com.bottlerocketstudios.continuitysample.legislator.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.bottlerocketstudios.continuitysample.BR;
import com.bottlerocketstudios.continuitysample.R;

/**
 * Created on 9/14/16.
 */
public class LegislatorViewModel extends BaseObservable implements Parcelable {
    private String mLastName;
    private String mStateName;
    private String mFirstName;
    private String mMiddleName;
    private long mDistrict;
    private boolean mInOffice;
    private String mState;
    private String mOcEmail;
    private String mParty;
    private String mWebsite;
    private String mGovtrackId;
    private String mFacebookId;
    private String mBioguideId;
    private String mNickname;
    private String mContactForm;
    private String mPhone;
    private String mTitle;
    private String mNameSuffix;
    private String mTwitterId;
    private String mChamber;
    private String mStateRank;
    private String mYoutubeId;

    private boolean mFavorite;
    private String mImageUrl;

    public LegislatorViewModel() {}

    protected LegislatorViewModel(Parcel in) {
        mLastName = in.readString();
        mStateName = in.readString();
        mFirstName = in.readString();
        mMiddleName = in.readString();
        mDistrict = in.readLong();
        mInOffice = in.readByte() != 0;
        mState = in.readString();
        mOcEmail = in.readString();
        mParty = in.readString();
        mWebsite = in.readString();
        mGovtrackId = in.readString();
        mFacebookId = in.readString();
        mBioguideId = in.readString();
        mNickname = in.readString();
        mContactForm = in.readString();
        mPhone = in.readString();
        mTitle = in.readString();
        mNameSuffix = in.readString();
        mTwitterId = in.readString();
        mChamber = in.readString();
        mStateRank = in.readString();
        mYoutubeId = in.readString();
        mFavorite = in.readByte() != 0;
        mImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLastName);
        dest.writeString(mStateName);
        dest.writeString(mFirstName);
        dest.writeString(mMiddleName);
        dest.writeLong(mDistrict);
        dest.writeByte((byte) (mInOffice ? 1 : 0));
        dest.writeString(mState);
        dest.writeString(mOcEmail);
        dest.writeString(mParty);
        dest.writeString(mWebsite);
        dest.writeString(mGovtrackId);
        dest.writeString(mFacebookId);
        dest.writeString(mBioguideId);
        dest.writeString(mNickname);
        dest.writeString(mContactForm);
        dest.writeString(mPhone);
        dest.writeString(mTitle);
        dest.writeString(mNameSuffix);
        dest.writeString(mTwitterId);
        dest.writeString(mChamber);
        dest.writeString(mStateRank);
        dest.writeString(mYoutubeId);
        dest.writeByte((byte) (mFavorite ? 1 : 0));
        dest.writeString(mImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LegislatorViewModel> CREATOR = new Creator<LegislatorViewModel>() {
        @Override
        public LegislatorViewModel createFromParcel(Parcel in) {
            return new LegislatorViewModel(in);
        }

        @Override
        public LegislatorViewModel[] newArray(int size) {
            return new LegislatorViewModel[size];
        }
    };

    @Bindable
    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
        notifyPropertyChanged(BR.favorite);
    }

    public String formatFullName(Context context) {
        if (!TextUtils.isEmpty(getNickname())) {
            return context.getString(R.string.legislator_full_name_format_with_nickname, getFirstName(), getNickname(), getLastName());
        } else {
            return context.getString(R.string.legislator_full_name_format, getFirstName(), getLastName());
        }
    }

    public String formatPartyAndState(Context context) {
        return context.getString(R.string.legislator_party_and_state, getParty(), getStateName());
    }

    public int getFavoriteDrawableTint(Context context, boolean isFavorite) {
        return ContextCompat.getColor(context, isFavorite ? R.color.favorite_selected : R.color.favorite_unselected);
    }

    public boolean isTwitterVisible(String twitterId) {
        return !TextUtils.isEmpty(twitterId);
    }

    public boolean isFacebookVisible(String facebookId) {
        return !TextUtils.isEmpty(facebookId);
    }

    public boolean isYouTubeVisible(String youtubeId) {
        return !TextUtils.isEmpty(youtubeId);
    }

    public boolean isWebsiteVisible(String website) {
        return !TextUtils.isEmpty(website);
    }

    /*  Regular Accessor Methods */

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getStateName() {
        return mStateName;
    }

    public void setStateName(String stateName) {
        mStateName = stateName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public void setMiddleName(String middleName) {
        mMiddleName = middleName;
    }

    public long getDistrict() {
        return mDistrict;
    }

    public void setDistrict(long district) {
        mDistrict = district;
    }

    public boolean isInOffice() {
        return mInOffice;
    }

    public void setInOffice(boolean inOffice) {
        mInOffice = inOffice;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getOcEmail() {
        return mOcEmail;
    }

    public void setOcEmail(String ocEmail) {
        mOcEmail = ocEmail;
    }

    public String getParty() {
        return mParty;
    }

    public void setParty(String party) {
        mParty = party;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getGovtrackId() {
        return mGovtrackId;
    }

    public void setGovtrackId(String govtrackId) {
        mGovtrackId = govtrackId;
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public void setFacebookId(String facebookId) {
        mFacebookId = facebookId;
    }

    public String getBioguideId() {
        return mBioguideId;
    }

    public void setBioguideId(String bioguideId) {
        mBioguideId = bioguideId;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public String getContactForm() {
        return mContactForm;
    }

    public void setContactForm(String contactForm) {
        mContactForm = contactForm;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getNameSuffix() {
        return mNameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        mNameSuffix = nameSuffix;
    }

    public String getTwitterId() {
        return mTwitterId;
    }

    public void setTwitterId(String twitterId) {
        mTwitterId = twitterId;
    }

    public String getChamber() {
        return mChamber;
    }

    public void setChamber(String chamber) {
        mChamber = chamber;
    }

    public String getStateRank() {
        return mStateRank;
    }

    public void setStateRank(String stateRank) {
        mStateRank = stateRank;
    }

    public String getYoutubeId() {
        return mYoutubeId;
    }

    public void setYoutubeId(String youtubeId) {
        mYoutubeId = youtubeId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}

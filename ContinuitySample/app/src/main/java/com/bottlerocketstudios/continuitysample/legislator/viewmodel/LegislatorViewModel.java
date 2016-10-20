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
 * ViewModel used to display legislator information. We used the automatic parcelable generation
 * feature in Android Studio as well as the automatic getter/setter generation after copying required
 * private fields from the Legislator model object.
 *
 * This is Parcelable because it is passed from the list views to the detail view.
 */
public class LegislatorViewModel extends BaseObservable implements Parcelable {
    private String mLastName;
    private String mStateName;
    private String mFirstName;
    private String mParty;
    private String mWebsite;
    private String mGovtrackId;
    private String mFacebookId;
    private String mBioguideId;
    private String mNickname;
    private String mTwitterId;
    private String mChamber;
    private String mYoutubeId;

    private boolean mFavorite;
    private String mImageUrl;

    public LegislatorViewModel() {}

    protected LegislatorViewModel(Parcel in) {
        mLastName = in.readString();
        mStateName = in.readString();
        mFirstName = in.readString();
        mParty = in.readString();
        mWebsite = in.readString();
        mGovtrackId = in.readString();
        mFacebookId = in.readString();
        mBioguideId = in.readString();
        mNickname = in.readString();
        mTwitterId = in.readString();
        mChamber = in.readString();
        mYoutubeId = in.readString();
        mFavorite = in.readByte() != 0;
        mImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLastName);
        dest.writeString(mStateName);
        dest.writeString(mFirstName);
        dest.writeString(mParty);
        dest.writeString(mWebsite);
        dest.writeString(mGovtrackId);
        dest.writeString(mFacebookId);
        dest.writeString(mBioguideId);
        dest.writeString(mNickname);
        dest.writeString(mTwitterId);
        dest.writeString(mChamber);
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

    /**
     * Gets the tint of the favorite drawable for the ImageView. We already have the attribute
     * mFavorite, but we take isFavorite as a parameter in the layout because it will cause DataBinding
     * to wire up an update anytime the favorite state changes.
     */
    public int getFavoriteDrawableTint(Context context, boolean isFavorite) {
        return ContextCompat.getColor(context, isFavorite ? R.color.favorite_selected : R.color.favorite_unselected);
    }

    public boolean isTwitterVisible() {
        return !TextUtils.isEmpty(getTwitterId());
    }

    public boolean isFacebookVisible() {
        return !TextUtils.isEmpty(getFacebookId());
    }

    public boolean isYouTubeVisible() {
        return !TextUtils.isEmpty(getYoutubeId());
    }

    public boolean isWebsiteVisible() {
        return !TextUtils.isEmpty(getWebsite());
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

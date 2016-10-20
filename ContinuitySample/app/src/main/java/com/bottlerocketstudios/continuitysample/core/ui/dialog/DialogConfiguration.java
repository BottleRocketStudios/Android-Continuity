package com.bottlerocketstudios.continuitysample.core.ui.dialog;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Configuration object for a SimpleDialogFragment
 */
public class DialogConfiguration implements Parcelable {
    private final int mDialogId;
    private final int mIconDrawableResourceId;
    private final String mTitle;
    private final String mMessage;
    private final String mPositiveText;
    private final String mNegativeText;
    private final String mNeutralText;
    private final boolean mProgressVisible;

    public DialogConfiguration(Builder builder) {
        mDialogId = builder.dialogId;
        mIconDrawableResourceId = builder.iconDrawableResourceId;
        mTitle = builder.title;
        mMessage = builder.message;
        mPositiveText = builder.positiveText;
        mNegativeText = builder.negativeText;
        mNeutralText = builder.neutralText;
        mProgressVisible = builder.progressVisible;
    }

    protected DialogConfiguration(Parcel in) {
        mDialogId = in.readInt();
        mIconDrawableResourceId = in.readInt();
        mTitle = in.readString();
        mMessage = in.readString();
        mPositiveText = in.readString();
        mNegativeText = in.readString();
        mNeutralText = in.readString();
        mProgressVisible = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDialogId);
        dest.writeInt(mIconDrawableResourceId);
        dest.writeString(mTitle);
        dest.writeString(mMessage);
        dest.writeString(mPositiveText);
        dest.writeString(mNegativeText);
        dest.writeString(mNeutralText);
        dest.writeByte((byte) (mProgressVisible ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DialogConfiguration> CREATOR = new Creator<DialogConfiguration>() {
        @Override
        public DialogConfiguration createFromParcel(Parcel in) {
            return new DialogConfiguration(in);
        }

        @Override
        public DialogConfiguration[] newArray(int size) {
            return new DialogConfiguration[size];
        }
    };

    public int getDialogId() {
        return mDialogId;
    }

    public int getIconDrawableResourceId() {
        return mIconDrawableResourceId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getPositiveText() {
        return mPositiveText;
    }

    public String getNeutralText() {
        return mNeutralText;
    }

    public String getNegativeText() {
        return mNegativeText;
    }

    public boolean isPositiveVisible() {
        return !TextUtils.isEmpty(getPositiveText());
    }

    public boolean isNeutralVisible() {
        return !TextUtils.isEmpty(getNeutralText());
    }

    public boolean isNegativeVisible() {
        return !TextUtils.isEmpty(getNegativeText());
    }

    public boolean isProgressVisible() {
        return mProgressVisible;
    }

    public static class Builder {
        private int dialogId;
        private int iconDrawableResourceId;
        private String title;
        private String message;
        private String positiveText;
        private String neutralText;
        private String negativeText;
        private boolean progressVisible;

        public Builder setDialogId(int dialogId) {
            this.dialogId = dialogId;
            return this;
        }

        public Builder setIconDrawableResourceId(int iconDrawableResourceId) {
            this.iconDrawableResourceId = iconDrawableResourceId;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setNeutralText(String neutralText) {
            this.neutralText = neutralText;
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder setProgressVisible(boolean visible) {
            this.progressVisible = visible;
            return this;
        }

        public DialogConfiguration build() {
            return new DialogConfiguration(this);
        }
    }
}
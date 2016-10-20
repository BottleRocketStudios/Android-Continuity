package com.bottlerocketstudios.continuitysample.core.ui;

import android.content.Context;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;

/**
 * User facing translation of ResponseStatus to a string resource.
 */
public class ResponseStatusTranslator {

    public static String getErrorString(Context context, ResponseStatus responseStatus) {
        switch (responseStatus) {
            case INVALID_DATA:
                return context.getString(R.string.lsrf_unexpected_data);
            default:
                return context.getString(R.string.lsrf_network_error);
        }
    }
}

package com.bottlerocketstudios.continuitysample.core.util;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * Created on 10/5/16.
 */

public class JSONUtil {

    private static final String NULL_LITERAL = "null";

    public static String optString(JSONObject jsonObject, String field, String defaultValue) {
        String result = jsonObject.optString(field, defaultValue);
        if (TextUtils.isEmpty(result) || NULL_LITERAL.equals(result)) {
            return defaultValue;
        }
        return result;
    }

}

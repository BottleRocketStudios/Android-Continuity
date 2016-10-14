package com.bottlerocketstudios.continuitysample.core.serialization;

import com.bottlerocketstudios.continuitysample.core.api.NetworkResponseStatus;
import com.bottlerocketstudios.continuitysample.core.api.ResponseHandler;
import com.bottlerocketstudios.continuitysample.core.util.LogIt;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created on 9/14/16.
 */
public abstract class JsonObjectResponseHandler<T> implements ResponseHandler<T> {
    private static final String TAG = JsonObjectResponseHandler.class.getSimpleName();

    private JSONObject mJsonObject;

    @Override
    public NetworkResponseStatus processResponseStatus(Response response) {
        if (response.isSuccessful()) {
            try {
                mJsonObject = new JSONObject(response.body().string());
                return NetworkResponseStatus.SUCCESS;
            } catch (JSONException e) {
                LogIt.e(TAG, "Caught org.json.JSONException", e);
                return NetworkResponseStatus.PARSE_ERROR;
            } catch (IOException e) {
                LogIt.e(TAG, "Caught java.io.IOException", e);
                return NetworkResponseStatus.IO_ERROR;
            }
        } else {
            return NetworkResponseStatus.SERVER_ERROR;
        }
    }

    protected JSONObject getJsonObject() {
        return mJsonObject;
    }

    @Override
    public T processResponse(Response response) {
        try {
            return processJson(getJsonObject());
        } catch (JSONException e) {
            LogIt.e(TAG, "Caught org.json.JSONException", e);
        }
        return null;
    }

    protected abstract T processJson(JSONObject jsonObject) throws JSONException;
}

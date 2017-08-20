package com.tarunisrani.sminq.network;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tarunisrani.sminq.listeners.NetworkResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by tarunisrani on 1/31/17.
 */
public class ServerNetworkCall {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static void doGetMethod(String url, final NetworkResponseListener listener){


        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request1, IOException e) {
                if(listener!=null){
                    listener.onFail();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // do something wih the result
                    if(listener!=null){
                        try {
                            listener.onSuccess(new JSONObject(response.body().string()));
                        }catch (JSONException exp){
                            exp.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void doPostMethod(String url, JSONObject jsonObject, final NetworkResponseListener listener){

        Log.e("Request", jsonObject.toString());

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request1, IOException e) {
                if(listener!=null){
                    listener.onFail();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // do something wih the result
                    if(listener!=null){
                        try {
                            listener.onSuccess(new JSONObject(response.body().string()));
                        }catch (JSONException exp){
                            exp.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}




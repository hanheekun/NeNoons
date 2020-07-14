package com.pixelro.nenoons.server;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

public class HttpTask extends AsyncTask<HashMap, Void, String> {

    String url;
    Handler handler;

    public HttpTask(String url, Handler handler) {
        this.url = url;
        this.handler = handler;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // JSON Object
        if (result!=null && !result.equals("")) {
            System.out.println(result);
            if (handler!=null) {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected String doInBackground(HashMap... hashMaps) {
        if (url==null || url.equals("")) {
            return null;
        }
        String resp = HttpConnectionUtil.postRequest(url, hashMaps[0]);
        System.out.println(resp);
        return resp;
    }
}
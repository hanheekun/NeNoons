package com.pixelro.nenoons;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pixelro.nenoons.server.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//            } else {
//                // Handle message within 10 seconds
//                handleNow(remoteMessage.getData());
//            }
            handleNow(remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(Map<String,String> map) {
        Log.d(TAG, "Short lived task is done.");
        map.forEach((s, s2) -> {
            System.out.println( "" + s + " " + s2);
        });
        String title = map.get("title").toString();
        String message = map.get("message").toString();
        String link = map.get("link").toString();
        String action = map.get("action").toString();
        String value = map.get("value").toString();
        sendNotification(title, message, link, action, value);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody, String link, String action, String value) {

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("link", link);
        intent.putExtra("action", action);
        intent.putExtra("value", value);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        // send token
        String fcmToken = s;
        String token = getToken(MyFirebaseMessagingService.this);
        String fcmTokenLocal = getToken(MyFirebaseMessagingService.this);
        //FCM 토큰 전송
        if (fcmToken!=null && !fcmToken.equals("")  && token!=null && token.equals("")) {
            if (fcmTokenLocal == "" || !fcmToken.equals(fcmTokenLocal)) {
                // 신규 fcmToken 서버에 저장
                HashMap<String, String> param = new HashMap<String, String>();
                // 파라메터는 넣기 예
                param.put("token", token);    //PARAM
                param.put("fcmToken", fcmToken);    // 서버연결 20200716 이름 추가 필요
                Handler handler = new Handler(message -> {
                    Bundle bundle = message.getData();
                    String result = bundle.getString("result");
                    System.out.println(result);
                    try {
                        JSONObject j = new JSONObject(result);
                        String error = j.getString("error");
                        String msg = j.getString("msg");
                        System.out.println(error);
                        System.out.println(msg);
                        if (error != "null") {
                            //Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                            System.out.println("저장 실패");
                        } else if (msg != "null") {
                            //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            System.out.println("저장 성공");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 실패
                        //Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                // API 주소와 위 핸들러 전달 후 실행.
//                                new HttpTask("https://nenoonsapi.du.r.appspot.com/android/update_user_fcm", handler).execute(param);
                new HttpTask("http://192.168.1.162:4002/android/update_user_fcm", handler).execute(param);

            }

        }

    }

    public String getToken(Context context) {
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN, "");
    }

    public String getFcmToken(Context context) {
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.FCM_TOKEN, "");
    }

    public void setFcmToken(Context context, String fcmToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.FCM_TOKEN, fcmToken);
        editor.commit();
    }
}
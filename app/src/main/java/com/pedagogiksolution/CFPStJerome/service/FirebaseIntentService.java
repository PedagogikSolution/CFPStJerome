package com.pedagogiksolution.CFPStJerome.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pedagogiksolution.CFPStJerome.MainActivity;
import com.pedagogiksolution.CFPStJerome.R;

import java.util.Map;
import java.util.Random;

public class FirebaseIntentService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String messageId = remoteMessage.getMessageId();
        Map<String,String> mData = remoteMessage.getData();
        if(mData.get("url")==null) {
            sendNotification(mData.get("title"),mData.get("body"));
        } else {
            sendNotificationWithData(mData.get("title"),mData.get("body"),mData.get("url"),messageId);
        }

    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }


    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("TITLE",title);
        intent.putExtra("BODY",body);
        intent.putExtra("PUSH",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body))
                        .setAutoCancel(true)

                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "CFP Performance Plus",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        Random random = new Random();
        int m = random.nextInt(119999 - 1000) + 1000;
        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }


    private void sendNotificationWithData(String title, String body, String url, String messageId) {


        Intent intentUrl = new Intent(this,MainActivity.class);
        intentUrl.putExtra("URL",url);
        intentUrl.putExtra("BODY",body);
        intentUrl.putExtra("TITLE",title);
        intentUrl.putExtra("PUSH",true);
        intentUrl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent urlPendingIntent = PendingIntent.getActivity(this,0,intentUrl,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentUrl2 = new Intent(this,MainActivity.class);
        intentUrl2.putExtra("URL",url);
        intentUrl2.putExtra("TITLE",title);
        intentUrl2.putExtra("BODY",body);
        intentUrl2.putExtra("PUSH2",true);
        intentUrl2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intentUrl2.setAction("dummy_action_" + messageId);
        PendingIntent urlPendingIntent2 = PendingIntent.getActivity(this,0,intentUrl2,PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body))
                        .addAction(R.drawable.common_google_signin_btn_icon_light,"Aller au lien",urlPendingIntent2)
                        .addAction(R.drawable.common_google_signin_btn_icon_light,"Sauvegarder",urlPendingIntent)
                        .setSound(defaultSoundUri)
                        .setContentIntent(urlPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "CFP Performance Plus",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        Random random = new Random();
        int m = random.nextInt(119999 - 1000) + 1000;
        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());

    }

}


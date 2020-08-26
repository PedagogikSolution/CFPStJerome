package com.pedagogiksolution.CFPStJerome.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.pedagogiksolution.CFPStJerome.MainActivity;
import com.pedagogiksolution.CFPStJerome.R;


public class MyRappelService extends IntentService {
	int mId,mId2;
	SharedPreferences sharedPref;
	
	public MyRappelService() {
		super("MyRappelService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		sharedPref = this.getSharedPreferences("rappel",0);
		mId2 = sharedPref.getInt("rappel", 0);
		mId= mId2 +1;

		String channelId = getString(R.string.default_notification_channel_id);

		Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this, channelId)
		        .setSmallIcon(R.drawable.notification)
		        .setContentTitle("Rappel")
		        .setContentText("Tu as un rappel, clique ici pour le voir")
				.setSound(defaultSoundUri)
		        .setAutoCancel(true);
				
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		resultIntent.putExtra("RAPPEL", true);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );

		notificationBuilder.setContentIntent(resultPendingIntent);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(channelId,
					"Poly Lavigne",
					NotificationManager.IMPORTANCE_DEFAULT);
			notificationManager.createNotificationChannel(channel);
		}
		// mId allows you to update the notification later on.
        notificationManager.notify(mId, notificationBuilder.build());


		sharedPref = getSharedPreferences("rappel",0);
   		SharedPreferences.Editor editor2 = sharedPref.edit();
   		editor2.putInt("rappel", mId);
   		editor2.commit();
		
		
		
	}

	
	



}


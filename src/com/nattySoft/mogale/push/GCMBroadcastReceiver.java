package com.nattySoft.mogale.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.analytics.n;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nattySoft.mogale.MainActivity;
import com.nattySoft.mogale.net.CommunicationHandler.Action;

public final class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

	private static final String LOG_TAG = GCMBroadcastReceiver.class.getSimpleName();
	
	

	@Override
	public void onReceive(final Context context, final Intent intent) {

		Log.d(LOG_TAG, "onReceive(" + intent + ")");

		final Bundle extras = intent.getExtras();
		final GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(context);
		final String messageType = googleCloudMessaging.getMessageType(intent);
		
		if (!extras.isEmpty()) {

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				
				Log.d(LOG_TAG,
	                    "Message received: " + extras.toString());
				Log.d(LOG_TAG,
	                    "Message received: " + extras.getString("message"));
				
					String message = extras.getString("description")+" : "+extras.getString("message");
					startNotification(context, message);
				
				
			}
		}
	}

	private void startNotification(final Context context, final String message) {

		final Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
		
		final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(com.nattySoft.mogale.R.drawable.ic_launcher)
				.setContentTitle("Mogale")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message);
		
		notificationManager.notify(1337, mBuilder.build());
	}
}
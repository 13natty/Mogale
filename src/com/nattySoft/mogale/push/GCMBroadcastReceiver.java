package com.nattySoft.mogale.push;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
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
import com.google.android.gms.wearable.NodeApi.GetConnectedNodesResult;
import com.nattySoft.mogale.ChatActivity;
import com.nattySoft.mogale.MainActivity;
import com.nattySoft.mogale.listener.ChatResponceListener;
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
	                    "Message received: " + extras.getString("body"));
					if(extras.getString("type").equalsIgnoreCase("chat"))
					{
						RunningTaskInfo info = isAppInForeground(context); 
						if(info!=null)
						{
							String className = info.topActivity.getShortClassName();
							if(className.equalsIgnoreCase(".ChatActivity"))
							{
								ChatActivity listenerInterface;
								listenerInterface = (ChatActivity) context.getApplicationContext();
								listenerInterface.hasResponse(extras.getString("body"), extras.getString("senderName"), extras.getString("timestamp").substring(0,extras.getString("timestamp").indexOf(" ")));
							}
						}
						else
						{
							//dislay a push notification
							startNotification(context, extras.getString("senderName")+" : "+extras.getString("body"));
						}
					}
				
					String message = extras.getString("description")+" : "+extras.getString("message");
					
				
				
			}
		}
	}
	
	// foreground---
    public static RunningTaskInfo isAppInForeground(Context context) {
        List<RunningTaskInfo> tasks =
            ((ActivityManager) context.getSystemService(
             Context.ACTIVITY_SERVICE))
             .getRunningTasks(1);
        if (tasks.isEmpty()) {
            return null;
        }
        
        RunningTaskInfo info = tasks.get(0);
        String packageName = info.topActivity.getPackageName();
        String className = info.topActivity.getShortClassName();
      
        return info;
//        return tasks
//            .get(0)
//            .topActivity
//            .getPackageName()
//            .equalsIgnoreCase(context.getPackageName());
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
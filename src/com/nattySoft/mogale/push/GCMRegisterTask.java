package com.nattySoft.mogale.push;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.Toast;

import com.nattySoft.mogale.AppConstants;
import com.nattySoft.mogale.listener.RequestResponseListener;
import com.nattySoft.mogale.net.CommunicationHandler;
import com.nattySoft.mogale.util.Preferences;
import com.google.android.gms.gcm.GoogleCloudMessaging;

final class GCMRegisterTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private String employeeNumber;
	private RequestResponseListener listener;
	private ProgressDialog dialog;
	
	GCMRegisterTask(final Context context, String employeeNumber, RequestResponseListener listener, ProgressDialog dialog) {
		this.context = context;
		this.employeeNumber = employeeNumber;
		this.listener = listener;
		this.dialog = dialog;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		final GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(context);
		
		String registrationId = null;
		try {
			registrationId = googleCloudMessaging.register(AppConstants.Config.GCM_SENDER_ID);
			Log.d("GCMRegisterTask", "Device registered, registration ID= "+registrationId);//context, , Toast.LENGTH_LONG).show();
		} catch (IOException ioe) {
			Log.e("registerTask", "ioe.getMessage() "+ioe.getMessage());
		}
		
		if (registrationId != null) {
			Log.d("registerTask", "registrationId "+registrationId);
			Preferences.savePreference(context, AppConstants.PreferenceKeys.KEY_REGISTRATION_ID, registrationId);
			
			CommunicationHandler.registerForPush(context, registrationId, employeeNumber, listener, dialog);
			//Toast.makeText(context, "Device registered For Push", Toast.LENGTH_LONG).show();
		}
		
		return null;
	}
}

package com.nattySoft.mogale.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;

import com.nattySoft.mogale.AppConstants;
import com.nattySoft.mogale.RegistrationActivity;
import com.nattySoft.mogale.listener.RequestResponseListener;
import com.nattySoft.mogale.push.GCMer;
import com.nattySoft.mogale.util.Preferences;

public class CommunicationHandler {

	private static final String SERVER_URL = AppConstants.Config.SERVER_URL;
	private static final String LOG_TAG = CommunicationHandler.class.getSimpleName();

	public enum Action {
		GET_ALL_OPEN_INCIDENCES, REGISTER, GET_PRODUCTS;
	}

	public static void registerForPush(final Context context, String deviceId, String employeeNumber, RequestResponseListener listener, ProgressDialog dialog) {
		
//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//        nameValuePairs.add(new BasicNameValuePair("nav", "registerdevice.mobi"));
//        nameValuePairs.add(new BasicNameValuePair("deviceid", deviceId));
        
        JSONObject json = new JSONObject();
        try {
			json.accumulate("nav", "registerdevice.mobi");
			json.accumulate("registrationId", deviceId);
	        json.accumulate("employeeNum", employeeNumber);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        
		Log.d(LOG_TAG, "SERVER_URL "+SERVER_URL);
		Log.d(LOG_TAG, "json.toString() "+json.toString());
		
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
	}
	
	public static void getBalance(Context context, RequestResponseListener listener, ProgressDialog dialog) {
		
		String userID = Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_USER_ID);
		
		if (userID != null) {
			
			String serverUrl = SERVER_URL + userID.replaceAll("\"", "") + "/balance";
			new ConnectionManager().get(context, listener, dialog, serverUrl, "true", "true");
		}
	}
	
	public static void getMenu(Context context, RequestResponseListener listener, ProgressDialog dialog) {
		
		String userID = Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_USER_ID);
		
		if (userID != null) {
			
			String serverUrl = "http://spazacloudservice.cloudapp.net/Spaz.svc/products";
			new ConnectionManager().get(context, listener, dialog, serverUrl, "true", "true");
		}
	}

	public static void getOpenIncidents(Context context, RequestResponseListener listener, ProgressDialog dialog) {
        
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//        nameValuePairs.add(new BasicNameValuePair("nav", "incidents.mobi"));
//        nameValuePairs.add(new BasicNameValuePair("userid", "00000"));
//        nameValuePairs.add(new BasicNameValuePair("password", "mogale"));
		
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "incidents.mobi");
			json.accumulate("userid", "00000");
			json.accumulate("password", "mogale");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d(LOG_TAG, "SERVER_URL "+SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() "+json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
	}

	public static void registerUser(Context context, RequestResponseListener listener, ProgressDialog dialog,
			String employeeNumber) {
		
		GCMer.onCreate(context, employeeNumber, listener, dialog);
		
	}

	/**
	 * Posts a request for order with the given parameters
	 * 
	 * @param context
	 * @param listener
	 */
//	public static void postPurchase(Context context, RequestResponseListener listener, List<BasketItem> items, ProgressDialog dialog) {
//
//		String userID = Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_USER_ID);
//
//		if (userID != null) {
//			String serverUrl = SERVER_URL + userID.replaceAll("\"", "") + "/purchase";
//			String json = "[";
//
//			Log.d(LOG_TAG, "serverURL : " + serverUrl);
//			if (items != null && items.size() > 0) {
//
//				if (items.size() > 1) {
//
//					for (int i = 0; i < items.size(); i++) {
//						json += "{";
//						json += "\"ItemId\": " + items.get(i).getId() + ",";
//						json += "\"Price\":" + items.get(i).getPrice() + ",";
//						json += "\"ShortDesc\": \""
//								+ items.get(i).getDescription() + "\"";
//						json += "}";
//						if (i + 1 < items.size()) {
//							json += ",";
//						}
//					}
//				} else {
//					json += "{";
//					json += "\"ItemId\": " + items.get(0).getId() + ",";
//					json += "\"Price\": " + items.get(0).getPrice() + ",";
//					json += "\"ShortDesc\": \"" + items.get(0).getDescription()
//							+ "\"";
//					
//					json += "}";
//				}
//
//				json += "]";
//
//				Log.d(LOG_TAG, "json : " + json);
//
//				new ConnectionManager().post(context, listener, dialog, new Pair<String, String>(serverUrl, json));
//			}
//		}
//	}
}

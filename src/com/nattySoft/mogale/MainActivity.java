package com.nattySoft.mogale;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogale.push.*;
import com.nattySoft.mogale.util.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nattySoft.mogale.FragmentComunicator;
import com.nattySoft.mogale.net.CommunicationHandler.Action;
import com.nattySoft.mogale.listener.IncidentClickedListener;
import com.nattySoft.mogale.listener.RequestResponseListener;
import com.nattySoft.mogale.net.CommunicationHandler;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements FragmentComunicator, RequestResponseListener, IncidentClickedListener {

	private Action action;
	FragmentComunicator fragComm;
	private FragmentManager fragmentManager;
	private String TAG = MainActivity.class.getSimpleName();
	private boolean registered = false;
	Bundle savedInstanceState = null;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		String regStr = Preferences.getPreference(this, AppConstants.PreferenceKeys.KEY_REGISTERED);
		registered = regStr!=null && regStr.equalsIgnoreCase("true")?true:false;
		if (registered) {
			// Check device for Play Services APK.
			if (checkPlayServices()) {

				startApp(savedInstanceState);

			}
		} else {
			action = Action.REGISTER;
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivityForResult(intent, 2);
		}
	}

	private void startApp(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
//		if (savedInstanceState == null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			ft.add(R.id.container, new PlaceholderFragment(), "list");
			// ft.add(R.id.container, new IncidentDetailsFragment(),
			// "viewer");
			ft.commit();
//		}

		fragmentManager = getSupportFragmentManager();
		action = Action.GET_ALL_OPEN_INCIDENCES;
		CommunicationHandler.getOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving The Open Incidents..."));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	@Override
	public void hasResponse(String response) {
		if (!"-1".equals(response)) {
			if (action == Action.GET_ALL_OPEN_INCIDENCES) {
				fragComm = (FragmentComunicator) this;
				fragComm.incidentsResponce(response);
				Log.d("MenuFragment", "products " + response);
			}
		}
	}

	@Override
	public void incidentsResponce(String responce) {
		PlaceholderFragment frag = (PlaceholderFragment) fragmentManager.findFragmentByTag("list");
		frag.setMenus(responce, this);
	}

	class SpazaAdapeter extends ArrayAdapter<String> {

		Context context;
		int[] icons;
		ArrayList<HashMap<String, String>> productlist;
		private View row;

		public SpazaAdapeter(Context context, int[] icons, ArrayList<HashMap<String, String>> productlist) {
			super(context, R.layout.single_incident);
			this.context = context;
			this.icons = icons;
			this.productlist = productlist;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.single_incident, parent, false);
			ImageView myImage = (ImageView) row.findViewById(R.id.imageView1);
			TextView desc = (TextView) row.findViewById(R.id.textView1);
			TextView price = (TextView) row.findViewById(R.id.textView2);
			// int category =
			// Integer.parseInt(productlist.get(position).get("Category"));
			int category = 0;
			Log.d("getView", "pos " + position);
			switch (position) {
			case 0:
			case 14:
			case 15:
				category = 0;
				break;
			case 7:
			case 8:
			case 10:
			case 11:
			case 12:
			case 13:
			case 16:
			case 17:
			case 18:
			case 19:
			case 21:
			case 22:
			case 23:
			case 24:
				category = 1;
				break;
			case 2:
				category = 2;
				break;
			case 1:
			case 3:
			case 4:
			case 5:
			case 6:
			case 9:
				category = 3;
				break;
			}
			myImage.setImageResource(icons[category]);
			desc.setText(productlist.get(position).get("description"));
			price.setText((productlist.get(position).get("created")).substring(0, 9));

			return row;
		}

		@Override
		public int getCount() {
			int listSize = productlist.size();
			int count = super.getCount();
			int productlistC = productlist.size();
			Log.d("inside getCount", "productlist.size() " + listSize);
			Log.d("inside getCount", "superC " + count);
			Log.d("inside getCount", "productlistC " + productlistC);
			if (count < 1) {
				return productlistC;
			}

			return count;
		}
	}

	@Override
	public void incidentClicked(HashMap<String, String> item) {
		IncidentDetailsFragment frag = (IncidentDetailsFragment) fragmentManager.findFragmentById(R.id.viewer);
		frag.updateFields(item);
	}

	@Override
	public void hasBeenClicked(HashMap<String, String> item) {
		fragComm = (FragmentComunicator) this;
		fragComm.incidentClicked(item);
		Log.d("MenuFragment", "clicked incident " + item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	// Call Back method to get the Message form other Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// check if the request code is same as what is passed here it is 2
		if (requestCode == 2) {
//			startApp(savedInstanceState);
			new AlertDialog.Builder(this)
		    .setTitle("Delete entry")
		    .setMessage("Are you sure you want to delete this entry?")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show(); 

		}

	}

}

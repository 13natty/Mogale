package com.nattySoft.mogale;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogale.MainActivity.incidentAdapeter;
import com.nattySoft.mogale.listener.IncidentClickedListener;
import com.nattySoft.mogale.util.Preferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
	
	private String TAG = PlaceholderFragment.class.getSimpleName();
	ListView menuList;
	ArrayList<HashMap<String, String>> incidentslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> incidentsBigList = new ArrayList<HashMap<String, String>>();
	int[] icons = {R.drawable.no_water_50,R.drawable.water_meter_50,R.drawable.burst_pipe_50,R.drawable.water_pump_50,R.drawable.resevoir,R.drawable.water_tower_50};

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		menuList = (ListView) getActivity().findViewById(R.id.listView1);		
	}
	
	public void setMenus(String responce, final IncidentClickedListener listener) {
		
		JSONObject incidents = null;
		
		// try parse the string to a JSON object
		try {
			incidents = new JSONObject(responce);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		
		if(incidents!=null)
		{
			try {
	
				// Getting JSON Array from URL
				String[] data = new String[incidents.length()];
				Log.d("forst size", "data1 size "+data.length);
				Log.d(TAG, "incidents "+incidents);
				
				JSONArray incidentsArray = incidents.getJSONArray("data");
				
				Log.d("forst size", "incidentsArray size "+incidentsArray.length());
								
				for (int i = 0; i < incidentsArray.length(); i++) {
					HashMap<String, String> bigMap = new HashMap<String, String>();
					JSONObject c = incidentsArray.getJSONObject(i);
					// Storing JSON item in a Variable
					String type = c.getString("type");
					String created = c.getString("created");
					String description = c.getString("description");
					// Adding some value HashMap key => value
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("type", type);
					map.put("created", created);
					map.put("description", description);
					
					incidentslist.add(map);			

					// Adding all value HashMap key => value
					bigMap.put("status", c.getString("status"));
					bigMap.put("reporterSurname", c.getString("reporterSurname"));
					//assignee object
					JSONArray assigneeArr = c.optJSONArray("assignees");
					if(assigneeArr != null)
					{
						bigMap.put("assigneeSize", ""+assigneeArr.length());
						for (int j = 0; j < assigneeArr.length(); j++) {
							JSONObject d = assigneeArr.getJSONObject(j);
							bigMap.put("assigneeId_"+j, d.getString("id"));
							bigMap.put("assigneeSuperiorId_"+j, d.getString("superiorId"));
							bigMap.put("assigneeEmployeeNum_"+j, d.getString("employeeNum"));
							bigMap.put("assigneeEmail_"+j, d.getString("email"));
							bigMap.put("assigneeCellphone_"+j, d.getString("cellphone"));
							String name = d.getString("name");
							bigMap.put("assigneeName_"+j, d.getString("name"));
							bigMap.put("designation_"+j, d.getString("designation"));
							bigMap.put("assigneeSurname_"+j, d.getString("surname"));
							bigMap.put("active_"+j, d.getString("active"));
							bigMap.put("password_"+j, d.getString("password"));
						}						
					}
					
					JSONArray accepteeArr = c.optJSONArray("acceptees");
					boolean acctepted = false;
					if(accepteeArr != null)
					{
						bigMap.put("accepteeSize", ""+accepteeArr.length());
						for (int j = 0; j < accepteeArr.length(); j++) {
							JSONObject d = accepteeArr.getJSONObject(j);
							bigMap.put("accepteeId_"+j, d.getString("id"));
							bigMap.put("accepteeSuperiorId_"+j, d.getString("superiorId"));
							bigMap.put("accepteeEmployeeNum_"+j, d.getString("employeeNum"));
							bigMap.put("accepteeEmail_"+j, d.getString("email"));
							bigMap.put("accepteeCellphone_"+j, d.getString("cellphone"));
							bigMap.put("accepteeName_"+j, d.getString("name"));
							bigMap.put("designation_"+j, d.getString("designation"));
							bigMap.put("accepteeSurname_"+j, d.getString("surname"));
							bigMap.put("active_"+j, d.getString("active"));
							bigMap.put("password_"+j, d.getString("password"));	
							if(Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM).equals(d.getString("employeeNum")))
							{
								acctepted = true;
							}
						}						
					}
					if(acctepted)
					{
						bigMap.put("accepted", "true");
					}
					else
					{
						bigMap.put("accepted", "false");
					}
					bigMap.put("type", type);
					bigMap.put("severity", c.getString("severity"));
					bigMap.put("reporterName", c.getString("reporterName"));
					bigMap.put("id", c.getString("id"));
					bigMap.put("accountNumber", c.getString("accountNumber"));
					bigMap.put("area", c.getString("area"));
					bigMap.put("source", c.getString("source"));
					bigMap.put("address", c.getString("address"));
					bigMap.put("created", created);
					bigMap.put("description", description);
					bigMap.put("reporterIdNumber", c.getString("reporterIdNumber"));
					bigMap.put("reporterContact", c.getString("reporterContact"));
					bigMap.put("zone", c.getString("zone"));
					incidentsBigList.add(bigMap);
				}
				MainActivity myActivity = new MainActivity();
				incidentAdapeter adapter = myActivity.new incidentAdapeter(getActivity(), icons, incidentslist);
				menuList.setAdapter(adapter);
				listener.hasBeenClicked(incidentsBigList.get(0));
				menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Log.d("ITEM clicked", "productlist "+incidentsBigList.toString());
						Log.d("ITEM clicked", "position "+position);
						Log.d("ITEM clicked", "id "+id);
						listener.hasBeenClicked(incidentsBigList.get(position));
					}
				});
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

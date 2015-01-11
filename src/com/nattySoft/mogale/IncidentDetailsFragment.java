package com.nattySoft.mogale;

import java.util.ArrayList;
import java.util.HashMap;

import com.nattySoft.mogale.MainActivity.incidentAdapeter;
import com.nattySoft.mogale.R.id;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class IncidentDetailsFragment extends Fragment {
	
	ImageView image;
	TextView title;
	TextView reporterName;
	TextView reporterSurName;
	TextView assigneeName;
	TextView assigneeSurName;
	TextView accountNumber;
	TextView Area;
	TextView address;
	TextView Zone;
	TextView reporterIdNumber;
	TextView reporterContact;
	
	ListView assigneeList;
	
	Button acceptButton;
	Button declineButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.incidentdetails_flagment, container, false);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		image = (ImageView) getActivity().findViewById(R.id.imageView1);
		title = (TextView) getActivity().findViewById(R.id.title);
		reporterName = (TextView) getActivity().findViewById(R.id.reporterName);
		reporterSurName = (TextView) getActivity().findViewById(R.id.reporterSurname);
		assigneeName = (TextView) getActivity().findViewById(R.id.assigneeName);
		assigneeSurName = (TextView) getActivity().findViewById(R.id.assigneeSurname);
		accountNumber = (TextView) getActivity().findViewById(R.id.accountNumber);
		Area = (TextView) getActivity().findViewById(R.id.area);
		Zone = (TextView) getActivity().findViewById(R.id.zone);
		reporterIdNumber = (TextView) getActivity().findViewById(R.id.reporterIdNumber);
		reporterContact = (TextView) getActivity().findViewById(R.id.reporterContact);
		
		assigneeList = (ListView) getActivity().findViewById(id.assigneeView);
				
		acceptButton = (Button) getActivity().findViewById(R.id.button_accept);
		acceptButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				tha
			}
		});
		
		
		declineButton = (Button) getActivity().findViewById(R.id.button_decline);
	}
	
	public void updateFields(HashMap<String,String> item)
	{
		if(title!=null)
		{	
			title.setText(item.get("description"));
			reporterName.setText(item.get("reporterName"));
			reporterSurName.setText(item.get("reporterSurname"));
			assigneeName.setText(item.get("assigneeName"));
			assigneeSurName.setText(item.get("assigneeSurname"));
			accountNumber.setText(item.get("accountNumber"));
			Area.setText(item.get("area"));
			Zone.setText(item.get("zone"));
			reporterIdNumber.setText(item.get("reporterIdNumber"));
			reporterContact.setText(item.get("reporterContact"));

			int assigneeSize = Integer.parseInt(item.get("assigneeSize"));
			ArrayList<HashMap<String, String>> assigneeAList;
			assigneeAList = new ArrayList<HashMap<String, String>>();
			
			for (int i = 0; i < assigneeSize; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("assigneeId", item.get("assigneeId_"+i));
				map.put("assigneeSuperiorId", item.get("assigneeSuperiorId_"+i));
				map.put("assigneeEmployeeNum", item.get("assigneeEmployeeNum_"+i));
				map.put("assigneeEmail", item.get("assigneeEmail_"+i));
				map.put("assigneeCellphone", item.get("assigneeCellphone_"+i));
				map.put("assigneeName", item.get("assigneeName_"+i));
				map.put("designation", item.get("designation_"+i));
				map.put("assigneeSurname", item.get("assigneeSurname_"+i));
				map.put("active", item.get("active_"+i));
				map.put("password", item.get("password_"+i));
				
				
				assigneeAList.add(map);	
				assigneeAdapeter adapter = new assigneeAdapeter(getActivity(), assigneeAList);
				assigneeList.setAdapter(adapter);		
				
			}

		}
	}
	
	class assigneeAdapeter extends ArrayAdapter<String> {

		Context context;
		ArrayList<HashMap<String, String>> attributeslist;
		private View row;

		public assigneeAdapeter(Context context, ArrayList<HashMap<String, String>> attributeslist) {
			super(context, R.layout.single_asignee);
			this.context = context;
			this.attributeslist = attributeslist;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.single_asignee, parent, false);
			
			TextView textView_id = (TextView) row.findViewById(R.id.textView_id);
			TextView textView_superiorId = (TextView) row.findViewById(R.id.textView_superiorId);
			TextView textView_employeeNum = (TextView) row.findViewById(R.id.textView_employeeNum);
			TextView textView_email = (TextView) row.findViewById(R.id.textView_email);
			TextView textView_cellphone = (TextView) row.findViewById(R.id.textView_cellphone);
			TextView textView_name = (TextView) row.findViewById(R.id.textView_name);
			TextView textView_designation = (TextView) row.findViewById(R.id.textView_designation);
			TextView textView_active = (TextView) row.findViewById(R.id.textView_active);
			TextView textView_surname = (TextView) row.findViewById(R.id.textView_surname);
			
			textView_id.append(attributeslist.get(position).get("assigneeId"));
			textView_superiorId.append(attributeslist.get(position).get("assigneeSuperiorId"));
			textView_employeeNum.append(attributeslist.get(position).get("assigneeEmployeeNum"));
			textView_email.append(attributeslist.get(position).get("assigneeEmail"));
			textView_cellphone.append(attributeslist.get(position).get("assigneeCellphone"));
			textView_name.append(attributeslist.get(position).get("assigneeName"));
			textView_designation.append(attributeslist.get(position).get("designation"));
			textView_surname.append(attributeslist.get(position).get("assigneeSurname"));
			textView_active.append(attributeslist.get(position).get("active"));

			return row;
		}

		@Override
		public int getCount() {
			int listSize = attributeslist.size();
			int count = super.getCount();
			int productlistC = attributeslist.size();
			Log.d("inside getCount", "productlist.size() " + listSize);
			Log.d("inside getCount", "superC " + count);
			Log.d("inside getCount", "productlistC " + productlistC);
			if (count < 1) {
				return productlistC;
			}

			return count;
		}
	}

}

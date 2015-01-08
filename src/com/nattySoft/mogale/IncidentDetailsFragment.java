package com.nattySoft.mogale;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
		}
	}

}

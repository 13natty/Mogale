package com.nattySoft.mogale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogale.MainActivity.incidentAdapeter;
import com.nattySoft.mogale.R.id;
import com.nattySoft.mogale.listener.IncidentClickedListener;
import com.nattySoft.mogale.listener.RequestResponseListener;
import com.nattySoft.mogale.net.CommunicationHandler;
import com.nattySoft.mogale.net.CommunicationHandler.Action;
import com.nattySoft.mogale.util.Preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class IncidentDetailsFragment extends Fragment implements FragmentComunicator, RequestResponseListener, IncidentClickedListener, OnLongClickListener {

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

	LinearLayout assigneeLayout;

	Button acceptButton;
	Button commentButton;
	Button viewCommentsButton;
	Button declineButton;
	private ArrayList<HashMap<String, String>> assigneeAList;
	private String incidentID = null;
	private LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.inflater = inflater;
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

		assigneeLayout = (LinearLayout) getActivity().findViewById(id.assigneeView);

		acceptButton = (Button) getActivity().findViewById(R.id.button_accept);
		acceptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.action = Action.ACCEPT_INCIDENT;
				CommunicationHandler.acceptIncident(getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Accepting Incidents..."), Preferences.getPreference(IncidentDetailsFragment.this.getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), incidentID);
			}
		});

		commentButton = (Button) getActivity().findViewById(R.id.comment_button);
		commentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// custom dialog
				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.add_comment);
				dialog.setTitle("Comment on " + title.getText());

				Button sendButton = (Button) dialog.findViewById(R.id.comment_send);
				// if button is clicked, close the custom dialog
				sendButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText message = (EditText) dialog.findViewById(R.id.comment_message);
						MainActivity.action = Action.ADD_COMMENT;
						CommunicationHandler.addComment(getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Sending your comment..."), Preferences.getPreference(IncidentDetailsFragment.this.getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), incidentID, message.getText().toString());
						dialog.dismiss();
					}
				});

				Button cancelButton = (Button) dialog.findViewById(R.id.comment_cancel);
				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});

		viewCommentsButton = (Button) getActivity().findViewById(R.id.view_comments);
		viewCommentsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.action = Action.GET_COMMENTS;
				CommunicationHandler.getComments(getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Sending your comment..."), incidentID);
			}
		});

		declineButton = (Button) getActivity().findViewById(R.id.button_decline);
		declineButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.action = Action.DECLINE_INCIDENT;
				CommunicationHandler.declineIncident(getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Accepting Incidents..."), Preferences.getPreference(IncidentDetailsFragment.this.getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), incidentID, "first one");
			}
		});
	}

	public void updateFields(HashMap<String, String> item) {
		if (title != null) {
			incidentID = item.get("id");
			switch (item.get("severity")) {
			case "Minor":
				image.setImageResource(R.drawable.minor);
				break;
			case "Major":
				image.setImageResource(R.drawable.major);
				break;
			case "Critical":
				image.setImageResource(R.drawable.critical);
				break;

			default:
				break;
			}

			if (item.get("accepted") != null) {
				boolean accepted = item.get("accepted").equals("true") ? true : false;
				if (accepted) {
					acceptButton.setVisibility(View.GONE);
					declineButton.setVisibility(View.GONE);
					commentButton.setVisibility(View.VISIBLE);
					viewCommentsButton.setVisibility(View.VISIBLE);
				} else {
					acceptButton.setVisibility(View.VISIBLE);
					declineButton.setVisibility(View.VISIBLE);
					commentButton.setVisibility(View.GONE);
					viewCommentsButton.setVisibility(View.GONE);
				}
			}

			title.setText(item.get("description"));
			reporterName.setText(item.get("reporterName"));
			reporterSurName.setText(item.get("reporterSurname"));
			assigneeName.setText("");// item.get("assigneeName")
			assigneeSurName.setText("");// item.get("assigneeSurname")
			accountNumber.setText(item.get("accountNumber"));
			Area.setText(item.get("area"));
			Zone.setText(item.get("zone"));
			reporterIdNumber.setText(item.get("reporterIdNumber"));
			reporterContact.setText(item.get("reporterContact"));

			int assigneeSize = Integer.parseInt(item.get("assigneeSize"));
			assigneeAList = new ArrayList<HashMap<String, String>>();

			assigneeLayout.removeAllViews();

			for (int i = 0; i < assigneeSize; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("assigneeId", item.get("assigneeId_" + i));
				map.put("assigneeSuperiorId", item.get("assigneeSuperiorId_" + i));
				map.put("assigneeEmployeeNum", item.get("assigneeEmployeeNum_" + i));
				map.put("assigneeEmail", item.get("assigneeEmail_" + i));
				map.put("assigneeCellphone", item.get("assigneeCellphone_" + i));
				map.put("assigneeName", item.get("assigneeName_" + i));
				map.put("designation", item.get("designation_" + i));
				map.put("assigneeSurname", item.get("assigneeSurname_" + i));
				map.put("active", item.get("active_" + i));
				map.put("password", item.get("password_" + i));

				assigneeAList.add(map);
				TextView nameTV = new TextView(this.getActivity());
				nameTV.setImeActionLabel(map.get("assigneeEmployeeNum"), -1);
				nameTV.setText("Assignee Name : " + map.get("assigneeName"));
				nameTV.setOnLongClickListener(this);
				nameTV.setBackground(getResources().getDrawable(R.drawable.assignee_border));
				assigneeLayout.addView(nameTV);
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		Log.d(getTag(), "view has been clicked get ID " + v.getId());
		TextView tv = (TextView) v;
		String empNo = (String) tv.getImeActionLabel();
		Log.d(getTag(), "view has been clicked get getText " + tv.getText());
		Log.d(getTag(), "view has been clicked get empNo " + empNo);
		for (int i = 0; i < assigneeAList.size(); i++) {
			HashMap<String, String> objmap = assigneeAList.get(i);
			String emp = objmap.get("assigneeEmployeeNum");
			Log.d(getTag(), "emp " + emp);
			if (emp.equalsIgnoreCase(empNo)) {
				Dialog profDiag = new Dialog(this.getActivity());
				profDiag.requestWindowFeature(Window.FEATURE_NO_TITLE);
				profDiag.setContentView(R.layout.assignee_profile);

				TextView employeeNum = (TextView) profDiag.findViewById(R.id.assignee_employeeNum);
				TextView email = (TextView) profDiag.findViewById(R.id.assignee_email);
				TextView cell = (TextView) profDiag.findViewById(R.id.assignee_cellphone);
				TextView name = (TextView) profDiag.findViewById(R.id.assignee_name);
				TextView designation = (TextView) profDiag.findViewById(R.id.assignee_designation);
				TextView surname = (TextView) profDiag.findViewById(R.id.assignee_surname);
				TextView active = (TextView) profDiag.findViewById(R.id.assignee_active);

				employeeNum.append(emp);
				email.append(objmap.get("assigneeEmail"));
				cell.append(objmap.get("assigneeCellphone"));
				name.append(objmap.get("assigneeName"));
				designation.append(objmap.get("designation"));
				surname.append(objmap.get("assigneeSurname"));
				active.append(objmap.get("active"));

				profDiag.show();

			}
		}
		return false;
	}

	@Override
	public void hasBeenClicked(HashMap<String, String> item) {
		Log.d(getTag(), "hasBeenClicked, item " + item);
	}

	@Override
	public void hasResponse(String response) {
		Log.d(getTag(), "hasResponse, response " + response);
	}

	@Override
	public void incidentsResponce(String responce) {
		Log.d(getTag(), "incidentsResponce, responce " + responce);
		JSONObject responceJSON = null;

		// try parse the string to a JSON object
		try {
			responceJSON = new JSONObject(responce);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		if (MainActivity.action == Action.ACCEPT_INCIDENT) {
			if (responceJSON != null) {
				try {
					String resp = responceJSON.getString("response");

					if (resp.equalsIgnoreCase("[\"success\"]")) {

						acceptButton.setVisibility(View.GONE);
						declineButton.setVisibility(View.GONE);
						commentButton.setVisibility(View.VISIBLE);
						viewCommentsButton.setVisibility(View.VISIBLE);

						new AlertDialog.Builder(getActivity()).setTitle("Succesfully Accepetd").setMessage(responceJSON.getString("message")).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).setIcon(android.R.drawable.ic_dialog_info).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (MainActivity.action == Action.GET_COMMENTS) {
			
			JSONArray data = null;
			try {
				data = responceJSON.getJSONArray("data");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			View dialogView = inflater.inflate(R.layout.incident_comments, null, false);
			
			LinearLayout commentsList = (LinearLayout) dialogView.findViewById(R.id.commentsLayout);
			
			// Create custom dialog object
			 final Dialog dialog = new Dialog(getActivity());
			 // Include dialog.xml file
			 dialog.setContentView(dialogView);
			 // Set dialog title
			 dialog.setTitle("Comments "+title.getText());
			if (data != null) {
				for (int i = 0; i < data.length(); i++) {
//					comment = (LinearLayout )inflater.inflate(R.layout.single_comment, container, false);
					LinearLayout comment = new Comment(getActivity()).getView(getActivity(), inflater);
					TextView name = (TextView) comment.findViewById(R.id.commentor);
					TextView time = (TextView) comment.findViewById(R.id.commentDate);
					TextView singleComment = (TextView) comment.findViewById(R.id.singleComment);
					
					try {
						String strComment = ((JSONObject)data.getJSONObject(i)).getString("comment");
						String timestamp = ((JSONObject)data.getJSONObject(i)).getString("timestamp");
						String strName = ((JSONObject)data.getJSONObject(i)).getJSONObject("commentor").getString("name");
						strName += " "+((JSONObject)data.getJSONObject(i)).getJSONObject("commentor").getString("surname");
						
						name.setText(strName);
						time.setText(timestamp.substring(0, timestamp.indexOf(".")));
						singleComment.setText(strComment);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					commentsList.addView(comment);
					
				}
				final ScrollView scroll = (ScrollView) dialogView.findViewById(R.id.commentsScroll);
				
				scroll.postDelayed(new Runnable() {
				    @Override
				    public void run() {
				        scroll.fullScroll(ScrollView.FOCUS_DOWN);
				    }
				}, 1000);
			}
			 
						
			 dialog.show();
		}
	}

	@Override
	public void incidentClicked(HashMap<String, String> item) {
		Log.d(getTag(), "incidentClicked, item " + item);

	}

}

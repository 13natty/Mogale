package com.nattySoft.mogale;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Comment extends LinearLayout {

	private Context context;

	public Comment(Context context) {
		super(context);
		this.context = context;
	}
	
	public LinearLayout getView(FragmentActivity fragmentActivity, LayoutInflater inflater)
	{
		return (LinearLayout )inflater.inflate(R.layout.single_comment, null, false);
	}

}

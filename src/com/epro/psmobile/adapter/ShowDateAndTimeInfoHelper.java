package com.epro.psmobile.adapter;


import java.util.Date;

import com.epro.psmobile.R;
import com.epro.psmobile.util.TextFormat;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ShowDateAndTimeInfoHelper {

	public ShowDateAndTimeInfoHelper() {
		// TODO Auto-generated constructor stub
	}
	public static void fillDateAndTimeGreenLine(Context context,
			Date currentDateTime,
			View vParent)
	{

		String textDateFormat = TextFormat.convertDate(currentDateTime);
		String textTimeFormat = TextFormat.convertTime(currentDateTime);
		
		String strDate = context.getString(R.string.dialog_team_text_current_date, textDateFormat);
		String strTime = context.getString(R.string.dialog_team_text_current_time,textTimeFormat);
		
		TextView tvDate = (TextView)vParent.findViewById(R.id.tv_green_line_date_today);
		TextView tvTime = (TextView)vParent.findViewById(R.id.tv_green_line_time_today);
		
		tvDate.setText(strDate);
		tvTime.setText(strTime);
		
	}
}

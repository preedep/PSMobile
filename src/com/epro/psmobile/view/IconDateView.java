package com.epro.psmobile.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.epro.psmobile.R;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.FontUtil.FontName;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IconDateView extends RelativeLayout {

	
	public IconDateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public IconDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public IconDateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	public void setDateTime(Date date,int colorRes)
	{
		int color = this.getContext().getResources().getColor(colorRes);
		this.setBackgroundColor(color);
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
				
		Date now = new Date();
		
		c1.setTime(new Date(now.getYear(),now.getMonth(),now.getDate()));//today
		
		c2.setTime(new Date(date.getYear(),date.getMonth(),date.getDate()));
		
		
		TextView tvDate = new TextView(this.getContext());
		TextView tvMonth = new TextView(this.getContext());
		
		FontUtil.replaceFontTextView(getContext(), tvDate,FontName.THSARABUN_BOLD);
		FontUtil.replaceFontTextView(getContext(), tvMonth,FontName.THSARABUN_BOLD);

		LayoutParams tvDateLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tvDateLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
		tvDate.setLayoutParams(tvDateLayout);

		LayoutParams tvMonthLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tvMonthLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		tvMonthLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		tvMonth.setLayoutParams(tvMonthLayout);

		tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
		if (c2.equals(c1)){
			//today
			tvDate.setText(this.getResources().getString(R.string.txt_today));
		}else{
			
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat month_date = new SimpleDateFormat("MMMMMMMMM");
			String month_name = month_date.format(c2.getTime());
			tvDate.setText(""+c2.get(Calendar.DAY_OF_MONTH));
			tvMonth.setText(month_name);
		}
		
		this.addView(tvDate);
		this.addView(tvMonth);
	}
}

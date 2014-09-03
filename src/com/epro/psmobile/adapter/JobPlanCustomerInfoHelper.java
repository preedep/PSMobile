package com.epro.psmobile.adapter;

import java.util.Calendar;

import org.joda.time.Days;

import com.epro.psmobile.R;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.FontUtil.FontName;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class JobPlanCustomerInfoHelper {

	public JobPlanCustomerInfoHelper() {
		// TODO Auto-generated constructor stub
	}
	public static void fillCustomerInfo(Context context,
			Task task,
			View vParent)
	{
		JobRequest jobRequest = task.getJobRequest();
		TextView tvCompanyName = (TextView)vParent.findViewById(R.id.tv_list_job_item_company_name);
		TextView tvSurveySiteCount = (TextView)vParent.findViewById(R.id.tv_list_job_item_survey_site_count);
		TextView tvInspectType = (TextView)vParent.findViewById(R.id.tv_list_job_item_inspect_type);
		TextView tvRemainDate = (TextView)vParent.findViewById(R.id.tv_list_job_item_date_remain);
		FontUtil.replaceFontTextView(context, tvCompanyName,FontName.THSARABUN);
		FontUtil.replaceFontTextView(context, tvSurveySiteCount,FontName.THSARABUN);
		FontUtil.replaceFontTextView(context, tvInspectType,FontName.THSARABUN);
		FontUtil.replaceFontTextView(context, tvRemainDate,FontName.THSARABUN);
		
		String name = "";
		
		if (jobRequest.getCustomerSurveySiteList() != null){
			name = jobRequest.getCustomerSurveySiteList().get(0).getCustomerName();
		}
		if (task.isDuplicatedTask()){
			name += " ("+context.getResources().getString(R.string.label_task_duplicated)+")";
		}
		tvCompanyName.setText(name);
		
		int surveySiteCount = 0;
		
		if (jobRequest.getCustomerSurveySiteList() != null){
			surveySiteCount = jobRequest.getCustomerSurveySiteList().size();
		}
		String strSurveySiteCount = context.getResources().getString(
				R.string.txt_customer_survey_site_task_code_and_amount,task.getTaskCode(), surveySiteCount+"");

		String inspectType = jobRequest.getInspectType().getInspectTypeName();
		String strInspectType = context.getResources().getString(R.string.txt_customer_survey_site_inspect_type, inspectType);
		
		Calendar today = Calendar.getInstance();
		
		long remainsDate = DataUtil.daysBetween(today.getTime(), task.getTaskDate());
		
		String strRemainDate = context.getResources().getString(R.string.txt_customer_survey_site_remain_date, remainsDate+"");
		
		if (task.getFlagOtherTeam().equalsIgnoreCase("Y")){
			String text = context.getString(R.string.text_not_allow_postpone);
			strRemainDate += "\r\n";
			strRemainDate += text;
		}
		
		tvSurveySiteCount.setText(strSurveySiteCount);
		tvInspectType.setText(strInspectType);
		tvRemainDate.setText(strRemainDate);
		
		
	}
}

package com.epro.psmobile.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;
import com.epro.psmobile.R;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.dialog.JobPlanDetailDialog;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.FontUtil.FontName;
import com.epro.psmobile.util.StatusColorMapper;

public class DashboardCalendarAdapter extends CaldroidGridAdapter {

	private ArrayList<Task> tasks;
	//private LayoutInflater inflater;
	private Context ctxt;
	public DashboardCalendarAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData,
			ArrayList<Task> tasks) {
		
		
		super(context, month, year, caldroidData, extraData);
		// TODO Auto-generated constructor stub
		this.tasks = tasks;
		//inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ctxt = context;

		extraData.put("TASK", tasks);
	}

	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasks = tasks;		
		extraData.put("TASK", this.tasks);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		
		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_cell, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		TextView tv1 = (TextView) cellView.findViewById(R.id.tv_date);
		FontUtil.replaceFontTextView(context, tv1, FontName.THSARABUN_BOLD);		
		tv1.setTextColor(Color.BLACK);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();

		// Set color of the dates in previous / next month
		if (dateTime.getMonthOfYear() != month) {
			tv1.setTextColor(resources
					.getColor(R.color.caldroid_darker_gray));
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.isBefore(minDateTime))
				|| (maxDateTime != null && dateTime.isAfter(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			tv1.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(R.drawable.red_border_gray_bg);
			}

		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cellView.setBackgroundColor(resources
						.getColor(R.color.caldroid_sky_blue));
			}

			tv1.setTextColor(CaldroidFragment.selectedTextColor);

		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(R.drawable.red_border);
			} else {
				cellView.setBackgroundResource(R.drawable.cell_bg);
			}			
		}

		tv1.setText("" + dateTime.getDayOfMonth());

		final ViewGroup vgDashboardItems = (ViewGroup)cellView.findViewById(R.id.dash_board_items);
		vgDashboardItems.removeAllViews();

		this.tasks = (ArrayList<Task>)extraData.get("TASK");
		
		if (this.tasks != null)
		{
			int day = dateTime.getDayOfMonth();
			int month = dateTime.getMonthOfYear();
			int year = dateTime.getYear();
			
			Log.d("DEBUG_D", " date = "+day+"-"+month+"-"+year);

			final ArrayList<Task> taskPerCell = new ArrayList<Task>();
			
			for(final Task t : this.tasks)
			{
				Date taskDate = t.getTaskDate();
				if (taskDate == null)
					continue;
				

				Calendar c =  Calendar.getInstance();
				c.setTime(taskDate);
				
				int task_day = c.get(Calendar.DAY_OF_MONTH);
				int task_month =c.get(Calendar.MONTH) + 1;
				int task_year = c.get(Calendar.YEAR);

				//(task_day - day) + (task_month - month) + (task_year - year);
				
				if ((task_day == day)&&
					(task_month == month)&&
					(task_year == year))
				{
					
					taskPerCell.add(t);
					
					final View vCalendarItem = inflater.inflate(R.layout.custom_calendar_cell_item, null);	
					
					
					boolean bForceEdit = false;								
					int color = StatusColorMapper.getStatusColor(context, 
							t.getTaskStatus(),
							t.isUploadSynced(),
							bForceEdit
							);	
					
					Drawable drawable = context.getResources().getDrawable(
							R.drawable.task_calendar_icon);
					if (drawable instanceof GradientDrawable)
					{
							GradientDrawable gradient = (GradientDrawable)drawable;
							gradient.setColor(
													context.getResources().getColor(color)
														);
					}
					ImageView dot = (ImageView)vCalendarItem.findViewById(R.id.icon_task_on_date);
					dot.setImageDrawable(drawable);
					
/*
					String detail = t.getJobRequest().getInspectType().getInspectTypeName()+"\n"  
							+ t.getJobRequest().getCustomerSurveySiteList().get(0).getCustomerName();
*/
					String detail = t.getJobRequest().getInspectType().getInspectTypeName();  

					
					final TextView tv = (TextView)vCalendarItem.findViewById(R.id.tv_task_on_date_customer);
//					tv.setEllipsize(TruncateAt.MARQUEE);
					tv.setText(detail);
					
					/*
					tv.setOnLongClickListener(new OnLongClickListener(){

						@Override
						public boolean onLongClick(View arg0) {
							// TODO Auto-generated method stub
							
							JobPlanDetailDialog jobPlanDetailDlg = JobPlanDetailDialog.newInstance(t);
							jobPlanDetailDlg.show(((SherlockFragmentActivity)ctxt).getSupportFragmentManager(), 
									"jobPlanDetailDlg");
							
							return false;
						}
					});									
*/					
					//if (vgDashboardItems.getChildCount() == 0)
					//{
						FontUtil.replaceFontTextView(context, tv, FontName.THSARABUN);
						vgDashboardItems.addView(vCalendarItem);
					//}
				}
			}
			
			if (taskPerCell.size() > 0)
			{
				cellView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						ViewGroup vg = 
								(ViewGroup) ((Activity)context).findViewById(
										R.id.dashboard_summary_task);

						if (vg != null)
						{
							vg.removeAllViews();//clear all item before add new
							
							for(final Task t : taskPerCell){
								String detail = t.getJobRequest().getInspectType().getInspectTypeName()+"\n"  
										+ t.getJobRequest().getCustomerSurveySiteList().get(0).getCustomerName();

								
								boolean bForceEdit = false;								
								int color = StatusColorMapper.getStatusColor(context, 
										t.getTaskStatus(),
										t.isUploadSynced(),
										bForceEdit
										);	
								
								View vDetail = inflater.inflate(R.layout.custom_calendar_cell_item_details, null);

								
								Drawable drawable = context.getResources().getDrawable(
														R.drawable.task_calendar_icon);
								if (drawable instanceof GradientDrawable)
								{
									GradientDrawable gradient = (GradientDrawable)drawable;
									gradient.setColor(
											context.getResources().getColor(color)

											);
								}
								ImageView dot = (ImageView)vDetail.findViewById(R.id.icon_task_on_date);
								dot.setImageDrawable(drawable);
								
								
								
								
								vDetail.setOnClickListener(new OnClickListener(){

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										JobPlanDetailDialog jobPlanDetailDlg = JobPlanDetailDialog.newInstance(t);
										jobPlanDetailDlg.show(((SherlockFragmentActivity)ctxt).getSupportFragmentManager(), 
												"jobPlanDetailDlg");
									}
									
								});
								TextView tvDetail = (TextView)vDetail.findViewById(R.id.tv_task_on_date_customer);
								tvDetail.setText(detail);
								
															
								tvDetail.setTextColor(
										Color.GRAY
										);
								
								String textTime = "";
								if (t.getTaskEndTime().isEmpty()){
									textTime = t.getTaskStartTime();				
								}else{
									textTime = t.getTaskStartTime()+" - "+t.getTaskEndTime();
								}
								
								TextView tvTime = (TextView)vDetail.findViewById(R.id.tv_calendar_detail_date_time);
								tvTime.setText(textTime);
								tvTime.setTextColor(
										Color.GRAY
										);
								
								
								vg.addView(vDetail);
//								vg.addView(tv);
								
								/*
								http://stackoverflow.com/questions/5049852/android-drawing-separator-divider-line-in-layout
								*/
								ImageView imgView = new ImageView(context);
								imgView.setLayoutParams(new ViewGroup.LayoutParams(
											ViewGroup.LayoutParams.MATCH_PARENT,
											ViewGroup.LayoutParams.WRAP_CONTENT));
								imgView.setScaleType(ScaleType.FIT_XY);
								imgView.setImageResource(android.R.drawable.divider_horizontal_dark);

								vg.addView(imgView);
							}
						}
					}
					
				});
			}else{
				cellView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ViewGroup vg = (ViewGroup) ((Activity)context).findViewById(R.id.dashboard_summary_task);
						if (vg != null){
							vg.removeAllViews();
						}
					}
					
				});
			}
		}
		
		
		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);

		
		
		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.performClick();
			} 		
		}
		return cellView;
	}

}

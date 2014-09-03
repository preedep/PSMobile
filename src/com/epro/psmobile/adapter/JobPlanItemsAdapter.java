package com.epro.psmobile.adapter;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.TaskComplete;
import com.epro.psmobile.data.TaskResend;
import com.epro.psmobile.data.TaskResponseList;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.StatusColorMapper;
import com.epro.psmobile.util.FontUtil.FontName;
import com.epro.psmobile.util.GeoLocUtil;
import com.epro.psmobile.view.IconDateView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JobPlanItemsAdapter extends BaseAdapter
{
	public enum TaskListType{
		TASK_PLAN,
		DO_TASK
	};
	private Context context;
	private ArrayList<JobPlanItem> jobPlanItem;
	private LayoutInflater inflater;
	private TaskListType taskListType;
	private Location currentLocation;
	
	//private TaskResponseList<TaskResend> taskResendResponseList;
	private TaskResponseList<TaskComplete> taskCompleteResponseList;
	
	public JobPlanItemsAdapter(Context ctxt,
			ArrayList<JobPlanItem> jobPlanItem,
			TaskListType taskListType,
			/*TaskResponseList<TaskResend> taskResendResponseList,*/
			TaskResponseList<TaskComplete> taskCompleteResponseList)
	{
		this.context = ctxt;
		this.jobPlanItem = jobPlanItem;
		inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.taskListType = taskListType;
//		this.taskResendResponseList = taskResendResponseList;
		this.taskCompleteResponseList = taskCompleteResponseList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jobPlanItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jobPlanItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		JobPlanItem item = (JobPlanItem)getItem(position);
		if (item.isTitle)
		{
			v = inflater.inflate(R.layout.ps_list_fragment_group_title_name, null);
			TextView tv = (TextView)v.findViewById(R.id.tv_list_fragment_group_title_name);
			FontUtil.replaceFontTextView(context, tv,FontName.THSARABUN);
			tv.setText(item.titleName);
			
		}else{
			v = inflater.inflate(R.layout.ps_list_job_item_view, null);
			
			IconDateView iconDateView = (IconDateView)v.findViewById(R.id.icon_date_view);
			ImageView imgViewStatus = (ImageView)v.findViewById(R.id.icon_task_status);
			imgViewStatus.setVisibility(View.GONE);
			
			/*
			if (taskListType == TaskListType.TASK_PLAN){
				if ((item.task.getTaskStatus() == TaskStatus.WAIT_TO_CONFIRM)||
					(item.task.getTaskStatus() == TaskStatus.CANCEL)||
					(item.task.getTaskStatus() == TaskStatus.SHIFT))
				{
					iconDateView.setDateTime(item.task.getTaskDate(), R.color.NOT_SYNC);					
				}else{
					iconDateView.setDateTime(item.task.getTaskDate(), R.color.ALREADY_SYNC);
				}
			}else{
				if ((item.task.getTaskStatus() == TaskStatus.DUPLICATED)||
					 (item.task.getTaskStatus() == TaskStatus.CONFIRM_INSPECT))
					{
						iconDateView.setDateTime(item.task.getTaskDate(), R.color.NOT_SYNC);					
					}else if ((item.task.getTaskStatus() == TaskStatus.LOCAL_SAVED))
					{
						iconDateView.setDateTime(item.task.getTaskDate(), R.color.ALREADY_INSPECTED);
					}else{
						iconDateView.setDateTime(item.task.getTaskDate(), R.color.ALREADY_SYNC);						
					}
			}*/
			boolean bForceEdit = false;
			/*
			if ((taskResendResponseList != null)&&(taskResendResponseList.size() > 0))
			{
				bForceEdit = (taskResendResponseList.hasTaskResponse
													(
														item.task.getTaskCode(), 
														SharedPreferenceUtil.getTeamID(context)
													) != null);
			}*/
			int resColorId = StatusColorMapper.getStatusColor(context, 
					item.task.getTaskStatus(),
					item.task.isUploadSynced(),
					bForceEdit
					);
						
			iconDateView.setDateTime(item.task.getTaskDate(), resColorId);
			TextView tvTime = (TextView)v.findViewById(R.id.tv_device_access_time);
			
			String textTime = "";
			if (item.task.getTaskEndTime().isEmpty()){
				textTime = item.task.getTaskStartTime();				
			}else{
				textTime = item.task.getTaskStartTime()+" - "+item.task.getTaskEndTime();
			}
			//iconDateView.setDateTime(item.task.getTaskDate(), R.color.ALREADY_SYNC);

			tvTime.setText(textTime);
			
			JobPlanCustomerInfoHelper.fillCustomerInfo(this.context, item.task, v);
			
			
			final String remark = item.task.getRemark();
			if ((remark != null)&&(!remark.isEmpty()))
			{
				//have remark
				TextView tv_action_about = (TextView)v.findViewById(R.id.tv_action_about);
				tv_action_about.setText(context.getString(R.string.inspect_have_remark));
				
				View vBtnClickRemark = v.findViewById(R.id.btn_action_show_remark);
				vBtnClickRemark.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(context)
					    .setTitle(R.string.inspect_have_remark_dlg_title)
					    .setMessage(remark)
					    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					        }
					     })
					     .show();
					}
					
				});
			}
			
			
			if (this.getCurrentLocation() != null)
			{
				TextView tvDistance = (TextView)v.findViewById(R.id.tv_distance_location_place);
				
				try{
					ArrayList<CustomerSurveySite> customerSurveySiteList = 
						item.task.getJobRequest().getCustomerSurveySiteList();
					
					
					if (customerSurveySiteList != null)
					{
						
					CustomerSurveySite firstSite = customerSurveySiteList.get(0);

					Location siteLocation = new Location(firstSite.getCustomerSurveySiteID()+"");
					siteLocation.setLatitude(firstSite.getSiteLocLat());
					siteLocation.setLongitude(firstSite.getSiteLocLon());
					float distance = GeoLocUtil.distFrom(getCurrentLocation(), siteLocation);
					distance = distance / 1000;
					
					String textDistance = context.getString(R.string.distance_from_current_location,
							String.format( "%.2f", distance));
					
					tvDistance.setText(textDistance);
					}					
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		if (v != null)
			v.setTag(item);
		
		return v;
	}
	public Location getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

}

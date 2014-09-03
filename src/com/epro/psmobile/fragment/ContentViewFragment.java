package com.epro.psmobile.fragment;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.app.SherlockFragment;
import com.epro.psmobile.PsDrawLayoutActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.JobPlanItem;
import com.epro.psmobile.adapter.JobPlanItemsAdapter;
import com.epro.psmobile.adapter.JobPlanItemsAdapter.TaskListType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskComplete;
import com.epro.psmobile.data.TaskResend;
import com.epro.psmobile.data.TaskResponseList;
import com.epro.psmobile.key.params.InstanceStateKey;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.FrameLayout.LayoutParams;

public class ContentViewFragment extends ContentViewBaseFragment {

	
	private View currentContentView;
	private Activity currentActivity;
	public ContentViewFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//Log.d("DEBUG_D", "Content Fragment Create View");		
		
		currentContentView =  null;
		Bundle bundle = this.getArguments();
		if (bundle != null)
		{
			int tabMenuIdx = bundle.getInt(InstanceStateKey.TAB_INDEX_KEY);
			Log.d("DEBUG_D", "Get idx = "+tabMenuIdx);
			
			currentContentView = getCurrentViewFromMenu(inflater,tabMenuIdx,container);
		}
		return currentContentView;
	}
	private View getCurrentViewFromMenu(LayoutInflater inflater,
			int tabMenuIdx,
			ViewGroup container)
	{
		
		currentContentView = null;
		switch(tabMenuIdx){
		case 0://dashboard
		{
			//currentContentView = inflater.inflate(R.layout.ps_fragment_calendar_view, container, false);
							
		}break;
		case 1://plan job list
		{
			currentContentView = inflater.inflate(R.layout.ps_fragment_plan_task_list_view, container, false);		
		}break;
		case 2:			
		{
			currentContentView = inflater.inflate(R.layout.ps_fragment_task_list_view, container, false);					
		}break;
		default:{
			currentContentView = inflater.inflate(R.layout.ps_fragment_calendar_view, container, false);
			
		}break;
		}
		
		return currentContentView;
	}
	private void initialTaskListView(TaskListType taskListType)
	{
		ListView lvJobPlan = null;
		if (taskListType == JobPlanItemsAdapter.TaskListType.TASK_PLAN){			
			//currentContentView = inflater.inflate(R.layout.ps_fragment_plan_task_list_view, container, false);		
			lvJobPlan = (ListView)currentContentView.findViewById(R.id.lv_fragment_task_plan_list);
		}else{
			//currentContentView = inflater.inflate(R.layout.ps_fragment_task_list_view, container, false);		
			lvJobPlan = (ListView)currentContentView.findViewById(R.id.lv_fragment_task_list);
		}
		if (lvJobPlan != null)
		{
			/**
			 Query task
			 and add tasks to JobPlanItems
			 */
			
			PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(getActivity());
			ArrayList<Task> tasks = null;
			try {
				tasks = dbAdapter.getAllTasks();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (tasks == null)return;
			
			String[] titleGroups = null;
			if (taskListType == JobPlanItemsAdapter.TaskListType.TASK_PLAN){							
				titleGroups = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_task_sort_by_task_status);
			}else{
				titleGroups = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_sort_array_2);				
			}
			ArrayList<JobPlanItem> jobPlanGroupTitle = new ArrayList<JobPlanItem>(); 
			for(int i = 0; i < titleGroups.length;i++)
			{
				JobPlanItem item = new JobPlanItem();
				item.isTitle = true;
				item.titleName = titleGroups[i];
				jobPlanGroupTitle.add(item);
				for(Task t : tasks)
				{
					item = new JobPlanItem();
					
					item.isTitle = false;
					ArrayList<CustomerSurveySite> surveySites = null;
					try {
						/*
						surveySites = dbAdapter.findCustomerSurveySite(
								t.getJobRequest().getCustomerCode(),
								t.getJobRequest().getJobRequestID());
								*/
						surveySites = dbAdapter.findCustomerSurveySite(
								t.getTaskID()
								);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					item.task = t;
					if (surveySites != null)
					{
						if (item.task.getJobRequest().getCustomerSurveySiteList() == null)
						{
							for(CustomerSurveySite surveySite : surveySites){
								item.task.getJobRequest().addCustomerSurveySite(surveySite);							
							}
						}
					}
					jobPlanGroupTitle.add(item);
				}
			}
			TaskResponseList<TaskComplete> taskCompleteResponseList = null;
			TaskResponseList<TaskResend> taskResendResponseList =  null;
			
			try{
				taskCompleteResponseList = getDataAdapter().getTaskComplete();		
				taskResendResponseList = getDataAdapter().getTaskResend();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			JobPlanItemsAdapter adapter = 
						new JobPlanItemsAdapter(this.getActivity(),
												jobPlanGroupTitle,
												taskListType,
												/*taskResendResponseList,*/
												taskCompleteResponseList
												);
			
			lvJobPlan.setAdapter(adapter);
			registerForContextMenu(lvJobPlan);	

			/*
			String[] ctxt_job_plan_menus = getActivity().getResources().getStringArray(
					R.array.txt_plan_job_context_menus);
		
			registerForCustomContextMenu(lvJobPlan, "", ctxt_job_plan_menus);
			*/
			Log.d("DEBUG_D", "Get ListView");
		}
	}
	private void initialCalendarView(){
		
		 
		 final Calendar nextYear = Calendar.getInstance();
		    nextYear.add(Calendar.YEAR, 1);

		    final Calendar lastYear = Calendar.getInstance();
		    lastYear.add(Calendar.YEAR, -1);
		    
		    /*
		    CalendarPickerView calendar = (CalendarPickerView)currentContentView.findViewById(R.id.calendar_view);
		    calendar.init(lastYear.getTime(), nextYear.getTime()) //
		        .inMode(SelectionMode.SINGLE) //
		        .withSelectedDate(new Date());
		        */

	}
	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Log.d("DEBUG_D", "onActivityCreated");
		
		
		/*initial view**/
		currentActivity = getActivity();
		/*
		if (currentContentView != null){
			ViewTreeObserver observer = currentContentView.getViewTreeObserver();
	        observer.addOnGlobalLayoutListener(layoutListener);
		} */ 
		//initialTaskListView();
		Bundle bundle = this.getArguments();
		if (bundle != null)
		{
			int tabMenuIdx = bundle.getInt(InstanceStateKey.TAB_INDEX_KEY);
			Log.d("DEBUG_D", "Get idx = "+tabMenuIdx);
			
			switch(tabMenuIdx)
			{
				case 0:
				{
					initialCalendarView();
				}break;
				case 1:
				{
					//Activity a = this.getActivity();					
					initialTaskListView(TaskListType.TASK_PLAN);
				}break;
				case 2:
				{
					initialTaskListView(TaskListType.DO_TASK);
				}break;
				default:
				{
					initialCalendarView();
				}break;
			}
		}
		
		
		
	}
    /* (non-Javadoc)
	 * @see android.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.lv_fragment_task_plan_list){
		
			final AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
			int pos = adapterMenuInfo.position;
			ListView lv = (ListView)v;
			JobPlanItem item = (JobPlanItem)lv.getAdapter().getItem(pos);
			if (!item.isTitle)
			{
					//String[] ctxt_job_plan_menus = getActivity().getResources().getStringArray(
					//		R.array.txt_plan_job_context_menus);
					
					/*
					for (int i = 0; i<ctxt_job_plan_menus.length; i++) {
					      menu.add(Menu.NONE, i, i, ctxt_job_plan_menus[i]);
					}*/
					//registerForCustomContextMenu(v, "", ctxt_job_plan_menus);
				
				  MenuInflater inflater = getActivity().getMenuInflater();
				  inflater.inflate(R.menu.context_menu_plan_job, menu);
			}
		}else if (v.getId() == R.id.lv_fragment_task_list){
			
			final AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
			int pos = adapterMenuInfo.position;
			ListView lv = (ListView)v;
			JobPlanItem item = (JobPlanItem)lv.getAdapter().getItem(pos);
			if (!item.isTitle)
			{
				   MenuInflater inflater = getActivity().getMenuInflater();
				   inflater.inflate(R.menu.context_menu_do_task, menu);
			}			
		}else
		{
			super.onCreateContextMenu(menu, v, menuInfo);
		}
	}
	/* (non-Javadoc)
	 * @see android.app.Fragment#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();		 
		 long id = info.id;
		 //if (id == 1)
		 {
			 Log.d("DEBUG_D", "DO_TASK_START SELECTED");
			 
			 /*
			  Intent myIntent = new Intent(CurrentActivity.this, NextActivity.class);
myIntent.putExtra("key", value); //Optional parameters
CurrentActivity.this.startActivity(myIntent);
			  */
			 Intent intent = new Intent(this.getActivity(),PsDrawLayoutActivity.class);
			 this.getActivity().startActivity(intent);
			 
		 }
		return super.onContextItemSelected(item);
	}
	// Because the fragment doesn't have a reliable callback to notify us when
    // the activity's layout is completely drawn, this OnGlobalLayoutListener provides
    // the necessary callback so we can add top-margin to the ListView in order to dodge
    // the ActionBar. Which is necessary because the ActionBar is in overlay mode, meaning
    // that it will ordinarily sit on top of the activity layout as a top layer and
    // the ActionBar height can vary. Specifically, when on a small/normal size screen,
    // the action bar tabs appear in a second row, making the action bar twice as tall.
    ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
        	
        	
            //int barHeight = getActivity().getActionBar().getHeight();
        	if (currentActivity == null)
        	{
        		Log.d("DEBUG_D", "getActivity() == null");
        		return;
        	}
            int barHeight = currentActivity.getActionBar().getHeight();
           
            ViewGroup.LayoutParams params1 =  currentContentView.getLayoutParams();
            
            //LayoutParams param =  currentContentView.getLayoutParams()
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) currentContentView.getLayoutParams();
            // The list view top-margin should always match the action bar height
            if (params.topMargin != barHeight) {
                params.topMargin = barHeight;
                //params.topMargin += 10;//padding
                currentContentView.setLayoutParams(params);
            }
            // The action bar doesn't update its height when hidden, so make top-margin zero
            if (!currentActivity.getActionBar().isShowing()) {
              params.topMargin = 0;
              currentContentView.setLayoutParams(params);
            }
            
            currentContentView.invalidate();
        }
    };
}

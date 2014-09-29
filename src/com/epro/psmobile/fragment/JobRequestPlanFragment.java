/**
 * 
 */
package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.epro.psmobile.PsDrawLayoutActivity;
import com.epro.psmobile.PsMainActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.JobPlanItem;
import com.epro.psmobile.adapter.JobPlanItemsAdapter;
import com.epro.psmobile.adapter.JobPlanItemsAdapter.TaskListType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.dialog.TaskManagementDialog;
import com.epro.psmobile.dialog.TaskManagementDialog.OnTaskManagementDialogListener;
import com.epro.psmobile.dialog.TaskManagementDialog.TaskManagementType;
import com.epro.psmobile.dialog.TaskManagementDialog.TaskManagementValue;
import com.epro.psmobile.fragment.CommonListMenuFragment.MenuGroupTypeCmd;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.location.PSMobileLocationManager;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * @author nickmsft
 *
 */
public class JobRequestPlanFragment extends BaseJobTaskList 
implements com.epro.psmobile.fragment.ContentViewBaseFragment.BoardcastLocationListener 
{

	private ListView lvJobPlan;
	
	/*
	 	this.groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_ALL;
		this.selectedInspectType = null;
		this.allInspectType = null;
		this.status = TaskStatus.NOT;
		this.whereInStatus = WhereInStatus.DO_PLAN_TASK;
	 */
	private MenuGroupTypeCmd groupCmd = MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_ALL;
	private InspectType selectedInspectType = null;
	private ArrayList<InspectType> allInspectType = null;
	private TaskStatus status = TaskStatus.NOT;
	private WhereInStatus whereInStatus = WhereInStatus.DO_PLAN_TASK;
	
	private PSMobileLocationManager locManager;
	
	/**
	 * 
	 */
	public JobRequestPlanFragment() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#getView()
	 */
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View currentContentView = inflater.inflate(R.layout.ps_fragment_plan_task_list_view, container, false);
		
	    super.registerLocationListener();

		initialViews(currentContentView);
		
		
		return currentContentView;		
	}
	   /* (non-Javadoc)
	    * @see android.support.v4.app.Fragment#onDestroyView()
	    */
	   @Override
	   public void onDestroyView() {
	      // TODO Auto-generated method stub
	      
	      super.unregisterLocationListener();
	      
	      super.onDestroyView();
	      if (locManager != null)
	      {
	         locManager.stopRequestLocationUpdated();
	      }
	   }
	private void initialViews(View currentContentView){
		/**
		 Query task
		 and add tasks to JobPlanItems
		 */
		
		/**
		 Query task
		 and add tasks to JobPlanItems
		 */
		lvJobPlan = (ListView)currentContentView.findViewById(R.id.lv_fragment_task_plan_list);
		registerForContextMenu(lvJobPlan);	

		//setBoardcastLocationListener(this);
		
		 if (locManager == null)
         {
            PsMainActivity a = null;
            if (getSherlockActivity() instanceof PsMainActivity){
               a = (PsMainActivity)getSherlockActivity();
               locManager = a.getLocationManager();
            }
         }
         
         try 
         {
            setBoardcastLocationListener(this);
            locManager.requestLocationUpdated();
         }
         catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
		ArrayList<Task> tasks = null;
		/*
		try {
			tasks = getDataAdapter().getAllTasks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (tasks == null)return;
		*/
		
	
		
		showTaskList(lvJobPlan, 
				tasks, 
				TaskStatus.NOT, 
				null,
				null,
				MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_ALL, 
				TaskListType.TASK_PLAN,WhereInStatus.DO_PLAN_TASK);

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
									
					  MenuInflater inflater = getActivity().getMenuInflater();
					  inflater.inflate(R.menu.context_menu_plan_job, menu);
					  /*
					  if (item.task != null){
						  if (item.task.getFlagOtherTeam().equalsIgnoreCase("Y")){
							  MenuItem menuShift = menu.findItem(R.id.do_plan_job_shift);
							  menuShift.setVisible(false);
						  }
					  }*/
				}
			}
		}
		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
		 */
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			boolean bRet = true;
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			switch(item.getItemId())
			{
				case R.id.do_plan_job_cancel:{
					View v = info.targetView;
					Object tagValue = v.getTag();
					if (tagValue instanceof JobPlanItem)
					{
						JobPlanItem jobPlanItem = (JobPlanItem)tagValue;
						Task currentTask = jobPlanItem.task;
						TaskManagementDialog taskMgnDlg = TaskManagementDialog.newInstance(TaskManagementType.CANCEL, 
								currentTask,
								R.string.label_cancel_dlg_title,
								R.string.label_btn_cancel,
								R.string.label_simple_dlg_btn_cancel);
						

						taskMgnDlg.setOnTaskManagementDialogListener(new OnTaskManagementDialogListener(){

							@Override
							public void onClickTaskManagementDialogOk(
									TaskManagementValue taskManagementValue) {								
								// TODO Auto-generated method stub
								
								int rowEffected = getDataAdapter().updateTaskCancel(taskManagementValue.getCurrentTask().getTaskCode(), 
										(ReasonSentence)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_REASON), 
										(String)taskManagementValue.getValue(TaskManagementDialog.CANCEL_CAUSE));
								
								if (rowEffected > 0)
								{
									sortTaskList(lvJobPlan, 
											groupCmd, 
											status,
											selectedInspectType,
											allInspectType,
											TaskListType.TASK_PLAN,whereInStatus);
								}
							}

							@Override
							public void onClickTaskManagementDialogCancel() {
								// TODO Auto-generated method stub
								
							}
							
						});
						taskMgnDlg.show(getFragmentManager(), "taskMgnDlg");
					}
					
				}break;
				case R.id.do_plan_job_confirm:{
					View v = info.targetView;
					Object tagValue = v.getTag();
					if (tagValue instanceof JobPlanItem)
					{
						JobPlanItem jobPlanItem = (JobPlanItem)tagValue;
						Task currentTask = jobPlanItem.task;
						TaskManagementDialog taskMgnDlg = TaskManagementDialog.newInstance(
								TaskManagementType.CONFIRM, 
								currentTask,
								R.string.label_confirm_dlg_title,
								R.string.label_btn_confirm,
								R.string.label_simple_dlg_btn_cancel);
						
						taskMgnDlg.setOnTaskManagementDialogListener(new OnTaskManagementDialogListener(){

							@Override
							public void onClickTaskManagementDialogOk(
									TaskManagementValue taskManagementValue) {
								// TODO Auto-generated method stub
								//TaskStatus status = TaskStatus.WAIT_TO_CONFIRM;
								/*
								 * if flag other team is "N" , inspector don't wait to sync download again
								 */
								//if (taskManagementValue.getCurrentTask().getFlagOtherTeam().equalsIgnoreCase("N")){
								//	status = TaskStatus.CONFIRMED_FROM_WEB;
								//}
								int rowEffected = getDataAdapter().updateTaskConfirm(
										taskManagementValue.getCurrentTask().getTaskID(),
										(String)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_TO_START_TIME),
										(String)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_TO_END_TIME),
										TaskStatus.CONFIRM_INSPECT);								
								Log.d("DEBUG_D", "Update task status rowEffected = "+rowEffected);
								if (rowEffected > 0){
									//reload list view
									sortTaskList(lvJobPlan, 
											groupCmd, 
											TaskStatus.CONFIRM_INSPECT,
											selectedInspectType,
											allInspectType,
											TaskListType.TASK_PLAN,whereInStatus);									
								}
							}

							@Override
							public void onClickTaskManagementDialogCancel() {
								// TODO Auto-generated method stub
								
							}
							
						});
						taskMgnDlg.show(getFragmentManager(), "taskMgnDlg");
					}
				
				}break;
				case R.id.do_plan_job_shift:{
					View v = info.targetView;
					Object tagValue = v.getTag();
					if (tagValue instanceof JobPlanItem)
					{
						JobPlanItem jobPlanItem = (JobPlanItem)tagValue;
						Task currentTask = jobPlanItem.task;
						TaskManagementDialog taskMgnDlg = TaskManagementDialog.newInstance(TaskManagementType.POSTPONE, 
								currentTask,
								R.string.label_postpone_dlg_title,
								R.string.label_btn_postpone,
								R.string.label_simple_dlg_btn_cancel);
						
						taskMgnDlg.setOnTaskManagementDialogListener(new TaskManagementDialog.OnTaskManagementDialogListener(){

							@Override
							public void onClickTaskManagementDialogOk(
									TaskManagementValue taskManagementValue) {
								// TODO Auto-generated method stub
								int rowEffected = getDataAdapter().updateTaskPostPone(taskManagementValue.getCurrentTask().getTaskCode(),
										(String)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_TO_DATE), 
										(String)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_TO_START_TIME),
										(String)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_TO_END_TIME),
										(ReasonSentence)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_REASON), 
										(String)taskManagementValue.getValue(TaskManagementDialog.POSTPONE_CAUSE)
										);
								if (rowEffected > 0)
								{
									sortTaskList(lvJobPlan, 
											groupCmd, 
											status,
											selectedInspectType,
											allInspectType,
											TaskListType.TASK_PLAN,whereInStatus);
								}
							}

							@Override
							public void onClickTaskManagementDialogCancel() {
								// TODO Auto-generated method stub
								
							}
							}
						);
						
						taskMgnDlg.show(getFragmentManager(), "taskMgnDlg");
					}
				}break;
				case R.id.do_plan_job_show_detail:{
					if (info != null)
					{
						View v = info.targetView;
						Object tagValue = v.getTag();
						if (tagValue instanceof JobPlanItem)
						{
							JobPlanItem jobPlanItem = (JobPlanItem)tagValue;
							
							FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
							
							CustomerInfoDetailFragment customerDetailFragment = CustomerInfoDetailFragment.newInstance(
									jobPlanItem.task.getJobRequest(),
									jobPlanItem.task,true);
							/*
							JobRequestPlanDetailFragment detailFragment = new JobRequestPlanDetailFragment();
							
							Bundle bundle = new Bundle();
							bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobPlanItem.task.getJobRequest());
							detailFragment.setArguments(bundle);
							*/
							ft.replace(R.id.content_frag,
									customerDetailFragment,
									CustomerInfoDetailFragment.class.getName());
							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
							ft.addToBackStack(null);
							ft.commit();
						}
					}
				}break;
				default:
				{
					bRet = super.onContextItemSelected(item);					
				}break;
			}
			return bRet;
		}
		
		@Override
		public void sortTaskListByStatus(MenuGroupTypeCmd groupCmd,
				InspectType selectedInspectType,
				ArrayList<InspectType> allInspectType,
				TaskStatus status,
				WhereInStatus whereInStatus) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			/*
			 * save 
			 */
			this.groupCmd = groupCmd;
			this.selectedInspectType = selectedInspectType;
			this.allInspectType = allInspectType;
			this.status = status;
			this.whereInStatus = whereInStatus;
			
			sortTaskList(lvJobPlan, 
					groupCmd, 
					status,
					selectedInspectType,
					allInspectType,
					TaskListType.TASK_PLAN,whereInStatus);
		}
		
		
	@Override
	public void onBoardcastLocationUpdated(Location currentLocation) {
			// TODO Auto-generated method stub
		if (lvJobPlan != null)
		{
			JobPlanItemsAdapter adapter = (JobPlanItemsAdapter)lvJobPlan.getAdapter();
			adapter.setCurrentLocation(currentLocation);
			adapter.notifyDataSetChanged();
		}
	}
}

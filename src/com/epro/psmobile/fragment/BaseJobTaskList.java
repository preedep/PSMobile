package com.epro.psmobile.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.widget.ListView;

import com.epro.psmobile.PsMainActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.JobPlanItem;
import com.epro.psmobile.adapter.JobPlanItemsAdapter;
import com.epro.psmobile.adapter.JobPlanItemsAdapter.TaskListType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskComplete;
import com.epro.psmobile.data.TaskResend;
import com.epro.psmobile.data.TaskResponseList;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.fragment.CommonListMenuFragment.MenuGroupTypeCmd;
import com.epro.psmobile.location.PSMobileLocationManager;
import com.epro.psmobile.util.DataUtil;

public abstract class BaseJobTaskList extends ContentViewBaseFragment {

	private JobPlanItemsAdapter itemAdapter;
	
	private final static int IDX_WAIT_TO_CONFIRM = 0;
	private final static int IDX_INSPECT_CONFIRMED = 1;
	private final static int IDX_SHIFT = 2;
	private final static int IDX_CANCEL = 3;
	
	private final static int IDX_FINISH = 0;
	private final static int IDX_LOCAL_SAVED = 1;
	private final static int IDX_CONFIRM = 2;
	private final static int IDX_ALLOW_EDIT = 3;
	
	
	private final static int IDX_TODAY = 0;
	private final static int IDX_NEXT_WEEK = 1;
	private final static int IDX_CUSTOM_TIME = 2;
	
	private Date selectedDate;
	
	public BaseJobTaskList() {
		// TODO Auto-generated constructor stub
	}
	protected ArrayList<Task> getTaskListByStatus(TaskStatus status,WhereInStatus whereInStatus) throws Exception
	{
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		return dataAdapter.getAllTasks(status,whereInStatus);
	}
	protected ArrayList<Task> getTaskListByToday(WhereInStatus whereInStatus)throws Exception
	{
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		return this.getDataAdapter().getAllTasksToday(today,whereInStatus);
	}
	protected ArrayList<Task> getTaskListBySelectedDate(WhereInStatus whereInStatus) throws Exception
	{
		return getDataAdapter().getAllTasksToday(getSelectedDate(),whereInStatus);		
	}
	protected ArrayList<Task> getTaskListByNextWeek(WhereInStatus whereInStatus) throws Exception
	{
		Calendar cal = Calendar.getInstance();
		Date today = DataUtil.getZeroTimeDate(cal.getTime());
		
		cal = Calendar.getInstance();		
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date startNextWeek = null;
		Date endNextWeek = null;
		for(int i = 0;i < 2;/*next 1 week*/i++){
			startNextWeek = DataUtil.getZeroTimeDate(cal.getTime());
			cal.add(Calendar.DAY_OF_WEEK, 6);
			endNextWeek = DataUtil.getZeroTimeDate(cal.getTime());
			/*
			if (startNextWeek.compareTo(today) == 0){
				break;
			}else{
				if (startNextWeek.after(today))
					break;
			}*/
			
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
		return this.getDataAdapter().getAllTasksNextWeek(startNextWeek, endNextWeek,whereInStatus);
	}
	public Date getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	protected void sortTaskList(ListView lv ,
			MenuGroupTypeCmd groupCmd,
			TaskStatus status,
			InspectType selectedInspectType,
			ArrayList<InspectType> allInspectType,
			TaskListType taskListType,
			WhereInStatus whereInStatus)
	{
		ArrayList<Task> taskList = null;
		
		try {
			
			 	if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_TODAY)
			 	{
			 		this.selectedDate = null;
			 		taskList = getTaskListByToday(whereInStatus);
			 	}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_NEXT_WEEK){
			 		this.selectedDate = null;
			 		taskList = getTaskListByNextWeek(whereInStatus);
			 	}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_CUSTOM_TIME){
			 		taskList = getTaskListBySelectedDate(whereInStatus);
			 	}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ID){
			 		this.selectedDate = null;
			 		taskList = this.getDataAdapter().getAllTasksByInspectID(selectedInspectType.getInspectTypeID(),whereInStatus);
			 	}else
			 	{
			 		/*
			 		 * select by status
			 		  */
			 		this.selectedDate = null;
			 		taskList = new ArrayList<Task>();
			 		
			 		ArrayList<Task>	taskTmpList = getTaskListByStatus(status,whereInStatus);
			 		if (taskTmpList != null){
			 			taskList.addAll(taskTmpList);
			 		}
			 		/*
			 		if (status == TaskStatus.WAIT_TO_CONFIRM){
			 			ArrayList<Task> taskWaitToConfirmList = getTaskListByStatus(TaskStatus.CONFIRM_INSPECT,whereInStatus);
			 			if (taskWaitToConfirmList != null)
			 			{
			 				taskList.addAll(taskWaitToConfirmList);
			 			}
			 		}
			 		*/			 		
			 	}
			 	showTaskList(lv, taskList,status,selectedInspectType,allInspectType, groupCmd, taskListType,whereInStatus);
			} 
			catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
	}
	public abstract void sortTaskListByStatus(MenuGroupTypeCmd groupCmd,
			InspectType selectedInspectType,
			ArrayList<InspectType> allInspectType,
			TaskStatus status,
			WhereInStatus whereInStatus);
	
	
	protected void showTaskList(ListView lv,
			ArrayList<Task> tasks,
			TaskStatus filterByTask,
			InspectType selectedInspectType,
			ArrayList<InspectType> allInspectType,
			MenuGroupTypeCmd groupCmd,
			TaskListType taskListType,
			WhereInStatus whereInStatus)
	{
		String[] titleGroups = null;
		if ((groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_ALL)||
			(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS)||
			(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK)||
			(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK_ALL)){
			if (
					(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK_ALL)||
					(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK)
				)
				/*
				 * change sort type header
				 */
				titleGroups = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_task_sort_by_task_status);
			else
				titleGroups = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_task_sort_by_plan_job_status);				
		}else if ((groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_ALL)||
				(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_TODAY)||
				(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_NEXT_WEEK)||
				(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_CUSTOM_TIME)
				)
		{
			titleGroups = this.getActivity().getResources().getStringArray(R.array.sub_menu_group_sort_array_2);							
		}else if ((groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ALL)||
				(groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ID))
		{
			if (allInspectType != null){
				titleGroups = new String[allInspectType.size()];
				int idx = 0;
				for(InspectType ins : allInspectType)
				{
					titleGroups[idx] = ins.getInspectTypeName();
					idx++;
				}				
			}
		}
		ArrayList<JobPlanItem> jobPlanGroupTitle = new ArrayList<JobPlanItem>(); 
		for(int i = 0; i < titleGroups.length;i++)
		{
			if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS){
				
				/*
				 * skip for filter data each sort type
				 * 
				 * plan
				 */
				if (filterByTask == TaskStatus.WAIT_TO_CONFIRM){
					if (i != IDX_WAIT_TO_CONFIRM)continue;				
				}else if (filterByTask == TaskStatus.CONFIRM_INSPECT){
					if (i != IDX_INSPECT_CONFIRMED)continue;
				}else if (filterByTask == TaskStatus.SHIFT){
					if (i != IDX_SHIFT)continue;
				}else if (filterByTask == TaskStatus.CANCEL){
					if (i != IDX_CANCEL)continue;
				}
				
			}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK){
				/*
				 * skip for filter data each sort type
				 * 
				 * task must todo
				 */
				if ((filterByTask == TaskStatus.CONFIRMED_FROM_WEB)||
					(filterByTask == TaskStatus.DUPLICATED))
				{
					if (i != IDX_CONFIRM)continue;
				}else if ((filterByTask == TaskStatus.LOCAL_SAVED))
				{
					if (i != IDX_LOCAL_SAVED)continue;
				}else if (filterByTask == TaskStatus.FINISH){
					if (i != IDX_FINISH)continue;
				}else if (filterByTask == TaskStatus.ALLOW_EDIT){
					if (i != IDX_ALLOW_EDIT)continue;
				}

			}
			else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_TODAY){
				if (i != IDX_TODAY)continue;
			}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_NEXT_WEEK){
				if (i != IDX_NEXT_WEEK)continue;
			}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_ALL){
				if (i == IDX_CUSTOM_TIME){//skip custom time for show time all
					break;
				}				
			}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_CUSTOM_TIME){
				if (i != IDX_CUSTOM_TIME)continue;
			}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ID){
				String strTitleGroups = titleGroups[i];
				if (!strTitleGroups.equalsIgnoreCase(selectedInspectType.getInspectTypeName()))continue;
			}
			
			JobPlanItem item = new JobPlanItem();
			item.isTitle = true;
			if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_CUSTOM_TIME){
				if (getSelectedDate() != null){
					item.titleName = DataUtil.convertDateToStringDDMMYYYY(getSelectedDate());
				}
			}else{
				item.titleName = titleGroups[i];
			}
			jobPlanGroupTitle.add(item);
						
				if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_ALL){
					/*
					 * 
					 */
					
				  try{
					if (i == IDX_WAIT_TO_CONFIRM){
						ArrayList<Task> tasksWaitToConfirm = getDataAdapter().getAllTasks(TaskStatus.WAIT_TO_CONFIRM, whereInStatus);
						//ArrayList<Task> tasksConfirmInspect
						//= getDataAdapter().getAllTasks(TaskStatus.CONFIRM_INSPECT, whereInStatus);
						tasks = new ArrayList<Task>();
						if (tasksWaitToConfirm != null)
							tasks.addAll(tasksWaitToConfirm);
						//if (tasksConfirmInspect != null)
						//	tasks.addAll(tasksConfirmInspect);						
					}else if (i == IDX_INSPECT_CONFIRMED){
					   tasks= getDataAdapter().getAllTasks(TaskStatus.CONFIRM_INSPECT, whereInStatus);
					}
					else if (i == IDX_SHIFT){
						tasks = getDataAdapter().getAllTasks(TaskStatus.SHIFT, whereInStatus);
					}else if (i == IDX_CANCEL){
						tasks = getDataAdapter().getAllTasks(TaskStatus.CANCEL, whereInStatus);
					}
				  }catch(Exception ex){
					  ex.printStackTrace();
				  }
				}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_STATUS_DO_TASK_ALL)
				{
					/*
					ArrayList<Task> tmpTasks = new ArrayList<Task>();
					if (tasks != null)
					{
						for(Task t : tasks){
							if (i == IDX_FINISH){
								if (t.getTaskStatus() == TaskStatus.FINISH){
									tmpTasks.add(t);
									
								}
							} 
							if (i == IDX_LOCAL_SAVED){
									if (t.getTaskStatus() == TaskStatus.LOCAL_SAVED){
										tmpTasks.add(t);
									}
							}
							if (i == IDX_CONFIRM){
								if (t.getTaskStatus() == TaskStatus.CONFIRM_INSPECT){
									tmpTasks.add(t);
								}
							}
						}
					}										
					tasks = tmpTasks;
					*/
					try{
						if (i == IDX_FINISH){
							tasks = getDataAdapter().getAllTasks(TaskStatus.FINISH, whereInStatus);
						}else if (i == IDX_LOCAL_SAVED){
							//tasks = new ArrayList<Task>();
							
							tasks = getDataAdapter().getAllTasks(TaskStatus.LOCAL_SAVED, whereInStatus);
							//ArrayList<Task> tasks_allowEdit = getDataAdapter().getAllTasks(TaskStatus.ALLOW_EDIT, whereInStatus);
							/*
							if (tasks_localSaved != null)
							{
								tasks.addAll(tasks_localSaved);
							}*/
							/*
							if (tasks_allowEdit != null){
								tasks.addAll(tasks_allowEdit);
							}
							*/
							/*
							Collections.sort(tasks, new Comparator<Task>(){

								@Override
								public int compare(Task lhs, Task rhs) {
									// TODO Auto-generated method stub
									Date d1 = DataUtil.getZeroTimeDate(lhs.getTaskDate());
									Date d2 = DataUtil.getZeroTimeDate(rhs.getTaskDate());
									return d1.compareTo(d2);
								}
								
							});*/
							
						}else if (i == IDX_ALLOW_EDIT){
							tasks = getDataAdapter().getAllTasks(TaskStatus.ALLOW_EDIT, whereInStatus);
						}
						else if (i == IDX_CONFIRM){
							ArrayList<Task> tasksInspect = getDataAdapter().getAllTasks(TaskStatus.CONFIRM_INSPECT, whereInStatus);
							ArrayList<Task> tasksConfirmed = getDataAdapter().getAllTasks(TaskStatus.CONFIRMED_FROM_WEB, whereInStatus);
							ArrayList<Task> taskDuplicated = getDataAdapter().getAllTasks(TaskStatus.DUPLICATED, whereInStatus);
							
							tasks = new ArrayList<Task>();
							
							if (tasksInspect != null)
								tasks.addAll(tasksInspect);
							if (tasksConfirmed != null)
								tasks.addAll(tasksConfirmed);
							if (taskDuplicated != null)
								tasks.addAll(taskDuplicated);
							
							Collections.sort(tasks, new Comparator<Task>(){

								@Override
								public int compare(Task lhs, Task rhs) {
									// TODO Auto-generated method stub
									Date d1 = DataUtil.getZeroTimeDate(lhs.getTaskDate());
									Date d2 = DataUtil.getZeroTimeDate(rhs.getTaskDate());
									return d1.compareTo(d2);
								}
								
							});
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_ALL){
					/*
					 * 
					 */
					try{
						if (i == IDX_TODAY){
							tasks = getTaskListByToday(whereInStatus);
						}else if (i == IDX_NEXT_WEEK){
							tasks = getTaskListByNextWeek(whereInStatus);
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}					
				}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_TODAY){
					try {
						tasks = getTaskListByToday(whereInStatus);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_TIME_NEXT_WEEK){
					try {
						tasks = getTaskListByNextWeek(whereInStatus);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ID){
					try{
						
						tasks = this.getDataAdapter().getAllTasksByInspectID(selectedInspectType.getInspectTypeID(),whereInStatus);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}else if (groupCmd == MenuGroupTypeCmd.SHOW_TASK_BY_INSPECT_ALL){					
					try{
						tasks = this.getDataAdapter().getAllTasksByInspectID(
								allInspectType.get(i).getInspectTypeID(),whereInStatus
								);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}

				/*
				 
				 */
			if (tasks != null)
			{
				for(Task t : tasks)
				{
					item = new JobPlanItem();
				
					item.isTitle = false;
					ArrayList<CustomerSurveySite> surveySites = null;
					
					try 
					{
						/*
						surveySites = getDataAdapter().findCustomerSurveySite(
							t.getJobRequest().getCustomerCode(),
							t.getJobRequest().getJobRequestID()
							);*/
						surveySites = getDataAdapter().findCustomerSurveySite(
								t.getTaskID()
								);

					} catch (Exception e) 
					{
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
			}
		
		TaskResponseList<TaskComplete> taskCompleteResponseList = null;
		TaskResponseList<TaskResend> taskResendResponseList =  null;
		
		try{
			taskCompleteResponseList = getDataAdapter().getTaskComplete();		
			taskResendResponseList = getDataAdapter().getTaskResend();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		itemAdapter = new JobPlanItemsAdapter(this.getActivity(),
				jobPlanGroupTitle,
				taskListType,
				/*taskResendResponseList,*/
				taskCompleteResponseList
				);
		
		 PSMobileLocationManager locManager = null;
		 if (locManager == null)
         {
            PsMainActivity a = null;
            if (getSherlockActivity() instanceof PsMainActivity){
               a = (PsMainActivity)getSherlockActivity();
               locManager = a.getLocationManager();
               itemAdapter.setCurrentLocation(locManager.getLastknowLocation());
            }
         }
		lv.setAdapter(itemAdapter);
		
	}
}

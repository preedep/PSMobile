package com.epro.psmobile.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.epro.psmobile.R;
import com.epro.psmobile.R.id;
import com.epro.psmobile.R.layout;
import com.epro.psmobile.adapter.JobPlanCustomerInfoHelper;
import com.epro.psmobile.data.ReasonSentence.ReasonType;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.GeoLocUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.view.ReasonSentenceSpinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TextView.OnEditorActionListener;

public class TaskManagementDialog extends DialogFragment 
{


	public final static String POSTPONE_TO_DATE = "postPoneToDate";
	public final static String POSTPONE_TO_START_TIME = "postPoneToStartTime";
	public final static String POSTPONE_TO_END_TIME = "postPoneToEndTime";
	public final static String POSTPONE_CAUSE = "postPoneCause";
	public final static String POSTPONE_REASON = "postPoneReason";
	
	public final static String CANCEL_CAUSE = "cancelCause";
	
	private final static String KEY_TASK_MGN_DLG = "KEY_TASK_MGN_DLG";
	private final static String KEY_TASK_VALUE = "KEY_TASK_VALUE";
	private final static String KEY_TASK_DLG_TITLE = "KEY_TASK_DLG_TITLE";
	private final static String KEY_TASK_BTN_TEXT_OK = "KEY_TASK_BTN_TEXT_OK";
	private final static String KEY_TASK_BTN_TEXT_CANCEL = "KEY_TASK_BTN_TEXT_CANCEL";
	
	public enum TaskManagementType
	{
		CONFIRM(0),
		POSTPONE(1),
		CANCEL(2);
		
		private int typeValue;
		
		TaskManagementType(int typeValue)
		{
			this.typeValue = typeValue;			
		}
		
		public int getTypeValue(){
			return this.typeValue;					
		}
		
		public static TaskManagementType getType(int typeValue){
			
			for(int i = 0; i < values().length;i++)
			{
				if (values()[i].getTypeValue() == typeValue)
				{
					return values()[i];
				}
			}
			
			return TaskManagementType.CONFIRM;
		}
	}
	
	public class TaskManagementValue{
		
		private Task currentTask;
		private TaskManagementType taskManagementType = TaskManagementType.CONFIRM;
		private Hashtable<String,Object> datas = null;
		
		public void addValue(String key,Object value){
			if (datas == null)
			{
				datas = new Hashtable<String,Object>();				
			}
			datas.put(key, value);
		}
		public Object getValue(String key)
		{
			if (datas != null)
				return datas.get(key);
			else
				return null;
		}
		public Task getCurrentTask() {
			return currentTask;
		}
		public void setCurrentTask(Task currentTask) {
			this.currentTask = currentTask;
		}

		public TaskManagementType getTaskManagementType() {
			return taskManagementType;
		}

		public void setTaskManagementType(TaskManagementType taskManagementType) {
			this.taskManagementType = taskManagementType;
		}
		
		
	}
	public interface OnTaskManagementDialogListener{
		void onClickTaskManagementDialogOk(TaskManagementValue taskManagementValue);
		void onClickTaskManagementDialogCancel();
	}

	private View currentView;
	private OnTaskManagementDialogListener onTaskManagementDialogListener;
	private Button btnOk;
	private Button btnCancel;
	private EditText edt_postpone_date;
	private EditText edt_postpone_start_time;
	private EditText edt_postpone_end_time;
	private EditText edt_postpone_cause;
	
	private EditText edt_confirm_start_time;
	private EditText edt_confirm_end_time;
	
	private Task currentTask;
	
	private int mYear;
	private int mMonth;
	private int mDay;
	
	private int mHour;
	private int mMinute;
	
	private TimePickerDialog dlgStartTime;
	private TimePickerDialog dlgEndTime;
	
	private DatePickerDialog dlgDate;
	
	private ReasonSentenceSpinner reason;
	private ReasonSentenceSpinner reason_cancel;
			
	private Location currentLocation;
	
	private ViewGroup vGroupConfirm;
	private View vGroupConfirmRoot;
	
	private boolean isPostPone = false;
	protected BroadcastReceiver mLocationReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			currentLocation = arg1.getParcelableExtra(InstanceStateKey.KEY_BDX_LOC_UPDATED);
			
			if (currentLocation != null){
				Log.d("DEBUG_D", "TaskManagementDialog:BroadcastReceiver:onReceive -> " +
						"lat = "+currentLocation.getLatitude()+" , lon = "+currentLocation.getLongitude());
				
				if ((vGroupConfirm != null)&&(currentTask != null))
					setupTaskStatusDisplay(vGroupConfirm,currentTask);
				
			}
		}		
	};
	
	public static TaskManagementDialog newInstance(TaskManagementType taskMgnType,
			Task task,
			int resTextTitle,
			int resTextOk,
			int resTextCancel){

		TaskManagementDialog taskMgnDlg = new TaskManagementDialog();
		Bundle bArgument = new Bundle();
		bArgument.putInt(KEY_TASK_MGN_DLG, taskMgnType.getTypeValue());
		bArgument.putParcelable(KEY_TASK_VALUE, task);
		bArgument.putInt(KEY_TASK_DLG_TITLE, resTextTitle);
		bArgument.putInt(KEY_TASK_BTN_TEXT_OK, resTextOk);
		bArgument.putInt(KEY_TASK_BTN_TEXT_CANCEL, resTextCancel);
		taskMgnDlg.setArguments(bArgument);
		return taskMgnDlg;
	}
	private TaskManagementDialog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
//		this.setStyle(STYLE_NO_TITLE,getTheme());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		currentView = inflater.inflate(R.layout.task_management_dialog, container, false);
		initial(currentView,inflater);
		
		IntentFilter filter = new IntentFilter(InstanceStateKey.BDX_LOC_UPDATED_ACTION);
		this.getActivity().registerReceiver(mLocationReceiver, filter);
		return currentView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mLocationReceiver != null)
		{
			this.getActivity().unregisterReceiver(mLocationReceiver);
		}
		super.onDestroy();
	}
	private void initial(View v,LayoutInflater inflater)
	{
		Bundle bArgument = getArguments();
		if (bArgument != null)
		{
			int taskTypeValue = bArgument.getInt(KEY_TASK_MGN_DLG);
			currentTask = bArgument.getParcelable(KEY_TASK_VALUE);
			
			/*
			bArgument.putInt(KEY_TASK_DLG_TITLE, resTextTitle);
			bArgument.putInt(KEY_TASK_BTN_TEXT_OK, resTextOk);
			bArgument.putInt(KEY_TASK_BTN_TEXT_CANCEL, resTextCancel); 
			 */
			
			int resTextTitle = bArgument.getInt(KEY_TASK_DLG_TITLE);
			int resTextOk = bArgument.getInt(KEY_TASK_BTN_TEXT_OK);
			int resTextCancel = bArgument.getInt(KEY_TASK_BTN_TEXT_CANCEL);
			
			this.getDialog().setTitle(resTextTitle);
			
			btnOk = (Button)v.findViewById(R.id.btn_do_task_management);
			btnCancel = (Button)v.findViewById(R.id.btn_cancel_task_management);
			
			btnOk.setText(resTextOk);
			btnCancel.setText(resTextCancel);

			
			View customerInfoItem = v.findViewById(R.id.inc_customer_information_item);
			JobPlanCustomerInfoHelper.fillCustomerInfo(this.getActivity(), 
					currentTask, 
					customerInfoItem);/**/
			
			TaskManagementType taskMgnType = TaskManagementType.getType(taskTypeValue);
			
			switch (taskMgnType)
			{
				case CONFIRM:
				{
					vGroupConfirmRoot = inflater.inflate(R.layout.task_management_confirm, null, false);
					
					vGroupConfirm = (ViewGroup)v.findViewById(R.id.ll_task_management_content);
					vGroupConfirm.addView(vGroupConfirmRoot);
					setupTaskStatusDisplay(vGroupConfirm,currentTask);
					
					vGroupConfirm.invalidate();
					setupConfirm(vGroupConfirmRoot);
				}break;
				case POSTPONE:
				{
					View vPostpone = inflater.inflate(R.layout.task_management_postpone, null, false);					
					ViewGroup vContent = (ViewGroup)v.findViewById(R.id.ll_task_management_content);
					vContent.addView(vPostpone);
					vContent.invalidate();

					setupPostPoneView(vPostpone);
					
				}break;
				case CANCEL:
				{
					vGroupConfirmRoot = inflater.inflate(R.layout.task_management_cancel, null, false);					
					vGroupConfirm = (ViewGroup)v.findViewById(R.id.ll_task_management_content);
					vGroupConfirm.addView(vGroupConfirmRoot);
					setupTaskStatusDisplay(vGroupConfirm,currentTask);

					vGroupConfirm.invalidate();
					setupCancel(vGroupConfirmRoot);
				}break;
			}
		}
	}
	public void setupCancel(View v)
	{
		final EditText ed_cancel_cause = (EditText)v.findViewById(R.id.et_cancel_cause);

		reason_cancel = (ReasonSentenceSpinner)v.findViewById(R.id.sp_reason_cancel);
		try {
			reason_cancel.initialWithReasonType(ReasonType.REASON_REJECT_REQIEST);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (btnOk != null){
			btnOk.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TaskManagementDialog.this.dismiss();
					
					TaskManagementValue value = new TaskManagementValue();
					value.setTaskManagementType(TaskManagementType.CANCEL);
					value.setCurrentTask(currentTask);
					value.addValue(CANCEL_CAUSE, ed_cancel_cause.getText().toString());
					value.addValue(POSTPONE_REASON,reason_cancel.getItemAtPosition(reason_cancel.getSelectedItemPosition()));
					if (getOnTaskManagementDialogListener() != null)
					{
						getOnTaskManagementDialogListener().onClickTaskManagementDialogOk(value);
					}
				}
			});
		}
		if (btnCancel != null)
		{
			btnCancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TaskManagementDialog.this.dismiss();
					if (getOnTaskManagementDialogListener() != null)
					{
						getOnTaskManagementDialogListener().onClickTaskManagementDialogCancel();
					}
				}
				
			});
		}
	}
	public void setupConfirm(View v)	
	{
		
		 edt_confirm_start_time = (EditText)v.findViewById(R.id.et_confirm_start_time);
		 edt_confirm_start_time.setInputType(InputType.TYPE_NULL);
		 
		 edt_confirm_end_time = (EditText)v.findViewById(R.id.et_confirm_end_time);
		 edt_confirm_end_time.setInputType(InputType.TYPE_NULL);
	
		 
		 final Calendar c = Calendar.getInstance();
		       
		 mHour = c.get(Calendar.HOUR_OF_DAY);
		 mMinute = c.get(Calendar.MINUTE);

			
		    
		 String forceTime = "N";
		 if (currentTask != null)
		 {
			 forceTime = currentTask.getForceTime();
		 }
		 if (forceTime.trim().equalsIgnoreCase("Y")){
			 edt_confirm_start_time.setEnabled(false);
		 }
		 
		 edt_confirm_start_time.setText(currentTask.getTaskStartTime());
		 edt_confirm_end_time.setText(currentTask.getTaskEndTime());
		 
		 edt_confirm_start_time.setOnEditorActionListener(mOnEditorActionListener);
		 
		 edt_confirm_end_time.setOnEditorActionListener(mOnEditorActionListener);

		    
		    
		 edt_confirm_start_time.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showTimePickerDialog(dlgStartTime,edt_confirm_start_time);
				}
		    	
		    });
		    
		 edt_confirm_end_time.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showTimePickerDialog(dlgEndTime,edt_confirm_end_time);
				}
		    	
		    });
		 
		if (btnOk != null){
			btnOk.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Date taskDate = currentTask.getTaskDate();
					//Calendar c = Calendar.getInstance();
					
					//Date currentDate = DataUtil.getZeroTimeDate(c.getTime());
					
					try{
//						int hh = c.get(Calendar.HOUR_OF_DAY);
//						int mm = c.get(Calendar.MINUTE);
						
						
						
						String forceTime = "N";
						
						if (currentTask.getForceTime() != null){
							forceTime = currentTask.getForceTime();
						}
						if (forceTime.trim().equalsIgnoreCase("Y"))
						{
							Calendar cTaskDate = Calendar.getInstance();
							cTaskDate.setTime(taskDate);
							String startTime = edt_confirm_start_time.getText().toString();
							String[] strTimeSplit = startTime.split(":");
							String sStartHours = strTimeSplit[0];
							String sStartMinutes = strTimeSplit[1];
							cTaskDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sStartHours));
							cTaskDate.set(Calendar.MINUTE,Integer.parseInt(sStartMinutes));
							cTaskDate.set(Calendar.SECOND, 0);
							cTaskDate.set(Calendar.MILLISECOND, 0);
							Date taskDateAndTime = cTaskDate.getTime();

							Calendar cCurrent = Calendar.getInstance();
							cCurrent.set(Calendar.SECOND, 0);
							cCurrent.set(Calendar.MILLISECOND, 0);
							Date currentDateAndTime = cCurrent.getTime();
							
							if (taskDateAndTime.before(currentDateAndTime))
							 {
								 MessageBox.showMessage(getActivity(), R.string.text_error_title, R.string.text_error_confirm_job_previous_date);
								 return;
							 }
						}else{
							/*can confirm today*/
							//ignore time
							Calendar cToday = Calendar.getInstance();
							Date today = DataUtil.getZeroTimeDate(cToday.getTime());
							Date task_Date = DataUtil.getZeroTimeDate(currentTask.getTaskDate());
							
							if (task_Date.before(today)){
								 MessageBox.showMessage(getActivity(), R.string.text_error_title, R.string.text_error_confirm_job_previous_date);
								 return;								
							}
						}
						
					}catch(Exception ex){}
					
					TaskManagementDialog.this.dismiss();
					
					TaskManagementValue value = new TaskManagementValue();
					value.setTaskManagementType(TaskManagementType.CONFIRM);
					value.setCurrentTask(currentTask);
					
					value.addValue(POSTPONE_TO_START_TIME, edt_confirm_start_time.getText().toString());
					value.addValue(POSTPONE_TO_END_TIME, edt_confirm_end_time.getText().toString());

					
					if (getOnTaskManagementDialogListener() != null)
					{
						getOnTaskManagementDialogListener().onClickTaskManagementDialogOk(value);
					}
				}
			});
		}
		if (btnCancel != null)
		{
			btnCancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TaskManagementDialog.this.dismiss();
					if (getOnTaskManagementDialogListener() != null)
					{
						getOnTaskManagementDialogListener().onClickTaskManagementDialogCancel();
					}
				}
				
			});
		}
	}
	public void setupPostPoneView(View v)
	{
		 isPostPone = true;
		 edt_postpone_date = (EditText)v.findViewById(R.id.et_postpone_date);
		 edt_postpone_date.setInputType(InputType.TYPE_NULL);
		
		 
		 edt_postpone_start_time = (EditText)v.findViewById(R.id.et_postpone_start_time);
		 edt_postpone_start_time.setInputType(InputType.TYPE_NULL);
		 
		 
		 
		 edt_postpone_end_time = (EditText)v.findViewById(R.id.et_postpone_end_time);
		 edt_postpone_end_time.setInputType(InputType.TYPE_NULL);
		 
		 edt_postpone_cause = (EditText)v.findViewById(R.id.et_postpone_cause);
		 
		  reason = (ReasonSentenceSpinner)v.findViewById(R.id.sp_reasonSentencePostPone);
		 try {
			reason.initialWithReasonType(ReasonType.REASON_CHANGE_TASK_PLAN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 InputFilter timeFilter  = new InputFilter() {


				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					// TODO Auto-generated method stub
					   if (source.length() == 0) {
			                return null;// deleting, keep original editing
			            }
			            String result = "";
			            result += dest.toString().substring(0, dstart);
			            result += source.toString().substring(start, end);
			            result += dest.toString().substring(dend, dest.length());

			            if (result.length() > 5) {
			                return "";// do not allow this edit
			            }
			            boolean allowEdit = true;
			            char c;
			            if (result.length() > 0) {
			                c = result.charAt(0);
			                allowEdit &= (c >= '0' && c <= '2');
			            }
			            if (result.length() > 1) {
			                c = result.charAt(1);
			                if(result.charAt(0) == '0' || result.charAt(0) == '1')
			                    allowEdit &= (c >= '0' && c <= '9');
			                else
			                    allowEdit &= (c >= '0' && c <= '3');
			            }
			            if (result.length() > 2) {
			                c = result.charAt(2);
			                allowEdit &= (c == ':');
			            }
			            if (result.length() > 3) {
			                c = result.charAt(3);
			                allowEdit &= (c >= '0' && c <= '5');
			            }
			            if (result.length() > 4) {
			                c = result.charAt(4);
			                allowEdit &= (c >= '0' && c <= '9');
			            }
			            return allowEdit ? null : "";
				}

		    };
		
		 //edt_postpone_time.setFilters(new InputFilter[]{timeFilter});

		 // get the current date
	    final Calendar c = Calendar.getInstance();
	    mYear = c.get(Calendar.YEAR);
	    mMonth = c.get(Calendar.MONTH)+1;
	    mDay = c.get(Calendar.DAY_OF_MONTH);
	    
	    mHour = c.get(Calendar.HOUR_OF_DAY);
	    mMinute = c.get(Calendar.MINUTE);

		
	    
		edt_postpone_start_time.setText(currentTask.getTaskStartTime());
		edt_postpone_end_time.setText(currentTask.getTaskEndTime());
		
		edt_postpone_date.setText(DataUtil.convertDateToStringYYYYMMDD(currentTask.getTaskDate()));

		if (currentTask.getFlagOtherTeam().equalsIgnoreCase("Y")){
			edt_postpone_date.setEnabled(false);
		}
	    edt_postpone_start_time.setOnEditorActionListener(mOnEditorActionListener);
	    edt_postpone_end_time.setOnEditorActionListener(mOnEditorActionListener);	    
	    if (currentTask.getForceTime().equalsIgnoreCase("Y"))
	    {
	    	edt_postpone_start_time.setEnabled(false);
	    	edt_postpone_end_time.setEnabled(false);
	    }
	    /////////////////////////////////////////////////////
	    edt_postpone_start_time.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog(dlgStartTime,edt_postpone_start_time);
			}
	    	
	    });
	    edt_postpone_end_time.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog(dlgEndTime,edt_postpone_end_time);
			}
	    	
	    });
	    
	    /*
	    edt_postpone_date.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				InputMethodManager inputManager = (InputMethodManager)
						TaskManagementDialog.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                          InputMethodManager.RESULT_UNCHANGED_SHOWN);
				return true;
			}
	    	
	    });
	    */
	    edt_postpone_date.setOnEditorActionListener(mOnEditorActionListener);
	    
	    edt_postpone_date.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				InputMethodManager inputManager = (InputMethodManager)
						TaskManagementDialog.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 

				inputManager.hideSoftInputFromWindow(
						TaskManagementDialog.this.getActivity().getCurrentFocus().getWindowToken(),
                           InputMethodManager.HIDE_NOT_ALWAYS);
				*/
				if (dlgDate == null)
				dlgDate = new DatePickerDialog(TaskManagementDialog.this.getActivity(), 
						mDateSetListener, 
						mYear, 
						mMonth - 1,
		                mDay);
				
				if (!dlgDate.isShowing())
					dlgDate.show();
			}
	    	
	    });
	    
		if (btnOk != null){
			btnOk.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String postPoneDate = edt_postpone_date.getText().toString();
					String postPoneTime = edt_postpone_start_time.getText().toString();
					
					if (
						  (postPoneDate.isEmpty()||
						  (postPoneTime.isEmpty()))
						)
					{
						//alert
						MessageBox.showMessage(getActivity(), R.string.label_postpone_dlg_title, R.string.text_error_required_field);
						return;
					}
					
					/*
					 * compare date
					 */
					Date dtpostPoneDate = DataUtil.convertToDateYYYYMMDD(postPoneDate);
					Date scheduledTaskDate = currentTask.getTaskScheduleDate();
					Calendar c = Calendar.getInstance();
					
					Date dtNow = c.getTime();
					/*
					if (dtpostPoneDate.compareTo(scheduledTaskDate) > 0)
					{
						//
						String scheduledTasDateString = DataUtil.convertDateToStringYYYYMMDD(scheduledTaskDate);
						String errorMsg = getActivity().getString(R.string.text_alert_not_more_than_task_scheduled, scheduledTasDateString);
						MessageBox.showMessage(getActivity(), 
								R.string.label_postpone_dlg_title, 
								errorMsg);
						return;
					}
					if (dtpostPoneDate.compareTo(dtNow) < 0){
						MessageBox.showMessage(getActivity(), R.string.label_postpone_dlg_title, R.string.text_error_not_support_past);
						return;
					}
					*/
					TaskManagementDialog.this.dismiss();
					
					TaskManagementValue value = new TaskManagementValue();
					value.setCurrentTask(currentTask);
					value.setTaskManagementType(TaskManagementType.POSTPONE);
					value.addValue(POSTPONE_TO_DATE, edt_postpone_date.getText().toString());
					value.addValue(POSTPONE_TO_START_TIME, edt_postpone_start_time.getText().toString());
					value.addValue(POSTPONE_TO_END_TIME, edt_postpone_end_time.getText().toString());
					value.addValue(POSTPONE_CAUSE, edt_postpone_cause.getText().toString());
					value.addValue(POSTPONE_REASON, reason.getItemAtPosition(reason.getSelectedItemPosition()));
					if (getOnTaskManagementDialogListener() != null)
						getOnTaskManagementDialogListener().onClickTaskManagementDialogOk(value);
					
				}
				
			});
		}
		
		if (btnCancel != null)
		{
			btnCancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TaskManagementDialog.this.dismiss();
					if (getOnTaskManagementDialogListener() != null)
						getOnTaskManagementDialogListener().onClickTaskManagementDialogCancel();
				}
				
			});
		}
	}
	
	// updates the date in the TextView
	private void updateDisplay() {
		/*
	    mDateDisplay.setText(getString(R.string.strSelectedDate,
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(mMonth + 1).append("-")
	                .append(mDay).append("-")
	                .append(mYear).append(" ")));
	                */
		if (edt_postpone_date != null)
		{
			edt_postpone_date.setText(new StringBuilder()
            // Month is 0 based so add 1
			
/*			.append(mDay).append("-")
            .append(mMonth + 1).append("-")            
            .append(mYear).append(" "));
*/
			.append(mYear).append("-")
            .append(mMonth).append("-")            
            .append(mDay).append(" "));
			
		}
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	        new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					   mYear = year;
		               mMonth = monthOfYear + 1;
		               mDay = dayOfMonth;
		               
		               if (isPostPone)
		               {
		               String forceTime = currentTask.getForceTime();
		               
						Calendar calTaskDate = Calendar.getInstance();

						Date today = DataUtil.getZeroTimeDate(calTaskDate.getTime());
						
						
						if (forceTime.equalsIgnoreCase("Y"))
						{
							calTaskDate.setTime(currentTask.getTaskDate());
							int taskDateYear = calTaskDate.get(Calendar.YEAR);//currentTask.getTaskDate().getYear();
							int taskDateMonth = calTaskDate.get(Calendar.MONTH)+1;//currentTask.getTaskDate().getMonth();
							int taskDateDay = calTaskDate.get(Calendar.DAY_OF_MONTH);//currentTask.getTaskDate().getDay();

							/*
							periodInspect = 
									DataUtil.convertSlashDateToStringDDMMYYYY(currentTask.getTaskDate());
									*/
							
							if (
									(taskDateYear != mYear)||
									(taskDateMonth != mMonth)||
									(taskDateDay != mDay)
									){
								/*
								 * alert
								 */
								MessageBox.showMessage(getActivity(), 
										R.string.text_warning_title, 
										R.string.text_post_pone_force_time);
							}else{
					               updateDisplay();								
							}
						}else{
							/*
							periodInspect = 
									DataUtil.convertSlashDateToStringDDMMYYYY(to_day) + " - " + DataUtil.convertSlashDateToStringDDMMYYYY(currentTask.getTaskDate());
									*/
							calTaskDate.set(Calendar.YEAR, mYear);
							calTaskDate.set(Calendar.MONTH, mMonth - 1);
							calTaskDate.set(Calendar.DAY_OF_MONTH, mDay);
							Date dtSelected = DataUtil.getZeroTimeDate(calTaskDate.getTime());
							
							if (dtSelected.compareTo(today) >= 0)
							{
								if (dtSelected.compareTo(DataUtil.getZeroTimeDate(currentTask.getTaskScheduleDate())) > 0)
								{
									MessageBox.showMessage(getActivity(), 
											R.string.text_warning_title, 
											R.string.text_post_pone_not_force_time);									
								}else{
						            updateDisplay();								
								}
							}else{
								MessageBox.showMessage(getActivity(), 
										R.string.text_warning_title, 
										R.string.text_post_pone_not_force_time);									
							}
						}
		             }else{
			               updateDisplay();								
		             }
				}
	        };

	public OnTaskManagementDialogListener getOnTaskManagementDialogListener() {
		return onTaskManagementDialogListener;
	}
	public void setOnTaskManagementDialogListener(
			OnTaskManagementDialogListener onTaskManagementDialogListener) {
		this.onTaskManagementDialogListener = onTaskManagementDialogListener;
	}
	private void showTimePickerDialog(TimePickerDialog dlgTime,final EditText editTextTime)
	{
		if (dlgTime == null)
			dlgTime = new TimePickerDialog(TaskManagementDialog.this.getActivity(), 
					new TimePickerDialog.OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							// TODO Auto-generated method stub
							String strMin = "";
							if (minute < 10)
							{
								strMin = "0"+minute;
							}else{
								strMin = ""+minute;
							}
							editTextTime.setText(hourOfDay+":"+strMin);
						}
					}, 
					mHour, 
					mMinute,
					DateFormat.is24HourFormat(getActivity()));

			
			if (!dlgTime.isShowing())
				dlgTime.show();
	}
	private OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView v, int actionId,
				KeyEvent event) {
			// TODO Auto-generated method stub
			InputMethodManager inputManager = (InputMethodManager)
					TaskManagementDialog.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                      InputMethodManager.RESULT_UNCHANGED_SHOWN);
			return true;
		}
    	
    };
    
    private void setupTaskStatusDisplay(View vContent,Task currentTask)
    {
    	TextView tvTime = (TextView)vContent.findViewById(R.id.tv_device_access_time);
    	if (tvTime != null){
//    		String textTime = currentTask.getTaskStartTime()+" - "+currentTask.getTaskEndTime();
			String textTime = "";
			if (currentTask.getTaskEndTime().isEmpty()){
				textTime = currentTask.getTaskStartTime();				
			}else{
				textTime = currentTask.getTaskStartTime()+" - "+currentTask.getTaskEndTime();
			}

    		tvTime.setText(textTime);
    	}
    	final String remark = currentTask.getRemark();
		if ((remark != null)&&(!remark.isEmpty()))
		{
			//have remark
			TextView tv_action_about = (TextView)vContent.findViewById(R.id.tv_action_about);
			tv_action_about.setText(getActivity().getString(R.string.inspect_have_remark));
			
			View vBtnClickRemark = vContent.findViewById(R.id.btn_action_show_remark);
			vBtnClickRemark.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(getActivity())
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
    	if (this.currentLocation != null)
    	{
    		TextView tvDistance = (TextView)vContent.findViewById(R.id.tv_distance_location_place);
			
			try{
				ArrayList<CustomerSurveySite> customerSurveySiteList = 
					currentTask.getJobRequest().getCustomerSurveySiteList();
				
				if(customerSurveySiteList != null)
				{
					CustomerSurveySite firstSite = customerSurveySiteList.get(0);

					Location siteLocation = new Location(firstSite.getCustomerSurveySiteID()+"");
					siteLocation.setLatitude(firstSite.getSiteLocLat());
					siteLocation.setLongitude(firstSite.getSiteLocLon());
					float distance = GeoLocUtil.distFrom(currentLocation, siteLocation);
					distance = distance / 1000;
					Activity activity = this.getActivity();
					if (activity != null){
						String textDistance = getString(R.string.distance_from_current_location,
							String.format( "%.2f", distance));
					
						tvDistance.setText(textDistance);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
    	}
    }
}

package com.epro.psmobile.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.MembersInTeamHistory;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TeamCheckInHistory;
import com.epro.psmobile.fragment.ReportFragment;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.HTMLReportPreviewUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ReportFilterDialog extends DialogFragment implements OnClickListener, OnFocusChangeListener, OnTouchListener {

	public enum ReportType
	{
		GENERAL(0),
		EXPENSE(1);
		
		int typeCode;
		ReportType(int reportType)
		{
			this.typeCode = reportType;
		}
		
		public int getReportType(){
			return this.typeCode;
		}
		
		public static ReportType findReportType(int reportType)
		{
			for(ReportType r : values()){
				if (r.getReportType() == reportType)
				{
					return r;
				}
			}
			return ReportType.GENERAL;
		}
	};
	public class ReportGenerateResult
	{
		public String html;
		public String error;
		public boolean bSuccessed;
	}

	public class ReportGenerateAsyncTask extends AsyncTask<Void,Void,ReportGenerateResult>
	{
		private ProgressDialog proDlg;
		private Activity activity;
		private String titleReport;
		private String inspectTypeName;
		private String teamName;
		private Date startDate;
		private Date endDate;
		private ArrayList<TeamCheckInHistory> teamCheckInHistoryList;
		private ArrayList<Expense> expenseList;
		private ReportType reportType;
		private Hashtable<String,ArrayList<TeamCheckInHistory>> mTableTeam;
		public ArrayList<TeamCheckInHistory> getTeamCheckInHistoryList() {
			return teamCheckInHistoryList;
		}
		public void setTeamCheckInHistoryList(
				ArrayList<TeamCheckInHistory> teamCheckInHistoryList ,
				Hashtable<String,ArrayList<TeamCheckInHistory>> mTableTeam) {
			this.teamCheckInHistoryList = teamCheckInHistoryList;
			this.mTableTeam = mTableTeam;
		}
		public ReportGenerateAsyncTask(Activity activity){
			proDlg = new ProgressDialog(activity);
			this.activity = activity;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			proDlg.setMessage("Waiting..");
			proDlg.setCancelable(false);
			proDlg.show();
		}
		@Override
		protected ReportGenerateResult doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String strHtml = "";
			ReportGenerateResult result = new ReportGenerateResult();
			result.bSuccessed = false;
			result.error = "No data";
			try {
				if (reportType == ReportType.GENERAL)
				{
					if (teamCheckInHistoryList != null){
						strHtml = HTMLReportPreviewUtil.generateReport(activity, 
							titleReport, 
							DataUtil.convertDateToStringDDMMYYYY(startDate),
							DataUtil.convertDateToStringDDMMYYYY(endDate), 
							teamName, 
							inspectTypeName, 
							teamCheckInHistoryList,mTableTeam);
						result.bSuccessed = true;
						result.html = strHtml;
					}
				}else if (reportType == ReportType.EXPENSE)
				{
					if (expenseList != null)
					{
						strHtml = HTMLReportPreviewUtil.generateReportExpense(activity, 
								titleReport, 
								DataUtil.convertDateToStringDDMMYYYY(startDate),
								DataUtil.convertDateToStringDDMMYYYY(endDate), 
								teamName, 
								inspectTypeName, 
								expenseList);
							result.bSuccessed = true;
							result.html = strHtml;	
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.bSuccessed = false;
				result.error = e.getMessage();
			}
			return result;
		}
		@Override
		protected void onPostExecute(ReportGenerateResult result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/*
			 */
			if (proDlg != null)
			{
				proDlg.dismiss();
				
			}
			if (result.bSuccessed)
			{
				ReportFilterDialog.this.dismiss();
				/*
				 * display html;
				 */
				Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
				
				if (f instanceof ReportFragment)
				{
					((ReportFragment)f).setDataOnWebView(reportType,result.html);
				}
			}else{
				MessageBox.showMessage(getActivity(), "Error", result.error);
			}
		}
		public String getTitleReport() {
			return titleReport;
		}
		public void setTitleReport(String titleReport) {
			this.titleReport = titleReport;
		}
		public String getInspectTypeName() {
			return inspectTypeName;
		}
		public void setInspectTypeName(String inspectTypeName) {
			this.inspectTypeName = inspectTypeName;
		}
		public String getTeamName() {
			return teamName;
		}
		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public ArrayList<Expense> getExpenseList() {
			return expenseList;
		}
		public void setExpenseList(ArrayList<Expense> expenseList) {
			this.expenseList = expenseList;
		}
		public ReportType getReportType() {
			return reportType;
		}
		public void setReportType(ReportType reportType) {
			this.reportType = reportType;
		}
		
	}
	public final static String KEY_REPORT_TYPE = "com.epro.psmobile.dialog.ReportFilterDialog.KEY_REPORT_TYPE";
	public final static String KEY_TITLE = "com.epro.psmobile.dialog.ReportFilterDialog.KEY_TITLE";
	private View currentView;
	private int mDay;
	private int mMonth;
	private int mYear;
	private DatePickerDialog mDateDlg;
	
	private EditText edtStartDate;
	private EditText edtEndDate;
	private com.epro.psmobile.view.InspectTypeSpinner inspectTypeSpinner;
	private int edt_date_picker_id ;
	private String title;
	
	private ReportType reportType;
	public ReportFilterDialog() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		currentView = inflater.inflate(R.layout.report_filter_dialog, container, false);
		try {
			initial(currentView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageBox.showMessage(getActivity(), "Error", e.getMessage());
		}
		return currentView;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Dialog dlg =  super.onCreateDialog(savedInstanceState);
		title = this.getArguments().getString(KEY_TITLE, "");
		dlg.setTitle(title);
		return dlg;
	}
	private void initial(View view) throws Exception
	{
		PSBODataAdapter dataAdapter = 
				PSBODataAdapter.getDataAdapter(getActivity());
		Team currentTeam = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(getActivity()));
		
		int iReportType = getArguments().getInt(KEY_REPORT_TYPE,0);
		
		reportType = ReportType.findReportType(iReportType);
		
		EditText edtTeam = (EditText)view.findViewById(R.id.et_report_filter_dlg_team_name);
		edtTeam.setText(currentTeam.getTeamName());
		
		
		edtStartDate = (EditText)view.findViewById(R.id.et_report_filter_dlg_start_date);
		edtEndDate = (EditText)view.findViewById(R.id.et_report_filter_dlg_end_date);
		edtStartDate.setInputType(InputType.TYPE_NULL);
		edtEndDate.setInputType(InputType.TYPE_NULL);
		edtStartDate.setOnEditorActionListener(mOnEditorActionListener);
		edtEndDate.setOnEditorActionListener(mOnEditorActionListener);
		//edtStartDate.setOnClickListener(this);
		//edtEndDate.setOnClickListener(this);
		edtStartDate.setOnTouchListener(this);
		edtEndDate.setOnTouchListener(this);
		//edtStartDate.setOnFocusChangeListener(this);
		//edtEndDate.setOnFocusChangeListener(this);
		
		
		Calendar c = Calendar.getInstance();
		this.mDay = c.get(Calendar.DAY_OF_MONTH);
		this.mMonth = c.get(Calendar.MONTH)+1;
		this.mYear =  c.get(Calendar.YEAR);
		
		String defaultDate = c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
		edtStartDate.setText(defaultDate);
		edtEndDate.setText(defaultDate);
		
		inspectTypeSpinner
		  = (com.epro.psmobile.view.InspectTypeSpinner)view.findViewById(R.id.spn_report_filter_dlg_inspect_type);
		inspectTypeSpinner.initial();
		
		Button btnOk = (Button)view.findViewById(R.id.btn_report_filter_dlg_ok);
		Button btnCancel = (Button)view.findViewById(R.id.btn_report_filter_dlg_cancel);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		edt_date_picker_id = v.getId();
		if (edt_date_picker_id == R.id.btn_report_filter_dlg_ok)
		{
			
			PSBODataAdapter dataAdapter = 
					PSBODataAdapter.getDataAdapter(getActivity());
			
			InspectType inspectType = null;
			try {
				int inspectTypeID = 0;
				ArrayList<TeamCheckInHistory> teamCheckInHistoryList = null;
				Hashtable<String,ArrayList<TeamCheckInHistory>> mTableTeam = null;
				ArrayList<Expense> expenseList = null;
				if (inspectTypeSpinner != null)
				{
					if (inspectTypeSpinner.getSelectedItem() != null){
						inspectType = (InspectType)inspectTypeSpinner.getSelectedItem();
						inspectTypeID = inspectType.getInspectTypeID();
					}
				}
				Team team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(getActivity()));

				if (reportType == ReportType.GENERAL)
				{
					
					teamCheckInHistoryList = 
						dataAdapter.getTeamCheckInHistoryList(team.getTeamID(),
								DataUtil.convertDateToStringYYYYMMDD(
										DataUtil.convertToDateDDMMYYYY(edtStartDate.getText().toString())), 
								DataUtil.convertDateToStringYYYYMMDD(
										DataUtil.convertToDateDDMMYYYY(edtEndDate.getText().toString())),
								inspectTypeID);
					
					mTableTeam = 
								dataAdapter.getTableTeamCheckInHistoryList(team.getTeamID(),
							DataUtil.convertDateToStringYYYYMMDD(
									DataUtil.convertToDateDDMMYYYY(edtStartDate.getText().toString())), 
							DataUtil.convertDateToStringYYYYMMDD(
									DataUtil.convertToDateDDMMYYYY(edtEndDate.getText().toString())),
							inspectTypeID);
						//dataAdapter.getAllTeamCheckInHistory();
					if (teamCheckInHistoryList != null)
					{
						for(TeamCheckInHistory teamCheckInHistory : teamCheckInHistoryList)
						{
							ArrayList<MembersInTeamHistory> members = 
									dataAdapter.getMembersInTeamHistoryListByHistoryID(
											teamCheckInHistory.getTeamCheckInHistoryID())
											;
							if (members != null)
							{
								for(MembersInTeamHistory member : members)
								{
									teamCheckInHistory.addMember(member);
								}
							}
						}
					/*
					 * 
					 */
					}
				}else if (reportType == ReportType.EXPENSE)
				{
					expenseList = dataAdapter.getAllExpense(DataUtil.convertDateToStringDDMMYYYY(
							DataUtil.convertToDateDDMMYYYY(edtStartDate.getText().toString())), 
					DataUtil.convertDateToStringDDMMYYYY(
							DataUtil.convertToDateDDMMYYYY(edtEndDate.getText().toString())));
					
				}
				ReportGenerateAsyncTask reportGenerate = new
							ReportGenerateAsyncTask(getActivity());
//					reportGenerate.setTeamCheckInHistoryList(teamCheckInHistoryList);
					if (inspectType != null){
						reportGenerate.setInspectTypeName(inspectType.getInspectTypeName());
					}
					if (reportType == ReportType.EXPENSE)
					{
						reportGenerate.setExpenseList(expenseList);
					}else if (reportType == ReportType.GENERAL)
					{
						reportGenerate.setTeamCheckInHistoryList(teamCheckInHistoryList,mTableTeam);
					}
					reportGenerate.setTeamName(team.getTeamName());
					reportGenerate.setTitleReport(title);
					reportGenerate.setStartDate(DataUtil.convertToDateDDMMYYYY(edtStartDate.getText().toString()));
					reportGenerate.setEndDate(DataUtil.convertToDateDDMMYYYY(edtEndDate.getText().toString()));
					reportGenerate.setReportType(reportType);
					reportGenerate.execute(new Void[]{null});
				
			} 
				catch (Exception e) {
				// TODO Auto-generated catch block
				MessageBox.showMessage(getActivity(), "Error", e.getMessage());
			}
		}else if (edt_date_picker_id == R.id.btn_report_filter_dlg_cancel)
		{
			this.dismiss();
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
		               
		               String dateText = dayOfMonth + "-" + (monthOfYear) + "-" + year;
		               if (edt_date_picker_id == R.id.et_report_filter_dlg_start_date)
		               {
		            	   if (edtStartDate != null)
		            		   edtStartDate.setText(dateText);
		               }else if (edt_date_picker_id == R.id.et_report_filter_dlg_end_date)
		               {
		            	   if (edtEndDate != null)
		            		   edtEndDate.setText(dateText);
		               }
		               
				}
	};
	
	private OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView v, int actionId,
				KeyEvent event) {
			// TODO Auto-generated method stub
			InputMethodManager inputManager = (InputMethodManager)
					getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                      InputMethodManager.RESULT_UNCHANGED_SHOWN);
			return true;
		}
    	
    };
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		edt_date_picker_id = v.getId();
		if (edt_date_picker_id == R.id.et_report_filter_dlg_start_date)
		{
			if (hasFocus){
			mDateDlg = new DatePickerDialog(getActivity(), 
					mDateSetListener, 
					mYear, 
					mMonth - 1,
	                mDay);
			
			if (!mDateDlg.isShowing()){
				mDateDlg.show();
			}
			}
		}else if (edt_date_picker_id == R.id.et_report_filter_dlg_end_date)
		{
			if (hasFocus){
			mDateDlg = new DatePickerDialog(getActivity(), 
					mDateSetListener, 
					mYear, 
					mMonth - 1,
	                mDay);
			
			
			if (!mDateDlg.isShowing()){
				mDateDlg.show();
			}
			}
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			edt_date_picker_id = v.getId();
			if (edt_date_picker_id == R.id.et_report_filter_dlg_start_date)
			{
				mDateDlg = new DatePickerDialog(getActivity(), 
						mDateSetListener, 
						mYear, 
						mMonth - 1,
		                mDay);
				
				if (!mDateDlg.isShowing()){
					mDateDlg.show();
				}
			}else if (edt_date_picker_id == R.id.et_report_filter_dlg_end_date)
			{
				mDateDlg = new DatePickerDialog(getActivity(), 
						mDateSetListener, 
						mYear, 
						mMonth - 1,
		                mDay);
				
				
				if (!mDateDlg.isShowing()){
					mDateDlg.show();
				}
			}
		}
		return false;
	}
	public ReportType getReportType() {
		return reportType;
	}
	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}
}

package com.epro.psmobile.dialog;

import java.util.Date;

import com.epro.psmobile.R;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.view.CustomerSurveySiteSpinner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TaskDuplicateDialog extends InfoDialog {

	public interface OnClickTaskDuplicateDialog
	{
		void onClickOk(String locationStart,int startSiteID,
				String locationTo,int endSiteID,
				double distance);
		void onClickCancel();
	}
	
	private View currentView;
	private OnClickTaskDuplicateDialog onClickTaskDuplicateDialog;
	private CustomerSurveySiteSpinner spv_site_start;
	CustomerSurveySiteSpinner spv_site_to;
	public static TaskDuplicateDialog newInstance(Task task,Date currentDate){
		TaskDuplicateDialog frag = new TaskDuplicateDialog();
        Bundle args = new Bundle();
        args.putLong(InstanceStateKey.KEY_ARGUMENT_DLG_DATE, currentDate.getTime());
        args.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK,task);
        frag.setArguments(args);
        return frag;
	}
	
	public TaskDuplicateDialog() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/*
		currentView = inflater.inflate(R.layout.task_duplicate_dialog, container, false);
		initialView(currentView);
		return currentView;*/
		initialView(currentView);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		return super.onCreateDialog(savedInstanceState);
		Builder bld = new AlertDialog.Builder(this.getActivity());
		bld.setTitle(R.string.label_task_duplicate_dlg_title);
		//bld.setIcon(R.drawable.ic_psmobile);
		LayoutInflater inflater = 
				(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		currentView = inflater.inflate(R.layout.task_duplicate_dialog, null);
		bld.setView(currentView);
		Dialog dlg = bld.create();
		return dlg;
	}

	private void initialView(final View rootView)
	{
		Bundle bArgument =  getArguments();
		
		this.initialGreenLineTime(rootView, bArgument);
		
		
		Button btnOk = (Button)rootView.findViewById(R.id.btn_task_duplicate_dlg_ok);
		Button btnCancel = (Button)rootView.findViewById(R.id.btn_task_duplicate_dlg_cancel);
		
		spv_site_start = 
				 (CustomerSurveySiteSpinner)rootView.findViewById(R.id.spn_customer_survey_site_start);
		
		spv_site_to = 
				 (CustomerSurveySiteSpinner)rootView.findViewById(R.id.spn_customer_survey_site_to);
		
		Task task = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
		if (task != null)
		{
			try{
				spv_site_start.initial(task);
				spv_site_to.initial(task);
			}catch(Exception ex){
				MessageBox.showMessage(getActivity(), "Error", ex.getMessage());
			}
		}
		
		btnOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				if (getOnClickTaskDuplicateDialog() != null)
				{
					double distance = 0;
					//EditText edtStart = (EditText)rootView.findViewById(R.id.edt_task_duplication_location_start);
					//EditText edtTo = (EditText)rootView.findViewById(R.id.edt_task_duplication_location_to);
					
					EditText edtDistance = (EditText)rootView.findViewById(R.id.edt_task_duplication_distance);

					String start = "";//edtStart.getText().toString();
					String to = "";//edtTo.getText().toString();
					int startSiteId = 0;
					int endSiteId = 0;
					if (spv_site_start.getSelectedItem() != null){
						CustomerSurveySite startSite = (CustomerSurveySite)spv_site_start.getSelectedItem();
						start = startSite.getSiteAddress();
						startSiteId = startSite.getCustomerSurveySiteID();
					}
					if (spv_site_to.getSelectedItem() != null){
						CustomerSurveySite toSite = (CustomerSurveySite)spv_site_to.getSelectedItem();
						to = toSite.getSiteAddress();
						endSiteId = toSite.getCustomerSurveySiteID();
					}
					try{
						distance = Double.parseDouble(edtDistance.getText().toString());
					}catch(Exception e){
						e.printStackTrace();
					}
					if (!start.isEmpty() && !to.isEmpty())
					{
						if (startSiteId == endSiteId)
						{
							MessageBox.showMessage(getActivity(), R.string.text_error_title, R.string.text_error_duplicate_same_location);
							return;
						}
						TaskDuplicateDialog.this.dismiss();						
						getOnClickTaskDuplicateDialog().onClickOk(start,startSiteId, to,endSiteId, distance);
					}
				}
			}
			
		});
		
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TaskDuplicateDialog.this.dismiss();
				
			}
			
		});
	}

	public OnClickTaskDuplicateDialog getOnClickTaskDuplicateDialog() {
		return onClickTaskDuplicateDialog;
	}

	public void setOnClickTaskDuplicateDialog(OnClickTaskDuplicateDialog onClickTaskDuplicateDialog) {
		this.onClickTaskDuplicateDialog = onClickTaskDuplicateDialog;
	}

}

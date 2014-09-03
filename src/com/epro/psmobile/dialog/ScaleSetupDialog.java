package com.epro.psmobile.dialog;

import com.epro.psmobile.R;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ScaleSetupDialog extends DialogFragment {

	
	private View currentView;
	private static OnScaleSetupDialogInterface scaleSetupDialogInterface;
	public interface OnScaleSetupDialogInterface
	{
		 void onScaleSetupComplete(Task currentTask,
				 CustomerSurveySite site,
				 double siteWidth,
				 double siteLong);
		 
	};
	public static ScaleSetupDialog newInstance(Task currentTask,
			CustomerSurveySite site,OnScaleSetupDialogInterface onScaleSetupDialogInterface)
	{
		ScaleSetupDialog scaleDlg = new ScaleSetupDialog();
		Bundle bArgument = new Bundle();
		bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
		bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, site);
		scaleDlg.setArguments(bArgument);
		
		scaleSetupDialogInterface = onScaleSetupDialogInterface;
		return scaleDlg;
	}
	private ScaleSetupDialog() {
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
		currentView = inflater.inflate(R.layout.scale_setup_dialog, container, false);
		initial(currentView);
		return currentView;
	}
	private void initial(View view)
	{
		
		if (this.getArguments() != null)
		{
			final EditText editTextWidth = (EditText)view.findViewById(R.id.edt_scale_customer_site_width);
			final EditText editTextLong = (EditText)view.findViewById(R.id.edt_scale_customer_site_long);
			
			editTextWidth.setText("1000");
			editTextLong.setText("1000");
			
			Button btnSave = (Button)view.findViewById(R.id.btn_scale_setup_dlg_save);
			
			final Task currentTask = 
					this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			final CustomerSurveySite site = 
					this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
			
			
			btnSave.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (scaleSetupDialogInterface != null)
					{
						double siteWidth = 0;
						double siteLong = 0;
						try{
							siteWidth = Double.parseDouble(editTextWidth.getText().toString());
						}catch(Exception ex){}
						try{
							siteLong = Double.parseDouble(editTextLong.getText().toString());
						}catch(Exception ex){}
						
						scaleSetupDialogInterface.onScaleSetupComplete(currentTask, site, siteWidth, siteLong);
					}
				}
				
			});
		}
	}
	
}

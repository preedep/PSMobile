package com.epro.psmobile.dialog;

import java.util.Date;

import com.epro.psmobile.R;
import com.epro.psmobile.R.id;
import com.epro.psmobile.R.layout;
import com.epro.psmobile.adapter.ShowDateAndTimeInfoHelper;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.LicensePlate;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InfoDialog extends DialogFragment {

	private com.epro.psmobile.view.LicensePlateSpinner licensePlateSpinner;
	private String carMileEndValue;
	private ViewGroup vGroupContainer;
	public com.epro.psmobile.view.LicensePlateSpinner getLicensePlate(){
		return licensePlateSpinner;
	}
	public interface OnAppInfoDialogListener
	{
		void onClickOk();
		void onClickCancel();
	}
	private View currentView;
	private OnAppInfoDialogListener onAppInfoDlgListener;
	
	public static InfoDialog newInstance(int resTitle,
			Date currentDate,
			String message,
			int resOk,
			int resCancel)
	{
		return newInstance(resTitle,currentDate,message,resOk,resCancel,false);
	}
	public static InfoDialog newInstance(int resTitle,
			Date currentDate,
			String message,
			int resOk,
			int resCancel,
			boolean isCheckOut) 
	{
		InfoDialog frag = new InfoDialog();
        Bundle args = new Bundle();
        args.putInt(InstanceStateKey.KEY_ARGUMENT_DLG_TITLE_NAME, resTitle);
        args.putLong(InstanceStateKey.KEY_ARGUMENT_DLG_DATE, currentDate.getTime());
        args.putString(InstanceStateKey.KEY_ARGUMENT_DLG_MESSAGE, message);
        args.putInt(InstanceStateKey.KEY_ARGUMENT_DLG_BTN_OK, resOk);
        args.putInt(InstanceStateKey.KEY_ARGUMENT_DLG_BTN_CANCEL, resCancel);  
        args.putBoolean(InstanceStateKey.KEY_ARGUMENT_DLG_SHOW_CHECKOUT_DATA, isCheckOut);
        frag.setArguments(args);
        return frag;
    }
	
	public InfoDialog(
			) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//this.setStyle(STYLE_NO_TITLE, 0);
				
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateDialog(savedInstanceState);
		Builder bld = new AlertDialog.Builder(this.getActivity());
//		bld.setTitle(titleId)
//		bld.setIcon(R.drawable.ic_psmobile);
		LayoutInflater inflater = 
				(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		currentView = inflater.inflate(R.layout.simple_dialog_message_ok_cancel, null);
		bld.setView(currentView);
		Dialog dlg = bld.create();
		return dlg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//bld.setView(currentView);
		initial(currentView,this.getDialog());
		return super.onCreateView(inflater, container, savedInstanceState);
		//this.getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	protected void initialGreenLineTime(View v,Bundle bArgument)
	{
		long l_currentTime = bArgument.getLong(InstanceStateKey.KEY_ARGUMENT_DLG_DATE);
		ShowDateAndTimeInfoHelper.fillDateAndTimeGreenLine(getActivity(), new Date(l_currentTime), v);

	}
	private void initial(View v,Dialog dlgBuilder)
	{
		try{
		Bundle bArgument =  getArguments();
		int resTitle = bArgument.getInt(InstanceStateKey.KEY_ARGUMENT_DLG_TITLE_NAME);
		String message = bArgument.getString(InstanceStateKey.KEY_ARGUMENT_DLG_MESSAGE);
		int resBtnOk = bArgument.getInt(InstanceStateKey.KEY_ARGUMENT_DLG_BTN_OK);
		int resBtnCancel = bArgument.getInt(InstanceStateKey.KEY_ARGUMENT_DLG_BTN_CANCEL);
		final boolean isCheckOut = bArgument.getBoolean(InstanceStateKey.KEY_ARGUMENT_DLG_SHOW_CHECKOUT_DATA, false);
		
		String textTitle = this.getActivity().getString(resTitle);
		String textBtnOk = this.getActivity().getString(resBtnOk);
		String textBtnCancel = this.getActivity().getString(resBtnCancel);
		
		
		initialGreenLineTime(v,bArgument);

		//TextView tvTitleName = (TextView)v.findViewById(R.id.tv_simple_msg_dialog_title_name);
		TextView tvMessage = (TextView)v.findViewById(R.id.tv_simple_dialog_message);
		
		Button btnOk = (Button)v.findViewById(R.id.btn_simple_dlg_ok);
		Button btnCancel = (Button)v.findViewById(R.id.btn_simple_dlg_cancel);

		if (isCheckOut)
		{
			tvMessage.getLayoutParams().height = 50;
			vGroupContainer = 
					(ViewGroup)v.findViewById(R.id.ll_data_container);
			vGroupContainer.setVisibility(View.VISIBLE);
			
			
			licensePlateSpinner = 
					(com.epro.psmobile.view.LicensePlateSpinner)vGroupContainer.findViewById(R.id.sp_simple_dlg_license_late);
			
			licensePlateSpinner.initial();
			
			
			PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
			try {
				Team team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(this.getActivity()));
				if (team != null)
				{
					for(int i = 0; i < this.licensePlateSpinner.getAdapter().getCount();i++)
					{
						LicensePlate licensePlate  = (LicensePlate)this.licensePlateSpinner.getAdapter().getItem(i);
						if (licensePlate.getLicensePlateID() == team.getLicensePlateId()){
							this.licensePlateSpinner.setSelection(i);
							break;
						}
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
			
		//tvTitleName.setText(textTitle);
		dlgBuilder.setTitle(textTitle);
		
		tvMessage.setText(message);
		
		btnOk.setText(textBtnOk);
		btnCancel.setText(textBtnCancel);
		
		
		
		btnOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (onAppInfoDlgListener != null){
					InfoDialog.this.dismiss();
					
					if (isCheckOut)
					{
						if (vGroupContainer != null)
						{
							EditText edt = (EditText)vGroupContainer.findViewById(R.id.simple_dlg_et_team_car_mile);
							carMileEndValue = edt.getText().toString();
						}
					}
					onAppInfoDlgListener.onClickOk();
				}
			}
			
		});
		
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (onAppInfoDlgListener != null)
				{
					InfoDialog.this.dismiss();
					onAppInfoDlgListener.onClickCancel();
				}
			}
			
		});
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public OnAppInfoDialogListener getOnAppInfoDlgListener() {
		return onAppInfoDlgListener;
	}

	public void setOnAppInfoDlgListener(OnAppInfoDialogListener onAppInfoDlgListener) {
		this.onAppInfoDlgListener = onAppInfoDlgListener;
	}
	public String getCarMileEndValue() {
		return carMileEndValue;
	}
	public void setCarMileEndValue(String carMileEndValue) {
		this.carMileEndValue = carMileEndValue;
	}
}

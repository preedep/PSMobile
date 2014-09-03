package com.epro.psmobile.dialog;

import com.epro.psmobile.R;
import com.epro.psmobile.dialog.ReportFilterDialog.ReportType;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ReportTypeSelectDialog extends DialogFragment {

	public ReportTypeSelectDialog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.report_title);
        builder.setItems(R.array.report_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
               // mDoneButton.setText(items[item]);
           	 
            	if ((item == 0)||(item == 1))
            	{
            		String[] arrays = 
            				getActivity().getResources().getStringArray(R.array.report_list);
            		ReportFilterDialog reportFilterDlg = new ReportFilterDialog();
            		Bundle bArgument = new Bundle();
            		bArgument.putString(ReportFilterDialog.KEY_TITLE, arrays[item]);
            		if (item == 0)
            		{
            			bArgument.putInt(ReportFilterDialog.KEY_REPORT_TYPE, ReportType.GENERAL.getReportType());
            		}else if (item == 1)
            		{
            			bArgument.putInt(ReportFilterDialog.KEY_REPORT_TYPE, ReportType.EXPENSE.getReportType());
            		}
            		reportFilterDlg.setArguments(bArgument);
            		reportFilterDlg.show(
            				getActivity().getSupportFragmentManager(),
            				ReportFilterDialog.class.getName());
            		
            	}
            }
        });
//        AlertDialog alert = builder.create();
//        alert.show();
//		return super.onCreateDialog(savedInstanceState);
        return builder.create();
	}

}

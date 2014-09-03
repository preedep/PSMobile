/**
 * 
 */
package com.epro.psmobile.dialog;

import com.epro.psmobile.R;
import com.epro.psmobile.R.id;
import com.epro.psmobile.R.layout;
import com.epro.psmobile.data.Employee;
import com.epro.psmobile.view.EmployeeSpinner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * @author nickmsft
 *
 */
public class EmployeeSpinnerDialog extends Dialog {

	private String[] employeesIgnored;
	
	public interface EmployeeDialogListener {
        public void ready(Employee employee);
        public void cancelled();
    }
	
	
	private EmployeeDialogListener employeeDialogListener;
	
	/**
	 * @param context
	 */
	public EmployeeSpinnerDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param theme
	 */
	public EmployeeSpinnerDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	public EmployeeSpinnerDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	
	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.setContentView(R.layout.employee_spinner_dialog);
		
		
		final EmployeeSpinner e_sp = (EmployeeSpinner)this.findViewById(R.id.sp_employee_dialog);
		e_sp.initial(employeesIgnored);
		
		  Button buttonOK = (Button) findViewById(R.id.dialogOK);
	        Button buttonCancel = (Button) findViewById(R.id.dialogCancel);
	        buttonOK.setOnClickListener(new android.view.View.OnClickListener(){
	            public void onClick(View v) {
	                int n = e_sp.getSelectedItemPosition();
	                if (e_sp.getItemAtPosition(n) instanceof Employee){
	                	employeeDialogListener.ready((Employee)e_sp.getItemAtPosition(n));
	                }
	                EmployeeSpinnerDialog.this.dismiss();
	            }
	        });
	        buttonCancel.setOnClickListener(new android.view.View.OnClickListener(){
	            public void onClick(View v) {
	            	employeeDialogListener.cancelled();
	            	EmployeeSpinnerDialog.this.dismiss();
	            }
	        });
	}

	public void setOnEmployeeDialogListener(EmployeeDialogListener employeeDialogListener )
	{
		this.employeeDialogListener = employeeDialogListener;
	}

	public String[] getEmployeesIgnored() {
		return employeesIgnored;
	}

	public void setEmployeesIgnored(String[] employeesIgnored) {
		this.employeesIgnored = employeesIgnored;
	}
}

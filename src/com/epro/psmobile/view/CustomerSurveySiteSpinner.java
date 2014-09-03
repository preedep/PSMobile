package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.SubExpenseType;
import com.epro.psmobile.data.Task;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CustomerSurveySiteSpinner extends Spinner {

	public CustomerSurveySiteSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomerSurveySiteSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public CustomerSurveySiteSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomerSurveySiteSpinner(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomerSurveySiteSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	public void initial(Task currentTask) throws Exception
	{
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
		ArrayList<CustomerSurveySite> surveySites = dataAdapter.findCustomerSurveySite(currentTask.getTaskID());
		if (surveySites != null)
		{
			ArrayAdapter<CustomerSurveySite> adapter = new ArrayAdapter<CustomerSurveySite>(this.getContext(),
					android.R.layout.simple_spinner_item,
					surveySites);
			this.setAdapter(adapter);
		}
	}
}

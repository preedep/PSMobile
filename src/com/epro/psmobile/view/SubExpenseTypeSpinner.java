package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.SubExpenseType;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SubExpenseTypeSpinner extends Spinner {

	
	public SubExpenseTypeSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SubExpenseTypeSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public SubExpenseTypeSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SubExpenseTypeSpinner(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SubExpenseTypeSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	public void initial(int expenseTypeID) throws Exception
	{
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
		ArrayList<SubExpenseType> allSubExpenseTypes = 
				dataAdapter.getAllSubExpenseTypeByExpenseTypeID(expenseTypeID);
		if (allSubExpenseTypes != null)
		{
			ArrayAdapter<SubExpenseType> adapter = new ArrayAdapter<SubExpenseType>(this.getContext(),
					android.R.layout.simple_spinner_item,
					allSubExpenseTypes);
			this.setAdapter(adapter);
		}
	}
}

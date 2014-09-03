package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.ExpenseType;
import com.epro.psmobile.data.ReasonSentence.ReasonType;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class ExpenseTypeSpinner extends BasicSpinner {

	private ArrayList<ExpenseType> expenseTypeList = null;//new ArrayList<ExpenseType>();
	
	
	public ExpenseTypeSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ExpenseTypeSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public ExpenseTypeSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ExpenseTypeSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ExpenseTypeSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initial() throws Exception {
		// TODO Auto-generated method stub
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
		
		expenseTypeList =  dataAdapter.getAllExpenseType();
		
		String expenseTypes[] = new String[expenseTypeList.size()];
		
		for(int i = 0; i < expenseTypeList.size();i++)
		{
			expenseTypes[i] = expenseTypeList.get(i).getExpenseTypeName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
				android.R.layout.simple_spinner_item,expenseTypes);
		this.setAdapter(adapter);
	}

	@Override
	public <T> ArrayList<T> getData(Class<T> type) {
		// TODO Auto-generated method stub
		return (ArrayList<T>) expenseTypeList;
	}

}

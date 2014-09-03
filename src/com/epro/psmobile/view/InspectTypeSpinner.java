package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.util.MessageBox;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class InspectTypeSpinner extends Spinner {

	public InspectTypeSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InspectTypeSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public InspectTypeSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InspectTypeSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public InspectTypeSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	public void initial(){
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
		try {
			ArrayList<InspectType> inspectTypes = dataAdapter.getAllInspectType();
			ArrayAdapter<InspectType> adapter = new ArrayAdapter<InspectType>(this.getContext(),
					android.R.layout.simple_spinner_item,inspectTypes);
			this.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageBox.showMessage(getContext(), 
					"Error", 
					e.getMessage());
		}
	}
}

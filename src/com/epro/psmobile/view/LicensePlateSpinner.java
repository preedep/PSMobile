package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.LicensePlate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

public class LicensePlateSpinner extends BasicSpinner {

	private ArrayList<LicensePlate> licensePlateList;
	public LicensePlateSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param context
	 * @param mode
	 */
	public LicensePlateSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LicensePlateSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public LicensePlateSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param mode
	 */
	public LicensePlateSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void initial() throws Exception {
		// TODO Auto-generated method stub
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
		licensePlateList = 
				dataAdapter.getAllLicensePlate();
		
		if (licensePlateList != null)
		{
			ArrayAdapter<LicensePlate> adapter = new ArrayAdapter<LicensePlate>(this.getContext(),
					android.R.layout.simple_spinner_item,
					licensePlateList);
			this.setAdapter(adapter);
		}
	}

	@Override
	public <T> ArrayList<T> getData(Class<T> type) {
		// TODO Auto-generated method stub
		return (ArrayList<T>) licensePlateList;
	}

}

/**
 * 
 */
package com.epro.psmobile.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import com.epro.psmobile.R;
import com.epro.psmobile.data.ExpenseEntryRow;
import com.epro.psmobile.data.ExpenseEntryRowFuel;
import com.epro.psmobile.data.ExpenseEntryRowOther;
import com.epro.psmobile.data.ExpenseType;
import com.epro.psmobile.data.SubExpenseType;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.ScreenUtil;
import com.epro.psmobile.util.ViewUtil;
import com.epro.psmobile.view.SubExpenseTypeSpinner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * @author nickmsft
 *
 */
public class ExpenseEntryRowAdapter extends BaseAdapter {

	public enum ExpenseGroupType
	{
		FUEL,
		OTHER
	};
	
	private ArrayList<ExpenseEntryRowFuel> expenseEntryRowFuelList;
	private ArrayList<ExpenseEntryRowOther> expenseEntryRowOtherList;

	private LayoutInflater inflater;
	private Context context;
	private ExpenseGroupType expenseGroupType = ExpenseGroupType.FUEL;
	
	private final static int COL_WIDTH = 150;
	
	private  View viewRowHeader = null;
	private boolean bShownDatePickerDlg = false;
	
	
	class TextExpenseEntryWatcher implements TextWatcher
	{
		private EditText currentEditText = null;
		private ExpenseEntryRow entryRow = null;
		private int entryIdx = 0;
		
		int mYear = 0;
		int mMonth = 0;
		int mDay = 0;
		
		int mHourOfDay = 0;
		int mMinute = 0;
		
		private DatePickerDialog datePickerDlg;
		private TimePickerDialog timePickerDlg;
		
		private final OnTimeSetListener mTimeSetListener =new OnTimeSetListener()
        {

			@Override
			public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				mHourOfDay = hourOfDay;
				mMinute = minute;
				
				updateFieldTimeEntryDisplay();
			}
			
        };
		private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				mYear = year;
				mMonth = monthOfYear + 1;
				mDay = dayOfMonth;
				
				updateFieldDateEntryDisplay();
			}
		};
		
		public TextExpenseEntryWatcher(final EditText currentEditText,final ExpenseEntryRow entryRow){
			this.currentEditText = currentEditText;
			this.currentEditText.setOnFocusChangeListener(new OnFocusChangeListener(){

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if ((expenseGroupType == ExpenseGroupType.FUEL)||
						(expenseGroupType == ExpenseGroupType.OTHER))
					{
						if (entryIdx == 0){
							//datetime picker
							ViewUtil.softkeyHide(context, currentEditText);
							if (hasFocus)
							{								
								final Calendar c = Calendar.getInstance();
								mYear = c.get(Calendar.YEAR);
								mMonth = c.get(Calendar.MONTH)+1;
								mDay = c.get(Calendar.DAY_OF_MONTH);
								
								Log.d("DEBUG_D", "day-month-year"+mDay+"-"+mMonth+"-"+mYear);
								if (datePickerDlg == null)
								{
									datePickerDlg = 
											new DatePickerDialog(context,mDateSetListener,
													mYear,
													mMonth - 1,
													mDay);									
								}
								if (!datePickerDlg.isShowing()){
									datePickerDlg.show();										
								}								
							}
						}else if (entryIdx == 1)/*time picker*/
						{
							ViewUtil.softkeyHide(context, currentEditText);
							if (hasFocus)
							{	
								Calendar cal=Calendar.getInstance();
								
								if (timePickerDlg == null)
								{
									timePickerDlg=new TimePickerDialog(context,
											mTimeSetListener,
											cal.get(Calendar.HOUR_OF_DAY),
											cal.get(Calendar.MINUTE),true);
								}
								if (!timePickerDlg.isShowing())
									timePickerDlg.show();
							}
						}
					}
				}
				
			});
			this.entryRow = entryRow;
			this.entryIdx = (Integer.parseInt(currentEditText.getTag().toString()));
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (expenseGroupType == ExpenseGroupType.FUEL)
			{
				Log.d("DEBUG_D", "after text changed "+currentEditText.getText().toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (expenseGroupType == ExpenseGroupType.FUEL)
			{
				ExpenseEntryRowFuel fuelRow = (ExpenseEntryRowFuel)entryRow;
				if (entryIdx == 0){
					fuelRow.setExpenseDate(
							  DataUtil.convertToDateDDMMYYYY(currentEditText.getText().toString())
							);
				}else if (entryIdx == 1){
					fuelRow.setExpenseTime(currentEditText.getText().toString());
				}else if (entryIdx == 2){
					fuelRow.setMilesNumber(currentEditText.getText().toString());
				}else if (entryIdx == 3){
					fuelRow.setFuelTypeAndPaidType(currentEditText.getText().toString());					
				}else if (entryIdx == 4){
					double amount = 0;
					try{
						amount = Double.parseDouble(currentEditText.getText().toString());
					}catch(Exception ex){}
					fuelRow.setExpenseAmount(amount);
				}else if (entryIdx == 5){
					double liter = 0;
					try{
						liter = Double.parseDouble(currentEditText.getText().toString());
					}catch(Exception ex){}
					fuelRow.setLiter(liter);
				}else
				if (entryIdx == 6){
					fuelRow.setRemark(currentEditText.getText().toString());
				}
			}else 
			if (expenseGroupType == ExpenseGroupType.OTHER){
				ExpenseEntryRowOther otherRow = (ExpenseEntryRowOther)entryRow;
				if (entryIdx == 0){	
					otherRow.setExpenseDate(DataUtil.convertToDateDDMMYYYY(currentEditText.getText().toString()));					
				}else if (entryIdx == 1){
					otherRow.setExpenseTime(currentEditText.getText().toString());
				}else if (entryIdx == 2){
					otherRow.setExpenseOtherType(currentEditText.getText().toString());
				}else if (entryIdx == 3){
					double amount = 0;
					try{
						amount = Double.parseDouble(currentEditText.getText().toString());
					}catch(Exception ex){}
					otherRow.setExpenseAmount(amount);
				}else if (entryIdx == 4){
					otherRow.setRemark(currentEditText.getText().toString());
				}
			}			
		}
		
		public void updateFieldDateEntryDisplay(){
			StringBuilder strTextDate = new StringBuilder();
			strTextDate.append(mDay).append("-").append(mMonth).append("-").append(mYear);
			currentEditText.setText(strTextDate.toString());
		}
		
		public void updateFieldTimeEntryDisplay(){
			StringBuilder strTextTime = new StringBuilder();
			String strMin = "";
			if (mMinute < 10){
				strMin = "0"+mMinute;
			}else{
				strMin = mMinute+"";
			}
			strTextTime.append(mHourOfDay).append(":").append(strMin);
			currentEditText.setText(strTextTime);
		}
	}
	/**
	 * 
	 */
	public ExpenseEntryRowAdapter(Context context,ExpenseGroupType expenseGroupType) {
		// TODO Auto-generated constructor stub
		
		this.expenseGroupType = expenseGroupType;
		this.context = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			expenseEntryRowFuelList = new ArrayList<ExpenseEntryRowFuel>();
			ExpenseEntryRowFuel header = new ExpenseEntryRowFuel();
			header.setRowHeader(true);
			expenseEntryRowFuelList.add(header);
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{
			expenseEntryRowOtherList = new ArrayList<ExpenseEntryRowOther>();
			ExpenseEntryRowOther header = new ExpenseEntryRowOther();
			header.setRowHeader(true);
			expenseEntryRowOtherList.add(header);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			return this.expenseEntryRowFuelList.size();
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{
			return this.expenseEntryRowOtherList.size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			return this.expenseEntryRowFuelList.get(arg0);
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{
			return this.expenseEntryRowOtherList.get(arg0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
			Log.d("DEBUG_D", "getView at position = "+position);
			
			View v = convertView;
			ExpenseEntryRow entryRow = (ExpenseEntryRow)this.getItem(position);
			
			v = inflater.inflate(R.layout.expense_entry_row_item, parent, false);
			v.setTag(entryRow);
				
			if (v instanceof ViewGroup){	
					if (entryRow.isRowHeader())
					{	
						setupColumnHeader((ViewGroup)v);
						viewRowHeader = v;
					}else{
						if (entryRow.isEditable()){
							setupRowEditor((ViewGroup)v,entryRow);
						}else{
							setupRowDisplay((ViewGroup)v,entryRow);
						}
					}
				}
			
		
		return v;
	}
	public void setupRowDisplay(ViewGroup v,ExpenseEntryRow row)
	{
		int resArrayString = 0;
		ViewGroup vRowItem = (ViewGroup)v.findViewById(R.id.ll_expense_row_item);
		vRowItem.removeAllViews();
		boolean isFuelRow = false;
		
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			resArrayString = R.array.label_columns_expense_fuel;
			isFuelRow = true;
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{
			resArrayString = R.array.label_columns_expense_other;
		}		
		String titleColumns[] = context.getResources().getStringArray(resArrayString);	
		for(int i = 0; i < titleColumns.length;i++){
			TextView tvTitleColumn = new TextView(context);
			tvTitleColumn.setLayoutParams(new LinearLayout.LayoutParams(
					(int)ScreenUtil.convertDpToPixel(COL_WIDTH, this.context),
					LayoutParams.WRAP_CONTENT));
			String text = "";

			if (isFuelRow)
			{
				ExpenseEntryRowFuel fuelRow = (ExpenseEntryRowFuel)row;
				text = fuelRow.findTextAttributeIndxDisplay(i);
			}else{
				ExpenseEntryRowOther otherRow = (ExpenseEntryRowOther)row;
				text = otherRow.findTextAttributeIndxDisplay(i);				
			}
			if ((text == null)||(text.equalsIgnoreCase("null"))){
				text = "";
			}
			tvTitleColumn.setText(text);				
			vRowItem.addView(tvTitleColumn);
			vRowItem.requestLayout();
		}
	}
	public void setupRowEditor(ViewGroup v,ExpenseEntryRow entryRow)
	{
		int resArrayString = 0;		
		ViewGroup vRowItem = (ViewGroup)v.findViewById(R.id.ll_expense_row_item);
		vRowItem.removeAllViews();

		boolean isFuelRow = false;
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			resArrayString = R.array.label_columns_expense_fuel;
			isFuelRow = true;
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{
			resArrayString = R.array.label_columns_expense_other;
		}		
		String titleColumns[] = context.getResources().getStringArray(resArrayString);	
		for(int i = 0; i < titleColumns.length;i++)
		{
			int expenseTypeID = -1;
			ExpenseEntryRowFuel fuelRow = null;
			ExpenseEntryRowOther otherRow = null;
			
			if (isFuelRow)
			{
				fuelRow = (ExpenseEntryRowFuel)entryRow;
				if (i == 3){
					expenseTypeID = ExpenseType.EXPENSE_GROUP_FUEL;
				}
			}else{
				otherRow = (ExpenseEntryRowOther)entryRow;
				if (i == 2){
					expenseTypeID = ExpenseType.EXPENSE_GROUP_OTHER;
				}
			}
			/*
			 * use drowndownlist
			 */
			if (expenseTypeID >= 0)/*this column is dropdownlist*/
			{
				final SubExpenseTypeSpinner subExpenseTypeSpinner = new SubExpenseTypeSpinner(context);
				subExpenseTypeSpinner.setTag(expenseTypeID);
				try {
					subExpenseTypeSpinner.initial(expenseTypeID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				subExpenseTypeSpinner.setLayoutParams(new LinearLayout.LayoutParams(
						(int)ScreenUtil.convertDpToPixel(COL_WIDTH, this.context),
						LayoutParams.WRAP_CONTENT));		
				
				final ExpenseEntryRowFuel ex_fuelRow = fuelRow;
				final ExpenseEntryRowOther ex_otherRow = otherRow;
				
				subExpenseTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						// TODO Auto-generated method stub
						int expenseGroupTypeID = (Integer)subExpenseTypeSpinner.getTag();
						if (expenseGroupTypeID == ExpenseType.EXPENSE_GROUP_FUEL){
							if (ex_fuelRow != null)
							{
								Object item = parent.getItemAtPosition(pos);
								if (item != null)
								{
									SubExpenseType subExpenseType = (SubExpenseType)item;
									ex_fuelRow.setFuelTypeAndPaidType(subExpenseType.getSubExpenseTypeName());
									ex_fuelRow.setSubExpenseTypeID(subExpenseType.getSubExpenseTypeID());
								}
							}
						}else{
							if (ex_otherRow != null)
							{
								Object item = parent.getItemAtPosition(pos);
								if (item != null){
									SubExpenseType subExpenseType = (SubExpenseType)item;
									ex_otherRow.setExpenseOtherType(subExpenseType.getSubExpenseTypeName());
									ex_otherRow.setSubExpenseTypeID(subExpenseType.getSubExpenseTypeID());
								}
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				vRowItem.addView(subExpenseTypeSpinner);	
				vRowItem.requestLayout();

			}else{
				
			
			EditText etText = new EditText(context);			
			etText.setLayoutParams(new LinearLayout.LayoutParams(
					(int)ScreenUtil.convertDpToPixel(COL_WIDTH, this.context),
					LayoutParams.WRAP_CONTENT));
			
			etText.setSingleLine(true);
			etText.setTag(i);			
			
			String text = "";

			if (isFuelRow)
			{
				//ExpenseEntryRowFuel fuelRow = (ExpenseEntryRowFuel)entryRow;
				text = fuelRow.findTextAttributeIndxDisplay(i);
			}else{
				//ExpenseEntryRowOther otherRow = (ExpenseEntryRowOther)entryRow;
				text = otherRow.findTextAttributeIndxDisplay(i);				
			}
			

			if (isFuelRow)
			{
				if (i == 2)
				{
					etText.setInputType(InputType.TYPE_CLASS_NUMBER);
					
				}else if ((i == 4)||
						  (i == 5))
				{
					etText.setRawInputType(InputType.TYPE_CLASS_NUMBER | 
							InputType.TYPE_NUMBER_FLAG_SIGNED | 
							InputType.TYPE_NUMBER_FLAG_DECIMAL);					
					
					try{
						double d = Double.parseDouble(text);
						text = DataUtil.decimal2digiFormat(d);
					}catch(Exception ex){}
				}
				
			}else{
				if (i == 3){
					etText.setRawInputType(
							InputType.TYPE_CLASS_NUMBER | 
							InputType.TYPE_NUMBER_FLAG_SIGNED | 
							InputType.TYPE_NUMBER_FLAG_DECIMAL);					
					
					try{
						double d = Double.parseDouble(text);
						text = DataUtil.decimal2digiFormat(d);
					}catch(Exception ex){}

				}
			}
			
				if ((text == null)||(text.equalsIgnoreCase("null"))){
					text = "";
				}
				etText.setText(text);	
				TextExpenseEntryWatcher textWatcher = new TextExpenseEntryWatcher(etText,entryRow);
				etText.addTextChangedListener(textWatcher);
			
				vRowItem.addView(etText);	
				vRowItem.requestLayout();
			}
		}		
		
	}
	public void setupColumnHeader(ViewGroup v)
	{	
		int resArrayString = 0;
		ViewGroup vRowItem = (ViewGroup)v.findViewById(R.id.ll_expense_row_item);
		vRowItem.removeAllViews();
		vRowItem.setBackgroundColor(
				context.getResources().getColor(R.color.gray_expense_title_color_bg));
		
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			resArrayString = R.array.label_columns_expense_fuel;
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{
			resArrayString = R.array.label_columns_expense_other;
		}		
		String titleColumns[] = context.getResources().getStringArray(resArrayString);	
		for(int i = 0; i < titleColumns.length;i++){
			TextView tvTitleColumn = new TextView(context);
			tvTitleColumn.setLayoutParams(new LinearLayout.LayoutParams(
					(int)ScreenUtil.convertDpToPixel(COL_WIDTH, this.context),
					LayoutParams.WRAP_CONTENT));
			tvTitleColumn.setText(titleColumns[i]);
			vRowItem.addView(tvTitleColumn);	
			vRowItem.requestLayout();
		}
	}
	public void addNewExpenseRow(){
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			for(ExpenseEntryRowFuel item : expenseEntryRowFuelList)
			{
				item.setEditable(false);//reset to view only
			}			
			ExpenseEntryRowFuel fuel = new ExpenseEntryRowFuel();		
			fuel.setRowHeader(false);
			fuel.setEditable(true);
			expenseEntryRowFuelList.add(fuel);
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{
			for(ExpenseEntryRowOther item : expenseEntryRowOtherList)
			{
				item.setEditable(false);
			}
			ExpenseEntryRowOther  other = new ExpenseEntryRowOther();
			other.setRowHeader(false);
			other.setEditable(true);
			expenseEntryRowOtherList.add(other);
		}		
		update();
	}
	public void update(){
		this.notifyDataSetChanged();
	}
	public View getRowHeaderView(){
		return viewRowHeader;
	}
	public void removeAt(int position)
	{
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			this.expenseEntryRowFuelList.remove(position);
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{			
			this.expenseEntryRowOtherList.remove(position);
		}
		update();
	}
	public void setEditMode(int position)
	{
		if (expenseGroupType == ExpenseGroupType.FUEL)
		{
			this.expenseEntryRowFuelList.get(position).setEditable(true);
		}else if (expenseGroupType == ExpenseGroupType.OTHER)
		{			
			this.expenseEntryRowOtherList.get(position).setEditable(true);
		}
		update();
	}
	public void addExpenseFuel(ArrayList<ExpenseEntryRowFuel> fuelList)
	{
		/*
		expenseEntryRowFuelList = new ArrayList<ExpenseEntryRowFuel>();
		ExpenseEntryRowFuel header = new ExpenseEntryRowFuel();
		header.setRowHeader(true);
		expenseEntryRowFuelList.add(header);*/
		expenseEntryRowFuelList.addAll(fuelList);
		
		//update();
	}
	public void addExpenseOther(ArrayList<ExpenseEntryRowOther> otherList)
	{
		/*
		expenseEntryRowOtherList = new ArrayList<ExpenseEntryRowOther>();
		ExpenseEntryRowOther header = new ExpenseEntryRowOther();
		header.setRowHeader(true);
		expenseEntryRowOtherList.add(header);*/
		expenseEntryRowOtherList.addAll(otherList);
		
		//update();
	}
}

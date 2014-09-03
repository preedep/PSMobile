/**
 * 
 */
package com.epro.psmobile.form.template.choice.matrix;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.choice.ChoiceBaseAdapter;
import com.epro.psmobile.util.DataUtil;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class MatrixItemAdapter extends ChoiceBaseAdapter {
	
	private Context mCtxt;
	private LayoutInflater mInflater;
	private ArrayList<String> mChoiceNames;
	private ArrayList<String> mChoiceValues;
	
	private String[] checkedList;
	

	/**
	 * 
	 */
	public MatrixItemAdapter(Context ctxt,
			ArrayList<String> choiceNames,
			ArrayList<String> choiceValues) {
		// TODO Auto-generated constructor stub
		mCtxt = ctxt;
		mInflater = LayoutInflater.from(ctxt);
		mChoiceNames = new ArrayList<String>();
		mChoiceNames.add("");
		mChoiceNames.addAll(choiceNames);
		
		
		mChoiceValues = new ArrayList<String>();
		mChoiceValues.add("");
		mChoiceValues.addAll(choiceValues);
		
		
		checkedList = new String[choiceValues.size()];
		DataUtil.intialStringArray(checkedList, "0");

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mChoiceNames.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;//this.mChoiceNames.get(arg0);
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = null;
		int cellWidth = 150;
		int cellHeight = 100;
		if (convertView == null)
		{
			v = this.mInflater.inflate(R.layout.choice_type_matrix, null);
			/*
			GridView gridView = (GridView)v.findViewById(R.id.gv_choice_matrix);
			gridView.setNumColumns(this.mChoiceValues.size()+1);
			gridView.setAdapter(new MatrixChoiceAdapter());
			*/
			
			ViewGroup vgRow = (ViewGroup)v.findViewById(R.id.ll_choice_matrix_panel);
			if (position == 0){
				for(int i = 0; i < this.mChoiceValues.size();i++)
				{
					TextView tv = new TextView(this.mCtxt);
					tv.setLayoutParams(new ViewGroup.LayoutParams(cellWidth,cellHeight));				
					tv.setText(this.mChoiceValues.get(i));				
					tv.setGravity(Gravity.CENTER);				
					vgRow.addView(tv);
				}
			}
			else{
				
				TextView tv = new TextView(this.mCtxt);
				tv.setLayoutParams(new ViewGroup.LayoutParams(cellWidth,cellHeight));			
				tv.setText(this.mChoiceNames.get(position));				
				tv.setGravity(Gravity.CENTER);				
				vgRow.addView(tv);

				
				//checkedList.add(position, "0");
				if (this.getDataSaved() == null)
				{
					checkedList[position] = "0";
//					checkedList.add(position, "0");
				}

				
				
				RadioGroup rg = new RadioGroup(this.mCtxt);
				rg.setOrientation(RadioGroup.HORIZONTAL);
				rg.setLayoutParams(
						new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT));

				
					int choiceValuesItem = this.mChoiceValues.size() - 1 ; // - 1 because remove empty item
					for(int i = 0; i < choiceValuesItem;i++)
					{
						final int iRadioItemChanged = (i+1);
						RadioButton rb = new RadioButton(this.mCtxt);						
						rb.setLayoutParams(new ViewGroup.LayoutParams(cellWidth,cellHeight));			
						rb.setGravity(Gravity.CENTER);
						
						rg.addView(rb);//add to radio group

						//////////////////////////////////////////
						if (getDataSaved() != null)
						{
							//if (this.getDataSaved().getTaskControlNo() == position)
							//{
									try{
										String value = this.getDataSaved().getValueAt(position);
										//checkedList.add(position,value);
										checkedList[position] = value;
										
										int idxColumnAt = Integer.parseInt(value) - 1;
										if (i == idxColumnAt){
											rb.setChecked(true);
										}
									}catch(Exception ex)
									{
//										checkedList.add(position,"0");//reset all
										checkedList[position] = "0";
										ex.printStackTrace();
									}
							//}
							
						}
						///////////////////////////////////
						
						rb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								checkedList[position]= isChecked?iRadioItemChanged+"":"0";
							}
							
						});
					}
				vgRow.addView(rg);
			}
		}else{
			v = convertView;
		}
		
		
		
		return v;
	}

	@Override
	public TaskFormDataSaved getValues() {
		// TODO Auto-generated method stub
		TaskFormDataSaved dataSaved = new TaskFormDataSaved();
		StringBuilder strBld = new StringBuilder();
		for(String checked : checkedList)
		{
			strBld.append(checked+"@@");
		}
		dataSaved.setTaskDataValues(strBld.toString());
		return dataSaved;	
	}
	@Override
	public ArrayList<ReasonSentence> getChooceReasonSentenceSelected() {
		// TODO Auto-generated method stub
		return null;
	}
}

/**
 * 
 */
package com.epro.psmobile.form.template.choice.single;


import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.TaskFormTemplate;
import com.epro.psmobile.data.choice.ChoiceBaseAdapter;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class SingleItemAdapter extends ChoiceBaseAdapter {

	public interface OnChoiceClickListener
	{
		void onClick(boolean isChecked,int position,TaskFormTemplate child);
	}
	private LayoutInflater inflater;
	private ArrayList<String> names;
	TaskFormTemplate formTemplate = null;
	private int mResourceId = 0;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
	private ArrayList<ReasonSentence> reasonSentenceList;

	private ArrayList<TaskFormTemplate> childList;
	
	private OnChoiceClickListener clickListener;
	private Activity activity;
	
	private ReasonSentence childDropDownRentenceSelected;
	
    private String[] checkedList;
    public class ViewHolder{
        public TextView        name;
        public RadioButton     radioBtn;
        public TaskFormTemplate child;
    }
    public SingleItemAdapter(Activity activity,
			ArrayList<String> names)
    {
    	this(activity,names,null);
    }
	public SingleItemAdapter(Activity activity,
			ArrayList<String> names,
			ArrayList<TaskFormTemplate> childList)
	{
		 this.activity = activity;
		 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 this.names = names;	
		 this.childList = childList;
		 checkedList = new String[names.size()];
		 DataUtil.intialStringArray(checkedList, "0");

		 
	}
	public void setOnChoiceClick(OnChoiceClickListener clickListener)
	{
		this.clickListener = clickListener;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.names.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return names.get(arg0);
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
		View view = convertView;
        ViewHolder holder;
        
		if(view == null)
		{
			 	view = inflater.inflate(R.layout.choice_type_multiple_single_item, arg2,false);
				TextView tvChoiceName = (TextView)view.findViewById(R.id.tv_single_choice_name);
				tvChoiceName.setText(this.getItem(position)+"");

				holder = new ViewHolder();
	            holder.name = (TextView)view.findViewById(R.id.tv_single_choice_name);
	            holder.radioBtn = (RadioButton)view.findViewById(R.id.rd_single_choice);

	            view.setTag(holder);
	            
//	            checkedList.add(position, "0");
	            
				if (this.getDataSaved() == null)
				{
					checkedList[position] =  "0";
				}

	        }else{
	            holder = (ViewHolder)view.getTag();
	        }

		 
		 holder.radioBtn.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
                   SharedPreferenceUtil.setAlreadyCommentSaved(activity,false);

	            	
	            	Log.d("DEBUG_D", "Radio clicked");

	                if((position != mSelectedPosition && mSelectedRB != null)){
	                    mSelectedRB.setChecked(false);
	                    	checkedList[position] =  "0";
	                }
	                for(int i = 0; i < checkedList.length;i++)
	                	if (i != position){
	                		checkedList[i] = "0";
	                	}

                    checkedList[position] =  "1";	                	
	                
                    
	                
	    	        mSelectedPosition = position;
	                mSelectedRB = (RadioButton)v;
	               
	                
	                //////////////
	    	        if (clickListener != null)
	                {
	                		
	    	        	formTemplate = null;
	                	if (childList != null)
	                	{
	                		/*
	                		try{
	                			
	                			formTemplate = childList.get(position);
	                			
	                		}catch(Exception ex){}
	                		*/
	                		try{
	                			
//	                			formTemplate = childList.get(position);              
	                		    formTemplate = null;
	                			ReasonSentence rs = reasonSentenceList.get(position);
	                			for(TaskFormTemplate tf : childList)
	                			{
		                			if (tf.getParentId().equalsIgnoreCase(rs.getReasonSentencePath()))
		                			{
		                				formTemplate = tf;
		                			}
	                			}
	                			
	                		}catch(Exception ex){}
	                	}
	                	activity.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								clickListener.onClick(mSelectedRB.isChecked(),position,
		                				formTemplate);
							}
	                		
	                	});
	                	
	                }	  
	    	        
	            }
	        });
	        if(mSelectedPosition != position){
	            holder.radioBtn.setChecked(false);	            
                checkedList[position] =  "0";
                
	        }else{
	            holder.radioBtn.setChecked(true);
                checkedList[position] =  "1";
                
                
	            if(mSelectedRB != null && holder.radioBtn != mSelectedRB){
	                mSelectedRB = holder.radioBtn;
	            }
	        }
	        
	        
	        if (this.getDataSaved() != null)
	        {
				try{
					String value = this.getDataSaved().getValueAt(position);
					checkedList[position] = value;
					holder.radioBtn.setChecked(Integer.parseInt(value) > 0);
					if (holder.radioBtn.isChecked())
					{
				         mSelectedRB = holder.radioBtn;
				         
				        formTemplate = null;
		                if (childList != null)
		                {
		                		try{
		                			ReasonSentence rs = reasonSentenceList.get(position);
		                			for(TaskFormTemplate tf : childList)
		                			{
			                			if (tf.getParentId().equalsIgnoreCase(rs.getReasonSentencePath()))
			                			{
			                				formTemplate = tf;
			                			}
		                			}
		                		}catch(Exception ex){}
		                 }
				         activity.runOnUiThread(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									clickListener.onClick(mSelectedRB.isChecked(),
											position,
			                				formTemplate);
								}
		                		
		                	});
					}
				}catch(Exception ex)
				{
					checkedList[position] = "0";//reset all
					ex.printStackTrace();
				}	        	
	        }

	        holder.name.setText(getItem(position)+"");

		return view;
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
	public void setChildReasonSentence(ReasonSentence reasonSentence)
	{
		this.childDropDownRentenceSelected = reasonSentence; 
	}
	public ReasonSentence getChildDropDownReasonSetence(){
		return this.childDropDownRentenceSelected;
	}
	public ArrayList<ReasonSentence> getReasonSentenceList() {
		return reasonSentenceList;
	}
	public void setReasonSentenceList(ArrayList<ReasonSentence> reasonSentenceList) {
		this.reasonSentenceList = reasonSentenceList;
	}
	@Override
	public ArrayList<ReasonSentence> getChooceReasonSentenceSelected() {
		// TODO Auto-generated method stub
		ArrayList<String> taskDataSaveds = DataUtil.textSplitByComma(getValues().getTaskDataValues());
		this.chooceReasonSentenceSelected.clear();
		for(int i = 0; i < taskDataSaveds.size();i++)
		{
			String dataSave = taskDataSaveds.get(i);
			if (dataSave.equalsIgnoreCase("1")){
				this.chooceReasonSentenceSelected.add(this.reasonSentenceList.get(i));
			}
		}
		return this.chooceReasonSentenceSelected;
	}

}

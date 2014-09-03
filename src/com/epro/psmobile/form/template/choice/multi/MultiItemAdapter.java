package com.epro.psmobile.form.template.choice.multi;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.choice.ChoiceBaseAdapter;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MultiItemAdapter extends ChoiceBaseAdapter {
	
	public class TagHolder
	{
		public CheckBox checkBox;
		
	}
	private LayoutInflater inflater;
	private ArrayList<String> names;
	private String[] checkedList;
	private ArrayList<ReasonSentence> reasonSentenceList;
	private Context context;
	public MultiItemAdapter(Activity activity,ArrayList<String> names)
	{
	      this.context = activity;
		 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 this.names = names;	
		 checkedList = new String[names.size()];
		 DataUtil.intialStringArray(checkedList, "0");

	}
	public ArrayList<ReasonSentence> getReasonSentenceList() {
		return reasonSentenceList;
	}
	public void setReasonSentenceList(ArrayList<ReasonSentence> reasonSentenceList) {
		this.reasonSentenceList = reasonSentenceList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.names.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.names.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;

		if (convertView == null)
		{
			view  = inflater.inflate(R.layout.choice_type_multiple_multi_item, null);
			//TextView tvChoiceName = (TextView)view.findViewById(R.id.tv_single_choice_name);
			//tvChoiceName.setText(this.getItem(position)+"");
			CheckBox chkBox = (CheckBox)view.findViewById(R.id.cb_multi_choice);
		
			TextView tvChoiceName = (TextView)view.findViewById(R.id.tv_multi_choice_name);
			tvChoiceName.setText(this.getItem(position)+"");
			

			
			if (this.getDataSaved() == null)
				checkedList[position]=  "0";
			else{
//				if (this.getDataSaved().getTaskControlNo() == position){
					try{
						String value = this.getDataSaved().getValueAt(position);
						checkedList[position] = value;
						chkBox.setChecked(Integer.parseInt(value) > 0);
					}catch(Exception ex)
					{
						checkedList[position] = "0";//reset all
						ex.printStackTrace();
					}
//				}
			}
			
			TagHolder tagHolder = new TagHolder();
			tagHolder.checkBox = chkBox;
			
			view.setTag(tagHolder);
		}else{
			view = convertView;
		}


		((TagHolder)(view.getTag())).checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
		         SharedPreferenceUtil.setAlreadyCommentSaved(context,false);

				checkedList[position] =  isChecked?"1":"0";
		}}) ;

		
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

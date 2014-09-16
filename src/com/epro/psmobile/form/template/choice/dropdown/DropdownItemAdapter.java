package com.epro.psmobile.form.template.choice.dropdown;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.choice.ChoiceBaseAdapter;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.view.ReasonSentenceSpinner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class DropdownItemAdapter extends ChoiceBaseAdapter {

	public class TagHolder
	{
		ReasonSentenceSpinner reason;
		EditText editText;
	};
	
	private Context activity;
	private ArrayList<ReasonSentence> sentences;
	private LayoutInflater inflater;
	private ReasonSentenceSpinner rsp;
	private EditText editText;
	public DropdownItemAdapter(Context activity,
			ArrayList<ReasonSentence> sentences) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.sentences = sentences;
		
		 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		if (convertView == null)
		{
			v = inflater.inflate(R.layout.choice_type_dropdownlist, null, false);
			
			rsp =  (ReasonSentenceSpinner)v.findViewById(R.id.sp_comment_custom_form_reason);
			rsp.initialWithReasonList(sentences);
			rsp.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					// TODO Auto-generated method stub
//					DropdownItemAdapter.this.setReasonSentence(reasonSentence);
					Object obj  = parent.getItemAtPosition(position);
					if (obj instanceof ReasonSentence)
					{
					    SharedPreferenceUtil.setAlreadyCommentSaved(activity,false);

						DropdownItemAdapter.this.setReasonSentence((ReasonSentence)obj);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			editText = (EditText)v.findViewById(R.id.et_answer_dropdownlist_question);
			
			
			TagHolder tagHolder = new TagHolder();
			tagHolder.reason = rsp;
			tagHolder.editText = editText;
			
			v.setTag(tagHolder);
		}else{
			v = convertView;
		}
		
		TagHolder holder = (TagHolder)v.getTag();
		
		if (this.getDataSaved() != null)
		{
			try{
			String values = this.getDataSaved().getTaskDataValues();
			if ((values != null)&&(!values.isEmpty()))
			{
				String[] valueArray = values.split("@@");
				holder.editText.setText(valueArray[1]);
				
				for(int i = 0 ; i < rsp.getCount();i++){
					ReasonSentence reasonSentenceSelected = (ReasonSentence)rsp.getItemAtPosition(i);
					if (reasonSentenceSelected.getReasonText().equalsIgnoreCase(valueArray[0]))
					{
						rsp.setSelection(i);
						break;
					}
				}
			 }
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return v;
	}

	@Override
	public TaskFormDataSaved getValues() {
		// TODO Auto-generated method stub

		TaskFormDataSaved dataSaved = new TaskFormDataSaved();
		StringBuilder strBld = new StringBuilder();
		if (rsp != null){
		   ReasonSentence reasonSentenceSelected = (ReasonSentence)rsp.getSelectedItem();
		
		   String reasonText = reasonSentenceSelected.getReasonText();
		   String dataText = editText.getText().toString();
		   
		   strBld.append(reasonText);
		   strBld.append("@@");
		   strBld.append(dataText);
		
		   dataSaved.setTaskDataValues(strBld.toString());
		}else{
		   dataSaved.setTaskDataValues("");
		}
		return dataSaved;
	}
	@Override
	public ArrayList<ReasonSentence> getChooceReasonSentenceSelected() {
		// TODO Auto-generated method stub
		return null;
	}
}

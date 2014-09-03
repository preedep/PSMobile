/**
 * 
 */
package com.epro.psmobile.form.template.choice.sliderbar;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.choice.ChoiceBaseAdapter;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class SliderBarItemAdapter extends ChoiceBaseAdapter  {

	public class TagHolder
	{
		SeekBar seekBar;
	};
	private Context mCtxt;
	private LayoutInflater mInflater;
	private ArrayList<String> mChoiceNames;
	private int mMaxScale;
	private int mMin;
	private int mStep;
	private String[] checkedList;
	/**
	 * 
	 */
	public SliderBarItemAdapter(Context ctxt,
			ArrayList<String> choiceNames,
			int maxScale,
			int min,
			int step) {
		// TODO Auto-generated constructor stub
		
		this.mCtxt = ctxt;
		mInflater = LayoutInflater.from(ctxt);
		mChoiceNames = choiceNames;
		mMaxScale = maxScale;
		mMin = min;
		mStep = step;
		
		checkedList = new String[mChoiceNames.size()];
		DataUtil.intialStringArray(checkedList, "0");
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mChoiceNames.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mChoiceNames.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		View v = null;
		
		if (convertView == null)
		{
			//checkedList.add(position, "0");
			
			v = mInflater.inflate(R.layout.choice_type_slider_item, null);
			TextView tvChoiceName = (TextView)v.findViewById(R.id.tv_slider_choice_name);
			tvChoiceName.setText(this.getItem(position)+"");

			TextView tvSlideChanged = (TextView)v.findViewById(R.id.tv_slider_indicator_value);

			SeekBar slide = (SeekBar)v.findViewById(R.id.slider_value);
			slide.setProgress(mMin);
			slide.setMax(mMaxScale);
			slide.setTag(tvSlideChanged);
//			slide.setOnSeekBarChangeListener(this);	
			
			TagHolder tagHolder = new TagHolder();
			tagHolder.seekBar = slide;
			v.setTag(tagHolder);
			
			if (this.getDataSaved() == null)
				checkedList[position] =  "0";
			else{
				//if (this.getDataSaved().getTaskControlNo() == position)
				//{
					try{
						String value = this.getDataSaved().getValueAt(position);
						checkedList[position] = value;
						slide.setProgress(Integer.parseInt(value));
					}catch(Exception ex)
					{
						checkedList[position] = "0";//reset all
						ex.printStackTrace();
					}
				//}
			}

		}else{
			v = convertView;
		}
		
		((TagHolder)v.getTag()).seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
               SharedPreferenceUtil.setAlreadyCommentSaved(mCtxt,false);

			    progress = ((int)Math.round(progress/mStep))*mStep;
			    seekBar.setProgress(progress);
			    
			    TextView textView = (TextView)seekBar.getTag();
			    textView.setText(progress + "");
			    
			    checkedList[position] = progress+"";

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
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

/**
 * 
 */
package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.ReasonSentence.ReasonType;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class ReasonSentenceSpinner extends BasicSpinner {

	private ArrayList<ReasonSentence> sentenceList;
	class ReasonSentenceAdapter extends BaseAdapter 
	{

		private ArrayList<ReasonSentence> reasonList;
		private Context context;
		private LayoutInflater inflater;
		
		public ReasonSentenceAdapter(
				Context context,
				ArrayList<ReasonSentence> reasonList)
		{
			this.context = context;
			this.reasonList = reasonList;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}
		public void clear(){
			if (this.reasonList != null)
				this.reasonList.clear();
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (this.reasonList == null)
				return 0;
			return this.reasonList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.reasonList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ReasonSentence reason =  (ReasonSentence)this.getItem(position);
				
			View v = inflater.inflate(R.layout.simple_spinner_item, null, false);
			
			TextView tv = (TextView)v.findViewById(R.id.tv_simple_spinner_text_item);
			tv.setText(reason.getReasonText());
			
			v.setTag(reason);
			
			return v;
		}
		
	}
	/**
	 * @param context
	 */
	public ReasonSentenceSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param mode
	 */
	public ReasonSentenceSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ReasonSentenceSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ReasonSentenceSpinner(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param mode
	 */
	public ReasonSentenceSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.view.BasicSpinner#initial()
	 */
	@Override
	public void initial() throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Not support");
	}
	/* (non-Javadoc)
	 * @see com.epro.psmobile.view.BasicSpinner#getData(java.lang.Class)
	 */
	@Override
	public <T> ArrayList<T> getData(Class<T> type) {
		// TODO Auto-generated method stub
		return (ArrayList<T>)sentenceList;
	}

	public void initialWithReasonType(ReasonType reasonType) throws Exception {
		// TODO Auto-generated method stub
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
		sentenceList = dataAdapter.getAllReasonSentenceByType(reasonType);
		
		ReasonSentenceAdapter adapter = new ReasonSentenceAdapter(getContext(),sentenceList);
		setAdapter(adapter);
		((ReasonSentenceAdapter)getAdapter()).notifyDataSetChanged();
	}
	
	public void initialWithReasonList(ArrayList<ReasonSentence> reasonList)
	{
		sentenceList = reasonList;
		///////
		
		ReasonSentenceAdapter adapter = new ReasonSentenceAdapter(getContext(),sentenceList);
		setAdapter(adapter);		
		((ReasonSentenceAdapter)getAdapter()).notifyDataSetChanged();
	}
	public void clear(){
		if (this.getAdapter() != null){
			((ReasonSentenceAdapter)getAdapter()).clear();
			((ReasonSentenceAdapter)getAdapter()).notifyDataSetChanged();
		}
		else{
			this.setAdapter(null);
		}
	}

}

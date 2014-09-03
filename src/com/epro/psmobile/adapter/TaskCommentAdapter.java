/**
 * 
 */
package com.epro.psmobile.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.TaskControlTemplate;
import com.epro.psmobile.data.TaskFormTemplate;
import com.epro.psmobile.data.choice.ChoiceBaseAdapter;
import com.epro.psmobile.form.template.choice.dropdown.DropdownItemAdapter;
import com.epro.psmobile.form.template.choice.matrix.MatrixItemAdapter;
import com.epro.psmobile.form.template.choice.multi.MultiItemAdapter;
import com.epro.psmobile.form.template.choice.single.SingleItemAdapter;
import com.epro.psmobile.form.template.choice.sliderbar.SliderBarItemAdapter;
import com.epro.psmobile.form.template.choice.sortable.DragListener;
import com.epro.psmobile.form.template.choice.sortable.DragNDropAdapter;
import com.epro.psmobile.form.template.choice.sortable.DragNDropListView;
import com.epro.psmobile.form.template.choice.sortable.DropListener;
import com.epro.psmobile.form.template.choice.sortable.RemoveListener;
import com.epro.psmobile.util.DataUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class TaskCommentAdapter extends BaseAdapter {

	public class TaskCommentHolder
	{
		public TaskFormTemplate taskFormTemplate;
		public ChoiceBaseAdapter choiceBaseAdapter;
		public String simpleTextAnswer;
	};
	
	private ArrayList<TaskFormTemplate> taskFormTemplateList;
	private LayoutInflater inflater;
	private Context context;
	private View[] activedViewList;
	private View edt_inject;
	
	private int year;
	private int month;
	private int day;
	
	private Hashtable<Integer,DatePickerDialog> datePickerDlgTable = 
				new Hashtable<Integer,DatePickerDialog>();
	/**
	 * 
	 */
	public TaskCommentAdapter(Context ctxt,ArrayList<TaskFormTemplate> taskFormTemplateList) {
		// TODO Auto-generated constructor stub
		this.context = ctxt;
		this.taskFormTemplateList = taskFormTemplateList;
		inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		activedViewList = new View[this.taskFormTemplateList.size()];
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return taskFormTemplateList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return taskFormTemplateList.get(position);
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
		if (activedViewList[position] != null)
		{
			v = activedViewList[position];
		}else{

			final TaskFormTemplate  taskFormTemplate = (TaskFormTemplate)getItem(position);
			TaskControlTemplate.TaskControlType controlType =  taskFormTemplate.getControlType();

			Log.d("DEBUG_D", "question = "+taskFormTemplate.getTextQuestion()+" " +
					"control type = "+controlType.name()+" no "+taskFormTemplate.getTaskControlNo());
			
			switch(controlType)
			{
				case SimpleTextDecimal:
				case SimpleTextDate:		
				case SimpleText:{
			
					int resId = 0;
					
					if (controlType == TaskControlTemplate.TaskControlType.SimpleText){
						v = inflater.inflate(R.layout.choice_type_simple_text_question, null,false);						
						resId = R.id.et_answer_simple_question;
					}else{
						v = inflater.inflate(R.layout.choice_type_simple_text_format_question, null, false);
						resId = R.id.et_answer_simple_format_question;
					}					
					edt_inject = v;
					final EditText edtAnswer = (EditText)v.findViewById(resId);
					
					if (controlType == TaskControlTemplate.TaskControlType.SimpleTextDecimal)
					{
						edtAnswer.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
					}else if (controlType == TaskControlTemplate.TaskControlType.SimpleTextDate)
					{
						edtAnswer.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								/*
								 open date picker dialog
								 */
								final Calendar c = Calendar.getInstance();
					            year = c.get(Calendar.YEAR);
					            month = c.get(Calendar.MONTH)+1;
					            day = c.get(Calendar.DAY_OF_MONTH);

					            
					            int controlNo = taskFormTemplate.getTaskControlNo();
					            DatePickerDialog dlgDate = null;
					            if (!datePickerDlgTable.containsKey(controlNo))
					            {
					            	dlgDate = new DatePickerDialog(context, 
						            		new OnDateSetListener(){

												@Override
												public void onDateSet(
														DatePicker view, int year,
														int monthOfYear,
														int dayOfMonth) {
													// TODO Auto-generated method stub
													
												}
						            	
						            }, year, 
						               month - 1,
						               day);
					            	
					            	datePickerDlgTable.put(controlNo, dlgDate);
					            }else{
					            	dlgDate = datePickerDlgTable.get(controlNo);
					            }
					            
					            if (!dlgDate.isShowing())
					            		dlgDate.show();					            
							}
							
						});
					}
					edtAnswer.addTextChangedListener(new TextWatcher(){

						@Override
						public void afterTextChanged(Editable arg0) {
							// TODO Auto-generated method stub
							/*
							TaskCommentHolder holder = new TaskCommentHolder();
							holder.simpleTextAnswer = edtAnswer.getText().toString();
							holder.taskFormTemplate = taskFormTemplate;
							
							v.setTag(holder);*/
							
							View currentView = activedViewList[position];
							if (currentView != null)
							{
								((TaskCommentHolder)currentView.getTag()).simpleTextAnswer = 
										edtAnswer.getText().toString();
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub
						}
					});

					if (taskFormTemplate.getDataSaved() != null)
					{
						edtAnswer.setText(taskFormTemplate.getDataSaved().getTaskDataValues());
					}
					
					TaskCommentHolder holder = new TaskCommentHolder();
					holder.simpleTextAnswer = edtAnswer.getText().toString();
					holder.taskFormTemplate = taskFormTemplate;
					
					v.setTag(holder);
					
					activedViewList[position] = v;

				}break;
				case CheckBoxList:
				{
					ArrayList<String> names = DataUtil.textSplitByComma(taskFormTemplate.getChoiceTexts());
					MultiItemAdapter multipleAdapter = new MultiItemAdapter((Activity)context,names);	
					multipleAdapter.setDataSaved(taskFormTemplate.getDataSaved());
					multipleAdapter.setReasonSentenceList(taskFormTemplate.getChildReasonSentenceList());
					
					/*
					v = inflater.inflate(R.layout.inspect_comment_entry_list_item, null,false);
					ListView lv = (ListView)v.findViewById(R.id.lv_tasks_comment_item);
					lv.setAdapter(multipleAdapter);	
					
					injectGlobalLayout(lv);
					 */
					
					ViewGroup l_container = (ViewGroup)inflater.inflate(R.layout.choice_type_linear_layout_container, null, false);
					
					ViewGroup radioCheckContainer = (ViewGroup)l_container.findViewById(R.id.radio_check_list_container);
					for(int i = 0; i < multipleAdapter.getCount();i++)
					{
						View vItem = multipleAdapter.getView(i, null, l_container);
						radioCheckContainer.addView(vItem);
					}
					
					v = l_container;
					
					TaskCommentHolder holder = new TaskCommentHolder();
					holder.choiceBaseAdapter = multipleAdapter;
					holder.taskFormTemplate = taskFormTemplate;		
					holder.choiceBaseAdapter.setDataSaved(taskFormTemplate.getDataSaved());
					v.setTag(holder);		
					
					activedViewList[position] = v;

					
					
				}break;
				case RadioBoxList:{
					ArrayList<String> names = DataUtil.textSplitByComma(taskFormTemplate.getChoiceTexts());
					SingleItemAdapter singleAdapter = new SingleItemAdapter((Activity)context,names);	
					singleAdapter.setDataSaved(taskFormTemplate.getDataSaved());
					singleAdapter.setReasonSentenceList(taskFormTemplate.getChildReasonSentenceList());
					/*
					v = inflater.inflate(R.layout.inspect_comment_entry_list_item, null,false);
					ListView lv = (ListView)v.findViewById(R.id.lv_tasks_comment_item);
					lv.setAdapter(singleAdapter);				
					*/
					//LinearLayout l_container = new LinearLayout(context);
					//l_container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					ViewGroup l_container = (ViewGroup)inflater.inflate(R.layout.choice_type_linear_layout_container, null, false);
					
					ViewGroup radioCheckContainer = (ViewGroup)l_container.findViewById(R.id.radio_check_list_container);
					for(int i = 0; i < singleAdapter.getCount();i++)
					{
						View vItem = singleAdapter.getView(i, null, l_container);
						radioCheckContainer.addView(vItem);
					}
					
					v = l_container;
					
					TaskCommentHolder holder = new TaskCommentHolder();
					holder.choiceBaseAdapter = singleAdapter;
					holder.taskFormTemplate = taskFormTemplate;					
					holder.choiceBaseAdapter.setDataSaved(taskFormTemplate.getDataSaved());

					v.setTag(holder);
					activedViewList[position] = v;


				}break;
				case RadioBoxMatrix:{
					ArrayList<String> names = DataUtil.textSplitByComma(taskFormTemplate.getChoiceTexts());
					//ArrayList<String> choiceTexts = new ArrayList<String>();
					//choiceTexts.addAll(names);
					
					ArrayList<String> columns = DataUtil.textSplitByComma(taskFormTemplate.getChoiceTextMatrixColumns());
					
					MatrixItemAdapter matrixAdapter = new MatrixItemAdapter(this.context,names,columns);
					matrixAdapter.setDataSaved(taskFormTemplate.getDataSaved());
					
					v = inflater.inflate(R.layout.inspect_comment_entry_list_item,null,false);
					ListView lv = (ListView)v.findViewById(R.id.lv_tasks_comment_item);
					lv.setAdapter(matrixAdapter);
					
					injectGlobalLayout(lv,position);
					
					TaskCommentHolder holder = new TaskCommentHolder();
					holder.choiceBaseAdapter = matrixAdapter;
					holder.taskFormTemplate = taskFormTemplate;
					holder.choiceBaseAdapter.setDataSaved(taskFormTemplate.getDataSaved());

					v.setTag(holder);

					activedViewList[position] = v;

					
				}break;
				case Slider:{
					ArrayList<String> names = DataUtil.textSplitByComma(taskFormTemplate.getChoiceTexts());
					
					int max = taskFormTemplate.getMaxSlider();
					int min = taskFormTemplate.getMinSlider();
					int step = taskFormTemplate.getStepSlider();
					SliderBarItemAdapter adapter = new SliderBarItemAdapter(this.context,names,max,min,step);
					adapter.setDataSaved(taskFormTemplate.getDataSaved());
					
					v = inflater.inflate(R.layout.inspect_comment_entry_list_item, null,false);
					ListView lv = (ListView)v.findViewById(R.id.lv_tasks_comment_item);
					lv.setAdapter(adapter);
					
					injectGlobalLayout(lv,position);					

					TaskCommentHolder holder = new TaskCommentHolder();
					holder.choiceBaseAdapter = adapter;
					holder.taskFormTemplate = taskFormTemplate;
					holder.choiceBaseAdapter.setDataSaved(taskFormTemplate.getDataSaved());

					v.setTag(holder);
					
					activedViewList[position] = v;

				}break;
				case Dropdownlist:{
					
					ArrayList<ReasonSentence> reasons = null;
					String reasonSentenceType = taskFormTemplate.getReasonSentenceType();
					
					PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);
					
					try {
						reasons = dataAdapter.getAllReasonSentenceByType(reasonSentenceType);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					DropdownItemAdapter adapter = new DropdownItemAdapter(this.context,reasons);
					adapter.setDataSaved(taskFormTemplate.getDataSaved());//restored last saved
					
					v = inflater.inflate(R.layout.inspect_comment_entry_list_item, null,false);
					ListView lv = (ListView)v.findViewById(R.id.lv_tasks_comment_item);
					lv.setAdapter(adapter);
					
					adapter.notifyDataSetChanged();
					
					injectGlobalLayout(lv,position);					

					TaskCommentHolder holder = new TaskCommentHolder();
					holder.choiceBaseAdapter = adapter;
					holder.taskFormTemplate = taskFormTemplate;
					holder.choiceBaseAdapter.setDataSaved(taskFormTemplate.getDataSaved());

					v.setTag(holder);
					
					activedViewList[position] = v;

				}break;
				/*
				case Sortable:
				{
					ArrayList<String> names = DataUtil.textSplitByComma(taskFormTemplate.getChoiceTexts());

					v = inflater.inflate(R.layout.choice_type_sortable, parent,false);
					DragNDropListView lv = (DragNDropListView)v.findViewById(R.id.lv_multiple_sortable);

					DragNDropAdapter adapter = new DragNDropAdapter(this.context,
							new int[]{R.layout.choice_type_sortable_item},
							new int[]{R.id.tv_sortable_choice_name,R.id.tv_sortable_number},names);
		 			lv.setAdapter(adapter);
		 		
		 			lv.setDropListener(mDropListener);
		 			lv.setRemoveListener(mRemoveListener);
		 			lv.setDragListener(mDragListener);

					injectGlobalLayout(lv);					

					
					TaskCommentHolder holder = new TaskCommentHolder();
					holder.choiceBaseAdapter = adapter;
					holder.taskFormTemplate = taskFormTemplate;
					holder.choiceBaseAdapter.setDataSaved(taskFormTemplate.getDataSaved());
					
					v.setTag(holder);
					
					activedViewList[position] = v;
					
				}break;
				*/
				default:
			
					break;
			
			}
			

			View vTextQuestion = v.findViewById(R.id.tv_open_text_question);
			if (vTextQuestion instanceof TextView)
			{
				((TextView)vTextQuestion).setText(taskFormTemplate.getTextQuestion());
			}
		}
		
		return v;
	}
	private void injectGlobalLayout(final View lv,int atPosition){
		injectGlobalLayout(lv,-1,atPosition);
	}
	private void injectGlobalLayout(final View lv,
			final int gridRowCount,final int atPosition)
	{
		lv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				int height = 0;
				if (gridRowCount == -1){
					int lvHeight = lv.getHeight();					
					int item = 1;
					if (lv instanceof ListView){
						item = ((ListView)lv).getAdapter().getCount();
					}
					height = lvHeight * item ;
				}else{
					 height = lv.getHeight() * gridRowCount;					
				}
				ViewGroup.LayoutParams layoutParam = lv.getLayoutParams();
				/*
				if (atPosition == 12){
					height += 30;
				}*/
				layoutParam.height = height;
				lv.setLayoutParams(layoutParam);
				
				lv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
			
		});
	}
	
	
	/*
	 * sortable
	*/
	private DropListener mDropListener = 
			new DropListener() {
	        public void onDrop(ListView lv,int from, int to) {
	        	ListAdapter adapter = lv.getAdapter();
	        	if (adapter instanceof DragNDropAdapter) {
	        		((DragNDropAdapter)adapter).onDrop(lv,from, to);
	        		lv.invalidateViews();
	        	}
	        }
	    };
	    
	    private RemoveListener mRemoveListener =
	        new RemoveListener() {
	        public void onRemove(ListView lv,int which) {
	        	ListAdapter adapter = lv.getAdapter();
	        	if (adapter instanceof DragNDropAdapter) {
	        		((DragNDropAdapter)adapter).onRemove(lv,which);
	        		lv.invalidateViews();
	        	}
	        }
	    };
	    
	    private DragListener mDragListener =
	    	new DragListener() {

	    	int backgroundColor = 0xe0103010;
	    	int defaultBackgroundColor;
	    	
				public void onDrag(int x, int y, ListView listView) {
					// TODO Auto-generated method stub
				}

				public void onStartDrag(View itemView) {
					itemView.setVisibility(View.INVISIBLE);
					defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
					itemView.setBackgroundColor(backgroundColor);
					//ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
					//if (iv != null) iv.setVisibility(View.INVISIBLE);
				}

				public void onStopDrag(View itemView) {
					itemView.setVisibility(View.VISIBLE);
					itemView.setBackgroundColor(defaultBackgroundColor);
					//ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
					//if (iv != null) iv.setVisibility(View.VISIBLE);
				}
	    	
	    };
	/*
	private int getTotalHeightofListView(BaseAdapter mAdapter,ListView lv) {

	    ListAdapter LvAdapter = lv.getAdapter();
	    int listviewElementsheight = 0;
	    for (int i = 0; i < mAdapter.getCount(); i++) {
	        View mView = mAdapter.getView(i, null, lv);
	        
	        mView.measure(
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        listviewElementsheight += mView.getMeasuredHeight();
	    }
	    return listviewElementsheight;
	}*/
	    public View[] getActivedViews(){
	    	return 	activedViewList;
	    }
}

/**
 * 
 */
package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.TaskCommentAdapter;
import com.epro.psmobile.adapter.TaskCommentAdapterV2;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.UniversalControlType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskControlTemplate.TaskControlType;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.TaskFormTemplate;
import com.epro.psmobile.form.template.choice.single.SingleItemAdapter;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.InspectServiceSupportUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.ReportInspectSummaryStatusHelper;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ListView;

/**
 * @author nickmsft
 *
 */
public class JobCommentFragment extends ContentViewBaseFragment {

	private View currentContentView;
	private ListView lv_comments;
	private Task currentTask;
	private JobRequest jobRequest;
	private CustomerSurveySite site;
	private ArrayList<InspectFormView> formViewAllCols;
	private JobRequestProduct jobRequestProduct;
	
	/**
	 * 
	 */
	public static JobCommentFragment newUniversalInstance(JobRequest jobRequest,
	      Task task,
	      CustomerSurveySite site,
	      ArrayList<InspectFormView> formViewAllCols,
	      JobRequestProduct jobRequestProduct){
	   
	   JobCommentFragment comment = new JobCommentFragment();
	   
	   Bundle bArgument = new Bundle();
	   
	   bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL,jobRequest);
       bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK,task);
       bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, site);
       bArgument.putParcelableArrayList(InstanceStateKey.KEY_ARGUMENT_UNIVERSAL_COL_PROPERTIES, formViewAllCols);
       bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST, jobRequestProduct);
       
       comment.setArguments(bArgument);
       
	   return comment;
	}
	public JobCommentFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		currentContentView = inflater.inflate(R.layout.inspect_comment_entry_form, container, false);
		initialView(currentContentView);
		return currentContentView;
		
	}
	private void initialView(View view)
	{
	   
	    SharedPreferenceUtil.setAlreadyCommentSaved(getActivity(), false);
	    ////////////////////
	   
	   
		Bundle argument =  this.getArguments();
		if (argument != null)
		{
			ArrayList<TaskFormDataSaved> dataSavedList = null;
			
			jobRequest =  argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
		    currentTask = argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			site = argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
			formViewAllCols = argument.getParcelableArrayList(InstanceStateKey.KEY_ARGUMENT_UNIVERSAL_COL_PROPERTIES);
			jobRequestProduct = argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST);
			
			
		    this.setCustomerInfoTitle(view,currentTask, jobRequest);
			
			PSBODataAdapter adapter = PSBODataAdapter.getDataAdapter(this.getActivity());
			try {
			   if (jobRequest.getInspectType().getInspectTypeID() > InspectServiceSupportUtil.SERVICE_FARM_LAND_2)
               {
			      if (jobRequestProduct != null)
			      {
	                    dataSavedList = adapter.findUniversalTaskFormDataSavedList(jobRequest.getJobRequestID(), 
	                            jobRequest.getInspectType().getInspectTypeID(),
	                            currentTask.getTaskCode(),
	                            site.getCustomerSurveySiteID(),jobRequestProduct.getProductRowID()
	                            );                			         
			      }else{
		                dataSavedList = adapter.findTaskFormDataSavedListByJobRequestId(jobRequest.getJobRequestID(), 
		                        currentTask.getTaskCode());			         
			      }
               }
			   else{
				dataSavedList = adapter.findTaskFormDataSavedListByJobRequestId(jobRequest.getJobRequestID(), 
						currentTask.getTaskCode());
               }
				
				ArrayList<TaskFormTemplate> comments = null;
				if (jobRequest.getInspectType().getInspectTypeID() > InspectServiceSupportUtil.SERVICE_FARM_LAND_2)
				{
				   /*
				    * for universal
				    * 
				    * 
				    */
				   if (formViewAllCols != null)
				   {
				      /*generate comments for check list each row */
	                   InspectFormView vColCheckListForm = null;
	                   for(InspectFormView vCol : formViewAllCols)
	                   {
	                      UniversalControlType ctrlType = UniversalControlType.getControlType(vCol.getColType());
	                      if (ctrlType == UniversalControlType.CheckListForm)
	                      {
	                         vColCheckListForm = vCol;
	                         break;
	                      }
	                   }
	                   if (vColCheckListForm != null){
	                       comments =   adapter.findTaskFormTemplateListByTemplateFormId(vColCheckListForm.getTaskFormTemplateID());                      
	                   }				      
				   }else{
				      ////////
	                   comments =   adapter.findTaskFormTemplateListByTemplateFormId(currentTask.getTaskFormTemplateID());				      
				   }
				}else{
				   ////////
				   comments =	adapter.findTaskFormTemplateListByTemplateFormId(currentTask.getTaskFormTemplateID());
				}
				if (dataSavedList != null)
				{
					for(TaskFormTemplate formTemplate : comments)
					{
						/*
						 * 
						 */
						ArrayList<TaskFormDataSaved> savedList = new ArrayList<TaskFormDataSaved>();
						
						for(TaskFormDataSaved dataSaved : dataSavedList)
						{
							//if (formTemplate.getTaskControlNo() == dataSaved.getTaskControlNo())
						   if (formTemplate.getTaskFormAttributeID() == dataSaved.getTaskFormAttributeID())
	                            
							{
								formTemplate.setDataSaved(dataSaved);
								break;
							}
						}
						/*
						if (savedList.size() > 1)
						{
							StringBuilder strBld = new StringBuilder();
							for(TaskFormDataSaved item_each : savedList)
							{
								String value = item_each.getTaskDataValues();
								strBld.append(value);
								strBld.append("@@");
							}
							int idx = strBld.toString().lastIndexOf("@@");
							String values = strBld.substring(0, idx-1);
							TaskFormDataSaved lastDataSaved = savedList.get(0);
							lastDataSaved.setTaskDataValues(values);
							formTemplate.setDataSaved(lastDataSaved);
						}else if (savedList.size() == 1){
							formTemplate.setDataSaved(savedList.get(0));
						}*/
					}
				}
				
				if (comments != null)
				{
				 for(TaskFormTemplate formTemplate : comments)
  				 {
					if ((formTemplate.getControlType() == TaskControlType.CheckBoxList) ||
						(formTemplate.getControlType() == TaskControlType.RadioBoxList))
					{
						
						String reasonSentenceType = formTemplate.getReasonSentenceType();
						if ((reasonSentenceType != null)&&(!reasonSentenceType.isEmpty())){
							ArrayList<ReasonSentence> reasons = 
									adapter.getAllReasonSentenceByType(reasonSentenceType);
							String strTexts = "";
							for(ReasonSentence reason : reasons){
								strTexts += reason.getReasonText()+"";
								strTexts += "@@";
							}
							strTexts = strTexts.substring(0, strTexts.length()-1);
							formTemplate.setChoiceTexts(strTexts);
							formTemplate.setChildReasonSentenceList(reasons);
						}
					}
				 }
					
				 ArrayList<TaskFormTemplate> comments_parent = new ArrayList<TaskFormTemplate>();
				 for(TaskFormTemplate comment : comments)
				 {
					 if ((comment.getChildReasonSentenceList() != null)&&
						 (comment.getChildReasonSentenceList().size() > 0)){

						 ArrayList<ReasonSentence> reasons = 
								 comment.getChildReasonSentenceList();
						 
						 for(ReasonSentence reason : reasons)
						 {
							 String path = reason.getReasonSentencePath();
							 if ((path != null)&&(!path.isEmpty()))
							 {
								 for(TaskFormTemplate c_comment : comments)
								 {
									 if ((c_comment.getParentId() != null)&&(!c_comment.getParentId().isEmpty()))
									 {
										 //Log.d("DEBUG_D","Parent ID -> "+ c_comment.getParentId() + " , Path = "+path);
										 if (path.equalsIgnoreCase(c_comment.getParentId()))
										 {
											 /*
											  * 
											  */
											 Log.d("DEBUG_D", "have parent of path = "+path);
											 comment.addChildTaskFormTemplate(c_comment);
										 }
									 }
								 }
							 }
						 }
					 }
				 }
				 for(TaskFormTemplate comment : comments)
				 {
					 if ((comment.getTextQuestion() != null)&&(!comment.getTextQuestion().isEmpty())){
						 comments_parent.add(comment); 
					 }
				 }
				 lv_comments = (ListView)view.findViewById(R.id.lv_tasks_comment);
					
				 Log.d("DEBUG_D", "Task form template id -> "+currentTask.getTaskFormTemplateID());
				 /*
				 if (currentTask.getTaskFormTemplateID() == 1)
				 {
					 TaskCommentAdapter commentAdapter = new TaskCommentAdapter(this.getActivity(),comments);
					 lv_comments.setAdapter(commentAdapter);
					 commentAdapter.notifyDataSetChanged();

				 }else
				 {
					 TaskCommentAdapterV2 commentAdapter = new TaskCommentAdapterV2(this.getActivity(),comments);
					 lv_comments.setAdapter(commentAdapter);
					 commentAdapter.notifyDataSetChanged();
					 
				 }
*/				 
				 TaskCommentAdapterV2 commentAdapter = new TaskCommentAdapterV2(this.getActivity(),comments_parent,jobRequestProduct);
				 lv_comments.setAdapter(commentAdapter);
				 lv_comments.setItemsCanFocus(true);
				 
				 commentAdapter.notifyDataSetChanged();

				 
				 lv_comments.refreshDrawableState();
				 lv_comments.invalidate();
				 lv_comments.postInvalidate();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater,InspectOptMenuType.ENTRY_FORM);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	    if (item.getItemId() == R.id.menu_general_take_photo){
	       Bundle argument = new Bundle();
	         
	       argument.putString(InstanceStateKey.KEY_ARGUMENT_TASK_CODE, this.currentTask.getTaskCode());
	       argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, this.jobRequest);
	       //argument.putInt(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY_ID, this.customerSurveySite.getCustomerSurveySiteID());
	       argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_TAKE_GENERAL_PHOTO, true);

	         int photoSetId = 0;
	         
	         PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
	         try {
	            ArrayList<InspectDataObjectPhotoSaved> photoSavedList = 
	                  dataAdapter.getInspectDataObjectPhotoSavedWithGeneralImage(this.currentTask.getTaskCode());
	            if (photoSavedList != null)
	            {
	               if (photoSavedList.size() > 0)
	               {
	                  photoSetId = photoSavedList.get(0).getPhotoID();
	               }
	            }
	         }
	         catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	         }
	         
	         argument.putInt(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID,photoSetId);
	         ActivityUtil.startNewActivityWithResult(getActivity(),
	               InspectPhotoEntryActivity.class, 
	               argument, 
	               InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY);
	    }else
		if (item.getItemId() == R.id.menu_entry_save)
		{
			Log.d("DEBUG_D", "Insect entry save");
			try {
			   
			    
                final int index = lv_comments.getFirstVisiblePosition();
                View v = lv_comments.getChildAt(0);
                final int top = (v == null) ? 0 : v.getTop();

				int rowEffected = saveAllData();
				
				/*
				 * reset generated inspect report
				 */
		        ReportInspectSummaryStatusHelper.resetReportStatus(getActivity(), currentTask);
		        
				Log.d("DEBUG_D", "Row effected = "+rowEffected);
				if (rowEffected > 0)
				{
	                /*
                    * reload data
                    */
				   
				    /*universal check list each object*/
				    if (getCurrentJobRequestProduct() != null)
				    {
				       SharedPreferenceUtil.setAlreadyCommentSaved(getActivity(), true);
				       getCurrentJobRequestProduct().setHasCheckList(true);
			           Intent data = new Intent();
			           data.putExtra(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST, getCurrentJobRequestProduct());
			           getSherlockActivity().setResult(Activity.RESULT_OK, data);
			           getSherlockActivity().finish();
			           
				    }else{
	                    initialView(currentContentView);
	                    
	                    lv_comments.setSelectionFromTop(index, top);

	                    lv_comments.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener(){

	                     @Override
	                     public void onScrollChanged() {
	                        // TODO Auto-generated method stub
	                      
	                        SharedPreferenceUtil.setAlreadyCommentSaved(getActivity(), true);
	                        
	                        MessageBox.showSaveCompleteMessage(getActivity());
	                      
	                        lv_comments.getViewTreeObserver().removeOnScrollChangedListener(this);
	                     }
	                       
	                    });		       
				    }

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				MessageBox.showMessage(getSherlockActivity(), "Error", e.getMessage());
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	public int saveAllData() throws Exception
	{
		int rowEffected = 0;
		if (lv_comments != null )
		{
			Bundle argument = this.getArguments();
			JobRequest jobRequest =  (JobRequest)argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
			Task currentTask = (Task)argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
		
			
			ArrayList<TaskFormDataSaved> dataSavedList = null;
			if (lv_comments.getCount() > 0)
				dataSavedList  = new ArrayList<TaskFormDataSaved>();
			
			TaskCommentAdapterV2 taskCommentAdapter =  (TaskCommentAdapterV2)lv_comments.getAdapter();
			
			
			View[] viewActivieds = taskCommentAdapter.getActivedViews();
			
			for(int i = 0; i < /*taskCommentAdapter.getActivedViews().length*/taskCommentAdapter.getCount();i++)
			{
				//View v = taskCommentAdapter.getActivedViews()[i];

			    View convertView = null;
			    boolean isShown = true;
			    if (viewActivieds.length > i){
			       convertView = viewActivieds[i];
			    }
			    
			    isShown = (convertView != null);
			    
			    View v = taskCommentAdapter.getView(i, convertView, null);
			    
				if (v != null)
				{
					Object obj = v.getTag();
					if (obj instanceof TaskCommentAdapterV2.TaskCommentHolder)
					{
						TaskCommentAdapterV2.TaskCommentHolder holder = (TaskCommentAdapterV2.TaskCommentHolder)obj;
						if (holder.taskFormTemplate != null)
						{
							TaskFormDataSaved dataSaved = null;
							if ((holder.taskFormTemplate.getControlType() == TaskControlType.SimpleText)||
							    (holder.taskFormTemplate.getControlType() == TaskControlType.SimpleTextDecimal)||
							    (holder.taskFormTemplate.getControlType() == TaskControlType.SimpleTextDate)||
							    (holder.taskFormTemplate.getControlType() == TaskControlType.SimpleTextSingleLine)||
							    (holder.taskFormTemplate.getControlType() == TaskControlType.SimpleTextDecimalSingleLine))
							{
								dataSaved = new TaskFormDataSaved();
								dataSaved.setTaskFormAttributeID(holder.taskFormTemplate.getTaskFormAttributeID());
								dataSaved.setTaskDataValues(holder.simpleTextAnswer);
								dataSaved.setTaskID(currentTask.getTaskID());
								dataSaved.setTaskCode(currentTask.getTaskCode());
								dataSaved.setJobRequestID(jobRequest.getJobRequestID());
								dataSaved.setTaskControlNo(holder.taskFormTemplate.getTaskControlNo());
								dataSaved.setTaskControlType(holder.taskFormTemplate.getControlType());
								dataSaved.setTaskFormTemplateID(holder.taskFormTemplate.getTaskFormTemplateId());
								
								if (jobRequestProduct != null)
                                   dataSaved.setProductRowId(jobRequestProduct.getProductRowID());
								if (site != null)
								   dataSaved.setCustomerSurveySiteID(site.getCustomerSurveySiteID());

								
								dataSavedList.add(dataSaved);
								
							}else if (holder.taskFormTemplate.getControlType() == TaskControlType.Dropdownlist)
							{
								dataSaved = holder.choiceBaseAdapter.getValues();
								if (holder.choiceBaseAdapter.getReasonSentence() != null)
								{
									dataSaved.setReasonSentence(holder.choiceBaseAdapter.getReasonSentence());									
								}
								dataSaved.setTaskFormAttributeID(holder.taskFormTemplate.getTaskFormAttributeID());
								dataSaved.setTaskID(currentTask.getTaskID());
								dataSaved.setTaskCode(currentTask.getTaskCode());
								dataSaved.setJobRequestID(jobRequest.getJobRequestID());
								dataSaved.setTaskControlNo(holder.taskFormTemplate.getTaskControlNo());
								dataSaved.setTaskControlType(holder.taskFormTemplate.getControlType());
								dataSaved.setTaskFormTemplateID(holder.taskFormTemplate.getTaskFormTemplateId());
								dataSaved.setParentID(holder.taskFormTemplate.getParentId());
								
								if (jobRequestProduct != null)
                                   dataSaved.setProductRowId(jobRequestProduct.getProductRowID());
                                if (site != null)
                                   dataSaved.setCustomerSurveySiteID(site.getCustomerSurveySiteID());

								dataSavedList.add(dataSaved);
								
								
							}else
							if ((holder.taskFormTemplate.getControlType() == TaskControlType.CheckBoxList) ||
								(holder.taskFormTemplate.getControlType() == TaskControlType.RadioBoxList))
							{
								ArrayList<ReasonSentence> chooceReasonSentences = 
										holder.choiceBaseAdapter.getChooceReasonSentenceSelected();/*list for checkbox*/

								
								for(ReasonSentence chooiceSentence : chooceReasonSentences)
								{
									dataSaved = holder.choiceBaseAdapter.getValues();	
									dataSaved.setTaskFormAttributeID(holder.taskFormTemplate.getTaskFormAttributeID());
//									dataSaved.setTaskDataValues("1");
									dataSaved.setTaskID(currentTask.getTaskID());
									dataSaved.setTaskCode(currentTask.getTaskCode());
									dataSaved.setJobRequestID(jobRequest.getJobRequestID());
									dataSaved.setTaskControlNo(holder.taskFormTemplate.getTaskControlNo());
									dataSaved.setTaskControlType(holder.taskFormTemplate.getControlType());
									dataSaved.setTaskFormTemplateID(holder.taskFormTemplate.getTaskFormTemplateId());
									dataSaved.setReasonSentence(chooiceSentence);
									
									if (jobRequestProduct != null)
									   dataSaved.setProductRowId(jobRequestProduct.getProductRowID());
		                            if (site != null)
		                               dataSaved.setCustomerSurveySiteID(site.getCustomerSurveySiteID());

									dataSavedList.add(dataSaved);			
									
									/*
									 * 
									 */
									   if (holder.taskFormTemplate != null)
									   {
	                                      if ((holder.taskFormTemplate.getChildTaskFormTemplate() != null)&&
                                                (holder.taskFormTemplate.getChildTaskFormTemplate().size() > 0))
                                        {
                                                for(TaskFormTemplate childTemplate : holder.taskFormTemplate.getChildTaskFormTemplate())
                                                {
                                                    if (childTemplate.getParentId().equalsIgnoreCase(chooiceSentence.getReasonSentencePath()))
                                                    {
                                                        dataSaved = new TaskFormDataSaved();
                                                        dataSaved.setTaskFormAttributeID(childTemplate.getTaskFormAttributeID());
                                                        dataSaved.setTaskID(currentTask.getTaskID());
                                                        dataSaved.setTaskCode(currentTask.getTaskCode());
                                                        dataSaved.setJobRequestID(jobRequest.getJobRequestID());
                                                        dataSaved.setTaskControlNo(childTemplate.getTaskControlNo());
                                                        dataSaved.setTaskControlType(childTemplate.getControlType());
                                                        dataSaved.setTaskFormTemplateID(childTemplate.getTaskFormTemplateId());
                                                        dataSaved.setParentID(childTemplate.getParentId());
                                                        
                                                        if (jobRequestProduct != null)/*not null when show for universal check list each row*/
                                                           dataSaved.setProductRowId(jobRequestProduct.getProductRowID());
                                                        if (site != null)
                                                           dataSaved.setCustomerSurveySiteID(site.getCustomerSurveySiteID());

                                                        
                                                        if (holder.choiceBaseAdapter != null)
                                                        {
                                                           if (isShown)
                                                           {
                                                                 SingleItemAdapter sa = (SingleItemAdapter)holder.choiceBaseAdapter;
                                                                    if (sa.getChildDropDownReasonSetence() != null)
                                                                    {
                                                                       dataSaved.setReasonSentence(sa.getChildDropDownReasonSetence());
                                                                    }
                                                           }else{
                                                              if (childTemplate.getDataSaved() != null){
                                                                 dataSaved.setReasonSentence(
                                                                          childTemplate.getDataSaved().getReasonSentence()
                                                                       );                                                   
                                                             }                                                
                                                           }
                                                        }
                                                       dataSavedList.add(dataSaved); 
                                                       break;
                                                    }
              
                                                }
                                            }								      
									   }
								}
								
							}else{
								dataSaved = holder.choiceBaseAdapter.getValues();								
								dataSaved.setTaskFormAttributeID(holder.taskFormTemplate.getTaskFormAttributeID());
								dataSaved.setTaskID(currentTask.getTaskID());
								dataSaved.setTaskCode(currentTask.getTaskCode());
								dataSaved.setJobRequestID(jobRequest.getJobRequestID());
								dataSaved.setTaskControlNo(holder.taskFormTemplate.getTaskControlNo());
								dataSaved.setTaskControlType(holder.taskFormTemplate.getControlType());
								dataSaved.setTaskFormTemplateID(holder.taskFormTemplate.getTaskFormTemplateId());
								
								if (jobRequestProduct != null)
								   dataSaved.setProductRowId(jobRequestProduct.getProductRowID());
                                if (site != null)
                                   dataSaved.setCustomerSurveySiteID(site.getCustomerSurveySiteID());

								dataSavedList.add(dataSaved);
							}
						}
					}					
				}
			}
			
			if ((dataSavedList != null)&&(dataSavedList.size() > 0))
			{
				PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(getActivity());
				rowEffected = dbAdapter.insertTaskFormDataSaved(dataSavedList,jobRequestProduct);
				
				Log.d("DEBUG_D", "insertTaskFormDataSaved rowEffected = "+rowEffected);
			}else{
				MessageBox.showMessage(getActivity(), getActivity().getString(R.string.text_warning_title), "Don't have data to insert");
			}
		}
		return rowEffected;
	}
	public JobRequestProduct getCurrentJobRequestProduct(){
	   return jobRequestProduct;
	}
}

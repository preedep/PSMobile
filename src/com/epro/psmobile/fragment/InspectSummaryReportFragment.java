package com.epro.psmobile.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.UniversalControlType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectDataSVGResult;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TaskControlTemplate.TaskControlType;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.TaskFormTemplate;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.fragment.InspectSummaryReportFragment.ResultReportGenerate;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class InspectSummaryReportFragment extends ContentViewBaseFragment{

	class ResultReportGenerate{
		public boolean bSuccess;
		public String errorMessage;
		public String html;
	}
	private View currentView;
	private String html;
	private WebView webView;
	private Task currentTask;
	private Team team  = null;
    
	
	public InspectSummaryReportFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.setHasOptionsMenu(true);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		return super.onCreateView(inflater, container, savedInstanceState);
		currentView = inflater.inflate(R.layout.ps_activity_inspect_report, container, false);
		
		initial(currentView);
		
		return currentView;
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater,InspectOptMenuType.ENTRY_FORM);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_entry_save)
		{
			if (this.html != null)
			{
				File root = android.os.Environment.getExternalStorageDirectory();
		        // See
		        // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
		        File dir = new File(root.getAbsolutePath() + CommonValues.REPORT_FOLDER);
		        if (!dir.exists()){
		        	dir.mkdirs();
		        }
		        /*
		         * create file
		         */
		        if (currentTask != null){
			        String fileName = DataUtil.regenerateTaskCodeForMakeFolder(currentTask.getTaskCode());		        	
			        fileName = fileName + ".html";
			        File file = new File(dir, fileName);
			        if (file.exists()){
			        	file.delete();
			        }
			        FileOutputStream f = null;
			        PrintWriter pw = null;
			        try {
			            f = new FileOutputStream(file);
			            pw = new PrintWriter(f);
			            pw.println(this.html);
			            pw.flush();
			            
			            PSBODataAdapter dataAdapter = 
			            		PSBODataAdapter.getDataAdapter(getActivity());
			            dataAdapter.updateTaskIsReportGenerated(currentTask.getTaskCode(),
			            		true,
			            		currentTask.getTaskDuplicatedNo());
			            
						MessageBox.showSaveCompleteMessage(getActivity());				
			        }catch(Exception ex)
			        {
			        	MessageBox.showMessage(getActivity(), "Error", ex.getMessage());
			        }finally{
			        	if (pw != null)
			        	{
			        		pw.close();
			        	}
			        	if (f != null)
			        	{
			        		try {
								f.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	}
			        }
		        }
			}
		}
		return super.onOptionsItemSelected(item);
	}
	class GenerateReportTask extends AsyncTask<Task,Void,ResultReportGenerate>
	{
	   private ProgressDialog progressDialog = null;
	      
		public GenerateReportTask(){
			
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = 
					ProgressDialog.show(getActivity(), "", "Please wait...");
			progressDialog.setCancelable(false);

			PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
			try {
            team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(getActivity()));
			}
			catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
			}
		}


		@Override
		protected void onPostExecute(ResultReportGenerate result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressDialog != null)
			{
				progressDialog.dismiss();
			}
			if (!result.bSuccess)
			{
				MessageBox.showMessage(getActivity(), "Error", result.errorMessage);
			}else{
				 InspectSummaryReportFragment.this.html = result.html;
				 webView.loadData(result.html, 
						 "text/html; charset=utf-8","utf-8");
			}
			
		}

		@Override
		protected ResultReportGenerate doInBackground(Task... params) {
			// TODO Auto-generated method stub
			ResultReportGenerate result = new ResultReportGenerate();
			try {
			   String html = "";
			   int inspectTypeID = params[0].getJobRequest().getInspectType().getInspectTypeID();
			    if (inspectTypeID == 4)/*car*/
			    {
			         if (team != null)
			         {
			            if (team.isQCTeam()){
		                      html = generateHtmlReport(params[0]);			               
			            }else{
		                     html = generateHtmlCarReport(params[0]);			               
			            }
			         }
			    }else if ((inspectTypeID == 3)||(inspectTypeID >= 5)){
			       /*inspect type is universal layout*/
			       html = generateHtmlUniversalReport(params[0]);
			    }
			    else{
			          html = generateHtmlReport(params[0]);
			    }
				result.bSuccess = true;
				result.html = html;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result.bSuccess = false;
				result.errorMessage = e.getMessage();
			}
			return result;
		}
	}
	private void initial(View view){
		 currentTask = getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
		
		if (currentTask != null)
		{
			webView = (WebView)view.findViewById(R.id.webview_inspect_report);
			WebSettings setting = webView.getSettings();
			setting.setBuiltInZoomControls(true);
			setting.setAppCacheMaxSize(1);
			setting.setDefaultTextEncodingName("utf-8");
			
			Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
		    noCacheHeaders.put("Pragma", "no-cache");
		    noCacheHeaders.put("Cache-Control", "no-cache");
		    
		    webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		    webView.setScrollbarFadingEnabled(false);

	
		    GenerateReportTask asyncGenReport = new GenerateReportTask();
		    asyncGenReport.execute(new Task[]{currentTask});
		}
	}
	private String generateHtmlUniversalReport(Task currentTask)
	throws Exception
	{
	   StringBuilder strBld = new StringBuilder();
	   PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
	      
	   strBld.append("<html>");
	    strBld.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
       strBld.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
       strBld.append(" <head>");
       strBld.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
       strBld.append("<title>Untitled Document</title>");
       strBld.append("</head>");
     
       strBld.append("<body>");
       
       strBld.append("<p>"+this.getString(R.string.report_inspection_job_detail_title)+"</p>");
       strBld.append("<p><u>"+this.getString(R.string.report_inspection_customer_detail_title)+"</u></p>");

       ArrayList<InspectDataObjectSaved> inspectSavedList
         = new ArrayList<InspectDataObjectSaved>();
       
       if (currentTask.getJobRequest().getCustomerSurveySiteList() != null){
           CustomerSurveySite siteFirst = currentTask.getJobRequest().getCustomerSurveySiteList().get(0);
           strBld.append("<p>"+siteFirst.getCustomerName()+"</p>");            
           for(CustomerSurveySite site : currentTask.getJobRequest().getCustomerSurveySiteList())
           {
               ArrayList<InspectDataObjectSaved> dataSaved = dataAdapter.getInspectDataObjectSaved(
                       currentTask.getTaskCode(),
                       site.getCustomerSurveySiteID());
               if (dataSaved != null)
               {
                   inspectSavedList.addAll(dataSaved);
               }
               
               strBld.append(site.getSiteAddress()+"<br/>");
               strBld.append(this.getString(R.string.text_tel_prefix)+" "+site.getSiteTels()+"<br/>");

           }
           strBld.append("<p>");
           strBld.append(this.getString(R.string.txt_customer_survey_site_inspect_type,
                   currentTask.getJobRequest().getInspectType().getInspectTypeName()));
           strBld.append("</p>");
           strBld.append("<p>");
           strBld.append(this.getString(R.string.txt_task_code, currentTask.getTaskCode()));
           strBld.append("</p>");
           /*
            *          
            * tvCustomerCode.setText(this.getResources().getString(R.string.text_customer_code_prefix)+" "+jobRequest.getCustomerCode());
              tvBusinessType.setText(this.getResources().getString(R.string.text_customer_business_type,jobRequest.getBusinessType()));
            */
            strBld.append("<p>"+this.getString(R.string.text_customer_code_prefix)+" "
                               +currentTask.getJobRequest().getCustomerCode()+"</p>");
            
            strBld.append("<p>"+this.getString(R.string.text_customer_business_type,
                               currentTask.getJobRequest().getBusinessType()));
            
           strBld.append("<p>"+this.getResources().getString(
                               R.string.text_customer_schedule_preiod_inspect,
                               DataUtil.getInspectPeriod(false, currentTask))+"</p>");


//         strBld.append("<p>"+this.getString(R.string.text_customer_code_prefix)+" "+siteFirst.getCustomerCode()+"</p>");
//         strBld.append(this.getString(R.string.text_customer_business_type,currentTask.getJobRequest().getBusinessType()));
       }

       
       strBld.append("<p><u>"+this.getString(R.string.text_titles_of_job_detail_product)+"</u></p>");
       ArrayList<CarInspectStampLocation> godownCheckInList =
             dataAdapter.getAllCarInspectStampLocation(currentTask.getJobRequest().getJobRequestID(),currentTask.getTaskCode());
       
       InspectJobMapper jobMapper =  dataAdapter.getInspectJobMapper(currentTask.getJobRequest().getJobRequestID(), currentTask.getTaskCode());
       
       ArrayList<InspectFormView> inspectFormViewList = 
             dataAdapter.getInspectFormViewList(jobMapper.getInspectFormViewID());
       
       ArrayList<InspectFormView> reportColumns = new ArrayList<InspectFormView>();
       for(InspectFormView formViewItem : inspectFormViewList){
          UniversalControlType ctrlType = UniversalControlType.getControlType(formViewItem.getColType());
          if ((ctrlType != UniversalControlType.Camera)&&
               (ctrlType != UniversalControlType.CheckListForm)&&
               (ctrlType != UniversalControlType.GodownList)&&
               (ctrlType != UniversalControlType.DeleteRow))
          {
             reportColumns.add(formViewItem);
          }
       }
       float maxWidth = 2048;
       float locWidth = 0;
       float percentPerCol = (maxWidth-locWidth)/reportColumns.size();
       if (godownCheckInList != null){
          for(CarInspectStampLocation godownLoc : godownCheckInList){
             
             if (!currentTask.getTaskCode().equalsIgnoreCase(godownLoc.getTaskCode())){
                continue;
             }

             String locationName = 
                   this.getString(R.string.txt_customer_survey_site_amount,godownLoc.getSiteAddress());
             
             
             ArrayList<JobRequestProduct> jobRequestProductList = 
                   dataAdapter.findUniversalJobRequestProduct(currentTask.getJobRequest().getJobRequestID(),
                         currentTask.getTaskCode(),
                         godownLoc.getCustomerSurveySiteID());
             
             strBld.append("<p>"+locationName+"</p>");
             strBld.append("<table width=\""+maxWidth+"px\" border=\"0\">");
             strBld.append("<tr>");
             //strBld.append("<td width=\""+locWidth+"px\">&nbsp;</td>");
             for(InspectFormView col : reportColumns){
                strBld.append("<td width=\""+percentPerCol+"px\">"+col.getColTextDisplay()+"</td>");
             }
             strBld.append(" </tr>");
             //strBld.append("<tr>");
             //1 is location column
             //strBld.append("<td colspan=\""+(reportColumns.size()+1)+"\"><h1>"+locationName+"</h1></td>");
             //strBld.append(" </tr>");

             if (jobRequestProductList != null)
             {
                for(JobRequestProduct jrp : jobRequestProductList){
                   strBld.append("<tr>");
                   //strBld.append("<td></td>");
                   for(InspectFormView col : reportColumns)
                   {
                      UniversalControlType ctrlType = UniversalControlType.getControlType(col.getColType());
                      if (ctrlType == UniversalControlType.ProductType){
                        strBld.append("<td>"+jrp.getProductGroup()+"</td>");  
                      }else if (ctrlType == UniversalControlType.Product){
                         strBld.append("<td>"+jrp.getProductName()+"</td>");
                      }else if (ctrlType == UniversalControlType.ProductUnit){
                         strBld.append("<td>"+jrp.getProductUnit()+"</td>");
                      }else if (ctrlType == UniversalControlType.DropdownList){
                         /*reason sentence*/
                         int rId = jrp.getcReasonID();
                         String rCode = jrp.getcReasonCode();
                         try{
                            ArrayList<ReasonSentence> rsList = 
                                  dataAdapter.getAllReasonSentenceByType(rCode);
                            if (rsList != null){
                               for(ReasonSentence rs : rsList){
                                  if (rs.getReasonID() == rId){
                                     strBld.append("<td>"+rs.getReasonText()+"</td>");
                                     break;
                                  }
                               }
                            }
                         }catch(Exception ex){}
                      }else if (ctrlType == UniversalControlType.Layout)
                      {
                         int objId = jrp.getInspectDataObjectID();
                         try
                         {
                            ArrayList<InspectDataObjectSaved> objSaveds = 
                                  dataAdapter.getInspectDataObjectSavedUniverals(currentTask.getTaskCode(), jrp.getCustomerSurveySiteID());
                            if (objSaveds != null)
                            {
                               boolean hasObjId = false;
                               for(InspectDataObjectSaved objSave : objSaveds)
                               {
                                  if (objSave.getInspectDataObjectID() == objId){
                                     hasObjId = true;
                                     String name = "";
                                     if ((objSave.getObjectName() != null)&&(!objSave.getObjectName().equalsIgnoreCase("null"))){
                                        name = objSave.getObjectName().trim();
                                     }
                                     strBld.append("<td>"+objSave.getInspectDataObjectID()+","+name+"</td>");
                                     break;
                                  }
                               }
                               if (!hasObjId){
                                  strBld.append("<td></td>");                                  
                               }
                            }else{
                               strBld.append("<td></td>");
                            }
                         }catch(Exception ex){
                            ex.printStackTrace();
                         }
                      }else if (ctrlType == UniversalControlType.MarketPrice)
                      {
                         double marketPrice = jrp.getMarketPrice();
                         try{
                            String value = UniversalListEntryAdapter.setFormat(marketPrice, col);
                            strBld.append("<td>"+value+"</td>");
                         }catch(Exception ex){
                            strBld.append("<td>"+marketPrice+"</td>");
                         }
                      }else
                      {
                         if (col.getColInvokeField() != null)
                         {
                            String[] fields = col.getColInvokeField().split(",");
                            StringBuilder textDisplays = new StringBuilder();
                            for(String f : fields)
                            {
                               Object objValue = UniversalListEntryAdapter.invokeGetValue(jrp,f);
                               if (objValue != null){
                                  textDisplays.append(objValue+"<br/>");
                               }
                            }                            
                            String strValue = (textDisplays.toString().isEmpty())?"":textDisplays.toString();
                            strBld.append("<td>"+strValue+"</td>");
                         }else{
                            strBld.append("<td></td>");
                         }
                      }
                   }
                   strBld.append("</tr>");
                }
             }
             strBld.append("</table>");
             strBld.append("<p></p>");
             strBld.append("<p></p>");
             strBld.append("<p></p>");

          }
       }
       ///////////////////
       
       //strBld.append("<hr/>");

       //////////////////////////
       strBld.append("<p><u>"+this.getString(R.string.report_inspection_comments)+"</u></p>");
       ArrayList<TaskFormDataSaved> taskFormDataSavedList =
               dataAdapter.findTaskFormDataSavedListByJobRequestId(currentTask.getJobRequest().getJobRequestID(),
               currentTask.getTaskCode());

        ArrayList<TaskFormTemplate> comments = dataAdapter.findTaskFormTemplateListByTemplateFormId(currentTask.getTaskFormTemplateID());

        for(TaskFormTemplate formTemplate : comments)
        {
               if ((formTemplate.getControlType() == TaskControlType.CheckBoxList) ||
                   (formTemplate.getControlType() == TaskControlType.RadioBoxList))
               {
                   
                   String reasonSentenceType = formTemplate.getReasonSentenceType();
                   if ((reasonSentenceType != null)&&(!reasonSentenceType.isEmpty())){
                       ArrayList<ReasonSentence> reasons = 
                               dataAdapter.getAllReasonSentenceByType(reasonSentenceType);
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
        
       ///////////////
       for(TaskFormTemplate each_comment : comments_parent)
       {
           strBld.append("<p>"+each_comment.getTextQuestion()+"</p>");
           /*
            * print data saved.
            */
           if (taskFormDataSavedList != null)
           {
               TaskFormDataSaved comment_saved = null;
               for(TaskFormDataSaved taskFormDataSaved : taskFormDataSavedList)
               {
                   if (taskFormDataSaved.getTaskFormAttributeID() == each_comment.getTaskFormAttributeID())
                   {
                       comment_saved = taskFormDataSaved;
                       break;
                   }
               }
               
               if (comment_saved != null)
               {
                   if (!comment_saved.getReasonSentence().getReasonText().equalsIgnoreCase("null"))
                   {
                       if (!comment_saved.getReasonSentence().getReasonText().trim().isEmpty())
                       {
                           strBld.append("<p> - "+comment_saved.getReasonSentence().getReasonText()+"</p>");
                       }
                   }else{
                       if (
                               (comment_saved.getTaskDataValues() != null)&&
                               (!comment_saved.getTaskDataValues().equalsIgnoreCase("null"))
                          )
                               {
                                  if (!comment_saved.getTaskDataValues().trim().isEmpty()){
                                      strBld.append("<p> - "+comment_saved.getTaskDataValues()+"</p>");                                                            
                                  }
                               }
                   }

                   if (each_comment.getControlType() == TaskControlType.RadioBoxList)
                   {
                       if (each_comment.getChildTaskFormTemplate() != null){
                            ArrayList<TaskFormTemplate> each_child_comments = 
                                    each_comment.getChildTaskFormTemplate();
                            
                            comment_saved = null;
                            loopA : for(TaskFormTemplate each_child_comment : each_child_comments)
                            {
                               for(TaskFormDataSaved taskFormDataSaved : taskFormDataSavedList)
                               {
//                                 if (each_child_comment.getTaskControlNo() == taskFormDataSaved.getTaskControlNo())
                                    if (each_child_comment.getTaskFormAttributeID() == taskFormDataSaved.getTaskFormAttributeID())

                                   {
                                       comment_saved = taskFormDataSaved;
                                       break loopA;
                                   }
                               }
                            }
                            if (comment_saved != null)
                            {
                                if (!comment_saved.getReasonSentence().getReasonText().equalsIgnoreCase("null"))
                                   {
                                       if (!comment_saved.getReasonSentence().getReasonText().trim().isEmpty()){
                                           strBld.append("<p><p> - "+comment_saved.getReasonSentence().getReasonText()+"</p></p>");
                                       }
                                   }else{
                                       if (
                                               (comment_saved.getTaskDataValues() != null)&&
                                               (!comment_saved.getTaskDataValues().equalsIgnoreCase("null"))
                                          )
                                               {
                                                  if (!comment_saved.getTaskDataValues().trim().isEmpty()){
                                                      strBld.append("<p><p> - "+comment_saved.getTaskDataValues()+"</p></p>");                                                         
                                                  }
                                               }
                                   }
                            }
                       }
                   }
               }
           }
       }

       //strBld.append("<hr/>");
       /////////////////////
       
       
       strBld.append("<p><u>"+this.getString(R.string.report_inspection_general_photos)+"</u></p>");
       ArrayList<InspectDataObjectPhotoSaved> photoSavedList = 
             dataAdapter.getInspectDataObjectPhotoSavedWithGeneralImage(currentTask.getTaskCode());
       
       int i = 1;
       
       if (photoSavedList != null)
       {
          for(InspectDataObjectPhotoSaved photoSaved : photoSavedList )
          {
             String cameraNo = this.getString(R.string.report_camera_no, (i++)+"");
             strBld.append("<u><p>"+cameraNo+"</u></p>");                        
             String encodeBase64 = 
                     ImageUtil.convertImageToBase64(ImageUtil.getResizedBitmapFromFile(photoSaved.getFileName()));
             if (encodeBase64 != null)
             {
                 strBld.append("<p><img src=\"data:image/jpg;base64,"+encodeBase64+"\" width=\"90%\"/></p>");
                 strBld.append("<p>"+photoSaved.getAngleDetail()+"</p>");
                 strBld.append("<p>"+photoSaved.getComment()+"</p>");
             }
          }
       }
       
       strBld.append("<p></p>");
       strBld.append("<p></p>");
       strBld.append("<p></p>");
       strBld.append("<p></p>");

       strBld.append("<p><u>"+this.getString(R.string.report_inspection_products_photos)+"</u></p>");
       
       if (godownCheckInList != null){
          for(CarInspectStampLocation godownLoc : godownCheckInList)
          {
             if (!godownLoc.getTaskCode().equalsIgnoreCase(currentTask.getTaskCode())){
                continue;
             }
             String locationName = 
                   this.getString(R.string.txt_customer_survey_site_amount,godownLoc.getSiteAddress());
             
             strBld.append(locationName+"<br/>");
             
             
             ArrayList<InspectDataObjectSaved> objSaveds = 
                   dataAdapter.getInspectDataObjectSaved(currentTask.getTaskCode(), godownLoc.getCustomerSurveySiteID());
             
             if (objSaveds != null){
                
                   boolean hasFirstCamera = false;
                   ArrayList<InspectDataItem> dataItems = 
                         dataAdapter.getAllInspectDataItem();
                   
                   for(InspectDataObjectSaved objSaved : objSaveds)
                   {
                       for(InspectDataItem item : dataItems)
                       {
                          if (objSaved.getInspectDataItemID() == item.getInspectDataItemID())
                          {
                             if (item.isCameraObject())
                             {
                                   int photoSetId = objSaved.getPhotoID();
                                   if (!hasFirstCamera){
                                      strBld.append("<p><u>"+this.getString(R.string.report_inspection_general_photos)+"</u></p>");
                                      hasFirstCamera = true;
                                   }
                                   /*
                                    */
                                   photoSavedList =
                                         dataAdapter.getInspectDataObjectPhotoSaved(currentTask.getTaskCode(),
                                               godownLoc.getCustomerSurveySiteID(),
                                               photoSetId);
                             
                                   if (photoSavedList != null)
                                   {
                                      i = 1;
                                      for(InspectDataObjectPhotoSaved photoSaved : photoSavedList )
                                      {
                                          String cameraNo = this.getString(R.string.report_camera_no, (i++)+"");
                                          strBld.append("<u><p>"+cameraNo+"</u></p>");                        
                                          String encodeBase64 = 
                                                  ImageUtil.convertImageToBase64(ImageUtil.getResizedBitmapFromFile(photoSaved.getFileName()));
                                          if (encodeBase64 != null)
                                          {
                                              strBld.append("<p><img src=\"data:image/jpg;base64,"+encodeBase64+"\" width=\"90%\"/></p>");
                                          }
                                          try{
                                              String[] splits = 
                                                      photoSaved.getInspectDataTextSelected().split("\r\n");
                                              StringBuilder s = new StringBuilder();
                                              strBld.append("<p>");
                                              for(String ss : splits)
                                              {
                                                  if (ss.isEmpty())continue;
                                                  
                                                  String result = DataUtil.removePID(ss);
                                                  
                                                  strBld.append(result+"<br/>");
                                              }
                                              strBld.append("</p>");
                                          }catch(Exception e){
                                              strBld.append("<p>"+photoSaved.getInspectDataTextSelected().replace("\r\n", "</br>")+"</p>");                           
                                          }
                                          strBld.append("<p>"+photoSaved.getAngleDetail()+"</p>");
                                          strBld.append("<p>"+photoSaved.getComment()+"</p>");
                                      }                  
                                   }
                                   
                                   ////////////
                             }
                             break;
                          }
                       }
                   }
             }
             
             ArrayList<JobRequestProduct> jobRequestProductList = 
                   dataAdapter.findUniversalJobRequestProduct(currentTask.getJobRequest().getJobRequestID(),
                         currentTask.getTaskCode(),
                         godownLoc.getCustomerSurveySiteID());
             if (jobRequestProductList != null){
                for(JobRequestProduct jrp : jobRequestProductList){
                   int photoSetId = jrp.getPhotoSetID();
                   
                   photoSavedList =
                         dataAdapter.getInspectDataObjectPhotoSaved(photoSetId);
             
                   if (photoSavedList != null)
                   {
                      i = 1;
                      for(InspectDataObjectPhotoSaved photoSaved : photoSavedList )
                      {
                          String cameraNo = this.getString(R.string.report_camera_no, (i++)+"");
                          strBld.append("<u><p>"+cameraNo+"</u></p>");                        
                          String encodeBase64 = 
                                  ImageUtil.convertImageToBase64(ImageUtil.getResizedBitmapFromFile(photoSaved.getFileName()));
                          if (encodeBase64 != null)
                          {
                              strBld.append("<p><img src=\"data:image/jpg;base64,"+encodeBase64+"\" width=\"90%\"/></p>");
                          }
                          try{
                              String[] splits = 
                                      photoSaved.getInspectDataTextSelected().split("\r\n");
                              StringBuilder s = new StringBuilder();
                              strBld.append("<p>");
                              for(String ss : splits)
                              {
                                  if (ss.isEmpty())continue;
                                  
                                  String result = DataUtil.removePID(ss);
                                  
                                  strBld.append(result+"<br/>");
                              }
                              strBld.append("</p>");
                          }catch(Exception e){
                              strBld.append("<p>"+photoSaved.getInspectDataTextSelected().replace("\r\n", "</br>")+"</p>");                           
                          }
                          strBld.append("<p>"+photoSaved.getAngleDetail()+"</p>");
                          strBld.append("<p>"+photoSaved.getComment()+"</p>");
                      }                  
                   }
                }
             }
          }          
       }

       //strBld.append("<hr/>");
       ///////////////////////
       
       strBld.append("<p><u>"+this.getString(R.string.report_inspection_layout_details)+"</u></p>");
       /*
        * layout
        */
       if (godownCheckInList != null){
          for(CarInspectStampLocation godownLoc : godownCheckInList){
             String fileName = DataUtil.regenerateTaskCodeForMakeFolder(currentTask.getTaskCode());
             fileName += "-";
             fileName += godownLoc.getCustomerSurveySiteID();
             String htmlFileName = fileName + ".html";
             
             File directory = Environment.getExternalStorageDirectory();
             String rootFolder = directory.getAbsolutePath();
             rootFolder += CommonValues.PREVIEW_FOLDER;
             rootFolder += "/"+htmlFileName;
             
             File f = new File(rootFolder);
             if (f.exists())
             {
                String locationName = 
                      this.getString(R.string.txt_customer_survey_site_amount,godownLoc.getSiteAddress());
                
                strBld.append(locationName+"<br/>");
              
                StringBuilder contentBuilder = new StringBuilder();
                try {
                    BufferedReader in = new BufferedReader(new FileReader(f));
                    String str;
                    while ((str = in.readLine()) != null) {
                        contentBuilder.append(str);
                    }
                    in.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
                String content = contentBuilder.toString();
                String data = StringUtils.substringBetween(content, "<table>", "</table>");
                strBld.append("<table width=\""+maxWidth+"px\">"+data+"</table>");
                
             }else{
                Log.d("DEBUG_D", htmlFileName+" not found!!!");
             }
          }
       }
       
       
       strBld.append("</body>");
	   strBld.append("</html>");
	   return strBld.toString();
	}
	private String generateHtmlCarReport(Task currentTask)
	throws Exception
	{
	   StringBuilder strBld = new StringBuilder();
	   PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
	   
	   ArrayList<JobRequestProduct> jobRequestProductList = 
	         dataAdapter.findOrderByCOrderJobRequestProductsByJobRequestID(currentTask.getJobRequest().getJobRequestID());
	   
	   strBld.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
	   strBld.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	   strBld.append(" <head>");
	   strBld.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	   strBld.append("<title>Untitled Document</title>");
	   strBld.append("</head>");
	 
	   Calendar cal  = Calendar.getInstance();
	   
	   String strDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
	   
	   ArrayList<CustomerSurveySite> sites = dataAdapter.findCustomerSurveySite(currentTask.getTaskID());
	   
	   String strDealerName = sites.get(0).getCustomerName();
	   String strDealerCode = sites.get(0).getCustomerCode();
	   
	   strBld.append("<body>");
	   strBld.append("<table width=\"100%\" border=\"0\">");
	   strBld.append("<tr>");
	   strBld.append("<td width=\"33%\">&nbsp;</td>");
	   strBld.append("<td width=\"32%\">"+this.getResources().getString(R.string.text_car_inspect_report_title)+"</td>");
	   strBld.append("<td width=\"35%\">&nbsp;</td>");
	   strBld.append("</tr>");
	   strBld.append("<tr>");
	   strBld.append("<td>&nbsp;</td>");
	   strBld.append("	     <td>"+this.getResources().getString(R.string.text_car_inspect_report_date, strDate)+"</td>");
	   strBld.append("	     <td>"+this.getResources().getString(R.string.text_car_inspect_report_task_code, currentTask.getTaskCode())+"</td>");
	   strBld.append("	   </tr>");
	   strBld.append("	   <tr>");
	   strBld.append("	     <td colspan=\"2\">"+this.getResources().getString(R.string.text_car_inspect_report_dealer_name, strDealerName)+"</td>");
	   strBld.append("	     <td>"+this.getResources().getString(R.string.text_car_inspect_report_dealer_code, strDealerCode)+"</td>");
	   strBld.append("	   </tr>");
	   strBld.append("</table>");
	   strBld.append("<br/>");
       strBld.append("<br/>");
	   
	   int carTotal = 0;
	   int sold = 0;
	   int equip = 0;
	   int testDrive = 0;
	   int showOutSide = 0;
	   int noCar  = 0;
	   int transferred = 0;
	   
	   if (jobRequestProductList != null)
	   {
	       JobRequestProduct firstRow = jobRequestProductList.get(0);
	       String cReasonCode = firstRow.getcReasonCode();
	       ArrayList<ReasonSentence> reasonSentences = 
	             
	       dataAdapter.getAllReasonSentenceByType(cReasonCode);
	       /*for(JobRequestProduct jrp : jobRequestProductList){
	          
	       }*/
	       carTotal = jobRequestProductList.size();
	       
	       strBld.append(" <table width=\"100%\" border=\"0\">");
	       strBld.append("<tr>");
	       strBld.append("<td width=\"33%\">"+this.getString(R.string.text_car_inspect_report_summary_title)+"</td>");
	       strBld.append("<td width=\"67%\">"+this.getString(R.string.text_car_inspect_report_summary_total, carTotal+"")+"</td>");
	       strBld.append("</tr>");

	       int countHasCar = 0;
	       for(JobRequestProduct jrp : jobRequestProductList)
	       {
	          if (jrp.getcSight() != null){
	              if (jrp.getcSight().equalsIgnoreCase("Y")){
	                 countHasCar++;
	              }	             
	          }
	       }
           strBld.append("<tr>");
           strBld.append("<td width=\"33%\">&nbsp;</td>");
           strBld.append("<td>"+this.getString(R.string.text_car_inspect_report_summary_found, countHasCar+"")+"</td>");
           strBld.append("</tr>");

           
	       for(ReasonSentence rs : reasonSentences)
	       {
	          int countItem = 0;
	          for(JobRequestProduct jrp : jobRequestProductList){
	             if (jrp.getcReasonID() == rs.getReasonID()){
	                countItem++;
	             }
	          }
	          String strAmount = this.getString(R.string.text_car_inspect_report_summary_amount, countItem +"");
	          strBld.append("<tr>");
	          strBld.append("       <td width=\"33%\">&nbsp;</td>");
	          strBld.append("<td>"+rs.getReasonText()+" "+strAmount+"</td>");
	          strBld.append("</tr>");
	       }
	       
	       /*
	       strBld.append("     <tr>");
	       strBld.append("       <td width=\"33%\">&nbsp;</td>");
	       strBld.append("       <td>"+this.getString(R.string.text_car_inspect_report_summary_sold, sold+"")+"</td>");
	       strBld.append("     </tr>");
	       strBld.append("     <tr>");
	       strBld.append("       <td>&nbsp;</td>");
	       strBld.append("       <td>"+this.getString(R.string.text_car_inspect_report_summary_equip, equip+"")+"</td>");
	       strBld.append("     </tr>");
	       strBld.append("     <tr>");
	       strBld.append("       <td>&nbsp;</td>");
	       strBld.append("       <td>ทดลองขับ : xxx คัน</td>");
	       strBld.append("     </tr>");
	       strBld.append("     <tr>");
	       strBld.append("       <td>&nbsp;</td>");
	       strBld.append("<td>นำไปโชว์นอกสถานที่ : xxx คัน</td>");
	       strBld.append("</tr>");
	       strBld.append("<tr>");
	       strBld.append("<td>&nbsp;</td>");
	       strBld.append("<td>ไม่พบรถยนต์ : xxx คัน</td>");
	       strBld.append("</tr>");
	       strBld.append("<tr>");
	       strBld.append("<td>&nbsp;</td>");
	       strBld.append("<td>โอนระหว่างผู้แทนจำหน่าย : xxx คัน</td>");
	       strBld.append(" </tr>");
	       strBld.append(" <tr>");
	       strBld.append(" <td width=\"33%\">&nbsp;</td>");
	       strBld.append(" <td>อื่นๆ : xxx คัน</td>");
	       strBld.append("</tr>");
	       */
	       strBld.append("</table>");  
	       strBld.append("<br/>");
	       strBld.append("<br/>");

	       String[] colsHeader = this.getActivity().getResources().getStringArray(R.array.text_car_inspect_report_cols_headers);
	       
	       strBld.append("<table width=\"100%\" border=\"1\">");
	       strBld.append("<tr>");
	       strBld.append("<th width=\"10%\" scope=\"col\">"+colsHeader[0]+"</th>");
	       strBld.append("<th width=\"26%\" scope=\"col\">"+colsHeader[1]+"</th>");
	       strBld.append("<th width=\"10%\" scope=\"col\">"+colsHeader[2]+"</th>");
	       strBld.append("<th width=\"15%\" scope=\"col\">"+colsHeader[3]+"</th>");
	       strBld.append("<th width=\"15%\" scope=\"col\">"+colsHeader[4]+"</th>");
	       strBld.append("<th width=\"10%\" scope=\"col\">"+colsHeader[5]+"</th>");
           strBld.append("<th width=\"14%\" scope=\"col\">"+colsHeader[6]+"</th>");
	       strBld.append(" </tr>");
	       int iCount = 0;
	       for(JobRequestProduct jrp : jobRequestProductList)
	       {
	          
	          if (jrp.getcSight().equalsIgnoreCase("Y"))
	             continue;
	          
	          iCount++;
	          strBld.append("<tr>");
	          strBld.append("  <td><div align=\"center\">"+/*(iCount)*/jrp.getcOrder()+"</div></td>");
	          strBld.append("   <td><div align=\"center\">"+jrp.getcMid()+"</div></td>");
	          String payType = this.getString(R.string.text_car_inspect_report_free);
	          
	          String strSoldDate = "";
	          String strPayDate = "";
	          if (jrp.getcSold().equalsIgnoreCase("Y")){
	             payType = this.getString(R.string.text_car_inspect_report_free);//"Sold";
	             strSoldDate = jrp.getcDate();
	          }else if (jrp.getcPay().equalsIgnoreCase("Y")){
	             payType = this.getString(R.string.text_car_inspect_report_free);//"Pay";
	             strPayDate = jrp.getcDate();
	          }
	          strBld.append("   <td><div align=\"center\">"+payType+"</div></td>");
	          strBld.append("   <td><div align=\"center\">"+DataUtil.convertToDisplayCarInspectDateFormat(strSoldDate)+"</div></td>");
              strBld.append("   <td><div align=\"center\">"+DataUtil.convertToDisplayCarInspectDateFormat(strPayDate)+"</div></td>");
	          
	          if (jrp.getcDocument().equalsIgnoreCase("Y")){
                 strBld.append("   <td><div align=\"center\">"+this.getResources().getString(R.string.text_car_inspect_report_summary_has_doc)+"</div></td>");	             
	          }else{
	             strBld.append("   <td><div align=\"center\">"+this.getResources().getString(R.string.text_car_inspect_report_summary_no_has_doc)+"</div></td>");
	          }
	          
	          String reasonText = "";
	          
	          for(ReasonSentence rs : reasonSentences)
	          {
	             if (rs.getReasonID() == jrp.getcReasonID()){
	                reasonText = rs.getReasonText();
	                break;
	             }
	          }
	          strBld.append("   <td><div align=\"center\">"+reasonText+"</div></td>");
	          strBld.append("  </tr>");
	       }
	       strBld.append("  </table>");

           strBld.append("<br/>");
           strBld.append("<br/>");

	       String[] cols_summary = this.getActivity().getResources().getStringArray(R.array.text_car_inspect_report_summary_cols_headers);
	       
	       strBld.append("  <table width=\"100%\" border=\"0\">");
	       strBld.append("    <tr>");
	       strBld.append("      <td>&nbsp;</td>");
	       strBld.append("      <td><div align=\"center\">"+cols_summary[0]+"</div></td>");
	       strBld.append("      <td><div align=\"center\">"+cols_summary[1]+"</div></td>");
	       strBld.append("      <td><div align=\"center\">"+cols_summary[2]+"</div></td>");
	       strBld.append("    </tr>");
	       
	       ArrayList<CarInspectStampLocation> locationStamps = 
	             dataAdapter.getAllCarInspectStampLocation(currentTask.getJobRequest().getJobRequestID(),currentTask.getTaskCode());
	       
	       for(CarInspectStampLocation stmpLoc : locationStamps)
	       {
	          int iCountLoc = 0;
	         /* for(JobRequestProduct jrp : jobRequestProductList)
	          {
	             if (jrp.getcWareHouse() == stmpLoc.getCustomerSurveySiteID()){
	                iCountLoc++;
	             }
	          }
	          */
	          iCountLoc = dataAdapter.getCountHasCar(currentTask.getJobRequest().getJobRequestID(),stmpLoc.getCustomerSurveySiteID());
	          strBld.append("   <tr>");
	          strBld.append("     <td width=\"10%\">&nbsp;</td>");
	          strBld.append("      <td width=\"44%\"><div align=\"left\">"+stmpLoc.getSiteAddress()+"</div></td>");
	          strBld.append("      <td width=\"21%\"><div align=\"center\">"+stmpLoc.getTimeRecorded()+"</div></td>");
	          strBld.append("      <td width=\"25%\"><div align=\"center\">"+iCountLoc+"</div></td>");
	          strBld.append("    </tr>");
	       }
	       
	       strBld.append("   </table>");
	       
	       strBld.append("<br/>");
           strBld.append("<br/>");

	       strBld.append("    <table width=\"100%\" border=\"0\">");
	       strBld.append("      <tr>");
	       strBld.append("       <td><div align=\"center\">"+this.getResources().getString(R.string.text_car_inspect_report_summary_dealer_name)+"</div></td>");
	       strBld.append("    <td><div align=\"center\">"+this.getResources().getString(R.string.text_car_inspect_report_summary_inspector_name)+"</div></td>");
	       strBld.append("      </tr>");
	       strBld.append("       <tr>");
	       strBld.append("   <td height=\"50\"><div align=\"center\">"+this.getResources().getString(R.string.text_car_inspect_report_summary_sign_holder)+"</div></td>");
	       strBld.append("   <td><div align=\"center\">"+this.getResources().getString(R.string.text_car_inspect_report_summary_sign_holder)+"</div></td>");
	       strBld.append("  </tr>");
	       strBld.append("  </table>");
	   }
	   strBld.append("</body>");
	   return strBld.toString();
	}
	private String generateHtmlReport(Task currentTask)
	throws Exception
	{
		PSBODataAdapter dataAdapter = 
				PSBODataAdapter.getDataAdapter(getActivity());

		if (team == null){
		   team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(getActivity()));
		}
		ArrayList<Product> products = dataAdapter.getAllProduct();
		StringBuilder strBld = new StringBuilder();
		strBld.append("<head>");
		strBld.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		strBld.append("<title>Inspection Report</title>");
		strBld.append("</head>");
		strBld.append("<body>");
		strBld.append("<p>");
		strBld.append(""+this.getString(R.string.report_inspection_report_title));
		strBld.append("</p>");
		strBld.append("<hr/>");
		strBld.append("<p>"+this.getString(R.string.report_inspection_job_detail_title)+"</p>");
		strBld.append("<p><u>"+this.getString(R.string.report_inspection_customer_detail_title)+"</u></p>");

		ArrayList<InspectDataObjectSaved> inspectSavedList
		  = new ArrayList<InspectDataObjectSaved>();
		
		if (currentTask.getJobRequest().getCustomerSurveySiteList() != null){
			CustomerSurveySite siteFirst = currentTask.getJobRequest().getCustomerSurveySiteList().get(0);
			strBld.append("<p>"+siteFirst.getCustomerName()+"</p>");			
			for(CustomerSurveySite site : currentTask.getJobRequest().getCustomerSurveySiteList())
			{
				ArrayList<InspectDataObjectSaved> dataSaved = dataAdapter.getInspectDataObjectSaved(
						currentTask.getTaskCode(),
						site.getCustomerSurveySiteID());
				if (dataSaved != null)
				{
					inspectSavedList.addAll(dataSaved);
				}
				
				strBld.append(site.getSiteAddress()+"<br/>");
				strBld.append(this.getString(R.string.text_tel_prefix)+" "+site.getSiteTels()+"<br/>");

			}
			strBld.append("<p>");
			strBld.append(this.getString(R.string.txt_customer_survey_site_inspect_type,
					currentTask.getJobRequest().getInspectType().getInspectTypeName()));
			strBld.append("</p>");
			strBld.append("<p>");
			strBld.append(this.getString(R.string.txt_task_code, currentTask.getTaskCode()));
			strBld.append("</p>");
			/*
			 * 			
			 * tvCustomerCode.setText(this.getResources().getString(R.string.text_customer_code_prefix)+" "+jobRequest.getCustomerCode());
			   tvBusinessType.setText(this.getResources().getString(R.string.text_customer_business_type,jobRequest.getBusinessType()));
			 */
			 strBld.append("<p>"+this.getString(R.string.text_customer_code_prefix)+" "
					 			+currentTask.getJobRequest().getCustomerCode()+"</p>");
			 
			 strBld.append("<p>"+this.getString(R.string.text_customer_business_type,
					 			currentTask.getJobRequest().getBusinessType()));
			 
			strBld.append("<p>"+this.getResources().getString(
								R.string.text_customer_schedule_preiod_inspect,
								DataUtil.getInspectPeriod(false, currentTask))+"</p>");


//			strBld.append("<p>"+this.getString(R.string.text_customer_code_prefix)+" "+siteFirst.getCustomerCode()+"</p>");
//			strBld.append(this.getString(R.string.text_customer_business_type,currentTask.getJobRequest().getBusinessType()));
		}
		//////////////////////////////
		if (!team.isQCTeam()){
		/*
		 * product detail list;
		 */
		strBld.append("<p><u>"+this.getString(R.string.report_inspection_product_details)+"</u></p>");
		
		
				
		ArrayList<InspectDataItem> dataItemList = 
				dataAdapter.getAllInspectDataItem();
		
		////////////
		Hashtable<CustomerSurveySite,ArrayList<InspectDataObjectSaved>> inspectSavedTable 
		   = new Hashtable<CustomerSurveySite,ArrayList<InspectDataObjectSaved>>();
		
		for(CustomerSurveySite site : currentTask.getJobRequest().getCustomerSurveySiteList()){
			for(InspectDataObjectSaved dataSaved : inspectSavedList)
			{
				if (dataSaved.getCustomerSurveySiteID() == site.getCustomerSurveySiteID())
				{
					if (!inspectSavedTable.containsKey(site))
					{
						inspectSavedTable.put(site, new ArrayList<InspectDataObjectSaved>());
					}
					inspectSavedTable.get(site).add(dataSaved);
				}
			}			
		}
		///////////////////////////
		/*
		 * show by site address
		 */
		for(CustomerSurveySite site : inspectSavedTable.keySet())
		{
			ArrayList<InspectDataObjectSaved> dataObjectSavedControlled = new 
					ArrayList<InspectDataObjectSaved>();
			
			ArrayList<InspectDataObjectSaved> dataObjectSavedNotControlled = new 
					ArrayList<InspectDataObjectSaved>();

			strBld.append(getString(R.string.text_prefix_location_inspect_summary_report)+""+site.getSiteAddress()+"</br>");
		
			ArrayList<InspectDataObjectSaved> inspectSavedListPerSite = inspectSavedTable.get(site);
			
			for(InspectDataObjectSaved dataSaved : inspectSavedListPerSite)
			{
				if (dataSaved.isProductControlled())
				{
					InspectDataItem item = null;
					for(InspectDataItem dataItem : dataItemList)
					{
						if (dataItem.getInspectDataItemID() == dataSaved.getInspectDataItemID())
						{
							item = dataItem;
							break;
						}
					}
					if (item != null){
						if (item.isInspectObject())
						{
							dataObjectSavedControlled.add(dataSaved);						
						}
					}
				}else{
					InspectDataItem item = null;
					for(InspectDataItem dataItem : dataItemList)
					{
						if (dataItem.getInspectDataItemID() == dataSaved.getInspectDataItemID())
						{
							item = dataItem;
							break;
						}
					}
					if (item != null){
						if (item.isInspectObject())
						{
							dataObjectSavedNotControlled.add(dataSaved);
						}
					}
				}
			}
			
			if (dataObjectSavedControlled.size() > 0)
			{
				strBld.append("<br/>");
				strBld.append("<p><u>"+this.getString(R.string.report_inspection_product_in_controlled)+"</u></p>");
				strBld.append(
						generateTableProductList(dataObjectSavedControlled,
								products));
				strBld.append("<br/>");
			}
			
			if (dataObjectSavedNotControlled.size() > 0)
			{
				strBld.append("<p><u>"+this.getString(R.string.report_inspection_product_not_controlled)+"</u></p>");
				strBld.append(
						generateTableProductList(dataObjectSavedNotControlled,products
						));
				strBld.append("<br/>");
			}
			
			strBld.append("<br/>");
		}

		
		}
		strBld.append("<hr/>");

		//////////////////////////
		strBld.append("<p>"+this.getString(R.string.report_inspection_comments)+"</p>");
		ArrayList<TaskFormDataSaved> taskFormDataSavedList =
				dataAdapter.findTaskFormDataSavedListByJobRequestId(currentTask.getJobRequest().getJobRequestID(),
				currentTask.getTaskCode());

		 ArrayList<TaskFormTemplate> comments = dataAdapter.findTaskFormTemplateListByTemplateFormId(currentTask.getTaskFormTemplateID());

		 for(TaskFormTemplate formTemplate : comments)
		 {
				if ((formTemplate.getControlType() == TaskControlType.CheckBoxList) ||
					(formTemplate.getControlType() == TaskControlType.RadioBoxList))
				{
					
					String reasonSentenceType = formTemplate.getReasonSentenceType();
					if ((reasonSentenceType != null)&&(!reasonSentenceType.isEmpty())){
						ArrayList<ReasonSentence> reasons = 
								dataAdapter.getAllReasonSentenceByType(reasonSentenceType);
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
		 
		///////////////
		for(TaskFormTemplate each_comment : comments_parent)
		{
			strBld.append("<p>"+each_comment.getTextQuestion()+"</p>");
			/*
			 * print data saved.
			 */
			if (taskFormDataSavedList != null)
			{
				TaskFormDataSaved comment_saved = null;
				for(TaskFormDataSaved taskFormDataSaved : taskFormDataSavedList)
				{
				    if (taskFormDataSaved.getTaskFormAttributeID() == each_comment.getTaskFormAttributeID())
					{
				    	comment_saved = taskFormDataSaved;
						break;
					}
				}
				
				if (comment_saved != null)
				{
					if (!comment_saved.getReasonSentence().getReasonText().equalsIgnoreCase("null"))
					{
						if (!comment_saved.getReasonSentence().getReasonText().trim().isEmpty())
						{
							strBld.append("<p> - "+comment_saved.getReasonSentence().getReasonText()+"</p>");
						}
					}else{
						if (
								(comment_saved.getTaskDataValues() != null)&&
								(!comment_saved.getTaskDataValues().equalsIgnoreCase("null"))
						   )
								{
								   if (!comment_saved.getTaskDataValues().trim().isEmpty()){
									   strBld.append("<p> - "+comment_saved.getTaskDataValues()+"</p>");															
								   }
								}
					}

					if (each_comment.getControlType() == TaskControlType.RadioBoxList)
					{
						if (each_comment.getChildTaskFormTemplate() != null){
							 ArrayList<TaskFormTemplate> each_child_comments = 
									 each_comment.getChildTaskFormTemplate();
							 
							 comment_saved = null;
							 loopA : for(TaskFormTemplate each_child_comment : each_child_comments)
							 {
								for(TaskFormDataSaved taskFormDataSaved : taskFormDataSavedList)
								{
//									if (each_child_comment.getTaskControlNo() == taskFormDataSaved.getTaskControlNo())
	                                 if (each_child_comment.getTaskFormAttributeID() == taskFormDataSaved.getTaskFormAttributeID())

									{
										comment_saved = taskFormDataSaved;
										break loopA;
									}
								}
							 }
							 if (comment_saved != null)
							 {
								 if (!comment_saved.getReasonSentence().getReasonText().equalsIgnoreCase("null"))
									{
										if (!comment_saved.getReasonSentence().getReasonText().trim().isEmpty()){
											strBld.append("<p><p> - "+comment_saved.getReasonSentence().getReasonText()+"</p></p>");
										}
									}else{
										if (
												(comment_saved.getTaskDataValues() != null)&&
												(!comment_saved.getTaskDataValues().equalsIgnoreCase("null"))
										   )
												{
												   if (!comment_saved.getTaskDataValues().trim().isEmpty()){
													   strBld.append("<p><p> - "+comment_saved.getTaskDataValues()+"</p></p>");															
												   }
												}
									}
							 }
						}
					}
				}
			}
		}
		 /*
		if (taskFormDataSavedList != null)
		{
			for(TaskFormDataSaved taskFormDataSaved : taskFormDataSavedList)
			{
				TaskFormTemplate templateItem = null;
				for(TaskFormTemplate template : templates)
				{
					if (taskFormDataSaved.getTaskFormAttributeID() == template.getTaskFormAttributeID())
					{
						templateItem = template;
						break;
					}
				}
				
				if (templateItem != null)
				{
					
					//print question
					 
					strBld.append("<p>"+templateItem.getTextQuestion()+"</p>");
					if (!taskFormDataSaved.getReasonSentence().getReasonText().equalsIgnoreCase("null"))
					{
						strBld.append("<p> - "+taskFormDataSaved.getReasonSentence().getReasonText()+"</p>");
					}else{
						if (
								(taskFormDataSaved.getTaskDataValues() != null)&&
								(!taskFormDataSaved.getTaskDataValues().equalsIgnoreCase("null"))
						   )
								{
									strBld.append("<p> - "+taskFormDataSaved.getTaskDataValues()+"</p>");															
								}
					}
				}
			}
		}
		*/
		
		strBld.append("<hr />");
		////////////////////////
		if (team.isQCTeam())
		{
		   strBld.append("<p>"+this.getString(R.string.report_inspection_general_photos)+"</p>");
		   ArrayList<InspectDataObjectPhotoSaved> photoSavedList = 
                 dataAdapter.getInspectDataObjectPhotoSavedWithGeneralImage(currentTask.getTaskCode());
		   
		   int i = 1;
           
		   if (photoSavedList != null)
		   {
		      for(InspectDataObjectPhotoSaved photoSaved : photoSavedList )
              {
		         String cameraNo = this.getString(R.string.report_camera_no, (i++)+"");
                 strBld.append("<u><p>"+cameraNo+"</u></p>");                        
                 String encodeBase64 = 
                         ImageUtil.convertImageToBase64(ImageUtil.getResizedBitmapFromFile(photoSaved.getFileName()));
                 if (encodeBase64 != null)
                 {
                     strBld.append("<p><img src=\"data:image/jpg;base64,"+encodeBase64+"\" width=\"90%\"/></p>");
                     strBld.append("<p>"+photoSaved.getAngleDetail()+"</p>");
                     strBld.append("<p>"+photoSaved.getComment()+"</p>");
                 }
              }
		   }
		   
		}else{
		strBld.append("<p>"+this.getString(R.string.report_inspection_products_photos)+"</p>");
		if (currentTask.getJobRequest().getCustomerSurveySiteList() != null){
			for(CustomerSurveySite site : currentTask.getJobRequest().getCustomerSurveySiteList())
			{
				strBld.append(site.getSiteAddress()+"<br/>");
				ArrayList<InspectDataObjectSaved> dataObjectSavedList = 
				      dataAdapter.getInspectDataObjectSaved(currentTask.getTaskCode(), site.getCustomerSurveySiteID());
				
				if ((dataObjectSavedList == null) || (dataObjectSavedList.size() == 0))continue;/*if don't have any objects in layout i'll skip*/
				
				ArrayList<InspectDataObjectPhotoSaved> photoSavedList =
						dataAdapter.getInspectDataObjectPhotoSaved(currentTask.getTaskCode(),site.getCustomerSurveySiteID());
				int i = 1;
				if (photoSavedList != null)
				{
					for(InspectDataObjectPhotoSaved photoSaved : photoSavedList )
					{
					    /*
					     * if don't have camera obj in layout
					     * i'll skip not show in report
					     */
					    boolean hasCameraObj = false;
					    for(InspectDataObjectSaved aObjSaved : dataObjectSavedList)
					    {
					       if (photoSaved.getPhotoID() == aObjSaved.getPhotoID()){
					          hasCameraObj = true;
					          break;
					       }
					    }
					    if (!hasCameraObj)continue;
					    
						String cameraNo = this.getString(R.string.report_camera_no, (i++)+"");
						strBld.append("<u><p>"+cameraNo+"</u></p>");						
						String encodeBase64 = 
								ImageUtil.convertImageToBase64(ImageUtil.getResizedBitmapFromFile(photoSaved.getFileName()));
						if (encodeBase64 != null)
						{
							strBld.append("<p><img src=\"data:image/jpg;base64,"+encodeBase64+"\" width=\"90%\"/></p>");
						}
						try{
							String[] splits = 
									photoSaved.getInspectDataTextSelected().split("\r\n");
							StringBuilder s = new StringBuilder();
							strBld.append("<p>");
							for(String ss : splits)
							{
								if (ss.isEmpty())continue;
								
								String result = DataUtil.removePID(ss);
								
								strBld.append(result+"<br/>");
							}
							strBld.append("</p>");
						}catch(Exception e){
							strBld.append("<p>"+photoSaved.getInspectDataTextSelected().replace("\r\n", "</br>")+"</p>");							
						}
						strBld.append("<p>"+photoSaved.getAngleDetail()+"</p>");
						strBld.append("<p>"+photoSaved.getComment()+"</p>");
					}
				}
			}
		  }
		}
	    strBld.append("<hr/>");

		/////////////////////////////////
		if (!team.isQCTeam())
		{
			
		strBld.append("<p>"+this.getString(R.string.report_inspection_layout_details)+"</p>");
		strBld.append("<table width=\"100%\" border=\"0\">");
		if (currentTask.getJobRequest().getCustomerSurveySiteList() != null){
			for(CustomerSurveySite site : currentTask.getJobRequest().getCustomerSurveySiteList())
			{
				strBld.append("<tr><td>"+site.getSiteAddress()+"</td></tr>");
				ArrayList<InspectDataSVGResult> allInspectDataSVGResult = dataAdapter.getAllInspectDataSVGResult();
				if (allInspectDataSVGResult != null)
				{
					InspectDataSVGResult svgResultItem = null;
					for(InspectDataSVGResult svgResult: allInspectDataSVGResult){
						if (svgResult.getCustomerSurveySiteID() == site.getCustomerSurveySiteID())
						{
							if (svgResult.getTaskCode().equalsIgnoreCase(currentTask.getTaskCode())){
								svgResultItem = svgResult;
								break;
							}
						}
					}
					
					if (svgResultItem != null)
					{
						/*
						 * copy photo to upload folder
						 */
						File src = new File(svgResultItem.getSvgResultFullLayout());
						if (src.exists())
						{
							
							String encodeBase64 = 
									ImageUtil.convertImageToBase64(svgResultItem.getSvgResultFullLayout());
							if (encodeBase64 != null)
							{
								strBld.append("<tr>");
								strBld.append(" <td width=\"70%\" align=\"left\" valign=\"top\">");
								strBld.append("<div style=\"height:1200px; width:800px; overflow:auto;\">");
								strBld.append("<img src=\"data:image/jpg;base64,"+encodeBase64+"\" width=\"2651px\" height=\"3750px\"/>");
								strBld.append("</div>");
								strBld.append("</td>");
								strBld.append("<td width=\"30%\" align=\"left\" valign=\"top\">");
								/*
								 * loop of show product list
								 */
								ArrayList<InspectDataObjectSaved> dataSaved = dataAdapter.getInspectDataObjectSaved(
										currentTask.getTaskCode(),
										site.getCustomerSurveySiteID());
								
								Collections.sort(dataSaved, new Comparator<InspectDataObjectSaved>(){

									@Override
									public int compare(
											InspectDataObjectSaved lhs,
											InspectDataObjectSaved rhs) {
										// TODO Auto-generated method stub
										return lhs.getInspectDataObjectID() - rhs.getInspectDataObjectID();
									}
									
								});
								if (dataSaved != null)
								{
									ArrayList<InspectDataItem> dataItems = dataAdapter.getAllInspectDataItem();
									
									for(InspectDataObjectSaved savedItem : dataSaved)
									{
										InspectDataItem dataItem = null;
										for(InspectDataItem item : dataItems)
										{
											if (item.getInspectDataItemID() == savedItem.getInspectDataItemID())
											{
												if (item.isInspectObject())
												{
													dataItem = item;
													continue;
												}
											}
										}
										int productGroupId =  savedItem.getProductGroupID();
									    int productId = savedItem.getProductID();

									    Product p = dataAdapter.findProductByProductGroupIdAndProductId(productGroupId, productId);
									    if ((p != null)&&(dataItem != null))
									    {
									    	strBld.append("<p>Object id : "+savedItem.getInspectDataObjectID()+"</p>");
									    	strBld.append("<p>"+p.getProductName()+"</p>");
									    	//String sizeText = this.getString(R.string.text_html_inspect_preview_size);
							        		//strBld.append("<p>"+sizeText+"</p>");
							        		strBld.append("<p>");
							        		strBld.append(
							        				DataUtil.decimal2digiFormat(savedItem.getWidth() / dataItem.getConvertRatioWidth())+"");
							        		strBld.append(" X ");
							        		strBld.append(DataUtil.decimal2digiFormat(savedItem.getdLong() / dataItem.getConvertRatioDeep())+"");
							        		strBld.append(" X ");
							        		strBld.append(DataUtil.decimal2digiFormat(savedItem.getHeight() / dataItem.getConvertRatioHeight())+"");
							        		strBld.append("</p>");

							        		strBld.append("<p>");
							        		String missCapacity = getActivity().getString(R.string.text_html_inspect_preview_miss_capacity);
						                    strBld.append(missCapacity);
						                    strBld.append(DataUtil.decimal2digiFormat(savedItem.getLost()));
						                    strBld.append("</p>");

						                    strBld.append("<p>");
						                    String overCapcity = getActivity().getString(R.string.text_html_inspect_preview_over_capacity);
						                    strBld.append(overCapcity);
						                    strBld.append(DataUtil.decimal2digiFormat(savedItem.getOver()));
						                    strBld.append("</p>");
							        		
							        		strBld.append("<p>"+DataUtil.decimal2digiFormat(savedItem.getTotal())+" "+savedItem.getProductAmountUnitText()+"</p>");
							        		strBld.append("<p>"+DataUtil.decimal2digiFormat(savedItem.getValue())+" "+this.getString(R.string.report_inspection_baths_unit)+"</p>");

									    }
									}
								}
							  
								strBld.append("</td>");
								strBld.append("</tr>");
							}
						}
					}
				}
			}
		}
		strBld.append("</table>");
		}
		strBld.append("<body>");
		return strBld.toString();
	}
	private String generateTableProductList(ArrayList<InspectDataObjectSaved> inspectSavedList , 
			ArrayList<Product> products)
	{
		StringBuilder strBld = new StringBuilder();
		strBld.append("<table width=\"100%\" border=\"0\">");
		strBld.append("<tr>");
		strBld.append("<td width=\"60%\">"+this.getString(R.string.report_inspection_product_details_product_name)+"</td>");
		strBld.append("<td width=\"20%\">"+this.getString(R.string.report_inspection_product_details_capacity)+"</td>");
	    strBld.append("<td width=\"20%\">"+this.getString(R.string.market_price_label)+"</td>");
		strBld.append("<td width=\"20%\">"+this.getString(R.string.report_inspection_product_details_product_total)+"</td>");
		strBld.append("</tr>");
		

		/*
		 * group by product id
		 * 
		 */
		Hashtable<Integer,InspectDataObjectSaved> mapProduct = 
				new Hashtable<Integer,InspectDataObjectSaved>();
		for(InspectDataObjectSaved dataSaved : inspectSavedList)
		{
			InspectDataObjectSaved tmp_dataSaved = null;
			double total = 0;
			double value = 0;
			if (mapProduct.containsKey(dataSaved.getProductID())){
				tmp_dataSaved = (InspectDataObjectSaved)mapProduct.get(dataSaved.getProductID());
				total = tmp_dataSaved.getTotal();
				value = tmp_dataSaved.getValue();
				
				total += dataSaved.getTotal();
				value += dataSaved.getValue();
				
				tmp_dataSaved.setTotal(total);
				tmp_dataSaved.setValue(value);
			}else{
				mapProduct.put(dataSaved.getProductID(), dataSaved);
			}
		}
		inspectSavedList = new ArrayList<InspectDataObjectSaved>();
		inspectSavedList.addAll(mapProduct.values());
		///////////
		Collections.sort(inspectSavedList, new Comparator<InspectDataObjectSaved>(){

			@Override
			public int compare(InspectDataObjectSaved lhs,
					InspectDataObjectSaved rhs) {
				// TODO Auto-generated method stub
				return lhs.getProductID() - rhs.getProductID();
			}
			
		});
		
		
		
		int iCountItem = 0;
		double totalSummary = 0.0;
		for(int i = 0; i < inspectSavedList.size();i++)
		{
			InspectDataObjectSaved dataSaved = inspectSavedList.get(i);
			int productId  = dataSaved.getProductID();
			Product product = null;
			String productName = "";
			String productCapacity = "";
			for(Product p : products){
				if (p.getProductID() == productId)
				{
					product = p;
					iCountItem++;
					break;
				}
			}
			//String textProduct = "Not available product id = "+productId;
			String totalPrice = "0";
			if (product != null){
				productName = (iCountItem) + " "+product.getProductName();
				productCapacity = " "+ DataUtil.numberFormat(dataSaved.getTotal()/*all capacity*/);
				productCapacity += " "+dataSaved.getProductAmountUnitText();
			}else{
				continue;//skip
			}
			totalPrice = DataUtil.numberFormat(dataSaved.getValue()/*all value*/)+"";
			strBld.append("<tr>");
			strBld.append("<td>"+productName+"</td>");
			strBld.append("<td>"+productCapacity+"</td>");
			strBld.append("<td>"+DataUtil.numberFormat(dataSaved.getMarketPrice())+"</td>");
			strBld.append("<td>"+totalPrice+"</td>");
			
			totalSummary += dataSaved.getValue();
			
			strBld.append("</tr>");
		}
		strBld.append("<tr>");
		strBld.append("<td>"+getActivity().getString(R.string.report_inspection_product_summary_total)+"</td>");
		strBld.append("<td>&nbsp;</td>");
	    strBld.append("<td>&nbsp;</td>");
		strBld.append("<td>"+DataUtil.numberFormat(totalSummary)+"</td>");
		
		strBld.append("</tr>");
		strBld.append("</table>");

		return strBld.toString();
	}
}

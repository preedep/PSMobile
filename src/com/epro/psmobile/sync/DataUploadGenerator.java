package com.epro.psmobile.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import org.json.JSONException;

import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.UniversalControlType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectDataSVGResult;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.MembersInTeamHistory;
import com.epro.psmobile.data.PhotoTeamHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskControlTemplate.TaskControlType;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.TaskFormTemplate;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TeamCheckInHistory;
import com.epro.psmobile.data.TeamCheckInHistory.HistoryType;
import com.epro.psmobile.data.upload.CheckListFormDetail;
import com.epro.psmobile.data.upload.CustomerSurveyOffSite;
import com.epro.psmobile.data.upload.ExpenseTransaction;
import com.epro.psmobile.data.upload.LoginHistory;
import com.epro.psmobile.data.upload.ResultCheckJob;
import com.epro.psmobile.data.upload.ResultCheckJobImage;
import com.epro.psmobile.data.upload.ResultCheckJobProduct;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.AppFolderUtil;
import com.epro.psmobile.util.InspectServiceSupportUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.DataUtil;

import android.content.Context;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

public class DataUploadGenerator {

	@SuppressWarnings("unused")
	private Context context;
	public DataUploadGenerator(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public String generate() throws Exception
	{
		//String zipFileName = "";
	   
	   try{
	      
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);
		
		/*
		 * query for backup
		 */
		ArrayList<JobRequestProduct> jobRequestsInTaskNoFinishs = 
		      dataAdapter.getAllJobRequestProductInTaskNoFinish();
		
		if (jobRequestsInTaskNoFinishs != null){
		   /*
		    * delete in backup
		    */
		   dataAdapter.insertJobRequestProductBackup(jobRequestsInTaskNoFinishs);
		}
		//////////////////
		
		
		/*
		 * delete previous uploaded data
		 * 
		 */
		String folderUpload = 
				Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+CommonValues.UPLOAD_FOLDER;
		String psmobileFolder =
		 		Environment.getExternalStorageDirectory().getAbsoluteFile() +"/"+CommonValues.REPORT_FOLDER;
		String deviceId = SharedPreferenceUtil.getDeviceId(context);
		
		String uploadDataFolder = folderUpload;
		String rootUploadDataFolder = "";
		String rootUploadReportFolder = "";
		
		uploadDataFolder += "/";
		uploadDataFolder += deviceId;
		uploadDataFolder += "/";

		///
		File rootFolderUpdate = new File(uploadDataFolder);
		if (rootFolderUpdate.exists()){
		    Log.d("DEBUG_D", "Delete recursive -> "+rootFolderUpdate.getPath());
			AppFolderUtil.deleteRecursive(rootFolderUpdate);
		}
		rootFolderUpdate.mkdirs();
		////////////////////
		rootUploadDataFolder = new String(uploadDataFolder);
		rootUploadReportFolder = new String(uploadDataFolder);
		uploadDataFolder += CommonValues.UPLOAD_DATA_FOLDER;
		uploadDataFolder += "/";
		File fRoot = new File(uploadDataFolder);
		if (!fRoot.exists())
		{
			fRoot.mkdirs();
		}
		//////////
		String reportFolder = rootUploadReportFolder;
		reportFolder += CommonValues.UPLOAD_REPORT_FOLDER;
		File fReport = new File(reportFolder);
		if (!fReport.exists()){
			fReport.mkdirs();
		}
		
		/*
		 * query all data 
		 */
		ArrayList<Task> allTasksUpload = new ArrayList<Task>();
		
		//ArrayList<Task> allPlanAndTaskUpload = new ArrayList<Task>();
		
		ArrayList<Task> allFinishedTasks = dataAdapter.getAllTasksByStatus(TaskStatus.FINISH,true);
		
		ArrayList<Task> allPlanTasks = dataAdapter.getAllTasks(WhereInStatus.DO_PLAN_TASK,true);
		
		//ArrayList<Task> allPendingCarInspected = dataAdapter.getAllTasksByStatus(TaskStatus.LOCAL_SAVED);
		
		
		//ArrayList<Task> allTodoTasks = dataAdapter.getAllTasks(WhereInStatus.TO_DO_TASK,true);
		
		if (allPlanTasks != null)
		{
			allTasksUpload.addAll(allPlanTasks);
			
			//allPlanAndTaskUpload.addAll(allPlanTasks);
		}
		if (allFinishedTasks != null)
		{
			allTasksUpload.addAll(allFinishedTasks);
			//allTasksUpload.addAll(allTodoTasks);
			
			//allPlanAndTaskUpload.addAll(allTodoTasks);
		}
		/*
		ArrayList<Task> allTaskUploadAndCarPending = new ArrayList<Task>();
		allTaskUploadAndCarPending.addAll(allTasksUpload);
		if (allPendingCarInspected != null){
		   allTaskUploadAndCarPending.addAll(allPendingCarInspected);
		}*/
//	    boolean bWritten = generateJSONFile(context,uploadDataFolder,"task.json",allTaskUploadAndCarPending);
	    ///////////////////////////
	    
	    
		boolean bWritten = generateJSONFile(context,uploadDataFolder,"task.json",allTasksUpload);
//		boolean bWritten = generateJSONFile(context,uploadDataFolder,"task.json",allPlanAndTaskUpload);

		
		ArrayList<TaskFormDataSaved> allCommentsSaved =  dataAdapter.getAllTaskFormDataSaved();
		bWritten = generateJSONFile(context,uploadDataFolder,"taskFormDataSaved.json",allCommentsSaved);
		
		///////////
		ArrayList<Expense> allExpense = dataAdapter.getAllExpense();
		bWritten = generateJSONFile(context,uploadDataFolder,"expense.json",allExpense);
		
		ArrayList<ExpenseTransaction> allExpenseTrans = new ArrayList<ExpenseTransaction>();		
		Team team = dataAdapter.getTeamByDeviceId(deviceId);
		if (allExpense != null){
			for(Expense expense : allExpense)
			{
				ExpenseTransaction expenseTrans = new ExpenseTransaction(this.context,
						expense,
						team);
				expenseTrans.executeAdapter();
				allExpenseTrans.add(expenseTrans);
			}
		}
		bWritten = generateJSONFile(context,uploadDataFolder,"expenseTransaction.json",allExpenseTrans);

		//////////////
		ArrayList<TeamCheckInHistory> allTeamCheckInHistory = dataAdapter.getAllTeamCheckInHistory();
		bWritten = generateJSONFile(context,uploadDataFolder,"teamCheckInHistory.json",allTeamCheckInHistory);
		
		////////////////
		ArrayList<MembersInTeamHistory> allMembersInTeamHistory = dataAdapter.getAllMembersInTeamHistory();
		bWritten = generateJSONFile(context,uploadDataFolder,"membersInTeamHistory.json",allMembersInTeamHistory);
		
		//////////////////
		ArrayList<PhotoTeamHistory> allPhotoTeamHistory = dataAdapter.getAllPhotoTeamHistory();
		Calendar cal = Calendar.getInstance();
		String today = cal.get(Calendar.DAY_OF_MONTH)+""+(cal.get(Calendar.MONTH)+1)+""+cal.get(Calendar.YEAR);
		if (allPhotoTeamHistory != null)
		{
			for(PhotoTeamHistory history : allPhotoTeamHistory)
			{
				ArrayList<TeamCheckInHistory> teamHistoryList = allTeamCheckInHistory;
				TeamCheckInHistory teamCheckInHistory = null;
				for(TeamCheckInHistory teamChk : teamHistoryList)
				{
					if (teamChk.getImgTeamHistoryID() == history.getPhotoTeamCheckInHistoryId())
					{
						teamCheckInHistory = teamChk;
						break;
					}
				}
				/*
				 * copy photo to upload folder
				 */
				File src = new File(history.getPhotoTeamCheckInFileName());
				if (src.exists())
				{
					if (history.getPhotoTeamCheckInFileName().endsWith(".jpg")){
						int lastSlashIdx = history.getPhotoTeamCheckInFileName().lastIndexOf("/");
						String fileName = history.getPhotoTeamCheckInFileName().substring(lastSlashIdx+1,
								history.getPhotoTeamCheckInFileName().length());
						Log.d("DEBUG_D", "File name : "+fileName);
						
						/*
						 * create destination file
						 */
						String destFolder = "";;
//						destFolder += "/";
						destFolder += CommonValues.UPLOAD_IMAGE_TEAM_CHECKIN_FOLDER;
						destFolder += "/";
						destFolder += SharedPreferenceUtil.getTeamID(context);
						destFolder += "/";
						
						if (teamCheckInHistory != null)
						{
							destFolder += teamCheckInHistory.getHistoryType().getCode();
							destFolder += "/";
							
							if (
									(teamCheckInHistory.getTaskCode() != null) &&
									(teamCheckInHistory.getTaskCode().trim().length() > 0)){

								String taskCodeInFormatFolder = 
									DataUtil.regenerateTaskCodeForMakeFolder(teamCheckInHistory.getTaskCode());
								taskCodeInFormatFolder += teamCheckInHistory.getTaskDuplicateNo();
								destFolder += taskCodeInFormatFolder;
								destFolder += "/";
							}
							
							destFolder += today;
							destFolder += "/";
						}
						Log.d("DEBUG_D", "Destination folder - >"+destFolder);
						File folderDest = new File(rootUploadDataFolder+"/"+destFolder);
						if (!folderDest.exists())
						{
							folderDest.mkdirs();
						}
						
						File dest = new File(folderDest.getAbsolutePath()+"/"+fileName);

						AppFolderUtil.copy(src,dest);
						
						history.setPhotoTeamCheckInFileName(DataUtil.replaceSingleBackSlash(destFolder+fileName));
						
						teamCheckInHistory.setTeamCheckInPhotosPath(DataUtil.replaceSingleBackSlash(destFolder+fileName));
					}
				}else{
					Log.d("DEBUG_D", "File "+history.getPhotoTeamCheckInFileName()+" not exist");
				}
			}
		}
		bWritten = generateJSONFile(context,uploadDataFolder,"photoTeamHistory.json",allPhotoTeamHistory);

		/*
		 * generate login history
		 */
		ArrayList<LoginHistory> loginHistoryList = new ArrayList<LoginHistory>();
		if (allTeamCheckInHistory != null)
		{
			for(Task task : allTasksUpload)
			{
				for(TeamCheckInHistory teamChkInHistory : allTeamCheckInHistory)
				{
					if (task.getTaskCode().equalsIgnoreCase(teamChkInHistory.getTaskCode())){
						if (task.getTaskID() == teamChkInHistory.getTaskID())
						{
							if (task.getTaskDuplicatedNo() == teamChkInHistory.getTaskDuplicateNo())
							{
								ArrayList<MembersInTeamHistory> memberList = new
														ArrayList<MembersInTeamHistory>();
								for(MembersInTeamHistory members :  allMembersInTeamHistory){
									if (members.getTeamCheckInHistoryID() == 
											teamChkInHistory.getTeamCheckInHistoryID()){
										
										memberList.add(members);
									}
								}
								
								LoginHistory loginHistory = new
										LoginHistory(
												context,
												task,teamChkInHistory,memberList);
								loginHistory.executeAdapter();
								loginHistoryList.add(loginHistory);
							}
						}
					}
				}
			}			
		}
		

		/*sub check in*/
		///////////////////
		// sub check in history
        ArrayList<CarInspectStampLocation> subLoginHistoryList =
              dataAdapter.getAllCarInspectStampLocation();
        
        ArrayList<LoginHistory> tmp_login_history = new 
              ArrayList<LoginHistory>();
        if (subLoginHistoryList != null)
        {
           
           for(Task task : allTasksUpload)
           {
              //CarInspectStampLocation carInspectLoginHistory = null;
              ArrayList<CarInspectStampLocation> subLoginList = new ArrayList<CarInspectStampLocation>();
              for(CarInspectStampLocation subLogin : subLoginHistoryList)
              {
                 if (task.getTaskCode().equalsIgnoreCase(subLogin.getTaskCode()))
                 {
                       if (task.getTaskID() == subLogin.getTaskID())
                       {
                           if (task.getTaskDuplicatedNo() == subLogin.getTaskDuplicateNo())
                           {
                              subLoginList.add(subLogin);
                           }
                       }
                 }
              }
              for(CarInspectStampLocation carInspectLoginHistory :  subLoginList)
              {
                 //if (carInspectLoginHistory != null)
                 //{
                    for(LoginHistory loginHistory : loginHistoryList)
                    {
                       if (carInspectLoginHistory.getTaskCode().equalsIgnoreCase(loginHistory.getTask().getTaskCode()))
                       {
                          if (carInspectLoginHistory.getTaskID() == loginHistory.getTask().getTaskID()){

                             if (carInspectLoginHistory.getTaskDuplicateNo() == loginHistory.getTask().getTaskDuplicatedNo())
                             {
                                   LoginHistory  subLogin = new LoginHistory(context);
                                   
                                   TeamCheckInHistory teamHistory = 
                                         loginHistory.getTeamCheckInHistory();
                                   
                                   TeamCheckInHistory subTeamCheckInHistory = new TeamCheckInHistory();
                                   subTeamCheckInHistory.setCarLicenseNumber(teamHistory.getCarLicenseNumber());
                                   subTeamCheckInHistory.setHistoryType(HistoryType.START_INSPECT_SUB_CHECKIN);
                                   subTeamCheckInHistory.setTeamStartLatLoc(carInspectLoginHistory.getSiteLat());
                                   subTeamCheckInHistory.setTeamStartLonLoc(carInspectLoginHistory.getSiteLon());
                                   subTeamCheckInHistory.setLicensePlateId(teamHistory.getLicensePlateId());
                                   subTeamCheckInHistory.setImgTeamHistoryID(teamHistory.getImgTeamHistoryID());
                                   subTeamCheckInHistory.setNumberOfMilesAtStartPoint(teamHistory.getNumberOfMilesAtStartPoint());
                                   subTeamCheckInHistory.setNumberOfMilesAtEndPoint(carInspectLoginHistory.getMilesNo());
                                   subTeamCheckInHistory.setLastCheckInDateTime(
                                         DataUtil.convertStringToTimestampYYYYMMDDHHmmss(carInspectLoginHistory.getTimeRecorded()));
                                   subTeamCheckInHistory.setLastCheckOutDateTime(
                                         teamHistory.getLastCheckOutDateTime());
                                   subTeamCheckInHistory.setTeamCheckInPhotosPath(teamHistory.getTeamCheckInPhotosPath());
                                   subTeamCheckInHistory.setTeamID(teamHistory.getTeamID());
                                   
                                   subLogin.setTeamCheckInHistory(subTeamCheckInHistory);
                                   subLogin.setMemberInTeamHistory(loginHistory.getMemberInTeamHistory());
                                   subLogin.setTask(task);
                                   subLogin.setJobLocationId(carInspectLoginHistory.getCustomerSurveySiteID());
                                   subLogin.executeAdapter();
                                   
                                   tmp_login_history.add(subLogin);
                             }
                          }
                       }
                    }
                 //}            
              }

           }
        }
        //////////// end sub check in
        loginHistoryList.addAll(tmp_login_history);
		/*
		 * login start date
		 */
		if (allTeamCheckInHistory != null)
		{
		for(TeamCheckInHistory teamChkInHistory : allTeamCheckInHistory)
		{
			if (teamChkInHistory.getHistoryType() == HistoryType.START_DATE_CHECKIN)
			{
				ArrayList<MembersInTeamHistory> memberList = new
						ArrayList<MembersInTeamHistory>();
						for(MembersInTeamHistory members :  allMembersInTeamHistory)
						{
								if (members.getTeamCheckInHistoryID() == 
											teamChkInHistory.getTeamCheckInHistoryID())
								{
		
										memberList.add(members);
								}
						}

						LoginHistory loginHistory = new
								LoginHistory(context,null,teamChkInHistory,memberList);
						loginHistory.executeAdapter();
						loginHistoryList.add(loginHistory);
			}
		}
		}
		
		
		
		
		bWritten = generateJSONFile(context,
				uploadDataFolder,
				"loginHistory.json",
				loginHistoryList);
		
		////////////////////////
		
		/////////////////
		ArrayList<InspectDataSVGResult> allInspectDataSVGResult = dataAdapter.getAllInspectDataSVGResult();
		if ((allInspectDataSVGResult != null)&&(allFinishedTasks != null))/*fix , if plan and task empty */
		{
		    ArrayList<InspectDataSVGResult> tmpAllInspectDataSVGResult = 
		          new ArrayList<InspectDataSVGResult>();
		   
			for(InspectDataSVGResult svgResult: allInspectDataSVGResult)
			{
			   boolean hasSVGResult = false;
			   for(Task t : allFinishedTasks){
			      if (t.getTaskCode().equalsIgnoreCase(svgResult.getTaskCode())){
			         hasSVGResult = true;
			         break;
			      }
			   }
			   if (!hasSVGResult){
			      continue;
			   }
				/*
				 * copy photo to upload folder
				 */
				File src = new File(svgResult.getSvgResultFullLayout());
				if (src.exists())
				{
					String taskCode = DataUtil.regenerateTaskCodeForMakeFolder(svgResult.getTaskCode());//.replace("/", "-");
					int customerSurveySiteID = svgResult.getCustomerSurveySiteID();
					//int duplicateNo = svgResult.getTaskDuplicateNo();
					
					String newFolder = taskCode+"/"+customerSurveySiteID+"/";
				
					int lastSlashIdx = svgResult.getSvgResultFullLayout().lastIndexOf("/");
					String fileName = svgResult.getSvgResultFullLayout().substring(lastSlashIdx+1,
							svgResult.getSvgResultFullLayout().length());
					
					/*
					 * create destination file
					 */
					String destFolder = rootUploadDataFolder;
					destFolder += "/";
					destFolder += CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
					destFolder += "/";					
					destFolder += newFolder;
					destFolder += "/";
					destFolder += CommonValues.UPLOAD_SVG_RESULT_FOLDER;
					destFolder += "/";
					File folderDest = new File(destFolder);
					if (!folderDest.exists())
					{
						folderDest.mkdirs();
					}
					
					File dest = new File(destFolder+fileName);

					AppFolderUtil.copy(src,dest);
					
					///////////
					String rootPath = CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
					rootPath += "/";
					rootPath += newFolder;
					String relativePath = rootPath;
					relativePath += "/";
					relativePath += CommonValues.UPLOAD_SVG_RESULT_FOLDER;
					relativePath += "/";
					relativePath += fileName;
					
					svgResult.setSvgResultFullLayout(DataUtil.replaceSingleBackSlash(relativePath));
					
					tmpAllInspectDataSVGResult.add(svgResult);
				}
			}
			
			allInspectDataSVGResult = tmpAllInspectDataSVGResult;
		}
		bWritten = generateJSONFile(context,uploadDataFolder,"inspectDataSVGResult.json",allInspectDataSVGResult);

		ArrayList<ResultCheckJob> resultCheckJobList = new ArrayList<ResultCheckJob>();
		
		
		if (allFinishedTasks != null)
		{
		   String defaultResultStatus = 
	              context.getString(R.string.default_result_status);
	  
			for(Task task : allFinishedTasks)
			{
				ArrayList<CustomerSurveySite> customerSurveySites =  dataAdapter.findCustomerSurveySite(
						task.getTaskID()
						);

				if (customerSurveySites == null){
				   customerSurveySites = new ArrayList<CustomerSurveySite>();
				}
				
				 ArrayList<CarInspectStampLocation> carInspectCheckIn = 
	                      dataAdapter.getAllCarInspectStampLocation();
	                if (carInspectCheckIn != null)
	                {
	                   for(CarInspectStampLocation checkIn : carInspectCheckIn)
	                   {
	                      Task taskObj = null;
	                      for(Task t : allFinishedTasks){
	                         if (t.getTaskID() == checkIn.getTaskID()){
	                            if (t.getTaskCode().equalsIgnoreCase(checkIn.getTaskCode())){
	                               taskObj = t;
	                               break;
	                            }
	                         }
	                      }
	                      if (taskObj == null)continue;
	                      
	                      CustomerSurveySite oldSite = null;
	                      for(CustomerSurveySite tmp_oldSite : customerSurveySites){
	                         if (checkIn.getCustomerSurveySiteID() == tmp_oldSite.getCustomerSurveySiteID()){
	                            oldSite = tmp_oldSite;
	                            break;
	                         }
	                      }
	                      
	                      CustomerSurveySite newSite = new CustomerSurveySite();

	                      if (oldSite != null){
	                         newSite = oldSite;
	                      }
	                      else{
	                         newSite.setCustomerSurveySiteID(checkIn.getCustomerSurveySiteID());
	                         newSite.setSiteAddress(checkIn.getSiteAddress());
	                         newSite.setNewAddress(true);
	                      }
                          newSite.setLocationCheckInDate(checkIn.getTimeRecorded());
	                      newSite.setSiteLocLat(checkIn.getSiteLat());
                          newSite.setSiteLocLon(checkIn.getSiteLon());
                          newSite.setMileNo(checkIn.getMilesNo());
                          
                          
                          if (newSite.isNewAddress()){
                             customerSurveySites.add(newSite);
                          }
	                   }
	                }
	                
	                      
				if (customerSurveySites != null)
				{
					for(CustomerSurveySite site : customerSurveySites)
					{
						InspectDataSVGResult dataSvgResult = null;
						ArrayList<InspectDataSVGResult> resultList = allInspectDataSVGResult;
						if (resultList != null)
						{
							for (InspectDataSVGResult resultItem : resultList)
							{
								if (resultItem.getTaskCode().equalsIgnoreCase(task.getTaskCode())){
									if (resultItem.getCustomerSurveySiteID() == site.getCustomerSurveySiteID()){
										dataSvgResult = resultItem;
										break;
									}
								}
							}
						}
						TeamCheckInHistory teamCheckHistory = null;
						ArrayList<TeamCheckInHistory> teamCheckInList =  allTeamCheckInHistory;
						if (allTeamCheckInHistory != null){
							for(TeamCheckInHistory teamHistory : teamCheckInList)
							{	
								if (teamHistory.getTaskCode().equalsIgnoreCase(task.getTaskCode())){
									if (teamHistory.getTaskDuplicateNo() == task.getTaskDuplicatedNo()){
										teamCheckHistory = teamHistory;
										break;
									}
								}
							}
						}
						ArrayList<MembersInTeamHistory> members = null;
						if (teamCheckHistory != null)
						{
							if (allMembersInTeamHistory != null)
							{
								members = new ArrayList<MembersInTeamHistory>();
								
								for(MembersInTeamHistory member : allMembersInTeamHistory)
								{
									if (member.getTeamCheckInHistoryID() == teamCheckHistory.getTeamCheckInHistoryID()){
										members.add(member);
									}
								}
							}
						}
						ResultCheckJob resultCheckJob = new ResultCheckJob(task,
								site,
								dataSvgResult,
								team,
								teamCheckHistory,
								members);
						/*
						 *move  
						 *inspect report file
						 */
						String fileInFormat = 
								DataUtil.regenerateTaskCodeForMakeFolder(task.getTaskCode());
						String html = fileInFormat + ".html";
						String fileHtml = psmobileFolder + html;
						File src = new File(fileHtml);
						if (src.exists())
						{
							/*
							 * copy to upload
							 */
							File dest = new File(fReport.getAbsolutePath()+"/"+html);
							AppFolderUtil.copy(src,dest);
							resultCheckJob.setSummaryReportPathPdf(CommonValues.UPLOAD_REPORT_FOLDER+html);
							
						}
						resultCheckJob.setResultStatus(defaultResultStatus);
						
						
						resultCheckJob.setLocationCompleteCheckDate(site.getLocationCheckInDate());
		                resultCheckJob.setMileNo(site.getMileNo());
		                resultCheckJob.setLocationLatitude(site.getSiteLocLat());
		                resultCheckJob.setLocationLongitude(site.getSiteLocLon());
						
						resultCheckJob.executeAdapter();
						resultCheckJobList.add(resultCheckJob);
					}
				}
			}
			
		                           
                      /*
		              ResultCheckJob resultCheckJob = new ResultCheckJob();
		              resultCheckJob.setJobRowId(checkIn.getTaskID());
		              resultCheckJob.setJobLocationId(checkIn.getCustomerSurveySiteID());
		              resultCheckJob.setJobNo(checkIn.getTaskCode());
		              resultCheckJob.setResultStatus(defaultResultStatus);
		              resultCheckJob.setTeamId(team.getTeamID());
		              resultCheckJob.setLocationLatitude(checkIn.getSiteLat());
		              resultCheckJob.setLocationLongitude(checkIn.getSiteLon());
		              resultCheckJob.setLocationCompleteCheckDate(checkIn.getTimeRecorded());
		              resultCheckJob.setMileNo(checkIn.getMilesNo());
		        
		              resultCheckJob.setTask(taskObj);
		              resultCheckJobList.add(resultCheckJob);*/
		         	
		}

		
		bWritten = generateJSONFile(context,
				uploadDataFolder,
				"resultCheckJob.json",
				resultCheckJobList);
		

		
		ArrayList<InspectDataObjectPhotoSaved> allInspectDataObjectPhotoSaved = 
		      dataAdapter.getAllInspectDataObjectPhotoSaved();	
		
		
		if ((allInspectDataObjectPhotoSaved != null)&&(allFinishedTasks != null))
		{
		    ArrayList<InspectDataObjectPhotoSaved> tmpPhotos = new ArrayList<InspectDataObjectPhotoSaved>();
		    
			for(InspectDataObjectPhotoSaved photoSaved : allInspectDataObjectPhotoSaved)
			{
			   boolean hasPhotoToUpload = false;
			   for(Task t : allFinishedTasks){
			      if (photoSaved.getTaskCode().equalsIgnoreCase(t.getTaskCode())){
			         hasPhotoToUpload = true;
			         break;
			      }
			   }
			   if (!hasPhotoToUpload)
			   {
			      continue;
			   }
			   //if (photoSaved.getTaskCode().equalsIgnoreCase(task.get))
				/*
				 * copy photo to upload folder
				 */
				File src = new File(photoSaved.getFileName());
				
				Log.d("DEBUG_DD", "src file image -> "+photoSaved.getFileName());
				if (src.exists())
				{
				     
					String taskCode = DataUtil.regenerateTaskCodeForMakeFolder(photoSaved.getTaskCode());
					
					int customerSurveySiteID = photoSaved.getCustomerSurveySiteID();
					
					String newFolder = taskCode+"/"+customerSurveySiteID+"/";
				
					int lastSlashIdx = photoSaved.getFileName().lastIndexOf("/");
					String fileName = photoSaved.getFileName().substring(lastSlashIdx+1,
							photoSaved.getFileName().length());
					
					/*
					 * create destination file
					 */
					String destFolder = rootUploadDataFolder;
					destFolder += "/";
					destFolder += CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
					destFolder += "/";					
					destFolder += newFolder;
					destFolder += "/";
					destFolder += CommonValues.UPLOAD_PHOTO_FOLDER;
					destFolder += "/";
					
					File folderDest = new File(destFolder);
					if (!folderDest.exists())
					{
						folderDest.mkdirs();
					}
					
					File dest = new File(destFolder+fileName);

					 float[] latlon = new float[]{0,0};
	                 ExifInterface exifInterface  = new ExifInterface(src.getAbsolutePath());
	                 exifInterface.getLatLong(latlon);
	                 String dateString = exifInterface.getAttribute(/*ExifInterface.TAG_DATETIME*/"UserComment");

	                 Log.d("DEBUG_DD", "src file image -> "+photoSaved.getFileName()+ ", dateString = "+dateString);

	                 
					AppFolderUtil.copy(src,dest);

					//http://stackoverflow.com/questions/5280479/how-to-save-gps-coordinates-in-exif-data-on-android
					

					
					String rootPath = CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
					rootPath += "/";
					rootPath += newFolder;
					String relativePath = rootPath;
					relativePath += "/";
					relativePath += CommonValues.UPLOAD_PHOTO_FOLDER;
					relativePath += "/";
					relativePath += fileName;
					photoSaved.setFileName(DataUtil.replaceSingleBackSlash(relativePath));
					photoSaved.setLatitude(latlon[0]);
					photoSaved.setLongitude(latlon[1]);
					photoSaved.setImageDate(dateString);
					
					
					tmpPhotos.add(photoSaved);
			   }
			}
			
			allInspectDataObjectPhotoSaved = tmpPhotos;/*swap*/
		}
		//bWritten = generateJSONFile(context,uploadDataFolder,"inspectDataObjectPhotoSaved.json",allInspectDataObjectPhotoSaved);

		
		ArrayList<InspectDataObjectSaved> inspectDataObjectSavedFinished
			= new ArrayList<InspectDataObjectSaved>();
		
		
		/*
		ArrayList<InspectDataObjectSaved> allgetAllInspectDataObjectSavedGodownAndBuildings = 
				dataAdapter.findGodownInspectDataObjectSaved();
		
		if (allFinishedTasks != null)
		{
			if (allgetAllInspectDataObjectSavedGodownAndBuildings != null)//godown or building
			{
				for(Task task : allFinishedTasks)
				{
					for(InspectDataObjectSaved dataSaved : allgetAllInspectDataObjectSavedGodownAndBuildings)
					{
						if (dataSaved.getTaskCode().equalsIgnoreCase(task.getTaskCode())){						   
							inspectDataObjectSavedFinished.add(dataSaved);
						}
					}
				}
			}
		}
		
		*/
		
		ArrayList<InspectDataObjectSaved> allgetAllInspectDataObjectSaved = 
              dataAdapter.getAllInspectDataObjectSaved();
      
	   ArrayList<InspectDataItem> dataItems = dataAdapter.getAllInspectDataItem();
		
      if (allFinishedTasks != null)
      {
          if (allgetAllInspectDataObjectSaved != null)//all objects 
          {
              for(Task task : allFinishedTasks)
              {
                  for(InspectDataObjectSaved dataSaved : allgetAllInspectDataObjectSaved)
                  {
                      if (dataSaved.getTaskCode().equalsIgnoreCase(task.getTaskCode()))
                      {
                          /*
                           * get convertRatio 
                           */ 
                          InspectDataItem _inspectDataItem = null;
                          if (dataItems != null)
                          {
                             for(InspectDataItem item : dataItems)
                             {
                                if (item.getInspectDataItemID() == dataSaved.getInspectDataItemID()){
                                   _inspectDataItem = item;
                                   break;
                                }
                             }
                          }
                          if (_inspectDataItem != null)
                          {
                             dataSaved.setRealWidth(dataSaved.getWidth() / _inspectDataItem.getConvertRatioWidth());
                             dataSaved.setRealHeight(dataSaved.getHeight() / _inspectDataItem.getConvertRatioHeight());
                             dataSaved.setRealDeep(dataSaved.getdLong() / _inspectDataItem.getConvertRatioDeep());
                             
                          }
                          inspectDataObjectSavedFinished.add(dataSaved);
                      }
                  }
              }
          }
      }
      ///////////////////////
      
		////////////////////////////////
		bWritten = generateJSONFile(context,uploadDataFolder,"inspectDataObjectSaved.json",inspectDataObjectSavedFinished);

		ArrayList<CheckListFormDetail> checkListFormDetailList = new ArrayList<CheckListFormDetail>();
		ArrayList<ResultCheckJobImage> resultCheckJobImageList = new ArrayList<ResultCheckJobImage>();
		ArrayList<ResultCheckJobProduct> resultCheckJobProductList = new ArrayList<ResultCheckJobProduct>();

		int resultRowId = 0;
		if (allFinishedTasks != null)
		{

			for(Task task : allFinishedTasks)
			{
			   
			   if (team.isQCTeam())
			   {
			      genGeneralPhoto(dataAdapter, task,rootUploadDataFolder,resultCheckJobImageList);
			   }
			   /*
			    *  get inspect type 
			    */
			   int inspectTypeID = task.getJobRequest().getInspectType().getInspectTypeID();
			   /*for car and universal*/
			   if ((inspectTypeID > InspectServiceSupportUtil.SERVICE_FARM_LAND_2)&&(!team.isQCTeam()))
			   {
			      ArrayList<JobRequestProduct> jobRequestProductList = 
			            dataAdapter.findJobRequestProductsByJobRequestID(task.getJobRequest().getJobRequestID());
			      if (jobRequestProductList != null)
			      {
			         for(JobRequestProduct jobRequestProduct : jobRequestProductList)
			         {
			            resultRowId += 1;/*for audit , if not audit using productRowId */
			            ResultCheckJobProduct resultCheckJobProduct = 
                              new ResultCheckJobProduct(task,
                                      null,
                                      resultRowId,
                                      null,
                                      null);
			            
			            
			            for(Method mGet : JobRequestProduct.class.getMethods())
			            {
			               String methodName = mGet.getName();
			               if (methodName.startsWith("get")){
			                  String setMethod = methodName.replace("get", "set");
			                  try{
	                              Method mSetMethod = 
	                                    ResultCheckJobProduct.class.getMethod(setMethod,
	                                          mGet.getReturnType());                              
	                              if (mSetMethod != null){
	                                 Object objValue = mGet.invoke(jobRequestProduct);
	                                 mSetMethod.invoke(resultCheckJobProduct, 
	                                       objValue);
	                              }			                     
			                  }catch(Exception ex){
			                     ex.printStackTrace();
			                  }
			               }
			            }
			            
                        
                        /*
                         * copy to folder image
                         */
                        ArrayList<InspectDataObjectPhotoSaved> photoSavedList = 
                              dataAdapter.getInspectDataObjectPhotoSaved(jobRequestProduct.getPhotoSetID());
                        if (photoSavedList != null)
                        {
                           int iCount = 0;
                           for(InspectDataObjectPhotoSaved photoSaved : photoSavedList)
                           {
                              if (!photoSaved.getTaskCode().equalsIgnoreCase(task.getTaskCode())){
                                 continue;
                              }
                              
                              String fileName = photoSaved.getFileName();
                              String relativePath = "";
                              float[] latlon = new float[]{0,0};
                              String tmp_fileName = "";
                              String dateString = "";
                              
                              
                              try{
                              /*
                               * copy photo to upload folder
                               */
                              File src = new File(fileName);
                              if (src.exists())
                              {
                                  String taskCode = DataUtil.regenerateTaskCodeForMakeFolder(photoSaved.getTaskCode());
                                  
                                  int customerSurveySiteID = photoSaved.getCustomerSurveySiteID();
                                  
                                  String newFolder = taskCode+"/"+customerSurveySiteID+"/";
                              
                                  int lastSlashIdx = fileName.lastIndexOf("/");
                                  tmp_fileName = fileName.substring(lastSlashIdx+1,
                                        fileName.length());
                                  
                                  /*
                                   * create destination file
                                   */
                                  String destFolder = rootUploadDataFolder;
                                  destFolder += "/";
                                  destFolder += CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
                                  destFolder += "/";                  
                                  destFolder += newFolder;
                                  destFolder += "/";
                                  destFolder += CommonValues.UPLOAD_PHOTO_FOLDER;

                                  if (inspectTypeID == InspectServiceSupportUtil.SERVICE_CAR_INSPECT){
                                     destFolder += "/car/";
                                     destFolder += jobRequestProduct.getcVin();
                                     destFolder += "/";
                                  }else{
                                     destFolder += "/universal/"+inspectTypeID+"/"+jobRequestProduct.getProductRowID();
                                  }
                                  
                                  
                                  File folderDest = new File(destFolder);
                                  if (!folderDest.exists())
                                  {
                                      folderDest.mkdirs();
                                  }
                                  
                                  File dest = new File(destFolder+tmp_fileName);

                                  ExifInterface exifInterface  = new ExifInterface(src.getAbsolutePath());
                                  exifInterface.getLatLong(latlon);
                                  
                                  dateString = exifInterface.getAttribute(
                                        /*ExifInterface.TAG_DATETIME*/"UserComment");

                                  AppFolderUtil.copy(src,dest);

                                  String rootPath = CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
                                  rootPath += "/";
                                  rootPath += newFolder;
                                  relativePath = rootPath;
                                  relativePath += "/";
                                  relativePath += CommonValues.UPLOAD_PHOTO_FOLDER;
                                  
                                  if (inspectTypeID == InspectServiceSupportUtil.SERVICE_CAR_INSPECT){
                                     relativePath += "/car/";
                                     relativePath += jobRequestProduct.getcVin();
                                     relativePath += "/";
                                  }else{
                                     //relativePath += "/universal/";
                                     relativePath += "/universal/"+inspectTypeID+"/"+jobRequestProduct.getProductRowID();
                                  }
                                }
                              
                              }catch(Exception ex){
                                 ex.printStackTrace();
                              }
                              
                                  //relativePath += tmp_fileName;
                                  resultCheckJobProduct.setcImage(DataUtil.replaceSingleBackSlash(relativePath));
                                  
                                  /*create fake*/
                                  CustomerSurveySite site = new CustomerSurveySite();
                                  site.setCustomerSurveySiteID(resultCheckJobProduct.getcWareHouse());
                                  site.setCustomerSurveySiteRowID(resultCheckJobProduct.getcWareHouse());
                                  
                                  InspectDataObjectSaved cameraItem = new InspectDataObjectSaved();
                                  cameraItem.setInspectDataObjectID(0);
                                  ///////////
                                  
                                  photoSaved.setFileName(resultCheckJobProduct.getcImage()+""+tmp_fileName);
                                  photoSaved.setLatitude(latlon[0]);
                                  photoSaved.setLongitude(latlon[1]);
                                  photoSaved.setImageDate(dateString);
                                  
                                  
                                  
                                  
                                  ResultCheckJobImage checkJobImage = 
                                        new ResultCheckJobImage(task,
                                                site,
                                                iCount++,
                                                cameraItem,
                                                photoSaved);
                                checkJobImage.setResultProductRowId(resultRowId);
                                checkJobImage.setImageDetailName(photoSaved.getInspectDataTextSelected().trim());
                                checkJobImage.executeAdapter();
                                resultCheckJobImageList.add(checkJobImage);
                           }
                        }
                        ////////////////////
			            resultCheckJobProduct.executeAdapter();/*bind data to object*/
			            resultCheckJobProductList.add(resultCheckJobProduct);
			            ////////////////
			         }
			      }			      
			      
			      /*
			       * general image
			       */
			      genGeneralPhoto(dataAdapter,task, rootUploadDataFolder,resultCheckJobImageList);
			      
			   }
			   //else
			   {
			      /*
			       * inspect type id = 1,2
			       * or QC team
			       */
			      /*
                   * general image
                   */
                  //genGeneralPhoto(dataAdapter,task, rootUploadDataFolder,resultCheckJobImageList);

                  
                  if (task.getJobRequest().getInspectType().getInspectTypeID() <= 2)
                  {
                     resultRowId = 0;
                     ArrayList<CustomerSurveySite> customerSurveySites =  dataAdapter.findCustomerSurveySite(
                           task.getTaskID()
                           );
                   ArrayList<InspectDataItem> inspectDataItemList = 
                           dataAdapter.getAllDataInspectItemsForFarmLand(
                                          task.getJobRequest().getInspectType().getInspectTypeID());//dataAdapter.findInspectDataItemsByGroupId(0,0/*inspect type id , ignored*/);/*0 = find all*/
                   
                   if (customerSurveySites != null)
                   {
                       for(CustomerSurveySite site : customerSurveySites)
                       {
                           /*
                            */
                           InspectDataObjectSaved dataSaved = null;
                           
                           ArrayList<InspectDataObjectSaved> objectSavedList = dataAdapter.getInspectDataObjectSaved(
                                                                                               task.getTaskCode(), 
                                                                                               site.getCustomerSurveySiteID()
                                                                                               );
                           //allgetAllInspectDataObjectSaved;
                           if (objectSavedList != null)
                           {
                               ArrayList<InspectDataObjectSaved> obj_camera_saved_list = 
                                       new ArrayList<InspectDataObjectSaved>();
                               
                             for(InspectDataObjectSaved obj_dataSaved : objectSavedList)
                             {
                               InspectDataItem in_item = null;
                               for(InspectDataItem item : inspectDataItemList)
                               {
                                   if (obj_dataSaved.getInspectDataItemID() == item.getInspectDataItemID())
                                   {
                                       in_item = item;
                                       break;
                                   }                               
                               }
                               if (in_item != null)
                               {
                                   if (in_item.isCameraObject())
                                   {
                                       obj_camera_saved_list.add(obj_dataSaved);
                                   }else if (in_item.isInspectObject())
                                   {
                                       resultRowId += 1;
                                       ResultCheckJobProduct resultCheckJobProduct = 
                                                       new ResultCheckJobProduct(task,
                                                               site,
                                                               resultRowId,
                                                               obj_dataSaved,
                                                               null);
                                       resultCheckJobProduct.setProductRowID(resultRowId);
                                       resultCheckJobProduct.executeAdapter();/*bind data to object*/
                                       resultCheckJobProductList.add(resultCheckJobProduct);
                                   }
                               }
                            }
                             //////////////////
                            for(InspectDataObjectSaved cameraItem : obj_camera_saved_list)
                            {
                                   ArrayList<InspectDataObjectPhotoSaved> objectPhtoSavedList = 
                                           allInspectDataObjectPhotoSaved;
                                   if (objectPhtoSavedList != null)
                                   {
                                       int imageNo = 1;
                                       for(InspectDataObjectPhotoSaved photoSaved : objectPhtoSavedList)
                                       {
                                           if (photoSaved.getPhotoID() == cameraItem.getPhotoID())
                                           {
                                               if (!photoSaved.getTaskCode().equalsIgnoreCase(task.getTaskCode())){
                                                  continue;
                                               }
                                               /////////////
                                               String displaySelected = photoSaved.getInspectDataTextSelected();
                                               String[] productItems = 
                                                       displaySelected.split("\r\n");
                                               for(String productItem : productItems)
                                               {
                                                   if (productItem.trim().isEmpty()) continue;
                                                   
                                                   int idxOfPid = productItem.indexOf("pid:");
                                                   if (idxOfPid >= 0)
                                                   {
                                                       String strSub = productItem.substring(idxOfPid+("pid:".length()), 
                                                               productItem.length());
                                                       int idx = strSub.indexOf(")");
                                                       String strPid = 
                                                               strSub.substring(0, idx);
                                                       
                                                       String[] splitText = strPid.split(",");
                                                       
                                                       int productId = Integer.parseInt(splitText[0]);     
                                                       int objId = Integer.parseInt(splitText[1]);
                                                       
                                                       int rowId = 0;                          
                                                       
                                                       for(ResultCheckJobProduct jobProduct : resultCheckJobProductList){
                                                           if (jobProduct.getProductId() == productId){
                                                               if (jobProduct.getInsectDataObjectSavedID() == objId){
                                                                   rowId = jobProduct.getResultProductRowID();
                                                                   break;
                                                               }
                                                           }
                                                       }
                                                       
                                                       ResultCheckJobImage checkJobImage = 
                                                               new ResultCheckJobImage(task,
                                                                       site,
                                                                       imageNo++,
                                                                       cameraItem,
                                                                       photoSaved);
                                                       checkJobImage.setResultProductRowId(rowId);
                                                       
                                                       try{
                                                           checkJobImage.setImageDetailName(DataUtil.removePID(productItem));
                                                       }catch(Exception ex){
                                                           checkJobImage.setImageDetailName(productItem);
                                                           ex.printStackTrace();
                                                       }
                                                       //checkJobImage.setCameraViewNo(cameraItem.getInspectDataObjectID());
                                                       checkJobImage.executeAdapter();
                                                       resultCheckJobImageList.add(checkJobImage);

                                                       
                                                   }else{
                                                       
                                                       ResultCheckJobImage checkJobImage = 
                                                               new ResultCheckJobImage(task,
                                                                       site,
                                                                       imageNo++,
                                                                       cameraItem,
                                                                       photoSaved);
                                                       checkJobImage.setResultProductRowId(-1);
                                                       checkJobImage.setImageDetailName(productItem);
                                                       checkJobImage.executeAdapter();
                                                       resultCheckJobImageList.add(checkJobImage);
                                                   }
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
	                ArrayList<CustomerSurveySite> customerSurveySites =  dataAdapter.findCustomerSurveySite(
	                        task.getJobRequest().getCustomerCode(),
	                        task.getJobRequest().getJobRequestID()
	                        );*/
			      
			        /*
			         * where productRowId = 0 
			         * 
			         * ignored saved in checklist
			         **/
	                ArrayList<TaskFormDataSaved> taskFormDataSavedList =
	                                dataAdapter.findTaskFormDataSavedListByJobRequestId(task.getJobRequest().getJobRequestID(),
	                                                                                    task.getTaskCode());

	                ArrayList<TaskFormTemplate> taskFormTemplateList = 
	                        dataAdapter.findTaskFormTemplateListByTemplateFormId(task.getTaskFormTemplateID());
	                
	                
	                ArrayList<CheckListFormDetail> chkListDetails = generateCheckListFormDetail(task,taskFormDataSavedList,taskFormTemplateList);
	                if (chkListDetails.size() > 0){
	                   checkListFormDetailList.addAll(chkListDetails);
	                }
	                
	                //////////////
	                /*
	                 * for universal (check list)
	                 */
	                
	                if (task.getJobRequest().getInspectType().getInspectTypeID() > 2)
	                {
	                   ArrayList<JobRequestProduct> jrpList = 
	                          dataAdapter.findUniversalJobRequestProductAllSite(task.getJobRequest().getJobRequestID());

	                    InspectJobMapper jobMapper = dataAdapter.getInspectJobMapper(task.getJobRequest().getJobRequestID(), task.getTaskCode());
	                    
	                    if (jobMapper != null)
	                    {
	                       ArrayList<InspectFormView> inspectFormViews = dataAdapter.getInspectFormViewList(jobMapper.getInspectFormViewID());
	                        
	                        for(InspectFormView formView : inspectFormViews){
	                           UniversalControlType type = 
	                                 UniversalListEntryAdapter.UniversalControlType.getControlType(formView.getColType());   
	                           if (type == UniversalControlType.CheckListForm){
	                              taskFormTemplateList = 
	                                    dataAdapter.findTaskFormTemplateListByTemplateFormId(formView.getTaskFormTemplateID());
	                              break;
	                           }
	                        }
	                        
	                        if (taskFormTemplateList != null)
	                        {
	                            for(JobRequestProduct jrp : jrpList){
	                               
	                               taskFormDataSavedList =  dataAdapter.findUniversalTaskFormDataSavedList(task.getJobRequest().getJobRequestID(),
	                                           task.getJobRequest().getInspectType().getInspectTypeID(), 
	                                           task.getTaskCode(), 
	                                           jrp.getCustomerSurveySiteID(),
	                                           jrp.getProductRowID());

	                               if (taskFormDataSavedList != null){
	                                  chkListDetails = generateCheckListFormDetail(task,taskFormDataSavedList,taskFormTemplateList);
	                                  if (chkListDetails.size() > 0){
	                                     checkListFormDetailList.addAll(chkListDetails);
	                                  }
	                               }
	                            }                      
	                        }                       
	                    }
	                }

	            }
			   //end
			}
		}//end if
		
		
		bWritten = generateJSONFile(context,uploadDataFolder,"resultCheckJobProduct.json",resultCheckJobProductList);
		bWritten = generateJSONFile(context,uploadDataFolder,"resultCheckJobImage.json",resultCheckJobImageList);
		bWritten = generateJSONFile(context,uploadDataFolder,"checklistFormDetail.json",checkListFormDetailList);

		ArrayList<CarInspectStampLocation> customerSiteOfflines = 
		      dataAdapter.getAllCarInspectStampLocationByAddNewSiteOnly();
		
		ArrayList<CustomerSurveyOffSite> customerSurveyOffSiteList = new ArrayList<CustomerSurveyOffSite>();
		
		if ((customerSiteOfflines != null)&&(allFinishedTasks != null))
		{
		   for(CarInspectStampLocation newSite : customerSiteOfflines)
		   {
		      Task taskObj = null;
              for(Task t : allFinishedTasks){
                 if (t.getTaskID() == newSite.getTaskID()){
                    if (t.getTaskCode().equalsIgnoreCase(newSite.getTaskCode())){
                       taskObj = t;
                       break;
                    }
                 }
              }
              if (taskObj == null)continue;
              
              
		      CustomerSurveyOffSite site = new CustomerSurveyOffSite(newSite);
		      customerSurveyOffSiteList.add(site);
		   }
		}
		bWritten = generateJSONFile(context,uploadDataFolder,"customerSurveyOffsite.json",customerSurveyOffSiteList);
        
		////////////
		/*
		 * zip folder
		 */
		//ZipFile zipFile = new ZipFile(filePath);
		//if (zipFile.isEncrypted()) {
		//	String encrypted = AppStateUtil.getPassword(getActivity());//DataUtil.encryptToMD5("progress1234");
		//	zipFile.setPassword(encrypted);
			
		//	zipFile.createZipFileFromFolder(folderToAdd, parameters, splitArchive, splitLength)
		//}
		String zipFileName = folderUpload+"/"+deviceId+".zip";
		File fZip = new File(zipFileName);
		if (fZip.exists())
			fZip.delete();
		
		ZipFile zipFile = new ZipFile(zipFileName);
		ZipParameters params = new ZipParameters();
		zipFile.createZipFileFromFolder(new File(rootUploadDataFolder), params, false, 0);
		return zipFileName;
	   }catch(Exception ex){
	      ex.printStackTrace();
	      
	      throw ex;
	   }
	   
	}
	@SuppressWarnings("unused")
	private  static <T> boolean generateJSONFile(Context context, 
			String uploadDataFolder,
			String fileJsonName,
			ArrayList<T> allTasks) throws JSONException, IOException
	{
		ArrayList<JSONDataHolder> jsonDataHolder = new ArrayList<JSONDataHolder>();
		if (allTasks != null)
			jsonDataHolder.addAll((ArrayList<JSONDataHolder>)allTasks);
		
		boolean bWritten = FilePSMobileJSONWriter.writeJSONArrayToFile(context,
				uploadDataFolder, 
				fileJsonName, 
				jsonDataHolder);
		return bWritten;
	}
	
	private void genGeneralPhoto(PSBODataAdapter dataAdapter,
	      Task task,
	      String rootUploadDataFolder,
	      ArrayList<ResultCheckJobImage> resultCheckJobImageList)
	throws Exception
	{
	   int iCount = 0;
       ArrayList<InspectDataObjectPhotoSaved> photoSavedList = null;
       photoSavedList = dataAdapter.getInspectDataObjectPhotoSavedWithGeneralImage(task.getTaskCode());
       
       try{
       
       
       if (photoSavedList != null){
          ArrayList<InspectDataObjectSaved> objSaveList = 
                dataAdapter.getInspectDataObjectSavedNoSiteID(task.getTaskCode());
       
          if (objSaveList != null){
             for(InspectDataObjectSaved dataObjSaveItem : objSaveList)
             {
                if (dataObjSaveItem.getPhotoID() > 0){
                   ArrayList<InspectDataObjectPhotoSaved> photoSaveds = 
                         dataAdapter.getInspectDataObjectPhotoSaved(dataObjSaveItem.getPhotoID());
                   if (photoSaveds != null){
                      for(InspectDataObjectPhotoSaved item : photoSaveds){
                         item.setInspectDataObjectID(dataObjSaveItem.getInspectDataObjectID());
                         photoSavedList.add(item);
                      }
                   }
                }
             }
          }
       }else{
          ArrayList<InspectDataObjectPhotoSaved> generalInLayouts = 
                dataAdapter.getInspectDataObjectPhotoSavedWithGeneralImageLayout(task.getTaskCode());
          if (generalInLayouts != null){
             photoSavedList =  new ArrayList<InspectDataObjectPhotoSaved>();
             if (generalInLayouts != null)
             {
                ArrayList<InspectDataObjectSaved> objSaveList = 
                      dataAdapter.getInspectDataObjectSavedNoSiteID(task.getTaskCode());
             
                   if (objSaveList != null){
                      for(InspectDataObjectSaved dataObjSaveItem : objSaveList)
                      {
                         if (dataObjSaveItem.getPhotoID() > 0){
                            ArrayList<InspectDataObjectPhotoSaved> photoSaveds = 
                                  dataAdapter.getInspectDataObjectPhotoSaved(dataObjSaveItem.getPhotoID());
                            if (photoSaveds != null)
                            {
                                for(InspectDataObjectPhotoSaved item : photoSaveds){
                                   item.setInspectDataObjectID(dataObjSaveItem.getInspectDataObjectID());
                                   photoSavedList.add(item);
                                }
                            }
                         }
                      }
                   }
                 
               }
          }
       }
       
       
       if (photoSavedList != null)
       {
          
          
          InspectDataObjectSaved cameraItem = new InspectDataObjectSaved();
          for(InspectDataObjectPhotoSaved generalPhoto : photoSavedList)
          {
             
             if (generalPhoto.getInspectDataObjectID() > 0){
                cameraItem.setInspectDataObjectID(generalPhoto.getInspectDataObjectID());
             }else{
                cameraItem.setInspectDataObjectID(0);
             }
             CustomerSurveySite site = new CustomerSurveySite();
             site.setCustomerSurveySiteID(generalPhoto.getCustomerSurveySiteID());
             site.setCustomerSurveySiteRowID(generalPhoto.getCustomerSurveySiteID());
            
             
             /*
              * copy image
              */
             
             String fileName = generalPhoto.getFileName();
             
             /*
              * copy photo to upload folder
              */
             File src = new File(fileName);
             if (src.exists())
             {
                 String taskCode = DataUtil.regenerateTaskCodeForMakeFolder(generalPhoto.getTaskCode());
                 
                 int customerSurveySiteID = generalPhoto.getCustomerSurveySiteID();
                 
                 String newFolder = taskCode+"/"+customerSurveySiteID+"/";
             
                 int lastSlashIdx = fileName.lastIndexOf("/");
                 String tmp_fileName = fileName.substring(lastSlashIdx+1,
                       fileName.length());
                 
                 /*
                  * create destination file
                  */
                 String destFolder = rootUploadDataFolder;
                 destFolder += "/";
                 destFolder += CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
                 destFolder += "/";                  
                 destFolder += newFolder;
                 destFolder += "/";
                 destFolder += CommonValues.UPLOAD_PHOTO_FOLDER;
                 destFolder += "/general/";
                 File folderDest = new File(destFolder);
                 if (!folderDest.exists())
                 {
                     folderDest.mkdirs();
                 }
                 
                 File dest = new File(destFolder+tmp_fileName);

                 float[] latlon = new float[]{0,0};
                 ExifInterface exifInterface  = new ExifInterface(src.getAbsolutePath());
                 exifInterface.getLatLong(latlon);
                 //String dateString = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                 String dateString = exifInterface.getAttribute(/*ExifInterface.TAG_DATETIME*/"UserComment");

                 AppFolderUtil.copy(src,dest);

                 //http://stackoverflow.com/questions/5280479/how-to-save-gps-coordinates-in-exif-data-on-android
                 //float[] latlon = new float[]{0,0};
                 //ExifInterface exifInterface  = new ExifInterface(src.getAbsolutePath());
                 //exifInterface.getLatLong(latlon);
                 //String dateString = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);

                  
                 String rootPath = CommonValues.UPLOAD_IMAGE_INSPECT_FOLDER;
                 rootPath += "/";
                 rootPath += newFolder;
                 String relativePath = rootPath;
                 relativePath += "/";
                 relativePath += CommonValues.UPLOAD_PHOTO_FOLDER;
                 relativePath += "/general/";

                 
                                     
                 generalPhoto.setFileName(DataUtil.replaceSingleBackSlash(relativePath)+""+tmp_fileName);
                 generalPhoto.setLatitude(latlon[0]);
                 generalPhoto.setLongitude(latlon[1]);
                 generalPhoto.setImageDate(dateString);
             }
             //////////
             
             ResultCheckJobImage checkJobImage = 
                   new ResultCheckJobImage(task,
                           site,
                           iCount++,
                           cameraItem,
                           generalPhoto);
           checkJobImage.setResultProductRowId(0);
           checkJobImage.setImageDetailName(generalPhoto.getInspectDataTextSelected().trim());
           checkJobImage.executeAdapter();
           
           resultCheckJobImageList.add(checkJobImage);
           
          }
        }
       }catch(Exception ex){
          ex.printStackTrace();
       }
	}
	
	public ArrayList<CheckListFormDetail> generateCheckListFormDetail(Task task,
	      ArrayList<TaskFormDataSaved> taskFormDataSavedList,
	      ArrayList<TaskFormTemplate> taskFormTemplateList){
	   
	   ArrayList<CheckListFormDetail> checkListFormDetailList = new ArrayList<CheckListFormDetail>();
       if ((taskFormDataSavedList != null)&&(taskFormTemplateList != null))
       {
           for(TaskFormDataSaved taskFormDataSaved : taskFormDataSavedList)
           {
                   @SuppressWarnings("unused")
                   TaskFormTemplate taskFormTemplate = null;
                   for(TaskFormTemplate formTemplate : taskFormTemplateList)
                   {
                       if (taskFormDataSaved.getTaskFormAttributeID() == 
                                       formTemplate.getTaskFormAttributeID()){
                           taskFormTemplate = formTemplate;
                           break;
                       }
                   }
                   
                   if ((taskFormDataSaved.getTaskControlType() == TaskControlType.RadioBoxList)||
                           (taskFormDataSaved.getTaskControlType() == TaskControlType.CheckBoxList)){
                           taskFormDataSaved.setTaskDataValues("1");                                   
                   }else if (taskFormDataSaved.getTaskControlType() == TaskControlType.Dropdownlist){

                       if ((taskFormDataSaved.getParentID() != null)&&
                               (!taskFormDataSaved.getParentID().equalsIgnoreCase("null")))
                       {
                           if (taskFormTemplate != null)
                           {
                               boolean hasParent = false;
                               for (TaskFormDataSaved ts : taskFormDataSavedList)
                               {
                                   if (ts.getReasonSentence().getReasonSentencePath().equalsIgnoreCase(taskFormTemplate.getParentId())){
                                       hasParent = true;
                                       break;
                                   }
                               }                         
                               /*
                               if (!hasParent){
                                   continue;
                               }*/
                               
                           }                                   
                       }
                   }else if ((taskFormDataSaved.getTaskControlType() == TaskControlType.SimpleText)||
                             (taskFormDataSaved.getTaskControlType() == TaskControlType.SimpleTextDate)||
                             (taskFormDataSaved.getTaskControlType() == TaskControlType.SimpleTextDecimal)||
                             (taskFormDataSaved.getTaskControlType() == TaskControlType.SimpleTextSingleLine)||
                             (taskFormDataSaved.getTaskControlType() == TaskControlType.SimpleTextDecimalSingleLine))
                   {
                       if ((taskFormDataSaved.getTaskDataValues() == null)||
                           (taskFormDataSaved.getTaskDataValues().isEmpty())){
                           continue;
                       }
                   }
                   
                   String parentId = "";
                   String path = "";
                   if (taskFormTemplate != null)
                   {
                       parentId = taskFormTemplate.getParentId();
                       path = taskFormTemplate.getPath();
                   }
                   
                   CheckListFormDetail detail = new CheckListFormDetail(
                           task,
                           taskFormDataSaved,
                           parentId,
                           path);
                   
                   if (taskFormTemplate != null)
                   {
                       detail.isQCTeam = taskFormTemplate.isQCTeam();
                   }
                   
                   detail.executeAdapter();
                   checkListFormDetailList.add(detail);
               }
       }
       return checkListFormDetailList;
	}
}

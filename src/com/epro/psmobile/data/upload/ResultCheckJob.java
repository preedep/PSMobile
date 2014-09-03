package com.epro.psmobile.data.upload;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataSVGResult;
import com.epro.psmobile.data.MembersInTeamHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TeamCheckInHistory;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class ResultCheckJob implements JSONDataHolder , UploadDataAdapter {

	private final static String ATT_JOB_ROW_ID = "jobRowId";
	private final static String ATT_JOB_LOCATION_ID = "jobLocationId";
	private final static String ATT_JOB_NO = "jobDocumentNo";
	private final static String ATT_SLA = "sla";
	private final static String ATT_RESULT_STATUS = "resultStatus";
	private final static String ATT_USER_CHECK_RESULT = "userCheckResult";
	private final static String ATT_LOCATION_LAYOUT_IMG ="locationLayoutPathImage";
	private final static String ATT_LOCATION_FULL_LAYOUT_IMG = "locationFullLayoutPathImage";
	private final static String ATT_LOCATION_REPORT_PDF ="summaryReportPathPdf";
	private final static String ATT_COMPLETE_CHECK_DATE ="completeCheckDate";
	private final static String ATT_COMPLETE_CHECK_END_DATE ="completeCheckEndDate";
	private final static String ATT_EMPLOYEE_ID_1 ="employeeId1";
	private final static String ATT_EMPLOYEE_ID_2 ="employeeId2";
	private final static String ATT_EMPLOYEE_ID_3 ="employeeId3";
	private final static String ATT_TEAM_ID ="teamId";
	private final static String ATT_LOCATION_INFO ="locationInfo";
	
	private final static String ATT_LOCATION_LAT = "locationLatitude";
	private final static String ATT_LOCATION_LON = "locationLongitude";
	
	private final static String ATT_CAR_INSPECT_FLAG_SUMMARY = "flagSummary";
	
	private final static String ATT_LOCATION_COMPLETE_CHECK_DATE = "locationCompleteCheckDate";
	private final static String ATT_LOCATION_COMPLETE_CHECK_END_DATE = "locationCompleteCheckEndDate";
	private final static String ATT_MILE_NO = "mileNo";
	
	private int jobRowId;
	private int jobLocationId;
	private String jobNo;
	private int sla;
	private String resultStatus;
	private String userCheckResult;
	private String locationLayoutPathImage;
	private String locationFullLayoutPathImage;
	private String summaryReportPathPdf;
	private String completeCheckDate;
	private String completeCheckEndDate;
	private String employeeId1;
	private String employeeId2;
	private String employeeId3;
	private int teamId;
	private String locationInfo;
	private double locationLatitude = 0.0d;
	private double locationLongitude = 0.0d;
	
	private String flagSummary = "N";/*inspect car*/
	
	private String locationCompleteCheckDate;
	private String locationCompleteCheckEndDate;
	private String mileNo;
	
	
	private Task currentTask;
	private CustomerSurveySite customerSurveySite;
	private Team team;
	private InspectDataSVGResult dataSVGResult;
	private TeamCheckInHistory teamCheckInHistory;
	private ArrayList<MembersInTeamHistory> members;
	public ResultCheckJob(
			Task currentTask,
			CustomerSurveySite customerSurveySite,
			InspectDataSVGResult dataSVGResult,
			Team team,
			TeamCheckInHistory teamCheckInHistory,
			ArrayList<MembersInTeamHistory> members
			) throws Exception {
		// TODO Auto-generated constructor stub
		if (currentTask == null)throw new Exception("Task is null");
		if (customerSurveySite == null)throw new Exception("Customer survey site is null");
		if (team == null)throw new Exception("Team is null");
		//if (dataSVGResult == null)throw new Exception("dataSVGResult is null");
		
		this.currentTask = currentTask;
		this.customerSurveySite = customerSurveySite;
		this.team = team;
		this.dataSVGResult = dataSVGResult;
		this.teamCheckInHistory = teamCheckInHistory;
		this.members = members;
	}
	public ResultCheckJob(){
	   
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject getJSONObject() throws JSONException
	{
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj, ATT_JOB_ROW_ID ,jobRowId);
		JSONDataUtil.put(jsonObj, ATT_JOB_LOCATION_ID ,jobLocationId);
		JSONDataUtil.put(jsonObj, ATT_JOB_NO ,jobNo);
		JSONDataUtil.put(jsonObj, ATT_SLA ,sla);
		JSONDataUtil.put(jsonObj, ATT_RESULT_STATUS ,resultStatus);
		JSONDataUtil.put(jsonObj, ATT_USER_CHECK_RESULT ,userCheckResult);
		JSONDataUtil.put(jsonObj, ATT_LOCATION_LAYOUT_IMG ,locationLayoutPathImage);
		JSONDataUtil.put(jsonObj, ATT_LOCATION_FULL_LAYOUT_IMG, locationFullLayoutPathImage);
		JSONDataUtil.put(jsonObj, ATT_LOCATION_REPORT_PDF ,summaryReportPathPdf);
		JSONDataUtil.put(jsonObj, ATT_COMPLETE_CHECK_DATE ,completeCheckDate);
		JSONDataUtil.put(jsonObj, ATT_COMPLETE_CHECK_END_DATE ,completeCheckEndDate);
		JSONDataUtil.put(jsonObj, ATT_EMPLOYEE_ID_1,employeeId1);
		JSONDataUtil.put(jsonObj, ATT_EMPLOYEE_ID_2 ,employeeId2);
		JSONDataUtil.put(jsonObj, ATT_EMPLOYEE_ID_3 ,employeeId3);
		JSONDataUtil.put(jsonObj, ATT_TEAM_ID ,teamId);
		JSONDataUtil.put(jsonObj, ATT_LOCATION_INFO ,locationInfo);
		JSONDataUtil.put(jsonObj, ATT_LOCATION_LAT, this.locationLatitude);
		JSONDataUtil.put(jsonObj, ATT_LOCATION_LON, this.locationLongitude);
		
		if (this.currentTask != null){
		   JSONDataUtil.put(jsonObj, ATT_CAR_INSPECT_FLAG_SUMMARY, this.currentTask.getFlagSummaryCarInspect());
		}
		
		JSONDataUtil.put(jsonObj, ATT_LOCATION_COMPLETE_CHECK_DATE, this.locationCompleteCheckDate);
		JSONDataUtil.put(jsonObj, ATT_LOCATION_COMPLETE_CHECK_END_DATE, this.locationCompleteCheckEndDate);
		JSONDataUtil.put(jsonObj, ATT_MILE_NO, this.mileNo);
		return jsonObj;
	}

	public void setTask(Task task)
	{
	   this.currentTask = task;
	}
	@Override
	public void executeAdapter() {
		// TODO Auto-generated method stub
		this.jobRowId = this.currentTask.getTaskID();
		this.jobLocationId = this.customerSurveySite.getCustomerSurveySiteRowID();
		if (this.jobLocationId == 0){
		   this.jobLocationId = this.customerSurveySite.getCustomerSurveySiteID();
		}
		this.jobNo = this.currentTask.getTaskCode();
		if (this.teamCheckInHistory != null)
		{
			this.completeCheckDate = this.teamCheckInHistory.getLastCheckInDateTime().toString();
			if (this.teamCheckInHistory.getLastCheckOutDateTime() != null)
			{
				this.completeCheckEndDate = this.teamCheckInHistory.getLastCheckOutDateTime().toString();
			}
		}
		
		this.locationLayoutPathImage = (this.dataSVGResult == null)?"":this.dataSVGResult.getSvgResultLayoutOnly();
		this.locationFullLayoutPathImage = (this.dataSVGResult == null)?"":this.dataSVGResult.getSvgResultFullLayout();
		//this.summaryReportPathPdf = this.customerSurveySite.getResultPdfPath();
		
		if (locationLatitude <= 0)
		this.locationLatitude = (this.dataSVGResult == null)?0:this.dataSVGResult.getLocationLatitude();
		
		if (locationLongitude <= 0)
		this.locationLongitude = (this.dataSVGResult == null)?0:this.dataSVGResult.getLocationLongitude();
		
		if (this.members != null)
		{
			try{
				this.employeeId1 = members.get(0).getEmployeeCode();
			}catch(Exception ex){}
			try{
				this.employeeId2 = members.get(1).getEmployeeCode();
			}catch(Exception ex){}
			try{
				this.employeeId3 = members.get(2).getEmployeeCode();				
			}catch(Exception ex){}
		}
		this.teamId = this.team.getTeamID();
		this.locationInfo = this.currentTask.getRemark();
	}




	public String getLocationLayoutPathImage() {
		return locationLayoutPathImage;
	}

	public void setLocationLayoutPathImage(String locationLayoutPathImage) {
		this.locationLayoutPathImage = locationLayoutPathImage;
	}

	public String getLocationFullLayoutPathImage() {
		return locationFullLayoutPathImage;
	}

	public void setLocationFullLayoutPathImage(
			String locationFullLayoutPathImage) {
		this.locationFullLayoutPathImage = locationFullLayoutPathImage;
	}

	public String getSummaryReportPathPdf() {
		return summaryReportPathPdf;
	}

	public void setSummaryReportPathPdf(String summaryReportPathPdf) {
		this.summaryReportPathPdf = summaryReportPathPdf;
	}

	public String getCompleteCheckDate() {
		return completeCheckDate;
	}

	public void setCompleteCheckDate(String completeCheckDate) {
		this.completeCheckDate = completeCheckDate;
	}

	public String getCompleteCheckEndDate() {
		return completeCheckEndDate;
	}

	public void setCompleteCheckEndDate(String completeCheckEndDate) {
		this.completeCheckEndDate = completeCheckEndDate;
	}

	public String getEmployeeId1() {
		return employeeId1;
	}

	public void setEmployeeId1(String employeeId1) {
		this.employeeId1 = employeeId1;
	}

	public String getEmployeeId2() {
		return employeeId2;
	}

	public void setEmployeeId2(String employeeId2) {
		this.employeeId2 = employeeId2;
	}

	public String getEmployeeId3() {
		return employeeId3;
	}

	public void setEmployeeId3(String employeeId3) {
		this.employeeId3 = employeeId3;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public int getJobRowId() {
		return jobRowId;
	}

	public void setJobRowId(int jobRowId) {
		this.jobRowId = jobRowId;
	}

	public int getJobLocationId() {
		return jobLocationId;
	}

	public void setJobLocationId(int jobLocationId) {
		this.jobLocationId = jobLocationId;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public int getSla() {
		return sla;
	}

	public void setSla(int sla) {
		this.sla = sla;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getUserCheckResult() {
		return userCheckResult;
	}

	public void setUserCheckResult(String userCheckResult) {
		this.userCheckResult = userCheckResult;
	}

	public double getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(double locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public double getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(double locationLongitude) {
		this.locationLongitude = locationLongitude;
	}

   public String getFlagSummary() {
      return flagSummary;
   }

   public void setFlagSummary(String flagSummary) {
      this.flagSummary = flagSummary;
   }
   public String getLocationCompleteCheckDate() {
      return locationCompleteCheckDate;
   }
   public void setLocationCompleteCheckDate(String locationCompleteCheckDate) {
      this.locationCompleteCheckDate = locationCompleteCheckDate;
   }
   public String getLocationCompleteCheckEndDate() {
      return locationCompleteCheckEndDate;
   }
   public void setLocationCompleteCheckEndDate(String locationCompleteCheckEndDate) {
      this.locationCompleteCheckEndDate = locationCompleteCheckEndDate;
   }
   public String getMileNo() {
      return mileNo;
   }
   public void setMileNo(String mileNo) {
      this.mileNo = mileNo;
   }

}

package com.epro.psmobile.data.upload;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.epro.psmobile.R;
import com.epro.psmobile.data.MembersInTeamHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TeamCheckInHistory;
import com.epro.psmobile.data.TeamCheckInHistory.HistoryType;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class LoginHistory implements
		UploadDataAdapter , JSONDataHolder {

	public final static String COLUMN_TEAM_ID = "teamId";
	public final static String COLUMN_LOGIN_TYPE = "loginType";
	public final static String COLUMN_LOGIN_TYPE_DESC = "loginTypeDesc";
	public final static String COLUMN_LOGIN_START_DATE = "loginStartDate";
	public final static String COLUMN_LOGIN_END_DATE = "loginEndDate";
	public final static String COLUMN_EMPLOYEE_ID_1 = "employeeId1";
	public final static String COLUMN_EMPLOYEE_ID_2 = "employeeId2";
	public final static String COLUMN_EMPLOYEE_ID_3 = "employeeId3";
	public final static String COLUMN_LOGIN_IMAGE_PATH = "loginImagePath";
	public final static String COLUMN_MILE_NO = "mileNo";
	public final static String COLUMN_MILE_NO_END_OF_DAY = "mileNoEndOfDay";
	public final static String COLUMN_LICENSE_NO = "licenseNo";
	public final static String COLUMN_CURRENT_LATITUDE = "currentLatitude";
	public final static String COLUMN_CURRENT_LONGITUDE = "currentLongitude";
	public final static String COLUMN_JOB_DOCUMENT_NO = "jobDocumentNo"; 
	public final static String COLUMN_JOB_ROW_ID = "jobRowId";
	public final static String COLUMN_JBO_NO_SEQ_ID = "jobNoSeqId";

	
	public final static String COLUMN_JOB_LOC_ID = "jobLocationId";
	
	private Task task;
	private TeamCheckInHistory teamCheckInHistory;
	private ArrayList<MembersInTeamHistory> memberInTeamHistory;
	private Context context;
	
	private int jobLocationId;
	
	public LoginHistory(Context context,
			Task task,
			TeamCheckInHistory teamCheckInHistory,
			ArrayList<MembersInTeamHistory> memberInTeamHistory)
	{
		this.context = context;
		this.task = task;
		this.teamCheckInHistory = teamCheckInHistory;
		this.memberInTeamHistory = memberInTeamHistory;
	}
	public LoginHistory(Context context)
	{
	    this.context = context;
	}
	public void setTask(Task task)
	{
	   this.task = task;
	}
	public Task getTask(){
	   return task;
	}
	@Override
	public void executeAdapter() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		/*
		 - teamId: teamID
		 - loginType: historyType
		 - loginStartDate : lastCheckInDateTime
		 - loginEndDate : lastCheckOutDateTime
		 - employeeId1: employeeCode (รหัสพนักงาน หัวหน้าทีม)
		 - employeeId2: employeeCode (รหัสพนักงาน ผู้ตรวจ 1)
		 - employeeId3: employeeCode (รหัสพนักงาน ผู้ตรวจ 2)
		 - loginImagePath : imgFileTeamCheckIn (จาก photoTeamHistory)
	  	 - mileNo : numberOfMilesAtStartPoint
		 - licenseNo : carLicenseNumber
		 - currentLatitude : teamStartLatLoc
		 - currentLongitude : teamStartLonLoc
		 - jobDocumentNo : taskCode
		 - jobRowId : taskID
		 - jobNoSeqId : taskDuplicateNo
		 */
		JSONDataUtil.put(jsonObj, COLUMN_TEAM_ID, this.teamCheckInHistory.getTeamID());
		JSONDataUtil.put(jsonObj, COLUMN_LOGIN_TYPE, teamCheckInHistory.getHistoryType().getCode());
		if (teamCheckInHistory.getHistoryType() == HistoryType.START_DATE_CHECKIN){
			JSONDataUtil.put(jsonObj, COLUMN_LOGIN_TYPE_DESC, this.context.getString(R.string.text_login_type_1_desc));
		}else if (teamCheckInHistory.getHistoryType() == HistoryType.START_INSPECT_CHECKIN)
		{
			JSONDataUtil.put(jsonObj, COLUMN_LOGIN_TYPE_DESC, this.context.getString(R.string.text_login_type_2_desc));			
		}else if (teamCheckInHistory.getHistoryType() == HistoryType.START_INSPECT_SUB_CHECKIN)
		{
           JSONDataUtil.put(jsonObj, COLUMN_LOGIN_TYPE_DESC, this.context.getString(R.string.text_login_type_3_desc));         		   
		}
		JSONDataUtil.put(jsonObj, COLUMN_LOGIN_START_DATE, 
				this.teamCheckInHistory.getLastCheckInDateTime());
		
		JSONDataUtil.put(jsonObj, 
				COLUMN_LOGIN_END_DATE, this.teamCheckInHistory.getLastCheckOutDateTime());

		JSONDataUtil.put(jsonObj, COLUMN_LOGIN_IMAGE_PATH, teamCheckInHistory.getTeamCheckInPhotosPath());
		String employeeId1 = "";
		String employeeId2 = "";
		String employeeId3 = "";
		try{
			employeeId1 = memberInTeamHistory.get(0).getEmployeeCode();
		}catch(Exception ex){}
		try{
			employeeId2 = memberInTeamHistory.get(1).getEmployeeCode();
		}catch(Exception ex){}
		try{
			employeeId3 = memberInTeamHistory.get(2).getEmployeeCode();
		}catch(Exception ex){}

		JSONDataUtil.put(jsonObj, COLUMN_EMPLOYEE_ID_1, employeeId1);
		JSONDataUtil.put(jsonObj, COLUMN_EMPLOYEE_ID_2, employeeId2);
		JSONDataUtil.put(jsonObj, COLUMN_EMPLOYEE_ID_3, employeeId3);
		
		/*
		 - mileNo : numberOfMilesAtStartPoint
		 - licenseNo : carLicenseNumber
		 - currentLatitude : teamStartLatLoc
		 - currentLongitude : teamStartLonLoc
		 */
		JSONDataUtil.put(jsonObj, COLUMN_MILE_NO, this.teamCheckInHistory.getNumberOfMilesAtStartPoint());
		/*
		 * waiting to confirm
		 */
		JSONDataUtil.put(jsonObj, COLUMN_MILE_NO_END_OF_DAY, this.teamCheckInHistory.getNumberOfMilesAtEndPoint());
		
		JSONDataUtil.put(jsonObj, COLUMN_LICENSE_NO, this.teamCheckInHistory.getCarLicenseNumber());
		JSONDataUtil.put(jsonObj, COLUMN_CURRENT_LATITUDE, this.teamCheckInHistory.getTeamStartLatLoc());
		JSONDataUtil.put(jsonObj, COLUMN_CURRENT_LONGITUDE, this.teamCheckInHistory.getTeamStartLonLoc());
		
		
		String jobDocumentNo = "";
		int jobRowId = 0;
		int jobNoSeqId = 0;
		
		if (this.task != null)
		{
			jobDocumentNo = this.task.getTaskCode();
			jobRowId = this.task.getTaskID();
			jobNoSeqId = this.task.getTaskDuplicatedNo();
		}
		JSONDataUtil.put(jsonObj, COLUMN_JOB_DOCUMENT_NO, jobDocumentNo);
		JSONDataUtil.put(jsonObj, COLUMN_JOB_ROW_ID, jobRowId);
		JSONDataUtil.put(jsonObj, COLUMN_JBO_NO_SEQ_ID, jobNoSeqId);
		
		
		JSONDataUtil.put(jsonObj, COLUMN_JOB_LOC_ID, this.jobLocationId);
		return jsonObj;
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}
   public int getJobLocationId() {
      return jobLocationId;
   }
   public void setJobLocationId(int jobLocationId) {
      this.jobLocationId = jobLocationId;
   }
   /**
    * @return the teamCheckInHistory
    */
   public TeamCheckInHistory getTeamCheckInHistory() {
      return teamCheckInHistory;
   }
   /**
    * @param teamCheckInHistory the teamCheckInHistory to set
    */
   public void setTeamCheckInHistory(TeamCheckInHistory teamCheckInHistory) {
      this.teamCheckInHistory = teamCheckInHistory;
   }
   /**
    * @return the memberInTeamHistory
    */
   public ArrayList<MembersInTeamHistory> getMemberInTeamHistory() {
      return memberInTeamHistory;
   }
   /**
    * @param memberInTeamHistory the memberInTeamHistory to set
    */
   public void setMemberInTeamHistory(ArrayList<MembersInTeamHistory> memberInTeamHistory) {
      this.memberInTeamHistory = memberInTeamHistory;
   }

}

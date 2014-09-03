package com.epro.psmobile.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class TeamCheckInHistory implements DbCursorHolder , 
Parcelable , 
TransactionStmtHolder,
JSONDataHolder
{
	public enum HistoryType{
		NONE(-1),
		START_DATE_CHECKIN(1),
		START_INSPECT_CHECKIN(2),
		START_INSPECT_SUB_CHECKIN(3);
		
		private int code;
		HistoryType(int code)
		{
			this.code = code;
		}
		public int getCode(){
			return this.code;
		}
		public static HistoryType getType(int code){
			for(int i = 0 ; i < values().length;i++){
				if (values()[i].getCode() == code)
				{
					return values()[i];
				}
			}
			return HistoryType.NONE;
		}
	}
	
	public final static String COLUMN_TEAM_CHECK_IN_HISTORY_ID = "teamCheckInHistoryID";
	public final static String COLUMN_TEAM_ID = "teamID";
	public final static String COLUMN_LAST_CHECK_IN_DATETIME = "lastCheckInDateTime";
	public final static String COLUMN_LAST_CHECK_OUT_DATETIME = "lastCheckOutDateTime";
	public final static String COLUMN_TEAM_START_LAT_LOC = "teamStartLatLoc";
	public final static String COLUMN_TEAM_START_LON_LOC = "teamStartLonLoc";
	public final static String COLUMN_NUMBER_OF_MILES_AT_START_POINT = "numberOfMilesAtStartPoint";
	
	public final static String COLUMN_NUMBER_OF_MILES_AT_END_POINT = "numberOfMilesAtEndPoint";/*have at update only*/
	
	public final static String COLUMN_CAR_LICENSE_NUMBER = "carLicenseNumber";
	public final static String COLUMN_IMG_TEAM_HISTORY_ID = "imgTeamHistoryID";
	public final static String COLUMN_HISTORY_TYPE = "historyType";
	public final static String COLUMN_TASK_CODE = "taskCode";
	public final static String COLUMN_TASK_ID = "taskID";
	public final static String COLUMN_TASK_DUPLICATE_NO = "taskDuplicateNo";
	
	public final static String COLUMN_LICENSE_PLATE_ID = "licensePlateId" /*have at update only*/;
	
	/*
	 customerName TEXT,
	 customerSurveySites TEXT,
	 inspectTypeID INTEGER,
	 inspectTypeName TEXT,
	 isTaskCompleted BOOLEAN
	 */
	public final static String COLUMN_CUSTOMER_NAME = "customerName";
	public final static String COLUMN_CUSTOMER_SURVEY_SITES = "customerSurveySites";
	public final static String COLUMN_INSPECT_TYPE_ID = "inspectTypeID";
	public final static String COLUMN_INSPECT_TYPE_NAME = "inspectTypeName";
	public final static String COLUMN_IS_TASKCOMPLETED = "isTaskCompleted";
//	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	
	private int teamCheckInHistoryID;
	private int teamID;
	private java.sql.Timestamp lastCheckInDateTime;
	private java.sql.Timestamp lastCheckOutDateTime;
	private double teamStartLatLoc;
	private double teamStartLonLoc;
	private String numberOfMilesAtStartPoint;
	private String numberOfMilesAtEndPoint;
	private String carLicenseNumber;
	private int imgTeamHistoryID;

	private String teamCheckInPhotosPath;
	
	private HistoryType historyType = HistoryType.NONE;
	private String taskCode = "";
	private int taskID = -1;
	private int taskDuplicateNo;
	private int licensePlateId;
//	private int customerSurveySiteID;
	private String customerName;
	private String customerSurveySites;
    private int	 inspectTypeID;
	private String inspectTypeName;
	private boolean isTaskCompleted;
	
	private ArrayList<MembersInTeamHistory> membersInTeam;
	
	public static final Parcelable.Creator<TeamCheckInHistory> CREATOR = new Parcelable.Creator<TeamCheckInHistory>()
	{

		@Override
		public TeamCheckInHistory createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new TeamCheckInHistory(source);
		}

		@Override
		public TeamCheckInHistory[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TeamCheckInHistory[size];
		}
		
	};
	public TeamCheckInHistory(Parcel source)
	{
		
	}
	public TeamCheckInHistory() {
		// TODO Auto-generated constructor stub
	}

	public int getTeamCheckInHistoryID() {
		return teamCheckInHistoryID;
	}
	public void setTeamCheckInHistoryID(int teamCheckInHistoryID) {
		this.teamCheckInHistoryID = teamCheckInHistoryID;
	}
	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public java.sql.Timestamp getLastCheckInDateTime() {
		return lastCheckInDateTime;
	}

	public void setLastCheckInDateTime(java.sql.Timestamp lastCheckInDateTime) {
		this.lastCheckInDateTime = lastCheckInDateTime;
	}

	

	public java.sql.Timestamp getLastCheckOutDateTime() {
		return lastCheckOutDateTime;
	}

	public void setLastCheckOutDateTime(java.sql.Timestamp lastCheckOutDateTime) {
		this.lastCheckOutDateTime = lastCheckOutDateTime;
	}

	public String getNumberOfMilesAtStartPoint() {
		return numberOfMilesAtStartPoint;
	}

	public void setNumberOfMilesAtStartPoint(String numberOfMilesAtStartPoint) {
		this.numberOfMilesAtStartPoint = numberOfMilesAtStartPoint;
	}

	public String getCarLicenseNumber() {
		return carLicenseNumber;
	}

	public void setCarLicenseNumber(String carLicenseNumber) {
		this.carLicenseNumber = carLicenseNumber;
	}

	public double getTeamStartLatLoc() {
		return teamStartLatLoc;
	}

	public void setTeamStartLatLoc(double teamStartLatLoc) {
		this.teamStartLatLoc = teamStartLatLoc;
	}

	public double getTeamStartLonLoc() {
		return teamStartLonLoc;
	}

	public void setTeamStartLonLoc(double teamStartLonLoc) {
		this.teamStartLonLoc = teamStartLonLoc;
	}

	public int getImgTeamHistoryID() {
		return imgTeamHistoryID;
	}

	public void setImgTeamHistoryID(int imgTeamHistoryID) {
		this.imgTeamHistoryID = imgTeamHistoryID;
	}

	public HistoryType getHistoryType() {
		return historyType;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	/*
	public int getCustomerSurveySiteID() {
		return customerSurveySiteID;
	}
	public void setCustomerSurveySiteID(int customerSurveySiteID) {
		this.customerSurveySiteID = customerSurveySiteID;
	}*/
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	public int getTaskDuplicateNo() {
		return taskDuplicateNo;
	}
	public void setTaskDuplicateNo(int taskDuplicateNo) {
		this.taskDuplicateNo = taskDuplicateNo;
	}
	public void setHistoryType(HistoryType historyType) {
		this.historyType = historyType;
	}
	public void addMember(MembersInTeamHistory member)
	{
		if (membersInTeam == null)
			membersInTeam = new ArrayList<MembersInTeamHistory>();
		
		membersInTeam.add(member);
	}
	public ArrayList<MembersInTeamHistory> getMembers(){
		return membersInTeam;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		/*
		private int teamID;
		private int employeeID;
		private java.sql.Timestamp lastCheckInDateTime;
		private java.sql.Timestamp lastCheckOutDateTime;
		private double teamStartLatLoc;
		private double teamStartLonLoc;
		private String numberOfMilesAtStartPoint;
		private String carLicenseNumber;
		private int imgTeamHistoryID; 
		 */

		this.teamCheckInHistoryID = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_CHECK_IN_HISTORY_ID));
		this.teamID = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_ID));
		String strCheckInDateTime = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_CHECK_IN_DATETIME));
		String strCheckOutDateTime = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_CHECK_OUT_DATETIME));
		
		this.lastCheckInDateTime  = DataUtil.convertStringToTimestampYYYYMMDDHHmmss(strCheckInDateTime);
		this.lastCheckOutDateTime = DataUtil.convertStringToTimestampYYYYMMDDHHmmss(strCheckOutDateTime);
		
		this.teamStartLatLoc = cursor.getDouble(cursor.getColumnIndex(COLUMN_TEAM_START_LAT_LOC));
		this.teamStartLonLoc = cursor.getDouble(cursor.getColumnIndex(COLUMN_TEAM_START_LON_LOC));
		this.numberOfMilesAtStartPoint = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER_OF_MILES_AT_START_POINT));
		this.numberOfMilesAtEndPoint = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER_OF_MILES_AT_END_POINT));
		this.carLicenseNumber = cursor.getString(cursor.getColumnIndex(COLUMN_CAR_LICENSE_NUMBER));
		this.imgTeamHistoryID = cursor.getInt(cursor.getColumnIndex(COLUMN_IMG_TEAM_HISTORY_ID));
		this.licensePlateId = cursor.getInt(cursor.getColumnIndex(COLUMN_LICENSE_PLATE_ID));
		/*
		 private HistoryType historyType = HistoryType.NONE;
		 private String taskCode = "";
		 private int taskID = -1;	
		 */
		int iHistoryTypeCode = cursor.getInt(cursor.getColumnIndex(COLUMN_HISTORY_TYPE));
		this.historyType = HistoryType.getType(iHistoryTypeCode);
		
		this.taskCode = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_CODE));
		this.taskID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID));
		
		this.taskDuplicateNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_DUPLICATE_NO));
		
		
		/*
		 customerName TEXT,
		 customerSurveySites TEXT,
		 inspectTypeID INTEGER,
		 inspectTypeName TEXT,
		 isTaskCompleted BOOLEAN
		 */
		/*
		 * public final static String COLUMN_CUSTOMER_NAME = "customerName";
	public final static String COLUMN_CUSTOMER_SURVEY_SITES = "customerSurveySites";
	public final static String COLUMN_INSPECT_TYPE_ID = "inspectTypeID";
	public final static String COLUMN_INSPECT_TYPE_NAME = "inspectTypeName";
	public final static String COLUMN_IS_TASKCOMPLETED = "isTaskCompleted";
		 */
		this.customerName = 
				cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME));
		this.customerSurveySites = cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITES));
		this.inspectTypeID = 
				cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_TYPE_ID));
		this.inspectTypeName =
				cursor.getString(cursor.getColumnIndex(COLUMN_INSPECT_TYPE_NAME));
		try{
			this.isTaskCompleted = 
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_TASKCOMPLETED)));
		}catch(Exception ex){}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("delete from teamCheckInHistory where teamCheckInHistoryID="+this.getTeamCheckInHistoryID());
		strBld.append(";");
		strBld.append("delete from memberInTeamHistory where teamCheckInHistoryID="+this.getTeamCheckInHistoryID());
		strBld.append(";");
		strBld.append("delete from phototeamhistory where imgTeamHistoryID="+this.getImgTeamHistoryID());
		return strBld.toString();
	}
	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		if (membersInTeam != null)
		{
			/*
			for(MembersInTeamHistory member : membersInTeam)
			{
				strBld.append(member.insertStatement()+";");
			}*/
					
		}
		/*
		 public final static String COLUMN_TEAM_ID = "teamID";
		 public final static String COLUMN_LAST_CHECK_IN_DATETIME = "lastCheckInDateTime";
		 public final static String COLUMN_LAST_CHECK_OUT_DATETIME = "lastCheckOutDateTime";
		 public final static String COLUMN_TEAM_START_LAT_LOC = "teamStartLatLoc";
		 public final static String COLUMN_TEAM_START_LON_LOC = "teamStartLonLoc";
		 public final static String COLUMN_NUMBER_OF_MILES_AT_START_POINT = "numberOfMilesAtStartPoint";
		 public final static String COLUMN_CAR_LICENSE_NUMBER = "carLicenseNumber";
		 public final static String COLUMN_IMG_TEAM_HISTORY_ID = "imgTeamHistoryID";
		 
		 public final static String COLUMN_HISTORY_TYPE = "historyType";
		 public final static String COLUMN_TASK_CODE = "taskCode";
		 public final static String COLUMN_TASK_ID = "taskID";

		 */
		strBld.append("insert into TeamCheckInHistory");
		strBld.append("(");
		strBld.append(COLUMN_TEAM_ID+",");
		strBld.append(COLUMN_LAST_CHECK_IN_DATETIME+",");
		strBld.append(COLUMN_LAST_CHECK_OUT_DATETIME+",");
		strBld.append(COLUMN_TEAM_START_LAT_LOC+",");
		strBld.append(COLUMN_TEAM_START_LON_LOC+",");
		strBld.append(COLUMN_NUMBER_OF_MILES_AT_START_POINT+",");
		strBld.append(COLUMN_CAR_LICENSE_NUMBER+",");
		strBld.append(COLUMN_IMG_TEAM_HISTORY_ID+",");
		strBld.append(COLUMN_HISTORY_TYPE+",");
		strBld.append(COLUMN_TASK_CODE+",");
		strBld.append(COLUMN_TASK_ID+",");
		strBld.append(COLUMN_TASK_DUPLICATE_NO);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.getTeamID()+",");
		strBld.append("'"+DataUtil.convertTimestampToStringYYYMMDDHHmmss(getLastCheckInDateTime())+"',");
		strBld.append("'"+DataUtil.convertTimestampToStringYYYMMDDHHmmss(getLastCheckOutDateTime())+"',");
		strBld.append(""+this.getTeamStartLatLoc()+",");
		strBld.append(""+this.getTeamStartLonLoc()+",");
		strBld.append("'"+this.getNumberOfMilesAtStartPoint()+"',");
		strBld.append("'"+this.getCarLicenseNumber()+"',");
		strBld.append(""+this.getImgTeamHistoryID()+",");
		strBld.append(""+this.getHistoryType().getCode()+",");
		strBld.append("'"+this.getTaskCode()+"',");
		strBld.append(""+this.getTaskID()+",");
		strBld.append(""+this.getTaskDuplicateNo()+"");
		strBld.append(")");	
		
		return strBld.toString();
	}
	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		
		JSONDataUtil.put(jsonObj, COLUMN_TEAM_CHECK_IN_HISTORY_ID ,this.teamCheckInHistoryID);
		JSONDataUtil.put(jsonObj, COLUMN_TEAM_ID ,this.teamID);
		JSONDataUtil.put(jsonObj, COLUMN_LAST_CHECK_IN_DATETIME ,this.lastCheckInDateTime.toString());
		JSONDataUtil.put(jsonObj, COLUMN_LAST_CHECK_OUT_DATETIME ,
				(this.lastCheckOutDateTime != null)?this.lastCheckOutDateTime.toString():"");
		JSONDataUtil.put(jsonObj, COLUMN_TEAM_START_LAT_LOC , this.teamStartLatLoc);
		JSONDataUtil.put(jsonObj, COLUMN_TEAM_START_LON_LOC , this.teamStartLonLoc);
		JSONDataUtil.put(jsonObj, COLUMN_NUMBER_OF_MILES_AT_START_POINT , this.numberOfMilesAtStartPoint);
		JSONDataUtil.put(jsonObj, COLUMN_CAR_LICENSE_NUMBER , this.carLicenseNumber);
		JSONDataUtil.put(jsonObj, COLUMN_IMG_TEAM_HISTORY_ID ,this.imgTeamHistoryID);
		JSONDataUtil.put(jsonObj, COLUMN_HISTORY_TYPE , this.historyType.getCode());
		JSONDataUtil.put(jsonObj, COLUMN_TASK_CODE , this.taskCode);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_ID ,this.taskID);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_DUPLICATE_NO , this.taskDuplicateNo);
		
		return jsonObj;
	}
	public String getTeamCheckInPhotosPath() {
		return teamCheckInPhotosPath;
	}
	public void setTeamCheckInPhotosPath(String teamCheckInPhotosPath) {
		this.teamCheckInPhotosPath = teamCheckInPhotosPath;
	}
	public String getNumberOfMilesAtEndPoint() {
		return numberOfMilesAtEndPoint;
	}
	public void setNumberOfMilesAtEndPoint(String numberOfMilesAtEndPoint) {
		this.numberOfMilesAtEndPoint = numberOfMilesAtEndPoint;
	}
	public int getLicensePlateId() {
		return licensePlateId;
	}
	public void setLicensePlateId(int licensePlateId) {
		this.licensePlateId = licensePlateId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerSurveySites() {
		return customerSurveySites;
	}
	public void setCustomerSurveySites(String customerSurveySites) {
		this.customerSurveySites = customerSurveySites;
	}
	public int getInspectTypeID() {
		return inspectTypeID;
	}
	public void setInspectTypeID(int inspectTypeID) {
		this.inspectTypeID = inspectTypeID;
	}
	public String getInspectTypeName() {
		return inspectTypeName;
	}
	public void setInspectTypeName(String inspectTypeName) {
		this.inspectTypeName = inspectTypeName;
	}
	public boolean isTaskCompleted() {
		return isTaskCompleted;
	}
	public void setTaskCompleted(boolean isTaskCompleted) {
		this.isTaskCompleted = isTaskCompleted;
	}

}

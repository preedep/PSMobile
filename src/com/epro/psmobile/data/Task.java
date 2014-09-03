package com.epro.psmobile.data;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.*;

public class Task implements DbCursorHolder , 
							 Parcelable ,  
							 Cloneable,
							 TransactionStmtHolder,
							 JSONDataHolder
{

	/*
	 	taskID INTEGER,
	 	taskCode TEXT,
		jobRequestID INTEGER REFERENCES JobRequest (jobRequestID),
		taskNo INTEGER,
		teamID INTEGER,
		customerSurveySiteID INTEGER,
		taskFormTemplateID INTEGER,
		taskLevel INTEGER,
		taskStatus INTEGER,
		taskDate DATE,
		taskTime TIME,
		shiftCause TEXT,
		remark TEXT,
		PRIMARY KEY (taskID,taskCode,jobRequestID,taskNo,teamID,customerSurveySiteID)

	 */
	public final static String COLUMN_TASK_ID = "taskID";
	public final static String COLUMN_TASK_CODE = "taskCode";
	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_TASK_NO = "taskNo";
	public final static String COLUMN_TASK_DUPLICATED_NO = "taskDuplicateNo";
	public final static String COLUMN_TEAM_ID = "teamID";

	public final static String COLUMN_FORCE_TIME = "forceTime";
	public final static String COLUMN_FLAG_OTHER_TEAM = "flagOtherTeam";
	
	public final static String COLUMN_TASK_LEVEL = "taskLevel";
	public final static String COLUMN_TASK_STATUS = "taskStatus";
	public final static String COLUMN_TASK_DATE = "taskDate";
	public final static String COLUMN_TASK_SCHEDULE_DATE = "taskScheduleDate";
	public final static String COLUMN_TASK_START_TIME = "taskStartTime";
	public final static String COLUMN_TASK_END_TIME = "taskEndTime";
	public final static String COLUMN_SHIFT_CAUSE = "shiftCause";
	public final static String COLUMN_REMARK = "remark";
	public final static String COLUMN_REASON_SENTENCE_ID = "reasonSentenceID";
	public final static String COLUMN_REASON_SENTENCE_TYPE = "reasonSentenceType";
	
	public final static String COLUMN_IS_DUPLICATED_TASK = "isDuplicatedTask";
	public final static String COLUMN_DUPLICATED_FROM_TASK_ID = "duplicatedFromTaskID";
	public final static String COLUMN_DUPLICATED_LOCATION_START = "duplicatedLocationStart";
	public final static String COLUMN_DUPLICATED_LOCATION_TO = "duplicatedLocationEnd";
	public final static String COLUMN_DUPLICATED_DISTANCE = "duplicatedDistance";
	
	public final static String COLUMN_IS_TASK_TEAM_LEADER = "isTaskTeamLeader";

	public final static String COLUMN_TASK_FORM_TEMPLATE_ID = "taskFormTemplateID";
	
	public final static String COLUMN_IS_UPLOADED_SYNCED  = "isUploadSynced";
	
	public final static String COLUMN_NOT_UPLOAD_FLAG = "notUploadFlag";
	
	public final static String COLUMN_IS_INSPECT_REPORT_GENERATED = "isInspectReportGenerated";
	
	public final static String COLUMN_DUPLICATE_SURVEY_START_ID = "duplicatedSurveyStartSiteID";
	public final static String COLUMN_DUPLICATE_SURVEY_END_ID = "duplicatedSurveyEndSiteID";
	
	public final static String COLUMN_FLAG_CAR_REINSPECT = "flagSummaryCarInspect";
	
	private int taskID;
	private String taskCode;
	private JobRequest jobRequest;
	private int taskNo;
	private int taskDuplicatedNo;
	private int teamID;
//	private CustomerSurveySite customerSurveySite;
	private int taskFormTemplateID;
	private String forceTime;
	private String flagOtherTeam;
	
	private int taskLevel;
	private TaskStatus taskStatus = TaskStatus.WAIT_TO_CONFIRM;
	private Date taskDate;
	private Date taskScheduleDate;
	private String taskStartTime;
	private String taskEndTime;
	private String shiftCause;
	private String remark;
	
	private int reasonSentenceID;
	private String reasonSentenceType;
	
	private boolean isDuplicatedTask;
	private int duplicatedFromTaskID;
	private String duplicatedLocationStart;
	private String duplicatedLocationTo;
	private double duplicatedDistance;
	
	private boolean isTaskTeamLeader;

	private boolean isUploadSynced;
	
	private String notUploadFlag = "N";
	
	private boolean isInspectReportGenerated;
	
	private int duplicatedSurveyStartSiteID;
	private int duplicatedSurveyEndSiteID;
	
	private String flagSummaryCarInspect = "N";
	
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>()
	{

		@Override
		public Task createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Task(source);
		}

		@Override
		public Task[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Task[size];
		}
		
	};
	public Task(Parcel source)
	{
		/*
		dest.writeInt(this.getTaskID());
		dest.writeString(this.getTaskCode());
		dest.writeParcelable(this.getJobRequest(), flags);
		dest.writeInt(this.getTaskNo());
		dest.writeInt(this.getTaskDuplicatedNo());
		dest.writeInt(this.getTeamID());
		dest.writeString(this.getForceTime());
		dest.writeString(this.getFlagOtherTeam());
		dest.writeInt(this.getTaskLevel());
		dest.writeInt(this.getTaskStatus().getTaskStatus());
		dest.writeLong(this.getTaskDate().getTime());
		dest.writeString(this.getTaskStartTime());
		dest.writeString(this.getTaskEndTime());
		dest.writeString(this.getRemark());
		dest.writeString(this.getShiftCause());
		dest.writeInt(this.getReasonSentenceID());
		dest.writeString(this.getReasonSentenceType());
		dest.writeBooleanArray(new boolean[]{this.isDuplicatedTask()});
		dest.writeInt(this.getDuplicatedFromTaskID());
		dest.writeString(this.getDuplicatedLocationStart());
		dest.writeString(this.getDuplicatedLocationTo());
		dest.writeDouble(this.getDuplicatedDistance());

		 */
		this.setTaskID(source.readInt());
		this.setTaskCode(source.readString());
		this.setJobRequest((JobRequest)source.readParcelable(this.getClass().getClassLoader()));
		this.setTaskNo(source.readInt());
		this.setTaskDuplicatedNo(source.readInt());
		this.setTeamID(source.readInt());
		this.setForceTime(source.readString());
		this.setFlagOtherTeam(source.readString());
		this.setTaskLevel(source.readInt());
		
		this.setTaskStatus(TaskStatus.getStatus(source.readInt()));
		
		this.setTaskDate(new Date(source.readLong()));
		this.setTaskScheduleDate(new Date(source.readLong()));
		
		this.setTaskStartTime(source.readString());
		this.setTaskEndTime(source.readString());
		this.setRemark(source.readString());
		this.setShiftCause(source.readString());
		this.setReasonSentenceID(source.readInt());
		this.setReasonSentenceType(source.readString());

		boolean[] b = new boolean[1];
		source.readBooleanArray(b);
		this.isDuplicatedTask = b[0];

		this.setDuplicatedFromTaskID(source.readInt());
		this.setDuplicatedLocationStart(source.readString());
		this.setDuplicatedLocationTo(source.readString());
		this.setDuplicatedDistance(source.readDouble());
		
		b = new boolean[1];
		source.readBooleanArray(b);
		this.isTaskTeamLeader = b[0];
		
		this.setTaskFormTemplateID(source.readInt());
		
		b = new boolean[1];
		source.readBooleanArray(b);
		this.isUploadSynced = b[0];
		
		this.setNotUploadFlag(source.readString());
		
		b = new boolean[1];
		source.readBooleanArray(b);
		this.isInspectReportGenerated = b[0];
		
		this.duplicatedSurveyStartSiteID = source.readInt();
		this.duplicatedSurveyEndSiteID = source.readInt();
		
		this.flagSummaryCarInspect = source.readString();
		
	}
	public Task() {
		// TODO Auto-generated constructor stub
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public JobRequest getJobRequest() {
		return jobRequest;
	}

	public void setJobRequest(JobRequest jobRequest) {
		this.jobRequest = jobRequest;
	}

	public int getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
	}

	public int getTaskDuplicatedNo() {
		return taskDuplicatedNo;
	}
	public void setTaskDuplicatedNo(int taskDuplicatedNo) {
		this.taskDuplicatedNo = taskDuplicatedNo;
	}
	public int getTaskFormTemplateID() {
		return taskFormTemplateID;
	}
	public void setTaskFormTemplateID(int taskFormTemplateID) {
		this.taskFormTemplateID = taskFormTemplateID;
	}
	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}
	
	public int getTaskLevel() {
		return taskLevel;
	}

	public void setTaskLevel(int taskLevel) {
		this.taskLevel = taskLevel;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public Date getTaskScheduleDate() {
		return taskScheduleDate;
	}
	public void setTaskScheduleDate(Date taskScheduleDate) {
		this.taskScheduleDate = taskScheduleDate;
	}
	public String getTaskStartTime() {
		return taskStartTime;
	}
	public void setTaskStartTime(String taskStartTime) {
		this.taskStartTime = taskStartTime;
	}
	public String getTaskEndTime() {
		return taskEndTime;
	}
	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
	public String getShiftCause() {
		return shiftCause;
	}

	public void setShiftCause(String shiftCause) {
		this.shiftCause = shiftCause;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isDuplicatedTask() {
		return isDuplicatedTask;
	}
	public void setDuplicatedTask(boolean isDuplicatedTask) {
		this.isDuplicatedTask = isDuplicatedTask;
	}
	public int getDuplicatedFromTaskID() {
		return duplicatedFromTaskID;
	}
	public void setDuplicatedFromTaskID(int duplicatedFromTaskID) {
		this.duplicatedFromTaskID = duplicatedFromTaskID;
	}
	public String getDuplicatedLocationStart() {
		return duplicatedLocationStart;
	}
	public void setDuplicatedLocationStart(String duplicatedLocationStart) {
		this.duplicatedLocationStart = duplicatedLocationStart;
	}
	public String getDuplicatedLocationTo() {
		return duplicatedLocationTo;
	}
	public void setDuplicatedLocationTo(String duplicatedLocationTo) {
		this.duplicatedLocationTo = duplicatedLocationTo;
	}
	public double getDuplicatedDistance() {
		return duplicatedDistance;
	}
	public void setDuplicatedDistance(double duplicatedDistance) {
		this.duplicatedDistance = duplicatedDistance;
	}
	public int getReasonSentenceID() {
		return reasonSentenceID;
	}
	public void setReasonSentenceID(int reasonSentenceID) {
		this.reasonSentenceID = reasonSentenceID;
	}
	public String getReasonSentenceType() {
		return reasonSentenceType;
	}
	public void setReasonSentenceType(String reasonSentenceType) {
		this.reasonSentenceType = reasonSentenceType;
	}
	
	public String getForceTime() {
		return forceTime;
	}
	public void setForceTime(String forceTime) {
		this.forceTime = forceTime;
	}
	public String getFlagOtherTeam() {
		return flagOtherTeam;
	}
	public void setFlagOtherTeam(String flagOtherTeam) {
		this.flagOtherTeam = flagOtherTeam;
	}
	public boolean isTaskTeamLeader() {
		return isTaskTeamLeader;
	}
	public void setTaskTeamLeader(boolean isTaskTeamLeader) {
		this.isTaskTeamLeader = isTaskTeamLeader;
	}
	public boolean isUploadSynced() {
		return isUploadSynced;
	}
	public boolean isInspectReportGenerated() {
		return isInspectReportGenerated;
	}
	public void setInspectReportGenerated(boolean isInspectReportGenerated) {
		this.isInspectReportGenerated = isInspectReportGenerated;
	}
	public void setUploadSynced(boolean isUploadSynced) {
		this.isUploadSynced = isUploadSynced;
	}
	public String getNotUploadFlag() {
		return notUploadFlag;
	}
	public void setNotUploadFlag(String notUploadFlag) {
		this.notUploadFlag = notUploadFlag;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		/*
	 	taskID INTEGER,
taskCode VARCHAR(50),
jobRequestID INTEGER,
taskNo INTEGER,
taskDuplicateNo INTEGER,
teamID INTEGER,
taskLevel INTEGER,
taskStatus INTEGER,
taskDate DATE,
taskStartTime TEXT,
taskEndTime TEXT,
reasonSentenceID INTEGER,
reasonSentenceType TEXT,
shiftCause TEXT,
remark TEXT,
isDuplicatedTask INTEGER,
duplicatedFromTaskID INTEGER,
duplicated_location_start TEXT,
duplicated_location_to TEXT,
duplicated_distance REAL,
forceTeam TEXT,
flagOtherTeam TEXT,

	 */
		this.taskID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID));
		this.taskCode = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_CODE));
		
		this.jobRequest = new JobRequest();
		this.jobRequest.onBind(cursor);
		
		this.forceTime = cursor.getString(cursor.getColumnIndex(COLUMN_FORCE_TIME));
		this.flagOtherTeam = cursor.getString(cursor.getColumnIndex(COLUMN_FLAG_OTHER_TEAM));
		
		this.taskDuplicatedNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_DUPLICATED_NO));
		
		this.taskNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_NO));
		this.teamID = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_ID));
		
//		this.customerSurveySite = new CustomerSurveySite();
//		this.customerSurveySite.onBind(cursor);
		this.taskDuplicatedNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_DUPLICATED_NO));
		
		this.taskLevel = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_LEVEL));
		int status = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_STATUS));
		this.taskStatus = TaskStatus.getStatus(status);
		
		String strTaskDate = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATE));
		this.taskDate = DataUtil.convertToDate(strTaskDate);
		
		String strTaskScheduleDate = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_SCHEDULE_DATE));
		this.taskScheduleDate = DataUtil.convertToDate(strTaskScheduleDate);
		
		this.taskStartTime = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_START_TIME));
		this.taskEndTime = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_END_TIME));
				
		this.shiftCause = cursor.getString(cursor.getColumnIndex(COLUMN_SHIFT_CAUSE));
		this.remark = cursor.getString(cursor.getColumnIndex(COLUMN_REMARK));
		
		try{
			this.isDuplicatedTask = 
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_DUPLICATED_TASK))) ; // (cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DUPLICATED_TASK)) == 0)?false:true;
		}catch(Exception ex){}
		this.duplicatedFromTaskID = cursor.getInt(cursor.getColumnIndex(COLUMN_DUPLICATED_FROM_TASK_ID));
		this.duplicatedLocationStart = cursor.getString(cursor.getColumnIndex(COLUMN_DUPLICATED_LOCATION_START));
		this.duplicatedLocationTo = cursor.getString(cursor.getColumnIndex(COLUMN_DUPLICATED_LOCATION_TO));
		this.duplicatedDistance = cursor.getDouble(cursor.getColumnIndex(COLUMN_DUPLICATED_DISTANCE));
		
		try{
			this.isTaskTeamLeader = 
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_TASK_TEAM_LEADER)));
		}catch(Exception ex){}
		
		try{
			this.isInspectReportGenerated = 
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_INSPECT_REPORT_GENERATED)));
		}catch(Exception ex){}
		
		this.taskFormTemplateID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_FORM_TEMPLATE_ID));
		
		try{
			this.isUploadSynced = 
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_UPLOADED_SYNCED)));
		}catch(Exception ex){}
		
		this.notUploadFlag = 
				cursor.getString(cursor.getColumnIndex(COLUMN_NOT_UPLOAD_FLAG));
		
		
		this.duplicatedSurveyStartSiteID = 
				cursor.getInt(cursor.getColumnIndex(COLUMN_DUPLICATE_SURVEY_START_ID));
		this.duplicatedSurveyEndSiteID = 
				cursor.getInt(cursor.getColumnIndex(COLUMN_DUPLICATE_SURVEY_END_ID));
		
		try{
		this.flagSummaryCarInspect = 
		      cursor.getString(cursor.getColumnIndex(COLUMN_FLAG_CAR_REINSPECT));
		
		}catch(Exception ex){
		   this.flagSummaryCarInspect = "N";
		}
		
		this.reasonSentenceID = cursor.getInt(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_ID));
		this.reasonSentenceType = cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_TYPE));
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		/*
		 * 	
			
	private int taskID;
	private String taskCode;
	private JobRequest jobRequest;
	private int taskNo;
	private int taskDuplicatedNo;
	private int teamID;
//	private CustomerSurveySite customerSurveySite;
	private String forceTime;
	private String flagOtherTeam;
	
	private int taskLevel;
	private TaskStatus taskStatus = TaskStatus.WAIT_TO_CONFIRM;
	private Date taskDate;
	private String taskStartTime;
	private String taskEndTime;
	private String shiftCause;
	private String remark;
	
	private int reasonSentenceID;
	private String reasonSentenceType;
	
	private boolean isDuplicatedTask;
	private int duplicatedFromTaskID;
	private String duplicatedLocationStart;
	private String duplicatedLocationTo;
	private double duplicatedDistance;
	
		  */
		dest.writeInt(this.getTaskID());
		dest.writeString(this.getTaskCode());
		dest.writeParcelable(this.getJobRequest(), flags);
		dest.writeInt(this.getTaskNo());
		dest.writeInt(this.getTaskDuplicatedNo());
		dest.writeInt(this.getTeamID());
		dest.writeString(this.getForceTime());
		dest.writeString(this.getFlagOtherTeam());
		dest.writeInt(this.getTaskLevel());
		dest.writeInt(this.getTaskStatus().getTaskStatus());
		dest.writeLong(this.getTaskDate().getTime());
		dest.writeLong(this.getTaskScheduleDate().getTime());
		dest.writeString(this.getTaskStartTime());
		dest.writeString(this.getTaskEndTime());
		dest.writeString(this.getRemark());
		dest.writeString(this.getShiftCause());
		dest.writeInt(this.getReasonSentenceID());
		dest.writeString(this.getReasonSentenceType());
		dest.writeBooleanArray(new boolean[]{this.isDuplicatedTask()});
		dest.writeInt(this.getDuplicatedFromTaskID());
		dest.writeString(this.getDuplicatedLocationStart());
		dest.writeString(this.getDuplicatedLocationTo());
		dest.writeDouble(this.getDuplicatedDistance());
		dest.writeBooleanArray(new boolean[]{this.isTaskTeamLeader});
		dest.writeInt(this.getTaskFormTemplateID());
		dest.writeBooleanArray(new boolean[]{this.isUploadSynced});
		dest.writeString(this.getNotUploadFlag());
		dest.writeBooleanArray(new boolean[]{this.isInspectReportGenerated});
		dest.writeInt(this.getDuplicatedSurveyStartSiteID());
		dest.writeInt(this.getDuplicatedSurveyEndSiteID());
		
		dest.writeString(this.getFlagSummaryCarInspect());
	}

	 public  Object clone() throws CloneNotSupportedException {
	        return super.clone();
	  }
	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into Task");
		strBld.append("(");
		strBld.append("taskID,");
		strBld.append("taskCode,");
		strBld.append("jobRequestID,");
		strBld.append("taskNo,");
		strBld.append(COLUMN_TASK_DUPLICATED_NO+",");
		strBld.append("teamID,");
		strBld.append("taskLevel,");
		strBld.append("taskStatus,");
		strBld.append("taskDate,");
		strBld.append(COLUMN_TASK_SCHEDULE_DATE+",");
		strBld.append(COLUMN_TASK_START_TIME+",");
		strBld.append(COLUMN_TASK_END_TIME+",");
		strBld.append("reasonSentenceID,");
		strBld.append("reasonSentenceType,");
		strBld.append("shiftCause,");
		strBld.append("remark,");
		strBld.append("isDuplicatedTask,");
		strBld.append("duplicatedFromTaskID,");
		strBld.append(""+COLUMN_DUPLICATED_LOCATION_START+",");
		strBld.append(""+COLUMN_DUPLICATED_LOCATION_TO+",");
		strBld.append(""+COLUMN_DUPLICATED_DISTANCE+",");
		strBld.append(COLUMN_FORCE_TIME+",");
		strBld.append(COLUMN_FLAG_OTHER_TEAM+",");
		strBld.append(COLUMN_IS_TASK_TEAM_LEADER+",");
		strBld.append(COLUMN_TASK_FORM_TEMPLATE_ID+",");
		strBld.append(COLUMN_IS_UPLOADED_SYNCED+",");
		strBld.append(COLUMN_NOT_UPLOAD_FLAG+",");
		strBld.append(COLUMN_IS_INSPECT_REPORT_GENERATED+",");
		strBld.append(COLUMN_DUPLICATE_SURVEY_START_ID+",");
//	    strBld.append(COLUMN_DUPLICATE_SURVEY_END_ID+"");

		strBld.append(COLUMN_DUPLICATE_SURVEY_END_ID+",");
		strBld.append(COLUMN_FLAG_CAR_REINSPECT);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(""+this.getTaskID()+",");
		strBld.append("'"+this.getTaskCode()+"',");
		strBld.append(""+this.getJobRequest().getJobRequestID()+",");
		strBld.append(""+this.getTaskNo()+",");
		strBld.append(""+this.taskDuplicatedNo+",");
		strBld.append(""+this.getTeamID()+",");
		strBld.append(""+this.getTaskLevel()+",");
		strBld.append(""+this.getTaskStatus().getTaskStatus()+",");
		strBld.append("'"+DataUtil.convertDateToStringYYYYMMDD(this.getTaskDate())+"',");
		strBld.append("'"+DataUtil.convertDateToStringYYYYMMDD(this.getTaskScheduleDate())+"',");
		strBld.append("'"+this.getTaskStartTime()+"',");
		strBld.append("'"+this.getTaskEndTime()+"',");
		strBld.append(""+this.reasonSentenceID+",");
		strBld.append("'"+this.reasonSentenceType+"',");
		strBld.append("'"+this.getShiftCause()+"',");
		strBld.append("'"+this.getRemark()+"',");
		strBld.append("'"+isDuplicatedTask()+"',");
		strBld.append(""+this.getDuplicatedFromTaskID()+",");
		strBld.append("'"+this.getDuplicatedLocationStart()+"',");
		strBld.append("'"+this.getDuplicatedLocationTo()+"',");
		strBld.append(""+this.getDuplicatedDistance()+",");
		strBld.append("'"+this.forceTime+"',");
		strBld.append("'"+this.flagOtherTeam+"',");
		strBld.append("'"+this.isTaskTeamLeader()+"',");
		strBld.append(""+this.getTaskFormTemplateID()+",");
		strBld.append("'"+this.isUploadSynced+"',");
		strBld.append("'"+this.notUploadFlag+"',");
		strBld.append("'"+this.isInspectReportGenerated+"',");
		strBld.append(""+this.getDuplicatedSurveyStartSiteID()+",");
		strBld.append(""+this.getDuplicatedSurveyEndSiteID()+",");
		strBld.append("'"+this.getFlagSummaryCarInspect()+"'");
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
		this.taskID = JSONDataUtil.getInt(jsonObj,COLUMN_TASK_ID);
		this.taskCode = JSONDataUtil.getString(jsonObj,COLUMN_TASK_CODE);
		this.jobRequest = new JobRequest();
		this.jobRequest.setJobRequestID(
				JSONDataUtil.getInt(jsonObj, COLUMN_JOB_REQUEST_ID)
			);
		
		this.taskDuplicatedNo = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_DUPLICATED_NO);
		this.teamID = JSONDataUtil.getInt(jsonObj, COLUMN_TEAM_ID);
		
		this.taskLevel = JSONDataUtil.getInt(jsonObj,COLUMN_TASK_LEVEL);
		
		int iTaskStatus = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_STATUS);
		this.taskStatus = TaskStatus.getStatus(iTaskStatus);

		String strTaskDate = JSONDataUtil.getString(jsonObj, COLUMN_TASK_DATE);
		this.taskDate = DataUtil.convertToDateYYYYMMDD(strTaskDate);
		
		String strTaskScheduleDate = JSONDataUtil.getString(jsonObj, COLUMN_TASK_SCHEDULE_DATE);
		this.setTaskScheduleDate(DataUtil.convertToDateYYYYMMDD(strTaskScheduleDate));
		
		this.taskStartTime = JSONDataUtil.getString(jsonObj, COLUMN_TASK_START_TIME);
		this.taskEndTime = JSONDataUtil.getString(jsonObj,COLUMN_TASK_END_TIME);

		this.reasonSentenceID = JSONDataUtil.getInt(jsonObj, COLUMN_REASON_SENTENCE_ID);
		this.reasonSentenceType = JSONDataUtil.getString(jsonObj, COLUMN_REASON_SENTENCE_TYPE);
		
		this.shiftCause = JSONDataUtil.getString(jsonObj, COLUMN_SHIFT_CAUSE);
		this.remark = JSONDataUtil.getString(jsonObj,COLUMN_REMARK);
		
		int iIsDuplicatedTask = JSONDataUtil.getInt(jsonObj, COLUMN_IS_DUPLICATED_TASK);
		this.isDuplicatedTask = (iIsDuplicatedTask > 0);
		
		int iIsInspectReportGenerated = JSONDataUtil.getInt(jsonObj, COLUMN_IS_INSPECT_REPORT_GENERATED);
		this.isInspectReportGenerated = (iIsInspectReportGenerated > 0);
		
		
		this.duplicatedFromTaskID = JSONDataUtil.getInt(jsonObj, COLUMN_DUPLICATED_FROM_TASK_ID);
		this.duplicatedLocationStart = JSONDataUtil.getString(jsonObj, COLUMN_DUPLICATED_LOCATION_START);
		this.duplicatedLocationTo = JSONDataUtil.getString(jsonObj, COLUMN_DUPLICATED_LOCATION_TO);
		this.duplicatedDistance = JSONDataUtil.getDouble(jsonObj, COLUMN_DUPLICATED_DISTANCE);
		
		this.forceTime = JSONDataUtil.getString(jsonObj, COLUMN_FORCE_TIME);
		this.flagOtherTeam = JSONDataUtil.getString(jsonObj, COLUMN_FLAG_OTHER_TEAM);
		
		this.isTaskTeamLeader =  JSONDataUtil.getBoolean(jsonObj, COLUMN_IS_TASK_TEAM_LEADER);
		
		this.taskFormTemplateID = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_FORM_TEMPLATE_ID);
		
		this.notUploadFlag = JSONDataUtil.getString(jsonObj, COLUMN_NOT_UPLOAD_FLAG);
		if ((this.notUploadFlag == null)||
			(this.notUploadFlag.isEmpty())){
			this.notUploadFlag = "N";
		}
		
		this.duplicatedSurveyStartSiteID  = JSONDataUtil.getInt(jsonObj, COLUMN_DUPLICATE_SURVEY_START_ID);
		this.duplicatedSurveyEndSiteID = JSONDataUtil.getInt(jsonObj, COLUMN_DUPLICATE_SURVEY_END_ID);
		
		this.flagSummaryCarInspect = JSONDataUtil.getString(jsonObj, COLUMN_FLAG_CAR_REINSPECT);
		if ((this.flagSummaryCarInspect == null)||
		     (this.flagSummaryCarInspect.isEmpty())){
		   this.flagSummaryCarInspect = "N";
		}
		
	}
	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj, COLUMN_TASK_ID, this.taskID);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_CODE, this.taskCode);
		JSONDataUtil.put(jsonObj, COLUMN_JOB_REQUEST_ID ,this.jobRequest.getJobRequestID());
		
		//JSONDataUtil.put(jsonObj, COLUMN_TASK_NO ,this.taskNo);//ignored
		
		JSONDataUtil.put(jsonObj,  COLUMN_TASK_DUPLICATED_NO ,this.taskDuplicatedNo);
		JSONDataUtil.put(jsonObj, COLUMN_TEAM_ID ,this.teamID);

		JSONDataUtil.put(jsonObj, COLUMN_FORCE_TIME ,this.forceTime);
		JSONDataUtil.put(jsonObj, COLUMN_FLAG_OTHER_TEAM ,this.flagOtherTeam);
	
		JSONDataUtil.put(jsonObj, COLUMN_TASK_LEVEL ,this.taskLevel);
		if (taskStatus.getTaskStatus() == TaskStatus.FINISH.getTaskStatus()){
			if (this.isDuplicatedTask){
				JSONDataUtil.put(jsonObj, COLUMN_TASK_STATUS ,TaskStatus.DUPLICATED.getTaskStatus());				
			}else{	
				JSONDataUtil.put(jsonObj, COLUMN_TASK_STATUS ,this.taskStatus.getTaskStatus());
			}
		}else{
			JSONDataUtil.put(jsonObj, COLUMN_TASK_STATUS ,this.taskStatus.getTaskStatus());			
		}
		JSONDataUtil.put(jsonObj, COLUMN_TASK_DATE , taskDate);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_SCHEDULE_DATE ,taskScheduleDate);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_START_TIME ,this.taskStartTime);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_END_TIME ,this.taskEndTime);
		JSONDataUtil.put(jsonObj, COLUMN_SHIFT_CAUSE ,this.shiftCause);
		JSONDataUtil.put(jsonObj, COLUMN_REMARK ,this.remark);
		JSONDataUtil.put(jsonObj, COLUMN_REASON_SENTENCE_ID ,this.reasonSentenceID);
		JSONDataUtil.put(jsonObj, COLUMN_REASON_SENTENCE_TYPE ,this.reasonSentenceType);
	
		JSONDataUtil.put(jsonObj, COLUMN_IS_DUPLICATED_TASK ,(this.isDuplicatedTask?1:0));
		JSONDataUtil.put(jsonObj, COLUMN_DUPLICATED_FROM_TASK_ID ,this.duplicatedFromTaskID);
		JSONDataUtil.put(jsonObj, COLUMN_DUPLICATED_LOCATION_START ,this.duplicatedLocationStart);
		JSONDataUtil.put(jsonObj, COLUMN_DUPLICATED_LOCATION_TO ,this.duplicatedLocationTo);
		JSONDataUtil.put(jsonObj, COLUMN_DUPLICATED_DISTANCE ,this.duplicatedDistance);
	
		JSONDataUtil.put(jsonObj, COLUMN_IS_TASK_TEAM_LEADER ,(this.isTaskTeamLeader?1:0));

		JSONDataUtil.put(jsonObj, COLUMN_TASK_FORM_TEMPLATE_ID ,this.taskFormTemplateID);

		JSONDataUtil.put(jsonObj, COLUMN_DUPLICATE_SURVEY_START_ID, this.duplicatedSurveyStartSiteID);
		JSONDataUtil.put(jsonObj, COLUMN_DUPLICATE_SURVEY_END_ID, this.duplicatedSurveyEndSiteID);
		
		//JSONDataUtil.put(jsonObj, COLUMN_FLAG_CAR_REINSPECT, this.flagSummaryCarInspect);
		//JSONDataUtil.put(jsonObj, COLUMN_IS_INSPECT_REPORT_GENERATED, (this.isInspectReportGenerated?1:0));
		//JSONDataUtil.put(jsonObj, COLUMN_NOT_UPLOAD_FLAG, this.notUploadFlag);
		return jsonObj;
	}
	public int getDuplicatedSurveyStartSiteID() {
		return duplicatedSurveyStartSiteID;
	}
	public void setDuplicatedSurveyStartSiteID(int duplicatedSurveyStartSiteID) {
		this.duplicatedSurveyStartSiteID = duplicatedSurveyStartSiteID;
	}
	public int getDuplicatedSurveyEndSiteID() {
		return duplicatedSurveyEndSiteID;
	}
	public void setDuplicatedSurveyEndSiteID(int duplicatedSurveyEndSiteID) {
		this.duplicatedSurveyEndSiteID = duplicatedSurveyEndSiteID;
	}
   public String getFlagSummaryCarInspect() {
      if (flagSummaryCarInspect == null)return "N";
      
      return flagSummaryCarInspect;
   }
   public void setFlagSummaryCarInspect(String flagSummaryCarInspect) {
      this.flagSummaryCarInspect = flagSummaryCarInspect;
   }
}

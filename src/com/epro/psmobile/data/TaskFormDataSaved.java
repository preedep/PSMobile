package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.data.TaskControlTemplate.TaskControlType;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;

public class TaskFormDataSaved implements DbCursorHolder, 
TransactionStmtHolder,
JSONDataHolder
{

	public final static String COLUMN_TASK_FORM_TEMPLATE_ID = "taskFormTemplateID";
	public final static String COLUMN_TASK_CONTROL_TYPE = "taskControlType";
	public final static String COLUMN_TASK_CONTROL_NO = "taskControlNo";
	public final static String COLUMN_TASK_FORM_ATTRIBUTE_ID = "taskFormAttributeID";
	public final static String COLUMN_TASK_ID = "taskID";
	public final static String COLUMN_TASK_CODE = "taskCode";
	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_TASK_DATA_VALUES = "taskDataValues";
	public final static String COLUMN_REASON_SENTENCE_TYPE = "chooseReasonSentenceType";
	public final static String COLUMN_REASON_SENTENCE_ID = "chooseReasonSentenceID";
	public final static String COLUMN_REASON_SENTENCE_TEXT = "chooseReasonSentenceText";
	public final static String COLUMN_REASON_SENTENCE_PATH = "chooseReasonSentencePath";
	public final static String COLUMN_PARENT_ID = "parentID";
	
	private int taskFormTemplateID;
	private TaskControlType taskControlType = TaskControlType.SimpleText;
	private int taskControlNo;
	private int taskFormAttributeID;
	private int taskID;
	private String taskCode;
	private int jobRequestID;
	private String taskDataValues;
	private String parentID;
	private ReasonSentence reasonSentence;
	
	public TaskFormDataSaved() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		if (this.reasonSentence == null){
			this.reasonSentence = new ReasonSentence();
		}
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into TaskFormDataSaved");
		strBld.append("(");
		strBld.append(COLUMN_TASK_FORM_TEMPLATE_ID+",");
		strBld.append(COLUMN_TASK_FORM_ATTRIBUTE_ID+",");
		strBld.append(COLUMN_TASK_CONTROL_TYPE+",");
		strBld.append(COLUMN_TASK_CONTROL_NO+",");
		strBld.append(COLUMN_TASK_ID+",");
		strBld.append(COLUMN_TASK_CODE+",");
		strBld.append(COLUMN_JOB_REQUEST_ID+",");
		strBld.append(COLUMN_TASK_DATA_VALUES+",");
		strBld.append(COLUMN_REASON_SENTENCE_TYPE+",");
		strBld.append(COLUMN_REASON_SENTENCE_ID+",");
		strBld.append(COLUMN_REASON_SENTENCE_TEXT+",");
		strBld.append(COLUMN_REASON_SENTENCE_PATH+",");
		strBld.append(COLUMN_PARENT_ID);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.getTaskFormTemplateID()+",");
		strBld.append(this.getTaskFormAttributeID()+",");
		strBld.append(this.getTaskControlType().getCode()+",");
		strBld.append(this.getTaskControlNo()+",");
		strBld.append(this.getTaskID()+",");
		strBld.append("'"+this.getTaskCode()+"',");
		strBld.append(this.getJobRequestID()+",");
		strBld.append("'"+this.getTaskDataValues()+"',");
		strBld.append("'"+this.reasonSentence.getReasonTypeCode()+"',");
		strBld.append(""+this.reasonSentence.getReasonID()+",");
		strBld.append("'"+this.reasonSentence.getReasonText()+"',");
		strBld.append("'"+this.reasonSentence.getReasonSentencePath()+"',");
		strBld.append("'"+this.parentID+"'");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.taskFormTemplateID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_FORM_TEMPLATE_ID));
		this.taskFormAttributeID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_FORM_ATTRIBUTE_ID));
		
		this.taskControlType = TaskControlType.getControlType(
				cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_CONTROL_TYPE)));
		this.taskControlNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_CONTROL_NO));
		this.taskID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID));
		this.taskCode = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_CODE));
		this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COLUMN_JOB_REQUEST_ID));
		this.taskDataValues = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATA_VALUES));
		
		this.reasonSentence = new ReasonSentence();
		
		this.reasonSentence.setReasonID(cursor.getInt(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_ID)));
		this.reasonSentence.setReasonText(cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_TEXT)));
		this.reasonSentence.setReasonTypeCode(cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_TYPE)));
		
		this.reasonSentence.setReasonSentencePath(cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_PATH)));
		this.parentID = cursor.getString(cursor.getColumnIndex(COLUMN_PARENT_ID));
	}

	public int getTaskFormTemplateID() {
		return taskFormTemplateID;
	}

	public void setTaskFormTemplateID(int taskFormTemplateID) {
		this.taskFormTemplateID = taskFormTemplateID;
	}

	public int getTaskFormAttributeID() {
		return taskFormAttributeID;
	}

	public void setTaskFormAttributeID(int taskFormAttributeID) {
		this.taskFormAttributeID = taskFormAttributeID;
	}

	public TaskControlType getTaskControlType() {
		return taskControlType;
	}

	public void setTaskControlType(TaskControlType taskControlType) {
		this.taskControlType = taskControlType;
	}

	public int getTaskControlNo() {
		return taskControlNo;
	}

	public void setTaskControlNo(int taskControlNo) {
		this.taskControlNo = taskControlNo;
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

	public int getJobRequestID() {
		return jobRequestID;
	}

	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}

	public String getTaskDataValues() {
		return taskDataValues;
	}

	public void setTaskDataValues(String taskDataValues) {
		this.taskDataValues = taskDataValues;
	}

	public ReasonSentence getReasonSentence() {
		return reasonSentence;
	}

	public void setReasonSentence(ReasonSentence reasonSentence) {
		this.reasonSentence = reasonSentence;
	}

	public String getValueAt(int position)
	{
		if (this.taskDataValues != null){
			String[] values = this.taskDataValues.split("@@");
			return values[position];
		}
		return null;
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		/*
		public final static String COLUMN_TASK_FORM_TEMPLATE_ID = "taskFormTemplateID";
	public final static String COLUMN_TASK_CONTROL_TYPE = "taskControlType";
	public final static String COLUMN_TASK_CONTROL_NO = "taskControlNo";
	public final static String COLUMN_TASK_ID = "taskID";
	public final static String COLUMN_TASK_CODE = "taskCode";
	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_TASK_DATA_VALUES = "taskDataValues";
		 */
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj, COLUMN_TASK_FORM_TEMPLATE_ID, this.taskFormTemplateID);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_FORM_ATTRIBUTE_ID, this.taskFormAttributeID);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_CONTROL_TYPE, this.taskControlType.getCode());
		JSONDataUtil.put(jsonObj, COLUMN_TASK_CONTROL_NO, this.taskControlNo);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_ID, this.taskID);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_CODE, this.taskCode);
		JSONDataUtil.put(jsonObj, COLUMN_JOB_REQUEST_ID, this.jobRequestID);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_DATA_VALUES, this.taskDataValues);
		return jsonObj;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
}

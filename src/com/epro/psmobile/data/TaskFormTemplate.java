package com.epro.psmobile.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;

public class TaskFormTemplate implements DbCursorHolder , JSONDataHolder , TransactionStmtHolder
{

	
	public final static String COLUMN_TASK_FROM_TEMPLATE_ID = "taskFormTemplateID";
	public final static String COLUMN_TASK_CONTROL_TYPE = "taskControlType";
	public final static String COLUMN_TASK_CONTROL_NO = "taskControlNo";
	public final static String COLUMN_TASK_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_TEXT_QUESTION = "textQuestion";
	public final static String COLUMN_CHOICE_TEXT_MATRIX_COLUMNS = "choiceTextMatrixColumns";
	public final static String COLUMN_CHOICE_TEXTS = "choiceTexts";
	public final static String COLUMN_CHOICE_VALUES = "choiceValues";
	public final static String COLUMN_MIN_SLIDER = "minSlider";
	public final static String COLUMN_MAX_SLIDER = "maxSlider";
	public final static String COLUMN_STEP_SLIDER = "stepSlider";
	public final static String COLUMN_REASON_SENTENCE_ID = "reasonSentenceID";
	public final static String COLUMN_REASON_SENTENCE_TYPE = "reasonSentenceType";
	
	public final static String COLUMN_TASK_FORM_ATTR_ID = "taskFormAttributeID";
	
	public final static String COLUMN_PARENT_ID = "parentId";
	public final static String COLUMN_PATH = "path";// : TEXT,

	public final static String COLUMN_IS_QC_TEAM = "isQCTemplate";
	
	private int taskFormTemplateId;
	private TaskControlTemplate.TaskControlType controlType = TaskControlTemplate.TaskControlType.SimpleText;
	private int taskControlNo;
	private int jobRequestID;
	private String textQuestion;
	private String choiceTextMatrixColumns;
	private String choiceTexts;
	private String choiceValues;
	private int minSlider;
	private int maxSlider;
	private int stepSlider;
	private int reasonSentenceID;
	private String reasonSentenceType;
	private int taskFormAttributeID;
	private String parentId;
	private String path;
	private boolean isQCTeam;
	
	private TaskFormDataSaved dataSaved;
	
	private ArrayList<ReasonSentence> reasonSentenceList;
	
	private ArrayList<TaskFormTemplate> childTaskFormTemplate;
	
	public TaskFormTemplate() {
		// TODO Auto-generated constructor stub
	}

	public int getTaskFormTemplateId() {
		return taskFormTemplateId;
	}

	public void setTaskFormTemplateId(int taskFormTemplateId) {
		this.taskFormTemplateId = taskFormTemplateId;
	}

	public TaskControlTemplate.TaskControlType getControlType() {
		return controlType;
	}

	public void setControlType(TaskControlTemplate.TaskControlType controlType) {
		this.controlType = controlType;
	}

	public int getTaskControlNo() {
		return taskControlNo;
	}

	public void setTaskControlNo(int taskControlNo) {
		this.taskControlNo = taskControlNo;
	}

	public int getJobRequestID() {
		return jobRequestID;
	}

	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}

	
	public int getMinSlider() {
		return minSlider;
	}

	public void setMinSlider(int minSlider) {
		this.minSlider = minSlider;
	}

	public int getMaxSlider() {
		return maxSlider;
	}

	public void setMaxSlider(int maxSlider) {
		this.maxSlider = maxSlider;
	}

	public int getStepSlider() {
		return stepSlider;
	}

	public void setStepSlider(int stepSlider) {
		this.stepSlider = stepSlider;
	}

	public String getTextQuestion() {
		return textQuestion;
	}

	public void setTextQuestion(String textQuestion) {
		this.textQuestion = textQuestion;
	}

	public String getChoiceTextMatrixColumns() {
		return choiceTextMatrixColumns;
	}

	public void setChoiceTextMatrixColumns(String choiceTextMatrixColumns) {
		this.choiceTextMatrixColumns = choiceTextMatrixColumns;
	}

	public String getChoiceTexts() {
		return choiceTexts;
	}

	public void setChoiceTexts(String choiceTexts) {
		this.choiceTexts = choiceTexts;
	}

	public String getChoiceValues() {
		return choiceValues;
	}

	public void setChoiceValues(String choiceValues) {
		this.choiceValues = choiceValues;
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

	public int getTaskFormAttributeID() {
		return taskFormAttributeID;
	}

	public void setTaskFormAttributeID(int taskFormAttributeID) {
		this.taskFormAttributeID = taskFormAttributeID;
	}

	public TaskFormDataSaved getDataSaved() {
		return dataSaved;
	}

	public void setDataSaved(TaskFormDataSaved dataSaved) {
		this.dataSaved = dataSaved;
	}

	public ArrayList<ReasonSentence> getChildReasonSentenceList() {
		return reasonSentenceList;
	}

	public void setChildReasonSentenceList(ArrayList<ReasonSentence> reasonSentenceList) {
		this.reasonSentenceList = reasonSentenceList;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.setTaskFormTemplateId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_FROM_TEMPLATE_ID)));
		int controlTypeId = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_CONTROL_TYPE));
		this.setControlType(controlType.getControlType(controlTypeId));
		this.setTaskControlNo(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_CONTROL_NO)));
		this.setJobRequestID(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_JOB_REQUEST_ID)));
		this.setTextQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT_QUESTION)));
		this.setChoiceTextMatrixColumns(cursor.getString(cursor.getColumnIndex(COLUMN_CHOICE_TEXT_MATRIX_COLUMNS)));
		this.setChoiceTexts(cursor.getString(cursor.getColumnIndex(COLUMN_CHOICE_TEXTS)));
		this.setChoiceValues(cursor.getString(cursor.getColumnIndex(COLUMN_CHOICE_VALUES)));
		this.setMinSlider(cursor.getInt(cursor.getColumnIndex(COLUMN_MIN_SLIDER)));
		this.setMaxSlider(cursor.getInt(cursor.getColumnIndex(COLUMN_MAX_SLIDER)));
		this.setStepSlider(cursor.getInt(cursor.getColumnIndex(COLUMN_STEP_SLIDER)));
		this.setReasonSentenceID(cursor.getInt(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_ID)));
		this.setReasonSentenceType(cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_TYPE)));
		this.setTaskFormAttributeID(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_FORM_ATTR_ID)));
		
		this.setParentId(cursor.getString(cursor.getColumnIndex(COLUMN_PARENT_ID)));
		this.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_PATH)));
		
		try{
			this.isQCTeam = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_QC_TEAM)));
		}catch(Exception ex){}
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.taskFormTemplateId = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_FROM_TEMPLATE_ID);
		int controlTypeId = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_CONTROL_TYPE);
		
		this.controlType = TaskControlTemplate.getControlType(controlTypeId);
		
		this.taskControlNo = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_CONTROL_NO);
		this.jobRequestID = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_JOB_REQUEST_ID);
		this.textQuestion = JSONDataUtil.getString(jsonObj, COLUMN_TEXT_QUESTION);
		this.choiceTextMatrixColumns = JSONDataUtil.getString(jsonObj, 
				COLUMN_CHOICE_TEXT_MATRIX_COLUMNS);

		this.choiceTexts = JSONDataUtil.getString(jsonObj, COLUMN_CHOICE_TEXTS);
		this.minSlider = JSONDataUtil.getInt(jsonObj, COLUMN_MIN_SLIDER);
		this.maxSlider = JSONDataUtil.getInt(jsonObj, COLUMN_MAX_SLIDER);
		
		this.stepSlider = JSONDataUtil.getInt(jsonObj, COLUMN_STEP_SLIDER);
		
		this.reasonSentenceID = JSONDataUtil.getInt(jsonObj, COLUMN_REASON_SENTENCE_ID);
		this.reasonSentenceType = JSONDataUtil.getString(jsonObj, COLUMN_REASON_SENTENCE_TYPE);
		
		this.taskFormAttributeID = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_FORM_ATTR_ID);
		
		this.parentId = JSONDataUtil.getString(jsonObj, COLUMN_PARENT_ID);
		this.path = JSONDataUtil.getString(jsonObj, COLUMN_PATH);
		
		int i_isQcTeam = JSONDataUtil.getInt(jsonObj, COLUMN_IS_QC_TEAM);
		this.isQCTeam = (i_isQcTeam > 0);
	}

	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		
		/*
		public final static String COLUMN_TASK_FROM_TEMPLATE_ID = "taskFormTemplateID";
		public final static String COLUMN_TASK_CONTROL_TYPE = "taskControlType";
		public final static String COLUMN_TASK_CONTROL_NO = "taskControlNo";
		public final static String COLUMN_TASK_JOB_REQUEST_ID = "jobRequestID";
		public final static String COLUMN_TEXT_QUESTION = "textQuestion";
		public final static String COLUMN_CHOICE_TEXT_MATRIX_COLUMNS = "choiceTextMatrixColumns";
		public final static String COLUMN_CHOICE_TEXTS = "choiceTexts";
		public final static String COLUMN_CHOICE_VALUES = "choiceValues";
		public final static String COLUMN_MIN_SLIDER = "minSlider";
		public final static String COLUMN_MAX_SLIDER = "maxSlider";
		public final static String COLUMN_STEP_SLIDER = "stepSlider";
		public final static String COLUMN_REASON_SENTENCE_ID = "reasonSentenceID";
		public final static String COLUMN_REASON_SENTENCE_TYPE = "reasonSentenceType";

	 */

		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into TaskFormTemplate");
		strBld.append("(");
		strBld.append(COLUMN_TASK_FROM_TEMPLATE_ID+",");
		strBld.append(COLUMN_TASK_CONTROL_TYPE+",");
		strBld.append(COLUMN_TASK_CONTROL_NO+",");
		strBld.append(COLUMN_TASK_JOB_REQUEST_ID+",");
		strBld.append(COLUMN_TEXT_QUESTION+",");
		strBld.append(COLUMN_CHOICE_TEXT_MATRIX_COLUMNS+",");
		strBld.append(COLUMN_CHOICE_TEXTS+",");
		strBld.append(COLUMN_MIN_SLIDER+",");
		strBld.append(COLUMN_MAX_SLIDER+",");
		strBld.append(COLUMN_STEP_SLIDER+",");
		strBld.append(COLUMN_REASON_SENTENCE_ID+",");
		strBld.append(COLUMN_REASON_SENTENCE_TYPE+",");
		strBld.append(COLUMN_TASK_FORM_ATTR_ID+",");
		strBld.append(COLUMN_PARENT_ID+",");
		strBld.append(COLUMN_PATH+",");
		strBld.append(COLUMN_IS_QC_TEAM);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.taskFormTemplateId+",");
		strBld.append(this.controlType.getCode()+",");
		strBld.append(this.taskControlNo+",");
		strBld.append(this.jobRequestID+",");
		strBld.append("'"+this.textQuestion+"',");
		strBld.append("'"+this.choiceTextMatrixColumns+"',");
		strBld.append("'"+this.choiceTexts+"',");
		strBld.append(this.minSlider+",");
		strBld.append(this.maxSlider+",");
		strBld.append(this.stepSlider+",");
		strBld.append(this.reasonSentenceID+",");
		strBld.append("'"+this.reasonSentenceType+"',");
		strBld.append(""+this.taskFormAttributeID+",");
		strBld.append("'"+this.parentId+"',");
		strBld.append("'"+this.path+"',");
		strBld.append("'"+Boolean.toString(this.isQCTeam)+"'");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void addChildTaskFormTemplate(TaskFormTemplate childFormTemplate)
	{
		if (this.childTaskFormTemplate == null){
			this.childTaskFormTemplate = new ArrayList<TaskFormTemplate>();
		}
		this.childTaskFormTemplate.add(childFormTemplate);
	}
	public boolean isQCTeam() {
		return isQCTeam;
	}

	public void setQCTeam(boolean isQCTeam) {
		this.isQCTeam = isQCTeam;
	}

	public ArrayList<TaskFormTemplate> getChildTaskFormTemplate() {
		return childTaskFormTemplate;
	}

	public void setChildTaskFormTemplate(ArrayList<TaskFormTemplate> childTaskFormTemplate) {
		this.childTaskFormTemplate = childTaskFormTemplate;
	}


}

package com.epro.psmobile.data.upload;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class CheckListFormDetail implements UploadDataAdapter,JSONDataHolder {

	public final static String COLUMN_REQUEST_ID = "requestId";
	public final static String COLUMN_ATTRIBUTE_ID = "attributeId";
	public final static String COLUMN_JOB_ROW_ID = "jobRowId";
	public final static String COLUMN_JOB_LOCATION_ID = "jobLocationId";
	public final static String COLUMN_JOB_NO = "jobDocumentNo";
	public final static String COLUMN_CTRL_NO = "ctrlNo";
	public final static String COLUMN_TYPE = "type";
	public final static String COLUMN_VALUE = "value";
	public final static String COLUMN_CHOOSE_REASON_SENTENCE_TYPE = "chooseReasonSentenceType";
	public final static String COLUMN_CHOOSE_REASON_SENTENCE_ID = "chooseReasonSentenceID";
	public final static String COLUMN_CHOOSE_REASON_SENTENCE_TEXT = "chooseReasonSentenceText";
	
	public final static String COLUMN_PARENT_ID = "parentId";
	public final static String COLUMN_PATH = "path";// : TEXT,

	public final static String COLUMN_IS_QC_TEAM = "isQCTemplate";
		
	private int requestId;
	private int attributeId;
	private int jobRowId;
	private int jobLocationId;
	private String jobNo;
	private String type;
	private String value;
	private int ctrlNo;
	private String chooseReasonSentenceType;
	private int chooseReasonSentenceID;
	private String chooseReasonSentenceText;
	
	private int resultProductRowId;
	public String parentId;
	public String path;
	
	public boolean isQCTeam;
	
	/*
	 * SimpleText(0),
		SimpleTextDecimal(1),
		SimpleTextDate(2),
		CheckBoxList(3),
		RadioBoxList(4),
		RadioBoxMatrix(5),
		Slider(6),
		Dropdownlist(7);
		
		- type : String , taskControlType  
		 (0 : 'textarea', 
		 1 : 'decimal', 
		 2 : 'datetime', 
		 3 : 'checkbox', 4 : 'radio', 5 : ไม่มี, 6 : ไม่มี, 7 : 'dropdown', 8 : 'text')


Label(10),
        SimpleTextSingleLine(11),
        SimpleTextDecimalSingleLine(12);
        
        
        
        SimpleText(0),
        SimpleTextDecimal(1),
        SimpleTextDate(2),
        CheckBoxList(3),
        RadioBoxList(4),
        RadioBoxMatrix(5),
        Slider(6),
        Dropdownlist(7),
        RadioBoxListAndDropdown(9),
        Label(10),
        SimpleTextSingleLine(11),
        SimpleTextDecimalSingleLine(12);
	 */
	private String[] typeArray = new String[]{
			"textarea",
			"decimal",
			"datetime",
			"checkbox",
			"radio",
			"-",
			"-",
			"dropdown",
			"radiolistanddropdown",
			"text",
			"textareasingleline",
			"decimalsingleline"
			};
	private Task task;
//	private CustomerSurveySite site;
	private TaskFormDataSaved dataSaved;
	public CheckListFormDetail(Task task,
			TaskFormDataSaved dataSaved,
			String parentId,
			String path){
		
		this.task = task;
		this.dataSaved = dataSaved;
		this.parentId = parentId;
		this.path = path;
	}
	@Override
	public void executeAdapter() {
		// TODO Auto-generated method stub

		this.requestId = dataSaved.getTaskFormTemplateID();
		this.jobRowId = task.getTaskID();
//		this.jobLocationId = site.getCustomerSurveySiteRowID();
		this.jobNo = task.getTaskCode();
		
		if (this.dataSaved != null)
		{
			this.attributeId = dataSaved.getTaskFormAttributeID();

			this.type = typeArray[dataSaved.getTaskControlType().getCode()];
			this.value = dataSaved.getTaskDataValues();
			if (this.value.indexOf("@@") != -1){
				String values[] = this.value.split("@@");
				if (values.length > 1)
				{
					this.value = values[1];
				}
			}
			this.ctrlNo = dataSaved.getTaskControlNo();
			
			if (dataSaved.getReasonSentence() != null){
				this.chooseReasonSentenceID = dataSaved.getReasonSentence().getReasonID();
				this.chooseReasonSentenceType = dataSaved.getReasonSentence().getReasonTypeCode();
				this.chooseReasonSentenceText = dataSaved.getReasonSentence().getReasonText();
			}
		}
		
		
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChooseReasonSentenceType() {
		return chooseReasonSentenceType;
	}

	public void setChooseReasonSentenceType(String chooseReasonSentenceType) {
		this.chooseReasonSentenceType = chooseReasonSentenceType;
	}

	public int getChooseReasonSentenceID() {
		return chooseReasonSentenceID;
	}

	public void setChooseReasonSentenceID(int chooseReasonSentenceID) {
		this.chooseReasonSentenceID = chooseReasonSentenceID;
	}

	public String getChooseReasonSentenceText() {
		return chooseReasonSentenceText;
	}

	public void setChooseReasonSentenceText(String chooseReasonSentenceText) {
		this.chooseReasonSentenceText = chooseReasonSentenceText;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		
		JSONDataUtil.put(jsonObj,COLUMN_REQUEST_ID,this.requestId);
		JSONDataUtil.put(jsonObj,COLUMN_ATTRIBUTE_ID ,attributeId);
		JSONDataUtil.put(jsonObj,COLUMN_JOB_ROW_ID ,jobRowId);
		JSONDataUtil.put(jsonObj,COLUMN_JOB_LOCATION_ID ,jobLocationId);
		JSONDataUtil.put(jsonObj,COLUMN_JOB_NO ,jobNo);
		JSONDataUtil.put(jsonObj,COLUMN_CTRL_NO ,ctrlNo);
		JSONDataUtil.put(jsonObj,COLUMN_TYPE ,type);
		JSONDataUtil.put(jsonObj, COLUMN_VALUE ,value);
		JSONDataUtil.put(jsonObj,COLUMN_CHOOSE_REASON_SENTENCE_TYPE ,chooseReasonSentenceType);
		JSONDataUtil.put(jsonObj,COLUMN_CHOOSE_REASON_SENTENCE_ID ,chooseReasonSentenceID);
		JSONDataUtil.put(jsonObj,COLUMN_CHOOSE_REASON_SENTENCE_TEXT ,chooseReasonSentenceText);
		
		JSONDataUtil.put(jsonObj, "resultProductRowId", this.dataSaved.getProductRowId());
		
		JSONDataUtil.put(jsonObj, COLUMN_PARENT_ID, this.parentId);
		JSONDataUtil.put(jsonObj, COLUMN_PATH, this.path);
		JSONDataUtil.put(jsonObj, COLUMN_IS_QC_TEAM, (this.isQCTeam?1:0));
		return jsonObj;
	}
	public int getCtrlNo() {
		return ctrlNo;
	}
	public void setCtrlNo(int ctrlNo) {
		this.ctrlNo = ctrlNo;
	}
	/**
	 * @return the isQCTeam
	 */
	public boolean isQCTeam() {
		return isQCTeam;
	}
	/**
	 * @param isQCTeam the isQCTeam to set
	 */
	public void setQCTeam(boolean isQCTeam) {
		this.isQCTeam = isQCTeam;
	}
   public int getResultProductRowId() {
      return resultProductRowId;
   }
   public void setResultProductRowId(int resultProductRowId) {
      this.resultProductRowId = resultProductRowId;
   }

}

package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class ReasonSentence implements DbCursorHolder , Parcelable , JSONDataHolder , TransactionStmtHolder
{

	public enum ReasonType
	{
		REASON_NONE(""),
		//REASON_REJECT_REQIEST("request_reject_reason"),
		//REASON_CHANGE_TASK_PLAN("opinion_edit_job_plan"),
		//REASON_NOT_APPROVED_INSPECT("opinion_reject_result");
		REASON_REJECT_REQIEST("request_reject_reason"),
		REASON_CHANGE_TASK_PLAN("opinion_edit_job_plan"),
		REASON_NOT_APPROVED_INSPECT("opinion_reject_result"),
		REASON_OTHER("999");
		private String typeCode;
		ReasonType(String typeCode)
		{
			this.typeCode = typeCode;
		}
		public String getTypeCode(){
			return typeCode;
		}
		public static ReasonType getType(String typeCode)
		{
			for(int i = 0 ; i < values().length;i++){
				if (typeCode.equalsIgnoreCase(values()[i].getTypeCode())){
					return values()[i];
				}
			}			
			return ReasonType.REASON_OTHER;
		}
	}
	public final static String COLUMN_REASON_SENTENCE_ID = "reasonSentenceID";
	public final static String COLUMN_REASON_SENTENCE_TYPE = "reasonSentenceType";
	public final static String COLUMN_REASON_SENTENCE_TEXT = "reasonSentenceText";
	public final static String COLUMN_REASON_SENTENCE_PATH = "reasonSentencePath";
	
	private int reasonID;
	private String reasonText;
	private String shortName;
	private ReasonType reasonType;
	private String reasonTypeCode = "";
	private String reasonSentencePath;
	
	public static final Parcelable.Creator<ReasonSentence> CREATOR = new Parcelable.Creator<ReasonSentence>()
	{

		@Override
		public ReasonSentence createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ReasonSentence(source);
		}

		@Override
		public ReasonSentence[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ReasonSentence[size];
		}
		
	};
	public ReasonSentence(Parcel source) {
		// TODO Auto-generated constructor stub
		this.setReasonID(source.readInt());
		this.setReasonType(ReasonType.getType(source.readString()));
		this.setReasonText(source.readString());
		this.setReasonTypeCode(source.readString());
		this.setReasonSentencePath(source.readString());
	}
	public ReasonSentence(){
		
	}

	public int getReasonID() {
		return reasonID;
	}
	public void setReasonID(int reasonID) {
		this.reasonID = reasonID;
	}
	public String getReasonText() {
		return reasonText;
	}

	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public ReasonType getReasonType() {
		return reasonType;
	}

	public void setReasonType(ReasonType reasonType) {
		this.reasonType = reasonType;
	}

	public String getReasonTypeCode() {
		return reasonTypeCode;
	}
	public void setReasonTypeCode(String reasonTypeCode) {
		this.reasonTypeCode = reasonTypeCode;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.reasonID = cursor.getInt(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_ID));
		String iType = cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_TYPE));
		this.reasonType = ReasonType.getType(iType);
		if (this.reasonType == ReasonType.REASON_OTHER)
		{
			this.reasonTypeCode = iType;
		}
		this.reasonText = cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_TEXT));
		this.reasonSentencePath = cursor.getString(cursor.getColumnIndex(COLUMN_REASON_SENTENCE_PATH));
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(this.getReasonID());
		dest.writeString(this.getReasonType().getTypeCode());
		dest.writeString(this.getReasonText());
		dest.writeString(this.getReasonTypeCode());
		dest.writeString(this.getReasonSentencePath());
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.reasonID = JSONDataUtil.getInt(jsonObj, COLUMN_REASON_SENTENCE_ID);
		
		String iReasonTypeId = JSONDataUtil.getString(jsonObj,COLUMN_REASON_SENTENCE_TYPE);
		this.reasonType = ReasonType.getType(iReasonTypeId);
		if (reasonType == ReasonType.REASON_OTHER)
		{
			this.reasonTypeCode = iReasonTypeId;
		}
		
		this.reasonText = JSONDataUtil.getString(jsonObj, COLUMN_REASON_SENTENCE_TEXT);
		this.reasonSentencePath = JSONDataUtil.getString(jsonObj, COLUMN_REASON_SENTENCE_PATH);
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
		strBld.append("insert into ReasonSentence");
		strBld.append("(");
		strBld.append(COLUMN_REASON_SENTENCE_ID+",");
		strBld.append(COLUMN_REASON_SENTENCE_TYPE+",");
		strBld.append(COLUMN_REASON_SENTENCE_TEXT+",");
		strBld.append(COLUMN_REASON_SENTENCE_PATH);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.reasonID+",");
		if (this.reasonType == ReasonType.REASON_OTHER){
			strBld.append("'"+this.reasonTypeCode+"',");			
		}else{
			strBld.append("'"+this.reasonType.getTypeCode()+"',");
		}
		strBld.append("'"+this.reasonText+"',");
		strBld.append("'"+this.reasonSentencePath+"'");
		strBld.append(")");
		
		return strBld.toString();
	}
	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getReasonSentencePath() {
		return reasonSentencePath;
	}
	public void setReasonSentencePath(String reasonSentencePath) {
		this.reasonSentencePath = reasonSentencePath;
	}

}

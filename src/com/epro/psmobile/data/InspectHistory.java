package com.epro.psmobile.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class InspectHistory implements DbCursorHolder, TransactionStmtHolder,Parcelable {

	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_TASK_NO = "taskNo";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	public final static String COLUMN_IMAGE_FILE_LAST_LAYOUT = "imageFileLastLayout";
	public final static String COLUMN_IMAGE_FILE_LAST_RESULT = "imageFileLastResult";
	
	
	private int jobRequestID;
	private int taskNo;
	private int customerSurveySiteID;
	private String imageFileLastLayout;
	private String imageFileLastResult;
	
	public static final Parcelable.Creator<InspectHistory> CREATOR = new Parcelable.Creator<InspectHistory>()
	{

		@Override
		public InspectHistory createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new InspectHistory(source);
		}

		@Override
		public InspectHistory[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InspectHistory[size];
		}
		
	};
	public InspectHistory(Parcel source)
	{
		/*
		 	private int jobRequestID;
	private int taskNo;
	private int customerSurveySiteID;
	private String imageFileLastLayout;
	private String imageFileLastResult;

		 */
		this.jobRequestID = source.readInt();
		this.taskNo = source.readInt();
		this.customerSurveySiteID = source.readInt();
		this.imageFileLastLayout = source.readString();
		this.imageFileLastResult = source.readString();
		
	}
	public InspectHistory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("delete from InspectHistory");
		strBld.append(" where ");
		strBld.append(COLUMN_JOB_REQUEST_ID+"="+this.getJobRequestID());
		strBld.append(" and ");
		strBld.append(COLUMN_TASK_NO+"="+this.getTaskNo());
		strBld.append(" and ");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+"="+this.getCustomerSurveySiteID());
		return strBld.toString();
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into InspectHistory");
		strBld.append("(");
		strBld.append(COLUMN_JOB_REQUEST_ID+",");
		strBld.append(COLUMN_TASK_NO+",");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+",");
		strBld.append(COLUMN_IMAGE_FILE_LAST_LAYOUT+",");
		strBld.append(COLUMN_IMAGE_FILE_LAST_RESULT);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.getJobRequestID()+",");
		strBld.append(this.getTaskNo()+",");
		strBld.append(this.getCustomerSurveySiteID()+",");
		strBld.append("'"+this.getImageFileLastLayout()+"',");
		strBld.append("'"+this.getImageFileLastResult()+"'");
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
		this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COLUMN_JOB_REQUEST_ID));
		this.taskNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_NO));
		this.customerSurveySiteID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ID));
		this.imageFileLastLayout = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_FILE_LAST_LAYOUT));
		this.imageFileLastResult = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_FILE_LAST_RESULT));
	}

	public int getJobRequestID() {
		return jobRequestID;
	}

	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}

	public int getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
	}

	public int getCustomerSurveySiteID() {
		return customerSurveySiteID;
	}

	public void setCustomerSurveySiteID(int customerSurveySiteID) {
		this.customerSurveySiteID = customerSurveySiteID;
	}

	public String getImageFileLastLayout() {
		return imageFileLastLayout;
	}

	public void setImageFileLastLayout(String imageFileLastLayout) {
		this.imageFileLastLayout = imageFileLastLayout;
	}

	public String getImageFileLastResult() {
		return imageFileLastResult;
	}

	public void setImageFileLastResult(String imageFileLastResult) {
		this.imageFileLastResult = imageFileLastResult;
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
		 * private int jobRequestID;
	private int taskNo;
	private int customerSurveySiteID;
	private String imageFileLastLayout;
	private String imageFileLastResult;
		 */
		dest.writeInt(this.jobRequestID);
		dest.writeInt(this.taskNo);
		dest.writeInt(this.customerSurveySiteID);
		dest.writeString(this.imageFileLastLayout);
		dest.writeString(this.imageFileLastResult);
	}

}

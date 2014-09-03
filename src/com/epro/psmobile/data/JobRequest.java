package com.epro.psmobile.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class JobRequest implements DbCursorHolder , 
									Parcelable , 
									JSONDataHolder , 
									TransactionStmtHolder
{
	/*
	jobRequestID INTEGER PRIMARY KEY  AUTOINCREMENT,
	inspectTypeID INTEGER REFERENCES InspectType (inspectType),
	customerID INTEGER REFERENCES Customer (customerID),
	purposeName TEXT,
	typeOfSurvey TEXT,
	dateTimeRecievedMail TIMESTAMP
	 * */
	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_JOB_INSPECT_TYPE_ID = "inspectTypeID";
	//public final static String COLUMN_JOB_CUSTOMER_ID = "customerID";
	public final static String CUSTOMER_JOB_CUSTOMER_CODE = "customerCode";
	public final static String COLUMN_PURPOSE_NAME = "purposeName";
	public final static String COLUMN_TYPE_OF_SURVEY = "typeOfSurvey";
	public final static String COLUMN_BUSINESS_TYPE = "businessType";
	public final static String COLUMN_JOB_NOTES = "jobRequestNotes";
	private int jobRequestID;
	private InspectType inspectType;
	private int inspectTypeID;
	private String customerCode;
	private String purposeName;
	private String typeOfSurvey;
	private String businessType;
	private String jobRequestNotes;
	
	private ArrayList<CustomerSurveySite> customerSurveySiteList;
	
	
	public static final Parcelable.Creator<JobRequest> CREATOR = new Parcelable.Creator<JobRequest>()
	{

		@Override
		public JobRequest createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JobRequest(source);
		}

		@Override
		public JobRequest[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JobRequest[size];
		}
		
	};
	
	public JobRequest(Parcel source)
	{
		/*
		 dest.writeInt(this.getJobRequestID());
		dest.writeParcelable(getInspectType(), arg1);
		dest.writeString(this.getCustomerCode());
		dest.writeString(this.getPurposeName());
		dest.writeString(this.getTypeOfSurvey());
		 */
		this.setJobRequestID(source.readInt());
		this.setInspectType((InspectType)source.readParcelable(this.getClass().getClassLoader()));		
		this.setCustomerCode(source.readString());
		this.setPurposeName(source.readString());
		this.setTypeOfSurvey(source.readString());
		this.setBusinessType(source.readString());
		this.setJobRequestNotes(source.readString());
		try{			
			customerSurveySiteList = (ArrayList<CustomerSurveySite>)source.readArrayList(CustomerSurveySite.class.getClassLoader());
//			CustomerSurveySite[] surveySites =  (CustomerSurveySite[])source.readArray(CustomerSurveySite.class.getClassLoader());
//			CustomerSurveySite[] surveySites =  (CustomerSurveySite[]) source.readParcelableArray(this.getClass().getClassLoader());
//			if (surveySites != null)
//			{
//			if (this.customerSurveySiteList != null)
//				this.customerSurveySiteList.clear();
			
//				for(CustomerSurveySite site : surveySites)
//				{
//					this.addCustomerSurveySite(site);
//				}
//			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public JobRequest() {
		// TODO Auto-generated constructor stub
	}

	public int getJobRequestID() {
		return jobRequestID;
	}

	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}

	public int getInspectTypeID() {
		return inspectTypeID;
	}
	public void setInspectTypeID(int inspectTypeID) {
		this.inspectTypeID = inspectTypeID;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public InspectType getInspectType() {
		return inspectType;
	}

	public void setInspectType(InspectType inspectType) {
		this.inspectType = inspectType;
		if (this.inspectType != null){
		   this.inspectTypeID = this.inspectType.getInspectTypeID();
		}
	}


	public String getPurposeName() {
		return purposeName;
	}

	public void setPurposeName(String purposeName) {
		this.purposeName = purposeName;
	}

	public String getTypeOfSurvey() {
		return typeOfSurvey;
	}

	public void setTypeOfSurvey(String typeOfSurvey) {
		this.typeOfSurvey = typeOfSurvey;
	}

	public void addCustomerSurveySite(CustomerSurveySite surveySite)
	{
		if (customerSurveySiteList == null)
		{
			customerSurveySiteList = new ArrayList<CustomerSurveySite>();
		}
		customerSurveySiteList.add(surveySite);
	}
	public void addCustomerSurveySiteList(ArrayList<CustomerSurveySite> surveySiteList)
	{
		if (customerSurveySiteList == null)
		{
			customerSurveySiteList = new ArrayList<CustomerSurveySite>();
		}
		customerSurveySiteList.clear();
		if (surveySiteList != null)
			customerSurveySiteList.addAll(surveySiteList);
		
	}
	public ArrayList<CustomerSurveySite> getCustomerSurveySiteList(){
		return customerSurveySiteList;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		/*
		 *  jobRequestID INTEGER PRIMARY KEY  AUTOINCREMENT,
			inspectTypeID INTEGER REFERENCES InspectType (inspectType),
		    customerID INTEGER REFERENCES Customer (customerID),
			purposeName TEXT,
			typeOfSurvey TEXT,
			dateTimeRecievedMail TIMESTAMP
		 * */
		
		/*
		 jobRequestID INTEGER PRIMARY KEY  AUTOINCREMENT,
		 inspectTypeID INTEGER REFERENCES InspectType (inspectTypeID),
		 customerCode TEXT,
		 purposeName TEXT,
		 typeOfSurvey TEXT
		  */
		this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COLUMN_JOB_REQUEST_ID));

		this.inspectType = new InspectType();
		this.inspectType.onBind(cursor);
		
		this.customerCode = cursor.getString(cursor.getColumnIndex(CUSTOMER_JOB_CUSTOMER_CODE));
		
		this.purposeName = cursor.getString(cursor.getColumnIndex(COLUMN_PURPOSE_NAME));
		this.typeOfSurvey = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_OF_SURVEY));
		
	
		this.businessType = cursor.getString(cursor.getColumnIndex(COLUMN_BUSINESS_TYPE));
		this.jobRequestNotes = cursor.getString(cursor.getColumnIndex(COLUMN_JOB_NOTES));
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		/*
		 private int jobRequestID;
		 private InspectType inspectType;
		 private Customer customer;
		 private String purposeName;
		 private String typeOfSurvey;
		 private java.sql.Timestamp dateTimeRecievedMail;	
		 */
		dest.writeInt(this.getJobRequestID());
		dest.writeParcelable(getInspectType(), arg1);
		dest.writeString(this.getCustomerCode());
		dest.writeString(this.getPurposeName());
		dest.writeString(this.getTypeOfSurvey());
		dest.writeString(this.getBusinessType());
		dest.writeString(this.getJobRequestNotes());
		if (this.getCustomerSurveySiteList() != null){
			/*
			CustomerSurveySite[] surveySites = new CustomerSurveySite[this.getCustomerSurveySiteList().size()];
			for(int i = 0;i < surveySites.length;i++)
			{
				surveySites[i] = this.getCustomerSurveySiteList().get(i);
			}			
			*/
			dest.writeList(this.getCustomerSurveySiteList());
		}
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.jobRequestID = JSONDataUtil.getInt(jsonObj,COLUMN_JOB_REQUEST_ID);

		this.inspectTypeID = JSONDataUtil.getInt(jsonObj, COLUMN_JOB_INSPECT_TYPE_ID);
		this.customerCode = JSONDataUtil.getString(jsonObj,CUSTOMER_JOB_CUSTOMER_CODE);
		
		this.purposeName = JSONDataUtil.getString(jsonObj,COLUMN_PURPOSE_NAME);
		this.typeOfSurvey = JSONDataUtil.getString(jsonObj,COLUMN_TYPE_OF_SURVEY);
		
		this.businessType = JSONDataUtil.getString(jsonObj, COLUMN_BUSINESS_TYPE);
		
		this.jobRequestNotes = JSONDataUtil.getString(jsonObj, COLUMN_JOB_NOTES);
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
		strBld.append("insert into JobRequest");
		strBld.append("(");
		strBld.append(COLUMN_JOB_REQUEST_ID+",");
		strBld.append(COLUMN_JOB_INSPECT_TYPE_ID+",");
		strBld.append(CUSTOMER_JOB_CUSTOMER_CODE+",");
		strBld.append(COLUMN_PURPOSE_NAME+",");
		strBld.append(COLUMN_TYPE_OF_SURVEY+",");
		strBld.append(COLUMN_BUSINESS_TYPE+",");
		strBld.append(COLUMN_JOB_NOTES);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.jobRequestID+",");
		strBld.append(this.inspectTypeID+",");
		strBld.append("'"+this.customerCode+"',");
		strBld.append("'"+this.purposeName+"',");
		strBld.append("'"+this.typeOfSurvey+"',");
		strBld.append("'"+this.businessType+"',");
		strBld.append("'"+this.jobRequestNotes+"'");
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
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getJobRequestNotes() {
		return jobRequestNotes;
	}
	public void setJobRequestNotes(String jobRequestNotes) {
		this.jobRequestNotes = jobRequestNotes;
	}

}

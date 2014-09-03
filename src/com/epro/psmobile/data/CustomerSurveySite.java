package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class CustomerSurveySite implements DbCursorHolder , Parcelable , JSONDataHolder , TransactionStmtHolder
{
/*
 	customerSurveySiteID INTEGER PRIMARY KEY  AUTOINCREMENT,
	customerID INTEGER REFERENCES Customer (customerID),
	siteAddress TEXT,
	siteTels TEXT,
	siteEmails TEXT,
	siteLocLat REAL,
	siteLocLon REAL,
	siteMapImg BLOB,
	areaID INTEGER	
 */
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID = "customerSurveySiteRowID";
	public final static String COLUMN_CUSTOMER_JOBREQUEST_ID = "jobRequestID";
	public final static String COLUMN_TASK_ID = "taskID";
	public final static String COLUMN_CUSTOMER_ID = "customerID";
	public final static String COLUMN_CUSTOMER_CODE = "customerCode";
	public final static String COLUMN_CUSTOMER_NAME = "customerName";
	public final static String COLUMN_SITE_ADDRESS = "siteAddress";
	public final static String COLUMN_SITE_TELS = "siteTels";
	public final static String COLUMN_SITE_EMAILS = "siteEmails";
	public final static String COLUMN_SITE_LOC_LAT = "siteLocLat";
	public final static String COLUMN_SITE_LOC_LON = "siteLocLon";
	public final static String COLUMN_SITE_MAP_IMG = "siteMapImg";	
	public final static String COLUMN_AREA_ID = "areaID";
	public final static String COLUMN_PROVINCIAL_ID = "provincialID";
	public final static String COLUMN_CUSTOMER_ASSIST = "customerNameAssist";
	
	/*
	 * 
	 */
	public final static String COLUMN_LAYOUT_SVG_PATH =  "layoutSvgPath";
	public final static String COLUMN_RESULT_PDF_PATH =  "resultPdfPath";
	public final static String COLUMN_SITE_ADDR_KEY = "siteAddressKey";

	/*extra fields for car inspection*/
	public final static String COLUMN_LOCATION_COMPLETE_CHECK_DATE = "locationCompleteCheckDate";
	public final static String COLUMN_LOCATION_COMPLETE_CHECK_END_DATE = "locationCompleteCheckEndDate";
	public final static String COLUMN_MILE_NO = "mileNo";
	
	
	private int customerSurveySiteID;
	private int customerSurveySiteRowID;
	private int jobRequestID;
	private int taskID;
	private int customerID;
	private String customerCode;
	private String customerName;
	private String siteAddress;
	private String siteTels;
	private String siteEmails;
	private float siteLocLat;
	private float siteLocLon;
//	private Bitmap siteMapImg;
	private int areaID;
	private String checkLeader; //The check leader from customer
	private int provinceID;
	
	private String layoutSVGPath;
	private String resultPdfPath;
	
	private String siteAddressKey;
	
	private boolean isNewAddress = false;
	
	private String mileNo;
	private String locationCheckInDate;
	private String locationCheckOutDate;
	
	

   public static final Parcelable.Creator<CustomerSurveySite> CREATOR = new Parcelable.Creator<CustomerSurveySite>()
	{

		@Override
		public CustomerSurveySite createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new CustomerSurveySite(source);
		}

		@Override
		public CustomerSurveySite[] newArray(int size) {
			// TODO Auto-generated method stub
			return new CustomerSurveySite[size];
		}
		
	};
	public CustomerSurveySite(Parcel source)
	{
		/*
		dest.writeInt(this.getCustomerSurveySiteID());
		dest.writeString(this.getCustomerCode());
		dest.writeInt(this.getJobRequestID());
		dest.writeInt(this.getCustomerSurveySiteRowID());
		dest.writeInt(this.getCustomerID());
		dest.writeString(this.getSiteAddress());
		dest.writeString(this.getSiteTels());
		dest.writeString(this.getSiteEmails());
		dest.writeFloat(this.getSiteLocLat());
		dest.writeFloat(this.getSiteLocLon());
		dest.writeInt(this.getAreaID());
		dest.writeInt(this.getProvinceID());
		dest.writeString(this.getCheckLeader());
		 */
		this.setCustomerSurveySiteID(source.readInt());
		this.setCustomerCode(source.readString());
		this.setCustomerName(source.readString());
		this.setJobRequestID(source.readInt());
		this.setTaskID(source.readInt());
		this.setCustomerSurveySiteRowID(source.readInt());
		this.setCustomerID(source.readInt());
		this.setSiteAddress(source.readString());
		this.setSiteTels(source.readString());
		this.setSiteEmails(source.readString());
		this.setSiteLocLat(source.readFloat());
		this.setSiteLocLon(source.readFloat());
		this.setAreaID(source.readInt());
		this.setProvinceID(source.readInt());
		this.setCheckLeader(source.readString());
		
		/*
		 * 		dest.writeString(this.getLayoutSVGPath());
		dest.writeString(this.getResultPdfPath());
		 */
		this.setLayoutSVGPath(source.readString());
		this.setResultPdfPath(source.readString());
		this.setSiteAddressKey(source.readString());
		
		this.setLocationCheckInDate(source.readString());
		this.setLocationCheckOutDate(source.readString());
		this.setMileNo(source.readString());
	}
	public CustomerSurveySite() {
		// TODO Auto-generated constructor stub
		
	}

	public int getCustomerSurveySiteID() {
		return customerSurveySiteID;
	}

	public void setCustomerSurveySiteID(int customerSurveySiteID) {
		this.customerSurveySiteID = customerSurveySiteID;
	}

	public int getCustomerSurveySiteRowID() {
		return customerSurveySiteRowID;
	}
	public void setCustomerSurveySiteRowID(int customerSurveySiteRowID) {
		this.customerSurveySiteRowID = customerSurveySiteRowID;
	}
	public int getJobRequestID() {
		return jobRequestID;
	}
	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		if ((customerName == null)||
			(customerName.isEmpty())){
			customerName = "ABC Company Co.,Ltd.";
		}
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getSiteTels() {
		return siteTels;
	}

	public void setSiteTels(String siteTels) {
		this.siteTels = siteTels;
	}

	public String getSiteEmails() {
		return siteEmails;
	}

	public void setSiteEmails(String siteEmails) {
		this.siteEmails = siteEmails;
	}

	public float getSiteLocLat() {
		return siteLocLat;
	}

	public void setSiteLocLat(float siteLocLat) {
		this.siteLocLat = siteLocLat;
	}

	public float getSiteLocLon() {
		return siteLocLon;
	}

	public void setSiteLocLon(float siteLocLon) {
		this.siteLocLon = siteLocLon;
	}
/*
	public Bitmap getSiteMapImg() {
		return siteMapImg;
	}

	public void setSiteMapImg(Bitmap siteMapImg) {
		this.siteMapImg = siteMapImg;
	}
*/
	public int getAreaID() {
		return areaID;
	}

	public void setAreaID(int areaID) {
		this.areaID = areaID;
	}

	public String getCheckLeader() {
		return checkLeader;
	}
	public void setCheckLeader(String checkLeader) {
		this.checkLeader = checkLeader;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public int getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}
	public String getLayoutSVGPath() {
		return layoutSVGPath;
	}
	public void setLayoutSVGPath(String layoutSVGPath) {
		this.layoutSVGPath = layoutSVGPath;
	}
	public String getResultPdfPath() {
		return resultPdfPath;
	}
	public void setResultPdfPath(String resultPdfPath) {
		this.resultPdfPath = resultPdfPath;
	}
	public String getSiteAddressKey() {
		return siteAddressKey;
	}
	public void setSiteAddressKey(String siteAddressKey) {
		this.siteAddressKey = siteAddressKey;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		/*
			customerSurveySiteID INTEGER,
			customerCode TEXT,
			jobRequestID INTEGER,
			customerSurveySiteRowID INTEGER,
			customerID INTEGER,
			siteAddress TEXT,
			siteTels TEXT,
			siteEmails TEXT,
			siteLocLat REAL,
			siteLocLon REAL,
			areaID INTEGER,
			provincialID INTEGER,
			customerNameAssist TEXT,
	 */
		this.customerSurveySiteID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ID));
		this.customerCode = cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_CODE));
		this.customerName = cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME));
		this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_JOBREQUEST_ID));
		this.taskID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID));
		this.customerSurveySiteRowID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID));
		this.customerID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_ID));
		this.siteAddress = cursor.getString(cursor.getColumnIndex(COLUMN_SITE_ADDRESS));
		this.siteTels = cursor.getString(cursor.getColumnIndex(COLUMN_SITE_TELS));
		this.siteEmails = cursor.getString(cursor.getColumnIndex(COLUMN_SITE_EMAILS));
		this.siteLocLat = cursor.getFloat(cursor.getColumnIndex(COLUMN_SITE_LOC_LAT));
		this.siteLocLon = cursor.getFloat(cursor.getColumnIndex(COLUMN_SITE_LOC_LON));
		this.areaID = cursor.getInt(cursor.getColumnIndex(COLUMN_AREA_ID));
		this.provinceID = cursor.getInt(cursor.getColumnIndex(COLUMN_PROVINCIAL_ID));
		this.checkLeader = cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_ASSIST));
		
		this.layoutSVGPath = cursor.getString(cursor.getColumnIndex(COLUMN_LAYOUT_SVG_PATH));
		this.resultPdfPath = cursor.getString(cursor.getColumnIndex(COLUMN_RESULT_PDF_PATH));
		
		this.siteAddressKey = cursor.getString(cursor.getColumnIndex(COLUMN_SITE_ADDR_KEY));
		
		this.locationCheckInDate = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_COMPLETE_CHECK_DATE));
		this.locationCheckOutDate = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_COMPLETE_CHECK_END_DATE));
		this.mileNo = cursor.getString(cursor.getColumnIndex(COLUMN_MILE_NO));
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
		customerSurveySiteID INTEGER,
		customerCode TEXT,
		jobRequestID INTEGER,
		customerSurveySiteRowID INTEGER,
		customerID INTEGER,
		siteAddress TEXT,
		siteTels TEXT,
		siteEmails TEXT,
		siteLocLat REAL,
		siteLocLon REAL,
		areaID INTEGER,
		provincialID INTEGER,
		customerNameAssist TEXT,
 */
		dest.writeInt(this.getCustomerSurveySiteID());
		dest.writeString(this.getCustomerCode());
		dest.writeString(this.getCustomerName());
		dest.writeInt(this.getJobRequestID());
		dest.writeInt(this.getTaskID());
		dest.writeInt(this.getCustomerSurveySiteRowID());
		dest.writeInt(this.getCustomerID());
		dest.writeString(this.getSiteAddress());
		dest.writeString(this.getSiteTels());
		dest.writeString(this.getSiteEmails());
		dest.writeFloat(this.getSiteLocLat());
		dest.writeFloat(this.getSiteLocLon());
		dest.writeInt(this.getAreaID());
		dest.writeInt(this.getProvinceID());
		dest.writeString(this.getCheckLeader());
		
		
		dest.writeString(this.getLayoutSVGPath());
		dest.writeString(this.getResultPdfPath());
		dest.writeString(this.getSiteAddressKey());
		
		////////////
		dest.writeString(this.getLocationCheckInDate());
		dest.writeString(this.getLocationCheckOutDate());
		dest.writeString(this.getMileNo());
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.customerSurveySiteID = JSONDataUtil.getInt(jsonObj, COLUMN_CUSTOMER_SURVEY_SITE_ID);
		this.customerSurveySiteRowID = JSONDataUtil.getInt(jsonObj, COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID);
		this.jobRequestID = JSONDataUtil.getInt(jsonObj, COLUMN_CUSTOMER_JOBREQUEST_ID);
		this.taskID = JSONDataUtil.getInt(jsonObj, COLUMN_TASK_ID);
		this.customerCode = JSONDataUtil.getString(jsonObj, COLUMN_CUSTOMER_CODE);
		this.customerName = JSONDataUtil.getString(jsonObj, COLUMN_CUSTOMER_NAME);
		this.siteAddress = JSONDataUtil.getString(jsonObj, COLUMN_SITE_ADDRESS);
		this.siteTels = JSONDataUtil.getString(jsonObj, COLUMN_SITE_TELS);
		this.siteEmails = JSONDataUtil.getString(jsonObj, COLUMN_SITE_EMAILS);
		this.siteLocLat = (float) JSONDataUtil.getDouble(jsonObj, COLUMN_SITE_LOC_LAT);
		this.siteLocLon = (float) JSONDataUtil.getDouble(jsonObj, COLUMN_SITE_LOC_LON);
		
		this.provinceID = JSONDataUtil.getInt(jsonObj, COLUMN_PROVINCIAL_ID);
		this.checkLeader = JSONDataUtil.getString(jsonObj,COLUMN_CUSTOMER_ASSIST);
		
		this.layoutSVGPath = JSONDataUtil.getString(jsonObj, COLUMN_LAYOUT_SVG_PATH);
		this.resultPdfPath = JSONDataUtil.getString(jsonObj, COLUMN_RESULT_PDF_PATH);
		
		this.siteAddressKey = JSONDataUtil.getString(jsonObj, COLUMN_SITE_ADDR_KEY);
		
		this.locationCheckInDate = JSONDataUtil.getString(jsonObj, COLUMN_LOCATION_COMPLETE_CHECK_DATE);
		this.locationCheckOutDate = JSONDataUtil.getString(jsonObj, COLUMN_LOCATION_COMPLETE_CHECK_END_DATE);
		this.mileNo = JSONDataUtil.getString(jsonObj, COLUMN_MILE_NO);
		
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
		strBld.append("insert into CustomerSurveySite");
		strBld.append("(");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+",");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID+",");
		strBld.append(COLUMN_CUSTOMER_JOBREQUEST_ID+",");
		strBld.append(COLUMN_TASK_ID+",");
		strBld.append(COLUMN_CUSTOMER_CODE+",");
		strBld.append(COLUMN_CUSTOMER_NAME+",");
		strBld.append(COLUMN_SITE_ADDRESS+",");
		strBld.append(COLUMN_SITE_TELS+",");
		strBld.append(COLUMN_SITE_EMAILS+",");
		strBld.append(COLUMN_SITE_LOC_LAT+",");
		strBld.append(COLUMN_SITE_LOC_LON+",");
		strBld.append(COLUMN_PROVINCIAL_ID+",");
		strBld.append(COLUMN_CUSTOMER_ASSIST+",");
		strBld.append(COLUMN_LAYOUT_SVG_PATH+",");
		strBld.append(COLUMN_RESULT_PDF_PATH+",");
		strBld.append(COLUMN_SITE_ADDR_KEY+",");
		strBld.append(COLUMN_LOCATION_COMPLETE_CHECK_DATE+",");
		strBld.append(COLUMN_LOCATION_COMPLETE_CHECK_END_DATE+",");
		strBld.append(COLUMN_MILE_NO);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.customerSurveySiteID+",");
		strBld.append(this.customerSurveySiteRowID+",");
		strBld.append(this.jobRequestID+",");
		strBld.append(this.taskID+",");
		strBld.append("'"+this.customerCode+"',");
		strBld.append("'"+this.customerName+"',");
		strBld.append("'"+this.siteAddress+"',");
		strBld.append("'"+this.siteTels+"',");
		strBld.append("'"+this.siteEmails+"',");
		strBld.append(""+this.siteLocLat+",");
		strBld.append(""+this.siteLocLon+",");
		strBld.append(""+this.provinceID+",");
		strBld.append("'"+this.checkLeader+"',");
		strBld.append("'"+this.layoutSVGPath+"',");
		strBld.append("'"+this.resultPdfPath+"',");
		strBld.append("'"+this.siteAddressKey+"',");
		strBld.append("'"+this.locationCheckInDate+"',");
		strBld.append("'"+this.locationCheckOutDate+"',");
		strBld.append("'"+this.mileNo+"'");
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
//		return super.toString();
		return this.getSiteAddress();
	}
   public boolean isNewAddress() {
      return isNewAddress;
   }
   public void setNewAddress(boolean isNewAddress) {
      this.isNewAddress = isNewAddress;
   }
   public String getMileNo() {
      return mileNo;
   }
   public void setMileNo(String mileNo) {
      this.mileNo = mileNo;
   }
   public String getLocationCheckInDate() {
      return locationCheckInDate;
   }
   public void setLocationCheckInDate(String locationCheckInDate) {
      this.locationCheckInDate = locationCheckInDate;
   }
   /**
   * @return the locationCheckOutDate
   */
  public String getLocationCheckOutDate() {
     return locationCheckOutDate;
  }
  /**
   * @param locationCheckOutDate the locationCheckOutDate to set
   */
  public void setLocationCheckOutDate(String locationCheckOutDate) {
     this.locationCheckOutDate = locationCheckOutDate;
  }
}

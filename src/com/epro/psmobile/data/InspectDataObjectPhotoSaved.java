package com.epro.psmobile.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class InspectDataObjectPhotoSaved implements 
		DbCursorHolder ,
		TransactionStmtHolder,
		Serializable,
		Cloneable,
		Parcelable,
		JSONDataHolder
{

	/**
	 CREATE TABLE InspectDataObjectPhotoSaved
(
photoID INTEGER ,
photoNo INTEGER,
fileName TEXT,
comment TEXT,
taskCode TEXT,
customerSurveySiteID INTEGER,
inspectDataObjectID INTEGER,
PRIMARY KEY(photoID,photoNo)
);

	 */
	private static final long serialVersionUID = 3656924815036746121L;
	public final static String COLUMN_PHOTO_ID = "photoID";
	public final static String COLUMN_PHOTO_NO = "photoNo";
	public final static String COLUMN_FILE_NAME = "fileName";
	public final static String COLUMN_COMMENT = "comment";
	public final static String COLUMN_TASK_CODE = "taskCode";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	public final static String COLUMN_INSPECT_DATA_OBJECT_ID = "inspectDataObjectID";
	public final static String COLUMN_INSPECT_DATA_TEXT_SELECTED = "inspectDataTextSelected";
	//public final static String COLUMN_INSPECT_PRODUCTS_LIST_TEXT_SELECTED = "inspectProductsTextSelected";
	
	public final static String COLUMN_ANGLE_DETAIL = "angleDetail";
	public final static String COLUMN_PRODUCT_ID = "productID";
	public final static String COLUMN_INSPECT_TYPE_ID = "inspectTypeID";
	
	private final static String COLUMN_FLAG_GENERAL_IMAGE = "flagGeneralImage";
	private int photoID;
	private int photoNo;
	private String fileName;
	private String comment = "";
	private String taskCode;
	private int customerSurveySiteID;
	private int inspectDataObjectID;
	private String inspectDataTextSelected;
	private String angleDetail;
	private int productID;
	private int inspectTypeID;
	//private String inspectProductsTextSelected;
	
	private double latitude;
	private double longitude;

	private String flagGeneralImage = "N";
	
	private String imageDate;
	public ArrayList<String> productDisplayDetailList = new ArrayList<String>();
	
	
	public static final Parcelable.Creator<InspectDataObjectPhotoSaved> CREATOR = new Parcelable.Creator<InspectDataObjectPhotoSaved>()
			{

				@Override
				public InspectDataObjectPhotoSaved createFromParcel(
						Parcel source) {
					// TODO Auto-generated method stub
					return new InspectDataObjectPhotoSaved(source);
				}

				@Override
				public InspectDataObjectPhotoSaved[] newArray(int size) {
					// TODO Auto-generated method stub
					return new InspectDataObjectPhotoSaved[size];
				}
		
			};
			
	public InspectDataObjectPhotoSaved(Parcel source)
	{
		this.photoID = source.readInt();
		this.photoNo = source.readInt();
		this.fileName = source.readString();
		this.comment = source.readString();
		/*
		 	private String taskCode;
	private int customerSurveySiteID;
	private int inspectDataObjectID;
		 */
		this.taskCode = source.readString();
		this.customerSurveySiteID = source.readInt();
		this.inspectDataObjectID = source.readInt();
		
		this.inspectDataTextSelected = source.readString();
		this.angleDetail = source.readString();
		this.productID = source.readInt();
		
		this.inspectTypeID = source.readInt();
//		this.inspectProductsTextSelected = source.readString();
		
		this.flagGeneralImage = source.readString();
	}
	public InspectDataObjectPhotoSaved() {
		// TODO Auto-generated constructor stub
	}
	public void addProductName(String productName)
	{
		productDisplayDetailList.add(productName);
	}
	public void removeProductName(String productName)
	{
		productDisplayDetailList.remove(productName);
	}
	public ArrayList<String> getProductDisplayDetailList(){
	   return productDisplayDetailList;
	}
	public String generateProductNamesList(){
		StringBuilder strBld = new StringBuilder();
		for(String strDisplayDetail : productDisplayDetailList)
		{
			strBld.append(strDisplayDetail+"\r\n");
		}
		return strBld.toString();
	}
	public void setProductNamesList(String productNamesList)
	{
		if (!productNamesList.startsWith("\r\n"))
		{
			productNamesList = "\r\n"+productNamesList;
		}
		String[] productNameArray  = productNamesList.split("\r\n");
		productDisplayDetailList = new ArrayList<String>();
		for(String s : productNameArray)
		{
			productDisplayDetailList.add(s);
		}
	}
	public int getPhotoID() {
		return photoID;
	}

	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}

	public int getPhotoNo() {
		return photoNo;
	}

	public void setPhotoNo(int photoNo) {
		this.photoNo = photoNo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getComment() {
		if ((comment == null)||(comment.equalsIgnoreCase("null"))){
			return "";
		}
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public int getCustomerSurveySiteID() {
		return customerSurveySiteID;
	}
	public void setCustomerSurveySiteID(int customerSurveySiteID) {
		this.customerSurveySiteID = customerSurveySiteID;
	}
	public int getInspectDataObjectID() {
		return inspectDataObjectID;
	}
	public void setInspectDataObjectID(int inspectDataObjectID) {
		this.inspectDataObjectID = inspectDataObjectID;
	}
	public String getInspectDataTextSelected() {
//		return this.generateProductNamesList();
		return inspectDataTextSelected;
	}
	public void setInspectDataTextSelected(String inspectDataTextSelected) {
		this.inspectDataTextSelected = inspectDataTextSelected;
	}
	public String getAngleDetail() {
		if ((angleDetail == null)||(angleDetail.equalsIgnoreCase("null"))){
			return "";
		}
		return angleDetail;
	}
	public void setAngleDetail(String angleDetail) {
		this.angleDetail = angleDetail;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.setPhotoID(cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_ID)));
		this.setPhotoNo(cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_NO)));
		this.setFileName(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_NAME)));
		this.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT)));
		
		this.setTaskCode(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_CODE)));
		this.setCustomerSurveySiteID(cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ID)));
		this.setInspectDataObjectID(cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_DATA_OBJECT_ID)));
		
		this.setInspectDataTextSelected(cursor.getString(cursor.getColumnIndex(COLUMN_INSPECT_DATA_TEXT_SELECTED)));
		this.setAngleDetail(cursor.getString(cursor.getColumnIndex(COLUMN_ANGLE_DETAIL)));
		this.setProductID(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
		
		this.setInspectTypeID(cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_TYPE_ID)));
		
		this.setFlagGeneralImage(cursor.getString(cursor.getColumnIndex(COLUMN_FLAG_GENERAL_IMAGE)));
	}

	@Override
	public String deleteStatement() {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("delete from InspectDataObjectPhotoSaved where "+COLUMN_PHOTO_ID+"="+this.getPhotoID());
		return strBld.toString();
	}

	@Override
	public String insertStatement() {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();		
		strBld.append("insert into InspectDataObjectPhotoSaved");
		strBld.append("(");
		strBld.append(COLUMN_PHOTO_ID+",");
		strBld.append(COLUMN_PHOTO_NO+",");
		strBld.append(COLUMN_FILE_NAME+",");
		strBld.append(COLUMN_COMMENT+",");
		strBld.append(COLUMN_TASK_CODE+",");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+",");
		strBld.append(COLUMN_INSPECT_DATA_OBJECT_ID+",");
		strBld.append(COLUMN_INSPECT_DATA_TEXT_SELECTED+",");
		strBld.append(COLUMN_ANGLE_DETAIL+",");
		strBld.append(COLUMN_PRODUCT_ID+",");
		strBld.append(COLUMN_INSPECT_TYPE_ID+",");
		strBld.append(COLUMN_FLAG_GENERAL_IMAGE);
		//strBld.append(COLUMN_INSPECT_PRODUCTS_LIST_TEXT_SELECTED);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(""+this.getPhotoID()+",");
		strBld.append(""+this.getPhotoNo()+",");
		strBld.append("'"+this.getFileName()+"',");
		strBld.append("'"+this.getComment()+"',");
		strBld.append("'"+this.getTaskCode()+"',");
		strBld.append(""+this.getCustomerSurveySiteID()+",");
		strBld.append(""+this.getInspectDataObjectID()+",");
		strBld.append("'"+this.generateProductNamesList()+"',");
		strBld.append("'"+this.angleDetail+"',");
		strBld.append(""+this.productID+",");
		strBld.append(""+this.inspectTypeID+",");
		strBld.append("'"+this.flagGeneralImage+"'");
//		strBld.append("'"+this.inspectProductsTextSelected+"'");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("update InspectDataObjectPhotoSaved set ");
		if ((this.getFileName() != null)&&(!this.getFileName().trim().isEmpty()))
			strBld.append(COLUMN_FILE_NAME+"='"+this.getFileName()+"',");
		
		strBld.append(COLUMN_COMMENT+"='"+this.getComment()+"',");
		strBld.append(COLUMN_ANGLE_DETAIL+"='"+this.getAngleDetail()+"'");
//		strBld.append(COLUMN_INSPECT_PRODUCTS_LIST_TEXT_SELECTED+"='"+this.getInspectProductsTextSelected()+"'");
			
		strBld.append(" where "+COLUMN_PHOTO_ID+"="+this.getPhotoID()+" " +
				"and "+COLUMN_PHOTO_NO+"="+this.getPhotoNo());
		
		return strBld.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
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
		 private int photoID;
		 private int photoNo;
		 private String fileName;
		 private String comment;
		 */
		dest.writeInt(this.getPhotoID());
		dest.writeInt(this.getPhotoNo());
		dest.writeString(this.getFileName());
		dest.writeString(this.getComment());
		
		dest.writeString(this.getTaskCode());
		dest.writeInt(this.getCustomerSurveySiteID());
		dest.writeInt(this.getInspectDataObjectID());
		
		dest.writeString(this.getInspectDataTextSelected());
		dest.writeString(this.angleDetail);
		dest.writeInt(this.productID);
		
		dest.writeInt(this.inspectTypeID);
		
		dest.writeString(this.flagGeneralImage);
//		dest.writeString(this.inspectProductsTextSelected);
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		/*
		public final static String COLUMN_PHOTO_ID = "photoID";
		public final static String COLUMN_PHOTO_NO = "photoNo";
		public final static String COLUMN_FILE_NAME = "fileName";
		public final static String COLUMN_COMMENT = "comment";
		public final static String COLUMN_TASK_CODE = "taskCode";
		public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
		public final static String COLUMN_INSPECT_DATA_OBJECT_ID = "inspectDataObjectID";
		public final static String COLUMN_INSPECT_DATA_TEXT_SELECTED = "inspectDataTextSelected";
		 */
		JSONDataUtil.put(jsonObj, COLUMN_PHOTO_ID, this.photoID);
		JSONDataUtil.put(jsonObj, COLUMN_PHOTO_NO, this.photoNo);
		JSONDataUtil.put(jsonObj, COLUMN_FILE_NAME, this.fileName);
		JSONDataUtil.put(jsonObj, COLUMN_COMMENT, this.comment);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_CODE, this.taskCode);
		JSONDataUtil.put(jsonObj, COLUMN_CUSTOMER_SURVEY_SITE_ID, this.customerSurveySiteID);
		JSONDataUtil.put(jsonObj, COLUMN_INSPECT_DATA_OBJECT_ID, this.inspectDataObjectID);
		JSONDataUtil.put(jsonObj, COLUMN_INSPECT_DATA_TEXT_SELECTED, this.getInspectDataTextSelected());
		JSONDataUtil.put(jsonObj, COLUMN_ANGLE_DETAIL, this.angleDetail);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_ID, this.productID);
//		JSONDataUtil.put(jsonObj, COLUMN_INSPECT_PRODUCTS_LIST_TEXT_SELECTED, this.inspectProductsTextSelected);
		return jsonObj;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/*
	public String getInspectProductsTextSelected() {
		return inspectProductsTextSelected;
	}
	public void setInspectProductsTextSelected(
			String inspectProductsTextSelected) {
		this.inspectProductsTextSelected = inspectProductsTextSelected;
	}*/
	public String getImageDate() {
		return imageDate;
	}
	public void setImageDate(String imageDate) {
		this.imageDate = imageDate;
	}
   public int getInspectTypeID() {
      return inspectTypeID;
   }
   public void setInspectTypeID(int inspectTypeID) {
      this.inspectTypeID = inspectTypeID;
   }
   public String getFlagGeneralImage() {
      return flagGeneralImage;
   }
   public void setFlagGeneralImage(String flagGeneralImage) {
      this.flagGeneralImage = flagGeneralImage;
   }
}

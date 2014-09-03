package com.epro.psmobile.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

public class InspectDataObjectSaved implements DbCursorHolder,
		TransactionStmtHolder ,
		java.io.Serializable,
		Cloneable,
		Parcelable,
		JSONDataHolder
		
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2255546039932065350L;
	public final static String COLUMN_SITE_ADDRESS_KEY = "siteAddressKey";
	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_JOB_TASK_CODE = "taskCode";
	public final static String COLUMN_CUSTOMER_SURVEYSITE = "customerSurveySiteID";
	public final static String COLUMN_LAYOUT_DRAWING_PAGE_NO = "layoutDrawingPageNo";
	public final static String COLUMN_INSPECT__DATA_OBJECT_ID = "inspectDataObjectID";
	public final static String COLUMN_INSPECT_DATA_ITEM_ID = "inspectDataItemID";
	public final static String COLUMN_START_X = "inspectDataItemStartX";
	public final static String COLUMN_START_Y = "inspectDataItemStartY";
	public final static String COLUMN_Z_ORDER = "inspectDataItemZOrder";
	public final static String COLUMN_PRODUCT_ID = "productID";
	public final static String COLUMN_PRODUCT_GROUP_ID = "productGroupID";
	public final static String COLUMN_PRODUCT_AMT_UNIT_ID = "productAmountUnitID";
	public final static String COLUMN_PRODUCT_AMT_UNIT_TEXT = "productAmountUnitText";
	public final static String COLUMN_WIDTH = "width";
	public final static String COLUMN_LONG = "deep";
	public final static String COLUMN_WIDTH_OBJECT = "widthObject";
	public final static String COLUMN_LONG_OBJECT = "longObject";
	public final static String COLUMN_HEIGHT = "height";
	public final static String COLUMN_ANGLE = "angle";
	public final static String COLUMN_RADIUS = "radius";
	public final static String COLUMN_ADITIONAL_VALUE = "overValue";
	public final static String COLUMN_NEGATIVE_VALUE = "lost";
	public final static String COLUMN_TOTAL = "total";
	public final static String COLUMN_VALUE = "value";
	public final static String COLUMN_QTY = "qty";
	public final static String COLUMN_OPINION_VALUE = "opinion";
	public final static String COLUMN_DENSITY = "density";
	public final static String COLUMN_MARKET_PRICE_ID = "marketPriceID";
	public final static String COLUMN_MARKET_PRICE = "marketPrice";
	public final static String COLUMN_PHOTO_ID = "photoID";
	public final static String COLUMN_IS_PRODUCT_CONTROLLED = "isProductControlled";
	public final static String COLUMN_AUTHORIZED = "authorized";
	public final static String COLUMN_TEAM_ID_OBJECT_OWNER = "teamIDObjectOwner";
	public final static String COLUMN_ADD_OBJECT_FLAG = "addObjectFlag";
	public final static String COLUMN_PALATE_AMOUNT = "palateAmount";
	
	
	public final static String COLUMN_REAL_WIDTH = "realWidth";
	public final static String COLUMN_REAL_HEIGHT = "realHeight";
	public final static String COLUMN_REAL_DEEP = "realDeep";
	
	public final static String COLUMN_IS_CUSTOM_MARKET_PRICE = "isCustomMarketPrice";
	public final static String COLUMN_OBJECT_NAME = "objectName";
	public final static String COLUMN_INSPECT_TYPE_ID = "inspectTypeID";
	
	private String siteAddressKey;
	private int jobRequestID;
	private String taskCode;
	private int customerSurveySiteID;
	private int layoutDrawingPageNo;
	private int inspectDataObjectID = -1;
	private int inspectDataItemID;
	private double inspectDataItemStartX;
	private double inspectDataItemStartY;
	private int inspectDataItemZOrder;
	private int productID;
	private int productGroupID;
	private int productAmountUnitID;
	private String productAmountUnitText;
	private double width;
	private double dLong;

	private double palateAmount = 1.00d;
	
	private int widthObject = -1;
	private int longObject = -1;
	
	private double height;
	private double angle;
	private double radius;
	private double lost;
	private double over;
	private double total;
	private double value;
	private double qty;
	private String opinionValue;
	private int photoID;
	private int marketPriceID;
	private double marketPrice;
	private double density;
	
	private boolean isProductControlled;
	
	private boolean isCustomMarketPrice;
	//private Rect bounds;
	
	private int leftBounds;
	private int topBounds;
	private int rightBounds;
	private int bottomBounds;
	
	private boolean authorized = true;/*default*/
	
	private int teamIDObjectOwner;
	
	private String addObjectFlag = "N";//Y,N
	
	private double realWidth;
	private double realHeight;
	private double realDeep;
	
	
	private ArrayList<InspectDataObjectPhotoSaved> photosSaved;
	private InspectDataSavedSpinnerDisplay inspectDataSavedSpinnerDisplay;
	
	private String objectName;
	private int inspectTypeID;
	
	public static final Parcelable.Creator<InspectDataObjectSaved> CREATOR = new Parcelable.Creator<InspectDataObjectSaved>()
	{

		@Override
		public InspectDataObjectSaved createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new InspectDataObjectSaved(source);
		}

		@Override
		public InspectDataObjectSaved[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InspectDataObjectSaved[size];
		}
		
	};
	public InspectDataObjectSaved(Parcel source)
	{
		/*
		dest.writeInt(this.jobRequestID);
		dest.writeString(this.taskCode);
		dest.writeInt(this.customerSurveySiteID);
		dest.writeInt(this.layoutDrawingPageNo);
		dest.writeInt(this.inspectDataObjectID);
		dest.writeInt(this.inspectDataItemID);
		dest.writeDouble(this.inspectDataItemStartX);
		dest.writeDouble(this.inspectDataItemStartY);
		dest.writeInt(inspectDataItemZOrder);
		dest.writeInt(this.productID);
		dest.writeInt(this.productGroupID);
		dest.writeInt(this.productAmountUnitID);
		dest.writeDouble(this.width);
		dest.writeDouble(this.height);
		dest.writeDouble(this.dLong);
		dest.writeDouble(this.angle);
		dest.writeDouble(this.lost);
		dest.writeDouble(this.over);
		dest.writeDouble(this.total);
		dest.writeDouble(this.value);
		dest.writeString(this.opinionValue);
		dest.writeDouble(this.marketPrice); 
		 */
		this.siteAddressKey = source.readString();
		this.jobRequestID = source.readInt();
		this.taskCode = source.readString();
		this.customerSurveySiteID = source.readInt();
		this.layoutDrawingPageNo = source.readInt();
		this.inspectDataObjectID = source.readInt();
		this.inspectDataItemID = source.readInt();
		this.inspectDataItemStartX = source.readDouble();
		this.inspectDataItemStartY = source.readDouble();
		this.inspectDataItemZOrder = source.readInt();
		this.productID = source.readInt();
		this.productGroupID = source.readInt();
		this.productAmountUnitID = source.readInt();
		this.productAmountUnitText = source.readString();
		this.width = source.readDouble();
		this.height = source.readDouble();
		this.dLong = source.readDouble();
		this.angle = source.readDouble();
		this.radius = source.readDouble();
		this.lost = source.readDouble();
		this.over = source.readDouble();
		this.total = source.readDouble();
		this.value = source.readDouble();
		this.qty = source.readDouble();
		this.opinionValue = source.readString();
		this.marketPriceID = source.readInt();
		this.marketPrice = source.readDouble();
		this.density = source.readDouble();
		this.palateAmount = source.readDouble();
		
		this.widthObject = source.readInt();
		this.longObject = source.readInt();
		
		
		
		
		
		boolean b[] = new boolean[1];
		source.readBooleanArray(b);
		this.isProductControlled = b[0];
		
		b = new boolean[1];
		source.readBooleanArray(b);
		this.authorized = b[0];
		
		this.teamIDObjectOwner = source.readInt();
		this.addObjectFlag = source.readString();
		
		this.inspectDataSavedSpinnerDisplay = source.readParcelable(InspectDataSavedSpinnerDisplay.class.getClassLoader());
		
	}
	public InspectDataObjectSaved() {
		// TODO Auto-generated constructor stub
		
		
	}

	@Override
	public String deleteStatement() throws Exception 
	{
		// TODO Auto-generated method stub
		if (this.getJobRequestID() <= 0){
			throw new Exception("column "+COLUMN_JOB_REQUEST_ID+" must have");
		}
		/*
		if (this.getTaskNo() <= 0){
			throw new Exception("column "+COLUMN_JOB_TASK_NO+" must have");
		}*/
		if (this.getCustomerSurveySiteID() <= 0){
			throw new Exception("column "+COLUMN_CUSTOMER_SURVEYSITE+" must have");
		}
		if (this.getLayoutDrawingPageNo() <= 0){
			//throw new Exception("column "+COLUMN_LAYOUT_DRAWING_PAGE_NO+" must have");			
		}
		if (this.getInspectDataObjectID() <= 0){
			//throw new Exception("column "+COLUMN_INSPECT__DATA_OBJECT_ID+" must have");						
		}
		String sql = "delete from InspectDataObjectSaved";
		sql += " where "+COLUMN_JOB_REQUEST_ID+"="+this.getJobRequestID();
		sql += " and "+COLUMN_JOB_TASK_CODE+"='"+this.getTaskCode()+"'";
		sql += " and "+COLUMN_CUSTOMER_SURVEYSITE+"="+this.getCustomerSurveySiteID();
		
//		sql += " and "+COLUMN_LAYOUT_DRAWING_PAGE_NO+"="+this.getLayoutDrawingPageNo();
//		sql += " and "+COLUMN_INSPECT__DATA_OBJECT_ID+"="+this.getInspectDataObjectID();
		
		return sql;
	}

	@Override
	public String insertStatement() throws Exception{
		// TODO Auto-generated method stub
		StringBuilder strInsertStmt = new StringBuilder();
		strInsertStmt.append("INSERT INTO InspectDataObjectSaved");
		strInsertStmt.append("(");
		strInsertStmt.append(COLUMN_JOB_REQUEST_ID+",");
		strInsertStmt.append(COLUMN_JOB_TASK_CODE+",");
		strInsertStmt.append(COLUMN_CUSTOMER_SURVEYSITE+",");
		strInsertStmt.append(COLUMN_LAYOUT_DRAWING_PAGE_NO+",");
		strInsertStmt.append(COLUMN_INSPECT__DATA_OBJECT_ID+",");
		strInsertStmt.append(COLUMN_INSPECT_DATA_ITEM_ID+",");
		strInsertStmt.append(COLUMN_START_X +",");
		strInsertStmt.append(COLUMN_START_Y+",");
		strInsertStmt.append(COLUMN_Z_ORDER+",");
		strInsertStmt.append(COLUMN_PRODUCT_ID+",");
		strInsertStmt.append(COLUMN_PRODUCT_GROUP_ID+",");
		strInsertStmt.append(COLUMN_PRODUCT_AMT_UNIT_ID+",");
		strInsertStmt.append(COLUMN_PRODUCT_AMT_UNIT_TEXT+",");
		strInsertStmt.append(COLUMN_WIDTH+",");
		strInsertStmt.append(COLUMN_HEIGHT+",");
		strInsertStmt.append(COLUMN_LONG+",");
		strInsertStmt.append(COLUMN_VALUE+",");
		strInsertStmt.append(COLUMN_QTY+",");
		strInsertStmt.append(COLUMN_ANGLE+",");
		strInsertStmt.append(COLUMN_RADIUS+",");
		strInsertStmt.append(COLUMN_ADITIONAL_VALUE+",");
		strInsertStmt.append(COLUMN_NEGATIVE_VALUE+",");
		strInsertStmt.append(COLUMN_TOTAL +",");
		strInsertStmt.append(COLUMN_OPINION_VALUE+",");
		strInsertStmt.append(COLUMN_PHOTO_ID+",");
		strInsertStmt.append(COLUMN_MARKET_PRICE_ID+",");
		strInsertStmt.append(COLUMN_MARKET_PRICE+",");
		strInsertStmt.append(COLUMN_DENSITY+",");
		strInsertStmt.append(COLUMN_PALATE_AMOUNT+",");
		strInsertStmt.append(COLUMN_WIDTH_OBJECT+",");
		strInsertStmt.append(COLUMN_LONG_OBJECT+",");
		strInsertStmt.append(COLUMN_IS_PRODUCT_CONTROLLED+",");
		strInsertStmt.append(COLUMN_AUTHORIZED+",");
		strInsertStmt.append(COLUMN_SITE_ADDRESS_KEY+",");
		strInsertStmt.append(COLUMN_TEAM_ID_OBJECT_OWNER+",");
		strInsertStmt.append(COLUMN_ADD_OBJECT_FLAG+",");
		strInsertStmt.append(COLUMN_IS_CUSTOM_MARKET_PRICE+",");
		strInsertStmt.append(COLUMN_OBJECT_NAME+",");
		strInsertStmt.append(COLUMN_INSPECT_TYPE_ID+"");
		strInsertStmt.append(")");
		strInsertStmt.append(" values");
		strInsertStmt.append("(");
		strInsertStmt.append(this.getJobRequestID()+",");
		strInsertStmt.append("'"+this.getTaskCode()+"',");
		strInsertStmt.append(this.getCustomerSurveySiteID()+",");
		strInsertStmt.append(this.getLayoutDrawingPageNo()+",");
		strInsertStmt.append(this.getInspectDataObjectID()+",");
		strInsertStmt.append(this.getInspectDataItemID()+",");
		strInsertStmt.append(this.getInspectDataItemStartX()+",");
		strInsertStmt.append(this.getInspectDataItemStartY()+",");
		strInsertStmt.append(this.getInspectDataItemZOrder()+",");
		strInsertStmt.append(this.getProductID()+",");
		strInsertStmt.append(this.getProductGroupID()+",");
		strInsertStmt.append(this.getProductAmountUnitID()+",");
		strInsertStmt.append("'"+this.productAmountUnitText+"',");
		strInsertStmt.append(this.getWidth()+",");
		strInsertStmt.append(this.getHeight()+",");
		strInsertStmt.append(this.getdLong()+",");
		strInsertStmt.append(this.getValue()+",");
		strInsertStmt.append(this.getQty()+",");
		strInsertStmt.append(this.getAngle()+",");
		strInsertStmt.append(this.getRadius()+",");
		strInsertStmt.append(this.getOver()+",");
		strInsertStmt.append(this.getLost()+",");
		strInsertStmt.append(this.getTotal()+",");
		strInsertStmt.append("'"+this.getOpinionValue()+"',");
		strInsertStmt.append(this.getPhotoID()+",");
		strInsertStmt.append(this.getMarketPriceID()+",");
		strInsertStmt.append(this.getMarketPrice()+",");
		strInsertStmt.append(this.getDensity()+",");
		strInsertStmt.append(this.getPalateAmount()+",");
		strInsertStmt.append(this.getWidthObject()+",");
		strInsertStmt.append(this.getLongObject()+",");
		strInsertStmt.append("'"+this.isProductControlled()+"',");
		strInsertStmt.append("'"+this.authorized+"',");
		strInsertStmt.append("'"+this.getSiteAddressKey()+"',");
		strInsertStmt.append(this.getTeamIDObjectOwner()+",");
		strInsertStmt.append("'"+this.addObjectFlag+"',");
		strInsertStmt.append("'"+(isCustomMarketPrice?"Y":"N")+"',");
		strInsertStmt.append("'"+this.getObjectName()+"',");
		strInsertStmt.append(""+this.getInspectTypeID()+"");
		strInsertStmt.append(")");
		
		return strInsertStmt.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.siteAddressKey = cursor.getString(cursor.getColumnIndex(COLUMN_SITE_ADDRESS_KEY));
		this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COLUMN_JOB_REQUEST_ID));
		this.taskCode = cursor.getString(cursor.getColumnIndex(COLUMN_JOB_TASK_CODE));
		this.customerSurveySiteID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEYSITE));
		this.layoutDrawingPageNo = cursor.getInt(cursor.getColumnIndex(COLUMN_LAYOUT_DRAWING_PAGE_NO));
		this.inspectDataObjectID = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT__DATA_OBJECT_ID));
		this.inspectDataItemID = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_DATA_ITEM_ID));
		this.inspectDataItemStartX = cursor.getDouble(cursor.getColumnIndex(COLUMN_START_X));
		this.inspectDataItemStartY = cursor.getDouble(cursor.getColumnIndex(COLUMN_START_Y));
		this.inspectDataItemZOrder = cursor.getInt(cursor.getColumnIndex(COLUMN_Z_ORDER));
		this.productID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
		this.productGroupID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_GROUP_ID));
		this.productAmountUnitID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_AMT_UNIT_ID));
		this.productAmountUnitText = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_AMT_UNIT_TEXT));
		this.width = cursor.getDouble(cursor.getColumnIndex(COLUMN_WIDTH));
		this.height = cursor.getDouble(cursor.getColumnIndex(COLUMN_HEIGHT));
		this.dLong = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONG));
		this.angle = cursor.getDouble(cursor.getColumnIndex(COLUMN_ANGLE));
		this.radius = cursor.getDouble(cursor.getColumnIndex(COLUMN_RADIUS));
		this.over = cursor.getDouble(cursor.getColumnIndex(COLUMN_ADITIONAL_VALUE));
		this.lost = cursor.getDouble(cursor.getColumnIndex(COLUMN_NEGATIVE_VALUE));
		this.total = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL));
		this.value = cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE));
		this.qty = cursor.getDouble(cursor.getColumnIndex(COLUMN_QTY));
		this.opinionValue = cursor.getString((cursor.getColumnIndex(COLUMN_OPINION_VALUE)));
		this.photoID = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_ID));
		this.marketPriceID = cursor.getInt(cursor.getColumnIndex(COLUMN_MARKET_PRICE_ID));
		this.marketPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_MARKET_PRICE));
		this.density = cursor.getDouble(cursor.getColumnIndex(COLUMN_DENSITY));
		this.palateAmount = cursor.getDouble(cursor.getColumnIndex(COLUMN_PALATE_AMOUNT));
		
		this.widthObject = cursor.getInt(cursor.getColumnIndex(COLUMN_WIDTH_OBJECT));
		this.longObject = cursor.getInt(cursor.getColumnIndex(COLUMN_LONG_OBJECT));
		this.teamIDObjectOwner = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_ID_OBJECT_OWNER));
		this.addObjectFlag = cursor.getString(cursor.getColumnIndex(COLUMN_ADD_OBJECT_FLAG));
		try{
			this.isProductControlled = Boolean.parseBoolean(
					cursor.getString(cursor.getColumnIndex(COLUMN_IS_PRODUCT_CONTROLLED)));
		}catch(Exception ex){}
		
		try{
			this.authorized = Boolean.parseBoolean(
					cursor.getString(cursor.getColumnIndex(COLUMN_AUTHORIZED)));
		}catch(Exception ex){}
		
		try{
		       String strCustomMarketPrice =
		             cursor.getString(cursor.getColumnIndex(COLUMN_IS_CUSTOM_MARKET_PRICE));
		       this.isCustomMarketPrice = (strCustomMarketPrice.equalsIgnoreCase("Y"));
		}catch(Exception ex){}
		
		this.objectName = cursor.getString(cursor.getColumnIndex(COLUMN_OBJECT_NAME));
		this.inspectTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_TYPE_ID));
		
	}

	public int getJobRequestID() {
		return jobRequestID;
	}

	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}
	
	public int getCustomerSurveySiteID() {
		return customerSurveySiteID;
	}

	public void setCustomerSurveySiteID(int customerSurveySiteID) {
		this.customerSurveySiteID = customerSurveySiteID;
	}

	public int getLayoutDrawingPageNo() {
		return layoutDrawingPageNo;
	}

	public void setLayoutDrawingPageNo(int layoutDrawingPageNo) {
		this.layoutDrawingPageNo = layoutDrawingPageNo;
	}

	public int getInspectDataObjectID() {
		return inspectDataObjectID;
	}

	public void setInspectDataObjectID(int inspectDataObjectID) {
		this.inspectDataObjectID = inspectDataObjectID;
	}

	public int getInspectDataItemID() {
		return inspectDataItemID;
	}

	public void setInspectDataItemID(int inspectDataItemID) {
		this.inspectDataItemID = inspectDataItemID;
	}

	public double getInspectDataItemStartX() {
		return inspectDataItemStartX;
	}

	public void setInspectDataItemStartX(double inspectDataItemStartX) {
		this.inspectDataItemStartX = inspectDataItemStartX;
	}

	public double getInspectDataItemStartY() {
		return inspectDataItemStartY;
	}

	public void setInspectDataItemStartY(double inspectDataItemStartY) {
		this.inspectDataItemStartY = inspectDataItemStartY;
	}

	public int getInspectDataItemZOrder() {
		return inspectDataItemZOrder;
	}

	public void setInspectDataItemZOrder(int inspectDataItemZOrder) {
		this.inspectDataItemZOrder = inspectDataItemZOrder;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getProductGroupID() {
		return productGroupID;
	}

	public void setProductGroupID(int productGroupID) {
		this.productGroupID = productGroupID;
	}

	public int getProductAmountUnitID() {
		return productAmountUnitID;
	}

	public void setProductAmountUnitID(int productAmountUnitID) {
		this.productAmountUnitID = productAmountUnitID;
	}


	public String getProductAmountUnitText() {
		return productAmountUnitText;
	}
	public void setProductAmountUnitText(String productAmountUnitText) {
		this.productAmountUnitText = productAmountUnitText;
	}
	public int getPhotoID() {
		return photoID;
	}

	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}

	public double getLost() {
		return lost;
	}

	public void setLost(double lost) {
		this.lost = lost;
	}

	public double getOver() {
		return over;
	}

	public void setOver(double over) {
		this.over = over;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getOpinionValue() {
		if (opinionValue == null)
			return "";
		else
		{
			if (opinionValue.equalsIgnoreCase("null")){
				opinionValue = "";
			}
		}
		return opinionValue;
	}

	public void setOpinionValue(String opinionValue) {
		this.opinionValue = opinionValue;
	}

	public ArrayList<InspectDataObjectPhotoSaved> getPhotosSaved() {
		return photosSaved;
	}

	public void setPhotosSaved(ArrayList<InspectDataObjectPhotoSaved> photosSaved) {
		this.photosSaved = photosSaved;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
		if (width == 10.0){
		   int xxx = 0;
		   xxx++;
		}
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getdLong() {
		return dLong;
	}

	public void setdLong(double dLong) {
		this.dLong = dLong;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public int getMarketPriceID() {
		return marketPriceID;
	}
	public void setMarketPriceID(int marketPriceID) {
		this.marketPriceID = marketPriceID;
	}
	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public double getDensity() {
		return density;
	}
	public void setDensity(double density) {
		this.density = density;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public InspectDataSavedSpinnerDisplay getInspectDataSavedSpinnerDisplay() {
		return inspectDataSavedSpinnerDisplay;
	}
	public void setInspectDataSavedSpinnerDisplay(
			InspectDataSavedSpinnerDisplay inspectDataSavedSpinnerDisplay) {
		this.inspectDataSavedSpinnerDisplay = inspectDataSavedSpinnerDisplay;
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
		 	private int jobRequestID;
			private String taskCode;
			private int customerSurveySiteID;
			private int layoutDrawingPageNo;
			private int inspectDataObjectID;
			private int inspectDataItemID;
			private double inspectDataItemStartX;
			private double inspectDataItemStartY;
			private int inspectDataItemZOrder;
			private int productID;
			private int productGroupID;
			private int productAmountUnitID;
			private double width;
			private double height;
			private double dLong;
			private double angle;
			private double lost;
			private double over;
			private double total;
			private double value;
			private String opinionValue;
			private int photoID;
			private double marketPrice;	
		 */
		dest.writeString(this.siteAddressKey);
		dest.writeInt(this.jobRequestID);
		dest.writeString(this.taskCode);
		dest.writeInt(this.customerSurveySiteID);
		dest.writeInt(this.layoutDrawingPageNo);
		dest.writeInt(this.inspectDataObjectID);
		dest.writeInt(this.inspectDataItemID);
		dest.writeDouble(this.inspectDataItemStartX);
		dest.writeDouble(this.inspectDataItemStartY);
		dest.writeInt(inspectDataItemZOrder);
		dest.writeInt(this.productID);
		dest.writeInt(this.productGroupID);
		dest.writeInt(this.productAmountUnitID);
		dest.writeString(this.productAmountUnitText);
		dest.writeDouble(this.width);
		dest.writeDouble(this.height);
		dest.writeDouble(this.dLong);
		dest.writeDouble(this.angle);
		dest.writeDouble(this.radius);
		dest.writeDouble(this.lost);
		dest.writeDouble(this.over);
		dest.writeDouble(this.total);
		dest.writeDouble(this.value);
		dest.writeDouble(this.qty);
		dest.writeString(this.opinionValue);
		dest.writeInt(this.marketPriceID);
		dest.writeDouble(this.marketPrice);
		dest.writeDouble(this.density);
		dest.writeDouble(this.palateAmount);
		
		dest.writeInt(this.widthObject);
		dest.writeInt(this.longObject);
		
		dest.writeBooleanArray(new boolean[]{this.isProductControlled});
		dest.writeBooleanArray(new boolean[]{this.authorized});
		
		dest.writeInt(this.teamIDObjectOwner);
		dest.writeString(this.addObjectFlag);
		dest.writeParcelable(inspectDataSavedSpinnerDisplay,flags);
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.siteAddressKey = JSONDataUtil.getString(jsonObj, COLUMN_SITE_ADDRESS_KEY);
		this.jobRequestID = JSONDataUtil.getInt(jsonObj, COLUMN_JOB_REQUEST_ID);
		
		this.taskCode = JSONDataUtil.getString(jsonObj, COLUMN_JOB_TASK_CODE);
		this.customerSurveySiteID = JSONDataUtil.getInt(jsonObj, COLUMN_CUSTOMER_SURVEYSITE);
		this.layoutDrawingPageNo = JSONDataUtil.getInt(jsonObj, COLUMN_LAYOUT_DRAWING_PAGE_NO);
		this.inspectDataObjectID = JSONDataUtil.getInt(jsonObj, COLUMN_INSPECT__DATA_OBJECT_ID);
		this.inspectDataItemID = JSONDataUtil.getInt(jsonObj, COLUMN_INSPECT_DATA_ITEM_ID);
		this.inspectDataItemStartX = JSONDataUtil.getDouble(jsonObj, COLUMN_START_X);
		this.inspectDataItemStartY = JSONDataUtil.getDouble(jsonObj, COLUMN_START_Y);
		this.inspectDataItemZOrder = JSONDataUtil.getInt(jsonObj, COLUMN_Z_ORDER);
		this.productID = JSONDataUtil.getInt(jsonObj, COLUMN_PRODUCT_ID);
		this.productGroupID = JSONDataUtil.getInt(jsonObj, COLUMN_PRODUCT_GROUP_ID);
		this.productAmountUnitID = JSONDataUtil.getInt(jsonObj, COLUMN_PRODUCT_AMT_UNIT_ID);
		this.productAmountUnitText = JSONDataUtil.getString(jsonObj, COLUMN_PRODUCT_AMT_UNIT_TEXT);
		this.width = JSONDataUtil.getDouble(jsonObj, COLUMN_WIDTH);
		this.height = JSONDataUtil.getDouble(jsonObj, COLUMN_HEIGHT);
		this.dLong = JSONDataUtil.getDouble(jsonObj, COLUMN_LONG);
		this.angle = JSONDataUtil.getDouble(jsonObj, COLUMN_ANGLE);
		this.radius = JSONDataUtil.getDouble(jsonObj, COLUMN_RADIUS);
		this.over = JSONDataUtil.getDouble(jsonObj, COLUMN_ADITIONAL_VALUE);
		this.lost = JSONDataUtil.getDouble(jsonObj, COLUMN_NEGATIVE_VALUE);
		this.total = JSONDataUtil.getDouble(jsonObj, COLUMN_TOTAL);
		this.value = JSONDataUtil.getDouble(jsonObj, COLUMN_VALUE);
		this.qty = JSONDataUtil.getDouble(jsonObj, COLUMN_QTY);
		this.opinionValue = JSONDataUtil.getString(jsonObj, COLUMN_OPINION_VALUE);
		this.photoID = JSONDataUtil.getInt(jsonObj, COLUMN_PHOTO_ID);
		this.marketPriceID = JSONDataUtil.getInt(jsonObj, COLUMN_MARKET_PRICE_ID);
		this.marketPrice = JSONDataUtil.getDouble(jsonObj, COLUMN_MARKET_PRICE);
		this.density = JSONDataUtil.getDouble(jsonObj, COLUMN_DENSITY);
		this.palateAmount = JSONDataUtil.getDouble(jsonObj, COLUMN_PALATE_AMOUNT);
		
		this.widthObject = JSONDataUtil.getInt(jsonObj, COLUMN_WIDTH_OBJECT);
		this.longObject = JSONDataUtil.getInt(jsonObj, COLUMN_LONG_OBJECT);
		this.teamIDObjectOwner = JSONDataUtil.getInt(jsonObj, COLUMN_TEAM_ID_OBJECT_OWNER);
		this.addObjectFlag = JSONDataUtil.getString(jsonObj, COLUMN_ADD_OBJECT_FLAG);
		try{
			this.isProductControlled = Boolean.parseBoolean(
					JSONDataUtil.getString(jsonObj, COLUMN_IS_PRODUCT_CONTROLLED));
		}catch(Exception ex){}
		
		try{
			this.authorized = Boolean.parseBoolean(
					JSONDataUtil.getString(jsonObj, COLUMN_AUTHORIZED));
		}catch(Exception ex){}
	}
	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();

		JSONDataUtil.put(jsonObj, COLUMN_SITE_ADDRESS_KEY, this.siteAddressKey);
		JSONDataUtil.put(jsonObj, COLUMN_JOB_REQUEST_ID , this.jobRequestID);
		JSONDataUtil.put(jsonObj, COLUMN_JOB_TASK_CODE ,this.taskCode);
		JSONDataUtil.put(jsonObj, COLUMN_CUSTOMER_SURVEYSITE ,this.customerSurveySiteID);
		JSONDataUtil.put(jsonObj, COLUMN_LAYOUT_DRAWING_PAGE_NO ,this.layoutDrawingPageNo);
		JSONDataUtil.put(jsonObj, COLUMN_INSPECT__DATA_OBJECT_ID ,this.inspectDataObjectID);
		JSONDataUtil.put(jsonObj, COLUMN_INSPECT_DATA_ITEM_ID ,this.inspectDataItemID);
		JSONDataUtil.put(jsonObj, COLUMN_START_X ,this.inspectDataItemStartX);
		JSONDataUtil.put(jsonObj, COLUMN_START_Y ,this.inspectDataItemStartY);
		JSONDataUtil.put(jsonObj, COLUMN_Z_ORDER ,this.inspectDataItemZOrder);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_ID ,this.productID);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_GROUP_ID ,this.productGroupID);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_AMT_UNIT_ID ,this.productAmountUnitID);

		JSONDataUtil.put(jsonObj, COLUMN_WIDTH ,this.width);
		JSONDataUtil.put(jsonObj, COLUMN_HEIGHT,this.height);
		JSONDataUtil.put(jsonObj, COLUMN_LONG,this.dLong);
		JSONDataUtil.put(jsonObj, COLUMN_ANGLE ,this.angle);
		JSONDataUtil.put(jsonObj, COLUMN_RADIUS ,this.radius);
		JSONDataUtil.put(jsonObj, COLUMN_ADITIONAL_VALUE ,this.over);
		JSONDataUtil.put(jsonObj, COLUMN_NEGATIVE_VALUE ,this.lost);
		JSONDataUtil.put(jsonObj, COLUMN_TOTAL ,this.total);
		JSONDataUtil.put(jsonObj, COLUMN_VALUE ,this.value);
		JSONDataUtil.put(jsonObj, COLUMN_QTY, this.qty);
		JSONDataUtil.put(jsonObj, COLUMN_OPINION_VALUE ,this.opinionValue);
		JSONDataUtil.put(jsonObj, COLUMN_DENSITY,this.density);
		
		//skip to generate this attribute
		//JSONDataUtil.put(jsonObj, COLUMN_PALATE_AMOUNT, this.palateAmount);
		
		JSONDataUtil.put(jsonObj, COLUMN_MARKET_PRICE_ID, this.marketPriceID);
		JSONDataUtil.put(jsonObj, COLUMN_MARKET_PRICE,this.marketPrice);
		JSONDataUtil.put(jsonObj, COLUMN_PHOTO_ID,this.photoID);

		JSONDataUtil.put(jsonObj, COLUMN_WIDTH_OBJECT, this.widthObject);
		JSONDataUtil.put(jsonObj, COLUMN_LONG_OBJECT, this.longObject);
		
		
		JSONDataUtil.put(jsonObj, COLUMN_IS_PRODUCT_CONTROLLED, Boolean.toString(this.isProductControlled));
		JSONDataUtil.put(jsonObj, COLUMN_AUTHORIZED, Boolean.toString(this.authorized));

		JSONDataUtil.put(jsonObj, COLUMN_TEAM_ID_OBJECT_OWNER, this.teamIDObjectOwner);
		JSONDataUtil.put(jsonObj, COLUMN_ADD_OBJECT_FLAG, this.addObjectFlag);
		
		
		
		JSONDataUtil.put(jsonObj, COLUMN_REAL_WIDTH, 
		                           DataUtil.decimal2digiFormat(this.realWidth));
		JSONDataUtil.put(jsonObj, COLUMN_REAL_HEIGHT, 
		                           DataUtil.decimal2digiFormat(this.realHeight));
		JSONDataUtil.put(jsonObj, COLUMN_REAL_DEEP, 
		                           DataUtil.decimal2digiFormat(this.realDeep));
		
		JSONDataUtil.put(jsonObj, "inspectLayoutName", this.objectName);
		
		return jsonObj;
	}
	public int getWidthObject() {
		return widthObject;
	}
	public void setWidthObject(int widthObject) {
		this.widthObject = widthObject;
	}
	public int getLongObject() {
		return longObject;
	}
	public void setLongObject(int longObject) {
		this.longObject = longObject;
	}
	public boolean isProductControlled() {
		return isProductControlled;
	}
	public void setProductControlled(boolean isProductControlled) {
		this.isProductControlled = isProductControlled;
	}
	public Rect getBounds() {
		return new Rect(this.leftBounds,this.topBounds,this.rightBounds,this.bottomBounds);
	}
	public void setBounds(int left,int top,int right,int bottom) {
		this.leftBounds = left;
		this.topBounds = top;
		this.rightBounds = right;
		this.bottomBounds = bottom;
	}
	/*
	public InspectDataObjectSaved Copy() throws CloneNotSupportedException
	{
		return (InspectDataObjectSaved)this.clone();
	}*/
	public boolean isAuthorized() {
		//return false;
		return authorized;
	}
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	public String getSiteAddressKey() {
		return siteAddressKey;
	}
	public void setSiteAddressKey(String siteAddressKey) {
		this.siteAddressKey = siteAddressKey;
	}
	public int getTeamIDObjectOwner() {
		return teamIDObjectOwner;
	}
	public void setTeamIDObjectOwner(int teamIDObjectOwner) {
		this.teamIDObjectOwner = teamIDObjectOwner;
	}
	public String getAddObjectFlag() {
		return addObjectFlag;
	}
	public void setAddObjectFlag(String addObjectFlag) {
		this.addObjectFlag = addObjectFlag;
	}
	public double getPalateAmount() {
		return palateAmount;
	}
	public void setPalateAmount(double palateAmount) {
		this.palateAmount = palateAmount;
	}
   public double getRealWidth() {
      return realWidth;
   }
   public void setRealWidth(double realWidth) {
      this.realWidth = realWidth;
   }
   public double getRealHeight() {
      return realHeight;
   }
   public void setRealHeight(double realHeight) {
      this.realHeight = realHeight;
   }
   public double getRealDeep() {
      return realDeep;
   }
   public void setRealDeep(double realDeep) {
      this.realDeep = realDeep;
   }
   public boolean isCustomMarketPrice() {
      return isCustomMarketPrice;
   }
   public void setCustomMarketPrice(boolean isCustomMarketPrice) {
      this.isCustomMarketPrice = isCustomMarketPrice;
   }
   /**
    * @return the objectName
    */
   public String getObjectName() {
      if ((objectName == null)||(objectName.equalsIgnoreCase("null")))
         objectName = "";
      
      return objectName;
   }
   /**
    * @param objectName the objectName to set
    */
   public void setObjectName(String objectName) {
      this.objectName = objectName;
   }
   /**
    * @return the inspectTypeID
    */
   public int getInspectTypeID() {
      return inspectTypeID;
   }
   /**
    * @param inspectTypeID the inspectTypeID to set
    */
   public void setInspectTypeID(int inspectTypeID) {
      this.inspectTypeID = inspectTypeID;
   }
   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      // TODO Auto-generated method stub
      if (inspectDataObjectID >= 0){
         return this.inspectDataObjectID +". "+(this.getObjectName());
      }else{
         return "";
      }
   }

}

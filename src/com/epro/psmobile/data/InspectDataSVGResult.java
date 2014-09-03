package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class InspectDataSVGResult implements TransactionStmtHolder,
		DbCursorHolder, JSONDataHolder {

	public final static String COLUMN_TASK_CODE = "taskCode";
	public final static String COLUMN_TASK_DUPLICATE_NO = "taskDuplicateNo";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	public final static String COLUMN_SVG_FILE_FULL_LAYOUT = "svgResultFullLayout";
	public final static String COLUMN_SVG_FILE_LAYOUT_ONLY = "svgResultLayoutOnly";
	
	public final static String COLUMN_LOCATION_LAT = "locationLatitude";
	public final static String COLUMN_LOCATION_LON = "locationLongitude";
	
	private String taskCode;
	private int taskDuplicateNo;
	private int customerSurveySiteID;
	private String svgResultFullLayout;
	private String svgResultLayoutOnly; 
	
	private double locationLatitude;
	private double locationLongitude;

	public InspectDataSVGResult() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj, COLUMN_TASK_CODE, this.taskCode);
		JSONDataUtil.put(jsonObj, COLUMN_TASK_DUPLICATE_NO, this.taskDuplicateNo);
		JSONDataUtil.put(jsonObj, COLUMN_CUSTOMER_SURVEY_SITE_ID, this.customerSurveySiteID);
		JSONDataUtil.put(jsonObj, COLUMN_SVG_FILE_FULL_LAYOUT, this.svgResultFullLayout);
		JSONDataUtil.put(jsonObj, COLUMN_SVG_FILE_LAYOUT_ONLY, this.svgResultLayoutOnly);
		JSONDataUtil.put(jsonObj, COLUMN_LOCATION_LAT, this.locationLatitude);
		JSONDataUtil.put(jsonObj, COLUMN_LOCATION_LON, this.locationLongitude);
		return jsonObj;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.taskCode = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_CODE));
		this.taskDuplicateNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_DUPLICATE_NO));
		this.customerSurveySiteID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ID));
		this.svgResultFullLayout = cursor.getString(cursor.getColumnIndex(COLUMN_SVG_FILE_FULL_LAYOUT));
		this.svgResultLayoutOnly = cursor.getString(cursor.getColumnIndex(COLUMN_SVG_FILE_LAYOUT_ONLY));
		this.locationLatitude = cursor.getFloat(cursor.getColumnIndex(COLUMN_LOCATION_LAT));
		this.locationLongitude = cursor.getFloat(cursor.getColumnIndex(COLUMN_LOCATION_LON));
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		String strSql = "delete from InspectDataSVGResult where ";
		strSql += " "+COLUMN_TASK_CODE+"='"+this.taskCode+"'";
		strSql += " and "+COLUMN_TASK_DUPLICATE_NO+"="+this.taskDuplicateNo+"";
		strSql += " and "+COLUMN_CUSTOMER_SURVEY_SITE_ID+"="+this.customerSurveySiteID;
		
		return strSql;
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into InspectDataSVGResult");
		strBld.append("(");
		strBld.append(COLUMN_TASK_CODE+",");
		strBld.append(COLUMN_TASK_DUPLICATE_NO+",");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+",");
		strBld.append(COLUMN_SVG_FILE_FULL_LAYOUT+",");
		strBld.append(COLUMN_SVG_FILE_LAYOUT_ONLY+",");
		strBld.append(COLUMN_LOCATION_LAT+",");
		strBld.append(COLUMN_LOCATION_LON);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append("'"+this.taskCode+"',");
		strBld.append(""+this.taskDuplicateNo+",");
		strBld.append(""+this.customerSurveySiteID+",");
		strBld.append("'"+this.svgResultFullLayout+"',");
		strBld.append("'"+this.svgResultLayoutOnly+"',");
		strBld.append(""+this.locationLatitude+",");
		strBld.append(""+this.locationLongitude);
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public int getTaskDuplicateNo() {
		return taskDuplicateNo;
	}

	public void setTaskDuplicateNo(int taskDuplicateNo) {
		this.taskDuplicateNo = taskDuplicateNo;
	}

	public int getCustomerSurveySiteID() {
		return customerSurveySiteID;
	}

	public void setCustomerSurveySiteID(int customerSurveySiteID) {
		this.customerSurveySiteID = customerSurveySiteID;
	}

	public String getSvgResultFullLayout() {
		return svgResultFullLayout;
	}

	public void setSvgResultFullLayout(String svgResultFullLayout) {
		this.svgResultFullLayout = svgResultFullLayout;
	}

	public String getSvgResultLayoutOnly() {
		return svgResultLayoutOnly;
	}

	public void setSvgResultLayoutOnly(String svgResultLayoutOnly) {
		this.svgResultLayoutOnly = svgResultLayoutOnly;
	}

	public double getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(double locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public double getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(double locationLongitude) {
		this.locationLongitude = locationLongitude;
	}

}

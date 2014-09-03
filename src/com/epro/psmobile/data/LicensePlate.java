package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class LicensePlate implements DbCursorHolder,JSONDataHolder, TransactionStmtHolder {

	public final static String COLUMN_LICENSE_PLATE_ID = "licensePlateId";
	public final static String COLUMN_LICENSE_PLATE = "licensePlate";
	public final static String COLUMN_LICENSE_PLATE_PROVINCE = "licensePlateProvince";
	public final static String COLUMN_VEHICAL_FUEL_TYPE = "vehicleFuelType";
	
	private int licensePlateID;
	private String licensePlate;
	private String licensePlateProvince;
	private String vehicleFuelType;
	
	public LicensePlate()
	{
		
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
		strBld.append("insert into LicensePlate");
		strBld.append("(");
		strBld.append(COLUMN_LICENSE_PLATE_ID+",");
		strBld.append(COLUMN_LICENSE_PLATE+",");
		strBld.append(COLUMN_LICENSE_PLATE_PROVINCE+",");
		strBld.append(COLUMN_VEHICAL_FUEL_TYPE+"");
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.licensePlateID+",");
		strBld.append("'"+this.licensePlate+"',");
		strBld.append("'"+this.licensePlateProvince+"',");
		strBld.append("'"+this.vehicleFuelType+"'");
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
		this.licensePlateID = 
				cursor.getInt(cursor.getColumnIndex(COLUMN_LICENSE_PLATE_ID));
		this.licensePlate = 
				cursor.getString(cursor.getColumnIndex(COLUMN_LICENSE_PLATE));
		this.licensePlateProvince = 
				cursor.getString(cursor.getColumnIndex(COLUMN_LICENSE_PLATE_PROVINCE));
		this.vehicleFuelType = 
				cursor.getString(cursor.getColumnIndex(COLUMN_VEHICAL_FUEL_TYPE));
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.licensePlateID = JSONDataUtil.getInt(jsonObj, COLUMN_LICENSE_PLATE_ID);
		this.licensePlate = JSONDataUtil.getString(jsonObj, COLUMN_LICENSE_PLATE);
		this.licensePlateProvince = JSONDataUtil.getString(jsonObj, COLUMN_LICENSE_PLATE_PROVINCE);
		this.vehicleFuelType = JSONDataUtil.getString(jsonObj, COLUMN_VEHICAL_FUEL_TYPE);
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLicensePlateID() {
		return licensePlateID;
	}

	public void setLicensePlateID(int licensePlateID) {
		this.licensePlateID = licensePlateID;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getLicensePlateProvince() {
		return licensePlateProvince;
	}

	public void setLicensePlateProvince(String licensePlateProvince) {
		this.licensePlateProvince = licensePlateProvince;
	}

	public String getVehicleFuelType() {
		return vehicleFuelType;
	}

	public void setVehicleFuelType(String vehicleFuelType) {
		this.vehicleFuelType = vehicleFuelType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getLicensePlate();
	}

	

}

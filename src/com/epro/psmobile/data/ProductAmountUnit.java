package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;

public class ProductAmountUnit implements DbCursorHolder , JSONDataHolder , TransactionStmtHolder
{
	public final static String COLUMN_PRODUCT_AMOUNT_UNIT_ID = "productAmountUnitID";
	public final static String COLUMN_UNIT_NAME = "unitName";
	
	private int productAmountUnitID;
	private String unitName;
	public ProductAmountUnit() {
		// TODO Auto-generated constructor stub
	}
	public int getProductAmountUnitID() {
		return productAmountUnitID;
	}
	public void setProductAmountUnitID(int productAmountUnitID) {
		this.productAmountUnitID = productAmountUnitID;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.productAmountUnitID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_AMOUNT_UNIT_ID));
		this.unitName = cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_NAME));
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException
	{
		// TODO Auto-generated method stub
		this.productAmountUnitID = JSONDataUtil.getInt(jsonObj,COLUMN_PRODUCT_AMOUNT_UNIT_ID);
		this.unitName = JSONDataUtil.getString(jsonObj,COLUMN_UNIT_NAME);
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
		strBld.append("insert into ProductAmountUnit");
		strBld.append("(");
		strBld.append(COLUMN_PRODUCT_AMOUNT_UNIT_ID+",");
		strBld.append(COLUMN_UNIT_NAME);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.productAmountUnitID+",");
		strBld.append("'"+this.unitName+"'");
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

}

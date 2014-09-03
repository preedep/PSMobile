/**
 * 
 */
package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;

/**
 * @author nickmsft
 *
 */
public class MarketPrice implements DbCursorHolder , JSONDataHolder , TransactionStmtHolder
{

	private int marketPriceID;
	private int provincialID;
	private int productGroupID;
	private int productID;
	private double marketPrice;
	private String unit;
//	private int ratioPerUnit;

	public final static String COLUMN_MARKET_PRICE_ID = "marketPriceID";
	public final static String COLUMN_PROVINCIAL_ID = "provincialID";
	public final static String COLUMN_PRODUCT_GROUP_ID = "productGroupID";
	public final static String COLUMN_PRODUCT_ID = "productID";
	public final static String COLUMN_MARKET_PRICE = "marketPrice";
	public final static String COLUMN_UNIT = "unit";
//	public final static String COLUMN_RATIO_PER_UNIT = "ratioPerUnit";
	
	/**
	 * 
	 */
	public MarketPrice() {
		// TODO Auto-generated constructor stub
	}

	public int getMarketPriceID() {
		return marketPriceID;
	}

	public void setMarketPriceID(int marketPriceID) {
		this.marketPriceID = marketPriceID;
	}

	public int getProvincialID() {
		return provincialID;
	}

	public void setProvincialID(int provincialID) {
		this.provincialID = provincialID;
	}

	public int getProductGroupID() {
		return productGroupID;
	}

	public void setProductGroupID(int productGroupID) {
		this.productGroupID = productGroupID;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.data.DbCursorHolder#onBind(android.database.Cursor)
	 */
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/*
	public int getRatioPerUnit() {
		return ratioPerUnit;
	}

	public void setRatioPerUnit(int ratioPerUnit) {
		this.ratioPerUnit = ratioPerUnit;
	}*/

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.provincialID = cursor.getInt(
				cursor.getColumnIndex(COLUMN_PROVINCIAL_ID));
		this.productGroupID = cursor.getInt(
				cursor.getColumnIndex(COLUMN_PRODUCT_GROUP_ID));
		this.productID = cursor.getInt(
				cursor.getColumnIndex(COLUMN_PRODUCT_ID));
		this.marketPrice = cursor.getDouble(
				cursor.getColumnIndex(COLUMN_MARKET_PRICE));
		this.unit = cursor.getString(cursor.getColumnIndex(COLUMN_UNIT));
		//this.ratioPerUnit = cursor.getInt(cursor.getColumnIndex(COLUMN_RATIO_PER_UNIT));
		
		this.marketPriceID = cursor.getInt(cursor.getColumnIndex(COLUMN_MARKET_PRICE_ID));
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.marketPriceID = JSONDataUtil.getInt(jsonObj, COLUMN_MARKET_PRICE_ID);
		this.provincialID = JSONDataUtil.getInt(jsonObj, COLUMN_PROVINCIAL_ID);
		this.productGroupID = JSONDataUtil.getInt(jsonObj, COLUMN_PRODUCT_GROUP_ID);
		this.productID = JSONDataUtil.getInt(jsonObj,COLUMN_PRODUCT_ID);
		this.marketPrice = JSONDataUtil.getDouble(jsonObj,COLUMN_MARKET_PRICE);
		this.unit = JSONDataUtil.getString(jsonObj, COLUMN_UNIT);
		//this.ratioPerUnit = JSONDataUtil.getInt(jsonObj, COLUMN_RATIO_PER_UNIT);
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
		strBld.append("insert into MarketPrice");
		strBld.append("(");
		strBld.append(COLUMN_PROVINCIAL_ID+",");
		strBld.append(COLUMN_PRODUCT_GROUP_ID+",");
		strBld.append(COLUMN_PRODUCT_ID+",");
		strBld.append(COLUMN_MARKET_PRICE+",");
		strBld.append(COLUMN_UNIT+",");
//		strBld.append(COLUMN_RATIO_PER_UNIT+",");
		strBld.append(COLUMN_MARKET_PRICE_ID);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.provincialID+",");
		strBld.append(this.productGroupID+",");
		strBld.append(this.productID+",");
		strBld.append(this.marketPrice+",");
		strBld.append("'"+this.unit+"',");
//		strBld.append(""+this.ratioPerUnit+",");
		strBld.append(this.marketPriceID);
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

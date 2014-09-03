package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class ProductGroup  implements DbCursorHolder , JSONDataHolder , TransactionStmtHolder,Parcelable
{

	public final static String COLUMN_PRODUCT_GROUP_ID = "productGroupID";
	public final static String COLUMN_PRODUCT_GROUP_NAME = "productGroupName";
	//public final static String COLUMN_PRODUCT_GROUP_DENSITY = "densityValue";
	//public final static String COLUMN_PRODUCT_AMOUT_UNIT = "productAmountUnitID";
	
 	private int productGroupID;
	private String productGroupName;
	//private double densityValue;
	//private int productAmountUnitID;
	
	public static final Parcelable.Creator<ProductGroup> CREATOR = new Parcelable.Creator<ProductGroup>()
	{

		@Override
		public ProductGroup createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ProductGroup(source);
		}

		@Override
		public ProductGroup[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProductGroup[size];
		}
		
	};
	public ProductGroup(Parcel source)
	{
		this.productGroupID = source.readInt();
		this.productGroupName = source.readString();
		//this.productAmountUnitID = source.readInt();
	}
	public ProductGroup() {
		// TODO Auto-generated constructor stub
	}

	public int getProductGroupID() {
		return productGroupID;
	}

	public void setProductGroupID(int productGroupID) {
		this.productGroupID = productGroupID;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	/*
	public double getDensityValue() {
		return densityValue;
	}

	public void setDensityValue(double densityValue) {
		this.densityValue = densityValue;
	}*/

	/*
	public int getProductAmountUnitID() {
		return productAmountUnitID;
	}

	public void setProductAmountUnitID(int productAmountUnitID) {
		this.productAmountUnitID = productAmountUnitID;
	}*/

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.productGroupID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_GROUP_ID));
		this.productGroupName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_GROUP_NAME));
		//this.densityValue = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_GROUP_DENSITY));
		//this.productAmountUnitID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_AMOUT_UNIT));
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException 
	{
		// TODO Auto-generated method stub
		this.productGroupID = JSONDataUtil.getInt(jsonObj,COLUMN_PRODUCT_GROUP_ID);
		this.productGroupName = JSONDataUtil.getString(jsonObj, COLUMN_PRODUCT_GROUP_NAME);
		//this.productAmountUnitID = JSONDataUtil.getInt(jsonObj,COLUMN_PRODUCT_AMOUT_UNIT);
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
		strBld.append("insert into ProductGroup");
		strBld.append("(");
		strBld.append(COLUMN_PRODUCT_GROUP_ID+",");
		strBld.append(COLUMN_PRODUCT_GROUP_NAME+"");
		//strBld.append(COLUMN_PRODUCT_AMOUT_UNIT);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.productGroupID+",");
		strBld.append("'"+this.productGroupName+"'");
//		strBld.append(this.productAmountUnitID);
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
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		/*
		 	private int productGroupID;
			private String productGroupName;
			//private double densityValue;
			private int productAmountUnitID;
		 */
		dest.writeInt(productGroupID);
		dest.writeString(productGroupName);
//		dest.writeInt(productAmountUnitID);
	}

}

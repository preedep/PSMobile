package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements DbCursorHolder , JSONDataHolder , TransactionStmtHolder , Parcelable
{

	public final static String COLUMN_PRODUCT_ID   = "productID";
	public final static String COLUMN_PRODUCT_GROUP_ID = "productGroupID";
	public final static String COLUMN_PRODUCT_NAME = "productName";
	public final static String COLUMN_DENSITY_VALUE = "densityValue";
	public final static String COLUMN_PRODUCT_AMT_UNIT_ID = "productAmountUnitID";
	public final static String COLUMN_PRODUCT_AMT_UNIT_NAME = "unitName";
	public final static String COLUMN_COVERT_RATIO = "convertRatio";
	
	
	private int productID;
	private int productGroupID;
	private String productName;
	private double densityValue;
	private int productAmountUnitID;
	private int convertRatio;
	private String unitName;
	
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>()
			{

				@Override
				public Product createFromParcel(Parcel source) {
					// TODO Auto-generated method stub
					return new Product(source);
				}

				@Override
				public Product[] newArray(int size) {
					// TODO Auto-generated method stub
					return new Product[size];
				}
		
			};
	public Product(Parcel source)
	{
		/*
		 * private int productID;
	private int productGroupID;
	private String productName;
	private double densityValue;
		 */

		this.productID = source.readInt();
		this.productGroupID = source.readInt();
		this.productName = source.readString();
		this.densityValue = source.readDouble();
		this.productAmountUnitID = source.readInt();
		this.convertRatio = source.readInt();
		this.unitName = source.readString();
	}
	public Product() {
		// TODO Auto-generated constructor stub
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getDensityValue() {
		return densityValue;
	}

	public void setDensityValue(double densityValue) {
		this.densityValue = densityValue;
	}

	public int getProductAmountUnitID() {
		return productAmountUnitID;
	}
	public void setProductAmountUnitID(int productAmountUnitID) {
		this.productAmountUnitID = productAmountUnitID;
	}
	public int getConvertRatio() {
	    
	   if (convertRatio == 0)
	       convertRatio = 1;
	    
		return convertRatio;
	}
	public void setConvertRatio(int convertRatio) {
		this.convertRatio = convertRatio;
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
		this.productID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
		this.productGroupID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_GROUP_ID));
		this.productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
		this.densityValue = cursor.getDouble(cursor.getColumnIndex(COLUMN_DENSITY_VALUE));
		this.productAmountUnitID = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_AMT_UNIT_ID));
		this.convertRatio = cursor.getInt(cursor.getColumnIndex(COLUMN_COVERT_RATIO));
		
		try{
		   this.unitName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_AMT_UNIT_NAME));
		}catch(Exception ex){}
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException 
	{
		// TODO Auto-generated method stub
		this.productGroupID = JSONDataUtil.getInt(jsonObj,COLUMN_PRODUCT_GROUP_ID);
		this.productID = JSONDataUtil.getInt(jsonObj,COLUMN_PRODUCT_ID);
		this.productName = JSONDataUtil.getString(jsonObj,COLUMN_PRODUCT_NAME);
		this.densityValue = JSONDataUtil.getDouble(jsonObj,COLUMN_DENSITY_VALUE);
		this.productAmountUnitID = JSONDataUtil.getInt(jsonObj, COLUMN_PRODUCT_AMT_UNIT_ID);
		this.convertRatio = JSONDataUtil.getInt(jsonObj, COLUMN_COVERT_RATIO);
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
		strBld.append("insert into Product");
		strBld.append("(");
		strBld.append(COLUMN_PRODUCT_GROUP_ID+",");
		strBld.append(COLUMN_PRODUCT_ID+",");
		strBld.append(COLUMN_PRODUCT_NAME+",");
		strBld.append(COLUMN_DENSITY_VALUE+",");
		strBld.append(COLUMN_PRODUCT_AMT_UNIT_ID+",");
		strBld.append(COLUMN_COVERT_RATIO);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.productGroupID+",");
		strBld.append(this.productID+",");
		strBld.append("'"+this.productName+"',");
		strBld.append(""+this.densityValue+",");
		strBld.append(""+this.productAmountUnitID+",");
		strBld.append(""+this.convertRatio+"");
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
		 * private int productID;
	private int productGroupID;
	private String productName;
	private double densityValue;
		 */
		dest.writeInt(this.productID);
		dest.writeInt(this.productGroupID);
		dest.writeString(this.productName);
		dest.writeDouble(this.densityValue);
		dest.writeInt(this.productAmountUnitID);
		dest.writeInt(this.convertRatio);
		dest.writeString(this.unitName);
	}
   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      // TODO Auto-generated method stub
      return this.getProductName()+" ("+this.getUnitName()+")";
   }

}

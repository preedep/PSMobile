package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class InspectType  implements DbCursorHolder,
									 Parcelable , 
									 TransactionStmtHolder , 
									 JSONDataHolder
{
	public final static String COLUMN_INSPECT_TYPE = "inspectTypeID";
	public final static String COLUMN_INSPECT_TYPE_NAME = "inspectTypeName";
	public final static String COLUMN_INSPECT_PRODUCT_TYPE = "inpsectProductType";
	
	
	private int inspectTypeID;
	private String inspectTypeName;
	private int inspectProductType;
	
	public static final Parcelable.Creator<InspectType> CREATOR = new Parcelable.Creator<InspectType>()
	{
		public InspectType createFromParcel(Parcel source) {
			return new InspectType(source);
		}
	
		public InspectType[] newArray(int size) {
			return new InspectType[size];
		}
	};
	
	public InspectType(Parcel source)
	{
		this.setInspectTypeID(source.readInt());
		this.setInspectTypeName(source.readString());
		this.setInspectProductType(source.readInt());
	}
	public InspectType() {
		// TODO Auto-generated constructor stub
	}

	public int getInspectTypeID() {
		return inspectTypeID;
	}

	
	public void setInspectTypeID(int inspectTypeID) {
		this.inspectTypeID = inspectTypeID;
	}

	public String getInspectTypeName() {
		return inspectTypeName;
	}

	public void setInspectTypeName(String inspectTypeName) {
		this.inspectTypeName = inspectTypeName;
	}

	public boolean isCluster(){
		return (this.inspectTypeName.toLowerCase().indexOf("cluster") != -1);
	}
	public int getInspectProductType() {
		return inspectProductType;
	}
	public void setInspectProductType(int inspectProductType) {
		this.inspectProductType = inspectProductType;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.inspectTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_TYPE));
		this.inspectTypeName = cursor.getString(cursor.getColumnIndex(COLUMN_INSPECT_TYPE_NAME));
		this.inspectProductType = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_PRODUCT_TYPE));
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
		 private int inspectTypeID;
		 private String inspectTypeName;
		 */
		dest.writeInt(this.getInspectTypeID());
		dest.writeString(this.getInspectTypeName());
		dest.writeInt(this.getInspectProductType());
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
		strBld.append("insert into InspectType");
		strBld.append("(");
		strBld.append(COLUMN_INSPECT_TYPE+",");
		strBld.append(COLUMN_INSPECT_TYPE_NAME+",");
		strBld.append(COLUMN_INSPECT_PRODUCT_TYPE);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.inspectTypeID+",");
		strBld.append("'"+this.inspectTypeName+"',");
		strBld.append(this.inspectProductType);
		strBld.append(")");
		return strBld.toString();
	}
	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.inspectTypeID = JSONDataUtil.getInt(jsonObj,COLUMN_INSPECT_TYPE);
		this.inspectTypeName = JSONDataUtil.getString(jsonObj,COLUMN_INSPECT_TYPE_NAME);
		this.inspectProductType = JSONDataUtil.getInt(jsonObj,COLUMN_INSPECT_PRODUCT_TYPE);
	}
	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getInspectTypeName();
	}

}

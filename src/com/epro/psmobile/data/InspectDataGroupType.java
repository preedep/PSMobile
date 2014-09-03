package com.epro.psmobile.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class InspectDataGroupType implements DbCursorHolder , 
Parcelable , JSONDataHolder , TransactionStmtHolder , java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -756422972097730119L;
	public final static String COLUMN_INSPECT_DATA_GROUP_TYPE_ID = "inspectDataGroupTypeID";
	public final static String COLUMN_INSPECT_DATA_GROUP_TYPE_NAME = "inspectDataGroupTypeName";
	public final static String COLUMN_INSPECT_TYPE_ID = "inspectTypeID";

	private int inspectDataGroupTypeID;
	private String inspectDataGroupTypeName;
	private int inspectTypeID;
	
	private ArrayList<InspectDataItem> inspectDataItemList;
	
	public static final Parcelable.Creator<InspectDataGroupType> CREATOR = new Parcelable.Creator<InspectDataGroupType>()
	{

		@Override
		public InspectDataGroupType createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new InspectDataGroupType(source);
		}

		@Override
		public InspectDataGroupType[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InspectDataGroupType[size];
		}		
	};
			
	public InspectDataGroupType() {
		// TODO Auto-generated constructor stub
	}

	public InspectDataGroupType(Parcel source)
	{
		/*
		 dest.writeInt(this.getInspectDataGroupTypeID());
		dest.writeString(this.getInspectDataGroupTypeName());
		dest.writeBooleanArray(new boolean[]{this.isLayoutComponent()});		
		 */
		this.setInspectDataGroupTypeID(source.readInt());
		this.setInspectDataGroupTypeName(source.readString());
		
		this.setInspectTypeID(source.readInt());
		
//		boolean b[] = new boolean[1];
//		source.readBooleanArray(b);
//		this.setLayoutComponent(b[0]);
	}
	public int getInspectDataGroupTypeID() {
		return inspectDataGroupTypeID;
	}

	public void setInspectDataGroupTypeID(int inspectDataGroupTypeID) {
		this.inspectDataGroupTypeID = inspectDataGroupTypeID;
	}

	public String getInspectDataGroupTypeName() {
		return inspectDataGroupTypeName;
	}

	public void setInspectDataGroupTypeName(String inspectDataGroupTypeName) {
		this.inspectDataGroupTypeName = inspectDataGroupTypeName;
	}

	/*
	public boolean isCameraComponent(){
		return (this.getInspectDataGroupTypeID() == 5);
	}*/
	/*
	public boolean isLayoutComponent() {
		if ((this.getInspectDataGroupTypeID() == 1)||
			(this.getInspectDataGroupTypeID() == 2)||
			(this.getInspectDataGroupTypeID() == 3)||
			(this.getInspectDataGroupTypeID() == 4)){
			isLayoutComponent = true;
		}
		return isLayoutComponent;
	}

	public void setLayoutComponent(boolean isLayoutComponent) {
		this.isLayoutComponent = isLayoutComponent;
	}
*/	
	/*
	public boolean isCameraObject() {
		return isCameraObject;
	}

	public void setCameraObject(boolean isCameraObject) {
		this.isCameraObject = isCameraObject;
	}
*/
	public ArrayList<InspectDataItem> getInspectDataItemList() {
		return inspectDataItemList;
	}

	public void addInspectDataItem(InspectDataItem inspectDataItem) {
		if (this.inspectDataItemList == null)
		{
			this.inspectDataItemList = new ArrayList<InspectDataItem>();
		}
		this.inspectDataItemList.add(inspectDataItem);
	}
	public void addInspectDataItemList(ArrayList<InspectDataItem> items)
	{
		if (this.inspectDataItemList == null)
		{
			this.inspectDataItemList = new ArrayList<InspectDataItem>();
		}
		this.inspectDataItemList.clear();
		this.inspectDataItemList.addAll(items);
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		try{
			this.inspectDataGroupTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_DATA_GROUP_TYPE_ID));
			this.inspectDataGroupTypeName = cursor.getString(cursor.getColumnIndex(COLUMN_INSPECT_DATA_GROUP_TYPE_NAME));

//			this.isLayoutComponent =  Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_LAYOUT_COMPONENT)));
		}catch(Exception ex){}
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
		 private int inspectDataGroupTypeID;
		 private String inspectDataGroupTypeName;
		 private boolean isLayoutComponent;
		 */
		dest.writeInt(this.getInspectDataGroupTypeID());
		dest.writeString(this.getInspectDataGroupTypeName());
		
		dest.writeInt(this.getInspectTypeID());
//		dest.writeBooleanArray(new boolean[]{this.isLayoutComponent()});		
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.inspectDataGroupTypeID = JSONDataUtil.getInt(jsonObj, COLUMN_INSPECT_DATA_GROUP_TYPE_ID);
		this.inspectDataGroupTypeName = JSONDataUtil.getString(jsonObj, COLUMN_INSPECT_DATA_GROUP_TYPE_NAME);
		
		this.inspectTypeID = JSONDataUtil.getInt(jsonObj, COLUMN_INSPECT_TYPE_ID);
	}

	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
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
		strBld.append("insert into InspectDataGroupType");
		strBld.append("(");
		strBld.append(COLUMN_INSPECT_DATA_GROUP_TYPE_ID+",");
		strBld.append(COLUMN_INSPECT_DATA_GROUP_TYPE_NAME+",");
		strBld.append(COLUMN_INSPECT_TYPE_ID);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.inspectDataGroupTypeID+",");
		strBld.append("'"+this.inspectDataGroupTypeName+"',");
		strBld.append(this.inspectTypeID);
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

   public int getInspectTypeID() {
      return inspectTypeID;
   }

   public void setInspectTypeID(int inspectTypeID) {
      this.inspectTypeID = inspectTypeID;
   }

	

}

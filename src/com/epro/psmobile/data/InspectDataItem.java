package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.JSONDataUtil;
import com.larvalabs.svgandroid.SVG;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Parcel;
import android.os.Parcelable;

public class InspectDataItem implements DbCursorHolder , 
										Parcelable ,
										JSONDataHolder,
										TransactionStmtHolder,
										Cloneable,
										java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6538516769477237304L;
	public final static String COLUMN_INSPECT_DATA_ITEM_ID = "inspectDataItemID";
	public final static String COLUMN_INSPECT_ITEM_NAME = "inspectDataItemName";
	public final static String COLUMN_INSPECT_GRP_TYPE_ID = "inspectDataGroupTypeID";
	public final static String COLUMN_PARAMETERS = "parameters";
	public final static String COLUMN_FORMULA = "formular";
	public final static String COLUMN_KILOGRAM = "kilogram";
	//public final static String COLUMN_DENSITY_VALUE = "densityValue";
	public final static String COLUMN_IMAGE_FILE_NAME = "imageFileName";
	
	public final static String COLUMN_IS_LAYOUT_COMPONENT = "isBuildingComponent";
	public final static String COLUMN_IS_CAMERA_COMPONENT = "isCameraObject";
	public final static String COLUMN_IS_GODOWN_COMPONENT = "isGodownComponent";
	public final static String COLUMN_IS_PRODUCT_INSPECT_COMPONENT = "isInspectObject";
	public final static String COLUMN_IS_UNIVERSAL_LAYOUT_PRODUCT_OBJ = "isUniversalLayoutDropdown";
	
	
	public final static String COLUMN_MULTIPLE_TYPE = "multipleType";

	public final static String COLUMN_RATIO_WIDTH = "convertRatioWidth";
	public final static String COLUMN_RATIO_DEEP = "convertRatioDeep";
	public final static String COLUMN_RATIO_HEIGHT = "convertRatioHeight";
	
	private final static double CONVERT_CM_TO_METER = 1;//100.00;
	
	private final static String COLUMN_INSPECT_TYPE_ID = "inspectTypeID";
	
	
	private int inspectDataItemID;
	private String inspectDataItemName;
	private InspectDataGroupType groupType;
	private int parameters;
	private String formula;
	private double kilogram;
	//private int densityValue;
	private String imageFileName;
	
	private boolean isLayoutComponent;
	private boolean isCameraObject;
	private boolean isGodownComponent;
	private boolean isInspectObject;
	private boolean isUniversalLayoutDropdown;
	
	private boolean isLine;
	
	private int inspectTypeID;
	
	private double convertRatioWidth = 1;
	private double convertRatioDeep = 1;
	private double convertRatioHeight = 1;
	
	private Bitmap bmpThump = Bitmap.createBitmap(1, 1, Config.ARGB_4444);
	private SVG svgObj;
	
	private int multipleType;
	
	public static final Parcelable.Creator<InspectDataItem> CREATOR = new Parcelable.Creator<InspectDataItem>()
	{

		@Override
		public InspectDataItem createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new InspectDataItem(source);
		}

		@Override
		public InspectDataItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InspectDataItem[size];
		}
		
	};
	public InspectDataItem(Parcel source) {
		// TODO Auto-generated constructor stub
		/*
		dest.writeInt(this.getInspectDataItemID());
		dest.writeString(this.getInspectDataItemName());
		dest.writeParcelable(this.groupType,0);
		dest.writeInt(this.getParameters());
		dest.writeString(this.getFormula());
		//dest.writeInt(this.getDensityValue());
		dest.writeString(this.getImageFileName());
		dest.writeBooleanArray(new boolean[]{this.isLayoutComponent,this.isCameraObject});

		 */
		this.setInspectDataItemID(source.readInt());
		this.setInspectDataItemName(source.readString());
		this.setGroupType(
				(InspectDataGroupType)source.readParcelable(InspectDataGroupType.class.getClassLoader()));
		this.setParameters(source.readInt());
		this.setFormula(source.readString());
		this.setKilogram(source.readDouble());
		//this.setDensityValue(source.readInt());
		this.setImageFileName(source.readString());
		boolean[] b = new boolean[5];
		source.readBooleanArray(b);
		this.setLayoutComponent(b[0]);
		this.setCameraObject(b[1]);
		this.setGodownComponent(b[2]);
		this.setInspectObject(b[3]);
		this.setUniversalLayoutDropdown(b[4]);
		
		this.setMultipleType(source.readInt());
		
		this.setConvertRatioWidth(source.readDouble());
		this.setConvertRatioDeep(source.readDouble());
		this.setConvertRatioHeight(source.readDouble());
		
		this.setInspectTypeID(source.readInt());
	}

	public InspectDataItem()
	{
		
	}

	public int getInspectDataItemID() {
		return inspectDataItemID;
	}

	public void setInspectDataItemID(int inspectDataItemID) {
		this.inspectDataItemID = inspectDataItemID;
	}

	public String getInspectDataItemName() {
		return inspectDataItemName;
	}

	public void setInspectDataItemName(String inspectDataItemName) {
		this.inspectDataItemName = inspectDataItemName;
	}

	public InspectDataGroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(InspectDataGroupType groupType) {
		this.groupType = groupType;
	}

	
	public int getParameters() {
		return parameters;
	}

	public void setParameters(int parameters) {
		this.parameters = parameters;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public double getKilogram() {
	    if (kilogram == 0)
	       kilogram = 1;
	    
		return kilogram;
	}

	public void setKilogram(double kilogram) {
		this.kilogram = kilogram;
	}

	/*
	public int getDensityValue() {
		return densityValue;
	}

	public void setDensityValue(int densityValue) {
		this.densityValue = densityValue;
	}
*/
	public String getImageFileName() {
		if ((imageFileName == null)||(imageFileName.isEmpty())){
			imageFileName = "obj_bin.svg";
		}
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	
	public boolean isCameraObject() {
		//if (this.getGroupType() != null){
		//	isCameraObject =  (this.getInspectDataItemID() == 21);
		//}
		/*
		if (this.getGroupType().getInspectDataGroupTypeID() == 316){
			if (this.getInspectDataItemID() == 22){
				return true;
			}
		}
		return false;*/
		if (this.getInspectDataItemID() == 21)
		{
			int xxx = 0;
			xxx++;
		}
		return this.isCameraObject;
	}

	public void setCameraObject(boolean isCameraObject) {
		this.isCameraObject = isCameraObject;
	}

	public boolean isLayoutComponent() {
		return isLayoutComponent;
	}

	public void setLayoutComponent(boolean isLayoutComponent) {
		this.isLayoutComponent = isLayoutComponent;
	}

	public boolean isLine() {
		if (this.getGroupType().getInspectDataGroupTypeID() == 316){
			if (this.getInspectDataItemID() == 21){
				return true;
			}
		}
		return false;	
	}

	public void setLine(boolean isLine) {
		this.isLine = isLine;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.inspectDataItemID = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_DATA_ITEM_ID));
		this.inspectDataItemName = cursor.getString(cursor.getColumnIndex(COLUMN_INSPECT_ITEM_NAME));
		this.groupType = new InspectDataGroupType();
		this.groupType.onBind(cursor);
		
		this.formula = cursor.getString(cursor.getColumnIndex(COLUMN_FORMULA));
		this.imageFileName = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_FILE_NAME));
		
		this.kilogram = cursor.getDouble(cursor.getColumnIndex(COLUMN_KILOGRAM));
		try{
			this.isLayoutComponent = DataUtil.convertToBoolean(
											cursor.getString(
													cursor.getColumnIndex(COLUMN_IS_LAYOUT_COMPONENT)));
		}catch(Exception ex){
			ex.printStackTrace();
		}

		try{
			this.isCameraObject = DataUtil.convertToBoolean(
												cursor.getString(
														cursor.getColumnIndex(COLUMN_IS_CAMERA_COMPONENT)));
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		try{
			this.isGodownComponent = DataUtil.convertToBoolean(
											cursor.getString(
													cursor.getColumnIndex(COLUMN_IS_GODOWN_COMPONENT)));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		try{
			this.isInspectObject = DataUtil.convertToBoolean(
												cursor.getString(
														cursor.getColumnIndex(COLUMN_IS_PRODUCT_INSPECT_COMPONENT)));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		try{
		   this.isUniversalLayoutDropdown = 
		                DataUtil.convertToBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_IS_UNIVERSAL_LAYOUT_PRODUCT_OBJ)));
		}catch(Exception ex){
		   ex.printStackTrace();
		}
		/////////////
		this.multipleType = cursor.getInt(cursor.getColumnIndex(COLUMN_MULTIPLE_TYPE));
		
		this.convertRatioWidth = cursor.getDouble(cursor.getColumnIndex(COLUMN_RATIO_WIDTH));
		this.convertRatioDeep = cursor.getDouble(cursor.getColumnIndex(COLUMN_RATIO_DEEP));
		this.convertRatioHeight = cursor.getDouble(cursor.getColumnIndex(COLUMN_RATIO_HEIGHT));
		
		this.inspectTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_INSPECT_TYPE_ID));
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		 /*
		 public final static String COLUMN_INSPECT_DATA_ITEM_ID = "inspectDataItemID";
		public final static String COLUMN_INSPECT_ITEM_NAME = "inspectDataItemName";
		public final static String COLUMN_INSPECT_GRP_TYPE_ID = "inspectDataGroupTypeID";
		public final static String COLUMN_PARAMETERS = "parameters";
		public final static String COLUMN_FORMULA = "formula";
		//public final static String COLUMN_DENSITY_VALUE = "densityValue";
		public final static String COLUMN_IMAGE_FILE_NAME = "imageFileName";
	
		public final static String COLUMN_IS_LAYOUT_COMPONENT = "isBuildingComponent";
		public final static String COLUMN_IS_CAMERA_COMPONENT = "isCameraObject";

		  */
		dest.writeInt(this.getInspectDataItemID());
		dest.writeString(this.getInspectDataItemName());
		dest.writeParcelable(this.groupType,0);
		dest.writeInt(this.getParameters());
		dest.writeString(this.getFormula());
		dest.writeDouble(this.getKilogram());
		dest.writeString(this.getImageFileName());
		dest.writeBooleanArray(new boolean[]{this.isLayoutComponent,this.isCameraObject,this.isGodownComponent,this.isInspectObject,this.isUniversalLayoutDropdown});
		dest.writeInt(this.getMultipleType());
		
		dest.writeDouble(this.getConvertRatioWidth());
		dest.writeDouble(this.getConvertRatioDeep());
		dest.writeDouble(this.getConvertRatioHeight());
		
		dest.writeInt(this.getInspectTypeID());
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.inspectDataItemID = JSONDataUtil.getInt(jsonObj,COLUMN_INSPECT_DATA_ITEM_ID);
		this.groupType = new InspectDataGroupType();
		this.groupType.setInspectDataGroupTypeID(JSONDataUtil.getInt(jsonObj,COLUMN_INSPECT_GRP_TYPE_ID));
		this.inspectDataItemName = JSONDataUtil.getString(jsonObj,COLUMN_INSPECT_ITEM_NAME);
		this.formula = JSONDataUtil.getString(jsonObj, COLUMN_FORMULA);
		this.imageFileName = JSONDataUtil.getString(jsonObj,COLUMN_IMAGE_FILE_NAME);
		
		int iIsBuildingComponent = JSONDataUtil.getInt(jsonObj,COLUMN_IS_LAYOUT_COMPONENT);
		int iIsCameraObject = JSONDataUtil.getInt(jsonObj,COLUMN_IS_CAMERA_COMPONENT);
		int iIsGoDownComponent = JSONDataUtil.getInt(jsonObj, COLUMN_IS_GODOWN_COMPONENT);
		int iInspectObject = JSONDataUtil.getInt(jsonObj, COLUMN_IS_PRODUCT_INSPECT_COMPONENT);
		int iIsUniveralObj = JSONDataUtil.getInt(jsonObj, COLUMN_IS_UNIVERSAL_LAYOUT_PRODUCT_OBJ);
		
		this.isLayoutComponent = (iIsBuildingComponent > 0);
		this.isCameraObject = (iIsCameraObject > 0);
		this.isGodownComponent = (iIsGoDownComponent > 0);
		this.isInspectObject = (iInspectObject > 0);
		this.isUniversalLayoutDropdown = (iIsUniveralObj > 0);
		
		this.kilogram = JSONDataUtil.getDouble(jsonObj, COLUMN_KILOGRAM);

		this.multipleType = JSONDataUtil.getInt(jsonObj, COLUMN_MULTIPLE_TYPE);
		
		this.convertRatioWidth = JSONDataUtil.getDouble(jsonObj,COLUMN_RATIO_WIDTH);
		if (this.convertRatioWidth <= 0)
			this.convertRatioWidth = 1;
		
		this.convertRatioDeep = JSONDataUtil.getDouble(jsonObj,COLUMN_RATIO_DEEP);
		if (this.convertRatioDeep <= 0)
			this.convertRatioDeep = 1;
		
		this.convertRatioHeight = JSONDataUtil.getDouble(jsonObj,COLUMN_RATIO_HEIGHT);
		if (this.convertRatioHeight <= 0)
			this.convertRatioHeight = 1;

		
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
		/*
		public final static String COLUMN_INSPECT_DATA_ITEM_ID = "inspectDataItemID";
		public final static String COLUMN_INSPECT_ITEM_NAME = "inspectDataItemName";
		public final static String COLUMN_INSPECT_GRP_TYPE_ID = "inspectDataGroupTypeID";
		public final static String COLUMN_PARAMETERS = "parameters";
		public final static String COLUMN_FORMULA = "formula";
		//public final static String COLUMN_DENSITY_VALUE = "densityValue";
		public final static String COLUMN_IMAGE_FILE_NAME = "imageFileName";
	
		public final static String COLUMN_IS_LAYOUT_COMPONENT = "isBuildingComponent";
		public final static String COLUMN_IS_CAMERA_COMPONENT = "isCameraObject";

		*/
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into InspectDataItem");
		strBld.append("(");
		strBld.append(COLUMN_INSPECT_DATA_ITEM_ID+",");
		strBld.append(COLUMN_INSPECT_ITEM_NAME+",");
		strBld.append(COLUMN_INSPECT_GRP_TYPE_ID+",");
		strBld.append(COLUMN_FORMULA+",");
		strBld.append(COLUMN_KILOGRAM+",");
		strBld.append(COLUMN_IMAGE_FILE_NAME+",");
		strBld.append(COLUMN_IS_LAYOUT_COMPONENT+",");
		strBld.append(COLUMN_IS_CAMERA_COMPONENT+",");
		strBld.append(COLUMN_IS_GODOWN_COMPONENT+",");
		strBld.append(COLUMN_IS_PRODUCT_INSPECT_COMPONENT+",");
		strBld.append(COLUMN_IS_UNIVERSAL_LAYOUT_PRODUCT_OBJ+",");
		strBld.append(COLUMN_MULTIPLE_TYPE+",");
		strBld.append(COLUMN_RATIO_WIDTH+",");
		strBld.append(COLUMN_RATIO_DEEP+",");
		strBld.append(COLUMN_RATIO_HEIGHT+",");
		strBld.append(COLUMN_INSPECT_TYPE_ID);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.inspectDataItemID+",");
		strBld.append("'"+this.inspectDataItemName+"',");
		strBld.append(this.groupType.getInspectDataGroupTypeID()+",");
		strBld.append("'"+this.formula+"',");
		strBld.append(""+this.kilogram+",");
		strBld.append("'"+this.imageFileName+"',");
		strBld.append("'"+this.isLayoutComponent+"',");
		strBld.append("'"+this.isCameraObject+"',");
		strBld.append("'"+this.isGodownComponent+"',");
		strBld.append("'"+this.isInspectObject+"',");
		strBld.append("'"+this.isUniversalLayoutDropdown+"',");
		strBld.append(""+this.multipleType+",");
		strBld.append(""+this.convertRatioWidth+",");
		strBld.append(""+this.convertRatioDeep+",");
		strBld.append(""+this.convertRatioHeight+",");
		strBld.append(""+this.inspectTypeID+"");
		strBld.append(")");
		return strBld.toString(); 
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Bitmap getBmpThump() {
		return bmpThump;
	}

	public void setBmpThump(Bitmap bmpThump) {
		this.bmpThump = bmpThump;
	}

	public SVG getSvgObj() {
		return svgObj;
	}

	public void setSvgObj(SVG svgObj) {
		this.svgObj = svgObj;
	}

	public boolean isComponentBuiding()
	{
		/*
		boolean bRet = true;
		String formula = this.getFormula();
		if ((formula != null)&&(!formula.isEmpty())){
			if (!this.isLine())
			{
				bRet = false;
			}
		}
		return bRet;*/
		return this.isLayoutComponent;
	}

	public int getMultipleType() {
		return multipleType;
	}

	public void setMultipleType(int multipleType) {
		this.multipleType = multipleType;
	}

	public boolean isGodownComponent() {
		return isGodownComponent;
	}

	public void setGodownComponent(boolean isGodownComponent) {
		this.isGodownComponent = isGodownComponent;
	}

	public boolean isInspectObject() {
		/*
		if ((this.formula != null)&&(this.formula.trim().length() > 0)){
				return true;
		}*/
		return isInspectObject;
	}

	public void setInspectObject(boolean isInspectObject) {
		this.isInspectObject = isInspectObject;
	}
/*
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}*/

	public double getConvertRatioWidth() {
		if (convertRatioWidth == 1)return convertRatioWidth;
		
		return (convertRatioWidth/CONVERT_CM_TO_METER);
	}
	public double getConvertRatioDeep() {
		if (convertRatioDeep == 1)return convertRatioDeep;
		
		return (convertRatioDeep/CONVERT_CM_TO_METER);
	}
	public double getConvertRatioHeight() {
		if (convertRatioHeight == 1)return convertRatioHeight;
		
		return (convertRatioHeight/CONVERT_CM_TO_METER);
	}

	public void setConvertRatioWidth(double convertRatioWidth) {
		this.convertRatioWidth = convertRatioWidth;
	}


	public void setConvertRatioDeep(double convertRatioDeep) {
		this.convertRatioDeep = convertRatioDeep;
	}


	public void setConvertRatioHeight(double convertRatioHeight) {
		this.convertRatioHeight = convertRatioHeight;
	}

   public int getInspectTypeID() {
      return inspectTypeID;
   }

   public void setInspectTypeID(int inspectTypeID) {
      this.inspectTypeID = inspectTypeID;
   }

   public boolean isUniversalLayoutDropdown() {
      return isUniversalLayoutDropdown;
   }

   public void setUniversalLayoutDropdown(boolean isUniversalLayoutDropdown) {
      this.isUniversalLayoutDropdown = isUniversalLayoutDropdown;
   }
}

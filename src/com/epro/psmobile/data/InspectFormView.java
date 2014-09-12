package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextWatcher;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class InspectFormView implements DbCursorHolder, JSONDataHolder , TransactionStmtHolder , Parcelable
{

   /*
    * inspectFormViewID INTEGER,
    colNo INTEGER,
    colFomula TEXT,
    colEditable TEXT,
    colHidden TEXT,
    colType INTEGER,
    colTextDisplay TEXT,
    colDefaultValue TEXT,
    colWidth REAL,
    colInvokeField TEXT,
    reasonSentenceCode TEXT,
    taskFormTemplateID INTEGER
    */
   public final static String COL_INSPECT_FORM_VIEW_ID = "inspectFormViewID";
   public final static String COL_COLUMN_NO = "colNo";
   public final static String COL_COLUMN_FORMULA = "colFomula";
   public final static String COL_EDITABLE = "colEditable";
   public final static String COL_HIDDEN = "colHidden";
   public final static String COL_TYPE = "colType";
   public final static String COL_TEXT_DISPLAY = "colTextDisplay";
   public final static String COL_DEFAULT_VALUE = "colDefaultValue";
   public final static String COL_WIDTH = "colWidth";
   public final static String COL_INVOKE_FIELD  = "colInvokeField";
   public final static String COL_REASON_SENTENCE_CODE = "reasonSentenceCode";
   public final static String COL_TASK_FORM_TEMPLATE_ID = "taskFormTemplateID";
   
   public final static String COL_DATE_TIME_FORMAT = "dateTimeFormate";
   public final static String COL_DECIMAL_FORMAT = "decimalFormat";
   
   private int inspectFormViewID;
   private int colNo;
   private String colFormula;
   private boolean isColEditable;
   private boolean isColHidden;
   private int colType;
   private String colTextDisplay;
   private String colDefaultValue;
   private float colWidth;
   private String colInvokeField;
   private String reasonSentenceCode;
   private int taskFormTemplateID;
   
   private String dateTimeFormate;
   private String decimalFormat;
   
   public TextWatcher textWatcher;
   
   
   public static final Parcelable.Creator<InspectFormView> CREATOR = new Parcelable.Creator<InspectFormView>()
         {

             @Override
             public InspectFormView createFromParcel(Parcel source) {
                 // TODO Auto-generated method stub
                 return new InspectFormView(source);
             }

             @Override
             public InspectFormView[] newArray(int size) {
                 // TODO Auto-generated method stub
                 return new InspectFormView[size];
             }
     
         };
   public InspectFormView(Parcel source)
   {
      inspectFormViewID = source.readInt();
      colNo = source.readInt();
      colFormula = source.readString();
      source.readBooleanArray(new boolean[]{isColEditable});
      source.readBooleanArray(new boolean[]{isColHidden});
      colType = source.readInt();
      colTextDisplay = source.readString();
      colDefaultValue = source.readString();
      colWidth = source.readFloat();
      colInvokeField = source.readString();
      reasonSentenceCode = source.readString();
      taskFormTemplateID = source.readInt();
      dateTimeFormate = source.readString();
      decimalFormat = source.readString();

   }
   public InspectFormView() {
      // TODO Auto-generated constructor stub
   }

   @Override
   public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
      // TODO Auto-generated method stub
      
      /*
       private int inspectFormViewID;
   private int colNo;
   private String colFormula;
   private boolean isColEditable;
   private boolean isColHidden;
   private int colType;
   private String colTextDisplay;
   private String colDefaultValue;
   private float colWidth;
   private String colInvokeField;
   private String reasonSentenceCode;
   private int taskFormTemplateID;
       */
      this.inspectFormViewID = JSONDataUtil.getInt(jsonObj,InspectFormView.COL_INSPECT_FORM_VIEW_ID);
      this.colNo = JSONDataUtil.getInt(jsonObj, InspectFormView.COL_COLUMN_NO);
      this.colFormula = JSONDataUtil.getString(jsonObj, InspectFormView.COL_COLUMN_FORMULA);
      this.isColEditable = JSONDataUtil.getBoolean(jsonObj, InspectFormView.COL_EDITABLE);
      this.isColHidden = JSONDataUtil.getBoolean(jsonObj, InspectFormView.COL_HIDDEN);
      this.colType = JSONDataUtil.getInt(jsonObj, InspectFormView.COL_TYPE);
      this.colTextDisplay = JSONDataUtil.getString(jsonObj, InspectFormView.COL_TEXT_DISPLAY);
      this.colDefaultValue = JSONDataUtil.getString(jsonObj, InspectFormView.COL_DEFAULT_VALUE);
      this.colWidth = (float)JSONDataUtil.getDouble(jsonObj, InspectFormView.COL_WIDTH);
      this.colInvokeField = JSONDataUtil.getString(jsonObj, InspectFormView.COL_INVOKE_FIELD);
      this.reasonSentenceCode = JSONDataUtil.getString(jsonObj, InspectFormView.COL_REASON_SENTENCE_CODE);
      this.taskFormTemplateID = JSONDataUtil.getInt(jsonObj, InspectFormView.COL_TASK_FORM_TEMPLATE_ID);      
      
      this.dateTimeFormate = JSONDataUtil.getString(jsonObj, InspectFormView.COL_DATE_TIME_FORMAT);
      this.decimalFormat = JSONDataUtil.getString(jsonObj, InspectFormView.COL_DECIMAL_FORMAT);
      
   }

   @Override
   public JSONObject getJSONObject() throws JSONException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void onBind(Cursor cursor) {
      // TODO Auto-generated method stub
      this.inspectFormViewID = cursor.getInt(cursor.getColumnIndex(InspectFormView.COL_INSPECT_FORM_VIEW_ID));
      this.colNo = cursor.getInt(cursor.getColumnIndex(InspectFormView.COL_COLUMN_NO));
      this.colFormula = cursor.getString(cursor.getColumnIndex(InspectFormView.COL_COLUMN_FORMULA));
      try{
         this.isColEditable = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(InspectFormView.COL_EDITABLE)));
      }catch(Exception ex){}
      
      try{
         this.isColHidden = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(InspectFormView.COL_HIDDEN)));
      }catch(Exception ex){}
      
      this.colType = cursor.getInt(cursor.getColumnIndex(InspectFormView.COL_TYPE));
      this.colTextDisplay =cursor.getString(cursor.getColumnIndex(InspectFormView.COL_TEXT_DISPLAY));
      
      this.colDefaultValue = cursor.getString(cursor.getColumnIndex(InspectFormView.COL_DEFAULT_VALUE));
      this.colWidth = (float)cursor.getFloat(cursor.getColumnIndex( InspectFormView.COL_WIDTH));
      this.colInvokeField = cursor.getString(cursor.getColumnIndex(InspectFormView.COL_INVOKE_FIELD));
      this.reasonSentenceCode = cursor.getString(cursor.getColumnIndex(InspectFormView.COL_REASON_SENTENCE_CODE));
      this.taskFormTemplateID = cursor.getInt(cursor.getColumnIndex(InspectFormView.COL_TASK_FORM_TEMPLATE_ID));
      
      
      this.dateTimeFormate = cursor.getString(cursor.getColumnIndex(InspectFormView.COL_DATE_TIME_FORMAT));
      this.decimalFormat = cursor.getString(cursor.getColumnIndex(InspectFormView.COL_DECIMAL_FORMAT));
      
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
      strBld.append("insert into UniversalListFormView");
      strBld.append("(");
      strBld.append(COL_INSPECT_FORM_VIEW_ID+",");
      strBld.append(COL_COLUMN_NO+",");
      strBld.append(COL_COLUMN_FORMULA+",");
      strBld.append(COL_EDITABLE+",");
      strBld.append(COL_HIDDEN+",");
      strBld.append(COL_TYPE+",");
      strBld.append(COL_TEXT_DISPLAY+",");
      strBld.append(COL_DEFAULT_VALUE+",");
      strBld.append(COL_WIDTH+",");
      strBld.append(COL_INVOKE_FIELD+",");
      strBld.append(COL_REASON_SENTENCE_CODE+",");
      strBld.append(COL_TASK_FORM_TEMPLATE_ID+",");
      strBld.append(COL_DATE_TIME_FORMAT+",");
      strBld.append(COL_DECIMAL_FORMAT);
      strBld.append(")");
      strBld.append(" values");
      strBld.append("(");
      strBld.append(this.inspectFormViewID+",");
      strBld.append(this.colNo+",");
      strBld.append("'"+this.colFormula+"',");
      strBld.append("'"+this.isColEditable+"',");
      strBld.append("'"+this.isColHidden+"',");
      strBld.append(""+this.colType+",");
      strBld.append("'"+this.colTextDisplay+"',");
      strBld.append("'"+this.colDefaultValue+"',");
      strBld.append(""+this.colWidth+",");
      strBld.append("'"+this.colInvokeField+"',");
      strBld.append("'"+this.reasonSentenceCode+"',");
      strBld.append(""+this.taskFormTemplateID+",");
      strBld.append("'"+this.dateTimeFormate+"',");
      strBld.append("'"+this.decimalFormat+"'");
      strBld.append(")");
      return strBld.toString();
   }

   @Override
   public String updateStatement() throws Exception {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * @return the inspectFormViewID
    */
   public int getInspectFormViewID() {
      return inspectFormViewID;
   }

   /**
    * @param inspectFormViewID the inspectFormViewID to set
    */
   public void setInspectFormViewID(int inspectFormViewID) {
      this.inspectFormViewID = inspectFormViewID;
   }

   /**
    * @return the colNo
    */
   public int getColNo() {
      return colNo;
   }

   /**
    * @param colNo the colNo to set
    */
   public void setColNo(int colNo) {
      this.colNo = colNo;
   }

   /**
    * @return the colFormula
    */
   public String getColFormula() {
      return colFormula;
   }

   /**
    * @param colFormula the colFormula to set
    */
   public void setColFormula(String colFormula) {
      this.colFormula = colFormula;
   }

   /**
    * @return the isColEditable
    */
   public boolean isColEditable() {
      return isColEditable;
   }

   /**
    * @param isColEditable the isColEditable to set
    */
   public void setColEditable(boolean isColEditable) {
      this.isColEditable = isColEditable;
   }

   /**
    * @return the isColHidden
    */
   public boolean isColHidden() {
      return isColHidden;
   }

   /**
    * @param isColHidden the isColHidden to set
    */
   public void setColHidden(boolean isColHidden) {
      this.isColHidden = isColHidden;
   }

   /**
    * @return the colType
    */
   public int getColType() {
      return colType;
   }

   /**
    * @param colType the colType to set
    */
   public void setColType(int colType) {
      this.colType = colType;
   }

   /**
    * @return the colTextDisplay
    */
   public String getColTextDisplay() {
      return colTextDisplay;
   }

   /**
    * @param colTextDisplay the colTextDisplay to set
    */
   public void setColTextDisplay(String colTextDisplay) {
      this.colTextDisplay = colTextDisplay;
   }

   /**
    * @return the colDefaultValue
    */
   public String getColDefaultValue() {
      return colDefaultValue;
   }

   /**
    * @param colDefaultValue the colDefaultValue to set
    */
   public void setColDefaultValue(String colDefaultValue) {
      this.colDefaultValue = colDefaultValue;
   }

   /**
    * @return the colWidth
    */
   public float getColWidth() {
      return colWidth;
   }

   /**
    * @param colWidth the colWidth to set
    */
   public void setColWidth(float colWidth) {
      this.colWidth = colWidth;
   }

   /**
    * @return the colInvokeField
    */
   public String getColInvokeField() {
      return colInvokeField;
   }

   /**
    * @param colInvokeField the colInvokeField to set
    */
   public void setColInvokeField(String colInvokeField) {
      this.colInvokeField = colInvokeField;
   }

   /**
    * @return the reasonSentenceCode
    */
   public String getReasonSentenceCode() {
      return reasonSentenceCode;
   }

   /**
    * @param reasonSentenceCode the reasonSentenceCode to set
    */
   public void setReasonSentenceCode(String reasonSentenceCode) {
      this.reasonSentenceCode = reasonSentenceCode;
   }

   /**
    * @return the taskFormTemplateID
    */
   public int getTaskFormTemplateID() {
      return taskFormTemplateID;
   }

   /**
    * @param taskFormTemplateID the taskFormTemplateID to set
    */
   public void setTaskFormTemplateID(int taskFormTemplateID) {
      this.taskFormTemplateID = taskFormTemplateID;
   }

   public String getDateTimeFormate() {
      return dateTimeFormate;
   }

   public void setDateTimeFormate(String dateTimeFormate) {
      this.dateTimeFormate = dateTimeFormate;
   }

   public String getDecimalFormat() {
      return decimalFormat;
   }

   public void setDecimalFormat(String decimalFormat) {
      this.decimalFormat = decimalFormat;
   }

   @Override
   public int describeContents() {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      // TODO Auto-generated method stub
      dest.writeInt(inspectFormViewID);
      dest.writeInt(colNo);
      dest.writeString(colFormula);
      dest.writeBooleanArray(new boolean[]{isColEditable});
      dest.writeBooleanArray(new boolean[]{isColHidden});
      dest.writeInt(colType);
      dest.writeString(colTextDisplay);
      dest.writeString(colDefaultValue);
      dest.writeFloat(colWidth);
      dest.writeString(colInvokeField);
      dest.writeString(reasonSentenceCode);
      dest.writeInt(taskFormTemplateID);
      
      dest.writeString(dateTimeFormate);
      dest.writeString(decimalFormat);

   }

}

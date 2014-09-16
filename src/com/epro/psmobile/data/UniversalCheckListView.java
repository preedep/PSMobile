package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class UniversalCheckListView implements DbCursorHolder, JSONDataHolder, TransactionStmtHolder {

   public final static String COL_TASK_FORM_TEMPLATE_ID = "taskFormTemplateID";
   public final static String COL_TASK_FORM_ATTRIBUTE_ID = "taskFormAttributeID";
   public final static String COL_INVOKE_FIELDS = "colInvokeField";
   
   private int taskFormTemplateID;
   private int taskFormAttributeID;
   private String colInvokeField;
   public UniversalCheckListView() {
      // TODO Auto-generated constructor stub
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
      strBld.append("insert into UniversalCheckListView");
      strBld.append("(");
      strBld.append(COL_TASK_FORM_TEMPLATE_ID+",");
      strBld.append(COL_TASK_FORM_ATTRIBUTE_ID+",");
      strBld.append(COL_INVOKE_FIELDS);
      strBld.append(")");
      strBld.append(" values");
      strBld.append("(");
      strBld.append(""+this.taskFormTemplateID+",");
      strBld.append(""+this.taskFormAttributeID+",");
      strBld.append("'"+this.colInvokeField+"'");
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
      this.taskFormTemplateID = JSONDataUtil.getInt(jsonObj, COL_TASK_FORM_TEMPLATE_ID);
      this.taskFormAttributeID = JSONDataUtil.getInt(jsonObj, COL_TASK_FORM_ATTRIBUTE_ID);
      this.colInvokeField = JSONDataUtil.getString(jsonObj, COL_INVOKE_FIELDS);
   }

   @Override
   public JSONObject getJSONObject() throws JSONException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void onBind(Cursor cursor) {
      // TODO Auto-generated method stub
      this.taskFormTemplateID = cursor.getInt(cursor.getColumnIndex(COL_TASK_FORM_TEMPLATE_ID));
      this.taskFormAttributeID = cursor.getInt(cursor.getColumnIndex(COL_TASK_FORM_ATTRIBUTE_ID));
      this.colInvokeField = cursor.getString(cursor.getColumnIndex(COL_INVOKE_FIELDS));
   }

   public int getTaskFormTemplateID() {
      return taskFormTemplateID;
   }

   public void setTaskFormTemplateID(int taskFormTemplateID) {
      this.taskFormTemplateID = taskFormTemplateID;
   }

   public int getTaskFormAttributeID() {
      return taskFormAttributeID;
   }

   public void setTaskFormAttributeID(int taskFormAttributeID) {
      this.taskFormAttributeID = taskFormAttributeID;
   }

   public String getColInvokeField() {
      return colInvokeField;
   }

   public void setColInvokeField(String colInvokeField) {
      this.colInvokeField = colInvokeField;
   }

}

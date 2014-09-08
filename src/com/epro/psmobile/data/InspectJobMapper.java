package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class InspectJobMapper implements DbCursorHolder, JSONDataHolder, TransactionStmtHolder {

   /*
    inspectFormViewID INTEGER,
    inspectFormViewNo INTEGER,
    jobRequestID INTEGER,
    taskCode TEXT,
    isAudit TEXT
    */
   public final static String COL_INSPECT_FORM_VIEW_ID = "inspectFormViewID";
   public final static String COL_INSPECT_FORM_VIEW_NO = "inspectFormViewNo";
   public final static String COL_JOBREQUEST_ID = "jobRequestID";
   public final static String COL_TASK_CODE = "taskCode";
   public final static String COL_IS_AUDIT = "isAudit";
   
   private int inspectFormViewID;
   private int inspectFormViewNo;
   private int jobRequestID;
   private String taskCode;
   private boolean isAudit;
   
   public InspectJobMapper() {
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
      strBld.append("insert into UniversalJobMapper");
      strBld.append("(");
      strBld.append(COL_INSPECT_FORM_VIEW_ID+",");
      strBld.append(COL_INSPECT_FORM_VIEW_NO+",");
      strBld.append(COL_JOBREQUEST_ID+",");
      strBld.append(COL_TASK_CODE+",");
      strBld.append(COL_IS_AUDIT+"");
      strBld.append(")");
      strBld.append(" values");
      strBld.append("(");
      strBld.append(this.inspectFormViewID+",");
      strBld.append(this.inspectFormViewNo+",");
      strBld.append(""+this.jobRequestID+",");
      strBld.append("'"+this.taskCode+"',");
      strBld.append("'"+this.isAudit+"'");
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
      this.inspectFormViewID = JSONDataUtil.getInt(jsonObj, COL_INSPECT_FORM_VIEW_ID);
      this.inspectFormViewNo =  JSONDataUtil.getInt(jsonObj,COL_INSPECT_FORM_VIEW_NO );
      this.jobRequestID = JSONDataUtil.getInt(jsonObj, COL_JOBREQUEST_ID );
      this.taskCode =  JSONDataUtil.getString(jsonObj, COL_TASK_CODE);
      this.isAudit = JSONDataUtil.getBoolean(jsonObj,COL_IS_AUDIT );

   }

   @Override
   public JSONObject getJSONObject() throws JSONException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void onBind(Cursor cursor) {
      // TODO Auto-generated method stub
      this.inspectFormViewID = cursor.getInt(cursor.getColumnIndex(COL_INSPECT_FORM_VIEW_ID));
      this.inspectFormViewNo =  cursor.getInt(cursor.getColumnIndex(COL_INSPECT_FORM_VIEW_NO ));
      this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COL_JOBREQUEST_ID ));
      this.taskCode =  cursor.getString(cursor.getColumnIndex(COL_TASK_CODE));
      try{
         this.isAudit = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COL_IS_AUDIT )));
      }catch(Exception ex){}
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
    * @return the inspectFormViewNo
    */
   public int getInspectFormViewNo() {
      return inspectFormViewNo;
   }

   /**
    * @param inspectFormViewNo the inspectFormViewNo to set
    */
   public void setInspectFormViewNo(int inspectFormViewNo) {
      this.inspectFormViewNo = inspectFormViewNo;
   }

   /**
    * @return the jobRequestID
    */
   public int getJobRequestID() {
      return jobRequestID;
   }

   /**
    * @param jobRequestID the jobRequestID to set
    */
   public void setJobRequestID(int jobRequestID) {
      this.jobRequestID = jobRequestID;
   }

   /**
    * @return the taskCode
    */
   public String getTaskCode() {
      return taskCode;
   }

   /**
    * @param taskCode the taskCode to set
    */
   public void setTaskCode(String taskCode) {
      this.taskCode = taskCode;
   }

   /**
    * @return the isAudit
    */
   public boolean isAudit() {
      return isAudit;
   }

   /**
    * @param isAudit the isAudit to set
    */
   public void setAudit(boolean isAudit) {
      this.isAudit = isAudit;
   }

}

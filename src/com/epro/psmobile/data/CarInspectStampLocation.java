package com.epro.psmobile.data;

import android.database.Cursor;

public class CarInspectStampLocation implements TransactionStmtHolder, DbCursorHolder {

   public final static String COLUMN_TASK_ID = "taskID";
   public final static String COLUMN_TASK_CODE = "taskCode";
   public final static String COLUMN_TASK_DUPLICATED_NO = "taskDuplicateNo";
   public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
   public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
   public final static String COLUMN_SITE_ADDRESS = "siteAddress";
   public final static String COLUMN_MILES_NO = "milesNo";
   public final static String COLUMN_TIME_RECORDED = "timeRecorded";
   public final static String COLUMN_FLAG_ADD_NEW_SITE = "flagIsAddNewCustomerSite";
   public final static String COLUMN_SITE_LAT = "siteLat";
   public final static String COLUMN_SITE_LON = "siteLon";
   
         
   private int taskID;
   
   private String taskCode;
   private int taskDuplicateNo;
   private int jobRequestID;
   private int customerSurveySiteID;
   private String siteAddress;
   private String milesNo;
   private String timeRecorded;
   private String flagIsAddNewCustomerSite = "N";
   
   private float siteLat;
   private float siteLon;
   
   
   public CarInspectStampLocation() {
      // TODO Auto-generated constructor stub
   }

   @Override
   public void onBind(Cursor cursor) {
      // TODO Auto-generated method stub
      this.taskID = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID));
      this.taskCode = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_CODE));
      this.taskDuplicateNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_DUPLICATED_NO));
      this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COLUMN_JOB_REQUEST_ID));
      this.customerSurveySiteID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ID));
      this.siteAddress = cursor.getString(cursor.getColumnIndex(COLUMN_SITE_ADDRESS));
      this.milesNo = cursor.getString(cursor.getColumnIndex(COLUMN_MILES_NO));
      this.timeRecorded = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_RECORDED));
      this.flagIsAddNewCustomerSite = cursor.getString(cursor.getColumnIndex(COLUMN_FLAG_ADD_NEW_SITE));
      
      this.siteLat = cursor.getFloat(cursor.getColumnIndex(COLUMN_SITE_LAT));
      this.siteLon = cursor.getFloat(cursor.getColumnIndex(COLUMN_SITE_LON));
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
      strBld.append("insert into CarInspectStampLocation");
      strBld.append("(");
      /*
          private final static String COLUMN_TASK_CODE = "taskCode";
   private final static String COLUMN_TASK_DUPLICATED_NO = "taskDuplicateNo";
   private final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
   private final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
   private final static String COLUMN_SITE_ADDRESS = "siteAddress";
   private final static String COLUMN_MILES_NO = "milesNo";
   private final static String COLUMN_TIME_RECORDED = "timeRecorded";

       */
      strBld.append(COLUMN_TASK_CODE+",");
      strBld.append(COLUMN_TASK_DUPLICATED_NO+",");
      strBld.append(COLUMN_JOB_REQUEST_ID+",");
      strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+",");
      strBld.append(COLUMN_SITE_ADDRESS+",");
      strBld.append(COLUMN_MILES_NO+",");
      strBld.append(COLUMN_TIME_RECORDED+",");
      strBld.append(COLUMN_FLAG_ADD_NEW_SITE+",");
      strBld.append(COLUMN_TASK_ID+",");
      strBld.append(COLUMN_SITE_LAT+",");
      strBld.append(COLUMN_SITE_LON);
      strBld.append(")");
      strBld.append(" values");
      strBld.append("(");
      strBld.append("'"+this.taskCode+"',");
      strBld.append(""+this.taskDuplicateNo+",");
      strBld.append(""+this.jobRequestID+",");
      strBld.append(""+this.customerSurveySiteID+",");
      strBld.append("'"+this.siteAddress+"',");
      strBld.append("'"+this.milesNo+"',");
      strBld.append("'"+this.timeRecorded+"',");
      strBld.append("'"+this.flagIsAddNewCustomerSite+"',");
      strBld.append(""+this.taskID+",");
      strBld.append(this.siteLat+",");
      strBld.append(this.siteLon);
      strBld.append(")");
      return strBld.toString();
   }

   @Override
   public String updateStatement() throws Exception {
      // TODO Auto-generated method stub
      return null;
   }

   public int getTaskID() {
      return taskID;
   }

   public void setTaskID(int taskID) {
      this.taskID = taskID;
   }

   public String getTaskCode() {
      return taskCode;
   }

   public void setTaskCode(String taskCode) {
      this.taskCode = taskCode;
   }

   public int getTaskDuplicateNo() {
      return taskDuplicateNo;
   }

   public void setTaskDuplicateNo(int taskDuplicateNo) {
      this.taskDuplicateNo = taskDuplicateNo;
   }

   public int getJobRequestID() {
      return jobRequestID;
   }

   public void setJobRequestID(int jobRequestID) {
      this.jobRequestID = jobRequestID;
   }

   public int getCustomerSurveySiteID() {
      return customerSurveySiteID;
   }

   public void setCustomerSurveySiteID(int customerSurveySiteID) {
      this.customerSurveySiteID = customerSurveySiteID;
   }

   public String getSiteAddress() {
      return siteAddress;
   }

   public void setSiteAddress(String siteAddress) {
      this.siteAddress = siteAddress;
   }

   public String getMilesNo() {
      return milesNo;
   }

   public void setMilesNo(String milesNo) {
      this.milesNo = milesNo;
   }

   public String getTimeRecorded() {
      return timeRecorded;
   }

   public void setTimeRecorded(String timeRecorded) {
      this.timeRecorded = timeRecorded;
   }

   public String getFlagIsAddNewCustomerSite() {
      return flagIsAddNewCustomerSite;
   }

   public void setFlagIsAddNewCustomerSite(String flagIsAddNewCustomerSite) {
      this.flagIsAddNewCustomerSite = flagIsAddNewCustomerSite;
   }

   public float getSiteLat() {
      return siteLat;
   }

   public void setSiteLat(float siteLat) {
      this.siteLat = siteLat;
   }

   public float getSiteLon() {
      return siteLon;
   }

   public void setSiteLon(float siteLon) {
      this.siteLon = siteLon;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      // TODO Auto-generated method stub
      return this.getSiteAddress();//super.toString();
   }

}

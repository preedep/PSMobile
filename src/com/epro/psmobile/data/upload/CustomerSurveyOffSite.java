package com.epro.psmobile.data.upload;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class CustomerSurveyOffSite implements UploadDataAdapter, JSONDataHolder {

   public static String COLUMN_TASK_ID = "taskID";
   public static String COLUMN_JOB_DOC_NO = "jobDoucmentNo";
   public static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
   public static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
   public static String COLUMN_SITE_ADDRESS = "siteAddress";
   
   private CarInspectStampLocation carInspectLocation;
   public CustomerSurveyOffSite(CarInspectStampLocation carInspectStampLocation) {
      // TODO Auto-generated constructor stub
      this.carInspectLocation = carInspectStampLocation;
   }

   @Override
   public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
      // TODO Auto-generated method stub

   }

   @Override
   public JSONObject getJSONObject() throws JSONException {
      // TODO Auto-generated method stub
      JSONObject json = new JSONObject();
      if (carInspectLocation != null)
      {
        JSONDataUtil.put(json,COLUMN_TASK_ID, carInspectLocation.getTaskID());
        JSONDataUtil.put(json,COLUMN_JOB_DOC_NO, carInspectLocation.getTaskCode());
        JSONDataUtil.put(json,COLUMN_JOB_REQUEST_ID, carInspectLocation.getJobRequestID());
        JSONDataUtil.put(json,COLUMN_CUSTOMER_SURVEY_SITE_ID, carInspectLocation.getCustomerSurveySiteID());
        JSONDataUtil.put(json,COLUMN_SITE_ADDRESS, carInspectLocation.getSiteAddress());
      }
      return json;
   }

   @Override
   public void executeAdapter() {
      // TODO Auto-generated method stub

   }

}

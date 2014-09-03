package com.epro.psmobile.data.upload;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class ResultCheckJobImage implements JSONDataHolder ,UploadDataAdapter{

	/*
	private final static String ATT_JOB_ROW_ID = "job_row_id";
	private final static String ATT_JOB_LOCATION_ID = "job_location_id";
	private final static String ATT_JOB_NO = "job_no";
	private final static String ATT_IMAGE_DET_NAME = "image_detail_name";
	private final static String ATT_IMAGE_DET_QTY = "image_detail_qty";
	private final static String ATT_IMAGE_DET_NOTE = "image_detail_note";
	private final static String ATT_IMAGE_DET_PATH = "image_detail_path";
	private final static String ATT_IMAGE_LAT = "image_latitude";
	private final static String ATT_IMAGE_LON = "image_longitude";
  */
	private final static String ATT_JOB_ROW_ID =  "jobRowId";
	private final static String ATT_JOB_LOCATION_ID =  "jobLocationId";
	private final static String ATT_JOB_NO =  "jobDocumentNo";
	private final static String ATT_RESULT_PRODUCT_ROW_ID = "resultProductRowId";
	private final static String ATT_CAMERA_VIEW_NO = "cameraViewNo";
	private final static String ATT_IMAGE_DETAIL_NAME = "imageDetailName";
	private final static String ATT_IMAGE_DETAIL_QTY =  "imageDetailQty";
	private final static String ATT_IMAGE_DETAIL_NOTE =  "imageDetailNote";
	private final static String ATT_IMAGE_DETAIL_PATH =  "imageDetailPath";
	private final static String ATT_IMAGE_LAT =  "imageLatitude";
	private final static String ATT_IMAGE_LON =  "imageLongitude";
	private final static String ATT_IMAGE_NO = "imageNo";
	private final static String ATT_IMAGE_DATE = "imageDate";
	private final static String ATT_FLAG_GENERAL_IMAGE = "flagGeneralImage";
	
	private int jobRowId;
	private int jobLocationId;
	private String jobNo;
	private int resultProductRowId;
	private int cameraViewNo;
	private String imageDetailName;
	private String imageDetailQty;
	private String imageDetailNote;
	private String imageDetailPath;
	private double imageLatitude;
	private double imageLongitude;
	private String imageDate;
	private String flagGeneralImage;
	private int imageNo;
	private Task task;
	private CustomerSurveySite site;
	private InspectDataObjectSaved dataObjSaved;
	private InspectDataObjectPhotoSaved dataObjPhotoSaved;
	
	public ResultCheckJobImage(Task task,
			CustomerSurveySite site,
			int imageNo,
			InspectDataObjectSaved dataObjSaved,
			InspectDataObjectPhotoSaved dataObjPhotoSaved
			) {
		// TODO Auto-generated constructor stub
		this.task = task;
		this.site = site;
		this.dataObjSaved = dataObjSaved;
		this.dataObjPhotoSaved = dataObjPhotoSaved;
		this.imageNo = imageNo;
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj, ATT_JOB_ROW_ID ,jobRowId);
		JSONDataUtil.put(jsonObj, ATT_JOB_LOCATION_ID ,jobLocationId);
		JSONDataUtil.put(jsonObj, ATT_JOB_NO ,jobNo);
		if (resultProductRowId < 0){
			JSONDataUtil.put(jsonObj, ATT_RESULT_PRODUCT_ROW_ID ,"null");			
		}else{
			JSONDataUtil.put(jsonObj, ATT_RESULT_PRODUCT_ROW_ID ,resultProductRowId);
		}
		JSONDataUtil.put(jsonObj, ATT_CAMERA_VIEW_NO ,cameraViewNo);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_DETAIL_NAME ,imageDetailName);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_DETAIL_QTY ,imageDetailQty);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_DETAIL_NOTE ,imageDetailNote);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_DETAIL_PATH ,imageDetailPath);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_LAT ,imageLatitude);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_LON ,imageLongitude);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_NO, imageNo);
		JSONDataUtil.put(jsonObj, ATT_IMAGE_DATE, imageDate);
		
		JSONDataUtil.put(jsonObj, ATT_FLAG_GENERAL_IMAGE, flagGeneralImage);/*new requirement*/
		
		return jsonObj;
	}
	@Override
	public void executeAdapter() {
		// TODO Auto-generated method stub
		this.jobRowId = this.task.getTaskID();
		this.jobLocationId = this.site.getCustomerSurveySiteRowID();
		this.jobNo = this.task.getTaskCode();
		
		if (this.dataObjSaved != null)
		{
			this.cameraViewNo = this.dataObjSaved.getInspectDataObjectID();
		}
		if (this.dataObjPhotoSaved != null)
		{
			//this.imageDetailName = 
			//		this.dataObjPhotoSaved.getInspectDataTextSelected();
			this.flagGeneralImage = this.dataObjPhotoSaved.getFlagGeneralImage();
			this.imageDetailPath = 
					this.dataObjPhotoSaved.getFileName();
			this.imageDetailNote = 
					this.dataObjPhotoSaved.getComment();
			
			this.imageDetailQty = 
					this.dataObjPhotoSaved.getAngleDetail();
			
			this.imageLatitude = this.dataObjPhotoSaved.getLatitude();
			this.imageLongitude = this.dataObjPhotoSaved.getLongitude();
			
			this.imageDate = this.dataObjPhotoSaved.getImageDate();
			if (this.imageDate != null)
			{
				try{
					String sImgDate[] = this.imageDate.split(" ");
					String sYear = sImgDate[0].replace(":", "-");
					String sTime = sImgDate[1];
					this.imageDate = sYear+" "+sTime;
				}catch(Exception ex){}
			}
		}
	}
	public int getJobRowId() {
		return jobRowId;
	}

	public void setJobRowId(int jobRowId) {
		this.jobRowId = jobRowId;
	}

	public int getJobLocationId() {
		return jobLocationId;
	}

	public void setJobLocationId(int jobLocationId) {
		this.jobLocationId = jobLocationId;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public int getResultProductRowId() {
		return resultProductRowId;
	}

	public void setResultProductRowId(int resultProductRowId) {
		this.resultProductRowId = resultProductRowId;
	}

	public int getCameraViewNo() {
		return cameraViewNo;
	}

	public void setCameraViewNo(int cameraViewNo) {
		this.cameraViewNo = cameraViewNo;
	}

	public String getImageDetailName() {
		return imageDetailName;
	}

	public void setImageDetailName(String imageDetailName) {
		this.imageDetailName = imageDetailName;
	}

	public String getImageDetailQty() {
		return imageDetailQty;
	}

	public void setImageDetailQty(String imageDetailQty) {
		this.imageDetailQty = imageDetailQty;
	}

	public String getImageDetailNote() {
		return imageDetailNote;
	}

	public void setImageDetailNote(String imageDetailNote) {
		this.imageDetailNote = imageDetailNote;
	}

	public String getImageDetailPath() {
		return imageDetailPath;
	}

	public void setImageDetailPath(String imageDetailPath) {
		this.imageDetailPath = imageDetailPath;
	}

	public double getImageLatitude() {
		return imageLatitude;
	}

	public void setImageLatitude(double imageLatitude) {
		this.imageLatitude = imageLatitude;
	}

	public double getImageLongitude() {
		return imageLongitude;
	}

	public void setImageLongitude(double imageLongitude) {
		this.imageLongitude = imageLongitude;
	}

	public String getImageDate() {
		return imageDate;
	}

	public void setImageDate(String imageDate) {
		this.imageDate = imageDate;
	}



}

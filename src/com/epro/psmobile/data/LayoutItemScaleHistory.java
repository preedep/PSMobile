package com.epro.psmobile.data;

import android.database.Cursor;

public class LayoutItemScaleHistory implements DbCursorHolder,
		TransactionStmtHolder {

	/*
	 scaleHistoryId INTEGER PRIMARY KEY  AUTOINCREMENT,
	taskCode TEXT,
	taskDuplicateNo INTEGER,
	customerSurveySiteID INTEGER,
	customerSurveySiteRowID INTEGER,	
	siteWidth REAL,
	siteLong REAL
	 */
	public final static String COLUMN_SCALE_HISTORY_ID = "scaleHistoryId";
	public final static String COLUMN_SCALE_TASK_CODE = "taskCode";
	public final static String COLUMN_TASK_DUPLICATE_NO = "taskDuplicateNo";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID = "customerSurveySiteRowID";
	public final static String COLUMN_SITE_WIDTH = "siteWidth";
	public final static String COLUMN_SITE_LONG = "siteLong";
	
	
	private int scaleHistoryId;
	private String taskCode;
	private int duplicatedNo;
	private int customerSurveySiteId;
	private int customerSurveySiteRowId;
	private double siteWidth;
	private double siteLong;
	
	public LayoutItemScaleHistory() {
		// TODO Auto-generated constructor stub
	}

	public int getScaleHistoryId() {
		return scaleHistoryId;
	}

	public void setScaleHistoryId(int scaleHistoryId) {
		this.scaleHistoryId = scaleHistoryId;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public int getDuplicatedNo() {
		return duplicatedNo;
	}

	public void setDuplicatedNo(int duplicatedNo) {
		this.duplicatedNo = duplicatedNo;
	}

	public int getCustomerSurveySiteId() {
		return customerSurveySiteId;
	}

	public void setCustomerSurveySiteId(int customerSurveySiteId) {
		this.customerSurveySiteId = customerSurveySiteId;
	}

	public int getCustomerSurveySiteRowId() {
		return customerSurveySiteRowId;
	}

	public void setCustomerSurveySiteRowId(int customerSurveySiteRowId) {
		this.customerSurveySiteRowId = customerSurveySiteRowId;
	}

	public double getSiteWidth() {
		return siteWidth;
	}

	public void setSiteWidth(double siteWidth) {
		this.siteWidth = siteWidth;
	}

	public double getSiteLong() {
		return siteLong;
	}

	public void setSiteLong(double siteLong) {
		this.siteLong = siteLong;
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("delete from LayoutItemScaleHistory");
		strBld.append( " where taskCode='"+taskCode+"' and customerSurveySiteID="+customerSurveySiteId +"");
		return strBld.toString();
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		
		/*
		 public final static String COLUMN_SCALE_TASK_CODE = "taskCode";
	public final static String COLUMN_TASK_DUPLICATE_NO = "taskDuplicateNo";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID = "customerSurveySiteRowID";
	public final static String COLUMN_SITE_WIDTH = "siteWidth";
	public final static String COLUMN_SITE_LONG = "siteLong";
		 */
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into LayoutItemScaleHistory");
		strBld.append("(");
		strBld.append(COLUMN_SCALE_TASK_CODE+",");
		strBld.append(COLUMN_TASK_DUPLICATE_NO+",");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+",");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID+",");
		strBld.append(COLUMN_SITE_WIDTH+",");
		strBld.append(COLUMN_SITE_LONG);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append("'"+this.taskCode+"',");
		strBld.append(""+this.duplicatedNo+",");
		strBld.append(""+this.customerSurveySiteId+",");
		strBld.append(""+this.customerSurveySiteRowId+",");
		strBld.append(""+this.siteWidth+",");
		strBld.append(""+this.siteLong+"");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		/*
		 scaleHistoryId INTEGER PRIMARY KEY  AUTOINCREMENT,
		taskCode TEXT,
		taskDuplicateNo INTEGER,
		customerSurveySiteID INTEGER,
		customerSurveySiteRowID INTEGER,	
		siteWidth REAL,
		siteLong REAL
		 */
		this.scaleHistoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_SCALE_HISTORY_ID));
		this.taskCode = cursor.getString(cursor.getColumnIndex(COLUMN_SCALE_TASK_CODE));
		this.duplicatedNo = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_DUPLICATE_NO));
		this.customerSurveySiteId = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ID));
		this.customerSurveySiteRowId = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_SURVEY_SITE_ROW_ID));
		this.siteWidth = cursor.getDouble(cursor.getColumnIndex(COLUMN_SITE_WIDTH));
		this.siteLong = cursor.getDouble(cursor.getColumnIndex(COLUMN_SITE_LONG));
		
	}

}

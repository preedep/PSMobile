package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class JobRequestProductGroup implements JSONDataHolder,
		TransactionStmtHolder {

	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_PRODUCT_GROUP_ID = "productGroupID";
	
	private int jobRequestID;
	private int productGroupID;
	
	public JobRequestProductGroup() {
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
		strBld.append("insert into JobRequestProductGroup");
		strBld.append("(");
		strBld.append(COLUMN_JOB_REQUEST_ID+",");
		strBld.append(COLUMN_PRODUCT_GROUP_ID);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.jobRequestID+",");
		strBld.append(this.productGroupID);
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
		this.jobRequestID = JSONDataUtil.getInt(jsonObj, COLUMN_JOB_REQUEST_ID);
		this.productGroupID = JSONDataUtil.getInt(jsonObj, COLUMN_PRODUCT_GROUP_ID);
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getJobRequestID() {
		return jobRequestID;
	}

	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}

	public int getProductGroupID() {
		return productGroupID;
	}

	public void setProductGroupID(int productGroupID) {
		this.productGroupID = productGroupID;
	}

}

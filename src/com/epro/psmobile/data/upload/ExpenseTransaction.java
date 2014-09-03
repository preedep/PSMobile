package com.epro.psmobile.data.upload;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.epro.psmobile.R;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.Expense.ExpenseStatus;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class ExpenseTransaction implements UploadDataAdapter,JSONDataHolder {

	private Expense expense;
	private Team team;
	
	
	public final static String COLUMN_TEAM_ID = "teamId";
	public final static String COLUMN_EXPENSE_GROUP_TYPE_ID = "expenseGroupTypeID";
	public final static String COLUMN_EXPENSE_ID = "expenseId";
	public final static String COLUMN_EXPENSE_AMOUNT = "expenseAmount";
	public final static String COLUMN_LAST_EXPENSE_DATE_TIME = "lastExpenseDateTime";
	public final static String COLUMN_EXPENSE_STATUS = "expenseStatus";
	public final static String COLUMN_CANCEL_CAUSE = "cancelCause";
	public final static String COLUMN_ACTIVITY = "activity";
	public final static String COLUMN_MILE_NO = "mileNo";
	public final static String COLUMN_REMAKRS = "remarks";
	

	private int teamID;
	private int expenseGroupTypeID;
	private int expenseId;
	private double expenseAmount;
	private String lastExpenseDateTime;
	private String expenseStatus;
	private String cancelCause;
	private String activity;
	private String mileNo;
	private String remarks;
	
	
	private Context context;
	public ExpenseTransaction(
			Context context,
			Expense expense,
			Team team) throws Exception {
		// TODO Auto-generated constructor stub
		if (expense == null)throw new Exception("Expense is null");
		if (team == null)throw new Exception("Team is null");

		this.expense = expense;
		this.team = team;
		this.context = context;
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj,COLUMN_TEAM_ID,this.teamID);
		JSONDataUtil.put(jsonObj,COLUMN_EXPENSE_GROUP_TYPE_ID, this.expenseGroupTypeID);
		JSONDataUtil.put(jsonObj,COLUMN_EXPENSE_ID,this.expenseId);
		JSONDataUtil.put(jsonObj,COLUMN_EXPENSE_AMOUNT,this.expenseAmount);
		JSONDataUtil.put(jsonObj,COLUMN_LAST_EXPENSE_DATE_TIME,this.lastExpenseDateTime);
		JSONDataUtil.put(jsonObj,COLUMN_EXPENSE_STATUS,this.expenseStatus);
		JSONDataUtil.put(jsonObj,COLUMN_CANCEL_CAUSE,this.cancelCause);
		JSONDataUtil.put(jsonObj,COLUMN_ACTIVITY,this.activity);
		JSONDataUtil.put(jsonObj,COLUMN_MILE_NO,this.mileNo);
		JSONDataUtil.put(jsonObj,COLUMN_REMAKRS,this.remarks);
		return jsonObj;
	}

	@Override
	public void executeAdapter() {
		// TODO Auto-generated method stub
		this.teamID = this.team.getTeamID();
		this.expenseGroupTypeID = this.expense.getExpenseGroupID();
		this.expenseAmount = this.expense.getAmount();
		String strDate = this.expense.getExpenseDate();
		try{
			String[] strSplit = strDate.split("-");
			strDate = strSplit[2]+"-"+strSplit[1]+"-"+strSplit[0];
		}catch(Exception ex){}
		this.lastExpenseDateTime = strDate+" "+this.expense.getExpenseTime();
		this.expenseStatus = "";
		switch(this.expense.getStatus())
		{
			case STORED:{
				expenseStatus = 
						context.getString(R.string.expense_status_stored);
			}break;
			case APPROVED:{
				expenseStatus = 
						context.getString(R.string.expense_status_approved);
			}break;
			case EDITED:{
				expenseStatus = 
						context.getString(R.string.expense_status_edited);
			}break;
			case CANCEL:{
				expenseStatus = 
						context.getString(R.string.expense_status_cancel);
			}break;
		}
		//this.expense.getStatus().name()+"";
		this.cancelCause = this.expense.getExpenseCancelCause();
		this.activity = this.expense.getExpenseTypeObj().getExpenseTypeName();
		this.mileNo = this.expense.getMileNumber() + "";
		this.remarks = this.expense.getRemark();
		
		this.expenseId = this.expense.getSubExpenseTypeID();
		
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getExpenseGroupTypeID() {
		return expenseGroupTypeID;
	}

	public void setExpenseGroupTypeID(int expenseGroupTypeID) {
		this.expenseGroupTypeID = expenseGroupTypeID;
	}

	public int getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}

	public double getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(double expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getLastExpenseDateTime() {
		return lastExpenseDateTime;
	}

	public void setLastExpenseDateTime(String lastExpenseDateTime) {
		this.lastExpenseDateTime = lastExpenseDateTime;
	}

	public String getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(String expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public String getCancelCause() {
		return cancelCause;
	}

	public void setCancelCause(String cancelCause) {
		this.cancelCause = cancelCause;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getMileNo() {
		return mileNo;
	}

	public void setMileNo(String mileNo) {
		this.mileNo = mileNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}

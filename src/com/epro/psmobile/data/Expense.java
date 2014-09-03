package com.epro.psmobile.data;


import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;

public class Expense implements DbCursorHolder , 
TransactionStmtHolder , 
JSONDataHolder
{

	public enum ExpenseStatus
	{
		STORED(0),
		APPROVED(1),
		EDITED(2),
		CANCEL(3);
		
		private int statusCode;
		ExpenseStatus(int statusCode)
		{
			this.statusCode = statusCode;
		}
		public int getStatusCode()
		{
			return this.statusCode;
		}
		public static ExpenseStatus getExpenseStatus(int statusCode){
			for(int i = 0 ; i < values().length;i++){
				if (values()[i].getStatusCode() == statusCode){
					return values()[i];
				}
			}
			return ExpenseStatus.STORED;
		}
	}
	public final static int EXPENSE_GROUP_FUEL = 0;
	public final static int EXPENSE_GROUP_OTHER = 1;

	
	public final static String COLUMN_EXPENSE_ID = "expenseID";
	public final static String COLUMN_EXPENSE_GROUP_ID = "expenseGroupID";
	public final static String COLUMN_EXPENSE_TYPE_ID = "expenseTypeID";
	public final static String COLUMN_EXPENSE_DATE = "expenseDate";
	public final static String COLUMN_EXPENSE_TIME = "expenseTime";
	public final static String COLUMN_MILE_NUMBER = "mileNumber";
	public final static String COLUMN_TYPE_FUEL_AND_PAID_TYPE = "typeFuelAndPaidType";
	public final static String COLUMN_OTHER_EXPENSE_TYPE = "otherExpenseType";
	public final static String COLUMN_AMOUNT = "amount";
	public final static String COLUMN_LITER = "liter";
	public final static String COLUMN_REMARK = "remark";
	public final static String COLUMN_EXPENSE_STATUS = "expenseStatus";
	public final static String COLUMN_EXPENSE_CANCEL_CAUSE = "expenseCancelCause";
	public final static String COLUMN_EXPENSE_SUB_EXPENSE_TYPE_ID = "subExpenseTypeID";
	
	
	private int expenseID;
	private int expenseGroupID;
	private int expenseTypeID;
	private String expenseDate;
	private String expenseTime;
	private int mileNumber;
	private String typeFuelAndPaidType;
	private String otherExpenseType;
	private double amount;
	private double liter;
	private String remark;
	private ExpenseStatus status;
	private String expenseCancelCause;
	private ExpenseType expenseTypeObj;
	private int subExpenseTypeID;
	
	
	public Expense() {
		// TODO Auto-generated constructor stub
	}



	public int getExpenseID() {
		return expenseID;
	}



	public void setExpenseID(int expenseID) {
		this.expenseID = expenseID;
	}



	public int getExpenseGroupID() {
		return expenseGroupID;
	}



	public void setExpenseGroupID(int expenseGroupID) {
		this.expenseGroupID = expenseGroupID;
	}



	public int getExpenseTypeID() {
		return expenseTypeID;
	}



	public void setExpenseTypeID(int expenseTypeID) {
		this.expenseTypeID = expenseTypeID;
	}



	public String getExpenseDate() {
		if ((expenseDate == null)||(expenseDate.equalsIgnoreCase("null"))){
			return "";
		}
		return expenseDate;
	}



	public void setExpenseDate(String expenseDate) {
		this.expenseDate = expenseDate;
	}



	public String getExpenseTime() {
		if ((expenseTime == null)||(expenseTime.equalsIgnoreCase("null"))){
			return "";
		}
		return expenseTime;
	}



	public void setExpenseTime(String expenseTime) {
		this.expenseTime = expenseTime;
	}



	public int getMileNumber() {
		return mileNumber;
	}



	public void setMileNumber(int mileNumber) {
		this.mileNumber = mileNumber;
	}



	public String getTypeFuelAndPaidType() {
		return typeFuelAndPaidType;
	}



	public void setTypeFuelAndPaidType(String typeFuelAndPaidType) {
		this.typeFuelAndPaidType = typeFuelAndPaidType;
	}



	public String getOtherExpenseType() {
		return otherExpenseType;
	}



	public void setOtherExpenseType(String otherExpenseType) {
		this.otherExpenseType = otherExpenseType;
	}



	public double getAmount() {
		return amount;
	}



	public void setAmount(double amount) {
		this.amount = amount;
	}



	public String getRemark() {
		if ((remark == null)||
			(remark.equalsIgnoreCase("null")))
		{
			remark = "";
		}
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public ExpenseStatus getStatus() {
		return status;
	}



	public void setStatus(ExpenseStatus status) {
		this.status = status;
	}



	public String getExpenseCancelCause() {
		return expenseCancelCause;
	}



	public void setExpenseCancelCause(String expenseCancelCause) {
		this.expenseCancelCause = expenseCancelCause;
	}



	public ExpenseType getExpenseTypeObj() {
		return expenseTypeObj;
	}



	public void setExpenseTypeObj(ExpenseType expenseTypeObj) {
		this.expenseTypeObj = expenseTypeObj;
	}



	public int getSubExpenseTypeID() {
		return subExpenseTypeID;
	}



	public void setSubExpenseTypeID(int subExpenseTypeID) {
		this.subExpenseTypeID = subExpenseTypeID;
	}



	public double getLiter() {
		return liter;
	}



	public void setLiter(double liter) {
		this.liter = liter;
	}



	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.expenseID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID));
		this.expenseGroupID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_GROUP_ID));
		this.expenseTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE_ID));
		this.expenseDate = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE));
		this.expenseTime = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_TIME));
		this.mileNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_MILE_NUMBER));
		this.typeFuelAndPaidType = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_FUEL_AND_PAID_TYPE));
		this.otherExpenseType = cursor.getString(cursor.getColumnIndex(COLUMN_OTHER_EXPENSE_TYPE));
		this.amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
		this.liter = cursor.getDouble(cursor.getColumnIndex(COLUMN_LITER));
		this.remark = cursor.getString(cursor.getColumnIndex(COLUMN_REMARK));
		int iStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_STATUS));
		this.status = ExpenseStatus.getExpenseStatus(iStatus);
		this.expenseCancelCause = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CANCEL_CAUSE));
		this.subExpenseTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_SUB_EXPENSE_TYPE_ID));
		try{
			this.expenseTypeObj = new ExpenseType();
			this.expenseTypeObj.onBind(cursor);
		}catch(Exception ex){
			ex.printStackTrace();
		}
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
		strBld.append("insert into Expense");
		strBld.append("(");
		strBld.append("expenseGroupID,");
		strBld.append("expenseTypeID,");
		strBld.append("expenseDate,");
		strBld.append("expenseTime,");
		strBld.append("mileNumber,");
		strBld.append("typeFuelAndPaidType,");
		strBld.append("otherExpenseType,");
		strBld.append("amount,");
		strBld.append(COLUMN_LITER+",");
		strBld.append("remark,");
		strBld.append("expenseStatus,");
		strBld.append("expenseCancelCause,");
		strBld.append(COLUMN_EXPENSE_SUB_EXPENSE_TYPE_ID);
		strBld.append(")");
		strBld.append(" values(");
		strBld.append(this.getExpenseGroupID()+",");
		strBld.append(this.getExpenseTypeID()+",");
		strBld.append("'"+this.getExpenseDate()+"',");
		strBld.append("'"+this.getExpenseTime()+"',");
		strBld.append(this.getMileNumber()+",");
		strBld.append("'"+this.getTypeFuelAndPaidType()+"',");
		strBld.append("'"+this.getOtherExpenseType()+"',");
		strBld.append(this.getAmount()+",");
		strBld.append(this.getLiter()+",");
		strBld.append("'"+this.getRemark()+"',");
		strBld.append(this.getStatus().getStatusCode()+",");
		strBld.append("'"+this.getExpenseCancelCause()+"',");
		strBld.append(""+this.subExpenseTypeID+"");
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
		
	}



	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_ID , this.expenseID);
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_GROUP_ID , this.expenseGroupID);
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_TYPE_ID , this.expenseTypeID);
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_DATE ,this.expenseDate);
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_TIME ,this.expenseTime);
		JSONDataUtil.put(jsonObj, COLUMN_MILE_NUMBER ,this.mileNumber);
		JSONDataUtil.put(jsonObj, COLUMN_TYPE_FUEL_AND_PAID_TYPE , this.typeFuelAndPaidType);
		JSONDataUtil.put(jsonObj, COLUMN_OTHER_EXPENSE_TYPE , this.otherExpenseType);
		JSONDataUtil.put(jsonObj, COLUMN_AMOUNT , this.amount);
		JSONDataUtil.put(jsonObj, COLUMN_REMARK ,this.remark);
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_STATUS , this.status.getStatusCode());
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_CANCEL_CAUSE , this.expenseCancelCause);
		JSONDataUtil.put(jsonObj, COLUMN_EXPENSE_SUB_EXPENSE_TYPE_ID, this.subExpenseTypeID);
		return jsonObj;
	}

}

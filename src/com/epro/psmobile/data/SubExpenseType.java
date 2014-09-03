package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class SubExpenseType implements DbCursorHolder, JSONDataHolder,
		TransactionStmtHolder 
		{
	public final static String COLUMN_SUB_EXPENSE_TYPE_ID = "subExpenseTypeID";
	public final static String COLUMN_SUB_EXPENSE_TYPE_NAME = "subExpenseTypeName";
	public final static String COLUMN_EXPENSE_TYPE_ID = "expenseTypeID";
	public final static String COLUMN_EXPENSE_TYPE_NAME = "expenseTypeName";
	public final static String COLUMN_EXPENSE_MONEY_TYPE = "expenseMoneyType";
	public final static String COLUMN_EXPENSE_FUEL_NAME = "expenseFuelName";
	
	private int subExpenseTypeID;
	private String subExpenseTypeName;
	private int expenseTypeID;
	private String expenseType;
	private String expenseMoneyType;
	private String expenseFuelName;
	
	
	public SubExpenseType() {
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
		strBld.append("insert into SubExpenseType");
		strBld.append("(");
		strBld.append(COLUMN_SUB_EXPENSE_TYPE_ID+",");
		strBld.append(COLUMN_SUB_EXPENSE_TYPE_NAME+",");
		strBld.append(COLUMN_EXPENSE_TYPE_ID+",");
		strBld.append(COLUMN_EXPENSE_TYPE_NAME+",");
		strBld.append(COLUMN_EXPENSE_MONEY_TYPE+",");
		strBld.append(COLUMN_EXPENSE_FUEL_NAME);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(""+this.subExpenseTypeID+",");
		strBld.append("'"+this.subExpenseTypeName+"',");
		strBld.append(""+this.expenseTypeID+",");
		strBld.append("'"+this.expenseType+"',");
		strBld.append("'"+this.expenseMoneyType+"',");
		strBld.append("'"+this.expenseFuelName+"'");
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
		this.subExpenseTypeID = JSONDataUtil.getInt(jsonObj, COLUMN_SUB_EXPENSE_TYPE_ID);
		this.subExpenseTypeName = JSONDataUtil.getString(jsonObj, COLUMN_SUB_EXPENSE_TYPE_NAME);
		this.expenseTypeID = JSONDataUtil.getInt(jsonObj, COLUMN_EXPENSE_TYPE_ID);
		this.expenseType = JSONDataUtil.getString(jsonObj, COLUMN_EXPENSE_TYPE_NAME);
		this.expenseMoneyType = JSONDataUtil.getString(jsonObj, COLUMN_EXPENSE_MONEY_TYPE);
		this.expenseFuelName = JSONDataUtil.getString(jsonObj, COLUMN_EXPENSE_FUEL_NAME);
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.subExpenseTypeID =  cursor.getInt(cursor.getColumnIndex(COLUMN_SUB_EXPENSE_TYPE_ID));
		this.subExpenseTypeName = cursor.getString(cursor.getColumnIndex(COLUMN_SUB_EXPENSE_TYPE_NAME));
		this.expenseTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE_ID));
		this.expenseType = cursor.getString(cursor.getColumnIndex( COLUMN_EXPENSE_TYPE_NAME));
		this.expenseMoneyType = cursor.getString(cursor.getColumnIndex( COLUMN_EXPENSE_MONEY_TYPE));
		this.expenseFuelName = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_FUEL_NAME));
	}

	public int getSubExpenseTypeID() {
		return subExpenseTypeID;
	}

	public void setSubExpenseTypeID(int subExpenseTypeID) {
		this.subExpenseTypeID = subExpenseTypeID;
	}

	public String getSubExpenseTypeName() {
		return subExpenseTypeName;
	}

	public void setSubExpenseTypeName(String subExpenseTypeName) {
		this.subExpenseTypeName = subExpenseTypeName;
	}

	public int getExpenseTypeID() {
		return expenseTypeID;
	}

	public void setExpenseTypeID(int expenseTypeID) {
		this.expenseTypeID = expenseTypeID;
	}

	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}

	public String getExpenseMoneyType() {
		return expenseMoneyType;
	}

	public void setExpenseMoneyType(String expenseMoneyType) {
		this.expenseMoneyType = expenseMoneyType;
	}

	public String getExpenseFuelName() {
		return expenseFuelName;
	}

	public void setExpenseFuelName(String expenseFuelName) {
		this.expenseFuelName = expenseFuelName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getSubExpenseTypeName();
	}

}

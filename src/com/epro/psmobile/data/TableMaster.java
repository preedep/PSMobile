package com.epro.psmobile.data;

import android.database.Cursor;

public class TableMaster implements TransactionStmtHolder ,DbCursorHolder{
	
	private String tableName;
	
	public TableMaster() {
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
		return null;
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.tableName = cursor.getString(cursor.getColumnIndex("name"));
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}

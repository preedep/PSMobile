/**
 * 
 */
package com.epro.psmobile.data;


import java.util.Date;

import com.epro.psmobile.util.DataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author thrm0006
 *
 */
public class ExpenseSummary extends Expense implements DbCursorHolder ,Parcelable
{

	private final static String COLUM_SUM_AMOUNT = "sum_amount";
		
	private double totlaAmount;
	
	
	public static final Parcelable.Creator<ExpenseSummary> CREATOR = new Parcelable.Creator<ExpenseSummary>()
	{

		@Override
		public ExpenseSummary createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ExpenseSummary(source);
		}

		@Override
		public ExpenseSummary[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ExpenseSummary[size];
		}
		
	};
	
	public ExpenseSummary(Parcel source)
	{
		/*
		 dest.writeDouble(this.getAmount());
		dest.writeParcelable(getExpenseTypeObj(), flags);
		dest.writeString(this.getExpenseDate());
		dest.writeInt(this.getStatus().getStatusCode());
		 */
		this.setAmount(source.readDouble());
		this.setExpenseTypeID(source.readInt());
		this.setExpenseTypeObj((ExpenseType)source.readParcelable(this.getClass().getClassLoader()));
		
		this.setExpenseDate(source.readString());
		this.setStatus(ExpenseStatus.getExpenseStatus(source.readInt()));
	}
	/**
	 * 
	 */
	public ExpenseSummary() {
		// TODO Auto-generated constructor stub
	}

	public double getTotlaAmount() {
		return totlaAmount;
	}

	public void setTotlaAmount(double totlaAmount) {
		this.totlaAmount = totlaAmount;
	}


	/* (non-Javadoc)
	 * @see com.epro.psmobile.data.DbCursorHolder#onBind(android.database.Cursor)
	 */
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		
		this.totlaAmount = cursor.getDouble(cursor.getColumnIndex(COLUM_SUM_AMOUNT));
		
		ExpenseType expenseType = new ExpenseType();
		String expenseTypeName = cursor.getString(cursor.getColumnIndex(ExpenseType.COLUMN_EXPENSE_TYPE_NAME));
		int expenseTypeID = cursor.getInt(cursor.getColumnIndex(ExpenseType.COLUMN_EXPENSE_TYPE_ID));
		expenseType.setExpenseTypeID(expenseTypeID);
		expenseType.setExpenseTypeName(expenseTypeName);
		this.setExpenseTypeObj(expenseType);
		this.setExpenseTypeID(expenseTypeID);
		
		String expenseDate = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE));
//		Date d = DataUtil.convertToDateYYYYMMDD(expenseDate);
//		this.setExpenseDate(DataUtil.convertDateToStringDDMMYYYY(d));
		this.setExpenseDate(expenseDate);
		int iStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_STATUS));
		ExpenseStatus status =  ExpenseStatus.getExpenseStatus(iStatus);
		this.setStatus(status);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeDouble(this.getAmount());
		dest.writeInt(getExpenseTypeID());
		dest.writeParcelable(getExpenseTypeObj(), flags);
		dest.writeString(this.getExpenseDate());
		dest.writeInt(this.getStatus().getStatusCode());
		
	}


}

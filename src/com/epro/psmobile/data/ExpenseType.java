/**
 * 
 */
package com.epro.psmobile.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nickmsft
 *
 */
public class ExpenseType implements DbCursorHolder ,Parcelable
{

	public final static int EXPENSE_GROUP_FUEL = 0;
	public final static int EXPENSE_GROUP_OTHER = 1;
	
	public final static String COLUMN_EXPENSE_TYPE_ID = "expenseTypeID";
	public final static String COLUMN_EXPENSE_TYPE_NAME = "expenseTypeName";
	private int expenseTypeID;
	private String expenseTypeName;
	
	public static final Parcelable.Creator<ExpenseType> CREATOR = new Parcelable.Creator<ExpenseType>()
	{

		@Override
		public ExpenseType createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ExpenseType(source);
		}

		@Override
		public ExpenseType[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ExpenseType[size];
		}
		
	};
	public ExpenseType(Parcel source)
	{
		this.setExpenseTypeID(source.readInt());
		this.setExpenseTypeName(source.readString());
	}
	/**
	 * 
	 */
	public ExpenseType() {
		// TODO Auto-generated constructor stub
	}

	public int getExpenseTypeID() {
		return expenseTypeID;
	}

	public void setExpenseTypeID(int expenseTypeID) {
		this.expenseTypeID = expenseTypeID;
	}

	public String getExpenseTypeName() {
		return expenseTypeName;
	}

	public void setExpenseTypeName(String expenseTypeName) {
		this.expenseTypeName = expenseTypeName;
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.data.DbCursorHolder#onBind(android.database.Cursor)
	 */
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.expenseTypeID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE_ID));
		this.expenseTypeName = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE_NAME));
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(this.getExpenseTypeID());
		dest.writeString(this.getExpenseTypeName());
	}

}

package com.epro.psmobile.data;

import java.util.Date;

import com.epro.psmobile.data.Expense.ExpenseStatus;

public abstract class ExpenseEntryRow {

	
	protected Date expenseDate = new Date();
	protected int expenseTypeID;
	protected String expenseTime;
	protected double expenseAmount;
	protected double liter;
	



	protected String remark;
	private boolean isEditable = false;
	private boolean isRowHeader = false;
	private ExpenseStatus status = ExpenseStatus.STORED;
	protected int subExpenseTypeID;
	
	public ExpenseEntryRow() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return the expenseTypeID
	 */
	public int getExpenseTypeID() {
		return expenseTypeID;
	}



	/**
	 * @param expenseTypeID the expenseTypeID to set
	 */
	public void setExpenseTypeID(int expenseTypeID) {
		this.expenseTypeID = expenseTypeID;
	}



	public Date getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}

	public String getExpenseTime() {
		return expenseTime;
	}

	public void setExpenseTime(String expenseTime) {
		this.expenseTime = expenseTime;
	}

	public double getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(double expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getRemark() {
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

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isRowHeader() {
		return isRowHeader;
	}

	public void setRowHeader(boolean isRowHeader) {
		this.isRowHeader = isRowHeader;
	}
	
	public abstract String findTextAttributeIndxDisplay(int idx);
	public abstract Expense getExpense();
	public abstract void setExpenseEntryRow(Expense expense);


	public double getLiter() {
		return liter;
	}
	public void setLiter(double liter) {
		this.liter = liter;
	}
	public int getSubExpenseTypeID() {
		return subExpenseTypeID;
	}



	public void setSubExpenseTypeID(int subExpenseTypeID) {
		this.subExpenseTypeID = subExpenseTypeID;
	}
}

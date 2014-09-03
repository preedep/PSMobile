package com.epro.psmobile.data;

import com.epro.psmobile.util.DataUtil;

public class ExpenseEntryRowOther extends ExpenseEntryRow {

	private String expenseOtherType;
	
	public ExpenseEntryRowOther() {
		// TODO Auto-generated constructor stub
	}

	public String getExpenseOtherType() {
		return expenseOtherType;
	}

	public void setExpenseOtherType(String expenseOtherType) {
		this.expenseOtherType = expenseOtherType;
	}

	@Override
	public String findTextAttributeIndxDisplay(int idx) {
		// TODO Auto-generated method stub
		String[] strDataDisplay = new String[]{
				DataUtil.convertDateToStringDDMMYYYY(expenseDate),
				expenseTime,
				expenseOtherType,
				DataUtil.decimal2digiFormat(expenseAmount)+"",
				remark
			};
		return strDataDisplay[idx];
	}

	@Override
	public Expense getExpense() {
		// TODO Auto-generated method stub
		Expense expense = new Expense();
		expense.setExpenseGroupID(Expense.EXPENSE_GROUP_OTHER);
		expense.setExpenseTypeID(getExpenseTypeID());
//		expense.setExpenseDate(getExpenseDate().toString());
		expense.setExpenseDate(DataUtil.convertDateToStringDDMMYYYY(getExpenseDate()));
		
		expense.setExpenseTime(getExpenseTime());
		expense.setOtherExpenseType(getExpenseOtherType());
		expense.setAmount(getExpenseAmount());
		expense.setRemark(getRemark());
		expense.setStatus(getStatus());		
		expense.setSubExpenseTypeID(getSubExpenseTypeID());
		return expense;	}

	@Override
	public void setExpenseEntryRow(Expense expense) {
		// TODO Auto-generated method stub
		this.setExpenseTypeID(expense.getExpenseTypeID());
		this.setExpenseDate(DataUtil.convertToDateDDMMYYYY(expense.getExpenseDate()));
		this.setExpenseTime(expense.getExpenseTime());
		this.setExpenseOtherType(expense.getOtherExpenseType());
		this.setExpenseAmount(expense.getAmount());
		this.setRemark(expense.getRemark());
		this.setStatus(expense.getStatus());
		this.setSubExpenseTypeID(expense.getSubExpenseTypeID());
	}

}

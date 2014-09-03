package com.epro.psmobile.data;

import com.epro.psmobile.util.DataUtil;

public class ExpenseEntryRowFuel extends ExpenseEntryRow {

	protected String milesNumber;
	protected String fuelTypeAndPaidType;
	
	
	
	public ExpenseEntryRowFuel() {
		// TODO Auto-generated constructor stub
	}

	public String getMilesNumber() {
		return milesNumber;
	}

	public void setMilesNumber(String milesNumber) {
		this.milesNumber = milesNumber;
	}

	public String getFuelTypeAndPaidType() {
		return fuelTypeAndPaidType;
	}

	public void setFuelTypeAndPaidType(String fuelTypeAndPaidType) {
		this.fuelTypeAndPaidType = fuelTypeAndPaidType;
	}

	public String findTextAttributeIndxDisplay(int idx)
	{
		String[] strDataDisplay = new String[]{
				DataUtil.convertDateToStringDDMMYYYY(expenseDate),
				expenseTime,
				milesNumber,
				fuelTypeAndPaidType,
				DataUtil.decimal2digiFormat(expenseAmount)+"",
				DataUtil.decimal2digiFormat(liter)+"",
				remark
			};
		return strDataDisplay[idx];
	}

	@Override
	public Expense getExpense() {
		// TODO Auto-generated method stub
		Expense expense = new Expense();
		expense.setExpenseGroupID(Expense.EXPENSE_GROUP_FUEL);
		expense.setExpenseTypeID(getExpenseTypeID());
		expense.setExpenseDate(DataUtil.convertDateToStringDDMMYYYY(getExpenseDate()));
		expense.setExpenseTime(getExpenseTime());
		expense.setTypeFuelAndPaidType(getFuelTypeAndPaidType());
		
		try{
			expense.setMileNumber(Integer.parseInt(getMilesNumber()));
		}catch(Exception ex){}
		
		expense.setAmount(getExpenseAmount());
		expense.setLiter(getLiter());
		expense.setRemark(getRemark());
		expense.setStatus(getStatus());	
		expense.setSubExpenseTypeID(getSubExpenseTypeID());
		return expense;
	}

	@Override
	public void setExpenseEntryRow(Expense expense) {
		// TODO Auto-generated method stub
		this.setExpenseTypeID(expense.getExpenseTypeID());
		this.setExpenseDate(DataUtil.convertToDateDDMMYYYY(expense.getExpenseDate()));
		this.setExpenseTime(expense.getExpenseTime());
		this.setFuelTypeAndPaidType(expense.getTypeFuelAndPaidType());
		this.setExpenseAmount(expense.getAmount());
		this.setLiter(expense.getLiter());
		this.setMilesNumber(expense.getMileNumber()+"");
		this.setRemark(expense.getRemark());
		this.setStatus(expense.getStatus());
		this.setSubExpenseTypeID(expense.getSubExpenseTypeID());
	}
}

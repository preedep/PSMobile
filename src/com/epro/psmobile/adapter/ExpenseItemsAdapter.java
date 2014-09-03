package com.epro.psmobile.adapter;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.Expense.ExpenseStatus;
import com.epro.psmobile.data.ExpenseSummary;
import com.epro.psmobile.data.ExpenseType;
import com.epro.psmobile.util.DataUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExpenseItemsAdapter extends BaseAdapter {

	private ArrayList<ExpenseSummary> expenseList;
	private Context context;
	private LayoutInflater inflater;

	public ExpenseItemsAdapter(Context context,
							  ArrayList<ExpenseSummary> expenseList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.expenseList = expenseList;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.expenseList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.expenseList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.expense_lv_summary_item, null, false);
		
		ExpenseSummary expense = (ExpenseSummary)this.getItem(position);
		
		v.setTag(expense);
		
		TextView tvTypeName = (TextView)v.findViewById(R.id.tv_expense_type_name);
		TextView tvDate = (TextView)v.findViewById(R.id.tv_expense_date);
		TextView tvAmount = (TextView)v.findViewById(R.id.tv_expense_total);

		tvTypeName.setText(
				expense.getExpenseTypeObj().getExpenseTypeName());
		tvDate.setText(
				context.getString(R.string.text_expense_date,expense.getExpenseDate()));
		tvAmount.setText(
				context.getString(R.string.text_expense_total_amount,
						DataUtil.decimal2digiFormat(expense.getTotlaAmount())+""));
		
		
		TextView vIcon = (TextView)v.findViewById(R.id.icon_expense_status);
		if (expense.getStatus() == ExpenseStatus.STORED){
			vIcon.setBackgroundColor(context.getResources().getColor(R.color.expense_status_new));
			vIcon.setText(context.getString(R.string.label_expense_status_text_stored));
		}else if (expense.getStatus() == ExpenseStatus.APPROVED)
		{
			vIcon.setBackgroundColor(context.getResources().getColor(R.color.expense_status_approved));
			vIcon.setText(context.getString(R.string.label_expense_status_text_finish));
		}else if (expense.getStatus() == ExpenseStatus.CANCEL){
			vIcon.setBackgroundColor(context.getResources().getColor(R.color.expense_status_cancel));
			vIcon.setText(context.getString(R.string.label_expense_status_text_edited));
		}
		return v;
	}

	

}

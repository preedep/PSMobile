/**
 * 
 */
package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.ExpenseEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.ExpenseItemsAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.Expense.ExpenseStatus;
import com.epro.psmobile.data.ExpenseSummary;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

/**
 * @author nickmsft
 *
 */
public class ExpenseFragment extends ContentViewBaseFragment {

	private View currentView;
	private ListView lv;
	/**
	 * 
	 */
	public ExpenseFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//ActivityUtil.startNewActivity(this.getActivity(), ExpenseEntryActivity.class);
		this.setHasOptionsMenu(true);
		currentView = inflater.inflate(R.layout.expense_content_fragment, container, false);
		try{
			initialView(currentView);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return currentView;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
//		inflater.inflate(R.menu.main_menu, menu);
		menu.findItem(R.id.menu_add_new_content).setVisible(true);
		menu.findItem(R.id.menu_view_as_grid).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int menuId = item.getItemId();
		switch(menuId)
		{
			case R.id.menu_add_new_content:{
				ActivityUtil.startNewActivityWithResult(getActivity(), 
						ExpenseEntryActivity.class, 
						null, 
						InstanceStateKey.ADD_NEW_EXPENSE);
//				ActivityUtil.startNewActivity(this.getActivity(), ExpenseEntryActivity.class);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void initialView(View v) throws Exception
	{
		/*
		 * 
		 */
		
		lv = (ListView)v.findViewById(R.id.lv_expense_fragment_content);
//		this.registerForCustomContextMenu(targetView, title, items)
		this.registerForContextMenu(lv);
		bindListView();
	}
	public void bindListView() throws Exception
	{
		this.bindListViewByWithExpenseTypeId(-1);
	}
	public void bindListViewByWithExpenseTypeId(int expenseTypeId) throws Exception
	{
		ExpenseItemsAdapter adapter = null;
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		ArrayList<ExpenseSummary> expenseList = dataAdapter.getAllSummaryExpense();
		if (expenseList != null){
			if (expenseTypeId >= 0){
				ArrayList<ExpenseSummary> filterList = new ArrayList<ExpenseSummary>();
				for(ExpenseSummary summaryItem : expenseList){
					if (summaryItem.getExpenseTypeObj().getExpenseTypeID() == expenseTypeId)
					{
						filterList.add(summaryItem);
					}
					adapter = new ExpenseItemsAdapter(this.getActivity(),filterList);				
				}
			}else{
				adapter = new ExpenseItemsAdapter(this.getActivity(),expenseList);				
			}
			lv.setAdapter(adapter);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode)
		{
			case InstanceStateKey.ADD_NEW_EXPENSE:
			{
				if (resultCode == Activity.RESULT_OK)
				{
					if (lv != null)
					{
						try{
							PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
							ArrayList<ExpenseSummary> expenseList = dataAdapter.getAllSummaryExpense();
							ExpenseItemsAdapter adapter = new ExpenseItemsAdapter(this.getActivity(),expenseList);
							lv.setAdapter(adapter);
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			}
			break;
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.edit_expense_summary){
			//open expenseActivity in edit mode
			AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		    ExpenseSummary summaryRow = (ExpenseSummary)info.targetView.getTag();
			  
			Bundle bArgument = new Bundle();
			bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_SUMMARY_EXPENSE, summaryRow);
			ActivityUtil.startNewActivityWithResult(getActivity(), 
					ExpenseEntryActivity.class, 
					bArgument, 
					InstanceStateKey.ADD_NEW_EXPENSE);
		}else if (item.getItemId() == R.id.show_expense_summary_report){
			//show report
		}
		return super.onContextItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

	    android.view.MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.context_menu_expense_summary, menu);

	    ExpenseSummary summaryRow = (ExpenseSummary)info.targetView.getTag();
	    if (summaryRow.getStatus() == ExpenseStatus.APPROVED){
	    	menu.findItem(R.id.edit_expense_summary).setVisible(false);
	    }
		super.onCreateContextMenu(menu, v, menuInfo);
	}

}

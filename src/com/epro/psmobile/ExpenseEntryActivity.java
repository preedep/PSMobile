package com.epro.psmobile;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.adapter.ExpenseEntryRowAdapter;
import com.epro.psmobile.adapter.ExpenseEntryRowAdapter.ExpenseGroupType;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.ExpenseEntryRow;
import com.epro.psmobile.data.ExpenseEntryRowFuel;
import com.epro.psmobile.data.ExpenseEntryRowOther;
import com.epro.psmobile.data.ExpenseSummary;
import com.epro.psmobile.data.ExpenseType;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TransactionStmtHolder;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SysInfoGetter;
import com.epro.psmobile.util.ViewUtil;
import com.epro.psmobile.view.BasicSpinner;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ExpenseEntryActivity extends PsBaseActivity {

	private ListView lvFuel;
	private ListView lvOther;
	private BasicSpinner bsp_expense_type;
	private ExpenseSummary expenseSummary;
	public ExpenseEntryActivity() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.PsBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//this.setContentView(R.layout.expense_entry)
		this.setContentView(R.layout.expense_entry_form);
		initialTeamHeader();
		initialControls();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.option_menu_save_only, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_entry_save)
		{
			//save data
			try {
				if (expenseSummary == null)
					saveExpenseEntrys(false);
				else{
					//update
					/*
					PSBODataAdapter adapter = PSBODataAdapter.getDataAdapter(this);
					adapter.deleteExpenseByExpenseTypeIdAndStatus(
							expenseSummary.getExpenseTypeID(), 
							expenseSummary.getStatus().getStatusCode());*/
					saveExpenseEntrys(true);
				}
				MessageBox.showSaveCompleteMessage(this);
				this.setResult(RESULT_OK);
				this.finish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				MessageBox.showMessage(this, "Error", e.getMessage());
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		ExpenseEntryRow entryRow = null;
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		View vRow = info.targetView;
		if (vRow.getTag() instanceof ExpenseEntryRow)
		{
			entryRow = (ExpenseEntryRow)vRow.getTag();
			if (!entryRow.isRowHeader())
			{
				android.view.MenuInflater inflater = this.getMenuInflater();
				inflater.inflate(R.menu.context_menu_do_expense, menu);		  
				super.onCreateContextMenu(menu, v, menuInfo);				
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		View currentView = info.targetView;
		int position = info.position;
		boolean isExpenseFuel = true; // other is false
		isExpenseFuel = (currentView.getTag() instanceof ExpenseEntryRowFuel);
		
		ExpenseEntryRowAdapter adapter = null;
		if (isExpenseFuel){
			 adapter = (ExpenseEntryRowAdapter)lvFuel.getAdapter();
		}else
		{
			adapter = (ExpenseEntryRowAdapter)lvOther.getAdapter();	
		}
		
		switch(item.getItemId()){
			case R.id.do_expense_edit:{
				if (adapter != null)
				{
					adapter.setEditMode(position);
				}
			}
			break;
			case R.id.do_expense_del:{
				if (isExpenseFuel)
				{
					adapter.removeAt(position);
					ViewUtil.setListViewHeightBasedOnChildren(lvFuel);					
				}else{
					adapter.removeAt(position);				
					ViewUtil.setListViewHeightBasedOnChildren(lvOther);
				}								
			}break;
		}
		return super.onContextItemSelected(item);
	}

	private void initialControls(){
		
		 expenseSummary = (ExpenseSummary)getIntent().getParcelableExtra(InstanceStateKey.KEY_ARGUMENT_SUMMARY_EXPENSE);
		
		View vHeaderFuel = this.findViewById(R.id.expense_group_type_header_fuel);
		View vOther = this.findViewById(R.id.expense_group_type_header_other);
		bsp_expense_type = (BasicSpinner)this.findViewById(R.id.sp_expense_type);

		try {
			bsp_expense_type.initial();
			
			if (expenseSummary != null)//edit mode
			{
				for(int i = 0; i < bsp_expense_type.getCount();i++)
				{
					Object item = bsp_expense_type.getItemAtPosition(i);
					if (item instanceof String)
					{
						String str_expense_type = (String)item;
						if (str_expense_type.equalsIgnoreCase(expenseSummary.getExpenseTypeObj().getExpenseTypeName())){
							bsp_expense_type.setSelection(i);
							break;
						}
					}
				}
				bsp_expense_type.setEnabled(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView tvFuelGroupTypeName  = (TextView)vHeaderFuel.findViewById(R.id.tv_expense_group_type_name);
		TextView tvOtherGroupTypeName = (TextView)vOther.findViewById(R.id.tv_expense_group_type_name);
		
		tvFuelGroupTypeName.setText(R.string.label_expense_group_fuel_name);
		tvOtherGroupTypeName.setText(R.string.label_expense_group_other_name);
		
	    lvFuel = (ListView)this.findViewById(R.id.lv_expense_fuel);
		lvOther = (ListView)this.findViewById(R.id.lv_expense_other);
		
		
		final ExpenseEntryRowAdapter fuelAdapter = new ExpenseEntryRowAdapter(this,ExpenseGroupType.FUEL);		
		final ExpenseEntryRowAdapter otherAdapter = new ExpenseEntryRowAdapter(this,ExpenseGroupType.OTHER);
		
		if (expenseSummary != null)/*edit mode*/
		{
			/*
			 * query data here
			 */
			PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);
			ArrayList<ExpenseEntryRowFuel> entryRowFuelList = new ArrayList<ExpenseEntryRowFuel>();
			ArrayList<ExpenseEntryRowOther> entryRowOtherList = new ArrayList<ExpenseEntryRowOther>();
			try {
				ArrayList<Expense> expenseList =  dataAdapter.getExpenseByTypeID(expenseSummary.getExpenseTypeID());
				for(Expense e : expenseList)
				{
					if (e.getExpenseGroupID() == Expense.EXPENSE_GROUP_FUEL){
						ExpenseEntryRowFuel e_fuel = new ExpenseEntryRowFuel();				
						e_fuel.setRowHeader(false);
						e_fuel.setEditable(false);
						e_fuel.setExpenseEntryRow(e);
						entryRowFuelList.add(e_fuel);
					}else if (e.getExpenseGroupID() == Expense.EXPENSE_GROUP_OTHER){
						ExpenseEntryRowOther e_other = new ExpenseEntryRowOther();
						e_other.setExpenseEntryRow(e);
						e_other.setRowHeader(false);
						e_other.setEditable(false);
						entryRowOtherList.add(e_other);
					}
				}
				
				fuelAdapter.addExpenseFuel(entryRowFuelList);
				otherAdapter.addExpenseOther(entryRowOtherList);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		lvFuel.setAdapter(fuelAdapter);		
		lvOther.setAdapter(otherAdapter);

		if (expenseSummary != null)
		{
			//edit mode
			ViewUtil.setListViewHeightBasedOnChildren(lvFuel);
			ViewUtil.setListViewHeightBasedOnChildren(lvOther);
		}
		
		this.registerForContextMenu(lvFuel);
		this.registerForContextMenu(lvOther);
		
		ImageButton imgBtnAddNewFuel = (ImageButton)vHeaderFuel.findViewById(R.id.imgBtn_add_new_row);
		ImageButton imgBtnAddNewOther = (ImageButton)vOther.findViewById(R.id.imgBtn_add_new_row);
		
		imgBtnAddNewFuel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fuelAdapter.addNewExpenseRow();
				ViewUtil.setListViewHeightBasedOnChildren(lvFuel);
				//fuelAdapter.update();
			}
			
		});
		
		imgBtnAddNewOther.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				otherAdapter.addNewExpenseRow();
				ViewUtil.setListViewHeightBasedOnChildren(lvOther);
				//otherAdapter.update();				
			}			
		});
		
	}
	private void initialTeamHeader(){
		//83cc7d16-d60e-31cc-a9ef-094770ebebc0
		String deviceId = SysInfoGetter.getDeviceID(this);
		Log.d("DEBUG_D", "DeviceID = "+deviceId);
		//83cc7d16-d60e-31cc-a9ef-094770ebebc0
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this);
		try {
			Team team = dataAdapter.getTeamByDeviceId(deviceId);
			if (team != null)
			{
				TextView tvTeamName = (TextView)this.findViewById(R.id.tv_team_zone);
				TextView tvTeamAddress = (TextView)this.findViewById(R.id.tv_team_address);
				
				
				tvTeamName.setText(team.getTeamName());
				

				String teamAddress = "";
				String teamMobileNo = "";
				
				if ((team.getTeamAddress() != null)&&(!team.getTeamAddress().equalsIgnoreCase("null"))){
					teamAddress = team.getTeamAddress();
				}

				if ((team.getTeamMobileNo() != null)&&(!team.getTeamMobileNo().equalsIgnoreCase("null"))){
					teamMobileNo = team.getTeamMobileNo();
				}

				if (teamAddress.isEmpty() && teamMobileNo.isEmpty()){
					tvTeamAddress.setVisibility(View.GONE);
				}
				tvTeamAddress.setText(
							teamAddress+", "+teamMobileNo
						);
				
//				tvTeamAddress.setText(team.getTeamAddress()+", "+team.getTeamMobileNo());
			}				
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void saveExpenseEntrys(boolean isUpdated)
	throws Exception
	{
		Object selectedItem = bsp_expense_type.getSelectedItem();
		ExpenseType expenseType = null;
		if (selectedItem instanceof String)
		{
			String strExpenseType = (String)selectedItem;
			@SuppressWarnings("unused")
			ArrayList<ExpenseType> expenseTypeList =  bsp_expense_type.getData(ExpenseType.class);
			for(ExpenseType item : expenseTypeList)
			{
				if (item.getExpenseTypeName().equalsIgnoreCase(strExpenseType)){
					expenseType = item;
					break;
				}
			}
			if (expenseType != null)
			{
				ArrayList<TransactionStmtHolder> stmtInsertList = new ArrayList<TransactionStmtHolder>();
				
				ListAdapter fuelAdapter = lvFuel.getAdapter();
				for(int i = 0; i < fuelAdapter.getCount();i++)
				{
					ExpenseEntryRowFuel rowFuel = (ExpenseEntryRowFuel)fuelAdapter.getItem(i);
					if (rowFuel.isRowHeader())continue;
					
					Expense expenseObj = rowFuel.getExpense();
					expenseObj.setExpenseTypeID(expenseType.getExpenseTypeID());
					
					String expenseDate = expenseObj.getExpenseDate();
					String expenseTime = expenseObj.getExpenseTime();
					int mileNumber = expenseObj.getMileNumber();
					double amount = expenseObj.getAmount();
					double liter = expenseObj.getLiter();
					if (expenseDate.trim().isEmpty() || expenseTime.trim().isEmpty())
					{
						throw new Exception(this.getString(R.string.text_error_expense_required_field));
					}else if (mileNumber <= 0){
						throw new Exception(this.getString(R.string.text_error_expense_required_field));						
					}else if (amount <= 0){
						throw new Exception(this.getString(R.string.text_error_expense_required_field));						
					}else if (liter <= 0)
					{
						throw new Exception(this.getString(R.string.text_error_expense_required_field));						
					}
					
					stmtInsertList.add(expenseObj);
				}				
				
				ListAdapter otherAdapter = lvOther.getAdapter();
				for(int i = 0; i < otherAdapter.getCount();i++)
				{
					ExpenseEntryRowOther rowOther = (ExpenseEntryRowOther)otherAdapter.getItem(i);
					if (rowOther.isRowHeader())continue;
					
					Expense expenseObj = rowOther.getExpense();
					expenseObj.setExpenseTypeID(expenseType.getExpenseTypeID());
					
					
					
					String expenseDate = expenseObj.getExpenseDate();
					String expenseTime = expenseObj.getExpenseTime();
					double amount = expenseObj.getAmount();
					if (expenseDate.trim().isEmpty() || expenseTime.trim().isEmpty())
					{
						throw new Exception(this.getString(R.string.text_error_expense_required_field));
					}else if (amount <= 0){
						throw new Exception(this.getString(R.string.text_error_expense_required_field));						
					}
					stmtInsertList.add(expenseObj);
				}
				
				PSBODataAdapter adapter = PSBODataAdapter.getDataAdapter(this);
				if (isUpdated){
					adapter.updateExpense(expenseSummary.getExpenseTypeID(), 
							expenseSummary.getStatus().getStatusCode(), stmtInsertList);
				}else{
					adapter.insertStatementHolderList(stmtInsertList);
				}
				
			}else{
				Log.d("DEBUG_D", "Can't find expense type match with "+strExpenseType);
			}
		}
	}

}

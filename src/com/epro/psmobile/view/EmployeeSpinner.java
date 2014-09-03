/**
 * 
 */
package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Employee;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class EmployeeSpinner extends Spinner {

	class EmployeeSpinnerAdapter extends BaseAdapter
	{

		private Context ctxt;
		private ArrayList<Employee> empList;
		private LayoutInflater inflater;

		public EmployeeSpinnerAdapter(Context ctxt,
				ArrayList<Employee> empList
				)
		{
			this.ctxt = ctxt;
			this.empList = empList;
			inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.empList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.empList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			View v = null;
			v = inflater.inflate(R.layout.team_member_item, null, false);
			
			TextView tvName = (TextView)v.findViewById(R.id.tv_team_member_name);
			
			Employee emp = (Employee)this.getItem(position);
			
			tvName.setText(emp.getPreFix()+" "+emp.getFirstName()+" "+emp.getLastName());
			v.setTag(emp);
			return v;
		}
		
	}
	/**
	 * @param context
	 */
	public EmployeeSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param mode
	 */
	public EmployeeSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public EmployeeSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public EmployeeSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param mode
	 */
	public EmployeeSpinner(Context context, AttributeSet attrs, int defStyle,
			int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	public void initial(String[] employeeCodeFilters){

		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getContext());
		 try {
			ArrayList<Employee> employees = null;
			
			if (employeeCodeFilters == null)
				employees = dataAdapter.getAllEmployee();
			else{
				employees = dataAdapter.getAllEmployee(employeeCodeFilters);
			}
			EmployeeSpinnerAdapter adapter = new EmployeeSpinnerAdapter(this.getContext(),employees);
			
			this.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

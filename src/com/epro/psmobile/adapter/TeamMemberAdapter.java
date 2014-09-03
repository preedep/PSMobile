/**
 * 
 */
package com.epro.psmobile.adapter;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.EmpAssignedInTeam;
import com.epro.psmobile.data.Team;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author thrm0006
 *
 */
public class TeamMemberAdapter extends BaseAdapter {

	private Context ctxt;
	private ArrayList<EmpAssignedInTeam> empAssignedInTeams;
	private LayoutInflater inflater;
	private Team mTeam;
	
	/**
	 * 
	 */
	public TeamMemberAdapter(Context ctxt,
			Team team,
			ArrayList<EmpAssignedInTeam> empAssignedInTeams)
	{
		// TODO Auto-generated constructor stub
		this.ctxt = ctxt;
		this.empAssignedInTeams = empAssignedInTeams;
		this.mTeam = team;
		inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return empAssignedInTeams.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return empAssignedInTeams.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		v = inflater.inflate(R.layout.team_member_item, parent, false);
	
		EmpAssignedInTeam empAssignedInTeam = (EmpAssignedInTeam)this.getItem(position);
		
		v.setTag(empAssignedInTeam);
		
		String textRole = ctxt.getResources().getString(R.string.label_inspect_emp_role);
		if (empAssignedInTeam.isTeamLeader()){
			textRole = ctxt.getResources().getString(R.string.label_inspect_emp_role_leader);
		}
		
		TextView tvName = (TextView)v.findViewById(R.id.tv_team_member_name);
		
		String fullName = "";
		fullName += empAssignedInTeam.getEmployee().getPreFix();
		fullName += " ";
		fullName += empAssignedInTeam.getEmployee().getFirstName();
		fullName += " ";
		fullName += empAssignedInTeam.getEmployee().getLastName();
		fullName += "   ";
		fullName += textRole;
		
		tvName.setText(fullName);
		
		//int height = tvName.getLayoutParams().height;
		//Log.d("DEBUG_D", "Item height = "+height);
		
		return v;
	}
	
	public void addNewEmpAssignedInTeam(EmpAssignedInTeam empAssignedInTeam)
	{
		if (this.mTeam != null)
		{
			empAssignedInTeam.setTeamID(this.mTeam.getTeamID());
			empAssignedInTeam.setTeamLeader(false);
		}
		this.empAssignedInTeams.add(empAssignedInTeam);
		this.notifyDataSetChanged();
	}
	public void removeEmpAssignedInTeam(EmpAssignedInTeam empAssignedInTeam)
	{
		for(EmpAssignedInTeam emp : empAssignedInTeams)
		{
			if (emp.getEmployee().getEmployeeCode().equalsIgnoreCase(empAssignedInTeam.getEmployee().getEmployeeCode()))
			{
				empAssignedInTeams.remove(emp);
				break;
			}
		}
		this.notifyDataSetChanged();
	}
}

package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class EmpAssignedInTeam implements DbCursorHolder , 
			 TransactionStmtHolder , 
			 Parcelable ,
			 JSONDataHolder
{
	public final static String TEAM_ID = "teamID";
	// final static String EMPLOYEE_ID = "employeeID";
	public final static String EMPLOYEE_CODE = "employeeCode";
	public final static String IS_TEAM_LEADER = "isTeamLeader";
	
	public final static String SIMPLE_QUERY_EMP_ASSIGNED_IN_TEAM = "select * from EmpAssignedInTeam";
	
	private int teamID;
	//private int employeeID;
	private String employeeCode;
	private boolean isTeamLeader = false;
	
	private Team team;
	private Employee employee;
	
	
	public static final Parcelable.Creator<EmpAssignedInTeam> CREATOR = new Parcelable.Creator<EmpAssignedInTeam>()
	{

		@Override
		public EmpAssignedInTeam createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new EmpAssignedInTeam(source);
		}

		@Override
		public EmpAssignedInTeam[] newArray(int size) {
			// TODO Auto-generated method stub
			return new EmpAssignedInTeam[size];
		}
		
	};
	
	public EmpAssignedInTeam(Parcel source)
	{
		this.teamID = source.readInt();
		//this.employeeID = source.readInt();
		this.employeeCode = source.readString();
		boolean b[] = new boolean[1];
		source.readBooleanArray(b);
		this.isTeamLeader = b[0];
	}
	public EmpAssignedInTeam() {
		// TODO Auto-generated constructor stub
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	/*
	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
*/
	public boolean isTeamLeader() {
		return isTeamLeader;
	}

	public void setTeamLeader(boolean isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
		if (this.team != null)
		{
			this.setTeamID(this.team.getTeamID());
		}
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
		if (this.employee != null)
		{
			this.setEmployeeCode(this.employee.getEmployeeCode());
		}
	}

	@Override
	public void onBind(Cursor cur) {
		// TODO Auto-generated method stub
		/*
		this.setTeamID(cur.getInt(cur.getColumnIndex(TEAM_ID)));
		this.setEmployeeID(cur.getInt(cur.getColumnIndex(EMPLOYEE_ID)));
		int iBool = cur.getInt(cur.getColumnIndex(IS_TEAM_LEADER));
		this.setTeamLeader((iBool == 0)?false:true);
*/		
		try{
			
			
			this.team = new Team();
			this.team.onBind(cur);
	
			this.setTeamID(this.team.getTeamID());
			
			this.employee = new Employee();
			this.employee.onBind(cur);
			
			//this.setEmployeeID(this.employee.getEmployeeID());
			
//			this.setEmployeeCode(this.employee.getEmployeeCode());
	
			this.employeeCode = cur.getString(cur.getColumnIndex(EMPLOYEE_CODE));
			int iBool = cur.getInt(cur.getColumnIndex(IS_TEAM_LEADER));
			this.setTeamLeader((iBool == 0)?false:true);

			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*
 	public final static String TEAM_ID = "teamID";
	public final static String EMPLOYEE_ID = "employeeID";
	public final static String IS_TEAM_LEADER = "isTeamLeader";
 */
	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub		
		return "delete from EmpAssignedInTeam where "+TEAM_ID+"="+this.getTeamID()+" and "+IS_TEAM_LEADER+" < 1";
//		return "delete from EmpAssignedInTeam where "+TEAM_ID+"="+this.getTeamID();

	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into EmpAssignedInTeam");
		strBld.append("(");
		strBld.append(TEAM_ID);
		strBld.append(",");
		strBld.append(EMPLOYEE_CODE);
		strBld.append(",");
		strBld.append(IS_TEAM_LEADER);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.getTeamID());
		strBld.append(",");
		strBld.append("'"+this.getEmployeeCode()+"'");
		strBld.append(",");
		strBld.append(this.isTeamLeader()?1:0);
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		/*
		this.teamID = source.readInt();
		this.employeeID = source.readInt();
		boolean b[] = new boolean[1];
		source.readBooleanArray(b);
		this.isTeamLeader = b[0];
		 */
		dest.writeInt(this.getTeamID());
		//dest.writeInt(this.getEmployeeID());
		dest.writeString(this.getEmployeeCode());
		dest.writeBooleanArray(new boolean[]{this.isTeamLeader()});
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.teamID = JSONDataUtil.getInt(jsonObj, TEAM_ID);
		this.employeeCode = JSONDataUtil.getString(jsonObj, EMPLOYEE_CODE);		
		this.isTeamLeader = JSONDataUtil.getBoolean(jsonObj, IS_TEAM_LEADER);
	}
	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

}

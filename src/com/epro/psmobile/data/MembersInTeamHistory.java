package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class MembersInTeamHistory implements DbCursorHolder,
		TransactionStmtHolder,
		Parcelable,
		JSONDataHolder
		{

	public final static String COLUMN_TEAM_CHECK_IN_HISTORY_ID = "teamCheckInHistoryID";
	public final static String COLUMN_EMPLOYEE_CODE = "employeeCode";
	
	private int teamCheckInHistoryID;
	private String employeeCode;
	
	public static final Parcelable.Creator<MembersInTeamHistory> CREATOR = new Parcelable.Creator<MembersInTeamHistory>()
	{

		@Override
		public MembersInTeamHistory createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MembersInTeamHistory(source);
		}

		@Override
		public MembersInTeamHistory[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MembersInTeamHistory[size];
		}
		
	};
	public MembersInTeamHistory(Parcel source)
	{
		this.teamCheckInHistoryID = source.readInt();
		this.employeeCode = source.readString();
	}
	public MembersInTeamHistory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("delete from MemberInTeamHistory");
		strBld.append(" where "+COLUMN_TEAM_CHECK_IN_HISTORY_ID+"="+this.getTeamCheckInHistoryID());
		strBld.append(" and "+COLUMN_EMPLOYEE_CODE+"='"+this.getEmployeeCode()+"'");
		return strBld.toString();
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into MemberInTeamHistory");
		strBld.append("(");
		strBld.append(COLUMN_TEAM_CHECK_IN_HISTORY_ID+",");
		strBld.append(COLUMN_EMPLOYEE_CODE);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.getTeamCheckInHistoryID()+",");
		strBld.append("'"+this.getEmployeeCode()+"'");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.teamCheckInHistoryID = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_CHECK_IN_HISTORY_ID));
		this.employeeCode = cursor.getString(cursor.getColumnIndex(COLUMN_EMPLOYEE_CODE));
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(this.teamCheckInHistoryID);
		dest.writeString(this.employeeCode);
	}
	public int getTeamCheckInHistoryID() {
		return teamCheckInHistoryID;
	}
	public void setTeamCheckInHistoryID(int teamCheckInHistoryID) {
		this.teamCheckInHistoryID = teamCheckInHistoryID;
	}
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj, COLUMN_TEAM_CHECK_IN_HISTORY_ID, this.teamCheckInHistoryID);
		JSONDataUtil.put(jsonObj, COLUMN_EMPLOYEE_CODE, this.employeeCode);
		return jsonObj;
	}

}

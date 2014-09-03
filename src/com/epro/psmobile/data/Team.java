package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Team implements DbCursorHolder, JSONDataHolder , TransactionStmtHolder,Parcelable

{
	public final static String TEAM_ID = "teamID";
	public final static String TEAM_CODE = "teamCode";
	public final static String TEAM_NAME = "teamName";
	public final static String DEVICE_ID = "deviceID";
	public final static String TEAM_ADDRESS = "teamAddress";
	public final static String TEAM_MOBILE_NO = "teamMobileNo";
	
	public final static String IS_QC_TEAM = "isQCTeam";
	public final static String DEFAULT_LICENSE_PLATE_ID = "licensePlateId";
	
	public final static String SIMPLE_QUERY_TEAM = "select * from team";
	
	private int teamID;
	private String teamCode;
	private String teamName;
	private String teamAddress;
	private String teamMobileNo;
	private String deviceID;
	private boolean isQCTeam;
	private int licensePlateId;
	
	public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>()
	{

		@Override
		public Team createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Team(source);
		}

		@Override
		public Team[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Team[size];
		}
		
	};
	public Team(Parcel source)
	{
		/*
		 dest.writeInt(this.teamID);
		dest.writeString(this.teamCode);
		dest.writeString(this.teamName);
		dest.writeString(this.teamAddress);
		dest.writeString(this.teamMobileNo);
		dest.writeString(this.deviceID);
		dest.writeBooleanArray(new boolean[]{this.isQCTeam});
		dest.writeInt(this.licensePlateId);
		 */
		this.teamID = source.readInt();
		this.teamCode = source.readString();
		this.teamName = source.readString();
		this.teamAddress = source.readString();
		this.teamMobileNo = source.readString();
		this.deviceID = source.readString();
		boolean bArray[] = new boolean[1];
		source.readBooleanArray(bArray);
		this.isQCTeam = bArray[0];
		this.licensePlateId = source.readInt();
	}
	public Team() {
		// TODO Auto-generated constructor stub
	}
	public int getTeamID() {
		return teamID;
	}
	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamAddress() {
		return teamAddress;
	}
	public void setTeamAddress(String teamAddress) {
		this.teamAddress = teamAddress;
	}
	public String getTeamMobileNo() {
		return teamMobileNo;
	}
	public void setTeamMobileNo(String teamMobileNo) {
		this.teamMobileNo = teamMobileNo;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public boolean isQCTeam() {
		return isQCTeam;
	}
	public void setQCTeam(boolean isQCTeam) {
		this.isQCTeam = isQCTeam;
	}
	@Override
	public void onBind(Cursor cur) {
		// TODO Auto-generated method stub
		this.setTeamID(cur.getInt(cur.getColumnIndex(TEAM_ID)));
		this.setTeamCode(cur.getString(cur.getColumnIndex(TEAM_CODE)));
		this.setTeamName(cur.getString(cur.getColumnIndex(TEAM_NAME)));
		this.setDeviceID(cur.getString(cur.getColumnIndex(DEVICE_ID)));
		this.setTeamAddress(cur.getString(cur.getColumnIndex(TEAM_ADDRESS)));
		this.setTeamMobileNo(cur.getString(cur.getColumnIndex(TEAM_MOBILE_NO)));
		try{
			this.setQCTeam(Boolean.parseBoolean(cur.getString(cur.getColumnIndex(IS_QC_TEAM))));
		}catch(Exception ex){}
		
		this.setLicensePlateId(cur.getInt(cur.getColumnIndex(DEFAULT_LICENSE_PLATE_ID)));
		
	}
	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into Team");
		strBld.append("(");
		strBld.append(TEAM_ID+",");
		strBld.append(TEAM_CODE+",");
		strBld.append(TEAM_NAME+",");
		strBld.append(DEVICE_ID+",");
		strBld.append(TEAM_ADDRESS+",");
		strBld.append(TEAM_MOBILE_NO+",");
		strBld.append(IS_QC_TEAM+",");
		strBld.append(DEFAULT_LICENSE_PLATE_ID);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.teamID+",");
		strBld.append("'"+this.teamCode+"',");
		strBld.append("'"+this.teamName+"',");
		strBld.append("'"+this.deviceID+"',");
		strBld.append("'"+this.teamAddress+"',");
		strBld.append("'"+this.teamMobileNo+"',");
		strBld.append("'"+this.isQCTeam+"',");
		strBld.append(this.licensePlateId);
		strBld.append(")");
		
		return strBld.toString();
	}
	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.teamID = JSONDataUtil.getInt(jsonObj, TEAM_ID);
		this.teamCode = JSONDataUtil.getString(jsonObj, TEAM_CODE);
		this.teamName = JSONDataUtil.getString(jsonObj,TEAM_NAME);
		this.deviceID = JSONDataUtil.getString(jsonObj,DEVICE_ID);
		this.teamAddress = JSONDataUtil.getString(jsonObj,TEAM_ADDRESS);
		this.teamMobileNo = JSONDataUtil.getString(jsonObj,TEAM_MOBILE_NO);
		
		int iIsQcTeam = JSONDataUtil.getInt(jsonObj,IS_QC_TEAM);
		this.isQCTeam = (iIsQcTeam > 0);
		this.licensePlateId = JSONDataUtil.getInt(jsonObj,DEFAULT_LICENSE_PLATE_ID);
		
	}
	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getLicensePlateId() {
		return licensePlateId;
	}
	public void setLicensePlateId(int licensePlateId) {
		this.licensePlateId = licensePlateId;
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
		 * private int teamID;
		   private String teamCode;
		   private String teamName;
		   private String teamAddress;
		   private String teamMobileNo;
		   private String deviceID;
		   private boolean isQCTeam;
		   private int licensePlateId;

		 */
		dest.writeInt(this.teamID);
		dest.writeString(this.teamCode);
		dest.writeString(this.teamName);
		dest.writeString(this.teamAddress);
		dest.writeString(this.teamMobileNo);
		dest.writeString(this.deviceID);
		dest.writeBooleanArray(new boolean[]{this.isQCTeam});
		dest.writeInt(this.licensePlateId);

	}

}

package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class TaskResponse implements DbCursorHolder, 
								   JSONDataHolder{

	public enum TaskRespnseType
	{
		TaskResend,
		TaskComplete
	};
	public final static String TASK_CODE = "taskCode";
	public final static String TEAM_ID = "teamID";
	
	private String taskCode;
	private int teamID;
	protected TaskRespnseType taskRespnseType;
	
	public TaskResponse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		this.taskCode = JSONDataUtil.getString(jsonObj, TASK_CODE);
		this.teamID = JSONDataUtil.getInt(jsonObj, TEAM_ID);
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.taskCode = cursor.getString(cursor.getColumnIndex(TASK_CODE));
		this.teamID = cursor.getInt(cursor.getColumnIndex(TEAM_ID));
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}



}

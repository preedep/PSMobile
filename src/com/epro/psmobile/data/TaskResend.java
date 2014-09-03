package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.data.TaskResponse.TaskRespnseType;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class TaskResend extends TaskResponse implements TransactionStmtHolder
{
	public final static String TASK_CODE_OLD = "taskCodeOld";
	private String taskCodeOld;
	
	public TaskResend(){
		taskRespnseType = TaskRespnseType.TaskResend;
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
		strBld.append("insert into TaskResend");
		strBld.append("(");
		strBld.append(TASK_CODE+",");
		strBld.append(TEAM_ID+",");
		strBld.append(TASK_CODE_OLD);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append("'"+this.getTaskCode()+"',");
		strBld.append(""+this.getTeamID()+",");
		strBld.append("'"+this.getTaskCodeOld()+"'");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public String getTaskCodeOld() {
		return taskCodeOld;
	}
	public void setTaskCodeOld(String taskCodeOld) {
		this.taskCodeOld = taskCodeOld;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		super.onBind(cursor);
		
		this.taskCodeOld = 
				cursor.getString(cursor.getColumnIndex(TASK_CODE_OLD));
	}
}

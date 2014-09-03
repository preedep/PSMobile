package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.epro.psmobile.remote.api.JSONDataHolder;

public class TaskComplete extends TaskResponse implements TransactionStmtHolder
{
	public TaskComplete(){
		taskRespnseType = TaskRespnseType.TaskComplete;
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
		strBld.append("insert into TaskComplete");
		strBld.append("(");
		strBld.append(TASK_CODE+",");
		strBld.append(TEAM_ID);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append("'"+this.getTaskCode()+"',");
		strBld.append(""+this.getTeamID()+"");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

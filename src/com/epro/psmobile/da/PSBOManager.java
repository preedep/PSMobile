package com.epro.psmobile.da;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.epro.psmobile.R;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.EmpAssignedInTeam;
import com.epro.psmobile.data.Employee;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.remote.api.Result;

public class PSBOManager {

	private DataMasterBuilder databaseBuilder;
	private Context context;
	public PSBOManager(Context context) {
		// TODO Auto-generated constructor stub
		databaseBuilder = new DataMasterBuilder(context);
		this.context = context;
	}
	public void initialMastertableIfNotExist()
	{
		if (databaseBuilder != null)
		{
			try{
				databaseBuilder.open();
			}finally{
				databaseBuilder.close();
			}
		}
	}

	/*
	public void loadDataTest()
	{
		databaseBuilder.loadDataTest();
	}
	*/
	
	public boolean chkLoginAlready(){		
		String sqlQuery = "select * from TransactionHistoryLog";
		SQLiteDatabase db =  null;
		Cursor cur = null;
		try{
			db = databaseBuilder.open();
			cur = db.rawQuery(sqlQuery, null);
			return cur.moveToFirst();
		}finally{
			if (db != null)db.close();
		}
	}	
	public int logout(){
		int rowEffected = 0;
		
		String sqlDelete = "delete from TransactionHistoryLog";
//		String sqlInsert = "insert into TransactionHistoryLog values('0','datetime()')";
		
		SQLiteDatabase db =  null;
		try{
			db = databaseBuilder.open();
		
//			rowEffected = DbUtils.executeSqlStatementsInTx(db, new String[]{sqlDelete,sqlInsert});
			rowEffected = DbUtils.executeSqlStatements(db, new String[]{sqlDelete});
		}finally{
			if (db != null)db.close();
		}
		return rowEffected;

	}
	public int updateTransactionLogFistTime(){
		int rowEffected = 0;
		
		String sqlDelete = "delete from TransactionHistoryLog";
		String sqlInsert = "insert into TransactionHistoryLog values('1','datetime()')";
		
		SQLiteDatabase db =  null;
		try{
			db = databaseBuilder.open();
		
			rowEffected = DbUtils.executeSqlStatementsInTx(db, new String[]{sqlDelete,sqlInsert});
		}finally{
			if (db != null)db.close();
		}
		return rowEffected;
	}
	public Result doLogin(String deviceId,
			String user,String password)
	{
		Result result = null;
		String outBoMessage = null;
		boolean bOk = false;
		
		SQLiteDatabase db =  databaseBuilder.open();
		Team team = new Team();
		try{
			String teamSql = Team.SIMPLE_QUERY_TEAM + " where "+Team.DEVICE_ID+"='"+deviceId+"'";
			
			Cursor cur = db.rawQuery(teamSql ,null); 
			if (cur.moveToFirst())
			{
				team.onBind(cur);
				cur.close();
				
				String empInTeamSql = EmpAssignedInTeam.SIMPLE_QUERY_EMP_ASSIGNED_IN_TEAM + 
						" where "+EmpAssignedInTeam.TEAM_ID+"="+team.getTeamID()+""+
						" and "+EmpAssignedInTeam.IS_TEAM_LEADER+"=1";

				cur = db.rawQuery(empInTeamSql, null);
				if (cur.moveToFirst())
				{
					EmpAssignedInTeam empInTeam = new EmpAssignedInTeam();
					empInTeam.onBind(cur);
					cur.close();

					String employeeSql = Employee.SIMPLE_QUERY_EMPLOYEE + " where "+Employee.EMPLOYEE_CODE+"='"+empInTeam.getEmployeeCode()+"'";
					employeeSql += " and "+Employee.USER_NAME_LOGOIN+"='"+user+"'";
					employeeSql += " and "+Employee.PASSWORD_LOGIN+"='"+password+"'";
					
					cur = db.rawQuery(employeeSql, null);
					bOk = cur.moveToFirst();
					if (!bOk)
					{
						outBoMessage = new String(this.context.getResources().getString(R.string.login_user_not_exist));						
					}
					cur.close();
				}else{
					if (outBoMessage == null)
					{
						outBoMessage = new String(this.context.getResources().getString(R.string.login_no_user_in_team));
					}
				}
			}else{
				if (outBoMessage == null)
				{
					outBoMessage = new String(this.context.getResources().getString(R.string.login_user_no_device_id));
				}
			}
		}finally{
			if (db != null)
			{
				db.close();
			}
		}
//		result = new Result(bOk,outBoMessage);
//		return result;
		return null;
	}
}

package com.epro.psmobile.da;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.DbCursorHolder;
import com.epro.psmobile.data.EmpAssignedInTeam;
import com.epro.psmobile.data.Employee;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.ExpenseSummary;
import com.epro.psmobile.data.ExpenseType;
import com.epro.psmobile.data.InspectDataGroupType;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectDataSVGResult;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectHistory;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.LayoutItemScaleHistory;
import com.epro.psmobile.data.LicensePlate;
import com.epro.psmobile.data.MarketPrice;
import com.epro.psmobile.data.MarketPriceFinder;
import com.epro.psmobile.data.MembersInTeamHistory;
import com.epro.psmobile.data.News;
import com.epro.psmobile.data.PhotoTeamHistory;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductAmountUnit;
import com.epro.psmobile.data.ProductGroup;
import com.epro.psmobile.data.ProductInJobRequest;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.ReasonSentence.ReasonType;
import com.epro.psmobile.data.SubExpenseType;
import com.epro.psmobile.data.TableMaster;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskComplete;
import com.epro.psmobile.data.TaskFormDataSaved;
import com.epro.psmobile.data.TaskFormTemplate;
import com.epro.psmobile.data.TaskResend;
import com.epro.psmobile.data.TaskResponse;
import com.epro.psmobile.data.TaskResponseList;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TeamCheckInHistory;
import com.epro.psmobile.data.TeamCheckInHistory.HistoryType;
import com.epro.psmobile.data.TransactionStmtHolder;
import com.epro.psmobile.data.UniversalCheckListView;
import com.epro.psmobile.util.DataUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PSBODataAdapter {

	public enum WhereInStatus{
		NONE(0),
		DO_PLAN_TASK(1),
		TO_DO_TASK(2);
		
		private int code = 0;
		WhereInStatus(int code)
		{
			this.code = code;
		}
		public int getCode(){
			return this.code;
		}
		public static WhereInStatus getWhereInStatus(int code)
		{
			for(int i = 0; i < values().length;i++)
			{
				if (values()[i].getCode() == code)
				{
					return values()[i];
				}
			}
			return WhereInStatus.NONE;
		}
	};
	private enum ExecuteType
	{
		INSERT_STMT,
		UPDATE_STMT,
		DELETE_STMT
	};
	private Context context;
	private PSBOManager dbMgn;
	private DataMasterBuilder databaseBuilder;
	
	private PSBODataAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dbMgn = new PSBOManager(context);
		dbMgn.initialMastertableIfNotExist();
		
		//DataUtil.installDBTest(context, DataMasterHelper.DATABASE_VERSION);
		/*
		try{
			dbMgn.loadDataTest();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}*/
		databaseBuilder = new DataMasterBuilder(context);
	}

	/*
	 * Transaction statements
	 */
	public synchronized int deleteStatementHolder(TransactionStmtHolder transactionStmtHolder) throws Exception
	{
		return executeStatementHolder(transactionStmtHolder,ExecuteType.DELETE_STMT);
	}
	public synchronized int updateStatementHolder(TransactionStmtHolder transactionStmtHolder) throws Exception
	{
		return executeStatementHolder(transactionStmtHolder,ExecuteType.UPDATE_STMT);
	}
	public synchronized int insertStatementHolder(TransactionStmtHolder transactionStmtHolder) throws Exception
	{
		return executeStatementHolder(transactionStmtHolder,ExecuteType.INSERT_STMT);
	}
	public synchronized int deleteStatementHolderList(ArrayList<TransactionStmtHolder> transactionList) throws Exception
	{
		return executeStatementHolderList(transactionList,ExecuteType.DELETE_STMT);
	}
	public synchronized int insertStatementHolderList(ArrayList<TransactionStmtHolder> transactionList) throws Exception
	{
		return executeStatementHolderList(transactionList,ExecuteType.INSERT_STMT);
	}
	public synchronized int updateStatementHolderList(ArrayList<TransactionStmtHolder> dels , ArrayList<TransactionStmtHolder> inserts)
	throws Exception
	{
		int rowEffected = 0;
		int iStmts = inserts.size();
		String sqls[] = new String[iStmts+1];
		sqls[0] = dels.get(0).deleteStatement();
		for(int i = 0; i < inserts.size();i++)
		{
			sqls[i+1] = inserts.get(i).insertStatement();
		}
		rowEffected = executeStatements(sqls);
		return rowEffected;
	}
	public synchronized int deleteAllInspectDataObjectSaved(String taskCode,int customerSurveySiteID)
	throws Exception
	{
	   String sqls[] = new String[]{
	         "delete from InspectDataObjectSaved where taskCode='"+taskCode+"' and customerSurveySiteID="+customerSurveySiteID
	   };
	   return executeStatements(sqls);
	    
	}
	public synchronized int updateExpense(int expenseTypeId,int statusCode,ArrayList<TransactionStmtHolder> expenseList) throws Exception
	{
		String sqls[] = new String[expenseList.size()+1];
		String sql = "delete from expense where expenseTypeId="+expenseTypeId+" and expenseStatus="+statusCode;
		sqls[0] = sql;
		for(int i = 0;i < expenseList.size();i++)
		{
			sqls[i+1] = expenseList.get(i).insertStatement();
		}
		return executeStatements(sqls);
		
	}
	public synchronized int updateEmpAssignedInTeams(ArrayList<EmpAssignedInTeam> empAssignedInTeamList) throws Exception
	{
		String sqls[] = new String[empAssignedInTeamList.size()+1];
		sqls[0] = "delete from EmpAssignedInTeam";
		for(int i = 0;i < empAssignedInTeamList.size();i++)
		{
			sqls[i+1] = empAssignedInTeamList.get(i).insertStatement();
		}
		return executeStatements(sqls);
	}
	public synchronized int executeStatements(String[] stmts) throws Exception
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		try{
			sqliteDb = databaseBuilder.open();			
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, stmts);
		}	
		finally{
		if (sqliteDb != null)
			sqliteDb.close();
		}
		return rowEffected;
	}
	private synchronized int executeStatementHolderList(ArrayList<TransactionStmtHolder> transactionList,
			ExecuteType executeType) throws Exception
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		int idx = 0;
		try{
			String[] sqlStmts = new String[transactionList.size()];
			
			switch(executeType)
			{
				case INSERT_STMT:
				{
					for(TransactionStmtHolder transactionStmtHolder:transactionList){
							String sqlExecute = transactionStmtHolder.insertStatement();
							sqlStmts[idx] = sqlExecute;
							idx++;
					}
				}break;
				case UPDATE_STMT:
				{
					for(TransactionStmtHolder transactionStmtHolder:transactionList){
						String sqlExecute = transactionStmtHolder.updateStatement();
						sqlStmts[idx] = sqlExecute;
						idx++;
					}
				}break;
				case DELETE_STMT:
				{
					for(TransactionStmtHolder transactionStmtHolder:transactionList){
						String sqlExecute = transactionStmtHolder.deleteStatement();
						sqlStmts[idx] = sqlExecute;
						idx++;
					}
				}break;
			}
			
			sqliteDb = databaseBuilder.open();			
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, sqlStmts);
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
	  return rowEffected;
	}
	public synchronized int clearTaskComplete(String taskCode)
	{
		int rowEffected = 0;
		String[] tables = new String[] {"InspectDataSVGResult",
										"LayoutItemScaleHistory",
										"InspectDataObjectSaved",
										"InspectDataObjectPhotoSaved",
										"TaskFormDataSaved"};
		String[] dels = new String[tables.length];
		for(int i = 0; i < tables.length;i++)
			dels[i] = "delete from "+tables[i]+" where taskCode='"+taskCode+"'";
		
		SQLiteDatabase sqliteDb  = null;
		try{
		  sqliteDb = databaseBuilder.open();			
		  rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, dels);
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();			
		}
		return rowEffected;
	}
	public synchronized ArrayList<LicensePlate> getAllLicensePlate() throws Exception
	{
		String sql = "select * from LicensePlate";
		return query(sql,LicensePlate.class);
	}
	public synchronized ArrayList<SubExpenseType> getAllSubExpenseTypeByExpenseTypeID(int expenseTypeID) throws Exception
	{
		String sql = "select * from SubExpenseType " +
				"where "+SubExpenseType.COLUMN_EXPENSE_TYPE_ID+"="+expenseTypeID;
		return query(sql,SubExpenseType.class);
	}
	public synchronized ArrayList<InspectDataObjectSaved> getAllInspectDataObjectSaved() throws Exception
	{
		String sql = "select * from InspectDataObjectSaved";
		return query(sql,InspectDataObjectSaved.class);
	}
	/*
	public synchronized ArrayList<InspectDataObjectSaved> findGodownInspectDataObjectSaved() throws Exception
	{
		String sql = "select * from InspectDataObjectSaved , InspectDataItem" +
				" where InspectDataObjectSaved.inspectDataItemID = InspectDataItem.inspectDataItemID " +
				" and ( InspectDataItem.isGodownComponent = 'true' or " + 
				" InspectDataItem.isBuildingComponent = 'true' )";
				
		return query(sql,InspectDataObjectSaved.class);
	}*/

	public synchronized ArrayList<InspectDataObjectPhotoSaved> getAllInspectDataObjectPhotoSaved() throws Exception
	{
		String sql = "select * from InspectDataObjectPhotoSaved";
		return query(sql,InspectDataObjectPhotoSaved.class);
	}
	public synchronized ArrayList<InspectDataSVGResult> getAllInspectDataSVGResult() throws Exception
	{
		String sql = "select * from InspectDataSVGResult";
		return query(sql,InspectDataSVGResult.class);
	}
	public synchronized int updateInspectDataSVGResult(InspectDataSVGResult dataSvgResult) throws Exception
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;

		try{
			sqliteDb = databaseBuilder.open();			
			String delSql = dataSvgResult.deleteStatement();
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{delSql,dataSvgResult.insertStatement()});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		
		return rowEffected;
	}
	public synchronized int updateSyncStatusToTasks() throws Exception
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;

		try{
			sqliteDb = databaseBuilder.open();			
			String updateTask = "update Task set "+Task.COLUMN_IS_UPLOADED_SYNCED+"='true'";
			updateTask += " where "+Task.COLUMN_TASK_STATUS+" in ("+TaskStatus.CONFIRM_INSPECT.getTaskStatus();
			updateTask += " , "+TaskStatus.SHIFT.getTaskStatus();
			updateTask += " ,"+TaskStatus.CANCEL.getTaskStatus();
			updateTask += "," +TaskStatus.FINISH.getTaskStatus();
			updateTask += ")";
			
			
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{updateTask});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		
		return rowEffected;
		
	}
	public synchronized int updateCarPhotoSetIDInJobRequestProduct(JobRequestProduct jobRequestProduct) throws Exception
	{
	   int rowEffected = 0;
	   
       SQLiteDatabase sqliteDb  = null;

       try{
           sqliteDb = databaseBuilder.open();          
           String update = "update JobRequestProduct set "+JobRequestProduct.COLUMN_PHOTO_SET_ID+"="+jobRequestProduct.getPhotoSetID();
           update += " where "+JobRequestProduct.COLUMN_C_VIN+"='"+jobRequestProduct.getcVin().trim()+"'";
           
           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{update});
       }finally{
           if (sqliteDb != null)
               sqliteDb.close();
       }
	   
	   return rowEffected;
	}
	public synchronized int updateUniversalPhotoSetIDInJobRequestProduct(JobRequestProduct jobRequestProduct) throws Exception
    {
       int rowEffected = 0;
       
       SQLiteDatabase sqliteDb  = null;

       try{
           sqliteDb = databaseBuilder.open();          
           String update = "update JobRequestProduct set "+JobRequestProduct.COLUMN_PHOTO_SET_ID+"="+jobRequestProduct.getPhotoSetID();
           update += " where "+JobRequestProduct.COLUMN_C_WARE_HOUSE+"="+jobRequestProduct.getcWareHouse()+"";
           update += " and "+JobRequestProduct.COLUMN_PRODUCT_ROW_ID+"="+jobRequestProduct.getProductRowID();
           
           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{update});
       }finally{
           if (sqliteDb != null)
               sqliteDb.close();
       }
       
       return rowEffected;
    }
	public synchronized int updateInspectDataObjectSaved(InspectDataObjectSaved dataObjectSaved) throws Exception
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;

		try{
			sqliteDb = databaseBuilder.open();			
			String delSql = dataObjectSaved.deleteStatement();
			delSql += " and "+InspectDataObjectSaved.COLUMN_INSPECT__DATA_OBJECT_ID+"="+dataObjectSaved.getInspectDataObjectID();
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{delSql,dataObjectSaved.insertStatement()});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		
		return rowEffected;
	}
	public synchronized LayoutItemScaleHistory getLayoutScale(String taskCode,int customerSurveySiteId) throws Exception
	{
		LayoutItemScaleHistory scaleHistory = null;
		String sql = "select * from LayoutItemScaleHistory where taskCode='"+taskCode+"' and customerSurveySiteID="+customerSurveySiteId;
		ArrayList<LayoutItemScaleHistory> scaleList = query(sql,LayoutItemScaleHistory.class);
		if (scaleList != null)
		{
			return scaleList.get(0);
		}
		return scaleHistory;
	}
	private synchronized int executeStatementHolder(TransactionStmtHolder transactionStmtHolder,ExecuteType executeType) throws Exception
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		
		try{
			String sqlExecute = "";
			switch(executeType)
			{
				case INSERT_STMT:
				{
					sqlExecute = transactionStmtHolder.insertStatement();
					Log.d("DEBUG_D", "insert statement "+sqlExecute);
				}break;
				case UPDATE_STMT:
				{
					sqlExecute = transactionStmtHolder.updateStatement();
				}break;
				case DELETE_STMT:
				{
					sqlExecute = transactionStmtHolder.deleteStatement();
				}break;
			}
			String[] sqlStmts = new String[]{
					sqlExecute
			};
			sqliteDb = databaseBuilder.open();			
			rowEffected = DbUtils.executeSqlStatements(sqliteDb, sqlStmts);
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
	  return rowEffected;
	}
	public synchronized int insertLayoutItemScaleHistory(LayoutItemScaleHistory scaleHistory) throws Exception 
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;
		try{
			sqliteDb = databaseBuilder.open();
			String del = scaleHistory.deleteStatement();
			String insert = scaleHistory.insertStatement();
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{del,insert});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}		
		return rowEffected;
		
	}

   public synchronized ArrayList<CarInspectStampLocation> getAllCarInspectStampLocation()
   throws Exception 
   {
	   String sql = "select * from CarInspectStampLocation ";
	   return query(sql,CarInspectStampLocation.class);
   }
   public synchronized ArrayList<CarInspectStampLocation> getAllCarInspectStampLocationByAddNewSiteOnly()
   throws Exception 
   {
      String sql = "select * from CarInspectStampLocation " +
      		"where "+CarInspectStampLocation.COLUMN_FLAG_ADD_NEW_SITE+"='Y'";
      return query(sql,CarInspectStampLocation.class);
   }
   public synchronized InspectJobMapper getInspectJobMapper(int jobRequestID,String taskCode)
   throws Exception
   {
      InspectJobMapper jobMapper = null;
      String sql = "select * from UniversalJobMapper where jobRequestID="+jobRequestID+" and taskCode='"+taskCode+"'";
      ArrayList<InspectJobMapper> mapperList = 
            query(sql,InspectJobMapper.class);
      if (mapperList != null){
         jobMapper = mapperList.get(0);
      }
      return jobMapper;
   }
   public synchronized ArrayList<InspectFormView> getInspectFormViewList(int inspectFormViewID)
   throws Exception
   {
      String sql = "select * from UniversalListFormView where inspectFormViewID = "+inspectFormViewID;
      return query(sql,InspectFormView.class);
   }
   public synchronized ArrayList<CarInspectStampLocation> getAllCarInspectStampLocation(
	          int jobRequestID)
	    throws Exception
	    {
	       
	       String sql = "select * from CarInspectStampLocation";
	       sql += " where "+CarInspectStampLocation.COLUMN_JOB_REQUEST_ID+"="+jobRequestID;
	       
	       ArrayList<CarInspectStampLocation> results = query(sql,CarInspectStampLocation.class);
	       return results;
	    }
    public synchronized int updateNewLocationOfCarInspectStampLocation(CarInspectStampLocation updateLocation)
    throws Exception
    {
       int rowEffected = 0;
       SQLiteDatabase sqliteDb = null;
       try{
           sqliteDb = databaseBuilder.open();
           String update = "update  CarInspectStampLocation set " +
           		""+CarInspectStampLocation.COLUMN_SITE_ADDRESS+"='"+updateLocation.getSiteAddress()+"'";
           update += " where "+CarInspectStampLocation.COLUMN_TASK_CODE+"='"+updateLocation.getTaskCode()+"'";
           update += " and "+CarInspectStampLocation.COLUMN_JOB_REQUEST_ID+"="+updateLocation.getJobRequestID();
           update += " and "+CarInspectStampLocation.COLUMN_CUSTOMER_SURVEY_SITE_ID+"="+updateLocation.getCustomerSurveySiteID();

           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{
                 update});
       }finally{
           if (sqliteDb != null)
               sqliteDb.close();
       }       
       return rowEffected;
    }
	public synchronized CarInspectStampLocation findCarInspectStampLocation(String taskCode,
	      int taskDuplicatedNo,
	      int jobRequestID,
	      int customerSurveySiteID)
	throws Exception
	{
	   CarInspectStampLocation carInspectStampLocation = null;
	   
	   String sql = "select * from CarInspectStampLocation";
	   sql += " where "+CarInspectStampLocation.COLUMN_TASK_CODE+"='"+taskCode+"'";
	   sql += " and "+CarInspectStampLocation.COLUMN_TASK_DUPLICATED_NO+"="+taskDuplicatedNo;
	   sql += " and "+CarInspectStampLocation.COLUMN_JOB_REQUEST_ID+"="+jobRequestID;
	   sql += " and "+CarInspectStampLocation.COLUMN_CUSTOMER_SURVEY_SITE_ID+"="+customerSurveySiteID;
       ArrayList<CarInspectStampLocation> results = query(sql,CarInspectStampLocation.class);
       if ((results != null)&&(results.size() > 0))
       {
          carInspectStampLocation = results.get(0);
       }
	   return carInspectStampLocation;
	}
	public synchronized int insertCarInspectStampLocation(CarInspectStampLocation carInspectStampLocation) throws Exception
	{
       int rowEffected = 0;
       SQLiteDatabase sqliteDb = null;
       try{
           sqliteDb = databaseBuilder.open();
           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{carInspectStampLocation.insertStatement()});
       }finally{
           if (sqliteDb != null)
               sqliteDb.close();
       }       
       return rowEffected;
	   
	}
	public synchronized int insertInspectHistory(InspectHistory history) throws Exception{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;
		try{
			sqliteDb = databaseBuilder.open();
			String del = history.deleteStatement();
			String insert = history.insertStatement();
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{del,insert});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}		
		return rowEffected;
	}
	public synchronized int deleteInspectPhotoEntryByPhotoSetID(int photoSetId){
	   int rowEffected = 0;
       SQLiteDatabase sqliteDb  = null;
       String delete = "delete from InspectDataObjectPhotoSaved where photoID="+photoSetId;
       try{
          sqliteDb = databaseBuilder.open();          
          rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{delete});
      }finally{
          if (sqliteDb != null)
              sqliteDb.close();
      }
       return rowEffected;
	}
	public synchronized int insertInspectPhotoEntry(ArrayList<InspectDataObjectPhotoSaved> inspectDataObjectPhotoSavedList)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		
		if ((inspectDataObjectPhotoSavedList != null)&&(inspectDataObjectPhotoSavedList.size() > 0))
		{
			int photoSetId = inspectDataObjectPhotoSavedList.get(0).getPhotoID();
			
			String delete = "delete from InspectDataObjectPhotoSaved where photoID="+photoSetId;
			String strSqls[] = new String[inspectDataObjectPhotoSavedList.size()+1];
			strSqls[0] = delete;
			for(int i = 0; i < inspectDataObjectPhotoSavedList.size();i++)
			{
				strSqls[i+1] = inspectDataObjectPhotoSavedList.get(i).insertStatement();
			}
			try{
				sqliteDb = databaseBuilder.open();			
				rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, strSqls);
			}finally{
				if (sqliteDb != null)
					sqliteDb.close();
			}
		}
		return rowEffected;
	}
	public synchronized Task createDuplicateTask(Task oldTask,
			String locationStart,
			int startSiteID,
			String locationTo,
			int endSiteID,
			double distance) throws Exception
	{
		Task newTask = null;
		if (oldTask != null)
		{
			ArrayList<Task> taskDuplicatedList =  new ArrayList<Task>();//getAllTasksByTaskCode(oldTask.getTaskCode());
			
			ArrayList<Task> tasks = this.getAllTasks();
			for(Task t : tasks){
				String taskCode = t.getTaskCode();
				if (taskCode.contains(oldTask.getTaskCode())){
					taskDuplicatedList.add(t);
				}
			}
			
			int duplicatedNo = 0;
			Task tail = taskDuplicatedList.get(taskDuplicatedList.size()-1);
			
			duplicatedNo = tail.getTaskDuplicatedNo() + 1;
			
			newTask = (Task)oldTask.clone();			
			
			newTask.setTaskCode(newTask.getTaskCode()+"_"+(duplicatedNo-1));
			
			newTask.setTaskDuplicatedNo(duplicatedNo);
			newTask.setDuplicatedTask(true);
			newTask.setDuplicatedFromTaskID(oldTask.getTaskID());
			newTask.setDuplicatedLocationStart(locationStart);
			newTask.setDuplicatedLocationTo(locationTo);
			newTask.setDuplicatedDistance(distance);
			newTask.setDuplicatedSurveyStartSiteID(startSiteID);
			newTask.setDuplicatedSurveyEndSiteID(endSiteID);
//			newTask.setTaskStatus(TaskStatus.CONFIRMED_FROM_WEB);
			newTask.setTaskStatus(TaskStatus.DUPLICATED);
			
			int rowEffected = this.insertStatementHolder(newTask);
			if (rowEffected > 0)return newTask;
					
		}
		return null;
	}
	public synchronized boolean hasCarJobRequestProductOneRow(JobRequestProduct jobRequestProduct) throws Exception
	{
	   boolean bRet = false;
	   String sql = " select * from JobRequestProduct where ";
	   //sql += " cWareHouse="+jobRequestProduct.getcWareHouse();
       //sql += " and (cErrorType != 0)";
       sql += " cVin ='"+jobRequestProduct.getcVin()+"'";

       ArrayList<JobRequestProduct> rows = this.query(sql, JobRequestProduct.class);
       if (rows.size() == 1){
          bRet = true;
       }
	   return bRet;
	}
	public synchronized int updateResetErrorTypeOfJobRequestProduct(JobRequestProduct jobRequestProduct)
	{
	   int rowEffected = 0;
       SQLiteDatabase sqliteDb = null;

       
           try{
              /*
               String sql = "delete from InspectDataObjectPhotoSaved " +
                   "where taskCode='"+taskCode.trim()+"' and customerSurveySiteID="+customerSurveySiteID+ " " +
                           "and photoID =  "+photoID;
           */
           String sql = "update JobRequestProduct set cErrorType = 0 where ";
              sql += " cWareHouse="+jobRequestProduct.getcWareHouse();
              sql += " and cTeamId = "+jobRequestProduct.getcTeamId();
              sql += " and cJobRowId = "+jobRequestProduct.getcJobRowId();
              sql += " and cVin ='"+jobRequestProduct.getcVin()+"'";
              sql += " and cOrder="+jobRequestProduct.getcOrder();
           
           sqliteDb = databaseBuilder.open();
           rowEffected = DbUtils.executeSqlStatements(sqliteDb, new String[]{sql});
           }finally{
               if (sqliteDb != null)
                   sqliteDb.close();
           }
       
       return rowEffected;
	}
	public synchronized int deleteJobRequestProduct(JobRequestProduct jobRequestProduct)
	{
	   int rowEffected = 0;
       SQLiteDatabase sqliteDb = null;

       
           try{
              /*
               String sql = "delete from InspectDataObjectPhotoSaved " +
                   "where taskCode='"+taskCode.trim()+"' and customerSurveySiteID="+customerSurveySiteID+ " " +
                           "and photoID =  "+photoID;
           */
           String sql = "delete from JobRequestProduct where ";
              sql += " cWareHouse="+jobRequestProduct.getcWareHouse();
              sql += " and (cErrorType != 0)";
              sql += " and cTeamId = "+jobRequestProduct.getcTeamId();
              sql += " and cJobRowId = "+jobRequestProduct.getcJobRowId();
              sql += " and cVin ='"+jobRequestProduct.getcVin()+"'";
           
           sqliteDb = databaseBuilder.open();
           rowEffected = DbUtils.executeSqlStatements(sqliteDb, new String[]{sql});
           }finally{
               if (sqliteDb != null)
                   sqliteDb.close();
           }
       
       return rowEffected;

	}
	
	public synchronized int deleteUniversalRowJobRequestProduct(JobRequestProduct jobRequestProduct)
    {
       int rowEffected = 0;
       SQLiteDatabase sqliteDb = null;

       
           try{
              /*
               String sql = "delete from InspectDataObjectPhotoSaved " +
                   "where taskCode='"+taskCode.trim()+"' and customerSurveySiteID="+customerSurveySiteID+ " " +
                           "and photoID =  "+photoID;
           */
           String sql = "delete from JobRequestProduct where ";
              sql += " jobRequestId =  "+jobRequestProduct.getJobRequestID();
              sql += " and customerSurveySiteID="+jobRequestProduct.getCustomerSurveySiteID();
              sql += " and productRowId="+jobRequestProduct.getProductRowID();
              
           sqliteDb = databaseBuilder.open();
           rowEffected = DbUtils.executeSqlStatements(sqliteDb, new String[]{sql});
           }finally{
               if (sqliteDb != null)
                   sqliteDb.close();
           }
       
       return rowEffected;

    }
	
	public synchronized int deletePhotoObjectSaveds(int photoID,String taskCode,int customerSurveySiteID)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;

		
			try{
				String sql = "delete from InspectDataObjectPhotoSaved " +
					"where taskCode='"+taskCode.trim()+"' and customerSurveySiteID="+customerSurveySiteID+ " " +
							"and photoID =  "+photoID;
			
			sqliteDb = databaseBuilder.open();
			rowEffected = DbUtils.executeSqlStatements(sqliteDb, new String[]{sql});
			}finally{
				if (sqliteDb != null)
					sqliteDb.close();
			}
		
		return rowEffected;

	}
	public synchronized int deleteDuplicatedTask(String taskCode,int taskDuplicatedNo){
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;

		if (taskDuplicatedNo > 1)
		{
			try{
				String sql = "delete from Task " +
					"where taskCode='"+taskCode.trim()+"' and taskDuplicateNo="+taskDuplicatedNo;
			
			sqliteDb = databaseBuilder.open();
			rowEffected = DbUtils.executeSqlStatements(sqliteDb, new String[]{sql});
			}finally{
				if (sqliteDb != null)
					sqliteDb.close();
			}
		}
		return rowEffected;
	}
	public synchronized int updateTaskIsReportGenerated(String taskCode,
			boolean isGenerated,
			int duplicateNo)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;
		try{
			sqliteDb = databaseBuilder.open();
			String sql = "update task set ";
			sql += " isInspectReportGenerated='"+isGenerated+"'";
			sql += " where taskCode='"+taskCode+"'";
			sql += " and taskDuplicateNo="+duplicateNo;
			
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}		
		return rowEffected;		

	}
	public synchronized int updateTaskCancel(String taskCode,ReasonSentence reason,String other)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;
		try{
			sqliteDb = databaseBuilder.open();
			String sql = "update task set ";
			sql += "reasonSentenceID="+reason.getReasonID()+",";
			
//			sql += "reasonSentenceType="+reason.getReasonType().getTypeCode()+",";
			if (reason.getReasonType() == ReasonType.REASON_OTHER){
				sql += "reasonSentenceType='"+reason.getReasonTypeCode()+"',";				
			}else{
				sql += "reasonSentenceType='"+reason.getReasonType().getTypeCode()+"',";
			}
		
			sql += "taskStatus="+TaskStatus.CANCEL.getTaskStatus();
			if ((other != null)&&(!other.isEmpty())){
				sql += ",shiftCause = '"+other.trim()+"'";
			}
			sql += " where taskCode='"+taskCode+"'";
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}		
		return rowEffected;		
	}
	public synchronized int updateTaskRemark(int jobRequestID,
			String taskCode,String remark)
	{
		int rowEffected = 0;
		String strSql = "update task set remark='"+remark+"'";
		strSql += " where jobRequestID="+jobRequestID;
		strSql += " and taskCode='"+taskCode+"'";
		SQLiteDatabase sqliteDb = null;
		try{
			sqliteDb = databaseBuilder.open();
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{strSql});
		}
		finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}		
		return rowEffected;
	}
	public synchronized int updateTaskPostPone(String taskCode,
			String date,
			String startTime,
			String endTime,
			ReasonSentence reason,
			String other){
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;
		try{
			sqliteDb = databaseBuilder.open();
			String sql = "update task set taskDate ='"+date+"' , taskStartTime='"+startTime+"',taskEndTime='"+endTime+"',";
			sql += "reasonSentenceID="+reason.getReasonID()+",";
			if (reason.getReasonType() == ReasonType.REASON_OTHER){
				sql += "reasonSentenceType='"+reason.getReasonTypeCode()+"',";				
			}else{
				sql += "reasonSentenceType='"+reason.getReasonType().getTypeCode()+"',";
			}
			sql += "taskStatus="+TaskStatus.SHIFT.getTaskStatus();
			
			if ((other != null)&&(!other.isEmpty())){
				sql += ", shiftCause = '"+other.trim()+"'";
			}
			sql += " where taskCode='"+taskCode+"'";
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}		
		return rowEffected;
	}
	public synchronized int updateTaskConfirm(int taskID,String startTime,String endTime,TaskStatus taskStatus)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		try{
			sqliteDb = databaseBuilder.open();
			//taskStartTime='"+startTime+"',taskEndTime='"+endTime+"'
			String sql = "update task set taskStatus="+taskStatus.getTaskStatus()+"," +
					" taskStartTime='"+startTime+"',taskEndTime='"+endTime+"' " +
					" where taskID="+taskID;
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		return rowEffected;
	}
	
	public synchronized int updateTaskStatus(String taskCode,int taskDuplicatedNo , TaskStatus taskStatus)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		try{
			sqliteDb = databaseBuilder.open();
			String sql = "update task set taskStatus="+taskStatus.getTaskStatus()+" " +
					" where taskCode='"+taskCode.trim()+"'";
			sql += "  and taskDuplicateNo="+taskDuplicatedNo;
			
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		return rowEffected;
	}
	public synchronized int deleteExpenseByExpenseTypeIdAndStatus(int expenseTypeId,int statusCode)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		try{
			sqliteDb = databaseBuilder.open();
			String sql = "delete from expense where expenseTypeId="+expenseTypeId+" and expenseStatus="+statusCode;
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		return rowEffected;		
	}
	///////////////////////////////////////////////
	
	public static PSBODataAdapter getDataAdapter(Context context){
		return new PSBODataAdapter(context);		
	}	
	public synchronized int clearCarInspectStampLocation(){
	   int rowEffected = 0;
       SQLiteDatabase sqliteDb  = null;
       try{
           sqliteDb = databaseBuilder.open();
           /*
           String delete = "delete from JobRequestProduct where jobRequestID="+jobRequestID;
           String[] stmts = new String[jobRequestProducts.size()+1];
           stmts[0] = delete;
           for(int i = 0;i < jobRequestProducts.size();i++)
           {
              stmts[i+1] = jobRequestProducts.get(i).insertStatement();
           }*/
           
           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{"delete from CarInspectStampLocation"});
       }finally{
           if (sqliteDb != null)
               sqliteDb.close();
       }
       return rowEffected;
	}
	public synchronized int insertJobRequestProductBackup(ArrayList<JobRequestProduct> jobRequestProducts)
	throws Exception
	{
	   int rowEffected = 0;
	   SQLiteDatabase sqliteDb  = null;
       
	   String[] strInserts = new String[jobRequestProducts.size()];
	   Hashtable<Integer,Integer> hJobRequestTable = new Hashtable<Integer,Integer>();
	   
	   for(int i = 0; i < jobRequestProducts.size();i++){
	      JobRequestProduct jobRequestProduct = jobRequestProducts.get(i);
	      jobRequestProduct.setBackup(true);
	      strInserts[i] = jobRequestProduct.insertStatement();
	      if (!hJobRequestTable.containsKey(jobRequestProduct.getJobRequestID())){
	         hJobRequestTable.put(jobRequestProduct.getJobRequestID(), jobRequestProduct.getJobRequestID());
	      }
	   }
	   ArrayList<Integer> jobRequestIds = new ArrayList<Integer>(hJobRequestTable.keySet());
	   String[] strDels = new String[jobRequestIds.size()];
	   for(int i = 0; i < jobRequestIds.size();i++){
	      strDels[i] = "delete from JobRequestProductBackup where jobRequestID = "+jobRequestIds.get(i);
	   }
	   
	   try{
	       String[] stmts = concat(strDels,strInserts);
           sqliteDb = databaseBuilder.open();
           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, stmts);
       }finally{
           if (sqliteDb != null)
               sqliteDb.close();
       } 
	   return rowEffected;
	}
	
	@SuppressWarnings({ "unused", "unchecked"})
   private static <T> T[] concat(T[] a, T[] b) {
	    final int alen = a.length;
	    final int blen = b.length;
	    if (alen == 0) {
	        return b;
	    }
	    if (blen == 0) {
	        return a;
	    }
	    final T[] result = (T[]) java.lang.reflect.Array.
	            newInstance(a.getClass().getComponentType(), alen + blen);
	    System.arraycopy(a, 0, result, 0, alen);
	    System.arraycopy(b, 0, result, alen, blen);
	    return result;
	}
	public synchronized ArrayList<JobRequestProduct> getAllJobRequestProductInTaskNoFinish()
	throws Exception
	{
	   String sql = "SELECT JobRequestProduct.* " +
	   		" FROM JobRequestProduct INNER JOIN Task ON JobRequestProduct.jobRequestID = Task.jobRequestID " +
	   		" INNER JOIN JobRequest ON Task.jobRequestID = JobRequest.jobRequestID " +
	   		" and jobRequest.inspectTypeID > 2 and task.taskStatus = 4";
	   
       return query(sql,JobRequestProduct.class);
	   
	}
	public synchronized Hashtable<Integer,Integer> tableJobRequestdInJobRequestProductBackup()
	throws Exception
	{
	   ArrayList<JobRequestProduct> backupList =  query("select * from JobRequestProductBackup ",JobRequestProduct.class);
	   if (backupList != null){
	      Hashtable<Integer,Integer> jobRequestIdTable = new Hashtable<Integer,Integer>();
	      for(JobRequestProduct jrp : backupList){
	         if (!jobRequestIdTable.containsKey(jrp.getJobRequestID())){
	            jobRequestIdTable.put(jrp.getJobRequestID(), jrp.getJobRequestID());
	         }
	      }
	      return jobRequestIdTable;
	   }
	   return null;
	}
	public synchronized ArrayList<JobRequestProduct> getAllJobRequestProductBackup()
	throws Exception
	{
	   return query("select * from JobRequestProductBackup ",JobRequestProduct.class);
	}
	public synchronized ArrayList<JobRequestProduct> findJobRequestProductsByJobRequestID(int jobRequestID,
	      int customerSurveySiteID) throws Exception
	{
		String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID;
		if (customerSurveySiteID > 0){
		 sql +=  " and customerSurveySiteID="+customerSurveySiteID+" order by productRowId";
		}
		return query(sql,JobRequestProduct.class);
	}
	public synchronized ArrayList<JobRequestProduct> findJobRequestProductsByJobRequestIDWithWarehouse(int jobRequestID,
          int customerSurveySiteID) throws Exception
    {
        String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + " and cWarehouse="+customerSurveySiteID+" order by productRowId";
        return query(sql,JobRequestProduct.class);
    }
	public synchronized UniversalCheckListView findUniversalCheckListView(int taskFormTemplateID,int taskFormAttributeID) throws Exception
	{
	   String sql = "select * from UniversalCheckListView where taskFormTemplateID = "+taskFormTemplateID +" and taskFormAttributeID="+taskFormAttributeID;
	   ArrayList<UniversalCheckListView> chkListViews = query(sql,UniversalCheckListView.class);
	   if (chkListViews != null){
	      return chkListViews.get(0);
	   }
	   return null;
	}
	public synchronized ArrayList<JobRequestProduct> findJobRequestProductsByJobRequestIDWithSiteID(int jobRequestID,
	          int inspectTypeID,
	          String taskCode,
	          int customerSurveySiteID,
	          int maxRowPerPage,
	          int rowOffset,String keyFilter) throws Exception
	{
	        String sql = "select * from JobRequestProduct where " +
	        		   " jobRequestID="+jobRequestID;
	        if ((inspectTypeID == 5)||(inspectTypeID == 8))
	        {
	        }else{
               sql += " and customerSurveySiteID="+customerSurveySiteID+" ";	           
	        }
	        if (!keyFilter.isEmpty()){
	           sql += " and "+keyFilter;
	        }
	        sql +=   " order by productRowId";
	        sql += " limit "+maxRowPerPage+" offset "+rowOffset;
	        
	        ArrayList<JobRequestProduct> jrpList = query(sql,JobRequestProduct.class);
	        if (jrpList != null){
	           for(JobRequestProduct jrp : jrpList){
	              ArrayList<TaskFormDataSaved> dataSaveds = 
	                    this.findUniversalTaskFormDataSavedList(jobRequestID,inspectTypeID,
	                          taskCode, customerSurveySiteID, jrp.getProductRowID());	              
	              if (dataSaveds != null){
	                 jrp.setHasCheckList(true);
	              }
	           }
	        }
	        return jrpList;
	}
	public synchronized int getRowCountOfJobRequestProduct(int jobRequestID,
              int customerSurveySiteID,String keyFilter) throws Exception
    {
	        String sql = "select * from JobRequestProduct where " +
             " jobRequestID="+jobRequestID+"";
	        if (customerSurveySiteID > 0){
	           sql += " and customerSurveySiteID="+customerSurveySiteID+" ";
	        }
	        if (!keyFilter.isEmpty()){
	           sql += " and "+keyFilter;
	        }
            sql+= " order by productRowId";
	        
	        ArrayList<JobRequestProduct> jobRequestProducts = 
	              query(sql,JobRequestProduct.class);
	        
	        if (jobRequestProducts != null)
	        {
	           return jobRequestProducts.size();
	        }
	        return 0;
    }
	public synchronized ArrayList<JobRequestProduct> findJobRequestProductsByJobRequestID(int jobRequestID) throws Exception
	{
	  String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + "  order by cReasonId, productRowId";
	   //   String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + "  order by cReasonId";

	  return query(sql,JobRequestProduct.class);
	}
	   public synchronized ArrayList<JobRequestProduct> findJobRequestProductsByJobRequestIDAndOrderBy(int jobRequestID) throws Exception
	    {
	      String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + "  order by cOrder";
	       //   String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + "  order by cReasonId";

	      return query(sql,JobRequestProduct.class);
	    }
	   public synchronized ArrayList<JobRequestProduct> findOrderByCOrderJobRequestProductsByJobRequestID(int jobRequestID) throws Exception
	    {
	      String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + "  order by cReasonId";
	       //   String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + "  order by cReasonId";

	      return query(sql,JobRequestProduct.class);
	    }
	   public synchronized ArrayList<JobRequestProduct> findUniversalJobRequestProduct(int jobRequestID,int customerSurveySiteID)
	   throws Exception
	   {
	      String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + " and customerSurveySiteID = "+customerSurveySiteID+" order by productRowId";
	      return query(sql,JobRequestProduct.class);
	   }
       public synchronized ArrayList<JobRequestProduct> findUniversalJobRequestProductAllSite(int jobRequestID)
       throws Exception
       {
          String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + " order by productRowId";
          return query(sql,JobRequestProduct.class);
       }

	public synchronized ArrayList<JobRequestProduct> findCarJobRequestProductsByJobRequestID(int jobRequestID,
          int customerSurveySiteID) throws Exception
    {
        String sql = "select * from JobRequestProduct where jobRequestID="+jobRequestID + " and " +
        		" ( (cWareHouse="+customerSurveySiteID+") or (cWareHouse <= 0) )" +
        				" order by productRowId";
        return query(sql,JobRequestProduct.class);
    }
	public synchronized int insertCarJobRequestProduct(int jobRequestID,
	      ArrayList<JobRequestProduct> jobRequestProducts)
	throws Exception
	{
	   int rowEffected = 0;
       SQLiteDatabase sqliteDb  = null;
       try{
           sqliteDb = databaseBuilder.open();
           String delete = "delete from JobRequestProduct where jobRequestID="+jobRequestID;
           String[] stmts = new String[jobRequestProducts.size()+1];
           stmts[0] = delete;
           for(int i = 0;i < jobRequestProducts.size();i++)
           {
              stmts[i+1] = jobRequestProducts.get(i).insertStatement();
           }
           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, stmts);
       }finally{
           if (sqliteDb != null)
               sqliteDb.close();
       }
       return rowEffected;
	}
	   public synchronized int insertUniversalJobRequestProduct(int inspectTypeID ,
	         int jobRequestID,int siteID,
	          ArrayList<JobRequestProduct> jobRequestProducts)
	    throws Exception
	    {
	       int rowEffected = 0;
	       SQLiteDatabase sqliteDb  = null;
	       try{
	           Hashtable<Integer,ArrayList<JobRequestProduct>> mapTable = new Hashtable<Integer,ArrayList<JobRequestProduct>>();
	           ArrayList<JobRequestProduct> 
	              tmp_jobRequestProducts = new ArrayList<JobRequestProduct>();

	           for(JobRequestProduct jrp : jobRequestProducts){
	              if (!mapTable.containsKey(jrp.getProductRowID())){
	                 mapTable.put(jrp.getProductRowID(), new ArrayList<JobRequestProduct>());
	                 mapTable.get(jrp.getProductRowID()).add(jrp);
	                 tmp_jobRequestProducts.add(jrp);
	              }
	           }
	           jobRequestProducts = tmp_jobRequestProducts;
	           sqliteDb = databaseBuilder.open();
	           String delete = "";
	           if ((inspectTypeID == 5)||(inspectTypeID == 8)){
                  delete = "delete from JobRequestProduct where jobRequestID="+jobRequestID;
	           }
	           else{
	               delete = "delete from JobRequestProduct where jobRequestID="+jobRequestID+" and customerSurveySiteID="+siteID;	              
	           }
	           String[] stmts = new String[jobRequestProducts.size()*2];
	           for(int i = 0; i < jobRequestProducts.size();i++)
	           {
	              stmts[i] = delete + " and productRowId = "+jobRequestProducts.get(i).getProductRowID();
	           }
	           int objIdx = 0;
	           for(int i = jobRequestProducts.size();i < jobRequestProducts.size()*2;i++)
	           {
	              stmts[i] = jobRequestProducts.get(objIdx++).insertStatement();
	           }
	           rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, stmts);
	       }finally{
	           if (sqliteDb != null)
	               sqliteDb.close();
	       }
	       return rowEffected;
	    }
	   public synchronized int insertSingleRowUniversalJobRequestProduct(int jobRequestID,int siteID,
             JobRequestProduct jobRequestProduct)
       throws Exception
       {
          int rowEffected = 0;
          SQLiteDatabase sqliteDb  = null;
          try{
              sqliteDb = databaseBuilder.open();
              rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{jobRequestProduct.insertStatement()});
          }finally{
              if (sqliteDb != null)
                  sqliteDb.close();
          }
          return rowEffected;
       }
	//cWareHouse
	public synchronized int getCountHasCar(int jobRequestID,int wareHouse)
	{
	   return getCount("JobRequestProduct","jobRequestID = "+jobRequestID+" and cWarehouse = "+wareHouse+" and cSight='Y'");
	}
	public synchronized ArrayList<JobRequestProduct> getAllJobRequestProduct() throws Exception
	{
		String sql = "select * from JobRequestProduct";
		return query(sql,JobRequestProduct.class);
	}
	public synchronized TaskResponseList<TaskComplete> getTaskComplete()
	throws Exception 
	{
		TaskResponseList<TaskComplete> datas = new TaskResponseList<TaskComplete>() ;
		String sql = "select * from taskComplete";
		ArrayList<TaskComplete> taskCompletes = query(sql,TaskComplete.class);
		if (taskCompletes != null){
			datas.addAll(taskCompletes);
		}
		return datas;
	}
	public synchronized TaskResponseList<TaskResend> getTaskResend()
	throws Exception 
	{
		TaskResponseList<TaskResend> datas = new TaskResponseList<TaskResend>() ;
		String sql = "select * from taskResend";
		ArrayList<TaskResend> taskCompletes = query(sql,TaskResend.class);
		if (taskCompletes != null){
					datas.addAll(taskCompletes);
		}
		return datas;
	}
	public synchronized ArrayList<News> getAllNewsPublishPeriod(int days)
	throws Exception
	{
		/*
		 SELECT
    REPLACE(SUBSTR(d, CASE SUBSTR(d, 6, 1) WHEN '-' THEN 7 ELSE 5 END), '-', '')||'-'|| --Year
    SUBSTR('0'||REPLACE(SUBSTR(d, 3, CASE SUBSTR(d, 4, 1) WHEN '-' THEN 2 ELSE 3 END), '-', ''), -2)||'-'|| --Month
    SUBSTR('0'||REPLACE(SUBSTR(d, 1, 2), '-', ''), -2) --Day
FROM (SELECT '11-11-2013' AS d)
		 */
//		String sql = "select * from News where publishDate >= date('now','-"+days+" daay')";			
		
		String sql = " select * , ( " +
				
			    " SELECT "+
			    " REPLACE(SUBSTR(publishDate, CASE SUBSTR(publishDate, 6, 1) WHEN '-' THEN 7 ELSE 5 END), '-', '')||'-'|| "+
			    " SUBSTR('0'||REPLACE(SUBSTR(publishDate, 3, CASE SUBSTR(publishDate, 4, 1) WHEN '-' THEN 2 ELSE 3 END), '-', ''), -2)||'-'|| "+
			    " SUBSTR('0'||REPLACE(SUBSTR(publishDate, 1, 2), '-', ''), -2) "+
			    " ) as new_publishDate "+
			    " from News ";
		sql += " where new_publishDate >= date('now','-"+days+" day')";	
			
		return query(sql,News.class);
	}
	public synchronized ArrayList<News> getAllNewsPublish()
	throws Exception
	{
//		String sql = "select * from News where publishDate <= date('now')";	
		String sql = " select * , ( " +
				
			    " SELECT "+
			    " REPLACE(SUBSTR(publishDate, CASE SUBSTR(publishDate, 6, 1) WHEN '-' THEN 7 ELSE 5 END), '-', '')||'-'|| "+
			    " SUBSTR('0'||REPLACE(SUBSTR(publishDate, 3, CASE SUBSTR(publishDate, 4, 1) WHEN '-' THEN 2 ELSE 3 END), '-', ''), -2)||'-'|| "+
			    " SUBSTR('0'||REPLACE(SUBSTR(publishDate, 1, 2), '-', ''), -2) "+
			    " ) as new_publishDate "+
			    " from News ";
		sql += " where new_publishDate <= date('now')";
		return query(sql,News.class);
	}
	public synchronized ArrayList<News> getAllNewsPublishNow()
	throws Exception
	{
//		String sql = "select * from News where publishDate == date('now')";			
String sql = " select * , ( " +
				
			    " SELECT "+
			    " REPLACE(SUBSTR(publishDate, CASE SUBSTR(publishDate, 6, 1) WHEN '-' THEN 7 ELSE 5 END), '-', '')||'-'|| "+
			    " SUBSTR('0'||REPLACE(SUBSTR(publishDate, 3, CASE SUBSTR(publishDate, 4, 1) WHEN '-' THEN 2 ELSE 3 END), '-', ''), -2)||'-'|| "+
			    " SUBSTR('0'||REPLACE(SUBSTR(publishDate, 1, 2), '-', ''), -2) "+
			    " ) as new_publishDate "+
			    " from News ";
		sql += " where new_publishDate == date('now')";
		return query(sql,News.class);
	}
	public synchronized ArrayList<InspectHistory> getInspectHistory(int jobRequestID,int taskNo)
	throws Exception
	{
		String sql = "select * from InspectHistory where jobRequestID="+jobRequestID+" and taskNo="+taskNo;
		return query(sql,InspectHistory.class);
	}
	public synchronized MarketPriceFinder getMarketPriceFinderBy(int provincialID) throws Exception{
		
		String sql = "select * from marketprice where provincialID="+provincialID;
		ArrayList<MarketPrice> priceList = query(sql,MarketPrice.class);		
		if (priceList != null){
			MarketPriceFinder finder = new MarketPriceFinder();
			finder.addAll(priceList);
			return finder;
		}else
			return null;
	}
	
	public synchronized ArrayList<Product> findProductByProductGroupId(int productGroupID)
	throws Exception
	{
		String sql = "select * from product where productGroupID="+productGroupID+" order by productName";
		return query(sql,Product.class);
	}

	public synchronized ArrayList<Product> findProductWithUnitByProductGroupId(int productGroupID)
	         throws Exception
	{
       String sql = "select product.*, ProductAmountUnit.unitName from product,ProductAmountUnit " +
             " where product.productAmountUnitID = ProductAmountUnit.productAmountUnitID";
	    if (productGroupID >= 0){
	       sql += " and  product.productGroupID="+productGroupID+"";
	    }
	    sql += " order by product.productName";
	     return query(sql,Product.class);
	 }

	public synchronized ArrayList<ProductGroup> findProductGroupByJobRequestId(int jobRequestID)
	throws Exception
	{
		String sql = "select * from  ProductGroup  , JobRequestProductGroup " +
				" where ProductGroup.productGroupID = JobRequestProductGroup.productGroupID";
			sql += " and JobRequestProductGroup.jobRequestID = "+jobRequestID;
		return query(sql,ProductGroup.class);
	}
	
	public synchronized ArrayList<ProductGroup> findProductGroupByJobRequestId(int jobRequestID,int productGroupID)
			throws Exception
			{
				String sql = "select * from  ProductGroup  , JobRequestProductGroup " +
						" where ProductGroup.productGroupID = JobRequestProductGroup.productGroupID";
					sql += " and JobRequestProductGroup.jobRequestID = "+jobRequestID;
					sql += " and ProductGroup.productGroupID="+productGroupID;
				return query(sql,ProductGroup.class);
			}
	public synchronized Product findProductByProductGroupIdAndProductId(int productGroupID,int productID)
	throws Exception
	{
		String sql = "select * from product where productGroupID="+productGroupID+" and productID="+productID;
		ArrayList<Product> products = query(sql,Product.class);
		if (products != null)
		{
			return products.get(0);
		}
		return null;
	}
	public synchronized ArrayList<ProductGroup> getAllProductGroups()
	throws Exception
	{
		String sql = "select * from productgroup";
		return query(sql,ProductGroup.class);
	}
	public synchronized ProductGroup findProductGroupById(int productGroupID)
	throws Exception
	{
		String sql = "select * from productgroup where productGroupID="+productGroupID;
		ArrayList<ProductGroup> productGroups = query(sql,ProductGroup.class);
		if (productGroups != null)
		{
			return productGroups.get(0);
		}return null;
	}
	public synchronized ArrayList<ReasonSentence> getAllReasonSentenceByType(ReasonType reasonType)
	throws Exception
	{
		return getAllReasonSentenceByType(reasonType.getTypeCode());
	}
	public synchronized ArrayList<ReasonSentence> getAllReasonSentenceByType(String reasonType)
	throws Exception
	{
		String sql = " select * from reasonsentence where reasonSentenceType = '"+reasonType+"'";
		return query(sql,ReasonSentence.class);
	}

	public synchronized ArrayList<ExpenseSummary> getAllSummaryExpense()
	throws Exception
	{
		String sql = "select sum(amount) as sum_amount ," +
				" expenseType.expenseTypeName , " +
				"  expenseType.expenseTypeID, " +
				"  expense.expenseStatus , " +
				"  expense.expenseDate "+
				"from expense , expenseType ";
		sql += " where expense.expenseTypeId = expenseType.expenseTypeId";
		sql += " group by  expense.expenseTypeId , expense.expenseStatus ,expense.expenseDate ";
		
		return query(sql,ExpenseSummary.class);
	}
	public synchronized ArrayList<Expense> getAllExpense()
	throws Exception
	{
		/*
		Sequel Pro
		 */
		String sql = "select * from expense , expenseType ";
		sql += " where expense.expenseTypeID = expenseType.expenseTypeID";
		
		return query(sql,Expense.class);
	}
	public synchronized ArrayList<Expense> getExpenseByTypeID(int expenseTypeID)
	throws Exception
	{
		String sql = "select * from expense where expenseTypeID="+expenseTypeID;
		return query(sql,Expense.class);
	}
	public synchronized ArrayList<ExpenseType> getAllExpenseType()
	throws Exception
	{
		String sql = "select * from expenseType";
		return query(sql,ExpenseType.class);
	}
	public ArrayList<Expense> getAllExpense(String startDate,
											String endDate)
			throws Exception
	{
//		startDate = endDate = "21-1-2014";
		String sql = "select * from expense , expenseType ";
		sql += " where expense.expenseTypeID = expenseType.expenseTypeID";
		sql += " and  (expense.expenseDate between '"+startDate+"'";
		sql += " and '"+endDate+"')";

		
		return query(sql,Expense.class);
	}
	public synchronized ArrayList<InspectDataObjectPhotoSaved> getInspectDataObjectPhotoSaved(int photoID)
	throws Exception
	{
		String sql = "select * from InspectDataObjectPhotoSaved where photoID="+photoID;
		return query(sql,InspectDataObjectPhotoSaved.class);
	}
	public synchronized ArrayList<InspectDataObjectPhotoSaved> getInspectDataObjectPhotoSaved(String taskCode,
          int customerSurveySiteID,int photoID)
	      throws Exception
	      {
	          String sql = "select * from InspectDataObjectPhotoSaved ";
	          sql += "where taskCode='"+taskCode+"' and ";
	                sql += " customerSurveySiteID="+customerSurveySiteID;
	          sql += " and photoID="+photoID;
	          return query(sql,InspectDataObjectPhotoSaved.class);
	      }
	public synchronized ArrayList<InspectDataObjectPhotoSaved> getInspectDataObjectPhotoSaved(String taskCode,
			int customerSurveySiteID)
			throws Exception
	{
		String sql = "select * from InspectDataObjectPhotoSaved where taskCode='"+taskCode+"' and ";
		sql += " customerSurveySiteID="+customerSurveySiteID;
		return query(sql,InspectDataObjectPhotoSaved.class);
	}
	
	public synchronized ArrayList<InspectDataObjectPhotoSaved> getInspectDataObjectPhotoSavedWithGeneralImage(String taskCode,
          int customerSurveySiteID)
          throws Exception
  {
      String sql = "select * from InspectDataObjectPhotoSaved where taskCode='"+taskCode+"' and ";
      sql += " customerSurveySiteID="+customerSurveySiteID;
      sql += " and flagGeneralImage='Y'";
      return query(sql,InspectDataObjectPhotoSaved.class);
  }
	   
	public synchronized ArrayList<InspectDataObjectPhotoSaved> getInspectDataObjectPhotoSavedWithGeneralImage(
	         String taskCode
	         )
	          throws Exception
	  {
	      String sql = "select * from InspectDataObjectPhotoSaved where taskCode='"+taskCode+"'";
	      sql += " and flagGeneralImage='Y'";
	      return query(sql,InspectDataObjectPhotoSaved.class);
	  }
	/*
	public ArrayList<InspectDataObjectSaved> getInspectDataObjectSaved(int jobRequestID,
			int customerSurveySiteID) throws Exception
	{
		String sql = " select * from InspectDataObjectSaved where jobRequestID="+jobRequestID+" and ";
		sql += " customerSurveySiteID="+customerSurveySiteID;
		
		
		return query(sql,InspectDataObjectSaved.class);
	}*/
	public synchronized ArrayList<InspectDataObjectSaved> getInspectDataObjectSaved(String taskCode,
			int customerSurveySiteID) throws Exception
	{
		String sql = " select * from InspectDataObjectSaved where taskCode='"+taskCode+"' and ";
		sql += " customerSurveySiteID="+customerSurveySiteID;
		sql += " order by inspectDataObjectID";
		return query(sql,InspectDataObjectSaved.class);
	}
	
	
     public synchronized ArrayList<InspectDataObjectSaved> getInspectDataObjectSavedUniverals(String taskCode,
	            int customerSurveySiteID) throws Exception
	 {
        
        String sql = " SELECT DISTINCT InspectDataObjectSaved.*  " +
        		" FROM InspectDataObjectSaved INNER JOIN InspectDataItem ON InspectDataObjectSaved.inspectDataItemID = InspectDataItem.inspectDataItemID " +
        		" where InspectDataObjectSaved.taskCode='"+taskCode+"' and InspectDataItem.isUniversalLayoutDropdown = 'true' " +
        		" and InspectDataObjectSaved.customerSurveySiteID = "+customerSurveySiteID+ "  order by InspectDataObjectSaved.inspectDataObjectID "
        		;
        
        
//	        String sql = " select * from InspectDataObjectSaved where taskCode='"+taskCode+"' and ";
//	        sql += " customerSurveySiteID="+customerSurveySiteID;
//	        sql += " order by inspectDataObjectID";
	        return query(sql,InspectDataObjectSaved.class);
	    }
	public synchronized int insertPhotoTeamHistory(ArrayList<PhotoTeamHistory> photoTeamHistoryList) throws Exception
	{
		int rowEffected = 0;
		
		SQLiteDatabase sqliteDb  = null;
		try{
			if ((photoTeamHistoryList != null)&&(photoTeamHistoryList.size() > 0)){
				PhotoTeamHistory photoTeam = photoTeamHistoryList.get(0);
				String delSql = "delete from PhotoTeamHistory where imgTeamHistoryID="+photoTeam.getPhotoTeamCheckInHistoryId();
				String[] sqls = new String[photoTeamHistoryList.size()+1];
				sqls[0] = delSql;
				for(int i = 0; i < photoTeamHistoryList.size();i++)
				{
					sqls[i+1] = photoTeamHistoryList.get(0).insertStatement();
				}
				sqliteDb = databaseBuilder.open();
				rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, sqls);
			}
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		
		return rowEffected;		
	}
	public synchronized ArrayList<PhotoTeamHistory> getPhotoTeamHistoryByImgTeamHistoryID(int imgTeamHistoryID) throws Exception
	{
		String strSql = "select * from phototeamhistory where imgteamhistoryid="+imgTeamHistoryID;
		return query(strSql,PhotoTeamHistory.class);
	}
	public synchronized int insertMembersInTeamHistory(ArrayList<MembersInTeamHistory> membersInTeamHistory) throws Exception
	{
		ArrayList<TransactionStmtHolder> stmt_list = new ArrayList<TransactionStmtHolder>();
		stmt_list.addAll(membersInTeamHistory);
		int rowEffected = executeStatementHolderList(
				stmt_list,ExecuteType.INSERT_STMT);
		return rowEffected;
	}
	public synchronized int updateLastTeamCheckOutDateTime(
			java.sql.Timestamp currentTimestamp,
			String taskCode,
			int taskDuplicateNo,
			int historyType,
			String customerName,
			String customerSurveySites,
			int inspectTypeID,
			String inspectTypeName,
			boolean isTaskCompleted
			)
	{
		/*
		 * customerName TEXT,
customerSurveySites TEXT,
inspectTypeName TEXT,
isTaskCompleted BOOLEAN
		 */
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		try{
			sqliteDb = databaseBuilder.open();
			String sql = "update teamcheckinhistory set lastCheckOutDateTime = '"+DataUtil.convertTimestampToStringYYYMMDDHHmmss(currentTimestamp)+"'";
			sql += " , customerName='"+customerName+"'";
			sql += " , customerSurveySites='"+customerSurveySites+"'";
			sql += " , inspectTypeID="+inspectTypeID;
			sql += " , inspectTypeName='"+inspectTypeName+"'";
			sql += " , isTaskCompleted='"+Boolean.toString(isTaskCompleted)+"'";
			sql += " where taskCode='"+taskCode+"'";
			sql += " and taskDuplicateNo="+taskDuplicateNo;
			sql += " and historyType="+historyType;
			
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		return rowEffected;
	}

	public synchronized int updateLastTeamCheckOutDateTimeDay(java.sql.Timestamp currentTimestamp,
			String numberOfMilesAtEndPoint,
			String carLicenseNumber,
			int licensePlateId,
			int teamCheckInHistoryID)
	{
		int rowEffected = 0;
		SQLiteDatabase sqliteDb  = null;
		try{
			sqliteDb = databaseBuilder.open();
//			String sql = "update task set taskStatus="+taskStatus.getTaskStatus()+" where taskID="+taskID;
			String sql = "update teamcheckinhistory set lastCheckOutDateTime = '"+DataUtil.convertTimestampToStringYYYMMDDHHmmss(currentTimestamp)+"'";
			sql += " , numberOfMilesAtEndPoint = '"+numberOfMilesAtEndPoint+"',";
			sql += " carLicenseNumber = '"+carLicenseNumber+"'";
			sql += " , licensePlateId = "+licensePlateId+"";
			sql += " , isTaskCompleted='true'";
			sql += " , inspectTypeID = -1 ";
			sql += " where teamCheckInHistoryID="+teamCheckInHistoryID;
			
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, new String[]{sql});
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}
		return rowEffected;
	}
	public synchronized ArrayList<TeamCheckInHistory> getAllTeamCheckInHistory()
	throws Exception 
	{
		String strSql = "select * from TeamCheckInHistory ";
		return query(strSql,TeamCheckInHistory.class);
	}
	public synchronized ArrayList<TeamCheckInHistory> getTeamCheckInHistoryList(int teamID,
			String startDate/*yyyy-mm-dd*/,
			String endDate,
			int inspectTypeID)
	throws Exception
	{
		String strSql = "select * from TeamCheckInHistory " +
				" where teamID="+teamID+" and " +
				"inspectTypeID = "+inspectTypeID+" and isTaskCompleted='true'";
		
		
		strSql += " and  ( date(TeamCheckInHistory.lastCheckInDateTime) between date('"+startDate+"')";
		strSql += " and date('"+endDate+"'))";

		
		return query(strSql,TeamCheckInHistory.class);
	}
	public synchronized Hashtable<String,ArrayList<TeamCheckInHistory>> getTableTeamCheckInHistoryList(int teamID,
			String startDate,
			String endDate,
			int inspectTypeID)
			throws Exception
	{
		Hashtable<String,ArrayList<TeamCheckInHistory>> mTable = 
				new Hashtable<String,ArrayList<TeamCheckInHistory>>();
		String strSql = "select * from TeamCheckInHistory " +
				" where teamID="+teamID+" and " +
				" ( inspectTypeID = "+inspectTypeID+" or inspectTypeID = -1 ) and isTaskCompleted='true'";
		
		
		strSql += " and  ( date(TeamCheckInHistory.lastCheckInDateTime) between date('"+startDate+"')";
		strSql += " and date('"+endDate+"'))";
		strSql += " order by  numberOfMilesAtStartPoint desc";
		ArrayList<TeamCheckInHistory> teamCheckInList =  query(strSql,TeamCheckInHistory.class);
		
		
		if (teamCheckInList != null)
		{
			for(TeamCheckInHistory teamCheckIn : teamCheckInList)
			{
				Date date = DataUtil.getZeroTimeDate(teamCheckIn.getLastCheckInDateTime());
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				int day = c.get(Calendar.DAY_OF_MONTH);
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);
				
				String key = day+""+month+""+year;
				if (!mTable.containsKey(key))
				{
					mTable.put(key, new ArrayList<TeamCheckInHistory>());
				}
				mTable.get(key).add(teamCheckIn);
			}
		}
		
		return mTable;
	}
	public synchronized ArrayList<MembersInTeamHistory> getAllMembersInTeamHistory()
	throws Exception
	{
		String strSql = "select * from MemberInTeamHistory";
		return query(strSql,MembersInTeamHistory.class);
	}
	public synchronized ArrayList<PhotoTeamHistory> getAllPhotoTeamHistory()
	throws Exception
	{
		String strSql = "select * from PhotoTeamHistory";
		return query(strSql,PhotoTeamHistory.class);
	}
	public synchronized ArrayList<MembersInTeamHistory> getMembersInTeamHistoryListByHistoryID(int teamCheckInHistoryID)
	throws Exception 
	{
		String strSql = "select * from MemberInTeamHistory where teamCheckInHistoryID = "+teamCheckInHistoryID;
		return query(strSql,MembersInTeamHistory.class);
	}
	public synchronized TeamCheckInHistory getLastTeamCheckInHistory() throws Exception
	{
		/*
		 * select * from TABLE where COLUMN < (select max(COLUMN) from TABLE);
		 */
		String strSql = "select * from teamcheckinhistory " +
				"where teamCheckInHistoryID = (select max(teamCheckInHistoryID) from teamcheckinhistory)";
		ArrayList<TeamCheckInHistory> teamsHistoryList = query(strSql,TeamCheckInHistory.class);
		if ((teamsHistoryList != null)&&(teamsHistoryList.size() > 0)){
			return teamsHistoryList.get(0);
		}
		return null;
	}
	public synchronized int insertTeamCheckInHistory(TeamCheckInHistory teamCheckInHistory) throws Exception
	{
		int rowEffected = executeStatementHolder(teamCheckInHistory, ExecuteType.INSERT_STMT);
		return rowEffected;
	}
	public synchronized int getMaxTeamCheckInHistoryID(HistoryType historyType){
		String strWhere = null;
		if (historyType != HistoryType.NONE)
		{
			strWhere = "historyType="+historyType.getCode();
		}
		return getMax("teamCheckInHistoryID","TeamCheckInHistory",strWhere);
	}
	public synchronized int getMaxTeamPhotoID(){
		return getMax("imgTeamHistoryID","PhotoTeamHistory",null);
	}
	public synchronized int getMaxPhotoID(){
		return getMax("photoID","InspectDataObjectPhotoSaved",null);
	}
	public synchronized int getMaxPhotoNoByID(int photoId)
	{
		return getMax("photoNo","InspectDataObjectPhotoSaved","photoID="+photoId);		
	}
	public synchronized int getMaxCustomerSurveySiteIDFromStamp(){
	   return getMax("customerSurveySiteID","CarInspectStampLocation",null);
	}
	public synchronized int getMaxCustomerSurveySiteID(){
	   return getMax("customerSurveySiteID","CustomerSurveySite",null);
	}
	public synchronized int getMaxObjectId()
	{
		return getMax("inspectDataObjectID","InspectDataObjectSaved","addObjectFlag='Y'");
	}
	@SuppressWarnings("unused")
	private synchronized int getCount(String tableName,String whereCause)
	{
       int maxValue = 0;
       SQLiteDatabase database = null;
       Cursor cursor = null;
       try{
           String sql = "select count(*) as max_value from "+tableName;
           if (whereCause != null)
           {
               sql += " where ";
               sql += whereCause;
           }
           
           database = databaseBuilder.open();
           cursor = database.rawQuery(sql, null);
           if (cursor.moveToFirst())
           {
               maxValue = cursor.getInt(cursor.getColumnIndex("max_value"));
           }
       }
       finally{
           if (cursor != null)
               cursor.close();
           if (database != null)
               database.close();
       }
       return maxValue;

	}
	private synchronized int getMax(String fieldName,
			String tableName,
			String whereCaluse)
	{
		int maxValue = 0;
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try{
			String sql = "select max("+fieldName+") as max_value from "+tableName;
			if (whereCaluse != null)
			{
				sql += " where ";
				sql += whereCaluse;
			}
			
			database = databaseBuilder.open();
			cursor = database.rawQuery(sql, null);
			if (cursor.moveToFirst())
			{
				maxValue = cursor.getInt(cursor.getColumnIndex("max_value"));
			}
		}
		finally{
			if (cursor != null)
				cursor.close();
			if (database != null)
				database.close();
		}
		return maxValue;
	}
	public synchronized Team getTeamByDeviceId(String deviceId) throws Exception
	{
		String sql = Team.SIMPLE_QUERY_TEAM + " where deviceID='"+deviceId+"'";
		Team team = null;
		ArrayList<Team> teams = query(sql,Team.class);
		if (teams != null){
		   if (teams.size() > 0){
		      team = teams.get(0);
		   }		
		}
		return team;
	}
	public synchronized Team getTeamByTeamId(int teamID) throws Exception
	{
		String sql = Team.SIMPLE_QUERY_TEAM + " where teamID="+teamID+"";
		Team team = null;
		ArrayList<Team> teams = query(sql,Team.class);
		if (teams != null){
		   if (teams.size() > 0){
		      team = teams.get(0);
		   }
		}
		return team;
	}
	public synchronized ArrayList<EmpAssignedInTeam> getAllEmpAssignedInTeamEx(int teamID)
	throws Exception 
	{
		 String sql = " SELECT Team.*, "
				 +" EmpAssignedInTeam.*, "
				 +" Employee.*"
				 +" FROM EmpAssignedInTeam INNER JOIN Employee ON EmpAssignedInTeam.employeeCode = Employee.employeeCode"
				 +" INNER JOIN Team ON Team.teamID = EmpAssignedInTeam.teamID ";
		 
			 
		 sql += " where team.teamID = "+teamID;
		 return query(sql,EmpAssignedInTeam.class);
	}
	public synchronized ArrayList<EmpAssignedInTeam> getAllEmpAssignedInTeam(int teamID)
	throws Exception
	{
		String sql = EmpAssignedInTeam.SIMPLE_QUERY_EMP_ASSIGNED_IN_TEAM + " where teamID="+teamID;
		return query(sql,EmpAssignedInTeam.class);
	}
	public synchronized Employee getEmployeeByEmpId(int employeeId) throws Exception
	{
		Employee emp = null;
		ArrayList<Employee> empList = query("select * from employee where employeeID="+employeeId,Employee.class);
		if (empList.size() > 0){
			emp = empList.get(0);
		}
		return emp;
	}
	public synchronized Employee getEmployeeByEmpCode(String employeeCode) throws Exception
	{
		Employee emp = null;
		ArrayList<Employee> empList = query("select * from employee " +
				"where employeeCode='"+employeeCode+"'",
				Employee.class);
		if (empList.size() > 0){
			emp = empList.get(0);
		}
		return emp;
	}
	public synchronized ArrayList<Employee> getAllEmployee() throws Exception
	{
		String sql = "select * from employee";
		return query(sql,Employee.class);
	}
	
	public synchronized ArrayList<Employee> getAllEmployee(String[] employeeCodeIgnores) throws Exception
	{
		String whereClause = "";
		if (employeeCodeIgnores != null)
		{
			if (employeeCodeIgnores.length > 0)
			{
				whereClause = " where employeeCode not in";
				whereClause += "(";
				for(String empCode : employeeCodeIgnores)
				{
					whereClause += "'"+empCode+"'";
					whereClause += ",";
				}
				whereClause = whereClause.substring(0, whereClause.length()-1);
				whereClause += ")";
			}
		}
		String sql = "select * from employee";
		sql += whereClause;
		sql += " order by firstName";
		return query(sql,Employee.class);
	}
	
	public synchronized ArrayList<ProductAmountUnit> getProductAmountUnitById(int productAmountUnitID) throws Exception
	{
		String sql = "select * from productAmountUnit where productAmountUnitID="+productAmountUnitID;
		return query(sql,ProductAmountUnit.class);
	}

	public synchronized ArrayList<ProductAmountUnit> getProductAmountUnit() throws Exception
	{
		String sql = "select * from productAmountUnit";
		return query(sql,ProductAmountUnit.class);
	}
	public synchronized int insertTaskFormDataSaved(ArrayList<TaskFormDataSaved> dataListSaved,JobRequestProduct jobRequestProduct) throws Exception{
		
		/*
		public final static String COLUMN_TASK_ID = "taskID";
		public final static String COLUMN_TASK_CODE = "taskCode";
		public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
 
		 */
		int rowEffected = 0;
		SQLiteDatabase sqliteDb = null;
		try{
			sqliteDb = databaseBuilder.open();
			TaskFormDataSaved dataSaved = dataListSaved.get(0);
			String del = "delete from TaskFormDataSaved where taskID = "+dataSaved.getTaskID()+" and " +
					"taskCode='"+dataSaved.getTaskCode()+"' and jobRequestID="+dataSaved.getJobRequestID();
			if (jobRequestProduct != null)
			{
			   del += " and productRowId="+jobRequestProduct.getProductRowID()+"";
			}else{
			   del += " and productRowId <= 0";
			}

			String[] strSqls = new String[dataListSaved.size()+1];
			strSqls[0] = del;//delete first
			for(int i = 0; i < dataListSaved.size();i++)
			{
				strSqls[i+1] = dataListSaved.get(i).insertStatement();
			}
			rowEffected = DbUtils.executeSqlStatementsInTx(sqliteDb, strSqls);
		}finally{
			if (sqliteDb != null)
				sqliteDb.close();
		}		
		return rowEffected;
	}
	public synchronized ArrayList<TaskFormTemplate> findTaskFormTemplateListByJobRequestId(int jobRequestID) throws Exception
	{
		String sql = "select * from taskformtemplate where jobrequestid="+jobRequestID+" order by taskcontrolno";
		return query(sql,TaskFormTemplate.class);
	}
	public synchronized ArrayList<TaskFormTemplate> findTaskFormTemplateListByTemplateFormId(int templateFormId) throws Exception
	{
		String sql = "select * from taskformtemplate where taskFormTemplateID="+templateFormId+" order by taskcontrolno";
		return query(sql,TaskFormTemplate.class);
	}
	public synchronized ArrayList<Task> findTaskDuplicates(String taskCode) throws Exception
	{
		String sql = "select * from Task where taskCode='"+taskCode+"'";
		return query(sql,Task.class);
	}
	public synchronized ArrayList<TaskFormDataSaved> getAllTaskFormDataSaved() throws Exception
	{
		String sql = "select * from TaskFormDataSaved";
		return query(sql,TaskFormDataSaved.class);
	}
	public synchronized ArrayList<TaskFormDataSaved> findTaskFormDataSavedListByJobRequestId(int jobRequestID,
			String taskCode) throws Exception
	{
		String sql = "select * from TaskFormDataSaved where taskCode='"+taskCode+"' " +
				"and jobRequestID="+jobRequestID+" " +
		        "and productRowId <= 0 "+
				"order by taskcontrolno";
		return query(sql,TaskFormDataSaved.class);
	}
	public synchronized ArrayList<TaskFormDataSaved> findUniversalTaskFormDataSavedList(
	      int jobRequestID,
	      int inspectTypeID,
          String taskCode,int siteID,int productRowID) throws Exception
  {
      String sql = "select * from TaskFormDataSaved where taskCode='"+taskCode+"' " +
              " and jobRequestID="+jobRequestID+" ";
       if ((inspectTypeID == 5)||(inspectTypeID == 8))
       {
       }else{
          sql+=  " and customerSurveySiteID="+siteID+" ";          
       }
       sql +=  " and productRowId="+productRowID+" "+
       "order by taskcontrolno";
      
      return query(sql,TaskFormDataSaved.class);
  }
	public synchronized ArrayList<Product> getAllProduct() throws Exception
	{
		String sql = "select * from product";
		return query(sql,Product.class);
	}
	public synchronized ArrayList<InspectDataItem> 
	            findInspectDataItemsByGroupId(int inspectDataGroupTypeId,int inspectTypeID) throws Exception
	{
		/*
		String sql = "SELECT InspectDataItem.inspectDataItemID, "
				+ " InspectDataItem.inspectDataGroupTypeID, "
				+ " InspectDataItem.inspectDataItemName, "
				+ " InspectDataItem.parameters, "
				+ " InspectDataItem.formula, "
				+ " InspectDataItem.densityValue, "
				+ " InspectDataItem.imageFileName"
				+ " FROM InspectDataItem";
		sql += " where  InspectDataItem.inspectDataGroupTypeID = "+inspectDataGroupTypeId;
		*/
		String sql = " SELECT InspectDataItem.*, "
				+" InspectDataGroupType.* "
				+" FROM InspectDataItem INNER JOIN InspectDataGroupType ON InspectDataItem.inspectDataGroupTypeID = InspectDataGroupType.inspectDataGroupTypeID"
		        +"  AND  InspectDataItem.inspectTypeID = InspectDataGroupType.inspectTypeID ";
		if (inspectDataGroupTypeId > 0)
			sql += " where InspectDataItem.inspectDataGroupTypeID = "+inspectDataGroupTypeId;
		if (inspectTypeID > 0)
		    sql += " and InspectDataItem.inspectTypeID="+inspectTypeID;
		
		if ((inspectDataGroupTypeId == 0)&&(inspectTypeID == 0)){
		   sql += " group by InspectDataItem.inspectTypeID";
		}
		
		return query(sql,InspectDataItem.class);
	}
	public synchronized ArrayList<InspectDataGroupType> 
	            getAllInspectDataGroupType(int inspectTypeID) throws Exception
	{
		String sql = "SELECT InspectDataGroupType.* "
					+ " FROM InspectDataGroupType where inspectTypeID="+inspectTypeID;
		return query(sql,InspectDataGroupType.class);		
	}
	public synchronized ArrayList<ProductInJobRequest> getProductInJobRequest(int jobRequestID) throws Exception
	{
		 String sql = " SELECT Product.productID, "
					+" Product.productName," 
					+" ProductInJobRequest.jobRequestID, "
					+" 	ProductInJobRequest.amount, "
					+" ProductAmountUnit.productAmountUnitID, "
					+" ProductAmountUnit.unitName"
					+" FROM ProductInJobRequest INNER JOIN Product ON ProductInJobRequest.productID = Product.productID"
	 			+"  INNER JOIN ProductAmountUnit ON ProductInJobRequest.productAmountUnitID = ProductAmountUnit.productAmountUnitID";
		 sql += " where ProductInJobRequest.jobRequestID="+jobRequestID;
					
		 return query(sql,ProductInJobRequest.class);
	}
	/*
	 SELECT InspectDataGroupType.inspecDataGroupTypeID, 
	InspectDataGroupType.inspectDataGroupTypeName, 
	InspectDataGroupType.isLayoutComponent, 
	InspectDataItem.inspecDataItemID, 
	InspectDataItem.parameters, 
	InspectDataItem.formula, 
	InspectDataItem.densityValue, 
	InspectDataItem.imageFileName
	FROM InspectDataGroupType INNER JOIN InspectDataItem ON 
		InspectDataGroupType.inspecDataGroupTypeID = InspectDataItem.inspectDataGroupTypeID
	 * */
	
	public synchronized ArrayList<InspectDataItem> getAllInspectDataItem() throws Exception
	{
		String sql =  "SELECT InspectDataGroupType.*, "
			+" InspectDataItem.*" 
			+" FROM InspectDataGroupType INNER JOIN InspectDataItem ON "
			+" 	InspectDataGroupType.inspectDataGroupTypeID = InspectDataItem.inspectDataGroupTypeID";
		
		return query(sql,InspectDataItem.class);		
	}
	public synchronized ArrayList<InspectType> getAllInspectType() throws Exception
	{
		String sql = "SELECT * FROM InspectType";
		return query(sql,InspectType.class);
	}
	/*
	public ArrayList<ProductInJobRequest> findProductInJobRequest(int jobRequestID) throws Exception
	{
		String sql = "SELECT Product.*"
					+" ProductAmountUnit.*, "
					+" ProductInJobRequest.*"
					+" FROM ProductInJobRequest INNER JOIN Product ON ProductInJobRequest.productID = Product.productID"
					+" INNER JOIN ProductAmountUnit ON ProductInJobRequest.productAmountUnitID = ProductAmountUnit.productAmountUnitID"
					+" WHERE jobRequestID="+jobRequestID+""; 
	
	return query(sql,ProductInJobRequest.class);
	}*/
	public synchronized ArrayList<CustomerSurveySite> findCustomerSurveySite(int taskID) throws Exception
	{
		String sql = "select * from CustomerSurveySite where taskID="+taskID;
		return query(sql,CustomerSurveySite.class);		
	}
	/*
	public ArrayList<CustomerSurveySite> findCustomerSurveySite(String customerCode,int jobRequestID) throws Exception
	{
		String sql = "select * from CustomerSurveySite where customerCode='"+customerCode+"' and jobRequestID = "+jobRequestID;
		return query(sql,CustomerSurveySite.class);		
	}*/
	/*
	public ArrayList<CustomerSurveySite> findCustomerSurveySite(int customerID) throws Exception
	{
		String sql = "select * from CustomerSurveySite where customerID="+customerID;
		return query(sql,CustomerSurveySite.class);		
	}*/
	public synchronized ArrayList<JobRequest> getAllJobRequests() throws Exception
	{
		String sql = "SELECT JobRequest.*"
					+" 	InspectType.*,  "
					+" FROM JobRequest INNER JOIN InspectType ON JobRequest.inspectTypeID = InspectType.inspectTypeID ";
		
		return query(sql,JobRequest.class);
	}
	public synchronized ArrayList<Task> getAllTasks(WhereInStatus whereInStatus)
	throws Exception
	{
		return getAllTasks(whereInStatus,false);
	}
	public synchronized ArrayList<Task> getAllTasks(WhereInStatus whereInStatus,boolean notUploadFlag)
	throws Exception
	{
		if (!notUploadFlag){
			return getAllTasks(null,null,false,TaskStatus.NOT,whereInStatus);
		}else{
			ArrayList<Task> tasks = getAllTasks(null,null,false,TaskStatus.NOT,whereInStatus);
			if (tasks != null){
				ArrayList<Task> tasksUploadFlags = new ArrayList<Task>();
				for(Task t : tasks){
					if (t.getNotUploadFlag().equalsIgnoreCase("N")){
						tasksUploadFlags.add(t);
					}
				}
				if (tasksUploadFlags.size() > 0)
					return tasksUploadFlags;
				else
					return null;
			}else{
				return null;
			}
		}
	}
	public synchronized ArrayList<Task> getAllTasksByStatus(TaskStatus status) throws Exception
	{
		return getAllTasksByStatus(status,false);
	}
	public synchronized ArrayList<Task> getAllTasksByStatus(TaskStatus status,boolean notUploadFlag) throws Exception
	{
		if (!notUploadFlag)
			return getAllTasks(null,null,false,status,WhereInStatus.NONE);
		else{
			ArrayList<Task> tasks = getAllTasks(null,null,false,status,WhereInStatus.NONE);
			if (tasks != null)
			{
				ArrayList<Task> tasksUploadFlags = new ArrayList<Task>();
				for(Task t : tasks){
					if (t.getNotUploadFlag().equalsIgnoreCase("N")){
						tasksUploadFlags.add(t);
					}
				}
				if (tasksUploadFlags.size() > 0)
					return tasksUploadFlags;
				else
					return null;
			}else{
				return null;
			}
		}
	}	
	public synchronized ArrayList<Task> getAllTasks() throws Exception
	{
		return getAllTasks(null,null,false,TaskStatus.NOT,WhereInStatus.NONE);
	}
	public synchronized ArrayList<Task> getAllTasksToday(Date today,WhereInStatus whereInStatus) throws Exception{
		return getAllTasks(today,null,false,TaskStatus.NOT,whereInStatus);
	}
	public synchronized ArrayList<Task> getAllTasksNextWeek(Date startDayNextWeek,Date endDayNextWeek,WhereInStatus whereInStatus) throws Exception{
		return getAllTasks(startDayNextWeek,endDayNextWeek,true,TaskStatus.NOT,whereInStatus);
	}
	public synchronized ArrayList<Task> getAllTasks(TaskStatus taskStatus,WhereInStatus whereInStatus) throws Exception
	{
		return getAllTasks(null,null,false,taskStatus,whereInStatus);
	}
	
	private synchronized String getSimpleTaskQuery(){
		/*
		String sql = " SELECT Task.taskID, "
				+" Task.taskCode, "
				+" Task.taskNo, "
				+" Task.teamID, "
				+" Task.taskFormTemplateID, "
				+" Task.taskLevel, "
				+" Task.taskStatus, "
				+" Task.taskDate, "
				+" Task.taskTime, "
				+" Task.shiftCause," 
				+" Task.remark, "
				+" JobRequest.jobRequestID, "
				+" JobRequest.purposeName, "
				+" JobRequest.typeOfSurvey, "
				+" JobRequest.dateTimeRecievedMail, "
				+" InspectType.inspectTypeName, "
				+" InspectType.inspectType, "
				+" CustomerSurveySite.customerSurveySiteID, "
				+" CustomerSurveySite.siteAddress, "
				+" CustomerSurveySite.siteTels, "
				+" CustomerSurveySite.siteEmails," 
				+" CustomerSurveySite.siteLocLat, "
				+" CustomerSurveySite.siteLocLon," 
				+" CustomerSurveySite.siteMapImg," 
				+" CustomerSurveySite.areaID, "
				+" Customer.customerID, "
				+" Customer.customerCode," 
				+" Customer.customerName, "
				+" Customer.customerAddress, "
				+" Customer.customerTels, "
				+" Customer.customerFaxs, "
				+" Customer.customerEmails, "
				+" Customer.officeLocLat, "
				+" Customer.officeLocLon, "
				+" Customer.officeMapImg, "
				+" Customer.areaID"
				+" FROM Task INNER JOIN JobRequest ON Task.jobRequestID = JobRequest.jobRequestID"
				+" INNER JOIN InspectType ON JobRequest.inspectTypeID = InspectType.inspectType"
				+" INNER JOIN Customer ON JobRequest.customerID = Customer.customerID"
				+" INNER JOIN CustomerSurveySite ON Task.customerSurveySiteID = CustomerSurveySite.customerSurveySiteID";
				*/
		String sql = " SELECT Task.*,"
				+" JobRequest.*, "
				+" InspectType.* "
				+" FROM Task INNER JOIN JobRequest ON Task.jobRequestID = JobRequest.jobRequestID"
				+" INNER JOIN InspectType ON JobRequest.inspectTypeID = InspectType.inspectTypeID";
		return sql;
	}
	public synchronized ArrayList<Task> getAllTasksByTaskCode(String taskCode) throws Exception
	{
		String sql = getSimpleTaskQuery();
		sql += " where Task.taskCode='"+taskCode.trim()+"'";
		sql += " order by date(Task.taskDate) ";
		sql += " , Task.taskDuplicateNo";

		return query(sql,Task.class);
	}
	public synchronized ArrayList<Task> getAllTasksByInspectID(int inspectID,WhereInStatus whereInStatus) throws Exception
	{
		String sql = getSimpleTaskQuery();
		sql += " where ";
		sql += "  InspectType.inspectTypeID="+inspectID;
		/*
		if (whereInStatus == WhereInStatus.DO_PLAN_TASK){
			sql += " and task.taskStatus in ("+TaskStatus.WAIT_TO_CONFIRM.getTaskStatus()+"," +
				""+TaskStatus.CANCEL.getTaskStatus()+"," +
				""+TaskStatus.SHIFT.getTaskStatus()+")";
		}else if (whereInStatus == WhereInStatus.TO_DO_TASK)
		{
			sql += " and task.taskStatus in ("+TaskStatus.FINISH.getTaskStatus()+"," +
					""+TaskStatus.LOCAL_SAVED.getTaskStatus()+"," +
					""+TaskStatus.CONFIRM_INSPECT.getTaskStatus()+")";			
		}*/
		String wherCaluseIn = generateWhereInStatus(whereInStatus);
		if (!wherCaluseIn.isEmpty()){
			sql += " and ";
			sql += wherCaluseIn;
		}
		sql += " order by date(Task.taskDate) ";

		return query(sql,Task.class);
	}
	private synchronized String generateWhereInStatus(WhereInStatus whereInStatus)
	{
		String whereCaluse = "";
		if (whereInStatus == WhereInStatus.DO_PLAN_TASK){
			whereCaluse += " task.taskStatus in ("+TaskStatus.WAIT_TO_CONFIRM.getTaskStatus()+"," +
				""+TaskStatus.CONFIRM_INSPECT.getTaskStatus()+","+
				""+TaskStatus.CANCEL.getTaskStatus()+"," +
				""+TaskStatus.SHIFT.getTaskStatus()+")";
		}else if (whereInStatus == WhereInStatus.TO_DO_TASK)
		{
			whereCaluse += " task.taskStatus in ("+TaskStatus.FINISH.getTaskStatus()+"," +
					""+TaskStatus.LOCAL_SAVED.getTaskStatus()+"," +
					""+TaskStatus.DUPLICATED.getTaskStatus()+","+
					""+TaskStatus.ALLOW_EDIT.getTaskStatus()+","+
					""+TaskStatus.CONFIRMED_FROM_WEB.getTaskStatus()+")";			
		}
		return whereCaluse;
	}
	public synchronized ArrayList<Task> getAllTasks(Date today,
			Date endday,
			boolean nextWeek,
			TaskStatus taskStatus,
			WhereInStatus whereInStatus) throws Exception
	{		
		String sql = getSimpleTaskQuery();
		
		if (today == null){
			if (taskStatus != TaskStatus.NOT){
				sql += " where task.taskStatus = "+taskStatus.getTaskStatus();
			}
		}else{
			//select by date
			if (nextWeek){
				sql += " where date(Task.taskDate) between date('"+DataUtil.convertDateToStringYYYYMMDD(today)+"')";
				sql += " and date('"+DataUtil.convertDateToStringYYYYMMDD(endday)+"')";
			}else{
				sql += " where date(Task.taskDate) = date('"+DataUtil.convertDateToStringYYYYMMDD(today)+"')";
			}
		}
/*		
		if (whereInStatus == WhereInStatus.DO_PLAN_TASK){
			sql += " and task.taskStatus in ("+TaskStatus.WAIT_TO_CONFIRM.getTaskStatus()+"," +
				""+TaskStatus.CANCEL.getTaskStatus()+"," +
				""+TaskStatus.SHIFT.getTaskStatus()+")";
		}else if (whereInStatus == WhereInStatus.TO_DO_TASK)
		{
			sql += " and task.taskStatus in ("+TaskStatus.FINISH.getTaskStatus()+"," +
					""+TaskStatus.LOCAL_SAVED.getTaskStatus()+"," +
					""+TaskStatus.CONFIRM_INSPECT.getTaskStatus()+")";			
		}
	*/
		if (whereInStatus != WhereInStatus.NONE)
		{
			sql += " and ";
			sql += generateWhereInStatus(whereInStatus);			
		}
		
		sql += " order by date(Task.taskDate) ";
		Log.d("DEBUG_D", sql);
		return query(sql,Task.class);
	}
	/*
	public ArrayList<Customer> getAllCustomers() throws Exception
	{
		String sql = "select * from customer";
		return query(sql,Customer.class);		
	}
	 */
	public synchronized ArrayList<TaskResend> getTaskResendForUpdate() throws Exception
	{
		return query("select * from taskresend",TaskResend.class);
	}
	public synchronized ArrayList<TableMaster> getAllTables() throws Exception{
		return query("SELECT name FROM sqlite_master WHERE type='table'",TableMaster.class);
	}
	public synchronized <T> ArrayList<T> query(String sqlStatement,Class<T> type) throws Exception
	{
		SQLiteDatabase database = null;
		ArrayList<T> datas = null;
		Cursor cursor = null;
		try{
			String sql = sqlStatement;
			
			database = databaseBuilder.open();
			cursor = database.rawQuery(sql, null);
			if (cursor.moveToFirst())
			{
				datas = new ArrayList<T>();
				do{
				  T obj = type.newInstance();
				  DbCursorHolder cursorHolder =  (DbCursorHolder)obj;
				  cursorHolder.onBind(cursor);
				  datas.add(obj);						
				}while(cursor.moveToNext());				
			}
		}
		finally{
			if (cursor != null)
				cursor.close();
			
			if (database != null)
				database.close();
		}
		return datas;
	}
}

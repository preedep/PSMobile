package com.epro.psmobile.sync;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.EmpAssignedInTeam;
import com.epro.psmobile.data.Employee;
import com.epro.psmobile.data.InspectDataGroupType;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.InspectType;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.JobRequestProductGroup;
import com.epro.psmobile.data.LicensePlate;
import com.epro.psmobile.data.MarketPrice;
import com.epro.psmobile.data.News;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductAmountUnit;
import com.epro.psmobile.data.ProductGroup;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.SubExpenseType;
import com.epro.psmobile.data.TableMaster;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskComplete;
import com.epro.psmobile.data.TaskFormTemplate;
import com.epro.psmobile.data.TaskResend;
import com.epro.psmobile.data.TaskStatus;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TransactionStmtHolder;
import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.SharedPreferenceUtil;

public class JSONToDBLoader {

	public JSONToDBLoader() {
		// TODO Auto-generated constructor stub
	}
	public static void loadJSONFileToDb(Context context,
			String jsonPath,
			OnInsertJSONToDBHandler onInsertJSONToDbHandler)
	{
		File jsonFolder = new File(jsonPath);
		String[] jsonFiles = jsonFolder.list(new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".json");
		    }
		});		
		
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);

		/*
         * query for backup
         */
		try{
		      ArrayList<JobRequestProduct> jobRequestsInTaskNoFinishs = 
		            dataAdapter.getAllJobRequestProductInTaskNoFinish();
        
		      if (jobRequestsInTaskNoFinishs != null){
		         /*
		          * delete in backup
		          */
		         dataAdapter.insertJobRequestProductBackup(jobRequestsInTaskNoFinishs);
		      }
		}catch(Exception ex){
		   ex.printStackTrace();
		}
        //////////////////
		for(String jsonFile : jsonFiles)
		{
			String tableName = jsonFile.replace(".json", "");
			Log.d("DEBUG_D", "json file = "+jsonFile);
			/*
			 * 
			 */
			String deleteData = "delete from "+ tableName;
			String jsonFilePath = jsonPath+"/"+jsonFile;
/*	
			try{
				FilePSMobileJSONFileReader.readFile(jsonFilePath, Product.class);
			}catch(Exception ex)
			{
				
			}*/
			
			Object objType = registerMapType().get(tableName.toLowerCase());
			try {
				if (objType != null){
					@SuppressWarnings("unchecked")				
					
					ArrayList<String> array_sqls = new ArrayList<String>();
					
					ArrayList<JSONDataHolder> objList = 
						(ArrayList<JSONDataHolder>) FilePSMobileJSONFileReader.readFile(jsonFilePath, 
								objType.getClass());
					
					String sqls[] = new String[objList.size()+1];// 1 + delete
					
					/*
					if (objType instanceof Task)
					{
						deleteData += " where "+Task.COLUMN_TASK_STATUS+" != "+TaskStatus.FINISH.getTaskStatus(); 
					}*/
					
					sqls[0] = deleteData;
					/*
					 * delete have condition
					 */
					/*
					 * execute sql statements and make transaction
					 */
					int maxObjectId = 0;
					
					
					if (objType instanceof InspectDataObjectSaved)
					{
						//sqls[0] += " where teamIDObjectOwner != "+ SharedPreferenceUtil.getTeamID(context);
						sqls[0] += " where addObjectFlag = 'N'";
						maxObjectId = dataAdapter.getMaxObjectId();
					}
					array_sqls.add(sqls[0]);
					
					/////////////
					Hashtable<Integer,Integer> tableJobRequestId = 
                          dataAdapter.tableJobRequestdInJobRequestProductBackup();
                    
					StringBuilder strBld = new StringBuilder();
					for(int i = 0;i < objList.size();i++)
					{
						JSONDataHolder dataHolder = objList.get(i);
						if (dataHolder instanceof InspectDataObjectSaved)
						{
							((InspectDataObjectSaved)dataHolder).setInspectDataObjectID(maxObjectId+(i+1));
							((InspectDataObjectSaved)dataHolder).setAddObjectFlag("N");
						}
						if (tableJobRequestId != null){
	                        if (dataHolder instanceof JobRequestProduct)
	                        {
	                           JobRequestProduct jrp = (JobRequestProduct)dataHolder;
	                           if (tableJobRequestId.containsKey(jrp.getJobRequestID())){
	                              continue;
	                           }
	                        }						   
						}
						if (dataHolder instanceof TransactionStmtHolder)
						{
							TransactionStmtHolder stmt = (TransactionStmtHolder)dataHolder;
							sqls[i+1] = stmt.insertStatement();
							array_sqls.add(sqls[i+1]);
							
							if (objType instanceof InspectDataObjectSaved)
							{
								strBld.append(stmt.insertStatement()+"\r\n");
							}
						}
					}
					
					
					
					Log.d("DEBUG_D", strBld.toString());
					
					
					String[] stmt_sqls = array_sqls.toArray(new String[array_sqls.size()]);
					int rowEffected = dataAdapter.executeStatements(stmt_sqls);
					Log.d("DEBUG_D", "insert data to Table ["+tableName+"] row effected : "+rowEffected);
					
					
					/*
                     * restore from backup
                     */
                    if (objType instanceof JobRequestProduct)
                    {
                       ArrayList<JobRequestProduct> jobRequestProductsInBackup =  
                             dataAdapter.getAllJobRequestProductBackup();
                       /*
                       Hashtable<Integer,Integer> tableJobRequestId = 
                             dataAdapter.tableJobRequestdInJobRequestProductBackup();
                       */
                       if (jobRequestProductsInBackup != null)
                       {
                          //String[] stmts = new String[jobRequestProductsInBackup.size()];
                          ArrayList<String> stmts = new ArrayList<String>();
                          for(int i = 0; i < jobRequestProductsInBackup.size();i++){
                             
                             JobRequestProduct jrp = jobRequestProductsInBackup.get(i);
                             /*
                             if (tableJobRequestId != null){
                                if (tableJobRequestId.containsKey(jrp.getJobRequestID())){
                                   continue;
                                }
                             }*/
                             stmts.add(jrp.insertStatement());
                          }
                          /*
                           */
                          String[] strStmts = stmts.toArray(new String[stmts.size()]);
                          dataAdapter.executeStatements(strStmts);
                       }
                    }
					if (onInsertJSONToDbHandler != null)
					{
						onInsertJSONToDbHandler.onInsertCompleted(rowEffected,
								jsonFile, 
								tableName);
					}
				}else{
					Log.d("DEBUG_D", "Table ["+tableName+"] not registered yet");
				}
			} catch(Exception ex)
			{
				Log.d("DEBUG_D","Read JSON File error -> "+ex.getMessage());
			}
		}
		
		
		/*
		 * replace old task by new task code from taskresend.json
		 */
		//PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);
		try{
		   
		    dataAdapter.clearCarInspectStampLocation();
		   
			ArrayList<TableMaster> tableMasters = dataAdapter.getAllTables();
			ArrayList<TaskResend> taskResends = dataAdapter.getTaskResendForUpdate();
			if ((taskResends != null)&&(taskResends.size() > 0))
			{
				for(TableMaster tm : tableMasters)
				{
					String[] sqls = new String[taskResends.size()+1];
					int idx = 0;
					for(TaskResend ts : taskResends)
					{
						sqls[idx++] = "update " +
								""+tm.getTableName()+" " +
										" set taskCode='"+ts.getTaskCode().trim()+"' " +
												"where taskCode='"+ts.getTaskCodeOld().trim()+"'";
					}
					
					try
					{
						int rowEffected = dataAdapter.executeStatements(sqls);
						Log.d("DEBUG_D", "Row effected : "+rowEffected+" , update to table["+tm.getTableName()+"]");
					}catch(Exception ex){}
				}				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
	@SuppressLint("DefaultLocale")
   public static Hashtable<String,JSONDataHolder> registerMapType(){
		Hashtable<String,JSONDataHolder> mapType = new Hashtable<String,JSONDataHolder>();
		mapType.put("product", new Product());
		mapType.put("productgroup", new ProductGroup());
		mapType.put("productamountunit", new ProductAmountUnit());
		mapType.put("customersurveysite", new CustomerSurveySite());
		mapType.put("marketprice", new MarketPrice());
		mapType.put("employee", new Employee());
		mapType.put("team", new Team());
		mapType.put("empassignedinteam", new EmpAssignedInTeam());
		mapType.put("inspecttype", new InspectType());
		mapType.put("news", new News());
		mapType.put("inspectdataitem", new InspectDataItem());
		mapType.put("inspectdatagrouptype", new InspectDataGroupType());
		mapType.put("reasonsentence", new ReasonSentence());
		mapType.put("task", new Task());
		mapType.put("jobrequest", new JobRequest());
		mapType.put("taskformtemplate", new TaskFormTemplate());
		mapType.put("taskresend", new TaskResend());
		mapType.put("taskcomplete", new TaskComplete());
		mapType.put("jobrequestproduct", new JobRequestProduct());
		mapType.put("subexpensetype", new SubExpenseType());
		mapType.put("licenseplate", new LicensePlate());
		mapType.put("jobrequestproductgroup", new JobRequestProductGroup());
		mapType.put("inspectdataobjectsaved", new InspectDataObjectSaved());
		mapType.put("UniversalJobMapper".toLowerCase(),new InspectJobMapper());
		mapType.put("UniversalListFormView".toLowerCase(), new InspectFormView());
		return mapType;
	}
}

package com.epro.psmobile.da;

import java.io.IOException;
import java.io.InputStream;

import com.epro.psmobile.util.SharedPreferenceUtil;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataMasterHelper extends SQLiteOpenHelper {

	public final static String DATABASE_NAME = "PSMobile";
	
	public static final int DATABASE_VERSION = 251;  /*product is 246*/
	                                                 /*product is 248 , version code 170*/
	                                                 /*product is 249 , version code 173*/
	                                                 /*product is 250 , version code 174*/
	public static final String ASSET_CREATE_TABLE_SQL_FILE = "tables.sql";
	
	public static final String ASSET_CREATE_TABLE_SQL_FILE_247 = "alter_tables_247.sql";
    public static final String ASSET_CREATE_TABLE_SQL_FILE_248 = "alter_tables_248.sql";
    public static final String ASSET_CREATE_TABLE_SQL_FILE_249 = "alter_tables_249.sql";
	
    public static final String ASSET_CREATE_TABLE_SQL_FILE_250 = "alter_tables_250.sql";

    public static final String ASSET_CREATE_TABLE_SQL_FILE_251 = "alter_tables_251.sql";

	private Context context;
   
	
	public DataMasterHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public DataMasterHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE,false);
		executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_247,false);
	    executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_248,false);
	    executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_249,false);
	    executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_250,false);
        executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_251,false);/*car inspection keep backup , restore*/
	    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	   /*
	   if (oldVersion == 246)//production 4-04-2014
	   {
	      switch(newVersion)
	      {
	         case 248:
	         {
	            executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_247,true);                
                executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_248,true);
                executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_249,false);
                executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_250,false);
	         }break;
	      }
	   }
	   else if (oldVersion == 248)//product is 248 , version code 170
	   {
	      switch(newVersion){
	         case 249:{
	            executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_249,false);
	         }break;
	      }
	   }else
	      */
	   if (oldVersion == 249)
	   {
	      switch(newVersion){
	         case 250:{
                executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_250,false);	            
	         }break;
	      }
	   }else if (oldVersion == 250){
	      switch(newVersion){
	         case 251:{
                executeSqlFile(db,ASSET_CREATE_TABLE_SQL_FILE_251,false);               	            
	         }break;
	      }
	   }
	}

	private void executeSqlFile(SQLiteDatabase db,String fileSql,boolean isUpgraded) 
	{
		try {
			DbUtils.executeSqlScript(context, db, fileSql,true);
			/**
			 */
			if (!isUpgraded){
			   SharedPreferenceUtil.setStateTeamCheckIn(context, false);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR_E", e.getMessage());
		}
	}
}

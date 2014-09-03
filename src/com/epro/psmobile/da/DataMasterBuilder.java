package com.epro.psmobile.da;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.epro.psmobile.R;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataMasterBuilder 
{

	@SuppressWarnings("unused")
	private DataMasterHelper helper;
	private SQLiteDatabase db;
	private Context context;
	public DataMasterBuilder(Context ctxt) {
		// TODO Auto-generated constructor stub
		helper = new DataMasterHelper(ctxt,DataMasterHelper.DATABASE_NAME,null,DataMasterHelper.DATABASE_VERSION);
		this.context = ctxt;
	}
	
	public SQLiteDatabase open() throws SQLException {
		db = helper.getWritableDatabase();		
		return db;
	}
	public void close(){
		if (db != null)
		{
			db.close();
		}
	}
	public void loadDataTest(){
		SQLiteDatabase db = null;
		try{
			db = open();
			DbUtils.executeSqlScript(context, db, "tables.sql",true);
			//DbUtils.executeSqlScript(context, db, "expense.sql",true);
			DbUtils.executeSqlStatementsInTx(db, context.getResources().getStringArray(R.array.sql_expense_master_arrays));
		}catch(Exception ex){
		   Log.d("DEBUG_D", "Error : "+ex.getMessage());
		}finally{
			if (db != null)
				db.close();
		}
	}	
	public static void dumpAppDB(Context ctxt,String outputPath)
	{
		String DB_PATH = "/data/data/" + ctxt.getPackageName() + "/databases/";
		File f = new File(DB_PATH);
		if (f.isDirectory()){
			File[] fList = f.listFiles();
			if (fList != null)
			{
				for(File fDb : fList)
				{
					if (fDb.isFile()){
						FileInputStream fin = null;
						FileOutputStream fout = null;
						try{
							fin = new FileInputStream(fDb);
							byte data[] = new byte[fin.available()];
							fin.read(data);
							
//							fout = new FileOutputStream(outputPath+"/"+DataMasterHelper.DATABASE_NAME+".db");
							String fileDb = outputPath+"/"+fDb.getName()+".db";
							fout = new FileOutputStream(fileDb);

							fout.write(data);
							fout.flush();
						}catch(Exception ex)
						{
							Log.d("ERROR", ex.getMessage());
						}finally{
							if (fout != null)
								try {
									fout.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							if (fin != null)
								try {
									fin.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
						}
					}
				}
			}

		}
	}
}

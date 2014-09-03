package com.epro.psmobile.util;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Task;

public class ReportInspectSummaryStatusHelper {

   public ReportInspectSummaryStatusHelper() {
      // TODO Auto-generated constructor stub
   }

   public static void resetReportStatus(Context context,Task currentTask)
   {
      /*
       * delete report
       */
      File root = android.os.Environment.getExternalStorageDirectory();
      // See
      // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
      File dir = new File(root.getAbsolutePath() + CommonValues.REPORT_FOLDER);
      if (!dir.exists()){
          dir.mkdirs();
      }
      /*
       * create file
       */
      if (currentTask != null){
          String fileName = DataUtil.regenerateTaskCodeForMakeFolder(currentTask.getTaskCode());                  
          fileName = fileName + ".html";
          File file = new File(dir, fileName);
          if (file.exists()){
              file.delete();
              Log.d("DEBUG_D", "File -> "+fileName+" deleted!!!");
          }
      }
      try
      {
        // if (currentTask.isInspectReportGenerated())
         {
            Log.d("DEBUG_D", "Report generated!!!!");
            PSBODataAdapter dataAdapter = 
                  PSBODataAdapter.getDataAdapter(context);
            
               dataAdapter.updateTaskIsReportGenerated(currentTask.getTaskCode(),
                  false,
                  currentTask.getTaskDuplicatedNo());              
         }
      }catch(Exception ex){
         Log.d("DEBUG_D", "Error[updateTaskIsReportGenerated] -> "+ex.getMessage());
         ex.printStackTrace();
      }
      
      currentTask.setInspectReportGenerated(false);
   }
}

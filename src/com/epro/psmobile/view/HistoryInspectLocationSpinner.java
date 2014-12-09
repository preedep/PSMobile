package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class HistoryInspectLocationSpinner extends Spinner {

   public HistoryInspectLocationSpinner(Context context) {
      super(context);
      // TODO Auto-generated constructor stub
   }

   public HistoryInspectLocationSpinner(Context context, int mode) {
      super(context, mode);
      // TODO Auto-generated constructor stub
   }

   public HistoryInspectLocationSpinner(Context context, AttributeSet attrs) {
      super(context, attrs);
      // TODO Auto-generated constructor stub
   }

   public HistoryInspectLocationSpinner(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      // TODO Auto-generated constructor stub
   }

   public HistoryInspectLocationSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
      super(context, attrs, defStyle, mode);
      // TODO Auto-generated constructor stub
   }
   public void initial(final int jobRequestID)
   {
      Thread t = new Thread(new Runnable(){

         @Override
         public void run() {
            // TODO Auto-generated method stub
            try{
               PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
               ArrayList<CarInspectStampLocation> historyStampLocs = 
                     dataAdapter.getAllCarInspectStampLocation(jobRequestID);
               /*
               ArrayAdapter adapter = new ArrayAdapter();
               */
               if (historyStampLocs != null){
                  CarInspectStampLocation tmp = historyStampLocs.get(0);
                  
                  CarInspectStampLocation firstRowEmpty = new CarInspectStampLocation();
                  firstRowEmpty.setCustomerSurveySiteID(0);
                  firstRowEmpty.setSiteAddress("");
                  firstRowEmpty.setTimeRecorded("");
                  firstRowEmpty.setJobRequestID(tmp.getJobRequestID());
                  firstRowEmpty.setTaskDuplicateNo(tmp.getTaskDuplicateNo());
                  firstRowEmpty.setMilesNo("");
                  firstRowEmpty.setTaskCode(tmp.getTaskCode());
                  firstRowEmpty.setTimeRecorded("");
                  
                  ArrayList<CarInspectStampLocation> tmp_carInspectList = new ArrayList<CarInspectStampLocation>();
                  tmp_carInspectList.add(firstRowEmpty);
                  tmp_carInspectList.addAll(historyStampLocs);
                  
                  final ArrayAdapter<CarInspectStampLocation> adapter = new ArrayAdapter<CarInspectStampLocation>(getContext(),
                     android.R.layout.simple_spinner_item,tmp_carInspectList);
                  
                  Activity activity = (Activity)getContext();
                  activity.runOnUiThread(new Runnable(){

                     @Override
                     public void run() {
                        // TODO Auto-generated method stub
                        setAdapter(adapter);
                     }
                     
                  });
                  
                 
               }   
            }catch(Exception ex)
            {
               ex.printStackTrace();
            }
         }
         
      });
      t.start();
      
   }

}

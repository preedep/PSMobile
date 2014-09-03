package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.data.Product;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class InspectReportListColumnSpinner extends Spinner {

   public InspectReportListColumnSpinner(Context context) {
      super(context);
      // TODO Auto-generated constructor stub
   }

   public InspectReportListColumnSpinner(Context context, int mode) {
      super(context, mode);
      // TODO Auto-generated constructor stub
   }

   public InspectReportListColumnSpinner(Context context, AttributeSet attrs) {
      super(context, attrs);
      // TODO Auto-generated constructor stub
   }

   public InspectReportListColumnSpinner(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      // TODO Auto-generated constructor stub
   }

   public InspectReportListColumnSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
      super(context, attrs, defStyle, mode);
      // TODO Auto-generated constructor stub
   }

   public void initial()
   {
      String columnNames[] = this.getContext().getResources().getStringArray(R.array.columns_car_inspect_summary_no);
//      String columnNameWithoutCamera[] = new String[columnNames.length-1];
      ArrayList<String> columnsFilter = new ArrayList<String>();
      for(int i = 0; i < columnNames.length;i++)
      {
         if (!columnNames[i].isEmpty()){
            columnsFilter.add(columnNames[i]);
         }
      }
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this.getContext(),android.R.layout.simple_spinner_item,
            columnsFilter);

      this.setAdapter(adapter);
   }
}

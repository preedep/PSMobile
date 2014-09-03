/**
 * 
 */
package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.InspectDataObjectSaved;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author preedeeponchevin
 *
 */
public class LayoutSpinner extends Spinner {

   private ArrayList<InspectDataObjectSaved> inspectDataObjectSavedList;
   private int oldCustomerSurveySiteID; 
   /**
    * @param context
    */
   public LayoutSpinner(Context context) {
      super(context);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param context
    * @param mode
    */
   public LayoutSpinner(Context context, int mode) {
      super(context, mode);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param context
    * @param attrs
    */
   public LayoutSpinner(Context context, AttributeSet attrs) {
      super(context, attrs);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param context
    * @param attrs
    * @param defStyleAttr
    */
   public LayoutSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param context
    * @param attrs
    * @param defStyleAttr
    * @param mode
    */
   public LayoutSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
      super(context, attrs, defStyleAttr, mode);
      // TODO Auto-generated constructor stub
   }
   
   public void initial(String taskCode,
         int customerSurveySiteID
         ){
      initial(taskCode,customerSurveySiteID,false);
   }
   
   public void initial(String taskCode,
         int customerSurveySiteID,
         boolean reload)
   {
      PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
      
      final ArrayList<InspectDataObjectSaved> layoutList = new ArrayList<InspectDataObjectSaved>();
      layoutList.add(new InspectDataObjectSaved());
     
      /*
       * 
       */
      //if (customerSurveySiteID <= 0)return;
      
      if (this.oldCustomerSurveySiteID != customerSurveySiteID){
         oldCustomerSurveySiteID = customerSurveySiteID;
         reload = true;
      }
      try {
         if (reload)
         {
            inspectDataObjectSavedList = 
                  dataAdapter.getInspectDataObjectSavedUniverals(taskCode, customerSurveySiteID);
            Log.d("DEBUG_D_D", "Layout Spinner Reload data");
         }else{
            if ((inspectDataObjectSavedList == null)||(inspectDataObjectSavedList.size() == 0))
            {
               inspectDataObjectSavedList = 
                     dataAdapter.getInspectDataObjectSavedUniverals(taskCode, customerSurveySiteID);               
            }
         }
         ////////////////////
         
         
         if (inspectDataObjectSavedList != null)
         {
            layoutList.addAll(inspectDataObjectSavedList);
         }
         
         ArrayAdapter<InspectDataObjectSaved> adapter = new ArrayAdapter<InspectDataObjectSaved>(this.getContext(),
               android.R.layout.simple_spinner_item,layoutList){

                  /* (non-Javadoc)
                   * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                   */
                  @Override
                  public View getView(int position, View convertView, ViewGroup parent) {
                     // TODO Auto-generated method stub
                     View v =  super.getView(position, convertView, parent);
                     TextView tvText = (TextView)v.findViewById(android.R.id.text1);
                     
                     InspectDataObjectSaved objSaved = layoutList.get(position);

                     if (position == 0){
                        tvText.setText("");
                     }else{
                        tvText.setText(objSaved.getInspectDataObjectID()+". "+objSaved.getObjectName());
                     }
                     
                     v.setTag(objSaved);
                     return v;
                  }               
         };
            
        this.setAdapter(adapter);

      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}

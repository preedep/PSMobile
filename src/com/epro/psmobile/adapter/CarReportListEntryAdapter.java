package com.epro.psmobile.adapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.epro.psmobile.R;
import com.epro.psmobile.adapter.callback.OnAddAndRemoveContextMenuListener;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.adapter.filter.BaseFilterable;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.form.xml.ReportListEntryColumn;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.ReportInspectSummaryStatusHelper;
import com.epro.psmobile.util.ResourceValueUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.view.HistoryInspectLocationSpinner;
import com.epro.psmobile.view.LayoutSpinner;
import com.epro.psmobile.view.ReasonSentenceSpinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.SpinnerAdapter;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TimePicker;

public class CarReportListEntryAdapter extends BaseAdapter implements  Filterable {

    public enum FieldTextChange
    {
      MILE_NO,
      REMARK
    };
    public class TextChangeListener implements TextWatcher
    {
      
      public JobRequestProduct jobProductRequest;
      public EditText edtText;
      public FieldTextChange fieldTextChange = FieldTextChange.MILE_NO;

      @Override
      public void afterTextChanged(Editable s) {
         // TODO Auto-generated method stub
         if (jobProductRequest != null)
         {
            switch(fieldTextChange){
               case MILE_NO:{
                 jobProductRequest.setcKms(s.toString());
                // edtText.setText(jobProductRequest.getcKms());
               }break;
               case REMARK:{
                 jobProductRequest.setcRemark(s.toString());
                 // Log.d("DEBUG_D", jobProductRequest.getcVin()+" -> "+s.toString());
                // edtText.setText(jobProductRequest.getcRemark());
               }break;
            }
         }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
         // TODO Auto-generated method stub
         
      }
       
    }
    public static class Holder
    {
      TextView tvOrder;
      TextView tvMid;
      //TextView tvModel;
      TextView tvDesc;
      //TextView tvVin;
      //TextView tvColor;
      TextView tvEngine;
      RadioButton radioHasCar;
      RadioButton radioNoHasCar;
      CheckBox chkSold;
      CheckBox chkPay;
      Button edtDate;
      CheckBox chkDeviceSetup;
      CheckBox chkHasDocument;
      ReasonSentenceSpinner reasonSentenceSpinner;
      EditText edtCarMiles;
      EditText edtRemark;
      HistoryInspectLocationSpinner historyLocationSpinner;
      Button btnAddAndRemove;
      Button btnTakeCamera;
      TextChangeListener textMileNoChangeListener;//new TextChangeListener(); 
      TextChangeListener textRemarkChangeListener;
      AlertDialog alertDialog;
      
      JobRequestProduct tmpRequestProduct;
      LayoutSpinner layoutSpinner;
      
      Runnable rCallbackLayoutSpinner;
    };
	private Context context = null;
	private LayoutInflater inflater;
	private CarFilter carFilter;
	private ArrayList<JobRequestProduct> jobRequestProducts;
	private ArrayList<JobRequestProduct> originalList;
	private Task currentTask;
	private String[] columnHeaders = null;
	private OnTakeCameraListener<JobRequestProduct> onTakeCameraListener;
	private OnAddAndRemoveContextMenuListener<JobRequestProduct> onShowContextMenu;
	private CustomerSurveySite site;
	//private boolean bDatePickerShown = false;
	
	private Hashtable<String,ArrayList<JobRequestProduct>> vinTable = null;
	
	//private static View[] view_arrays = null;
	public CarReportListEntryAdapter(Context context,
	      Task currentTask,
	      CustomerSurveySite site,
	      ArrayList<JobRequestProduct> jobRequestProducts) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.jobRequestProducts = jobRequestProducts;
		this.originalList = jobRequestProducts;
		this.currentTask = currentTask;
		this.site = site;
		
		columnHeaders = context.getResources().getStringArray(R.array.columns_car_inspect_summary_no_multi_row);
		
		reloadVinTable();
		
		//view_arrays = new View[jobRequestProducts.size()];
	}
	public void reloadVinTable(){
       
       vinTable = new Hashtable<String,ArrayList<JobRequestProduct>>();
       
       for(JobRequestProduct jrp : originalList){
          if (!vinTable.containsKey(jrp.getcVin())){
             ArrayList<JobRequestProduct> tmpJobRequestProductList = new  ArrayList<JobRequestProduct>();
             tmpJobRequestProductList.add(jrp);
             vinTable.put(jrp.getcVin(), tmpJobRequestProductList);
          }else{
             vinTable.get(jrp.getcVin()).add(jrp);
          }
       }
	   
	}
	public void deleteRowAt(JobRequestProduct rowAt){
	   jobRequestProducts.remove(rowAt);
	   /*
	    * remove from original
	    */
	   JobRequestProduct tmp = null;
	   for(JobRequestProduct jp : originalList)
	   {
	      if (jp.getcTeamId() == rowAt.getcTeamId()){
	        if (jp.getcJobRowId() == rowAt.getcJobRowId()){
	           if (jp.getcVin().equalsIgnoreCase(rowAt.getcVin())){
	              tmp = jp;
	              break;
	           }
	        }
	      }
	   }
	   if (tmp != null)
	   {
	      originalList.remove(tmp);
	   }
	   //this.notifyDataSetChanged();
	   //this.notifyDataSetInvalidated();
	   
	   
	   reloadVinTable();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jobRequestProducts.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return jobRequestProducts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    View v = null;
	    final JobRequestProduct requestProduct = (JobRequestProduct)getItem(position);
	    
	       
	    if (convertView == null)
	    {
	       v = inflater.inflate(R.layout.ps_activity_report_list_entry_row, parent, false);
	       //view_arrays[position] = v;
	       
	       ViewGroup vContainer = (ViewGroup)v.findViewById(R.id.ll_report_list_entry_row_containers);
	       Holder holder = new Holder();
	       for(int i = 0;i < columnHeaders.length;i++)
	       {
	          switch(i)
	          {
	             case 0:
	             case 1:
	             case 2:
	             case 3:
	             {
	                View vText = inflater.inflate(R.layout.ps_activity_report_list_entry_column_textview, null, false);
	                vContainer.addView(vText);
	                if (i == 0){
                       holder.tvOrder = (TextView)vText.findViewById(R.id.et_report_list_entry_column_textview);
                       holder.tvOrder.getLayoutParams().width = (int) getColumnWidth(context,i);
                       holder.tvOrder.setGravity(Gravity.CENTER);
	                }else
	                if (i == 1){
	                   holder.tvMid = (TextView)vText.findViewById(R.id.et_report_list_entry_column_textview);
	                   holder.tvMid.getLayoutParams().width = (int) getColumnWidth(context,i);
	                }else if (i == 2){
                       holder.tvDesc = (TextView)vText.findViewById(R.id.et_report_list_entry_column_textview);
                       holder.tvDesc.getLayoutParams().width = (int) getColumnWidth(context,i);
	                }else if (i == 3){
                       holder.tvEngine = (TextView)vText.findViewById(R.id.et_report_list_entry_column_textview);
                       holder.tvEngine.getLayoutParams().width = (int) getColumnWidth(context,i);	 
                       holder.tvEngine.setGravity(Gravity.CENTER);
	                }
	             }break;
	             case 4:{
	                View vRadioGroup = inflater.inflate(R.layout.ps_activity_report_list_entry_column_group_rdobox, null, false);
	                ViewGroup vg = (ViewGroup)vRadioGroup.findViewById(R.id.rdo_group_chk_car);
	                vg.getLayoutParams().width = (int)getColumnWidth(context,i);
	                
	                holder.radioHasCar = (RadioButton)vRadioGroup.findViewById(R.id.chkbox_report_list_entry_column_chkbox_has);
	                holder.radioNoHasCar = (RadioButton)vRadioGroup.findViewById(R.id.chkbox_report_list_entry_column_chkbox_no_has);
	                
	                //holder.radioHasCar.setChecked(false);
	                //holder.radioNoHasCar.setChecked(false);
	                
	                vContainer.addView(vRadioGroup);
	             }break;
	             case 5:
	             case 6:
	             case 8:
	             case 9:
	             {
	                View vChkBok = inflater.inflate(R.layout.ps_activity_report_list_entry_column_chkbox, null, false);
	                if (i == 5){
	                   holder.chkSold = (CheckBox)vChkBok.findViewById(R.id.chkbox_report_list_entry_column_chkbox);
	                   holder.chkSold.getLayoutParams().width = (int)getColumnWidth(context,i);
	                   holder.chkSold.setGravity(Gravity.CENTER);
	                }else if (i == 6)
	                {
                       holder.chkPay = (CheckBox)vChkBok.findViewById(R.id.chkbox_report_list_entry_column_chkbox);
                       holder.chkPay.getLayoutParams().width = (int)getColumnWidth(context,i);	                   
                       holder.chkPay.setGravity(Gravity.CENTER);

	                }else if (i == 8){
                       holder.chkDeviceSetup = (CheckBox)vChkBok.findViewById(R.id.chkbox_report_list_entry_column_chkbox);
                       holder.chkDeviceSetup.getLayoutParams().width = (int)getColumnWidth(context,i);                     
                       holder.chkDeviceSetup.setGravity(Gravity.CENTER);

	                }else if (i == 9){
                       holder.chkHasDocument = (CheckBox)vChkBok.findViewById(R.id.chkbox_report_list_entry_column_chkbox);
                       holder.chkHasDocument.getLayoutParams().width = (int)getColumnWidth(context,i);                     
                       holder.chkHasDocument.setGravity(Gravity.CENTER);

	                }
	                vContainer.addView(vChkBok);
	             }break;
	             case 10:{
	                /*spinner*/
	                View vSpinner = 
                          inflater.inflate(R.layout.ps_activity_report_list_entry_column_reason_spinner, null, false);
	                ReasonSentenceSpinner sp_reasonSentence = (ReasonSentenceSpinner)vSpinner.findViewById(R.id.sp_reason_sentence);
	                sp_reasonSentence.getLayoutParams().width = (int)getColumnWidth(context,i);
	                
	                PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);
	                String reasonCode = requestProduct.getcReasonCode();
	                
	                try 
	                {
	                   ArrayList<ReasonSentence> reasonSentences = dataAdapter.getAllReasonSentenceByType(reasonCode);
	                   if (reasonSentences != null)
	                   {
	                      ArrayList<ReasonSentence> reasonSentences_empty_first_row = new ArrayList<ReasonSentence>();
	                      ReasonSentence firstEmpty = new ReasonSentence();
	                      firstEmpty.setReasonText("");
	                      reasonSentences_empty_first_row.add(firstEmpty);
	                      reasonSentences_empty_first_row.addAll(reasonSentences);
	                      
	                      sp_reasonSentence.initialWithReasonList(reasonSentences_empty_first_row);
	                   }
                  } 
                  catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
	                
//	                sp_reasonSentence.initialWithReasonList(reasonList)
	                holder.reasonSentenceSpinner = sp_reasonSentence;
	                
	                vContainer.addView(vSpinner);
	             }break;
	             case 11:
	             case 12:
	             {
                    View vEdit = 
                          inflater.inflate(R.layout.ps_activity_report_list_entry_column_text, null, false);
                    EditText edt = (EditText)vEdit.findViewById(R.id.et_report_list_entry_column_text);
                    edt.getLayoutParams().width = (int)getColumnWidth(context,i);
                    
                    if (i == 11){
                       holder.edtCarMiles = edt;
                       holder.edtCarMiles.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }else if (i == 12){
                       holder.edtRemark = edt;
                    }
                    vContainer.addView(vEdit);
	             }break;
	             case 13:{
	                View vSpinner = 
                          inflater.inflate(R.layout.ps_activity_report_list_entry_column_history_location_spinner, null, false);
	                HistoryInspectLocationSpinner sp_historyLocation = (HistoryInspectLocationSpinner)vSpinner.findViewById(R.id.sp_history_inspect_location);
	                sp_historyLocation.getLayoutParams().width = (int)getColumnWidth(context,i);
                    
                    //String reasonCode = requestProduct.getcReasonCode();
                    
                   try 
                   {
                      sp_historyLocation.initial(requestProduct.getJobRequestID(),requestProduct.getJobNo());
                      /*
                      for(int i_history = 0;i_history < sp_historyLocation.getCount();i_history++)
                      {
                         Object obj = sp_historyLocation.getItemAtPosition(i_history);  
                         if (obj instanceof CarInspectStampLocation){
                            CarInspectStampLocation history = (CarInspectStampLocation)obj;
                            if (history.getSiteAddress().equalsIgnoreCase(site.getSiteAddress())){
                               sp_historyLocation.setSelection(i_history);
                               break;
                            }
                         }
                      }*/
                   } 
                   catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
                    holder.historyLocationSpinner = sp_historyLocation;
                    vContainer.addView(vSpinner);
	             }break;
	             case 14:{
	                /*layout */
	                View vSpinner = 
                          inflater.inflate(R.layout.ps_activity_report_list_entry_column_layout_spinner, null, false);
                    LayoutSpinner sp_layout = (LayoutSpinner)vSpinner.findViewById(R.id.sp_layout_inspect);
                    sp_layout.getLayoutParams().width = (int)getColumnWidth(context,i);
                   try 
                   {
                      if (requestProduct.getcWareHouse() > 0){
                         sp_layout.initial(this.currentTask.getTaskCode(),
                               requestProduct.getcWareHouse());                         
                      }else{
                      sp_layout.initial(this.currentTask.getTaskCode(),
                            this.site.getCustomerSurveySiteID());
                      }
                   } 
                   catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
                    holder.layoutSpinner = sp_layout;
                    vContainer.addView(vSpinner);
	             }break;
	             case 7:	                
	             case 15:
	             case 16:{
	                View vCamera = 
                          inflater.inflate(R.layout.ps_activity_report_list_entry_column_camera, null, false);
                    Button btn = (Button)vCamera.findViewById(R.id.btn_report_list_entry_column_camera);
                    btn.getLayoutParams().width = (int)getColumnWidth(context,i);

                    if (i == 7){
                       holder.edtDate = btn;
                       holder.edtDate.setText("");
                       holder.edtDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    }else
                    if (i == 15){
                       holder.btnAddAndRemove = btn;
                       holder.btnAddAndRemove.setText("+/-");
                    }else
                    if (i == 16){
                       holder.btnTakeCamera = btn;
                       holder.btnTakeCamera.setText("");
                       holder.btnTakeCamera.setCompoundDrawablesWithIntrinsicBounds(
                             R.drawable.ic_device_access_camera,0,0,0);
                       //holder.btnTakeCamera.setBackgroundResource(R.drawable.ic_device_access_camera);
                    }
                    vContainer.addView(vCamera);
	             }break;
	          }
	       }
	       v.setTag(holder);
	       
	       setEnable(holder,false);
	       
	       Log.d("DEBUG_D_D", "pos -> "+position);
	    }
	    else{
	       v = convertView;
	       //v = view_arrays[position];
	       
	       Log.d("DEBUG_D_D", "created pos -> "+position);
	    }
	    
	    final Holder holder = (Holder)v.getTag();
	    
	    
	    holder.layoutSpinner.setTag(null);
	    holder.historyLocationSpinner.setTag(null);
	       
	       
	    holder.layoutSpinner.setTag(requestProduct);
	    holder.historyLocationSpinner.setTag(requestProduct);

	    //setEnable(holder,false);
	    
	    holder.tmpRequestProduct = requestProduct;
	    
        if (holder.textMileNoChangeListener != null)
           holder.edtCarMiles.removeTextChangedListener(holder.textMileNoChangeListener);
        
        if (holder.textRemarkChangeListener != null)
           holder.edtRemark.removeTextChangedListener(holder.textRemarkChangeListener);
	    
        
	    
	   	final View vContainer = v;  

	   	vContainer.setBackgroundResource(android.R.color.white);
	   	
	   	int sizeCountVin =  vinTable.get(requestProduct.getcVin()).size();
        if (sizeCountVin > 1)
        {
           requestProduct.setcErrorType(JobRequestProduct.ROW_RESULT_ERROR);
        }
        

	   //////////////
	  
	    holder.reasonSentenceSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

	       @Override
	       public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            // TODO Auto-generated method stub
	           Object obj = parent.getItemAtPosition(pos);
	           if (obj instanceof ReasonSentence){
	              ReasonSentence reason = (ReasonSentence)obj;
	              requestProduct.setcReasonID(reason.getReasonID());
                  //setWareHouseId(requestProduct);
	              setDataModifled();
	           }
	       }
	       @Override
	       public void onNothingSelected(AdapterView< ? > arg0) {
            // TODO Auto-generated method stub
            
	       }
	       
	    });
	   
	    holder.chkPay.setOnCheckedChangeListener(new OnCheckedChangeListener(){
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
              if (isChecked)
              {
                 requestProduct.setcPay("Y");
                 requestProduct.setcSold("N");
                 requestProduct.setcEquipping("N");
                 
             
                 //holder.reasonSentenceSpinner.setSelection(1);
                 //holder.reasonSentenceSpinner.setEnabled(false);

                 /*
                 Object obj = holder.reasonSentenceSpinner.getSelectedItem();
                 if (obj instanceof ReasonSentence)
                 {
                    ReasonSentence rs = (ReasonSentence)obj;
                    requestProduct.setcReasonID(rs.getReasonID());
                 }*/
                 
                 holder.edtDate.setEnabled(true);
                 holder.chkHasDocument.setEnabled(true);
                 holder.edtRemark.setEnabled(true);
                 //holder.chkDeviceSetup.setEnabled(false);                 
                 
                 holder.chkSold.setChecked(false);
                 holder.chkDeviceSetup.setChecked(false);

                 if (requestProduct.getcDate() != null){
                    holder.edtDate.setText(
                          DataUtil.convertToDisplayCarInspectDateFormat(requestProduct.getcDate()));
                   }
                 
                 setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
                 
              }else
              {
                 requestProduct.setcPay("N");
                 
                 //holder.chkHasDocument.setEnabled(false);
                 holder.edtRemark.setEnabled(true);
                 
                 
                 //requestProduct.setcDate("");                 
                 //holder.edtDate.setText("");

                 
                 /*
                  * fix 9-08-2014
                  */
                 //if (!holder.chkSold.isChecked())
                 if (!requestProduct.getcSold().equalsIgnoreCase("Y"))
                 {
                    holder.chkDeviceSetup.setEnabled(true);
                    holder.edtDate.setEnabled(false);
                    
                    /*
                     * fix 9-08-2014
                     */
                    //requestProduct.setcReasonID(0);
                    SpinnerAdapter adapter = holder.reasonSentenceSpinner.getAdapter();
                    if (adapter != null){
                         ReasonSentence rs =  (ReasonSentence)adapter.getItem(1);
                         if (requestProduct.getcReasonID() == rs.getReasonID()){
                            requestProduct.setcReasonID(0);                            
                         }
                    }
                    /*
                    holder.reasonSentenceSpinner.setSelection(0);
                    holder.reasonSentenceSpinner.setEnabled(true);*/
                    
                    setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
                 }
                 

                 /*
                 if ((!holder.chkPay.isChecked())&&(!holder.chkSold.isChecked()))
                 {
                    holder.reasonSentenceSpinner.setSelection(0);
                    holder.reasonSentenceSpinner.setEnabled(false);                      
                 }*/
                 setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);

                 
                 if ((requestProduct.getcPay().equalsIgnoreCase("N")&&
                     (requestProduct.getcSold().equalsIgnoreCase("N"))))
                    {
                    requestProduct.setcDate("");                 
                    holder.edtDate.setText(""); 
                 }
                 
                 if (requestProduct.getcSight().equalsIgnoreCase("Y")){
                    holder.chkPay.setEnabled(false);
                    holder.chkSold.setEnabled(false);
                    holder.chkDeviceSetup.setEnabled(false);
                    
                    requestProduct.setcDate("");
                    holder.edtDate.setText("");   
                 }


 
              }
              setDataModifled();
           }           
        });
	    //////
	    holder.chkSold.setOnCheckedChangeListener(new OnCheckedChangeListener(){
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
              if (isChecked){
                 requestProduct.setcSold("Y");
                 requestProduct.setcPay("N");
                 requestProduct.setcEquipping("N");
                
                 //if (holder.chkSold.isChecked() || holder.chkPay.isChecked()){
                    
            
//                    holder.reasonSentenceSpinner.setSelection(1);
//                    holder.reasonSentenceSpinner.setEnabled(false);
  
                 setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);

                    /*
                    Object obj = holder.reasonSentenceSpinner.getSelectedItem();
                    if (obj instanceof ReasonSentence)
                    {
                       ReasonSentence rs = (ReasonSentence)obj;
                       requestProduct.setcReasonID(rs.getReasonID());
                    }*/

                    holder.edtDate.setEnabled(true);
                    holder.chkHasDocument.setEnabled(true);
                    holder.edtRemark.setEnabled(true);
                    //holder.chkDeviceSetup.setEnabled(false);
                    
                    holder.chkPay.setChecked(false);
                    holder.chkDeviceSetup.setChecked(false);
                    
                    if (requestProduct.getcDate() != null){
                       holder.edtDate.setText(
                             DataUtil.convertToDisplayCarInspectDateFormat(requestProduct.getcDate()));
                      }

                 //}
              }else{
                 requestProduct.setcSold("N");

                 //holder.chkHasDocument.setEnabled(false);
                 holder.edtRemark.setEnabled(true);
                 
                 
                 //requestProduct.setcReasonID(0);
                 //holder.reasonSentenceSpinner.setSelection(0);/*to empty*/
                 
                 //requestProduct.setcDate("");                 
                 //holder.edtDate.setText("");

                 //String cPay = (requestProduct.getcPay() == null)?"N":requestProduct.getcPay();
                 //String cSold = (requestProduct.getcSold() == null)?"N":requestProduct.getcSold();
               
                 
                 /*
                  *   Fix 09-08-2014
                  */
                 //if (!holder.chkPay.isChecked())
                 if (!requestProduct.getcPay().equalsIgnoreCase("Y"))
                 {
                    holder.chkDeviceSetup.setEnabled(true);
                    holder.edtDate.setEnabled(false);
                                      
                    
                    /*
                     * fix 09-08-2014
                     */
                    SpinnerAdapter adapter = holder.reasonSentenceSpinner.getAdapter();
                    if (adapter != null){
                         ReasonSentence rs =  (ReasonSentence)adapter.getItem(1);
                         if (requestProduct.getcReasonID() == rs.getReasonID()){
                            requestProduct.setcReasonID(0);                            
                         }
                    }
                    /*
                    if (requestProduct.getcReasonID() == 1){
                       //clear show show
                       requestProduct.setcReasonID(0);
                    }*/
                    
                    //holder.reasonSentenceSpinner.setSelection(0);
                    //holder.reasonSentenceSpinner.setEnabled(true);
                    setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);

                 }
                 
                // requestProduct.setcDate("");
                // holder.edtDate.setText("");                   
/*                 
                 if ((!holder.chkPay.isChecked())&&(!holder.chkSold.isChecked()))
                 {
                    holder.reasonSentenceSpinner.setSelection(0);
                    holder.reasonSentenceSpinner.setEnabled(false);  
                    
//                    requestProduct.setcDate("");                 
//                    holder.edtDate.setText("");                    

                 }
  */
                 setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);

                 if ((requestProduct.getcPay().equalsIgnoreCase("N")&&
                       (requestProduct.getcSold().equalsIgnoreCase("N"))))
                 {
                      requestProduct.setcDate("");                 
                      holder.edtDate.setText(""); 
                 }

                 if (requestProduct.getcSight().equalsIgnoreCase("Y")){
                    holder.chkPay.setEnabled(false);
                    holder.chkSold.setEnabled(false);
                    holder.chkDeviceSetup.setEnabled(false);
                    
                    requestProduct.setcDate("");
                    holder.edtDate.setText("");   
                 }

              }
              
              //setWareHouseId(requestProduct);
              setDataModifled();

           }           
        });
	    /////////
	    holder.chkDeviceSetup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
              if (isChecked)
              {
                 requestProduct.setcEquipping("Y");
                 requestProduct.setcSold("N");
                 requestProduct.setcPay("N");
                 
                 requestProduct.setcDate("");
                 holder.edtDate.setText("");  
                 
                 holder.chkPay.setChecked(false);
                 holder.chkSold.setChecked(false);
 
                 //holder.chkPay.setEnabled(false);
                 //holder.chkSold.setEnabled(false);
                 
                 holder.chkHasDocument.setEnabled(true);
                 holder.edtRemark.setEnabled(true);
                 
                 setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
                 /*
                 String cSight = (requestProduct.getcSight() == null)?"N":requestProduct.getcSight();
                 
                 if (cSight.equalsIgnoreCase("N"))
                 {
             
                   // holder.reasonSentenceSpinner.setSelection(2);
                   // holder.reasonSentenceSpinner.setEnabled(false);

                    setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
                    
                 }else
                 {
                    //holder.reasonSentenceSpinner.setEnabled(false);
                    //holder.reasonSentenceSpinner.setSelection(0);
                    requestProduct.setcReasonID(0);
                    
                    setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);

                 }*/
              }else{
                 requestProduct.setcEquipping("N");

                 holder.chkPay.setEnabled(true);
                 holder.chkSold.setEnabled(true);

                 /*
                 if ((!holder.chkPay.isChecked())&&(!holder.chkSold.isChecked()))
                 {
                    requestProduct.setcReasonID(0);
                   // holder.reasonSentenceSpinner.setSelection(0);
                   // holder.reasonSentenceSpinner.setEnabled(true);
                    setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
                 }*/
                 
                 /*
                  * fix 9-08-2014
                  */
                 //requestProduct.setcReasonID(0);
                 SpinnerAdapter adapter = holder.reasonSentenceSpinner.getAdapter();
                 if (adapter != null){
                      ReasonSentence rs =  (ReasonSentence)adapter.getItem(2);
                      if (requestProduct.getcReasonID() == rs.getReasonID()){
                         requestProduct.setcReasonID(0);                            
                      }
                 }
                 ////////
                 
                 setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
                 

                 if (requestProduct.getcSight().equalsIgnoreCase("Y"))
                 {
                    holder.chkPay.setEnabled(false);
                    holder.chkSold.setEnabled(false);
                    holder.chkDeviceSetup.setEnabled(false);
                 }
                 
              }
              
              //setWareHouseId(requestProduct);
              setDataModifled();

           }           
        });
	    ////////
	    holder.chkHasDocument.setOnCheckedChangeListener(new OnCheckedChangeListener(){
	       @Override
	       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
	          if (isChecked){
	             requestProduct.setcDocument("Y");
	          }else{
	             requestProduct.setcDocument("N");
	             
	             
	          }
	          
              //setWareHouseId(requestProduct);
              setDataModifled();

	       }	       
	    });
	    ////////////////
	    holder.radioHasCar.setOnCheckedChangeListener(new OnCheckedChangeListener(){
	       @Override
	       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
	            if (isChecked)
	            {
	               //if (requestProduct.getcMid().equalsIgnoreCase("302390"))
	               //{
	                  Log.d("DEBUG_D_D", "Warehouse id = "+requestProduct.getcMid()+", " +
	                  		""+requestProduct.getcWareHouse());
	               //}
	               ///////////////////////
	               requestProduct.setcSight("Y");
	               
	               if (requestProduct.getcErrorType() == JobRequestProduct.ROW_RESULT_ERROR)
	               {
	                  vContainer.setBackgroundResource(R.color.inspect_car_error_bg);
	               }else if (requestProduct.getcErrorType() == JobRequestProduct.ROW_RESULT_WARNING)
	               {
	                  vContainer.setBackgroundResource(R.color.inspect_car_warning_bg);
	               }else{
	                  vContainer.setBackgroundResource(R.color.inspect_car_normal_bg);
	               }
	               
	               setEnable(holder,false);
	               
	               
                   requestProduct.setcReasonID(0);
                   requestProduct.setcDate("");
                   requestProduct.setcPay("N");
                   requestProduct.setcSold("N");
                   requestProduct.setcEquipping("N");
                   
	               holder.chkDeviceSetup.setEnabled(false);
	               
	               holder.chkHasDocument.setEnabled(true);
	               holder.edtCarMiles.setEnabled(true);
	               holder.btnTakeCamera.setEnabled(true);
	               holder.btnAddAndRemove.setEnabled(true);
	               holder.edtRemark.setEnabled(true);
	               holder.historyLocationSpinner.setEnabled(true);
	               
	               
	               /*
	               holder.reasonSentenceSpinner.setEnabled(false);
                   holder.reasonSentenceSpinner.setSelection(0);
                   */
	               /*
	                * fix 09-08-2014
	                */
	               setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);

	               
                   holder.edtDate.setText("");
                   
                   holder.chkPay.setChecked(false);
                   holder.chkSold.setChecked(false);
                   holder.chkDeviceSetup.setChecked(false);
                   
                   
                   holder.edtDate.setEnabled(false);
                   holder.chkPay.setEnabled(false);
                   holder.chkSold.setEnabled(false);
                   
                   

                   ///////////////////////
                   
                   setupLayoutDropdownList(requestProduct,holder);
                   
                   
                   SpinnerAdapter hist_adapter = holder.historyLocationSpinner.getAdapter();
                   if (hist_adapter != null)
                   {
                      for(int i = 0; i < hist_adapter.getCount();i++)
                      {
                         CarInspectStampLocation loc = (CarInspectStampLocation)hist_adapter.getItem(i);
                         if (requestProduct.getcWareHouse() > 0)
                         {
                            if (requestProduct.getcWareHouse() == loc.getCustomerSurveySiteID())
                            {
                               //if (requestProduct.getcMid().equalsIgnoreCase("302390"))
                               //{
                               //   Log.d("DEBUG_D_D", "set setSelection "+i);
                               //}
                               holder.historyLocationSpinner.setSelection(i);
                               break;
                            }
                         }else{
                            if (site.getCustomerSurveySiteID() == loc.getCustomerSurveySiteID()){
                               requestProduct.setcWareHouse(site.getCustomerSurveySiteID());
                               holder.historyLocationSpinner.setSelection(i);
                               break;
                            }
                         }
                      }
                   }
                   
                   /**/
                   
                   holder.layoutSpinner.post(new Runnable(){

                     @Override
                     public void run() {
                        // TODO Auto-generated method stub
                        holder.layoutSpinner.initial(currentTask.getTaskCode(), 
                              holder.tmpRequestProduct.getcWareHouse());
                        final SpinnerAdapter adapter = holder.layoutSpinner.getAdapter();
                        int inspectDataObjectID = holder.tmpRequestProduct.getInspectDataObjectID();
                        for(int i = 0; i < adapter.getCount();i++){
                           Object obj = adapter.getItem(i);
                           if (obj instanceof InspectDataObjectSaved){
                              InspectDataObjectSaved obj_tmp = (InspectDataObjectSaved)obj;
                              if (obj_tmp.getInspectDataObjectID() == inspectDataObjectID){
                                // selected = obj_tmp;
                                 holder.layoutSpinner.setSelection(i, false);
                                 break;
                              }
                           }
                        }
                     }
                      
                   });
            
                   ////////////////
                   
                   setRowColor(vContainer,requestProduct);
                   
                  
	           }
	           setDataModifled();

	       }
	    });
	    holder.radioNoHasCar.setOnCheckedChangeListener(new OnCheckedChangeListener(){

         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked)
            {
               vContainer.setBackgroundResource(android.R.color.white);

               requestProduct.setcSight("N");
               requestProduct.setcWareHouse(0);
               
               holder.historyLocationSpinner.setSelection(0);
               if (holder.rCallbackLayoutSpinner != null)
               {
                  holder.layoutSpinner.removeCallbacks(holder.rCallbackLayoutSpinner);           
               }
               holder.layoutSpinner.setSelection(0, false);
               holder.layoutSpinner.setEnabled(false);

               //vContainer.setBackgroundResource(android.R.color.white);

               setEnable(holder,false);
               
               holder.chkPay.setEnabled(true);
               holder.chkSold.setEnabled(true);
               holder.chkDeviceSetup.setEnabled(true);
               holder.reasonSentenceSpinner.setEnabled(true);
               holder.edtRemark.setEnabled(true);
               
               
               String cPay = (requestProduct.getcPay() == null)?"N":requestProduct.getcPay();
               String cSold = (requestProduct.getcSold() == null)?"N":requestProduct.getcSold();
               String cEquiping = (requestProduct.getcEquipping() == null)?"N":requestProduct.getcEquipping();
               
  
               if (
                        (cPay.equalsIgnoreCase("Y") || 
                              cSold.equalsIgnoreCase("Y") ||
                              cEquiping.equalsIgnoreCase("Y"))
                     )
               {
                  if (cEquiping.equalsIgnoreCase("N"))
                  {
                     holder.edtDate.setEnabled(true);
                  }
                  holder.reasonSentenceSpinner.setEnabled(false);
               }
               holder.edtCarMiles.setText("");
               
               ////////////
               requestProduct.setcWareHouse(0);
               requestProduct.setInspectDataObjectID(-1);/*clear */
               ////////////////////
               
               
               if (requestProduct.getcErrorType() == JobRequestProduct.ROW_RESULT_ERROR){
                  holder.btnAddAndRemove.setEnabled(true);
               }
               
               
               setRowColor(vContainer,requestProduct);

               
               /*fix 9-08-2014*/
               //setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
               /*
                * fix 09-08-2014
                */
               setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
               
               ////////////
            }
            setDataModifled();
         }
	    });
	    holder.btnAddAndRemove.setOnClickListener(new OnClickListener(){

         @Override
         public void onClick(View v) {
            // TODO Auto-generated method stub
            if (getOnShowContextMenu() != null)
            {
               getOnShowContextMenu().onShowContextMenu(v, requestProduct);
               setDataModifled();

            }
         }
	       
	    });
	    holder.btnTakeCamera.setOnClickListener(new OnClickListener(){

         @Override
         public void onClick(View v) {
            // TODO Auto-generated method stub
            if (getOnTakeCameraListener() != null)
            {
               getOnTakeCameraListener().onTakeCamera(requestProduct);
               setDataModifled();

            }
         }
	       
	    });

	    

	    
	    ///////
	    holder.edtDate.setOnClickListener(new OnClickListener(){

         @Override
         public void onClick(View v) 
         {
            // TODO Auto-generated method stub
            final View dialogView = View.inflate(context, R.layout.date_time_picker, null);
            holder.alertDialog = new AlertDialog.Builder(context).create();
            
            holder.alertDialog.setOnShowListener(new OnShowListener(){

               @Override
               public void onShow(DialogInterface dialog) {
                  // TODO Auto-generated method stub
                  TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                  timePicker.setIs24HourView(true);
                  timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                  timePicker.setVisibility(View.GONE);
                  
                  //bDatePickerShown = true;
               }
               
            });
            holder.alertDialog.setOnCancelListener(new OnCancelListener(){

               @Override
               public void onCancel(DialogInterface dialog) {
                  // TODO Auto-generated method stub
                  dialog.dismiss();
                  //bDatePickerShown = false;
               }
            });
            dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                     DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                     TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                     timePicker.setVisibility(View.GONE);
                     
                     Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth(),
                                        timePicker.getCurrentHour(),
                                        timePicker.getCurrentMinute());

                     long time = calendar.getTimeInMillis();
                     String timestamp = DataUtil.convertDateToStringYYYYMMDD(new Timestamp(time));
                     
                     holder.edtDate.setText(
                           DataUtil.convertToDisplayCarInspectDateFormat(timestamp));
                     
                     
                     //requestProduct.setcDate(timestamp);
                     holder.tmpRequestProduct.setcDate(timestamp);
                     
                     //setWareHouseId(requestProduct);
                     
                     holder.alertDialog.dismiss();
                     //bDatePickerShown = false;
                }});
            holder.alertDialog.setView(dialogView);
            if (!holder.alertDialog.isShowing())
            {
               holder.alertDialog.show();
               //bDatePickerShown = true;
            }
         }
	       
	    });

	         
 
        ////////////////////
        
        if (requestProduct.getPhotoSetID() > 0){
           holder.btnTakeCamera.setCompoundDrawablesWithIntrinsicBounds(
                 R.drawable.ic_device_access_camera_active,0,0,0);
           }else{
           holder.btnTakeCamera.setCompoundDrawablesWithIntrinsicBounds(
                 R.drawable.ic_device_access_camera,0,0,0);
           }
        
        holder.tvOrder.setText(requestProduct.getcOrder()+"");
        holder.tvMid.setText(requestProduct.getcMid()+"\r\n"+requestProduct.getcVin());
        holder.tvDesc.setText(requestProduct.getcDescription()+"\r\n"+requestProduct.getcModel());
        holder.tvEngine.setText(requestProduct.getcEngine()+"\r\n"+requestProduct.getcColor());

        holder.chkPay.setChecked(requestProduct.getcPay().equalsIgnoreCase("Y"));
        holder.chkSold.setChecked(requestProduct.getcSold().equalsIgnoreCase("Y"));
        holder.chkDeviceSetup.setChecked(requestProduct.getcEquipping().equalsIgnoreCase("Y"));
        holder.chkHasDocument.setChecked(requestProduct.getcDocument().equalsIgnoreCase("Y"));
        
        holder.edtCarMiles.setText(requestProduct.getcKms());
        holder.edtRemark.setText(requestProduct.getcRemark());

        
        setRowColor(vContainer,requestProduct);
        
        
        if ((requestProduct.getcRemark() != null)&&(!requestProduct.getcRemark().isEmpty()))
        {
           holder.edtRemark.setText(requestProduct.getcRemark());
        }else{
           holder.edtRemark.setText("");
        }
        
        ///////////////////
        if (requestProduct.getcSight() != null)
        {
           if (requestProduct.getcSight().equalsIgnoreCase("Y")){
              holder.radioHasCar.setChecked(true);

              
              holder.chkPay.setEnabled(false);
              holder.chkSold.setEnabled(false);
              holder.chkDeviceSetup.setEnabled(false);
              
           }else{
              holder.radioNoHasCar.setChecked(true);
           }
        }else{
           holder.radioNoHasCar.setChecked(true);
           
        }
        
        if (requestProduct.getcSight().equalsIgnoreCase("Y"))
        {
           SpinnerAdapter hist_adapter = holder.historyLocationSpinner.getAdapter();
           if (hist_adapter != null){
              for(int i = 0; i < hist_adapter.getCount();i++){
                 CarInspectStampLocation loc = (CarInspectStampLocation)hist_adapter.getItem(i);
                 if (requestProduct.getcWareHouse() > 0)
                 {
                    if (requestProduct.getcWareHouse() == loc.getCustomerSurveySiteID()){
                       holder.historyLocationSpinner.setSelection(i);
                       break;
                    }
                 }
              }
           }           
        }else{
           holder.historyLocationSpinner.setSelection(0);
        }

 
        setReasonSentenceEnable(holder.reasonSentenceSpinner, requestProduct);
        
        holder.edtDate.setText(
              DataUtil.convertToDisplayCarInspectDateFormat(requestProduct.getcDate()));

        
        holder.textMileNoChangeListener = new TextChangeListener();
        holder.textRemarkChangeListener = new TextChangeListener();
        
        holder.textMileNoChangeListener.fieldTextChange = FieldTextChange.MILE_NO;
        holder.textRemarkChangeListener.fieldTextChange = FieldTextChange.REMARK;

        
        holder.textMileNoChangeListener.jobProductRequest = requestProduct;    
        holder.textRemarkChangeListener.jobProductRequest = requestProduct;
        
        //textMileNoChangeListener.edtText = holder.edtCarMiles;
        //textRemarkChangeListener.edtText = holder.edtRemark;
        
        holder.edtCarMiles.addTextChangedListener(holder.textMileNoChangeListener);
        holder.edtRemark.addTextChangedListener(holder.textRemarkChangeListener);
         
        //InspectDataObjectSaved selected = null;
        //////////////////////////
        Log.d("DEBUG_D", "get view pos = "+position+" , "+requestProduct.getcOrder());
        if (requestProduct.getcSight().equalsIgnoreCase("Y"))
        {
           setupLayoutDropdownList(requestProduct,holder);
        }else{
           holder.layoutSpinner.setSelection(0, false);
           holder.layoutSpinner.setEnabled(false);
        }
        /////////////////////////////
		return v;
	}
	private void setupLayoutDropdownList(JobRequestProduct requestProduct,final Holder holder){
	   //////////////////
	   holder.layoutSpinner.setEnabled(true);
	   ////////
       if (holder.rCallbackLayoutSpinner != null)
       {
          holder.layoutSpinner.removeCallbacks(holder.rCallbackLayoutSpinner);           
       }
       

      holder.layoutSpinner.initial(currentTask.getTaskCode(), 
             requestProduct.getcWareHouse());

      SpinnerAdapter adapter = holder.layoutSpinner.getAdapter();
       
      int inspectDataObjectID = requestProduct.getInspectDataObjectID();

      
       int iSelected = 0;
       for(int i = 0; i < adapter.getCount();i++){
          Object obj = adapter.getItem(i);
          if (obj instanceof InspectDataObjectSaved){
             InspectDataObjectSaved obj_tmp = (InspectDataObjectSaved)obj;
             if (obj_tmp.getInspectDataObjectID() == inspectDataObjectID){
               // selected = obj_tmp;
                iSelected = i;
                break;
             }
          }
       }

       holder.layoutSpinner.setSelection(iSelected, false);

       ///////////
       if (adapter != null)
       {
          if (adapter.getCount() > 0)
          {
            holder.rCallbackLayoutSpinner = new Runnable(){

                 @Override
                 public void run() {
                    

                    ////////////
                    holder.historyLocationSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) 
                     {
                        // TODO Auto-generated method stub
                        Object obj = parent.getItemAtPosition(pos);
                        if (obj instanceof CarInspectStampLocation){
                           CarInspectStampLocation loc = (CarInspectStampLocation)obj;
                          
                           JobRequestProduct jrp = (JobRequestProduct)holder.historyLocationSpinner.getTag();
                           
                           jrp.setcWareHouse(loc.getCustomerSurveySiteID());
                           
                           if (jrp.getcSight().equalsIgnoreCase("Y"))
                           {
                              holder.layoutSpinner.setEnabled(true);
                              holder.layoutSpinner.initial(currentTask.getTaskCode(), jrp.getcWareHouse());
                              int inspectDataObjectID = jrp.getInspectDataObjectID();
                              SpinnerAdapter adapter = holder.layoutSpinner.getAdapter();
                              if (adapter.getCount() > 0){
                                 int iSelected = 0;
                                 for(int i = 0; i < adapter.getCount();i++)
                                 {
                                     obj = adapter.getItem(i);
                                    if (obj instanceof InspectDataObjectSaved)
                                    {
                                       InspectDataObjectSaved obj_tmp = (InspectDataObjectSaved)obj;
                                       if (obj_tmp.getInspectDataObjectID() == inspectDataObjectID){
                                         // selected = obj_tmp;
                                          iSelected = i;
                                          break;
                                       }
                                    }
                                  }
                                 holder.layoutSpinner.setSelection(iSelected, false);
                              }
                           }else{
                              holder.layoutSpinner.setSelection(0, false);
                              holder.layoutSpinner.setEnabled(false);
                           }
                           
                           setDataModifled();
                        }
                     }

                     @Override
                     public void onNothingSelected(AdapterView< ? > arg0) {
                        // TODO Auto-generated method stub
                        
                     }
                       
                    });
                    
                    
                    
                    holder.layoutSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                          // TODO Auto-generated method stub
                          if (parent.getSelectedItem() instanceof InspectDataObjectSaved){
                             InspectDataObjectSaved objSelected = (InspectDataObjectSaved)parent.getItemAtPosition(pos);
                             //requestProduct.setInspectDataObjectID(objSelected.getInspectDataObjectID());
                             JobRequestProduct jrp = (JobRequestProduct)holder.layoutSpinner.getTag();
                             Log.d("DEBUG_D", "Layout selected -> "+jrp.getcOrder()+" , "+objSelected.getInspectDataObjectID());
                             jrp.setInspectDataObjectID(objSelected.getInspectDataObjectID());
                             setDataModifled();
                          }
                       }

                       @Override
                       public void onNothingSelected(AdapterView< ? > arg0) {
                          // TODO Auto-generated method stub
                          
                       }
                        
                     });

                 }
            };
            //holder.layoutSpinner.setTag(requestProduct);
            holder.layoutSpinner.post(holder.rCallbackLayoutSpinner);
          }

       }else{
          holder.historyLocationSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
              // TODO Auto-generated method stub
              Object obj = parent.getItemAtPosition(pos);
              if (obj instanceof CarInspectStampLocation){
                 CarInspectStampLocation loc = (CarInspectStampLocation)obj;
                 JobRequestProduct jrp = (JobRequestProduct)holder.historyLocationSpinner.getTag();
                 jrp.setcWareHouse(loc.getCustomerSurveySiteID());
                 
                 
                 
                 holder.layoutSpinner.initial(currentTask.getTaskCode(), 
                       jrp.getcWareHouse());
                 final SpinnerAdapter adapter = holder.layoutSpinner.getAdapter();
                 int inspectDataObjectID = holder.tmpRequestProduct.getInspectDataObjectID();
                 for(int i = 0; i < adapter.getCount();i++){
                     obj = adapter.getItem(i);
                    if (obj instanceof InspectDataObjectSaved){
                       InspectDataObjectSaved obj_tmp = (InspectDataObjectSaved)obj;
                       if (obj_tmp.getInspectDataObjectID() == inspectDataObjectID){
                         // selected = obj_tmp;
                          holder.layoutSpinner.setSelection(i, false);
                          break;
                       }
                    }
                 }
              }
           }

           @Override
           public void onNothingSelected(AdapterView< ? > arg0) {
              // TODO Auto-generated method stub
              
           }
             
          });
       }
	}
	private void setRowColor(View vContainer,JobRequestProduct requestProduct)
	{
	   boolean hasSingleVin = false;
	   
	   if (requestProduct.getcErrorType() != JobRequestProduct.ROW_RESULT_NORMAL)
       {
	      if (this.vinTable.containsKey(requestProduct.getcVin())){
	          int size = this.vinTable.get(requestProduct.getcVin()).size();
	          if (size == 1){
	             hasSingleVin = true;
	          }
	       }
	       if (hasSingleVin){
	          requestProduct.setcErrorType(JobRequestProduct.ROW_RESULT_NORMAL);
	       }  
       }
	
	   /////////////
       if (requestProduct.getcErrorType() == JobRequestProduct.ROW_RESULT_ERROR)
       {
          vContainer.setBackgroundResource(R.color.inspect_car_error_bg);
       }else if (requestProduct.getcErrorType() == JobRequestProduct.ROW_RESULT_WARNING){
          vContainer.setBackgroundResource(R.color.inspect_car_warning_bg);   
       }else
       {
          if (requestProduct.getcSight() != null)
          {
             if (requestProduct.getcSight().equalsIgnoreCase("Y"))
             {
                   vContainer.setBackgroundResource(R.color.inspect_car_normal_bg);                                  
             }else{
                   vContainer.setBackgroundResource(android.R.color.white);
             }
          }else{
             vContainer.setBackgroundResource(android.R.color.white);              
          }
       }
	}
	private void setEnable(Holder holder,boolean bEnabled)
	{
	   //holder.chkPay.setEnabled(bEnabled);
       //holder.chkSold.setEnabled(bEnabled);
       //holder.chkDeviceSetup.setEnabled(bEnabled);
       
	   //holder.chkHasDocument.setEnabled(bEnabled);
       holder.btnAddAndRemove.setEnabled(bEnabled);
       //holder.btnTakeCamera.setEnabled(bEnabled);
       holder.edtCarMiles.setEnabled(bEnabled);
       holder.edtDate.setEnabled(bEnabled);
       holder.edtRemark.setEnabled(bEnabled);
       
       //holder.reasonSentenceSpinner.setEnabled(bEnabled);
       holder.historyLocationSpinner.setEnabled(bEnabled);
	}
	public class EditTextColumnWatcher implements TextWatcher
	{
	 
	  public JobRequestProduct jobRequestProduct;
	  public boolean isCarMile;
	  public boolean isCarRemark;
      @Override
      public void afterTextChanged(Editable s) {
         // TODO Auto-generated method stub
         if (this.jobRequestProduct != null)
         {
            if (isCarMile)
            {
               this.jobRequestProduct.setcKms(s.toString());
            }         
            if (isCarRemark)
            {
               this.jobRequestProduct.setcRemark(s.toString());
            }
         }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
         // TODO Auto-generated method stub
         
      }
	   
	}
	
	public static float getColumnWidth(Context context,int columnIdx){
	   float view_width = ResourceValueUtil.getAndroidDeimen(context, "car_column_"+(columnIdx+1));
	   return view_width;
	}

   public OnTakeCameraListener<JobRequestProduct> getOnTakeCameraListener() {
      return onTakeCameraListener;
   }

   public void setOnTakeCameraListener(OnTakeCameraListener<JobRequestProduct> onTakeCameraListener) {
      this.onTakeCameraListener = onTakeCameraListener;
   }

   public class CarFilter extends BaseFilterable {


      private int reasonID;
      private int warehouseID;
      private int inspectDataObjectID;
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
         // TODO Auto-generated method stub
         FilterResults result = new FilterResults();
         
         String textSearch = constraint.toString();
         if (!textSearch.isEmpty())
         {
            jobRequestProducts = new ArrayList<JobRequestProduct>();
            for(JobRequestProduct jobRequest : originalList)
            {
               /*
                <string name="col_min">Mid</string>
 <string name="col_model">Model</string>
 <string name="col_desc">Description</string>
 <string name="col_vin">Vin</string>
 <string name="col_color">Color</string>
 <string name="col_get_item">พบ/ไม่พบ</string>
 <string name="col_device_connected">ติดอุปกรณ์</string>
 <string name="col_miles">เลขไมล์</string>
 <string name="col_price">ราคา</string>
 <string name="col_remark">หมายเหตุ</string>
                */
               String field_for_search = "";
               switch(this.getColIdx())
               {
                  case 0:{
                     field_for_search = jobRequest.getcOrder()+"";
                  }break;
                  case 1:{
                     field_for_search = jobRequest.getcMid();
                  }break;
                  case 2:{
                     field_for_search = jobRequest.getcModel();
                  }break;
                  case 3:{
                     field_for_search = jobRequest.getcDescription();
                  }break;
                  case 4:{
                     field_for_search = jobRequest.getcVin();
                  }break;
                  case 5:{
                     field_for_search = jobRequest.getcColor();
                  }break;
                  case 6:{
                     field_for_search = jobRequest.getcEngine();
                  }break;
                  case 7:{
                     field_for_search = jobRequest.getcSight();
                  }break;
                  case 8:{
                     field_for_search = jobRequest.getcSold();
                  }break;
                  case 9:{
                     field_for_search = jobRequest.getcPay();
                  }break;
                  case 10:{
                     field_for_search = DataUtil.convertToDisplayCarInspectDateFormat(jobRequest.getcDate());
                  }break;
                  case 11:{
                     field_for_search = jobRequest.getcEquipping();
                  }break;
                  case 12:{
                     field_for_search = jobRequest.getcDocument();
                  }break;
                  case 13:{
                     //field_for_search = jobRequest.get
                  }break;
                  case 14:{
                     field_for_search = jobRequest.getcKms();
                  }break;
                  case 15:{
                     field_for_search = jobRequest.getcRemark();
                  }break;
               }
              if (getColIdx() == 13)
               {/*filter by reasonsentence using id to match object*/
                  if (this.getReasonID() == jobRequest.getcReasonID()){
                     jobRequestProducts.add(jobRequest);
                  }
               }else if (getColIdx() == 16){
                  if (this.getWarehouseID() == jobRequest.getcWareHouse()){
                     jobRequestProducts.add(jobRequest);                     
                  }
               }else if (getColIdx() == 17){
                  if (this.getInspectDataObjectID() == jobRequest.getInspectDataObjectID()){
                     jobRequestProducts.add(jobRequest);
                  }
               }else
               {
                     if (!field_for_search.isEmpty())
                     {
                        if (field_for_search.toLowerCase().contains(textSearch.toLowerCase())){
                           jobRequestProducts.add(jobRequest);  
                        }
                     }                  
               }
            }
            
            result.values = jobRequestProducts;
            result.count = jobRequestProducts.size();
         }else{
            result.values = originalList;
            result.count = originalList.size();
         }
         return result;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
         // TODO Auto-generated method stub
         if (results.count == 0) {
            notifyDataSetInvalidated();
        } else {
            jobRequestProducts = (ArrayList<JobRequestProduct>) results.values;
            notifyDataSetChanged();
        }
      }

      public int getReasonID() {
         return reasonID;
      }

      public void setReasonID(int reasonID) {
         this.reasonID = reasonID;
      }

      public int getWarehouseID() {
         return warehouseID;
      }

      public void setWarehouseID(int warehouseID) {
         this.warehouseID = warehouseID;
      }

      public int getInspectDataObjectID() {
         return inspectDataObjectID;
      }

      public void setInspectDataObjectID(int inspectDataObjectID) {
         this.inspectDataObjectID = inspectDataObjectID;
      }
   };
   @Override
   public Filter getFilter() {
      // TODO Auto-generated method stub
      if (carFilter == null){
         carFilter = new CarFilter();
      }
      return carFilter;
   }
   /*
   private void setWareHouseId(JobRequestProduct jobRequestProduct)
   {
      if (jobRequestProduct.getcWareHouse() <= 0)
      {
         jobRequestProduct.setcWareHouse(site.getCustomerSurveySiteID());
      }
   }
   */
   public OnAddAndRemoveContextMenuListener<JobRequestProduct> getOnShowContextMenu() {
      return onShowContextMenu;
   }

   public void setOnShowContextMenu(OnAddAndRemoveContextMenuListener<JobRequestProduct> onShowContextMenu) {
      this.onShowContextMenu = onShowContextMenu;
   }
   private void setReasonSentenceEnable(ReasonSentenceSpinner rsp,
         JobRequestProduct requestProduct)
   {
      
      if (requestProduct.getcSight().equalsIgnoreCase("Y")){
         if (requestProduct.getcOrder() == 7){
            int xxx = 0;
            xxx++;
         }
         requestProduct.setcReasonID(0);
         rsp.setSelection(0);
         rsp.setEnabled(false);
      }else{
         if ((requestProduct.getcPay().equalsIgnoreCase("Y")||requestProduct.getcSold().equalsIgnoreCase("Y")))
         {
            rsp.setSelection(1);
            rsp.setEnabled(false);
         }else 
         if (requestProduct.getcEquipping().equalsIgnoreCase("Y"))
         {
            rsp.setSelection(2);
            rsp.setEnabled(false);            
         }else{
            int reasonId = requestProduct.getcReasonID();
            if (reasonId > 0)
            {
               SpinnerAdapter adapter = rsp.getAdapter();
               if (adapter != null)
               {
                  for(int i = 0; i < adapter.getCount();i++)
                  {
                     ReasonSentence reason = (ReasonSentence)adapter.getItem(i);
                     if (requestProduct.getcReasonID() == reason.getReasonID()){
                        rsp.setSelection(i);
                        break;
                     }
                  }
               }
            }else{
               if (requestProduct.getcOrder() == 7){
                  int xxx = 0;
                  xxx++;
               }
               rsp.setSelection(0);
               rsp.setEnabled(true);
            }
         }
      }
   }
   private void setDataModifled(){
      SharedPreferenceUtil.saveCarInspectDataModified(context, true);
      if (currentTask.isInspectReportGenerated()){
         ReportInspectSummaryStatusHelper.resetReportStatus(context, currentTask);
         currentTask.setInspectReportGenerated(false);
      }
   }

   
}

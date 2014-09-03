package com.epro.psmobile.dialog;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.fragment.CustomerInfoDetailFragment;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.LocationUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.MessageBox.MessageConfirmType;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CarInspectDialog extends DialogFragment {

   private EditText edtMileNo;
   private Button btnTimeStamp;
   private CustomerSurveySite site;
   private Task task;
   private EditText edtAddress;
   private boolean bAddNewLocation;
   
   public interface OnCarInspectUpdated
   {
      void onUpdated();
   }
   private OnCarInspectUpdated carInspectUpdate;
   
   public static CarInspectDialog newInstance(Task currentTask,CustomerSurveySite site)
   {
      return newInstance(currentTask,site,false);
   }
   public static CarInspectDialog newInstance(Task currentTask,CustomerSurveySite site,boolean addNewLocation)
   {
      CarInspectDialog car_dlg = new CarInspectDialog();
      Bundle bArgument = new Bundle();
      bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
      bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, site);
      bArgument.putBoolean(InstanceStateKey.KEY_ARGUMENT_ADD_NEW_LOCATION, addNewLocation);
      car_dlg.setArguments(bArgument);
      return car_dlg;
   }
   private CarInspectDialog() {
      // TODO Auto-generated constructor stub
   }
   /* (non-Javadoc)
    * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
    */
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      View vCustom = View.inflate(getActivity(), R.layout.car_inspect_dialog, null);

      Bundle bArgument = this.getArguments();
      if (bArgument != null)
      {
         site  = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
         task = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
         bAddNewLocation  = bArgument.getBoolean(InstanceStateKey.KEY_ARGUMENT_ADD_NEW_LOCATION);
         
         TextView tvLocationAddr = (TextView)vCustom.findViewById(R.id.tv_car_inspect_site_name);
         edtAddress = (EditText)vCustom.findViewById(R.id.edt_inspect_site_name);
         tvLocationAddr.setText(site.getSiteAddress());
         if (bAddNewLocation){
            tvLocationAddr.setVisibility(View.GONE);
            edtAddress.setVisibility(View.VISIBLE);            
         }else{
            edtAddress.setVisibility(View.GONE);
            tvLocationAddr.setVisibility(View.VISIBLE);
         }
         edtMileNo = (EditText)vCustom.findViewById(R.id.et_car_inspect_dlg_mile_no);
         btnTimeStamp = (Button)vCustom.findViewById(R.id.btn_car_inspect_dlg_time);
         btnTimeStamp.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
               // TODO Auto-generated method stub
               final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
               final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

               alertDialog.setOnShowListener(new OnShowListener(){

                  @Override
                  public void onShow(DialogInterface dialog) {
                     // TODO Auto-generated method stub
                     TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                     timePicker.setIs24HourView(true);
                     timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                  }
                  
               });
               dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                        
                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                           datePicker.getMonth(),
                                           datePicker.getDayOfMonth(),
                                           timePicker.getCurrentHour(),
                                           timePicker.getCurrentMinute());

                        long time = calendar.getTimeInMillis();
                        String timestamp = DataUtil.convertTimestampToStringYYYMMDDHHmmss(new Timestamp(time));
                        btnTimeStamp.setText(timestamp);
                        alertDialog.dismiss();
                   }});
               alertDialog.setView(dialogView);
               alertDialog.show();
            }
         });
      }
      
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      if (this.bAddNewLocation){
         builder.setTitle(R.string.text_title_dlg_car_add_new_location);  
      }else{
         builder.setTitle(R.string.text_title_dlg_car_location_stamp);
      }
      builder.setView(vCustom);
      builder.setPositiveButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
         
         @Override
         public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            /*
             * save 
             */
            if (bAddNewLocation)
            {
               if (edtAddress != null){
                  if (edtAddress.getText().toString().trim().isEmpty()){
                     /*
                      * alert
                      */
                     final FragmentActivity activity = CarInspectDialog.this.getActivity();
                     
                     MessageBox.showMessage(activity,
                           getActivity().getString(R.string.text_error_title),
                           getActivity().getString(R.string.text_error_required_field_new_location));
                     return;
                  }
               }
            }
            
            if ((task != null)&&(site != null))
            {
               
               if (edtMileNo.getText().toString().trim().isEmpty()){
                  /*
                   * show alert
                   */
                  /*
                  MessageBox.showMessage(getActivity(),
                        R.string.text_error_title,
                        R.string.text_error_required_field_new_mile_no);
*/
                  final FragmentActivity activity = CarInspectDialog.this.getActivity();
                  
                  MessageBox.showMessageWithConfirm(activity,
                        getActivity().getString(R.string.text_error_title),
                        getActivity().getString(R.string.text_error_required_field_new_mile_no),
                        new MessageBox.MessageConfirmListener(){

                           @Override
                           public void onConfirmed(MessageConfirmType confirmType) {
                              // TODO Auto-generated method stub
                              if (activity != null)
                              {
                                 
                                 if (!bAddNewLocation)
                                 {
                                    activity.getSupportFragmentManager().popBackStack();
                                 
                                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                    CustomerInfoDetailFragment detail = CustomerInfoDetailFragment.newInstance(
                                          task.getJobRequest(), task,false);
                                    ft.replace(R.id.content_frag, detail, CustomerInfoDetailFragment.class.getName());
                                    ft.addToBackStack(null);
                                    ft.commit();                                 
                                 }                                 
                                 SharedPreferenceUtil.saveCarInspectDataModified(activity,
                                       false);
                              }
                           }
                     
                  },true);
                  
                  return;
               }
               if (btnTimeStamp.getText().toString().trim().isEmpty()){
                  /*
                  MessageBox.showMessage(getActivity(),
                        R.string.text_error_title,
                        R.string.text_error_required_field_new_time_stamp);
                  */
                  final FragmentActivity activity = CarInspectDialog.this.getActivity();
                  
                  MessageBox.showMessageWithConfirm(activity,
                        getActivity().getString(R.string.text_error_title),
                        getActivity().getString(R.string.text_error_required_field_new_time_stamp),
                        new MessageBox.MessageConfirmListener(){

                           @Override
                           public void onConfirmed(MessageConfirmType confirmType) {
                              // TODO Auto-generated method stub
                              
                              if (activity != null)
                              {
                                 if (!bAddNewLocation)
                                 {
                                    activity.getSupportFragmentManager().popBackStack();
                                 
                                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                    CustomerInfoDetailFragment detail = CustomerInfoDetailFragment.newInstance(
                                    task.getJobRequest(), task,false);
                                    ft.replace(R.id.content_frag, detail, CustomerInfoDetailFragment.class.getName());
                                    ft.addToBackStack(null);
                                    ft.commit();        
                                 }                                 
                                 SharedPreferenceUtil.saveCarInspectDataModified(activity, 
                                       false);

                              }
                           }
                     
                  },true);
                  return;
               }

               PSBODataAdapter adapter = PSBODataAdapter.getDataAdapter(getActivity());

               CarInspectStampLocation carInspectStamp = new CarInspectStampLocation();
               carInspectStamp.setTaskID(task.getTaskID());
//               carInspectStmap. task.getTaskID()
               carInspectStamp.setTaskCode(task.getTaskCode());
               carInspectStamp.setTaskDuplicateNo(task.getTaskDuplicatedNo());
               carInspectStamp.setJobRequestID(task.getJobRequest().getJobRequestID());
               carInspectStamp.setCustomerSurveySiteID(site.getCustomerSurveySiteID());
               carInspectStamp.setSiteAddress(site.getSiteAddress());
               carInspectStamp.setMilesNo(edtMileNo.getText().toString());
               carInspectStamp.setTimeRecorded(btnTimeStamp.getText().toString());
               
               Location loc = LocationUtil.getLastBestLocation(getActivity());
               if (loc != null)
               {
                  carInspectStamp.setSiteLat((float)loc.getLatitude());
                  carInspectStamp.setSiteLon((float)loc.getLongitude());
               }
               try 
               {
                  if (bAddNewLocation)
                  {
                     int maxSurveySiteId = adapter.getMaxCustomerSurveySiteID();
                     int maxSurveySiteIdFromStamp = adapter.getMaxCustomerSurveySiteIDFromStamp();
                     int maxId = maxSurveySiteId;
                     if (maxSurveySiteId < maxSurveySiteIdFromStamp){
                        maxId = maxSurveySiteIdFromStamp;
                     }
                     carInspectStamp.setCustomerSurveySiteID(maxId+1);
                     carInspectStamp.setSiteAddress(edtAddress.getText().toString());
                     carInspectStamp.setFlagIsAddNewCustomerSite("Y");
                  }                  
                  adapter.insertCarInspectStampLocation(carInspectStamp);
                  
                  MessageBox.showMessage(getActivity(), R.string.label_save_title,  R.string.label_save_complete);
                  
                  if (CarInspectDialog.this.getCarInspectUpdate() != null){
                     CarInspectDialog.this.getCarInspectUpdate().onUpdated();
                  }
               }
               catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
         }
      })
      .setNegativeButton(R.string.label_btn_cancel, new DialogInterface.OnClickListener() {
         
         @Override
         public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            if (!bAddNewLocation)
            {
               getActivity().getSupportFragmentManager().popBackStack();
            
               FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
               CustomerInfoDetailFragment detail = CustomerInfoDetailFragment.newInstance(
                  task.getJobRequest(), task,false);
               ft.replace(R.id.content_frag, detail, CustomerInfoDetailFragment.class.getName());
               ft.addToBackStack(null);
               ft.commit();
               
               SharedPreferenceUtil.saveCarInspectDataModified(getActivity(),
                     false);

            }
            CarInspectDialog.this.getDialog().dismiss();

         }
      });
      //return super.onCreateDialog(savedInstanceState);
      return builder.create();
   }
   /* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
    */
   @Override
   public View onCreateView(LayoutInflater inflater, 
         ViewGroup container, 
         Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      return super.onCreateView(inflater, container, savedInstanceState);
   }
   public OnCarInspectUpdated getCarInspectUpdate() {
      return carInspectUpdate;
   }
   public void setCarInspectUpdate(OnCarInspectUpdated carInspectUpdate) {
      this.carInspectUpdate = carInspectUpdate;
   }

}

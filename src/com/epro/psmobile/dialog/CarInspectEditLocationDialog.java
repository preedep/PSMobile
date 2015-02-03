package com.epro.psmobile.dialog;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.MessageBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;

public class CarInspectEditLocationDialog extends DialogFragment {

   private Task task;
   private CarInspectStampLocation stmpLoc;
   public static CarInspectEditLocationDialog newInstance(Task currentTask)
   {
      CarInspectEditLocationDialog editDlg = new CarInspectEditLocationDialog();
      Bundle bArgument = new Bundle();
      bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
      editDlg.setArguments(bArgument);
      return editDlg;
   }
   private CarInspectEditLocationDialog() {
      // TODO Auto-generated constructor stub
   }
   /* (non-Javadoc)
    * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
    */
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      View vCustom = View.inflate(getActivity(), R.layout.location_edit_dialog, null);
      final EditText edtLocation = (EditText)vCustom.findViewById(R.id.edt_location);
      Bundle bArgument = this.getArguments();
      if (bArgument != null)
      {
         stmpLoc = null;
         task = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
         if (task != null)
         {
            com.epro.psmobile.view.HistoryInspectLocationSpinner locations = 
               (com.epro.psmobile.view.HistoryInspectLocationSpinner)vCustom.findViewById(R.id.spn_location);
            locations.initial(task.getJobRequest().getJobRequestID(),task.getTaskCode());
            
            locations.setOnItemSelectedListener(new OnItemSelectedListener(){

               @Override
               public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                  // TODO Auto-generated method stub
                  Object objSelected =  parentView.getItemAtPosition(position);
                  if (objSelected != null)
                  {
                     if (objSelected instanceof CarInspectStampLocation){
                        stmpLoc = (CarInspectStampLocation)objSelected;
                        edtLocation.setText(stmpLoc.getSiteAddress());
                     }
                  }
               }

               @Override
               public void onNothingSelected(AdapterView< ? > arg0) {
                  // TODO Auto-generated method stub
                  
               }
               
            });
            if (locations.getCount() > 0){
               locations.setSelection(1);
            }
         }
      }
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle(R.string.text_title_edit_location);
      builder.setView(vCustom);
      builder.setPositiveButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {

         @Override
         public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            /*
             * update
             */
            if (stmpLoc != null){
               PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
               String location = 
                     edtLocation.getText().toString();
               if (location.trim().length() > 0){
                  stmpLoc.setSiteAddress(location);
                  try {
                     dataAdapter.updateNewLocationOfCarInspectStampLocation(stmpLoc);
                     /*
                      * update
                      */
                     dialog.dismiss();
                  }
                  catch (Exception e) {
                     // TODO Auto-generated catch block
//                     e.printStackTrace();
                     MessageBox.showMessage(getActivity(),
                           R.string.message_box_title_error,
                           e.getMessage());
                  }
               }
            }
            
         }
      })
      .setNegativeButton(R.string.label_btn_cancel, new DialogInterface.OnClickListener() {

         @Override
         public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            dialog.dismiss();
         }
      }
      );
      
      return builder.create();//super.onCreateDialog(savedInstanceState);
   }

}

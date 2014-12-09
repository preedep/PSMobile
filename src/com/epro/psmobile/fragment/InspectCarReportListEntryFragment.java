package com.epro.psmobile.fragment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.CarReportListEntryAdapter;
import com.epro.psmobile.adapter.CarReportListEntryAdapter.CarFilter;
import com.epro.psmobile.adapter.callback.OnAddAndRemoveContextMenuListener;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.adapter.filter.BaseFilterable;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.dialog.CarInspectDialog;
import com.epro.psmobile.dialog.CarInspectDialog.OnCarInspectUpdated;
import com.epro.psmobile.dialog.CarInspectEditLocationDialog;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.GlobalData;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.MessageBox.MessageConfirmType;
import com.epro.psmobile.util.ResourceValueUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.view.HistoryInspectLocationSpinner;
import com.epro.psmobile.view.InspectReportListColumnSpinner;
import com.epro.psmobile.view.LayoutSpinner;
import com.epro.psmobile.view.ReasonSentenceSpinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

public class InspectCarReportListEntryFragment extends InspectReportListFragment implements OnClickListener, OnTakeCameraListener<JobRequestProduct>, OnAddAndRemoveContextMenuListener<JobRequestProduct> {

    private Button btnSearch;
    private Button btnClear;
    private EditText edtSearch;
    private ViewGroup vObjectSearchContainer;
    
    private InspectReportListColumnSpinner columnFilter;
    private View vSub = null;
    private View vEntry = null;
    private ListView lsView;
    
    private JobRequestProduct currentJobRequestProduct;
    private JobRequestProduct currentRequestProduct;

	public InspectCarReportListEntryFragment() {
		// TODO Auto-generated constructor stub
		
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.fragment.InspectReportListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		View v = super.onCreateView(arg0, arg1, arg2);
		try {
			/*call initial after super onCreateView*/
			//super.initial(R.xml.report_list_car_entry);
		   this.setHasOptionsMenu(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}

   /* (non-Javadoc)
    * @see com.epro.psmobile.fragment.ContentViewBaseFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater, com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType)
    */
   @Override
   protected void onCreateOptionsMenu(Menu menu,
         MenuInflater inflater, 
         InspectOptMenuType inspectOptionMenuType) {
      // TODO Auto-generated method stub
      super.onCreateOptionsMenu(menu, inflater, inspectOptionMenuType);
   }

   /* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onDestroyView()
    */
   @Override
   public void onDestroyView() {
      // TODO Auto-generated method stub
      super.onDestroyView();
      if (lsView != null)
      {
         unregisterForContextMenu(lsView);
      }
      this.getSherlockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
   }
   @Override
   protected void onListViewUpdated() {
      // TODO Auto-generated method stub
      final ViewGroup vGroupHeader = (ViewGroup)currentView.findViewById(R.id.list_view_header);
      lsView = (ListView)currentView.findViewById(R.id.lv_report_list);
      
      setupHeader(vGroupHeader);
      setupListView(lsView);
      initialControls(currentView);

   }
   @Override
   protected void initial(final View currentView) {
      // TODO Auto-generated method stub
      /*
       * check stamp location
       */
      
      /*
      Bundle bArgument = this.getArguments();
      InspectReportListFragment.jobRequest = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
      InspectReportListFragment.currentTask = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
      InspectReportListFragment.customerSurveySite = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
      */
      GlobalData.inspectDataObjectSavedList = null;//reset
      GlobalData.inspectDataObjectTable = null;
      //final ViewGroup vGroupHeader = (ViewGroup)currentView.findViewById(R.id.list_view_header);
      lsView = (ListView)currentView.findViewById(R.id.lv_report_list);
      //lsView.setLongClickable(true);
      //lsView.setClickable(true);
      //lsView.setLongClickable(true);
      registerForContextMenu(lsView);
      
      //super.doPopupCheckIn();
      
      /*
      Log.d("DEBUG_D_D",InspectReportListFragment.currentTask.getTaskCode()+" , "+InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
      try {
         CarInspectStampLocation carInspectStampLocation = 
               this.getDataAdapter().findCarInspectStampLocation(InspectReportListFragment.currentTask.getTaskCode(), 
                     InspectReportListFragment.currentTask.getTaskDuplicatedNo(), 
                     InspectReportListFragment.currentTask.getJobRequest().getJobRequestID(), 
                     InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
         if (carInspectStampLocation == null)
         {
            if ((InspectReportListFragment.customerSurveySite.getLocationCheckInDate() == null)||(InspectReportListFragment.customerSurveySite.getLocationCheckInDate().isEmpty()))
            {
               //never stamp show dialog
               CarInspectDialog dlg = CarInspectDialog.newInstance(currentTask, customerSurveySite);
               dlg.setCarInspectUpdate(new CarInspectDialog.OnCarInspectUpdated() {
                  
                  @Override
                  public void onUpdated() {
                     // TODO Auto-generated method stub
                     setupHeader(vGroupHeader);
                     setupListView(lsView);
                     initialControls(currentView);
                  }
               });
               dlg.show(getChildFragmentManager(), CarInspectDialog.class.getName());               
            }else
            {
               
               ArrayList<CustomerSurveySite> sites = 
                     getDataAdapter().findCustomerSurveySite(currentTask.getTaskID());
               if (sites != null)
               {
                  int iCountInsertNewTmp = 0;
                  for(CustomerSurveySite site : sites)
                  {
                     if ((site.getLocationCheckInDate() != null)&&(!site.getLocationCheckInDate().isEmpty()))
                     {
                        //
                        // update to 
                        //
                        CarInspectStampLocation tmp_carInspectStampLocation = 
                              this.getDataAdapter().findCarInspectStampLocation(InspectReportListFragment.currentTask.getTaskCode(), 
                                    InspectReportListFragment.currentTask.getTaskDuplicatedNo(), 
                                    InspectReportListFragment.currentTask.getJobRequest().getJobRequestID(), 
                              site.getCustomerSurveySiteID());
                        if (tmp_carInspectStampLocation == null)
                        {
                           //insert new
                           CarInspectStampLocation carInspectStamp = new CarInspectStampLocation();
                           carInspectStamp.setTaskID(currentTask.getTaskID());
                           carInspectStamp.setTaskCode(currentTask.getTaskCode());
                           carInspectStamp.setTaskDuplicateNo(currentTask.getTaskDuplicatedNo());
                           carInspectStamp.setJobRequestID(currentTask.getJobRequest().getJobRequestID());
                           carInspectStamp.setCustomerSurveySiteID(site.getCustomerSurveySiteID());
                           carInspectStamp.setSiteAddress(site.getSiteAddress());
                           carInspectStamp.setMilesNo(site.getMileNo());
                          
                           
                           carInspectStamp.setTimeRecorded(site.getLocationCheckInDate());

                           getDataAdapter().insertCarInspectStampLocation(carInspectStamp);
                           iCountInsertNewTmp++;
                        }
                     }
                  }
                  if (iCountInsertNewTmp > 0){
                     initial(currentView);
                  }
               }
            }
         }else
         {
            setupHeader(vGroupHeader);
            setupListView(lsView);
            initialControls(currentView);
         }
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         MessageBox.showMessage(getActivity(), 
               R.string.text_error_title, 
               e.getMessage());
      }
      
      
   */   
        
   }
   
   private void initialControls(final View currentView){
      columnFilter = 
            (InspectReportListColumnSpinner)currentView.findViewById(R.id.ls_car_inspect_col_filter);
      
      columnFilter.initial();
      columnFilter.setOnItemSelectedListener(new OnItemSelectedListener(){

         @SuppressWarnings("unused")
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            switch(position){
               case 0:
               case 1:
               case 2:
               case 3:
               case 4:
               case 5:
               case 13:
               case 14:
               {
                 if (edtSearch == null)
                    edtSearch = (EditText)currentView.findViewById(R.id.edt_car_inspect_search);
                 edtSearch.setVisibility(View.VISIBLE);
                 
                 if (vObjectSearchContainer == null)
                    vObjectSearchContainer = (ViewGroup)currentView.findViewById(R.id.object_search_container);
                 vObjectSearchContainer.setVisibility(View.GONE);
               }break;
               default:
               {
                  if (edtSearch == null)
                     edtSearch = (EditText)currentView.findViewById(R.id.edt_car_inspect_search);
                  edtSearch.setVisibility(View.GONE);
                  
                  if (vObjectSearchContainer == null)
                     vObjectSearchContainer = (ViewGroup)currentView.findViewById(R.id.object_search_container);
                  vObjectSearchContainer.setVisibility(View.VISIBLE);                  
               }break;
            }
            
            if (vObjectSearchContainer.getVisibility() == View.VISIBLE){
               vObjectSearchContainer.removeAllViews();
               
               /*
                * create view of search entry
                */
               
               if (position == 6)
               {
                  vEntry = 
                        View.inflate(getActivity(), R.layout.ps_activity_report_list_entry_column_group_rdobox, null);
                  vSub = vEntry.findViewById(R.id.rdo_group_chk_car);
               }else if ((position == 7)||(position == 8)||(position == 10)||(position == 11))
               {
                  vEntry = 
                        View.inflate(getActivity(), 
                              R.layout.ps_activity_report_list_entry_column_chkbox, null);
                  
                  vSub = vEntry.findViewById(R.id.chkbox_report_list_entry_column_chkbox);
               }else if (position == 9){
                  vEntry = View.inflate(getActivity(), R.layout.ps_activity_report_list_entry_column_camera, null);
                  vSub = vEntry.findViewById(R.id.btn_report_list_entry_column_camera);
                  if (vSub instanceof Button){
                     ((Button)vSub).setText("");
                     ((Button)vSub).setOnClickListener(new OnClickListener(){

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
                                 timePicker.setVisibility(View.GONE);

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
                                    String timestamp = DataUtil.convertDateToStringYYYYMMDD(new Timestamp(time));
                                    ((Button)vSub).setText(timestamp);
                                    alertDialog.dismiss();
                               }});
                           alertDialog.setView(dialogView);
                           alertDialog.show();
                        }
                        
                     });
                  }
               }else if (position == 12){
                  vEntry = View.inflate(getActivity(),
                        R.layout.ps_activity_report_list_entry_column_reason_spinner, null);
                  vSub = vEntry.findViewById(R.id.sp_reason_sentence);
                  
                  ReasonSentenceSpinner spinner = (ReasonSentenceSpinner)vSub;
                  
                  if (jobRequestProducts != null){
                     PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
                     String reasonCode = jobRequestProducts.get(0).getcReasonCode();
                     try {
                        spinner.initialWithReasonList(
                              dataAdapter.getAllReasonSentenceByType(reasonCode));
                     }
                     catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                  }
                  
                  
               }else if (position == 15)
               {
                  vEntry = View.inflate(getActivity(),
                        R.layout.ps_activity_report_list_entry_column_history_location_spinner, null);
                  vSub = vEntry.findViewById(R.id.sp_history_inspect_location);
                  HistoryInspectLocationSpinner spinner = (HistoryInspectLocationSpinner)vSub;
                  if (jobRequestProducts != null){
                     spinner.initial(jobRequestProducts.get(0).getJobRequestID());
                  }
               }else if (position == 17)
               {
                  vEntry = View.inflate(getActivity(),
                        R.layout.ps_activity_report_list_entry_column_layout_spinner, null);
                  vSub = vEntry.findViewById(R.id.sp_layout_inspect);
                  LayoutSpinner spinner = (LayoutSpinner)vSub;
                  if (jobRequestProducts != null){
                     spinner.initial(currentTask.getTaskCode(),customerSurveySite.getCustomerSurveySiteID());
                  }
               }
               if (vEntry != null)
               {
                  DisplayMetrics metrics = getResources().getDisplayMetrics();
                  float pixels =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, metrics);
                  if (vSub != null){
                     vSub.getLayoutParams().width = (int) pixels;
                  }                  
                  vObjectSearchContainer.addView(vEntry);
               }
            }
            
         }

         @Override
         public void onNothingSelected(AdapterView< ? > arg0) {
            // TODO Auto-generated method stub
            
         }
         
      });

      
      TextView tvCustomerName = (TextView)currentView.findViewById(R.id.tv_report_list_customer_name);
      tvCustomerName.setText(InspectReportListFragment.customerSurveySite.getCustomerName());
      
      edtSearch = (EditText)currentView.findViewById(R.id.edt_car_inspect_search);
      vObjectSearchContainer = (ViewGroup)currentView.findViewById(R.id.object_search_container);
      
      btnSearch = (Button)currentView.findViewById(R.id.btn_car_inspect_search);
      btnSearch.setOnClickListener(this);
      
       btnClear = (Button)currentView.findViewById(R.id.btn_car_inspect_clear);
      btnClear.setOnClickListener(this);
   }
   private void setupListView(ListView lsView)
   {
      //lsView.setTextFilterEnabled(true);
      PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
      try {
         jobRequestProducts =  
                 dataAdapter.findJobRequestProductsByJobRequestIDAndOrderBy(jobRequest.getJobRequestID());
         if (jobRequestProducts != null)
         {
            GlobalData.inspectDataObjectSavedList = null;
            GlobalData.inspectDataObjectTable= null;
            
            CarReportListEntryAdapter carAdapter = new CarReportListEntryAdapter(getActivity(),
                  currentTask,
                  customerSurveySite,
                  jobRequestProducts);
            carAdapter.setOnTakeCameraListener(this);
            carAdapter.setOnShowContextMenu(this);
            lsView.setAdapter(carAdapter);
         }
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
//         e.printStackTrace();
         MessageBox.showMessage(getActivity(), 
               R.string.text_error_title, 
               e.getMessage());
      }

   }
   private void setupHeader(ViewGroup container)
   {
      if (container != null)
      {
         container.removeAllViews();
         String[] columnsHeader = getActivity().getResources().getStringArray(R.array.columns_car_inspect_summary_no_multi_row);
         for(int i = 0; i < columnsHeader.length;i++)
         {
            float view_width = CarReportListEntryAdapter.getColumnWidth(getActivity(), i);
            LayoutInflater  inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.ps_activity_report_list_entry_column_v2, null);
            if (view_width > 0)
            {
               TextView tvHeader = (TextView)v.findViewById(R.id.tv_car_list_header);
               tvHeader.getLayoutParams().width = (int) view_width;
               tvHeader.setText(columnsHeader[i]);
            }
            container.addView(v);
         }
      }
   }

   /* (non-Javadoc)
    * @see com.epro.psmobile.fragment.InspectReportListFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
    */
   /*
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // TODO Auto-generated method stub
      int id = item.getItemId();
      if (id == R.id.menu_switch_to_universal)
      {
          // switch to universal layout
          //automatic save before switch view
         if (saveAllData())
         {
            
            Bundle argument = new Bundle();
         
            argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, InspectReportListFragment.jobRequest);
            argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, InspectReportListFragment.customerSurveySite);
            argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, InspectReportListFragment.currentTask);
         
            Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
            boolean isShown = f.isVisible();
            argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_SCREEN_STATE, isShown);
            argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_IS_UNIVERSAL_LAYOUT, true);
            
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
         
            DrawingInspectFragment drawingFragment = new DrawingInspectFragment();                 
            drawingFragment.setArguments(argument);
         
            ft.replace(R.id.content_frag, drawingFragment,DrawingInspectFragment.class.getName());
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
         }
         
      }else
      if (id == R.id.menu_car_inspect_general_take_photo)
      {
         Bundle argument = new Bundle();
         
         argument.putString(InstanceStateKey.KEY_ARGUMENT_TASK_CODE, InspectReportListFragment.currentTask.getTaskCode());
         argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, InspectReportListFragment.jobRequest);
         argument.putInt(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY_ID, InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
         argument.putBoolean(InstanceStateKey.KEY_ARGUMENT_TAKE_GENERAL_PHOTO, true);

         int photoSetId = 0;
         
         PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
         try {
            ArrayList<InspectDataObjectPhotoSaved> photoSavedList = 
                  dataAdapter.getInspectDataObjectPhotoSavedWithGeneralImage(InspectReportListFragment.currentTask.getTaskCode(),
                        InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
            if (photoSavedList != null)
            {
               if (photoSavedList.size() > 0)
               {
                  photoSetId = photoSavedList.get(0).getPhotoID();
               }
            }
         }
         catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         
         argument.putInt(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID,photoSetId);
         ActivityUtil.startNewActivityWithResult(getActivity(),
               InspectPhotoEntryActivity.class, 
               argument, 
               InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY);
      }
      else if (id == R.id.menu_entry_save)
      {
         if (saveAllData()){
            MessageBox.showSaveCompleteMessage(getActivity());
         }
      }
      return super.onOptionsItemSelected(item);
   }*/

   protected boolean saveAllData()
   {
      boolean ret = false;
      if (lsView.getAdapter() != null)
      {
         if (lsView.getAdapter() instanceof CarReportListEntryAdapter)
         {
            CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)(lsView.getAdapter());
            ArrayList<JobRequestProduct> requestProducts = new ArrayList<JobRequestProduct>();
            for(int i = 0; i < adapter.getCount();i++){
               requestProducts.add((JobRequestProduct)adapter.getItem(i));
            }
            PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
            try {
               
               ArrayList<JobRequestProduct> tmps =  
                     dataAdapter.findJobRequestProductsByJobRequestID(jobRequest.getJobRequestID());

               if (tmps != null)
               {
                  for(JobRequestProduct item_tmp : tmps){
                     boolean hasData = false;
                     for(JobRequestProduct rp : requestProducts)
                     {
                          if (item_tmp.getcVin().equalsIgnoreCase(rp.getcVin())){
                             hasData = true;
                             break;
                          }
                     }
                     if (!hasData){
                        requestProducts.add(item_tmp);
                     }
                  }
                  dataAdapter.insertCarJobRequestProduct(InspectReportListFragment.jobRequest.getJobRequestID(), 
                        requestProducts);  
               }
               SharedPreferenceUtil.saveCarInspectDataModified(getActivity(), false);//reset flag , save every thing
               ret = true;
            }
            catch (Exception e) {
               // TODO Auto-generated catch block
               MessageBox.showMessage(getActivity(), 
                     getActivity().getString(R.string.text_error_title),
                     e.getMessage());   
               e.printStackTrace();
            }
         }
      }
      return ret;
   }
   /* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
    */
   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      // TODO Auto-generated method stub
      super.onCreateContextMenu(menu, v, menuInfo);
      menu.clear();
      this.getActivity().getMenuInflater().inflate(R.menu.context_menu_car_inspect, menu);
      if (currentRequestProduct != null){
         if (currentRequestProduct.getcErrorType() != JobRequestProduct.ROW_RESULT_ERROR){
            menu.findItem(R.id.menu_car_inspect_del_row).setVisible(false);
            menu.findItem(R.id.menu_car_inspect_reset_error_flag).setVisible(false);
         }
      }
   }

   /* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
    */
   @Override
   public boolean onContextItemSelected(android.view.MenuItem item) {
      // TODO Auto-generated method stub
      if (item.getItemId() == R.id.menu_car_inspect_add_new_location)
      {
         CarInspectDialog dlg = CarInspectDialog.newInstance(currentTask, customerSurveySite,true);
         dlg.setCarInspectUpdate(new OnCarInspectUpdated(){

            @Override
            public void onUpdated() {
               // TODO Auto-generated method stub
               if (lsView != null){
                  if (lsView.getAdapter() instanceof CarReportListEntryAdapter){
                     GlobalData.inspectDataObjectSavedList = null;
                     GlobalData.inspectDataObjectTable= null;
                     
                     CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)lsView.getAdapter();
                     adapter.notifyDataSetChanged();
                     adapter.notifyDataSetInvalidated();
                     lsView.setAdapter(adapter);
                  }
               }
            }
            
         });
         dlg.show(getChildFragmentManager(), CarInspectDialog.class.getName());
      }else if (item.getItemId() == R.id.menu_car_inspect_edit_location){
         
         CarInspectEditLocationDialog editDlg = CarInspectEditLocationDialog.newInstance(currentTask);
         editDlg.show(getChildFragmentManager(), CarInspectEditLocationDialog.class.getName());
         
      }
      else if (item.getItemId() == R.id.menu_car_inspect_reset_error_flag){
         if (lsView.getAdapter() instanceof CarReportListEntryAdapter)
         {
            if (currentRequestProduct instanceof JobRequestProduct)
            {
               currentRequestProduct.setcErrorType(0);
               currentRequestProduct.setcErrorText("");
               
               GlobalData.inspectDataObjectSavedList = null;
               GlobalData.inspectDataObjectTable = null;
               
               
               PSBODataAdapter dataAdapter = 
                     PSBODataAdapter.getDataAdapter(getActivity());
               dataAdapter.updateResetErrorTypeOfJobRequestProduct(currentRequestProduct);
               
               CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)lsView.getAdapter();
               //adapter.notifyDataSetChanged();
               //adapter.notifyDataSetInvalidated();
               
               adapter.notifyDataSetChanged();
               adapter.notifyDataSetInvalidated();

               
               lsView.setAdapter(adapter);
            }
         }
      }
      else if (item.getItemId() == R.id.menu_car_inspect_del_row){
         //AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
         //int index = info.position;
//         View view = info.targetView;
         
         
         if (lsView != null){
            if (lsView.getAdapter() instanceof CarReportListEntryAdapter){
               final CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)lsView.getAdapter();
//               Object obj = adapter.getItem(index);
               if (currentRequestProduct instanceof JobRequestProduct)
               {
                  final JobRequestProduct rowSelected = currentRequestProduct;
                  final PSBODataAdapter dataAdapter = 
                        PSBODataAdapter.getDataAdapter(getActivity());
                  
                  try {
                     final boolean allowDelete = true;
                     if (dataAdapter.hasCarJobRequestProductOneRow(rowSelected))
                     {
                        /*
                         * show alert
                         */
                        MessageBox.showMessageWithConfirm(getActivity(),
                              this.getString(R.string.text_warning_title), this.getString(R.string.text_alert_error_delete_car_duplicated),
                              new MessageBox.MessageConfirmListener(){

                                 @Override
                                 public void onConfirmed(MessageConfirmType confirmType) {
                                    // TODO Auto-generated method stub
                                    if (confirmType == MessageConfirmType.OK){
                                       if (allowDelete){
                                          int rowEffected = dataAdapter.deleteJobRequestProduct(rowSelected);
                                          if (rowEffected > 0)
                                          {
                                             /*
                                              * alert delete complete
                                              */
                                             adapter.deleteRowAt(rowSelected);
                                             

                                             //CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)lsView.getAdapter();
                                             //adapter.notifyDataSetChanged();
                                             //adapter.notifyDataSetInvalidated();
                                             lsView.setAdapter(adapter);
                                             
                                             MessageBox.showSaveCompleteMessage(getActivity());

                                          }                        
                                       }
                                    }
                                 }
                           
                        });
                     }else{
                        if (allowDelete)
                        {
                           int rowEffected = dataAdapter.deleteJobRequestProduct(rowSelected);
                           if (rowEffected > 0)
                           {
                              /*
                               * alert delete complete
                               */
                              adapter.deleteRowAt(rowSelected);
                              MessageBox.showSaveCompleteMessage(getActivity());
                              
                              // adapter = (CarReportListEntryAdapter)lsView.getAdapter();
                              //adapter.notifyDataSetChanged();
                              //adapter.notifyDataSetInvalidated();
                              lsView.setAdapter(adapter);

                           }                        
                        }
                     }
           
                  }
                  catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
            }
         }
      }
      return super.onContextItemSelected(item);
   }

   @Override
   public void onClick(View v) {
      // TODO Auto-generated method stub
      int id = v.getId();
      
      if (id == R.id.btn_car_inspect_search){
         if (columnFilter != null)
         {
            int idx = columnFilter.getSelectedItemPosition();
            String textSearch = edtSearch.getText().toString();
            //if (!textSearch.isEmpty())
            {
               /*search in filter*/
               if (lsView != null)
               {
//                  lsView.setFilterText(filterText)
                  CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)lsView.getAdapter();
                  if (adapter.getFilter() != null){
                     ((BaseFilterable)adapter.getFilter()).setColIdx(idx);
                     if (idx == 6){
                        if (vSub != null){
                           RadioButton rdoHas = (RadioButton)vSub.findViewById(R.id.chkbox_report_list_entry_column_chkbox_has);
                           RadioButton rdoNoHas = (RadioButton)vSub.findViewById(R.id.chkbox_report_list_entry_column_chkbox_no_has);
                           textSearch = "";
                           
                           if (rdoHas.isChecked()){
                              textSearch = "Y";
                           }
                           if (rdoNoHas.isChecked()){
                              textSearch = "N";
                           }
                        }
                     }else if ((idx == 7)||(idx == 8)||(idx == 10)||(idx == 11))
                     {
                        if (vSub != null){
                           CheckBox chk = (CheckBox)vSub;
                           if (chk.isChecked())
                           {
                              textSearch = "Y";
                           }else{
                              textSearch = "N";
                           }
                        }
                     }else if (idx == 9){
                        if (vSub != null)
                        {
                           Button btn = (Button)vSub;
                           textSearch = btn.getText().toString();
                        }
                     }else if (idx == 12)
                     {
                        if (vSub != null)
                        {
                           ReasonSentenceSpinner spinner = (ReasonSentenceSpinner)vSub;
                           ReasonSentence rs = (ReasonSentence)spinner.getSelectedItem();
                           ((CarFilter)adapter.getFilter()).setReasonID(rs.getReasonID());
                           textSearch = rs.getReasonText()+"";
                        }
                     }else if (idx == 15){
                        if (vSub != null){
                           HistoryInspectLocationSpinner spinner = (HistoryInspectLocationSpinner)vSub;
                           CarInspectStampLocation carLoc = (CarInspectStampLocation)spinner.getSelectedItem();
                           ((CarFilter)adapter.getFilter()).setWarehouseID(carLoc.getCustomerSurveySiteID());
                           textSearch = carLoc.getSiteAddress();
                        }
                     }else if (idx == 17){
                        if (vSub != null){
                           LayoutSpinner spinner = (LayoutSpinner)vSub;
                           InspectDataObjectSaved objSaved = (InspectDataObjectSaved)spinner.getSelectedItem();
                           ((CarFilter)adapter.getFilter()).setInspectDataObjectID(objSaved.getInspectDataObjectID());
                           textSearch = objSaved.getObjectName()+" ignored this parameters";
                        }
                     }
                     if (edtSearch.getVisibility() == View.VISIBLE)
                     {
                        if (!textSearch.isEmpty()){
                           //lsView.setFilterText(textSearch);    
                           
                           if (lsView.getAdapter() instanceof CarReportListEntryAdapter){
                              ((CarReportListEntryAdapter)lsView.getAdapter()).getFilter().filter(textSearch);
                           }
                           
                        }else{
                           //lsView.setFilterText("");
                           //lsView.clearTextFilter();
                           if (lsView.getAdapter() instanceof CarReportListEntryAdapter){
                              ((CarReportListEntryAdapter)lsView.getAdapter()).getFilter().filter("");
                           }

                        }                     
                     }else{
                        ///lsView.setFilterText(textSearch);
                        if (lsView.getAdapter() instanceof CarReportListEntryAdapter){
                           ((CarReportListEntryAdapter)lsView.getAdapter()).getFilter().filter(textSearch);
                        }
                     }
                  }
               }
            }
//            else{
//            }
         }
      }else{
//         lsView.setFilterText("");
//         lsView.clearTextFilter();
         if (lsView.getAdapter() instanceof CarReportListEntryAdapter){
            ((CarReportListEntryAdapter)lsView.getAdapter()).getFilter().filter("");
         }
      }
    
   }

   @Override
   public void onTakeCamera(JobRequestProduct type) {
      // TODO Auto-generated method stub
      /*
       argument.putParcelableArrayList(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_SAVED, dataSaved);
                argument.putString(InstanceStateKey.KEY_ARGUMENT_TASK_CODE, this.currentTask.getTaskCode());
                argument.putInt(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY_ID, this.customerSurveySite.getCustomerSurveySiteID());
                ActivityUtil.startNewActivityWithResult(getActivity(),
                        InspectPhotoEntryActivity.class, 
                        argument, 
                        InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY);
       */
      currentJobRequestProduct = type;
      Bundle argument = new Bundle();
      
      argument.putString(InstanceStateKey.KEY_ARGUMENT_TASK_CODE, InspectReportListFragment.currentTask.getTaskCode());
      argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, InspectReportListFragment.jobRequest);
      argument.putInt(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY_ID, InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID());
      argument.putInt(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID, type.getPhotoSetID());
      argument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST, type);
      ActivityUtil.startNewActivityWithResult(getActivity(),
            InspectPhotoEntryActivity.class, 
            argument, 
            InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY);
   }

   /* (non-Javadoc)
    * @see com.epro.psmobile.fragment.InspectReportListFragment#onActivityResult(int, int, android.content.Intent)
    */
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      // TODO Auto-generated method stub
      super.onActivityResult(requestCode, resultCode, data);
      switch (requestCode) {
         case InstanceStateKey.RESULT_INSPECT_PHOTO_ENTRY:{
             if (resultCode == Activity.RESULT_OK)
             {
                 int photoId = data.getIntExtra(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_PHOTOS_ID, -1);
                 if (currentJobRequestProduct != null)
                 {
                    currentJobRequestProduct.setPhotoSetID(photoId);
                    /*
                     * update current jobrequestProduct
                     */
                    /*
                     * update
                     */
                    try {
                       /*
                          super.getDataAdapter().updateInspectDataObjectSaved(
                                currentItemState.getInspectDataObjectSaved());*/
                       super.getDataAdapter().updateCarPhotoSetIDInJobRequestProduct(currentJobRequestProduct);
                       
                       CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)(lsView.getAdapter());
                       
                       adapter.notifyDataSetChanged();
                       adapter.notifyDataSetInvalidated();
/*                       
                       CarReportListEntryAdapter adapter = (CarReportListEntryAdapter)(lsView.getAdapter());
                       for(int i = 0; i < adapter.getCount();i++){
                          JobRequestProduct jp = (JobRequestProduct)adapter.getItem(i);
                          if (jp.getcVin().equalsIgnoreCase(string))
                       }*/
                    }
                    catch (Exception e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                       MessageBox.showMessage(getActivity(),
                             R.string.message_box_title_error,
                             e.getMessage());                          
                    }
                 }
             }
         }
      }
   }

   @Override
   public void onShowContextMenu(View v, JobRequestProduct type) {
      // TODO Auto-generated method stub
      this.currentRequestProduct = type;
      this.getActivity().openContextMenu(this.lsView);
   }

   @Override
   protected void onPhotoSetIdUpdated(JobRequestProduct currentJobRequestProduct) {
      // TODO Auto-generated method stub
      
   }

 
}

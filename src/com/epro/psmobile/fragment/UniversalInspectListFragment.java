package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.OnColumnInputChangeListener;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.MessageBox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class UniversalInspectListFragment extends InspectReportListFragment implements OnTakeCameraListener<JobRequestProduct> {

   //private View currentView;
   //private JobRequest jobRequest;
   //private Task task;
   //private CustomerSurveySite site;
   
   /*
   public static UniversalInspectListFragment newInstance(JobRequest jobRequest,
         Task currentTask,
         CustomerSurveySite site){
      UniversalInspectListFragment f = new UniversalInspectListFragment();
      Bundle bArgument = new Bundle();
      bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
      bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, currentTask);
      bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, site);
      f.setArguments(bArgument);
      return f;
   }*/
   
   /*
    * save to local variable use with Take photos
    **/
   @SuppressWarnings("unused")
   private JobRequestProduct currentJobRequestProduct;
   
   public UniversalInspectListFragment() {
      // TODO Auto-generated constructor stub
   }

   /* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
    */
   /*
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      //return super.onCreateView(inflater, container, savedInstanceState);
      currentView = inflater.inflate(R.layout.universal_inspect_list_view_fragment, container,false);

      this.setHasOptionsMenu(true);
      
      initial(currentView);
      

      return currentView;
   }*/
   /*
    * called from base classes
    */
   protected void initial(final View v)
   {
      
      TextView tvCustomerName = (TextView)currentView.findViewById(R.id.tv_universal_customer_name);
      tvCustomerName.setText(InspectReportListFragment.customerSurveySite.getCustomerName()+"\r\n" +
      		""+customerSurveySite.getSiteAddress());
      
      ListView ls = (ListView)v.findViewById(R.id.universal_lv_report);
      //ls.setAnimationCacheEnabled(false);
      //ls.setScrollingCacheEnabled(false);
      

      //super.doPopupCheckIn();
      
      PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
      try{
         InspectJobMapper jobMapper = 
               dataAdapter.getInspectJobMapper(jobRequest.getJobRequestID(), currentTask.getTaskCode());
         
         if (jobMapper != null)
         {
            if (!jobMapper.isAudit()){
               final Button btnAddNoAudit = (Button)v.findViewById(R.id.btn_add_no_audit);
               btnAddNoAudit.setVisibility(View.VISIBLE);
               btnAddNoAudit.setOnClickListener(new OnClickListener(){

                  @Override
                  public void onClick(View vClick) {
                     // TODO Auto-generated method stub
                     ListView ls = (ListView)v.findViewById(R.id.universal_lv_report);
                     //ls.setChildrenDrawingCacheEnabled(false);
                     //ls.setChildrenDrawnWithCacheEnabled(false);
                     if (ls.getAdapter() instanceof UniversalListEntryAdapter){
                        btnAddNoAudit.setEnabled(false);
                        ((UniversalListEntryAdapter)(ls.getAdapter())).addNewRowNoAudit();
                     }
                  }
                  
               });
            }
            ArrayList<InspectFormView>  formViewList = 
                  dataAdapter.getInspectFormViewList(jobMapper.getInspectFormViewID());
            if (formViewList != null){
               setupHeader(v,formViewList);  
            }

            
            /*
             * 
             */
            int rowCount = dataAdapter.getRowCountOfJobRequestProduct(jobRequest.getJobRequestID(), customerSurveySite.getCustomerSurveySiteID());
            int pages = rowCount % InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE;
            if (pages == 0){
               pages = rowCount / InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE;
            }else
            {
               pages = ((rowCount - pages)/InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE) + 1;
            }
            /*
             * Create Fragment page controller
             */
            
            jobRequestProducts = dataAdapter.findJobRequestProductsByJobRequestIDWithSiteID(jobRequest.getJobRequestID(),
                  customerSurveySite.getCustomerSurveySiteID(),
                  InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE,
                  0);
            
            setupList(v,formViewList,jobRequestProducts,jobMapper.isAudit());
         }
      }catch(Exception ex){
         MessageBox.showMessage(getSherlockActivity(), 
               R.string.message_box_title_error, ex.getMessage());
      }
   }
   private void setupHeader(View vRoot,ArrayList<InspectFormView> inspectViewList){
      ViewGroup headerContainer = (ViewGroup)vRoot.findViewById(R.id.universal_col_container);
      headerContainer.removeAllViews();
      
      
      for(InspectFormView formView : inspectViewList)
      {
         LayoutInflater  inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View v = inflater.inflate(R.layout.ps_activity_report_list_entry_column_v2, null);
         
         float colWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
               100, 
               this.getSherlockActivity().getResources().getDisplayMetrics());
         if (formView.getColWidth() >= 0){
            colWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                  formView.getColWidth(), 
                  this.getSherlockActivity().getResources().getDisplayMetrics());
         }
    
         TextView tvHeader = (TextView)v.findViewById(R.id.tv_car_list_header);
         tvHeader.getLayoutParams().width = (int) colWidth;
         tvHeader.setText(formView.getColTextDisplay());
         if (formView.isColHidden()){
            v.setVisibility(View.GONE);
         }
         headerContainer.addView(v);
      }
   }
   private void setupList(final View vRoot,ArrayList<InspectFormView> inspectViewList,
         ArrayList<JobRequestProduct> jobRequestProductList,boolean isAudit){
      final ListView ls = (ListView)vRoot.findViewById(R.id.universal_lv_report);
      ls.setScrollingCacheEnabled(false);
      ls.setCacheColorHint(Color.parseColor("#00000000"));
      final UniversalListEntryAdapter adapter = 
            new UniversalListEntryAdapter(getSherlockActivity(),
                  jobRequest,
                  currentTask,
                  customerSurveySite,
                  inspectViewList,jobRequestProductList,isAudit);
      adapter.setOnTakeCameraListener(this);
      adapter.setColumnInputChangeListener(new OnColumnInputChangeListener(){

         @Override
         public void onColumnInputChanged(View target,int index) {
            // TODO Auto-generated method stub
            /*
            ListView list = ls;
            int start = list.getFirstVisiblePosition();
            for(int i=start, j=list.getLastVisiblePosition();i<=j;i++)
                if(target==list.getItemAtPosition(i))
                {
                    View view = list.getChildAt(i-start);
                    list.getAdapter().getView(i, view, list);
                    break;
                }*/
            
         }

         @Override
         public void onRowSaved(JobRequestProduct jrp) {
            // TODO Auto-generated method stub
            try{
               saveSingleRowData(jrp);
            }catch(Exception ex)
            {
               ex.printStackTrace();
            }finally{
            }
         }

         @Override
         public void onReload(int position) {
            // TODO Auto-generated method stub
            //ls.invalidateViews();
            //if (position >= 0)
            //   ls.getChildAt(position);
            ((UniversalListEntryAdapter)ls.getAdapter()).notifyDataSetChanged();
            
            //View v = ls.getAdapter().getView(position, null, ls);
            //onModified(v,position);
            
            Button btnAddNoAudit = (Button)vRoot.findViewById(R.id.btn_add_no_audit);
            btnAddNoAudit.setEnabled(true);
            
         }

         @Override
         public void onModified(View target,int position) {
            // TODO Auto-generated method stub
          // ((UniversalListEntryAdapter)ls.getAdapter()).notifyDataSetChanged();
            /*
            if (position >= 0){
               View v = ls.getChildAt(position);
               if (v != null){
                  v.requestLayout();
               }
            }
            */
            try{
            ListView list = ls;
            int start = list.getFirstVisiblePosition();
            for(int i=start, j=list.getLastVisiblePosition();i<=j;i++)
                if(target==list.getItemAtPosition(i))
                {
                    View view = list.getChildAt(i-start);
                    list.getAdapter().getView(i, view, list);
                    break;
                }
            }catch(Exception ex){
               ex.printStackTrace();
            }
         }
         
      });
      ls.setAdapter(adapter);
   }

   @Override
   public void onTakeCamera(JobRequestProduct type) {
      // TODO Auto-generated method stub
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
    * @see android.support.v4.app.Fragment#startActivityForResult(android.content.Intent, int)
    */
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data){
      // TODO Auto-generated method stub
      super.onActivityResult(requestCode, resultCode, data);
      final ListView ls = (ListView)currentView.findViewById(R.id.universal_lv_report);      
      super.doActivityResultForTakePhoto(requestCode, 
            resultCode, 
            data, 
            currentJobRequestProduct, ls,false);
   }

   private boolean saveSingleRowData(JobRequestProduct jrp){
      boolean bRet = false;
      PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
      try {
         dataAdapter.insertSingleRowUniversalJobRequestProduct(InspectReportListFragment.jobRequest.getJobRequestID(),
                        InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID(),
                        jrp);
         bRet = true;
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return bRet;
   }
   @Override
   protected boolean saveAllData() {
      // TODO Auto-generated method stub
      boolean bRet = false;
      ListView ls = (ListView)currentView.findViewById(R.id.universal_lv_report);   
      if (ls.getAdapter() instanceof UniversalListEntryAdapter){
         PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
         
         UniversalListEntryAdapter adapter = (UniversalListEntryAdapter)ls.getAdapter();
         if (adapter.getAllJobRequestProducts() != null)
         {
            /*insert all*/
            try {
               /*
                * save all column 
                */
               dataAdapter.insertUniversalJobRequestProduct(InspectReportListFragment.jobRequest.getJobRequestID(),
                     InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID(),
                     adapter.getAllJobRequestProducts());
               bRet = true;
            }
            catch (Exception e) {
               // TODO Auto-generated catch block
               MessageBox.showMessage(getSherlockActivity(), 
                     getActivity().getString(R.string.text_error_title),
                     e.getMessage());   
               e.printStackTrace();
            }
         }else{
            bRet = true;/*first time get all jobRequestProducts*/
         }
         
      }
      return bRet;
   }

   @Override
   protected void onListViewUpdated() {
      // TODO Auto-generated method stub
      /*
       * final ViewGroup vGroupHeader = (ViewGroup)currentView.findViewById(R.id.list_view_header);
      lsView = (ListView)currentView.findViewById(R.id.lv_report_list);
      
      setupHeader(vGroupHeader);
      setupListView(lsView);
      initialControls(currentView);
       */
      initial(currentView);
   }
}

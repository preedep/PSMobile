package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.OnColumnInputChangeListener;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class UniversalInspectListFragmentItem extends InspectReportListFragment implements OnTakeCameraListener<JobRequestProduct> {

   @SuppressWarnings("unused")
   private JobRequestProduct currentJobRequestProduct;
   
   public interface OnDataReloadCompleted{
      void onReloadComplete();
   }
   private OnDataReloadCompleted dataReloadCompleted;
   
   public UniversalInspectListFragmentItem() {
      // TODO Auto-generated constructor stub
   }

   @Override
   protected void initial(View currentView) {
      // TODO Auto-generated method stub
      //ListView ls = (ListView)currentView.findViewById(R.id.universal_lv_report);
      PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
      try{
         InspectJobMapper jobMapper = 
               dataAdapter.getInspectJobMapper(jobRequest.getJobRequestID(), currentTask.getTaskCode());
         
         if (jobMapper != null)
         {
            ArrayList<InspectFormView>  formViewList = 
                  dataAdapter.getInspectFormViewList(jobMapper.getInspectFormViewID());
            jobRequestProducts = dataAdapter.findJobRequestProductsByJobRequestIDWithSiteID(jobRequest.getJobRequestID(),
                  customerSurveySite.getCustomerSurveySiteID(),
                  InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE,
                  this.rowOffset);
            
            setupList(currentView,formViewList,
                     jobRequestProducts,
                     jobMapper.isAudit()
                  );
         }
      }catch(Exception ex){
         ex.printStackTrace();
      }
   }

   @Override
   protected boolean saveAllData() {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   protected void onListViewUpdated() {
      // TODO Auto-generated method stub

   }

   
   @SuppressWarnings("unused")
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
            if (dataReloadCompleted != null){
               dataReloadCompleted.onReloadComplete();
            }
            
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
   public void addNewRowNoAudit(int lastRowProductIdOfCurrentPage){
      if (currentView != null){
         final ListView ls = (ListView)currentView.findViewById(R.id.universal_lv_report);      
         if (ls.getAdapter() instanceof UniversalListEntryAdapter)
         {
            ((UniversalListEntryAdapter)(ls.getAdapter())).addNewRowNoAudit(lastRowProductIdOfCurrentPage);
         }
      }
   }

   public ArrayList<JobRequestProduct> getAllJobRequestProduct(){
      ListView ls = (ListView)currentView.findViewById(R.id.universal_lv_report);   
      if (ls.getAdapter() instanceof UniversalListEntryAdapter){
         //PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
         UniversalListEntryAdapter adapter = (UniversalListEntryAdapter)ls.getAdapter();
         return adapter.getAllJobRequestProducts();
      }
      return null;
   }
   public OnDataReloadCompleted getDataReloadCompleted() {
      return dataReloadCompleted;
   }

   public void setDataReloadCompleted(OnDataReloadCompleted dataReloadCompleted) {
      this.dataReloadCompleted = dataReloadCompleted;
   }
}

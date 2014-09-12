package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.OnColumnInputChangeListener;
import com.epro.psmobile.adapter.callback.OnOpenCommentActivity;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class UniversalInspectListFragmentItem extends InspectReportListFragment 
implements OnTakeCameraListener<JobRequestProduct> , OnOpenCommentActivity {

   @SuppressWarnings("unused")
   private JobRequestProduct currentJobRequestProduct;
   
   private  Activity activity = null;
   private  View rootView;;
   //private  ArrayList<JobRequestProduct> jrpList;
   private  ListView lvItems;
   
   static class Holder{
     View vContainer;
     Activity aActivity;
   };
   public interface OnDataReloadCompleted{
      void onReloadComplete(boolean isNewRow);
   }
   private OnDataReloadCompleted dataReloadCompleted;
   
   public UniversalInspectListFragmentItem() {
      // TODO Auto-generated constructor stub
   }

   @Override
   protected void initial(View currentView) {
      // TODO Auto-generated method stub
      //ListView ls = (ListView)currentView.findViewById(R.id.universal_lv_report);
      if (rootView == null){
         rootView = currentView;
      }
      
//      lvItems = (ListView)currentView.findViewById(R.id.universal_lv_report);
   }
   /* (non-Javadoc)
    * @see com.epro.psmobile.fragment.InspectReportListFragment#onActivityCreated(android.os.Bundle)
    */
   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      activity = getSherlockActivity();
//      if (rootView == null)
      rootView = currentView;
      if (currentView != null){
         lvItems = (ListView)currentView.findViewById(R.id.universal_lv_report);
      }
      super.onActivityCreated(savedInstanceState);
      if (this.rowOffset == 0){
         /*first page*/
         this.renderOnPageChanged(activity);
      }
      
   }
   public class AsyncRenderOnPageChanged extends AsyncTask<Holder,Void,InspectJobMapper>
   {
      private ArrayList<JobRequestProduct> jrpList;
      private ArrayList<InspectFormView>  formViewList = null;
      private InspectJobMapper jobMapper = null;
      private ProgressDialog dialog = null;
      private View vContainer;
      
      public AsyncRenderOnPageChanged(Activity activity)
      {
         try{
            dialog = new ProgressDialog(activity);
         }catch(Exception ex){}
      }
      /* (non-Javadoc)
       * @see android.os.AsyncTask#onPreExecute()
       */
      @Override
      protected void onPreExecute() {
         // TODO Auto-generated method stub
         super.onPreExecute();
         try{
            if (dialog != null){
               dialog.setMessage("Fetching..");
               dialog.show();
            }
         }catch(Exception ex){}
      }

      @Override
      protected InspectJobMapper doInBackground(Holder... params) {
         // TODO Auto-generated method stub
         try{
            jrpList = null;
            if (activity == null){
               return null;
            }
            Holder holder = params[0];
            vContainer = params[0].vContainer;
            
            PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(holder.aActivity);
            
            jobMapper = 
                  dataAdapter.getInspectJobMapper(jobRequest.getJobRequestID(), currentTask.getTaskCode());
            
            if (jobMapper != null)
            {
               formViewList = 
                     dataAdapter.getInspectFormViewList(jobMapper.getInspectFormViewID());
               jrpList = dataAdapter.findJobRequestProductsByJobRequestIDWithSiteID(jobRequest.getJobRequestID(),
                     customerSurveySite.getCustomerSurveySiteID(),
                     InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE,
                     rowOffset,keyFilter);
               
               
            }
         }catch(Exception ex){
            ex.printStackTrace();
         }
         return jobMapper;
      }

      /* (non-Javadoc)
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(InspectJobMapper result) {
         // TODO Auto-generated method stub
         super.onPostExecute(result);
         if (jobMapper != null)
         {

            int maxWidth = 0;
            if (formViewList != null){
               maxWidth = setupHeader(vContainer,formViewList);  
            }
            //if (jrpList != null)
            //{
               setupList(lvItems,formViewList,
                  jrpList,
                  jobMapper.isAudit(),maxWidth
               );
            //}
            try{
            if (dialog != null){
                  dialog.dismiss();
               }
            }catch(Exception ex){}
         }
         
         if (jrpList == null){
            if (dataReloadCompleted != null){
               dataReloadCompleted.onReloadComplete(true);
            }
         }
      }


   }
   public void renderOnPageChanged(Activity activity){
      Holder h = new Holder();
      h.vContainer = currentView;
      h.aActivity = activity;
      new AsyncRenderOnPageChanged(h.aActivity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,h);
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

   private int setupHeader(View vRoot,ArrayList<InspectFormView> inspectViewList){
      if (vRoot == null)return 0;
      
      ViewGroup headerContainer = (ViewGroup)vRoot.findViewById(R.id.universal_col_container);
      headerContainer.removeAllViews();
      
      int maxWidth = 0;
      for(InspectFormView formView : inspectViewList)
      {
         LayoutInflater  inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
         maxWidth += (int)colWidth;
         
         tvHeader.setText(formView.getColTextDisplay());
         if (formView.isColHidden()){
            v.setVisibility(View.GONE);
         }
         headerContainer.addView(v);
      }
      headerContainer.getLayoutParams().width = maxWidth;
      return maxWidth;
   }
   
   @SuppressWarnings("unused")
   private void setupList(ListView lvItems,
         ArrayList<InspectFormView> inspectViewList,
         ArrayList<JobRequestProduct> jobRequestProductList,
         boolean isAudit,int maxWidth)
   {
//      if (vRoot == null)return;
      if (lvItems == null)return;
      
      final ListView ls = lvItems;// (ListView)vRoot.findViewById(R.id.universal_lv_report);
      ls.getLayoutParams().width = maxWidth;
      
      if (ls.getAdapter() instanceof UniversalListEntryAdapter){
         return; /*already binded*/
      }
      
      ls.setScrollingCacheEnabled(false);
      ls.setCacheColorHint(Color.parseColor("#00000000"));
      final UniversalListEntryAdapter adapter = 
            new UniversalListEntryAdapter(activity,
                  jobRequest,
                  currentTask,
                  customerSurveySite,
                  inspectViewList,
                  jobRequestProductList,
                  isAudit);
      adapter.setOnTakeCameraListener(this);
      adapter.setOpenCommentActivityListener(this);
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
         public void onReload(int position) 
         {
            // TODO Auto-generated method stub
            //ls.invalidateViews();
            //if (position >= 0)
            //   ls.getChildAt(position);
            //((UniversalListEntryAdapter)ls.getAdapter()).notifyDataSetChanged();
            
            //View v = ls.getAdapter().getView(position, null, ls);
            //onModified(v,position);
            if (dataReloadCompleted != null){
               dataReloadCompleted.onReloadComplete(false);
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
      if (lvItems != null)
      {
         final ListView ls = lvItems;//(ListView)rootView.findViewById(R.id.universal_lv_report);      
         super.doActivityResultForTakePhoto(requestCode, 
               resultCode, 
               data, 
               currentJobRequestProduct, ls,false);         
      }
   }

   private boolean saveSingleRowData(JobRequestProduct jrp){
      boolean bRet = false;
      PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(activity);
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
      if (lvItems != null){
         final ListView ls = lvItems;// (ListView)rootView.findViewById(R.id.universal_lv_report);      
         if (ls.getAdapter() instanceof UniversalListEntryAdapter)
         {
            ((UniversalListEntryAdapter)(ls.getAdapter())).addNewRowNoAudit(lastRowProductIdOfCurrentPage);
            //jrpList = ((UniversalListEntryAdapter)(ls.getAdapter())).getAllJobRequestProducts();
         }else{
            /*
             * isAudit is false
             * first time no record ls don't have Adapter
             */
            
         }
      }
   }

   public ArrayList<JobRequestProduct> getAllJobRequestProduct(){
      /*
      if (rootView != null){
         ListView ls = (ListView)rootView.findViewById(R.id.universal_lv_report);   
         if (ls.getAdapter() instanceof UniversalListEntryAdapter){
            //PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
            UniversalListEntryAdapter adapter = (UniversalListEntryAdapter)ls.getAdapter();
            return adapter.getAllJobRequestProducts();
         }
      }
      return null;*/
      //return jrpList;
      if (lvItems != null){
         ListView ls = lvItems;// (ListView)rootView.findViewById(R.id.universal_lv_report);   
         if (ls.getAdapter() instanceof UniversalListEntryAdapter){
            UniversalListEntryAdapter adapter = (UniversalListEntryAdapter)ls.getAdapter();
            return adapter.getAllJobRequestProducts();
         }         
      }
      return null;
   }
   public OnDataReloadCompleted getDataReloadCompleted() {
      return dataReloadCompleted;
   }

   public void setDataReloadCompleted(OnDataReloadCompleted dataReloadCompleted) {
      this.dataReloadCompleted = dataReloadCompleted;
   }

   @Override
   protected void onPhotoSetIdUpdated(JobRequestProduct currentJobRequestProduct) {
      // TODO Auto-generated method stub
      /*notification*/
      if (lvItems != null){
         ListView ls = lvItems;// (ListView)rootView.findViewById(R.id.universal_lv_report);   
         if (ls.getAdapter() instanceof UniversalListEntryAdapter){
            UniversalListEntryAdapter adapter = (UniversalListEntryAdapter)ls.getAdapter();
            adapter.refreshAtRowOfJobRequestProduct(currentJobRequestProduct);
            adapter.notifyDataSetChanged();
         }         
      }
   }

   @Override
   public void onOpenCommentActivity(ArrayList<InspectFormView> colProperties, JobRequestProduct type) {
      // TODO Auto-generated method stub
      super.doOpenUniversalComment(type, colProperties);
   }
}

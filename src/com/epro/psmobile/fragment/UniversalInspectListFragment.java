package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.OnColumnInputChangeListener;
import com.epro.psmobile.adapter.UniversalListPageFragmentAdapter;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.fragment.UniversalInspectListFragmentItem.OnDataReloadCompleted;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.MessageBox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("CutPasteId")
public class UniversalInspectListFragment extends InspectReportListFragment implements OnTakeCameraListener<JobRequestProduct>, OnClickListener {

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
   private int pages;
   
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
         final PSBODataAdapter dataAdapter = 
               PSBODataAdapter.getDataAdapter(getSherlockActivity());
            
      
         TextView tvCustomerName = (TextView)currentView.findViewById(R.id.tv_universal_customer_name);
         if (tvCustomerName != null){
                  tvCustomerName.setText(InspectReportListFragment.customerSurveySite.getCustomerName()+"\r\n" +
               ""+customerSurveySite.getSiteAddress());
         }
         //ListView ls = (ListView)v.findViewById(R.id.universal_lv_report);
         //ls.setAnimationCacheEnabled(false);
         //ls.setScrollingCacheEnabled(false);
         

         //super.doPopupCheckIn();
         
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
                       final ViewPager viewPager = (ViewPager)v.findViewById(R.id.universal_pager);
                        int currentViewIdx = viewPager.getCurrentItem();  
                        boolean addToNewPage = false;
                        int rowCount = 0;
                        try {
                            rowCount = dataAdapter.getRowCountOfJobRequestProduct(jobRequest.getJobRequestID(),
                                 customerSurveySite.getCustomerSurveySiteID())+1;
                           
                           int tmp_pages = rowCount % InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE;
                           if (tmp_pages == 0){
                              tmp_pages = rowCount / InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE;
                           }else
                           {
                              tmp_pages = ((rowCount - tmp_pages)/InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE) + 1;
                           }
                           
                           if (tmp_pages > pages){
                              pages = tmp_pages;
                              addToNewPage = true;
                           }
                           
                        }
                        catch (Exception e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                        }
                        
                        if (!addToNewPage){
                           if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                              UniversalListPageFragmentAdapter adapter =
                                    (UniversalListPageFragmentAdapter)viewPager.getAdapter();
                              Fragment f = adapter.getItem(currentViewIdx);
                              if ( f instanceof UniversalInspectListFragmentItem){
                                 UniversalInspectListFragmentItem universal_f = (UniversalInspectListFragmentItem)f;
                                 btnAddNoAudit.setEnabled(false);
                                 
                                 View vgNavigator = v.findViewById(R.id.ll_page_navigator);
                                 vgNavigator.setVisibility(View.GONE);
                                 if (pages > 1){
                                    vgNavigator.setVisibility(View.VISIBLE);
                                    Button btnNext = (Button)vgNavigator.findViewById(R.id.btn_nav_next);
                                    Button btnPrev = (Button)vgNavigator.findViewById(R.id.btn_nav_back);
                                    btnNext.setEnabled(false);
                                    btnPrev.setEnabled(false);
                                 }
                                 viewPager.setCurrentItem(adapter.getCount()-1);
                                 
                                 
                                 int lastRowProductId = 0;
                                 if (universal_f.getAllJobRequestProduct() != null){
                                    lastRowProductId = 
                                          universal_f.getAllJobRequestProduct().get(universal_f.getAllJobRequestProduct().size()-1).getProductRowID();
                                 }
                                 universal_f.addNewRowNoAudit(lastRowProductId);
                              }
                           }                           
                        }else{
                           if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                              
                              btnAddNoAudit.setEnabled(false);
                              
                              View vgNavigator = v.findViewById(R.id.ll_page_navigator);
                              vgNavigator.setVisibility(View.GONE);
                              if (pages > 1){
                                 vgNavigator.setVisibility(View.VISIBLE);
                                 Button btnNext = (Button)vgNavigator.findViewById(R.id.btn_nav_next);
                                 Button btnPrev = (Button)vgNavigator.findViewById(R.id.btn_nav_back);
                                 btnNext.setEnabled(false);
                                 btnPrev.setEnabled(false);
                              }
                              
                              final UniversalListPageFragmentAdapter adapter =
                                    (UniversalListPageFragmentAdapter)viewPager.getAdapter();
                              UniversalInspectListFragmentItem f = 
                                    (UniversalInspectListFragmentItem)InspectReportListFragment.newInstance(jobRequest, 
                                          currentTask, 
                                          customerSurveySite,
                                          (rowCount - 1) * (InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE), true);
                              
                              f.setDataReloadCompleted(new OnDataReloadCompleted(){

                                 @Override
                                 public void onReloadComplete() {
                                    // TODO Auto-generated method stub
                                    Button btnAddNoAudit = (Button)v.findViewById(R.id.btn_add_no_audit);
                                    btnAddNoAudit.setEnabled(true);              
                                    
                                    View vgNavigator = v.findViewById(R.id.ll_page_navigator);
                                    vgNavigator.setVisibility(View.GONE);
                                    if (pages > 1){
                                       vgNavigator.setVisibility(View.VISIBLE);
                                       Button btnNext = (Button)vgNavigator.findViewById(R.id.btn_nav_next);
                                       Button btnPrev = (Button)vgNavigator.findViewById(R.id.btn_nav_back);
                                       btnNext.setEnabled(true);
                                       btnPrev.setEnabled(true);
                                       
                                       
                                       
                                       String strTextCurrentPageNo = 
                                             getString(R.string.text_current_page, adapter.getCount(),pages);
                                       TextView tvCurrentPageNo = (TextView)currentView.findViewById(R.id.tv_current_page_no);
                                       tvCurrentPageNo.setText(strTextCurrentPageNo);
                                    
                                       
                                       
                                    }
                                    if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                                       UniversalListPageFragmentAdapter adapter =
                                             (UniversalListPageFragmentAdapter)viewPager.getAdapter();
                                       viewPager.setCurrentItem(adapter.getCount()-1);
                                    }
                                 }
                                 
                              });
                              adapter.append(f);
                              adapter.notifyDataSetChanged();
                              viewPager.setCurrentItem(adapter.getCount()-1);
                              int lastRowProductId = 0;
                              
                              try {
                                 ArrayList<JobRequestProduct> jrpList = 
                                       dataAdapter.findJobRequestProductsByJobRequestID(jobRequest.getJobRequestID(),
                                       customerSurveySite.getCustomerSurveySiteID());
                                 if (jrpList != null){
                                    lastRowProductId = jrpList.get(jrpList.size()-1).getProductRowID();
                                 }
                              }
                              catch (Exception e) {
                                 // TODO Auto-generated catch block
                                 e.printStackTrace();
                              }
                              f.addNewRowNoAudit(lastRowProductId);
                           }
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
               pages = rowCount % InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE;
               if (pages == 0){
                  pages = rowCount / InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE;
               }else
               {
                  pages = ((rowCount - pages)/InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE) + 1;
               }
               /*
                * Create Fragment page controller
                */
               /*
               jobRequestProducts = dataAdapter.findJobRequestProductsByJobRequestIDWithSiteID(jobRequest.getJobRequestID(),
                     customerSurveySite.getCustomerSurveySiteID(),
                     InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE,
                     0);
               
               setupList(v,formViewList,jobRequestProducts,jobMapper.isAudit());*/
               
               
               
               View vgNavigator = v.findViewById(R.id.ll_page_navigator);
               vgNavigator.setVisibility(View.GONE);
               if (pages > 1){
                  vgNavigator.setVisibility(View.VISIBLE);
                  Button btnNext = (Button)vgNavigator.findViewById(R.id.btn_nav_next);
                  Button btnPrev = (Button)vgNavigator.findViewById(R.id.btn_nav_back);
                  btnNext.setOnClickListener(this);
                  btnPrev.setOnClickListener(this);
               }
               
               
               ////////////
               final ViewPager viewPager = (ViewPager)v.findViewById(R.id.universal_pager);
               viewPager.setOnPageChangeListener(new OnPageChangeListener(){

                  @Override
                  public void onPageScrollStateChanged(int arg0) {
                     // TODO Auto-generated method stub
                     
                  }

                  @Override
                  public void onPageScrolled(int arg0, float arg1, int arg2) {
                     // TODO Auto-generated method stub
                     
                  }

                  @Override
                  public void onPageSelected(int arg0) {
                     // TODO Auto-generated method stub
                     if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                        UniversalListPageFragmentAdapter adapter = (UniversalListPageFragmentAdapter)viewPager.getAdapter();
                        Fragment f = adapter.getItem(arg0);
                        if (f instanceof UniversalInspectListFragmentItem){
                           ((UniversalInspectListFragmentItem)f).renderOnPageChanged();
                        }
                     }
                  }
                  
               });
               String strTextCurrentPageNo = 
                     this.getString(R.string.text_current_page, viewPager.getCurrentItem()+1,pages);
               TextView tvCurrentPageNo = (TextView)currentView.findViewById(R.id.tv_current_page_no);
               tvCurrentPageNo.setText(strTextCurrentPageNo);

               
               ArrayList<Fragment> fragments = 
                     new ArrayList<Fragment>();
               
               for(int i = 0; i < pages ;i++){
                  Fragment f = 
                        InspectReportListFragment.newInstance(jobRequest, 
                              currentTask, 
                              customerSurveySite,
                              i * (InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE), true);
                  if (f instanceof UniversalInspectListFragmentItem){
                     UniversalInspectListFragmentItem universal_f = (UniversalInspectListFragmentItem)f;
                     universal_f.setDataReloadCompleted(new OnDataReloadCompleted(){

                        @Override
                        public void onReloadComplete() {
                           // TODO Auto-generated method stub
                           Button btnAddNoAudit = (Button)v.findViewById(R.id.btn_add_no_audit);
                           btnAddNoAudit.setEnabled(true);              
                           
                           View vgNavigator = v.findViewById(R.id.ll_page_navigator);
                           vgNavigator.setVisibility(View.GONE);
                           if (pages > 1){
                              vgNavigator.setVisibility(View.VISIBLE);
                              Button btnNext = (Button)vgNavigator.findViewById(R.id.btn_nav_next);
                              Button btnPrev = (Button)vgNavigator.findViewById(R.id.btn_nav_back);
                              btnNext.setEnabled(true);
                              btnPrev.setEnabled(true);
                           }
                           if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                              UniversalListPageFragmentAdapter adapter =
                                    (UniversalListPageFragmentAdapter)viewPager.getAdapter();
                              viewPager.setCurrentItem(adapter.getCount()-1);
                           }
                           
                        }
                     });

                  }
                  fragments.add(f);
               }
               UniversalListPageFragmentAdapter lpFragment = new UniversalListPageFragmentAdapter(
                     this.getSherlockActivity().getSupportFragmentManager(),fragments);
               viewPager.setAdapter(lpFragment);
               if (fragments.size() > 0){
                  viewPager.setCurrentItem(0);/*default first time*/
                  /*
                  UniversalInspectListFragmentItem f = (UniversalInspectListFragmentItem)fragments.get(0);
                  f.renderOnPageChanged();*/
               }
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
   


   @Override
   protected boolean saveAllData() {
      // TODO Auto-generated method stub
      boolean bRet = false;
      
      ArrayList<JobRequestProduct> jobRequestProductList = 
             new ArrayList<JobRequestProduct>();
      
      
      ViewPager viewPager = (ViewPager)currentView.findViewById(R.id.universal_pager);
      if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
         UniversalListPageFragmentAdapter adapter =
               (UniversalListPageFragmentAdapter)viewPager.getAdapter();
         for(int i = 0; i < adapter.getCount();i++)
         {
            Fragment f = adapter.getItem(i);
            if ( f instanceof UniversalInspectListFragmentItem){
               UniversalInspectListFragmentItem universal_f = (UniversalInspectListFragmentItem)f;
               if (universal_f.getAllJobRequestProduct() != null){
                  jobRequestProductList.addAll(universal_f.getAllJobRequestProduct());
               }
            }            
         }
      }
      
     // if (ls.getAdapter() instanceof UniversalListEntryAdapter)
      {
         PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
         
         if (jobRequestProductList.size() > 0 )
         {
            /*insert all*/
            try {
               /*
                * save all column 
                */
               dataAdapter.insertUniversalJobRequestProduct(InspectReportListFragment.jobRequest.getJobRequestID(),
                     InspectReportListFragment.customerSurveySite.getCustomerSurveySiteID(),
                     jobRequestProductList);
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

   @Override
   public void onTakeCamera(JobRequestProduct type) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void onClick(View v) {
      // TODO Auto-generated method stub
      ViewPager viewPager = (ViewPager)currentView.findViewById(R.id.universal_pager);
      int currentViewIdx = viewPager.getCurrentItem();  
      
      
      
      int currentPage = 0;
      int id = v.getId();
      switch(id){
         case R.id.btn_nav_next:{
            if (currentViewIdx < pages){
               viewPager.setCurrentItem(currentViewIdx + 1);
            }
         }break;
         case R.id.btn_nav_back:{
            if (currentViewIdx > 0){
               viewPager.setCurrentItem(currentViewIdx - 1);               
            }
         }break;
      }
      
      currentPage = viewPager.getCurrentItem();
      String strTextCurrentPageNo = 
            this.getString(R.string.text_current_page, currentPage+1,pages);
      TextView tvCurrentPageNo = (TextView)currentView.findViewById(R.id.tv_current_page_no);
      tvCurrentPageNo.setText(strTextCurrentPageNo);

   }
}

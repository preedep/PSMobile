package com.epro.psmobile.fragment;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.InspectPhotoEntryActivity;
import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.OnColumnInputChangeListener;
import com.epro.psmobile.adapter.UniversalListEntryAdapter.UniversalControlType;
import com.epro.psmobile.adapter.UniversalListPageFragmentAdapter;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductGroup;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.fragment.UniversalInspectListFragmentItem.OnDataReloadCompleted;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.view.LayoutSpinner;
import com.epro.psmobile.view.ProductGroupSpinner;
import com.epro.psmobile.view.ProductSpinner;
import com.epro.psmobile.view.UniversalListFormViewColSpinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
   private String keyFilter = "";
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
         /*
         int currentPage = 0;
         String strTextCurrentPageNo = 
               this.getString(R.string.text_current_page, currentPage+1,pages);
         TextView tvCurrentPageNo = (TextView)currentView.findViewById(R.id.tv_current_page_no);
         tvCurrentPageNo.setText(strTextCurrentPageNo);*/
         //super.doPopupCheckIn();
         
         try{
            InspectJobMapper jobMapper = 
                  dataAdapter.getInspectJobMapper(jobRequest.getJobRequestID(), currentTask.getTaskCode());
            
            if (jobMapper != null)
            {
               ArrayList<InspectFormView>  formViewList = 
                     dataAdapter.getInspectFormViewList(jobMapper.getInspectFormViewID());
               if (formViewList != null){
                  setupFilterPanel(currentView, formViewList);
               }
               
               ////////////////////
               if (!jobMapper.isAudit())
               {
                  final Button btnAddNoAudit = (Button)v.findViewById(R.id.btn_add_no_audit);
                  if (!keyFilter.isEmpty()){
                     btnAddNoAudit.setVisibility(View.GONE);
                  }else{
                     btnAddNoAudit.setVisibility(View.VISIBLE);
                  }
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
                                 customerSurveySite.getCustomerSurveySiteID(),keyFilter)+1;
                           
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
                        
                        if (!addToNewPage)
                        {
                           if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                              UniversalListPageFragmentAdapter adapter =
                                    (UniversalListPageFragmentAdapter)viewPager.getAdapter();
                              
                              if (viewPager.getCurrentItem() != adapter.getCount()-1)
                              {
                                 /*if current page is not last page i'll move to last page first*/
                                 viewPager.setCurrentItem(adapter.getCount()-1);
                                 
                                 
                                 int currentPage = viewPager.getCurrentItem();
                                 String strTextCurrentPageNo = 
                                       getString(R.string.text_current_page, currentPage+1,pages);
                                 TextView tvCurrentPageNo = (TextView)currentView.findViewById(R.id.tv_current_page_no);
                                 tvCurrentPageNo.setText(strTextCurrentPageNo);

                              }else{
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
                                    
                                    int lastRowProductId = 0;
                                    if (universal_f.getAllJobRequestProduct() != null){
                                       lastRowProductId = 
                                             universal_f.getAllJobRequestProduct().get(universal_f.getAllJobRequestProduct().size()-1).getProductRowID();
                                    }
                                    universal_f.addNewRowNoAudit(lastRowProductId);
                                 }

                              }
                           }                           
                        }else{
                           if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter)
                           {
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
                              final UniversalInspectListFragmentItem f = 
                                    (UniversalInspectListFragmentItem)InspectReportListFragment.newInstance(jobRequest, 
                                          currentTask, 
                                          customerSurveySite,
                                          (pages - 1) * (InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE), true,keyFilter);
                              
                              f.setDataReloadCompleted(new OnDataReloadCompleted(){

                                 @Override
                                 public void onReloadComplete(boolean isNewRow) 
                                 {
                                    // TODO Auto-generated method stub
                                    Button btnAddNoAudit = (Button)v.findViewById(R.id.btn_add_no_audit);
                                    btnAddNoAudit.setEnabled(true);              
                                    
                                    View vgNavigator = v.findViewById(R.id.ll_page_navigator);
                                    vgNavigator.setVisibility(View.GONE);
                                    if (pages > 1)
                                    {
                                       vgNavigator.setVisibility(View.VISIBLE);
                                       Button btnNext = (Button)vgNavigator.findViewById(R.id.btn_nav_next);
                                       Button btnPrev = (Button)vgNavigator.findViewById(R.id.btn_nav_back);
                                       btnNext.setEnabled(true);
                                       btnPrev.setEnabled(true);
                                       btnNext.setOnClickListener(UniversalInspectListFragment.this);
                                       btnPrev.setOnClickListener(UniversalInspectListFragment.this);
                                       
                                       
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
                                    
                                    if (isNewRow)
                                    {
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
               /*
               ArrayList<InspectFormView>  formViewList = 
                     dataAdapter.getInspectFormViewList(jobMapper.getInspectFormViewID());
               if (formViewList != null){
                  setupHeader(v,formViewList);  
               }*/

               
               /*
                * 
                */
               int rowCount = dataAdapter.getRowCountOfJobRequestProduct(
                     jobRequest.getJobRequestID(), 
                     customerSurveySite.getCustomerSurveySiteID(),keyFilter);
               
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
               viewPager.setOnTouchListener(new OnTouchListener(){

                  @Override
                  public boolean onTouch(View v, MotionEvent event) {
                     // TODO Auto-generated method stub
                     switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                           viewPager.requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                           viewPager.requestDisallowInterceptTouchEvent(false);
                            break;
                        }
                     
                     return true;
                  }
                  
               });
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
                           ((UniversalInspectListFragmentItem)f).renderOnPageChanged(getSherlockActivity());
                        }
                     }
                  }
                  
               });
               String strTextCurrentPageNo = 
                     this.getString(R.string.text_current_page, 1,pages);/*first time default to page 1*/
               TextView tvCurrentPageNo = (TextView)currentView.findViewById(R.id.tv_current_page_no);
               tvCurrentPageNo.setText(strTextCurrentPageNo);

               
               ArrayList<Fragment> fragments = 
                     new ArrayList<Fragment>();
               
               if (rowCount == 0){
                  /*should be no audit*/
                  //i'll set to first page
                  pages = 1;
               }
               for(int i = 0; i < pages ;i++)
               {
                  final Fragment f = 
                        InspectReportListFragment.newInstance(jobRequest, 
                              currentTask, 
                              customerSurveySite,
                              i * (InstanceStateKey.UNIVERSAL_MAX_ROW_PER_PAGE), true,keyFilter);
                  if (f instanceof UniversalInspectListFragmentItem){
                     UniversalInspectListFragmentItem universal_f = (UniversalInspectListFragmentItem)f;
                     universal_f.setDataReloadCompleted(new OnDataReloadCompleted(){

                        @Override
                        public void onReloadComplete(boolean isNewRow) 
                        {
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
                              
                              btnNext.setOnClickListener(UniversalInspectListFragment.this);
                              btnPrev.setOnClickListener(UniversalInspectListFragment.this);

                           }
                           if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                              UniversalListPageFragmentAdapter adapter =
                                    (UniversalListPageFragmentAdapter)viewPager.getAdapter();
                              viewPager.setCurrentItem(adapter.getCount()-1);
                           }
                           
                           
                           if (isNewRow)
                           {
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
                              ((UniversalInspectListFragmentItem) f).addNewRowNoAudit(lastRowProductId);
                           }
                        }
                     });

                  }
                  fragments.add(f);
               }

               if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
                  ((UniversalListPageFragmentAdapter)viewPager.getAdapter()).clearAll();
                   viewPager.setAdapter(null);
               }
               UniversalListPageFragmentAdapter lpFragment = new UniversalListPageFragmentAdapter(
                        this.getChildFragmentManager(),fragments);
               viewPager.setAdapter(lpFragment);
                  if (fragments.size() > 0){
                     viewPager.setCurrentItem(0);/*default first time*/
                  }                  
            }
         }catch(Exception ex){
            MessageBox.showMessage(getSherlockActivity(), 
                  R.string.message_box_title_error, ex.getMessage());
         }       
   }
  


   @Override
   protected boolean saveAllData() {
      // TODO Auto-generated method stub
      boolean bRet = false;
      
      ArrayList<JobRequestProduct> jobRequestProductList = 
             new ArrayList<JobRequestProduct>();
      
      
      ViewPager viewPager = (ViewPager)currentView.findViewById(R.id.universal_pager);
      if (viewPager != null){
         if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter)
         {
            UniversalListPageFragmentAdapter adapter =
                  (UniversalListPageFragmentAdapter)viewPager.getAdapter();
            for(int i = 0; i < adapter.getCount();i++)
            {
              // String name = makeFragmentName(viewPager.getId(),i);
               //Fragment viewPagerFragment = getChildFragmentManager().findFragmentByTag(name);
               
               //Fragment f = viewPagerFragment;//adapter.getItem(i);
               Fragment f = adapter.getItem(i);
               if ( f instanceof UniversalInspectListFragmentItem){
                  UniversalInspectListFragmentItem universal_f = (UniversalInspectListFragmentItem)f;
                  if (universal_f.getAllJobRequestProduct() != null){
                     jobRequestProductList.addAll(universal_f.getAllJobRequestProduct());
                  }
               }            
            }
         }        
      }
      
     // if (ls.getAdapter() instanceof UniversalListEntryAdapter)
     // {
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
         
     // }
      return bRet;
   }
   private static String makeFragmentName(int viewId, int position) {
      return "android:switcher:" + viewId + ":" + position;
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
         case R.id.btn_universal_col_search:{
            doSearch();
         }break;
         case R.id.btn_universal_col_clear_result:{
            doClear();
         }break;
      }
      
      currentPage = viewPager.getCurrentItem();
      String strTextCurrentPageNo = 
            this.getString(R.string.text_current_page, currentPage+1,pages);
      TextView tvCurrentPageNo = (TextView)currentView.findViewById(R.id.tv_current_page_no);
      tvCurrentPageNo.setText(strTextCurrentPageNo);

   }

   /* (non-Javadoc)
    * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
    */
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      // TODO Auto-generated method stub
      super.onActivityResult(requestCode, resultCode, data);
      
      ViewPager viewPager = (ViewPager)currentView.findViewById(R.id.universal_pager);
      int currentViewIdx = viewPager.getCurrentItem();  
      if (viewPager.getAdapter() instanceof UniversalListPageFragmentAdapter){
         UniversalListPageFragmentAdapter adapter = (UniversalListPageFragmentAdapter)viewPager.getAdapter();
         UniversalInspectListFragmentItem f = (UniversalInspectListFragmentItem)adapter.getRegisteredFragment(currentViewIdx);
         f.onActivityResult(requestCode, resultCode, data);
      }
   }
   
   @SuppressWarnings("unused")
   private void setupFilterPanel(final View root,ArrayList<InspectFormView> colList){
      Button btnSearch = (Button)root.findViewById(R.id.btn_universal_col_search);
      Button btnClear = (Button)root.findViewById(R.id.btn_universal_col_clear_result);
      btnSearch.setOnClickListener(this);
      btnClear.setOnClickListener(this);
      UniversalListFormViewColSpinner  colFilterSpinner = (UniversalListFormViewColSpinner)root.findViewById(R.id.sp_universal_col_filter);
      colFilterSpinner.initial(colList);
      colFilterSpinner.setSelection(0);
      
      colFilterSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            InspectFormView col = (InspectFormView)parent.getSelectedItem();
            setupCriterialInput(root,col);
         }

         @Override
         public void onNothingSelected(AdapterView< ? > arg0) {
            // TODO Auto-generated method stub
            
         }
         
      });
   }
   private void doSearch(){
      initial(currentView);
   }
   private void doClear(){
      keyFilter = "";
      initial(currentView);
   }
   private void setupCriterialInput(View root,
         final InspectFormView col){
      UniversalControlType ctrlType = UniversalControlType.getControlType(col.getColType());

      ViewGroup vContainer = 
            (ViewGroup)root.findViewById(R.id.universal_col_filter_criterial_container);
      vContainer.removeAllViews();
      View v = null;
      
      int heightCtrl = 
            (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, this.getSherlockActivity().getResources().getDisplayMetrics());
      
      switch(ctrlType)
      {
         case Label:
         case SimpleText:
         {
            v = View.inflate(getSherlockActivity(), R.layout.ps_activity_report_list_entry_column_text, null);
            EditText edt = (EditText)v.findViewById(R.id.et_report_list_entry_column_text);
            edt.getLayoutParams().width = vContainer.getLayoutParams().width;
            edt.getLayoutParams().height = heightCtrl;//root.getLayoutParams().height;
            
            
            edt.addTextChangedListener(new TextWatcher(){

               @Override
               public void afterTextChanged(Editable s) {
                  // TODO Auto-generated method stub
//                  keyFilter = s.toString();
                  String colName = col.getColInvokeField();
                  keyFilter = colName+" like '%"+s.toString()+"%'";
                  Log.d("DEBUG_D_D_D", keyFilter);
               }

               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                  // TODO Auto-generated method stub
                  
               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {
                  // TODO Auto-generated method stub
                  
               }
               
            });
         }break;
         case ProductType:
         {
            v = View.inflate(getSherlockActivity(), R.layout.ps_activity_report_list_entry_column_product_group_spinner, null);
            final ProductGroupSpinner pgSpinner = 
                  (ProductGroupSpinner)v.findViewById(R.id.sp_product_group);
            pgSpinner.getLayoutParams().width = vContainer.getLayoutParams().width;
            pgSpinner.getLayoutParams().height = heightCtrl;//root.getLayoutParams().height;
            
            pgSpinner.initial(jobRequest.getJobRequestID());
            pgSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

               @Override
               public void onItemSelected(AdapterView< ? > arg0, View arg1, int arg2, long arg3) {
                  // TODO Auto-generated method stub
                  ProductGroup pg = (ProductGroup)pgSpinner.getProductGroups().get(arg2);
                  String colName = col.getColInvokeField();
                  if ((colName == null)||(colName.isEmpty())){
                     colName = "productGroup";
                  }
                  keyFilter = colName+" = '"+pg.getProductGroupName()+"'";
                  Log.d("DEBUG_D_D_D", keyFilter);
                  
               }

               @Override
               public void onNothingSelected(AdapterView< ? > arg0) {
                  // TODO Auto-generated method stub
                  
               }
               
            });
         }break;
         case Product:{
            v = View.inflate(getSherlockActivity(), R.layout.ps_activity_report_list_entry_column_product_spinner, null);
            final ProductSpinner pdSPinner = 
                  (ProductSpinner)v.findViewById(R.id.sp_product);
            pdSPinner.getLayoutParams().width = vContainer.getLayoutParams().width;
            pdSPinner.getLayoutParams().height = heightCtrl;//root.getLayoutParams().height;
            
            pdSPinner.initialWithJobRequestID(jobRequest.getJobRequestID());
            pdSPinner.setOnItemSelectedListener(new OnItemSelectedListener(){

               @Override
               public void onItemSelected(AdapterView< ? > arg0, View arg1, int arg2, long arg3) {
                  // TODO Auto-generated method stub
                  Product pg = (Product)pdSPinner.getProducts().get(arg2);
                  String colName = col.getColInvokeField();
                  if ((colName == null)||(colName.isEmpty())){
                     colName = "productId";
                  }
                  keyFilter = colName+" = "+pg.getProductID()+"";
                  Log.d("DEBUG_D_D_D", keyFilter);
                  
               }

               @Override
               public void onNothingSelected(AdapterView< ? > arg0) {
                  // TODO Auto-generated method stub
                  
               }
               
            });
         }break;
         case Layout:{
            v = View.inflate(getSherlockActivity(), R.layout.ps_activity_report_list_entry_column_layout_spinner, null);
            LayoutSpinner layoutSpinner = (LayoutSpinner)v.findViewById(R.id.sp_layout_inspect);
            layoutSpinner.getLayoutParams().width = vContainer.getLayoutParams().width;
            layoutSpinner.getLayoutParams().height = heightCtrl;//root.getLayoutParams().height;
            
            layoutSpinner.initial(currentTask.getTaskCode(), customerSurveySite.getCustomerSurveySiteID());
            layoutSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

               @Override
               public void onItemSelected(AdapterView< ? > arg0, View arg1, int arg2, long arg3) {
                  // TODO Auto-generated method stub
                  InspectDataObjectSaved pg = (InspectDataObjectSaved)arg0.getSelectedItem();
                  String colName = col.getColInvokeField();
                  if ((colName == null)||(colName.isEmpty())){
                     colName = "inspectDataObjectID";
                  }
                  keyFilter = colName+" = "+pg.getInspectDataObjectID()+"";
                  Log.d("DEBUG_D_D_D", keyFilter);
                  
               }

               @Override
               public void onNothingSelected(AdapterView< ? > arg0) {
                  // TODO Auto-generated method stub
                  
               }
               
            });
         }break;
      }
      if (v != null){
         //if (v.getLayoutParams() == null){
         //   v.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
         //}
         vContainer.addView(v);
      }
   }
}

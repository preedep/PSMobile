package com.epro.psmobile.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import com.epro.psmobile.R;
import com.epro.psmobile.adapter.callback.OnOpenCommentActivity;
import com.epro.psmobile.adapter.callback.OnTakeCameraListener;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CarInspectStampLocation;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.MarketPrice;
import com.epro.psmobile.data.MarketPriceFinder;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductAmountUnit;
import com.epro.psmobile.data.ProductGroup;
import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.TaskControlTemplate.TaskControlType;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.MathEval;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.view.HistoryInspectLocationSpinner;
import com.epro.psmobile.view.LayoutSpinner;
import com.epro.psmobile.view.ProductGroupSpinner;
import com.epro.psmobile.view.ProductSpinner;
import com.epro.psmobile.view.ProductUnitSpinner;
import com.epro.psmobile.view.ReasonSentenceSpinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

public class UniversalListEntryAdapter extends BaseAdapter  {

   /*
    * - 0 : simpleText (single line)
- 1 : simpleText (decimal)
- 2 : date
- 3 : checkbox
- 4 : radio
- 5 : dropdown
- 6 : camera
- 7 : layout
- 8 : checkListForm ( อันนี้จะวิ่งไปดูค่า taskFormTemplateID ) 
- 9 : productType   
-10: product
-11: marketPrice

    */
   
   public enum UniversalControlType
   {
       SimpleText(0),
       SimpleTextDecimal(1),
       SimpleTextDate(2),
       CheckBox(3),
       RadioBox(4),
       DropdownList(5),
       Camera(6),
       Layout(7),
       CheckListForm(8),
       ProductType(9),
       Product(10),
       MarketPrice(11),
       Label(12),
       DateInput(13),
       DateTimeInput(14),
       ProductUnit(15),
       GodownList(16),
       DeleteRow(17);
       
       
       
       private int code;
       UniversalControlType(int code)
       {
           this.code = code;
       }
       
       public int getCode(){
           return this.code;
       }
       public static UniversalControlType getControlType(int code){
           for(UniversalControlType item : values()){
               if (code == item.getCode()){
                   return item;
               }
           }
           return UniversalControlType.SimpleText;
       }
   }
   private Context context;
   
   static class Holder{
      View[] viewRows;
      boolean shown;
      int position = -1;
   };

   static class EachColViewHolder{
      int position;
      View viewCol;
      View viewRow;
      JobRequestProduct jrp;
      InspectFormView formView;
      Object value;
   };
   private ArrayList<JobRequestProduct> jobRequestProductList;
   private ArrayList<InspectFormView>  formViewList;
   private LayoutInflater inflater;
   
   private JobRequest jobRequest;
   private Task task;
   private CustomerSurveySite site;
   
   private PSBODataAdapter dataAdapter;
   private int lastPosition; //after add new
   
   private MarketPriceFinder marketPriceGlobalFinder;
   private MarketPriceFinder marketPriceForProvince;
   
   private int rowAtNeedToReload = -1;
   
   private boolean needToAllRedraw = false;
   private boolean isAudit;
   public interface OnColumnInputChangeListener{
      void onColumnInputChanged(View target,int rowPosition);
      void onRowSaved(JobRequestProduct jrp);
      void onReload(int position);
      void onModified(View viewRow,int position);
   }
   public interface OnRowContextOpenListener{
      void onClickContextOpen(View view,JobRequestProduct jrp);
   }
   
   private OnColumnInputChangeListener columnInputChangeListener;
   private OnOpenCommentActivity openCommentActivityListener;
   private OnRowContextOpenListener rowContextOpenListener;
   
   private long mLastClickTime;
   
   class UniversalTextWatcher implements TextWatcher{
   
      //private JobRequestProduct jobRequestProduct;
      private EditText editText;
      private View viewCol;
      private int position;
      //private String previousText;
      private View viewRow;
      private AsyncCalculate syncCaluclate;
      
      @Override
      public void afterTextChanged(Editable s) {
         // TODO Auto-generated method stub
         
      }
   
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         // TODO Auto-generated method stub
         //calculateOnTextChanged(s.toString());
         //previousText = s.toString();
         
      }
   
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
         // TODO Auto-generated method stub
         if (!s.toString().isEmpty())
         {
            int inputType = editText.getInputType();
            if (inputType == (InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL))
            {
               double d = 0;
               try{
                  d = Double.parseDouble(s.toString());
               }catch(Exception ex){}
               if (d > 0)
               {
                  calculateOnTextChanged(s.toString());
               }               
            }else{
               View vParent = (View)editText.getParent();
               if (vParent.getTag() != null)
               {
                  
                  InspectFormView formView = (InspectFormView)vParent.getTag();
                  final JobRequestProduct jrp = jobRequestProductList.get(this.getPosition());
                  
                  invokeSetValue(jrp,
                        formView,
                        s.toString());
              }
            }
         }
      }
      
      private void calculateOnTextChanged(final String s){
         {
           
            try{
               View vParent = (View)editText.getParent();
               if (vParent.getTag() != null)
               {
                  
                  InspectFormView formView = (InspectFormView)vParent.getTag();
                  final JobRequestProduct jrp = jobRequestProductList.get(this.getPosition());
                  
                  invokeSetValue(jrp,
                        formView,
                        s);
                  
                  //Log.d("DEBUG_D_D",getPosition()+" Active -> Comment -> "+jrp.getRemark());

                  if (viewCol.getTag() != null)
                  {
                      // if current col isn't decimal value i'll ignore calculate by formula
                      
                     
                     if (isArgumentOfGetterMethodIsDecimal(jrp,formView.getColInvokeField()))
                     {
                        calculateByFormula(jrp,formView,
                              (Holder)viewCol.getTag());
                        
                        /*
                         * re set value to control
                         */
                       // displayEachRow(viewCol,position);
                        Holder holder = (Holder)viewCol.getTag();
                        for(View vItem_col  : holder.viewRows){
                           InspectFormView inspectViewForm = (InspectFormView)vItem_col.getTag();
                           UniversalControlType ctrlType = UniversalControlType.getControlType(
                                 inspectViewForm.getColType());
                           if (ctrlType == UniversalControlType.SimpleTextDecimal){
                              if (!inspectViewForm.isColEditable())
                              {
                                 if ((inspectViewForm.getColFormula() != null)&&(!inspectViewForm.getColFormula().isEmpty())){
                                    EditText edt = 
                                          (EditText)vItem_col.findViewById(R.id.et_report_list_entry_column_text);
                                    
                                    Object value = 
                                          invokeGetValue(jrp,inspectViewForm);
                                    edt.setText(setFormat(value,inspectViewForm));
                                    break;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }catch(Exception ex){
               ex.printStackTrace();
            }
            

         }
      }
   
      public EditText getEditText() {
         return editText;
      }
   
      public void setEditText(EditText editText) {
         this.editText = editText;
      }
   
      public View getViewCol() {
         return viewCol;
      }
   
      public void setViewCol(View viewCol) {
         this.viewCol = viewCol;
      }
   
      public int getPosition() {
         return position;
      }
   
      public void setPosition(int poisition) {
         this.position = poisition;
      }
   
      public View getViewRow() {
         return viewRow;
      }
   
      public void setViewRow(View viewRow) {
         this.viewRow = viewRow;
      }
   }
   
   class AsyncCalculate extends AsyncTask<EachColViewHolder,Void,String>
   {

      private EachColViewHolder holder;
      private EditText edtText;
      private JobRequestProduct jrp;
      private InspectFormView inspectViewForm;
      @Override
      protected String doInBackground(EachColViewHolder... params) {
         // TODO Auto-generated method stub
         holder = params[0];
         String value = "";
         InspectFormView formView = holder.formView;
         
        jrp = jobRequestProductList.get(holder.position);
         
         invokeSetValue(jrp,
               formView,
               holder.value);
         
         if (isArgumentOfGetterMethodIsDecimal(jrp,formView.getColInvokeField()))
         {
            calculateByFormula(jrp,formView,
                  (Holder)holder.viewCol.getTag());
            
            /*
             * re set value to control
             */
           // displayEachRow(viewCol,position);
            //Holder holder = (Holder).getTag();
            Holder colHolder = (Holder)holder.viewCol.getTag();
            for(View vItem_col  : colHolder.viewRows)
            {
               inspectViewForm = (InspectFormView)vItem_col.getTag();
               UniversalControlType ctrlType = UniversalControlType.getControlType(
                     inspectViewForm.getColType());
               if (ctrlType == UniversalControlType.SimpleTextDecimal)
               {
                  value = inspectViewForm.getColDefaultValue();
                  //if (!inspectViewForm.isColEditable())
                  //{
                     if ((inspectViewForm.getColFormula() != null)&&(!inspectViewForm.getColFormula().isEmpty()))
                     {
                        EditText edt = 
                              (EditText)vItem_col.findViewById(R.id.et_report_list_entry_column_text);
                        edtText = edt;
                        Object retObj = invokeGetValue(jrp,inspectViewForm);
                        value = setFormat(
                              retObj,
                              inspectViewForm);
                        break;
                     }
                  //}
               }
            }
         }
         return value;
      }

      /* (non-Javadoc)
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(String result) {
         // TODO Auto-generated method stub
         super.onPostExecute(result);
         if (edtText != null)
         {
            edtText.setText(result);
         }
      }

   }
   public UniversalListEntryAdapter(Context context,
         JobRequest jobRequest,
         Task task,
         CustomerSurveySite site,
         ArrayList<InspectFormView>  formViewList,
         ArrayList<JobRequestProduct> jobRequestProductList,boolean isAudit) {
      // TODO Auto-generated constructor stub
      this.context = context;
      this.jobRequestProductList = jobRequestProductList;
//      this.jobRequestProductList.addAll(jobRequestProductList);
      this.formViewList = formViewList;
      this.jobRequest = jobRequest;
      this.task = task;
      this.site = site;
      this.isAudit = isAudit;
      inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      
      dataAdapter = PSBODataAdapter.getDataAdapter(context);
      try {
         marketPriceForProvince = dataAdapter.getMarketPriceFinderBy(site.getProvinceID());
         marketPriceGlobalFinder = dataAdapter.getMarketPriceFinderBy(184/*fix*/);
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Override
   public int getCount() {
      // TODO Auto-generated method stub
      if (jobRequestProductList == null)
         return 0;
      return jobRequestProductList.size();
   }

   @Override
   public Object getItem(int position) {
      // TODO Auto-generated method stub
      return jobRequestProductList.get(position);
   }

   @Override
   public long getItemId(int position) {
      // TODO Auto-generated method stub
      return position;
   }

   abstract class SpinnerHolder {
      protected View viewRow;
      protected int position;
      /**
       * @return the viewRow
       */
      public View getViewRow() {
         return viewRow;
      }

      /**
       * @param viewRow the viewRow to set
       */
      public void setViewRow(View viewRow) {
         this.viewRow = viewRow;
      }

      /**
       * @return the position
       */
      public int getPosition() {
         return position;
      }

      /**
       * @param position the position to set
       */
      public void setPosition(int position) {
         this.position = position;
      }     
   }
   class ProductTypeSelectImpl extends SpinnerHolder implements OnItemSelectedListener
   {
      @Override
      public void onItemSelected(AdapterView<?> parent, 
            View v,
            int pos, 
            long id) {
         // TODO Auto-generated method stub
         
         try{
            if (parent instanceof ProductGroupSpinner)
            {
               final ProductGroupSpinner pgs = (ProductGroupSpinner)parent;
               ProductGroup productGroup = 
                     pgs.getProductGroups().get(pos);
               
               jobRequestProductList.get(position).setProductGroupID(productGroup.getProductGroupID());
               jobRequestProductList.get(position).setProductGroup(productGroup.getProductGroupName());
               
               Log.d("DEBUG_D_D_D", "JobRequestProduct position -> "+position+" , "+productGroup.getProductGroupID()+" , "+productGroup.getProductGroupName());
               
               pgs.post(new Runnable(){

                  @Override
                  public void run() {
                     // TODO Auto-generated method stub
                     pgs.requestFocusFromTouch();
                     pgs.clearFocus();
                  }
                  
               });
               
               
               
               if (viewRow != null)
               {
                  if (viewRow.getTag() != null){
                     Holder holder = (Holder)viewRow.getTag();
                     for(View vEachCol : holder.viewRows)
                     {
                        if (vEachCol != null)
                        {
                           InspectFormView viewForm = (InspectFormView)vEachCol.getTag();
                           UniversalControlType ctrlType = UniversalControlType.getControlType(viewForm.getColType());
                           if (ctrlType == UniversalControlType.Product)
                           {
                              final ProductSpinner proSpinner = (ProductSpinner)vEachCol.findViewById(R.id.sp_product);
                              proSpinner.initial(productGroup.getProductGroupID());
                              break;
                           }
                        }
                     }
                  }
               }
               if (columnInputChangeListener != null){
                  columnInputChangeListener.onModified(viewRow,position);
               }
            }
         }catch(Exception ex){
            ex.printStackTrace();
         }
         

      }

      @Override
      public void onNothingSelected(AdapterView< ? > arg0) {
         // TODO Auto-generated method stub
         
      }

      
   }
   
   class ProductSelectImpl  extends SpinnerHolder implements OnItemSelectedListener{

     
      @Override
      public void onItemSelected(AdapterView<?> parent, 
            View v,
            int pos, 
            long id) {
         // TODO Auto-generated method stub
         
         try{
            if (parent instanceof ProductSpinner)
            {
               final ProductSpinner productSpinner = (ProductSpinner)parent;
               final Product currentProduct =
                     productSpinner.getProducts().get(pos);
               
               
               Log.d("DEBUG_D_D_D", "Product select -> "+currentProduct.getProductID()+" , "+currentProduct.getProductName());
               jobRequestProductList.get(position).setProductId(currentProduct.getProductID());
               jobRequestProductList.get(position).setProductName(currentProduct.getProductName());
               Log.d("DEBUG_D_D_D", "Set Product id -> object["+position+"] "+jobRequestProductList.get(position).getProductId());
               
               
               productSpinner.post(new Runnable(){

                  @Override
                  public void run() {
                     // TODO Auto-generated method stub
                     productSpinner.requestFocusFromTouch();
                     productSpinner.clearFocus();
                  }
                  
               });
               
               
               
               
               if (viewRow != null)
               {
                  if (viewRow.getTag() != null){
                     Holder holder = (Holder)viewRow.getTag();
                     for(View vEachCol : holder.viewRows){
                        InspectFormView viewForm = (InspectFormView)vEachCol.getTag();
                        UniversalControlType ctrlType = UniversalControlType.getControlType(viewForm.getColType());
                        
                        EachColViewHolder eachColViewHolder = new EachColViewHolder();
                        eachColViewHolder.formView = viewForm;
                        eachColViewHolder.jrp =jobRequestProductList.get(position);
                        eachColViewHolder.position = position;
                        eachColViewHolder.viewCol = vEachCol;                        
                        eachColViewHolder.viewRow = viewRow;
                        
                        if (ctrlType == UniversalControlType.MarketPrice){
                           new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, eachColViewHolder);
                        }else if (ctrlType == UniversalControlType.ProductUnit){
                           new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, eachColViewHolder);                        
                        }
                     }
                  }
               }
               if (columnInputChangeListener != null){
                  columnInputChangeListener.onModified(viewRow,position);
               }
            }           
         }catch(Exception ex){
            ex.printStackTrace();
         }

      }

      @Override
      public void onNothingSelected(AdapterView< ? > arg0) {
         // TODO Auto-generated method stub
         
      }
   }
   class ProductUnitSelectImpl extends SpinnerHolder implements OnItemSelectedListener
   {

      @Override
      public void onItemSelected(AdapterView<?> parent, 
            View v,
            int pos, 
            long id) {
         // TODO Auto-generated method stub
         
         try{
            if (parent instanceof ProductUnitSpinner)
            {
               final ProductUnitSpinner productUnitSpinner = (ProductUnitSpinner)parent;
               ProductAmountUnit currentProductUnit =
                     productUnitSpinner.getProductAmountUnits().get(pos);
               jobRequestProductList.get(position).setProductUnit(currentProductUnit.getUnitName());
               
               productUnitSpinner.post(new Runnable(){

                  @Override
                  public void run() {
                     // TODO Auto-generated method stub
                     productUnitSpinner.requestFocusFromTouch();
                     productUnitSpinner.clearFocus();

                  }
                  
               });
               
            }
         }catch(Exception ex){
            ex.printStackTrace();
         }
      }

      @Override
      public void onNothingSelected(AdapterView< ? > arg0) {
         // TODO Auto-generated method stub
         
      }
      
   }
   class LocationSelectImpl extends SpinnerHolder implements OnItemSelectedListener
   {

      @Override
      public void onItemSelected(AdapterView<?> parent, 
            View v,
            int pos, 
            long id) {
         // TODO Auto-generated method stub
         
         try{
            if (parent instanceof HistoryInspectLocationSpinner)
            {
               final HistoryInspectLocationSpinner locationSpinner = (HistoryInspectLocationSpinner)parent;
               Object obj = locationSpinner.getSelectedItem();
               if (obj instanceof CarInspectStampLocation)
               {
                  CarInspectStampLocation dataObjSaved = (CarInspectStampLocation)obj;
                  jobRequestProductList.get(position).setCustomerSurveySiteID(dataObjSaved.getCustomerSurveySiteID());
                  
                  locationSpinner.post(new Runnable(){

                     @Override
                     public void run() {
                        // TODO Auto-generated method stub
                        locationSpinner.requestFocusFromTouch();
                        locationSpinner.clearFocus();
                     }
                  });
                   
                  
                  if ((jobRequest.getInspectType().getInspectTypeID() == 5)||
                      (jobRequest.getInspectType().getInspectTypeID() == 8)){
                     if (viewRow != null)
                     {
                        if (viewRow.getTag() != null){
                           Holder holder = (Holder)viewRow.getTag();
                           for(View vEachCol : holder.viewRows){
                              InspectFormView viewForm = (InspectFormView)vEachCol.getTag();
                              UniversalControlType ctrlType = UniversalControlType.getControlType(viewForm.getColType());
                              
                              EachColViewHolder eachColViewHolder = new EachColViewHolder();
                              eachColViewHolder.formView = viewForm;
                              eachColViewHolder.jrp =jobRequestProductList.get(position);
                              eachColViewHolder.position = position;
                              eachColViewHolder.viewCol = vEachCol;                        
                              eachColViewHolder.viewRow = viewRow;
                              
                              if (ctrlType == UniversalControlType.Layout){
                                 new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, eachColViewHolder);
                              }
                           }
                        }
                     }
                  }
               }
            } 
         }catch(Exception ex){
            ex.printStackTrace();
         }

      }

      @Override
      public void onNothingSelected(AdapterView< ? > arg0) {
         // TODO Auto-generated method stub
         
      }
      
   }
   class LayoutSelectImpl  extends SpinnerHolder implements OnItemSelectedListener
   {

      @Override
      public void onItemSelected(AdapterView<?> parent, 
            View v,
            int pos, 
            long id) {
         // TODO Auto-generated method stub
         try
         {
            if (parent instanceof LayoutSpinner)
            {
               final LayoutSpinner layoutSpinner = (LayoutSpinner)parent;
               Object obj = layoutSpinner.getSelectedItem();
               if (obj instanceof InspectDataObjectSaved)
               {
                  InspectDataObjectSaved dataObjSaved = (InspectDataObjectSaved)obj;
                  jobRequestProductList.get(position).setInspectDataObjectID(dataObjSaved.getInspectDataObjectID());
                  
                  
               // hide virtual keyboard
                  //InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                  //imm.hideSoftInputFromWindow(mYourEditText.getWindowToken(), 0);
                  //imm.hideSoftInputFromInputMethod(((Activity)context).
                  
                  layoutSpinner.post(new Runnable(){

                     @Override
                     public void run() {
                        // TODO Auto-generated method stub
                        layoutSpinner.requestFocusFromTouch();
                        layoutSpinner.clearFocus();
                     }
                     
                  });
               }
            }    
         }catch(Exception ex){
            ex.printStackTrace();
         }

      }

      @Override
      public void onNothingSelected(AdapterView< ? > arg0) {
         // TODO Auto-generated method stub

         
      }
      
   }
   ////////////////////
   @Override
   public View getView(final int position, final View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      
      View view = convertView;
      
      if (convertView == null)
      {
         final View viewRow = inflater.inflate(R.layout.ps_activity_report_list_entry_row, parent, false);
         viewRow.setLongClickable(true);
         view = viewRow;
         ViewGroup vGroup = (ViewGroup)viewRow;
         View vContainer = viewRow.findViewById(R.id.ll_report_list_entry_row_containers);
         
         Holder holder = new Holder();
         //holder.position = position;
         holder.viewRows = new View[formViewList.size()];
         for(int i = 0; i < formViewList.size();i++)
         {
            InspectFormView formView = formViewList.get(i);
            UniversalControlType ctrlType = UniversalControlType.getControlType(formView.getColType());
            
            float colWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                  100, 
                  context.getResources().getDisplayMetrics());
            
            int heightDp = 60;
            if ((jobRequest.getInspectType().getInspectTypeID() == 5)||(jobRequest.getInspectType().getInspectTypeID() == 8))
            {
               heightDp = 60;
            }
            float colHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                  heightDp, 
                  context.getResources().getDisplayMetrics());
            
            // float colHeight = LayoutParams.WRAP_CONTENT;
            
            if (formView.getColWidth() >= 0){
               colWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                     formView.getColWidth(), 
                     context.getResources().getDisplayMetrics());
            }
            
            switch(ctrlType)
            {
               case SimpleText:
               case SimpleTextDecimal:
               case SimpleTextDate:{
                  View vEdit = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_text, vGroup, false);
                  final EditText edt = (EditText)vEdit.findViewById(R.id.et_report_list_entry_column_text);
                  edt.getLayoutParams().width = (int)colWidth;
                  edt.getLayoutParams().height = (int)colHeight;
                  
                  holder.viewRows[i] = vEdit;
                  
                  if (ctrlType == UniversalControlType.SimpleTextDecimal){
                     edt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                  }
                  /*
                  if (holder.textWatcher != null){
                     edt.removeTextChangedListener(holder.textWatcher);
                  }
                  holder.textWatcher = new UniversalTextWatcher();
                  holder.textWatcher.setEditText(edt);
                  holder.textWatcher.setJobRequestProduct(jobRequestProductList.get(position));
                  holder.textWatcher.setViewCol(viewRow);                  
                  edt.addTextChangedListener(holder.textWatcher);*/
                  
               }break;
               case CheckBox:{
                  View vChkBox = inflater.inflate(R.layout.ps_activity_report_list_entry_column_chkbox, vGroup, false);
                  CheckBox chkBox = (CheckBox)vChkBox.findViewById(R.id.chkbox_report_list_entry_column_chkbox);
                  chkBox.getLayoutParams().width = (int)colWidth;
                  chkBox.getLayoutParams().height = (int)colHeight;
                  
                  chkBox.setGravity(Gravity.CENTER);
                  holder.viewRows[i] = vChkBox;
               }break;
               case RadioBox:{
                  View vRadioGroup = inflater.inflate(R.layout.ps_activity_report_list_entry_column_group_rdobox, vGroup, false);
                  ViewGroup vg = (ViewGroup)vRadioGroup.findViewById(R.id.rdo_group_chk_car);
                  vg.getLayoutParams().width = (int)colWidth;
                  vg.getLayoutParams().height = (int)colHeight;
                  
                  holder.viewRows[i] = vRadioGroup;
               }break;
               case DropdownList:{
                  View vSpinner = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_reason_spinner, vGroup, false);
                  ReasonSentenceSpinner sp_reasonSentence = (ReasonSentenceSpinner)vSpinner.findViewById(R.id.sp_reason_sentence);
                  sp_reasonSentence.getLayoutParams().width = (int)colWidth;
                  sp_reasonSentence.getLayoutParams().height = (int)colHeight;
                  
                  PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);
                  String reasonCode = formView.getReasonSentenceCode();
                  
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
                holder.viewRows[i] = vSpinner;  
                
               }break;
               case Camera:{
                  View vCamera = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_camera, vGroup, false);
                  //vCamera.getLayoutParams().width = (int)colWidth;
                  if (vCamera instanceof LinearLayout){
                     ((LinearLayout)vCamera).setGravity(Gravity.CENTER);
                  }
                  Button btn = (Button)vCamera.findViewById(R.id.btn_report_list_entry_column_camera);
                  btn.getLayoutParams().width = (int)colWidth;
                  btn.setText("");
                  btn.setGravity(Gravity.CENTER);
                  btn.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_device_access_camera,0,0,0);
                  
                  holder.viewRows[i] = vCamera;
               }break;
               case Layout:{
                  View vSpinner = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_layout_spinner, vGroup, false);
                  LayoutSpinner sp_layout = (LayoutSpinner)vSpinner.findViewById(R.id.sp_layout_inspect);
                  sp_layout.getLayoutParams().width = (int)colWidth;
                  sp_layout.getLayoutParams().height = (int)colHeight;
                 
                  sp_layout.setEnabled(formView.isColEditable());
                  sp_layout.setClickable(formView.isColEditable());
                  /*
                 try 
                 {
                    sp_layout.initial(task.getTaskCode(),
                          site.getCustomerSurveySiteID());
                 } 
                 catch (Exception e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
                 }*/
                 holder.viewRows[i] = vSpinner;
               }break;
               case CheckListForm:{
                  View vCheckListForm = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_camera, vGroup, false);
                  //vCamera.getLayoutParams().width = (int)colWidth;
                  if (vCheckListForm instanceof LinearLayout){
                     ((LinearLayout)vCheckListForm).setGravity(Gravity.CENTER);
                  }
                  Button btn = (Button)vCheckListForm.findViewById(R.id.btn_report_list_entry_column_camera);
                  btn.getLayoutParams().width = (int)colWidth;
                  btn.setText("");
                  btn.setGravity(Gravity.CENTER);
                  btn.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_list,0,0,0);
                  holder.viewRows[i] = vCheckListForm;
               }break;
               case DeleteRow:{
                  View vCheckListForm = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_camera, vGroup, false);
                  //vCamera.getLayoutParams().width = (int)colWidth;
                  if (vCheckListForm instanceof LinearLayout){
                     ((LinearLayout)vCheckListForm).setGravity(Gravity.CENTER);
                  }
                  Button btn = (Button)vCheckListForm.findViewById(R.id.btn_report_list_entry_column_camera);
                  btn.getLayoutParams().width = (int)colWidth;
                  btn.setText("-");
                  btn.setGravity(Gravity.CENTER);
                  holder.viewRows[i] = vCheckListForm;

               }break;
               case ProductType:{
                  View vSpinner = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_product_group_spinner, vGroup, false);
                  final ProductGroupSpinner  sp_productGroup = 
                        (ProductGroupSpinner)vSpinner.findViewById(R.id.sp_product_group);
                  sp_productGroup.getLayoutParams().width = (int)colWidth;
                  sp_productGroup.getLayoutParams().height = (int)colHeight;
                  sp_productGroup.setEnabled(formView.isColEditable());
                  sp_productGroup.setClickable(formView.isColEditable());

                  holder.viewRows[i] = vSpinner;
               }break;
               case Product:{
                  View vSpinner = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_product_spinner, vGroup, false);
                  final ProductSpinner sp_product = (ProductSpinner)vSpinner.findViewById(R.id.sp_product);
                  sp_product.getLayoutParams().width = (int)colWidth;
                  sp_product.getLayoutParams().height = (int)colHeight;
                  holder.viewRows[i] = vSpinner;
                  
                  sp_product.setEnabled(formView.isColEditable());
                  sp_product.setClickable(formView.isColEditable());

               }break;
               case ProductUnit:{
                  View vSpinner = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_product_unit_spinner, vGroup, false);
                  final ProductUnitSpinner sp_product_unit = (ProductUnitSpinner)vSpinner.findViewById(R.id.sp_product_unit);
                  sp_product_unit.getLayoutParams().width = (int)colWidth;
                  sp_product_unit.getLayoutParams().height = (int)colHeight;
                  
                  holder.viewRows[i] = vSpinner;
                  
                  sp_product_unit.setEnabled(formView.isColEditable());
                  sp_product_unit.setClickable(formView.isColEditable());

               }break;
               case MarketPrice:{
                  View vEdit = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_text, vGroup, false);
                  EditText edt = (EditText)vEdit.findViewById(R.id.et_report_list_entry_column_text);
                  edt.setEnabled(false);/*market price ignored config colEdititable*/
                  edt.getLayoutParams().width = (int)colWidth;
                  edt.getLayoutParams().height = (int)colHeight;
                  holder.viewRows[i] = vEdit;
               }break;
               case DateInput:
               case DateTimeInput:{
                  /*reuse btn camera*/
                  View vDateTime = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_camera, null, false);
                  final Button btn = (Button)vDateTime.findViewById(R.id.btn_report_list_entry_column_camera);
                  btn.getLayoutParams().width = (int)colWidth;
                  btn.setText("");
                  btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                  
                  holder.viewRows[i] = vDateTime;
               }break;
               case GodownList:{
                  View vSpinner = 
                        inflater.inflate(R.layout.ps_activity_report_list_entry_column_history_location_spinner, vGroup, false);
                  final HistoryInspectLocationSpinner locationHistory = (HistoryInspectLocationSpinner)vSpinner.findViewById(R.id.sp_history_inspect_location);
                  //locationHistory.initial(jobRequest.getJobRequestID());
                  locationHistory.getLayoutParams().width = (int)colWidth;
                  locationHistory.getLayoutParams().height = (int)colHeight;
                  holder.viewRows[i] = vSpinner;
               }break;
               default:
               {
                  /*default is label*/
                  holder.viewRows[i]
                        = inflater.inflate(R.layout.ps_activity_report_list_entry_column_textview, 
                              vGroup, 
                              false);
                  TextView tvLabel = (TextView)holder.viewRows[i].findViewById(R.id.et_report_list_entry_column_textview);
                  tvLabel.getLayoutParams().width = (int) colWidth;
                  tvLabel.getLayoutParams().height = (int)colHeight;
                  tvLabel.setGravity(Gravity.CENTER);   
               }
               break;
            }
            
            if (holder.viewRows[i] != null)
               holder.viewRows[i].setTag(formView);
         }
         
         //////////////
         for(View v : holder.viewRows)
         {
            if (v != null)
            {
               InspectFormView viewForm = (InspectFormView)v.getTag();
               if (v instanceof ViewGroup){
                  applyViewEdititable((ViewGroup)v,viewForm.isColEditable());
               }else{
                  v.setEnabled(viewForm.isColEditable());
               }
               if (viewForm.isColHidden()){
                  v.setVisibility(View.GONE);
               }
               /*
               LayoutParams layoutParam = ((ViewGroup)vContainer).getLayoutParams();
               
               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                     layoutParam.width, LayoutParams.WRAP_CONTENT);
                 params.gravity = Gravity.CENTER_VERTICAL;
                 ((ViewGroup)vContainer).setLayoutParams(params);
                 */
               ((ViewGroup)vContainer).addView(v);
            }
         }
         view.setTag(holder);
      }
      
      //final JobRequestProduct jrp = jobRequestProductList.get(position);
      
      
      Log.d("DEBUG_D_D", "position -> "+position);
      
      //if (!needToAllRedraw)
      {
         Holder holder = (Holder)view.getTag();
         if ((lastPosition >= 0) && (position == lastPosition))
         {
            holder.shown = false;
            lastPosition = -1;
         }
         if (rowAtNeedToReload == position){
            holder.shown = false;
         }
         //////////////
         if (!holder.shown)
         {
               displayEachRow(view,position);
               holder.shown = true;                     
               holder.position = position;
               //rowAtNeedToReload = -1;
         }         
      }
      /*
      else
      {
         displayEachRow(view,position);
         if (position == jobRequestProductList.size()-1)
         {
            needToAllRedraw = false;
         }
      }*/
 //     displayEachRow(view,position);

      return view;
   }
   @SuppressWarnings("unused")
   private void displayEachRow(final View rowView,final int position){
      /*
       * display each row
       */
      final JobRequestProduct jrp = jobRequestProductList.get(position);
      Holder holder = (Holder)rowView.getTag();
      for( View vEachCol : holder.viewRows)
      {
         final InspectFormView formView =  (InspectFormView)vEachCol.getTag();
         final UniversalControlType ctrlType = UniversalControlType.getControlType(formView.getColType());
         switch(ctrlType){
            case SimpleText:
            case SimpleTextDecimal:
            case SimpleTextDate:{
                  final EditText edtText = (EditText)vEachCol.findViewById(R.id.et_report_list_entry_column_text);
                  Object objValue = invokeGetValue(jrp,formView);
                  
                  if (formView.isColEditable())
                  {
                     /*
                     if (formView.textWatcher != null)
                     {
                        edtText.removeTextChangedListener(formView.textWatcher);
                        formView.textWatcher = null;
                        System.gc();
                     } */
                     
                     if (edtText.getTag() != null){
                        if (edtText.getTag() instanceof UniversalTextWatcher){
                           UniversalTextWatcher textWatcher= (UniversalTextWatcher)edtText.getTag();
                           edtText.removeTextChangedListener(textWatcher);
                        }
                     }
                  }
                  if (!objValue.toString().equalsIgnoreCase("null"))
                  {
                     edtText.setText(setFormat(objValue,formView));
                  }
                  
                  if (edtText.getOnFocusChangeListener() == null)
                  {
                     edtText.setOnFocusChangeListener(new OnFocusChangeListener(){

                     private String textBefore;
                     @Override
                     public void onFocusChange(View v, boolean hasFocus) {
                        // TODO Auto-generated method stub
                        if (hasFocus){
                           textBefore = edtText.getText().toString();
                           edtText.setText("");
                        }else{
                           if (edtText.getText().toString().trim().length() == 0)
                              edtText.setText(textBefore);
                        }
                     }
                    });
                  }
                  if (formView.isColEditable())
                  {
                     /*
                     formView.textWatcher = new UniversalTextWatcher();
                     ((UniversalTextWatcher)formView.textWatcher).setEditText(edtText);
                     ((UniversalTextWatcher)formView.textWatcher).setPosition(position);
                     ((UniversalTextWatcher)formView.textWatcher).setViewCol(rowView);
                     Log.d("DEBUG_D_D", " display text watcher position -> "+ ((UniversalTextWatcher)formView.textWatcher).getPosition());
                     edtText.addTextChangedListener(formView.textWatcher);                     
                     */
                     UniversalTextWatcher textWatcher = new UniversalTextWatcher();
                     textWatcher.setEditText(edtText);
                     textWatcher.setPosition(position);
                     textWatcher.setViewCol(rowView);
                     
                     edtText.setTag(textWatcher);
                     edtText.addTextChangedListener(textWatcher);
                  }
            }break;
            case CheckBox:{
               final CheckBox chkBox = (CheckBox)vEachCol.findViewById(R.id.chkbox_report_list_entry_column_chkbox);
               Object obj = invokeGetValue(jrp,formView);
               boolean check = false;
               if (obj != null){
                  if (obj.toString().equalsIgnoreCase("true")){
                     check = true;
                  }
               }
               chkBox.setChecked(check);
               chkBox.post(new Runnable(){

                  @Override
                  public void run() {
                     // TODO Auto-generated method stub
                     chkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                           // TODO Auto-generated method stub
                           String strChk = (isChecked)?"true":"false";
                           invokeSetValue(jrp,formView,strChk);
                        }
                     });
                  }
                  
               });
               
            }break;
            case MarketPrice:
            {
               /*
                  EditText edtText = (EditText)vEachCol.findViewById(R.id.et_report_list_entry_column_text);
                  if (jrp.getMarketPriceID() >= 0){
                     edtText.setText(setFormat(jrp.getMarketPrice(),formView));
                  }else{
                     edtText.setText(formView.getColDefaultValue());
                  }
                  */
               //if (product != null)
               
               EachColViewHolder eachColViewHolder = new EachColViewHolder();
               eachColViewHolder.position = position;
               eachColViewHolder.viewCol = vEachCol;
               eachColViewHolder.jrp = jrp;
               eachColViewHolder.formView = formView;
               
               //Log.d("DEBUG_D_D_D", "Display Market Price -> "+jrp.getProductGroupID()+", "+jrp.getProductId());
               //{
                 
                  
                  new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,eachColViewHolder);
                  
                  
                     /*
                      * find market price ctrl to set value
                      */

                 //}
            }break;
            case ProductType:
            {
               final ProductGroupSpinner sp_productGroup = 
                     (ProductGroupSpinner)vEachCol.findViewById(R.id.sp_product_group);
               sp_productGroup.initial(jrp.getJobRequestID());
               /*
                */
               ProductGroup productGroup = null;
               if ((sp_productGroup.getProductGroups() != null)&&(sp_productGroup.getProductGroups().size() > 0))
               {
                  int idx = 0;
                  for(ProductGroup pg : sp_productGroup.getProductGroups())
                  {
                     //if (jobRequestProductList.get(position).getProductGroup() != null)
                     //{
                       // if (jrp.getProductGroupID() == pg.getProductGroupID())
                       // {
                       //    productGroup = pg;
                       //    break;
                       // }                        
                     //}
                     if ((jrp.getProductGroup() != null)&&(!jrp.getProductGroup().isEmpty())){
                        if (jrp.getProductGroup().equalsIgnoreCase(pg.getProductGroupName())){
                           productGroup = pg;
                           jrp.setProductGroupID(pg.getProductGroupID());
                           jrp.setProductGroup(pg.getProductGroupName());
                           break;
                        }
                     }
                     idx++;
                  }
                 
                  
                  final ProductTypeSelectImpl productTypeSelectImpl = new ProductTypeSelectImpl();
                  
                  if (sp_productGroup.getOnItemSelectedListener() instanceof ProductTypeSelectImpl){
                     ((ProductTypeSelectImpl)sp_productGroup.getOnItemSelectedListener()).setViewRow(rowView);
                     ((ProductTypeSelectImpl)sp_productGroup.getOnItemSelectedListener()).setPosition(position);
                  }else
                  {                  
                     productTypeSelectImpl.setViewRow(rowView);
                     productTypeSelectImpl.setPosition(position);                  
                  }
                  
                  if (productGroup != null){
                     sp_productGroup.setSelection(idx, false);  
                  }else{
                     sp_productGroup.setSelection(0, false);
                  }
                  
                  if (productGroup == null){
                     productGroup = sp_productGroup.getProductGroups().get(0);/*if not match i'll assume to first row*/
                     jrp.setProductGroupID(productGroup.getProductGroupID());
                     jrp.setProductGroup(productGroup.getProductGroupName());
                  }
                  
                  
                  if (!(sp_productGroup.getOnItemSelectedListener() instanceof ProductTypeSelectImpl)){
                     /*
                  sp_productGroup.post(new Runnable(){
                     @Override
                     public void run() {
                        // TODO Auto-generated method stub
                           sp_productGroup.setOnItemSelectedListener(productTypeSelectImpl); 
                        }
                     });*/
                     sp_productGroup.setOnItemSelectedListener(productTypeSelectImpl); 

                  }
                  //////////////////
               }
            }break;
            case Product:{
               /*
                * find ctrl Product
                */
               //ProductSpinner sp_product = null;
              // final ProductSpinner sp_product = 
              //       (ProductSpinner)vEachCol.findViewById(R.id.sp_product);
               
               //Log.d("DEBUG_D_D_D", "get product list from group id -> "+jrp.getProductGroupID()+", "+jrp.getProductGroup());
              // sp_product.initial(jrp.getProductGroupID());
     
               EachColViewHolder eachColViewHolder = new EachColViewHolder();
               eachColViewHolder.position = position;
               eachColViewHolder.viewCol = vEachCol;
               eachColViewHolder.viewRow = rowView;
               eachColViewHolder.jrp = jrp;
               eachColViewHolder.formView = formView;
               
               new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,eachColViewHolder);
               
            }break;
            case ProductUnit:
            {
               /*product unit*/
               EachColViewHolder eachColViewHolder = new EachColViewHolder();
               eachColViewHolder.position = position;
               eachColViewHolder.viewCol = vEachCol;
               eachColViewHolder.viewRow = rowView;
               eachColViewHolder.jrp = jrp;
               eachColViewHolder.formView = formView;
               
               new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,eachColViewHolder);
              
               
            }break;
            case GodownList:{
               final HistoryInspectLocationSpinner sp_location = (HistoryInspectLocationSpinner)vEachCol.findViewById(R.id.sp_history_inspect_location);
               sp_location.initial(jobRequest.getJobRequestID(),task.getTaskCode());
               SpinnerAdapter adapter = sp_location.getAdapter();
               for(int i = 0; i < adapter.getCount();i++){
                  Object obj = adapter.getItem(i);
                  if (obj instanceof CarInspectStampLocation){
                     CarInspectStampLocation location = (CarInspectStampLocation)obj;
                     if (location.getCustomerSurveySiteID() == jrp.getCustomerSurveySiteID()){
                        sp_location.setSelection(i, false);
                        break;
                     }
                  }
               }
               
               final LocationSelectImpl locationSelect = new LocationSelectImpl();
               if (sp_location.getOnItemSelectedListener() instanceof LocationSelectImpl)
               {
                  ((LocationSelectImpl)sp_location.getOnItemSelectedListener()).setViewRow(rowView);
                  ((LocationSelectImpl)sp_location.getOnItemSelectedListener()).setPosition(position);
               }else{
                  locationSelect.setViewRow(rowView);
                  locationSelect.setPosition(position);
                  sp_location.setOnItemSelectedListener(locationSelect);
               }
               
            }break;
            case Layout:
            {
               
               final LayoutSpinner sp_layout = (LayoutSpinner)vEachCol.findViewById(R.id.sp_layout_inspect);
               sp_layout.initial(task.getTaskCode(), jrp.getCustomerSurveySiteID(),true);
               
               
               final LayoutSelectImpl layoutSelect = new LayoutSelectImpl();
               if (sp_layout.getOnItemSelectedListener() instanceof LayoutSelectImpl)
               {
                  ((LayoutSelectImpl)sp_layout.getOnItemSelectedListener()).setViewRow(rowView);
                  ((LayoutSelectImpl)sp_layout.getOnItemSelectedListener()).setPosition(position);
               }else{
                  layoutSelect.setViewRow(rowView);
                  layoutSelect.setPosition(position);
                  sp_layout.setOnItemSelectedListener(layoutSelect);
               }
               
               SpinnerAdapter adapter  = sp_layout.getAdapter();
               for(int i = 0; i < adapter.getCount();i++){
                  Object obj = adapter.getItem(i);
                  if (obj instanceof InspectDataObjectSaved){
                     InspectDataObjectSaved objSaved = (InspectDataObjectSaved)obj;
                     if (objSaved.getInspectDataObjectID() == jrp.getInspectDataObjectID()){
                        sp_layout.setSelection(i, false);
                        break;
                     }
                  }
               }
           
               
               /*
               EachColViewHolder eachColViewHolder = new EachColViewHolder();
               eachColViewHolder.position = position;
               eachColViewHolder.viewCol = vEachCol;
               eachColViewHolder.viewRow = rowView;
               eachColViewHolder.jrp = jrp;
               eachColViewHolder.formView = formView;
               
               new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,eachColViewHolder);
*/
               
               //////////////////////////////
            }break;
            case DeleteRow:{
               final Button btn = (Button)vEachCol.findViewById(R.id.btn_report_list_entry_column_camera);
               btn.setOnClickListener(new OnClickListener(){

                  @Override
                  public void onClick(View v) {
                     // TODO Auto-generated method stub
                     if (getRowContextOpenListener() != null){
                        getRowContextOpenListener().onClickContextOpen(rowView,jrp);
                     }
                  }
                  
               });
            }break;
            case CheckListForm:
            {
               final Button btn = (Button)vEachCol.findViewById(R.id.btn_report_list_entry_column_camera);
               btn.post(new Runnable(){

                  @Override
                  public void run() {
                     // TODO Auto-generated method stub
                     /*
                      * .setCompoundDrawablesWithIntrinsicBounds(
                          R.drawable.ic_device_access_camera_active,0,0,0);
                      */
                     if (jrp.isHasCheckList()){
                        btn.setCompoundDrawablesWithIntrinsicBounds(
                              R.drawable.ic_list_active,0,0,0);
                     }else{
                        btn.setCompoundDrawablesWithIntrinsicBounds(
                              R.drawable.ic_list,0,0,0);
                     }
                  }
               });
               final ArrayList<InspectFormView> colProperties = new ArrayList<InspectFormView>();
               for( View tmp : holder.viewRows)
               {
                  InspectFormView tmpProperty =  (InspectFormView)vEachCol.getTag();
                  colProperties.add(tmpProperty);
               }
               btn.setOnClickListener(new OnClickListener(){

                  @Override
                  public void onClick(View v) {
                     // TODO Auto-generated method stub
                     if (getOpenCommentActivityListener() != null){
                        getOpenCommentActivityListener().onOpenCommentActivity(colProperties, jrp);
                     }
                  }
                  
               });
            }
            break;
            
            case DateInput:
            case DateTimeInput:{
               /*reuse btn camera*/
               final Button btn = (Button)vEachCol.findViewById(R.id.btn_report_list_entry_column_camera);
               
               Object obj = invokeGetValue(jrp,formView);
               if (!obj.toString().equalsIgnoreCase("null")){
                  btn.setText(obj.toString());
               }
               
               btn.setOnClickListener(new OnClickListener(){

                  @Override
                  public void onClick(View v) {
                     // TODO Auto-generated method stub
                     final View dialogView = View.inflate(context, R.layout.date_time_picker, null);
                     final AlertDialog dateTimeDlg = new AlertDialog.Builder(context).create();
                     dateTimeDlg.setOnShowListener(new OnShowListener(){

                        @Override
                        public void onShow(DialogInterface dialog) {
                           // TODO Auto-generated method stub
                           TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                           timePicker.setIs24HourView(true);
                           timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                           
                           if (ctrlType == UniversalControlType.DateTimeInput){
                              timePicker.setVisibility(View.VISIBLE);  
                           }else{
                              timePicker.setVisibility(View.GONE);
                           }
                        }
                        
                     });
                     dateTimeDlg.setOnCancelListener(new OnCancelListener(){

                        @Override
                        public void onCancel(DialogInterface dialog) {
                           // TODO Auto-generated method stub
                           dialog.dismiss();
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
                             
                             String timestamp = DataUtil.universalDateToString(formView.getDateTimeFormate(), new Timestamp(time));
                             
                             btn.setText(timestamp);
                                                         
                             invokeSetValue(jrp,formView,timestamp);
                             
                             dateTimeDlg.dismiss();
                             //bDatePickerShown = false;
                        }});
                     dateTimeDlg.setView(dialogView);
                     dateTimeDlg.show();
                  }
                  
               });
            }break;
            case Camera:{
               /*
                * if (requestProduct.getPhotoSetID() > 0){
           holder.btnTakeCamera.setCompoundDrawablesWithIntrinsicBounds(
                 R.drawable.ic_device_access_camera_active,0,0,0);
           }else{
           holder.btnTakeCamera.setCompoundDrawablesWithIntrinsicBounds(
                 R.drawable.ic_device_access_camera,0,0,0);
           }
                */
               final Button btn = (Button)vEachCol.findViewById(R.id.btn_report_list_entry_column_camera);
               /*
               btn.post(new Runnable(){

                  @Override
                  public void run() {
                     // TODO Auto-generated method stub
                     if (jrp.getPhotoSetID() > 0)
                     {
                        btn.setCompoundDrawablesWithIntrinsicBounds(
                              R.drawable.ic_device_access_camera_active,0,0,0);
                     }else{
                        btn.setCompoundDrawablesWithIntrinsicBounds(
                              R.drawable.ic_device_access_camera,0,0,0);
                     }
                     btn.invalidate();
                  }
                  
               });*/
               if (jrp.getPhotoSetID() > 0)
               {
                  btn.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_device_access_camera_active,0,0,0);
               }else{
                  btn.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_device_access_camera,0,0,0);
               }
               btn.setOnClickListener(new OnClickListener(){

                  @Override
                  public void onClick(View v) {
                     // TODO Auto-generated method stub
                     // TODO Auto-generated method stub
                     if (getOnTakeCameraListener() != null)
                     {
                        getOnTakeCameraListener().onTakeCamera(jrp);
                        /*
                         * set modified
                         */
                     }
                  }
                  
               });
                 
            }break;
             default:
            {
                  TextView tvLabel = (TextView)vEachCol.findViewById(R.id.et_report_list_entry_column_textview);
                  if (tvLabel != null){
                     Object objValue = invokeGetValue(jrp,formView);
                     tvLabel.setText(setFormat(objValue,formView));
                     //tvLabel.invalidate();
                     //tvLabel.requestLayout();
                  }
            }break;
         }
      }
   }
   private void applyViewEdititable(ViewGroup parent, boolean isEditible) 
   {    
       for(int i = 0; i < parent.getChildCount(); i++)
       {
           View child = parent.getChildAt(i);            
           if(child instanceof ViewGroup) 
           {
              applyViewEdititable((ViewGroup)child, isEditible);
           }
           else if(child != null)
           {
              /*
               Log.d("menfis", child.toString());
               if(child.getClass() == TextView.class)
               {
                   ((TextView) child).setTypeface(font);
               }*/
              child.setEnabled(isEditible);
              child.setClickable(isEditible);
           }                
       }
   }
   public ArrayList<JobRequestProduct> getAllJobRequestProducts(){
      return this.jobRequestProductList;
   }
   
   public void addNewRowNoAudit(int lastRowProductIdOfCurrentPage)
   {
      if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
         Log.d("DEBUG_D_CRF", "On double clicked.....");
         return;
      }
      mLastClickTime = SystemClock.elapsedRealtime();
      
      if (this.jobRequestProductList == null){
         this.jobRequestProductList = new ArrayList<JobRequestProduct>();
      }
      //new AsyncTaskAddNewRowNoAudit().execute(lastRowProductIdOfCurrentPage);
      int maxRowId = lastRowProductIdOfCurrentPage;//params[0];
      for(JobRequestProduct item : jobRequestProductList)
      {
         if (item.getProductRowID() > maxRowId){
            maxRowId = item.getProductRowID();
         }
      }
      JobRequestProduct jrp = new JobRequestProduct();
      jrp.setProductRowID(maxRowId+1);
      jrp.setJobRequestID(jobRequest.getJobRequestID());
      jrp.setcWareHouse(site.getCustomerSurveySiteID());
      jrp.setJobLocationId(site.getCustomerSurveySiteID());
      jrp.setCustomerSurveySiteID(site.getCustomerSurveySiteID());
      jrp.setAudit(isAudit);
      jrp.setJobNo(task.getTaskCode());
      jrp.setInspectDataObjectID(-1);
      
      int teamId = SharedPreferenceUtil.getTeamID(context);      
      jrp.setcTeamId(teamId);
      
      //////////
      jobRequestProductList.add(jrp);
      
      Collections.sort(jobRequestProductList,new Comparator<JobRequestProduct>(){

         @Override
         public int compare(JobRequestProduct lhs, JobRequestProduct rhs) {
            // TODO Auto-generated method stub
            return lhs.getProductRowID() - rhs.getProductRowID();
         }
         
      });
      
      if (getColumnInputChangeListener() != null){
         //save to data base
         getColumnInputChangeListener().onRowSaved(jrp);
         rowAtNeedToReload = 
               lastPosition = jobRequestProductList.size()-1;
         notifyDataSetChanged();
         getColumnInputChangeListener().onReload(jobRequestProductList.size()-1);
      }
   }
   class AsyncTaskReloadColumn extends AsyncTask<EachColViewHolder,Void,Void>{

      
      private EachColViewHolder v;
      private UniversalControlType ctrlType;
      private Product product = null;
      private ProductAmountUnit productUnit = null;
      
      private int objProductIdx = -1;
      private int objProductUnitIdx = -1;
      private int objLayoutIdx = -1;
      private EachColViewHolder marketPriceeachColViewHolder;
      private EachColViewHolder productUniteachColViewHolder;
      
      //private int inspectDataObjectId = -1;
      
      private InspectDataObjectSaved dataSaved;
      @Override
      protected Void doInBackground(EachColViewHolder... params) {
         // TODO Auto-generated method stub
         v = params[0];
         MarketPrice defaultMarket = null;
         JobRequestProduct jrp = v.jrp;
          
         ctrlType = UniversalControlType.getControlType(v.formView.getColType());
         
         if (ctrlType == UniversalControlType.MarketPrice)
         {
            Log.d("DEBUG_D_D_D", "AsyncTaskReloadColumn -> doInBackground "+v.position);
            if (marketPriceForProvince != null){
               defaultMarket = marketPriceForProvince.find(jrp.getProductGroupID(),
                     jrp.getProductId());
            }
            if (defaultMarket == null){
               
                // try to check market price with global
               defaultMarket = marketPriceGlobalFinder.find(jrp.getProductGroupID(),
                     jrp.getProductId());
             }
            
            if (defaultMarket != null)
            {
               Log.d("DEBUG_D_D_D", "found default market ");
               jrp.setMarketPriceID(defaultMarket.getMarketPriceID());
               jrp.setMarketPrice(defaultMarket.getMarketPrice());
            }else{
               Log.d("DEBUG_D_D_D", "not found default market ");
               
               jrp.setMarketPriceID(-1);
               jrp.setMarketPrice(0.00);
            }
         }else if (ctrlType == UniversalControlType.Product)
         {
            final ProductSpinner sp_product = 
                  (ProductSpinner)v.viewCol.findViewById(R.id.sp_product);
            
            
            sp_product.findProductByGroupID(jrp.getProductGroupID());/*fetch from db and keep to cache*/

            if(sp_product.getProducts() != null)
            {
               for(int i = 0; i < sp_product.getProducts().size();i++)
               {
                  if (sp_product.getProducts().get(i).getProductID() == jrp.getProductId())
                  {
                     objProductIdx = i;
                     product = sp_product.getProducts().get(i);
                     v.jrp.setProductId(product.getProductID());
                     v.jrp.setProductName(product.getProductName());
                     break;
                  }
               }
             }
            if (v.viewRow != null)
            {
               if (v.viewRow.getTag() != null){
                  Holder holder = (Holder)v.viewRow.getTag();
                  for(View vEachCol : holder.viewRows){
                     InspectFormView viewForm = (InspectFormView)vEachCol.getTag();
                     UniversalControlType ctrlType = UniversalControlType.getControlType(viewForm.getColType());
                     if (ctrlType == UniversalControlType.MarketPrice)
                     {
                        marketPriceeachColViewHolder = new EachColViewHolder();
                        marketPriceeachColViewHolder.formView = viewForm;
                        marketPriceeachColViewHolder.jrp =v.jrp;
                        marketPriceeachColViewHolder.position = v.position;
                        marketPriceeachColViewHolder.viewCol = vEachCol;
                        marketPriceeachColViewHolder.viewRow = v.viewRow;
                     }else if (ctrlType == UniversalControlType.ProductUnit){
                        productUniteachColViewHolder = new EachColViewHolder();
                        productUniteachColViewHolder.formView = viewForm;
                        productUniteachColViewHolder.jrp =v.jrp;
                        productUniteachColViewHolder.position = v.position;
                        productUniteachColViewHolder.viewCol = vEachCol;
                        productUniteachColViewHolder.viewRow = v.viewRow;
                     }
                  }
               }
            }
         }else if (ctrlType == UniversalControlType.Layout){
            
            final LayoutSpinner sp_layout = (LayoutSpinner)v.viewCol.findViewById(R.id.sp_layout_inspect);
            
            ArrayList<InspectDataObjectSaved> datas = 
                  sp_layout.getDatas(task.getTaskCode(), jrp.getCustomerSurveySiteID(),true);
            
            /*
            SpinnerAdapter adapter  = sp_layout.getAdapter();
            for(int i = 0; i < adapter.getCount();i++){
               Object obj = adapter.getItem(i);
               if (obj instanceof InspectDataObjectSaved){
                  InspectDataObjectSaved objSaved = (InspectDataObjectSaved)obj;
                  if (objSaved.getInspectDataObjectID() == jrp.getInspectDataObjectID()){
                     dataSaved = objSaved;
                     objLayoutIdx = i;
                     break;
                  }
               }
            }*/
            
            if (datas != null){
               for(int i = 0;i<datas.size();i++){
                  if (datas.get(i).getInspectDataObjectID() == jrp.getInspectDataObjectID()){
                     dataSaved = datas.get(i);
                     objLayoutIdx = i;
                     //inspectDataObjectId = datas.get(i).getInspectDataObjectID();
                     break;
                  }
               }
            }
            
         }else
         if (ctrlType == UniversalControlType.ProductUnit)
         {
            int productAmountUnitID = -1;
            if (v.viewRow != null)
            {
               if (v.viewRow.getTag() != null)
               {
                  Holder holder = (Holder)v.viewRow.getTag();
                  for(View vEachCol : holder.viewRows){
                     if (vEachCol == null)continue;
                     
                     InspectFormView viewForm = (InspectFormView)vEachCol.getTag();
                     UniversalControlType ctrlType = UniversalControlType.getControlType(viewForm.getColType());
                     if (ctrlType == UniversalControlType.Product)
                     {
                        final ProductSpinner sp_product = 
                              (ProductSpinner)vEachCol.findViewById(R.id.sp_product);
                        if (sp_product.getProducts() == null)
                           sp_product.findProductByGroupID(v.jrp.getProductGroupID());
                        
                        if (sp_product.getProducts() != null)
                        {
                           for(Product p : sp_product.getProducts()){
                              if (p.getProductID() == v.jrp.getProductId()){
                                 productAmountUnitID = p.getProductAmountUnitID();
                                 break;
                              }
                           }
                        }
                        break;
                     }
                  }
               }
            }
            final ProductUnitSpinner sp_product_unit = 
                  (ProductUnitSpinner)v.viewCol.findViewById(R.id.sp_product_unit);
          
            sp_product_unit.initial();/*keep to cache*/
            
            for(int i = 0; i < sp_product_unit.getProductAmountUnits().size();i++){
               ProductAmountUnit productUnit = sp_product_unit.getProductAmountUnits().get(i);
               if (productAmountUnitID >= 0)
               {
                  if (productUnit.getProductAmountUnitID() == productAmountUnitID){
                     this.productUnit = productUnit;
                     //sp_product_unit.setSelection(i, false);
                     v.jrp.setProductUnit(this.productUnit.getUnitName());
                     this.objProductUnitIdx = i;                     
                     break;
                  }
               }else{
                  if (productUnit.getUnitName().equalsIgnoreCase(jrp.getProductUnit()))
                  {
                     this.productUnit = productUnit;
                     //sp_product_unit.setSelection(i, false);
                     v.jrp.setProductUnit(this.productUnit.getUnitName());
                     this.objProductUnitIdx = i;
                     break;
                  }
               }
            }
         }else if (ctrlType == UniversalControlType.Layout){
            
         }
         return null;
      }
      /* (non-Javadoc)
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(Void result) {
         // TODO Auto-generated method stub
         super.onPostExecute(result);
         
         if (ctrlType == UniversalControlType.MarketPrice)
         {

            final EditText edtText = (EditText)v.viewCol.findViewById(R.id.et_report_list_entry_column_text);
            if (v.jrp.getMarketPriceID() >= 0){
                  edtText.setText(setFormat(v.jrp.getMarketPrice(),v.formView));
            }else{
                  edtText.setText(v.formView.getColDefaultValue());
            }
         }else if (ctrlType == UniversalControlType.Product)
         {
            final ProductSpinner sp_product = 
                  (ProductSpinner)v.viewCol.findViewById(R.id.sp_product);
            sp_product.initial(v.jrp.getProductGroupID()/*ignored because already fetched from cache in doBackground*/);
            
            
            final ProductSelectImpl productSelectImpl = new ProductSelectImpl();
            if (sp_product.getOnItemSelectedListener() instanceof ProductTypeSelectImpl){
               ((ProductSelectImpl)sp_product.getOnItemSelectedListener()).setViewRow(v.viewRow);
               ((ProductSelectImpl)sp_product.getOnItemSelectedListener()).setPosition(v.position);
            }else
            {                  
               productSelectImpl.setViewRow(v.viewRow);
               productSelectImpl.setPosition(v.position); 
               sp_product.setOnItemSelectedListener(productSelectImpl);
            }
            
            if (sp_product.getProducts() != null)
            {
               if (product == null){
                  product = sp_product.getProducts().get(0);
                  v.jrp.setProductId(product.getProductID());
                  v.jrp.setProductName(product.getProductName());
                  sp_product.setSelection(0, false);/*must call onPostExecute*/
               }else{
                  sp_product.setSelection(objProductIdx, false);
               }               
               
               
               
               if (marketPriceeachColViewHolder != null){
                  new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, marketPriceeachColViewHolder);
               }
               if (productUniteachColViewHolder != null){
                  new AsyncTaskReloadColumn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, productUniteachColViewHolder);                  
               }
            }
         }else if (ctrlType == UniversalControlType.Layout){
            final LayoutSpinner sp_layout = (LayoutSpinner)v.viewCol.findViewById(R.id.sp_layout_inspect);
            
            sp_layout.initial(task.getTaskCode(), v.jrp.getCustomerSurveySiteID(),true);
            
            if (objLayoutIdx >= 0){
               sp_layout.setSelection(objLayoutIdx, false);
            }else{
               sp_layout.setSelection(0, false);
            }

            //v.jrp.setInspectDataObjectID(inspectDataObjectId);
            /////
            final LayoutSelectImpl layoutSelect = new LayoutSelectImpl();
            if (sp_layout.getOnItemSelectedListener() instanceof LayoutSelectImpl)
            {
               ((LayoutSelectImpl)sp_layout.getOnItemSelectedListener()).setViewRow(v.viewRow);
               ((LayoutSelectImpl)sp_layout.getOnItemSelectedListener()).setPosition(v.position);
            }else{
               layoutSelect.setViewRow(v.viewRow);
               layoutSelect.setPosition(v.position);
               sp_layout.setOnItemSelectedListener(layoutSelect);
            }
         }else if (ctrlType == UniversalControlType.ProductUnit)
         {
            final ProductUnitSpinner sp_product_unit = 
                  (ProductUnitSpinner)v.viewCol.findViewById(R.id.sp_product_unit);
          
            sp_product_unit.initial(null);/*call this method for bind data to adapter*/
            if (this.objProductUnitIdx >= 0){
               sp_product_unit.setSelection(objProductUnitIdx, false);
            }else{
               sp_product_unit.setSelection(0, false);
            }
            final ProductUnitSelectImpl productUnitSelectImpl = new ProductUnitSelectImpl();
            if (sp_product_unit.getOnItemSelectedListener() instanceof ProductUnitSelectImpl){
                ((ProductUnitSelectImpl)sp_product_unit.getOnItemSelectedListener()).setViewRow(v.viewRow);
                ((ProductUnitSelectImpl)sp_product_unit.getOnItemSelectedListener()).setPosition(v.position);
            }else{
               productUnitSelectImpl.setViewRow(v.viewRow);
               productUnitSelectImpl.setPosition(v.position);
               sp_product_unit.setOnItemSelectedListener(productUnitSelectImpl);
            }
         }else if (ctrlType == UniversalControlType.Layout){
            
         }
      }
   }
   class AsyncTaskAddNewRowNoAudit extends AsyncTask<Integer,Void,Void>{

      

      @Override
      protected Void doInBackground(Integer... params) {
         // TODO Auto-generated method stub
         
         int maxRowId = params[0];
         for(JobRequestProduct item : UniversalListEntryAdapter.this.jobRequestProductList)
         {
            if (item.getProductRowID() > maxRowId){
               maxRowId = item.getProductRowID();
            }
         }
         JobRequestProduct jrp = new JobRequestProduct();
         jrp.setProductRowID(maxRowId+1);
         jrp.setJobRequestID(jobRequest.getJobRequestID());
         jrp.setcWareHouse(site.getCustomerSurveySiteID());
         jrp.setJobLocationId(site.getCustomerSurveySiteID());
         jrp.setCustomerSurveySiteID(site.getCustomerSurveySiteID());
         jrp.setAudit(isAudit);
         jrp.setJobNo(task.getTaskCode());
         
         int teamId = SharedPreferenceUtil.getTeamID(context);      
         jrp.setcTeamId(teamId);
         
         //////////
         UniversalListEntryAdapter.this.jobRequestProductList.add(jrp);
         
         Collections.sort(UniversalListEntryAdapter.this.jobRequestProductList,new Comparator<JobRequestProduct>(){

            @Override
            public int compare(JobRequestProduct lhs, JobRequestProduct rhs) {
               // TODO Auto-generated method stub
               return lhs.getProductRowID() - rhs.getProductRowID();
            }
            
         });
         
         if (getColumnInputChangeListener() != null){
            /*save to data base*/
            getColumnInputChangeListener().onRowSaved(jrp);
         }
         
         return null;
      }
      
      /* (non-Javadoc)
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(Void result) {
         // TODO Auto-generated method stub
         super.onPostExecute(result);
         //notifyDataSetChanged();
         //notifyDataSetInvalidated();
         if (getColumnInputChangeListener() != null){
            notifyDataSetChanged();
            getColumnInputChangeListener().onReload(UniversalListEntryAdapter.this.jobRequestProductList.size()-1);
         }

      }
   }
   @SuppressWarnings("unused")
   public static synchronized Object invokeGetValue(JobRequestProduct jobRequestProduct,
         InspectFormView viewForm)
      {
         return invokeGetValue(jobRequestProduct,viewForm.getColInvokeField());
      }
   public static synchronized Object invokeGetValue(JobRequestProduct jobRequestProduct,
         String colInvokeField){
      
      String[] colInvokeFieldArray = colInvokeField.split(",");
      
      StringBuilder strBld = new StringBuilder();
      
      Object objRet = null;
      for(String colInvoke : colInvokeFieldArray)
      {
         String invokeField = "get"+colInvoke;//viewForm.getColInvokeField();
         @SuppressWarnings("rawtypes")
         Class jrpClass = jobRequestProduct.getClass();
         try {
            Method[] fSets = jrpClass.getDeclaredMethods();
            Method fInvoke = null;
            for(int i = 0 ; i <fSets.length;i++)
            {
               Method f = fSets[i];
               if (f.getName().equalsIgnoreCase(invokeField)){
                  fInvoke = f;
                  break;
               }
            }
            if (fInvoke != null)
            {
               objRet = fInvoke.invoke(jobRequestProduct);  
               if (colInvokeFieldArray.length > 1){
                  if (!objRet.toString().isEmpty()){
                     if (!objRet.toString().equalsIgnoreCase("null")){
                        strBld.append(objRet.toString()+" / ");                                             
                     }
                  }
               }else{
                  break;
               }
            }else{
               //throw new NoSuchFieldException(invokeField);
               Log.d("DEBUG_D", "No such field -> "+invokeField);
            }
         }
         catch(Exception ex){
            ex.printStackTrace();
         }
      }
      if (colInvokeFieldArray.length > 1){
         return strBld.toString();
      }else{
         return (objRet == null)?"":objRet;
      }
   }
   @SuppressWarnings({ "unused", "rawtypes"})
   public static synchronized void invokeSetValue(final JobRequestProduct jobRequestProduct,
         final InspectFormView viewForm,final Object value)
   {
      String invokeField = "set"+viewForm.getColInvokeField();
      Class jrpClass = jobRequestProduct.getClass();
      try {
         Method[] fSets = jrpClass.getDeclaredMethods();
         Method fInvoke = null;
         for(int i = 0 ; i <fSets.length;i++)
         {
            Method f = fSets[i];
            if (f.getName().equalsIgnoreCase(invokeField)){
               fInvoke = f;
               break;
            }
         }
         if (fInvoke != null)
         {
            Class[] types = fInvoke.getParameterTypes();
            if (types.length > 0){
               Class type = types[0];
               String className = type.getName();
               Object objValue = value;
               if (className.equalsIgnoreCase("int")){
                  objValue = Integer.parseInt(value.toString());
               }else if (className.equalsIgnoreCase("float")){
                  objValue = Float.parseFloat(value.toString());
               }else if (className.equalsIgnoreCase("double")){
                  objValue = Double.parseDouble(value.toString());
               }
               fInvoke.invoke(jobRequestProduct,objValue);
            }
            //fInvoke.invoke(jobRequestProduct, value);  
         }else{
           // throw new NoSuchFieldException(invokeField);
            Log.d("DEBUG_D", "No such field -> "+invokeField);

         }
      }
//      catch (NoSuchFieldException e) {
         // TODO Auto-generated catch block
         //e.printStackTrace();
//      }
      catch (IllegalAccessException e) {
         // TODO Auto-generated catch block
         //e.printStackTrace();
      }
      catch (IllegalArgumentException e) {
         // TODO Auto-generated catch block
        // e.printStackTrace();
         e.printStackTrace();
      }
      catch (InvocationTargetException e) {
         // TODO Auto-generated catch block
        // e.printStackTrace();
      }
   }
   public static String setFormat(Object objValue,InspectFormView formView)
   {
      //if (objValue == null){
      //   objValue = formView.getColDefaultValue();
      //}
      String strTextFormated = "";
      if (objValue != null)
      {
         strTextFormated = objValue.toString();
         
         UniversalControlType ctrlType = UniversalControlType.getControlType(formView.getColType());
         switch(ctrlType){
            case SimpleTextDate:
            case DateInput:
            case DateTimeInput:{
               if (objValue != null){
                  
               }
            }break;
            default:{
               if ((objValue instanceof Float)||(objValue instanceof Double)){
                  if ((formView.getDecimalFormat() != null)&&(!formView.getDecimalFormat().isEmpty())){
                     DecimalFormat decimalFormat = new DecimalFormat(formView.getDecimalFormat());
                     strTextFormated = decimalFormat.format(objValue);
                  }
               }
            }break;
         }
      }
      return strTextFormated;
   }
   @SuppressWarnings("unused")
   private void calculateByFormula(final JobRequestProduct jrp,
         final InspectFormView formView,final Holder holder)
   {
        //if ((formView.getColFormula() != null)&&(!formView.getColFormula().isEmpty()))
        //{
                 MathEval mathEval = new MathEval();
                 InspectFormView formViewFormula = null;
                 String formular = "";
                 for(View vEachCol : holder.viewRows)
                 { 
                    if (vEachCol != null){
                       InspectFormView tmp_form_view_col = (InspectFormView)vEachCol.getTag();
                       if ((tmp_form_view_col.getColFormula() != null)&&(!tmp_form_view_col.getColFormula().isEmpty())){
                          formular = tmp_form_view_col.getColFormula();
                          formViewFormula = tmp_form_view_col;
                       }
                       if ((tmp_form_view_col.getColInvokeField() != null)&&(!tmp_form_view_col.getColInvokeField().isEmpty())){
                          if (isArgumentOfGetterMethodIsDecimal(jrp,tmp_form_view_col.getColInvokeField())){
                             /*
                              * this field is decimal field
                              * 
                              * int , float , double only
                              */
                             mathEval.setVariable("col_"+tmp_form_view_col.getColNo(),
                                   Double.parseDouble(invokeGetValue(jrp,tmp_form_view_col).toString())
                                   );
                          }
                       }
                    }
                }
                /*
                 * 
                 */
                if (!formular.isEmpty()){
                   double retVal = mathEval.evaluate(formular);
                   invokeSetValue(jrp,formViewFormula,retVal);                   
                }
        //}
   }
   @SuppressWarnings({ "unused", "rawtypes"})
   private boolean isArgumentOfGetterMethodIsDecimal(JobRequestProduct jrp,String colInvoke){
      boolean bRet = false;
      String invokeField = "get"+colInvoke;
      Class jrpClass = jrp.getClass();
      Method[] fSets = jrpClass.getDeclaredMethods();
      Method fInvoke = null;
      for(int i = 0 ; i <fSets.length;i++)
      {
            Method f = fSets[i];
            if (f.getName().equalsIgnoreCase(invokeField)){
               fInvoke = f;
               break;
            }
      }
      if (fInvoke != null)
      {
         Class type = fInvoke.getReturnType();
            String className = type.getName();
            if (className.equalsIgnoreCase("int") ||
               className.equalsIgnoreCase("float") ||
               className.equalsIgnoreCase("double"))
               {
               bRet = true;
               }
      }
      return bRet;
   }
   
   
   private OnTakeCameraListener<JobRequestProduct>onTakeCameraListener; 
   public OnTakeCameraListener<JobRequestProduct> getOnTakeCameraListener() {
      return onTakeCameraListener;
   }

   public void setOnTakeCameraListener(OnTakeCameraListener<JobRequestProduct> onTakeCameraListener) {
      this.onTakeCameraListener = onTakeCameraListener;
   }

   public OnColumnInputChangeListener getColumnInputChangeListener() {
      return columnInputChangeListener;
   }

   public void setColumnInputChangeListener(OnColumnInputChangeListener columnInputChangeListener) {
      this.columnInputChangeListener = columnInputChangeListener;
   }

   public void refreshAtRowOfJobRequestProduct(JobRequestProduct currentJobRequestProduct){
      for(int i = 0;i < this.jobRequestProductList.size();i++){
         JobRequestProduct jrp = this.jobRequestProductList.get(i);
         if (jrp.getProductRowID() == currentJobRequestProduct.getProductRowID()){
            rowAtNeedToReload = i;
            jrp.setHasCheckList(currentJobRequestProduct.isHasCheckList());
            break;
         }
      }
   }

   public void removeItem(JobRequestProduct currentJobRequestProduct){
      jobRequestProductList.remove(currentJobRequestProduct);
      jobRequestProductList.trimToSize();
      
      needToAllRedraw = true;
      this.notifyDataSetChanged();
   }
   public OnOpenCommentActivity getOpenCommentActivityListener() {
      return openCommentActivityListener;
   }

   public void setOpenCommentActivityListener(OnOpenCommentActivity openCommentActivityListener) {
      this.openCommentActivityListener = openCommentActivityListener;
   }

   public OnRowContextOpenListener getRowContextOpenListener() {
      return rowContextOpenListener;
   }

   public void setRowContextOpenListener(OnRowContextOpenListener rowContextOpenListener) {
      this.rowContextOpenListener = rowContextOpenListener;
   }
  
}

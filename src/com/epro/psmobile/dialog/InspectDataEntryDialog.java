/**
 * 
 */
package com.epro.psmobile.dialog;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.MarketPrice;
import com.epro.psmobile.data.MarketPriceFinder;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductAmountUnit;
import com.epro.psmobile.data.ProductGroup;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.MathEval;
import com.epro.psmobile.util.MathEval.ArgParser;
import com.epro.psmobile.util.MathEval.FunctionHandler;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.view.ProductGroupSpinner;
import com.epro.psmobile.view.ProductSpinner;
import com.epro.psmobile.view.ProductUnitSpinner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * @author thrm0006
 *
 */
public class InspectDataEntryDialog extends DialogFragment implements OnFocusChangeListener, TextWatcher {

	private boolean isDialogInitial = true;
	//private int lastProductGroupID = 0;
	//private int lastProductID;
	public class DecimalDigitsInputFilter implements InputFilter {

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
		    mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

		        Matcher matcher=mPattern.matcher(dest);       
		        if(!matcher.matches())
		            return "";
		        return null;
		    }

	}
	public enum InspectDataEntryType
	{
		NORMAL_ENTRY(0),
		PHOTO_ENTRY(1),
		UNIVERSAL_LAYOUT(2);
		
		private int code;
		InspectDataEntryType(int code)
		{
			this.code = code;
		}
		public int getCode(){
			return code;
		}
		public static InspectDataEntryType getType(int iCode)
		{
			for(int i = 0; i < values().length;i++)
			{
				if (values()[i].getCode() == iCode){
					return values()[i];
				}
			}
			return NORMAL_ENTRY;
		}
	};
	public interface OnClickInspectDataEntryDialog
	{
		void onClickSaveDataInspectDataEntry(InspectDataItem inspectDataItem,
				InspectDataObjectSaved dataSaved);
	}
	
	public static InspectDataEntryDialog newInstance(
			JobRequest jobRequest,
			CustomerSurveySite customerSite,
			InspectDataItem inspectDataItem,
			InspectDataObjectSaved dataSaved,
			InspectDataObjectSaved godownDataSaved,
			double remainCapacity,
			InspectDataEntryType dataEntryType){
		InspectDataEntryDialog entryDlg = new InspectDataEntryDialog();
		Bundle bArgument = new Bundle();
		bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
		bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY, customerSite);
		bArgument.putParcelable(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ITEM, inspectDataItem);
		bArgument.putSerializable(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_SAVED, dataSaved);
		bArgument.putSerializable(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_GODOWN_SAVED, godownDataSaved);
		bArgument.putDouble(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_GODOWN_CAPACITY, remainCapacity);
		bArgument.putInt(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_TYPE, dataEntryType.getCode());
		entryDlg.setArguments(bArgument);
		return entryDlg;
	}
	/**
	 * 
	 */
	private View currentView;
	private OnClickInspectDataEntryDialog onClickInspectDataEntryDialog;
	private InspectDataObjectSaved inspectDataObjectSaved = null;		
	private InspectDataObjectSaved godownDataSaved = null;
	private InspectDataItem inspectDataItem = null;
	private Product productSelected = null;
	private Product previousProductSelected = null;
	private ProductGroup productGroupSelected = null;
	private ProductGroup previousProductGroupSelected = null;
	private ProductAmountUnit productAmountUnit;
	private CustomerSurveySite customerSite;
	private JobRequest jobRequest;
	
	private int productUnitIdxSelected = 0;
	private int productGroupIdxSelected = 0;
	private int productIdxSelected = 0;
	
	ProductUnitSpinner sp_product_unit;
	private MarketPrice marketPrice;
	
	
	private EditText edt_width;
	private EditText edt_long;
	private EditText edt_height;
	private EditText edt_radius;

	private EditText edt_lost;
	private EditText edt_over;
	private EditText edt_total;
	private EditText edt_value;
	
	private EditText edt_opinion;
	
	private EditText edt_marketPrice;

	private EditText edt_density;
	
	private EditText edt_palateAmount;
	
	private TextView tvFormula;
	private CheckBox chkIsProductControlled;
	
	private EditText edt_objectName;
	private EditText edt_areaUniversal;
	
	private MarketPriceFinder marketPriceFinder;
	private MarketPriceFinder marketPriceGlobalFinder;

	private String formular;
	
	private double total = 0.0;
	private double value = 0.0;
	
	
	private double lastCapacity = 0.0;
	private double lastTotalValue = 0.0;
	
	
	private String previousValue = "0.00";
	
	private ProductGroupSpinner sp_product_group;
	private ProductSpinner sp_product;
	
	private boolean isNewProductSelected = false;
	
	private InspectDataEntryType dataEntryType = InspectDataEntryType.NORMAL_ENTRY;;
    
	
	private View vCommentContainer;
	private View vLayoutNameContainer;
	
	private InspectDataEntryDialog() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Dialog onCreateDialog(Bundle arg0) {
		// TODO Auto-generated method stub
		
		return super.onCreateDialog(arg0);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg2) {
		// TODO Auto-generated method stub
		
		currentView = inflater.inflate(R.layout.inspect_item_data_entry_dialog, container, false);
		initialView(currentView);
		
		
		if (inspectDataItem != null)
		{
		   if (dataEntryType == InspectDataEntryType.NORMAL_ENTRY){
		      if (inspectDataItem.isGodownComponent())
		      {
		         setupHideViewForGodown();
		      }
		   }else{
		      /*
		       * universal
		       */
		      setupHideViewForUniveralLayout();
		   }
		}
		return currentView;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		
	
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if (inspectDataObjectSaved != null)
		{
			if (this.inspectDataObjectSaved.getMarketPrice() > 0){
				edt_marketPrice.setText(
					DataUtil.numberFormat(
								this.inspectDataObjectSaved.getMarketPrice()
								)
							);						
			}else{
				edt_marketPrice.setText("");
				edt_marketPrice.setEnabled(true);
			}				
		}else{
			edt_marketPrice.setText("");				
			edt_marketPrice.setEnabled(true);				
		}
	}
	public OnClickInspectDataEntryDialog getOnClickInspectDataEntryDialog() {
		return onClickInspectDataEntryDialog;
	}
	public void setOnClickInspectDataEntryDialog(
			OnClickInspectDataEntryDialog onClickInspectDataEntryDialog) {
		this.onClickInspectDataEntryDialog = onClickInspectDataEntryDialog;
	}
	public void initialView(final View v)
	{
		
		edt_width = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_width);
		edt_long = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_long);
		edt_height = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_heigh);
		edt_radius = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_radius);
		edt_lost = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_lost);
		edt_over = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_over);
		edt_total = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_total);
		edt_value = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_value);		
		edt_opinion = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_opinion);		
		edt_marketPrice = (EditText)v.findViewById(R.id.et_market_price);
		edt_density = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_density);
		edt_palateAmount = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_palate_amount);
		edt_objectName = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_object_name);
		edt_areaUniversal = (EditText)v.findViewById(R.id.et_inspect_entry_dlg_area);
		
		tvFormula = (TextView)v.findViewById(R.id.tv_formula);
		chkIsProductControlled = (CheckBox)v.findViewById(R.id.chk_product_controlled);
		
		
		vCommentContainer = v.findViewById(R.id.ll_comment);
		vLayoutNameContainer = v.findViewById(R.id.ll_layout_name);
		
		edt_width.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_long.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_height.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_radius.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_lost.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_over.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_total.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_value.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_marketPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});//6 digit for market price
		edt_density.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		edt_palateAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
		
		edt_value.setEnabled(false);/*values = markpet price * capacity*/
		edt_total.setEnabled(false);
		
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		
		try {
			this.customerSite = this.getArguments().getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
			marketPriceFinder = dataAdapter.getMarketPriceFinderBy(customerSite.getProvinceID());//hard code
			//marketPriceFinder = dataAdapter.getMarketPriceFinderBy(-1);//hard code
			
			
			marketPriceGlobalFinder = dataAdapter.getMarketPriceFinderBy(184/*fix*/);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageBox.showMessage(getActivity(), 
					getActivity().getString(R.string.text_error_title),
					e.getMessage());
		}
		
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edt_width.getWindowToken(), 0);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		edt_width.clearFocus();
		////////

        
		sp_product_unit = (ProductUnitSpinner)v.findViewById(R.id.sp_inspect_data_entry_amount_unit);

		sp_product_group = (ProductGroupSpinner)v.findViewById(R.id.sp_inspect_data_entry_product_group);
        
        sp_product = (ProductSpinner)v.findViewById(R.id.sp_inspect_data_entry_product);

		Bundle bArgument = this.getArguments();
		
        int dataEntryTypeCode = bArgument.getInt(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_TYPE);
        dataEntryType = InspectDataEntryType.getType(dataEntryTypeCode);

		if (dataEntryType == InspectDataEntryType.NORMAL_ENTRY){
		      initialNormalLayout(bArgument,v);
		}else if (dataEntryType == InspectDataEntryType.UNIVERSAL_LAYOUT){
		      initialUniversalLayout(bArgument,v);
		}
		
		
		
		

        
		Button btnSave = (Button)v.findViewById(R.id.btn_inspect_data_entry_dlg_save);
		Button btnCancel = (Button)v.findViewById(R.id.btn_inspect_data_entry_dlg_cancel);
		

		btnSave.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (getOnClickInspectDataEntryDialog() != null)
				{
					/*
					if (productSelected != null)
					{
						inspectDataObjectSaved.setProductID(productSelected.getProductGroupID());
					}*/
					godownDataSaved = (InspectDataObjectSaved) getArguments().getSerializable(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_GODOWN_SAVED);
					double remainCapacity = getArguments().getDouble(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_GODOWN_CAPACITY);
					
					double dWidth = DataUtil.convertToNumber(edt_width.getText().toString());
					double dLong  = DataUtil.convertToNumber(edt_long.getText().toString());
					double dHeight = DataUtil.convertToNumber(edt_height.getText().toString());
					//String text_value = edt_value.getText().toString();
					
					double dValue = DataUtil.convertToNumber(edt_total.getText().toString());/*get capacity of current product*/
					
					if (godownDataSaved != null)
					{
						if ((dWidth > godownDataSaved.getWidth())||
						   (dLong > godownDataSaved.getdLong())||
						   (dHeight > godownDataSaved.getHeight())){
							
							MessageBox.showMessage(getActivity(), 
									R.string.text_warning_title, 
									R.string.text_error_godown_maximum);
							//return;
						}
					}
					if (godownDataSaved != null)
					{
						if (dValue > remainCapacity)
						{
							MessageBox.showMessage(getActivity(), 
								getActivity().getString(R.string.text_warning_title), 
								getActivity().getString(R.string.text_error_godown_remain_capacity) + " " + DataUtil.decimal2digiFormat(remainCapacity));
							//return;
						
						}
					}
					///////////////
					inspectDataObjectSaved.setObjectName(edt_objectName.getText().toString());
					inspectDataObjectSaved.setProductControlled(chkIsProductControlled.isChecked());
					try{
						inspectDataObjectSaved.setWidth(
									Double.parseDouble(edt_width.getText().toString())
									* inspectDataItem.getConvertRatioWidth()
								);
					}catch(Exception ex){}
					try{
						/*
						 * long is height in 2D
						 */
						inspectDataObjectSaved.setdLong(
									Double.parseDouble(edt_long.getText().toString())
									* inspectDataItem.getConvertRatioDeep()
								);
					}catch(Exception ex){}
					try{
						inspectDataObjectSaved.setHeight(
									Double.parseDouble(edt_height.getText().toString())
									* inspectDataItem.getConvertRatioHeight()
								);
					}catch(Exception ex){}
					try{
						inspectDataObjectSaved.setRadius(Double.parseDouble(edt_radius.getText().toString()));
					}catch(Exception ex){}
					
					
					
					try{
						inspectDataObjectSaved.setLost(DataUtil.convertToNumber(edt_lost.getText().toString()));
					}catch(Exception ex){}
					try{
						inspectDataObjectSaved.setOver(DataUtil.convertToNumber(edt_over.getText().toString()));
					}catch(Exception ex){}
					try{
						/*capacity*/
					   if (dataEntryType == InspectDataEntryType.NORMAL_ENTRY){
					      inspectDataObjectSaved.setTotal(
								DataUtil.convertToNumber(edt_total.getText().toString()));
					   }else{
                          inspectDataObjectSaved.setTotal(
                                DataUtil.convertToNumber(edt_areaUniversal.getText().toString()));					      
					   }
					}catch(Exception ex){}
					try{
						inspectDataObjectSaved.setDensity(
								Double.parseDouble(edt_density.getText().toString()));
					}catch(Exception ex){}
					
					try{
						inspectDataObjectSaved.setPalateAmount(
								Double.parseDouble(edt_palateAmount.getText().toString()));
					}catch(Exception ex){}
					
					try{
						inspectDataObjectSaved.setMarketPrice(
								DataUtil.convertToNumber(
											 edt_marketPrice.getText().toString()
											)
								);
						
						if (inspectDataObjectSaved != null)
						{
							inspectDataObjectSaved.setMarketPriceID(marketPrice.getMarketPriceID());
						}
					}catch(Exception ex){}
					
					if (productAmountUnit != null){
						inspectDataObjectSaved.setProductAmountUnitID(productAmountUnit.getProductAmountUnitID());
						inspectDataObjectSaved.setProductAmountUnitText(productAmountUnit.getUnitName());
					}
					if (productGroupSelected != null){
						inspectDataObjectSaved.setProductGroupID(productGroupSelected.getProductGroupID());
					}					
					if (productSelected != null){
						inspectDataObjectSaved.setProductID(productSelected.getProductID());
					}
					
					
					inspectDataObjectSaved.setOpinionValue(edt_opinion.getText().toString());
					
	                   try{
	                        //total  = capacity * market price
	                      /*
	                       * recalculate alway
	                       */
	                      /*
	                        inspectDataObjectSaved.setValue(
	                                DataUtil.convertToNumber(edt_value.getText().toString())
	                                );
	                                */
	                      //if (inspectDataItem.isInspectObject())
	                      //{
	                          inspectDataObjectSaved.setValue(
	                                inspectDataObjectSaved.getMarketPrice() * 
	                                inspectDataObjectSaved.getQty()
	                                );	                         
	                      //}else{
	                       //   inspectDataObjectSaved.setValue(
	                        //            DataUtil.convertToNumber(edt_value.getText().toString())
	                         //           );	                         
	                     // }
	                    }catch(Exception ex){}

					if ((inspectDataObjectSaved.getWidth() == 0) ||
					   (inspectDataObjectSaved.getdLong() == 0))
					{
						/*
						 * 
						 */
						MessageBox.showMessage(getActivity(), R.string.text_error_title, R.string.text_error_side_of_object);
					}else{
						
						InspectDataEntryDialog.this.dismiss();
						
						getOnClickInspectDataEntryDialog().onClickSaveDataInspectDataEntry(inspectDataItem,
							inspectDataObjectSaved);
					}
				}
			}
			
		});
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				try{
					//capacity
					inspectDataObjectSaved.setTotal(
								lastCapacity
							);
				}catch(Exception ex){}
				try{
					//total  = capacity * market price
					inspectDataObjectSaved.setValue(
							  lastTotalValue
							);
				}catch(Exception ex){}
				
				SharedPreferenceUtil.saveLayoutModified(getActivity(), false);
				
				InspectDataEntryDialog.this.dismiss();
			}
			
		});
		
		////////////////

        edt_width.setOnFocusChangeListener(this);
        edt_long.setOnFocusChangeListener(this);
        edt_height.setOnFocusChangeListener(this);
        edt_radius.setOnFocusChangeListener(this);
        edt_lost.setOnFocusChangeListener(this);
        edt_over.setOnFocusChangeListener(this);
        //edt_total.setOnFocusChangeListener(this);
        //edt_value.setOnFocusChangeListener(this);
        edt_marketPrice.setOnFocusChangeListener(this);
        
        edt_density.setOnFocusChangeListener(this);
        
        edt_palateAmount.setOnFocusChangeListener(this);
        
        /*
         * listening text change 
         */
        edt_width.addTextChangedListener(this);
        edt_long.addTextChangedListener(this);
        edt_height.addTextChangedListener(this);
        edt_radius.addTextChangedListener(this);
        edt_lost.addTextChangedListener(this);
        edt_over.addTextChangedListener(this);
        //edt_total.addTextChangedListener(this);
        //edt_value.addTextChangedListener(this);
        edt_marketPrice.addTextChangedListener(this);
        edt_density.addTextChangedListener(this);
        edt_palateAmount.addTextChangedListener(this);

	}

	private void initialUniversalLayout(Bundle bArgument,final View v)
	{
	   initialNormalLayout(bArgument,v);
	}
	private void initialNormalLayout(Bundle bArgument,final View v)
	{
       if (bArgument != null)
       {
          
           jobRequest = (JobRequest)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);

           inspectDataObjectSaved = (InspectDataObjectSaved)bArgument.getSerializable(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ENTRY_SAVED);
           inspectDataItem = (InspectDataItem)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_INSPECT_DATA_ITEM);
           
                 
           lastCapacity = inspectDataObjectSaved.getTotal();
           lastTotalValue = inspectDataObjectSaved.getValue();

           sp_product_group.initial(jobRequest.getJobRequestID());
           
           
           
           //lastProductGroupID = inspectDataObjectSaved.getProductGroupID();
           //lastProductID = inspectDataObjectSaved.getProductID();
           
           if (inspectDataItem.getMultipleType() == 2)
           {
               TextView tvDensity = (TextView)v.findViewById(R.id.txt_edit_inspect_entry_field_density);
               tvDensity.setVisibility(View.GONE);
               edt_density.setVisibility(View.GONE);
               
               TextView tvPalateAmount = (TextView)v.findViewById(R.id.txt_edit_inspect_entry_field_palate_amount);
               tvPalateAmount.setVisibility(View.VISIBLE);
               edt_palateAmount.setVisibility(View.VISIBLE);
           }
           
           
           if (jobRequest.getInspectType().isCluster()){
               chkIsProductControlled.setVisibility(View.VISIBLE);
               if (inspectDataObjectSaved != null)
                   chkIsProductControlled.setChecked(inspectDataObjectSaved.isProductControlled());
           }
           
           this.getDialog().setTitle(inspectDataItem.getInspectDataItemName());
           
           formular = inspectDataItem.getFormula();
           
           tvFormula.setText(formular);
           
           ArrayList<ProductGroup> productGroup = sp_product_group.getProductGroups();

           if (productGroup.size() > 0)
           {
              for(int i = 0; i < productGroup.size();i++)
              {
                  if (productGroup.get(i).getProductGroupID() == inspectDataObjectSaved.getProductGroupID()){
                      productGroupIdxSelected = i;
                      previousProductGroupSelected = 
                            productGroupSelected = productGroup.get(i);
                      break;
                  }
              }
              
              sp_product_group.setSelection(productGroupIdxSelected,false);
              
              /*
               * first time , never saved
               */
              if (productGroupSelected == null){
                 productGroupSelected  = productGroup.get(productGroupIdxSelected);
              }
              //////
              /*
               * select product after product group because product is depend on product group 
               */
              if (productGroupSelected != null)
              {
                  sp_product.initial(productGroupSelected.getProductGroupID());
              }
              ArrayList<Product> products = sp_product.getProducts();
              if (products != null)
              {
                      for(int i = 0 ; i < products.size();i++)
                      {
                          if (products.get(i).getProductID() == inspectDataObjectSaved.getProductID())
                          {
                              productIdxSelected = i;
                              previousProductSelected = productSelected = products.get(i);
                              sp_product.setSelection(productIdxSelected,false);

                              if (productSelected != null)
                              {
                                  if (productSelected.getDensityValue() != 0)
                                  {
                                      /*
                                       */
                                      if (inspectDataObjectSaved.getDensity() > 0){
                                          edt_density.setText(
                                                          DataUtil.decimal2digiFormat(
                                                                  inspectDataObjectSaved.getDensity())+"");
                                      }else{
                                          edt_density.setText(
                                                      DataUtil.decimal2digiFormat(
                                                              productSelected.getDensityValue())+"");
                                          
                                          if (productSelected.getDensityValue() > 0){
                                             edt_density.setEnabled(false);
                                          }else{
                                             edt_density.setEnabled(true);
                                          }
                                      }
                                  }else{
                                      edt_density.setText(
                                                  DataUtil.decimal2digiFormat(
                                                              inspectDataObjectSaved.getDensity())+"");
                                  }
                              }
                           break;
                        }
                    }
                    //////
                    if (productSelected == null){
                         productSelected = products.get(productIdxSelected);
                    }
              }               
              
              /*
               * 
               */
              if (productSelected != null)
              {
                   sp_product_unit.initial(productSelected);
                    ArrayList<ProductAmountUnit> productUnits =  sp_product_unit.getProductAmountUnits();
                    if (productUnits != null)
                    {
                        for(int i = 0; i < productUnits.size();i++)
                        {
                            if (inspectDataObjectSaved != null)
                            {
                                if (inspectDataObjectSaved.getProductAmountUnitID() != 0)
                                {
                                     if (productUnits.get(i).getProductAmountUnitID() == productSelected.getProductAmountUnitID())
                                     {
                                            productAmountUnit = productUnits.get(i);
                                            productUnitIdxSelected = i;
                                            sp_product_unit.setSelection(productUnitIdxSelected,false);//set old selected
                                            sp_product_unit.setEnabled(false);
                                            break;
                                      }
                                }
                            }
                         }
                    }
              }
              
              
              setupSpinnerListener();/*add new listener for product , productgroup , product unit*/

           }/*end of if*/
           /////////////////////////////////
           
           
           //////////////////////////////////
           /*
           if (inspectDataItem.getMultipleType() > 1)
           {
              
           edt_width.setText(
                   DataUtil.decimal2digiFormat(
                               (inspectDataObjectSaved.getWidth() / inspectDataItem.getConvertRatioWidth())
                           )+"");
           edt_long.setText(
                   DataUtil.decimal2digiFormat(
                               (inspectDataObjectSaved.getdLong() / inspectDataItem.getConvertRatioDeep())
                           )+"");
           edt_height.setText(
                   DataUtil.decimal2digiFormat(
                               (inspectDataObjectSaved.getHeight() / inspectDataItem.getConvertRatioHeight())
                           )+"");
           }else
           {
               edt_width.setText(
                       DataUtil.decimal2digiFormat(
                                   (inspectDataObjectSaved.getWidth() / inspectDataItem.getConvertRatioWidth())
                               )+"");
               edt_long.setText(
                       DataUtil.decimal2digiFormat(
                                   (inspectDataObjectSaved.getdLong() / inspectDataItem.getConvertRatioDeep())
                               )+"");
               edt_height.setText(
                       DataUtil.decimal2digiFormat(
                                   (inspectDataObjectSaved.getHeight() / inspectDataItem.getConvertRatioHeight())
                               )+"");

           }*/
           edt_objectName.setText(inspectDataObjectSaved.getObjectName());
           
           
           edt_width.setText(
                 DataUtil.decimal2digiFormat(
                             (inspectDataObjectSaved.getWidth() / inspectDataItem.getConvertRatioWidth())
                         )+"");
           edt_long.setText(
                 DataUtil.decimal2digiFormat(
                             (inspectDataObjectSaved.getdLong() / inspectDataItem.getConvertRatioDeep())
                         )+"");
           edt_height.setText(
                 DataUtil.decimal2digiFormat(
                             (inspectDataObjectSaved.getHeight() / inspectDataItem.getConvertRatioHeight())
                         )+"");

         
           edt_radius.setText(
                   DataUtil.decimal2digiFormat(inspectDataObjectSaved.getRadius())+"");

           edt_lost.setText(
                   DataUtil.decimal2digiFormat(inspectDataObjectSaved.getLost())+"");
           edt_over.setText(
                   DataUtil.decimal2digiFormat(inspectDataObjectSaved.getOver())+"");
           
           /*total not include market price*/
           edt_total.setText(
                   DataUtil.decimal2digiFormat(inspectDataObjectSaved.getTotal())+"");
           edt_areaUniversal.setText(
                 DataUtil.decimal2digiFormat(inspectDataObjectSaved.getTotal())+"");
           //////////////
           
           
           
           
           edt_value.setText(
                   DataUtil.decimal2digiFormat(inspectDataObjectSaved.getValue())+"");
           
           
           edt_density.setText(
                   DataUtil.decimal2digiFormat(
                           inspectDataObjectSaved.getDensity()));
           
           edt_palateAmount.setText(                   
                   DataUtil.decimal2digiFormat(inspectDataObjectSaved.getPalateAmount()));
           
           edt_marketPrice.setText(
                   DataUtil.decimal2digiFormat(inspectDataObjectSaved.getMarketPrice())
                   );
           //edt_marketPrice.requestFocus();
           
           edt_opinion.setText(inspectDataObjectSaved.getOpinionValue());
           
           
           ///////////////////

       }
       
	}
	private void setupSpinnerListener()
	{
       sp_product_unit.setOnItemSelectedListener(new OnItemSelectedListener(){

           @Override
           public void onItemSelected(AdapterView<?> arg0, View arg1,
                   int arg2, long arg3) {
               // TODO Auto-generated method stub
               productAmountUnit = sp_product_unit.getProductAmountUnits().get(arg2);
           }

           @Override
           public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
               
           }
           
       });
       
       
	   sp_product_group.setOnItemSelectedListener(new OnItemSelectedListener(){

          @Override
          public void onItemSelected(AdapterView<?> arg0, View arg1,
                  int arg2, long arg3) {
              // TODO Auto-generated method stub
              try{
                  productGroupSelected = sp_product_group.getProductGroups().get(arg2);  
                  sp_product.initial(productGroupSelected.getProductGroupID());
                  if (sp_product.getProducts() != null){
                      sp_product.setSelection(productIdxSelected);
                  }
                  setupMarketPrice(edt_marketPrice);
                  if (productGroupSelected.getProductGroupID() != previousProductGroupSelected.getProductGroupID()){
                     previousProductGroupSelected = productGroupSelected;
                  }

              }catch(Exception ex){}
          }

          @Override
          public void onNothingSelected(AdapterView<?> arg0) {
              // TODO Auto-generated method stub
              
          }
          
      });
      
      sp_product.setOnItemSelectedListener(new OnItemSelectedListener(){

          @Override
          public void onItemSelected(AdapterView<?> arg0, View arg1,
                  int arg2, long arg3) 
          {
              // TODO Auto-generated method stub
              if (sp_product.getProducts() == null)return;
              
              productSelected = sp_product.getProducts().get(arg2);
              if (productSelected.getDensityValue() != 0){
                  /*
                   *  add manual product density
                   */
              }
              if (previousProductSelected == null)
                  previousProductSelected = productSelected;
              

              if (previousProductSelected.getProductID() != productSelected.getProductID())
              {
                     isNewProductSelected = true;
                      edt_density.setText(
                              DataUtil.decimal2digiFormat(productSelected.getDensityValue())+""); 
                      
                      if (productSelected.getDensityValue() > 0){
                         edt_density.setEnabled(false);
                      }else{
                         edt_density.setEnabled(true);
                      }
                      /*select new product change lastProductID*/
                      //lastProductID = productSelected.getProductID();
              }else{
                  isNewProductSelected =  false;
                  
                  if (inspectDataObjectSaved.getDensity() > 0)
                  {
                      edt_density.setText(
                              DataUtil.decimal2digiFormat(inspectDataObjectSaved.getDensity())+"");                       
                  }else{
                      edt_density.setText(
                          DataUtil.decimal2digiFormat(productSelected.getDensityValue())+"");
                      
                        if (productSelected.getDensityValue() > 0){
                             edt_density.setEnabled(false);
                          }else{
                             edt_density.setEnabled(true);
                          }

                  }
              }
              sp_product_unit.initial(productSelected);

              ArrayList<ProductAmountUnit> productUnits =  sp_product_unit.getProductAmountUnits();
              if (productUnits != null)
              {
                  for(int i = 0; i < productUnits.size();i++)
                  {
                      if (inspectDataObjectSaved != null)
                      {
                          if (inspectDataObjectSaved.getProductAmountUnitID() == 0)
                          {
                              //if (productUnits.get(i).getProductAmountUnitID() == productSelected.getProductAmountUnitID())
                              try{
//                                if (productUnits.get(i).getUnitName().equalsIgnoreCase(marketPrice.getUnit()))
                                  if (productUnits.get(i).getProductAmountUnitID() == productSelected.getProductAmountUnitID())
                                  {
                                      productAmountUnit = productUnits.get(i);
                                      productUnitIdxSelected = i;
                                      sp_product_unit.setSelection(productUnitIdxSelected);//set old selected
                                      sp_product_unit.setEnabled(false);
                                      break;
                                  }
                              }catch(Exception ex){ex.printStackTrace();}
                              
                          }
                          else{
                              if (isNewProductSelected)
                              {
                                 if (productUnits.get(i).getProductAmountUnitID() == productSelected.getProductAmountUnitID())
                                 {
                                    productAmountUnit = productUnits.get(i);
                                    productUnitIdxSelected = i;
                                    sp_product_unit.setSelection(productUnitIdxSelected);//set old selected
                                    sp_product_unit.setEnabled(false);
                                    break;
                                 }
                              }else{
                                  if (productUnits.get(i).getProductAmountUnitID() == inspectDataObjectSaved.getProductAmountUnitID()){
                                      productUnitIdxSelected = i;
                                      productAmountUnit = productUnits.get(i);
                                      sp_product_unit.setSelection(productUnitIdxSelected);//set old selected
                                      sp_product_unit.setEnabled(false);
                                      break;

                                  }
                              }
                          }
                      }else{
                          try{
//                            if (productUnits.get(i).getUnitName().equalsIgnoreCase(marketPrice.getUnit()))                              
                              if (productUnits.get(i).getProductAmountUnitID() == productSelected.getProductAmountUnitID())
                              {
                                  productAmountUnit = productUnits.get(i);
                                  productUnitIdxSelected = i;
                                  sp_product_unit.setSelection(productUnitIdxSelected);//set old selected
                                  sp_product_unit.setEnabled(false);
                                  break;
                              }
                          }catch(Exception ex){}
                      }
                  }
              }
              setupMarketPrice(edt_marketPrice);
              
              previousProductSelected = productSelected;
          }

          @Override
          public void onNothingSelected(AdapterView<?> arg0) {
              // TODO Auto-generated method stub
              
          }
          
      });

	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (v instanceof EditText)
		{
			final EditText edt = (EditText)v;
			if (hasFocus)
			{
				previousValue = edt.getText().toString();
				/*
				double d = 0.0;
				try{
					d = Double.parseDouble(edt.getText().toString());
				}catch(Exception ex){}
				if (d > 0)
					edt.setText(DataUtil.decimal2digiFormat(d)+"");
				else
					edt.setText("");
					*/
				edt.setText("");
			}else
			{
				/*
				if (edt.getText().toString().length() == 0){
					edt.setText("0.00");
				}
				*/
				if ((previousValue == null)||(previousValue.trim().length() == 0)){
					edt.setText("0.00");
				}else{
					double d = 0.00;
					try{
						d = Double.parseDouble(previousValue);
					}catch(Exception ex){}
					edt.setText(DataUtil.decimal2digiFormat(d)+"");
				}
			}
			edt.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					edt.setSelection(edt.getText().length());					
				}
				
			});
		}
	}
	
	private void setupMarketPrice(EditText edt_marketPrice)
	{
	  try{
		int productID = 1;
		if (productSelected != null)
			productID = productSelected.getProductID();

		if (marketPriceFinder != null)
		{
					
			marketPrice =  marketPriceFinder.find(
					productGroupSelected.getProductGroupID(), 
					productID);
			
			if (marketPrice == null)
				marketPrice = marketPriceGlobalFinder.find(
						productGroupSelected.getProductGroupID(), 
						productID);
			
		}else if (marketPriceGlobalFinder != null)
		{
			marketPrice = marketPriceGlobalFinder.find(
					productGroupSelected.getProductGroupID(), 
					productID);
		}
		
			
		if ((marketPrice != null)&&(marketPrice.getMarketPrice() > 0))
		{
		        /*have market price*/
				edt_marketPrice.setText(DataUtil.decimal2digiFormat(marketPrice.getMarketPrice())+"");
				edt_marketPrice.setEnabled(false);
				
				if (inspectDataObjectSaved != null)
				{
					inspectDataObjectSaved.setMarketPriceID(marketPrice.getMarketPriceID());
					inspectDataObjectSaved.setMarketPrice(marketPrice.getMarketPrice());
					inspectDataObjectSaved.setCustomMarketPrice(false);
					/////////////////////
				}
				
				/*
				 
				 */
				SpinnerAdapter adapter = sp_product_unit.getAdapter();
				if (adapter != null)
				{
				for(int i = 0 ; i < adapter.getCount();i++)
				{
					Object obj = adapter.getItem(i);
					if (obj != null)
					{
						Log.d("DEBUG_D", "Product unit = "+obj.toString());
						if (obj.toString().trim().equalsIgnoreCase(marketPrice.getUnit().trim()))
						{
							sp_product_unit.setSelection(i);
							sp_product_unit.setEnabled(false);
							
							productAmountUnit = 
									(ProductAmountUnit) sp_product_unit.getProductAmountUnits().get(i);
							
							break;
						}
					}
				  }
				}
			}
			else
			{
               edt_marketPrice.setText("");
               edt_marketPrice.setEnabled(true);
               inspectDataObjectSaved.setMarketPriceID(0);
               inspectDataObjectSaved.setCustomMarketPrice(true);
               if ((previousProductGroupSelected != null)&&(previousProductSelected != null))
               {
                  if ((previousProductGroupSelected.getProductGroupID() == productGroupSelected.getProductGroupID())&&
                        (previousProductSelected.getProductID() == productSelected.getProductID()))
                    {
                          if (this.inspectDataObjectSaved != null)
                          { 
                              if (this.inspectDataObjectSaved.getMarketPrice() > 0)
                              {
                                edt_marketPrice.setText(
                                        DataUtil.numberFormat(
                                                    this.inspectDataObjectSaved.getMarketPrice()
                                                    )
                                                );          
                             }
                          }                   
                    }                 
               }
			}
	  }catch(Exception ex){}
	  
		
//		edt_marketPrice.setEnabled(true);		
//		sp_product_unit.setEnabled(false);
	  
	  
	}
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		try{
			double in_width = 0;
			double in_long = 0;
			double in_height = 0;
			double in_radius = 0;
			double market_price = 1;
			double kilogram = 0;
			
			double in_lost = 0;
			double in_over = 0;
			
			double density = 1;
			double palateAmount = 1;
			
			
			if (arg0.toString().length() > 0)
				previousValue = arg0.toString();
			
			try{
				in_width = Double.parseDouble(edt_width.getText().toString());
			}catch(Exception ex){}
			try{
				in_long = Double.parseDouble(edt_long.getText().toString());				
			}catch(Exception ex){}
			try{
				in_height = Double.parseDouble(edt_height.getText().toString());
			}catch(Exception ex){}
			try{
				in_radius = Double.parseDouble(edt_radius.getText().toString());
			}catch(Exception ex){}
			try{
				market_price = DataUtil.convertToNumber(edt_marketPrice.getText().toString());
			}catch(Exception ex){}
			try{
				in_lost = DataUtil.convertToNumber(edt_lost.getText().toString());
			}catch(Exception ex){}
			try{
				in_over = DataUtil.convertToNumber(edt_over.getText().toString());
			}catch(Exception ex){}
			try{
				density = DataUtil.convertToNumber(edt_density.getText().toString());
			}catch(Exception ex){}
			
			try{
				palateAmount = Double.parseDouble(edt_palateAmount.getText().toString());
			}catch(Exception ex){}

			/*
			try{
				if (density == 0){
					productSelected.getDensityValue();
				}
			}catch(Exception ex){}
			*/
			kilogram = inspectDataItem.getKilogram();

			MathEval mathEval = new MathEval();
			
//			mathEval.setVariable("w", in_width / inspectDataItem.getConvertRatioWidth());
//			mathEval.setVariable("d", in_long / inspectDataItem.getConvertRatioDeep());
//			mathEval.setVariable("h", in_height / inspectDataItem.getConvertRatioHeight());
			mathEval.setVariable("w", in_width );
			mathEval.setVariable("d", in_long );
			mathEval.setVariable("h", in_height);

			mathEval.setVariable("a", in_radius);
			
			Log.d("DEBUG_D", "formular = "+formular);
			
			double resultFormula = mathEval.evaluate(formular);
			
			//boolean needCheckMultipleType = false;
			
			if (dataEntryType == InspectDataEntryType.NORMAL_ENTRY){
			   if ((inspectDataItem != null)&&(productSelected != null))
	            {
			      int multipleType = inspectDataItem.getMultipleType();
	                if (multipleType == 0)
	                {
	                    value = (resultFormula);
	                }
	                else if (multipleType == 1)
	                {
	                    value = (resultFormula * density)/productSelected.getConvertRatio();
	                }else 
	                {
	                    value = (resultFormula * kilogram)/productSelected.getConvertRatio();
	                    value = value * palateAmount;
	                    
	                    in_lost = (in_lost * kilogram)/productSelected.getConvertRatio();
	                    in_over = (in_over * kilogram)/productSelected.getConvertRatio();
	                }
	                value = Math.round(value);
	            }
			}else
			{
			   /*if */
			   value = (resultFormula);
			}
			/////////////////
			
			
			//inspectDataObjectSaved.getInspectDataItemID();
			
			edt_areaUniversal.setText(DataUtil.numberFormat(value));/*set field for universal layout , if it's normal layout this field it's hidden*/
			///////////

			double qty = value - Math.round(in_lost) + Math.round(in_over);/*capacity*/

			
			if (inspectDataObjectSaved != null)
			{
				//inspectDataObjectSaved.setValue(value);/*godown capacity*/
				inspectDataObjectSaved.setQty(qty);/*product capacity*/
			}
			
			//total = value - in_lost + in_over;

			if (kilogram > 0)
			{
				Log.d("DEBUG_D", "kilogram = "+kilogram);
			}
			////////////
		
			edt_total.setText(DataUtil.numberFormat(qty));/*display capacity*/
			edt_value.setText(DataUtil.numberFormat(qty * market_price));/*display all values*/
			

		}catch(Exception e){
			//ignored parse value 
		}
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	private void setupHideViewForUniveralLayout(){
       sp_product_unit.setVisibility(View.GONE);
       sp_product_group.setVisibility(View.GONE);
       sp_product.setVisibility(View.GONE);
       vLayoutNameContainer.setVisibility(View.VISIBLE);
       
       setHideCtrl(currentView,
               new int[]{
                   R.id.btn_inspect_data_entry_dlg_save,
                   R.id.btn_inspect_data_entry_dlg_cancel,
                   R.id.et_inspect_entry_dlg_long,
                   R.id.et_inspect_entry_dlg_width,
                   R.id.tv_label_inspect_data_entry_width,
                   R.id.tv_label_inspect_data_entry_long,
                   R.id.tv_label_inspect_data_entry_opinion,
                   R.id.et_inspect_entry_dlg_opinion,
                   R.id.tv_label_inspect_data_entry_object_name,
                   R.id.et_inspect_entry_dlg_object_name,
                   R.id.tv_label_inspect_data_entry_area,
                   R.id.et_inspect_entry_dlg_area
                   }
                   );
       //vCommentContainer.setVisibility(View.VISIBLE);
	}
	private void setupHideViewForGodown(){
		/*
		edt_width.setVisibility(View.GONE);
		edt_long.setVisibility(View.GONE);
		edt_height.setVisibility(View.GONE);
		edt_radius.setVisibility(View.GONE);
		edt_lost.setVisibility(View.GONE);
		edt_over.setVisibility(View.GONE);
		edt_total.setVisibility(View.GONE);
		edt_value.setVisibility(View.GONE);
		edt_density.setVisibility(View.GONE);
		edt_marketPrice.setVisibility(View.GONE);
		sp_product_unit.setVisibility(View.GONE);
		sp_product_group.setVisibility(View.GONE);
		sp_product.setVisibility(View.GONE);*/
		/*
		if (this.currentView instanceof ViewGroup){
			ViewGroup parent = (ViewGroup)this.currentView;
			for(int i = 0; i < parent.getChildCount();i++)
			{
				View vChild = parent.getChildAt(i);
				int ctrl_id = vChild.getId();
				if (ctrl_id != R.id.btn_inspect_data_entry_dlg_save){
					vChild.setVisibility(View.GONE);
				}
			}
		}
		*/
		sp_product_unit.setVisibility(View.GONE);
		sp_product_group.setVisibility(View.GONE);
		sp_product.setVisibility(View.GONE);
		setHideCtrl(currentView,
				new int[]{
					R.id.btn_inspect_data_entry_dlg_save,
					R.id.btn_inspect_data_entry_dlg_cancel,
					R.id.et_inspect_entry_dlg_long,
					R.id.et_inspect_entry_dlg_width,
					R.id.et_inspect_entry_dlg_heigh,
					R.id.tv_label_inspect_data_entry_width,
					R.id.tv_label_inspect_data_entry_height,
					R.id.tv_label_inspect_data_entry_long
					}
					);
	}
	private void setHideCtrl(View v , int[] ctrl_ids)
	{
		if (v instanceof ViewGroup)
		{
			ViewGroup parent = (ViewGroup)v;
			for(int i = 0; i < parent.getChildCount();i++)
			{
				View vChild = parent.getChildAt(i);
				setHideCtrl(vChild,ctrl_ids);
			}
		}else{
			boolean hasCtrl = false;
			for(int ctrl_id : ctrl_ids){
				if (v.getId() == ctrl_id)
				{
					hasCtrl = true;
					break;
				}				
			}
			if (!hasCtrl){
				v.setVisibility(View.GONE);
			}else{
			   v.setVisibility(View.VISIBLE);
			}
		}
			
	}
}

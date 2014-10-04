package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.adapter.UniversalListEntryAdapter;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectDataSavedSpinnerDisplay;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductGroup;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class InspectDataSavedSpinner extends Spinner {

	private ArrayList<InspectDataSavedSpinnerDisplay> originalDisplayList;
	        
	public InspectDataSavedSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InspectDataSavedSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public InspectDataSavedSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InspectDataSavedSpinner(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public InspectDataSavedSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

   @SuppressWarnings("unchecked")
   public void filter(ArrayList<String> productSelectedList )
   {
	   ArrayList<InspectDataSavedSpinnerDisplay> displayList =
             new ArrayList<InspectDataSavedSpinnerDisplay>();
	   
	   String textFirstRowDefault = this.getContext().getString(R.string.photo_entry_default);


	   displayList.add(originalDisplayList.get(0));
	   
	   
	   
	   //boolean hasFirstRow = false;
	   
	   if (originalDisplayList != null)
	   {
	      for(int i = 0; i < originalDisplayList.size();i++)
	      {
	         InspectDataSavedSpinnerDisplay display = originalDisplayList.get(i);
	         if (display.toString().equalsIgnoreCase(textFirstRowDefault)){
	            continue;
	         }
	         boolean hasProduct = false;
             for(String product : productSelectedList)
             {
                if (product.equalsIgnoreCase(display.toString())){
                      hasProduct = true;
                      continue;
                }
             }
             if (hasProduct){
                continue;
             }
             displayList.add(display);
	      }
	      
	      ArrayAdapter<InspectDataSavedSpinnerDisplay> adapter = 
                new ArrayAdapter<InspectDataSavedSpinnerDisplay>(this.getContext(),
                android.R.layout.simple_spinner_item,displayList);

	      this.setAdapter(adapter);
	   }
	}
    public void initialUniversal(JobRequestProduct jobRequestProduct,
          String taskCode,
          int customerSurveySiteId)
    {
       
       ArrayList<InspectDataSavedSpinnerDisplay> displayList =
             new ArrayList<InspectDataSavedSpinnerDisplay>();

      InspectDataSavedSpinnerDisplay displayFirst = new
           InspectDataSavedSpinnerDisplay(this.getContext());
      
      InspectDataItem item = new InspectDataItem();
      item.setInspectDataItemName(this.getContext().getString(R.string.photo_entry_default));
      displayFirst.inspectDataItem = item;
   
      InspectDataObjectSaved dataSaved = new InspectDataObjectSaved();
      dataSaved.setTaskCode(taskCode);
      dataSaved.setCustomerSurveySiteID(customerSurveySiteId);
      dataSaved.setInspectDataObjectID(-1);
      displayFirst.dataSaved = dataSaved;
      displayList.add(displayFirst);
      
       PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
       try {
          InspectJobMapper jobMapper = 
                dataAdapter.getInspectJobMapper(jobRequestProduct.getJobRequestID(), taskCode);
          if ((jobMapper.getColsInvokeForPhotoEntryItemDisplay() != null)&&
                (!jobMapper.getColsInvokeForPhotoEntryItemDisplay().isEmpty()))
          {
             String colsInvokes 
             = jobMapper.getColsInvokeForPhotoEntryItemDisplay();
             String[] colInvokeArray 
                = colsInvokes.split(",");
             StringBuilder strBld = new StringBuilder();
             for(String colInvoke : colInvokeArray){
                Object objValue = 
                      UniversalListEntryAdapter.invokeGetValue(jobRequestProduct, colInvoke.trim());
                if (!objValue.toString().equalsIgnoreCase("null")){
                   if (!objValue.toString().isEmpty()){
                      strBld.append(objValue);
                      strBld.append(" / ");                      
                   }
                }
             }
             
             
             InspectDataSavedSpinnerDisplay displaySecond = new
                   InspectDataSavedSpinnerDisplay(this.getContext());
             
             
             item = new InspectDataItem();
             item.setInspectDataItemName(strBld.toString());
             displaySecond.inspectDataItem = item;
          
             dataSaved = new InspectDataObjectSaved();
             dataSaved.setTaskCode(taskCode);
             dataSaved.setCustomerSurveySiteID(customerSurveySiteId);
             dataSaved.setInspectDataObjectID(-1);
             displaySecond.dataSaved = dataSaved;
             displayList.add(displaySecond);
             

             //displaySecond.jobRequestProduct = jobRequestProduct;
           
            // displayList.add(displaySecond);
           
             originalDisplayList = displayList;
            
            ArrayAdapter<InspectDataSavedSpinnerDisplay> adapter = 
                    new ArrayAdapter<InspectDataSavedSpinnerDisplay>(this.getContext(),
                    android.R.layout.simple_spinner_item,displayList);
            this.setAdapter(adapter);

          }
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
    }
    public void initial(JobRequestProduct jobRequestProduct,
          String taskCode,
          int customerSurveySiteId)
    {
       ArrayList<InspectDataSavedSpinnerDisplay> displayList =
             new ArrayList<InspectDataSavedSpinnerDisplay>();

      InspectDataSavedSpinnerDisplay displayFirst = new
           InspectDataSavedSpinnerDisplay(this.getContext());
      
      InspectDataItem item = new InspectDataItem();
      item.setInspectDataItemName(this.getContext().getString(R.string.photo_entry_default));
      displayFirst.inspectDataItem = item;
   
      InspectDataObjectSaved dataSaved = new InspectDataObjectSaved();
      dataSaved.setTaskCode(taskCode);
      dataSaved.setCustomerSurveySiteID(customerSurveySiteId);
      dataSaved.setInspectDataObjectID(-1);
      displayFirst.dataSaved = dataSaved;
      displayList.add(displayFirst);
     
      /////////////////////////
      
      InspectDataSavedSpinnerDisplay displaySecond = new
            InspectDataSavedSpinnerDisplay(this.getContext());
      
      
      String productItem = 
            jobRequestProduct.getcMid()+" ";
      productItem += jobRequestProduct.getcModel()+" ";
      productItem += jobRequestProduct.getcDescription()+" ";
      productItem += jobRequestProduct.getcVin()+" ";
      productItem += jobRequestProduct.getcColor()+" ";
      productItem += jobRequestProduct.getcEngine();
      
      //strBld.append(productItem);
      
       item = new InspectDataItem();
      item.setInspectDataItemName(productItem);
      displaySecond.inspectDataItem = item;
   
      dataSaved = new InspectDataObjectSaved();
      dataSaved.setTaskCode(taskCode);
      dataSaved.setCustomerSurveySiteID(customerSurveySiteId);
      dataSaved.setInspectDataObjectID(-1);
      displaySecond.dataSaved = dataSaved;
      displayList.add(displaySecond);
      

      //displaySecond.jobRequestProduct = jobRequestProduct;
    
     // displayList.add(displaySecond);
    
      originalDisplayList = displayList;
     
     ArrayAdapter<InspectDataSavedSpinnerDisplay> adapter = 
             new ArrayAdapter<InspectDataSavedSpinnerDisplay>(this.getContext(),
             android.R.layout.simple_spinner_item,displayList);
     this.setAdapter(adapter);
    }
	public void initial(final ArrayList<InspectDataObjectSaved> dataListSaved,
			final InspectDataSavedSpinnerDisplay lastInspectDataSavedDisplay,
			final String taskCode,
			final int customerSurveySiteId
		)
	{
		final ArrayList<InspectDataSavedSpinnerDisplay> displayList =
				new ArrayList<InspectDataSavedSpinnerDisplay>();

		InspectDataSavedSpinnerDisplay displayFirst = new
				InspectDataSavedSpinnerDisplay(this.getContext());
		InspectDataItem item = new InspectDataItem();
		item.setInspectDataItemName(this.getContext().getString(R.string.photo_entry_default));
		displayFirst.inspectDataItem = item;
		
		InspectDataObjectSaved dataSaved = new InspectDataObjectSaved();
		dataSaved.setTaskCode(taskCode);
		dataSaved.setCustomerSurveySiteID(customerSurveySiteId);
		dataSaved.setInspectDataObjectID(-1);
		displayFirst.dataSaved = dataSaved;
		displayList.add(displayFirst);
		
		
		Thread thread = new Thread()
		{

         @Override
         public void run() 
         {
            // TODO Auto-generated method stub
            if (dataListSaved != null)
            {
               PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
               ArrayList<ProductGroup> productGrps = null;
               ArrayList<InspectDataItem> dataItems = null;
                for(InspectDataObjectSaved obj : dataListSaved)
                {
                    InspectDataSavedSpinnerDisplay display = new InspectDataSavedSpinnerDisplay(getContext());
                    display.dataSaved = obj;
                    display.width = obj.getWidth();
                    display.deep = obj.getdLong();
                    display.height = obj.getHeight();
                    /*
                     * setup other fields
                     */
                    if (obj.getProductGroupID() >= 0)
                    {
                        try{
                           if (productGrps == null)
                              productGrps = dataAdapter.getAllProductGroups();
                            for(ProductGroup pg : productGrps){
                                if (pg.getProductGroupID() == obj.getProductGroupID()){
                                    display.productGroup = pg;
                                    break;
                                }
                            }
                            
                            if (display.productGroup != null)
                            {
                                ArrayList<Product> products = 
                                        dataAdapter.findProductByProductGroupId(display.productGroup.getProductGroupID());
                                for(Product p : products)
                                {
                                    if (p.getProductID() == obj.getProductID()){
                                        display.product = p;
                                        break;
                                    }
                                }
                            }
                            
                            if (dataItems == null)
                               dataItems = dataAdapter.getAllInspectDataItem();
                            
                            for(InspectDataItem dataItem : dataItems)
                            {
                                if (dataItem.getInspectDataItemID() == obj.getInspectDataItemID()){
                                    display.inspectDataItem = dataItem;
                                    break;
                                }
                            }

                            if (display.inspectDataItem != null)
                            {
                                if (!display.inspectDataItem.isComponentBuiding())
                                {
                                    /*
                                     * godown not show in list
                                     */
                                    if (!display.inspectDataItem.isGodownComponent())
                                    {
                                        if (!display.inspectDataItem.isCameraObject()&&
                                                (!display.inspectDataItem.isLine()))
                                        {
                                                    displayList.add(display);
                                        }                               
                                    }
                                }
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    
                    /*
                     * 
                     */
                }
            }
            
            if (getContext() instanceof Activity){
               ((Activity)getContext()).runOnUiThread(new Runnable(){

                  @Override
                  public void run() {
                     // TODO Auto-generated method stub
                     originalDisplayList = displayList;
                     
                     ArrayAdapter<InspectDataSavedSpinnerDisplay> adapter = 
                             new ArrayAdapter<InspectDataSavedSpinnerDisplay>(getContext(),
                             android.R.layout.simple_spinner_item,displayList);
                     setAdapter(adapter);
                  }
                  
               });
            }
            
         }
		};
		thread.start();
	}
}

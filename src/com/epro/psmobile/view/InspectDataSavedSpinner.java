package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectDataSavedSpinnerDisplay;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductGroup;

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
    public void initial(JobRequestProduct jobRequestProduct,String taskCode,int customerSurveySiteId)
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
	public void initial(ArrayList<InspectDataObjectSaved> dataListSaved,
			InspectDataSavedSpinnerDisplay lastInspectDataSavedDisplay,
			String taskCode,
			int customerSurveySiteId
		)
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
		
		
		if (dataListSaved != null)
		{
		   PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getContext());
	        for(InspectDataObjectSaved obj : dataListSaved)
	        {
	            InspectDataSavedSpinnerDisplay display = new InspectDataSavedSpinnerDisplay(this.getContext());
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
	                    ArrayList<ProductGroup> productGrps = dataAdapter.getAllProductGroups();
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
	                    
	                    ArrayList<InspectDataItem> dataItems = dataAdapter.getAllInspectDataItem();
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
		
		originalDisplayList = displayList;
		
		ArrayAdapter<InspectDataSavedSpinnerDisplay> adapter = 
				new ArrayAdapter<InspectDataSavedSpinnerDisplay>(this.getContext(),
				android.R.layout.simple_spinner_item,displayList);
		this.setAdapter(adapter);
		
	}
}

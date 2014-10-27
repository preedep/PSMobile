/**
 * 
 */
package com.epro.psmobile.view;

import java.util.ArrayList;
import java.util.Hashtable;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductGroup;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author nickmsft
 *
 */
public class ProductGroupSpinner extends Spinner {
	private ArrayList<ProductGroup> productGroups;

	public static 
    Hashtable<Integer,ArrayList<ProductGroup>> productGroupTable = new Hashtable<Integer,ArrayList<ProductGroup>>();

	/**
	 * @param context
	 */
	public ProductGroupSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param mode
	 */
	public ProductGroupSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ProductGroupSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ProductGroupSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param mode
	 */
	public ProductGroupSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	public void initial(int jobRequestId){
		PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(this.getContext());
		try {
		    productGroups = null;
		    if (!ProductGroupSpinner.productGroupTable.containsKey(jobRequestId)){
	            productGroups = dbAdapter.findProductGroupByJobRequestId(jobRequestId) ;//dbAdapter.getAllProductGroups();
	            if (productGroups != null){
	               ProductGroupSpinner.productGroupTable.put(jobRequestId, productGroups);
	            }
		    }else{
		       productGroups = ProductGroupSpinner.productGroupTable.get(jobRequestId);
		    }
			
			if (productGroups == null)return;
			
			
			String strProductGroups[] = new String[productGroups.size()];
			for(int i = 0; i < productGroups.size();i++)
			{
				strProductGroups[i] = productGroups.get(i).getProductGroupName();
			}
			
			
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
					android.R.layout.simple_spinner_item,
					strProductGroups);
			
			if (this.getContext() instanceof Activity){
			   ((Activity)this.getContext()).runOnUiThread(new Runnable(){

               @Override
               public void run() {
                  // TODO Auto-generated method stub
                  setAdapter(adapter);
               }
			      
			   });
			}else{
			   this.setAdapter(adapter);
			}
			/*
			if (productGroups.size() >= 0){
				this.setSelection(0,false);
			}*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<ProductGroup> getProductGroups(){
		if (productGroups == null)
			return new ArrayList<ProductGroup>();
		return productGroups;
	}
}

/**
 * 
 */
package com.epro.psmobile.view;

import java.util.ArrayList;
import java.util.Hashtable;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductGroup;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * @author nickmsft
 *
 */
public class ProductSpinner extends Spinner {

	private ArrayList<Product> products;
	
	public static 
	   Hashtable<Integer,ArrayList<Product>> productTable = new Hashtable<Integer,ArrayList<Product>>();
	/**
	 * @param context
	 */
	public ProductSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param mode
	 */
	public ProductSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ProductSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ProductSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param mode
	 */
	public ProductSpinner(Context context, AttributeSet attrs, int defStyle,
			int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}
	public ArrayList<Product> findProductByGroupID(int productGroupID){
	   PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(this.getContext());
       try {
           products = null;
            //products =  dbAdapter.findProductByProductGroupId(productGroupID);
           if (!ProductSpinner.productTable.containsKey(productGroupID)){
               products = dbAdapter.findProductWithUnitByProductGroupId(productGroupID);   
               if (products != null){
                  ProductSpinner.productTable.put(productGroupID, products);
               }
           }else{
              products = ProductSpinner.productTable.get(productGroupID);
           }
       }catch(Exception ex){
          ex.printStackTrace();
       }
       return products;
	}
	public void initialWithJobRequestID(int jobRequestId)
	{
	    products = new ArrayList<Product>();
	    PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(this.getContext());
	    try{
	    ArrayList<ProductGroup> productGroups = dbAdapter.findProductGroupByJobRequestId(jobRequestId);
	       for(ProductGroup productGroup : productGroups){
	          try {
	             ArrayList<Product> tmp_products = 
	                   dbAdapter.findProductWithUnitByProductGroupId(productGroup.getProductGroupID());
	             if (tmp_products != null){
	                products.addAll(tmp_products);
	             }
	          }
	          catch (Exception e) {
	             // TODO Auto-generated catch block
	                e.printStackTrace();
	          }   	       
	       }
	    }
	    catch(Exception ex){}
	    if (products != null)
        {
            ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(
                  this.getContext(),android.R.layout.simple_spinner_item,
                  products);

            this.setAdapter(adapter);
        }
	}
	public void initial(int productGroupID)
	{
		PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(this.getContext());
		try {
		    products = null;
			 //products =  dbAdapter.findProductByProductGroupId(productGroupID);
		    if (!ProductSpinner.productTable.containsKey(productGroupID))
		    {
	            products = dbAdapter.findProductWithUnitByProductGroupId(productGroupID);	
	            if (products != null){
	               ProductSpinner.productTable.put(productGroupID, products);
	            }
		    }else{
		       products = ProductSpinner.productTable.get(productGroupID);
		    }
		    ///////////////
		    
			if (products != null)
			{
	            ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(
	                  this.getContext(),android.R.layout.simple_spinner_item,
	                  products);

				this.setAdapter(adapter);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (products == null)
		{
			try{
				if ((this.getAdapter() != null) && (!this.getAdapter().isEmpty()))
				{
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,new String[]{""});
					this.setAdapter(adapter);
				}else{
					this.setAdapter(null);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public ArrayList<Product> getProducts(){
		return products;
	}
}

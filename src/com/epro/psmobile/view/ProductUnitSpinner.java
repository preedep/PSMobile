/**
 * 
 */
package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductAmountUnit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author nickmsft
 *
 */
public class ProductUnitSpinner extends Spinner {

	public static 
	ArrayList<ProductAmountUnit> productAmountUnits;
	/**
	 * @param context
	 */
	public ProductUnitSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param mode
	 */
	public ProductUnitSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ProductUnitSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ProductUnitSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param mode
	 */
	public ProductUnitSpinner(Context context, AttributeSet attrs,
			int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}
	public void initial(){
       PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(this.getContext());
       try {
          if (ProductUnitSpinner.productAmountUnits == null)
             ProductUnitSpinner.productAmountUnits  = dbAdapter.getProductAmountUnit();
       }catch(Exception ex){}	   
	}
	public void initial(Product product){
		PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(this.getContext());
		try {
		   /*
			productAmountUnits =  dbAdapter.getProductAmountUnit();//dbAdapter.getProductAmountUnitById(product.getProductID());
			if (productAmountUnits == null)
				productAmountUnits = dbAdapter.getProductAmountUnit();
			*/
		    if (ProductUnitSpinner.productAmountUnits == null)
		          ProductUnitSpinner.productAmountUnits  = dbAdapter.getProductAmountUnit();

		    /*no data*/
		    if (ProductUnitSpinner.productAmountUnits == null)return;
		    
			String strProducts[] = new String[productAmountUnits.size()];
			
			for(int i = 0; i < productAmountUnits.size();i++)
			{
				strProducts[i] = productAmountUnits.get(i).getUnitName();
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,strProducts);
			this.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if (productAmountUnits == null)
		{
			try{
				this.setAdapter(null);
			}catch(Exception ex){}
		}
	}
	public ArrayList<ProductAmountUnit> getProductAmountUnits(){
		return productAmountUnits;
	}
}

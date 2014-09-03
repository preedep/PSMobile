package com.epro.psmobile.data;

import com.epro.psmobile.R;
import com.epro.psmobile.util.DataUtil;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class InspectDataSavedSpinnerDisplay implements Parcelable {

	private Context context;
	public enum SpinnerItemType
	{
		NORMAL(0),
		OTHER(1);
		
		int code;
		SpinnerItemType(int code)
		{
			this.code = code;
		}
		public int getCode(){
			return code;
		}
		public static SpinnerItemType findByCode(int code)
		{
			for(int i = 0 ; i < values().length;i++)
			{
				if (values()[i].getCode() == code)
				{
					return values()[i];
				}
			}
			return NORMAL;
		}
	};

	public static final Parcelable.Creator<InspectDataSavedSpinnerDisplay> CREATOR = new Parcelable.Creator<InspectDataSavedSpinnerDisplay>()
	{

		@Override
		public InspectDataSavedSpinnerDisplay createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new InspectDataSavedSpinnerDisplay(source);
		}

		@Override
		public InspectDataSavedSpinnerDisplay[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InspectDataSavedSpinnerDisplay[size];
		}
	};
	public InspectDataSavedSpinnerDisplay(Parcel source)
	{
		//dataSaved = source.readParcelable(InspectDataObjectSaved.class.getClassLoader());
		product = source.readParcelable(Product.class.getClassLoader());
		productGroup = source.readParcelable(ProductGroup.class.getClassLoader());
		inspectDataItem = source.readParcelable(InspectDataItem.class.getClassLoader());
		spinerItemType = SpinnerItemType.findByCode(source.readInt());
		objectId = source.readInt();
		width = source.readDouble();
		deep = source.readDouble();
		height = source.readDouble();
	}
	public InspectDataSavedSpinnerDisplay(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		//arg0.writeParcelable(dataSaved, arg1);
		arg0.writeParcelable(product, arg1);
		arg0.writeParcelable(productGroup, arg1);
		arg0.writeParcelable(inspectDataItem, arg1);
		arg0.writeInt(spinerItemType.getCode());
		arg0.writeInt(objectId);
		arg0.writeDouble(width);
		arg0.writeDouble(deep);
		arg0.writeDouble(height);
	}
	public InspectDataObjectSaved dataSaved;
	public SpinnerItemType spinerItemType = SpinnerItemType.NORMAL;
	public Product product;
	public ProductGroup productGroup;
	public InspectDataItem inspectDataItem;
	public double width;
	public double deep;
	public double height;
	
	public int objectId;
	
	public JobRequestProduct jobRequestProduct;
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		
		if (jobRequestProduct != null)
		{
		   /*
		    * car inspect
		    * 
		    */
		   String productItem = 
		         jobRequestProduct.getcMid()+" ";
		   productItem += jobRequestProduct.getcModel()+" ";
		   productItem += jobRequestProduct.getcDescription()+" ";
		   productItem += jobRequestProduct.getcVin()+" ";
		   productItem += jobRequestProduct.getcColor()+" ";
		   productItem += jobRequestProduct.getcEngine();
		   
		   strBld.append(productItem);
		}else{
		   /*
		    */
	        if (this.inspectDataItem != null)
	        {
	            strBld.append(inspectDataItem.getInspectDataItemName());
	        }
	        if (this.productGroup != null)
	        {
	            strBld.append("/"+this.productGroup.getProductGroupName());
	        }
	        if (this.product != null)
	        {
	            strBld.append("/"+this.product.getProductName()+" (pid:"+product.getProductID()+","+dataSaved.getInspectDataObjectID()+")");
	        }
	        if ((width > 0) || (deep > 0) || (height > 0)){
	            
	            strBld.append(" (");
	            
	            int multipleType = inspectDataItem.getMultipleType();
	            
	            if (multipleType == 0)
	            {
	               /*
	               strBld.append(DataUtil.decimal2digiFormat(width)+" X " +
                            ""+DataUtil.decimal2digiFormat(deep)+" X "+
                            ""+DataUtil.decimal2digiFormat(height));
                    strBld.append(" )");
                    */
	               strBld.append(DataUtil.decimal2digiFormat(Math.round(width / inspectDataItem.getConvertRatioWidth()))+" X " +
                         ""+DataUtil.decimal2digiFormat(Math.round(deep / inspectDataItem.getConvertRatioDeep()))+" X "+
                         ""+DataUtil.decimal2digiFormat(Math.round(height / inspectDataItem.getConvertRatioHeight())));
                   strBld.append(" )"); 
                   
	            }else if (multipleType == 1)
	            {
	                strBld.append(DataUtil.decimal2digiFormat(width)+" X " +
	                        ""+DataUtil.decimal2digiFormat(deep)+" X "+
	                        ""+DataUtil.decimal2digiFormat(height));
	                strBld.append(" )");
	            }else
	            {
	               strBld.append(DataUtil.decimal2digiFormat(Math.round(width / inspectDataItem.getConvertRatioWidth()))+" X " +
                         ""+DataUtil.decimal2digiFormat(Math.round(deep / inspectDataItem.getConvertRatioDeep()))+" X "+
                         ""+DataUtil.decimal2digiFormat(Math.round(height / inspectDataItem.getConvertRatioHeight())));
	               strBld.append(" )"); 
	            }
	            strBld.append(" (");
	            strBld.append(DataUtil.decimal2digiFormat(dataSaved.getTotal())+" / ");
	            strBld.append(dataSaved.getProductAmountUnitText());
	            strBld.append(" )");
	            if (inspectDataItem.isInspectObject())
	            {
	                if (dataSaved.isProductControlled())
	                {
	                    strBld.append("("+context.getString(R.string.report_inspection_product_in_controlled)+")");
	                }else{
	                    //strBld.append("("+context.getString(R.string.report_inspection_product_not_controlled)+")");                  
	                }
	            }
	        } 
		}
		return strBld.toString();
	}
}

package com.epro.psmobile.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductGroup;
import com.epro.psmobile.view.InspectItemViewState;

public class HTMLInspectPreviewUtil {

	public static String writeHtml(Context context,
			String htmlFileName,
			String inspectImageFileName,
			ArrayList<InspectItemViewState> viewStateList) throws Exception
	{
		String htmlFile = "";
		File directory = Environment.getExternalStorageDirectory();
		String rootFolder = directory.getAbsolutePath();
		rootFolder += CommonValues.PREVIEW_FOLDER;
		File folder = new File(rootFolder);
		if (!folder.exists())
		{
			folder.mkdirs();
		}
		rootFolder += htmlFileName;
		htmlFile = rootFolder;
		File fHtml = new File(rootFolder);
		if (fHtml.exists())
		{
			fHtml.delete();
		}
		
		PSBODataAdapter dataAdater = PSBODataAdapter.getDataAdapter(context);
		FileOutputStream f = new FileOutputStream(fHtml);
        PrintWriter pw = new PrintWriter(f);
        pw.write("<head>");
        pw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        pw.write("</head>");
        pw.write("<html>");
        pw.write("<body>");
        //  String data = "<body>" + "<img src=\\"file:///android_asset/large_image.png\"/></body>";  
        //pw.write("<div style=\"height:1000px; width:800px; overflow:auto;\">");
        pw.write("<table>");
        pw.write("<tr>");
        pw.write("<td valign=\"top\">");
        pw.write("<div style=\"height:1200px; width:1100px; overflow:auto;\">");
        
//        pw.write("<img src=\"file://"+inspectImageFileName+"\" width=\"100%\" />");
        String encodeBase64 = 
				ImageUtil.convertImageToBase64(inspectImageFileName);
		if (encodeBase64 != null)
		{
			pw.write("<img src=\"data:image/jpg;base64,"+encodeBase64+"\" width=\"2651px\" height=\"3750px\"/>");
		}
        pw.write("</div>");
        pw.write("</td>");
        pw.write("<td valign=\"top\">");
        pw.write("<br/>");
        pw.write("<br/>");
        pw.write("<br/>");
        
        Collections.sort(viewStateList, new Comparator<InspectItemViewState>(){

			@Override
			public int compare(InspectItemViewState lhs,
					InspectItemViewState rhs) {
				// TODO Auto-generated method stub
				return lhs.getInspectDataObjectSaved().getInspectDataObjectID() - 
						rhs.getInspectDataObjectSaved().getInspectDataObjectID();
			}
        	
        });
        
        ArrayList<JobRequestProduct> jobRequestProducts = null;
        
        PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);
        if (viewStateList.size() > 0){
           int siteId = 
                 viewStateList.get(0).getInspectDataObjectSaved().getCustomerSurveySiteID();
           
           int jobRequestID = 
                 viewStateList.get(0).getInspectDataObjectSaved().getJobRequestID();
           
           jobRequestProducts = 
                 dataAdapter.findJobRequestProductsByJobRequestIDWithWarehouse(jobRequestID,siteId);
        }
        for(InspectItemViewState viewState : viewStateList)
        {
        	StringBuilder strBld = new StringBuilder();
        	int productGroupId =  viewState.getInspectDataObjectSaved().getProductGroupID();
		    int productId = viewState.getInspectDataObjectSaved().getProductID();
		    
		    String inspectDataItemName = viewState.getInspectDataItem().getInspectDataItemName();
		    ProductGroup productGrp =  dataAdater.findProductGroupById(productGroupId);
		    Product product = null;
		    strBld.append("<font color=\"#FF0000\">");		    
		    strBld.append("object id:"+viewState.getInspectDataObjectSaved().getInspectDataObjectID()+"");
		    strBld.append("</font>");
		    strBld.append("<br/>");
		    String itemPrefixName = context.getString(R.string.text_html_inspect_preview_product_name);
		    strBld.append(itemPrefixName+inspectDataItemName+"");
		    Log.d("DEBUG_D", 
		    		viewState.getInspectDataItem().getGroupType().getInspectDataGroupTypeID() + ","+
		    		viewState.getInspectDataItem().getInspectDataItemID()+","+inspectDataItemName
		    		);
		    
		    if 
		    ((!viewState.getInspectDataItem().isCameraObject())&&
		      (!viewState.getInspectDataItem().isComponentBuiding())
		    		)
		    {
		    	if (!viewState.getInspectDataItem().isGodownComponent()){
			    	if (productGrp != null)
			    	{
			    		strBld.append(","+productGrp.getProductGroupName());
			    		product = dataAdater.findProductByProductGroupIdAndProductId(productGroupId, productId);
			    		if (product != null)
			    		{
			    			strBld.append("/"+product.getProductName());
			    		}
			    	}
		    	}
	        	strBld.append("<br/>");
	        	if (!viewState.getInspectDataItem().isGodownComponent())
	        	{
	        	   
	        		String sizeText = context.getString(R.string.text_html_inspect_preview_size);
	        		strBld.append(sizeText);
	        		
	        		double convertWidth = viewState.getInspectDataItem().getConvertRatioWidth();
	        		double convertDeep = viewState.getInspectDataItem().getConvertRatioDeep();//long
	        		double convertHeight = viewState.getInspectDataItem().getConvertRatioHeight();
	        		
	        		strBld.append(
	        				   DataUtil.decimal2digiFormat(
	        				            viewState.getInspectDataObjectSaved().getWidth() / convertWidth
	        				         )
	        				);
	        		strBld.append(" X ");
	        		strBld.append(DataUtil.decimal2digiFormat(
	        		                  viewState.getInspectDataObjectSaved().getdLong() / convertDeep
	        		                 )
	        		             );
	        		strBld.append(" X ");
	        		strBld.append(DataUtil.decimal2digiFormat(
	        		                     viewState.getInspectDataObjectSaved().getHeight() / convertHeight
	        		                  )
	        		              );
	        		strBld.append("<br/>");
	        		
	        		////////////////////
	        		String missCapacity = context.getString(R.string.text_html_inspect_preview_miss_capacity);
	        		strBld.append(missCapacity);
	        		strBld.append(DataUtil.decimal2digiFormat(viewState.getInspectDataObjectSaved().getLost()));
	        		strBld.append("<br/>");
	        		
	        		String overCapcity = context.getString(R.string.text_html_inspect_preview_over_capacity);
	        		strBld.append(overCapcity);
                    strBld.append(DataUtil.decimal2digiFormat(viewState.getInspectDataObjectSaved().getOver()));
                    strBld.append("<br/>");
                    /////////////////	        		
	        		
	        		String valueText = context.getString(R.string.text_html_inspect_preview_value);
	        		strBld.append(valueText);
	        		strBld.append(DataUtil.decimal2digiFormat(viewState.getInspectDataObjectSaved().getValue()));
	        		strBld.append("<br/>");
	        	}
	        	else{
	        	   /*is godown*/
	        	   if (viewState.getInspectDataItem().isUniversalLayoutDropdown())
	        	   {
	        	      /*universal*/
	        	      String strSize = context.getString(R.string.universal_size, 
	        	            DataUtil.decimal2digiFormat(viewState.getInspectDataObjectSaved().getWidth()),
	        	            DataUtil.decimal2digiFormat(viewState.getInspectDataObjectSaved().getdLong()),
	        	            DataUtil.decimal2digiFormat(viewState.getInspectDataObjectSaved().getTotal()));
	        	      
	        	      strBld.append(strSize);
	        	      strBld.append("<br/>");
	        	      
	        	      String layoutName = context.getString(R.string.universal_layout_name, 
	        	            viewState.getInspectDataObjectSaved().getObjectName());
	        	      
	        	      strBld.append(layoutName);
	        	      strBld.append("<br/>");
	        	      
	        	      int total = 0;
	        	      
                      Hashtable<String,Integer> jrpTable = new Hashtable<String,Integer>();
	        	      if (jobRequestProducts != null)
	        	      {
	        	         for(JobRequestProduct jrp : jobRequestProducts)
	        	         {
	        	            if (jrp.getInspectDataObjectID() == viewState.getInspectDataObjectSaved().getInspectDataObjectID())
	        	            {
	        	               total++;
	        	               String proName = jrp.getcModel();
	        	               if (!jrpTable.containsKey(proName)){
	        	                  jrpTable.put(proName, 1);
	        	               }else{
	        	                  int current = jrpTable.get(proName);
	        	                  jrpTable.put(proName, current+1);
	        	               }
	        	            }
	        	         }
	        	      }
	        	      if (total > 0)
	        	      {
	        	         
	        	         if (viewState.getInspectDataObjectSaved().getInspectTypeID() == 
	        	               InspectServiceSupportUtil.SERVICE_CAR_INSPECT){
	        	            
	        	            
	        	            Enumeration<String> keys = jrpTable.keys();
	                         
	                         while(keys.hasMoreElements())
	                         {
	                           String key = keys.nextElement();
	                           String item = 
	                                 context.getString(R.string.univeral_car_item, key,jrpTable.get(key));
	                           strBld.append("<font color='green'><b>"+item+"</b></font>");
	                           strBld.append("<br/>");
	                         }
	                         
	                         
	                         
	                         String strTotal = 
	                               context.getString(R.string.univeral_total_car, total);
	                         strBld.append(strTotal);
	        	         }else{
                            String strTotal = 
                                  context.getString(R.string.univeral_total_item, total);
                            strBld.append(strTotal);
	        	         }
	        	        
                         strBld.append("<br/>");
	        	        
	        	      }
	        	      if ((viewState.getInspectDataObjectSaved().getOpinionValue() != null)&&
	        	            (!viewState.getInspectDataObjectSaved().getOpinionValue().isEmpty())){
	        	         
	        	         String note = context.getString(R.string.universal_note, 
	        	               viewState.getInspectDataObjectSaved().getOpinionValue());
	        	         strBld.append(note);
	        	         strBld.append("<br/>");
	        	      }
	        	      
	        	   }
	        	}
		    }else{
		    	if (viewState.getInspectDataObjectSaved().getInspectDataSavedSpinnerDisplay() != null){
		    		strBld.append(context.getString(R.string.text_html_inspect_preview_conjunction)+" ");
		    		strBld.append(viewState.getInspectDataObjectSaved().getInspectDataSavedSpinnerDisplay().toString());
		    	}
		    	strBld.append("<br/>");
		    }
        	strBld.append("<br/>");
        	
        	pw.write(strBld.toString());
        }        
        //pw.write("test<br/>test<br/>");
        
        pw.write("</td>");
        pw.write("</tr>");
        pw.write("</table>");
        //pw.write("</div>");
        pw.write("</body>");
        pw.write("</html>");
        pw.flush();
        pw.close();
        
        
        return htmlFile;
	}
}

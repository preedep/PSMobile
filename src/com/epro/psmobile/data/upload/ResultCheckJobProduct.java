package com.epro.psmobile.data.upload;


import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.util.JSONDataUtil;

public class ResultCheckJobProduct extends JobRequestProduct 
   implements  UploadDataAdapter {

	private final static String ATT_JOB_NO =  "jobDocumentNo";

	public final static String COLUMN_RESULT_PRODUCT_ROW_ID = "resultProductRowId";
	
	public final static String COLUMN_INSPECT_OBJECT_ID = "inspectDataObjectID";
	private Task task;
	private CustomerSurveySite site;
	private InspectDataObjectSaved inspectDataSaved;
	private JobRequestProduct jobRequestProduct;
	private int resultProductRowId;
	private double marketPrice;
	private int marketPriceID;
	//private int inspectDataObjectID;
	
	public ResultCheckJobProduct(Task task,
			CustomerSurveySite site,
			int resultProductRowId,
			InspectDataObjectSaved inspectDataSaved,
			JobRequestProduct jobRequestProduct) {
		// TODO Auto-generated constructor stub
		this.task = task;
		this.site = site;
		this.inspectDataSaved = inspectDataSaved;
		this.jobRequestProduct = jobRequestProduct;
		this.resultProductRowId = resultProductRowId;
	}

	public int getResultProductRowID(){
		return this.resultProductRowId;
	}
	public int getInsectDataObjectSavedID(){
		return inspectDataSaved.getInspectDataObjectID();
	}
	
	@Override
	public void executeAdapter() {
		// TODO Auto-generated method stub
		this.jobRowId = this.task.getTaskID();
		if (this.site != null){
		   this.jobLocationId = this.site.getCustomerSurveySiteRowID();
		}
		this.jobNo = this.task.getTaskCode();
		
		if (this.inspectDataSaved != null){
			this.productId = this.inspectDataSaved.getProductID();			
			
			this.productQty = this.inspectDataSaved.getQty();
			this.productUnit = this.inspectDataSaved.getProductAmountUnitText();
			
			this.productPrice = this.inspectDataSaved.getMarketPrice();
			this.productValue = this.inspectDataSaved.getValue();
			
			/*
			if (this.getcOrder() == 3){
			   this.inspectDataObjectID = this.inspectDataSaved.getInspectDataObjectID();
			}*/
			
			this.productInfo = this.inspectDataSaved.getOpinionValue();

			this.marketPriceID = this.inspectDataSaved.getMarketPriceID();
			this.marketPrice = this.inspectDataSaved.getMarketPrice();
			
			this.clusterControlFlag = (this.inspectDataSaved.isProductControlled())?"Y":"N";
			
			
			
		}
		
		
	}


	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj =  super.getJSONObject();
		if (this.isAudit){
	        JSONDataUtil.put(jsonObj, COLUMN_RESULT_PRODUCT_ROW_ID, this.resultProductRowId);           
        }else{
           JSONDataUtil.put(jsonObj, COLUMN_RESULT_PRODUCT_ROW_ID, this.productRowID);                      
        }
		
		JSONDataUtil.put(jsonObj, ATT_JOB_NO, this.jobNo);
		if (this.marketPriceID == 0){
			JSONDataUtil.put(jsonObj, COLUMN_MARKET_PRICE_ID, "null");//is null = empty
		}else{
			JSONDataUtil.put(jsonObj, COLUMN_MARKET_PRICE_ID, this.marketPriceID);
		}
		
		//JSONDataUtil.put(jsonObj, COLUMN_MARKET_PRICE,this.marketPrice);
		/*
		 * skip "productType", "itemNo", "productValueText", "jobRequestID", "productGroup", "productRowId"
		 * public final static String COLUMN_PRODUCT_TYPE = "productType";
		 * public final static String COLUMN_ITEM_NO = "itemNo";
		   public final static String COLUMN_PRODUCT_VALUE_TEXT = "productValueText";
		   public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
		   public final static String COLUMN_PRODUCT_ROW_ID = "productRowId";
		   public final static String COLUMN_PRODUCT_GROUP = "productGroup";
		 */
		jsonObj.remove(COLUMN_JOB_NO);
		jsonObj.remove(COLUMN_PRODUCT_TYPE);
		jsonObj.remove(COLUMN_ITEM_NO);
		jsonObj.remove(COLUMN_PRODUCT_VALUE_TEXT);
		jsonObj.remove(COLUMN_JOB_REQUEST_ID);
		jsonObj.remove(COLUMN_PRODUCT_ROW_ID);
		jsonObj.remove(COLUMN_PRODUCT_GROUP);

		return jsonObj;
	}

}

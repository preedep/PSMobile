package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class JobRequestProduct implements DbCursorHolder,
	JSONDataHolder, 
	TransactionStmtHolder ,
	Parcelable
	{
   
    public final static int ROW_RESULT_NORMAL = 0;
    public final static int ROW_RESULT_ERROR = 1;
    public final static int ROW_RESULT_WARNING = 2;

	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_PRODUCT_ROW_ID = "productRowId";
	public final static String COLUMN_ITEM_NO = "itemNo";
	public final static String COLUMN_PRODUCT_GROUP = "productGroup";
	public final static String COLUMN_PRODUCT_GROUP_ID = "productGroupID";
	public final static String COLUMN_PRODUCT_TYPE = "productType";
	public final static String COLUMN_PRODUCT_VALUE_TEXT = "productValueText";
	public final static String COLUMN_C_FRANCHISE = "cFranchise";
	public final static String COLUMN_C_INVOICE_DATE = "cInvoiceDate";
	public final static String COLUMN_C_REGISTER_NUMBER = "cRegisterNumber";
	public final static String COLUMN_C_MID = "cMid";
	public final static String COLUMN_C_VIN = "cVin";
	public final static String COLUMN_C_ENGINE = "cEngine";
	public final static String COLUMN_C_DESCRIPTION = "cDescription";
	public final static String COLUMN_C_COLOR = "cColor";
	public final static String COLUMN_C_MODEL = "cModel";
	public final static String COLUMN_C_YEAR = "cYear";
	public final static String COLUMN_C_TYPE = "cType";
	
	public final static String COLUMN_C_WARE_HOUSE =  "cWareHouse";// INTEGER DEFAULT(0);
	public final static String COLUMN_C_ORDER =  "cOrder";// INTEGER DEFAULT(0);
	public final static String COLUMN_C_SIGHT = "cSight";// TEXT DEFAULT('N');
	public final static String COLUMN_C_EQUIPPING = "cEquipping";// TEXT DEFAULT('N');
	public final static String COLUMN_C_PAY = "cPay";// datetime default CURRENT_TIMESTAMP;
	public final static String COLUMN_C_SOLD =  "cSold";// datetime default CURRENT_TIMESTAMP;
	public final static String COLUMN_C_DATE = "cDate";
	public final static String COLUMN_C_KMS  = "cKms";// TEXT;
	public final static String COLUMN_C_DOCUMENT =  "cDocument";// TEXT;
	public final static String COLUMN_C_REMARK = "cRemark";// TEXT;
	public final static String COLUMN_C_IMAGE = "cImage";
	public final static String COLUMN_C_REASON_ID = "cReasonId";
	public final static String COLUMN_C_REASON_CODE = "cReasonCode";
	public final static String COLUMN_C_ERROR_TYPE = "cErrorType";
	public final static String COLUMN_C_ERROR_TEXT = "cErrorText";
//	public final static String COLUMN_C_CUSTOMER_KASIKORN = "cCustomerKasikon";// TEXT;
	
	public final static String COLUMN_C_TEAM_ID = "cTeamId";
	public final static String COLUMN_C_JOB_ROW_ID = "cJobRowId";
	
	
	public final static String COLUMN_T_NO = "tNo";
	public final static String COLUMN_T_CODE = "tCode";
	public final static String COLUMN_T_SIZE = "tSize";
	public final static String COLUMN_T_WEIGHT = "tWeight";
	public final static String COLUMN_M_CONTRACT_NO = "mContractNo";
	public final static String COLUMN_M_FIXED_ASSET_NO = "mFixedAssetNumber";
	public final static String COLUMN_M_ASSET_NAME = "mAssetName";
	public final static String COLUMN_M_BRAND = "mBrand";
	public final static String COLUMN_M_MODEL = "mModel";
	public final static String COLUMN_M_SERIAL_NO = "mSerialNo";
	public final static String COLUMN_M_CHASSISSNO = "mChassissNo";
	public final static String COLUMN_M_ENGINE_NO = "mEngineNo";
	
	public final static String COLUMN_CR_HIRE = "crHire";
	public final static String COLUMN_CR_OWNER_NAME = "crOwnerName";
	public final static String COLUMN_CR_LICENSE = "crLicense";
	public final static String COLUMN_CR_BRAND = "crBrand";
	public final static String COLUMN_CR_RENEW_DATE = "crRenewDate";
	public final static String COLUMN_CR_CONTRACT_NO = "crContractNo";
	public final static String COLUMN_CR_CONTRACT_DATE = "crContractDate";
	public final static String COLUMN_CR_PRINCIPAL = "crPrincipal";
	public final static String COLUMN_CR_DUE_NUMBER = "crDueNumber";
	public final static String COLUMN_CR_DUE_AMOUNT = "crDueAmount";
	public final static String COLUMN_CR_LAST_PAYMENT = "crLastPayment";
	
	
	public final static String COLUMN_JOB_ROW_ID =  "jobRowId";
	public final static String COLUMN_JOB_LOCATION_ID =  "jobLocationId";
	public final static String COLUMN_CUSTOMER_SURVEY_SITE_ID = "customerSurveySiteID";
	public final static String COLUMN_JOB_NO = "jobNo";
	public final static String COLUMN_RESULT_PRODUCT_ROW_ID =  "resultProductRowId";
	public final static String COLUMN_PRODUCT_ID =  "productId";
	public final static String COLUMN_PRODUCT_QTY =  "productQty";
	public final static String COLUMN_PRODUCT_UNITY = "productUnit";
	public final static String COLUMN_PRODUCT_PRICE = "productPrice";
	public final static String COLUMN_PRODUCT_VALUE =  "productValue";
	public final static String COLUMN_FOUND_FLAG =  "foundFlag";
	public final static String COLUMN_PRODUCT_INFO = "productInfo";
	public final static String COLUMN_CLUSTER_CONTROL_FLAG =  "clusterControlFlag";

	public final static String COLUMN_PHOTO_SET_ID = "photoSetID";
	
	public final static String COLUMN_INSPECT_DATA_OBJ_ID = "inspectDataObjectID";
	
	public final static String COLUMN_MARKET_PRICE_ID = "marketPriceID";
    public final static String COLUMN_MARKET_PRICE = "marketPrice";
    
	public final static String COLUMN_REMARK = "remark";
	
	public final static String COLUMN_IS_AUDIT = "isAudit";
	
	public final static String COLUMN_CHAR_1 = "char1";
	public final static String COLUMN_CHAR_2 = "char2";
	public final static String COLUMN_CHAR_3 = "char3";
	public final static String COLUMN_CHAR_4 = "char4";
	public final static String COLUMN_CHAR_5 = "char5";
	public final static String COLUMN_NUM_1 = "num1";
	public final static String COLUMN_NUM_2 = "num2";
	public final static String COLUMN_NUM_3= "num3";
	public final static String COLUMN_NUM_4 = "num4";
	public final static String COLUMN_NUM_5 = "num5";
	public final static String COLUMN_DEC_1 = "dec1";
	public final static String COLUMN_DEC_2 = "dec2";
	public final static String COLUMN_DEC_3 = "dec3";
	public final static String COLUMN_DATE_1 = "date1";
	public final static String COLUMN_DATE_2 = "date2";
	public final static String COLUMN_DATE_3 = "date3";

	public final static String COLUMN_PRODUCT_NAME = "productName";
	
	protected int jobRowId;
	protected int jobLocationId;
	protected int customerSurveySiteID;
	protected String jobNo;
	protected int resultProductRowId;
	protected int productId;
	protected double productQty;
	protected String productUnit;
	protected double productPrice;
	protected double productValue;
	protected String foundFlag;
	protected String productInfo;
	protected String clusterControlFlag;
	
	
	protected int jobRequestID;
	protected int productRowID;
	protected String itemNo;
	protected String productGroup;
	protected int productGroupID;
	
	protected String productType;
	protected String productValueText;
	protected String cFranchise;
	protected String cInvoiceDate;
	protected String cRegisterNumber;
	protected String cMid;
	protected String cVin;
	protected String cEngine;
	protected String cDescription;
	protected String cColor;
	protected String cModel;
	protected String cYear;
	protected String cType;
	
	
	protected int cWareHouse = 0;// INTEGER DEFAULT(0);
	protected int cOrder;// INTEGER DEFAULT(0);
	protected String cSight = "";// TEXT DEFAULT('N');
	protected String cEquipping = "";// TEXT DEFAULT('N');
	protected String cPay;// datetime default CURRENT_TIMESTAMP;
	protected String cSold;// datetime default CURRENT_TIMESTAMP;
	protected String cDate;
	protected String cKms;// TEXT;
	protected String cDocument;// TEXT;
	protected String cImage;
	protected int cReasonID;
	protected String cReasonCode;
	protected String cRemark;// TEXT;
	protected int cErrorType;
	protected String cErrorText;
	protected int cTeamId;
	protected int cJobRowId;
    
	protected String tNo;
	protected String tCode;
	protected String tSize;
	protected double tWeight;
	protected String mContractNo;
	protected String mFixedAssetNumber;
	protected String mAssetName;
	protected String mBrand;
	protected String mModel;
	protected String mSerialNo;
	protected String mChassissNo;
	protected String mEngineNo;
	
	protected String crHire;
	protected String crOwnerName;
	protected String crLicense;
	protected String crBrand;
	protected String crRenewDate;
	protected String crContractNo;
	protected String crContractDate;
	protected String crPrincipal;
	protected String crDueNumber;
	protected String crDueAmount;
	protected String crLastPayment;
	
	protected int photoSetID;
	
	protected boolean isBackup = false;
	
	protected int inspectDataObjectID = -1;
	
	protected double marketPrice;
	protected int marketPriceID;

	protected String remark;
	
	protected boolean isAudit;
	
	protected String char1;
	protected String char2;// :String
	protected String char3;// :String
	protected String char4;// :String
	protected String char5;// :String
	protected int num1;// :int
	protected int num2;// :int
	protected int num3;// :int
	protected int num4;// :int
	protected int num5;// :int
	protected double dec1;// : decimal
	protected double dec2;//: decimal
	protected double dec3;//: decimal
	protected String date1;// :String
	protected String date2;// :String
	protected String date3;// :String

	protected String productName;
	
	private boolean hasCheckList;
	
	public static final Parcelable.Creator<JobRequestProduct> CREATOR = new Parcelable.Creator<JobRequestProduct>()
			{

				@Override
				public JobRequestProduct createFromParcel(Parcel source) {
					// TODO Auto-generated method stub
					return new JobRequestProduct(source);
				}

				@Override
				public JobRequestProduct[] newArray(int size) {
					// TODO Auto-generated method stub
					return new JobRequestProduct[size];
				}
		
			};
	public JobRequestProduct(Parcel source)
	{
		/*
		dest.writeInt(this.jobRequestID);
		dest.writeInt(this.productRowID);
		dest.writeString(this.itemNo);
		dest.writeString(this.productGroup);
		dest.writeString(this.productType);
		dest.writeString(this.productValueText);
		*/
		this.jobRequestID = source.readInt();
		this.productRowID = source.readInt();
		this.itemNo = source.readString();
		this.productGroup = source.readString();
		this.productType = source.readString();
		this.productValueText = source.readString();

		/*
		dest.writeString(this.cFranchise);
		dest.writeString(this.cInvoiceDate);
		dest.writeString(this.cRegisterNumber);
		dest.writeString(this.cMid);
		dest.writeString(this.cVin);
		dest.writeString(this.cEngine);
		dest.writeString(this.cDescription);
		dest.writeString(this.cColor);
		dest.writeString(this.cModel);
		dest.writeString(this.cYear);
		dest.writeString(this.cType);
		*/
		this.cFranchise = source.readString();
		this.cInvoiceDate = source.readString();
		this.cRegisterNumber = source.readString();
		this.cMid = source.readString();
		this.cVin = source.readString();
		this.cEngine = source.readString();
		this.cDescription = source.readString();
		this.cColor = source.readString();
		this.cModel = source.readString();
		this.cYear = source.readString();
		this.cType = source.readString();
		
		cWareHouse = source.readInt();
	    cOrder = source.readInt();
	   /*
	    * dest.writeInt(cWareHouse);//
        dest.writeInt(cOrder);// = source.readInt();
        dest.writeString(cSight);// TEXT DEFAULT('N');
        dest.writeString(cEquipping);// TEXT DEFAULT('N');
        dest.writeString(cPay);// datetime default CURRENT_TIMESTAMP;
        dest.writeString(cSold);// datetime default CURRENT_TIMESTAMP;
        dest.writeString(cDate);
        dest.writeString(cKms);// TEXT;
        dest.writeString(cDocument);
        dest.writeString(cImage);
        dest.writeInt(cReasonID);
        dest.writeString(cReasonCode);
        dest.writeString(cRemark);
	    */
        cSight = source.readString();// TEXT DEFAULT('N');
        cEquipping = source.readString();// TEXT DEFAULT('N');
        cPay = source.readString();// datetime default CURRENT_TIMESTAMP;
        cSold = source.readString();// datetime default CURRENT_TIMESTAMP;
        cDate = source.readString();
        cKms = source.readString();// TEXT;
        cDocument = source.readString();
        cImage = source.readString();
        cReasonID = source.readInt();
        cReasonCode = source.readString();
        cRemark = source.readString();
	    this.cErrorType = source.readInt();
	    this.cErrorText = source.readString();
	    this.cTeamId = source.readInt();
	    this.cJobRowId = source.readInt();
	    
		/*
		dest.writeString(this.tNo);
		dest.writeString(this.tCode);
		dest.writeString(this.tSize);
		dest.writeString(this.tWeight);*/
		
		this.tNo = source.readString();
		this.tCode = source.readString();
		this.tSize = source.readString();
		this.tWeight = source.readDouble();

		/*
		dest.writeString(this.mContractNo);
		dest.writeString(this.mFixedAssetNumber);
		dest.writeString(this.mAssetName);
		dest.writeString(this.mBrand);
		dest.writeString(this.mModel);
		dest.writeString(this.mSerialNo);
		dest.writeString(this.mChassissNo);
		dest.writeString(this.mEngineNo);
		*/
		
		this.mContractNo = source.readString();
		this.mFixedAssetNumber = source.readString();
		this.mAssetName = source.readString();
		this.mBrand = source.readString();
		this.mModel = source.readString();
		this.mSerialNo = source.readString();
		this.mChassissNo = source.readString();
		this.mEngineNo = source.readString();

		/*
		dest.writeString(this.crHire);
		dest.writeString(this.crOwnerName);
		dest.writeString(this.crLicense);
		dest.writeString(this.crBrand);
		dest.writeString(this.crRenewDate);
		dest.writeString(this.crContractNo);
		dest.writeString(this.crContractDate);
		dest.writeString(this.crPrincipal);
		dest.writeString(this.crDueNumber);
		dest.writeString(this.crDueAmount);
		dest.writeString(this.crLastPayment);
		 */
		this.crHire = source.readString();
		this.crOwnerName = source.readString();
		this.crLicense = source.readString();
		this.crBrand = source.readString();
		this.crRenewDate = source.readString();
		this.crContractNo = source.readString();
		this.crContractDate = source.readString();
		this.crPrincipal = source.readString();
		this.crDueNumber = source.readString();
		this.crDueAmount = source.readString();
		this.crLastPayment = source.readString();
		
		this.photoSetID = source.readInt();
		
		this.inspectDataObjectID = source.readInt();
		
		this.remark = source.readString();
		this.productId = source.readInt();
		
		/*
		 * dest.writeDouble(this.productPrice);
        dest.writeDouble(this.productQty);
        dest.writeDouble(this.productValue);
		 */
		this.productPrice = source.readDouble();
		this.productQty = source.readDouble();
		this.productValue = source.readDouble();
		
		
		this.char1  = source.readString();
		this.char2 = source.readString();// :String
		this.char3 = source.readString();// :String
		this.char4 = source.readString();// :String
		this.char5 = source.readString();// :String
		this.num1 = source.readInt();// :int
		this.num2 = source.readInt();// :int
		this.num3 = source.readInt();// :int
		this.num4 = source.readInt();// :int
		this.num5 = source.readInt();// :int
		this.dec1 = source.readDouble();// : decimal
		this.dec2 = source.readDouble();//: decimal
		this.dec3 = source.readDouble();//: decimal
		this.date1 = source.readString();// :String
		this.date2 = source.readString();// :String
		this.date3 = source.readString();// :String

		this.productName = source.readString();
		this.productRowID = source.readInt();

		boolean[] b = new boolean[1];
		source.readBooleanArray(b);
		this.hasCheckList = b[0];
	}
	public JobRequestProduct() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		if (this.isBackup){
           strBld.append("insert into JobRequestProductBackup");		   
		}else{
		   strBld.append("insert into JobRequestProduct");
		}
		strBld.append("(");
		strBld.append(COLUMN_JOB_REQUEST_ID+",");
		strBld.append(COLUMN_PRODUCT_ROW_ID+",");
		strBld.append(COLUMN_CUSTOMER_SURVEY_SITE_ID+",");
		strBld.append(COLUMN_ITEM_NO+",");
		strBld.append(COLUMN_PRODUCT_GROUP+",");
		strBld.append(COLUMN_PRODUCT_GROUP_ID+",");
		strBld.append(COLUMN_PRODUCT_TYPE+",");
		strBld.append(COLUMN_PRODUCT_ID+",");
		strBld.append(COLUMN_PRODUCT_UNITY+",");
		strBld.append(COLUMN_PRODUCT_VALUE_TEXT+",");
		/*c*/
		strBld.append(COLUMN_C_FRANCHISE+",");
		strBld.append(COLUMN_C_INVOICE_DATE+",");
		strBld.append(COLUMN_C_REGISTER_NUMBER+",");
		strBld.append(COLUMN_C_MID+",");
		strBld.append(COLUMN_C_VIN+",");
		strBld.append(COLUMN_C_ENGINE+",");
		strBld.append(COLUMN_C_DESCRIPTION+",");
		strBld.append(COLUMN_C_COLOR+",");
		strBld.append(COLUMN_C_MODEL+",");
		strBld.append(COLUMN_C_YEAR+",");
		strBld.append(COLUMN_C_TYPE+",");
		
		strBld.append(COLUMN_C_WARE_HOUSE+",");
		strBld.append(COLUMN_C_ORDER+",");// =  "cOrder";// INTEGER DEFAULT(0);
		strBld.append(COLUMN_C_SIGHT+",");// = "cSightNormal";// TEXT DEFAULT('N');
		strBld.append(COLUMN_C_EQUIPPING+",");// = "cSightEquipping";// TEXT DEFAULT('N');
		strBld.append(COLUMN_C_PAY+",");// = "cPayDate";// datetime default CURRENT_TIMESTAMP;
		strBld.append(COLUMN_C_SOLD+",");// =  "cSoldDate";// datetime default CURRENT_TIMESTAMP;
		strBld.append(COLUMN_C_DATE+",");
		strBld.append(COLUMN_C_KMS+",");//  = "cKms";// TEXT;
		strBld.append(COLUMN_C_DOCUMENT+",");// =  "cDocument";// TEXT;
	    strBld.append(COLUMN_C_REMARK+",");// = "cRemark";// TEXT;
	    strBld.append(COLUMN_C_IMAGE+",");
		strBld.append(COLUMN_C_REASON_ID+",");
		strBld.append(COLUMN_C_REASON_CODE+",");
		strBld.append(COLUMN_C_ERROR_TYPE+",");
		strBld.append(COLUMN_C_ERROR_TEXT+",");
		strBld.append(COLUMN_C_TEAM_ID+",");
		strBld.append(COLUMN_C_JOB_ROW_ID+",");
		/*
		 public final static String COLUMN_C_DOCUMENT =  "cDocument";// TEXT;
    public final static String COLUMN_C_REMARK = "cRemark";// TEXT;
    public final static String COLUMN_C_IMAGE = "cImage";
    public final static String COLUMN_C_REASON_ID = "cReasonId";
    public final static String COLUMN_C_REASON_CODE = "cReasonCode";

		 */
		
		/*t*/
		strBld.append(COLUMN_T_NO+",");
		strBld.append(COLUMN_T_CODE+",");
		strBld.append(COLUMN_T_SIZE+",");
		strBld.append(COLUMN_T_WEIGHT+",");

		/*m*/
		strBld.append(COLUMN_M_CONTRACT_NO+",");
		strBld.append(COLUMN_M_FIXED_ASSET_NO +",");
		strBld.append(COLUMN_M_ASSET_NAME +",");
		strBld.append(COLUMN_M_BRAND +",");
		strBld.append(COLUMN_M_MODEL+",");
		strBld.append(COLUMN_M_SERIAL_NO +",");
		strBld.append(COLUMN_M_CHASSISSNO+",");
		strBld.append(COLUMN_M_ENGINE_NO+",");
		/*cr*/
		strBld.append(COLUMN_CR_HIRE+",");
		strBld.append(COLUMN_CR_OWNER_NAME +",");
		strBld.append(COLUMN_CR_LICENSE +",");
		strBld.append(COLUMN_CR_BRAND +",");
		strBld.append(COLUMN_CR_RENEW_DATE +",");
		strBld.append(COLUMN_CR_CONTRACT_NO+",");
		strBld.append(COLUMN_CR_CONTRACT_DATE+",");
		strBld.append(COLUMN_CR_PRINCIPAL+",");
		strBld.append(COLUMN_CR_DUE_NUMBER +",");
		strBld.append(COLUMN_CR_DUE_AMOUNT +",");
		strBld.append(COLUMN_CR_LAST_PAYMENT +",");
		strBld.append(COLUMN_PHOTO_SET_ID+",");
		strBld.append(COLUMN_INSPECT_DATA_OBJ_ID+",");
		strBld.append(COLUMN_PRODUCT_QTY+",");
		strBld.append(COLUMN_PRODUCT_VALUE+",");
		strBld.append(COLUMN_PRODUCT_PRICE+",");
		strBld.append(COLUMN_REMARK+",");
		strBld.append(COLUMN_IS_AUDIT+",");
		strBld.append(COLUMN_JOB_LOCATION_ID+",");
		
		strBld.append(COLUMN_CHAR_1+",");//
		strBld.append(COLUMN_CHAR_2+",");// = "char2";
		strBld.append(COLUMN_CHAR_3+",");// = "char3";
		strBld.append(COLUMN_CHAR_4+",");// = " char4";
		strBld.append(COLUMN_CHAR_5+",");// = "char5";
		strBld.append(COLUMN_NUM_1+",");// = "num1";
		strBld.append(COLUMN_NUM_2+",");// = "num2";
		strBld.append(COLUMN_NUM_3+",");//= "num3";
		strBld.append(COLUMN_NUM_4+",");// = "num4";
		strBld.append(COLUMN_NUM_5+",");// = "num5";
	    strBld.append(COLUMN_DEC_1+",");// = "dec1";
	    strBld.append(COLUMN_DEC_2+",");// = "dec2";
	    strBld.append(COLUMN_DEC_3+",");// = "dec3";
	    strBld.append(COLUMN_DATE_1+",");// = "date1";
	    strBld.append(COLUMN_DATE_2+",");// = "date2";
	    strBld.append(COLUMN_DATE_3+",");// = "date3";
	    strBld.append(COLUMN_PRODUCT_NAME);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		/*
		 strBld.append(COLUMN_JOB_REQUEST_ID+",");
		 strBld.append(COLUMN_PRODUCT_ROW_ID+",");
		 strBld.append(COLUMN_ITEM_NO+",");
		 strBld.append(COLUMN_PRODUCT_GROUP+",");
		 strBld.append(COLUMN_PRODUCT_TYPE+",");
		 strBld.append(COLUMN_PRODUCT_VALUE_TEXT+",");
		 */
		strBld.append(this.jobRequestID+",");
		strBld.append(this.productRowID+",");
		strBld.append(""+this.customerSurveySiteID+",");
		strBld.append("'"+this.itemNo+"',");
		strBld.append("'"+this.productGroup+"',");
		strBld.append(""+this.productGroupID+",");
		strBld.append("'"+this.productType+"',");
	    strBld.append(""+this.productId+",");
	    strBld.append("'"+this.productUnit+"',");
		strBld.append("'"+this.productValueText+"',");
		/*
		 strBld.append(COLUMN_C_FRANCHISE+",");
		 strBld.append(COLUMN_C_INVOICE_DATE+",");
		 strBld.append(COLUMN_C_REGISTER_NUMBER+",");
	 	 strBld.append(COLUMN_C_MID+",");
		 strBld.append(COLUMN_C_VIN+",");
		 strBld.append(COLUMN_C_ENGINE+",");
		 strBld.append(COLUMN_C_DESCRIPTION+",");
		 strBld.append(COLUMN_C_COLOR+",");
		 strBld.append(COLUMN_C_MODEL+",");
		 strBld.append(COLUMN_C_YEAR+",");
		 strBld.append(COLUMN_C_TYPE+",");
		 */
		strBld.append("'"+this.cFranchise+"',");
		strBld.append("'"+this.cInvoiceDate+"',");
		strBld.append("'"+this.cRegisterNumber+"',");
		strBld.append("'"+this.cMid+"',");
		strBld.append("'"+this.cVin+"',");
		strBld.append("'"+this.cEngine+"',");
		strBld.append("'"+this.cDescription+"',");
		strBld.append("'"+this.cColor+"',");
		strBld.append("'"+this.cModel+"',");
		strBld.append("'"+this.cYear+"',");
		strBld.append("'"+this.cType+"',");
		
		strBld.append(""+cWareHouse+",");
		strBld.append(""+cOrder+",");
		/*
        strBld.append(COLUMN_C_SIGHT+",");// = "cSightNormal";// TEXT DEFAULT('N');
        strBld.append(COLUMN_C_EQUIPPING+",");// = "cSightEquipping";// TEXT DEFAULT('N');
        strBld.append(COLUMN_C_PAY+",");// = "cPayDate";// datetime default CURRENT_TIMESTAMP;
        strBld.append(COLUMN_C_SOLD+",");// =  "cSoldDate";// datetime default CURRENT_TIMESTAMP;
        strBld.append(COLUMN_C_DATE+",");
        strBld.append(COLUMN_C_KMS+",");//  = "cKms";// TEXT;
        strBld.append(COLUMN_C_DOCUMENT+",");// =  "cDocument";// TEXT;
        strBld.append(COLUMN_C_REMARK+",");// = "cRemark";// TEXT;
        strBld.append(COLUMN_C_IMAGE+",");
        strBld.append(COLUMN_C_REASON_ID+",");
        strBld.append(COLUMN_C_REASON_CODE+",");
		 */
	    strBld.append("'"+cSight+"',");// TEXT DEFAULT('N');
	    strBld.append("'"+cEquipping+"',");// TEXT DEFAULT('N');
	    strBld.append("'"+cPay+"',");// datetime default CURRENT_TIMESTAMP;
	    strBld.append("'"+cSold+"',");// datetime default CURRENT_TIMESTAMP;
	    strBld.append("'"+cDate+"',");// TEXT;
	    strBld.append("'"+cKms+"',");// TEXT;
	    strBld.append("'"+cDocument+"',");// TEXT;
	    strBld.append("'"+cRemark+"',");// TEXT;
	    strBld.append("'"+cImage+"',");// TEXT;
		strBld.append(""+cReasonID+",");
		strBld.append("'"+cReasonCode+"',");
		strBld.append(""+cErrorType+",");
		strBld.append("'"+cErrorText+"',");
		strBld.append(""+cTeamId+",");
		strBld.append(""+cJobRowId+",");
		
		/*
		 strBld.append(COLUMN_T_NO+",");
		 strBld.append(COLUMN_T_CODE+",");
		 strBld.append(COLUMN_T_SIZE+",");
		 strBld.append(COLUMN_T_WEIGHT+",");
		 */
		strBld.append("'"+this.tNo+"',");
		strBld.append("'"+this.tCode+"',");
		strBld.append("'"+this.tSize+"',");
		strBld.append(""+this.tWeight+",");
		/*
		 strBld.append(COLUMN_M_CONTRACT_NO+",");
		 strBld.append(COLUMN_M_FIXED_ASSET_NO +",");
		 strBld.append(COLUMN_M_ASSET_NAME +",");
		 strBld.append(COLUMN_M_BRAND +",");
		 strBld.append(COLUMN_M_MODEL+",");
		 strBld.append(COLUMN_M_SERIAL_NO +",");
		 strBld.append(COLUMN_M_CHASSISSNO+",");
		 strBld.append(COLUMN_M_ENGINE_NO+",");
		 */
		strBld.append("'"+this.mContractNo+"',");
		strBld.append("'"+this.mFixedAssetNumber+"',");
		strBld.append("'"+this.mAssetName+"',");
		strBld.append("'"+this.mBrand+"',");
		strBld.append("'"+this.mModel+"',");
		strBld.append("'"+this.mSerialNo+"',");
		strBld.append("'"+this.mChassissNo+"',");
		strBld.append("'"+this.mEngineNo+"',");
		/*
		 *strBld.append(COLUMN_CR_HIRE+",");
		  strBld.append(COLUMN_CR_OWNER_NAME +",");
		  strBld.append(COLUMN_CR_LICENSE +",");
		  strBld.append(COLUMN_CR_BRAND +",");
		  strBld.append(COLUMN_CR_RENEW_DATE +",");
		  strBld.append(COLUMN_CR_CONTRACT_NO+",");
		  strBld.append(COLUMN_CR_CONTRACT_DATE+",");
		  strBld.append(COLUMN_CR_PRINCIPAL+",");
		  strBld.append(COLUMN_CR_DUE_NUMBER +",");
		  strBld.append(COLUMN_CR_DUE_AMOUNT +",");
		  strBld.append(COLUMN_CR_LAST_PAYMENT +"");
		 */
		strBld.append("'"+this.crHire+"',");
		strBld.append("'"+this.crOwnerName+"',");
		strBld.append("'"+this.crLicense+"',");
		strBld.append("'"+this.crBrand+"',");
		strBld.append("'"+this.crRenewDate+"',");
		strBld.append("'"+this.crContractNo+"',");
		strBld.append("'"+this.crContractDate+"',");
		strBld.append("'"+this.crPrincipal+"',");
		strBld.append("'"+this.crDueNumber+"',");
		strBld.append("'"+this.crDueAmount+"',");
		strBld.append("'"+this.crLastPayment+"',");
		strBld.append(""+this.photoSetID+",");
		strBld.append(""+this.inspectDataObjectID+",");
		strBld.append(""+this.productQty+",");
		strBld.append(""+this.productValue+",");
		strBld.append(""+this.productPrice+",");
		strBld.append("'"+this.remark+"',");
		strBld.append("'"+this.isAudit+"',");
		strBld.append(""+this.jobLocationId+",");
		
		strBld.append("'"+char1+"',");
		strBld.append("'"+char2+"',");// :String
		strBld.append("'"+char3+"',");// :String
		strBld.append("'"+char4+"',");// :String
		strBld.append("'"+char5+"',");// :String
	    strBld.append(""+num1+",");// :int
	    strBld.append(""+num2+",");// :int
	    strBld.append(""+num3+",");// :int
	    strBld.append(""+num4+",");// :int
	    strBld.append(""+ num5+",");// :int
	    strBld.append(""+dec1+",");// : decimal
	    strBld.append(""+dec2+",");//: decimal
	    strBld.append(""+dec3+",");//: decimal
	    strBld.append("'"+date1+"',");// :String
	    strBld.append("'"+date2+"',");// :String
	    strBld.append("'"+date3+"',");// :String
	    strBld.append("'"+this.productName+"'");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		  this.jobRequestID = JSONDataUtil.getInt(jsonObj,JobRequestProduct.COLUMN_JOB_REQUEST_ID);
		  this.productRowID = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_PRODUCT_ROW_ID);
		  this.itemNo = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_ITEM_NO);
		  this.productGroup = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_PRODUCT_GROUP);
		  this.productId = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_PRODUCT_ID);
		  this.productType = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_PRODUCT_TYPE);
		  this.productValueText = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_PRODUCT_VALUE_TEXT);
		  //c
		  this.cFranchise = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_FRANCHISE);
		  this.cInvoiceDate = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_INVOICE_DATE);
		  this.cRegisterNumber = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_REGISTER_NUMBER);
		  this.cMid = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_MID);
		  this.cVin = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_VIN);
		  this.cEngine = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_ENGINE);
		  this.cDescription = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_DESCRIPTION);
		  this.cColor = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_COLOR);
		  this.cModel = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_MODEL);
		  this.cYear = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_YEAR);
		  this.cType = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_TYPE);
		  
          cWareHouse = JSONDataUtil.getInt(jsonObj, COLUMN_C_WARE_HOUSE);
          cOrder = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_ORDER);
          cSight = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_SIGHT);
          cEquipping = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_EQUIPPING);
          cPay = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_PAY);
          cSold= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_SOLD);
          cDate= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_DATE);
          cKms= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_KMS);
          cDocument= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_DOCUMENT);
          cRemark= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_REMARK);
          cImage = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_IMAGE);
          cReasonID = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_REASON_ID);
          cReasonCode = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_REASON_CODE);
		  cErrorType = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_ERROR_TYPE);
		  cErrorText = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_ERROR_TEXT);
		  cTeamId = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_TEAM_ID);
		  cJobRowId = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_JOB_ROW_ID);
		  
		  //t
		  this.tNo = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_T_NO);
		  this.tCode = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_T_CODE);
		  this.tSize = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_T_SIZE);
		  this.tWeight = JSONDataUtil.getDouble(jsonObj,JobRequestProduct.COLUMN_T_WEIGHT);
		  //m
		  this.mContractNo = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_CONTRACT_NO);
		  this.mFixedAssetNumber = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_FIXED_ASSET_NO);
		  this.mAssetName = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_ASSET_NAME);
		  this.mBrand = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_BRAND);
		  this.mModel = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_MODEL);
		  this.mSerialNo = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_SERIAL_NO);
		  this.mChassissNo = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_CHASSISSNO);
		  this.mEngineNo = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_M_ENGINE_NO);
		  //cr
		  this.crHire = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_HIRE);
		  this.crOwnerName = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_OWNER_NAME);
		  this.crLicense = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_LICENSE);
		  this.crBrand = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_BRAND);
		  this.crRenewDate = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_RENEW_DATE);
		  this.crContractNo = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_CONTRACT_NO);
		  this.crPrincipal = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_PRINCIPAL);
		  this.crDueNumber = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_DUE_NUMBER);
		  this.crDueAmount = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_DUE_AMOUNT);
		  this.crLastPayment = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_CR_LAST_PAYMENT);
		  ///////////////////////////////////
		  this.photoSetID = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_PHOTO_SET_ID);
		  
		  
		  
		  this.char1 = JSONDataUtil.getString(jsonObj, COLUMN_CHAR_1);//
		  this.char2 = JSONDataUtil.getString(jsonObj,COLUMN_CHAR_2);// = "char2";
		  this.char3 = JSONDataUtil.getString(jsonObj,COLUMN_CHAR_3);// = "char3";
		  this.char4 = JSONDataUtil.getString(jsonObj,COLUMN_CHAR_4);// = " char4";
		  this.char5 = JSONDataUtil.getString(jsonObj,COLUMN_CHAR_5);// = "char5";
		  this.num1 = JSONDataUtil.getInt(jsonObj,COLUMN_NUM_1);// = "num1";
		  this.num2 = JSONDataUtil.getInt(jsonObj,COLUMN_NUM_2);// = "num2";
		  this.num3 = JSONDataUtil.getInt(jsonObj,COLUMN_NUM_3);//= "num3";
		  this.num4 = JSONDataUtil.getInt(jsonObj,COLUMN_NUM_4);// = "num4";
		  this.num5 = JSONDataUtil.getInt(jsonObj,COLUMN_NUM_5);// = "num5";
		  this.dec1 = JSONDataUtil.getDouble(jsonObj,COLUMN_DEC_1);// = "dec1";
		  this.dec2 = JSONDataUtil.getDouble(jsonObj,COLUMN_DEC_2);// = "dec2";
		  this.dec3 = JSONDataUtil.getDouble(jsonObj,COLUMN_DEC_3);// = "dec3";
		  this.date1 = JSONDataUtil.getString(jsonObj,COLUMN_DATE_1);// = "date1";
		  this.date2 = JSONDataUtil.getString(jsonObj,COLUMN_DATE_2);// = "date2";
		  this.date3 = JSONDataUtil.getString(jsonObj,COLUMN_DATE_3);// = "date3";

		  
		  this.productName = JSONDataUtil.getString(jsonObj, COLUMN_PRODUCT_NAME);
		  this.jobLocationId = this.customerSurveySiteID = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_CUSTOMER_SURVEY_SITE_ID);
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		
		JSONDataUtil.put(jsonObj, COLUMN_JOB_ROW_ID,jobRowId);
		JSONDataUtil.put(jsonObj, COLUMN_JOB_LOCATION_ID ,jobLocationId);
		JSONDataUtil.put(jsonObj, COLUMN_JOB_NO ,jobNo);
		JSONDataUtil.put(jsonObj, COLUMN_RESULT_PRODUCT_ROW_ID ,resultProductRowId);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_ID ,productId);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_QTY ,productQty);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_UNITY ,productUnit);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_PRICE ,productPrice);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_VALUE ,productValue);
		JSONDataUtil.put(jsonObj, COLUMN_FOUND_FLAG ,foundFlag);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_INFO ,productInfo);
		JSONDataUtil.put(jsonObj, COLUMN_CLUSTER_CONTROL_FLAG ,clusterControlFlag);

		
		JSONDataUtil.put(jsonObj, COLUMN_JOB_REQUEST_ID ,jobRequestID);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_ROW_ID ,productRowID);
		JSONDataUtil.put(jsonObj, COLUMN_ITEM_NO ,itemNo);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_GROUP ,productGroup);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_TYPE ,productType);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_VALUE_TEXT ,productValueText);
		
		JSONDataUtil.put(jsonObj, COLUMN_C_FRANCHISE ,cFranchise);
		JSONDataUtil.put(jsonObj, COLUMN_C_INVOICE_DATE ,cInvoiceDate);
		JSONDataUtil.put(jsonObj, COLUMN_C_REGISTER_NUMBER ,cRegisterNumber);
		JSONDataUtil.put(jsonObj, COLUMN_C_MID ,cMid);
		JSONDataUtil.put(jsonObj, COLUMN_C_VIN ,cVin);
		JSONDataUtil.put(jsonObj, COLUMN_C_ENGINE ,cEngine);
		JSONDataUtil.put(jsonObj, COLUMN_C_DESCRIPTION ,cDescription);
		JSONDataUtil.put(jsonObj, COLUMN_C_COLOR ,cColor);
		JSONDataUtil.put(jsonObj, COLUMN_C_MODEL ,cModel);
		JSONDataUtil.put(jsonObj, COLUMN_C_YEAR ,cYear);
		JSONDataUtil.put(jsonObj, COLUMN_C_TYPE ,cType);
		
		/*
		 * car inspect extend
		 */
		/*
		 cWareHouse = JSONDataUtil.getInt(jsonObj, COLUMN_C_WARE_HOUSE);
          cOrder = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_ORDER);
          cSight = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_SIGHT);
          cEquipping = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_EQUIPPING);
          cPay = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_PAY);
          cSold= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_SOLD);
          cDate= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_DATE);
          cKms= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_KMS);
          cDocument= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_DOCUMENT);
          cRemark= JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_REMARK);
          cImage = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_IMAGE);
          cReasonID = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_REASON_ID);
          cReasonCode = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_REASON_CODE);
          cErrorType = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_ERROR_TYPE);
          cErrorText = JSONDataUtil.getString(jsonObj, JobRequestProduct.COLUMN_C_ERROR_TEXT);
          cTeamId = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_TEAM_ID);
          cJobRowId = JSONDataUtil.getInt(jsonObj, JobRequestProduct.COLUMN_C_JOB_ROW_ID);
		 */

		  JSONDataUtil.put(jsonObj, COLUMN_C_WARE_HOUSE ,this.cWareHouse);
		  JSONDataUtil.put(jsonObj, COLUMN_C_ORDER ,this.cOrder);
		  JSONDataUtil.put(jsonObj, COLUMN_C_SIGHT ,this.cSight);
		  JSONDataUtil.put(jsonObj, COLUMN_C_EQUIPPING ,this.cEquipping);
		  JSONDataUtil.put(jsonObj, COLUMN_C_PAY ,this.cPay);
		  JSONDataUtil.put(jsonObj, COLUMN_C_SOLD ,this.cSold);
		  JSONDataUtil.put(jsonObj, COLUMN_C_DATE ,this.cDate);
		  JSONDataUtil.put(jsonObj, COLUMN_C_KMS ,this.cKms);
		  JSONDataUtil.put(jsonObj, COLUMN_C_DOCUMENT ,this.cDocument);
          JSONDataUtil.put(jsonObj, COLUMN_C_REMARK ,this.cRemark);
          JSONDataUtil.put(jsonObj, COLUMN_C_IMAGE ,this.cImage);
          JSONDataUtil.put(jsonObj, COLUMN_C_REASON_ID ,this.cReasonID);
          JSONDataUtil.put(jsonObj, COLUMN_C_REASON_CODE ,this.cReasonCode);
          JSONDataUtil.put(jsonObj, COLUMN_C_ERROR_TYPE ,this.cErrorType);
          JSONDataUtil.put(jsonObj, COLUMN_C_ERROR_TEXT ,this.cErrorText);
          JSONDataUtil.put(jsonObj, COLUMN_C_TEAM_ID ,this.cTeamId);
          JSONDataUtil.put(jsonObj, COLUMN_C_JOB_ROW_ID ,this.cJobRowId);
		    

		
		 /////////////
		JSONDataUtil.put(jsonObj, COLUMN_T_NO ,tNo);
		JSONDataUtil.put(jsonObj, COLUMN_T_CODE ,tCode);
		JSONDataUtil.put(jsonObj, COLUMN_T_SIZE ,tSize);
		JSONDataUtil.put(jsonObj, COLUMN_T_WEIGHT ,tWeight);
		
		JSONDataUtil.put(jsonObj, COLUMN_M_CONTRACT_NO ,mContractNo);
		JSONDataUtil.put(jsonObj, COLUMN_M_FIXED_ASSET_NO ,mFixedAssetNumber);
		JSONDataUtil.put(jsonObj, COLUMN_M_ASSET_NAME ,mAssetName);
		JSONDataUtil.put(jsonObj, COLUMN_M_BRAND ,mBrand);
		JSONDataUtil.put(jsonObj, COLUMN_M_MODEL ,mModel);
		JSONDataUtil.put(jsonObj, COLUMN_M_SERIAL_NO ,mSerialNo);
		JSONDataUtil.put(jsonObj, COLUMN_M_CHASSISSNO ,mChassissNo);
		JSONDataUtil.put(jsonObj, COLUMN_M_ENGINE_NO ,mEngineNo);
		
		JSONDataUtil.put(jsonObj, COLUMN_CR_HIRE ,crHire);
		JSONDataUtil.put(jsonObj, COLUMN_CR_OWNER_NAME ,crOwnerName);
		JSONDataUtil.put(jsonObj, COLUMN_CR_LICENSE ,crLicense);
		JSONDataUtil.put(jsonObj, COLUMN_CR_BRAND ,crBrand);
		JSONDataUtil.put(jsonObj, COLUMN_CR_RENEW_DATE ,crRenewDate);
		JSONDataUtil.put(jsonObj, COLUMN_CR_CONTRACT_NO ,crContractNo);
		JSONDataUtil.put(jsonObj, COLUMN_CR_CONTRACT_DATE ,crContractDate);
		JSONDataUtil.put(jsonObj, COLUMN_CR_PRINCIPAL ,crPrincipal);
		JSONDataUtil.put(jsonObj, COLUMN_CR_DUE_NUMBER ,crDueNumber);
		JSONDataUtil.put(jsonObj, COLUMN_CR_DUE_AMOUNT ,crDueAmount);
		JSONDataUtil.put(jsonObj, COLUMN_CR_LAST_PAYMENT ,crLastPayment);
		
		if (inspectDataObjectID >= 0){
		   Log.d("DEBUG_D_D", " inspectDataObjectID = " + inspectDataObjectID);
		}
		JSONDataUtil.put(jsonObj, COLUMN_INSPECT_DATA_OBJ_ID,inspectDataObjectID);
		
		JSONDataUtil.put(jsonObj, COLUMN_REMARK, remark);
		
		
		 JSONDataUtil.put(jsonObj, COLUMN_CHAR_1,char1);//
         JSONDataUtil.put(jsonObj,COLUMN_CHAR_2,char2);// = "char2";
         JSONDataUtil.put(jsonObj,COLUMN_CHAR_3,char3);// = "char3";
         JSONDataUtil.put(jsonObj,COLUMN_CHAR_4,char4);// = " char4";
         JSONDataUtil.put(jsonObj,COLUMN_CHAR_5,char5);// = "char5";
         JSONDataUtil.put(jsonObj,COLUMN_NUM_1,num1);// = "num1";
         JSONDataUtil.put(jsonObj,COLUMN_NUM_2,num2);// = "num2";
         JSONDataUtil.put(jsonObj,COLUMN_NUM_3,num3);//= "num3";
         JSONDataUtil.put(jsonObj,COLUMN_NUM_4,num4);// = "num4";
         JSONDataUtil.put(jsonObj,COLUMN_NUM_5,num5);// = "num5";
         JSONDataUtil.put(jsonObj,COLUMN_DEC_1,dec1);// = "dec1";
         JSONDataUtil.put(jsonObj,COLUMN_DEC_2,dec2);// = "dec2";
         JSONDataUtil.put(jsonObj,COLUMN_DEC_3,dec3);// = "dec3";
         JSONDataUtil.put(jsonObj,COLUMN_DATE_1,date1);// = "date1";
         JSONDataUtil.put(jsonObj,COLUMN_DATE_2,date2);// = "date2";
         JSONDataUtil.put(jsonObj,COLUMN_DATE_3,date3);// = "date3";

		return jsonObj;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		/*
			public final static String COLUMN_JOB_NO = "jobNo";
			public final static String COLUMN_RESULT_PRODUCT_ROW_ID =  "resultProductRowId";
			public final static String COLUMN_PRODUCT_ID =  "productId";
			public final static String COLUMN_PRODUCT_QTY =  "productQty";
			public final static String COLUMN_PRODUCT_UNITY = "productUnit";
			public final static String COLUMN_PRODUCT_PRICE = "productPrice";
			public final static String COLUMN_PRODUCT_VALUE =  "productValue";
			public final static String COLUMN_FOUND_FLAG =  "foundFlag";
			public final static String COLUMN_PRODUCT_INFO = "productInfo";
			public final static String COLUMN_CLUSTER_CONTROL_FLAG =  "clusterControlFlag";
		*/
		  this.jobNo = cursor.getString(cursor.getColumnIndex(COLUMN_JOB_NO));
		  this.resultProductRowId = cursor.getInt(cursor.getColumnIndex(COLUMN_RESULT_PRODUCT_ROW_ID));
		  this.productId = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
		  this.productQty = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_QTY));
		  this.productUnit = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_UNITY));
		  this.productPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE));
		  this.productValue = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_VALUE));
		  this.foundFlag = cursor.getString(cursor.getColumnIndex(COLUMN_FOUND_FLAG));
		  this.productInfo = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_INFO));
		  this.clusterControlFlag = cursor.getString(cursor.getColumnIndex(COLUMN_CLUSTER_CONTROL_FLAG));
		  ////////////////////
		  this.jobRequestID = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_JOB_REQUEST_ID));
		  this.productRowID = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_PRODUCT_ROW_ID));
		  this.itemNo = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_ITEM_NO));
		  this.productGroup = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_PRODUCT_GROUP));
		  this.productGroupID = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_PRODUCT_GROUP_ID));
		  this.productType = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_PRODUCT_TYPE));
		  this.productValueText = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_PRODUCT_VALUE_TEXT));
		  //c
		  this.cFranchise = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_FRANCHISE));
		  this.cInvoiceDate = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_INVOICE_DATE));
		  this.cRegisterNumber = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_REGISTER_NUMBER));
		  this.cMid = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_MID));
		  this.cVin = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_VIN));
		  this.cEngine = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_ENGINE));
		  this.cDescription = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_DESCRIPTION));
		  this.cColor = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_COLOR));
		  this.cModel = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_MODEL));
		  this.cYear = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_YEAR));
		  this.cType = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_C_TYPE));
		  
		  cWareHouse = cursor.getInt(cursor.getColumnIndex(COLUMN_C_WARE_HOUSE));
	      cOrder = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_ORDER));
	      cSight = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_SIGHT));
	      cEquipping = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_EQUIPPING));
	      cPay = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_PAY));
	      cSold= cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_SOLD));
	      cDate= cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_DATE));
	      cKms= cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_KMS));
	      cDocument= cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_DOCUMENT));
	      cRemark= cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_REMARK));
	      cImage = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_IMAGE));
	      cReasonID = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_REASON_ID));
	      cReasonCode = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_REASON_CODE));
	      
	       cErrorType = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_ERROR_TYPE));
	       cErrorText = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_ERROR_TEXT));

	       cTeamId = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_TEAM_ID));
	       cJobRowId = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_C_JOB_ROW_ID));
	       
		  //t
		  this.tNo =  cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_T_NO));
		  this.tCode = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_T_CODE));
		  this.tSize = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_T_SIZE));
		  this.tWeight = cursor.getDouble(cursor.getColumnIndex(JobRequestProduct.COLUMN_T_WEIGHT));
		  //m
		  this.mContractNo = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_M_CONTRACT_NO));
		  this.mFixedAssetNumber = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_M_FIXED_ASSET_NO));
		  this.mAssetName = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_M_ASSET_NAME));
		  this.mBrand = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_M_BRAND));
		  this.mModel = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_M_MODEL));
		  this.mSerialNo = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_M_SERIAL_NO));
		  this.mChassissNo = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_M_CHASSISSNO));
		  this.mEngineNo = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_M_ENGINE_NO));
		  //cr
		  this.crHire = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_CR_HIRE));
		  this.crOwnerName = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_OWNER_NAME));
		  this.crLicense = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_LICENSE));
		  this.crBrand = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_BRAND));
		  this.crRenewDate = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_RENEW_DATE));
		  this.crContractNo = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_CONTRACT_NO));
		  this.crPrincipal = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_PRINCIPAL));
		  this.crDueNumber = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_DUE_NUMBER));
		  this.crDueAmount = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_DUE_AMOUNT));
		  this.crLastPayment = cursor.getString(cursor.getColumnIndex( JobRequestProduct.COLUMN_CR_LAST_PAYMENT));
		  ///////////////////////////////////
		  
		  this.photoSetID = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_PHOTO_SET_ID));
		  
		  this.inspectDataObjectID = cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_INSPECT_DATA_OBJ_ID));
		  
		  if (this.inspectDataObjectID >=  0){
		        Log.d("DEBUG_D_D", " onBind from query inspectDataObjectID = " + inspectDataObjectID);

		  }
		  
		  this.remark = cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_REMARK));
		  
		  try{
		  this.isAudit = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(JobRequestProduct.COLUMN_IS_AUDIT)));
		  }catch(Exception ex){}
		  
		  this.jobLocationId = 
		        cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_JOB_LOCATION_ID));
		  
		  this.customerSurveySiteID = 
		        cursor.getInt(cursor.getColumnIndex(JobRequestProduct.COLUMN_CUSTOMER_SURVEY_SITE_ID));
		  
		  
		  
		  this.char1 = cursor.getString(cursor.getColumnIndex( COLUMN_CHAR_1));//
          this.char2 = cursor.getString(cursor.getColumnIndex(COLUMN_CHAR_2));// = "char2";
          this.char3 = cursor.getString(cursor.getColumnIndex(COLUMN_CHAR_3));// = "char3";
          this.char4 = cursor.getString(cursor.getColumnIndex(COLUMN_CHAR_4));// = " char4";
          this.char5 = cursor.getString(cursor.getColumnIndex(COLUMN_CHAR_5));// = "char5";
          this.num1 = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_1));// = "num1";
          this.num2 = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_2));// = "num2";
          this.num3 = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_3));//= "num3";
          this.num4 = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_4));// = "num4";
          this.num5 = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_5));// = "num5";
          this.dec1 = cursor.getDouble(cursor.getColumnIndex(COLUMN_DEC_1));// = "dec1";
          this.dec2 = cursor.getDouble(cursor.getColumnIndex(COLUMN_DEC_2));// = "dec2";
          this.dec3 = cursor.getDouble(cursor.getColumnIndex(COLUMN_DEC_3));// = "dec3";
          this.date1 = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_1));// = "date1";
          this.date2 = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_2));// = "date2";
          this.date3 = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_3));// = "date3";

		  this.productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
	}

	public int getJobRequestID() {
		return jobRequestID;
	}

	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}

	public int getProductRowID() {
		return productRowID;
	}

	public void setProductRowID(int productRowID) {
		this.productRowID = productRowID;
	}

	/**
    * @return the customerSurveySiteID
    */
   public int getCustomerSurveySiteID() {
      return customerSurveySiteID;
   }
   /**
    * @param customerSurveySiteID the customerSurveySiteID to set
    */
   public void setCustomerSurveySiteID(int customerSurveySiteID) {
      this.customerSurveySiteID = customerSurveySiteID;
   }
   public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	/**
    * @return the productGroupID
    */
   public int getProductGroupID() {
      return productGroupID;
   }
   /**
    * @param productGroupID the productGroupID to set
    */
   public void setProductGroupID(int productGroupID) {
      this.productGroupID = productGroupID;
   }
   /**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * @return the productValueText
	 */
	public String getProductValueText() {
		return productValueText;
	}

	/**
	 * @param productValueText the productValueText to set
	 */
	public void setProductValueText(String productValueText) {
		this.productValueText = productValueText;
	}

	/**
	 * @return the cFranchise
	 */
	public String getcFranchise() {
		return cFranchise;
	}

	/**
	 * @param cFranchise the cFranchise to set
	 */
	public void setcFranchise(String cFranchise) {
		this.cFranchise = cFranchise;
	}

	/**
	 * @return the cInvoiceDate
	 */
	public String getcInvoiceDate() {
		return cInvoiceDate;
	}

	/**
	 * @param cInvoiceDate the cInvoiceDate to set
	 */
	public void setcInvoiceDate(String cInvoiceDate) {
		this.cInvoiceDate = cInvoiceDate;
	}

	/**
	 * @return the cRegisterNumber
	 */
	public String getcRegisterNumber() {
		return cRegisterNumber;
	}

	/**
	 * @param cRegisterNumber the cRegisterNumber to set
	 */
	public void setcRegisterNumber(String cRegisterNumber) {
		this.cRegisterNumber = cRegisterNumber;
	}

	/**
	 * @return the cMid
	 */
	public String getcMid() {
		return cMid;
	}

	/**
	 * @param cMid the cMid to set
	 */
	public void setcMid(String cMid) {
		this.cMid = cMid;
	}

	/**
	 * @return the cVin
	 */
	public String getcVin() {
		return cVin;
	}

	/**
	 * @param cVin the cVin to set
	 */
	public void setcVin(String cVin) {
		this.cVin = cVin;
	}

	/**
	 * @return the cEngine
	 */
	public String getcEngine() {
		return cEngine;
	}

	/**
	 * @param cEngine the cEngine to set
	 */
	public void setcEngine(String cEngine) {
		this.cEngine = cEngine;
	}

	/**
	 * @return the cDescription
	 */
	public String getcDescription() {
		return cDescription;
	}

	/**
	 * @param cDescription the cDescription to set
	 */
	public void setcDescription(String cDescription) {
		this.cDescription = cDescription;
	}

	/**
	 * @return the cColor
	 */
	public String getcColor() {
		return cColor;
	}

	/**
	 * @param cColor the cColor to set
	 */
	public void setcColor(String cColor) {
		this.cColor = cColor;
	}

	/**
	 * @return the cModel
	 */
	public String getcModel() {
		return cModel;
	}

	/**
	 * @param cModel the cModel to set
	 */
	public void setcModel(String cModel) {
		this.cModel = cModel;
	}

	/**
	 * @return the cYear
	 */
	public String getcYear() {
		return cYear;
	}

	/**
	 * @param cYear the cYear to set
	 */
	public void setcYear(String cYear) {
		this.cYear = cYear;
	}

	/**
	 * @return the cType
	 */
	public String getcType() {
		return cType;
	}

	/**
	 * @param cType the cType to set
	 */
	public void setcType(String cType) {
		this.cType = cType;
	}

	/**
    * @return the cWareHouse
    */
   public int getcWareHouse() {
      return cWareHouse;
   }
   /**
    * @param cWareHouse the cWareHouse to set
    */
   public void setcWareHouse(int cWareHouse) {
      /*
      if (this.getcMid().equalsIgnoreCase("302390"))
      {
         Log.d("DEBUG_D_D","setcWareHouse");
         if (cWareHouse == 6)
         {
            Log.d("DEBUG_D_D", "What's wrong?");
         }
      }*/
      this.cWareHouse = cWareHouse;
   }
   /**
    * @return the cOrder
    */
   public int getcOrder() {
      return cOrder;
   }
   /**
    * @param cOrder the cOrder to set
    */
   public void setcOrder(int cOrder) {
      this.cOrder = cOrder;
   }
 
   
   
   /**
    * @return the cSight
    */
   public String getcSight() {
      return cSight;
   }
   /**
    * @param cSight the cSight to set
    */
   public void setcSight(String cSight) {
      this.cSight = cSight;
   }
   /**
    * @return the cEquipping
    */
   public String getcEquipping() {
      if (cEquipping == null)return "N";
      return cEquipping;
   }
   /**
    * @param cEquipping the cEquipping to set
    */
   public void setcEquipping(String cEquipping) {
      this.cEquipping = cEquipping;
   }
   /**
    * @return the cPay
    */
   public String getcPay() {
      if (cPay == null)return "N";
      return cPay;
   }
   /**
    * @param cPay the cPay to set
    */
   public void setcPay(String cPay) {
      this.cPay = cPay;
   }
   /**
    * @return the cSold
    */
   public String getcSold() {
      if (cSold == null)
         return "N";
      return cSold;
   }
   /**
    * @param cSold the cSold to set
    */
   public void setcSold(String cSold) {
      this.cSold = cSold;
   }
   /**
    * @return the cDate
    */
   public String getcDate() {
      return cDate;
   }
   /**
    * @param cDate the cDate to set
    */
   public void setcDate(String cDate) {
      this.cDate = cDate;
   }
   /**
    * @return the cKms
    */
   public String getcKms() {
      return cKms;
   }
   /**
    * @param cKms the cKms to set
    */
   public void setcKms(String cKms) {
      this.cKms = cKms;
   }
   /**
    * @return the cDocument
    */
   public String getcDocument() {
      return cDocument;
   }
   /**
    * @param cDocument the cDocument to set
    */
   public void setcDocument(String cDocument) {
      this.cDocument = cDocument;
   }
   /**
    * @return the cImage
    */
   public String getcImage() {
      return cImage;
   }
   /**
    * @param cImage the cImage to set
    */
   public void setcImage(String cImage) {
      this.cImage = cImage;
   }
   /**
    * @return the cReasonID
    */
   public int getcReasonID() {
      return cReasonID;
   }
   /**
    * @param cReasonID the cReasonID to set
    */
   public void setcReasonID(int cReasonID) {
      if (this.cOrder == 7){
         if (this.cReasonID == 0){
            int xxx = 0;
            xxx++;
         }
      }
      this.cReasonID = cReasonID;
   }
   /**
    * @return the cReasonCode
    */
   public String getcReasonCode() {
      return cReasonCode;
   }
   /**
    * @param cReasonCode the cReasonCode to set
    */
   public void setcReasonCode(String cReasonCode) {
      this.cReasonCode = cReasonCode;
   }
   /**
    * @return the cRemark
    */
   public String getcRemark() {
      return cRemark;
   }
   /**
    * @param cRemark the cRemark to set
    */
   public void setcRemark(String cRemark) {
      this.cRemark = cRemark;
   }
   /**
    * @return the cErrorType
    */
   public int getcErrorType() {
      return cErrorType;
   }
   /**
    * @param cErrorType the cErrorType to set
    */
   public void setcErrorType(int cErrorType) {
      this.cErrorType = cErrorType;
   }
   /**
    * @return the cErrorText
    */
   public String getcErrorText() {
      return cErrorText;
   }
   /**
    * @param cErrorText the cErrorText to set
    */
   public void setcErrorText(String cErrorText) {
      this.cErrorText = cErrorText;
   }
   /**
    * @return the cTeamId
    */
   public int getcTeamId() {
      return cTeamId;
   }
   /**
    * @param cTeamId the cTeamId to set
    */
   public void setcTeamId(int cTeamId) {
      this.cTeamId = cTeamId;
   }
   /**
    * @return the cJobRowId
    */
   public int getcJobRowId() {
      return cJobRowId;
   }
   /**
    * @param cJobRowId the cJobRowId to set
    */
   public void setcJobRowId(int cJobRowId) {
      this.cJobRowId = cJobRowId;
   }
   /**
	 * @return the tNo
	 */
	public String gettNo() {
		return tNo;
	}

	/**
	 * @param tNo the tNo to set
	 */
	public void settNo(String tNo) {
		this.tNo = tNo;
	}

	/**
	 * @return the tCode
	 */
	public String gettCode() {
		return tCode;
	}

	/**
	 * @param tCode the tCode to set
	 */
	public void settCode(String tCode) {
		this.tCode = tCode;
	}

	/**
	 * @return the tSize
	 */
	public String gettSize() {
		return tSize;
	}

	/**
	 * @param tSize the tSize to set
	 */
	public void settSize(String tSize) {
		this.tSize = tSize;
	}

	/**
	 * @return the tWeight
	 */
	public double gettWeight() {
		return tWeight;
	}

	/**
	 * @param tWeight the tWeight to set
	 */
	public void settWeight(double tWeight) {
		this.tWeight = tWeight;
	}

	/**
	 * @return the mContractNo
	 */
	public String getmContractNo() {
		return mContractNo;
	}

	/**
	 * @param mContractNo the mContractNo to set
	 */
	public void setmContractNo(String mContractNo) {
		this.mContractNo = mContractNo;
	}

	/**
	 * @return the mFixedAssetNumber
	 */
	public String getmFixedAssetNumber() {
		return mFixedAssetNumber;
	}

	/**
	 * @param mFixedAssetNumber the mFixedAssetNumber to set
	 */
	public void setmFixedAssetNumber(String mFixedAssetNumber) {
		this.mFixedAssetNumber = mFixedAssetNumber;
	}

	/**
	 * @return the mAssetName
	 */
	public String getmAssetName() {
		return mAssetName;
	}

	/**
	 * @param mAssetName the mAssetName to set
	 */
	public void setmAssetName(String mAssetName) {
		this.mAssetName = mAssetName;
	}

	/**
	 * @return the mBrand
	 */
	public String getmBrand() {
		return mBrand;
	}

	/**
	 * @param mBrand the mBrand to set
	 */
	public void setmBrand(String mBrand) {
		this.mBrand = mBrand;
	}

	/**
	 * @return the mModel
	 */
	public String getmModel() {
		return mModel;
	}

	/**
	 * @param mModel the mModel to set
	 */
	public void setmModel(String mModel) {
		this.mModel = mModel;
	}

	/**
	 * @return the mSerialNo
	 */
	public String getmSerialNo() {
		return mSerialNo;
	}

	/**
	 * @param mSerialNo the mSerialNo to set
	 */
	public void setmSerialNo(String mSerialNo) {
		this.mSerialNo = mSerialNo;
	}

	/**
	 * @return the mChassissNo
	 */
	public String getmChassissNo() {
		return mChassissNo;
	}

	/**
	 * @param mChassissNo the mChassissNo to set
	 */
	public void setmChassissNo(String mChassissNo) {
		this.mChassissNo = mChassissNo;
	}

	/**
	 * @return the mEngineNo
	 */
	public String getmEngineNo() {
		return mEngineNo;
	}

	/**
	 * @param mEngineNo the mEngineNo to set
	 */
	public void setmEngineNo(String mEngineNo) {
		this.mEngineNo = mEngineNo;
	}

	/**
	 * @return the crHire
	 */
	public String getCrHire() {
		return crHire;
	}

	/**
	 * @param crHire the crHire to set
	 */
	public void setCrHire(String crHire) {
		this.crHire = crHire;
	}

	/**
	 * @return the crOwnerName
	 */
	public String getCrOwnerName() {
		return crOwnerName;
	}

	/**
	 * @param crOwnerName the crOwnerName to set
	 */
	public void setCrOwnerName(String crOwnerName) {
		this.crOwnerName = crOwnerName;
	}

	/**
	 * @return the crLicense
	 */
	public String getCrLicense() {
		return crLicense;
	}

	/**
	 * @param crLicense the crLicense to set
	 */
	public void setCrLicense(String crLicense) {
		this.crLicense = crLicense;
	}

	/**
	 * @return the crBrand
	 */
	public String getCrBrand() {
		return crBrand;
	}

	/**
	 * @param crBrand the crBrand to set
	 */
	public void setCrBrand(String crBrand) {
		this.crBrand = crBrand;
	}

	/**
	 * @return the crRenewDate
	 */
	public String getCrRenewDate() {
		return crRenewDate;
	}

	/**
	 * @param crRenewDate the crRenewDate to set
	 */
	public void setCrRenewDate(String crRenewDate) {
		this.crRenewDate = crRenewDate;
	}

	/**
	 * @return the crContractNo
	 */
	public String getCrContractNo() {
		return crContractNo;
	}

	/**
	 * @param crContractNo the crContractNo to set
	 */
	public void setCrContractNo(String crContractNo) {
		this.crContractNo = crContractNo;
	}

	/**
	 * @return the crContractDate
	 */
	public String getCrContractDate() {
		return crContractDate;
	}

	/**
	 * @param crContractDate the crContractDate to set
	 */
	public void setCrContractDate(String crContractDate) {
		this.crContractDate = crContractDate;
	}

	/**
	 * @return the crPrincipal
	 */
	public String getCrPrincipal() {
		return crPrincipal;
	}

	/**
	 * @param crPrincipal the crPrincipal to set
	 */
	public void setCrPrincipal(String crPrincipal) {
		this.crPrincipal = crPrincipal;
	}

	/**
	 * @return the crDueNumber
	 */
	public String getCrDueNumber() {
		return crDueNumber;
	}

	/**
	 * @param crDueNumber the crDueNumber to set
	 */
	public void setCrDueNumber(String crDueNumber) {
		this.crDueNumber = crDueNumber;
	}

	/**
	 * @return the crDueAmount
	 */
	public String getCrDueAmount() {
		return crDueAmount;
	}

	/**
	 * @param crDueAmount the crDueAmount to set
	 */
	public void setCrDueAmount(String crDueAmount) {
		this.crDueAmount = crDueAmount;
	}

	/**
	 * @return the crLastPayment
	 */
	public String getCrLastPayment() {
		return crLastPayment;
	}

	/**
	 * @param crLastPayment the crLastPayment to set
	 */
	public void setCrLastPayment(String crLastPayment) {
		this.crLastPayment = crLastPayment;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		/*
		 * JSONDataUtil.put(jsonObj, COLUMN_JOB_REQUEST_ID ,jobRequestID);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_ROW_ID ,productRowID);
		JSONDataUtil.put(jsonObj, COLUMN_ITEM_NO ,itemNo);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_GROUP ,productGroup);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_TYPE ,productType);
		JSONDataUtil.put(jsonObj, COLUMN_PRODUCT_VALUE_TEXT ,productValueText);
		*/
		dest.writeInt(this.jobRequestID);
		dest.writeInt(this.productRowID);
		dest.writeString(this.itemNo);
		dest.writeString(this.productGroup);
		dest.writeString(this.productType);
		dest.writeString(this.productValueText);
		/*
		JSONDataUtil.put(jsonObj, COLUMN_C_FRANCHISE ,cFranchise);
		JSONDataUtil.put(jsonObj, COLUMN_C_INVOICE_DATE ,cInvoiceDate);
		JSONDataUtil.put(jsonObj, COLUMN_C_REGISTER_NUMBER ,cRegisterNumber);
		JSONDataUtil.put(jsonObj, COLUMN_C_MID ,cMid);
		JSONDataUtil.put(jsonObj, COLUMN_C_VIN ,cVin);
		JSONDataUtil.put(jsonObj, COLUMN_C_ENGINE ,cEngine);
		JSONDataUtil.put(jsonObj, COLUMN_C_DESCRIPTION ,cDescription);
		JSONDataUtil.put(jsonObj, COLUMN_C_COLOR ,cColor);
		JSONDataUtil.put(jsonObj, COLUMN_C_MODEL ,cModel);
		JSONDataUtil.put(jsonObj, COLUMN_C_YEAR ,cYear);
		JSONDataUtil.put(jsonObj, COLUMN_C_TYPE ,cType);
		*/
		dest.writeString(this.cFranchise);
		dest.writeString(this.cInvoiceDate);
		dest.writeString(this.cRegisterNumber);
		dest.writeString(this.cMid);
		dest.writeString(this.cVin);
		dest.writeString(this.cEngine);
		dest.writeString(this.cDescription);
		dest.writeString(this.cColor);
		dest.writeString(this.cModel);
		dest.writeString(this.cYear);
		dest.writeString(this.cType);
		
		dest.writeInt(cWareHouse);//
		dest.writeInt(cOrder);// = source.readInt();
	    dest.writeString(cSight);// TEXT DEFAULT('N');
	    dest.writeString(cEquipping);// TEXT DEFAULT('N');
	    dest.writeString(cPay);// datetime default CURRENT_TIMESTAMP;
	    dest.writeString(cSold);// datetime default CURRENT_TIMESTAMP;
	    dest.writeString(cDate);
	    dest.writeString(cKms);// TEXT;
	    dest.writeString(cDocument);
	    dest.writeString(cImage);
	    dest.writeInt(cReasonID);
	    dest.writeString(cReasonCode);
	    dest.writeString(cRemark);
	    dest.writeInt(cErrorType);
	    dest.writeString(cErrorText);
	    dest.writeInt(cTeamId);
	    dest.writeInt(cJobRowId);
		/*
		JSONDataUtil.put(jsonObj, COLUMN_T_NO ,tNo);
		JSONDataUtil.put(jsonObj, COLUMN_T_CODE ,tCode);
		JSONDataUtil.put(jsonObj, COLUMN_T_SIZE ,tSize);
		JSONDataUtil.put(jsonObj, COLUMN_T_WEIGHT ,tWeight);
		*/
		dest.writeString(this.tNo);
		dest.writeString(this.tCode);
		dest.writeString(this.tSize);
		dest.writeDouble(this.tWeight);
		/*
		JSONDataUtil.put(jsonObj, COLUMN_M_CONTRACT_NO ,mContractNo);
		JSONDataUtil.put(jsonObj, COLUMN_M_FIXED_ASSET_NO ,mFixedAssetNumber);
		JSONDataUtil.put(jsonObj, COLUMN_M_ASSET_NAME ,mAssetName);
		JSONDataUtil.put(jsonObj, COLUMN_M_BRAND ,mBrand);
		JSONDataUtil.put(jsonObj, COLUMN_M_MODEL ,mModel);
		JSONDataUtil.put(jsonObj, COLUMN_M_SERIAL_NO ,mSerialNo);
		JSONDataUtil.put(jsonObj, COLUMN_M_CHASSISSNO ,mChassissNo);
		JSONDataUtil.put(jsonObj, COLUMN_M_ENGINE_NO ,mEngineNo);
		*/
		dest.writeString(this.mContractNo);
		dest.writeString(this.mFixedAssetNumber);
		dest.writeString(this.mAssetName);
		dest.writeString(this.mBrand);
		dest.writeString(this.mModel);
		dest.writeString(this.mSerialNo);
		dest.writeString(this.mChassissNo);
		dest.writeString(this.mEngineNo);
		/*
		JSONDataUtil.put(jsonObj, COLUMN_CR_HIRE ,crHire);
		JSONDataUtil.put(jsonObj, COLUMN_CR_OWNER_NAME ,crOwnerName);
		JSONDataUtil.put(jsonObj, COLUMN_CR_LICENSE ,crLicense);
		JSONDataUtil.put(jsonObj, COLUMN_CR_BRAND ,crBrand);
		JSONDataUtil.put(jsonObj, COLUMN_CR_RENEW_DATE ,crRenewDate);
		JSONDataUtil.put(jsonObj, COLUMN_CR_CONTRACT_NO ,crContractNo);
		JSONDataUtil.put(jsonObj, COLUMN_CR_CONTRACT_DATE ,crContractDate);
		JSONDataUtil.put(jsonObj, COLUMN_CR_PRINCIPAL ,crPrincipal);
		JSONDataUtil.put(jsonObj, COLUMN_CR_DUE_NUMBER ,crDueNumber);
		JSONDataUtil.put(jsonObj, COLUMN_CR_DUE_AMOUNT ,crDueAmount);
		JSONDataUtil.put(jsonObj, COLUMN_CR_LAST_PAYMENT ,crLastPayment);
		 */
		dest.writeString(this.crHire);
		dest.writeString(this.crOwnerName);
		dest.writeString(this.crLicense);
		dest.writeString(this.crBrand);
		dest.writeString(this.crRenewDate);
		dest.writeString(this.crContractNo);
		dest.writeString(this.crContractDate);
		dest.writeString(this.crPrincipal);
		dest.writeString(this.crDueNumber);
		dest.writeString(this.crDueAmount);
		dest.writeString(this.crLastPayment);
		
		dest.writeInt(this.photoSetID);
		
		dest.writeInt(this.inspectDataObjectID);
		
		dest.writeString(this.remark);
		
		dest.writeInt(this.productId);
		
		dest.writeDouble(this.productPrice);
		dest.writeDouble(this.productQty);
		dest.writeDouble(this.productValue);
		
		  
        dest.writeString(char1);//  = source.readString();
        dest.writeString(char2);// = source.readString();// :String
        dest.writeString(char3);// = source.readString();// :String
        dest.writeString(char4);// = source.readString();// :String
        dest.writeString(char5);// = source.readString();// :String
        dest.writeInt(num1);// = source.readInt();// :int
        dest.writeInt(num2);// = source.readInt();// :int
        dest.writeInt(num3);// = source.readInt();// :int
        dest.writeInt(num4);// = source.readInt();// :int
        dest.writeInt(num5);// = source.readInt();// :int
        dest.writeDouble(dec1);// = source.readDouble();// : decimal
        dest.writeDouble(dec2);// = source.readDouble();//: decimal
        dest.writeDouble(dec3);// = source.readDouble();//: decimal
        dest.writeString(date1);// = source.readString();// :String
        dest.writeString(date2);// = source.readString();// :String
        dest.writeString(date3);// = source.readString();// :String
        dest.writeString(productName);// = source.readString();
        
        dest.writeInt(this.productRowID);
        
        dest.writeBooleanArray(new boolean[]{hasCheckList});

	}
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public int getResultProductRowId() {
		return resultProductRowId;
	}
	public void setResultProductRowId(int resultProductRowId) {
		this.resultProductRowId = resultProductRowId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
	    if (this.productRowID == 1){
	       Log.d("DEBUG_XXXX", "Product id -> "+productId);
	    }
		this.productId = productId;
	}
	public double getProductQty() {
		return productQty;
	}
	public void setProductQty(double productQty) {
		this.productQty = productQty;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductUnit() {
		return productUnit;
	}
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	public double getProductValue() {
		return productValue;
	}
	public void setProductValue(double productValue) {
		this.productValue = productValue;
	}
	public String getFoundFlag() {
		return foundFlag;
	}
	public void setFoundFlag(String foundFlag) {
		this.foundFlag = foundFlag;
	}
	public String getProductInfo() {
		return productInfo;
	}
	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}
	public String getClusterControlFlag() {
		return clusterControlFlag;
	}
	public void setClusterControlFlag(String clusterControlFlag) {
		this.clusterControlFlag = clusterControlFlag;
	}
	/**
	 * @return the jobRowId
	 */
	public int getJobRowId() {
		return jobRowId;
	}
	/**
	 * @param jobRowId the jobRowId to set
	 */
	public void setJobRowId(int jobRowId) {
		this.jobRowId = jobRowId;
	}
	/**
	 * @return the jobLocationId
	 */
	public int getJobLocationId() {
		return jobLocationId;
	}
	/**
	 * @param jobLocationId the jobLocationId to set
	 */
	public void setJobLocationId(int jobLocationId) {
		this.jobLocationId = jobLocationId;
	}
   /**
    * @return the photoSetID
    */
   public int getPhotoSetID() {
      return photoSetID;
   }
   /**
    * @param photoSetID the photoSetID to set
    */
   public void setPhotoSetID(int photoSetID) {
      this.photoSetID = photoSetID;
   }
   /**
    * @return the isBackup
    */
   public boolean isBackup() {
      return isBackup;
   }
   /**
    * @param isBackup the isBackup to set
    */
   public void setBackup(boolean isBackup) {
      this.isBackup = isBackup;
   }
   /**
    * @return the inspectDataObjectID
    */
   public int getInspectDataObjectID() {
      return inspectDataObjectID;
   }
   /**
    * @param inspectDataObjectID the inspectDataObjectID to set
    */
   public void setInspectDataObjectID(int inspectDataObjectID) {
      if (this.getcOrder() == 1){
         if (inspectDataObjectID == -1){
            int xxx = 0;
            xxx++;            
         }
      }
      this.inspectDataObjectID = inspectDataObjectID;
   }
   /**
    * @return the marketPrice
    */
   public double getMarketPrice() {
      return marketPrice;
   }
   /**
    * @param marketPrice the marketPrice to set
    */
   public void setMarketPrice(double marketPrice) {
      this.marketPrice = marketPrice;
   }
   /**
    * @return the marketPriceID
    */
   public int getMarketPriceID() {
      return marketPriceID;
   }
   /**
    * @param marketPriceID the marketPriceID to set
    */
   public void setMarketPriceID(int marketPriceID) {
      this.marketPriceID = marketPriceID;
   }
   /**
    * @return the remark
    */
   public String getRemark() {
      /*
      remark = this.getcRemark();
      */
      if ((remark == null)||(remark.trim().equalsIgnoreCase("null")))
            return "";
      
      return remark;
   }
   /**
    * @param remark the remark to set
    */
   public void setRemark(String remark) {
      
      if (this.productRowID == 1){
         int xx = 0;
         xx++;
      }
      this.remark = remark;
//      this.cRemark = remark;
//      this.setcRemark(remark);
   }
   /**
    * @return the isAudit
    */
   public boolean isAudit() {
      return isAudit;
   }
   /**
    * @param isAudit the isAudit to set
    */
   public void setAudit(boolean isAudit) {
      this.isAudit = isAudit;
   }
   /**
    * @return the char1
    */
   public String getChar1() {
      return char1;
   }
   /**
    * @param char1 the char1 to set
    */
   public void setChar1(String char1) {
      this.char1 = char1;
   }
   /**
    * @return the char2
    */
   public String getChar2() {
      return char2;
   }
   /**
    * @param char2 the char2 to set
    */
   public void setChar2(String char2) {
      this.char2 = char2;
   }
   /**
    * @return the char3
    */
   public String getChar3() {
      return char3;
   }
   /**
    * @param char3 the char3 to set
    */
   public void setChar3(String char3) {
      this.char3 = char3;
   }
   /**
    * @return the char4
    */
   public String getChar4() {
      return char4;
   }
   /**
    * @param char4 the char4 to set
    */
   public void setChar4(String char4) {
      this.char4 = char4;
   }
   /**
    * @return the char5
    */
   public String getChar5() {
      return char5;
   }
   /**
    * @param char5 the char5 to set
    */
   public void setChar5(String char5) {
      this.char5 = char5;
   }
   /**
    * @return the num1
    */
   public int getNum1() {
      return num1;
   }
   /**
    * @param num1 the num1 to set
    */
   public void setNum1(int num1) {
      this.num1 = num1;
   }
   /**
    * @return the num2
    */
   public int getNum2() {
      return num2;
   }
   /**
    * @param num2 the num2 to set
    */
   public void setNum2(int num2) {
      this.num2 = num2;
   }
   /**
    * @return the num3
    */
   public int getNum3() {
      return num3;
   }
   /**
    * @param num3 the num3 to set
    */
   public void setNum3(int num3) {
      this.num3 = num3;
   }
   /**
    * @return the num4
    */
   public int getNum4() {
      return num4;
   }
   /**
    * @param num4 the num4 to set
    */
   public void setNum4(int num4) {
      this.num4 = num4;
   }
   /**
    * @return the num5
    */
   public int getNum5() {
      return num5;
   }
   /**
    * @param num5 the num5 to set
    */
   public void setNum5(int num5) {
      this.num5 = num5;
   }
   /**
    * @return the dec1
    */
   public double getDec1() {
      return dec1;
   }
   /**
    * @param dec1 the dec1 to set
    */
   public void setDec1(double dec1) {
      this.dec1 = dec1;
   }
   /**
    * @return the dec2
    */
   public double getDec2() {
      return dec2;
   }
   /**
    * @param dec2 the dec2 to set
    */
   public void setDec2(double dec2) {
      this.dec2 = dec2;
   }
   /**
    * @return the dec3
    */
   public double getDec3() {
      return dec3;
   }
   /**
    * @param dec3 the dec3 to set
    */
   public void setDec3(double dec3) {
      this.dec3 = dec3;
   }
   /**
    * @return the date1
    */
   public String getDate1() {
      return date1;
   }
   /**
    * @param date1 the date1 to set
    */
   public void setDate1(String date1) {
      this.date1 = date1;
   }
   /**
    * @return the date2
    */
   public String getDate2() {
      return date2;
   }
   /**
    * @param date2 the date2 to set
    */
   public void setDate2(String date2) {
      this.date2 = date2;
   }
   /**
    * @return the date3
    */
   public String getDate3() {
      return date3;
   }
   /**
    * @param date3 the date3 to set
    */
   public void setDate3(String date3) {
      this.date3 = date3;
   }
   /**
    * @return the productName
    */
   public String getProductName() {
      return productName;
   }
   /**
    * @param productName the productName to set
    */
   public void setProductName(String productName) {
      this.productName = productName;
   }
   public boolean isHasCheckList() {
      return hasCheckList;
   }
   public void setHasCheckList(boolean hasCheckList) {
      this.hasCheckList = hasCheckList;
   }
}

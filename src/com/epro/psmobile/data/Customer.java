package com.epro.psmobile.data;

import java.util.ArrayList;

import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements DbCursorHolder ,Parcelable , JSONDataHolder
{
	
	public final static String CUSTOMER_ID = "customerID";
	public final static String CUSTOMER_CODE = "customerCode";
	public final static String CUSTOMER_NAME = "customerName";
	public final static String CUSTOMER_ADDRESS = "customerAddress";
	public final static String CUSTOMER_TELS = "customerTels";
	public final static String CUSTOMER_FAXS = "customerFaxs";
	public final static String CUSTOMER_EMAILS = "customerEmails";
	public final static String CUSTOMER_OFFICE_LOC_LAT = "officeLocLat";
	public final static String CUSTOMER_OFFICE_LOC_LON = "officeLocLon";
	public final static String CUSTOMER_OFFICE_MAP_IMG = "officeMapImg";
	public final static String CUSTOMER_AREA_ID = "areaID";
	
	
	private int customerID;
	private String customerCode;
	private String customerName;
	private String customerAddress;
	private String customerTels;
	private String customerFaxs;
	private String customerEmails;
	private float officeLocLat;
	private float officeLocLon;
	private Bitmap officeMapImg;
	private int areaID;
	private ArrayList<CustomerSurveySite> customerSiteList = new ArrayList<CustomerSurveySite>();
	
	public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>()
	{

		@Override
		public Customer createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Customer(source);
		}

		@Override
		public Customer[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Customer[size];
		}
		
	};
	
	public Customer(Parcel source)
	{
		/*
		 dest.writeInt(this.getCustomerID());
		dest.writeString(this.getCustomerCode());
		dest.writeString(this.getCustomerName());
		dest.writeString(this.getCustomerAddress());
		dest.writeString(this.getCustomerTels());
		dest.writeString(this.getCustomerFaxs());
		dest.writeString(this.getCustomerEmails());
		dest.writeFloat(this.getOfficeLocLat());
		dest.writeFloat(this.getOfficeLocLon());
		
		//dest bitmap
		
		dest.writeInt(this.getAreaID());
		dest.writeList(getCustomerSiteList());
		 */
		this.setCustomerID(source.readInt());
		this.setCustomerCode(source.readString());
		this.setCustomerName(source.readString());
		this.setCustomerAddress(source.readString());
		this.setCustomerTels(source.readString());
		this.setCustomerFaxs(source.readString());
		this.setCustomerEmails(source.readString());
		this.setOfficeLocLat(source.readFloat());
		this.setOfficeLocLon(source.readFloat());
		
		this.setAreaID(source.readInt());
		this.customerSiteList = source.readArrayList(Customer.class.getClassLoader());
	}
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerTels() {
		return customerTels;
	}
	public void setCustomerTels(String customerTels) {
		this.customerTels = customerTels;
	}
	public String getCustomerFaxs() {
		return customerFaxs;
	}
	public void setCustomerFaxs(String customerFaxs) {
		this.customerFaxs = customerFaxs;
	}
	public String getCustomerEmails() {
		return customerEmails;
	}
	public void setCustomerEmails(String customerEmails) {
		this.customerEmails = customerEmails;
	}
	public float getOfficeLocLat() {
		return officeLocLat;
	}
	public void setOfficeLocLat(float officeLocLat) {
		this.officeLocLat = officeLocLat;
	}
	public float getOfficeLocLon() {
		return officeLocLon;
	}
	public void setOfficeLocLon(float officeLocLon) {
		this.officeLocLon = officeLocLon;
	}
	public Bitmap getOfficeMapImg() {
		return officeMapImg;
	}
	public void setOfficeMapImg(Bitmap officeMapImg) {
		this.officeMapImg = officeMapImg;
	}
	public int getAreaID() {
		return areaID;
	}
	public void setAreaID(int areaID) {
		this.areaID = areaID;
	}
	public ArrayList<CustomerSurveySite> getCustomerSiteList() {
		return customerSiteList;
	}
	public void addCustomerSiteList(CustomerSurveySite customerSite) {
		this.customerSiteList.add(customerSite);
	}
	public void addCustomerSiteListAll(ArrayList<CustomerSurveySite> customerSiteList)
	{
		this.customerSiteList.addAll(customerSiteList);
	}
	@Override
	public void onBind(Cursor cur) {
		// TODO Auto-generated method stub
		this.setCustomerID(cur.getInt(cur.getColumnIndex(CUSTOMER_ID)));
		this.setCustomerCode(cur.getString(cur.getColumnIndex(CUSTOMER_CODE)));
		this.setCustomerName(cur.getString(cur.getColumnIndex(CUSTOMER_NAME)));
		this.setCustomerAddress(cur.getString(cur.getColumnIndex(CUSTOMER_ADDRESS)));
		this.setCustomerTels(cur.getString(cur.getColumnIndex(CUSTOMER_TELS)));
		this.setCustomerFaxs(cur.getString(cur.getColumnIndex(CUSTOMER_FAXS)));
		this.setCustomerEmails(cur.getString(cur.getColumnIndex(CUSTOMER_EMAILS)));
		this.setOfficeLocLat(cur.getFloat(cur.getColumnIndex(CUSTOMER_OFFICE_LOC_LAT)));
		this.setOfficeLocLon(cur.getFloat(cur.getColumnIndex(CUSTOMER_OFFICE_LOC_LON)));
		//bmp
		this.setAreaID(cur.getInt(cur.getColumnIndex(CUSTOMER_AREA_ID)));
		
		
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
		 private int customerID;
	private String customerCode;
	private String customerName;
	private String customerAddress;
	private String customerTels;
	private String customerFaxs;
	private String customerEmails;
	private float officeLocLat;
	private float officeLocLon;
	private Bitmap officeMapImg;
	private int areaID;
	private ArrayList<CustomerSurveySite> customerSiteList = new ArrayList<CustomerSurveySite>(); 
		 
		 */
		dest.writeInt(this.getCustomerID());
		dest.writeString(this.getCustomerCode());
		dest.writeString(this.getCustomerName());
		dest.writeString(this.getCustomerAddress());
		dest.writeString(this.getCustomerTels());
		dest.writeString(this.getCustomerFaxs());
		dest.writeString(this.getCustomerEmails());
		dest.writeFloat(this.getOfficeLocLat());
		dest.writeFloat(this.getOfficeLocLon());
		
		//dest bitmap
		
		dest.writeInt(this.getAreaID());
		dest.writeList(getCustomerSiteList());
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}
	

}

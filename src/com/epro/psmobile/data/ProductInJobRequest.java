package com.epro.psmobile.data;

import android.database.Cursor;

public class ProductInJobRequest implements DbCursorHolder
{

	public final static String COLUMN_JOB_REQUEST_ID = "jobRequestID";
	public final static String COLUMN_AMOUNT = "amount";
	
	private int jobRequestID;
	private Product product;
	private int amount;
	private ProductAmountUnit unit;
	public ProductInJobRequest() {
		// TODO Auto-generated constructor stub
	}
	public int getJobRequestID() {
		return jobRequestID;
	}
	public void setJobRequestID(int jobRequestID) {
		this.jobRequestID = jobRequestID;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public ProductAmountUnit getUnit() {
		return unit;
	}
	public void setUnit(ProductAmountUnit unit) {
		this.unit = unit;
	}
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub
		this.jobRequestID = cursor.getInt(cursor.getColumnIndex(COLUMN_JOB_REQUEST_ID));
		this.product = new Product();
		this.product.onBind(cursor);
		this.amount = cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNT));
		this.unit = new ProductAmountUnit();
		this.unit.onBind(cursor);
	}

}

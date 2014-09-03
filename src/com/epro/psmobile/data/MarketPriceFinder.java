package com.epro.psmobile.data;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class MarketPriceFinder extends ArrayList<MarketPrice> {

	public MarketPriceFinder() {
		// TODO Auto-generated constructor stub
	}

	public MarketPriceFinder(int capacity) {
		super(capacity);
		// TODO Auto-generated constructor stub
	}

	public MarketPriceFinder(Collection<? extends MarketPrice> collection) {
		super(collection);
		// TODO Auto-generated constructor stub
	}

	public MarketPrice find(
			int productGroupID,
			int productID){
		for(int i = 0; i < size();i++)
		{
			MarketPrice marketPrice = this.get(i);
			if ((marketPrice.getProductGroupID() == productGroupID)&&
				(marketPrice.getProductID() == productID)){
				return marketPrice;
			}
		}
		return null;
	}
	
}

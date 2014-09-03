package com.epro.psmobile.data.choice;

public class MoreInfo extends DeQuestData {
	private int moreInfoID = INIT_INT;
	private MoreInfoType moreInfoType = MoreInfoType.TEXT;
	private String moreInfoData = INIT_STR;
	
	public MoreInfo(int moreInfoID,MoreInfoType moreInfoType,String moreInfoData){
		this.setMoreInfoID(moreInfoID);
		this.setMoreInfoType(moreInfoType);
		this.setMoreInfoData(moreInfoData);
	}

	/**
	 * @return the moreInfoID
	 */
	public int getMoreInfoID() {
		return moreInfoID;
	}

	/**
	 * @param moreInfoID the moreInfoID to set
	 */
	public void setMoreInfoID(int moreInfoID) {
		this.moreInfoID = moreInfoID;
	}

	/**
	 * @return the moreInfoType
	 */
	public MoreInfoType getMoreInfoType() {
		return moreInfoType;
	}

	/**
	 * @param moreInfoType the moreInfoType to set
	 */
	public void setMoreInfoType(MoreInfoType moreInfoType) {
		this.moreInfoType = moreInfoType;
	}

	/**
	 * @return the moreInfoData
	 */
	public String getMoreInfoData() {
		return moreInfoData;
	}

	/**
	 * @param moreInfoData the moreInfoData to set
	 */
	public void setMoreInfoData(String moreInfoData) {
		this.moreInfoData = moreInfoData;
	}
	
}

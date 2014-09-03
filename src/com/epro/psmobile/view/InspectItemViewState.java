package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectPhotoSaved;
import com.epro.psmobile.data.InspectDataObjectSaved;

public class InspectItemViewState implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 176060793797315856L;
	public int objectId = -1;
	public int imgWidth;
	public int imgHeight;
	public int fullWidthView;
	public int fullHeightView;
	public boolean viewRendered;
	public InspectDataItem inspectDataItem;
	public boolean bPressed;
	
	public InspectDataObjectSaved inspectDataObjectSaved;
	public ArrayList<InspectDataObjectPhotoSaved> inspectDataObjectPhotoSavedList;
	
	
	public InspectItemViewState(InspectDataObjectSaved dataSaved)
	{
		this.inspectDataObjectSaved = dataSaved;
		
		inspectDataObjectPhotoSavedList = new ArrayList<InspectDataObjectPhotoSaved>();

	}
	public InspectItemViewState() {
		// TODO Auto-generated constructor stub
		inspectDataObjectSaved = new InspectDataObjectSaved();
		inspectDataObjectPhotoSavedList = new ArrayList<InspectDataObjectPhotoSaved>();
	}

	
	public InspectDataObjectSaved getInspectDataObjectSaved(){
	    inspectDataObjectSaved.setInspectDataObjectID(this.objectId);//added
		
	    return inspectDataObjectSaved ;
	}
	
	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
		getInspectDataObjectSaved().setInspectDataObjectID(objectId);
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		if (imgWidth == 0)
		{
			int xxx = 0;
			xxx++;
		}
		this.imgWidth = imgWidth;
		//getInspectDataObjectSaved().setInspectDataItemWidth(imgWidth);
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
//		getInspectDataObjectSaved().setInspectDataItemLong(imgHeight);//in real perspective , 
	}

	public int getFullWidthView() {
		return fullWidthView;
	}

	public void setFullWidthView(int fullWidthView) {
		this.fullWidthView = fullWidthView;
	}

	public int getFullHeightView() {
		return fullHeightView;
	}

	public void setFullHeightView(int fullHeightView) {
		this.fullHeightView = fullHeightView;
	}

	public boolean isViewRendered() {
		return viewRendered;
	}

	public void setViewRendered(boolean viewRendered) {
		this.viewRendered = viewRendered;
	}

	public InspectDataItem getInspectDataItem() {
		return inspectDataItem;
	}

	public void setInspectDataItem(InspectDataItem inspectDataItem) {
		this.inspectDataItem = inspectDataItem;		
		getInspectDataObjectSaved().setInspectDataItemID(inspectDataItem.getInspectDataItemID());
	}

	public boolean isPressed() {
		return bPressed;
	}

	public void setPressed(boolean bPressed) {
		this.bPressed = bPressed;
	}
}

package com.epro.psmobile.action;

import com.epro.psmobile.data.InspectDataGroupType;
import com.epro.psmobile.data.InspectDataItem;

public interface OnClickInspectDataItem {

	void onInspectItemClicked(InspectDataGroupType currentGroupType,InspectDataItem currentItem);

}

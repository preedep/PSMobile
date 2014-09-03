package com.epro.psmobile.action;

import android.view.View;

import com.epro.psmobile.view.InspectItemViewState;

public interface OnClickInspectDrawingView {
	void onClickInspectItemDrawing(View viewInspect,InspectItemViewState dataItem);
	void onClickLongInspectItemDrawing(View viewInspect,InspectItemViewState dataItem);
	void onClickCopyInspectItemDrawing(InspectItemViewState dataItem);
}

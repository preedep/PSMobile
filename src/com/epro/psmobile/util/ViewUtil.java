package com.epro.psmobile.util;

import com.epro.psmobile.adapter.ExpenseEntryRowAdapter;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewUtil {

	public ViewUtil() {
		// TODO Auto-generated constructor stub
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    //ExpenseEntryRowAdapter listAdapter = (ExpenseEntryRowAdapter)listView.getAdapter();
		 ListAdapter listAdapter = listView.getAdapter();
		   
		if (listAdapter == null) {
	        return;
	    }
	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	    	 view = listAdapter.getView(i, view, listView);
	    	 /*
	    	 if (i == 0) {
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
	         }	    	
	        */	    	
	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);	    	
	        totalHeight += view.getMeasuredHeight();
	        
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1 ));	    
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	public  static void softkeyHide(Context context,View view){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}

package com.epro.psmobile.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {

	public KeyboardUtil() {
		// TODO Auto-generated constructor stub
	}
	public static void hideKeyboard(Activity context) {
	    // TODO Auto-generated method stub
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	    if(imm.isAcceptingText())// verifier si le soft keyboard est ouvert ou non                      
	    imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
	 }
    public static void showKeyboard(Context context,View view)
    {
    	InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.showSoftInputFromInputMethod(
    			((view == null)?null:view.getWindowToken()),0
    					);
    	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    public static void hideKeyboard(Context context,View view)
    {
    	InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow((view == null)?null:view.getWindowToken(), 0);
    }
}

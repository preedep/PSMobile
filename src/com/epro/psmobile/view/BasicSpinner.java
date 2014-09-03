package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.data.ReasonSentence.ReasonType;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public abstract class BasicSpinner extends Spinner {

	public BasicSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BasicSpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public BasicSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BasicSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BasicSpinner(Context context, AttributeSet attrs, int defStyle,
			int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	public abstract void initial() throws Exception;
	public abstract <T> ArrayList<T> getData(Class<T> type);
}

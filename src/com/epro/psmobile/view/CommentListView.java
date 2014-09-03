/**
 * 
 */
package com.epro.psmobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author preedeeponchevin
 *
 */
public class CommentListView extends ListView {

	/**
	 * @param context
	 */
	public CommentListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CommentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CommentListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
	    super.onSizeChanged(xNew, yNew, xOld, yOld);
	}
}

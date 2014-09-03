/**
 * 
 */
package com.epro.psmobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author preedeeponchevin
 *
 */
public class VScroll extends ScrollView {

	private float xDistance, yDistance, lastX, lastY;
/*
	private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;
*/
	/**
	 * @param context
	 */
	public VScroll(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//mGestureDetector = new GestureDetector(context, new YScrollDetector());
       // setFadingEdgeLength(0);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public VScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//mGestureDetector = new GestureDetector(context, new YScrollDetector());
        //setFadingEdgeLength(0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public VScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		//mGestureDetector = new GestureDetector(context, new YScrollDetector());
        //setFadingEdgeLength(0);
	}

	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

	/*
	 @Override
	 public boolean onInterceptTouchEvent(MotionEvent ev) {
		 switch (ev.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            xDistance = yDistance = 0f;
	            lastX = ev.getX();
	            lastY = ev.getY();
	            break;
	        case MotionEvent.ACTION_MOVE:
	            final float curX = ev.getX();
	            final float curY = ev.getY();
	            xDistance += Math.abs(curX - lastX);
	            yDistance += Math.abs(curY - lastY);
	            lastX = curX;
	            lastY = curY;
	            if(xDistance > yDistance)
	                return false;
	    }

	    return super.onInterceptTouchEvent(ev);
	 }*/

	    // Return false if we're scrolling in the x direction  
	    class YScrollDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	            if(Math.abs(distanceY) > Math.abs(distanceX)) {
	                return true;
	            }
	            return false;
	        }
	    }
}

/**
 * 
 */
package com.epro.psmobile.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.widget.RelativeLayout;

/**
 * @author nickmsft
 *
 */
public class InspectRelativeLayout extends RelativeLayout {

	float mScaleFactor = DEFAULT_FACTOR;
	float mPivotX;
	float mPivotY;
	
	public static float DEFAULT_FACTOR = 1f;
	private Bitmap bmpBackground;
	/**
	 * @param context
	 */
	public InspectRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		 setDrawingCacheEnabled(true);
		 setWillNotDraw(false);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public InspectRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		 setDrawingCacheEnabled(true);
		 setWillNotDraw(false);

	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public InspectRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		 setDrawingCacheEnabled(true);
		 setWillNotDraw(false);

	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//if (bmpBackground != null)
		//{
		//	canvas.drawBitmap(bmpBackground, 0, 0, new Paint());
		//}
		super.onDraw(canvas);
	}

	/* (non-Javadoc)
	 * @see android.widget.RelativeLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		  canvas.save(Canvas.MATRIX_SAVE_FLAG);
		  canvas.scale(mScaleFactor, mScaleFactor, mPivotX, mPivotY);
		  super.dispatchDraw(canvas);
		  canvas.restore();
	}
	public void scale(float scaleFactor, float pivotX, float pivotY) {
	    mScaleFactor = scaleFactor;
	    mPivotX = pivotX;
	    mPivotY = pivotY;
	    this.invalidate();
	}

	public void restore() {
	    mScaleFactor = 1;
	    this.invalidate();
	}

	public Bitmap getBmpBackground() {
		return bmpBackground;
	}

	public void setBmpBackground(Bitmap bmpBackground) {
		this.bmpBackground = bmpBackground;
	}
	
	public float getCurrentScaleFactor(){
	   return mScaleFactor;
	}
	public void setScaleFactor(float scaleFactor){
	   this.mScaleFactor = scaleFactor;
	}
	
	
}

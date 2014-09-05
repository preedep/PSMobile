package com.epro.psmobile.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {

   public NonSwipeableViewPager(Context context) {
      super(context);
      // TODO Auto-generated constructor stub
   }

   public NonSwipeableViewPager(Context context, AttributeSet attrs) {
      super(context, attrs);
      // TODO Auto-generated constructor stub
   }
   @Override
   public boolean onInterceptTouchEvent(MotionEvent ev) {
      /*
       boolean ret = super.onInterceptTouchEvent(ev);
       if (ret)
           getParent().requestDisallowInterceptTouchEvent(true);
       return ret;*/
      return false;
   }

   @Override
   public boolean onTouchEvent(MotionEvent ev) {
      /*
       boolean ret = super.onTouchEvent(ev);
       if (ret)
           getParent().requestDisallowInterceptTouchEvent(true);
       return ret;*/
      return false;
   }
}

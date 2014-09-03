package com.epro.psmobile.adapter.filter;

import android.widget.Filter;

public abstract class BaseFilterable extends Filter{

   private int colIdx;
   public BaseFilterable() {
      // TODO Auto-generated constructor stub
   }
   public int getColIdx() {
      return colIdx;
   }
   public void setColIdx(int colIdx) {
      this.colIdx = colIdx;
   }

}

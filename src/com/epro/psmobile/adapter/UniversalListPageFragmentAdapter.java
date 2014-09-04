package com.epro.psmobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class UniversalListPageFragmentAdapter extends FragmentStatePagerAdapter {

   private int maxPage;
   private int rowOffset;
   public UniversalListPageFragmentAdapter(FragmentManager fm,
         int maxPage,
         int rowOffset) {
      super(fm);
      // TODO Auto-generated constructor stub
      this.maxPage = maxPage;
      this.rowOffset = rowOffset;
   }

   @Override
   public Fragment getItem(int position) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public int getCount() {
      // TODO Auto-generated method stub
      return maxPage;
   }

}

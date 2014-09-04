package com.epro.psmobile.adapter;

import java.util.ArrayList;

import com.epro.psmobile.fragment.InspectReportListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class UniversalListPageFragmentAdapter extends FragmentStatePagerAdapter {

   //private int rowOffset;
   private ArrayList<Fragment> fragments;
   public UniversalListPageFragmentAdapter(FragmentManager fm,
         ArrayList<Fragment> fragments) {
      super(fm);
      // TODO Auto-generated constructor stub
      this.fragments = fragments;
     }

   @Override
   public Fragment getItem(int position) {
      // TODO Auto-generated method stub
      return fragments.get(position);
   }

   @Override
   public int getCount() {
      // TODO Auto-generated method stub
      return fragments.size();
   }
   
   public void append(Fragment f){
      this.fragments.add(f);
   }

}

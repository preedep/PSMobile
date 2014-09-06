package com.epro.psmobile.adapter;

import java.util.ArrayList;

import com.epro.psmobile.fragment.InspectReportListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class UniversalListPageFragmentAdapter extends FragmentStatePagerAdapter {

   SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

   /* (non-Javadoc)
    * @see android.support.v4.app.FragmentStatePagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
    */
   @Override
   public void destroyItem(ViewGroup container, int position, Object object) {
      // TODO Auto-generated method stub
      //super.destroyItem(container, position, object);
   }

   //private int rowOffset;
   private ArrayList<Fragment> fragments;
   private FragmentManager fm;
   public UniversalListPageFragmentAdapter(FragmentManager fm,
         ArrayList<Fragment> fragments) {
      super(fm);
      this.fm = fm;
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
   public void clearAll(){
      if (this.fragments != null)
      {
         for(int i=0; i<fragments.size(); i++)
            fm.beginTransaction().remove(fragments.get(i)).commit();
         
         this.fragments.clear();
      }
   }
   public void addAll(ArrayList<Fragment> fragments){
      if (fragments == null){
         fragments = new ArrayList<Fragment>();
      }
      this.fragments.addAll(fragments);
   }

   /* (non-Javadoc)
    * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
    */
   @Override
   public int getItemPosition(Object object) {
      // TODO Auto-generated method stub
      return POSITION_NONE;//super.getItemPosition(object);
   }
   
   @Override
   public Object instantiateItem(ViewGroup container, int position) {
       Fragment fragment = (Fragment) super.instantiateItem(container, position);
       registeredFragments.put(position, fragment);
       return fragment;
   }
   
   public Fragment getRegisteredFragment(int position) {
      if (registeredFragments.size() > position)
         return registeredFragments.get(position);
      else
         return null;
  }

}

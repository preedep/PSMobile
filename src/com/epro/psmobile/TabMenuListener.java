package com.epro.psmobile;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBODataAdapter.WhereInStatus;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.dialog.ReportTypeSelectDialog;
import com.epro.psmobile.fragment.DashboardCalendarFragment;
import com.epro.psmobile.fragment.CommonListMenuFragment;
import com.epro.psmobile.fragment.CommonListMenuFragment.MenuGroupType;
import com.epro.psmobile.fragment.ReportFragment;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.InspectServiceSupportUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SharedPreferenceUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class TabMenuListener<T extends android.support.v4.app.Fragment> implements TabListener {
	
	private final SherlockFragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    private Fragment mFragment;
    private Bundle argus;

    private Team team = null;
    
    public TabMenuListener(SherlockFragmentActivity activity, 
    		String tag, Class<T> clz,Bundle agruments) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
        argus = agruments;

        PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(activity);
        //Team team = null;
        try {
            team = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(activity));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
           e1.printStackTrace();
        }
        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
        if (mFragment != null && !mFragment.isDetached()) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.detach(mFragment);
            //ft.remove(mFragment);
            ft.commit();
        }
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		int idxTabSelected = tab.getPosition();
		if ((idxTabSelected == 2)||(idxTabSelected == 5))
		{
			onTabSelected(tab,ft);
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		if (SharedPreferenceUtil.getLayoutModified(mActivity)){
			MessageBox.showMessage(mActivity, 
					R.string.text_error_title, 
					R.string.text_error_layout_not_saved_yet);
			return;
		}
		if (!SharedPreferenceUtil.isAlreadyCommentSaved(mActivity))
        {
		   if (team != null){
		      if (team.isQCTeam()){
		           MessageBox.showMessage(mActivity, 
		                 R.string.text_error_title, 
		                 R.string.text_error_comment_qc_no13_not_saved_yet);
		      }else
		      {
		           MessageBox.showMessage(mActivity, 
		                 R.string.text_error_title, 
		                 R.string.text_error_comment_no13_not_saved_yet);
		      }
		   }else{
		      
		      MessageBox.showMessage(mActivity, 
                 R.string.text_error_title, 
                 R.string.text_error_comment_no13_not_saved_yet);
		   }
         return;
        }
		if (SharedPreferenceUtil.getCarInspectDataModified(mActivity)){
           MessageBox.showMessage(mActivity, 
                   R.string.text_error_title, 
                   R.string.text_error_car_inspect_not_saved_yet);
           return;
       }
		// TODO Auto-generated method stub
		 mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
		 int iStackCount = mActivity.getSupportFragmentManager().getBackStackEntryCount();
         int idxTabSelected = tab.getPosition();
			
		 //if (mFragment == null) 
		 //{
			 for(int i = 0; i < iStackCount;i++){
				 mActivity.getSupportFragmentManager().popBackStack();        		  
       	  	 }
             if (idxTabSelected == 0)
             {
            	 mFragment = DashboardCalendarFragment.newInstance(WhereInStatus.NONE);            		 
             }  
             else if (idxTabSelected == 1)
             {
            	 boolean needShowInList = SharedPreferenceUtil.checkJobAndTaskShownInListView(mActivity);
            	 if (!needShowInList)
            	 {
            	   mFragment = DashboardCalendarFragment.newInstance(WhereInStatus.DO_PLAN_TASK);
            			 //AppStateUtil.setJobAndTaskInListView(mActivity, true);
            	 }else{
                   mFragment = Fragment.instantiate(mActivity, mClass.getName(), argus);
            			 //AppStateUtil.setJobAndTaskInListView(mActivity, false);
            	 }
             }else if (idxTabSelected == 2)
             {
            	 boolean needShowInList = SharedPreferenceUtil.checkJobAndTaskShownInListView(mActivity);
            	 if (!needShowInList)
            	 {
            		mFragment = DashboardCalendarFragment.newInstance(WhereInStatus.TO_DO_TASK);
            			 //AppStateUtil.setJobAndTaskInListView(mActivity, true);
            	 }else{
                	mFragment = Fragment.instantiate(mActivity, mClass.getName(), argus);
            			 //AppStateUtil.setJobAndTaskInListView(mActivity, false);
            	 }            		 
             }else if (idxTabSelected == 5)
             {
            	 /*
            	  * show report menu
            	  */
            	 ReportTypeSelectDialog reportTypeDlg = new ReportTypeSelectDialog();
            	 reportTypeDlg.show(mActivity.getSupportFragmentManager(), ReportTypeSelectDialog.class.getName());
            	 
            	 mFragment = new ReportFragment();
             }else{
            	 mFragment = Fragment.instantiate(mActivity, mClass.getName(), argus);            	 
             }
             if (mFragment != null)
             {
            	 ft.replace(R.id.content_frag, mFragment, mTag);
            	 
         	      if (
         	    	  (idxTabSelected == 0)||
         	    	  (idxTabSelected == 1)||
         	    	  (idxTabSelected == 2)
         	    	  )
         	      {
         	    	  mActivity.invalidateOptionsMenu();
         	      }
             }
        //}
		 /*
		 else {
       	  	for(int i = 0; i < iStackCount;i++){
       	  		mActivity.getSupportFragmentManager().popBackStack();        		  
       	  	} 	
            if (mFragment != null) 
            {
            	ft.attach(mFragment);
       	      	if ((idxTabSelected == 1)||(idxTabSelected == 2))
       	      	{
       	      		mActivity.invalidateOptionsMenu();
       	      	}
            }
         }*/

		 //if (idxTabSelected != 5)
		 {
			 try{
				 Fragment lsMenu = mActivity.getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
				 if (lsMenu != null)
				 {
					 if (!lsMenu.isVisible())
					 {
						 int orientation = mActivity.getResources().getConfiguration().orientation;

					 		if (orientation == Configuration.ORIENTATION_LANDSCAPE)
					 		{
					 			FragmentTransaction ft_t = 
								mActivity.getSupportFragmentManager().beginTransaction();
					 			ft_t.show(lsMenu);
					 			ft_t.commit();
					 		}
					 }				 
					 if (tab.getPosition() == 1)
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.DO_PLAN_JOB);
					 else if (tab.getPosition() == 2){
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.DO_TASK);					 
					 }else if (tab.getPosition() == 3){
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.EXPENSE);					 
					 }else if (tab.getPosition() == 4){
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.NEWS);					 					 
					 }else if (tab.getPosition() == 5){
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.REPORT);					 					 						 
					 }else
					 {
						 ((CommonListMenuFragment)lsMenu).reloadListMenus(MenuGroupType.DASH_BOARD);					 					 
					 }
				 }
			 }catch(Exception ex){
				 ex.printStackTrace();
			 }
		 }
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		 if (mFragment != null) {
             ft.detach(mFragment);
         }
	}



}

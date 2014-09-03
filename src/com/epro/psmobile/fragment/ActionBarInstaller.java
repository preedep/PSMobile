package com.epro.psmobile.fragment;

import java.util.Calendar;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.caldroid.CaldroidFragment;
import com.epro.psmobile.R;
import com.epro.psmobile.TabMenuListener;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.FontUtil.FontName;

public class ActionBarInstaller {

	public ActionBarInstaller() {
		// TODO Auto-generated constructor stub
	}
	public static ActionBar installActionBar(
			SherlockFragmentActivity activity
			)
	{
		
		ActionBar bar = activity.getSupportActionBar();
		/*
		bar.setCustomView(R.layout.ps_custom_view_title);        
		bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayUseLogoEnabled(false);
        */
		bar.setDisplayShowTitleEnabled(false);

        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      
        String[] menu_title_names = activity.getResources().getStringArray(R.array.main_menu_text_array);
       
        for(int i = 0; i < menu_title_names.length;i++){
        	String title_name = menu_title_names[i];
        	//ContentViewFragment content = new ContentViewFragment();
        	TabMenuListener<?> listener = null;
    		Bundle bundle = new Bundle();
    		bundle.putInt(InstanceStateKey.TAB_INDEX_KEY, i);		

        	if (i == 0){
        		/*
        		 calendar view
        		 home screen
        		 * */
        		/*
        		Calendar cal = Calendar.getInstance();
        		bundle.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        		bundle.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        		bundle.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        		bundle.putBoolean(CaldroidFragment.FIT_ALL_MONTHS, false);*/
        		listener = 
            			new TabMenuListener<DashboardCalendarFragment>(activity,
            					InstanceStateKey.TAG_NAME_DASHBOARD_CALENDAR,
            					DashboardCalendarFragment.class,
            					bundle);        		
        	}else if (i == 1)
        	{
        		listener = 
        			new TabMenuListener<JobRequestPlanFragment>(activity,
        					InstanceStateKey.TAG_NAME_JOB_REQUEST_PLAN,
        					JobRequestPlanFragment.class,
        					bundle);
        	}else if (i == 2)
        	{
        		listener = 
            			new TabMenuListener<TaskListFragment>(activity,
            					InstanceStateKey.TAG_NAME_TASK_LIST,
            					TaskListFragment.class,
            					bundle);        		
        	}else if (i == 3){
        		listener = 
            			new TabMenuListener<ExpenseFragment>(activity,
            					InstanceStateKey.TAG_NAME_EXPENSE,
            					ExpenseFragment.class,
            					bundle);        		        		
        	}else if (i == 4){
        		listener = 
            			new TabMenuListener<NewsPagerFragment>(activity,
            					InstanceStateKey.TAG_NAME_NEWS,
            					NewsPagerFragment.class,
            					bundle);        		        		        		
        	}else {
        		listener = 
            			new TabMenuListener<ReportFragment>(activity,
            					InstanceStateKey.TAG_NAME_REPORT,
            					ReportFragment.class,
            					bundle);        		        		        		        		
        	}
        	
        	Tab tab = bar.newTab();
        	        	
        	tab.setCustomView(R.layout.tab_item);
        	TextView tabName = (TextView)tab.getCustomView().findViewById(R.id.tv_tab_name);
        	tabName.setText(title_name);
        	FontUtil.replaceFontTextView(activity, tabName, FontName.THSARABUN_BOLD);
        	
        	//tab.setText(title_name);
        	tab.setTabListener(listener);
        	bar.addTab(tab);
        }    
        return bar;
	}
}

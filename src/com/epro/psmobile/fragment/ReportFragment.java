/**
 * 
 */
package com.epro.psmobile.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.R;
import com.epro.psmobile.dialog.ReportFilterDialog.ReportType;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.MessageBox;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author nickmsft
 *
 */
public class ReportFragment extends ContentViewBaseFragment {

	private View currentView;
	private WebView webView;
	
	private String htmlData = "";
	private ReportType reportType;
	/**
	 * 
	 */
	public ReportFragment() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		
		super.onActivityCreated(savedInstanceState);
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.clear();
		inflater.inflate(R.menu.option_menu_report_zoom_save_only, menu);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentView = inflater.inflate(com.epro.psmobile.R.layout.ps_activity_html_preview, container, false);
		initial(currentView);
		return currentView;
	}

	private void initial(View view)
	{
		webView = (WebView)view.findViewById(R.id.html_preview);
		
		WebSettings setting = webView.getSettings();
		setting.setBuiltInZoomControls(true);
		setting.setAppCacheMaxSize(1);
		setting.setDefaultTextEncodingName("utf-8");
		
		Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
	    noCacheHeaders.put("Pragma", "no-cache");
	    noCacheHeaders.put("Cache-Control", "no-cache");
	    
	    webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	    webView.setScrollbarFadingEnabled(false);
	}
	
	public void setDataOnWebView(ReportType reportType,String data)
	{
		if (webView != null)
		{
			this.reportType = reportType;
			this.htmlData = data;
			webView.loadData(data, 
					 "text/html; charset=utf-8","utf-8");
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case R.id.menu_report_entry_full_screen:{
				Fragment f = getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
				if (f != null)
				{
					FragmentTransaction ft = 
							getSherlockActivity().getSupportFragmentManager().beginTransaction();
					if (f.isVisible()){
						ft.hide(f);
					}else{
						ft.show(f);										
					}
					ft.commit();
				}
			}break;
			case R.id.menu_report_entry_save:{
				/*
				 * save
				 */
			    Calendar cal = Calendar.getInstance();
			    int day = cal.get(Calendar.DAY_OF_MONTH);
			    int month = cal.get(Calendar.MONTH)+1;
			    int year = cal.get(Calendar.YEAR);
			    
			    int hh = cal.get(Calendar.HOUR_OF_DAY);
			    int min = cal.get(Calendar.MINUTE);
			    int sec = cal.get(Calendar.SECOND);
			    
		
			    String fileReport = reportType.name()+"_"+DataUtil.padZero(day)+""+
			    		DataUtil.padZero(month)+""+year+""+
			    		DataUtil.padZero(hh)+""+
			    		DataUtil.padZero(min)+""+
			    		DataUtil.padZero(sec)+".html";

			    File folderSaveReport = null;
			    
			    if (reportType == ReportType.GENERAL)
			    {
			    	folderSaveReport = 
				    		new File(Environment.getExternalStorageDirectory() + CommonValues.BACKUP_INSPECT_REPORT_FOLDER);			    	
			    }else{
			    	folderSaveReport = 
				    		new File(Environment.getExternalStorageDirectory() + CommonValues.BACKUP_EXPENSE_REPORT_FOLDER);			    				    	
			    }
			    
			    if (folderSaveReport != null)
			    {
			    	if (!folderSaveReport.exists()){
			    		folderSaveReport.mkdirs();
			    	}
			    	FileOutputStream f = null;
			        PrintWriter pw = null;
			        try {
			        	File fReport = new File(folderSaveReport.getAbsolutePath()+"/"+fileReport);
			            f = new FileOutputStream(fReport);
			            pw = new PrintWriter(f);
			            pw.println(this.htmlData);
			            pw.flush();
						MessageBox.showSaveCompleteMessage(getActivity());				
			        }catch(Exception ex)
			        {
			        	MessageBox.showMessage(getActivity(), "Error", ex.getMessage());
			        }finally{
			        	if (pw != null)
			        	{
			        		pw.close();
			        	}
			        	if (f != null)
			        	{
			        		try {
								f.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	}
			        }
			    }
			    /*
			    File rootBackupFolder = 
			    		new File(Environment.getExternalStorageDirectory() + CommonValues.BACKUP_FOLDER + "/"+folderName+"/");
			    if (!rootBackupFolder.exists()){
			    	rootBackupFolder.mkdirs();
			    }*/
			    
			    
			}break;
		}
		return super.onOptionsItemSelected(item);
	}
}

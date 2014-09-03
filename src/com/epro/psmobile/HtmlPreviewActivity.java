package com.epro.psmobile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.DataUtil;

import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HtmlPreviewActivity extends PsBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.ps_activity_html_preview);
		
		Bundle bArgument = this.getIntent().getExtras();
		if (bArgument != null)
		{
			Task task = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			CustomerSurveySite surveySite = bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
			
			
			String fileName = DataUtil.regenerateTaskCodeForMakeFolder(task.getTaskCode());
			fileName += "-";
			fileName += surveySite.getCustomerSurveySiteID();
			String htmlFileName = fileName + ".html";
			
			File directory = Environment.getExternalStorageDirectory();
			String rootFolder = directory.getAbsolutePath();
			rootFolder += CommonValues.PREVIEW_FOLDER;
			rootFolder += htmlFileName;

			File fHtml = new File(rootFolder);
			if (fHtml.exists()){
				WebView webView = (WebView)this.findViewById(R.id.html_preview);
				webView.setWebViewClient(new WebViewClient(){

					@Override
					public void onPageFinished(WebView view, String url) {
						// TODO Auto-generated method stub
						super.onPageFinished(view, url);
						view.clearCache(true);
					}
					
				});
				WebSettings setting = webView.getSettings();
				setting.setBuiltInZoomControls(true);
				setting.setAppCacheMaxSize(1);
				
				Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
			    noCacheHeaders.put("Pragma", "no-cache");
			    noCacheHeaders.put("Cache-Control", "no-cache");
			    
			    webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			    webView.setScrollbarFadingEnabled(false);
			    webView.loadUrl("file://"+rootFolder,noCacheHeaders);
			}
		}
	}

}

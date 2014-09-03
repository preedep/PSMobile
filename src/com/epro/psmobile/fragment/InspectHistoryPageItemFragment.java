package com.epro.psmobile.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.andpdf.refs.HardReference;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.InspectHistory;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.fragment.ContentViewBaseFragment.InspectOptMenuType;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.ImageUtil;
import com.epro.psmobile.util.PDFUtil;
import com.epro.psmobile.view.InspecItemViewCreator;
import com.epro.psmobile.view.InspectItemViewState;
import com.larvalabs.svgandroid.SVGParseException;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPaint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InspectHistoryPageItemFragment extends DrawingInspectFragment {

	private View currentView;
	private int webViewSize;
	private String fileImg = "";
	public InspectHistoryPageItemFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentView = inflater.inflate(R.layout.inspect_history_fragment_page_item, container, false);
		initial(currentView);
		return currentView;
	}

	private void initial(View view)
	{
		Bundle bArgument = this.getArguments();
		if (bArgument != null)
		{
			Task task = (Task)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			//InspectHistory history = (InspectHistory)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_INSPECT_HISTORY_ITEM);
			//ArrayList<CustomerSurveySite> customerSurveySiteList =  task.getJobRequest().getCustomerSurveySiteList();
			
			boolean bShownLayoutBuidlingOnly = bArgument.getBoolean(
					InstanceStateKey.KEY_ARGUMENT_IS_SHOW_JUST_LAYOUT_BUILDING, false);
			
			
			CustomerSurveySite surveySite = null;
			
			surveySite = (CustomerSurveySite)bArgument.getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
			
			if (surveySite != null)
			{
				TextView tvCustomerAddress = (TextView)view.findViewById(R.id.tv_lasted_inspect_customer_site_address);
				tvCustomerAddress.setText(surveySite.getSiteAddress());
				
				ImageView imgViewLayout = (ImageView)view.findViewById(R.id.img_inspect_drawing_history);
				final WebView webView = (WebView)view.findViewById(R.id.wv_inspect_last_result_history);
				ViewGroup viewGroup = (ViewGroup)view.findViewById(R.id.rl_previous_layout);
				ViewGroup layoutRestore = (ViewGroup)view.findViewById(R.id.v_scroll_show_layout_history);
				if (bShownLayoutBuidlingOnly)
				{
					layoutRestore.setVisibility(View.VISIBLE);
					viewGroup.setVisibility(View.VISIBLE);
					imgViewLayout.setVisibility(View.GONE);
					webView.setVisibility(View.GONE);
					
					try {
						restoreOldPlan(viewGroup,task.getTaskCode(),
								surveySite.getCustomerSurveySiteID());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						MessageBox.showMessage(getActivity(), "Error", e.getMessage());
					}
					/*
					 * show in svg
					 */
					/*
					String imgSvgFile = surveySite.getLayoutSVGPath();
					
					if (!imgSvgFile.isEmpty()){
						fileImg = SharedPreferenceUtil.getDownloadFolder(getActivity());
						fileImg += "/"+imgSvgFile;
						Bitmap bmpLastLayout = null;
						if (fileImg.endsWith(".svg"))
						{
							try {
								Drawable drawable = ImageUtil.createPictureDrawableFromSVGFile(fileImg);
								Bitmap bmpTmp = ImageUtil.drawableToBitmap(drawable);
								bmpLastLayout = ImageUtil.getResizedBitmapScale(bmpTmp, 800, 600);
								bmpTmp.recycle();
							} catch (SVGParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
						   bmpLastLayout = BitmapFactory.decodeFile(fileImg);							
						}
						if (bmpLastLayout != null)
						{
							imgViewLayout.setImageBitmap(bmpLastLayout);
						}
					}
					*/
				}else{
					layoutRestore.setVisibility(View.GONE);
					tvCustomerAddress.setVisibility(View.GONE);
					imgViewLayout.setVisibility(View.GONE);
					viewGroup.setVisibility(View.GONE);
					webView.setVisibility(View.VISIBLE);
					
					webView.getSettings().setJavaScriptEnabled(true);
			        webView.getSettings().setBuiltInZoomControls(true);//show zoom buttons
					webView.getSettings().setSupportZoom(true);//allow zoom
				    //get the width of the webview
					
					
					/*
					 * show in pdf file*/
					
					String pdfFile = surveySite.getResultPdfPath();
					if (!pdfFile.isEmpty())
					{
						//fileImg = history.getImageFileLastResult();
						
						fileImg = SharedPreferenceUtil.getDownloadFolder(getActivity());
						//fileImg += CommonValues.REPORT_FOLDER;
						fileImg += "/"+pdfFile;
						
						
						if (fileImg.endsWith(".pdf")){
					        webView.getSettings().setPluginState(PluginState.ON);
							webView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){

								@Override
								public void onGlobalLayout() {
									// TODO Auto-generated method stub
						        	webViewSize = webView.getWidth();
						            webView.getViewTreeObserver().removeGlobalOnLayoutListener(this);							
								}
								
							});
							// run async
					        new AsyncTask<Void, Void, Void>()
					        {
					        	// create and show a progress dialog
			                    ProgressDialog progressDialog = ProgressDialog.
			                    		show(getActivity(), "", "Opening...");
			                    /* (non-Javadoc)
								 * @see android.os.AsyncTask#onPreExecute()
								 */
								@Override
								protected void onPreExecute() {
									// TODO Auto-generated method stub
									if (progressDialog != null)
										progressDialog.setCancelable(false);
									
									super.onPreExecute();
								}
								@Override
			                    protected void onPostExecute(Void result)
			                    {
			                        //after async close progress dialog
									if (progressDialog != null)
										progressDialog.dismiss();
			                    }
								@Override
								protected Void doInBackground(Void... params) {
									// TODO Auto-generated method stub
									try {
										if (webViewSize == 0)
											webViewSize = 1000;
										
										PDFImage.sShowImages = true; // show images
									    PDFPaint.s_doAntiAlias = true; // make text smooth
									    HardReference.sKeepCaches = true; // save images in cache
									    
										final String html = PDFUtil.convertToHtml(webViewSize, fileImg);
										
										getActivity().runOnUiThread(new Runnable(){

											@Override
											public void run() {
												// TODO Auto-generated method stub
												webView.loadDataWithBaseURL("", html, "text/html","UTF-8", "");
												System.gc();											
											}
											
										});
										
										
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									return null;
								}
					        	
					        }.execute();
						}else if ((fileImg.endsWith(".html")||fileImg.endsWith(".htm"))){
							/*
							 * show loading report
							 */
//							String fileUrl = new
							webView.setWebViewClient(new WebViewClient(){

								/* (non-Javadoc)
								 * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
								 */
								@Override
								public boolean shouldOverrideUrlLoading(
										WebView view, String url) {
									// TODO Auto-generated method stub
									//return super.shouldOverrideUrlLoading(view, url);
									view.loadUrl(url);
									return true;
								}
								
							});
//							webView.loadUrl("http://www.google.com");
							
							 new AsyncTask<Void, Void, Void>()
						        {
								// create and show a progress dialog
				                    ProgressDialog progressDialog = null; 
				                    /* (non-Javadoc)
									 * @see android.os.AsyncTask#onPreExecute()
									 */
									@Override
									protected void onPreExecute() {
										// TODO Auto-generated method stub
										progressDialog = ProgressDialog.show(getActivity(), "", "Opening...");
										progressDialog.setCancelable(false);
										
										super.onPreExecute();
									}
									@Override
				                    protected void onPostExecute(Void result)
				                    {
				                        //after async close progress dialog
										if (progressDialog != null)
											progressDialog.dismiss();
				                    }
									@Override
									protected Void doInBackground(
											Void... params) {
										// TODO Auto-generated method stub
										getActivity().runOnUiThread(new Runnable(){

											@Override
											public void run() {
												// TODO Auto-generated method stub
												webView.loadUrl(Uri.fromFile(new File(fileImg)).toString());

											}
											
										});
										/*
										File file = new File(fileImg);

										//Read text from file
										final StringBuilder text = new StringBuilder();
										FileInputStream fis = null;

										try {
											fis = new FileInputStream(file);
											byte[] buffer = new byte[1024];
											while (fis.read(buffer) != -1) {
												text.append(new String(buffer));
											}
										    Log.d("DEBUG_D", text.toString());
										    getActivity().runOnUiThread(new Runnable(){

												@Override
												public void run() {
												    webView.loadData(text.toString(), "text/html; charset=UTF-8", null);

												}}
												);
										}catch(Exception ex)
										{
											ex.printStackTrace();
										}finally{
											if (fis != null)
												try {
													fis.close();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
										}*/
										
										return null;
									}
								 
						        }.execute();
						        
						}else{
							//not alert not support
							MessageBox.showMessage(getActivity(), getString(R.string.text_error_title), "File format doesn't supprt -> "+fileImg);
						}
							
						
				        
					}
				}
			}
			
		}
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		//super.onCreateOptionsMenu(menu, inflater,InspectOptMenuType.INSPECT_LOCATION);		
	}
	private void restoreOldPlan(ViewGroup vGroup,String taskCode,int customerSurveySiteID)
	throws Exception
	{
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(this.getActivity());
		ArrayList<InspectDataObjectSaved> objSavedList =  dataAdapter.getInspectDataObjectSaved(taskCode, 
				customerSurveySiteID);

		if (objSavedList != null)
		{
			//repaint all object on screen

			ArrayList<InspectDataItem> inspectDataItemList =  dataAdapter.getAllInspectDataItem();
			
			for(InspectDataObjectSaved dataSaved : objSavedList)
			{
				if (dataSaved.getAddObjectFlag().equalsIgnoreCase("Y"))continue;/*show previous download only*/
				
				InspectDataItem inspectItem = null;
				
				for(InspectDataItem item : inspectDataItemList)
				{
					if (item.getInspectDataItemID() == dataSaved.getInspectDataItemID()){
						inspectItem = item;
						break;
					}
				}				
				if (inspectItem != null){
					InspecItemViewCreator view = new InspecItemViewCreator(this.getActivity(),0);	
					
					final View vItem = view.createInspectItemView(inspectItem,
							dataSaved,
							dataSaved.getWidthObject(),
							dataSaved.getLongObject());
					
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vItem.getLayoutParams();
		            layoutParams.leftMargin = (int)dataSaved.getInspectDataItemStartX();
		            layoutParams.topMargin = (int)dataSaved.getInspectDataItemStartY();
		            vItem.setLayoutParams(layoutParams);
		            setSelected(vItem,true,false);
		            setVisibleConerButton(vItem,false);
					vItem.setRotation((float)dataSaved.getAngle());
					vGroup.addView(vItem);
					vItem.invalidate();
					
					InspectItemViewState state = (InspectItemViewState)vItem.getTag();
					
					 setResizeInspectItemSize(vItem, 
							 -1,-1,
							 state.getInspectDataObjectSaved().getWidthObject(), 
							 state.getInspectDataObjectSaved().getLongObject(),
							 true);
					 
				}
			}
		}
	}
}

/**
 * 
 */
package com.epro.psmobile.dialog;

import java.io.File;
import java.util.Calendar;

import net.lingala.zip4j.core.ZipFile;

import com.epro.psmobile.BuildConfig;
import com.epro.psmobile.R;
import com.epro.psmobile.da.DataMasterBuilder;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBOManager;
import com.epro.psmobile.data.TaskComplete;
import com.epro.psmobile.data.TaskResponseList;
import com.epro.psmobile.data.download.ResultSyncDownloadData;
import com.epro.psmobile.remote.api.HttpUploader;
import com.epro.psmobile.remote.api.PSMobleRemoteAPI;
import com.epro.psmobile.remote.api.Result;
import com.epro.psmobile.sync.BaseAsyncTask;
import com.epro.psmobile.sync.DataUploadGenerator;
import com.epro.psmobile.sync.DownloadManager;
import com.epro.psmobile.sync.JSONToDBLoader;
import com.epro.psmobile.sync.OnDownloadFileListener;
import com.epro.psmobile.sync.OnDownloadProgress;
import com.epro.psmobile.sync.OnInsertJSONToDBHandler;
import com.epro.psmobile.sync.TransferProgressListener;
import com.epro.psmobile.sync.UserAuthen;
import com.epro.psmobile.util.ActivityUtil;
import com.epro.psmobile.util.AppFolderUtil;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SysInfoGetter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author nickmsft
 *
 */
public class SyncDialog extends DialogFragment {

	public final static String THREAD_UPLOAD_NAME = "syncUpload";
	public final static String THREAD_DOWNLPAD_NAME = "syncDownload";
	
	private CheckBox chkOfflineMode;
	public interface OnSyncDialogInterface
	{
		void onDialogDismiss();
	};
	public interface OnSyncListener
	{
		void onSyncCompleted(SyncType syncType,boolean bSuccess);
	};
	
	public enum SyncType
	{
		SYNC_DOWNLOAD,
		SYNC_UPLOAD
	};

	private OnSyncDialogInterface onSyncDialogInterface;
	
	public class SyncThread extends Thread
	{
		private Activity activity;
		private SyncType syncType;
		private View rootView;
		
		private OnSyncListener onSyncListener;
		
		private ImageView imgSyncAnimation;
		
		public Thread animationThread;
		
		public boolean forceStopAnimationThread;
		
		private boolean bSuccessed = false;
		
		public SyncThread(Activity activity,
				SyncType syncType,
				View rootView)
		{
			this.activity = activity;
			this.syncType = syncType;
			this.rootView = rootView;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//super.run();

			if (syncType == SyncType.SYNC_UPLOAD)
			{
				this.setName(THREAD_UPLOAD_NAME);
				upload();
				
			}else if (syncType == SyncType.SYNC_DOWNLOAD)
			{
				this.setName(THREAD_DOWNLPAD_NAME);
				download();
			}
			
			

		}
		private void upload()
		{
			displayError(R.id.tv_sync_upload_error,"",false);
			displayProgress(R.id.tv_sync_upload_time_left,"",false);
			
			imgSyncAnimation = startAnimation(R.id.img_sync_upload);
			
			animationThread = new Thread(){

				/* (non-Javadoc)
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//super.run();
					int i = 0;
					while(true)
					{
						if (forceStopAnimationThread)break;							
						rotateAnimation(imgSyncAnimation,i);
						try {
							Thread.sleep(10);
							i++;
							//Log.d("DEBUG_D", "i = "+i);
							if (i > 359)i = 0;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			};
			forceStopAnimationThread = false;
			animationThread.start();
			/*
			 * 
			 */
			int versionCode = 0;
		       try 
		       {
		          versionCode = SysInfoGetter.getAppVersionCode(activity);
		       }
		       catch (NameNotFoundException e1) {
		          // TODO Auto-generated catch block
		          e1.printStackTrace();
		       }
			/*
			 * show complete!!!!
			 */
			DataUploadGenerator uploadGenerator = new DataUploadGenerator(activity);
			try {
					String zipFileName = uploadGenerator.generate();
					if (!chkOfflineMode.isChecked())
					{
				
						String userName = SharedPreferenceUtil.getUserName(activity);
						String password = SharedPreferenceUtil.getPassword(activity);
						String deviceId = SharedPreferenceUtil.getDeviceId(activity);
						HttpUploader httpUploader = new HttpUploader(
								SharedPreferenceUtil.getServiceUrl(activity)+
								CommonValues.PSMOBILE_WEB_UPLOAD_SERVICE_URL,deviceId
								);
							Result result = httpUploader.upload(
									PSMobleRemoteAPI.CMD_TYPE_UPLOAD, 
									userName, 
									password, 
									new File(zipFileName), 
									versionCode,
									new TransferProgressListener(){

										@Override
										public void transferred(
												String messageTypeId,
												long allOfDataSize, long num) {
											// TODO Auto-generated method stub
											displayProgress(R.id.tv_sync_upload_time_left,
													""+num+"/"+allOfDataSize,true);

										}
										
									});
						
							if (result.isSuccessful())
							{
								bSuccessed = true;
								displayProgress(R.id.tv_sync_upload_time_left,"Completed..",true);
							}else{
								bSuccessed = false;
								displayError(R.id.tv_sync_upload_error,result.getErrorMessage(),true);
							}
					 }else{
						 /*
						  * copy file to bkk up and display 
						  */
						    Calendar cal = Calendar.getInstance();
						    int day = cal.get(Calendar.DAY_OF_MONTH);
						    int month = cal.get(Calendar.MONTH)+1;
						    int year = cal.get(Calendar.YEAR);
						    
						    int hh = cal.get(Calendar.HOUR_OF_DAY);
						    int min = cal.get(Calendar.MINUTE);
						    int sec = cal.get(Calendar.SECOND);
						    
					
						    String folderName = DataUtil.padZero(day)+""+
						    		DataUtil.padZero(month)+""+year+""+
						    		DataUtil.padZero(hh)+""+
						    		DataUtil.padZero(min)+""+
						    		DataUtil.padZero(sec);

						    File rootBackupFolder = 
						    		new File(Environment.getExternalStorageDirectory() + CommonValues.BACKUP_FOLDER + "/"+folderName+"/");
						    if (!rootBackupFolder.exists()){
						    	rootBackupFolder.mkdirs();
						    }
	
						    String deviceId = SharedPreferenceUtil.getDeviceId(getActivity());
						    String fileUpload = deviceId+".zip";
						    
						    final File fData = new File(rootBackupFolder.getAbsolutePath()+"/"+fileUpload);
						    if (fData.exists())
						    	fData.delete();
						    
						    File fScr = new File(zipFileName);
						    
						    AppFolderUtil.copy(fScr, fData);
						    
							bSuccessed = true;
							displayProgress(R.id.tv_sync_upload_time_left,"Offline mode Completed..",true);
							
							
							try{
								activity.runOnUiThread(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(activity, "Backup complete to "+fData.getAbsolutePath(), 2000).show();
									}
									
								});
							}catch(Exception ex){}
							
					 }
				/////////////	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				bSuccessed = false;
				if (e instanceof NullPointerException){
					displayError(R.id.tv_sync_upload_error,"Null pointer exception",true);					
				}else{
					displayError(R.id.tv_sync_upload_error,e.getMessage(),true);
				}
			}

			
					
			forceStopAnimationThread = true;
			stopAnimation(imgSyncAnimation);
			
			if (onSyncListener != null)
			{
				onSyncListener.onSyncCompleted(SyncType.SYNC_UPLOAD,bSuccessed);
			}
		}
		private void download()
		{
			displayError(R.id.tv_sync_download_error,"",false);
			displayProgress(R.id.tv_sync_download_time_left,"",false);
			
			imgSyncAnimation = startAnimation(R.id.img_sync_download);
			
			animationThread = new Thread(){

				/* (non-Javadoc)
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//super.run();
					int i = 0;
					while(true)
					{
						if (forceStopAnimationThread)break;							
						rotateAnimation(imgSyncAnimation,i);
						try {
							Thread.sleep(10);
							i++;
							//Log.d("DEBUG_D", "i = "+i);
							if (i > 359)i = 0;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			};
			forceStopAnimationThread = false;
			animationThread.start();
			
			PSMobleRemoteAPI remoteApi = new PSMobleRemoteAPI(activity);
			String userName = SharedPreferenceUtil.getUserName(activity);
			String password = SharedPreferenceUtil.getPassword(activity);
			String deviceId = SharedPreferenceUtil.getDeviceId(activity);
			
			displayProgress(R.id.tv_sync_download_time_left,"Checking file..",true);
			
			int versionCode = 0;
			
			try 
			{
               versionCode = SysInfoGetter.getAppVersionCode(getActivity());
            }
            catch (NameNotFoundException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
			Result result = 
					remoteApi.login(userName, password, deviceId, "041e",versionCode);

			if (result.isSuccessful())
			{
				/*
				 * 
				 */
				try{
					final ResultSyncDownloadData data = result.getData(ResultSyncDownloadData.class);
					String url = 	SharedPreferenceUtil.getServiceUrl(activity)+
									CommonValues.PSMOBILE_WEB_DOWNLOAD_URL+"/"+data.getDataZipUrl();
					
					displayProgress(R.id.tv_sync_download_time_left,"Download..",true);

					DownloadManager.download(getActivity(), 
							null, url, 
							data.getDataZipUrl(), 
							new OnDownloadFileListener(){

								@Override
								public void onDownloadFileCompleted(
										Context context,
										BaseAsyncTask<?, ?, ?> asyncTask,
										String filePath, boolean isError,
										String errorMessage) {
									// TODO Auto-generated method stub
									
									if (!isError)
									{
										try{
											ZipFile zipFile = new ZipFile(filePath);
											if (zipFile.isEncrypted()) {
												String encrypted = SharedPreferenceUtil.getPassword(getActivity());//DataUtil.encryptToMD5("progress1234");
												zipFile.setPassword(encrypted);
											}
									    
											File externalSDDir = Environment.getExternalStorageDirectory();
							            
											File downloadDir = new File(externalSDDir.getAbsolutePath()+CommonValues.DOWNLOAD_FOLDER);
											
											/*
											if (downloadDir.exists())
											{
												downloadDir.deleteOnExit();
											}
											downloadDir.mkdirs();
											*/
											displayProgress(R.id.tv_sync_download_time_left,"Extract all..",true);

											zipFile.extractAll(downloadDir.getAbsolutePath());
											
											/*
											 * update new database
											 */
											String jsonFolder = downloadDir.getAbsolutePath();
											jsonFolder += "/"+data.getDataZipUrl().toLowerCase().replace(".zip", "")+"/";
											SharedPreferenceUtil.saveDownloadFolder(getActivity(),jsonFolder);
//											jsonFolder += "/D004/";
											File folderJsonDecompressd = new File(jsonFolder);
											if (folderJsonDecompressd.isDirectory())
											{
												File subFolders[] = folderJsonDecompressd.listFiles();
												for(File subFolder : subFolders)
												{
													try{
														displayProgress(R.id.tv_sync_download_time_left,"Importing..",true);

														if (subFolder.isDirectory())
														{
															JSONToDBLoader.loadJSONFileToDb(getActivity(),
																	subFolder.getAbsolutePath(),
															new OnInsertJSONToDBHandler(){

																@Override
																public void onInsertCompleted(
																		int rowEffected,
																		String jsonFileName,
																		String tableName) {
																	// TODO Auto-generated method stub
																	//asyncTask.displayProgress("Insert "+tableName+" completed.");
																	/*
																	 * if taskComplete delete all contents under taskCode
																	 */
																	if (tableName.equalsIgnoreCase("taskcomplete")){
																		Log.d("DEBUG_D", "has task complete");

																		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(activity);
																		
																		try {
																			TaskResponseList<TaskComplete> taskCompletes = dataAdapter.getTaskComplete();
																			for(TaskComplete taskComplete : taskCompletes)
																			{
																				int rows  = dataAdapter.clearTaskComplete(taskComplete.getTaskCode());
																				Log.d("DEBUG_D", "Row effected clear task complete ["+taskComplete.getTaskCode()+"]"+rows);
																			}
																		} catch (Exception e) {
																			// TODO Auto-generated catch block
																			e.printStackTrace();
																		}
																	}
																}															
															 }
															);												
														}
														
													}catch(Exception ex){
														Log.d("DEBUG_D", "Load json files in "+subFolder+" error ->"+ex.getMessage());
													}
													finally{
														//PSBOManager psboManager = new PSBOManager(getActivity());
														//psboManager.updateTransactionLogFistTime();	
														
													}
												}
											}
											/*
											 * finish
											 */
											/*
											 * alert complete
											 */
											displayProgress(R.id.tv_sync_download_time_left,"Completed..",true);
											if (getOnSyncListener() != null)
											{
												activity.runOnUiThread(new Runnable(){

													@Override
													public void run() {
														// TODO Auto-generated method stub
														getOnSyncListener().onSyncCompleted(syncType,true);						
													}					
												});
											}
											/*
											 * dump datas
											 */
											if (BuildConfig.DEBUG){
												File dbPath =  Environment.getExternalStorageDirectory();
												DataMasterBuilder.dumpAppDB(getActivity(), 
														dbPath.getAbsolutePath());
											}
										}catch(Exception ex){
											/*
											 show error  
											 */
											bSuccessed = false;
											displayError(R.id.tv_sync_download_error,ex.getMessage(),true);

										}
										finally{
											//stop animation
										}
									}else{										
										bSuccessed = false;
										displayError(R.id.tv_sync_download_error,
												errorMessage,
												true);
									}
								}
							}, 
							new OnDownloadProgress(){

								@Override
								public void onPublisProgress(int progress) {
									// TODO Auto-generated method stub
									displayProgress(R.id.tv_sync_download_time_left,progress+"/100",true);

									bSuccessed = true;
								}
						
					});
					
					
				}catch(Exception ex)
				{
					/*
					 * display error
					 */
					bSuccessed = false;
					ex.printStackTrace();
					displayError(R.id.tv_sync_download_error,ex.getMessage(),true);

				}
			}else{
				/*
				 * display error
				 */
				bSuccessed = false;
				String error = result.getErrorMessage();
				if (result.getException() != null)
				{
					error = result.getException().toString();
				}
				displayError(R.id.tv_sync_download_error,error,true);
			}

			/////////
			forceStopAnimationThread = true;
			stopAnimation(imgSyncAnimation);
			
			if (getOnSyncListener() != null)
			{
				getOnSyncListener().onSyncCompleted(SyncType.SYNC_DOWNLOAD,bSuccessed);
			}
		}
		private void displayProgress(int resId,
				final String status,
				final boolean shown)
		{
			final TextView tv = (TextView)rootView.findViewById(resId);
			try{
				activity.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (shown)
						{
							tv.setVisibility(View.VISIBLE);
							tv.setText(status);
						}else{
							tv.setVisibility(View.GONE);
						}
					}
					
				});
			}catch(Exception ex){}
		}
		private void displayError(
				int resId,
				final String errorMessage,
				final boolean shown)
		{
			final TextView tv = (TextView)rootView.findViewById(resId);
			try{
				activity.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (shown){
							tv.setVisibility(View.VISIBLE);
							tv.setText(errorMessage);
						}else{
							tv.setVisibility(View.GONE);
						}
					}
				});
			}catch(Exception ex){}
		}
		private ImageView startAnimation(int resId)
		{
			final ImageView imgSyncUpload = (ImageView)rootView.findViewById(resId);
			try{
				activity.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (imgSyncUpload != null)
							imgSyncUpload.setImageResource(R.drawable.ic_syncin_active);
					}
					
				});
			}catch(Exception ex){}		
			return imgSyncUpload;
		}
		private void stopAnimation(final ImageView imgSyncAnimation)
		{
			try{
				activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (imgSyncAnimation != null)
						imgSyncAnimation.setImageResource(R.drawable.ic_syncin);
				}
				
			  });
			}catch(Exception ex){}
		}
		private void rotateAnimation(final ImageView imgSyncAnimation,final float angle){
			try{
			activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (imgSyncAnimation != null)
					{
						imgSyncAnimation.setRotation(angle);
					}
				}
				
			});
		  }catch(Exception ex){}
		}
		public OnSyncListener getOnSyncListener() {
			return onSyncListener;
		}
		public void setOnSyncListener(OnSyncListener onSyncListener) {
			this.onSyncListener = onSyncListener;
		}
	};
	public static SyncDialog newInstance(){
		
		SyncDialog syncDlg = new SyncDialog();
		
		return syncDlg;
	}
	
	private View currentView;
	/**
	 * 
	 */
	private SyncDialog() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		currentView = inflater.inflate(R.layout.sync_dialog, container, false);
		initial(currentView);
		return currentView;
	}

	private void initial(final View view)
	{
		this.getDialog().setTitle(R.string.text_title_sync_dlg);
		
		final Button btnSync = (Button)view.findViewById(R.id.btn_sync_ok);
		Button btnCancel = (Button)view.findViewById(R.id.btn_sync_cancel);
		
		final CheckBox chkUpload = (CheckBox)view.findViewById(R.id.chk_sync_upload);
		final CheckBox chkDownload = (CheckBox)view.findViewById(R.id.chk_sync_download);

		chkUpload.setChecked(true);
		chkDownload.setChecked(true);

		chkOfflineMode = (CheckBox)view.findViewById(R.id.chk_sync_off_line_mode);
		chkOfflineMode.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked){
					chkDownload.setChecked(false);
					chkDownload.setEnabled(false);
					chkUpload.setChecked(true);
					chkUpload.setEnabled(false);
				}else{
					chkDownload.setEnabled(true);
					chkUpload.setEnabled(true);
				}
			}
			
		});
		
		btnSync.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * kill all thread first
				 */
				ActivityUtil.killThreadByName(THREAD_DOWNLPAD_NAME);
				ActivityUtil.killThreadByName(THREAD_UPLOAD_NAME);
				
				if (chkUpload.isChecked() && chkDownload.isChecked()){
					btnSync.setEnabled(false);
					/*
					 * - upload 
					 * - download
					 */
					SyncThread syncThread = new SyncThread(getActivity(),
							SyncType.SYNC_UPLOAD,
							view);
					syncThread.setOnSyncListener(new OnSyncListener(){

						@Override
						public void onSyncCompleted(SyncType syncType,boolean bSuccessed) {
							// TODO Auto-generated method stub
							if (!bSuccessed){
								try{
									getActivity().runOnUiThread(new Runnable(){
									@Override
									public void run() {
										// TODO Auto-generated method stub
										btnSync.setEnabled(true);
										}
									});
								}
								catch(Exception ex){}
								return;
							}
							
							SyncThread syncThread = new SyncThread(getActivity(),
									SyncType.SYNC_DOWNLOAD,
									view);
							syncThread.setOnSyncListener(new OnSyncListener()
							{

								@Override
								public void onSyncCompleted(SyncType syncType,boolean bSuccessed) {
									// TODO Auto-generated method stub
									try{
										getActivity().runOnUiThread(new Runnable(){

										@Override
										public void run() {
											// TODO Auto-generated method stub
											btnSync.setEnabled(true);
											}
										});
									}
									catch(Exception ex){}
									
									if (getOnSyncDialogInterface() != null)
									{
										if (bSuccessed){
											SyncDialog.this.dismiss();
											getOnSyncDialogInterface().onDialogDismiss();
										}
									}
								}
								
							});
							syncThread.start();	
						}
						
					});
					syncThread.start();
				}else if (chkUpload.isChecked()){
					btnSync.setEnabled(false);
					SyncThread syncThread = new SyncThread(getActivity(),
							SyncType.SYNC_UPLOAD,
							view);
					syncThread.setOnSyncListener(new OnSyncListener(){

						@Override
						public void onSyncCompleted(SyncType syncType,boolean bSuccessed) {
							// TODO Auto-generated method stub
							try
							{
							  getActivity().runOnUiThread(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									btnSync.setEnabled(true);
								}
							  });
							}catch(Exception ex){}

							if (getOnSyncDialogInterface() != null)
							{
								if (bSuccessed){
									PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
									try{
										int rowEffected  = dataAdapter.updateSyncStatusToTasks();
										if (rowEffected > 0){
											SyncDialog.this.dismiss();
											getOnSyncDialogInterface().onDialogDismiss();
										}
									}catch(Exception ex){
										MessageBox.showMessage(getActivity(), "Error", ex.getMessage());
									}
								}
							}

						}
						
					});
					syncThread.start();										
				}else if (chkDownload.isChecked()){
					btnSync.setEnabled(false);
					SyncThread syncThread = new SyncThread(getActivity(),
							SyncType.SYNC_DOWNLOAD,
							view);
					syncThread.setOnSyncListener(new OnSyncListener(){

						@Override
						public void onSyncCompleted(SyncType syncType,boolean bSuccessed) {
							// TODO Auto-generated method stub
							try{
								getActivity().runOnUiThread(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									btnSync.setEnabled(true);
								}
								});
							}catch(Exception ex){}
							
							if (getOnSyncDialogInterface() != null)
							{
								if (bSuccessed)
								{
									SyncDialog.this.dismiss();
									getOnSyncDialogInterface().onDialogDismiss();

								}
							}
						}						
					});
					syncThread.start();										
				}
				
			}
			
		});
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*
				 * stop all thread
				 */
				ActivityUtil.killThreadByName(THREAD_DOWNLPAD_NAME);
				ActivityUtil.killThreadByName(THREAD_UPLOAD_NAME);
				
				SyncDialog.this.dismiss();
			}
			
		});
	}

	/**
	 * @return the onSyncDialogInterface
	 */
	public OnSyncDialogInterface getOnSyncDialogInterface() {
		return onSyncDialogInterface;
	}

	/**
	 * @param onSyncDialogInterface the onSyncDialogInterface to set
	 */
	public void setOnSyncDialogInterface(OnSyncDialogInterface onSyncDialogInterface) {
		this.onSyncDialogInterface = onSyncDialogInterface;
	}
}

package com.epro.psmobile.dialog;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;

import org.json.JSONException;

import com.epro.psmobile.R;
import com.epro.psmobile.da.DataMasterBuilder;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.da.PSBOManager;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.download.ResultSyncDownloadData;
import com.epro.psmobile.remote.api.Result;
import com.epro.psmobile.sync.BaseAsyncTask;
import com.epro.psmobile.sync.DownloadFileAsyncTask;
import com.epro.psmobile.sync.JSONToDBLoader;
import com.epro.psmobile.sync.LoginAsyncTask;
import com.epro.psmobile.sync.OnAsyncTaskResultHandler;
import com.epro.psmobile.sync.OnDownloadFileListener;
import com.epro.psmobile.sync.OnInsertJSONToDBHandler;
import com.epro.psmobile.sync.UserAuthen;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.CommonValues;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.KeyboardUtil;
import com.epro.psmobile.util.MessageBox;
import com.epro.psmobile.util.SysInfoGetter;
import com.epro.psmobile.util.TextFormat;
import com.epro.psmobile.util.FontUtil.FontName;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TeamLoginDialog extends DialogFragment 
implements android.view.View.OnClickListener, OnAsyncTaskResultHandler {

	public interface OnLoginInterface{
		void onLoginCompleted();
		
	}
	private final static String USER = "admin";
	private final static String PASSWORD = "1234";
	
	//private Activity activity;
	private View currentView;
	

	private OnLoginInterface onLoginInterface;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//this.getDialog().setTitle(R.string.dialog_team_title_name);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		Dialog dlg =  super.onCreateDialog(savedInstanceState);
//		dlg.setTitle(R.string.dialog_team_title_name);
		Builder dialog = new AlertDialog.Builder(this.getActivity());
		dialog.setIcon(R.drawable.ic_psmobile);
		dialog.setTitle(R.string.dialog_team_title_name);
		
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    currentView = inflater.inflate(R.layout.ps_dialog_team_login, null);
	    initial(currentView);
		
	    dialog.setView(currentView);
		
		return dialog.create();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
		//currentView = inflater.inflate(R.layout.ps_dialog_team_login, container, false);
		//initial(currentView);
		//return currentView;
	}

	private void initial(View view)
	{
		
		EditText editUser = (EditText)view.findViewById(R.id.et_dialog_team_login_uername);
		EditText editPassword = (EditText)view.findViewById(R.id.et_dialog_team_login_password);

		TextView tvTextVersion = (TextView)view.findViewById(R.id.tv_text_app_version);
		tvTextVersion.setText(
				SharedPreferenceUtil.getTextVersion(getActivity())
				);
		
		boolean isDebuggable = (0 != (getActivity().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
		if (isDebuggable){
			//editUser.setText("47206");
			editUser.setText("50210");
			editPassword.setText("1234");
		}
		TextView tvCurrentDate = (TextView)view.findViewById(R.id.tv_login_date);
		TextView tvCurrentTime = (TextView)view.findViewById(R.id.tv_login_time);
		
		TextView tvDeviceId = (TextView)view.findViewById(R.id.tv_login_device_id);
		
		String deviceId = SysInfoGetter.getDeviceID(getActivity());
		
		Log.d("DEBUG_D", "Device id -> "+deviceId);

		
		tvDeviceId.setText(SysInfoGetter.getDeviceID(getActivity()));

		FontUtil.replaceFontTextView(getActivity(), tvCurrentDate,FontName.THSARABUN);
		FontUtil.replaceFontTextView(getActivity(), tvCurrentTime,FontName.THSARABUN);
		
		java.util.Date curDate = new java.util.Date();
		
		String strDateDisplay = getResources().getString(
									R.string.dialog_team_text_current_date, TextFormat.convertDate(curDate));
		tvCurrentDate.setText(strDateDisplay);
		
		String strTimeDisplay = getResources().getString(
				R.string.dialog_team_text_current_time, TextFormat.convertTime(curDate));
		
		tvCurrentTime.setText(strTimeDisplay);
		
		Button btnLogin = (Button)view.findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try{
			
			EditText editUser = (EditText)currentView.findViewById(R.id.et_dialog_team_login_uername);
			EditText editPassword = (EditText)currentView.findViewById(R.id.et_dialog_team_login_password);

			//editUser.setText("47206");
			//editPassword.setText("progress1234");
			
			
			
			
			String userName = editUser.getText().toString();//"51227";//editUser.getText().toString();
//			String password = DataUtil.encryptToMD5("progress1234");//editPassword.getText().toString();
			String password = DataUtil.encryptToMD5(editPassword.getText().toString());


			String deviceId = SysInfoGetter.getDeviceID(getActivity());
			
			Log.d("DEBUG_D", "Device id -> "+deviceId);
			
			SharedPreferenceUtil.saveUserPassword(getActivity(), userName, password,deviceId);
			
			UserAuthen userAuthen = new UserAuthen(getActivity());
			userAuthen.setUserName(userName);
			userAuthen.setPassword(password);
			userAuthen.setDeviceId(/*SysInfoGetter.getDeviceID(getContext())*/deviceId);
			userAuthen.setLocale("041e");
			
			LoginAsyncTask loginAsyncTask = new LoginAsyncTask(getActivity(),this);
			loginAsyncTask.execute(userAuthen);
			
			
            KeyboardUtil.hideKeyboard(getActivity(),v);
			
		}catch(Exception ex)
		{
           KeyboardUtil.hideKeyboard(getActivity(),v);

			MessageBox.showMessage(getActivity(), 
					R.string.message_box_title_error, ex.getMessage());
		}finally{
		}
		
	}

	@Override
	public void onFinishedTask(Result result) {
		// TODO Auto-generated method stub
		if (!result.isSuccessful())
		{
			MessageBox.showMessage(getActivity(), 
					R.string.message_box_title_error, 
					result.getErrorMessage());
		}else{
			/*
			 * 
			 */
			
			
			try {
				final ResultSyncDownloadData data = result.getData(ResultSyncDownloadData.class);
				String url = 	SharedPreferenceUtil.getServiceUrl(getActivity())+
								CommonValues.PSMOBILE_WEB_DOWNLOAD_URL+"/"+data.getDataZipUrl();
				
				final DownloadFileAsyncTask downloader = new DownloadFileAsyncTask(getActivity(),
						new OnDownloadFileListener(){

							@Override
							public void onDownloadFileCompleted(
									Context context,
									final BaseAsyncTask<?, ?, ?> asyncTask,
									String filePath,
									boolean isError, 
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
										if (downloadDir.exists())
										{
											downloadDir.deleteOnExit();
										}
										downloadDir.mkdirs();
										
										zipFile.extractAll(downloadDir.getAbsolutePath());
										
										/*
										 * update new database
										 */
										String jsonFolder = downloadDir.getAbsolutePath();
										jsonFolder += "/"+data.getDataZipUrl().toLowerCase().replace(".zip", "")+"/";
										SharedPreferenceUtil.saveDownloadFolder(getActivity(),jsonFolder);
//										jsonFolder += "/D004/";
										File folderJsonDecompressd = new File(jsonFolder);
										boolean hasError = false;
										if (folderJsonDecompressd.isDirectory())
										{
											File subFolders[] = folderJsonDecompressd.listFiles();
											for(File subFolder : subFolders)
											{
												try{
													if (subFolder.isDirectory()){
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
																
																
																//Log.d("DEBUG_D", "Insert "+tableName+" completed rowEffected = "+rowEffected);
															}															
														 }
														);												
													}
												}catch(Exception ex){
													Log.d("DEBUG_D", "Load json files in "+subFolder+" error ->"+ex.getMessage());
												}
												finally{
													
												}
											}
										}
										PSBOManager psboManager = new PSBOManager(getActivity());
										psboManager.updateTransactionLogFistTime();	
										/*
										 * finish
										 */
										
										
										/*
										 */
										PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
										Team team = dataAdapter.getTeamByDeviceId(
												SharedPreferenceUtil.getDeviceId(getActivity())
												);
										if (team != null)
										{
											SharedPreferenceUtil.saveTeamID(getActivity(), team.getTeamID());
										}
										/*
										 * start activity
										 * 
										 * 
										 */
										if (getOnLoginInterface() != null)
										{
											getOnLoginInterface().onLoginCompleted();
										}
										
									}catch(Exception ex)
									{
										ex.printStackTrace();
										Log.d("DEBUG_D", "ERROR : "+ ex.getMessage());
/*										
										MessageBox.showMessage(getActivity(), 
												R.string.message_box_title_error, 
												"Extract zip file error : "+errorMessage);
	*/									
									}
									finally{
										/*
										 * alert complete
										 */
										/*
										 * dump datas
										 */
										File dbPath =  Environment.getExternalStorageDirectory();
								        
										DataMasterBuilder.dumpAppDB(getActivity(), dbPath.getAbsolutePath());
										
										TeamLoginDialog.this.dismiss();	
										
									}
								}else{
									Log.d("DEBUG_D", "ERROR : "+errorMessage);
									/*
									MessageBox.showMessage(getActivity(), 
											R.string.message_box_title_error, 
											errorMessage);
*/									
								}
							}

										
				}
			);
				
				downloader.execute(new String[]{url,data.getDataZipUrl()});
				
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				MessageBox.showMessage(getActivity(), 
						R.string.message_box_title_error, 
						ex.getMessage());

			} 
		}
	}

	public OnLoginInterface getOnLoginInterface() {
		return onLoginInterface;
	}

	public void setOnLoginInterface(OnLoginInterface onLoginInterface) {
		this.onLoginInterface = onLoginInterface;
	}
	
}

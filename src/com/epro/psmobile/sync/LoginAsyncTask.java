package com.epro.psmobile.sync;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.epro.psmobile.R;
import com.epro.psmobile.remote.api.PSMobleRemoteAPI;
import com.epro.psmobile.remote.api.Result;
import com.epro.psmobile.util.SysInfoGetter;


public class LoginAsyncTask extends BaseAsyncTask<UserAuthen, Void, Result> {

	public LoginAsyncTask(Context context,OnAsyncTaskResultHandler onAsyncTaskResultHandler) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOnTaskResultHandler(onAsyncTaskResultHandler);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		this.displayMessage(R.string.async_task_login_message);		
	}
	
	@Override
	protected Result doInBackground(UserAuthen... arg0) {
		// TODO Auto-generated method stub
       int versionCode = 0;
       UserAuthen userAuthen = arg0[0];
       
       try 
       {
          versionCode = SysInfoGetter.getAppVersionCode(userAuthen.getContext());
       }
       catch (NameNotFoundException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
       }
		PSMobleRemoteAPI remoteApi = new PSMobleRemoteAPI(userAuthen.getContext());
		Result result = remoteApi.login(userAuthen.getUserName(),
				userAuthen.getPassword(),
				userAuthen.getDeviceId(),
				userAuthen.getLocale(),
				versionCode);
		return result;
	}

	@Override
	protected void onPostExecute(Result result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (this.getOnTaskResultHandler() != null)
		{
			this.getOnTaskResultHandler().onFinishedTask(result);
		}		
	}
}

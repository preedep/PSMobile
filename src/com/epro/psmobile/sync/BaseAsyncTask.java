package com.epro.psmobile.sync;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class BaseAsyncTask<T, K, L> extends AsyncTask<T, K, L> {

	private ProgressDialog alert;
	private ProgressDialog mProgressDialog;

	private OnAsyncTaskResultHandler onTaskResultHandler;
	private Context context;
	public BaseAsyncTask(Context context)
	{
		this.context = context;
	}
	protected Context getContext()
	{
		return this.context;
	}
	public void displayMessage(int resIdmsg)
	{
		displayMessage(context.getResources().getString(resIdmsg));
	}
	public void displayMessage(final String msg)
	{
		
		Activity activity = (Activity)context;
		
		activity.runOnUiThread(new Runnable(){/*safe thread*/

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(null == alert){
		    		alert = new ProgressDialog(context);
		    		alert.setMessage(msg);
		    		alert.setCancelable(false);
		    		alert.setIndeterminate(false);
		    	}else{
		    		alert.setMessage(msg);
		    	}
		    	
				if(!alert.isShowing())alert.show();				
			}			
		});		
	}
	public void displayProgress(final String msg)
	{
		Activity activity = (Activity)context;
		activity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// instantiate it within the onCreate method
				if (mProgressDialog == null){
					mProgressDialog = new ProgressDialog(context);
					mProgressDialog.setMessage("Downloading");
					mProgressDialog.setIndeterminate(false);
					mProgressDialog.setMax(100);
					mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				}else{
					mProgressDialog.setMessage(msg);
				}
				
				if (!mProgressDialog.isShowing())mProgressDialog.show();
								
			}
			
		});
	}
	protected void closeDisplayMessage(){
		
	   Activity activity = (Activity)context;
       
	    if (activity != null){
	       activity.runOnUiThread(new Runnable(){

            @Override
            public void run() {
               // TODO Auto-generated method stub
               if (alert != null)
               {
                   alert.dismiss();
               }
            }
	          
	       }) ; 
	    }
		
	}
	protected void closeProgressDisplay(){
	   Activity activity = (Activity)context;
       
       if (activity != null){
            activity.runOnUiThread(new Runnable(){

               @Override
               public void run() {
                  // TODO Auto-generated method stub
                  if (mProgressDialog != null)
                  {
                      mProgressDialog.dismiss();
                  }
               }
               
            });
       }
       
		
	}
	public OnAsyncTaskResultHandler getOnTaskResultHandler() {
		return onTaskResultHandler;
	}
	public void setOnTaskResultHandler(OnAsyncTaskResultHandler onTaskResultHandler) {
		this.onTaskResultHandler = onTaskResultHandler;
	}
	
	@Override
	protected void onPostExecute(L result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		closeDisplayMessage();
		closeProgressDisplay();
	}
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected L doInBackground(T... params) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(K... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		if (mProgressDialog != null)
		{
			if (values[0] instanceof Integer){
				mProgressDialog.setProgress((Integer)values[0]);
			}
		}
	}
	   
}

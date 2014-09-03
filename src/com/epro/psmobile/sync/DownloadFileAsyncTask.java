package com.epro.psmobile.sync;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.epro.psmobile.util.CommonValues;



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadFileAsyncTask extends BaseAsyncTask<String, Integer, String> {
	
	public final static int TIME_OUT = 60 * 1000;
	private OnDownloadFileListener downloadListener;
	public DownloadFileAsyncTask(Activity activity,
			OnDownloadFileListener downloadListener)
	{
		super(activity);
		this.downloadListener = downloadListener;
	}
    @Override
    protected String doInBackground(String... sUrl) {
    	
    	OutputStream output = null;
    	InputStream input = null;
    	URLConnection connection = null;
    	//Exception ex = null;
    	//boolean bSuccessful = false;
        try {
        	Log.d("DEBUG", "url downloading = "+sUrl[0]);
        	
            URL url = new URL(sUrl[0]);
            String fileZipName = sUrl[1];
            
            connection = url.openConnection();
            connection.setConnectTimeout(TIME_OUT);
            connection.setReadTimeout(TIME_OUT);
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            //InputStream input = new BufferedInputStream(url.openStream());
            
            input = new BufferedInputStream(connection.getInputStream());
            
            File externalSDDir = Environment.getExternalStorageDirectory();
            
            File downloadDir = new File(externalSDDir.getAbsolutePath()+CommonValues.DOWNLOAD_FOLDER);
            if (!downloadDir.exists())
            		downloadDir.mkdirs();
            
            String fullApk = downloadDir.getAbsoluteFile()+"/"+fileZipName;
            File fZip = new File(fullApk);
            if (fZip.exists()){
            	fZip.delete();
            }
            output = new FileOutputStream(fullApk);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
        		output.flush();

            }

            
            if (downloadListener != null)
            {
            	downloadListener.onDownloadFileCompleted(getContext(),this, 
            			fullApk, 
            			false, 
            			"Successful");
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
        	if (downloadListener != null)
            {
              downloadListener.onDownloadFileCompleted(getContext(), this,
            		  "", 
            		  true, 
            		  e.getMessage());
            }
        }
        finally{
        	//if (mProgressDialog != null)
        	//	mProgressDialog.dismiss();

        	if (output != null){
        		try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        return null;
    }
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mProgressDialog.show();
        this.displayProgress("Downloading");
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }
}

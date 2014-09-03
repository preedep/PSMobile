package com.epro.psmobile.sync;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.epro.psmobile.util.CommonValues;

public class DownloadManager {

	public DownloadManager() {
		// TODO Auto-generated constructor stub
	}

	public static String download(
			Activity activity,
			BaseAsyncTask<?, ?, ?> asyncTask,
			String urlLink,
			String zipFile,
			OnDownloadFileListener downloadListener,
			OnDownloadProgress downloadProgress)
	{
		OutputStream output = null;
    	InputStream input = null;
    	URLConnection connection = null;
    	//Exception ex = null;
    	//boolean bSuccessful = false;
        try {
        	Log.d("DEBUG", "url downloading = "+urlLink);
        	
            URL url = new URL(urlLink);
            String fileZipName = zipFile;
            
            connection = url.openConnection();
            connection.setConnectTimeout(DownloadFileAsyncTask.TIME_OUT);
            connection.setReadTimeout(DownloadFileAsyncTask.TIME_OUT);
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
                //publishProgress((int) (total * 100 / fileLength));
                if (downloadProgress != null)
                {
                	downloadProgress.onPublisProgress((int) (total * 100 / fileLength));
                }
                output.write(data, 0, count);
        		output.flush();

            }

            
            if (downloadListener != null)
            {
            	downloadListener.onDownloadFileCompleted(activity,
            			asyncTask, 
            			fullApk, 
            			false, 
            			"Successful");
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
        	if (downloadListener != null)
            {
              downloadListener.onDownloadFileCompleted(activity,
            		  asyncTask,
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
}

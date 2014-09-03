/**
 * 
 */
package com.epro.psmobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import com.epro.psmobile.util.RemoteServiceConfig;
import com.epro.psmobile.util.SharedPreferenceUtil;
import com.epro.psmobile.util.SysInfoGetter;

import dalvik.system.DexClassLoader;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;

/**
 * @author preedeeponchevin
 *
 */
public class PSMobileApplication extends Application {

	/**
	 * 
	 */
	public PSMobileApplication() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        //dexTool();

		/*
		 * initial remote service config
		 */
		try {
			String serverUrl = RemoteServiceConfig.getServerActived(getApplicationContext());
			if (serverUrl != null)
			{
				SharedPreferenceUtil.saveServiceUrl(getApplicationContext(), serverUrl);
				
				/*
				 */
				String textVersion = "version : "+RemoteServiceConfig.VERSION_NAME+ "("+RemoteServiceConfig.SERVICE_NAME+")";
				textVersion += "\r\n";
				textVersion += "version code : "+""+SysInfoGetter.getAppVersionCode(this)+"";
				SharedPreferenceUtil.saveTextVersion(getApplicationContext(), textVersion);
				
				/*
				 SharedPreferenceUtil.setAlreadyCommentSaved(getActivity(),true);
                 SharedPreferenceUtil.saveCarInspectDataModified(getActivity(),false);
                 SharedPreferenceUtil.saveLayoutModified(getActivity(), false);
				*/
				SharedPreferenceUtil.saveCarInspectDataModified(getApplicationContext(), false);
				SharedPreferenceUtil.setAlreadyCommentSaved(getApplicationContext(), true);
				SharedPreferenceUtil.saveLayoutModified(getApplicationContext(), false);
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * https://github.com/mmin18/Dex65536
	 * */
	
	/**
     * Copy the following code and call dexTool() after super.onCreate() in
     * Application.onCreate()
     * <p>
     * This method hacks the default PathClassLoader and load the secondary dex
     * file as it's parent.
     */
    @SuppressLint("NewApi")
    private void dexTool() {

        File dexDir = new File(getFilesDir(), "dlibs");
        dexDir.mkdir();
        File dexFile = new File(dexDir, "libs.apk");
        File dexOpt = new File(dexDir, "opt");
        dexOpt.mkdir();
        try {
            InputStream ins = getAssets().open("libs.apk");
            if (dexFile.length() != ins.available()) {
                FileOutputStream fos = new FileOutputStream(dexFile);
                byte[] buf = new byte[4096];
                int l;
                while ((l = ins.read(buf)) != -1) {
                    fos.write(buf, 0, l);
                }
                fos.close();
            }
            ins.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ClassLoader cl = getClassLoader();
        ApplicationInfo ai = getApplicationInfo();
        String nativeLibraryDir = null;
        if (Build.VERSION.SDK_INT > 8) {
            nativeLibraryDir = ai.nativeLibraryDir;
        } else {
            nativeLibraryDir = "/data/data/" + ai.packageName + "/lib/";
        }
        DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(),
                dexOpt.getAbsolutePath(), nativeLibraryDir, cl.getParent());

        try {
            Field f = ClassLoader.class.getDeclaredField("parent");
            f.setAccessible(true);
            f.set(cl, dcl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

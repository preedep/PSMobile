package com.epro.psmobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class AppFolderUtil {

	public AppFolderUtil() {
		// TODO Auto-generated constructor stub
	}

//	public static void createFolderTeamCheckInPhotos()
	public static void copy(File src, File dst) throws IOException {
	    InputStream in = null;//new FileInputStream(src);
	    OutputStream out = null;//new FileOutputStream(dst);

	    try{
		 in = new FileInputStream(src);
		 out = new FileOutputStream(dst);
	    	
	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    }finally{
	    	if (in != null)
	    		in.close();
	    	if (out != null)
	    		out.close();
	    }
	}
	
	private static void deleteDirectory( File dir )
	{

	    if ( dir.isDirectory() )
	    {
	        String [] children = dir.list();
	        for ( int i = 0 ; i < children.length ; i ++ )
	        {
	         File child =    new File( dir , children[i] );
	         if(child.isDirectory()){
	             deleteDirectory( child );
	             child.delete();
	         }else{
	             child.delete();

	         }
	        }
	        dir.delete();
	    }
	}
	
	public static void deleteRecursive(File dir)
	{
	    Log.d("DEBUG_D", "DELETEPREVIOUS TOP " + dir.getPath()+" \r\n----------------------------\r\n");
	    if (dir.isDirectory())
	    {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++)
	        {
	            File temp = new File(dir, children[i]);
	            if (temp.isDirectory())
	            {
	                Log.d("DEBUG_D", "Recursive Call" + temp.getPath());
	                deleteRecursive(temp);
	            }
	            else
	            {
	                Log.d("DEBUG_D", "Delete File" + temp.getPath());
	                boolean b = temp.delete();
	                if (b == false)
	                {
	                    Log.d("DEBUG_D", "DELETE FAIL");
	                }
	            }
	        }

	    }
	    dir.delete();
	}
}

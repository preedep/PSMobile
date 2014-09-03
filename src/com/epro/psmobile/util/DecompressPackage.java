package com.epro.psmobile.util;

import android.util.Log; 
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream; 
import java.util.List;
import java.util.zip.ZipEntry; 
import java.util.zip.ZipInputStream; 

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import org.apache.commons.io.IOUtils;

public class DecompressPackage {

  private String _zipFile; 
  private String _location; 
 
  public DecompressPackage(String zipFile, String location) { 
    _zipFile = zipFile; 
    _location = location; 
 
    _dirChecker(""); 
  } 
  
  public String upzipWithPassword(String password) throws Exception
  {
	  String unzipFileName = "";
	  
	  // Initiate ZipFile object with the path/name of the zip file.
		ZipFile zipFile = new ZipFile(_zipFile);
			
	  // Check to see if the zip file is password protected 
		if (zipFile.isEncrypted()) {
			// if yes, then set the password for the zip file
			zipFile.setPassword(password);
		}

		// Get the list of file headers from the zip file
		List fileHeaderList = zipFile.getFileHeaders();
			
			// Loop through the file headers
		for (int i = 0; i < fileHeaderList.size(); i++) {
			FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
			// Extract the file to the specified destination
			zipFile.extractFile(fileHeader, _location);
	  }
			
	  return unzipFileName;
  }
  public String unzip() 
  throws Exception
  { 
	  ZipInputStream zin = null;
	  String unzipFileName = "";
    try  { 
      FileInputStream fin = new FileInputStream(_zipFile); 
      zin = new ZipInputStream(fin); 
      ZipEntry ze = null; 
      while ((ze = zin.getNextEntry()) != null) { 
        Log.v("Decompress", "Unzipping " + ze.getName()); 
 
        if(ze.isDirectory()) { 
          _dirChecker(ze.getName()); 
        } else { 
          String lFileName = ze.getName();
          
           int idx = ze.getName().lastIndexOf("/");
           if (idx >= 0)
        		  lFileName = ze.getName().substring(idx, ze.getName().length());
          
           unzipFileName = _location +"/"+ lFileName;//ze.getName();
          FileOutputStream fout = new FileOutputStream(unzipFileName); 
          
          /*
           *    byte[] buffer = new byte[1024];
         		int count;
         			while ((count = zis.read(buffer)) != -1) {
             		baos.write(buffer, 0, count);
         		}
           */
          /*
          for (int c = zin.read(); c != -1; c = zin.read()) { 
            fout.write(c); 
          } */
          IOUtils.copy(zin, fout);
 
          zin.closeEntry(); 
          fout.close(); 
        }          
      } 
    } catch(Exception e) { 
      Log.e("Decompress", "unzip", e); 
      throw e;
    } 
    finally{
    	if (zin != null)
    		zin.close();
    }
    return unzipFileName;
  } 
 
  private void _dirChecker(String dir) { 
    File f = new File(_location + dir); 
 
    if(!f.isDirectory()) { 
      f.mkdirs(); 
    } 
  } 
} 
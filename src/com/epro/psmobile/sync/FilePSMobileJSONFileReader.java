package com.epro.psmobile.sync;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;

public class FilePSMobileJSONFileReader {

	public FilePSMobileJSONFileReader() {
		// TODO Auto-generated constructor stub
	}

	public static <T> ArrayList<T> readFile(String fileName,
			Class<T> type) throws IOException,
			JSONException, 
			InstantiationException, 
			IllegalAccessException
	{
		ArrayList<T> dataList = new ArrayList<T>();
		
		FileInputStream stream = null;
        String jString = null;
        try {
        	stream = new FileInputStream(fileName);
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
//            Charset charSet = Charset.defaultCharset();
//            boolean isUTF8Supported = Charset.isSupported("UTF-8");
//            jString = Charset.defaultCharset().decode(bb).toString();
            jString = Charset.forName("UTF-8").decode(bb).toString();
                      
            if (jString != null)
            {
            	JSONObject jsonObject = new JSONObject(jString);            	
            	JSONArray arrays = jsonObject.getJSONArray("data");
            	for(int i = 0; i < arrays.length();i++)
            	{
            		JSONObject jsonItem = arrays.getJSONObject(i);
            		T t_obj = type.newInstance();
            		if (t_obj instanceof JSONDataHolder)
            		{
            			JSONDataHolder dataHolder = (JSONDataHolder)t_obj;
            			dataHolder.onJSONDataBind(jsonItem);
            			
            			dataList.add(t_obj);
            		}
            	}
            }
          }
          finally {
        	  if (stream != null)
        		  stream.close();
          }
		return dataList;
	}
}

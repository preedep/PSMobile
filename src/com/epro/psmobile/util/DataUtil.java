package com.epro.psmobile.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.epro.psmobile.da.PSBOManager;
import com.epro.psmobile.data.Task;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

public class DataUtil {

	public DataUtil() {
		// TODO Auto-generated constructor stub
	}
    public static String convertToDisplayCarInspectDateFormat(String strDate){
	      if ((strDate != null)&&(strDate.trim().length() > 0)){
	         String strDateSplits[] = strDate.split("-");
	         return strDateSplits[2] +" / " + strDateSplits[1] + "/" + strDateSplits[0].substring(2);
	      }
	      return "";
	   }

	public static String getInspectPeriod(boolean showInPlan , Task task)
	{
		String periodInspect = "";
		
		Calendar cal = Calendar.getInstance();
		Date to_day = cal.getTime();

		String forceTime = task.getForceTime();
		if (showInPlan)
		{
			/*show in plan*/
			if (forceTime.equalsIgnoreCase("Y"))
			{
				periodInspect = 
						DataUtil.convertSlashDateToStringDDMMYYYY(task.getTaskDate());
			}else{
				periodInspect = 
						DataUtil.convertSlashDateToStringDDMMYYYY(to_day) + " - " + DataUtil.convertSlashDateToStringDDMMYYYY(task.getTaskScheduleDate());
			}
		}else
		{
			/*show in task*/
			periodInspect = 
					DataUtil.convertSlashDateToStringDDMMYYYY(task.getTaskDate());
					
		}
		return periodInspect;
	}
	public static boolean convertToBoolean(String strBoolean)
	{
		if ((strBoolean != null)&&(strBoolean.trim().length() > 0)){
			if (strBoolean.equalsIgnoreCase("true"))
				return true;
			else
				return false;
		}
		else{
			return false;
		}
	}
	public static String regenerateTaskCodeForMakeFolder(String taskCode)
	{
		return taskCode.replace("/", "-");
	}
	public static String replaceSingleBackSlash(String filePath)
	{
		String dummy =  filePath.replaceAll("///", "/");
		return dummy.replaceAll("//", "/");
	}
	public static void intialStringArray(String[] strArrays,String initialWithString)
	{
		for(int i = 0; i < strArrays.length;i++)
			strArrays[i] = initialWithString;
	}
	public static Date convertToDateYYYYMMDD(String dateString)
	{
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date convertedDate = null;
		 try {
		        convertedDate = dateFormat.parse(dateString);
		 }catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
		 return convertedDate;
   }
	
	public static Date convertToDateDDMMYYYY(String dateString)
	{
		 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		 Date convertedDate = null;
		 try {
		        convertedDate = dateFormat.parse(dateString);
		 }catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
		 return convertedDate;
   }

	public static Date convertToDate(String dateString)
	{
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date convertedDate = null;
		 try {
		        convertedDate = dateFormat.parse(dateString);
		 }catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
		 return convertedDate;
   }
   public static String convertTimestampToStringHHmmss(java.sql.Timestamp timeStamp)
   {
		   if (timeStamp == null)
			   return "";
		   
		   DateFormat dateFormatISO8601 = new SimpleDateFormat("HH:mm:ss");
		   return dateFormatISO8601.format(timeStamp);
   }
   public static String convertTimestampToStringYYYMMDDHHmmss(java.sql.Timestamp timeStamp)
   {
	   if (timeStamp == null)
		   return "";
	   
	   DateFormat dateFormatISO8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   return dateFormatISO8601.format(timeStamp);
   }
   public static java.sql.Timestamp convertStringToTimestampYYYYMMDDHHmmss(String strTimestamp)
   {
	   DateFormat dateFormatISO8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   try {
		return new java.sql.Timestamp(dateFormatISO8601.parse(strTimestamp).getTime());
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	   
	 return null;
   }
   
   public static String universalDateToString(String format,Date date){
      if (format.isEmpty()){
         format = "dd-MM-yyyy";
      }
      DateFormat df = new SimpleDateFormat(format);
      return df.format(date);

   }
   public static String convertDateToStringYYYYMMDD(Date date)
   {
	   //2013-07-22
		   DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		   return df.format(date);
   }
   public static String convertDateToStringDDMMYYYY(Date date)
   {
	   DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	   return df.format(date);
   }
   public static String convertSlashDateToStringDDMMYYYY(Date date)
   {
	   DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	   return df.format(date);
   }

   /*
   public static void installDBTest(Context ctxt,int dbVersion)
   {
	   String dbVersionKeyName = "com.epro.psmobile.pf.dbversion";
	   SharedPreferences prefs = ctxt.getSharedPreferences(
			      "com.epro.psmobile.pf", Context.MODE_PRIVATE);
	   int savedDbVersion = prefs.getInt(dbVersionKeyName, -1);
	   if (dbVersion != savedDbVersion){
		   //install new data
		   PSBOManager dbMgn = new PSBOManager(ctxt);
		   try{
			   //dbMgn.loadDataTest();
			   Editor editor = prefs.edit();
			   editor.putInt(dbVersionKeyName, dbVersion);
			   editor.commit();
		   }catch(Exception ex)
		   {
			   Log.d("DEBUG_D", ex.getMessage());
		   }
	   }
   }*/
   public static ArrayList<String> textSplitByComma(String texts)
   {
	   ArrayList<String> textList = new ArrayList<String>();
	   String[] datas = texts.split("@@");
	   for(String data : datas){
		   if (data.endsWith("@")){
			   data = data.substring(0, data.length()-1);
		   }
		   textList.add(data);
	   }
	   return textList;
   }
   
   public static String convertObjectToString(Object obj)
   throws Exception
   {
	   String strObj = "";
	   
	   ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

	    ObjectOutputStream objectOutput;
	    objectOutput = new ObjectOutputStream(arrayOutputStream);
	    objectOutput.writeObject(obj);
	    byte[] data = arrayOutputStream.toByteArray();
	    objectOutput.close();
	    arrayOutputStream.close();

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
	    b64.write(data);
	    b64.close();
	    out.close();

	    strObj =  new String(out.toByteArray());
	   
	   return strObj;
   }
   
   public static Calendar getDatePart(Date date){
	    Calendar cal = Calendar.getInstance();       // get calendar instance
	    cal.setTime(date);      
	    cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
	    cal.set(Calendar.MINUTE, 0);                 // set minute in hour
	    cal.set(Calendar.SECOND, 0);                 // set second in minute
	    cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

	    return cal;                                  // return the date part
	}
   
   /**
    * This method also assumes endDate >= startDate
   **/
   public static long daysBetween(Date startDate, Date endDate) {
     Calendar sDate = getDatePart(startDate);
     Calendar eDate = getDatePart(endDate);

     long daysBetween = 0;
     while (sDate.before(eDate)) {
         sDate.add(Calendar.DAY_OF_MONTH, 1);
         daysBetween++;
     }
     return daysBetween;
   }
   
   public static String numberFormat(double value)
   {
	   DecimalFormat formatter = new DecimalFormat("#,##0.00");
	   return formatter.format(value);
   }
   public static String decimal2digiFormat(double value)
   {
	   DecimalFormat formatter = new DecimalFormat("#,##0.00");
	   return formatter.format(value);	   
   }
   
   public static double convertToNumber(String textValue)
   {
	   try{
		   return Double.parseDouble(textValue.replace(",",""));
	   }catch(Exception ex){}
	   return 0;
   }
   public static String encryptToMD5(String password) throws Exception
   {
         // Create MD5 Hash
         MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
         digest.update(password.getBytes());
         byte messageDigest[] = digest.digest();
     
         StringBuffer MD5Hash = new StringBuffer();
         for (int i = 0; i < messageDigest.length; i++)
         {
               String h = Integer.toHexString(0xFF & messageDigest[i]);
               while (h.length() < 2)
                   h = "0" + h;
               MD5Hash.append(h);
         }          
        return MD5Hash.toString();
   }
   public static Date getZeroTimeDate(Date fecha) {
	    Date res = fecha;
	    Calendar calendar = Calendar.getInstance();

	    calendar.setTime( fecha );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    res = calendar.getTime();

	    return res;
	}
   public static String padZero(int value)
   {
	   String strValue = "";
	   if (value < 10){
		   strValue = "0"+value;
	   }else{
		   strValue = value+"";
	   }
	   return strValue;
   }
   public static String removePID(String text)
   {
	   String textRemoved = "";
	   String keyword = "(pid:";
	   int idxOfPid = text.indexOf(keyword);
		if (idxOfPid >= 0)
		{
			String head = text.substring(0,idxOfPid);
			
			String tail = text.substring(idxOfPid+(keyword.length()), text.length());

			tail = tail.substring(tail.indexOf(")")+1 , tail.length());

			textRemoved = head + tail;
		}else{
			textRemoved = text;
		}
	   return textRemoved;
   }
}

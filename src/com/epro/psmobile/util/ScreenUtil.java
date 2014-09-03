package com.epro.psmobile.util;

import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.LayoutItemScaleHistory;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {

	private static LayoutItemScaleHistory scale = null;
	private static String currentTaskCode = "";
	private static int customerSurveySiteID = -1;
	
	public static class DeviceScreen
	{
		public int width;
		public int height;
	};
	
	public static class SizeInPixel
	{
		public int widthPixel = 1;
		public int heightPixel = 1;
	};
	
	public static class PixelInSize{
		public double sizeWidth;
		public double sizeLong;
	};
	public ScreenUtil() {
		// TODO Auto-generated constructor stub
	}
	/*
	public final static int MIN_OBJ_WIDTH_DISPLAY_DP = 120;
	public final static int MIN_OBJ_HEIGHT_DISPLAY_DP = 120;
	public final static int MAX_OBJ_WIDTH_DISPLAY_DP = 1000;
	public final static int MAX_OBJ_HEIGHT_DISPLAY_DP = 1000;
	
	public final static int MIN_REAL_OBJ_WIDTH = 5;
	public final static int MIN_REAL_OBJ_HEIGHT = 5;
	public final static int MAX_REAL_OBJ_WIDTH = 100;
	public final static int MAX_REAL_OBJ_HEIGHT = 100;
	
	public final static double MULTIPLIER = 10;
	*/
	public final static int MIN_OBJ_WIDTH_DISPLAY_DP = 120;
	public final static int MIN_OBJ_HEIGHT_DISPLAY_DP = 120;
	public final static int MAX_OBJ_WIDTH_DISPLAY_DP = 2651;
	public final static int MAX_OBJ_HEIGHT_DISPLAY_DP = 3750;
	
	public final static int MIN_REAL_OBJ_WIDTH = 5;/* .m */
	public final static int MIN_REAL_OBJ_HEIGHT = 5;
	public final static int MAX_REAL_OBJ_WIDTH = 1000;
	public final static int MAX_REAL_OBJ_HEIGHT = 1000;
	
	public final static double MULTIPLIER_WIDTH = (((double)MAX_OBJ_WIDTH_DISPLAY_DP)/MAX_REAL_OBJ_WIDTH)*5;
	public final static double MULTIPLIER_HEIGHT = MULTIPLIER_WIDTH;//(((double)MAX_OBJ_HEIGHT_DISPLAY_DP)/MAX_REAL_OBJ_HEIGHT)*5;

	public static PixelInSize convertPixelToRealSize(Context context,
			InspectDataObjectSaved dataSaved,
			float pixelWidth,float pixelHeight)
	{
		PixelInSize pixelInSize = new PixelInSize();
		
		//float pWidth = convertDpToPixel(MAX_OBJ_WIDTH_DISPLAY_DP,context);
		//float pHeight = convertDpToPixel(MAX_OBJ_HEIGHT_DISPLAY_DP,context);
		
	    float dpWidth = pixelWidth;//convertPixelsToDp(pixelWidth,context);
		float dpHeight = pixelHeight;//convertPixelsToDp(pixelHeight,context);

		if ((dpWidth >= MAX_OBJ_WIDTH_DISPLAY_DP) || (dpWidth <= MIN_OBJ_WIDTH_DISPLAY_DP)){
			pixelInSize.sizeWidth = dataSaved.getWidth();
			if (pixelInSize.sizeWidth == 0)
				pixelInSize.sizeWidth = MIN_REAL_OBJ_WIDTH;
		}
		if ((dpHeight >= MAX_OBJ_HEIGHT_DISPLAY_DP) || (dpHeight <= MIN_OBJ_HEIGHT_DISPLAY_DP))
		{
			pixelInSize.sizeLong = dataSaved.getdLong();
			if (pixelInSize.sizeLong == 0)
				pixelInSize.sizeLong = MIN_REAL_OBJ_HEIGHT;
		}
		if ((dpWidth > MIN_OBJ_WIDTH_DISPLAY_DP)&&(dpWidth < MAX_OBJ_WIDTH_DISPLAY_DP)){
			//float real_width = (float) (MIN_REAL_OBJ_WIDTH + ((dpWidth - MIN_OBJ_WIDTH_DISPLAY_DP) / MULTIPLIER_WIDTH));			
			float real_width = (float) Math.round(((dpWidth) / MULTIPLIER_WIDTH));			

			pixelInSize.sizeWidth = real_width;
		}
		if ((dpHeight > MIN_OBJ_HEIGHT_DISPLAY_DP)&&(dpHeight <  MAX_OBJ_HEIGHT_DISPLAY_DP)){
//			float real_height = (float) (MIN_REAL_OBJ_HEIGHT + ((dpHeight - MIN_OBJ_HEIGHT_DISPLAY_DP)/ MULTIPLIER_HEIGHT));		
			float real_height = (float)Math.round(((dpHeight) / MULTIPLIER_HEIGHT));
			pixelInSize.sizeLong = real_height;
		}
		Log.d("DEBUG_D", "convertPixelToRealSize -> "+pixelInSize.sizeWidth+" , "+pixelInSize.sizeLong);
		return pixelInSize;
	}
	public static SizeInPixel calcObjectSizeByScale(Context context,
												 double realObjWidth,
												 double realObjHeight)
	{
		SizeInPixel sizeInPixel = new SizeInPixel();
		if ((realObjWidth <= MIN_REAL_OBJ_WIDTH) || (realObjHeight <= MIN_REAL_OBJ_HEIGHT))
		{
			if (realObjWidth <= MIN_REAL_OBJ_WIDTH){
				sizeInPixel.widthPixel = MIN_OBJ_WIDTH_DISPLAY_DP;
			}
			if (realObjHeight <= MIN_REAL_OBJ_HEIGHT){
				sizeInPixel.heightPixel = MIN_OBJ_HEIGHT_DISPLAY_DP;
			}
		}
		if ((realObjWidth >= MAX_REAL_OBJ_WIDTH) || (realObjHeight >= MAX_REAL_OBJ_HEIGHT))
		{
			if (realObjWidth >= MAX_REAL_OBJ_WIDTH){
				sizeInPixel.widthPixel = MAX_OBJ_WIDTH_DISPLAY_DP;
			}
			if (realObjHeight >= MAX_REAL_OBJ_HEIGHT){
				sizeInPixel.heightPixel = MAX_OBJ_HEIGHT_DISPLAY_DP;
			}
		}
		if ((realObjWidth > MIN_REAL_OBJ_WIDTH)&&(realObjWidth < MAX_REAL_OBJ_WIDTH))
		{
			/*
			sizeInPixel.widthPixel = (int) Math.round((realObjWidth - MIN_REAL_OBJ_WIDTH) * MULTIPLIER_WIDTH);
			sizeInPixel.widthPixel += MIN_OBJ_WIDTH_DISPLAY_DP;
			if (sizeInPixel.widthPixel > MAX_OBJ_WIDTH_DISPLAY_DP)
				sizeInPixel.widthPixel = MAX_OBJ_WIDTH_DISPLAY_DP;
				*/
			sizeInPixel.widthPixel = (int) Math.round((realObjWidth) * MULTIPLIER_WIDTH);
			if (sizeInPixel.widthPixel > MAX_OBJ_WIDTH_DISPLAY_DP)
				sizeInPixel.widthPixel = MAX_OBJ_WIDTH_DISPLAY_DP;
			else if (sizeInPixel.widthPixel < MIN_OBJ_WIDTH_DISPLAY_DP)
				sizeInPixel.widthPixel = MIN_OBJ_WIDTH_DISPLAY_DP;
			

		}
		if ((realObjHeight > MIN_REAL_OBJ_HEIGHT)&&(realObjHeight < MAX_REAL_OBJ_HEIGHT))
		{
//			sizeInPixel.heightPixel = (int) Math.round((realObjHeight - MIN_REAL_OBJ_HEIGHT) * MULTIPLIER_HEIGHT);
			sizeInPixel.heightPixel = (int)Math.round((realObjHeight)*MULTIPLIER_HEIGHT);
//			sizeInPixel.heightPixel += MIN_OBJ_HEIGHT_DISPLAY_DP;
			if (sizeInPixel.heightPixel > MAX_OBJ_HEIGHT_DISPLAY_DP)
				sizeInPixel.heightPixel = MAX_OBJ_HEIGHT_DISPLAY_DP;
			else if (sizeInPixel.heightPixel < MIN_OBJ_HEIGHT_DISPLAY_DP)
				sizeInPixel.heightPixel = MIN_OBJ_HEIGHT_DISPLAY_DP;
		}
		//sizeInPixel.widthPixel = (int) convertDpToPixel(sizeInPixel.widthPixel,context);
		//sizeInPixel.heightPixel = (int)convertDpToPixel(sizeInPixel.heightPixel,context);
		
		Log.d("DEBUG_D", "calcObjectSizeByScale -> "+sizeInPixel.widthPixel+" , "+sizeInPixel.heightPixel);
		return sizeInPixel;
	}
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	
	@SuppressWarnings("unused")
	public static SizeInPixel convertSizeInPixel(
			Activity activity,
			InspectDataObjectSaved dataSaved,
			double width,
			double height)
	{
		SizeInPixel screenInPixel = new SizeInPixel();
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(activity);
		try {
			double widthSite = 0;
			double longSite = 0;

			if (!dataSaved.getTaskCode().equalsIgnoreCase(currentTaskCode) || 
				(dataSaved.getCustomerSurveySiteID() != customerSurveySiteID)){
				scale = 
						dataAdapter.getLayoutScale(dataSaved.getTaskCode(), dataSaved.getCustomerSurveySiteID());
				currentTaskCode = dataSaved.getTaskCode();
				customerSurveySiteID = dataSaved.getCustomerSurveySiteID();
			}
			DeviceScreen deviceScreen = getDeviceScreen(activity);

			deviceScreen.height = deviceScreen.width;
			if (scale != null)
			{
				widthSite = scale.getSiteWidth();
				longSite = scale.getSiteLong();
			}else{
				widthSite = deviceScreen.width;
				longSite = deviceScreen.height;
			}
			
			screenInPixel.widthPixel = (int)Math.round(((deviceScreen.width / widthSite)*width));
			screenInPixel.heightPixel = (int)Math.round(((deviceScreen.height / longSite)*height));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return screenInPixel;
	}
	public static PixelInSize convertPixelToSize(
			Activity activity,
			InspectDataObjectSaved dataSaved,
			double pixelWidth,
			double pixelHeight)
	{
		PixelInSize pixelToSize = new PixelInSize();
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(activity);
		try {
			double widthSite = 0;
			double longSite = 0;
			
			if (!dataSaved.getTaskCode().equalsIgnoreCase(currentTaskCode) || 
					(dataSaved.getCustomerSurveySiteID() != customerSurveySiteID)){
					scale = 
							dataAdapter.getLayoutScale(dataSaved.getTaskCode(), dataSaved.getCustomerSurveySiteID());
					currentTaskCode = dataSaved.getTaskCode();
					customerSurveySiteID = dataSaved.getCustomerSurveySiteID();
				}

			DeviceScreen deviceScreen = getDeviceScreen(activity);
			deviceScreen.height = deviceScreen.width;

			if (scale != null)
			{
				widthSite = scale.getSiteWidth();
				longSite = scale.getSiteLong();
			}else{
				widthSite = deviceScreen.width;
				longSite = deviceScreen.height;
			}
			
			//screenInPixel.widthPixel = (int)((deviceScreen.width / widthSite)*width);
			//screenInPixel.heightPixel = (int)((deviceScreen.height / longSite)*height);
			pixelToSize.sizeWidth = Math.round(((widthSite/deviceScreen.width)*pixelWidth));
			pixelToSize.sizeLong = Math.round(((longSite/deviceScreen.height)*pixelHeight));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pixelToSize;
	}

	public static DeviceScreen getDeviceScreen(Activity context)
	{
		DeviceScreen deviceScreen = new DeviceScreen();
		int Measuredwidth = 0;  
		int Measuredheight = 0;  
		Point size = new Point();
		WindowManager w = context.getWindowManager();

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
		    w.getDefaultDisplay().getSize(size);
		    Measuredwidth = size.x;
		    Measuredheight = size.y; 
		}else{
		    Display d = w.getDefaultDisplay(); 
		    Measuredwidth = d.getWidth(); 
		    Measuredheight = d.getHeight(); 
		}
		deviceScreen.width = Measuredwidth;
		deviceScreen.height = Measuredheight;
		return deviceScreen;
	}
}

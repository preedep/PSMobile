package com.epro.psmobile.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class FontUtil {
	   public enum FontName
	   {
		   THSARABUN_BOLD("THSarabun Bold.ttf"),
		   THSARABUN_BOLDITALIC("THSarabun BoldItalic.ttf"),
		   THSARABUN_ITALIC("THSarabun Italic.ttf"),
		   THSARABUN("THSarabun.ttf");
		  
		   
		   private String fontName;
		   FontName(String fontName)
		   {
			   this.fontName = fontName;
		   }		   
		   public String getFontName(){
			   return "fonts/"+this.fontName;
		   }
		   public String getShortFontName(){
			   return this.fontName;
		   }
	   }
	   public static void replaceFontTextView(final Context context,
			   final View v,FontName fontName)
	   {
		   if (v instanceof TextView)
		   {
			   TextView tv = (TextView)v;
			   Typeface tf = Typeface.createFromAsset(context.getAssets(),fontName.getFontName());        
			   tv.setTypeface(tf);    	
		   }

	   }
}

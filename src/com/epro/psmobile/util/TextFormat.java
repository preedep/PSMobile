package com.epro.psmobile.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.Time;

public class TextFormat {

	public TextFormat() {
		// TODO Auto-generated constructor stub
	}
	public static String convertDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}
	public static String convertTime(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(date.getTime());
		return formattedDate;
	}
}

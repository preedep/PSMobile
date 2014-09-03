package com.epro.psmobile.util;

import com.epro.psmobile.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

public class MessageBox 
{
	public enum MessageConfirmType
	{
		OK,
		CANCEL
	};
	
	public interface MessageConfirmListener{
		void onConfirmed(MessageConfirmType confirmType);
	}
	public static void showMessage(Context ctxt,int titleResID,int messageResId)
	{
		if (ctxt == null)
		{
			Log.d("DEBUG_D", "print showMessage : Context is null");
			return;
		}
		showMessage(ctxt,
				ctxt.getResources().getString(titleResID),
				ctxt.getResources().getString(messageResId));
	}
	public static void showMessage(Context ctxt,int resID,String message)
	{
		if (ctxt == null)
		{
			Log.d("DEBUG_D", "print showMessage : "+message);
			return;
		}
		showMessage(ctxt,ctxt.getResources().getString(resID),message);
	}
	public static void showMessage(final Context ctxt,
			final String title,
			final String message)
	{
		Activity activity = null;
		if (ctxt instanceof Activity)
		{
			activity = (Activity)ctxt;
			activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(ctxt)
					.setTitle(title)
					.setMessage(message)
					.setPositiveButton("OK", new OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
				        // Some stuff to do when ok got clicked
						}
					})
				   .show();

				}
				
			});
		}else{		
			new AlertDialog.Builder(ctxt)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton("OK", new OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
		        // Some stuff to do when ok got clicked
				}
			})
		   .show();
		}
	}
	public static void showMessageWithConfirm(final Context ctxt,
          final String title,
          final String message,
          final MessageConfirmListener messageConfirmListener)
	{
	   showMessageWithConfirm(ctxt,title,message,messageConfirmListener,false);
	}
	public static void showMessageWithConfirm(final Context ctxt,
			final String title,
			final String message,
			final MessageConfirmListener messageConfirmListener,final boolean singleButton)
	{
		Activity activity = null;
		if (ctxt instanceof Activity)
		{
			activity = (Activity)ctxt;
			activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
				   AlertDialog.Builder bld = new AlertDialog.Builder(ctxt)
					.setTitle(title)
					.setMessage(message)
					.setPositiveButton(R.string.label_inspect_data_entry_btn_save, new OnClickListener() {
					    public void onClick(DialogInterface arg0, int arg1) {
					        // Some stuff to do when ok got clicked
					    	if (messageConfirmListener != null)
					    	{
					    		messageConfirmListener.onConfirmed(MessageConfirmType.OK);
					    	}
					    }
					});
					if (!singleButton){
					  bld.setNegativeButton(R.string.label_inspect_data_entry_btn_cancel, new OnClickListener(){

	                        @Override
	                        public void onClick(DialogInterface arg0, int arg1) {
	                            // TODO Auto-generated method stub
	                            if (messageConfirmListener != null)
	                            {
	                                messageConfirmListener.onConfirmed(MessageConfirmType.CANCEL);
	                            }
	                        }
	                        
	                    });
					}
					
					bld.show();
				}
				
			});
			
			
		}else{
		new AlertDialog.Builder(ctxt)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("OK", new OnClickListener() {
		    public void onClick(DialogInterface arg0, int arg1) {
		        // Some stuff to do when ok got clicked
		    	if (messageConfirmListener != null)
		    	{
		    		messageConfirmListener.onConfirmed(MessageConfirmType.OK);
		    	}
		    }
		}).setNegativeButton("Cancel", new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if (messageConfirmListener != null)
		    	{
		    		messageConfirmListener.onConfirmed(MessageConfirmType.CANCEL);
		    	}
			}
			
		})
		.show();
		}
	}
	public static void showSaveCompleteMessage(Context ctxt){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
		builder.setMessage(R.string.label_save_complete)
		       .setCancelable(false)
		       .setPositiveButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		        	   dialog.dismiss();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}

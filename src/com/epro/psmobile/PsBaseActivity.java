package com.epro.psmobile;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.epro.psmobile.da.PSBOManager;
import com.epro.psmobile.dialog.TeamLoginDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class PsBaseActivity extends SherlockFragmentActivity {

	private TeamLoginDialog login;
	private PSBOManager psboMgn;
	
	public PsBaseActivity() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (psboMgn == null)
		{
			psboMgn = new PSBOManager(this);
			psboMgn.initialMastertableIfNotExist();
		}
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
	   
	   try{
	      LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);;
	        boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	        if (!gpsEnabled)
	        {
	            /*
	             * start setting for set
	             */
	            //Toast.makeText(this, "GPS disabled , please open gps ",1000).show();
	             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	                alertDialogBuilder.setMessage(R.string.text_alert_gps_not_open)
	                .setCancelable(false)
	                .setPositiveButton(R.string.text_btn_gps_setting,
	                        new DialogInterface.OnClickListener(){
	                    public void onClick(DialogInterface dialog, int id){
	                        Intent callGPSSettingIntent = new Intent(
	                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                        startActivity(callGPSSettingIntent);
	                    }
	                });
	                AlertDialog alert = alertDialogBuilder.create();
	                alert.show();
	                
	        }else{
	        
	            if (!this.getPSBOManager().chkLoginAlready())
	            {
	                if (login == null){
	                    login =  new TeamLoginDialog();
	                    login.setCancelable(false);
	                }
	                if (!login.isVisible())
	                {
	                    login.show(getSupportFragmentManager(),TeamLoginDialog.class.getName());            
	                }
	            }
	        }

	   }catch(Exception ex){
	      ex.printStackTrace();
	   }
		super.onResume();
	}

	protected PSBOManager getPSBOManager()
	{
		return psboMgn;
	}
	protected TeamLoginDialog getLogin(){
		return login;
	}
}

package com.epro.psmobile.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.SharedPreferenceUtil;

public abstract class ContentViewBaseFragment extends SherlockFragment  {

	
	public interface BoardcastLocationListener
	{
		void onBoardcastLocationUpdated(Location currentLocation);
	}
	public enum InspectOptMenuType
	{
		NONE,
		INSPECT_LOCATION,
		ENTRY_FORM,
		ENTRY_FORM_AND_ZOOM
	};

	private InspectOptMenuType inspectOptionMenuType = InspectOptMenuType.NONE;
	
	private Location currentLocation;
	private BoardcastLocationListener boardcastLocationListener;
	
	protected BroadcastReceiver mLocationReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			currentLocation = arg1.getParcelableExtra(InstanceStateKey.KEY_BDX_LOC_UPDATED);
			
			if (currentLocation != null){
				Log.d("DEBUG_D", "BroadcastReceiver:onReceive -> " +
						"lat = "+currentLocation.getLatitude()+" , lon = "+currentLocation.getLongitude());
				
				if (getBoardcastLocationListener() != null)
				{
					getBoardcastLocationListener().onBoardcastLocationUpdated(currentLocation);
				}
			}
		}
		
	};
	public ContentViewBaseFragment() {
		// TODO Auto-generated constructor stub
	}
	protected void registerForCustomContextMenu(View targetView, String title, final String[] items) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(title);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), 
        		R.layout.context_menu_item, items);
        // You can also specify typeface at runtime by overriding the
        // getView(int position, View convertView, ViewGroup parent) method of ArrayAdapter
        // convertView = super.getView(int position, View convertView, ViewGroup parent)
        // TextView textView = (TextView) convertView;
        // textView.setTypeface(tf, style)
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCustomContextMenuItemSelected(items, which);
            }
        };
        builder.setAdapter(arrayAdapter, onClickListener);
        OnLongClickListener onLongClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                builder.show();
                return true;
            }
        };
        targetView.setOnLongClickListener(onLongClickListener);
    }

    protected void onCustomContextMenuItemSelected(String[] items, int which) {
    }
    
    protected void setCustomerInfoTitle(View currentContentView,
    		Task task,
    		JobRequest jobRequest)
    {
    	TextView tvCustomerName = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_name);
		TextView tvCustomerAddress = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_address);
		TextView tvCustomerTels = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_tels);
		TextView tvCustomerInspectType = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_inspect_type);

		tvCustomerName.setText(jobRequest.getCustomerSurveySiteList().get(0).getCustomerName());
		tvCustomerAddress.setText(jobRequest.getCustomerSurveySiteList().get(0).getSiteAddress());
		tvCustomerTels.setText(
				this.getResources().getString(R.string.text_tel_prefix)+"" +
						""+jobRequest.getCustomerSurveySiteList().get(0).getSiteTels());
		
		String textCustomerTaskInfo = this.getResources().getString(R.string.text_inspect_type_prefix)+""+jobRequest.getInspectType().getInspectTypeName();
		textCustomerTaskInfo += "\r\n";
		textCustomerTaskInfo += this.getResources().getString(R.string.txt_task_code, task.getTaskCode());
		tvCustomerInspectType.setText(
				textCustomerTaskInfo
			);
		
    }
	public InspectOptMenuType getInspectOptionMenuType() {
		return inspectOptionMenuType;
	}
	public void setInspectOptionMenuType(InspectOptMenuType inspectOptionMenuType) {
		this.inspectOptionMenuType = inspectOptionMenuType;
	}
	protected void onCreateOptionsMenu(Menu menu, 
	      MenuInflater inflater,
	      InspectOptMenuType inspectOptionMenuType)
	{
		switch(inspectOptionMenuType)
		{
			case INSPECT_LOCATION:
			{
			 	menu.clear();
				inflater.inflate(R.menu.drawing_menu, menu);				
			}break;
			case ENTRY_FORM:{
				menu.clear();
				inflater.inflate(R.menu.option_menu_save_only, menu);
				
				Fragment f = this.getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.content_frag);
				if (f instanceof JobCommentFragment)
				{
	                PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getSherlockActivity());
	                try 
	                {
	                   Team t = dataAdapter.getTeamByDeviceId(SharedPreferenceUtil.getDeviceId(getSherlockActivity()));
	                   if (t.isQCTeam()){
	                      menu.findItem(R.id.menu_general_take_photo).setVisible(true);
	                   }
	                }
	                catch (Exception e) {
	                  // TODO Auto-generated catch block
	                  e.printStackTrace();
	               }
				}
			}break;
			case ENTRY_FORM_AND_ZOOM:
			{
				menu.clear();
				inflater.inflate(R.menu.option_menu_zoom_save_only, menu);
			}break;
			default:{
				super.onCreateOptionsMenu(menu, inflater);
			}break;
		}
	}
	protected void toggleFullScreen(){

		Fragment f = getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.menu_group_frag);
		if (f != null)
		{
			FragmentTransaction ft = 
					getSherlockActivity().getSupportFragmentManager().beginTransaction();
			
			if (f.isVisible()){
				ft.hide(f);
			}else{
				ft.show(f);										
			}
			ft.commit();
		}
	}
	protected PSBODataAdapter getDataAdapter(){
		PSBODataAdapter dbAdapter = PSBODataAdapter.getDataAdapter(getActivity());
		return dbAdapter;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.getActivity().unregisterReceiver(mLocationReceiver);
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter(InstanceStateKey.BDX_LOC_UPDATED_ACTION);
		this.getActivity().registerReceiver(mLocationReceiver, filter);
	}

	public Location getCurrentLocation(){
		return this.currentLocation;
	}
	public BoardcastLocationListener getBoardcastLocationListener() {
		return boardcastLocationListener;
	}
	public void setBoardcastLocationListener(BoardcastLocationListener boardcastLocationListener) {
		this.boardcastLocationListener = boardcastLocationListener;
	}
	
}

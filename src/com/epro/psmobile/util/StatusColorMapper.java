package com.epro.psmobile.util;

import com.epro.psmobile.R;
import com.epro.psmobile.data.TaskStatus;

import android.content.Context;
import android.content.res.Resources;

public class StatusColorMapper {

	public StatusColorMapper() {
		// TODO Auto-generated constructor stub
	}
	public static int getStatusColor(Context context,
			TaskStatus taskStatus,
			boolean bAlreadySync,
			boolean bForceEdit)
	{
		/*
		if (bForceEdit)
			return R.color.task_status_allow_edit;
		 */
		if (bAlreadySync)
			return R.color.task_status_already_sync;
				
		Resources res = context.getResources();
		int resId = 0;
		switch(taskStatus)
		{
			case WAIT_TO_CONFIRM:{
				resId = R.color.task_status_wait_confirm;
			}break;
			case CONFIRM_INSPECT:
			{
				//resId = R.color
				resId = R.color.task_status_confirm_inspect;
			}break;
			case CONFIRMED_FROM_WEB:{
				resId = R.color.task_status_confirmed_from_web;
			}break;
			case CANCEL:{
				resId = R.color.task_status_cancel;
			}break;
			case LOCAL_SAVED:
			{
				resId = R.color.task_status_save_to_device;
			}break;
			case FINISH:{
				resId = R.color.task_status_complete;
			}break;
			case DUPLICATED:{
//				resId = R.color.task_status_is_duplicated;
				resId = R.color.task_status_confirmed_from_web;
			}break;
			case SHIFT:{
				resId = R.color.task_status_post_pone;
			}break;
			case ALLOW_EDIT:{
				resId = R.color.task_status_allow_edit;
			}break;
			/*
			case ALREADY_SYNC:{
				resId = R.color.task_status_already_sync;
			}break;*/
			default:
				resId = R.color.task_status_default;
			break;
		}
	
		return resId;
	}
}

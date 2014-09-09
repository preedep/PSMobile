/**
 * 
 */
package com.epro.psmobile.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Customer;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.ProductInJobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.DataUtil;
import com.epro.psmobile.util.FontUtil;
import com.epro.psmobile.util.FontUtil.FontName;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class JobRequestPlanDetailFragment extends ContentViewBaseFragment {

	/*
	public static JobRequestPlanDetailFragment newInstance(JobRequest jobRequest)
	{
		JobRequestPlanDetailFragment detail = new JobRequestPlanDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
		detail.setArguments(bundle);
		return detail;
	}*/
	public static JobRequestPlanDetailFragment newInstance(JobRequest jobRequest,
			Task task,
			boolean showInPlan)
	{
		JobRequestPlanDetailFragment detail = new JobRequestPlanDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL, jobRequest);
		bundle.putParcelable(InstanceStateKey.KEY_ARGUMENT_TASK, task);
		bundle.putBoolean(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL_IN_PLAN, showInPlan);
		detail.setArguments(bundle);
		return detail;
	}
	/**
	 * 
	 */
	private JobRequestPlanDetailFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#getView()
	 */
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		View currentView = inflater.inflate(R.layout.ps_, root)
		View currentContentView = inflater.inflate(R.layout.ps_fragment_job_detail, container, false);
		initialView(currentContentView);
		return currentContentView;
	}
	private void initialView(View currentContentView)
	{
		Bundle argument =  this.getArguments();
		if (argument != null)
		{
			JobRequest jobRequest =  (JobRequest)argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
			Task task = (Task)argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			boolean showInPlan = argument.getBoolean(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL_IN_PLAN);
			
			/*
			TextView tvCustomerName = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_name);
			TextView tvCustomerAddress = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_address);
			TextView tvCustomerTels = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_tels);
			TextView tvCustomerInspectType = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_inspect_type);
			 */
			this.setCustomerInfoTitle(currentContentView, task,jobRequest);
			
			TextView tvCustomerCode = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_code);
			TextView tvBusinessType = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_business_type);
			TextView tvInspectPreriod = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_inspect_preriod);
			
			
			TextView tvPurpose = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_subject);
			TextView tvInspectType = (TextView)currentContentView.findViewById(R.id.tv_job_detail_customer_inspect);
			
			TextView tvJobRequestNotes = (TextView)currentContentView.findViewById(R.id.tv_job_request_notes);
			
			tvPurpose.setText(jobRequest.getPurposeName());
			tvInspectType.setText(jobRequest.getTypeOfSurvey());
			tvJobRequestNotes.setText(jobRequest.getJobRequestNotes());
			/*
			tvCustomerName.setText(jobRequest.getCustomer().getCustomerName());
			tvCustomerAddress.setText(jobRequest.getCustomer().getCustomerAddress());
			tvCustomerTels.setText(this.getResources().getString(R.string.text_tel_prefix)+""+jobRequest.getCustomer().getCustomerTels());
			tvCustomerInspectType.setText(this.getResources().getString(R.string.text_inspect_type_prefix)+""+jobRequest.getInspectType().getInspectTypeName());
			*/
			
			
			tvCustomerCode.setText(this.getResources().getString(R.string.text_customer_code_prefix)+" "+jobRequest.getCustomerCode());
			tvBusinessType.setText(this.getResources().getString(R.string.text_customer_business_type,jobRequest.getBusinessType()));
			
			String periodInspect = "";
			/*
			Calendar cal = Calendar.getInstance();
			Date to_day = cal.getTime();

			String forceTime = task.getForceTime();
			if (showInPlan)
			{
				//show in plan
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
				//show in task
				periodInspect = 
						DataUtil.convertSlashDateToStringDDMMYYYY(task.getTaskDate());
						
			}*/
			periodInspect = DataUtil.getInspectPeriod(showInPlan,task);
			/*
			if (DataUtil.getZeroTimeDate(to_day).compareTo(DataUtil.getZeroTimeDate(task.getTaskDate())) == 0){
				periodInspect = DataUtil.convertSlashDateToStringDDMMYYYY(to_day);
			}else{
			periodInspect = 
					DataUtil.convertSlashDateToStringDDMMYYYY(to_day) + " - " + DataUtil.convertSlashDateToStringDDMMYYYY(task.getTaskDate());
			}*/
			tvInspectPreriod.setText(this.getResources().getString(
										R.string.text_customer_schedule_preiod_inspect,periodInspect));

			
			/*
			ArrayList<ProductInJobRequest> productInJobRequests = null;
			PSBODataAdapter adapter = PSBODataAdapter.getDataAdapter(this.getActivity());
			try {
				productInJobRequests = adapter.getProductInJobRequest(jobRequest.getJobRequestID());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (productInJobRequests != null){
				LinearLayout linearLayout = (LinearLayout)currentContentView.findViewById(R.id.ll_job_product_details);
				for(int i = 1; i <= productInJobRequests.size();i++)
				{
					ProductInJobRequest products = productInJobRequests.get(i-1);
					String strProductDetail = this.getString(R.string.text_customer_product_item_format,
							i,products.getProduct().getProductName(),
							products.getAmount(),
							products.getUnit().getUnitName());
					
					TextView tvProductDetail = new TextView(this.getActivity());
					tvProductDetail.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					tvProductDetail.setText(strProductDetail);
					linearLayout.addView(tvProductDetail);
				}
			}
			*/
			LinearLayout linearLayoutProducts = (LinearLayout)currentContentView.findViewById(R.id.ll_job_product_details);
			PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(getActivity());
			try {
				ArrayList<JobRequestProduct> jobRequestProducts =
						dataAdapter.findJobRequestProductsByJobRequestID(jobRequest.getJobRequestID());
				if (jobRequestProducts != null)
				{
				    /*
				     * distinct product group name
				     */
				    Hashtable<String,String> productGroupTable = 
				          new Hashtable<String,String>();
				    
					for(int i = 0; i < jobRequestProducts.size();i++)
					{
						JobRequestProduct jobRequestProduct = jobRequestProducts.get(i);
												if ((jobRequestProduct.getProductGroup() != null)&&
						     (!jobRequestProduct.getProductGroup().equalsIgnoreCase("null")))
						{
	                        if (!productGroupTable.containsKey(jobRequestProduct.getProductGroup()))
	                        {
	                           
	                           productGroupTable.put(jobRequestProduct.getProductGroup(), jobRequestProduct.getProductGroup());
	                        }						   
						}
					}
					ArrayList<String> productGroupNameList = 
					      new ArrayList<String>(productGroupTable.keySet());
					for(int i = 0; i < productGroupNameList.size();i++)
					{
	                    String strProductDetail = "";
	                    strProductDetail = 
	                            "" +(i+1)+" "+productGroupNameList.get(i) + " ";
	                    TextView tvProductDetail = new TextView(this.getActivity());
	                    FontUtil.replaceFontTextView(getActivity(), tvProductDetail, FontName.THSARABUN);
	                    tvProductDetail.setTextSize(20);
	                    tvProductDetail.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	                    tvProductDetail.setText(strProductDetail);
	                    linearLayoutProducts.addView(tvProductDetail);					   
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LinearLayout linearLayoutWarehouse = (LinearLayout)currentContentView.findViewById(R.id.ll_job_warehouse_details);
			ArrayList<CustomerSurveySite> surveySites = jobRequest.getCustomerSurveySiteList();
			for(int i = 1; i <= surveySites.size();i++)
			{
				CustomerSurveySite surveySite = surveySites.get(i-1);
				String strWareHouse = this.getString(R.string.text_customer_warehouse_details_format,
						i,						
						surveySite.getSiteAddress(),
						this.getString(R.string.text_tel_prefix)+""+surveySite.getSiteTels(),
						surveySite.getCheckLeader());
				TextView tvCustomerWareHouse = new TextView(this.getActivity());
				FontUtil.replaceFontTextView(getActivity(), tvCustomerWareHouse, FontName.THSARABUN);
				tvCustomerWareHouse.setTextSize(20);
				tvCustomerWareHouse.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				tvCustomerWareHouse.setText(strWareHouse);
				linearLayoutWarehouse.addView(tvCustomerWareHouse);
			}
			
			/*
			 * show map
			 */
			ImageButton btnNext = (ImageButton)currentContentView.findViewById(R.id.btn_next_view);
			btnNext.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*
					FragmentManager fm =  JobRequestPlanDetailFragment.this.getFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft.add(new CustomerMapFragment(), "");
					ft.addToBackStack(null);
					ft.commit();					
					*/
				}
				
			});
		}
	}

}

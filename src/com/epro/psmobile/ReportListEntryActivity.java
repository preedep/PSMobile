/**
 * 
 */
package com.epro.psmobile;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.form.xml.ReportListEntry;
import com.epro.psmobile.form.xml.ReportListEntryColumn;
import com.epro.psmobile.form.xml.ReportListEntryXMLReader;
import com.epro.psmobile.key.params.InstanceStateKey;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class ReportListEntryActivity extends PsBaseActivity {

	//private static long resXMLIdCar = R.xml.report_list_car_entry;
	
	/**
	 * 
	 */
	public ReportListEntryActivity() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.PsBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ps_activity_report_list_entry);

		initial();
		
	}
	private void initial()
	{
		ReportListEntry reportListEntry = null;
		JobRequest jobRequest = null;
		CustomerSurveySite customerSurveySite = null;
		
		try {
			jobRequest = this.getIntent().
							  getParcelableExtra(
									  InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
			
			customerSurveySite = this.getIntent().
									  getParcelableExtra(
											  InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
			
			
			reportListEntry = ReportListEntryXMLReader.read(this, R.xml.report_list_car_entry);
			
			if (reportListEntry != null)
			{
				this.setTitle(reportListEntry.getTitleName());//set title name;
				
				if (customerSurveySite != null)
				{
					TextView tvCustomerName = (TextView)this.findViewById(R.id.tv_report_list_customer_name);
					tvCustomerName.setText(customerSurveySite.getCustomerName());
				}

				LinearLayout columnsContainer = (LinearLayout)this.findViewById(R.id.report_list_entry_columns);
				columnsContainer.removeAllViews();
				ArrayList<ReportListEntryColumn> columns =  reportListEntry.getColumns();
				for(ReportListEntryColumn col : columns)
				{
					/*
					 * create layout
					 */
					 LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					 View v = inflater.inflate(
							 			R.layout.ps_activity_report_list_entry_column, 
							 			columnsContainer, 
							 			false);
					 TextView tvColumnText = (TextView)v.findViewById(R.id.tv_report_list_entry_column);
					 tvColumnText.setText(col.getColumnName());
					 
					 columnsContainer.addView(v);
				}
			}			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

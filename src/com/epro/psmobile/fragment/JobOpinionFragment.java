/**
 * 
 */
package com.epro.psmobile.fragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.MessageBox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * @author nickmsft
 *
 */
public class JobOpinionFragment extends ContentViewBaseFragment {

	public interface JobOpinionDataInterface
	{
		void jobOpinionSaved(String textRemark);
	};
	private View currentContentView;
	private JobRequest jobRequest;
	private Task currentTask;
	
	private JobOpinionDataInterface jobOpinionDataInterface;
	/**
	 * 
	 */
	public JobOpinionFragment() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentContentView = inflater.inflate(R.layout.inspect_opinion_entry_form, container, false);
		initialView(currentContentView);
		return currentContentView;
	}
	private void initialView(View view)
	{
		Bundle argument =  this.getArguments();
		if (argument != null)
		{
			jobRequest =  (JobRequest)argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
			currentTask = (Task)argument.getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
			this.setCustomerInfoTitle(view, currentTask,jobRequest);
			
			
			EditText ed = (EditText)currentContentView.findViewById(R.id.et_opinion_answer_simple_question);
			if (currentTask.getRemark() != null){
				ed.setText(currentTask.getRemark().trim());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater,InspectOptMenuType.ENTRY_FORM);
	}
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_entry_save){
			//save
			saveDataEntry();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void saveDataEntry(){
		if ((jobRequest != null)&&(currentTask != null))
		{
			if (currentContentView != null)
			{
				EditText ed = (EditText)currentContentView.findViewById(R.id.et_opinion_answer_simple_question);
				String remark = ed.getText().toString().trim();

				PSBODataAdapter dataAdater = PSBODataAdapter.getDataAdapter(getActivity());
				int rowEffected = dataAdater.updateTaskRemark(jobRequest.getJobRequestID(),
						currentTask.getTaskCode(), remark);
				
				if (rowEffected > 0){
					//alert
					if (jobOpinionDataInterface != null){
						jobOpinionDataInterface.jobOpinionSaved(remark);
					}
					MessageBox.showSaveCompleteMessage(getActivity());
					
				}				
			}
		}
	}
	public JobOpinionDataInterface getJobOpinionDataInterface() {
		return jobOpinionDataInterface;
	}
	public void setJobOpinionDataInterface(JobOpinionDataInterface jobOpinionDataInterface) {
		this.jobOpinionDataInterface = jobOpinionDataInterface;
	}
}

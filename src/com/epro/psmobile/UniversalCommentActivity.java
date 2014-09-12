package com.epro.psmobile;

import java.util.ArrayList;

import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.JobRequestProduct;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.fragment.JobCommentFragment;
import com.epro.psmobile.key.params.InstanceStateKey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class UniversalCommentActivity extends PsBaseActivity {

   public UniversalCommentActivity() {
      // TODO Auto-generated constructor stub
   }

   /* (non-Javadoc)
    * @see com.epro.psmobile.PsBaseActivity#onCreate(android.os.Bundle)
    */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);
      
      this.setContentView(R.layout.ps_activity_universal_comment_list);

      JobRequest jobRequest = this.getIntent().getExtras().getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_DETAIL);
      Task task = this.getIntent().getExtras().getParcelable(InstanceStateKey.KEY_ARGUMENT_TASK);
      CustomerSurveySite site = this.getIntent().getExtras().getParcelable(InstanceStateKey.KEY_ARGUMENT_CUSTOMER_SITE_SURVEY);
      ArrayList<InspectFormView> formViewAllCols = this.getIntent().getExtras().getParcelableArrayList(InstanceStateKey.KEY_ARGUMENT_UNIVERSAL_COL_PROPERTIES);
      JobRequestProduct jobRequestProduct = this.getIntent().getExtras().getParcelable(InstanceStateKey.KEY_ARGUMENT_JOB_PRODUCT_REQUEST);
      
      Fragment f = JobCommentFragment.newUniversalInstance(jobRequest, task, site, formViewAllCols,jobRequestProduct);
      
      FragmentTransaction ft = 
            getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.universal_check_list_view, f);
      ft.addToBackStack(null);
      ft.commit();
      
   }
   
   
}

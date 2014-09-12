package com.epro.psmobile.adapter.callback;

import java.util.ArrayList;

import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.JobRequestProduct;

public interface OnOpenCommentActivity {
   void onOpenCommentActivity(ArrayList<InspectFormView> colProperties,JobRequestProduct type);

}

/**
 * 
 */
package com.epro.psmobile;

import com.epro.psmobile.view.InspecItemViewCreator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author nickmsft
 *
 */
public class PsDrawLayoutActivity extends PsLayoutBaseActivity {

	/**
	 * 
	 */
	public PsDrawLayoutActivity() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.PsBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.ps_activity_draw_layout);
		
		InspecItemViewCreator view = new InspecItemViewCreator(this,0);		
		View vItem = view.createInspectItemView();
		View vItem2 = view.createInspectItemView();
		
		ViewGroup vg = (ViewGroup)this.findViewById(R.id.root_layout);
		
		vg.addView(vItem);
		vg.addView(vItem2);
		
		this.startMoveItemListener(vg);
	}

}

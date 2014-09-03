/**
 * 
 */
package com.epro.psmobile.adapter;

import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.action.OnClickInspectDataItem;
import com.epro.psmobile.adapter.InspectItemGridViewAdapter.GridItemClickListener;
import com.epro.psmobile.data.InspectDataGroupType;
import com.epro.psmobile.data.InspectDataItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author thrm0006
 *
 */
public class InspectItemExpandableListAdapter extends BaseExpandableListAdapter  {

	private Context ctxt;
	private ArrayList<InspectDataGroupType> inspectDataGroupTypeList;
	private LayoutInflater inflater;
	private int maxChildCount = 1;
	private OnClickInspectDataItem clickInspectItemListener;

	/**
	 * 
	 */
	public InspectItemExpandableListAdapter(Context ctxt,
			ArrayList<InspectDataGroupType> inspectDataGroupTypeList,
			OnClickInspectDataItem clickInspectItemListener) {
		// TODO Auto-generated constructor stub
		
		this.ctxt = ctxt;
		this.inspectDataGroupTypeList = inspectDataGroupTypeList;
		inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.clickInspectItemListener = clickInspectItemListener;
		
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return this.inspectDataGroupTypeList.get(groupPosition).getInspectDataItemList().get(childPosition);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewGroup item = getViewGroupChild(convertView, parent);
        final GridView inspectGridIcon = (GridView) item.findViewById(R.id.gv_inspect_item);

        InspectItemGridViewAdapter gridViewAdapter = new InspectItemGridViewAdapter(this.ctxt,
        		this.inspectDataGroupTypeList.get(groupPosition).getInspectDataItemList(),
        		new InspectItemGridViewAdapter.GridItemClickListener() {
					
					@Override
					public void onClick(InspectDataItem item) {
						// TODO Auto-generated method stub
						if (clickInspectItemListener != null)
						{
							clickInspectItemListener.onInspectItemClicked((InspectDataGroupType) getGroup(groupPosition), item);
						}
					}
				});
        
        inspectGridIcon.setAdapter(gridViewAdapter);
        
        //label.setAdapter(new GridAdapter(parent.getContext(), groupPosition+1));
       
        // initialize the following variables (i've done it based on your layout
        // note: rowHeightDp is based on my grid_cell.xml, that is the height i've
        //    assigned to the items in the grid.
        
       
        final int spacingDp = 10;
        final int colWidthDp = 120;
        final int rowHeightDp = 120;

        // convert the dp values to pixels
        float density = ctxt.getResources().getDisplayMetrics().density;
        
        
        final float COL_WIDTH = density * colWidthDp;
        final float ROW_HEIGHT = density * rowHeightDp;
        final float SPACING = density * spacingDp;

        // calculate the column and row counts based on your display
        float widthParent = 300*density;//parent.getWidth();
        
        final int colCount = (int)Math.floor((widthParent - (2 * SPACING)) / (COL_WIDTH + SPACING));
        final int rowCount = (int)Math.ceil((inspectDataGroupTypeList.get(groupPosition).getInspectDataItemList().size() + 0d) / colCount);

        // calculate the height for the current grid
        final int GRID_HEIGHT = Math.round(rowCount * (ROW_HEIGHT + SPACING));

        // set the height of the current grid
        inspectGridIcon.getLayoutParams().height = 200;//GRID_HEIGHT;
        /*
        inspectGridIcon.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				inspectGridIcon.getViewTreeObserver().removeGlobalOnLayoutListener( this );
	            View lastChild = inspectGridIcon.getChildAt( inspectGridIcon.getChildCount() - 1 );
	            inspectGridIcon.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, lastChild.getBottom() ) );	 
			}
        	
        })*/;
		return item;
	}
	private ViewGroup getViewGroupChild(View convertView, ViewGroup parent)
    {
        // The parent will be our ListView from the ListActivity
        if (convertView instanceof ViewGroup)
        {
            return (ViewGroup) convertView;
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup item = (ViewGroup) inflater.inflate(R.layout.insepct_item_grid_view_item, null);
        return item;
    }
	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;/*1 gridview only*///this.inspectDataGroupTypeList.get(groupPosition).getInspectDataItemList().size();
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.inspectDataGroupTypeList.get(groupPosition);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.inspectDataGroupTypeList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View item = getViewGroupGroup(convertView, parent);
		if (item != null)
		{
			TextView tv = (TextView)item.findViewById(R.id.tv_inspect_group_header_name);
			InspectDataGroupType groupType = (InspectDataGroupType)this.getGroup(groupPosition);
			tv.setText(groupType.getInspectDataGroupTypeName());
		}
		return item;
	}
	
	private View getViewGroupGroup(View convertView, ViewGroup parent)
    {
        // The parent will be our ListView from the ListActivity
		
        if (convertView instanceof View)
        {
            return (View) convertView;
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View item1 = (View) inflater.inflate(R.layout.inspect_group_expandable_header_item, null);
        return item1;
//        TextView header = new TextView(this.ctxt);
//        header.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//        return header;
    }
	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	
	

}

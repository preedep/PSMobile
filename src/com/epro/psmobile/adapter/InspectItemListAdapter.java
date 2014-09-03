/**
 * 
 */
package com.epro.psmobile.adapter;

import java.io.File;
import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.action.OnChangedInspectDataEntryPanel;
import com.epro.psmobile.action.OnClickInspectDataItem;
import com.epro.psmobile.data.InspectDataGroupType;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.key.params.InstanceStateKey;
import com.epro.psmobile.util.MathEval;
import com.epro.psmobile.view.InspectItemViewState;
import com.epro.psmobile.view.ProductSpinner;
import com.epro.psmobile.view.ProductUnitSpinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author nickmsft
 *
 */
public class InspectItemListAdapter extends BaseAdapter  {

	public enum InspectItemToolboxType
	{
		INSPECT_ITEM,
		INSPECT_DATA_ENTRY,
		PHOTO_DATA_ENTRY
	}
	
	private Context ctxt;
	private InspectDataGroupType groupType;
	private LayoutInflater inflater;
	private OnClickInspectDataItem clickInspectItemListener;
	private OnChangedInspectDataEntryPanel onChangedInspectDataEntryPanel;
	
	private InspectItemToolboxType toolboxType;
	private Uri imageUri;
	private GridView preview;
	private ArrayList<Bitmap> bmpList;
	private InspectDataItem inspectDataItem;
	private InspectItemViewState viewState;
	private View currentViewItem;
	
	private EditText etInspectDataItemWidth;
	private EditText etInspectDataItemLong;
	private EditText etInspectDataItemHeight;
	private EditText etInspectDataItemValue;
	

	
	public final static String ENTRY_FIELD_WIDTH = "width";
	public final static String ENTRY_FIELD_HEIGHT = "height";
	public final static String ENTRY_FIELD_LONG = "long";
	public final static String ENTRY_FIELD_VALUE = "valueEvaled";
	
	/**
	 * 
	 */
	public InspectItemListAdapter(Context context,
			InspectItemToolboxType toolboxType
			) {
		this.ctxt = context;
		// TODO Auto-generated constructor stub
		inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.toolboxType = toolboxType;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.getGroupType();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int arg0, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if  (convertView == null)
		{
			view = getViewGroupChild(convertView, parent,this.toolboxType);
			currentViewItem = view;
			switch(this.toolboxType)
			{
				case INSPECT_ITEM:
				{
					/*
					final GridView inspectGridIcon = (GridView) view.findViewById(R.id.gv_inspect_item);
			        InspectItemGridViewAdapter gridViewAdapter = new InspectItemGridViewAdapter(this.ctxt,
			        		this.getGroupType().getInspectDataItemList(),
			        		new InspectItemGridViewAdapter.GridItemClickListener() {
								
								@Override
								public void onClick(InspectDataItem item) {
									// TODO Auto-generated method stub
									if (getClickInspectItemListener() != null)
									{
										getClickInspectItemListener().onInspectItemClicked(getGroupType(), item);
									}
								}
							});
			        
			        inspectGridIcon.setColumnWidth(70);
			        inspectGridIcon.setAdapter(gridViewAdapter);
		
			        view.invalidate();
			        
			        injectGlobalLayout((ViewGroup)view,(int)(gridViewAdapter.getCount()));
			        */
					view = inflater.inflate(R.layout.inspect_item_panel_container, parent, false);
					
			        InspectItemGridViewAdapter gridViewAdapter = new InspectItemGridViewAdapter(this.ctxt,
			        		this.getGroupType().getInspectDataItemList(),
			        		new InspectItemGridViewAdapter.GridItemClickListener() {
								
								@Override
								public void onClick(InspectDataItem item) {
									// TODO Auto-generated method stub
									if (getClickInspectItemListener() != null)
									{
										getClickInspectItemListener().onInspectItemClicked(getGroupType(), item);
									}
								}
							});
			        
			        int maxColumns = 2;
			        int row = 0;
			        int adapterItem = gridViewAdapter.getCount();
			        if ((adapterItem % maxColumns) == 0)
			        {
			        	row = (int)adapterItem/maxColumns;
			        }else{
			        	if (adapterItem <= maxColumns)
			        	{
			        		row = 1;
			        	}else{
			        		row = ((int)adapterItem/maxColumns)+1;
			        	}
			        }
			        
			        for(int i = 0; i < row;i++)
			        {
			        	View rowItem = inflater.inflate(R.layout.inspect_item_panel_row, (ViewGroup) view, false);
			        	for(int j = 0; j < maxColumns;j++)			        	
			        	{
			        		int position = (i * maxColumns) + j;
			        		Log.d("DEBUG_D", "Position = "+position);
			        		if (position < gridViewAdapter.getCount())
			        		{
				        		View vItem = gridViewAdapter.getView(position, null, null);
				        		((ViewGroup)rowItem).addView(vItem);			        						        			
			        		}
			        	}
			        	/*add new row*/
			        	((ViewGroup)view).addView(rowItem);
			        }
			        View rowItemEmpty = inflater.inflate(R.layout.inspect_item_panel_row, (ViewGroup) view, false);
			        TextView tv = (TextView)rowItemEmpty.findViewById(R.id.tv_padding);
			        tv.setVisibility(View.VISIBLE);
			        ((ViewGroup)view).addView(rowItemEmpty);
				}break;
				case INSPECT_DATA_ENTRY:
				{
					ProductSpinner sp_product =  (ProductSpinner)view.findViewById(R.id.sp_inspect_data_entry_product);
					sp_product.initial(0);
					sp_product.setOnItemSelectedListener(new OnItemSelectedListener(){

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							
							//ProductUnitSpinner sp_unit = (ProductUnitSpinner)view.findViewById(R.id.sp_inspect_data_entry_amount_unit);
							//sp_unit.initial();

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
						
					});
					
					
		        	 initialInspectDataEntryControls(view);

				}break;
				case PHOTO_DATA_ENTRY:
				{
					
					
					Button btnChooseAlbum = (Button)view.findViewById(R.id.btn_choose_pic_from_album);
					Button btnTakeAPhoto = (Button)view.findViewById(R.id.btn_take_photo);

					preview = (GridView)view.findViewById(R.id.gv_picture_preview);
					if (bmpList != null){
						PictureGridPreviewAdapter a = new PictureGridPreviewAdapter(this.ctxt,bmpList);
						preview.setAdapter(a);	
						preview.invalidateViews();
						
					}
					
					btnChooseAlbum.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//http://viralpatel.net/blogs/pick-image-from-galary-android-app/
			                Intent i = new Intent(
			                        Intent.ACTION_PICK,
			                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			                if (ctxt instanceof Activity)
			                {
			                	((Activity)ctxt).startActivityForResult(i, InstanceStateKey.RESULT_LOAD_IMAGE_FROM_GALLERY);
			                }
						}
						
					});
					
					btnTakeAPhoto.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//http://stackoverflow.com/questions/2729267/android-camera-intent
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
						    intent.putExtra(MediaStore.EXTRA_OUTPUT,
						            Uri.fromFile(photo));
						    setImageUri(Uri.fromFile(photo));
						    if (ctxt instanceof Activity)
						    {
						    	((Activity)ctxt).startActivityForResult(intent, InstanceStateKey.TAKE_PICTURE);
						    }
						}					
					});
				}break;
			}
			
		}else{
			view = convertView;
			currentViewItem = view;
		}
		
        
        return view;
	}
	private ViewGroup getViewGroupChild(View convertView, ViewGroup parent,InspectItemToolboxType toolboxType)
    {
        // The parent will be our ListView from the ListActivity
        if (convertView instanceof ViewGroup)
        {
            return (ViewGroup) convertView;
        }
        ViewGroup item = null;
        if (toolboxType == InspectItemToolboxType.INSPECT_ITEM){
        	 item = (ViewGroup) inflater.inflate(R.layout.insepct_item_grid_view_item, null);
        }else if (toolboxType == InspectItemToolboxType.INSPECT_DATA_ENTRY){
        	 item = (ViewGroup)inflater.inflate(R.layout.inspect_data_entry_form, null);
        }else if (toolboxType == InspectItemToolboxType.PHOTO_DATA_ENTRY)
        {
        	item = (ViewGroup)inflater.inflate(R.layout.inspect_item_camera_view, null);
        }
        return item;
    }
	private void injectGlobalLayout(final ViewGroup lv,final int gridRowCount)
	{
		lv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				int height = 0;
				int row = 0;
				if ((gridRowCount % 3) == 0){
					row = gridRowCount / 3;
				}else
					row = gridRowCount + 1;
				
			    height = lv.getHeight() * row;
				ViewGroup.LayoutParams layoutParam = lv.getLayoutParams();
				layoutParam.height = height;
				lv.setLayoutParams(layoutParam);
				
				lv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
			
		});
	}
	private void initialInspectDataEntryControls(final View v){
		etInspectDataItemWidth = (EditText)v.findViewById(R.id.et_inspect_data_entry_width);
		etInspectDataItemLong = (EditText)v.findViewById(R.id.et_inspect_data_entry_long);
		etInspectDataItemHeight = (EditText)v.findViewById(R.id.et_inspect_data_entry_heigh);
		etInspectDataItemValue = (EditText)v.findViewById(R.id.et_inspect_data_entry_total_amount);

		etInspectDataItemWidth.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				afterDataEntryTextChanged(s,etInspectDataItemWidth);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		etInspectDataItemLong.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				afterDataEntryTextChanged(s,etInspectDataItemLong);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		etInspectDataItemHeight.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				afterDataEntryTextChanged(s,etInspectDataItemHeight);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	public InspectDataItem getInspectDataItem() {
		return inspectDataItem;
	}

	public void setInspectDataItem(InspectDataItem inspectDataItem) {
		this.inspectDataItem = inspectDataItem;
	}

	public InspectItemViewState getViewState() {
		return viewState;
	}

	public void setViewState(InspectItemViewState viewState) {
		this.viewState = viewState;
	}

	public OnClickInspectDataItem getClickInspectItemListener() {
		return clickInspectItemListener;
	}

	public void setClickInspectItemListener(OnClickInspectDataItem clickInspectItemListener) {
		this.clickInspectItemListener = clickInspectItemListener;
	}
	
	public OnChangedInspectDataEntryPanel getOnChangedInspectDataEntryPanel() {
		return onChangedInspectDataEntryPanel;
	}

	public void setOnChangedInspectDataEntryPanel(
			OnChangedInspectDataEntryPanel onChangedInspectDataEntryPanel) {
		this.onChangedInspectDataEntryPanel = onChangedInspectDataEntryPanel;
	}

	public InspectItemToolboxType getCurrentToolboxType(){
		return toolboxType;
	}
	public InspectDataGroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(InspectDataGroupType groupType) {
		this.groupType = groupType;
	}

	public Uri getImageUri() {
		return imageUri;
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
	public void reloadPictureGridPreview(Bitmap bmp){
/*		
		if (preview != null)
		{
			notifyDataSetChanged();
		}*/
		if (this.bmpList == null)
			this.bmpList = new ArrayList<Bitmap>();
		
		this.bmpList.add(bmp);
		
		this.notifyDataSetChanged();
	}
	public void setEntryField(String keyField,String value)
	{
		if (this.getCurrentToolboxType() == InspectItemToolboxType.INSPECT_DATA_ENTRY)
		{
			if (this.currentViewItem != null)
			{
				int resId = 0;
				TextView tv = null;
				if (keyField.equalsIgnoreCase(ENTRY_FIELD_WIDTH)){
					resId = R.id.et_inspect_data_entry_width;
				}
				if (keyField.equalsIgnoreCase(ENTRY_FIELD_LONG)){
					resId = R.id.et_inspect_data_entry_long;
				}
				if (keyField.equalsIgnoreCase(ENTRY_FIELD_HEIGHT)){
					resId = R.id.et_inspect_data_entry_heigh;
				}
				if (resId > 0){
					tv = (TextView)currentViewItem.findViewById(resId);
					tv.setText(value);
				}
			}
			this.notifyDataSetChanged();
		}
	}

	public void afterDataEntryTextChanged(Editable s,EditText currentEditText) {
		// TODO Auto-generated method stub
		String strWidth = "0";
		String strLong = "0";
		String strHeight = "0";
		String strValue = "0";
		
		if (this.etInspectDataItemWidth == currentEditText){
			strWidth = s.toString();
		}else{
			strWidth = this.etInspectDataItemWidth.getText().toString();
		}
		if (this.etInspectDataItemLong == currentEditText){
			strLong = s.toString();
		}else
			strLong = this.etInspectDataItemLong.getText().toString();
		
		if (this.etInspectDataItemHeight == currentEditText){
			strHeight = s.toString();
		}else
			strHeight = this.etInspectDataItemHeight.getText().toString();
		
		
		try{
			
			String formula = this.inspectDataItem.getFormula();
			if (formula != null)
			{
				MathEval mathEval = new MathEval();
				mathEval.setVariable("w", Double.parseDouble(strWidth));
				mathEval.setVariable("d", Double.parseDouble(strLong));
				mathEval.setVariable("h", Double.parseDouble(strHeight));
				double value = mathEval.evaluate(formula);
				Log.d("DEBUG_D", "formula = "+formula+" value = "+value);
				if (this.etInspectDataItemValue != null)
				{
					this.etInspectDataItemValue.setText(value+"");
					strValue = this.etInspectDataItemValue.getText().toString();
				}
			}
		}catch(Exception ex){
			
		}
		
		if (this.getOnChangedInspectDataEntryPanel() != null)
		{
				getOnChangedInspectDataEntryPanel().onDataFieldChanged(this.getViewState(),ENTRY_FIELD_WIDTH, 
						strWidth);
				getOnChangedInspectDataEntryPanel().onDataFieldChanged(this.getViewState(),ENTRY_FIELD_LONG, 
						strLong);
				getOnChangedInspectDataEntryPanel().onDataFieldChanged(this.getViewState(),ENTRY_FIELD_HEIGHT, 
						strHeight);
				getOnChangedInspectDataEntryPanel().onDataFieldChanged(this.getViewState(),ENTRY_FIELD_VALUE, strValue);				
		}
		

	}

	
}

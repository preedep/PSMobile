package com.epro.psmobile.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.CustomerSurveySite;
import com.epro.psmobile.data.InspectDataItem;
import com.epro.psmobile.data.InspectDataObjectSaved;
import com.epro.psmobile.data.JobRequest;
import com.epro.psmobile.data.LayoutItemScaleHistory;
import com.epro.psmobile.data.Product;
import com.epro.psmobile.data.ProductGroup;
import com.epro.psmobile.data.Task;
import com.epro.psmobile.view.InspectItemViewState;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ImageUtil {

    public final static int IMG_MAX_WIDTH = 320;
    
	public enum InspectSaveFilter
	{
		SAVE_ALL_ITEM,
		SAVE_LAYOUT_ONLY
	}
	public static String FOLDER_NAME = "psmobile_image";
	public static String FOLDER_NAME_PHOTO_INSPECT_ENTRY = "inspect_photo";
	public static String FOLDER_NAME_PHOTO_TEAM_CHECK_INT = "team_photo";
	public static String FOLDER_NAME_PHOTO_HISTORY_INSPECT = "inspect_history_photo";
	
	public static int A4_WIDTH_72DPI = 842;
	public static int A4_HEIGHT_72DPI = 595;
	public ImageUtil() {
		// TODO Auto-generated constructor stub
	}
	public static String convertImageToBase64(Bitmap bitmap) throws Exception
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream .toByteArray();
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}
	public static String convertImageToBase64(String fileName) throws IOException
	{
		@SuppressWarnings("resource")
		InputStream inputStream = new FileInputStream(fileName);//You can get an inputStream using any IO API
		byte[] bytes;
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = inputStream.read(buffer)) != -1) {
		    output.write(buffer, 0, bytesRead);
		}
		bytes = output.toByteArray();
		String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
		return encodedString;
	}
	public static PictureDrawable createPictureDrawable(Context ctxt,String svgFileName)
	{
		if ((svgFileName != null) && (svgFileName.endsWith("svg"))){
			int lastIdx = svgFileName.indexOf(".svg");
			String mDrawableName = svgFileName.substring(0, lastIdx);
			int resID = ctxt.getResources().getIdentifier(mDrawableName , "raw", ctxt.getPackageName());		
			SVG svg = SVGParser.getSVGFromResource(ctxt.getResources(), resID);
			return svg.createPictureDrawable();
		}
		return null;
	}
	public static PictureDrawable createPictureDrawableFromSVGFile(String svgFile) throws SVGParseException, FileNotFoundException
	{
		SVG svg = SVGParser.getSVGFromInputStream(new FileInputStream(svgFile));
		return svg.createPictureDrawable();
	}
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	public static void resizeVectorImgView(ImageView imgView,
			int newWidth,
			int newHeight)
	{
		 PictureDrawable drawable = (PictureDrawable)imgView.getDrawable();
		 Bitmap bmpNew = Bitmap.createBitmap(newWidth, newHeight,Bitmap.Config.ARGB_8888);
		 
		 
		 Canvas canvas = new Canvas(bmpNew);

		 Picture resizePicture = new Picture();

		 canvas = resizePicture.beginRecording(newWidth, newHeight);

		 canvas.drawPicture(drawable.getPicture(), new Rect(0,0,newWidth, newHeight));

		 resizePicture.endRecording();

		 Drawable vectorDrawing = new PictureDrawable(resizePicture);
		 
		 imgView.setImageDrawable(vectorDrawing);
		 imgView.setScaleType(ScaleType.CENTER);
//		 imgView.setBackgroundColor(Color.RED);
		 
		 /*
		 imgView.setLayoutParams(new RelativeLayout.LayoutParams(
				 RelativeLayout.LayoutParams.WRAP_CONTENT,
				 RelativeLayout.LayoutParams.WRAP_CONTENT));
		 */
		 /*
		 imgView.getLayoutParams().width = newWidth;
		 imgView.getLayoutParams().height = newHeight;*/
		 imgView.invalidate();
	}
	public static Bitmap resizeVectorImg(PictureDrawable drawable,int newWidth,int newHeight)
	{
		 Bitmap bmpNew = Bitmap.createBitmap(newWidth, newHeight,Bitmap.Config.ARGB_8888);
		 
		 
		 Canvas canvas = new Canvas(bmpNew);

		 Picture resizePicture = new Picture();

		 canvas = resizePicture.beginRecording(newWidth, newHeight);

		 canvas.drawPicture(drawable.getPicture(), new Rect(0,0,newWidth, newHeight));

		 resizePicture.endRecording();

		 Drawable vectorDrawing = new PictureDrawable(resizePicture);
		 
		 return drawableToBitmap(vectorDrawing);
	}
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	public static Bitmap getResizedBitmapScale(Bitmap originalImage,int width,int height){
		Bitmap background = Bitmap.createBitmap((int)width, (int)height, Config.ARGB_8888);
		float originalWidth = originalImage.getWidth(), originalHeight = originalImage.getHeight();
		Canvas canvas = new Canvas(background);
		float scale = width/originalWidth;
		float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale)/2.0f;
		Matrix transformation = new Matrix();
		transformation.postTranslate(xTranslation, yTranslation);
		transformation.preScale(scale, scale);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		canvas.drawBitmap(originalImage, transformation, paint);
		return background;
	}
	public static String saveImageFile(
			Context context,
			String fileName,
			Bitmap bmp)
	{
		return saveImageFile(context,null,fileName,bmp);
	}
	public static String saveImageFile(
			Context context,
			String folderName,
			String fileName,
			Bitmap bmp
			)
	{
			
			String fullName = "";
		    String root = Environment.getExternalStorageDirectory().toString();		    
		    File myDir = new File(root + "/"+FOLDER_NAME+((folderName != null)?"/"+folderName+"/":"/"));    
		    myDir.mkdirs();
		    if (!fileName.endsWith(".jpg")){
		    	fileName += ".jpg";
		    }
		    File file = new File (myDir, fileName);
		    
		    fullName = file.getAbsolutePath();
		    
		    if (file.exists ()) file.delete (); 
		    try {
		           FileOutputStream out = new FileOutputStream(file);
		           bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
		           out.flush();
		           out.close();

				    context.sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_MOUNTED,
			            	Uri.parse("file://" + Environment.getExternalStorageDirectory())));

		    } catch (Exception e) {
		           fullName = "";
		           e.printStackTrace();
		    }
		    
		    return fullName;
	}
	public static String saveImageFileV2(
			Context context,
			String folderName,
			String fileName,
			Bitmap bmp
			)
	{
			
		    File file = new File (folderName, fileName);
		    
		    String fullName = file.getAbsolutePath();
		    
		    if (file.exists ()) file.delete (); 
		    try {
		           FileOutputStream out = new FileOutputStream(file);
		           bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
		           out.flush();
		           out.close();

				    context.sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
			            	Uri.parse("file://" + Environment.getExternalStorageDirectory())));

		    } catch (Exception e) {
		           fullName = "";
		           e.printStackTrace();
		    }
		    
		    return fullName;
	}
	public static ArrayList<Bitmap> getBitmapFromFolderImage()
	{
		ArrayList<Bitmap> bmpList = new ArrayList<Bitmap>();
		
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/"+FOLDER_NAME);    
		if (myDir.exists())
		{
			String fileNames[] = myDir.list();
			for(String fileName : fileNames)
			{
				/*
				 BitmapFactory.Options options = new BitmapFactory.Options();
				 options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				 Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
				 */
				 BitmapFactory.Options options = new BitmapFactory.Options();
				 options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				 Bitmap bitmap = BitmapFactory.decodeFile(root + "/"+FOLDER_NAME+"/"+fileName, options);

				 Bitmap bResized = getResizedBitmap(bitmap,200,200);
				 bmpList.add(bResized);
				 
			}
		}
		return bmpList;
	}
	public static Bitmap getResizedBitmapFromFile(String fullFileName)
	{
		 return decodeScaledBitmapFromSdCard(fullFileName,320,240);
	}
	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(
	        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
	public synchronized static String captureViewToFile(Activity context,
			Task currentTask,
			CustomerSurveySite surveySite,
			View view,
			String savedToFileName,
			InspectSaveFilter drawFilter) throws Exception
	{
		String fullName = "";
		int widthScreen = (int)context.getResources().getDimension(R.dimen.draw_layout_width);/*(int)ScreenUtil.convertDpToPixel(
				context.getResources().getDimension(R.dimen.draw_layout_width),context);ScreenUtil.getDeviceScreen(context).width;*/
		int heightScreen = (int)context.getResources().getDimension(R.dimen.draw_layout_deep);/*(int)ScreenUtil.convertDpToPixel(
				context.getResources().getDimension(R.dimen.draw_layout_deep),context);//// ScreenUtil.convertDpToPixel(1000, context);//A4_HEIGHT_72DPI;//ScreenUtil.getDeviceScreen(context).height;
		//int widthDeviceScreen = ScreenUtil.getDeviceScreen(context).width;
		//double scaleWidth = A4_WIDTH_72DPI / (double)widthDeviceScreen;//ScreenUtil.getDeviceScreen(context).width;
		//double scaleHeight = A4_HEIGHT_72DPI / (double)heightScreen;
		
		/*
		@SuppressWarnings("unused")
		double siteWidth = 0;
		@SuppressWarnings("unused")
		double siteLong = 0;
		*/
		//PSBODataAdapter dataAdater = PSBODataAdapter.getDataAdapter(context);
		/*
			LayoutItemScaleHistory scale = dataAdater.getLayoutScale(
					currentTask.getTaskCode(), 
					surveySite.getCustomerSurveySiteID());
			if (scale != null)
			{
				siteWidth = scale.getSiteWidth();
				siteLong = scale.getSiteLong();
			}else{
				siteWidth = widthScreen;
				siteLong = heightScreen;
			}
		*/
		
		Bitmap viewCapture = null;
		
		viewCapture = Bitmap.createBitmap(
				widthScreen, 
				heightScreen, 
				Bitmap.Config.ARGB_8888);
		
		Canvas cRoot = new Canvas(viewCapture);		
		 // Fill with white
        cRoot.drawColor(0xffffffff);
        

        String lastSVGFile = surveySite.getLayoutSVGPath();
		if (!lastSVGFile.isEmpty())
		{
			String fileImg = SharedPreferenceUtil.getDownloadFolder(context);
			//fileImg += CommonValues.DOWNLOAD_FOLDER_IMGS;
			fileImg += "/"+lastSVGFile;
			/*
			 * show image
			 */
			Bitmap bmpLastLayout = null;
			if (fileImg.endsWith(".svg"))
			{
				PictureDrawable drawable = ImageUtil.createPictureDrawableFromSVGFile(fileImg);
				bmpLastLayout = ImageUtil.resizeVectorImg(drawable, widthScreen, heightScreen);
				Paint p = new Paint();
				p.setAntiAlias(true);
				cRoot.drawBitmap(bmpLastLayout, 
							0, 
							0,
							p );
				
			}
		}
		if (view instanceof ViewGroup){
			ViewGroup vg = (ViewGroup)view;
			for(int i = 0; i < vg.getChildCount();i++)
			{
				View vClient = vg.getChildAt(i);
				if (vClient.getTag() instanceof InspectItemViewState)
				{
					InspectItemViewState viewState = (InspectItemViewState)vClient.getTag();
					SVG svgObj = viewState.getInspectDataItem().getSvgObj();
					if (svgObj == null)
					{
						/*
						 * create svg object from file
						 */
						String svgFileName = viewState.getInspectDataItem().getImageFileName();				
						if ((svgFileName != null) && (svgFileName.endsWith("svg"))){
							String fileSvg = 
									SharedPreferenceUtil.getDownloadFolder(context)+"/"+svgFileName;
							try {
								svgObj = SVGParser.getSVGFromInputStream(new FileInputStream(fileSvg));
							} catch (SVGParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								throw e;
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();								
								throw e;
							}
						}
					}
					
					InspectDataItem dataItem = viewState.getInspectDataItem();
					InspectDataObjectSaved dataSaved = viewState.getInspectDataObjectSaved();
					
					double clientWidth = dataSaved.getWidth();
					double clientLong = dataSaved.getdLong();
					
					int newWidth = vClient.getWidth() ;//(int)(vClient.getWidth() * scaleWidth);//(int) (clientWidth*(widthScreen/siteWidth));
					int newHeight = vClient.getHeight();//(int)(vClient.getHeight() * scaleHeight);//(int)(clientLong*(heightScreen/siteLong));
					
					Bitmap bmpObj = Bitmap.createBitmap(newWidth, 
							newHeight  /*30 is area of text*/, 
							Bitmap.Config.ARGB_8888);
					
					/*
					 * filter for save
					 */
					Canvas canvasItem = new Canvas(bmpObj);
					canvasItem.drawColor(0,Mode.CLEAR);
					
					//Bitmap bmpSvg = ImageUtil.resizeVectorImg(svgObj.createPictureDrawable(),newWidth,newHeight);
					//canvasItem.drawBitmap(bmpSvg, 0, 0, new Paint());
					/*
					ViewGroup.LayoutParams layoutParams = vClient.getLayoutParams();
					int oldWidth = layoutParams.width;
					int oldHeight = layoutParams.height;
					layoutParams.width = (int)(layoutParams.width * scaleWidth);
					layoutParams.height = (int)(layoutParams.height  * scaleHeight);
					vClient.setLayoutParams(layoutParams);
					vClient.invalidate();*/
					vClient.draw(canvasItem);
					
					/*restore*/
					/*
					layoutParams.width = oldWidth;
					layoutParams.height = oldHeight;
					vClient.setLayoutParams(layoutParams);
					vClient.invalidate();
					*/
				    Paint textPaint = new Paint();
				    textPaint.setARGB(200, 254, 0, 0);
				    textPaint.setTextAlign(Align.CENTER);

//			    	int productGroupId =  viewState.getInspectDataObjectSaved().getProductGroupID();
//				    int productId = viewState.getInspectDataObjectSaved().getProductID();
				    
//				    String inspectDataItemName = viewState.getInspectDataItem().getInspectDataItemName();
//				    ProductGroup productGrp =  dataAdater.findProductGroupById(productGroupId);
//				    Product product = null;
				    StringBuilder strBldText = new StringBuilder();
				    strBldText.append("id:"+dataSaved.getInspectDataObjectID());
				    
				    /*
				     * draw item
				     */
					double mRotate = dataSaved.getAngle();
					Matrix matrix = new Matrix();
					matrix.setTranslate(
							(float)dataSaved.getInspectDataItemStartX(),
							(float)dataSaved.getInspectDataItemStartY()
							);
					matrix.preRotate((float)mRotate,bmpObj.getWidth()/2,bmpObj.getHeight()/2);
					
				    if (drawFilter == InspectSaveFilter.SAVE_LAYOUT_ONLY)
				    {
				    	if (dataItem.isComponentBuiding())
				    	{
							cRoot.drawBitmap(bmpObj, matrix, new Paint());				    		
				    	}
				    }else{
						cRoot.drawBitmap(bmpObj, matrix, new Paint());
				    }

				    if (drawFilter == InspectSaveFilter.SAVE_ALL_ITEM){
								float[] values = new float[9];
								matrix.getValues(values);
								float height = 0;
								if (viewState.getInspectDataItem().isCameraObject() ||
									viewState.getInspectDataItem().isComponentBuiding()){
									height = values[4] + (int)(svgObj.getPicture().getHeight());									
								}else {
									height = values[4] + (int)(viewState.getInspectDataObjectSaved().getLongObject());
								}
								height -= 5;
								drawMultiLineText(strBldText.toString(), 
										(float)dataSaved.getInspectDataItemStartX()+
											  (int)(dataSaved.getWidthObject() /2),
										(float)dataSaved.getInspectDataItemStartY()+(height), 
										textPaint,cRoot);
				  }
				}
			 }
		}
		if (viewCapture != null)
		{
			// create a matrix for the manipulation
			//Matrix matrix = new Matrix();
			// resize the bit map
			//matrix.postScale((float)scaleWidth,(float)scaleHeight);

			// recreate the new Bitmap
			//viewCapture = Bitmap.createBitmap(viewCapture, 0, 0, A4_WIDTH_72DPI, A4_HEIGHT_72DPI, matrix, true);
			
			 fullName = saveImageFile(context,
					FOLDER_NAME_PHOTO_HISTORY_INSPECT
					+"/"+DataUtil.regenerateTaskCodeForMakeFolder(currentTask.getTaskCode())+"-"+surveySite.getCustomerSurveySiteID()+"/",
					savedToFileName,
					viewCapture);
			
		}
		return fullName;
	}
	public static void drawMultiLineText(String str, float x, float y, Paint paint, Canvas canvas) 
	{
		   String[] lines = str.split("\n");
		   float txtSize = -paint.ascent() + paint.descent();       

		   if (paint.getStyle() == Style.FILL_AND_STROKE || paint.getStyle() == Style.STROKE){
		      txtSize += paint.getStrokeWidth(); //add stroke width to the text size
		   }
		   float lineSpace = txtSize * 0.2f;  //default line spacing

		   for (int i = 0; i < lines.length; ++i) {
		      canvas.drawText(lines[i], x, y + (txtSize + lineSpace) * i, paint);
		   }
		}
	public static void splitAndDrawLines(Canvas canvas,
			String text, 
			int x, 
			int y, 
			Paint textPaint, 
			int width){
	    ArrayList<String> lines = new ArrayList<String>();
	    String test = text;
	    while(test.isEmpty()){
	        int newLength = textPaint.breakText(test, true, canvas.getWidth(), null);
	        lines.add(test.substring(0, newLength));
	        test = test.substring(newLength);
	    }
	    Rect bounds = new Rect();
	    int yoff = 0;
	    for(String line:lines){
	        canvas.drawText(line, x, y + yoff, textPaint);
	        textPaint.getTextBounds(line, 0, line.length(), bounds);
	        yoff += bounds.height();
	    }
	}
	public static String captureViewToFile(Context context,
			View view,
			String savedToFileName,
			InspectSaveFilter drawFilter)
	{
		String fullName = "";
		
		if (view.getId() == R.id.root_layout){
			Log.d("DEBUG_D","Root view");
		}
		/*
		 Bitmap b = Bitmap.createBitmap(targetView.getWidth(),
				       targetView.getHeight(),
				       Bitmap.Config.ARGB_8888);
		 Canvas c = new Canvas(b);
		 targetView.draw(c);
		 BitmapDrawable d = new BitmapDrawable(getResources(), b);
		 canvasView.setBackgroundDrawable(d);
		 */
		/*
		Bitmap viewCapture = null;
		
		view.setDrawingCacheEnabled(false);
		view.destroyDrawingCache();
		
		view.setDrawingCacheEnabled(true);
		viewCapture = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		*/
		
		Bitmap viewCapture = null;
		int width = view.getWidth();
		int height = view.getHeight();
		
		viewCapture = Bitmap.createBitmap(
				width, 
				height, 
				Bitmap.Config.ARGB_8888);
		
		Canvas c = new Canvas(viewCapture);		
		 // Fill with white
        c.drawColor(0xffffffff);

       // view.draw(c);
		if (view instanceof ViewGroup){
			ViewGroup vg = (ViewGroup)view;
			for(int i = 0; i < vg.getChildCount();i++)
			{
				View v = vg.getChildAt(i);
				if (v.getTag() instanceof InspectItemViewState)
				{
					InspectItemViewState vState = (InspectItemViewState)v.getTag();
					if (drawFilter == InspectSaveFilter.SAVE_LAYOUT_ONLY)
					{
						if (!vState.getInspectDataItem().isLayoutComponent())
							continue;
					}
					
					Bitmap bmpObj = Bitmap.createBitmap(v.getWidth(), 
							v.getHeight(), 
							Bitmap.Config.ARGB_8888);
					/*
					 * filter for save
					 */
					Canvas canvasItem = new Canvas(bmpObj);
					v.draw(canvasItem);
//					Matrix matrix = new Matrix();
//					matrix.setRotate(mRotation,source.getWidth()/2,source.getHeight()/2);
//					canvas.drawBitmap(source, matrix, new Paint());

					double mRotate = vState.getInspectDataObjectSaved().getAngle();
					Matrix matrix = new Matrix();
					matrix.setTranslate(
							(float)vState.getInspectDataObjectSaved().getInspectDataItemStartX(),
							(float)vState.getInspectDataObjectSaved().getInspectDataItemStartY()
							);
					matrix.preRotate((float)mRotate,bmpObj.getWidth()/2,bmpObj.getHeight()/2);

					/*
					c.drawBitmap(bmpObj, 
							(float)vState.getInspectDataObjectSaved().getInspectDataItemStartX(),
							(float)vState.getInspectDataObjectSaved().getInspectDataItemStartY(), 
							null);
							*/
					c.drawBitmap(bmpObj, matrix, new Paint());
				}
			}
		}
		if (viewCapture != null)
		{
			 fullName = saveImageFile(context,
					FOLDER_NAME_PHOTO_HISTORY_INSPECT,
					savedToFileName,
					viewCapture);
			if (!fullName.isEmpty())
			{
				/*
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(fullName)), "image/jpeg");
				context.startActivity(intent);
				*/
				Log.d("DEBUG_D", "Save to history inspect at -> "+fullName);
			}
		}
		return fullName;
	}
	public static Bitmap createBitmapScaleFollowWidth(Bitmap source,int targetWidth)
    {
       Bitmap bmpDest = null;
       
       if (source != null)
       {
          double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
          int targetHeight = (int) (targetWidth * aspectRatio);
          
          bmpDest = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
          if (source != bmpDest)
          {
             try{
                if (!source.isRecycled()){
                    source.recycle();
                }
                source = null;
                System.gc();
             }catch(Exception ex){
             }
          }          
       }
       return bmpDest;
    }

}

package com.epro.psmobile.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;
import net.sf.andpdf.nio.ByteBuffer;
import net.sf.andpdf.refs.HardReference;

import android.graphics.Bitmap;
import android.util.Base64;
import android.webkit.WebView;

public class PDFUtil {

	public PDFUtil() {
		// TODO Auto-generated constructor stub
	}

	public static String convertToHtml(int viewSize,String filePdf) throws IOException
	{
		
		String html = "";
        RandomAccessFile raf = new RandomAccessFile(new File(filePdf), "r");
        FileChannel channel = raf.getChannel();
        
        ByteBuffer bb = ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
        raf.close();
        // create a pdf doc
        PDFFile pdf = new PDFFile(bb);
        //Get the first page from the pdf doc
        PDFPage PDFpage = pdf.getPage(1, true);
        //create a scaling value according to the WebView Width
        final float scale = viewSize / PDFpage.getWidth() * 0.95f;
        //convert the page into a bitmap with a scaling value
        Bitmap page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
        //save the bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        page.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.close();
        byte[] byteArray = stream.toByteArray();
        //convert the byte array to a base64 string
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //create the html + add the first image to the html
        html = "<!DOCTYPE html><html><body bgcolor=\"#7f7f7f\"><img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
        //loop through the rest of the pages and repeat the above
        for(int i = 2; i <= pdf.getNumPages(); i++)
        {
            PDFpage = pdf.getPage(i, true);
            page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
            stream = new ByteArrayOutputStream();
            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            byteArray = stream.toByteArray();
            base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            html += "<img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
        }
        html += "</body></html>";
		
		return html;
	}
}

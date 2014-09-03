package com.epro.psmobile.form.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.epro.psmobile.form.xml.ReportListEntryColumn.InputType;

import android.content.Context;

public class ReportListEntryXMLReader {

	/*
	 <report_list>
	<report_title>รายงานการตรวจสอบรถยนต์</report_title>    
	<report_columns>
	     <report_column id="carNumber" name="หมายเลขตัวร" inputType="text"/>
	     <report_column id="sellType" name="แบบการขาย" inputType="list" table=""/>
	     <report_column id="dueDate" name="วันที่ส่งมอบ" inputType="date"/>
	     <report_column id="dueDatePayment" name="กำหนดชำระเงิน" inputType="decimal"/>
	     <report_column id="isDocument" name="เอกสาร" inputType="checkBox" />
	     <report_column id="remark" name="หมายเหตุ" inputType="text"/>
	     <report_column id="camera" name="" inputType="camera"/>
	</report_columns>
	</report_list>
	 */
	private final static String TAG_REPORT_LIST = "report_list";
	private final static String TAG_REPORT_TITLE = "report_title";
	private final static String TAG_REPORT_COLUMNS = "report_columns";
	private final static String TAG_REPORT_COLUMN = "report_column";
	
	private final static String ATT_ID = "id";
	private final static String ATT_NAME = "name";
	private final static String ATT_INPUT_TYPE = "inputType";
	private final static String ATT_TABLE = "table";
	private final static String ATT_INVOKE_METHOD = "invokeTo";
	
	
	public ReportListEntryXMLReader() {
		// TODO Auto-generated constructor stub
	}

	public static ReportListEntry read(Context context,int resXmlId) throws XmlPullParserException, IOException
	{
		ReportListEntry reportListEntry = null;		
		XmlPullParser parser = context.getResources().getXml(resXmlId);
		int eventType = parser.getEventType();
		boolean done = false;
		while((eventType != XmlPullParser.END_DOCUMENT) && !done)
		{
			String name = "";
			switch(eventType)
			{
				case XmlPullParser.START_TAG:{
					name = parser.getName();
					if (name.equalsIgnoreCase(TAG_REPORT_LIST)){
						reportListEntry = new ReportListEntry();
					}else if (name.equalsIgnoreCase(TAG_REPORT_TITLE)){
						reportListEntry.setTitleName(parser.getText());
					}else if (name.equalsIgnoreCase(TAG_REPORT_COLUMN))
					{
						if (reportListEntry != null)
						{
							ReportListEntryColumn column = new ReportListEntryColumn();
							try{
								String id = parser.getAttributeValue(null, ATT_ID);							
								column.setColumnId(id);
							}catch(Exception ex){}
							
							try{
								String colName  = parser.getAttributeValue(null, ATT_NAME);
								column.setColumnName(colName);
							}catch(Exception ex){}

							try{
							String strInputType = parser.getAttributeValue(null, ATT_INPUT_TYPE);
							column.setInputType(InputType.getType(strInputType));
							}catch(Exception ex){}
							
							try{
							String tableName = parser.getAttributeValue(null, ATT_TABLE);							
							column.setTableName(tableName);
							}catch(Exception ex){}
							
							try{
								column.setInvokeMethod(parser.getAttributeValue(null, ATT_INVOKE_METHOD));
							}catch(Exception ex){}
							
							reportListEntry.addColumn(column);
						}
					}
				}break;
				case XmlPullParser.END_TAG:{
					name = parser.getName();
					if (name.equalsIgnoreCase(TAG_REPORT_LIST)){
						done = true;
					}
				}break;
			}											
			
			eventType = parser.next();
		}
		return reportListEntry;
	}
}

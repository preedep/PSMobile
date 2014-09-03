package com.epro.psmobile.form.xml;

public class ReportListEntryColumn {

	/*
	  <report_column id="sellType" name="แบบการขาย" inputType="list" db=""/>
	    
	 */
	public enum InputType
	{
		TEXT("text"),
		EDITTEXT("edittext"),
		DROP_LIST("drop_list"),
		DECIMAL("decimal"),
		CHECKBOX("checkbox"),
		CAMERA("camera");
		
		private String textInputType;
		
		InputType(String strInputText)
		{
			this.textInputType = strInputText;
		}
		public String getInputType(){
			return this.textInputType;
		}
		
		public static InputType getType(String type)
		{
			for(int i=0;i<values().length;i++)
			{
				if (values()[i].getInputType().equalsIgnoreCase(type)){
					return values()[i];
				}
			}
			return InputType.TEXT;
		}
		
	}
	private String columnId;
	private String columnName;
	private InputType inputType;
	private String tableName;
	private String invokeMethod;
	private int width;
	
	public ReportListEntryColumn() {
		// TODO Auto-generated constructor stub
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public InputType getInputType() {
		return inputType;
	}
	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getInvokeMethod() {
		return invokeMethod;
	}
	public void setInvokeMethod(String invokeMethod) {
		this.invokeMethod = invokeMethod;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

}

package com.epro.psmobile.form.xml;

import java.util.ArrayList;

public class ReportListEntry {

	private String titleName;
	private ArrayList<ReportListEntryColumn> columns = null;
	
	public ReportListEntry() {
		// TODO Auto-generated constructor stub
		
		columns = new ArrayList<ReportListEntryColumn>();
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public void addColumn(ReportListEntryColumn column)
	{
		columns.add(column);
	}
	public ArrayList<ReportListEntryColumn> getColumns()
	{
		return columns;
	}
	public void removeAllColumns(){
		columns.clear();
	}
}

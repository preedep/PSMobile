package com.epro.psmobile.data;

import android.database.Cursor;

public class TaskControlTemplate implements DbCursorHolder {

	public enum TaskControlType
	{
		SimpleText(0),
		SimpleTextDecimal(1),
		SimpleTextDate(2),
		CheckBoxList(3),
		RadioBoxList(4),
		RadioBoxMatrix(5),
		Slider(6),
		Dropdownlist(7),
		RadioBoxListAndDropdown(9),
		Label(10);
		
		private int code;
		TaskControlType(int code)
		{
			this.code = code;
		}
		
		public int getCode(){
			return this.code;
		}
		public static TaskControlType getControlType(int code){
			for(TaskControlType item : values()){
				if (code == item.getCode()){
					return item;
				}
			}
			return TaskControlType.SimpleText;
		}
	}
	public TaskControlTemplate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub

	}
	public static TaskControlType getControlType(int code)
	{
		TaskControlType ctrlType = TaskControlType.SimpleText;		
		return ctrlType.getControlType(code);
	}
}

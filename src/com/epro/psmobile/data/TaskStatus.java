package com.epro.psmobile.data;

public enum TaskStatus {
     NOT(-1),
	 WAIT_TO_CONFIRM(0),
	 CONFIRM_INSPECT(1),
	 CONFIRMED_FROM_WEB(2),
	 CANCEL(3),
	 LOCAL_SAVED(4),
	 FINISH(5),
	 DUPLICATED(6),
	 SHIFT(7),
	 ALLOW_EDIT(8);
	 
	 private int taskStatusCode = -1;
	 TaskStatus(int taskStatusCode)
	 {
		 this.taskStatusCode = taskStatusCode;
	 }
	 public int getTaskStatus()
	 {
		 return this.taskStatusCode;
	 }
	 public static TaskStatus getStatus(int code){
		 for(int i = 0; i < values().length;i++){
			 if (values()[i].getTaskStatus() == code){
				 return values()[i];
			 }
		 }
		 return TaskStatus.NOT;
	 }
}

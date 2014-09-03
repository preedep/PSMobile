package com.epro.psmobile.util;

import com.epro.psmobile.data.TaskStatus;

public class EnumUtil {

	public EnumUtil() {
		// TODO Auto-generated constructor stub
	}
	public static TaskStatus getStatus(int status)
	{
		 for(TaskStatus taskStatus : TaskStatus.values()){
			 if (taskStatus.getTaskStatus() == status)
				 return taskStatus;
		 }
		 return TaskStatus.NOT;
	}
}

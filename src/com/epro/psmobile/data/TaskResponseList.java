package com.epro.psmobile.data;

import java.util.ArrayList;

public class TaskResponseList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3787136135046293485L;

	public E hasTaskResponse(String taskCode,int teamId)
	{
		for(int i = 0; i < this.size();i++)
		{
			E e = this.get(i);
			if (e instanceof TaskResponse)
			{
				TaskResponse t_response = (TaskResponse)e;
				if ((t_response.getTaskCode().equalsIgnoreCase(taskCode))&&
					(t_response.getTeamID() == teamId)){
					return e;
				}
			}
		}
		return null;
	}
}

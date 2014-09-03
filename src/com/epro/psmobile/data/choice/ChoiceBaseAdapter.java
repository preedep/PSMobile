package com.epro.psmobile.data.choice;

import java.util.ArrayList;

import com.epro.psmobile.data.ReasonSentence;
import com.epro.psmobile.data.TaskFormDataSaved;

import android.widget.BaseAdapter;

public abstract class ChoiceBaseAdapter extends BaseAdapter {

	private TaskFormDataSaved dataSaved;
	private ReasonSentence reasonSentence;
	
	public ArrayList<ReasonSentence> chooceReasonSentenceSelected = new 
			ArrayList<ReasonSentence>();
	public ChoiceBaseAdapter() {
		// TODO Auto-generated constructor stub
	}


	public TaskFormDataSaved getDataSaved() {
		return dataSaved;
	}


	public void setDataSaved(TaskFormDataSaved dataSaved) {
		this.dataSaved = dataSaved;
	}


	public abstract TaskFormDataSaved getValues();


	public ReasonSentence getReasonSentence() {
		return reasonSentence;
	}


	public void setReasonSentence(ReasonSentence reasonSentence) {
		this.reasonSentence = reasonSentence;
	}
	
	public abstract ArrayList<ReasonSentence> getChooceReasonSentenceSelected();

}

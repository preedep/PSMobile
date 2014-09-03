package com.epro.psmobile.data.choice;

import android.os.Parcel;
import android.os.Parcelable;

public class Question extends DeQuestData implements Parcelable
{
	private int questionSetID = INIT_INT;
	private int questionNumber = INIT_INT;
	//private int questionTypeID = INIT_INT;
	private String questionType = INIT_STR;
	private String questionText = INIT_STR;
	private Choice choice = null;
	private String answerText = INIT_STR;
	private int gotoQuestionNumber = INIT_INT;
	
	 public static final Parcelable.Creator<Question> CREATOR  = new Parcelable.Creator<Question>() {
		 public Question createFromParcel(Parcel in) {
			 	return new Question(in);
		 }
		 public Question[] newArray(int size) {
			 return new Question[size];
		 }
	};
	public Question(Parcel in)
	{
		/*
		 dest.writeInt(this.getQuestionSetID());
		dest.writeInt(this.getQuestionNumber());
		dest.writeString(this.getQuestionText());
		dest.writeParcelable(this.getChoice(),0);
		dest.writeString(this.getAnswerText());
		dest.writeInt(this.getGotoQuestionNumber());
		 */
		this.setQuestionSetID(in.readInt());
		this.setQuestionNumber(in.readInt());
		this.setQuestionText(in.readString());
		Choice c = in.readParcelable(Choice.class.getClassLoader());
		this.setChoice(c);
		this.setAnswerText(in.readString());
		this.setGotoQuestionNumber(in.readInt());
	}
	public Question(int questionSetID,
			int questionNumber,
			//int questionTypeID,
			String questionText,
			Choice choice,
			int gotoQuestionNumber
			)
	{
		this.setQuestionSetID(questionSetID);
		this.setQuestionNumber(questionNumber);
		//this.setQuestionTypeID(questionTypeID);
		this.setQuestionText(questionText);
		this.setChoice(choice);
		this.setGotoQuestionNumber(gotoQuestionNumber);		
	}

	/**
	 * @return the questionSetID
	 */
	public int getQuestionSetID() {
		return questionSetID;
	}

	/**
	 * @param questionSetID the questionSetID to set
	 */
	public void setQuestionSetID(int questionSetID) {
		this.questionSetID = questionSetID;
	}

	/**
	 * @return the questionNumber
	 */
	public int getQuestionNumber() {
		return questionNumber;
	}

	/**
	 * @param questionNumber the questionNumber to set
	 */
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	/**
	 * @return the questionTypeID
	 */
/*	
	public int getQuestionTypeID() {
		return questionTypeID;
	}
*/
	/**
	 * @param questionTypeID the questionTypeID to set
	 */
	/*
	public void setQuestionTypeID(int questionTypeID) {
		this.questionTypeID = questionTypeID;
	}*/

	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}



	/**
	 * @return the answerText
	 */
	public String getAnswerText() {
		return answerText;
	}

	/**
	 * @param answerText the answerText to set
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	/**
	 * @return the gotoQuestionNumber
	 */
	public int getGotoQuestionNumber() {
		return gotoQuestionNumber;
	}

	/**
	 * @param gotoQuestionNumber the gotoQuestionNumber to set
	 */
	public void setGotoQuestionNumber(int gotoQuestionNumber) {
		this.gotoQuestionNumber = gotoQuestionNumber;
	}

	/**
	 * @return the choice
	 */
	public Choice getChoice() {
		return choice;
	}

	/**
	 * @param choice the choice to set
	 */
	public void setChoice(Choice choice) {
		this.choice = choice;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(this.getQuestionSetID());
		dest.writeInt(this.getQuestionNumber());
		dest.writeString(this.getQuestionText());
		dest.writeParcelable(this.getChoice(),flags);
		dest.writeString(this.getAnswerText());
		dest.writeInt(this.getGotoQuestionNumber());
	}
	
}

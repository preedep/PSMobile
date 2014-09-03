package com.epro.psmobile.data.choice;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Hashtable;
import java.util.Map;


public class Choice extends DeQuestData implements Parcelable
{
   private int choiceID = INIT_INT;
   private ChoiceType choiceType = ChoiceType.SINGLE;
   private Hashtable<Integer,String> hChoiceTexts = new Hashtable<Integer,String>();
   private Hashtable<Integer,String> hChoiceValues = new Hashtable<Integer,String>();
   private Hashtable<Integer,String> hChoiceSelecteds = new Hashtable<Integer,String>();
   private Hashtable<Integer,MoreInfo> hMoreInfos = new Hashtable<Integer,MoreInfo>();
   private int slideBarMaxValue = INIT_INT;
   private int slideBarStepValue = INIT_INT;
   private int slideBarMinValue = INIT_INT;

   public static final Parcelable.Creator<Choice> CREATOR  = new Parcelable.Creator<Choice>() {
		 public Choice createFromParcel(Parcel in) {
			 	return new Choice(in);
		 }
		 public Choice[] newArray(int size) {
			 return new Choice[size];
		 }
	};

@SuppressLint("UseValueOf")
public Choice(int choiceID,ChoiceType choiceType,
		   String choiceTexts
		   )
   {
       this.setChoiceID(choiceID);
	   this.setChoiceType(choiceType);
	  
	   if (choiceTexts != null)
	   {
		   String[] strChoiceTexts = choiceTexts.split(",");
		   for(int i = 0; i < strChoiceTexts.length;i++)
		   {
			   hChoiceTexts.put(new Integer(i),strChoiceTexts[i]);		   
		   }
	   }
   }
public Choice(Parcel in)
{
	this.setChoiceID(in.readInt());
	@SuppressWarnings("unused")
	ChoiceType type = ChoiceType.UNDEF;
	try {
        type = ChoiceType.valueOf(in.readString());
    } catch (IllegalArgumentException x) {
        type = ChoiceType.UNDEF;
    }

	this.setChoiceType(type);
	
	in.readMap(hChoiceTexts, Choice.class.getClassLoader());
	in.readMap(hChoiceSelecteds, Choice.class.getClassLoader());
	in.readMap(hChoiceValues, Choice.class.getClassLoader());
	
	this.setSlideBarMaxValue(in.readInt());
	this.setSlideBarMinValue(in.readInt());
	this.setSlideBarStepValue(in.readInt());
}
   /**
    * @return the choiceID
    */
   public int getChoiceID() {
	   return choiceID;
   }

   /**
    * @param choiceID the choiceID to set
    */
   public void setChoiceID(int choiceID) {
	   this.choiceID = choiceID;
   }

   /**
    * @return the choiceType
    */
   public ChoiceType getChoiceType() {
	   return choiceType;
   }
   
   /**
    * @param choiceType the choiceType to set
    */
   public void setChoiceType(ChoiceType choiceType) {
	   this.choiceType = choiceType;
   }
   
 @SuppressLint("UseValueOf")
 	public void addChoiceSelected(int choiceNumber,String value)
 	{
	 	hChoiceSelecteds.put(new Integer(choiceNumber),value);
 	}
 @SuppressLint("UseValueOf")
	public void addMoreInfo(int choiceNumber,MoreInfo moreInfo)
 	{
 		hMoreInfos.put(new Integer(choiceNumber),moreInfo);
 	}
 
 public void addChoiceValues(String choiceValues)
 {
	 if (hChoiceValues != null)
	   {
		   String[] strChoiceTexts = choiceValues.split(",");
		   for(int i = 0; i < strChoiceTexts.length;i++)
		   {
			   hChoiceValues.put(new Integer(i),strChoiceTexts[i]);		   
		   }
	   }
 }
 public Map<Integer, String> getChoiceTextsMap(){
	 return this.hChoiceTexts;
 }
 public Map<Integer,String> getChoiceValuesMap(){
	 return this.hChoiceValues;
 }
 public Map<Integer, String> getChoiceSelectedsMap(){
	 return this.hChoiceSelecteds;
 }

public int getSlideBarMaxValue() {
	return slideBarMaxValue;
}
public void setSlideBarMaxValue(int slideBarMaxValue) {
	this.slideBarMaxValue = slideBarMaxValue;
}
public int getSlideBarStepValue() {
	return slideBarStepValue;
}
public void setSlideBarStepValue(int slideBarStepValue) {
	this.slideBarStepValue = slideBarStepValue;
}
public int getSlideBarMinValue() {
	return slideBarMinValue;
}
public void setSlideBarMinValue(int slideBarMinValue) {
	this.slideBarMinValue = slideBarMinValue;
}
@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void writeToParcel(Parcel arg0, int arg1) {
	// TODO Auto-generated method stub
	arg0.writeInt(this.getChoiceID());
	arg0.writeString(this.getChoiceType().name());
	arg0.writeMap(getChoiceTextsMap());
	arg0.writeMap(getChoiceSelectedsMap());
	arg0.writeMap(getChoiceValuesMap());
	
	/*
	 	this.setSlideBarMaxValue(in.readInt());
	this.setSlideBarMinValue(in.readInt());
	this.setSlideBarStepValue(in.readInt());

	 */
	arg0.writeInt(this.getSlideBarMaxValue());
	arg0.writeInt(this.getSlideBarMinValue());
	arg0.writeInt(this.getSlideBarStepValue());
}
}

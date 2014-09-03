/**
 * 
 */
package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author thrm0006
 *
 */
public class PhotoTeamHistory implements DbCursorHolder ,
Parcelable,
TransactionStmtHolder,
JSONDataHolder
{

	public final static String COLUMN_PHOTO_TEAM_CHECK_IN_HIS_ID = "imgTeamHistoryID";
	public final static String COLUMN_PHOTO_TEAM_CHECK_IN_NO = "imgTeamHistoryNo";
	public final static String COLUMN_PHOTO_TEAM_CHECK_IN_FILE_NAME = "imgFileTeamCheckIn";
	
	
	private int photoTeamCheckInHistoryId;
	private int photoTeamCheckInNo;
	private String photoTeamCheckInFileName;
	
	public static final Parcelable.Creator<PhotoTeamHistory> CREATOR = new Parcelable.Creator<PhotoTeamHistory>()
			{

				@Override
				public PhotoTeamHistory createFromParcel(Parcel source) {
					// TODO Auto-generated method stub
					return new PhotoTeamHistory(source);
				}

				@Override
				public PhotoTeamHistory[] newArray(int size) {
					// TODO Auto-generated method stub
					return new PhotoTeamHistory[size];
				}
		
			};
	/**
	 * 
	 */
	public PhotoTeamHistory(Parcel source)
	{
		this.setPhotoTeamCheckInHistoryId(source.readInt());
		this.setPhotoTeamCheckInNo(source.readInt());
		this.setPhotoTeamCheckInFileName(source.readString());
	}
	public PhotoTeamHistory() {
		// TODO Auto-generated constructor stub
	}

	public int getPhotoTeamCheckInHistoryId() {
		return photoTeamCheckInHistoryId;
	}

	public void setPhotoTeamCheckInHistoryId(int photoTeamCheckInHistoryId) {
		this.photoTeamCheckInHistoryId = photoTeamCheckInHistoryId;
	}

	public int getPhotoTeamCheckInNo() {
		return photoTeamCheckInNo;
	}

	public void setPhotoTeamCheckInNo(int photoTeamCheckInNo) {
		this.photoTeamCheckInNo = photoTeamCheckInNo;
	}

	public String getPhotoTeamCheckInFileName() {
		return photoTeamCheckInFileName;
	}

	public void setPhotoTeamCheckInFileName(String photoTeamCheckInFileName) {
		this.photoTeamCheckInFileName = photoTeamCheckInFileName;
	}

	/* (non-Javadoc)
	 * @see com.epro.psmobile.data.DbCursorHolder#onBind(android.database.Cursor)
	 */
	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub

		this.setPhotoTeamCheckInHistoryId(cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_TEAM_CHECK_IN_HIS_ID)));
		this.setPhotoTeamCheckInNo(cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_TEAM_CHECK_IN_NO)));
		this.setPhotoTeamCheckInFileName(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_TEAM_CHECK_IN_FILE_NAME)));
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		/*
		 private int photoTeamCheckInHistoryId;
		 private int photoTeamCheckInNo;
		 private String photoTeamCheckInFileName;	
		 */
		dest.writeInt(this.getPhotoTeamCheckInHistoryId());
		dest.writeInt(this.getPhotoTeamCheckInNo());
		dest.writeString(this.getPhotoTeamCheckInFileName());
	}
	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return "delete from PhotoTeamHistory " +
				"where "+COLUMN_PHOTO_TEAM_CHECK_IN_HIS_ID+" ="+this.getPhotoTeamCheckInHistoryId() + " " +
						" and "+ COLUMN_PHOTO_TEAM_CHECK_IN_NO + "= "+this.getPhotoTeamCheckInNo();
	}
	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into PhotoTeamHistory");
		strBld.append("(");
		strBld.append(COLUMN_PHOTO_TEAM_CHECK_IN_HIS_ID+",");
		strBld.append(COLUMN_PHOTO_TEAM_CHECK_IN_NO+",");
		strBld.append(COLUMN_PHOTO_TEAM_CHECK_IN_FILE_NAME);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.getPhotoTeamCheckInHistoryId()+",");
		strBld.append(this.getPhotoTeamCheckInNo()+",");
		strBld.append("'"+this.getPhotoTeamCheckInFileName()+"'");
		strBld.append(")");
		return strBld.toString();
	}
	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONDataUtil.put(jsonObj, COLUMN_PHOTO_TEAM_CHECK_IN_HIS_ID, this.photoTeamCheckInHistoryId);
		JSONDataUtil.put(jsonObj, COLUMN_PHOTO_TEAM_CHECK_IN_NO, this.photoTeamCheckInNo);
		JSONDataUtil.put(jsonObj, COLUMN_PHOTO_TEAM_CHECK_IN_FILE_NAME, this.photoTeamCheckInFileName);
		return jsonObj;
	}

}

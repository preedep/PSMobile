package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

public class News implements TransactionStmtHolder, DbCursorHolder,
		JSONDataHolder,Parcelable {

	public final static String COLUMN_NEWS_ID = "newsID";
	public final static String COLUMN_NEWS_TITLE = "newsTitle";
	public final static String COLUMN_NEWS_CONTENT = "newsContent";
	public final static String COLUMN_PUBLISH_DATE = "publishDate";
	public final static String COLUMN_PUBLISH_BY = "publishBy";
	
	private int newsID;
	private String newsTitle;
	private String newsContent;
	private String publishDate;
	private String publishBy;
		
	public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>()
	{

		@Override
		public News createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new News(source);
		}

		@Override
		public News[] newArray(int size) {
			// TODO Auto-generated method stub
			return new News[size];
		}
	};
	public News(Parcel source)
	{
		/*
		 		dest.writeInt(newsID);
		dest.writeString(newsTitle);
		dest.writeString(newsContent);
		dest.writeString(publishDate);
		dest.writeString(publishBy);

		 */
		this.newsID = source.readInt();
		this.newsTitle = source.readString();
		this.newsContent = source.readString();
		this.publishDate = source.readString();
		this.publishBy = source.readString();
	}
	public News() {
		// TODO Auto-generated constructor stub
	}

	public int getNewsID() {
		return newsID;
	}

	public void setNewsID(int newsID) {
		this.newsID = newsID;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishBy() {
		return publishBy;
	}

	public void setPublishBy(String publishBy) {
		this.publishBy = publishBy;
	}

	@Override
	public void onJSONDataBind(JSONObject jsonObj) throws JSONException {
		// TODO Auto-generated method stub
//		this.newsID = jsonObj.getString(COLUMN_NEWS_ID);
		this.newsID = JSONDataUtil.getInt(jsonObj, COLUMN_NEWS_ID);
		this.newsTitle = JSONDataUtil.getString(jsonObj, COLUMN_NEWS_TITLE);
		this.newsContent = JSONDataUtil.getString(jsonObj, COLUMN_NEWS_CONTENT);
		this.publishDate = JSONDataUtil.getString(jsonObj, COLUMN_PUBLISH_DATE);
		this.publishBy = JSONDataUtil.getString(jsonObj, COLUMN_PUBLISH_BY);
		
	}

	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBind(Cursor cursor) {
		// TODO Auto-generated method stub

		this.newsID = cursor.getInt(cursor.getColumnIndex(COLUMN_NEWS_ID));
		this.newsTitle = cursor.getString(cursor.getColumnIndex(COLUMN_NEWS_TITLE));
		this.newsContent = cursor.getString(cursor.getColumnIndex(COLUMN_NEWS_CONTENT));
		this.publishDate = cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISH_DATE));
		this.publishBy = cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISH_BY));
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		
		/*
		this.newsID = JSONDataUtil.getInt(jsonObj, COLUMN_NEWS_ID);
		this.newsTitle = JSONDataUtil.getString(jsonObj, COLUMN_NEWS_TITLE);
		this.newsContent = JSONDataUtil.getString(jsonObj, COLUMN_NEWS_CONTENT);
		this.publishDate = JSONDataUtil.getString(jsonObj, COLUMN_PUBLISH_DATE);
		this.publishBy = JSONDataUtil.getString(jsonObj, COLUMN_PUBLISH_BY);

		 */
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into news");
		strBld.append("(");
		strBld.append(COLUMN_NEWS_ID+",");
		strBld.append(COLUMN_NEWS_TITLE+",");
		strBld.append(COLUMN_NEWS_CONTENT+",");
		strBld.append(COLUMN_PUBLISH_DATE+",");
		strBld.append(COLUMN_PUBLISH_BY);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append(this.newsID+",");
		strBld.append("'"+this.newsTitle+"',");
		strBld.append("'"+this.newsContent+"',");
		strBld.append("'"+this.publishDate+"',");
		strBld.append("'"+this.publishBy+"'");
		strBld.append(")");
		return strBld.toString();
	}

	@Override
	public String updateStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		/*
		 * private int newsID;
	private String newsTitle;
	private String newsContent;
	private String publishDate;
	private String publishBy;
		 */
		dest.writeInt(newsID);
		dest.writeString(newsTitle);
		dest.writeString(newsContent);
		dest.writeString(publishDate);
		dest.writeString(publishBy);
	}

}

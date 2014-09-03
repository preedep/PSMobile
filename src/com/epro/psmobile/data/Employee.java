package com.epro.psmobile.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.epro.psmobile.remote.api.JSONDataHolder;
import com.epro.psmobile.util.JSONDataUtil;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements DbCursorHolder , 
								 Parcelable , 
								 JSONDataHolder , 
								 TransactionStmtHolder
{
	
//	public final static String EMPLOYEE_ID = "employeeID";
	public final static String EMPLOYEE_CODE = "employeeCode";
	public final static String ROLE_ID = "roleID";
	public final static String PRE_FIX = "preFix";
	public final static String FIRST_NAME = "firstName";
	public final static String LAST_NAME = "lastName";
	public final static String ADDR_1 = "address_1";
	public final static String ADDR_2 = "address_2";
	public final static String TEL_NO = "telNo";
	public final static String MOBILE_NO = "mobileNo";
	public final static String EMAIL = "email";
	public final static String USER_NAME_LOGOIN = "userNameLogin";
	public final static String PASSWORD_LOGIN = "passWordLogin";
	public final static String PROFILE_IMG = "profileImg";
	public final static String UPDATED_DATE_TIME = "updatedDateTime";
	
	public final static String SIMPLE_QUERY_EMPLOYEE = "select * from employee";
	
//	private int employeeID;
	private String employeeCode;
	private int roleID;
	private String preFix;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String telNo;
	private String mobileNo;
	private String email;
	private String userNameLogin;
	private String passwordLogin;
	private Bitmap profileImg;
	private java.sql.Timestamp updatedDateTime;
	
	public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>()
	{

		@Override
		public Employee createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Employee[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the employeeCode
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}

	/**
	 * @param employeeCode the employeeCode to set
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getPreFix() {
		return preFix;
	}

	public void setPreFix(String preFix) {
		this.preFix = preFix;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserNameLogin() {
		return userNameLogin;
	}

	public void setUserNameLogin(String userNameLogin) {
		this.userNameLogin = userNameLogin;
	}

	public String getPasswordLogin() {
		return passwordLogin;
	}

	public void setPasswordLogin(String passwordLogin) {
		this.passwordLogin = passwordLogin;
	}

	public Bitmap getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(Bitmap profileImg) {
		this.profileImg = profileImg;
	}

	public java.sql.Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(java.sql.Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	@Override
	public void onBind(Cursor cur) {
		// TODO Auto-generated method stub
//		this.setEmployeeID(cur.getInt(cur.getColumnIndex(EMPLOYEE_ID)));
		this.setEmployeeCode(cur.getString(cur.getColumnIndex(EMPLOYEE_CODE)));
		this.setRoleID(cur.getInt(cur.getColumnIndex(ROLE_ID)));
		this.setPreFix(cur.getString(cur.getColumnIndex(PRE_FIX)));
		this.setFirstName(cur.getString(cur.getColumnIndex(FIRST_NAME)));
		this.setLastName(cur.getString(cur.getColumnIndex(LAST_NAME)));
		this.setAddress1(cur.getString(cur.getColumnIndex(ADDR_1)));
		this.setAddress2(cur.getString(cur.getColumnIndex(ADDR_2)));
		this.setTelNo(cur.getString(cur.getColumnIndex(TEL_NO)));
		this.setMobileNo(cur.getString(cur.getColumnIndex(MOBILE_NO)));
		this.setEmail(cur.getString(cur.getColumnIndex(EMAIL)));
		this.setUserNameLogin(cur.getString(cur.getColumnIndex(USER_NAME_LOGOIN)));
		this.setPasswordLogin(cur.getString(cur.getColumnIndex(PASSWORD_LOGIN)));
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String deleteStatement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertStatement() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder strBld = new StringBuilder();
		strBld.append("insert into Employee");
		strBld.append("(");
		strBld.append(EMPLOYEE_CODE+",");
		strBld.append(ROLE_ID+",");
		strBld.append(PRE_FIX+",");
		strBld.append(FIRST_NAME+",");
		strBld.append(LAST_NAME+",");
		strBld.append(ADDR_1+",");
		strBld.append(ADDR_2+",");
		strBld.append(TEL_NO+",");
		strBld.append(EMAIL+",");
		strBld.append(USER_NAME_LOGOIN+",");
		strBld.append(PASSWORD_LOGIN);
		strBld.append(")");
		strBld.append(" values");
		strBld.append("(");
		strBld.append("'"+this.employeeCode+"',");
		strBld.append(""+this.roleID+",");
		strBld.append("'"+this.preFix+"',");
		strBld.append("'"+this.firstName+"',");
		strBld.append("'"+this.lastName+"',");
		strBld.append("'"+this.address1+"',");
		strBld.append("'"+this.address2+"',");
		strBld.append("'"+this.telNo+"',");
		strBld.append("'"+this.email+"',");
		strBld.append("'"+this.userNameLogin+"',");
		strBld.append("'"+this.passwordLogin+"'");
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
		this.employeeCode = JSONDataUtil.getString(jsonObj, EMPLOYEE_CODE);
		this.roleID = JSONDataUtil.getInt(jsonObj,ROLE_ID);
		this.preFix = JSONDataUtil.getString(jsonObj, PRE_FIX);
		this.firstName = JSONDataUtil.getString(jsonObj, FIRST_NAME);
		this.lastName = JSONDataUtil.getString(jsonObj, LAST_NAME);
		this.address1 = JSONDataUtil.getString(jsonObj, ADDR_1);
		this.address2 = JSONDataUtil.getString(jsonObj, ADDR_2);
		this.telNo = JSONDataUtil.getString(jsonObj, TEL_NO);
		this.email = JSONDataUtil.getString(jsonObj, EMAIL);
		this.userNameLogin = JSONDataUtil.getString(jsonObj, USER_NAME_LOGOIN);
		this.passwordLogin = JSONDataUtil.getString(jsonObj, PASSWORD_LOGIN);
	}

	@Override
	public JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}



}

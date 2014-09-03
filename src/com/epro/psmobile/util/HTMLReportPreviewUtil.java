package com.epro.psmobile.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

import com.epro.psmobile.R;
import com.epro.psmobile.da.PSBODataAdapter;
import com.epro.psmobile.data.Employee;
import com.epro.psmobile.data.Expense;
import com.epro.psmobile.data.MembersInTeamHistory;
import com.epro.psmobile.data.Team;
import com.epro.psmobile.data.TeamCheckInHistory;

import android.content.Context;

public class HTMLReportPreviewUtil {

	public HTMLReportPreviewUtil() {
		// TODO Auto-generated constructor stub
	}
	public static String generateReportAllowance(
			Context context,
			String titleReport,
			String startDate,
			String endDate,
			String teamName,
			String inspectTypeName
			)
	throws Exception
	{
		StringBuilder strBld = 
				new StringBuilder();
		
		strBld.append("<html>");
		strBld.append("<head>");
		strBld.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		strBld.append("<title>Untitled Document</title>");
		strBld.append("</head>");
		strBld.append("<body>");
		strBld.append("<table width=\"100%\" border=\"0\">");
		strBld.append("<tr>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("<td>"+titleReport+"</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_start_date)+" "+ startDate +" </td>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_end_date)+" "+endDate+"</td>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_page_no)+" 1/1</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_team_name)+" "+teamName+"</td>");
		strBld.append(" <td>&nbsp;</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_inspect_type_name)+" "+inspectTypeName+"</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("</table>");
		/*
		 * paint data to report here
		 */
		strBld.append("<table width=\"100%\" border=\"0\">");
		strBld.append("<tr>");
		strBld.append("    <td width=\"40%\">"+context.getString(R.string.text_report_filter_dlg_inspect_type)+" : package</td>");
		strBld.append("    <td width=\"30%\">"+context.getString(R.string.text_report_allowance_amount)+" : xxx " +
				""+context.getString(R.string.text_report_allowance_items)+"</td>");
		strBld.append("<td width=\"30%\">"+context.getString(R.string.text_report_allowance_amount)+" : xxx " +
				""+context.getString(R.string.text_report_allowance_jobs)+"</td>");
		strBld.append(" </tr>	");	 
		strBld.append("</table>");
		
		
		strBld.append("</body>");
		strBld.append("</html>");
		return strBld.toString();
	}
	public static String generateReportExpense(
			Context context,
			String titleReport,
			String startDate,
			String endDate,
			String teamName,
			String inspectTypeName,
			ArrayList<Expense> expenseList
			)
	throws Exception
	{
		StringBuilder strBld = new
				StringBuilder();
		strBld.append("<html>");
		strBld.append("<head>");
		strBld.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		strBld.append("<title>Untitled Document</title>");
		strBld.append("</head>");
		strBld.append("<body>");
		strBld.append("<table width=\"100%\" border=\"0\">");
		strBld.append("<tr>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("<td>"+titleReport+"</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_start_date)+" "+ startDate +" </td>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_end_date)+" "+endDate+"</td>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_page_no)+" 1/1</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_team_name)+" "+teamName+"</td>");
		strBld.append(" <td>&nbsp;</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_inspect_type_name)+" "+inspectTypeName+"</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("</table>");
		/*
		 	<string name="text_report_expense_col_inspect_date">วันที่ตรวจ</string>
	<string name="text_report_expense_col_inspect_time">เวลา</string>
	<string name="text_report_expense_col_oil">น้ำมัน</string>
	<string name="text_report_expense_col_ngv">NGV</string>
	<string name="text_report_expense_col_internet_and_print">Internet/พิพม์งาน</string>
	<string name="text_report_expense_col_hi_way">ค่าทางด่วน</string>
	<string name="text_report_expense_col_post_office">ค่าไปรษณีย์</string>
	<string name="text_report_expense_col_other">อื่นๆ</string>
	<string name="text_report_expense_col_study">อบรม</string>
	<string name="text_report_expense_col_meeting">ประชุม/ย้ายสาย</string>
	<string name="text_report_expense_col_total">รวมเป็นเงินทั้งสิ้น</string>
	<string name="text_report_expense_col_feet">Feet</string>
	<string name="text_report_expense_col_cash">เงินสด</string>
	<string name="text_report_expense_col_liter">ลิตร</string>

		 */
		
		strBld.append("<table width=\"100%\" border=\"1\">");
		strBld.append("<tr>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_inspect_date)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_inspect_time)+"</th>");
		strBld.append("<th colspan=\"4\" scope=\"col\">"+context.getString(R.string.text_report_expense_col_oil)+"</th>");
		strBld.append("<th colspan=\"4\" scope=\"col\">"+context.getString(R.string.text_report_expense_col_ngv)+"</th>");
		strBld.append("<th colspan=\"6\" scope=\"col\">"+context.getString(R.string.text_report_expense_col_other)+"</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("</tr>");
		
		strBld.append(" <tr>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_feet)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_liter)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_feet)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_liter)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_feet)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_unit)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_feet)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_unit)+"</th>");
		
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_internet_and_print)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_hi_way)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_post_office)+"</th>");
	    strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_bus)+"</th>");
	    strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_hotel)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_other)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_expense_col_total)+"</th>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("<th scope=\"col\">&nbsp;</th>");
		strBld.append("</tr>");		
		
		Hashtable<String,ArrayList<Expense>>
		   mExpenseTable = new Hashtable<String,ArrayList<Expense>>();
		
		for(Expense expense : expenseList)
		{
			String key = expense.getExpenseDate()+" "+expense.getExpenseTime();
			if (!mExpenseTable.containsKey(key)){
				ArrayList<Expense> exList = new ArrayList<Expense>();
				mExpenseTable.put(key, exList);
			}
			mExpenseTable.get(key).add(expense);
		}
		for(String key : mExpenseTable.keySet())
		{
			ArrayList<Expense> expList = mExpenseTable.get(key);
			String expenseDate = expList.get(0).getExpenseDate();
			String expenseTime = expList.get(0).getExpenseTime();
			strBld.append("<tr>");
			strBld.append("<td>"+expenseDate+"</td>");
			strBld.append("<td>"+expenseTime+"</td>");
			double rowTotal = 0.0;
			double oil_feet = 0.0;
			double oil_liter = 0.0;
			double ngv_feet = 0.0;
			double ngv_unit = 0.0;
			double internet_print = 0.0;
			double toll_way = 0.0;
			double post_office = 0.0;
			double bus = 0.0;
			double hotel = 0.0;
			double other = 0.0;
			for(Expense e : expList)
			{
				int subExpenseTypeID = e.getSubExpenseTypeID();
					/*
					 * fuel
					 */
					switch(subExpenseTypeID)
					{
						case 1:{
							oil_liter += e.getAmount();				
						}break;
						case 2:{
							oil_feet += e.getAmount();
						}break;
						case 3:{
							ngv_unit += e.getAmount();
						}break;
						case 4:{
							ngv_feet += e.getAmount();
						}break;
						case 5:{
							internet_print += e.getAmount();
						}break;
						case 6:{
							toll_way += e.getAmount();
						}break;
						case 7:{
							post_office += e.getAmount();
						}break;
						case 8:{
							bus += e.getAmount();
						}break;
						case 9:{
							hotel += e.getAmount();
						}break;
						case 10:{
							other += e.getAmount();
						}break;
					}
					
			
			}
			rowTotal = oil_feet + oil_liter + ngv_feet + ngv_unit + internet_print + toll_way + post_office;
			rowTotal += bus;
			rowTotal += hotel;
			rowTotal += other;
					
			strBld.append("<td>"+setValue(oil_feet)+"</td>");
			strBld.append("<td>"+setValue(oil_liter)+"</td>");
			strBld.append("<td>&nbsp;</td>");
			strBld.append("<td>&nbsp;</td>");
			strBld.append("<td>"+setValue(ngv_feet)+"</td>");
			strBld.append("<td>"+setValue(ngv_unit)+"</td>");
			strBld.append("<td>&nbsp;</td>");
			strBld.append("<td>&nbsp;</td>");
			strBld.append("<td>"+setValue(internet_print)+"</td>");
			strBld.append("<td>"+setValue(toll_way)+"</td>");
			strBld.append("<td>"+setValue(post_office)+"</td>");
			strBld.append("<td>"+setValue(bus)+"</td>");
			strBld.append("<td>"+setValue(hotel)+"</td>");
			strBld.append("<td>"+setValue(other)+"</td>");
			strBld.append("<td>"+DataUtil.decimal2digiFormat(rowTotal)+"</td>");
			strBld.append("</tr>");
		}
		/*
		for(Expense expense : expenseList)
		{
			
			strBld.append("<tr>");
			strBld.append("<td>"+expense.getExpenseDate()+"</td>");
			strBld.append("<td>"+expense.getExpenseTime()+"</td>");
			
			String oilType = expense.getTypeFuelAndPaidType();
			boolean isNGV = false;
			isNGV = oilType.toUpperCase().contains("NGV");
			
			//oil
			if (!isNGV){
				strBld.append("<td>"+DataUtil.decimal2digiFormat(expense.getAmount())+"</td>");				
				strBld.append("<td>"+DataUtil.decimal2digiFormat(expense.getLiter())+"</td>");				
			}else{
				strBld.append("<td>&nbsp;</td>");
				strBld.append("<td>&nbsp;</td>");
			}
			
			strBld.append("<td>&nbsp;</td>");
			strBld.append("<td>&nbsp;</td>");
			
			//ngv
//			strBld.append("<td>&nbsp;</td>");
			if (isNGV){
				strBld.append("<td>"+DataUtil.decimal2digiFormat(expense.getAmount())+"</td>");				
				strBld.append("<td>"+DataUtil.decimal2digiFormat(expense.getLiter())+"</td>");				
			}else{
				strBld.append("<td>&nbsp;</td>");
				strBld.append("<td>&nbsp;</td>");
			}			
			strBld.append("<td>&nbsp;</td>");
			strBld.append("<td>&nbsp;</td>");
			
			/////////////
			strBld.append("<td>&nbsp;</td>");
		    strBld.append("<td>&nbsp;</td>");
		    strBld.append("<td>&nbsp;</td>");
		    strBld.append("<td>&nbsp;</td>");
		    strBld.append("<td>&nbsp;</td>");
		    strBld.append("<td>&nbsp;</td>");
		    strBld.append("<td>&nbsp;</td>");
			strBld.append("</tr>");
		}
		*/
		strBld.append("</table>");
		strBld.append("</body>");
		strBld.append("</html>");
		return strBld.toString();
	}
	public static String generateReport(
			Context context,
			String titleReport,
			String startDate,
			String endDate,
			String teamName,
			String inspectTypeName,
			ArrayList<TeamCheckInHistory> teamCheckInHistoryList,
			Hashtable<String,ArrayList<TeamCheckInHistory>> mTeamCheckTable
			)
	throws Exception
	{
		StringBuilder strBld = new
				StringBuilder();
		
		strBld.append("<html>");
		strBld.append("<head>");
		strBld.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		strBld.append("<title>Untitled Document</title>");
		strBld.append("</head>");
		strBld.append("<body>");
		strBld.append("<table width=\"100%\" border=\"0\">");
		strBld.append("<tr>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("<td>"+titleReport+"</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_start_date)+" "+ startDate +" </td>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_end_date)+" "+endDate+"</td>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_page_no)+" 1/1</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_team_name)+" "+teamName+"</td>");
		strBld.append(" <td>&nbsp;</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("<tr>");
		strBld.append("<td>"+context.getString(R.string.text_report_regular_day_inspect_type_name)+" "+inspectTypeName+"</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("<td>&nbsp;</td>");
		strBld.append("</tr>");
		strBld.append("</table>");
		
		
		strBld.append("<table width=\"100%\" border=\"1\">");
		strBld.append("<tr>");
		
		/*
		 	<string name="text_report_regular_day_col_team">ทีมตรวจ</string>
	<string name="text_report_regular_day_col_inspect_date">วันที่ตรวจ</string>
	<string name="text_report_regular_day_col_team_member_1">เจ้าหน้าที่ตรวจ 1</string>
	<string name="text_report_regular_day_col_team_member_2">เจ้าหน้าที่ตรวจ 2</string>
	<string name="text_report_regular_day_col_team_member_3">เจ้าหน้าที่ครวจ 3</string>
	<string name="text_report_regular_day_col_distance">ระยะทาง</string>
	<string name="text_report_regular_day_col_used_distance">ระยะทางที่ใช้</string>
	<string name="text_report_regular_day_col_customer_name">ชื่อลูกค้า</string>
	<string name="text_report_regular_day_col_task_code">เลขที่งาน</string>
	<string name="text_report_regular_day_col_customer_survey_sites">สถานที่เก็บสินค้า</string>
	<string name="text_report_regular_day_col_task_end_time">เวลาสิ้นสุด</string>
	<string name="text_report_regular_day_col_period">ระยะเวลา</string>
	<string name="text_report_regular_day_col_inspect_type">ประเภทการตรวจ</string>					
	
	<string name="text_report_regular_day_col_start">เริ่มต้น</string>
	<string name="text_report_regular_day_col_end">สิ้นสุด</string>

		 */
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_team)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_inspect_date)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_team_member_1)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_team_member_2)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_team_member_3)+"</th>");
		strBld.append("<th colspan=\"2\" scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_distance)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_used_distance)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_customer_name)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_task_code)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_customer_survey_sites)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_task_end_time)+"</th>");
		strBld.append("<th colspan=\"2\" scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_period)+"</th>");
		strBld.append("<th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_inspect_type)+"</th>");
		strBld.append("</tr>");
		strBld.append("  <tr>");
		strBld.append("    <th scope=\"col\">&nbsp;</th>");
		strBld.append("    <th scope=\"col\">&nbsp;</th>");
		strBld.append("    <th scope=\"col\">&nbsp;</th>");
		strBld.append("	   <th scope=\"col\">&nbsp;</th>");
		strBld.append("    <th scope=\"col\">&nbsp;</th>");
		strBld.append("	   <th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_start)+"</th>");
		strBld.append("	   <th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_end)+"</th>");
		strBld.append("	   <th scope=\"col\">&nbsp;</th>");
		strBld.append("	   <th scope=\"col\">&nbsp;</th>");
		strBld.append("	   <th scope=\"col\">&nbsp;</th>");
		strBld.append("    <th scope=\"col\">&nbsp;</th>");
		strBld.append("    <th scope=\"col\">&nbsp;</th>");
		strBld.append("    <th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_start)+"</th>");
	    strBld.append("    <th scope=\"col\">"+context.getString(R.string.text_report_regular_day_col_end)+"</th>");
		strBld.append("    <th scope=\"col\">&nbsp;</th>");
		strBld.append("  </tr>");
		
//		ArrayList<TeamCheckInHistory> teamCheckInHistoryList
		PSBODataAdapter dataAdapter = PSBODataAdapter.getDataAdapter(context);
		if (teamCheckInHistoryList != null)
		{
			String sKey = "";
			boolean isNextDay = false;
			for(TeamCheckInHistory teamCheckInHistory : teamCheckInHistoryList)
			{
				Team team = dataAdapter.getTeamByTeamId(teamCheckInHistory.getTeamID());
				/*
			 	<string name="text_report_regular_day_col_team">ทีมตรวจ</string>
				<string name="text_report_regular_day_col_inspect_date">วันที่ตรวจ</string>
				<string name="text_report_regular_day_col_team_member_1">เจ้าหน้าที่ตรวจ 1</string>
				<string name="text_report_regular_day_col_team_member_2">เจ้าหน้าที่ตรวจ 2</string>
				<string name="text_report_regular_day_col_team_member_3">เจ้าหน้าที่ครวจ 3</string>
				<string name="text_report_regular_day_col_distance">ระยะทาง</string>
				<string name="text_report_regular_day_col_used_distance">ระยะทางที่ใช้</string>
				<string name="text_report_regular_day_col_customer_name">ชื่อลูกค้า</string>
				<string name="text_report_regular_day_col_task_code">เลขที่งาน</string>
				<string name="text_report_regular_day_col_customer_survey_sites">สถานที่เก็บสินค้า</string>
				<string name="text_report_regular_day_col_task_end_time">เวลาสิ้นสุด</string>
				<string name="text_report_regular_day_col_period">ระยะเวลา</string>
				<string name="text_report_regular_day_col_inspect_type">ประเภทการตรวจ</string>					
		
				<string name="text_report_regular_day_col_start">เริ่มต้น</string>
				<string name="text_report_regular_day_col_end">สิ้นสุด</string>
			 */
				Date date = DataUtil.getZeroTimeDate(teamCheckInHistory.getLastCheckInDateTime());
				Calendar c= Calendar.getInstance();
				c.setTime(date);
				
				int day = c.get(Calendar.DAY_OF_MONTH);
				int month = c.get(Calendar.MONTH)+1;
				int year = c.get(Calendar.YEAR);
				String key = day+""+month+""+year;
				if (key.equalsIgnoreCase(sKey)){
					isNextDay = true;
				}else{
					isNextDay = false;
					sKey = key;
				}
				ArrayList<TeamCheckInHistory> tmpTeamCheckInHistorys = 
						mTeamCheckTable.get(key);
				
				strBld.append("  <tr>");
				strBld.append("  <td>"+team.getTeamName()+"</td>");
				strBld.append("  <td>"+DataUtil.convertDateToStringDDMMYYYY(teamCheckInHistory.getLastCheckInDateTime())+"</td>");

				try{
					MembersInTeamHistory member = teamCheckInHistory.getMembers().get(0);
					String employeeCode = member.getEmployeeCode();
					Employee emp =  dataAdapter.getEmployeeByEmpCode(employeeCode);
					strBld.append("  <td>"+emp.getFirstName()+" "+emp.getLastName()+"</td>");
				}catch(Exception ex){
					strBld.append("  <td>&nbsp;</td>");
				}
				try{
					MembersInTeamHistory member = teamCheckInHistory.getMembers().get(1);
					String employeeCode = member.getEmployeeCode();
					Employee emp =  dataAdapter.getEmployeeByEmpCode(employeeCode);
					strBld.append("  <td>"+emp.getFirstName()+" "+emp.getLastName()+"</td>");
				}catch(Exception ex){
					strBld.append("  <td>&nbsp;</td>");
				}
				try{
					MembersInTeamHistory member = teamCheckInHistory.getMembers().get(2);
					String employeeCode = member.getEmployeeCode();
					Employee emp =  dataAdapter.getEmployeeByEmpCode(employeeCode);
					strBld.append("  <td>"+emp.getFirstName()+" "+emp.getLastName()+"</td>");
				}catch(Exception ex){
					strBld.append("  <td>&nbsp;</td>");
				}

				//Date checkIn = teamCheckInHistory.getLastCheckInDateTime();
			    //Date checkOut = teamCheckInHistory.getLastCheckOutDateTime();
				String firstMileOfDay = "0";
				String finishMileOfDay = "0";
				
				String startMiles = "0";
				
				Collections.sort(tmpTeamCheckInHistorys, new Comparator<TeamCheckInHistory>(){

					@Override
					public int compare(TeamCheckInHistory lhs,
							TeamCheckInHistory rhs) {
						// TODO Auto-generated method stub
						int start = Integer.parseInt(lhs.getNumberOfMilesAtStartPoint());
						
						int end = Integer.parseInt(rhs.getNumberOfMilesAtStartPoint());
						
						return start-end;
					}
					
				});
			    for(int i = 0; i < tmpTeamCheckInHistorys.size();i++)
			    {
			    	TeamCheckInHistory tmpTeamItem = tmpTeamCheckInHistorys.get(i);
			    	if (i == 0){
			    		if (tmpTeamItem.getInspectTypeID() < 0)
			    		{
			    			/*
			    			 * check in of the day
			    			 */
			    			firstMileOfDay = tmpTeamItem.getNumberOfMilesAtStartPoint();
			    			finishMileOfDay = tmpTeamItem.getNumberOfMilesAtEndPoint();
			    		}
			    	}
			    	if (tmpTeamItem.getNumberOfMilesAtStartPoint().equalsIgnoreCase(
			    					teamCheckInHistory.getNumberOfMilesAtStartPoint()))
			    	{
			    		try{
			    			startMiles = tmpTeamCheckInHistorys.get(i-1).getNumberOfMilesAtStartPoint();
			    		}catch(Exception ex){}
			    	}
			    }
			    int iStart = 0;
			    if (isNextDay){
			    	strBld.append("  <td>"+firstMileOfDay+"</td>");
			    	try{
			    		iStart = Integer.parseInt(firstMileOfDay);
			    	}catch(Exception ex){}
			    }else{
			    	strBld.append("  <td>"+startMiles+"</td>");			    	
			    	try{
			    		iStart = Integer.parseInt(startMiles);
			    	}catch(Exception ex){}

			    }
				strBld.append("  <td>"+teamCheckInHistory.getNumberOfMilesAtStartPoint()+"</td>");
				
			    strBld.append("  <td>"+( Integer.parseInt(teamCheckInHistory.getNumberOfMilesAtStartPoint()) - iStart)+"</td>");
			    strBld.append("  <td>"+teamCheckInHistory.getCustomerName()+"</td>");
			    strBld.append("  <td>"+teamCheckInHistory.getTaskCode()+"</td>");
			    strBld.append("  <td>"+teamCheckInHistory.getCustomerSurveySites().replace("\r\n", "</br>")+"</td>");
			    strBld.append("  <td>"+DataUtil.convertTimestampToStringHHmmss(teamCheckInHistory.getLastCheckOutDateTime())+"</td>");
			    strBld.append("  <td>"+DataUtil.convertTimestampToStringHHmmss(teamCheckInHistory.getLastCheckInDateTime())+"</td>");
			    strBld.append("  <td>"+DataUtil.convertTimestampToStringHHmmss(teamCheckInHistory.getLastCheckOutDateTime())+"</td>");
			    strBld.append("  <td>"+teamCheckInHistory.getInspectTypeName()+"</td>");
			    strBld.append("  </tr>");
				
			}
		}
		/*
		  <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		  </tr>
		 */		 
		strBld.append("</table>");
		strBld.append("</body>");
		strBld.append("</html>");
		return strBld.toString();
	}
	
	private static String setValue(double value)
	{
		if (value <= 0){
			return "";
		}else{
			return DataUtil.numberFormat(value);
		}
	}
}

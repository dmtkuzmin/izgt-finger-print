package com.company.fingerprints.service;

import com.company.fingerprints.entity.EmployeeCheckInOutDTO;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Service(GetDataService.NAME)
public class GetDataServiceBean implements GetDataService {

    @Inject
    private Logger log;

    @Override
    public List<EmployeeCheckInOutDTO> getCheckInOudForToday() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return getCheckInOudOnDay(calendar);
    }

    @Override
    public List<EmployeeCheckInOutDTO> getCheckInOudForYesterday() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        return getCheckInOudOnDay(calendar);
    }

    @Override
    public List<EmployeeCheckInOutDTO> getCheckInOudOnDay(Calendar calendar) {
        List<EmployeeCheckInOutDTO> checkInOutDTOList = new LinkedList<>();
        try {
            DriverManager.registerDriver(new SQLServerDriver());
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                log.error("Error", e);
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String condition = format.format(calendar.getTime());

            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=anviz", "d.kuzmin", "ArbaleT38");
            String selectSql = "SELECT \n" +
                    "\t   MIN([anviz].[dbo].[Checkinout].CheckTime) AS inTime\n" +
                    "\t  ,MAX([anviz].[dbo].[Checkinout].CheckTime) AS outTime\n" +
                    "\t  ,[anviz].[dbo].[Userinfo].Userid\n" +
                    "\t  ,[anviz].[dbo].[Userinfo].Name\n" +
                    "\t  ,[anviz].[dbo].[Dept].DeptName\n" +
                    "  FROM [anviz].[dbo].[Checkinout] JOIN [anviz].[dbo].[Userinfo] ON [anviz].[dbo].[Checkinout].[Userid] = [anviz].[dbo].[Userinfo].Userid \n" +
                    "  JOIN [anviz].[dbo].[Dept] ON [anviz].[dbo].[Dept].Deptid = [anviz].[dbo].[Userinfo].Deptid\n" +
                    "  where Format(CheckTime,'yyyy-MM-dd')= '"+ condition + " ' \n" +
                    "  GROUP BY [anviz].[dbo].[Userinfo].Userid,[anviz].[dbo].[Userinfo].Name, [anviz].[dbo].[Dept].DeptName\n" +
                    "\n" +
                    "  ";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql);

            while (resultSet.next()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

                String inTimeString = resultSet.getString(1);
                Date inTime = dateFormat.parse(inTimeString);

                String outTimeString = resultSet.getString(2);
                Date outTime = dateFormat.parse(outTimeString);

                Integer userId = Integer.parseInt(resultSet.getString(3));
                String userNameString = resultSet.getString(4);
                String deptNameString = resultSet.getString(5);

                EmployeeCheckInOutDTO checkInOutDTO = new EmployeeCheckInOutDTO();
                checkInOutDTO.setInTime(inTime);
                checkInOutDTO.setOutTime(outTime);
                checkInOutDTO.setEmployeeId(userId);
                checkInOutDTO.setEmployeeName(userNameString);
                checkInOutDTO.setDeptName(deptNameString);
                checkInOutDTOList.add(checkInOutDTO);
            }
        } catch (SQLException | ParseException throwables) {
            log.error("Error", throwables);
        }
        log.info("Loaded {}", checkInOutDTOList);
        return checkInOutDTOList;
    }

    @Override
    public List<EmployeeCheckInOutDTO> getCheckInOudOnPeriod(Calendar calendarFrom, Calendar calendarTo) {
        List<EmployeeCheckInOutDTO> checkInOutDTOList = new LinkedList<>();
        try {
            DriverManager.registerDriver(new SQLServerDriver());
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                log.error("Error", e);
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String conditionFrom = format.format(calendarFrom.getTime());
            String conditionTo = format.format(calendarTo.getTime());

            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=anviz", "d.kuzmin", "ArbaleT38");
            String selectSql = "SELECT \n" +
                    "\t   MIN([anviz].[dbo].[Checkinout].CheckTime) AS inTime\n" +
                    "\t  ,MAX([anviz].[dbo].[Checkinout].CheckTime) AS outTime\n" +
                    "\t  ,[anviz].[dbo].[Userinfo].Userid\n" +
                    "\t  ,[anviz].[dbo].[Userinfo].Name\n" +
                    "\t  ,[anviz].[dbo].[Dept].DeptName\n" +
                    "  FROM [anviz].[dbo].[Checkinout] JOIN [anviz].[dbo].[Userinfo] ON [anviz].[dbo].[Checkinout].[Userid] = [anviz].[dbo].[Userinfo].Userid \n" +
                    "  JOIN [anviz].[dbo].[Dept] ON [anviz].[dbo].[Dept].Deptid = [anviz].[dbo].[Userinfo].Deptid\n" +
                    "  where Format(CheckTime,'yyyy-MM-dd') BETWEEN '" + conditionFrom + "' AND '" + conditionTo + "'\n" +
                    "  GROUP BY [anviz].[dbo].[Userinfo].Userid,[anviz].[dbo].[Userinfo].Name, [anviz].[dbo].[Dept].DeptName, Format(CheckTime,'yyyy-MM-dd')\n" +
                    "\n" +
                    "  ";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql);

            while (resultSet.next()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

                String inTimeString = resultSet.getString(1);
                Date inTime = dateFormat.parse(inTimeString);

                String outTimeString = resultSet.getString(2);
                Date outTime = dateFormat.parse(outTimeString);

                Integer userId = Integer.parseInt(resultSet.getString(3));
                String userNameString = resultSet.getString(4);
                String deptNameString = resultSet.getString(5);

                EmployeeCheckInOutDTO checkInOutDTO = new EmployeeCheckInOutDTO();
                checkInOutDTO.setInTime(inTime);
                checkInOutDTO.setOutTime(outTime);
                checkInOutDTO.setEmployeeId(userId);
                checkInOutDTO.setEmployeeName(userNameString);
                checkInOutDTO.setDeptName(deptNameString);
                checkInOutDTOList.add(checkInOutDTO);
            }
        } catch (SQLException | ParseException throwables) {
            log.error("Error", throwables);
        }
        log.info("Loaded {}", checkInOutDTOList);
        return checkInOutDTOList;
    }
}
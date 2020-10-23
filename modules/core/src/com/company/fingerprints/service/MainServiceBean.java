package com.company.fingerprints.service;

import com.company.fingerprints.entity.EmployeeCheckInOutDTO;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service(MainService.NAME)
public class MainServiceBean implements MainService {

    @Inject
    private Logger log;

    private final GetDataService getDataService;

    private final GoogleSendDataService googleSendDataService;

    public MainServiceBean(GetDataService getDataService, GoogleSendDataService googleSendDataService) {
        this.getDataService = getDataService;
        this.googleSendDataService = googleSendDataService;
    }

    @Override
    public void exportTodayData() {
        List<EmployeeCheckInOutDTO> checkInOudForToday = getDataService.getCheckInOudForToday();
        googleSendDataService.sendToGoogleApp(checkInOudForToday);
    }

    @Override
    public void exportYesterdayData() {
        List<EmployeeCheckInOutDTO> checkInOudForYesterday = getDataService.getCheckInOudForYesterday();
        googleSendDataService.sendToGoogleApp(checkInOudForYesterday);
    }

    @Override
    public void exportWeekData() {
        Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(new Date());

        Calendar weekBeforeCalendar = new GregorianCalendar();
        weekBeforeCalendar.setTime(new Date());
        weekBeforeCalendar.add(Calendar.DATE, -7);

        List<EmployeeCheckInOutDTO> checkInOudOnPeriod = getDataService.getCheckInOudOnPeriod(weekBeforeCalendar, todayCalendar);
        googleSendDataService.sendToGoogleApp(checkInOudOnPeriod);
    }

    @Override
    public void exportMonthData() {
        Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(new Date());

        Calendar monthBeforeCalendar = new GregorianCalendar();
        monthBeforeCalendar.setTime(new Date());
        monthBeforeCalendar.add(Calendar.MONTH, -1);

        List<EmployeeCheckInOutDTO> checkInOudOnPeriod = getDataService.getCheckInOudOnPeriod(monthBeforeCalendar, todayCalendar);
        googleSendDataService.sendToGoogleApp(checkInOudOnPeriod);
    }
}
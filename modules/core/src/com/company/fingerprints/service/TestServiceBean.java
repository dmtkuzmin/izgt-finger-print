package com.company.fingerprints.service;

import com.company.fingerprints.entity.EmployeeCheckInOutDTO;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service(TestService.NAME)
public class TestServiceBean implements TestService {

    private final GetDataService getDataService;
    @Inject
    private Logger log;
    private final GoogleSendDataService googleSendDataService;

    public TestServiceBean(GetDataService getDataService, GoogleSendDataService googleSendDataService) {
        this.getDataService = getDataService;
        this.googleSendDataService = googleSendDataService;
    }

    @Override
    public void test() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2020, Calendar.SEPTEMBER, 1);

        List<EmployeeCheckInOutDTO> list = getDataService.getCheckInOudOnDay(calendar);

        Calendar calendarFrom = new GregorianCalendar();
        calendarFrom.set(2020, Calendar.OCTOBER, 19);

        Calendar calendarTo = new GregorianCalendar();
        calendarTo.set(2020, Calendar.OCTOBER, 20);
        list = getDataService.getCheckInOudOnPeriod(calendarFrom, calendarTo);

        googleSendDataService.sendToGoogleApp(list);
    }
}
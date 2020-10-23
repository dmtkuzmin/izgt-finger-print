package com.company.fingerprints.service;

import com.company.fingerprints.entity.EmployeeCheckInOutDTO;

import java.util.Calendar;
import java.util.List;

public interface GetDataService {
    String NAME = "fingerprints_GetDataService";

    List<EmployeeCheckInOutDTO> getCheckInOudForToday();

    List<EmployeeCheckInOutDTO> getCheckInOudForYesterday();

    List<EmployeeCheckInOutDTO> getCheckInOudOnDay(Calendar calendar);

    List<EmployeeCheckInOutDTO> getCheckInOudOnPeriod(Calendar calendarFrom, Calendar calendarTo);
}
package com.company.fingerprints.service;

public interface MainService {
    String NAME = "fingerprints_MainService";

    void exportTodayData();

    void exportYesterdayData();

    void exportWeekData();

    void exportMonthData();
}
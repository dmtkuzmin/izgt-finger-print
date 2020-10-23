package com.company.fingerprints.service;

import com.company.fingerprints.entity.EmployeeCheckInOutDTO;

import java.util.List;

public interface GoogleSendDataService {
    String NAME = "fingerprints_GoogleSendDataService";

    void sendToGoogleApp(List<EmployeeCheckInOutDTO> list);
}
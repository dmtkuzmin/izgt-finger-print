package com.company.fingerprints.service;

import com.company.fingerprints.entity.EmployeeCheckInOutDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service(GoogleSendDataService.NAME)
public class GoogleSendDataServiceBean implements GoogleSendDataService {

    @Inject
    private Logger log;

    @Override
    public void sendToGoogleApp(List<EmployeeCheckInOutDTO> list) {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(list);

            String accessToken = getGoogleAccessToken();
            URL url = new URL("https://script.google.com/a/izgtgroup.ru/macros/s/AKfycbwK41WDhcsmd6hIYO5HIh3mYx5vlgDsTVqmxVjEW0ZJavNReMcQ/exec");
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;

            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            httpConnection.setRequestProperty("Content-type", "application/json");

            OutputStream os = httpConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            osw.write(jsonString);
            osw.flush();
            osw.close();
            os.close();

            BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = inputBuffer.lines().collect(Collectors.joining());

        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    private String getGoogleAccessToken() {
        try {
            URL url = new URL("https://accounts.google.com/o/oauth2/token?" +
                    "grant_type=refresh_token&" +
                    "client_id=121559841941-6tgqjjv3qm1baft48kpumk202r23kia5.apps.googleusercontent.com&" +
                    "client_secret=R2NALh-iWFDUHBveSDquzanL&" +
                    "refresh_token=1//0cjkDcUS-8yf8CgYIARAAGAwSNwF-L9Irwkg7EM9qeAROnzVwlIj_QCsZXGsg_MDZBaOgGRrrC8xKF0Pv6iKUPxwmcf3r1canwyo");
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;

            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("User-Agent", "Super Agent/0.0.1");
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.setFixedLengthStreamingMode(0);

            InputStream inputStream = connection.getInputStream();
            BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(inputStream));
            String result = inputBuffer.lines().collect(Collectors.joining());

            JsonParser p = new JsonParser();
            JsonElement jsonElement = p.parse(result);

            return ((JsonObject) jsonElement).get("access_token").getAsString();

        } catch (IOException e) {
            log.error("Error", e);
        }
        return null;
    }

}
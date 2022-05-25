package com.example.article.api.controller;

import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RequestMapping("/api")
@RestController
public class RestTestController {

    public static final String SERVICE_KEY = "GhZUxxifMMsTs7Gzabe9dCC1lRwVv9vuNfnp1G4O0" +
            "j28JrtveBXeFoGuVQ1j0uZCHm%2BJlFMjwk0IsE28Hjw7kQ%3D%3D";
    public static final String ACCESS_URI = "http://apis.data.go.kr/1790387/covid19HospitalBedStatus/covid19HospitalBedStatusJson";

    @GetMapping("/get-covid-info")
    public ResponseEntity<CovidResponse> getCovidInfo() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(ACCESS_URI); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY); /*Service Key*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장.
        System.out.println(sb.toString());

        try {
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());

            JSONObject parseResponse = (JSONObject) jsonObject.get("response");
            JSONArray arrayResult = (JSONArray) parseResponse.get("result");

            CovidResponse response = new CovidResponse();

            return new ResponseEntity<>(response.toResponse(parseResponse,arrayResult), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new IOException();
    }

    @Data
    static class CovidResponse{

        private String resultMsg;
        private String resultCode;
        private String resultCnt;
        private String statusDate;

        public CovidResponse toResponse(JSONObject object1, JSONArray arrayResult){
            this.resultMsg = object1.get("resultMsg").toString();
            for(int i =0;i<arrayResult.size();i++){
                JSONObject date = (JSONObject) arrayResult.get(i);
                statusDate = date.get("status_date").toString();
            }
            this.resultCode = object1.get("resultCode").toString();
            this.resultCnt = object1.get("resultCnt").toString();
            return this;
        }
    }
}



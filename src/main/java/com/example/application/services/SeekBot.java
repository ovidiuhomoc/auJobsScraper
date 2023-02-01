package com.example.application.services;

import com.example.application.model.Job;
import com.example.application.model.JobPlatformOrigin;
import com.example.application.model.ScrapingSettings;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeekBot implements ScraperBot {
    private static int maxId = 0;
    private final ScrapingSettings scrapingSettings;
    int responseCode;
    private int id;
    private List<String> userAgents;
    private String selectedUserAgent;
    private URL url;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private StringBuffer response;

    public SeekBot(ScrapingSettings scrapingSettings) {
        initialize();
        this.scrapingSettings = scrapingSettings;
        System.out.println("[SeekBot][id " + id + "] initialized");
    }

    private void initialize() {
        maxId++;
        id = maxId;
        selectedUserAgent = getRandomUserAgent();
    }

    private String getRandomUserAgent() {
        userAgents = new ArrayList<>();
        userAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0");
        userAgents.add("Mozilla/5.0 (Linux; Android 12; SM-S906N Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/80.0.3987.119 Mobile Safari/537.36");
        userAgents.add("Mozilla/5.0 (Linux; Android 10; SM-G996U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36");
        userAgents.add("Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.107 Mobile Safari/537.36");
        userAgents.add("Mozilla/5.0 (Linux; Android 9; SM-G973U Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Mobile Safari/537.36");
        userAgents.add("Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36");
        userAgents.add("Mozilla/5.0 (iPhone14,3; U; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/19A346 Safari/602.1");
        userAgents.add("Mozilla/5.0 (iPhone13,2; U; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/15E148 Safari/602.1");
        userAgents.add("Mozilla/5.0 (iPhone12,1; U; CPU iPhone OS 13_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/15E148 Safari/602.1");
        userAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1");
        userAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/69.0.3497.105 Mobile/15E148 Safari/605.1");
        userAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) FxiOS/13.2b11866 Mobile/16A366 Safari/605.1.15");
        userAgents.add("Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36");
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/601.3.9 (KHTML, like Gecko) Version/9.0.2 Safari/601.3.9");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");
        userAgents.add("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1");
        Random rand = new Random();
        return userAgents.get(rand.nextInt(userAgents.size()));
    }

    @Override
    public List<Job> scrapeJobs(int page) {
        try {
            url = new URL(scrapingSettings.getScrapeLink(page));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", selectedUserAgent);
            responseCode = connection.getResponseCode();

            System.out.println("[SeekBot][id " + id + "] User-Agent : " + selectedUserAgent);
            System.out.println("[SeekBot][id " + id + "] Sending 'GET' request to URL : " + url);
            if (responseCode != 200) {
                System.out.println("[SeekBot][id " + id + "] Request for page " + page + " failed with response code : " + responseCode);
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();


            System.out.println("[SeekBot][id " + id + "] Connection content Type : " + connection.getHeaderFields().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SeekBot][id " + id + "] JSON response " + response.toString());
        if (!response.toString().startsWith("<!DOCTYPE html>    <html>      <head>")) {
            List<JSONObject> jsonJobs = new ArrayList<>();
            List<Job> jobs = new ArrayList<>();

            JSONObject mainJsonObject = new JSONObject(response.toString());
            JSONArray jobsJsonArray = (JSONArray) mainJsonObject.get("data");

            for (int i = 0; i < jobsJsonArray.length(); i++) {
                jsonJobs.add((JSONObject) jobsJsonArray.get(i));
            }

            for (JSONObject jsonJob : jsonJobs) {
                jobs.add(new Job(JobPlatformOrigin.Seek, jsonJob));
            }
            return jobs;
        }
        return null;
    }

    @Override
    public int getId() {
        return id;
    }
}

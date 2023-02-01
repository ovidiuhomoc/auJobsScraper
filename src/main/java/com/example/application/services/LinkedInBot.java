package com.example.application.services;

import com.example.application.model.Job;
import com.example.application.model.ScrapingSettings;
import com.example.application.services.ScraperBot;

import java.util.List;

public class LinkedInBot implements ScraperBot {
    private static int id = 0;
    private ScrapingSettings scrapingSettings;

    public LinkedInBot(ScrapingSettings scrapingSettings) {
        setId();
        this.scrapingSettings = scrapingSettings;
    }

    private void setId() {
        id++;
    }

    @Override
    public List<Job> scrapeJobs(int page) {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }
}

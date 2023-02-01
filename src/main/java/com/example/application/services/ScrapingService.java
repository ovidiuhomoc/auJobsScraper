package com.example.application.services;


import com.example.application.entities.*;

import java.util.List;
import java.util.Objects;

public class ScrapingService {
    public ScrapingService() {
    }

    private ScraperBot newScraperBot(JobPlatformOrigin type, ScrapingSettings scrapingSettings) {
        ScraperBot ScraperBot;
        switch (type) {
            case Seek:
                ScraperBot = new SeekBot(scrapingSettings);
                break;
            case LinkedIn:
                ScraperBot = new LinkedInBot(scrapingSettings);
                break;
            default:
                throw new RuntimeException("No such bot");
        }
        return ScraperBot;
    }

    public List<Job> scrapeJobs(JobPlatformOrigin type, ScrapingSettings settings, int page) {
        if (Objects.isNull(settings)) {
            return newScraperBot(type, new ScrapingSettings()).scrapeJobs(page);
        }
        return newScraperBot(type, settings).scrapeJobs(page);
    }
}


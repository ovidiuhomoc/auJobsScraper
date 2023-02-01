package com.example.application.entities;

import java.util.List;

public interface ScraperBot {
    List<Job> scrapeJobs(int page);
    int getId();
}


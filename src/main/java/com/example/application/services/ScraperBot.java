package com.example.application.services;

import com.example.application.model.Job;

import java.util.List;

public interface ScraperBot {
    List<Job> scrapeJobs(int page);
    int getId();
}


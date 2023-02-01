package com.example.application.services;


import com.example.application.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ScrapingService {
    public ScrapingSettings scrapingSettings;
    private List<Future<Void>> tasksInExecution;

    public ScrapingService() {
        setDefaultScrapingSettings();
    }

    private void setDefaultScrapingSettings() {
        scrapingSettings = new ScrapingSettings("Settings for Seek");

        scrapingSettings.UrlToScrape()
                .setLink("https://www.seek.com.au")
                .addBranch("api")
                .addBranch("chalice-search")
                .addBranch("v4")
                .addSearch(List.of(
                        new LinkEntry(LinkComponentType.ScrapePage, "page"),
                        new LinkEntry(LinkComponentType.LinkText, "seekSelectAllPages", "true"),
                        new LinkEntry(LinkComponentType.LinkText, "keywords", "java developer"),
                        new LinkEntry(LinkComponentType.LinkText, "hadPremiumListings", "true")
                ));
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

    public Callable<Void> scrapeAllMainPagesAndProcess() {
        int pageCounter = 1;
        int jobsCounter = 1;

        List<Job> jobs = scrapeJobs(JobPlatformOrigin.Seek, scrapingSettings, pageCounter);
        while (Objects.nonNull(jobs) && !jobs.isEmpty()) {
            for (Job job : jobs) {
                System.out.println("Page: " + pageCounter + "." + jobsCounter + ": " + job.toShortString());
                jobsCounter++;
            }
            int randomSleep = (int) (Math.random() * 1000);
            try {
                Thread.sleep(2000 + randomSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pageCounter++;
            jobs = scrapeJobs(JobPlatformOrigin.Seek, scrapingSettings, pageCounter);
        }
        return () -> null;
    }

    public void scrapeAllMainPages() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Callable<Void>> callableTasks = new ArrayList<>();
        callableTasks.add(scrapeAllMainPagesAndProcess());
//        callableTasks.add(scrapeAllMainPagesAndProcess()); this would be a second threaded task

        tasksInExecution = executorService.invokeAll(callableTasks);
        executorService.shutdown();
    }

    public void stopAllTasks() {
        if (Objects.nonNull(tasksInExecution)) {
            for (Future<Void> task : tasksInExecution) {
                task.cancel(true);
            }
        }
    }

    public ScrapingSettings getScrapingSettings() {
        return scrapingSettings;
    }

    public void setScrapingSettings(ScrapingSettings scrapingSettings) {
        this.scrapingSettings = scrapingSettings;
    }
}


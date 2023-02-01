package com.example.application.model;

import com.example.application.services.ScrapingService;

public class ScrapingAllPagesTest {
    public static void main(String[] args) {
        ScrapingService scrapingService = new ScrapingService();

        try {
            scrapingService.scrapeAllMainPages();
            Thread.sleep(getSeconds(5));
            scrapingService.stopAllTasks();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static int getSeconds(int seconds){
        return seconds * 1000;
    }
}

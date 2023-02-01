package com.example.application.model;

import com.example.application.services.ScrapingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SeekScrapeTest {
    @Autowired
    private ScrapingService scrapingService;

    @Test
    public void testGeneratingScrapeLink() {
        ScrapingSettings scrapingSettings = new ScrapingSettings("Settings for Seek");

        scrapingSettings.UrlToScrape().setLink("https://www.seek.com.au");
        Assertions.assertEquals("https://www.seek.com.au", scrapingSettings.getScrapeLink(0));

        scrapingSettings.UrlToScrape().addBranch("api");
        Assertions.assertEquals("https://www.seek.com.au/api", scrapingSettings.getScrapeLink(0));

        scrapingSettings.UrlToScrape().addBranch("chalice-search");
        Assertions.assertEquals("https://www.seek.com.au/api/chalice-search", scrapingSettings.getScrapeLink(0));

        scrapingSettings.UrlToScrape().addBranch("v4");
        Assertions.assertEquals("https://www.seek.com.au/api/chalice-search/v4", scrapingSettings.getScrapeLink(0));

        scrapingSettings.UrlToScrape().addSearch(List.of(
                new LinkEntry(LinkComponentType.ScrapePage, "page"),
                new LinkEntry(LinkComponentType.LinkText, "seekSelectAllPages", "true"),
                new LinkEntry(LinkComponentType.LinkText, "keywords", "java developer"),
                new LinkEntry(LinkComponentType.LinkText, "hadPremiumListings", "true")
        ));
        Assertions.assertEquals("https://www.seek.com.au/api/chalice-search/v4/search?page=1&seekSelectAllPages=true&keywords=java%20developer&hadPremiumListings=true", scrapingSettings.getScrapeLink(1));
    }

    @Test
    public void testScrapingOneSeekPage() {
        ScrapingSettings scrapingSettings = new ScrapingSettings("Settings for Seek");

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
        List<Job> jobs = scrapingService.scrapeJobs(JobPlatformOrigin.Seek, scrapingSettings, 1);
        Assertions.assertEquals(22, jobs.size());
        jobs = scrapingService.scrapeJobs(JobPlatformOrigin.Seek, scrapingSettings, 10000);
        Assertions.assertNull(jobs);
    }
}

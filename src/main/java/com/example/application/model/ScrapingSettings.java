package com.example.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScrapingSettings {

    private String name;
    private UrlToScrape urlToScrape;

    public ScrapingSettings(String name) {
        this.name = name;
        this.urlToScrape = new UrlToScrape();
    }

    public ScrapingSettings(String name, String link) {
        this.name = name;
        setLink(link);
    }

    public String getScrapeLink(int page) {
        return urlToScrape.getLink(page);
    }

    public UrlToScrape setLink(String link) {
        this.urlToScrape = new UrlToScrape(link);
        return this.urlToScrape;
    }

    public UrlToScrape UrlToScrape() {
        return urlToScrape;
    }
}

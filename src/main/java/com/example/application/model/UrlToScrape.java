package com.example.application.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class UrlToScrape {
    private List<LinkEntry> linkEntries;
    private List<LinkEntry> searchEntries;

    public UrlToScrape(String link) {
        this();
        linkEntries.add(new LinkEntry(
                LinkComponentType.LinkText,
                "link",
                link));
    }

    public UrlToScrape setLink(String link) {
        linkEntries = new ArrayList<>();
        linkEntries.add(new LinkEntry(
                LinkComponentType.LinkText,
                "link",
                link));
        return this;
    }

    public UrlToScrape addBranch(String branch) {
        linkEntries.add(new LinkEntry(
                LinkComponentType.LinkText,
                "branch",
                branch));
        return this;
    }

    public UrlToScrape addSearch(List<LinkEntry> searchEntries) {
        this.linkEntries.add(new LinkEntry(
                LinkComponentType.LinkText,
                "search",
                ""));
        this.searchEntries = searchEntries;
        return this;
    }

    private String searchParamToString(int page) {
        String searchParam = "";

        for (int i = 0; i < searchEntries.size(); i++) {
            if (i != 0) {
                searchParam = searchParam + "&";
            }
            LinkEntry searchEntry = searchEntries.get(i);
            if (searchEntry.getLinkComponentType() == LinkComponentType.ScrapePage) {
                searchParam = searchParam + searchEntry.getKey() + "=" + page;
            } else {
                searchParam = searchParam + searchEntry.getKey() + "=" + searchEntry.getValue().replaceAll(" ", "%20");
            }
        }
        return searchParam;
    }

    public String getLink(int page) {
        String constructedLink = "";
        for (LinkEntry linkEntry : linkEntries) {
            if (linkEntry.getKey().equals("link")) {
                constructedLink = constructedLink + linkEntry.getValue();
            }
            if (linkEntry.getKey().equals("branch")) {
                constructedLink = constructedLink + "/" + linkEntry.getValue();
            }
            if (linkEntry.getKey().equals("search")) {
                constructedLink = constructedLink + "/search?" + searchParamToString(page);
            }
        }
        return constructedLink;
    }
}
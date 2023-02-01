package com.example.application.entities;

import java.util.ArrayList;
import java.util.List;

public class ScrapingSettings {

    private String name;
    private LinkContruct linkContruct;

    public ScrapingSettings() {
    }

    public ScrapingSettings(String name) {
        this.name = name;
    }

    public ScrapingSettings(String name, String link) {
        this.name = name;
        setFixLink(link);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink(int page) {
        return linkContruct.getLink(page);
    }

    public void setLink(String link) {
        setFixLink(link);
    }

    public LinkContruct setFixLink(String fixedPartOfLink) {
        LinkContruct linkContruct = new LinkContruct(fixedPartOfLink);
        this.linkContruct = linkContruct;
        return this.linkContruct;
    }

    public class LinkContruct {
        private final List<Tuple<linkComponents, String>> linkParts;
        private String keywords;

        public LinkContruct(String fixedPartOfLink) {
            linkParts = new ArrayList<>();
            add(fixedPartOfLink);
        }

        public LinkContruct addPage() {
            this.linkParts.add(new Tuple<>(linkComponents.Page, ""));
            return this;
        }

        public LinkContruct add(String text) {
            this.linkParts.add(new Tuple<>(linkComponents.Text, text));
            return this;
        }

        public LinkContruct addKeywords(String keywords) {
            this.keywords = keywords;
            this.linkParts.add(new Tuple<>(linkComponents.Keywords, ""));
            return this;
        }

        public String getLink(int page) {
            String constructedLink = "";
            for (Tuple<linkComponents, String> linkPart : linkParts) {
                switch (linkPart.getKey()) {
                    case Text:
                        constructedLink = constructedLink + linkPart.getValue();
                        break;
                    case Page:
                        constructedLink = constructedLink + page;
                        break;
                    case Keywords:
                        String keywords = this.keywords.replaceAll(" ", "%20");
                        constructedLink = constructedLink + keywords;
                        break;
                }
            }
            return constructedLink;
        }

        private enum linkComponents {
            Text,
            Page,
            Keywords
        }

        private class Tuple<K, V> {
            K key;
            V value;

            public Tuple(K key, V value) {
                this.key = key;
                this.value = value;
            }

            public void put(K key, V value) {
                this.key = key;
                this.value = value;
            }

            public K getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }
        }
    }
}

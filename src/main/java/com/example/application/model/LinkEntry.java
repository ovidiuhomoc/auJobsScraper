package com.example.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class LinkEntry {
    LinkComponentType linkComponentType;
    String key;
    String value;

    public LinkEntry(LinkComponentType linkComponentType, String key) {
        if (linkComponentType == LinkComponentType.ScrapePage) {
            this.linkComponentType = linkComponentType;
            this.key = key;
            this.value = "";
            return;
        }
        System.out.println("LinkEntry constructor only with type and key can be used for ScrapePage only");
    }

    public void put(LinkComponentType linkComponentType, String key, String value) {
        this.linkComponentType = linkComponentType;
        this.key = key;
        this.value = value;
    }
}
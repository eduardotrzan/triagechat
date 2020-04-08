package com.triagechat.nlp.training.loadset.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString(of = { "text", "tags", "chunk" })
public class TextTaxonomy {

    private String text = "";

    private final List<String> tags = new ArrayList<>();

    private final String chunk;

    public void addText(String text) {
        this.text += " " + text;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

}

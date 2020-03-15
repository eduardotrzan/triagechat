package com.triagechat.nlp.training.loadset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "word", "tagType", "chunkType" })
public class WordTaxonomy {

    private String word;

    private String tagType;

    private String chunkType;

}

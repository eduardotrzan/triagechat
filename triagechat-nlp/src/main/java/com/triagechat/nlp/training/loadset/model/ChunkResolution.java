package com.triagechat.nlp.training.loadset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "tokens", "tags", "chunks", "lemmatizers" })
public class ChunkResolution {

    private List<String> tokens;

    private List<String> tags;

    private List<String> chunks;

    private List<String> lemmatizers;

}

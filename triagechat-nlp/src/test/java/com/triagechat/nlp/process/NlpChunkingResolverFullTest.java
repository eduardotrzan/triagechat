package com.triagechat.nlp.process;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.triagechat.nlp.training.loadset.LoadSetFactory;
import com.triagechat.nlp.training.loadset.model.TextTaxonomy;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class NlpChunkingResolverFullTest {

    private final LoadSetFactory loadSetFactory = new LoadSetFactory();
    private final NlpChunkResolver nlpChunkResolver = new NlpChunkResolver(this.loadSetFactory);

    private TaxonomyResolver taxonomyResolver;

    @Before
    public void setup() {
        this.taxonomyResolver = new TaxonomyResolver(
                this.nlpChunkResolver
        );
    }

    @Test
    public void resolveChunks() {
        List<String> sentences = List.of("I am not feeling well.",
                                         "My head is hurting and I have pain in chest.",
                                         "I am a bit dizzy and my breathing is hard.");

        List<String> filterTags = List.of("RB", "VBG", "NN", "JJ");
        List<String> filterChunks = List.of("I-VP", "B-ADVP", "I-NP", "B-NP");
        List<String> words = sentences
                .parallelStream()
                .map(s -> taxonomyResolver.resolveChunks(s, filterTags, filterChunks))
                .flatMap(Collection::stream)
                .map(TextTaxonomy::getText)
                .collect(Collectors.toList());
        log.info("words={}", words);
    }

}

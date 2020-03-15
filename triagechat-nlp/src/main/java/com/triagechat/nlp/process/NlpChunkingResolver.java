package com.triagechat.nlp.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.triagechat.nlp.training.loadset.LoadSetFactory;
import com.triagechat.nlp.training.loadset.model.WordTaxonomy;
import com.triagechat.nlp.training.loadset.model.TrainingSetType;

@Slf4j
@RequiredArgsConstructor
@Component
public class NlpChunkingResolver {

    private final LoadSetFactory loadSetFactory;

    public List<WordTaxonomy> resolveChunks(String text, List<String> filterTags, List<String> filterChunks) {
        return this.resolveChunks(text)
                .stream()
                .filter(c -> hasTaxonomies(filterTags, filterChunks, c))
                .collect(Collectors.toList());
    }

    private boolean hasTaxonomies(List<String> filterTags, List<String> filterChunks, WordTaxonomy wordTaxonomy) {
        boolean hasOneOfTheTags = filterTags
                .stream()
                .anyMatch(ft -> wordTaxonomy.getTagType().equals(ft));
        if (!hasOneOfTheTags) {
            return false;
        }

        return filterChunks
                .stream()
                .anyMatch(ft -> wordTaxonomy.getChunkType().equals(ft));
    }


    public List<WordTaxonomy> resolveChunks(String text) {
        Optional<POSTaggerME> posTaggerOpt = loadSetFactory.posTagger(TrainingSetType.INITIAL);
        if (posTaggerOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<ChunkerME> chunkerOpt = loadSetFactory.chunker(TrainingSetType.INITIAL);
        if (chunkerOpt.isEmpty()) {
            return Collections.emptyList();
        }

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);
        if (tokens == null || tokens.length == 0) {
            return Collections.emptyList();
        }

        POSTaggerME posTagger = posTaggerOpt.get();
        String[] tags = posTagger.tag(tokens);
        if (tags == null || tags.length == 0) {
            return Collections.emptyList();
        }

        ChunkerME chunker = chunkerOpt.get();
        String[] chunks = chunker.chunk(tokens, tags);

        boolean isSameLengths = (tokens.length == tags.length)
                && (tokens.length == chunks.length);
        if (!isSameLengths) {
            throw new RuntimeException("Mismatching chunk and taxonomy evaluation.");
        }

        List<WordTaxonomy> chunkTaxonomies = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            chunkTaxonomies.add(WordTaxonomy.builder()
                                        .word(tokens[i])
                                        .tagType(tags[i])
                                        .chunkType(chunks[i])
                                        .build());
        }

        return chunkTaxonomies;
    }
}

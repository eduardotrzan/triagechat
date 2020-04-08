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
import com.triagechat.nlp.training.loadset.model.ChunkResolution;
import com.triagechat.nlp.training.loadset.model.TextTaxonomy;
import com.triagechat.nlp.training.loadset.model.TrainingSetType;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaxonomyResolver {

    private final NlpChunkResolver nlpChunkResolver;

    public List<TextTaxonomy> resolveChunks(String text, List<String> filterTags, List<String> filterChunks) {
        return this.resolveChunks(text)
                .stream()
                .filter(c -> hasTaxonomies(filterTags, filterChunks, c))
                .collect(Collectors.toList());
    }

    private boolean hasTaxonomies(List<String> filterTags, List<String> filterChunks, TextTaxonomy wordTaxonomy) {
        boolean hasOneOfTheTags = filterTags
                .stream()
                .anyMatch(ft -> wordTaxonomy.getTags().contains(ft));
        if (!hasOneOfTheTags) {
            return false;
        }

        return filterChunks
                .stream()
                .anyMatch(ft -> wordTaxonomy.getChunk().equals(ft));
    }

    public List<TextTaxonomy> resolveChunks(String text) {
        return this.nlpChunkResolver
                .resolve(text)
                .map(this::buildTextTaxonomies)
                .orElse(Collections.emptyList());
    }

    private List<TextTaxonomy> buildTextTaxonomies(ChunkResolution chunkResolution) {
        List<String> chunks = chunkResolution.getChunks();
        List<String> tags = chunkResolution.getTags();
        List<String> lemmatizers = chunkResolution.getLemmatizers();

        List<TextTaxonomy> textTaxonomies = new ArrayList<>();
        TextTaxonomy textTaxonomy = new TextTaxonomy(chunks.get(0));
        for (int i = 0; i < tags.size(); i++) {
            String chunk = chunks.get(i);
            String token = chunkResolution.getTokens().get(i);
            String lemmatizer = lemmatizers.get(i);
            String tag = tags.get(i);

            if (shouldCloseTaxonomy(textTaxonomy.getChunk(), chunk)) {
                textTaxonomies.add(textTaxonomy);
                textTaxonomy = new TextTaxonomy(chunk);
            }

            String text = lemmatizer.equals("O")
                    ? token
                    : lemmatizer;
            textTaxonomy.addText(text);
            textTaxonomy.addTag(tag);
        }

        return textTaxonomies;
    }

    private boolean shouldCloseTaxonomy(String taxonomyChunk, String chunk) {
        return !taxonomyChunk.equals(chunk)
                && !chunk.contains("ADVP") // Adverb
                && !chunk.contains("PP"); // Preposition
    }
}

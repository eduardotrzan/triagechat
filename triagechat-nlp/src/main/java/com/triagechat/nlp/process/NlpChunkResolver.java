package com.triagechat.nlp.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.triagechat.nlp.training.loadset.LoadSetFactory;
import com.triagechat.nlp.training.loadset.model.ChunkResolution;
import com.triagechat.nlp.training.loadset.model.TrainingSetType;

@Slf4j
@RequiredArgsConstructor
@Component
public class NlpChunkResolver {

    private final LoadSetFactory loadSetFactory;

    public Optional<ChunkResolution> resolve(String text) {
        Optional<POSTaggerME> posTaggerOpt = loadSetFactory.posTagger(TrainingSetType.INITIAL);
        if (posTaggerOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<ChunkerME> chunkerOpt = loadSetFactory.chunker(TrainingSetType.INITIAL);
        if (chunkerOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<DictionaryLemmatizer> lemmatizerOpt = loadSetFactory.lemmatizer(TrainingSetType.INITIAL);
        if (lemmatizerOpt.isEmpty()) {
            return Optional.empty();
        }

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);
        if (tokens == null || tokens.length == 0) {
            return Optional.empty();
        }

        POSTaggerME posTagger = posTaggerOpt.get();
        String[] tags = posTagger.tag(tokens);
        if (tags == null || tags.length == 0) {
            return Optional.empty();
        }

        String[] chunks = chunkerOpt.get().chunk(tokens, tags);
        if (chunks == null || chunks.length == 0) {
            return Optional.empty();
        }

        String[] lemmatizers = lemmatizerOpt.get().lemmatize(tokens, tags);
        if (lemmatizers == null || lemmatizers.length == 0) {
            return Optional.empty();
        }

        if (!isSameLengths(tokens, tags, chunks, lemmatizers)) {
            return Optional.empty();
        }

        ChunkResolution chunkResolution = ChunkResolution.builder()
                .tokens(Arrays.asList(tokens))
                .tags(Arrays.asList(tags))
                .chunks(Arrays.asList(chunks))
                .lemmatizers(Arrays.asList(lemmatizers))
                .build();
        return Optional.of(chunkResolution);
    }

    private boolean isSameLengths(String[]... arrays) {
        return Stream.of(arrays)
                .map(a -> a.length)
                .distinct()
                .count() == 1;
    }
}

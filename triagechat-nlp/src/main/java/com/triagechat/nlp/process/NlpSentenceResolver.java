package com.triagechat.nlp.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.triagechat.nlp.training.loadset.LoadSetFactory;
import com.triagechat.nlp.training.loadset.model.TrainingSetType;

@Slf4j
@RequiredArgsConstructor
@Component
public class NlpSentenceResolver {

    private final LoadSetFactory loadSetFactory;

    public List<String> resolve(String text) {
        Optional<SentenceDetectorME> sentenceDetectorOpt = loadSetFactory.sentenceModel(TrainingSetType.INITIAL);
        if (sentenceDetectorOpt.isEmpty()) {
            return Collections.emptyList();
        }

        SentenceDetectorME sentenceDetector = sentenceDetectorOpt.get();
        String[] sentences = sentenceDetector.sentDetect(text);
        return Optional.of(sentences)
                .filter(s -> s.length != 0)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
}

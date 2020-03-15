package com.triagechat.nlp.training.loadset;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.triagechat.nlp.training.loadset.model.TrainingSetType;

@Slf4j
@Component
public class LoadSetFactory {

    private static final String SENTENCE_PRE_TRAIN_PATH = "/triagechat/nlp/training/model/01-pretrained/en-sent.bin";
    private static final String TAGGING_PRE_TRAIN_PATH = "/triagechat/nlp/training/model/01-pretrained/en-pos-maxent.bin";
    private static final String CHUNKING_PRE_TRAIN_PATH = "/triagechat/nlp/training/model/01-pretrained/en-chunker.bin";

    public Optional<SentenceDetectorME> sentenceModel(TrainingSetType trainingSetType) {
        String trainingPath;
        switch (trainingSetType) {
            case INITIAL:
                trainingPath = SENTENCE_PRE_TRAIN_PATH;
                break;
            default:
                throw new RuntimeException("No training path for creating sentences.");
        }
        return this.getSentenceModel(trainingPath);
    }

    private Optional<SentenceDetectorME> getSentenceModel(String trainingPath) {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream(trainingPath);
            SentenceModel sentenceModel = new SentenceModel(inputStream);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

            return Optional.of(sentenceDetector);
        } catch (Exception e) {
            log.error("Could not create sentence detector due to an exception.", e);
            return Optional.empty();
        } finally {
            closeStream(inputStream);
        }
    }

    public Optional<POSTaggerME> posTagger(TrainingSetType trainingSetType) {
        String trainingPath;
        switch (trainingSetType) {
            case INITIAL:
                trainingPath = TAGGING_PRE_TRAIN_PATH;
                break;
            default:
                throw new RuntimeException("No training path for tagging.");
        }
        return this.getPosTagger(trainingPath);
    }

    private Optional<POSTaggerME> getPosTagger(String trainingPath) {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream(trainingPath);
            POSModel posModel = new POSModel(inputStream);
            POSTaggerME posTagger = new POSTaggerME(posModel);
            return Optional.of(posTagger);
        } catch (Exception e) {
            log.error("Could not create sentence detector due to an exception.", e);
            return Optional.empty();
        } finally {
            closeStream(inputStream);
        }
    }

    public Optional<ChunkerME> chunker(TrainingSetType trainingSetType) {
        String trainingPath;
        switch (trainingSetType) {
            case INITIAL:
                trainingPath = CHUNKING_PRE_TRAIN_PATH;
                break;
            default:
                throw new RuntimeException("No training path for chunking.");
        }
        return this.getChunker(trainingPath);
    }

    private Optional<ChunkerME> getChunker(String trainingPath) {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream(trainingPath);
            ChunkerModel chunkerModel = new ChunkerModel(inputStream);
            ChunkerME chunker = new ChunkerME(chunkerModel);
            return Optional.of(chunker);
        } catch (Exception e) {
            log.error("Could not create sentence detector due to an exception.", e);
            return Optional.empty();
        } finally {
            closeStream(inputStream);
        }
    }

    private void closeStream(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }

        try {
            inputStream.close();
        } catch (Exception e) {
            log.error("Could not close input stream.", e);
        }
    }
}

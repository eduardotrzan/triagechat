package com.triagechat.nlp.process;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.triagechat.nlp.training.loadset.LoadSetFactory;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class NlpSentenceResolverFullTest {

    private LoadSetFactory loadSetFactory = new LoadSetFactory();

    private NlpSentenceResolver nlpSentenceResolver;

    @Before
    public void setup() {
        this.nlpSentenceResolver = new NlpSentenceResolver(
                loadSetFactory
        );
    }

    @Test
    public void resolveSentences() {
        String paragraph = "This is a statement. This is another statement."
                + "Now is an abstract word for time, "
                + "that is always flying. And my email address is google@gmail.com.";

        List<String> sentences = this.nlpSentenceResolver.resolve(paragraph);

        log.info("sentences={}", sentences);
    }

}

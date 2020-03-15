package com.triagechat.service.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.triagechat.domain.entity.Message;
import com.triagechat.domain.entity.MessageTag;
import com.triagechat.domain.entity.Tag;
import com.triagechat.domain.repo.MessageTagRepository;
import com.triagechat.domain.repo.TagRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class TagService {

    private final MessageTagRepository messageTagRepository;
    private final TagRepository tagRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Tag create(Tag entity, Message message) {
        Tag tag = this.tagRepository.save(entity);
        MessageTag messageTag = MessageTag.builder()
                .message(message)
                .tag(tag)
                .build();
        this.messageTagRepository.save(messageTag);
        return tag;
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public Optional<Tag> findByUuid(UUID tagUuid) {
        return this.tagRepository.findByUuid(tagUuid);
    }

}

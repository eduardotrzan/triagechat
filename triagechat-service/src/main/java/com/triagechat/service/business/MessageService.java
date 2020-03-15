package com.triagechat.service.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.triagechat.domain.entity.Message;
import com.triagechat.domain.repo.MessageRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Message create(Message entity) {
        return this.messageRepository.save(entity);
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public Optional<Message> findByUuid(UUID messageUuid) {
        return this.messageRepository.findByUuid(messageUuid);
    }

}

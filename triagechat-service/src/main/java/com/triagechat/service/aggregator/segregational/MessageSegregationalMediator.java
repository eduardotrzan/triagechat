package com.triagechat.service.aggregator.segregational;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.triagechat.domain.entity.Message;
import com.triagechat.domain.entity.Tag;
import com.triagechat.dto.request.MessageCreateDto;
import com.triagechat.dto.response.MessageDto;
import com.triagechat.dto.response.TagDto;
import com.triagechat.service.aggregator.transactional.MessageTransactionalMediator;
import com.triagechat.service.aggregator.transactional.TagTransactionalMediator;
import com.triagechat.service.validation.NotFoundException;

@RequiredArgsConstructor
@Component
public class MessageSegregationalMediator {

    private final MessageTransactionalMediator messageTransactionalMediator;
    private final TagTransactionalMediator tagTransactionalMediator;

    @Transactional(propagation = Propagation.NEVER)
    public MessageDto create(MessageCreateDto request) {
        MessageDto message = this.messageTransactionalMediator.create(request);
        UUID messageUuid = message.getUuid();

        // TODO create tag using NLP on message text
//        List<TagDto> tags = this.tagTransactionalMediator.create(messageUuid, Collections.emptyList());

        return this.messageTransactionalMediator
                .findByUuid(messageUuid)
                .orElseThrow(() -> new NotFoundException(Message.class.getSimpleName()));
    }

    @Transactional(propagation = Propagation.NEVER)
    public Optional<MessageDto> findByUuid(UUID messageUuid) {
        return this.messageTransactionalMediator.findByUuid(messageUuid);
    }
}

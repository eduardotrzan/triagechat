package com.triagechat.service.aggregator.transactional;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.triagechat.domain.entity.Message;
import com.triagechat.dto.request.MessageCreateDto;
import com.triagechat.dto.response.MessageDto;
import com.triagechat.service.business.MessageService;
import com.triagechat.service.mapper.MessageMapper;
import com.triagechat.service.validation.NotFoundException;

@RequiredArgsConstructor
@Component
public class MessageTransactionalMediator {

    private final MessageService messageService;

    private final MessageMapper messageMapper;

    @Transactional
    public MessageDto create(MessageCreateDto request) {
        Message entity = this.messageMapper.toNewEntity(request);
        Message savedEntity = this.messageService.create(entity);

        return this.messageMapper
                .toDto(savedEntity)
                .orElseThrow(() -> new NotFoundException(Message.class.getSimpleName()));
    }

    @Transactional(readOnly = true)
    public Optional<MessageDto> findByUuid(UUID messageUuid) {
        return this.messageService
                .findByUuid(messageUuid)
                .flatMap(messageMapper::toDto);
    }

}

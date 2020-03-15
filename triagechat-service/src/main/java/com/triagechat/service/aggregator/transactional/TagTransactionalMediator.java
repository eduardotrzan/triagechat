package com.triagechat.service.aggregator.transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.triagechat.domain.entity.Message;
import com.triagechat.domain.entity.Tag;
import com.triagechat.dto.response.TagDto;
import com.triagechat.service.business.MessageService;
import com.triagechat.service.business.TagService;
import com.triagechat.service.mapper.TagMapper;
import com.triagechat.service.validation.BadRequestException;
import com.triagechat.service.validation.NotFoundException;

@RequiredArgsConstructor
@Component
public class TagTransactionalMediator {

    private final TagService tagService;
    private final MessageService messageService;

    private final TagMapper tagMapper;

    @Transactional
    public List<TagDto> create(UUID messageUuid, List<Tag> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            throw new BadRequestException(Tag.class.getSimpleName());
        }

        Message message = findMessageForUuid(messageUuid);
        return tags
                .stream()
                .map(t -> this.doCreateTag(t, message))
                .collect(Collectors.toList());
    }

    @Transactional
    public TagDto create(UUID messageUuid, Tag tag) {
        Message message = findMessageForUuid(messageUuid);

        return doCreateTag(tag, message);
    }

    private Message findMessageForUuid(UUID messageUuid) {
        return this.messageService.findByUuid(messageUuid)
                    .orElseThrow(() -> new NotFoundException(Message.class.getSimpleName()));
    }

    private TagDto doCreateTag(Tag tag, Message message) {
        Tag savedEntity = this.tagService.create(tag, message);

        return this.tagMapper
                .toDto(savedEntity)
                .orElseThrow(() -> new NotFoundException(Tag.class.getSimpleName()));
    }

    @Transactional(readOnly = true)
    public Optional<TagDto> findByUuid(UUID tagUuid) {
        return this.tagService
                .findByUuid(tagUuid)
                .flatMap(tagMapper::toDto);
    }

}

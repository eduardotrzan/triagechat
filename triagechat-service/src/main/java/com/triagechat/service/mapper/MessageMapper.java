package com.triagechat.service.mapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.triagechat.domain.entity.Message;
import com.triagechat.dto.request.MessageCreateDto;
import com.triagechat.dto.response.MessageDto;
import com.triagechat.dto.response.TagDto;

@RequiredArgsConstructor
@Component
public class MessageMapper {

    private final TagMapper tagMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<MessageDto> toDto(Message entity) {
        if (entity == null) {
            return Optional.empty();
        }

        MessageDto dto = buildDto(entity);
        return Optional.of(dto);
    }

    private MessageDto buildDto(Message entity) {
        List<TagDto> tags = this.tagMapper.toDtos(entity.getMessageTags());

        return MessageDto.builder()
                .uuid(entity.getUuid())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .text(entity.getText())
                .tags(tags)
                .build();
    }

    public Message toNewEntity(MessageCreateDto request) {
        Objects.requireNonNull(request);

        return Message.builder()
                .text(request.getText())
                .build();
    }

}

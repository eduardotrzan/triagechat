package com.triagechat.service.mapper;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.triagechat.domain.entity.MessageTag;
import com.triagechat.domain.entity.Tag;
import com.triagechat.dto.response.TagDto;

@RequiredArgsConstructor
@Component
public class TagMapper {

    @Transactional(propagation = Propagation.MANDATORY)
    public List<TagDto> toDtos(List<MessageTag> entities) {
        return Objects.requireNonNullElse(entities, Collections.<MessageTag>emptyList())
                .stream()
                .map(this::toDto)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<TagDto> toDto(MessageTag entity) {
        if (entity == null || entity.getTag() == null) {
            return Optional.empty();
        }

        TagDto dto = buildDto(entity.getTag());
        return Optional.of(dto);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<TagDto> toDto(Tag entity) {
        if (entity == null) {
            return Optional.empty();
        }

        TagDto dto = buildDto(entity);
        return Optional.of(dto);
    }

    private TagDto buildDto(Tag entity) {

        return TagDto.builder()
                .uuid(entity.getUuid())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .name(entity.getName())
                .build();
    }
}

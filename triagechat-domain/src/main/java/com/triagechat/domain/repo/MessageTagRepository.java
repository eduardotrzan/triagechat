package com.triagechat.domain.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.triagechat.domain.entity.MessageTag;

@Repository
public interface MessageTagRepository extends JpaRepository<MessageTag, Long> {

    Optional<MessageTag> findByUuid(UUID messageTagUuid);

}

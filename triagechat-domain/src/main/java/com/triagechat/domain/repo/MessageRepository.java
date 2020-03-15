package com.triagechat.domain.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.triagechat.domain.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findByUuid(UUID messageUuid);

}

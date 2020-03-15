package com.triagechat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import javax.annotation.security.PermitAll;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.triagechat.dto.request.MessageCreateDto;
import com.triagechat.dto.response.MessageDto;
import com.triagechat.service.aggregator.segregational.MessageSegregationalMediator;
import com.triagechat.service.validation.NotFoundException;

@Api(value = "Message Management")
@RestController
@RequestMapping({ "/v1/messages" })
@RequiredArgsConstructor
public class MessageController {

    private final MessageSegregationalMediator messageSegregationalMediator;

    @ApiOperation(value = "Creates a Message.", response = MessageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully create an Car")
    })
    @PermitAll
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto create(@Validated @RequestBody MessageCreateDto request) {
        return this.messageSegregationalMediator.create(request);
    }

    @ApiOperation(value = "Retrieves an Message by its UUID.", response = MessageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieves a Message."),
            @ApiResponse(code = 401, message = "You are not authorized to view the Message."),
            @ApiResponse(code = 404, message = "The Message you were trying to reach is NOT FOUND.")
    })
    @PermitAll
    @GetMapping(value = "/{messageUuid}", produces = "application/json")
    public MessageDto findComment(@PathVariable(value = "messageUuid") UUID messageUuid) {
        return this.messageSegregationalMediator.findByUuid(messageUuid)
                .orElseThrow(() -> new NotFoundException(MessageDto.class.getSimpleName()));
    }
}

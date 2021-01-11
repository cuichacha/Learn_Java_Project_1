package com.tanhua.messages.controller;

import com.tanhua.commons.service.messages.MessageService;
import com.tanhua.commons.vo.messages.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/contacts")
    public ResponseEntity<Void> addContact(@RequestHeader("Authorization") String token,
                                           @RequestBody Map<String, Object> param) {

        Long userId = Long.valueOf(param.get("userId").toString());
        Boolean result = messageService.addContact(token, userId);
        if (result) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/contacts")
    public ResponseEntity<MessageResult> queryContactsList(@RequestHeader("Authorization") String token,
                                                           @RequestParam(value = "page", defaultValue = "1") Integer startPage,
                                                           @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "keyword", required = false) String keyword) {

        MessageResult messageResult = messageService.queryContactsLists(token, startPage, pageSize, keyword);
        return ResponseEntity.ok(messageResult);
    }

    @GetMapping("/likes")
    public ResponseEntity<MessageResult> queryMessageLikeList(@RequestHeader("Authorization") String token,
                                                              @RequestParam(value = "page", defaultValue = "1") Integer startPage,
                                                              @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {

        MessageResult messageResult = messageService.queryComments(token, startPage, pageSize, 1);
        return ResponseEntity.ok(messageResult);
    }

    @GetMapping("/comments")
    public ResponseEntity<MessageResult> queryMessageCommentList(@RequestHeader("Authorization") String token,
                                                                 @RequestParam(value = "page", defaultValue = "1") Integer startPage,
                                                                 @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {

        MessageResult messageResult = messageService.queryComments(token, startPage, pageSize, 2);
        return ResponseEntity.ok(messageResult);
    }

    @GetMapping("/loves")
    public ResponseEntity<MessageResult> queryMessageLoveList(@RequestHeader("Authorization") String token,
                                                              @RequestParam(value = "page", defaultValue = "1") Integer startPage,
                                                              @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {

        MessageResult messageResult = messageService.queryComments(token, startPage, pageSize, 3);
        return ResponseEntity.ok(messageResult);
    }

    @GetMapping("/announcements")
    public ResponseEntity<MessageResult> queryAnnouncement(@RequestHeader("Authorization") String token,
                                                           @RequestParam(value = "page", defaultValue = "1") Integer startPage,
                                                           @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {

        MessageResult messageResult = messageService.queryAnnouncement(token, startPage, pageSize);
        return ResponseEntity.ok(messageResult);
    }

}

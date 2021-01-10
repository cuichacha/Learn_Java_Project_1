package com.tanhua.messages.controller;

import com.tanhua.commons.service.message.MessageService;
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


}

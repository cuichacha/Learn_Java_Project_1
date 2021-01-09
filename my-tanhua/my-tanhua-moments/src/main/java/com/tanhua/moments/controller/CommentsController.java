package com.tanhua.moments.controller;

import com.tanhua.commons.service.moments.CommentsService;
import com.tanhua.commons.vo.moments.MovementsResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping
    public ResponseEntity<MovementsResult> queryComments(@RequestHeader("Authorization") String token,
                                                         @RequestParam("movementId") ObjectId movementPublishId,
                                                         @RequestParam("page") Integer startPage,
                                                         @RequestParam("pagesize") Integer pageSize) {

        MovementsResult movementsResult = commentsService.queryComments(token, movementPublishId, startPage, pageSize);
        return ResponseEntity.ok(movementsResult);
    }

    @PostMapping
    public ResponseEntity<Void> publishComment(@RequestHeader("Authorization") String token,
                                               @RequestBody Map<String, String> param) {
        String publishId = param.get("movementId");
        ObjectId movementPublishId = new ObjectId(publishId);
        String content = param.get("comment");
        Boolean result = commentsService.publishComment(token, movementPublishId, content);
        if (result) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<Long> likeComment(@RequestHeader("Authorization") String token,
                                            @PathVariable("id") ObjectId publishId) {
        Long result = commentsService.likeComment(token, publishId);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}/dislike")
    public ResponseEntity<Long> disLikeComment(@RequestHeader("Authorization") String token,
                                               @PathVariable("id") ObjectId publishId) {
        Long result = commentsService.dislikeComment(token, publishId);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

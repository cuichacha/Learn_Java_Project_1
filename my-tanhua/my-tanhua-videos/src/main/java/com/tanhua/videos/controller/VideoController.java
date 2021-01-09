package com.tanhua.videos.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tanhua.commons.service.moments.CommentsService;
import com.tanhua.commons.service.moments.MovementsService;
import com.tanhua.commons.service.videos.VideoService;
import com.tanhua.commons.vo.moments.MovementsResult;
import com.tanhua.commons.vo.videos.VideoResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/smallVideos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Reference
    private MovementsService movementsService;

    @Reference
    private CommentsService commentsService;

    @GetMapping
    public ResponseEntity<VideoResult> queryVideoList(@RequestHeader("Authorization") String token,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer startPage,
                                                      @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {

        VideoResult videoResult = videoService.queryVideoList(token, startPage, pageSize);
        if (videoResult != null) {
            return ResponseEntity.ok(videoResult);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping
    public ResponseEntity<Void> publishVideo(@RequestHeader("Authorization") String token,
                                             @RequestParam(value = "videoThumbnail", required = false) MultipartFile thumbNail,
                                             @RequestParam(value = "videoFile", required = false) MultipartFile videoFile) {

        Boolean result = videoService.publishVideo(token, thumbNail, videoFile);
        if (result) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @PostMapping("/{id}/like")
    public ResponseEntity<Long> likeVideo(@RequestHeader("Authorization") String token,
                                          @PathVariable("id") ObjectId videoId) {

        Long result = movementsService.supportMovement(token, videoId, 1);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("{id}/dislike")
    public ResponseEntity<Long> disLikeVideo(@RequestHeader("Authorization") String token,
                                             @PathVariable("id") ObjectId publishId) {
        try {
            Long likeCount = this.movementsService.opposeMovement(token, publishId, 1);
            if (null != likeCount) {
                return ResponseEntity.ok(likeCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<MovementsResult> queryVideoCommentsList(@RequestHeader("Authorization") String token,
                                                                  @PathVariable("id") ObjectId videoId,
                                                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize) {
        MovementsResult movementsResult = commentsService.queryComments(token, videoId, page, pagesize);
        if (movementsResult != null) {
            return ResponseEntity.ok(movementsResult);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> publishVideoComments(@RequestHeader("Authorization") String token,
                                                     @PathVariable("id") ObjectId videoId,
                                                     @RequestBody Map<String, String> param) {

        String comment = param.get("comment");
        Boolean result = commentsService.publishComment(token, videoId, comment);
        if (result) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/comments/{id}/like")
    public ResponseEntity<Long> likeVideoComment(@RequestHeader("Authorization") String token,
                                                 @PathVariable("id") ObjectId publishId) {
        Long result = commentsService.likeComment(token, publishId);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/comments/{id}/dislike")
    public ResponseEntity<Long> dislikeVideoComment(@RequestHeader("Authorization") String token,
                                                    @PathVariable("id") String publishId) {
        Long result = commentsService.dislikeComment(token, new ObjectId(publishId));
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/{id}/userFocus")
    public ResponseEntity<Void> subscribe(@RequestHeader("Authorization") String token,
                                          @PathVariable("id") Long userId) {

        Boolean result = videoService.subscribe(token, userId);
        if (result != null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/{id}/userUnFocus")
    public ResponseEntity<Void> unsubscribe(@RequestHeader("Authorization") String token,
                                            @PathVariable("id") Long userId) {

        Boolean result = videoService.subscribe(token, userId);
        if (result != null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}

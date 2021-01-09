package com.tanhua.moments.controller;

import com.tanhua.commons.annotation.Cache;
import com.tanhua.commons.pojo.moments.Movements;
import com.tanhua.commons.service.moments.MovementsService;
import com.tanhua.commons.vo.moments.MovementsResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movements")
public class MovementsController {

    @Autowired
    private MovementsService movementsService;


    @PostMapping
    public ResponseEntity<Void> publishMoment(@RequestHeader("Authorization") String token,
                                              @RequestParam(value = "textContent", required = false) String textContent,
                                              @RequestParam(value = "location", required = false) String location,
                                              @RequestParam(value = "latitude", required = false) String latitude,
                                              @RequestParam(value = "longitude", required = false) String longitude,
                                              @RequestParam(value = "imageContent", required = false) MultipartFile[] multipartFile) {

        Boolean result = movementsService.publishMoment(token, textContent, location, latitude, longitude, multipartFile);
        if (result) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping
//    @Cache(time = "60")
    public MovementsResult queryFriendsMovements(@RequestHeader("Authorization") String token,
                                                 @RequestParam(value = "page") Integer startPage,
                                                 @RequestParam(value = "pagesize") Integer pageSize) {

        return movementsService.queryFriendsMovements(token, startPage, pageSize);
    }

    @GetMapping("/recommend")
//    @Cache(time = "60")
    public MovementsResult queryRecommendedMovements(@RequestHeader("Authorization") String token,
                                                     @RequestParam(value = "page") Integer startPage,
                                                     @RequestParam(value = "pagesize") Integer pageSize) {

        return movementsService.queryFriendsMovements(token, startPage, pageSize);
    }


    @GetMapping("/{id}/like")
    public ResponseEntity<Long> likeMovement(@RequestHeader("Authorization") String token,
                                             @PathVariable("id") ObjectId publishId) {
        Long result = movementsService.supportMovement(token, publishId, 1);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}/dislike")
    public ResponseEntity<Long> disLikeMovement(@RequestHeader("Authorization") String token,
                                                @PathVariable("id") ObjectId publishId) {
        Long result = movementsService.opposeMovement(token, publishId, 1);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}/love")
    public ResponseEntity<Long> loveMovement(@RequestHeader("Authorization") String token,
                                             @PathVariable("id") ObjectId publishId) {
        Long result = movementsService.supportMovement(token, publishId, 3);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}/unlove")
    public ResponseEntity<Long> disLoveMovement(@RequestHeader("Authorization") String token,
                                                @PathVariable("id") ObjectId publishId) {
        Long result = movementsService.opposeMovement(token, publishId, 3);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movements> querySingleMovement(@RequestHeader("Authorization") String token,
                                                         @PathVariable("id") ObjectId publishId) {
        Movements movements = movementsService.querySingleMovement(token, publishId);
        if (movements != null) {
            return ResponseEntity.ok(movements);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

}

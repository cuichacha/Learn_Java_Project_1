package com.tanhua.moments.controller;

import com.tanhua.commons.annotation.Cache;
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


    @PostMapping()
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
    public ResponseEntity<Long> likeComment(@RequestHeader("Authorization") String token,
                                            @PathVariable("id") ObjectId publishId) {
        Long result = movementsService.likeComment(token, publishId);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}

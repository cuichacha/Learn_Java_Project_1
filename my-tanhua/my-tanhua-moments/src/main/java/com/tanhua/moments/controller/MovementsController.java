package com.tanhua.moments.controller;

import com.tanhua.commons.service.moments.MovementsService;
import com.tanhua.commons.vo.moments.MovementsResult;
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
    public MovementsResult queryFriendsMovements(@RequestHeader("Authorization") String token,
                                                 @RequestParam(value = "page", required = false) Integer startPage,
                                                 @RequestParam(value = "pagesize", required = false) Integer pageSize) {

        MovementsResult movementsResult = movementsService.queryFriendsMovements(token, startPage, pageSize);
        return movementsResult;
    }

    @GetMapping("/recommend")
    public MovementsResult queryRecommendedMovements(@RequestHeader("Authorization") String token,
                                                     @RequestParam(value = "page" ,required = false) Integer startPage,
                                                     @RequestParam(value = "pagesize", required = false) Integer pageSize) {

        MovementsResult movementsResult = movementsService.queryFriendsMovements(token, startPage, pageSize);
        return movementsResult;
    }




}

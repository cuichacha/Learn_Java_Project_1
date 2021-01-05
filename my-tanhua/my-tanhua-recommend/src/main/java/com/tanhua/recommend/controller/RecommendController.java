package com.tanhua.recommend.controller;

import com.tanhua.commons.pojo.recommend.QueryUser;
import com.tanhua.commons.service.recommend.RecommendService;
import com.tanhua.commons.vo.recommend.RecommendUsers;
import com.tanhua.commons.vo.sso.ErrorResult;
import com.tanhua.commons.vo.recommend.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tanhua")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/todayBest")
    public ResponseEntity<TodayBest> todayBest(@RequestHeader("Authorization") String token) {
        TodayBest todayBest = recommendService.findTodayBest(token);
        if (todayBest == null) {
            ErrorResult errorResult = new ErrorResult();
            errorResult.setErrCode("006");
            errorResult.setErrMessage("查询出错！未查询到今日佳人数据！");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(todayBest);
    }

    @GetMapping("/recommendation")
    public ResponseEntity<RecommendUsers> findRecommendUsers(@RequestHeader("Authorization") String token, QueryUser queryUser) {
        RecommendUsers recommendUsers = recommendService.findRecommendUsers(token, queryUser);
        if (recommendUsers == null) {
            ErrorResult errorResult = new ErrorResult();
            errorResult.setErrCode("007");
            errorResult.setErrMessage("查询出错！未查询到推荐列表数据！");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(recommendUsers);
    }

}
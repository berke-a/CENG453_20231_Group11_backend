package com.example.ceng453_20231_group11_backend.controller;

import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.enums.LeaderboardInterval;
import com.example.ceng453_20231_group11_backend.service.ScoreService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/scoreboard", headers = "Accept=application/json")
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getLeaderboard(LeaderboardInterval interval) {
        if (interval.equals(LeaderboardInterval.WEEKLY)) {
            Pair<HttpStatus, ResponseDTO> serviceResponse = scoreService.getWeeklyLeaderboard();
            return ResponseEntity.status(serviceResponse.getFirst()).body(serviceResponse.getSecond());
        } else if (interval.equals(LeaderboardInterval.MONTHLY)) {
            Pair<HttpStatus, ResponseDTO> serviceResponse = scoreService.getMonthlyLeaderboard();
            return ResponseEntity.status(serviceResponse.getFirst()).body(serviceResponse.getSecond());
        } else {
            Pair<HttpStatus, ResponseDTO> serviceResponse = scoreService.getAllTimeLeaderboard();
            return ResponseEntity.status(serviceResponse.getFirst()).body(serviceResponse.getSecond());
        }
    }

}

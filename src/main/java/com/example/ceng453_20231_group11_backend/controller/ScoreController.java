package com.example.ceng453_20231_group11_backend.controller;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.NewScoreDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.enums.LeaderboardInterval;
import com.example.ceng453_20231_group11_backend.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Operation(summary = "Get Leaderboard",
            description = """
                    Retrieves the leaderboard for a specified time interval.
                    The interval can be weekly, monthly, or all-time.
                    """)
    public ResponseEntity<ResponseDTO> getLeaderboard(LeaderboardInterval interval) {
        if (interval.equals(LeaderboardInterval.WEEKLY)) {
            Pair<HttpStatus, ResponseDTO> serviceResponse = scoreService.getWeeklyLeaderboard();
            return ResponseEntity.status(serviceResponse.getFirst()).body(serviceResponse.getSecond());
        } else if (interval.equals(LeaderboardInterval.MONTHLY)) {
            Pair<HttpStatus, ResponseDTO> serviceResponse = scoreService.getMonthlyLeaderboard();
            return ResponseEntity.status(serviceResponse.getFirst()).body(serviceResponse.getSecond());
        } else if (interval.equals(LeaderboardInterval.ALLTIME)) {
            Pair<HttpStatus, ResponseDTO> serviceResponse = scoreService.getAllTimeLeaderboard();
            return ResponseEntity.status(serviceResponse.getFirst()).body(serviceResponse.getSecond());
        } else {
            return ResponseEntity.badRequest().body(new ResponseDTO(null, "Bad leaderboard request.", APIConstants.RESPONSE_FAIL));
        }
    }

    @PostMapping
    @Operation(summary = "Add Score",
            description = """
                    Adds a score to the database.
                    """)
    public ResponseEntity<ResponseDTO> addScore(NewScoreDTO newScore) {
        try {
            Pair<HttpStatus, ResponseDTO> serviceResponse = scoreService.addScore(newScore);
            return ResponseEntity.status(serviceResponse.getFirst()).body(serviceResponse.getSecond());
        } catch (Exception e) {
            log.error("Error during score addition: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error during score addition. " + e.getMessage(), APIConstants.RESPONSE_FAIL));
        }
    }

}

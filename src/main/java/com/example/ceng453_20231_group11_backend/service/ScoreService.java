package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.repository.ScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public Pair<HttpStatus, ResponseDTO> getWeeklyLeaderboard() {
        return Pair.of(HttpStatus.OK, new ResponseDTO(scoreRepository.getLeaderboardWeekly(), null, APIConstants.RESPONSE_SUCCESS));
    }

    public Pair<HttpStatus, ResponseDTO> getMonthlyLeaderboard() {
        return Pair.of(HttpStatus.OK, new ResponseDTO(scoreRepository.getLeaderboardMonthly(), null, APIConstants.RESPONSE_SUCCESS));
    }

    public Pair<HttpStatus, ResponseDTO> getAllTimeLeaderboard() {
        return Pair.of(HttpStatus.OK, new ResponseDTO(scoreRepository.getLeaderboardAllTime(), null, APIConstants.RESPONSE_SUCCESS));
    }


}

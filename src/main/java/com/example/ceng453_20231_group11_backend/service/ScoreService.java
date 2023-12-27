package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.NewScoreDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.entity.User;
import com.example.ceng453_20231_group11_backend.repository.ScoreRepository;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;

    public ScoreService(ScoreRepository scoreRepository, UserRepository userRepository) {
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
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

    public Pair<HttpStatus, ResponseDTO> addScore(NewScoreDTO newScore) throws ParseException {
        User user = userRepository.findByUsername(newScore.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());

        scoreRepository.addScore(user.getId(), newScore.getScore(), sqlDate);
        return Pair.of(HttpStatus.OK, new ResponseDTO(null, "Score added successfully.", APIConstants.RESPONSE_SUCCESS));
    }

}

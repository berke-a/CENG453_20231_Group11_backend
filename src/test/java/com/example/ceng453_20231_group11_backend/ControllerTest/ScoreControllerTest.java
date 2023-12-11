package com.example.ceng453_20231_group11_backend.ControllerTest;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.controller.ScoreController;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.enums.LeaderboardInterval;
import com.example.ceng453_20231_group11_backend.service.ScoreService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScoreControllerTest {

    @Autowired
    private ScoreController scoreController;

    @MockBean
    private ScoreService scoreService;

    @Test
    public void testGetLeaderboardWeekly() {
        ArrayList<Map<String, Long>> mockWeeklyLeaderboard = new ArrayList<>();
        Map<String, Long> mockLeaderboardEntry = new HashMap<>();
        mockLeaderboardEntry.put("testUser1", 100L);
        mockWeeklyLeaderboard.add(mockLeaderboardEntry);
        mockLeaderboardEntry.clear();
        mockLeaderboardEntry.put("testUser2", 150L);
        mockWeeklyLeaderboard.add(mockLeaderboardEntry);
        mockLeaderboardEntry.clear();
        mockLeaderboardEntry.put("testUser3", 50L);
        mockWeeklyLeaderboard.add(mockLeaderboardEntry);

        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK, new ResponseDTO(mockWeeklyLeaderboard, null, APIConstants.RESPONSE_SUCCESS));
        when(scoreService.getWeeklyLeaderboard()).thenReturn(response);
        Assert.assertEquals(response.getSecond(), scoreController.getLeaderboard(LeaderboardInterval.WEEKLY).getBody());
    }

    @Test
    public void testGetLeaderboardMonthly() {
        ArrayList<Map<String, Long>> mockMonthlyLeaderboard = new ArrayList<>();
        Map<String, Long> mockLeaderboardEntry = new HashMap<>();
        mockLeaderboardEntry.put("testUser1", 500L);
        mockMonthlyLeaderboard.add(mockLeaderboardEntry);
        mockLeaderboardEntry.clear();
        mockLeaderboardEntry.put("testUser2", 350L);
        mockMonthlyLeaderboard.add(mockLeaderboardEntry);
        mockLeaderboardEntry.clear();
        mockLeaderboardEntry.put("testUser3", 750L);
        mockMonthlyLeaderboard.add(mockLeaderboardEntry);

        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK, new ResponseDTO(mockMonthlyLeaderboard, null, APIConstants.RESPONSE_SUCCESS));
        when(scoreService.getMonthlyLeaderboard()).thenReturn(response);
        Assert.assertEquals(response.getSecond(), scoreController.getLeaderboard(LeaderboardInterval.MONTHLY).getBody());
    }

    @Test
    public void testGetLeaderboardAllTime() {
        ArrayList<Map<String, Long>> mockAllTimeLeaderboard = new ArrayList<>();
        Map<String, Long> mockLeaderboardEntry = new HashMap<>();
        mockLeaderboardEntry.put("testUser1", 1000L);
        mockAllTimeLeaderboard.add(mockLeaderboardEntry);
        mockLeaderboardEntry.clear();
        mockLeaderboardEntry.put("testUser2", 1500L);
        mockAllTimeLeaderboard.add(mockLeaderboardEntry);
        mockLeaderboardEntry.clear();
        mockLeaderboardEntry.put("testUser3", 1250L);
        mockAllTimeLeaderboard.add(mockLeaderboardEntry);

        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK, new ResponseDTO(mockAllTimeLeaderboard, null, APIConstants.RESPONSE_SUCCESS));
        when(scoreService.getAllTimeLeaderboard()).thenReturn(response);
        Assert.assertEquals(response.getSecond(), scoreController.getLeaderboard(LeaderboardInterval.ALLTIME).getBody());
    }

}

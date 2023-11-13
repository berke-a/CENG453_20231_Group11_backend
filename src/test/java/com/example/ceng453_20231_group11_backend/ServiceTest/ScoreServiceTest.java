package com.example.ceng453_20231_group11_backend.ServiceTest;

import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.repository.ScoreRepository;
import com.example.ceng453_20231_group11_backend.service.ScoreService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScoreServiceTest {

    @Autowired
    ScoreService scoreService;

    @MockBean
    ScoreRepository scoreRepository;

    @Test
    public void testGetWeeklyLeaderboard() {
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

        when(scoreRepository.getLeaderboardWeekly()).thenReturn(mockWeeklyLeaderboard);
        ResponseDTO response = scoreService.getWeeklyLeaderboard().getSecond();
        Assert.assertEquals(mockWeeklyLeaderboard, response.getData());
    }

    @Test
    public void testGetMonthlyLeaderboard() {
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

        when(scoreRepository.getLeaderboardMonthly()).thenReturn(mockMonthlyLeaderboard);
        ResponseDTO response = scoreService.getMonthlyLeaderboard().getSecond();
        Assert.assertEquals(mockMonthlyLeaderboard, response.getData());
    }

    @Test
    public void testGetAllTimeLeaderboard() {
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

        when(scoreRepository.getLeaderboardAllTime()).thenReturn(mockAllTimeLeaderboard);
        ResponseDTO response = scoreService.getAllTimeLeaderboard().getSecond();
        Assert.assertEquals(mockAllTimeLeaderboard, response.getData());
    }

}

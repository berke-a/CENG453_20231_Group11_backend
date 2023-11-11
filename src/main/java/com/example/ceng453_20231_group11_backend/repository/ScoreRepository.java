package com.example.ceng453_20231_group11_backend.repository;

import com.example.ceng453_20231_group11_backend.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query(value =
            "SELECT user.username, totalScoreTable.score " +
            "FROM user, " +
            "   (SELECT score.user_id, SUM(score.score_value) AS score " +
            "    FROM score " +
            "    WHERE score.created_at >= CURDATE() - INTERVAL 7 DAY " +
            "    GROUP BY score.user_id) as totalScoreTable " +
            "WHERE totalScoreTable.user_id = user.id " +
            "ORDER BY totalScoreTable.score DESC", nativeQuery = true)
    List<Map<String, Long>> getLeaderboardWeekly();

    @Query(value =
            "SELECT user.username, totalScoreTable.score " +
            "FROM user, " +
            "   (SELECT score.user_id, SUM(score.score_value) AS score " +
            "    FROM score " +
            "    WHERE score.created_at >= CURDATE() - INTERVAL 30 DAY " +
            "    GROUP BY score.user_id) as totalScoreTable " +
            "WHERE totalScoreTable.user_id = user.id " +
            "ORDER BY totalScoreTable.score DESC", nativeQuery = true)
    List<Map<String, Long>> getLeaderboardMonthly();

    @Query(value =
            "SELECT user.username, totalScoreTable.score " +
            "FROM user, " +
            "   (SELECT score.user_id, SUM(score.score_value) AS score " +
            "    FROM score " +
            "    GROUP BY score.user_id) as totalScoreTable " +
            "WHERE totalScoreTable.user_id = user.id " +
            "ORDER BY totalScoreTable.score DESC", nativeQuery = true)
    List<Map<String, Long>> getLeaderboardAllTime();

}

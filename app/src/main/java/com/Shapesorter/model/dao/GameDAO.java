package com.shapesorter.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {
    // Save player score to database
    public void saveScore(String playerName, int score) throws SQLException {
        String sql = "INSERT INTO scores (player_name, score) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        }
    }

    // Get the highest score from database
    public int getHighScore() throws SQLException {
        String sql = "SELECT MAX(score) as high_score FROM scores";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getInt("high_score") : 0;
        }
    }

    // Get top 10 scores
    public List<String> getTopScores() throws SQLException {
        List<String> topScores = new ArrayList<>();
        String sql = "SELECT player_name, score FROM scores ORDER BY score DESC LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                topScores.add(rs.getString("player_name") + ": " + rs.getInt("score"));
            }
        }
        return topScores;
    }

    // Check if player exists
    public boolean playerExists(String playerName) throws SQLException {
        String sql = "SELECT 1 FROM scores WHERE player_name = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playerName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}

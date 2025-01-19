package com.matthieudeglon.shootme.Database;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:shootme.db";

    public static void createNewDatabase() {
        File dbFile = new File("shootme.db");

        if (!dbFile.exists()) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (conn != null) {
                    System.out.println("A new database has been created.");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Database already exists.");
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS players ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " victories INTEGER DEFAULT 0,"
                + " games_played INTEGER DEFAULT 0"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addPlayerOrUpdate(String username) {
        if (playerExists(username)) {
            incrementGamesPlayed(username);
        } else {
            addPlayer(username);
        }
    }

    private static void addPlayer(String username) {
        String sql = "INSERT INTO players(username) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void incrementGamesPlayed(String username) {
        String sql = "UPDATE players SET games_played = games_played + 1 WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean playerExists(String username) {
        String sql = "SELECT COUNT(*) FROM players WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static ArrayList<String> getAllUsernames() {
        String sql = "SELECT username FROM players";
        ArrayList<String> usernames = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return usernames;
    }

    public static void incrementVictories(String username) {
        String sql = "UPDATE players SET victories = victories + 1 WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



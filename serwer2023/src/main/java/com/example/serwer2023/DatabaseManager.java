package com.example.serwer2023;

import java.io.File;
import java.sql.*;

public class DatabaseManager {
    static {
        try {
            File dbFile = new File("images/index.db");
            if (!dbFile.exists()) {
                Connection conn = connect();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS blur_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        path TEXT,
                        size INTEGER,
                        delay INTEGER
                    );
                """);
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:images/index.db");
    }

    public static void saveToDatabase(String path, int size, int delay) {
        try (Connection conn = connect()) {
            PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO blur_logs (path, size, delay) VALUES (?, ?, ?)
            """);
            ps.setString(1, path);
            ps.setInt(2, size);
            ps.setInt(3, delay);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

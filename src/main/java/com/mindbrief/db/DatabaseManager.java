package com.mindbrief.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:mindbrief.db");
            createTableIfNotExists();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS summaries (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                source TEXT,
                original_length INTEGER,
                summary_text TEXT,
                method TEXT,
                created_at TEXT
            )
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}

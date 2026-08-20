package com.mindbrief.db;

import com.mindbrief.model.SummaryRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SummaryDAO {
    public void save(SummaryRecord record) {
        String sql = "INSERT INTO summaries (source, original_length, summary_text, method, created_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, record.getSource());
            preparedStatement.setInt(2, record.getOriginalLength());
            preparedStatement.setString(3, record.getSummaryText());
            preparedStatement.setString(4, record.getMethod());
            preparedStatement.setString(5, record.getCreatedAt());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save summary", e);
        }
    }

    public List<SummaryRecord> getAll() {
        List<SummaryRecord> results = new ArrayList<>();
        String sql = "SELECT * FROM summaries ORDER BY id DESC";
        try (Statement statement = DatabaseManager.getInstance().getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                results.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch history", e);
        }
        return results;
    }

    public List<SummaryRecord> search(String keyword) {
        List<SummaryRecord> results = new ArrayList<>();
        String sql = "SELECT * FROM summaries WHERE source LIKE ? OR summary_text LIKE ? ORDER BY id DESC";
        try (PreparedStatement preparedStatement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            preparedStatement.setString(1, pattern);
            preparedStatement.setString(2, pattern);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Search failed", e);
        }
        return results;
    }

    private SummaryRecord mapRow(ResultSet resultSet) throws SQLException {
        return new SummaryRecord.Builder()
                .id(resultSet.getInt("id"))
                .source(resultSet.getString("source"))
                .originalLength(resultSet.getInt("original_length"))
                .summaryText(resultSet.getString("summary_text"))
                .method(resultSet.getString("method"))
                .createdAt(resultSet.getString("created_at"))
                .build();
    }
}

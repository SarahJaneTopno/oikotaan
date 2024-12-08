package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class RegisterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveUser(User user, String regId, LocalDateTime timestamp) {
        String sql = "INSERT INTO registrants (reg_id, email, type, name, leader_name, phone_number, city, details, accommodation_required, drive_link, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, regId, user.getEmail(), user.getType(), user.getName(), user.getLeaderName(),
                user.getPhoneNumber(), user.getCity(), user.getDetails(),
                user.getAccommodationRequired(), user.getDriveLink(), timestamp);
    }

    public void updateUser(User user, String regId) {
        String sql = "UPDATE registrants SET type = ?, name = ?, leader_name = ?, phone_number = ?, city = ?, details = ?, accommodation_required = ?, drive_link = ? " +
                "WHERE reg_id = ?";
        jdbcTemplate.update(sql, user.getType(), user.getName(), user.getLeaderName(), user.getPhoneNumber(),
                user.getCity(), user.getDetails(), user.getAccommodationRequired(), user.getDriveLink(), regId);
    }

    public String findRegIdByEmail(String email) {
        String sql = "SELECT reg_id FROM registrants WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, rs -> {
            if (rs.next()) {
                return rs.getString("reg_id");
            }
            return null;
        });
    }


    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM registrants";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
}
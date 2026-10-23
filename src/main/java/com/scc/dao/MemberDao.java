package com.scc.dao;

import com.scc.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {

    public boolean addMember(String fullName, String tier, double monthlyFee, String status) {
        // ID is not inserted: SQLite AUTOINCREMENT assigns 1, 2, 3, ...
        String sql = "INSERT INTO Members(full_name, tier, monthly_fee, status) VALUES(?, ?, ?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fullName);
            pstmt.setString(2, tier);
            pstmt.setDouble(3, monthlyFee);
            pstmt.setString(4, status);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Failed to save member: " + e.getMessage());
            return false;
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT id, full_name, tier, monthly_fee, status FROM Members ORDER BY id";

        try (Connection conn = databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("tier"),
                        rs.getDouble("monthly_fee"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching members: " + e.getMessage());
        }
        return members;
    }
}

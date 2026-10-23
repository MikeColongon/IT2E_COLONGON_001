package com.scc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {

        String membersTable = """
                CREATE TABLE IF NOT EXISTS Members (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
                    tier TEXT NOT NULL,
                    monthly_fee REAL NOT NULL,
                    status TEXT NOT NULL
                )""";

        try (Connection conn = databaseconnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(membersTable);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }

        new MemberDao();
        System.out.println("-> Database tables verified.");
    }
}

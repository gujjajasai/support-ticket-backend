package com.yourcompany; // Or your correct package

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final String DB_URL = "jdbc:postgresql://ep-solitary-hat-a12c6l1p-pooler.ap-southeast-1.aws.neon.tech/neondb?sslmode=require";
    private static final String DB_USER = "neondb_owner";
    private static final String DB_PASSWORD = "npg_PzamVFcO7lR2";

    private static HikariDataSource dataSource;

    // This static block initializes the connection pool when the application starts.
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);

        // --- HikariCP Professional Settings ---
        config.setMaximumPoolSize(10); // Max number of connections
        config.setMinimumIdle(5);      // Min number of idle connections
        config.setConnectionTimeout(30000); // How long to wait for a connection (30 seconds)
        config.setIdleTimeout(600000); // How long a connection can be idle (10 minutes)
        config.setMaxLifetime(1800000); // Max lifetime of a connection (30 minutes)
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        System.out.println("INFO: HikariCP connection pool initialized.");
    }

    private DatabaseConnectionManager() {}

    /**
     * Borrows a connection from the pool.
     * @return A healthy, ready-to-use database connection.
     * @throws SQLException if a connection cannot be obtained.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
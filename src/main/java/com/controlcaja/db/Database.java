package com.controlcaja.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Database {

    private static HikariDataSource dataSource;

    private Database() {}

    public static void init() {
        HikariConfig config = new HikariConfig();
        // Modo archivo: los datos persisten entre ejecuciones
        config.setJdbcUrl("jdbc:h2:./controlcaja_data;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(5);

        dataSource = new HikariDataSource(config);
        crearTablas();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) dataSource.close();
    }

    private static void crearTablas() {
        try (var is = Database.class.getClassLoader()
                .getResourceAsStream("schema.sql");
             var reader = new BufferedReader(new InputStreamReader(is));
             var conn   = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = reader.lines().collect(Collectors.joining("\n"));

            for (String sentencia : sql.split(";")) {
                String limpio = sentencia.trim();
                if (!limpio.isEmpty()) {
                    stmt.execute(limpio);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creando tablas: " + e.getMessage(), e);
        }
    }
}
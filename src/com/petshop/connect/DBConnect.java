package com.petshop.connect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {

    private static final String USERNAME = "sa";
    private static final String PASSWORD = "12345";
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "PETSHOP";
    private static final boolean USING_SSL = true;

    private static String CONNECT_STRING;
    private static Connection conn;

    static {
        try {
            // Nạp driver JDBC cho SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Xây dựng chuỗi kết nối
            StringBuilder connectStringBuilder = new StringBuilder();
            connectStringBuilder.append("jdbc:sqlserver://")
                    .append(SERVER).append(":").append(PORT).append(";")
                    .append("databaseName=").append(DATABASE_NAME).append(";")
                    .append("user=").append(USERNAME).append(";")
                    .append("password=").append(PASSWORD).append(";");

            // Nếu sử dụng SSL
            if (USING_SSL) {
                connectStringBuilder.append("encrypt=true;trustServerCertificate=true;");
            }

            CONNECT_STRING = connectStringBuilder.toString();
            System.out.println("Chuỗi kết nối: " + CONNECT_STRING);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lấy kết nối đến cơ sở dữ liệu
     *
     * @return Connection
     */
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(CONNECT_STRING);
            }
            return conn;
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Đóng kết nối
     */
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Đã đóng kết nối.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {
        Connection conn = getConnection();
        if (conn != null) {
            DatabaseMetaData dbmt = conn.getMetaData();
            System.out.println("Driver Name: " + dbmt.getDriverName());
            System.out.println("Database Product Name: " + dbmt.getDatabaseProductName());
            System.out.println("Database Product Version: " + dbmt.getDatabaseProductVersion());

            // Đóng kết nối sau khi sử dụng
            closeConnection();
        }
    }
}

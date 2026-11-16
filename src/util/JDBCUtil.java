package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Lớp tiện ích JDBC (JDBCUtil)
 * Quản lý kết nối JDBC và giải phóng tài nguyên CSDL theo chuẩn JDBC API
 * Tham khảo theo tài liệu hướng dẫn Template.docx & www.javaguides.net
 */
public class JDBCUtil {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tblcinemadb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.err.println("Không tìm thấy MySQL JDBC Driver: " + ex.getMessage());
            }
        }
    }

    /**
     * Mở kết nối JDBC tới cơ sở dữ liệu MySQL
     * @return java.sql.Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Đóng tài nguyên ResultSet, PreparedStatement, Connection an toàn
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
        if (ps != null) {
            try { ps.close(); } catch (SQLException ignored) {}
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    public static void close(Connection conn, PreparedStatement ps) {
        close(conn, ps, null);
    }
}

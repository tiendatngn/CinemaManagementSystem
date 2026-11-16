package dao;

import java.sql.Connection;
import util.JDBCUtil;

/**
 * Lớp cơ sở DAO9 - Quản lý kết nối JDBC MySQL
 * Thiết kế theo mẫu trong tài liệu BG HP TTTN 2 CNPM và biểu đồ lớp chi tiết
 */
public class DAO9 {
    public static Connection con;

    public DAO9() {
        if (con == null) {
            try {
                con = JDBCUtil.getConnection();
            } catch (Exception e) {
                System.err.println("Lỗi kết nối JDBC trong DAO9: " + e.getMessage());
            }
        }
    }

    /**
     * Hỗ trợ gán Connection phục vụ kiểm thử đơn vị hoặc transaction độc lập
     */
    public DAO9(Connection connection) {
        con = connection;
    }

    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                con = JDBCUtil.getConnection();
            }
        } catch (Exception e) {
            System.err.println("Lỗi làm mới kết nối JDBC: " + e.getMessage());
        }
        return con;
    }
}


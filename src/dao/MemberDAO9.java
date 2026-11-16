package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Customer9;
import model.ManagementStaff9;
import model.Member9;
import model.MemberCard9;

/**
 * Lớp MemberDAO9 - Tầng truy cập dữ liệu cho Thành viên, Nhân viên và Khách hàng
 * Thiết kế chuẩn theo Biểu đồ lớp chi tiết và Sequence Diagram Pha 3
 */
public class MemberDAO9 extends DAO9 {

    public MemberDAO9() {
        super();
    }

    public MemberDAO9(Connection con) {
        super(con);
    }

    /**
     * Xác thực thông tin đăng nhập người dùng
     * Khớp với xacThucDangNhap(username, password) trong Biểu đồ lớp Pha 3
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Member9 (có thể là thể hiện của ManagementStaff9 hoặc Customer9), hoặc null nếu thất bại
     */
    public Member9 xacThucDangNhap(String username, String password) {
        if (username == null || password == null) return null;

        String sql = "SELECT * FROM tblMember9 WHERE username = ? AND password = ?";
        Connection currentCon = getConnection();
        if (currentCon == null) return null;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String hoTen = rs.getString("hoTen");
                    String ngaySinh = rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toString() : "";
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    String sdt = rs.getString("sdt");

                    // 1. Kiểm tra xem có phải là Nhân viên quản lý (Staff) hay không
                    ManagementStaff9 staff = checkStaff(currentCon, id, hoTen, ngaySinh, diaChi, email, sdt, username, password);
                    if (staff != null) {
                        return staff;
                    }

                    // 2. Kiểm tra xem có phải là Khách hàng (Customer) hay không
                    Customer9 customer = checkCustomer(currentCon, id, hoTen, ngaySinh, diaChi, email, sdt, username, password);
                    if (customer != null) {
                        return customer;
                    }

                    // 3. Người dùng thành viên thông thường
                    return new Member9(id, hoTen, ngaySinh, diaChi, email, sdt, username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member9 checkLogin(String username, String password) {
        return xacThucDangNhap(username, password);
    }

    private ManagementStaff9 checkStaff(Connection con, int id, String hoTen, String ngaySinh, 
                                       String diaChi, String email, String sdt, String username, String password) {
        String sql = "SELECT viTri FROM tblStaff9 WHERE tblMember9id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String viTri = rs.getString("viTri");
                    return new ManagementStaff9(id, hoTen, ngaySinh, diaChi, email, sdt, username, password, viTri);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Customer9 checkCustomer(Connection con, int id, String hoTen, String ngaySinh, 
                                    String diaChi, String email, String sdt, String username, String password) {
        String sql = "SELECT * FROM tblCustomer9 WHERE tblMember9id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MemberCard9 card = new MemberCard9();
                    card.setId(id);
                    card.setMaThe(rs.getString("maThe"));
                    card.setPhatHanh(rs.getDate("ngayPhatHanh"));
                    card.setDiemTL(rs.getInt("diemTL"));
                    card.setHang(rs.getString("hang"));
                    card.setTrangThai(rs.getString("trangThai"));

                    return new Customer9(id, hoTen, ngaySinh, diaChi, email, sdt, username, password, card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm khách hàng theo mã thẻ thành viên để áp dụng ưu đãi
     */
    public Customer9 getCustomerByCard(String maThe) {
        if (maThe == null || maThe.trim().isEmpty()) return null;

        String sql = "SELECT m.*, c.maThe, c.ngayPhatHanh, c.diemTL, c.hang, c.trangThai " +
                     "FROM tblCustomer9 c " +
                     "INNER JOIN tblMember9 m ON c.tblMember9id = m.id " +
                     "WHERE LOWER(TRIM(c.maThe)) = LOWER(TRIM(?)) AND c.trangThai = 'Hoạt động'";

        Connection currentCon = getConnection();
        if (currentCon == null) return null;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setString(1, maThe.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String hoTen = rs.getString("hoTen");
                    String ngaySinh = rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toString() : "";
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    String sdt = rs.getString("sdt");
                    String username = rs.getString("username");

                    MemberCard9 card = new MemberCard9();
                    card.setId(id);
                    card.setMaThe(rs.getString("maThe"));
                    card.setPhatHanh(rs.getDate("ngayPhatHanh"));
                    card.setDiemTL(rs.getInt("diemTL"));
                    card.setHang(rs.getString("hang"));
                    card.setTrangThai(rs.getString("trangThai"));

                    return new Customer9(id, hoTen, ngaySinh, diaChi, email, sdt, username, "", card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

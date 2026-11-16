package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Bill9;
import model.Customer9;
import model.MemberCard9;
import model.Ticket9;

/**
 * Lớp BillDAO9 - Tầng truy cập dữ liệu cho Hóa đơn (Bill9)
 * Thiết kế theo Biểu đồ lớp chi tiết và Sequence Diagram Pha 3
 * Xử lý Transaction lưu hóa đơn, áp dụng ưu đãi thẻ thành viên và gọi TicketDAO9 cập nhật vé
 */
public class BillDAO9 extends DAO9 {

    public BillDAO9() {
        super();
    }

    public BillDAO9(Connection con) {
        super(con);
    }

    /**
     * Lưu hóa đơn đặt vé xem phim
     * Khớp với luuHoaDonDatVe(bill) trong Biểu đồ lớp và Sequence Diagram Pha 3 (Bước 57 -> Bước 66)
     * Luồng xử lý minh bạch:
     *   1. Xử lý ưu đãi giảm giá và tích lũy điểm từ Thẻ thành viên (nếu có).
     *   2. Lưu bản ghi hóa đơn vào bảng tblBill9.
     *   3. Gọi TicketDAO9.capNhatTrangThaiVe() để cập nhật trạng thái các vé sang "Đã bán" và gán mã hóa đơn.
     *   4. Toàn bộ thực thi trong một Database Transaction toàn vẹn.
     * @param bill Đối tượng Bill9 cần lưu
     * @return boolean
     */
    public boolean luuHoaDonDatVe(Bill9 bill) {
        if (bill == null) return false;

        Connection currentCon = getConnection();
        if (currentCon == null) return false;

        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = currentCon.getAutoCommit();
            currentCon.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Áp dụng ưu đãi Thẻ thành viên (nếu có)
            xuLyGiamGiaVaTichDiem(currentCon, bill);

            // 2. Lưu thông tin hóa đơn vào tblBill9
            String sqlBill = "INSERT INTO tblBill9 (tienGoc, giamGia, ptt, loai, tblStaff9id, tblCustomer9id) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
            int billId = -1;
            try (PreparedStatement psBill = currentCon.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setFloat(1, bill.getTienGoc());
                psBill.setFloat(2, bill.getGiamGia());
                psBill.setString(3, bill.getPtt() != null ? bill.getPtt() : "Chuyển khoản QR");
                psBill.setString(4, bill.getLoai() != null ? bill.getLoai() : "Online");

                if (bill.getNhanVien() != null && bill.getNhanVien().getId() > 0) {
                    psBill.setInt(5, bill.getNhanVien().getId());
                } else {
                    psBill.setNull(5, java.sql.Types.INTEGER);
                }

                if (bill.getKhachHang() != null && bill.getKhachHang().getId() > 0) {
                    psBill.setInt(6, bill.getKhachHang().getId());
                } else {
                    psBill.setNull(6, java.sql.Types.INTEGER);
                }

                int affected = psBill.executeUpdate();
                if (affected <= 0) {
                    currentCon.rollback();
                    return false;
                }

                try (ResultSet rsKeys = psBill.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        billId = rsKeys.getInt(1);
                        bill.setId(billId);
                    }
                }
            }

            // 3. Luồng cập nhật trạng thái vé minh bạch: Gọi TicketDAO9 cập nhật các vé sang "Đã bán"
            if (bill.getDsVe() != null && !bill.getDsVe().isEmpty()) {
                int[] dsIdVe = new int[bill.getDsVe().size()];
                for (int i = 0; i < bill.getDsVe().size(); i++) {
                    dsIdVe[i] = bill.getDsVe().get(i).getId();
                }

                TicketDAO9 ticketDAO = new TicketDAO9(currentCon);
                boolean updateTickets = ticketDAO.capNhatTrangThaiVe(currentCon, dsIdVe, billId);
                if (!updateTickets) {
                    currentCon.rollback();
                    return false;
                }
            }

            // Commit transaction thành công
            currentCon.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                currentCon.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                currentCon.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Xử lý ưu đãi giảm giá và tích lũy điểm từ thẻ thành viên
     */
    private void xuLyGiamGiaVaTichDiem(Connection con, Bill9 bill) {
        Customer9 customer = bill.getKhachHang();
        MemberCard9 card = bill.getTheTV();

        if (customer != null && card == null) {
            card = customer.getTheTV();
        }

        if (card != null && card.getMaThe() != null && !card.getMaThe().trim().isEmpty()) {
            // Truy vấn CSDL kiểm tra thẻ và hạng thẻ
            String sqlCard = "SELECT * FROM tblCustomer9 WHERE LOWER(TRIM(maThe)) = LOWER(TRIM(?)) AND trangThai = 'Hoạt động'";
            try (PreparedStatement ps = con.prepareStatement(sqlCard)) {
                ps.setString(1, card.getMaThe().trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String hang = rs.getString("hang");
                        int diemHienTai = rs.getInt("diemTL");
                        float giam = 0.0f;

                        // Chính sách giảm giá theo hạng thẻ
                        if ("VIP".equalsIgnoreCase(hang)) {
                            giam = bill.getTienGoc() * 0.10f; // Giảm 10% cho khách VIP
                        } else if ("Kim Cương".equalsIgnoreCase(hang) || "Diamond".equalsIgnoreCase(hang)) {
                            giam = bill.getTienGoc() * 0.15f; // Giảm 15%
                        } else {
                            giam = bill.getTienGoc() * 0.05f; // Giảm 5% cho thẻ thành viên tiêu chuẩn
                        }

                        bill.setGiamGia(giam);
                        bill.tinhThucTe();

                        // Tích lũy 10 điểm cho giao dịch đặt vé thành công
                        String sqlUpdateDiem = "UPDATE tblCustomer9 SET diemTL = diemTL + 10 WHERE maThe = ?";
                        try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdateDiem)) {
                            psUpdate.setString(1, card.getMaThe().trim());
                            psUpdate.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lấy chi tiết hóa đơn theo ID
     */
    public Bill9 getBillDetail(int idBill) {
        String sql = "SELECT * FROM tblBill9 WHERE id = ?";
        Connection currentCon = getConnection();
        if (currentCon == null) return null;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setInt(1, idBill);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill9 bill = new Bill9();
                    bill.setId(rs.getInt("id"));
                    bill.setNgayLap(rs.getTimestamp("ngayLap"));
                    bill.setTienGoc(rs.getFloat("tienGoc"));
                    bill.setGiamGia(rs.getFloat("giamGia"));
                    bill.setPtt(rs.getString("ptt"));
                    bill.setLoai(rs.getString("loai"));
                    bill.tinhThucTe();

                    // Lấy danh sách vé thuộc hóa đơn
                    String sqlTicket = "SELECT id FROM tblTicket9 WHERE tblBill9id = ?";
                    try (PreparedStatement psTk = currentCon.prepareStatement(sqlTicket)) {
                        psTk.setInt(1, idBill);
                        try (ResultSet rsTk = psTk.executeQuery()) {
                            ArrayList<Ticket9> listTk = new ArrayList<>();
                            while (rsTk.next()) {
                                Ticket9 t = new Ticket9();
                                t.setId(rsTk.getInt("id"));
                                listTk.add(t);
                            }
                            bill.setDsVe(listTk);
                        }
                    }
                    return bill;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hủy hóa đơn đặt vé
     */
    public boolean huyHoaDonDatVe(int idBill) {
        String sql = "DELETE FROM tblBill9 WHERE id = ?";
        Connection currentCon = getConnection();
        if (currentCon == null) return false;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setInt(1, idBill);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Seat9;
import model.Showtime9;
import model.Ticket9;

/**
 * Lớp TicketDAO9 - Tầng truy cập dữ liệu cho Vé xem phim (Ticket9)
 * Thiết kế theo Biểu đồ lớp chi tiết và Sequence Diagram Pha 3
 */
public class TicketDAO9 extends DAO9 {

    public TicketDAO9() {
        super();
    }

    public TicketDAO9(Connection con) {
        super(con);
    }

    /**
     * Lấy danh sách trạng thái vé của các ghế theo suất chiếu xác định
     * Khớp với getDSTrangThaiVe(showtimeId) trong Biểu đồ lớp Pha 3
     * @param showtimeId Mã suất chiếu
     * @return ArrayList<Ticket9>
     */
    public ArrayList<Ticket9> getDSTrangThaiVe(int showtimeId) {
        ArrayList<Ticket9> list = new ArrayList<>();
        String sql = "SELECT tk.*, s.ten as tenGhe, s.loai as loaiGhe, s.moTa as moTaGhe " +
                     "FROM tblTicket9 tk " +
                     "INNER JOIN tblSeat9 s ON tk.tblSeat9id = s.id " +
                     "WHERE tk.tblShowtime9id = ? " +
                     "ORDER BY s.id ASC";

        Connection currentCon = getConnection();
        if (currentCon == null) return list;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket9 tk = new Ticket9();
                    tk.setId(rs.getInt("id"));
                    tk.setGia(rs.getFloat("gia"));
                    tk.setTgTao(rs.getTimestamp("tgTao"));
                    tk.setTrangThai(rs.getString("trangThai"));

                    Seat9 seat = new Seat9();
                    seat.setId(rs.getInt("tblSeat9id"));
                    seat.setTen(rs.getString("tenGhe"));
                    seat.setLoai(rs.getString("loaiGhe"));
                    seat.setMoTa(rs.getString("moTaGhe"));
                    tk.setGhe(seat);

                    Showtime9 st = new Showtime9();
                    st.setId(showtimeId);
                    tk.setSuatChieu(st);

                    list.add(tk);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tạm khóa ghế khi khách hàng nhấn chọn đặt vé
     * Khớp với khoaGheTamThoi(dsIdGhe, idSuatChieu) trong Biểu đồ lớp Pha 3
     * @param dsIdGhe Mảng ID các ghế khách hàng chọn
     * @param idSuatChieu Mã suất chiếu
     * @return boolean
     */
    public boolean khoaGheTamThoi(int[] dsIdGhe, int idSuatChieu) {
        if (dsIdGhe == null || dsIdGhe.length == 0) return false;

        Connection currentCon = getConnection();
        if (currentCon == null) return false;

        String checkSql = "SELECT id, trangThai FROM tblTicket9 WHERE tblShowtime9id = ? AND tblSeat9id = ?";
        String updateSql = "UPDATE tblTicket9 SET trangThai = 'Đang giữ' WHERE tblShowtime9id = ? AND tblSeat9id = ?";
        String insertSql = "INSERT INTO tblTicket9 (gia, trangThai, tblSeat9id, tblShowtime9id) VALUES (?, 'Đang giữ', ?, ?)";

        try {
            for (int seatId : dsIdGhe) {
                // Kiểm tra xem vé cho ghế này trong suất chiếu đã tồn tại chưa
                int ticketId = -1;
                String currentStatus = "";
                try (PreparedStatement ps = currentCon.prepareStatement(checkSql)) {
                    ps.setInt(1, idSuatChieu);
                    ps.setInt(2, seatId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            ticketId = rs.getInt("id");
                            currentStatus = rs.getString("trangThai");
                        }
                    }
                }

                if (ticketId != -1) {
                    if ("Đã bán".equalsIgnoreCase(currentStatus)) {
                        return false; // Ghế đã bán, không thể khóa
                    }
                    try (PreparedStatement ps = currentCon.prepareStatement(updateSql)) {
                        ps.setInt(1, idSuatChieu);
                        ps.setInt(2, seatId);
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = currentCon.prepareStatement(insertSql)) {
                        ps.setFloat(1, 95500.0f); // Giá mặc định theo kịch bản
                        ps.setInt(2, seatId);
                        ps.setInt(3, idSuatChieu);
                        ps.executeUpdate();
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái các vé sang "Đã bán" và gán mã hóa đơn
     * Khớp với capNhatTrangThaiVe(dsIdVe, idBill) trong Biểu đồ lớp Pha 3
     * @param dsIdVe Mảng mã vé cần cập nhật
     * @param idBill Mã hóa đơn đã thanh toán
     * @return boolean
     */
    public boolean capNhatTrangThaiVe(int[] dsIdVe, int idBill) {
        return capNhatTrangThaiVe(getConnection(), dsIdVe, idBill);
    }

    /**
     * Phiên bản hỗ trợ Transaction dùng chung Connection với BillDAO9
     */
    public boolean capNhatTrangThaiVe(Connection currentCon, int[] dsIdVe, int idBill) {
        if (dsIdVe == null || dsIdVe.length == 0 || currentCon == null) return false;

        String sql = "UPDATE tblTicket9 SET trangThai = 'Đã bán', tblBill9id = ? WHERE id = ?";
        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            for (int ticketId : dsIdVe) {
                ps.setInt(1, idBill);
                ps.setInt(2, ticketId);
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

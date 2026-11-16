package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Seat9;
import model.Theater9;

/**
 * Lớp SeatDAO9 - Tầng truy cập dữ liệu cho Ghế ngồi (Seat9)
 * Thiết kế theo Biểu đồ lớp chi tiết và Sequence Diagram Pha 3
 */
public class SeatDAO9 extends DAO9 {

    public SeatDAO9() {
        super();
    }

    public SeatDAO9(Connection con) {
        super(con);
    }

    /**
     * Lấy danh sách toàn bộ ghế thuộc một phòng chiếu
     * Khớp với getDSGheTheoPhong(idPhong) trong Biểu đồ lớp Pha 3
     * @param idPhong Mã phòng chiếu
     * @return ArrayList<Seat9>
     */
    public ArrayList<Seat9> getDSGheTheoPhong(int idPhong) {
        ArrayList<Seat9> list = new ArrayList<>();
        String sql = "SELECT s.*, t.ten as tenPhong, t.loai as loaiPhong, t.sucChua " +
                     "FROM tblSeat9 s " +
                     "INNER JOIN tblTheater9 t ON s.tblTheater9id = t.id " +
                     "WHERE s.tblTheater9id = ? " +
                     "ORDER BY s.id ASC";

        Connection currentCon = getConnection();
        if (currentCon == null) return list;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setInt(1, idPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat9 seat = new Seat9();
                    seat.setId(rs.getInt("id"));
                    seat.setTen(rs.getString("ten"));
                    seat.setLoai(rs.getString("loai"));
                    seat.setMoTa(rs.getString("moTa"));

                    Theater9 th = new Theater9();
                    th.setId(rs.getInt("tblTheater9id"));
                    th.setTen(rs.getString("tenPhong"));
                    th.setLoai(rs.getString("loaiPhong"));
                    th.setSucChua(rs.getInt("sucChua"));
                    seat.setTheater(th);

                    list.add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

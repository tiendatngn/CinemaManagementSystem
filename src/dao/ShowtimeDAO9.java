package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Movie9;
import model.Showtime9;
import model.Theater9;

/**
 * Lớp ShowtimeDAO9 - Tầng truy cập dữ liệu cho Suất chiếu (Showtime9)
 * Thiết kế theo Biểu đồ lớp chi tiết và Sequence Diagram Pha 3
 */
public class ShowtimeDAO9 extends DAO9 {

    public ShowtimeDAO9() {
        super();
    }

    public ShowtimeDAO9(Connection con) {
        super(con);
    }

    /**
     * Lấy danh sách suất chiếu của một bộ phim
     * Khớp với getDSSuatChieu(movieId) trong Biểu đồ lớp Pha 3
     * @param movieId Mã phim
     * @return ArrayList<Showtime9>
     */
    public ArrayList<Showtime9> getDSSuatChieu(int movieId) {
        ArrayList<Showtime9> list = new ArrayList<>();
        String sql = "SELECT s.*, t.ten as tenPhong, t.loai as loaiPhong, t.sucChua, t.moTa as moTaPhong, " +
                     "m.ten as tenPhim, m.thoiLuong, m.theLoai, m.khoiChieu, m.moTa as moTaPhim, m.anhPoster " +
                     "FROM tblShowtime9 s " +
                     "INNER JOIN tblTheater9 t ON s.tblTheater9id = t.id " +
                     "INNER JOIN tblMovie9 m ON s.tblMovie9id = m.id " +
                     "WHERE s.tblMovie9id = ? " +
                     "ORDER BY s.ngayChieu ASC, s.batDau ASC";

        Connection currentCon = getConnection();
        if (currentCon == null) return list;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractShowtime(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Showtime9> getSuatChieuTheoPhim(int movieId) {
        return getDSSuatChieu(movieId);
    }

    /**
     * Lấy chi tiết suất chiếu theo ID
     */
    public Showtime9 getShowtimeById(int id) {
        String sql = "SELECT s.*, t.ten as tenPhong, t.loai as loaiPhong, t.sucChua, t.moTa as moTaPhong, " +
                     "m.ten as tenPhim, m.thoiLuong, m.theLoai, m.khoiChieu, m.moTa as moTaPhim, m.anhPoster " +
                     "FROM tblShowtime9 s " +
                     "INNER JOIN tblTheater9 t ON s.tblTheater9id = t.id " +
                     "INNER JOIN tblMovie9 m ON s.tblMovie9id = m.id " +
                     "WHERE s.id = ?";

        Connection currentCon = getConnection();
        if (currentCon == null) return null;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractShowtime(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Showtime9 extractShowtime(ResultSet rs) throws SQLException {
        Showtime9 st = new Showtime9();
        st.setId(rs.getInt("id"));
        st.setNgayChieu(rs.getDate("ngayChieu"));
        st.setBatDau(rs.getTime("batDau"));
        st.setKetThuc(rs.getTime("ketThuc"));
        st.setMoTa(rs.getString("moTa"));

        Theater9 th = new Theater9();
        th.setId(rs.getInt("tblTheater9id"));
        th.setTen(rs.getString("tenPhong"));
        th.setLoai(rs.getString("loaiPhong"));
        th.setSucChua(rs.getInt("sucChua"));
        th.setMoTa(rs.getString("moTaPhong"));
        st.setPhongChieu(th);

        Movie9 mv = new Movie9();
        mv.setId(rs.getInt("tblMovie9id"));
        mv.setTen(rs.getString("tenPhim"));
        mv.setThoiLuong(rs.getInt("thoiLuong"));
        mv.setTheLoai(rs.getString("theLoai"));
        mv.setKhoiChieu(rs.getDate("khoiChieu"));
        mv.setMoTa(rs.getString("moTaPhim"));
        mv.setAnhPoster(rs.getString("anhPoster"));
        st.setPhim(mv);

        return st;
    }
}

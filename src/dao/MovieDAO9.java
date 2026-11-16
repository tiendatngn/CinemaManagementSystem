package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Movie9;

/**
 * Lớp MovieDAO9 - Tầng truy cập dữ liệu cho Phim (Movie9)
 * Thiết kế chuẩn theo Biểu đồ lớp chi tiết và Sequence diagram Pha 3
 */
public class MovieDAO9 extends DAO9 {

    public MovieDAO9() {
        super();
    }

    public MovieDAO9(Connection con) {
        super(con);
    }

    /**
     * Lấy danh sách tất cả các bộ phim hiện có trong hệ thống
     * @return ArrayList<Movie9>
     */
    public ArrayList<Movie9> getDSPhim() {
        ArrayList<Movie9> list = new ArrayList<>();
        String sql = "SELECT * FROM tblMovie9 ORDER BY id ASC";

        Connection currentCon = getConnection();
        if (currentCon == null) return list;

        try (PreparedStatement ps = currentCon.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie9 m = extractMovie(rs);
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách phim đang được công chiếu
     * @return ArrayList<Movie9>
     */
    public ArrayList<Movie9> getDSPhimDangChieu() {
        ArrayList<Movie9> list = new ArrayList<>();
        // Lấy các phim có suất chiếu hoặc đang chiếu trong hệ thống
        String sql = "SELECT DISTINCT m.* FROM tblMovie9 m " +
                     "INNER JOIN tblShowtime9 s ON m.id = s.tblMovie9id " +
                     "ORDER BY m.id ASC";

        Connection currentCon = getConnection();
        if (currentCon == null) return list;

        try (PreparedStatement ps = currentCon.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractMovie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Nếu chưa có suất chiếu nào thì fallback lấy toàn bộ danh sách phim để demo
        if (list.isEmpty()) {
            return getDSPhim();
        }
        return list;
    }

    /**
     * Kiểm tra trùng lặp tên phim
     * @param tenPhim Tên bộ phim cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public boolean checkPhimTrung(String tenPhim) {
        if (tenPhim == null || tenPhim.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT id FROM tblMovie9 WHERE LOWER(TRIM(ten)) = LOWER(TRIM(?))";
        Connection currentCon = getConnection();
        if (currentCon == null) return false;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setString(1, tenPhim.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkDuplicateName(String ten) {
        return checkPhimTrung(ten);
    }

    /**
     * Lưu thông tin bộ phim mới vào CSDL
     * Khớp với phương thức luuPhim() trong Biểu đồ lớp và Sequence Diagram Pha 3
     * @param movie Đối tượng Movie9 cần lưu
     * @return boolean
     */
    public boolean luuPhim(Movie9 movie) {
        if (movie == null || movie.getTen() == null || movie.getTen().trim().isEmpty()) {
            return false;
        }
        if (movie.getThoiLuong() <= 0 || movie.getKhoiChieu() == null) {
            return false;
        }

        // Kiểm tra trùng lặp tên phim
        if (checkPhimTrung(movie.getTen())) {
            System.err.println("Lỗi: Tên phim đã tồn tại: " + movie.getTen());
            return false;
        }

        String sql = "INSERT INTO tblMovie9 (ten, thoiLuong, theLoai, khoiChieu, moTa, anhPoster) VALUES (?, ?, ?, ?, ?, ?)";
        Connection currentCon = getConnection();
        if (currentCon == null) return false;

        try (PreparedStatement ps = currentCon.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getTen().trim());
            ps.setInt(2, movie.getThoiLuong());
            ps.setString(3, movie.getTheLoai() != null ? movie.getTheLoai().trim() : "");
            ps.setDate(4, movie.getKhoiChieu());
            ps.setString(5, movie.getMoTa() != null ? movie.getMoTa().trim() : "");
            ps.setString(6, movie.getAnhPoster() != null ? movie.getAnhPoster().trim() : "");

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        movie.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addMovie(Movie9 movie) {
        return luuPhim(movie);
    }

    /**
     * Lấy thông tin phim theo ID
     */
    public Movie9 getMovieById(int id) {
        String sql = "SELECT * FROM tblMovie9 WHERE id = ?";
        Connection currentCon = getConnection();
        if (currentCon == null) return null;

        try (PreparedStatement ps = currentCon.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractMovie(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Movie9 extractMovie(ResultSet rs) throws SQLException {
        Movie9 m = new Movie9();
        m.setId(rs.getInt("id"));
        m.setTen(rs.getString("ten"));
        m.setThoiLuong(rs.getInt("thoiLuong"));
        m.setTheLoai(rs.getString("theLoai"));
        m.setKhoiChieu(rs.getDate("khoiChieu"));
        m.setMoTa(rs.getString("moTa"));
        m.setAnhPoster(rs.getString("anhPoster"));
        return m;
    }
}

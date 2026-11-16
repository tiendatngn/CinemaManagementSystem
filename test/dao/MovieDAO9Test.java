package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import model.Movie9;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Kiểm thử đơn vị cho lớp MovieDAO9 sử dụng JUnit
 * Xây dựng theo mục 5.3 (Kiểm thử đơn vị) trong giáo trình BG HP TTTN 2 CNPM
 */
public class MovieDAO9Test {

    private MovieDAO9 movieDAO;
    private static final String TEST_MOVIE_NAME = "Test Phim Mới Unit Test " + System.currentTimeMillis();

    @Before
    public void setUp() {
        movieDAO = new MovieDAO9();
    }

    @After
    public void tearDown() {
        // Dọn dẹp dữ liệu kiểm thử (nếu có) để đảm bảo tính độc lập và sạch sẽ của CSDL
        try {
            Connection con = movieDAO.getConnection();
            if (con != null) {
                String sql = "DELETE FROM tblMovie9 WHERE ten LIKE 'Test Phim Mới Unit Test%'";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            // bỏ qua lỗi dọn dẹp nếu CSDL offline
        }
    }

    /**
     * Test case kiểm tra lấy danh sách phim
     */
    @Test
    public void testGetDSPhim() {
        ArrayList<Movie9> list = movieDAO.getDSPhim();
        Assert.assertNotNull("Danh sách phim trả về không được null", list);
    }

    /**
     * Test case kiểm tra thêm phim mới chuẩn (hợp lệ)
     */
    @Test
    public void testAddMovie_Chuan() {
        Movie9 phim = new Movie9();
        phim.setTen(TEST_MOVIE_NAME);
        phim.setTheLoai("Bí ẩn, Khoa học viễn tưởng");
        phim.setThoiLuong(125);
        phim.setKhoiChieu(Date.valueOf("2026-12-01"));
        phim.setMoTa("Mô tả phim kiểm thử đơn vị");

        boolean kq = movieDAO.addMovie(phim);
        // Nếu CSDL kết nối thành công thì thêm thành công và sinh được ID > 0
        if (movieDAO.getConnection() != null) {
            Assert.assertTrue("Thêm phim hợp lệ phải trả về true", kq);
            Assert.assertTrue("ID phim được tạo phải lớn hơn 0", phim.getId() > 0);
        }
    }

    /**
     * Test case ngoại lệ: thêm đối tượng null
     */
    @Test
    public void testAddMovie_Null() {
        boolean kq = movieDAO.addMovie(null);
        Assert.assertFalse("Thêm phim null phải trả về false", kq);
    }

    /**
     * Test case ngoại lệ: tên phim rỗng
     */
    @Test
    public void testAddMovie_TenRong() {
        Movie9 phim = new Movie9("", 120, "Hành động", Date.valueOf("2026-10-10"), "Mô tả");
        boolean kq = movieDAO.addMovie(phim);
        Assert.assertFalse("Tên phim rỗng phải trả về false", kq);
    }

    /**
     * Test case ngoại lệ: thời lượng phim âm hoặc bằng 0
     */
    @Test
    public void testAddMovie_ThoiLuongKhongHopLe() {
        Movie9 phim = new Movie9("Phim Thời Lượng Âm", -10, "Hành động", Date.valueOf("2026-10-10"), "Mô tả");
        boolean kq = movieDAO.addMovie(phim);
        Assert.assertFalse("Thời lượng âm phải trả về false", kq);
    }

    /**
     * Test case ngoại lệ: thêm phim trùng tên đã có trong CSDL
     */
    @Test
    public void testAddMovie_TrungTen() {
        // Giả sử phim Hộ linh tráng sĩ đã có trong hệ thống
        Movie9 phimTrung = new Movie9("Hộ linh tráng sĩ", 135, "Chính kịch", Date.valueOf("2026-08-30"), "Mô tả");
        
        // Nếu tên này đã có trong CSDL, addMovie phải trả về false
        if (movieDAO.checkDuplicateName("Hộ linh tráng sĩ")) {
            boolean kq = movieDAO.addMovie(phimTrung);
            Assert.assertFalse("Thêm phim trùng tên phải trả về false", kq);
        }
    }

    /**
     * Test case kiểm tra hàm checkDuplicateName
     */
    @Test
    public void testCheckDuplicateName() {
        // Tên chắc chắn chưa có
        boolean duplicate = movieDAO.checkDuplicateName("PhimChacChanKhongTonTai_XYZ_" + System.currentTimeMillis());
        Assert.assertFalse("Tên chưa tồn tại phải trả về false", duplicate);
    }
}

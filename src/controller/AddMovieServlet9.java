package controller;

import java.io.IOException;
import java.sql.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MovieDAO9;
import model.Movie9;

/**
 * Servlet điều khiển: AddMovieServlet9
 * Thiết kế chuẩn theo Sơ đồ gói, Thiết kế tĩnh và Sequence Diagram Pha 3
 * Xử lý tiếp nhận biểu mẫu thêm phim, kiểm tra trùng lặp và lưu qua MovieDAO9
 */
@WebServlet(name = "AddMovieServlet9", urlPatterns = {"/addMovie", "/doAddMovie"})
public class AddMovieServlet9 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MovieDAO9 movieDAO;

    @Override
    public void init() {
        movieDAO = new MovieDAO9();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/management_view/MovieAddView9.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String tenPhim = request.getParameter("txtTenPhim");
        String theLoai = request.getParameter("slTheLoai");
        String thoiLuongStr = request.getParameter("txtThoiLuong");
        String ngayKhoiChieuStr = request.getParameter("txtNgayKhoiChieu");
        String moTa = request.getParameter("txtMoTa");
        String anhPoster = request.getParameter("fileAnhPoster");

        // 1. Kiểm tra để trống
        if (tenPhim == null || tenPhim.trim().isEmpty() ||
            theLoai == null || theLoai.trim().isEmpty() ||
            thoiLuongStr == null || thoiLuongStr.trim().isEmpty() ||
            ngayKhoiChieuStr == null || ngayKhoiChieuStr.trim().isEmpty()) {

            request.setAttribute("err", "Vui lòng điền đầy đủ tất cả các trường thông tin bắt buộc!");
            retainForm(request, tenPhim, theLoai, thoiLuongStr, ngayKhoiChieuStr, moTa, anhPoster);
            request.getRequestDispatcher("/views/management_view/MovieAddView9.jsp").forward(request, response);
            return;
        }

        // 2. Kiểm tra thời lượng
        int thoiLuong = 0;
        try {
            thoiLuong = Integer.parseInt(thoiLuongStr.trim());
            if (thoiLuong <= 0) {
                request.setAttribute("err", "Thời lượng phim phải là số nguyên dương lớn hơn 0!");
                retainForm(request, tenPhim, theLoai, thoiLuongStr, ngayKhoiChieuStr, moTa, anhPoster);
                request.getRequestDispatcher("/views/management_view/MovieAddView9.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("err", "Thời lượng phim không hợp lệ!");
            retainForm(request, tenPhim, theLoai, thoiLuongStr, ngayKhoiChieuStr, moTa, anhPoster);
            request.getRequestDispatcher("/views/management_view/MovieAddView9.jsp").forward(request, response);
            return;
        }

        // 3. Kiểm tra ngày khởi chiếu
        Date khoiChieu = null;
        try {
            khoiChieu = Date.valueOf(ngayKhoiChieuStr.trim());
        } catch (IllegalArgumentException e) {
            request.setAttribute("err", "Định dạng ngày khởi chiếu không đúng (yyyy-MM-dd)!");
            retainForm(request, tenPhim, theLoai, thoiLuongStr, ngayKhoiChieuStr, moTa, anhPoster);
            request.getRequestDispatcher("/views/management_view/MovieAddView9.jsp").forward(request, response);
            return;
        }

        // 4. Kiểm tra trùng lặp tên phim qua MovieDAO9.checkPhimTrung()
        if (movieDAO.checkPhimTrung(tenPhim)) {
            request.setAttribute("err", "Tên bộ phim '" + tenPhim + "' đã tồn tại trên hệ thống! Vui lòng chọn tên khác.");
            retainForm(request, tenPhim, theLoai, thoiLuongStr, ngayKhoiChieuStr, moTa, anhPoster);
            request.getRequestDispatcher("/views/management_view/MovieAddView9.jsp").forward(request, response);
            return;
        }

        // 5. Đóng gói đối tượng thực thể Movie9 và gọi MovieDAO9.luuPhim()
        Movie9 phim = new Movie9(tenPhim.trim(), thoiLuong, theLoai.trim(), khoiChieu, 
                                 moTa != null ? moTa.trim() : "", 
                                 anhPoster != null && !anhPoster.trim().isEmpty() ? anhPoster.trim() : "poster_default.jpg");

        boolean kq = movieDAO.luuPhim(phim);

        if (kq) {
            request.setAttribute("phimMoi", phim);
            request.setAttribute("status", "success");
            request.getRequestDispatcher("/views/management_view/MovieManagementView9.jsp").forward(request, response);
        } else {
            request.setAttribute("err", "Lỗi lưu trữ dữ liệu vào CSDL!");
            retainForm(request, tenPhim, theLoai, thoiLuongStr, ngayKhoiChieuStr, moTa, anhPoster);
            request.getRequestDispatcher("/views/management_view/MovieAddView9.jsp").forward(request, response);
        }
    }

    private void retainForm(HttpServletRequest request, String ten, String theLoai, String thoiLuong, String khoiChieu, String moTa, String poster) {
        request.setAttribute("ten", ten);
        request.setAttribute("theLoai", theLoai);
        request.setAttribute("thoiLuong", thoiLuong);
        request.setAttribute("khoiChieu", khoiChieu);
        request.setAttribute("moTa", moTa);
        request.setAttribute("poster", poster);
    }
}

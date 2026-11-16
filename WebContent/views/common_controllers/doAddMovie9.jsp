<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="model.Movie9" %>
<%@ page import="model.ManagementStaff9" %>
<%@ page import="dao.MovieDAO9" %>
<%
    // Thiết lập mã hóa UTF-8 cho tiếng Việt
    request.setCharacterEncoding("UTF-8");

    // Lấy thông tin nhân viên quản lý từ session
    ManagementStaff9 nv = (ManagementStaff9) session.getAttribute("nv");
    if (nv == null) {
        nv = new ManagementStaff9(1, "Nguyễn Văn Quản Lý", "1990-01-15", "Hà Nội", "quanly@cinema.com", "0912345678", "admin", "admin123", "Quản lý rạp");
        session.setAttribute("nv", nv);
    }

    boolean kq = false;
    Movie9 phim = (Movie9) request.getAttribute("phimMoi");
    String status = (String) request.getAttribute("status");

    String returnParams = "";

    if (phim != null && "success".equals(status)) {
        // Đã được xử lý thành công bởi AddMovieServlet9 / MovieServlet9
        kq = true;
    } else {
        String tenPhim = request.getParameter("txtTenPhim");
        String theLoai = request.getParameter("slTheLoai");
        String thoiLuongStr = request.getParameter("txtThoiLuong");
        String ngayKhoiChieuStr = request.getParameter("txtNgayKhoiChieu");
        String moTa = request.getParameter("txtMoTa");
        String anhPoster = request.getParameter("fileAnhPoster");

        returnParams = String.format("&ten=%s&theLoai=%s&thoiLuong=%s&khoiChieu=%s&moTa=%s",
            tenPhim != null ? URLEncoder.encode(tenPhim, "UTF-8") : "",
            theLoai != null ? URLEncoder.encode(theLoai, "UTF-8") : "",
            thoiLuongStr != null ? URLEncoder.encode(thoiLuongStr, "UTF-8") : "",
            ngayKhoiChieuStr != null ? URLEncoder.encode(ngayKhoiChieuStr, "UTF-8") : "",
            moTa != null ? URLEncoder.encode(moTa, "UTF-8") : ""
        );

        // 1. Kiểm tra để trống các trường bắt buộc
        if (tenPhim == null || tenPhim.trim().isEmpty() ||
            theLoai == null || theLoai.trim().isEmpty() ||
            thoiLuongStr == null || thoiLuongStr.trim().isEmpty() ||
            ngayKhoiChieuStr == null || ngayKhoiChieuStr.trim().isEmpty()) {
            response.sendRedirect("../management_view/MovieAddView9.jsp?err=" + 
                URLEncoder.encode("Các trường thông tin có dấu (*) là bắt buộc, không được để trống!", "UTF-8") + returnParams);
            return;
        }

        // 2. Kiểm tra thời lượng
        int thoiLuong = 0;
        try {
            thoiLuong = Integer.parseInt(thoiLuongStr.trim());
            if (thoiLuong <= 0) {
                response.sendRedirect("../management_view/MovieAddView9.jsp?err=" + 
                    URLEncoder.encode("Thời lượng phim phải là số nguyên dương lớn hơn 0!", "UTF-8") + returnParams);
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("../management_view/MovieAddView9.jsp?err=" + 
                URLEncoder.encode("Thời lượng phim không hợp lệ!", "UTF-8") + returnParams);
            return;
        }

        // 3. Kiểm tra ngày khởi chiếu
        Date khoiChieu = null;
        try {
            khoiChieu = Date.valueOf(ngayKhoiChieuStr.trim());
        } catch (IllegalArgumentException e) {
            response.sendRedirect("../management_view/MovieAddView9.jsp?err=" + 
                URLEncoder.encode("Định dạng ngày khởi chiếu không đúng (yyyy-MM-dd)!", "UTF-8") + returnParams);
            return;
        }

        // 4. Kiểm tra trùng lặp tên phim qua MovieDAO9.checkPhimTrung()
        MovieDAO9 movieDAO = new MovieDAO9();
        if (movieDAO.checkPhimTrung(tenPhim)) {
            response.sendRedirect("../management_view/MovieAddView9.jsp?err=" + 
                URLEncoder.encode("Tên bộ phim '" + tenPhim + "' đã tồn tại trên hệ thống! Vui lòng chọn tên khác.", "UTF-8") + returnParams);
            return;
        }

        // 5. Đóng gói đối tượng thực thể Movie9
        phim = new Movie9();
        phim.setTen(tenPhim.trim());
        phim.setTheLoai(theLoai.trim());
        phim.setThoiLuong(thoiLuong);
        phim.setKhoiChieu(khoiChieu);
        phim.setMoTa(moTa != null ? moTa.trim() : "");
        phim.setAnhPoster(anhPoster != null && !anhPoster.trim().isEmpty() ? anhPoster.trim() : "hlts.jpg");

        // 6. Gọi DAO thêm phim vào cơ sở dữ liệu qua luuPhim()
        kq = movieDAO.luuPhim(phim);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xử lý thêm phim - Kết quả</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
    <div class="container center-card">
        <% if (kq) { %>
            <div style="font-size: 56px; margin-bottom: 15px;">🎉</div>
            <h2 style="color: #276749;">Thêm thông tin phim thành công!</h2>
            <p style="color: #4a5568; margin-bottom: 25px;">
                Bộ phim <strong><%= phim.getTen() %></strong> (<%= phim.getMaPhim() %>) đã được lưu thành công vào cơ sở dữ liệu.
            </p>
            <a href="../management_view/MovieManagementView9.jsp?msg=success" id="btnOk" class="btn btn-primary" style="padding: 10px 30px;">
                OK (Quay lại danh sách phim)
            </a>
        <% } else { %>
            <div style="font-size: 56px; margin-bottom: 15px;">❌</div>
            <h2 style="color: #9b2c2c;">Lưu thông tin phim thất bại!</h2>
            <p style="color: #4a5568; margin-bottom: 25px;">
                Không thể lưu bộ phim vào hệ thống. Vui lòng kiểm tra lại kết nối cơ sở dữ liệu.
            </p>
            <a href="../management_view/MovieAddView9.jsp?<%= returnParams.length() > 0 ? returnParams.substring(1) : "" %>" class="btn btn-secondary">
                Quay lại biểu mẫu sửa
            </a>
        <% } %>
    </div>
</body>
</html>

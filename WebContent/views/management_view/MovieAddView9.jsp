<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.ManagementStaff9" %>
<%
    ManagementStaff9 nv = (ManagementStaff9) session.getAttribute("nv");
    if (nv == null) {
        nv = new ManagementStaff9(1, "Nguyễn Văn Quản Lý", "1990-01-15", "Hà Nội", "quanly@cinema.com", "0912345678", "admin", "admin123", "Quản lý rạp");
        session.setAttribute("nv", nv);
    }

    // Nhận thông báo lỗi từ request parameter (JSP redirect) hoặc request attribute (Servlet forward)
    String errorMsg = request.getParameter("err");
    if (errorMsg == null && request.getAttribute("err") != null) {
        errorMsg = (String) request.getAttribute("err");
    }

    String oldTen = request.getParameter("ten");
    if (oldTen == null && request.getAttribute("ten") != null) oldTen = (String) request.getAttribute("ten");

    String oldTheLoai = request.getParameter("theLoai");
    if (oldTheLoai == null && request.getAttribute("theLoai") != null) oldTheLoai = (String) request.getAttribute("theLoai");

    String oldThoiLuong = request.getParameter("thoiLuong");
    if (oldThoiLuong == null && request.getAttribute("thoiLuong") != null) oldThoiLuong = (String) request.getAttribute("thoiLuong");

    String oldKhoiChieu = request.getParameter("khoiChieu");
    if (oldKhoiChieu == null && request.getAttribute("khoiChieu") != null) oldKhoiChieu = (String) request.getAttribute("khoiChieu");

    String oldMoTa = request.getParameter("moTa");
    if (oldMoTa == null && request.getAttribute("moTa") != null) oldMoTa = (String) request.getAttribute("moTa");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm phim mới - Quản lý rạp phim</title>
    <link rel="stylesheet" href="../../css/style.css">
    <script>
        function validateForm() {
            var ten = document.getElementById("txtTenPhim").value.trim();
            var theLoai = document.getElementById("slTheLoai").value.trim();
            var thoiLuong = document.getElementById("txtThoiLuong").value.trim();
            var ngayKhoiChieu = document.getElementById("txtNgayKhoiChieu").value;

            if (ten === "") {
                alert("Vui lòng nhập tên phim!");
                document.getElementById("txtTenPhim").focus();
                return false;
            }
            if (theLoai === "") {
                alert("Vui lòng chọn hoặc nhập thể loại phim!");
                document.getElementById("slTheLoai").focus();
                return false;
            }
            var tl = parseInt(thoiLuong);
            if (isNaN(tl) || tl <= 0) {
                alert("Thời lượng phim phải là số nguyên dương lớn hơn 0!");
                document.getElementById("txtThoiLuong").focus();
                return false;
            }
            if (ngayKhoiChieu === "") {
                alert("Vui lòng chọn ngày khởi chiếu!");
                document.getElementById("txtNgayKhoiChieu").focus();
                return false;
            }

            // Kiểm tra ngày khởi chiếu >= hôm nay theo kịch bản Pha 2 (bước 6a)
            var selectedDate = new Date(ngayKhoiChieu);
            var today = new Date();
            today.setHours(0, 0, 0, 0);
            if (selectedDate < today) {
                alert("Ngày khởi chiếu phải lớn hơn hoặc bằng ngày hiện tại!");
                document.getElementById("txtNgayKhoiChieu").focus();
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
    <div class="container">
        <div class="header-banner">
            <h1>Hệ Thống Quản Lý Phim</h1>
            <div class="user-badge">
                👤 Quản lý: <strong><%= nv.getHoTen() %></strong>
            </div>
        </div>

        <div class="form-card">
            <h2 style="text-align: center; margin-bottom: 25px;">Thêm phim mới</h2>

            <% if (errorMsg != null && !errorMsg.trim().isEmpty()) { %>
                <div class="alert alert-error">
                    ⚠ <strong>Lỗi:</strong> <%= errorMsg %>
                </div>
            <% } %>

            <!-- Form gọi trang điều khiển doAddMovie9.jsp theo thiết kế lớp chi tiết và sequence diagram (Bước 14) -->
            <form action="../common_controllers/doAddMovie9.jsp" method="post" onsubmit="return validateForm();">
                <div class="form-group">
                    <label for="txtTenPhim">Tên phim <span class="required">*</span></label>
                    <input type="text" id="txtTenPhim" name="txtTenPhim" class="form-control" 
                           placeholder="Ví dụ: Hope: Vùng tử địa" 
                           value="<%= oldTen != null ? oldTen : "" %>" required>
                </div>

                <div class="form-group">
                    <label for="slTheLoai">Thể loại <span class="required">*</span></label>
                    <input type="text" list="genre-list" id="slTheLoai" name="slTheLoai" class="form-control" 
                           placeholder="Chọn hoặc nhập thể loại (Ví dụ: Hành động, Bí ẩn)" 
                           value="<%= oldTheLoai != null ? oldTheLoai : "" %>" required>
                    <datalist id="genre-list">
                        <option value="Hành động, Phiêu lưu">
                        <option value="Chính kịch, Hành động">
                        <option value="Hài, Gay cấn">
                        <option value="Bí ẩn, Khoa học - Viễn tưởng, Hành động">
                        <option value="Kinh dị, Giật gân">
                        <option value="Hoạt hình, Gia đình">
                        <option value="Tình cảm, Lãng mạn">
                    </datalist>
                </div>

                <div class="form-group">
                    <label for="txtThoiLuong">Thời lượng (phút) <span class="required">*</span></label>
                    <input type="number" id="txtThoiLuong" name="txtThoiLuong" class="form-control" 
                           min="1" placeholder="Ví dụ: 120" 
                           value="<%= oldThoiLuong != null ? oldThoiLuong : "" %>" required>
                </div>

                <div class="form-group">
                    <label for="txtNgayKhoiChieu">Ngày khởi chiếu <span class="required">*</span></label>
                    <input type="date" id="txtNgayKhoiChieu" name="txtNgayKhoiChieu" class="form-control" 
                           value="<%= oldKhoiChieu != null ? oldKhoiChieu : "" %>" required>
                </div>

                <div class="form-group">
                    <label for="txtMoTa">Mô tả</label>
                    <textarea id="txtMoTa" name="txtMoTa" class="form-control" 
                              placeholder="Nhập giới hạn độ tuổi, ngôn ngữ, tóm tắt nội dung..."><%= oldMoTa != null ? oldMoTa : "" %></textarea>
                </div>

                <div class="form-actions">
                    <!-- Nút Hủy theo thiết kế chi tiết: quay lại MovieManagementView9.jsp -->
                    <a href="MovieManagementView9.jsp" id="btnHuy" class="btn btn-secondary">
                        Hủy
                    </a>
                    <!-- Nút Thêm submit form -->
                    <button type="submit" id="btnThem" class="btn btn-primary">
                        Thêm
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>

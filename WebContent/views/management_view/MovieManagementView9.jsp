<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Movie9" %>
<%@ page import="model.ManagementStaff9" %>
<%@ page import="dao.MovieDAO9" %>
<%
    // Kiểm tra nhân viên quản lý
    ManagementStaff9 nv = (ManagementStaff9) session.getAttribute("nv");
    if (nv == null) {
        nv = new ManagementStaff9(1, "Nguyễn Văn Quản Lý", "1990-01-15", "Hà Nội", "quanly@cinema.com", "0912345678", "admin", "admin123", "Quản lý rạp");
        session.setAttribute("nv", nv);
    }

    // Lấy danh sách phim thông qua DAO theo đúng biểu đồ tuần tự (Bước 3 & Bước 4)
    MovieDAO9 movieDAO = new MovieDAO9();
    ArrayList<Movie9> listPhim = movieDAO.getDSPhim();

    String msg = request.getParameter("msg");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý phim - Hệ thống rạp chiếu</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
    <div class="container">
        <div class="header-banner">
            <h1>Quản lý phim</h1>
            <div class="user-badge">
                👤 Quản lý: <strong><%= nv.getHoTen() %></strong>
            </div>
        </div>

        <% if ("success".equals(msg)) { %>
            <div class="alert alert-success">
                ✓ Thêm thông tin phim mới thành công! Bản ghi đã được cập nhật vào danh sách bên dưới.
            </div>
        <% } %>

        <div class="btn-action-bar">
            <h2>Danh sách phim</h2>
            <!-- Nút Thêm phim mới theo thiết kế chi tiết (btnThemPhim) -->
            <a href="MovieAddView9.jsp" id="btnThemPhim" class="btn btn-primary">
                + Thêm phim mới
            </a>
        </div>

        <!-- Bảng danh sách phim theo thiết kế chi tiết (tblPhim) -->
        <table class="data-table" id="tblPhim">
            <thead>
                <tr>
                    <th style="width: 8%;">Mã</th>
                    <th style="width: 20%;">Tên</th>
                    <th style="width: 18%;">Thể loại</th>
                    <th style="width: 12%;">Thời lượng</th>
                    <th style="width: 14%;">Ngày khởi chiếu</th>
                    <th style="width: 28%;">Mô tả</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (listPhim == null || listPhim.isEmpty()) {
                %>
                    <tr>
                        <td colspan="6" style="text-align: center; color: #a0aec0; padding: 25px;">
                            Hiện chưa có bộ phim nào trong hệ thống. Nhấn "+ Thêm phim mới" để thêm.
                        </td>
                    </tr>
                <%
                    } else {
                        for (Movie9 m : listPhim) {
                %>
                    <tr>
                        <td style="text-align: center; font-weight: bold; color: #2b6cb0;"><%= m.getMaPhim() %></td>
                        <td style="font-weight: 600;"><%= m.getTen() %></td>
                        <td><%= m.getTheLoai() %></td>
                        <td style="text-align: center;"><%= m.getThoiLuong() %> phút</td>
                        <td style="text-align: center;"><%= m.getKhoiChieuFormatted() %></td>
                        <td style="font-size: 13px; line-height: 1.4;"><%= m.getMoTa() %></td>
                    </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <div>
            <!-- Nút Quay lại theo thiết kế chi tiết (btnQuayLai) -->
            <a href="ManagementStaffHomeView9.jsp" id="btnQuayLai" class="btn btn-outline">
                &larr; Quay lại
            </a>
        </div>
    </div>
</body>
</html>

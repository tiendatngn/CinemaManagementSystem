<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.ManagementStaff9" %>
<%
    // Kiểm tra phiên làm việc của Nhân viên Quản lý
    ManagementStaff9 nv = (ManagementStaff9) session.getAttribute("nv");
    if (nv == null) {
        // Tạo đối tượng nhân viên mặc định theo kịch bản nếu chưa có session
        nv = new ManagementStaff9(1, "Nguyễn Văn Quản Lý", "1990-01-15", "Hà Nội", "quanly@cinema.com", "0912345678", "admin", "admin123", "Quản lý rạp");
        session.setAttribute("nv", nv);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ NVQL - Quản lý rạp phim</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
    <div class="container center-card">
        <div class="header-banner">
            <h1>Hệ Thống Quản Lý Rạp Chiếu Phim</h1>
            <div class="user-badge">
                👤 Quản lý: <strong><%= nv.getHoTen() %></strong> (<%= nv.getViTri() %>)
            </div>
        </div>

        <div style="margin-top: 40px; margin-bottom: 40px;">
            <h2>Trang chủ NVQL</h2>
            <p style="color: #718096; margin-bottom: 25px;">Chọn chức năng cần thực hiện bên dưới:</p>
            
            <!-- Nút chức năng Quản lý phim theo đúng thiết kế chi tiết -->
            <form action="MovieManagementView9.jsp" method="get">
                <button type="submit" name="QuanLyPhim" id="QuanLyPhim" class="btn btn-primary" style="font-size: 16px; padding: 12px 30px;">
                    🎬 Quản lý phim
                </button>
            </form>
        </div>
    </div>
</body>
</html>

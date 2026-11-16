<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hệ thống Quản lý Rạp chiếu phim - Cinema 9</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .portal-box {
            max-width: 650px;
            margin: 80px auto;
            background: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            text-align: center;
        }
        .portal-title {
            color: #2c3e50;
            margin-bottom: 10px;
        }
        .portal-sub {
            color: #7f8c8d;
            margin-bottom: 40px;
        }
        .portal-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .portal-card {
            border: 2px solid #e1e8ed;
            border-radius: 8px;
            padding: 30px 20px;
            text-decoration: none;
            color: inherit;
            transition: 0.3s;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .portal-card:hover {
            border-color: #3498db;
            transform: translateY(-4px);
            box-shadow: 0 6px 15px rgba(52, 152, 219, 0.2);
        }
        .portal-icon {
            font-size: 40px;
            margin-bottom: 15px;
        }
        .portal-btn {
            margin-top: 15px;
            background: #3498db;
            color: white;
            padding: 8px 18px;
            border-radius: 4px;
            font-weight: bold;
            font-size: 14px;
        }
    </style>
</head>
<body style="background-color: #f4f7f6; font-family: Arial, sans-serif;">
    <div class="portal-box">
        <h1 class="portal-title">HỆ THỐNG RẠP CHIẾU PHIM CINEMA 9</h1>
        <p class="portal-sub">Đề tài số 13 - Môn: Phân tích và Thiết kế Hệ thống Thông tin</p>

        <div class="portal-grid">
            <a href="<%= request.getContextPath() %>/views/customer_view/CustomerHomeView9.jsp" class="portal-card">
                <div class="portal-icon">🎟️</div>
                <h3 style="margin: 0; color: #2c3e50;">Khách Hàng</h3>
                <p style="color: #7f8c8d; font-size: 13px; margin: 8px 0;">Xem phim, đặt vé online, áp dụng mã thẻ thành viên</p>
                <div class="portal-btn">Vào Đặt Vé</div>
            </a>

            <a href="<%= request.getContextPath() %>/movie?action=home" class="portal-card">
                <div class="portal-icon">🎬</div>
                <h3 style="margin: 0; color: #2c3e50;">Nhân Viên Quản Lý</h3>
                <p style="color: #7f8c8d; font-size: 13px; margin: 8px 0;">Quản lý danh sách phim, thêm thông tin phim mới</p>
                <div class="portal-btn">Quản Trị Phim</div>
            </a>
        </div>
    </div>
</body>
</html>

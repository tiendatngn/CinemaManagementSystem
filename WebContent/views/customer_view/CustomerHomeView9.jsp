<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Customer9" %>
<%
    Customer9 kh = (Customer9) session.getAttribute("kh");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Rạp chiếu phim - Cinema 9</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .home-box {
            text-align: center;
            background: white;
            padding: 80px 40px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            max-width: 600px;
            margin: 60px auto;
        }
        .btn-booking {
            background-color: #3498db;
            color: white;
            font-size: 20px;
            padding: 15px 45px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-weight: bold;
            box-shadow: 0 4px 10px rgba(52, 152, 219, 0.4);
            transition: 0.3s;
        }
        .btn-booking:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body style="background-color: #f8f9fa; font-family: Arial, sans-serif;">
    <div style="background-color: #2c3e50; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">CINEMA 9 - HỆ THỐNG RẠP CHIẾU PHIM</h2>
        <div>
            <% if (kh != null) { %>
                <span>Xin chào, <strong><%= kh.getHoTen() %></strong>!</span>
                <a href="<%= request.getContextPath() %>/login?action=logout" style="color: #ecf0f1; margin-left: 15px;">Đăng xuất</a>
            <% } else { %>
                <a href="<%= request.getContextPath() %>/login" style="color: #ecf0f1; margin-left: 15px; text-decoration: none; padding: 6px 12px; background: #34495e; border-radius: 4px;">Đăng nhập</a>
            <% } %>
        </div>
    </div>

    <div class="home-box">
        <h1 style="color: #2c3e50; margin-bottom: 20px;">Trang chủ rạp chiếu phim</h1>
        <p style="color: #7f8c8d; margin-bottom: 40px; font-size: 16px;">Chào mừng quý khách đến với hệ thống đặt vé trực tuyến nhanh chóng và thuận tiện.</p>
        <a href="<%= request.getContextPath() %>/views/customer_view/MovieListView9.jsp" class="btn-booking">Đặt vé</a>
    </div>
</body>
</html>

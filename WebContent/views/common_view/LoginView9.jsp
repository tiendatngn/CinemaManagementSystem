<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập - Hệ thống Rạp chiếu phim</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .login-card {
            max-width: 420px;
            margin: 60px auto;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .login-card h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #2c3e50;
        }
        .form-group {
            margin-bottom: 18px;
        }
        .form-group label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
        }
        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        .btn-login {
            background-color: #3498db;
            color: white;
            flex: 1;
            padding: 10px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        .btn-register {
            background-color: #2ecc71;
            color: white;
            flex: 1;
            padding: 10px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        .btn-back {
            background-color: #95a5a6;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            margin-top: 15px;
            text-align: center;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
    </style>
</head>
<body style="background-color: #f4f7f6; font-family: Arial, sans-serif;">
    <div class="login-card">
        <h2>Đăng nhập</h2>

        <% String err = (String) request.getAttribute("err");
           if (err != null && !err.isEmpty()) { %>
            <div class="alert-danger"><%= err %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/login" method="post">
            <input type="hidden" name="redirect" value="<%= request.getParameter("redirect") != null ? request.getParameter("redirect") : "" %>">
            
            <div class="form-group">
                <label for="txtUsername">Username</label>
                <input type="text" id="txtUsername" name="txtUsername" placeholder="Nhập tên đăng nhập" required>
            </div>

            <div class="form-group">
                <label for="txtPassword">Password</label>
                <input type="password" id="txtPassword" name="txtPassword" placeholder="Nhập mật khẩu" required>
            </div>

            <div class="btn-group">
                <button type="button" class="btn-register" onclick="alert('Chức năng Đăng ký tài khoản dành cho khách hàng mới!')">Đăng ký</button>
                <button type="submit" class="btn-login">Đăng nhập</button>
            </div>
        </form>

        <div style="text-align: center;">
            <a href="<%= request.getContextPath() %>/views/customer_view/CustomerHomeView9.jsp" class="btn-back">← Quay lại Trang chủ</a>
        </div>
        
        <div style="margin-top: 20px; font-size: 13px; color: #7f8c8d; text-align: center; background: #eef2f5; padding: 8px; border-radius: 4px;">
            <strong>Tài khoản test theo kịch bản:</strong><br>
            • Quản lý: <code>admin</code> / <code>admin123</code><br>
            • Khách hàng: <code>customer</code> / <code>customer123</code>
        </div>
    </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Bill9" %>
<%@ page import="model.Showtime9" %>
<%@ page import="model.Customer9" %>
<%
    Bill9 bill = (Bill9) request.getAttribute("bill");
    Showtime9 suatChieu = (Showtime9) request.getAttribute("suatChieu");
    Boolean isSuccess = (Boolean) request.getAttribute("paymentSuccess");

    float tienGoc = (bill != null) ? bill.getTienGoc() : 382000.0f;
    float giamGia = (bill != null) ? bill.getGiamGia() : 0.0f;
    float thucTe = (bill != null) ? bill.getThucTe() : (tienGoc - giamGia);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh toán và Vé xem phim - Cinema 9</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .payment-card {
            max-width: 750px;
            margin: 30px auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            position: relative;
        }
        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #ecf0f1;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }
        .movie-name {
            font-size: 24px;
            font-weight: bold;
            color: #2c3e50;
        }
        .timer {
            font-size: 26px;
            font-weight: bold;
            color: #e74c3c;
        }
        .body-section {
            display: flex;
            justify-content: space-between;
            gap: 30px;
        }
        .info-col {
            flex: 1;
        }
        .qr-col {
            width: 200px;
            text-align: center;
            border: 2px dashed #bdc3c7;
            padding: 15px;
            border-radius: 8px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background: #fafafa;
        }
        .qr-box {
            width: 150px;
            height: 150px;
            background: #2c3e50;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            font-weight: bold;
            border-radius: 6px;
            margin-bottom: 8px;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
            font-size: 15px;
        }
        .info-label {
            color: #7f8c8d;
        }
        .info-val {
            font-weight: bold;
            color: #2c3e50;
        }
        .loyalty-box {
            margin-top: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 6px;
            border: 1px solid #dfe6e9;
        }
        .loyalty-box input {
            padding: 8px;
            width: 200px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .btn-group {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
            border-top: 1px solid #eee;
            padding-top: 20px;
        }
        .btn {
            padding: 10px 22px;
            border: none;
            border-radius: 4px;
            font-size: 15px;
            cursor: pointer;
            text-decoration: none;
            font-weight: bold;
        }
        .btn-cancel { background: #e74c3c; color: white; }
        .btn-save { background: #3498db; color: white; }
        .btn-confirm { background: #3498db; color: white; }
        .success-banner {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: bold;
        }
    </style>
</head>
<body style="background-color: #f4f7f6; font-family: Arial, sans-serif;">
    <div class="payment-card">
        <% if (isSuccess != null && isSuccess) { %>
            <div class="success-banner">
                🎉 ĐẶT VÉ VÀ THANH TOÁN THÀNH CÔNG!<br>
                Mã hóa đơn: #HD0<%= bill != null ? bill.getId() : 1 %> | Trạng thái vé: ĐÃ BÁN
            </div>
        <% } %>

        <div class="header-section">
            <div class="movie-name">
                <%= suatChieu != null && suatChieu.getPhim() != null ? suatChieu.getPhim().getTen() : "Trại Buôn Người" %>
            </div>
            <div class="timer" id="countdown">7:59</div>
        </div>

        <form action="<%= request.getContextPath() %>/payment" method="post">
            <div class="body-section">
                <div class="info-col">
                    <div class="info-row">
                        <span class="info-label">Thời gian:</span>
                        <span class="info-val">15:30 ~ 17:35</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Ngày chiếu:</span>
                        <span class="info-val">13/10/2026</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Phòng chiếu:</span>
                        <span class="info-val">Cinema 6</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Định dạng:</span>
                        <span class="info-val">2D Phụ đề</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Ghế:</span>
                        <span class="info-val">F5, F6, F7, F8</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Tổng tiền gốc:</span>
                        <span class="info-val" style="color: #27ae60; font-size: 18px;"><%= String.format("%,.0f", tienGoc) %>đ</span>
                    </div>
                    <% if (giamGia > 0) { %>
                        <div class="info-row">
                            <span class="info-label">Giảm giá thẻ thành viên:</span>
                            <span class="info-val" style="color: #e74c3c;">-<%= String.format("%,.0f", giamGia) %>đ</span>
                        </div>
                    <% } %>
                    <div class="info-row">
                        <span class="info-label">Thanh toán thực tế:</span>
                        <span class="info-val" style="color: #c0392b; font-size: 20px;"><%= String.format("%,.0f", thucTe) %>đ</span>
                    </div>

                    <div class="loyalty-box">
                        <label for="txtMaTheTv" style="font-weight: bold; color: #2c3e50;">Nhập mã thẻ TV (nếu có):</label><br>
                        <input type="text" id="txtMaTheTv" name="txtMaTheTv" placeholder="Ví dụ: CARD001" style="margin-top: 6px;">
                        <span style="font-size: 12px; color: #7f8c8d; display: block; margin-top: 4px;">*Nhập mã thẻ VIP để được giảm 10% và tích điểm</span>
                    </div>
                </div>

                <div class="qr-col">
                    <div class="qr-box">Mã QR</div>
                    <span style="font-size: 13px; font-weight: bold; color: #2c3e50;">Quét mã thanh toán</span>
                    <span style="font-size: 11px; color: #7f8c8d; margin-top: 4px;">Ngân hàng MB Bank<br>STK: 0912345678</span>
                </div>
            </div>

            <div class="btn-group">
                <a href="<%= request.getContextPath() %>/views/customer_view/MovieListView9.jsp" class="btn btn-cancel">Hủy</a>
                
                <% if (isSuccess != null && isSuccess) { %>
                    <button type="button" class="btn btn-save" onclick="alert('Đã tải hóa đơn điện tử về máy tính!')">Lưu hóa đơn</button>
                    <a href="<%= request.getContextPath() %>/views/customer_view/CustomerHomeView9.jsp" class="btn btn-confirm">Hoàn tất</a>
                <% } else { %>
                    <button type="button" class="btn btn-save" onclick="alert('Vui lòng hoàn tất thanh toán trước khi lưu hóa đơn!')">Lưu hóa đơn</button>
                    <button type="submit" class="btn btn-confirm">Xác nhận thanh toán</button>
                <% } %>
            </div>
        </form>
    </div>

    <script>
        // Đồng hồ đếm ngược 8 phút
        let time = 479;
        const countdownEl = document.getElementById('countdown');
        if (countdownEl) {
            setInterval(() => {
                const minutes = Math.floor(time / 60);
                let seconds = time % 60;
                seconds = seconds < 10 ? '0' + seconds : seconds;
                countdownEl.innerHTML = `${minutes}:${seconds}`;
                if (time > 0) time--;
            }, 1000);
        }
    </script>
</body>
</html>

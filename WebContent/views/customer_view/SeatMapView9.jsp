<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Showtime9" %>
<%@ page import="model.Seat9" %>
<%@ page import="model.Ticket9" %>
<%
    Showtime9 suatChieu = (Showtime9) request.getAttribute("suatChieu");
    int suatId = suatChieu != null ? suatChieu.getId() : 1;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sơ đồ phòng chiếu - Cinema 9</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .seatmap-container {
            max-width: 720px;
            margin: 25px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .screen {
            background: #dfe6e9;
            height: 35px;
            line-height: 35px;
            text-align: center;
            font-weight: bold;
            color: #2d3436;
            margin-bottom: 30px;
            border-radius: 4px;
            letter-spacing: 2px;
        }
        .grid-seats {
            display: grid;
            grid-template-columns: repeat(10, 1fr);
            gap: 8px;
            margin-bottom: 25px;
        }
        .seat {
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 11px;
            font-weight: bold;
            border-radius: 4px;
            cursor: pointer;
            user-select: none;
            transition: 0.15s;
        }
        .seat-normal { background-color: #a29bfe; color: #2d3436; }
        .seat-vip { background-color: #fab1a0; color: #2d3436; }
        .seat-couple { background-color: #fd79a8; color: white; }
        .seat-booked { background-color: #b2bec3; color: white; cursor: not-allowed; }
        .seat-selected { background-color: #d63031 !important; color: white !important; }

        .legend {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-bottom: 25px;
            font-size: 13px;
        }
        .legend-item { display: flex; align-items: center; gap: 6px; }
        .legend-color { width: 16px; height: 16px; border-radius: 3px; }

        .actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .btn-action {
            background-color: #3498db;
            color: white;
            padding: 10px 24px;
            border: none;
            border-radius: 4px;
            font-size: 15px;
            cursor: pointer;
            text-decoration: none;
        }
        .btn-action:hover { background-color: #2980b9; }
    </style>
</head>
<body style="background-color: #f4f7f6; font-family: Arial, sans-serif;">
    <div class="seatmap-container">
        <h2 style="text-align: center; color: #2c3e50; margin-bottom: 20px;">Sơ đồ phòng chiếu</h2>

        <div class="screen">MÀN HÌNH</div>

        <form id="seatForm" action="<%= request.getContextPath() %>/selectSeat" method="post">
            <input type="hidden" name="suatChieuId" value="<%= suatId %>">

            <!-- Lưới ghế theo đúng mockup trong báo cáo Pha 3 -->
            <div class="grid-seats">
                <!-- Hàng A (Thường) -->
                <% for(int i=1; i<=10; i++) { %>
                    <div class="seat seat-normal" onclick="toggleSeat(this, <%= i %>)">A<%= i %></div>
                <% } %>

                <!-- Hàng B (Thường, có B7 đã đặt) -->
                <% for(int i=1; i<=10; i++) { 
                    if(i == 7) { %>
                        <div class="seat seat-booked">B7</div>
                <%  } else { %>
                        <div class="seat seat-normal" onclick="toggleSeat(this, <%= 10+i %>)">B<%= i %></div>
                <%  } } %>

                <!-- Hàng C (Thường, có C9 đã đặt) -->
                <% for(int i=1; i<=10; i++) { 
                    if(i == 9) { %>
                        <div class="seat seat-booked">C9</div>
                <%  } else { %>
                        <div class="seat seat-normal" onclick="toggleSeat(this, <%= 20+i %>)">C<%= i %></div>
                <%  } } %>

                <!-- Hàng D (VIP) -->
                <% for(int i=1; i<=10; i++) { %>
                    <div class="seat seat-vip" onclick="toggleSeat(this, <%= 30+i %>)">D<%= i %></div>
                <% } %>

                <!-- Hàng E (VIP) -->
                <% for(int i=1; i<=10; i++) { %>
                    <div class="seat seat-vip" onclick="toggleSeat(this, <%= 40+i %>)">E<%= i %></div>
                <% } %>

                <!-- Hàng F (VIP, F5, F6, F7, F8 đang chọn như mockup) -->
                <% for(int i=1; i<=10; i++) { 
                    boolean isMockSelected = (i >= 5 && i <= 8);
                %>
                    <div class="seat seat-vip <%= isMockSelected ? "seat-selected" : "" %>" 
                         onclick="toggleSeat(this, <%= 50+i %>)" 
                         data-id="<%= 50+i %>">F<%= i %></div>
                <% } %>

                <!-- Hàng G (VIP) -->
                <% for(int i=1; i<=10; i++) { %>
                    <div class="seat seat-vip" onclick="toggleSeat(this, <%= 60+i %>)">G<%= i %></div>
                <% } %>
            </div>

            <!-- Chú thích -->
            <div class="legend">
                <div class="legend-item"><div class="legend-color seat-booked"></div> Đã đặt</div>
                <div class="legend-item"><div class="legend-color seat-selected"></div> Ghế đang chọn</div>
                <div class="legend-item"><div class="legend-color seat-normal"></div> Ghế thường</div>
                <div class="legend-item"><div class="legend-color seat-vip"></div> VIP</div>
                <div class="legend-item"><div class="legend-color seat-couple"></div> Ghế đôi</div>
            </div>

            <!-- Các input ẩn chứa ghế đang chọn -->
            <div id="hiddenInputs">
                <input type="hidden" name="selectedSeats" value="3">
                <input type="hidden" name="selectedSeats" value="4">
                <input type="hidden" name="selectedSeats" value="5">
                <input type="hidden" name="selectedSeats" value="6">
            </div>

            <div class="actions">
                <a href="<%= request.getContextPath() %>/views/customer_view/ShowtimesSelectView9.jsp" class="btn-action">← Quay lại</a>
                <button type="submit" class="btn-action">Xác nhận</button>
            </div>
        </form>
    </div>

    <script>
        function toggleSeat(elem, seatId) {
            if (elem.classList.contains('seat-booked')) return;
            elem.classList.toggle('seat-selected');
            updateHiddenInputs();
        }

        function updateHiddenInputs() {
            const container = document.getElementById('hiddenInputs');
            container.innerHTML = '';
            document.querySelectorAll('.seat.seat-selected').forEach(elem => {
                const id = elem.getAttribute('data-id') || 3;
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'selectedSeats';
                input.value = id;
                container.appendChild(input);
            });
        }
    </script>
</body>
</html>

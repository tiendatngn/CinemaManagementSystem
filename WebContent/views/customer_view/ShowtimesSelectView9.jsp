<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Movie9" %>
<%@ page import="model.Showtime9" %>
<%@ page import="dao.ShowtimeDAO9" %>
<%
    Movie9 phim = (Movie9) request.getAttribute("phim");
    ArrayList<Showtime9> listSuatChieu = (ArrayList<Showtime9>) request.getAttribute("listSuatChieu");

    if (listSuatChieu == null) {
        ShowtimeDAO9 stDAO = new ShowtimeDAO9();
        listSuatChieu = stDAO.getDSSuatChieu(3); // Mặc định suất chiếu phim Trại Buôn Người
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chọn suất chiếu - Cinema 9</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .container-showtime {
            max-width: 650px;
            margin: 40px auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .date-tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 25px;
            border-bottom: 2px solid #eee;
            padding-bottom: 12px;
        }
        .date-tab {
            text-align: center;
            padding: 8px 14px;
            background: #f8f9fa;
            border-radius: 6px;
            cursor: pointer;
            border: 1px solid #dcdde1;
        }
        .date-tab.active {
            background: #2c3e50;
            color: white;
            border-color: #2c3e50;
        }
        .date-tab .day { font-size: 11px; }
        .date-tab .date { font-size: 18px; font-weight: bold; }
        .time-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 15px;
            margin-bottom: 30px;
        }
        .time-slot {
            border: 1px solid #bdc3c7;
            padding: 12px 10px;
            text-align: center;
            border-radius: 6px;
            cursor: pointer;
            transition: 0.2s;
        }
        .time-slot:hover {
            border-color: #3498db;
            background: #ebf5fb;
        }
        .time-slot.selected {
            background-color: #3498db;
            color: white;
            border-color: #3498db;
        }
        .time-start { font-weight: bold; font-size: 15px; }
        .time-room { font-size: 12px; margin-top: 4px; color: #7f8c8d; }
        .time-slot.selected .time-room { color: #ecf0f1; }
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
    <div class="container-showtime">
        <h2 style="text-align: center; color: #2c3e50; margin-bottom: 25px;">
            Chọn suất chiếu
        </h2>

        <% if (phim != null) { %>
            <div style="text-align: center; margin-bottom: 15px; font-weight: bold; color: #34495e;">
                Phim: <%= phim.getTen() %>
            </div>
        <% } %>

        <!-- Tab ngày chiếu theo mockup Pha 3 -->
        <div class="date-tabs">
            <div class="date-tab active">
                <div class="day">Hôm nay</div>
                <div class="date">12</div>
            </div>
            <div class="date-tab">
                <div class="day">Thứ 3</div>
                <div class="date">13</div>
            </div>
            <div class="date-tab">
                <div class="day">Thứ 4</div>
                <div class="date">14</div>
            </div>
            <div class="date-tab">
                <div class="day">Thứ 5</div>
                <div class="date">15</div>
            </div>
            <div class="date-tab">
                <div class="day">Thứ 6</div>
                <div class="date">16</div>
            </div>
        </div>

        <form id="showtimeForm" action="<%= request.getContextPath() %>/selectShowtime" method="post">
            <input type="hidden" id="selectedSuatChieu" name="idSuatChieu" value="<%= listSuatChieu != null && !listSuatChieu.isEmpty() ? listSuatChieu.get(0).getId() : 1 %>">
            <input type="hidden" id="selectedPhong" name="idPhong" value="<%= listSuatChieu != null && !listSuatChieu.isEmpty() ? listSuatChieu.get(0).getPhongChieu().getId() : 1 %>">

            <div class="time-grid">
                <% if (listSuatChieu != null && !listSuatChieu.isEmpty()) {
                    int idx = 0;
                    for (Showtime9 st : listSuatChieu) { %>
                        <div class="time-slot <%= idx == 0 ? "selected" : "" %>" 
                             onclick="selectSlot(this, <%= st.getId() %>, <%= st.getPhongChieu() != null ? st.getPhongChieu().getId() : 1 %>)">
                            <div class="time-start"><%= st.getBatDauFormatted() %> → <%= st.getKetThucFormatted() %></div>
                            <div class="time-room"><%= st.getPhongChieu() != null ? st.getPhongChieu().getTen() : "Cinema 6" %></div>
                        </div>
                <%      idx++;
                    }
                   } else { %>
                    <div class="time-slot selected" onclick="selectSlot(this, 1, 1)">
                        <div class="time-start">15:30 → 17:35</div>
                        <div class="time-room">Cinema 6</div>
                    </div>
                <% } %>
            </div>

            <div class="actions">
                <a href="<%= request.getContextPath() %>/views/customer_view/MovieListView9.jsp" class="btn-action">← Quay lại</a>
                <button type="submit" class="btn-action">Xác nhận</button>
            </div>
        </form>
    </div>

    <script>
        function selectSlot(element, suatId, phongId) {
            document.querySelectorAll('.time-slot').forEach(el => el.classList.remove('selected'));
            element.classList.add('selected');
            document.getElementById('selectedSuatChieu').value = suatId;
            document.getElementById('selectedPhong').value = phongId;
        }
    </script>
</body>
</html>

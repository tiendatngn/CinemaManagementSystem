<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.MovieDAO9" %>
<%@ page import="model.Movie9" %>
<%
    MovieDAO9 movieDAO = new MovieDAO9();
    ArrayList<Movie9> listPhim = movieDAO.getDSPhimDangChieu();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách phim đang chiếu - Cinema 9</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .container-movie {
            max-width: 900px;
            margin: 30px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }
        .movie-card {
            display: flex;
            align-items: center;
            border: 1px solid #e1e8ed;
            border-radius: 6px;
            padding: 15px;
            margin-bottom: 20px;
            transition: 0.2s;
        }
        .movie-card:hover {
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        .movie-poster {
            width: 90px;
            height: 120px;
            background-color: #bdc3c7;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #2c3e50;
            font-weight: bold;
            border-radius: 4px;
            margin-right: 20px;
            flex-shrink: 0;
        }
        .movie-info {
            flex-grow: 1;
        }
        .movie-title {
            font-size: 18px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 6px;
        }
        .movie-meta {
            color: #7f8c8d;
            font-size: 14px;
            margin-bottom: 8px;
        }
        .btn-buy {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            white-space: nowrap;
        }
        .btn-buy:hover {
            background-color: #2980b9;
        }
        .btn-back {
            background-color: #3498db;
            color: white;
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            margin-top: 15px;
        }
    </style>
</head>
<body style="background-color: #f4f7f6; font-family: Arial, sans-serif;">
    <div class="container-movie">
        <h2 style="color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; margin-bottom: 25px;">
            Danh sách phim đang chiếu
        </h2>

        <% if (listPhim != null && !listPhim.isEmpty()) {
            for (Movie9 m : listPhim) { %>
                <div class="movie-card">
                    <div class="movie-poster">
                        <%= m.getAnhPoster() != null && !m.getAnhPoster().isEmpty() ? m.getAnhPoster() : "Poster" %>
                    </div>
                    <div class="movie-info">
                        <div class="movie-title">
                            <%= m.getTen() %>
                            <span style="border: 1px solid #e74c3c; color: #e74c3c; font-size: 11px; padding: 2px 4px; border-radius: 3px; margin-left: 6px;">18+</span>
                        </div>
                        <div class="movie-meta"><%= m.getTheLoai() %> • <%= m.getThoiLuong() %> phút</div>
                        <div style="font-size: 13px; color: #34495e;"><%= m.getMoTa() %></div>
                    </div>
                    <div>
                        <a href="<%= request.getContextPath() %>/selectShowtime?movieId=<%= m.getId() %>" class="btn-buy">Mua vé ngay</a>
                    </div>
                </div>
        <%  }
           } else { %>
            <p>Hiện tại chưa có phim nào đang chiếu.</p>
        <% } %>

        <div>
            <a href="<%= request.getContextPath() %>/views/customer_view/CustomerHomeView9.jsp" class="btn-back">← Quay lại</a>
        </div>
    </div>
</body>
</html>

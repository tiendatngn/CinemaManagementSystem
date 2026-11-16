package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MovieDAO9;
import dao.ShowtimeDAO9;
import model.Movie9;
import model.Showtime9;

/**
 * Servlet điều khiển: SelectShowtimesServlet9
 * Thiết kế chuẩn theo Sơ đồ gói, Thiết kế tĩnh và Sequence Diagram Pha 3
 * Xử lý lấy danh sách suất chiếu của phim và điều hướng chọn suất
 */
@WebServlet(name = "SelectShowtimesServlet9", urlPatterns = {"/selectShowtime", "/showtimes"})
public class SelectShowtimesServlet9 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ShowtimeDAO9 showtimeDAO;
    private MovieDAO9 movieDAO;

    @Override
    public void init() {
        showtimeDAO = new ShowtimeDAO9();
        movieDAO = new MovieDAO9();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String movieIdStr = request.getParameter("movieId");
        if (movieIdStr == null || movieIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/views/customer_view/MovieListView9.jsp");
            return;
        }

        try {
            int movieId = Integer.parseInt(movieIdStr);
            Movie9 phim = movieDAO.getMovieById(movieId);
            ArrayList<Showtime9> listSuatChieu = showtimeDAO.getDSSuatChieu(movieId);

            request.setAttribute("phim", phim);
            request.setAttribute("listSuatChieu", listSuatChieu);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/customer_view/ShowtimesSelectView9.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/views/customer_view/MovieListView9.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nhận suất chiếu được chọn và chuyển sang sơ đồ ghế
        String suatChieuIdStr = request.getParameter("idSuatChieu");
        String phongIdStr = request.getParameter("idPhong");

        if (suatChieuIdStr != null && phongIdStr != null) {
            response.sendRedirect(request.getContextPath() + "/selectSeat?suatChieuId=" + suatChieuIdStr + "&phongId=" + phongIdStr);
        } else {
            doGet(request, response);
        }
    }
}

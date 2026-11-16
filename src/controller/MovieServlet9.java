package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MovieDAO9;
import model.ManagementStaff9;
import model.Movie9;

/**
 * Servlet điều khiển: MovieServlet9 (Java Servlet)
 * Quản lý danh sách phim, trang chủ và điều hướng trong phân hệ quản lý
 */
@WebServlet(name = "MovieServlet9", urlPatterns = {"/movie", "/movies"})
public class MovieServlet9 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MovieDAO9 movieDAO;

    @Override
    public void init() {
        movieDAO = new MovieDAO9();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        ManagementStaff9 nv = (ManagementStaff9) session.getAttribute("nv");
        if (nv == null) {
            nv = new ManagementStaff9(1, "Nguyễn Văn Quản Lý", "1990-01-15", "Hà Nội", 
                    "quanly@cinema.com", "0912345678", "admin", "admin123", "Quản lý rạp");
            session.setAttribute("nv", nv);
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "home":
                showHomePage(request, response);
                break;
            case "new":
                response.sendRedirect(request.getContextPath() + "/addMovie");
                break;
            case "list":
            default:
                listMovie(request, response);
                break;
        }
    }

    private void showHomePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/management_view/ManagementStaffHomeView9.jsp");
        dispatcher.forward(request, response);
    }

    private void listMovie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Movie9> listPhim = movieDAO.getDSPhim();
        request.setAttribute("listPhim", listPhim);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/management_view/MovieManagementView9.jsp");
        dispatcher.forward(request, response);
    }
}

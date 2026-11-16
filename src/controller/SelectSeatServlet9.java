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

import dao.SeatDAO9;
import dao.ShowtimeDAO9;
import dao.TicketDAO9;
import model.Customer9;
import model.Seat9;
import model.Showtime9;
import model.Ticket9;

/**
 * Servlet điều khiển: SelectSeatServlet9
 * Thiết kế chuẩn theo Sơ đồ gói, Thiết kế tĩnh và Sequence Diagram Pha 3
 * Xử lý tải sơ đồ ghế, lấy trạng thái vé và tạm khóa ghế (khoaGheTamThoi)
 */
@WebServlet(name = "SelectSeatServlet9", urlPatterns = {"/selectSeat", "/seatMap"})
public class SelectSeatServlet9 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SeatDAO9 seatDAO;
    private TicketDAO9 ticketDAO;
    private ShowtimeDAO9 showtimeDAO;

    @Override
    public void init() {
        seatDAO = new SeatDAO9();
        ticketDAO = new TicketDAO9();
        showtimeDAO = new ShowtimeDAO9();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String suatChieuIdStr = request.getParameter("suatChieuId");
        String phongIdStr = request.getParameter("phongId");

        if (suatChieuIdStr == null || phongIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/views/customer_view/MovieListView9.jsp");
            return;
        }

        try {
            int suatChieuId = Integer.parseInt(suatChieuIdStr);
            int phongId = Integer.parseInt(phongIdStr);

            Showtime9 suatChieu = showtimeDAO.getShowtimeById(suatChieuId);
            ArrayList<Seat9> dsGhe = seatDAO.getDSGheTheoPhong(phongId);
            ArrayList<Ticket9> dsVe = ticketDAO.getDSTrangThaiVe(suatChieuId);

            request.setAttribute("suatChieu", suatChieu);
            request.setAttribute("dsGhe", dsGhe);
            request.setAttribute("dsVe", dsVe);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/customer_view/SeatMapView9.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/views/customer_view/MovieListView9.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String suatChieuIdStr = request.getParameter("suatChieuId");
        String[] seatIdsStr = request.getParameterValues("selectedSeats");

        if (suatChieuIdStr == null || seatIdsStr == null || seatIdsStr.length == 0) {
            request.setAttribute("err", "Vui lòng chọn ít nhất một vị trí ghế ngồi!");
            doGet(request, response);
            return;
        }

        try {
            int suatChieuId = Integer.parseInt(suatChieuIdStr);
            int[] dsIdGhe = new int[seatIdsStr.length];
            for (int i = 0; i < seatIdsStr.length; i++) {
                dsIdGhe[i] = Integer.parseInt(seatIdsStr[i]);
            }

            // Gọi TicketDAO9.khoaGheTamThoi() theo Sequence Diagram Pha 3
            boolean lockOk = ticketDAO.khoaGheTamThoi(dsIdGhe, suatChieuId);
            if (!lockOk) {
                request.setAttribute("err", "Ghế bạn chọn đã có người giữ hoặc đã được đặt! Vui lòng chọn ghế khác.");
                doGet(request, response);
                return;
            }

            // Lưu thông tin giữ chỗ vào Session
            HttpSession session = request.getSession();
            session.setAttribute("booking_suatChieuId", suatChieuId);
            session.setAttribute("booking_dsIdGhe", dsIdGhe);

            // Kiểm tra trạng thái đăng nhập của khách hàng
            Customer9 kh = (Customer9) session.getAttribute("kh");
            if (kh == null) {
                // Khách hàng chưa đăng nhập -> Chuyển sang màn hình Đăng nhập
                response.sendRedirect(request.getContextPath() + "/login?redirect=payment");
            } else {
                // Đã đăng nhập -> Chuyển thẳng sang trang Thanh toán
                response.sendRedirect(request.getContextPath() + "/payment");
            }

        } catch (Exception e) {
            e.printStackTrace();
            doGet(request, response);
        }
    }
}

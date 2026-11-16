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

import dao.BillDAO9;
import dao.MemberDAO9;
import dao.ShowtimeDAO9;
import dao.TicketDAO9;
import model.Bill9;
import model.Customer9;
import model.MemberCard9;
import model.Showtime9;
import model.Ticket9;

/**
 * Servlet điều khiển: PaymentServlet9
 * Thiết kế chuẩn theo Sơ đồ gói, Thiết kế tĩnh và Sequence Diagram Pha 3 (Bước 54 -> Bước 70)
 * Xử lý tiếp nhận thanh toán, tích hợp kiểm tra thẻ thành viên, gọi BillDAO9 lưu hóa đơn và cập nhật vé
 */
@WebServlet(name = "PaymentServlet9", urlPatterns = {"/payment", "/doPayment"})
public class PaymentServlet9 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BillDAO9 billDAO;
    private TicketDAO9 ticketDAO;
    private ShowtimeDAO9 showtimeDAO;
    private MemberDAO9 memberDAO;

    @Override
    public void init() {
        billDAO = new BillDAO9();
        ticketDAO = new TicketDAO9();
        showtimeDAO = new ShowtimeDAO9();
        memberDAO = new MemberDAO9();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer suatChieuId = (Integer) session.getAttribute("booking_suatChieuId");
        int[] dsIdGhe = (int[]) session.getAttribute("booking_dsIdGhe");

        if (suatChieuId == null || dsIdGhe == null || dsIdGhe.length == 0) {
            response.sendRedirect(request.getContextPath() + "/views/customer_view/MovieListView9.jsp");
            return;
        }

        Showtime9 suatChieu = showtimeDAO.getShowtimeById(suatChieuId);
        ArrayList<Ticket9> listAllTickets = ticketDAO.getDSTrangThaiVe(suatChieuId);

        // Lọc danh sách vé thuộc các ghế đang chọn
        ArrayList<Ticket9> selectedTickets = new ArrayList<>();
        float tongTienGoc = 0.0f;
        for (Ticket9 tk : listAllTickets) {
            for (int gId : dsIdGhe) {
                if (tk.getGhe() != null && tk.getGhe().getId() == gId) {
                    selectedTickets.add(tk);
                    tongTienGoc += tk.getGia();
                    break;
                }
            }
        }

        // Tạm tính hóa đơn
        Bill9 bill = new Bill9();
        bill.setTienGoc(tongTienGoc);
        bill.setGiamGia(0.0f);
        bill.tinhThucTe();
        bill.setDsVe(selectedTickets);

        Customer9 kh = (Customer9) session.getAttribute("kh");
        if (kh == null) {
            // Mẫu khách hàng mặc định nếu demo trực tiếp
            kh = new Customer9();
            kh.setId(2);
            kh.setHoTen("Trần Khách Hàng");
        }
        bill.setKhachHang(kh);

        request.setAttribute("suatChieu", suatChieu);
        request.setAttribute("bill", bill);
        request.setAttribute("remainTime", 480); // 8 phút đếm ngược (7:59 như mockup)

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/customer_view/PaymentView9.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer suatChieuId = (Integer) session.getAttribute("booking_suatChieuId");
        int[] dsIdGhe = (int[]) session.getAttribute("booking_dsIdGhe");

        if (suatChieuId == null || dsIdGhe == null) {
            response.sendRedirect(request.getContextPath() + "/views/customer_view/MovieListView9.jsp");
            return;
        }

        String maTheTv = request.getParameter("txtMaTheTv");
        String ptt = request.getParameter("ptt");
        if (ptt == null || ptt.trim().isEmpty()) {
            ptt = "Quét mã QR chuyển khoản";
        }

        // Tải danh sách vé được chọn
        ArrayList<Ticket9> listAllTickets = ticketDAO.getDSTrangThaiVe(suatChieuId);
        ArrayList<Ticket9> selectedTickets = new ArrayList<>();
        float tongTienGoc = 0.0f;
        for (Ticket9 tk : listAllTickets) {
            for (int gId : dsIdGhe) {
                if (tk.getGhe() != null && tk.getGhe().getId() == gId) {
                    selectedTickets.add(tk);
                    tongTienGoc += tk.getGia();
                    break;
                }
            }
        }

        Customer9 kh = (Customer9) session.getAttribute("kh");
        if (kh == null) {
            kh = new Customer9();
            kh.setId(2);
            kh.setHoTen("Trần Khách Hàng");
        }

        // Đóng gói đối tượng Bill9
        Bill9 bill = new Bill9();
        bill.setTienGoc(tongTienGoc);
        bill.setPtt(ptt);
        bill.setLoai("Online");
        bill.setKhachHang(kh);
        bill.setDsVe(selectedTickets);

        // Gắn mã thẻ thành viên nếu có
        if (maTheTv != null && !maTheTv.trim().isEmpty()) {
            MemberCard9 card = new MemberCard9();
            card.setMaThe(maTheTv.trim());
            bill.setTheTV(card);
        } else if (kh.getTheTV() != null) {
            bill.setTheTV(kh.getTheTV());
        }

        // Gọi BillDAO9.luuHoaDonDatVe(bill) theo đúng Sequence Diagram Pha 3
        boolean kq = billDAO.luuHoaDonDatVe(bill);

        if (kq) {
            // Xóa session giữ chỗ
            session.removeAttribute("booking_suatChieuId");
            session.removeAttribute("booking_dsIdGhe");

            request.setAttribute("paymentSuccess", true);
            request.setAttribute("bill", bill);
            Showtime9 suatChieu = showtimeDAO.getShowtimeById(suatChieuId);
            request.setAttribute("suatChieu", suatChieu);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/customer_view/PaymentView9.jsp");
            dispatcher.forward(request, response);
        } else {
            request.setAttribute("err", "Giao dịch thanh toán hoặc lưu hóa đơn thất bại! Vui lòng thử lại.");
            doGet(request, response);
        }
    }
}

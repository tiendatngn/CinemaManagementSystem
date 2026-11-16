package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MemberDAO9;
import model.Customer9;
import model.ManagementStaff9;
import model.Member9;

/**
 * Servlet điều khiển: LoginServlet9
 * Thiết kế chuẩn theo Sơ đồ gói, Thiết kế tĩnh và Sequence Diagram Pha 3
 * Xử lý xác thực người dùng cho cả Nhân viên Quản lý và Khách hàng
 */
@WebServlet(name = "LoginServlet9", urlPatterns = {"/login", "/doLogin"})
public class LoginServlet9 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberDAO9 memberDAO;

    @Override
    public void init() {
        memberDAO = new MemberDAO9();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("logout".equalsIgnoreCase(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/common_view/LoginView9.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        String redirect = request.getParameter("redirect");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("err", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            request.getRequestDispatcher("/views/common_view/LoginView9.jsp").forward(request, response);
            return;
        }

        // Gọi MemberDAO9.xacThucDangNhap() theo Sequence diagram
        Member9 member = memberDAO.xacThucDangNhap(username, password);

        if (member == null) {
            request.setAttribute("err", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            request.getRequestDispatcher("/views/common_view/LoginView9.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        if (member instanceof ManagementStaff9) {
            session.setAttribute("nv", member);
            response.sendRedirect(request.getContextPath() + "/views/management_view/ManagementStaffHomeView9.jsp");
        } else if (member instanceof Customer9) {
            session.setAttribute("kh", member);
            if ("payment".equalsIgnoreCase(redirect)) {
                response.sendRedirect(request.getContextPath() + "/views/customer_view/PaymentView9.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/views/customer_view/CustomerHomeView9.jsp");
            }
        } else {
            // Thành viên thông thường
            session.setAttribute("user", member);
            response.sendRedirect(request.getContextPath() + "/views/customer_view/CustomerHomeView9.jsp");
        }
    }
}

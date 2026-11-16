package model;

import java.io.Serializable;

/**
 * Lớp thực thể Customer9 - Khách hàng
 * Kế thừa Member9, liên kết với MemberCard9 (theTV)
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class Customer9 extends Member9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private MemberCard9 theTV;

    public Customer9() {
        super();
    }

    public Customer9(MemberCard9 theTV) {
        super();
        this.theTV = theTV;
    }

    public Customer9(int id, String hoTen, String ngaySinh, String diaChi, String email, String sdt, String username, String password, MemberCard9 theTV) {
        super(id, hoTen, ngaySinh, diaChi, email, sdt, username, password);
        this.theTV = theTV;
    }

    public MemberCard9 getTheTV() {
        return theTV;
    }

    public void setTheTV(MemberCard9 theTV) {
        this.theTV = theTV;
    }
}

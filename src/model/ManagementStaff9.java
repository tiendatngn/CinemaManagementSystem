package model;

import java.io.Serializable;

/**
 * Lớp thực thể ManagementStaff9 - Đại diện cho Nhân viên Quản lý
 * Kế thừa từ Member9
 * Thiết kế theo Biểu đồ lớp chi tiết (Hình 3.b / image10)
 */
public class ManagementStaff9 extends Member9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private String viTri;

    public ManagementStaff9() {
        super();
    }

    public ManagementStaff9(String viTri) {
        super();
        this.viTri = viTri;
    }

    public ManagementStaff9(int id, String hoTen, String ngaySinh, String diaChi, String email, String sdt, String username, String password, String viTri) {
        super(id, hoTen, ngaySinh, diaChi, email, sdt, username, password);
        this.viTri = viTri;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
}

package model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Lớp thực thể MemberCard9 - Thẻ thành viên khách hàng
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class MemberCard9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maThe;
    private Date phatHanh;
    private int diemTL;
    private String hang;
    private String trangThai;

    public MemberCard9() {
    }

    public MemberCard9(int id, String maThe, Date phatHanh, int diemTL, String hang, String trangThai) {
        this.id = id;
        this.maThe = maThe;
        this.phatHanh = phatHanh;
        this.diemTL = diemTL;
        this.hang = hang;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaThe() {
        return maThe;
    }

    public void setMaThe(String maThe) {
        this.maThe = maThe;
    }

    public Date getPhatHanh() {
        return phatHanh;
    }

    public void setPhatHanh(Date phatHanh) {
        this.phatHanh = phatHanh;
    }

    public int getDiemTL() {
        return diemTL;
    }

    public void setDiemTL(int diemTL) {
        this.diemTL = diemTL;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

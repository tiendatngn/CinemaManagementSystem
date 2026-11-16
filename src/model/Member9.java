package model;

import java.io.Serializable;

/**
 * Lớp thực thể Member9 - Đại diện cho Thành viên hệ thống
 * Thiết kế theo Biểu đồ lớp chi tiết (Hình 3.b / image10)
 */
public class Member9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String hoTen;
    private String ngaySinh;
    private String diaChi;
    private String email;
    private String sdt;
    private String username;
    private String password;

    public Member9() {
    }

    public Member9(int id, String hoTen, String ngaySinh, String diaChi, String email, String sdt, String username, String password) {
        this.id = id;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.email = email;
        this.sdt = sdt;
        this.username = username;
        this.password = password;
    }

    public Member9(String hoTen, String ngaySinh, String diaChi, String email, String sdt, String username, String password) {
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.email = email;
        this.sdt = sdt;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

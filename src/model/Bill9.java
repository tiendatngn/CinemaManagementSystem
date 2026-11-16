package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Lớp thực thể Bill9 - Hóa đơn đặt vé xem phim
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class Bill9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Timestamp ngayLap;
    private float tienGoc;
    private float giamGia;
    private float thucTe;
    private String ptt; // Phương thức thanh toán (chuyển khoản, thẻ, qr,...)
    private String loai; // "Online", "TaiQuay"
    private Customer9 khachHang;
    private MemberCard9 theTV;
    private ArrayList<Ticket9> dsVe;
    private ManagementStaff9 nhanVien;
    private Timestamp tgHetHan;
    private String trangThai; // "Chờ thanh toán", "Đã thanh toán", "Đã hủy"

    public Bill9() {
        dsVe = new ArrayList<>();
    }

    public Bill9(int id, Timestamp ngayLap, float tienGoc, float giamGia, float thucTe, String ptt, String loai, 
                 Customer9 khachHang, MemberCard9 theTV, ArrayList<Ticket9> dsVe, ManagementStaff9 nhanVien, 
                 Timestamp tgHetHan, String trangThai) {
        this.id = id;
        this.ngayLap = ngayLap;
        this.tienGoc = tienGoc;
        this.giamGia = giamGia;
        this.thucTe = thucTe;
        this.ptt = ptt;
        this.loai = loai;
        this.khachHang = khachHang;
        this.theTV = theTV;
        this.dsVe = dsVe != null ? dsVe : new ArrayList<>();
        this.nhanVien = nhanVien;
        this.tgHetHan = tgHetHan;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public float getTienGoc() {
        return tienGoc;
    }

    public void setTienGoc(float tienGoc) {
        this.tienGoc = tienGoc;
        tinhThucTe();
    }

    public float getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(float giamGia) {
        this.giamGia = giamGia;
        tinhThucTe();
    }

    public float getThucTe() {
        return thucTe;
    }

    public void setThucTe(float thucTe) {
        this.thucTe = thucTe;
    }

    public void tinhThucTe() {
        this.thucTe = Math.max(0, this.tienGoc - this.giamGia);
    }

    public String getPtt() {
        return ptt;
    }

    public void setPtt(String ptt) {
        this.ptt = ptt;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public Customer9 getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(Customer9 khachHang) {
        this.khachHang = khachHang;
    }

    public MemberCard9 getTheTV() {
        return theTV;
    }

    public void setTheTV(MemberCard9 theTV) {
        this.theTV = theTV;
    }

    public ArrayList<Ticket9> getDsVe() {
        return dsVe;
    }

    public void setDsVe(ArrayList<Ticket9> dsVe) {
        this.dsVe = dsVe;
    }

    public ManagementStaff9 getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(ManagementStaff9 nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Timestamp getTgHetHan() {
        return tgHetHan;
    }

    public void setTgHetHan(Timestamp tgHetHan) {
        this.tgHetHan = tgHetHan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
